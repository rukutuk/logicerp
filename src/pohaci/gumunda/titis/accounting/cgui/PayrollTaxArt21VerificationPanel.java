package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.TaxArt21DeptValue;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.helper.TransactionDetailPreparingHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.DblCellEditor;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.application.StructuredTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.SearchEmployeeDlg;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21AccountPicker;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Component;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21ComponentSelectionTree;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21SubmitComponentDetail;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21SubmitEmployeeDetail;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Tariff;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class PayrollTaxArt21VerificationPanel extends PayrollVerificationPanel
		implements ActionListener {

	private static final long serialVersionUID = 1L;

	Connection m_conn = null;
	long m_sessionid = -1;
	JTextField m_statusTextField, m_submitdateTextField, m_verifinoTextField,
	/* m_verifydateTextField, */m_descriptionTextField;
	DatePicker m_verifyDate;
	JButton m_viewBt, m_verifyBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;
	private PayrollTable m_table;
	private Unit unit;
	private Vector vektor;
	public Vector m_cols = null;
	boolean m_edit = false;
	private TransactionDetail transactionDetail;
	private Vector transVector = new Vector();
	private JournalStandardSetting m_journalStandardSetting = null;
	private BaseCurrency baseCurrency = null;
	private TaxArt21AccountPicker m_taxArtAccountPicker;
	private Account taxAccount;
	private TaxArt21Submit taxSubmitted;
	Employee_n[] m_emps;
	TaxArt21SubmitEmployeeDetail[] m_empDetail;
	DefaultTableModel m_model;
	DecimalFormat m_formatDesimal;
	private HRMBusinessLogic m_hrmLogic;
	private AccountingBusinessLogic m_accLogic;

	public PayrollTaxArt21VerificationPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_hrmLogic = new HRMBusinessLogic(m_conn);
		m_accLogic = new AccountingBusinessLogic(m_conn);
		constructComponent();
		setEditable(false);
		getJournalStandardSettings();
		baseCurrency = BaseCurrency.createBaseCurrency(m_conn, m_sessionid);
	}

	private void getJournalStandardSettings() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_MASTER_DATA);

		List list = helper
				.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_TAX21);
		m_journalStandardSetting = (JournalStandardSetting) list.get(0);
	}

	void constructComponent() {
		m_excellBt = new javax.swing.JToggleButton(new ImageIcon(
				"../images/excell.gif"));
		m_excellBt.addActionListener(this);
		m_searchBt = new javax.swing.JToggleButton(new ImageIcon(
				"../images/filter2.gif"));
		m_searchBt.addActionListener(this);
		m_previewBt = new javax.swing.JToggleButton(new ImageIcon(
				"../images/refresh.gif"));
		m_previewBt.addActionListener(this);

		m_statusTextField = new JTextField();
		m_statusTextField.setEditable(false);
		// m_statusTextField.setText(EmployeePayrollSubmit.STR_STATUS1);
		m_submitdateTextField = new JTextField();
		m_submitdateTextField.setEditable(false);
		m_verifinoTextField = new JTextField();
		m_verifinoTextField.setEditable(false);
		m_verifyDate = new DatePicker();
		m_verifyDate.setDate(new java.util.Date());

		// m_verifydateTextField.setEditable(false);
		m_descriptionTextField = new JTextField();

		m_unitPicker = new UnitPicker(m_conn, m_sessionid);
		m_monthComboBox = new JMonthChooser(JMonthChooser.NO_SPINNER);

		m_yearField = new JSpinField();
		m_yearField.setMaximum(9999);
		m_yearField.setValue(new java.util.GregorianCalendar()
				.get(java.util.Calendar.YEAR));
		/*
		 * m_componentPicker = new PayrollComponentSubmitPicker(m_conn,
		 * m_sessionid, PayrollComponentTree.PAYCHEQUE_RECEIVABLES);
		 */

		m_taxArtAccountPicker = new TaxArt21AccountPicker(m_conn, m_sessionid);

		m_viewBt = new JButton("View");
		m_viewBt.addActionListener(this);
		m_verifyBt = new JButton("Verify");
		m_verifyBt.addActionListener(this);
		m_verifyBt.setEnabled(false);

		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Tax Art 21 Account");

		JLabel verifynolabel = new JLabel("Verfication No");
		JLabel verifydatelabel = new JLabel("Verfication Date");
		JLabel descriptionlabel = new JLabel("Description*");

		JPanel centerPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel formPanel = new JPanel();
		JPanel viewPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(m_excellBt);
		buttonPanel.add(m_searchBt);
		buttonPanel.add(m_previewBt);
		buttonPanel.add(m_viewBt);
		buttonPanel.add(m_verifyBt);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		formPanel.setLayout(new GridBagLayout());
		Misc.setGridBagConstraints(formPanel, submitlabel, gridBagConstraints,
				0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_submitdateTextField,
				gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, monthlabel, gridBagConstraints,
				3, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 4, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_monthComboBox,
				gridBagConstraints, 5, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, verifynolabel,
				gridBagConstraints, 0, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_verifinoTextField,
				gridBagConstraints, 2, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, yearlabel, gridBagConstraints, 3,
				1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1,
						3, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 4, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_yearField, gridBagConstraints,
				5, 1, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, statuslabel, gridBagConstraints,
				0, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_statusTextField,
				gridBagConstraints, 2, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, unitlabel, gridBagConstraints, 3,
				2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1,
						3, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 4, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_unitPicker, gridBagConstraints,
				5, 2, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, verifydatelabel,
				gridBagConstraints, 0, 3, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 3, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_verifyDate, gridBagConstraints,
				2, 3, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, viewPanel, gridBagConstraints, 3,
				3, GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER,
				1, 1.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, descriptionlabel,
				gridBagConstraints, 0, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_descriptionTextField,
				gridBagConstraints, 2, 4, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1,
						1));

		viewPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(viewPanel, viewlabel, gridBagConstraints, 0,
				0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1,
						1, 1, 1));

		Misc.setGridBagConstraints(viewPanel, new JLabel(" "),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(viewPanel, m_taxArtAccountPicker,
				gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, new Insets(1, 1, 1, 1));

		northPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		JPanel northleftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northleftPanel.add(northPanel);
		Misc.setGridBagConstraints(northPanel, buttonPanel, gridBagConstraints,
				0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 1, 1, 1));

		formPanel.setPreferredSize(new Dimension(600, 120));
		Misc.setGridBagConstraints(northPanel, formPanel, gridBagConstraints,
				0, 1, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(northPanel, new JPanel(),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1,
						1));

		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEtchedBorder());

		m_table = new PayrollTable(new DefaultMutableTreeNode("Column"));
		centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);

		setLayout(new BorderLayout());
		add(northleftPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}

	public void setEditable(boolean editable) {
		m_searchBt.setEnabled(editable);
		m_excellBt.setEnabled(editable);
		m_previewBt.setEnabled(editable);
	}

	public void actionPerformed(ActionEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == m_viewBt) {
			m_cekEmployee = false;
			presentingSubmittedData();
			setEditable(true);
		} else if (e.getSource() == m_verifyBt) {
			dataVerification();
		} else if (e.getSource() == m_excellBt) {
			jaspertReport(false);
		} else if (e.getSource() == m_searchBt) {
			SearchEmployeeDlg dlg = new SearchEmployeeDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), this,
					m_conn, m_sessionid);
			dlg.setVisible(true);
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	void jaspertReport(boolean check) {
		int month = m_monthComboBox.getMonth();
		int year = m_yearField.getValue();
		Unit unit = (Unit) m_unitPicker.getObject();
		String tgl = getMount(month) + " - " + year;
		if (unit != null) {
			month = month + 1;
			new pohaci.gumunda.titis.hrm.cgui.report.HRMPayrollTaxArt21Jasper(
					this, unit.getDescription(), tgl, m_empDetail, check);
		} else {
			JOptionPane.showMessageDialog(this, "Define unit", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public String getMount(int mount) {
		String m_mount = "";
		switch (mount) {
		case 0:
			m_mount = "January";
			break;
		case 1:
			m_mount = "February";
			break;
		case 2:
			m_mount = "March";
			break;
		case 3:
			m_mount = "April";
			break;
		case 4:
			m_mount = "May";
			break;
		case 5:
			m_mount = "June";
			break;
		case 6:
			m_mount = "July";
			break;
		case 7:
			m_mount = "August";
			break;
		case 8:
			m_mount = "September";
			break;
		case 9:
			m_mount = "October";
			break;
		case 10:
			m_mount = "November";
			break;
		case 11:
			m_mount = "December";
			break;
		}
		return m_mount;
	}

	boolean m_cekReport = false;

	public void tableBodyReport(DefaultTableModel model, boolean excel) {
		if (excel)
			m_formatDesimal = new DecimalFormat("###.00");
		else
			m_formatDesimal = new DecimalFormat("#,##0.00");
		m_model = model;
		m_cekReport = true;
		presentingSubmittedData();
	}

	private void dataVerification() {
		Calendar cal = Calendar.getInstance();
		Date date = new Date(cal.getTime().getTime());
		if (m_verifyDate.getDate() != null)
			date = m_verifyDate.getDate();
		java.util.ArrayList list = new java.util.ArrayList();
		String description = m_descriptionTextField.getText();
		if (description.length() == 0)
			list.add("Description");
		// Unit unit = m_unitPicker.getUnit();
		if (unit == null)
			list.add("Unit");
		if (taxAccount == null)
			list.add("Tax Art 21 Account");
		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if (exception.length > 0) {
			for (int i = 0; i < exception.length; i++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		// refNo
		ReferenceNoGeneratorHelper helper = new ReferenceNoGeneratorHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_MASTER_DATA, "");
		String refNo = helper.createPayrollReferenceNo(m_journalStandardSetting
				.getJournalStandard().getCode(), date);
		JournalStandard journalStandard = m_journalStandardSetting
				.getJournalStandard();
		Journal journal = m_journalStandardSetting.getJournalStandard()
				.getJournal();

		Transaction transaction = new Transaction(description, date, date,
				null, refNo, journal, journalStandard,
				(short) PayrollSubmitStatus.VERIFIED, unit);
		TransactionDetail[] transDetail = getTransactionDetail();

		// transDetail sudah sesuai
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		transDetail = transHelper.prepareJournalStandardTransactionDetail(
				m_journalStandardSetting.getJournalStandard()
						.getJournalStandardAccount(), transDetail, unit,
				baseCurrency, 1);

		try {
			/*
			 * transaction = m_accLogic.createTransactionData(m_sessionid,
			 * IDBConstants.MODUL_MASTER_DATA, transaction, transDetail);
			 *
			 */
			taxSubmitted.setStatus(PayrollSubmitStatus.VERIFIED);
			m_accLogic.createTaxTransactionData(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, transaction, transDetail,
					taxSubmitted);


			/*
			 * m_hrmLogic.updateTaxArt21Submit(m_sessionid,
			 * IDBConstants.MODUL_MASTER_DATA, transaction, taxSubmitted);
			 */

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		m_statusTextField.setText(" Verified");
		m_verifyDate.setDate(date);
		m_verifinoTextField.setText(" " + refNo);
		m_verifyBt.setEnabled(false);
		setDeptTotalValue();

	}

	public void setDeptTotalValue() {
		try {
			HashMap map = new HashMap();
			Iterator iterator = getMaperDept();
			while (iterator.hasNext()) {
				ArrayList ltax21list = new ArrayList();
				Organization org = (Organization) iterator.next();
				Taxart21[] taxart21 = (Taxart21[]) tax21list
						.toArray(new Taxart21[tax21list.size()]);
				for (int i = 0; i < taxart21.length; i++) {
					if (!map.containsKey(new Long(taxart21[i].getEmpdetail()
							.getAutoindex()))) {
						TaxArt21SubmitEmployeeDetail empdetail = taxart21[i]
								.getEmpdetail();
						Employment[] employement = m_hrmLogic
								.getEmployeeEmployment(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										empdetail.getEmployee().getAutoindex());
						if (employement != null) {
							if (org.getIndex() == employement[0]
									.getDepartment().getIndex()) {
								Taxart21 ltaxart21 = new Taxart21();
								ltaxart21.setEmpdetail(empdetail);
								ltaxart21.setAcc(taxart21[i].getAcc());
								ltaxart21.setValue(taxart21[i].getValue());
								ltax21list.add(ltaxart21);
								map.put(taxart21[i], new Long(taxart21[i]
										.getEmpdetail().getAutoindex()));
							}
						}
					}
				}

				TaxArt21DeptValue taxArt21DeptValue = new TaxArt21DeptValue();
				taxArt21DeptValue.setDepartment(org);
				taxArt21DeptValue.setTaxArt21Submit(taxSubmitted);
				if (ltax21list.size() > 0) {
					Taxart21[] ltaxt21 = (Taxart21[]) ltax21list
							.toArray(new Taxart21[ltax21list.size()]);
					for (int i = 0; i < ltaxt21.length; i++) {
						if (ltaxt21[i].getValue() > 0) {
							taxArt21DeptValue.setAccount(ltaxt21[i].getAcc());
							taxArt21DeptValue
									.setAccvalue(ltaxt21[i].getValue());
							m_accLogic
									.createTaxArtDeptValue(
											m_sessionid,
											pohaci.gumunda.titis.accounting.dbapi.IDBConstants.MODUL_ACCOUNTING,
											taxArt21DeptValue);
						}
					}
				}
				/*
				 * while(i.hasNext()){ Map.Entry me = (Map.Entry)i.next();
				 * TaxArt21SubmitEmployeeDetail empdetail =
				 * (TaxArt21SubmitEmployeeDetail)me.getKey(); //double val =
				 * ((Double)me.getValue()).doubleValue(); Employment[]
				 * employement =
				 * m_hrmLogic.getEmployeeEmployment(m_sessionid,IDBConstants.MODUL_MASTER_DATA,empdetail.getEmployee().getAutoindex());
				 * if (employement!=null){ if (org.getIndex() ==
				 * employement[0].getDepartment().getIndex()){
				 * map.put(empdetail,me.getValue());
				 * map.put(empdetail,me.getValue()); } } }
				 *
				 * TaxArt21DeptValue taxArt21DeptValue = new
				 * TaxArt21DeptValue(); taxArt21DeptValue.setDepartment(org);
				 * taxArt21DeptValue.setTaxArt21Submit(taxSubmitted); set =
				 * map.entrySet(); i = set.iterator(); while(i.hasNext()){
				 * Map.Entry me = (Map.Entry)i.next();
				 * TaxArt21SubmitEmployeeDetail empdetail =
				 * (TaxArt21SubmitEmployeeDetail)me.getKey(); double val =
				 * ((Double)me.getValue()).doubleValue();
				 * TaxArt21SubmitComponentDetail[] componentDetails =
				 * empdetail.getComponentDetails(); for (int j =0;j<componentDetails.length;j++){
				 *  }
				 * //taxArt21DeptValue.setAccount(empdetail.getComponentDetails().)
				 *  }
				 */
				/*
				 * EmployeeMealAllowanceSubmit[] emps =
				 * (EmployeeMealAllowanceSubmit[]) dataList.toArray(new
				 * EmployeeMealAllowanceSubmit[dataList.size()]); if
				 * (emps.length>0){ PayrollColumnHelper helper = new
				 * PayrollColumnHelper();
				 * helper.prepareColumnHelper(payrollComponentsDefault);
				 * PayrollDeptValue payrollDeptValue = new PayrollDeptValue();
				 * payrollDeptValue.setOrganization(org);
				 * payrollDeptValue.setEmployeePayrollSubmit(emps[0]); for (int
				 * i=0;i<emps.length;i++){ if (emps[i].getValue()>0){
				 * PayrollComponent payrollComponent =
				 * helper.getPayrollComponent(emps[i].getPayrollComponentIndex());
				 * payrollDeptValue.setAccount(payrollComponent.getAccount());
				 * payrollDeptValue.setAccvalue(emps[i].getValue());
				 * m_accLogic.createPayrollDeptValue(m_sessionid,pohaci.gumunda.titis.accounting.dbapi.IDBConstants.MODUL_ACCOUNTING,payrollDeptValue); } } }
				 */}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Iterator getMaperDept() {
		GenericMapper mapper = MasterMap.obtainMapperFor(Organization.class);
		mapper.setActiveConn(m_conn);
		String selectWhere = " 1=1 ";
		List list = mapper.doSelectWhere(selectWhere);
		Iterator iterator = list.iterator();
		return iterator;
	}

	private TransactionDetail[] getTransactionDetail() {
		TransactionDetail[] transDetail = new TransactionDetail[transVector
				.size()];
		transVector.copyInto(transDetail);
		return transDetail;
	}

	protected void presentingSubmittedData() {
		taxAccount = (Account) m_taxArtAccountPicker.getObject();
		TaxArt21Component[] taxArt21Components = getTaxArt21ComponentsByAccount(taxAccount);

		java.util.ArrayList list = new java.util.ArrayList();
		unit = (Unit) m_unitPicker.getObject();
		if (unit == null)
			list.add("Unit");
		String strexc = "Please insert :\n";
		if (taxAccount == null)
			list.add("Tax Art 21 Account");
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if (exception.length > 0) {
			for (int i = 0; i < exception.length; i++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		tableHeader(taxArt21Components);

		taxSubmitted = getTaxArt21Submit(getMonth(), getYear(), unit,
				taxAccount);

		if (taxSubmitted != null) {
			if (taxSubmitted.getStatus() >= PayrollSubmitStatus.SUBMITTED) {

				setTextfieldStatus(taxSubmitted.getStatus(), taxSubmitted);
			} else {
				System.err.println("STATUS TIDAK BERUBAH!!!");
			}
		} else {
			m_verifyBt.setEnabled(false);
			m_statusTextField.setText(" Not Submitted");
			m_submitdateTextField.setText("");
		}

		if (taxSubmitted != null)
			presentingData(taxSubmitted, taxArt21Components);

		// NUNG, KALO LU PAKE BARANG INI KASIH TAU GUA YA.. IRWAN
		// preparingData(transVector);

	}

	boolean m_cekEmployee = false;

	public void searchPresentingSubmittedData(String[] attr, String operator) {
		try {
			String date = m_yearField.getValue() + "-"
					+ (m_monthComboBox.getMonth() + 1) + "-1";
			m_emps = getEmployeeBy_Criteria(date, unit.getIndex(), attr,
					operator);
			m_cekEmployee = true;
			presentingSubmittedData();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// JANGAN DIHAPUS
	/*
	 * private void preparingData(Vector transactionVector) { double value = 0;
	 *
	 * transactionDetail = null;
	 *
	 * if(transactionVector.size()>0){ TransactionDetail[] details =
	 * (TransactionDetail[]) transactionVector.toArray(new
	 * TransactionDetail[transactionVector.size()]); for(int i=0; i<details.length;
	 * i++){ value += details[i].getValue(); }
	 *
	 * TransactionDetail detail = details[0]; detail.setValue(value);
	 *
	 * transactionDetail = detail; }
	 *
	 * transVector = new Vector(); transVector.addElement(transactionDetail); }
	 */

	private TaxArt21Submit getTaxArt21Submit(int month, int year,
			Unit unitSelected, Account accountSelected) {
		try {
			return m_hrmLogic.getFullTaxArt21Submit(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, month, year, unitSelected,
					accountSelected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private TaxArt21Component[] getTaxArt21ComponentsByAccount(Account account) {
		try {
			TaxArt21Component[] components = m_hrmLogic
					.getTaxArt21ComponentsByAccount(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA, account.getIndex());

			return components;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	String[] rpt = { "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	String[] rptTtl = { "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "" };

	private void presentingData(TaxArt21Submit taxSubmitted,
			TaxArt21Component[] taxArt21Components) {

		PayrollColumnHelper helper = new PayrollColumnHelper();
		int no = 0;
		int leftMargin = 0;
		helper.prepareColumnHelper(taxArt21Components);
		vektor = new Vector();
		transVector.clear();

		TaxArt21SubmitEmployeeDetail[] employeeDetails = taxSubmitted
				.getEmployeeDetails();
		if (m_cekEmployee) {
			employeeDetails = sortedDetails(employeeDetails);
		}
		// bandingkan jika null;
		m_empDetail = employeeDetails;

		double[] ttl = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0 };
		/*
		 * map = new HashMap(); mapEmployee = new HashMap();
		 */
		tax21list = new ArrayList();
		for (int i = 0; i < employeeDetails.length; i++) {
			if (employeeDetails[i] != null) {
				TaxArt21SubmitEmployeeDetail empDetail = employeeDetails[i];

				vektor = new Vector();

				tableContent(employeeDetails[i], no++);
				leftMargin = vektor.size();
				// row++; // i add this

				TaxArt21SubmitComponentDetail[] componentDetails = employeeDetails[i]
						.getComponentDetails();

				double monthly = 0;
				TaxArt21SubmitComponentDetail componentDetail = null;
				for (int j = 0; j < componentDetails.length; j++) {
					componentDetail = componentDetails[j];
					int colIdx = helper.getColumnIndex(componentDetail
							.getComponent().getIndex());

					if (colIdx != -1) {
						Double value = componentDetail.getValue();
						monthly += value.doubleValue();
						Object content = null;

						if (value != null) {
							content = value;
							// TaxArt21Component taxComp =
							// componentDetail.getComponent();

						} else {
							content = "";
						}
						helper.addDataAtColumn(vektor, colIdx + leftMargin,
								content);
					}
				}
				if (vektor.size() > 0) {
					vektor = addTaxElement(monthly, vektor, empDetail,
							componentDetail.getComponent().getAccount());
					m_table.addRow(vektor);
				}
			}

			if (m_cekReport) {
				getReport(helper, employeeDetails, ttl, i);
			}
		}
	}

	private TaxArt21SubmitEmployeeDetail[] sortedDetails(
			TaxArt21SubmitEmployeeDetail[] employeeDetails) {
		List list = new ArrayList();
		for (int i = 0; i < employeeDetails.length; i++) {
			if (findEmp(employeeDetails[i]) != null)
				list.add(employeeDetails[i]);
		}
		TaxArt21SubmitEmployeeDetail[] details = (TaxArt21SubmitEmployeeDetail[]) list
				.toArray(new TaxArt21SubmitEmployeeDetail[list.size()]);
		return details;
	}

	private Employee_n findEmp(TaxArt21SubmitEmployeeDetail employeeDetail) {
		for (int i = 0; i < m_emps.length; i++) {
			if (m_emps[i].getAutoindex() == employeeDetail.getEmployee()
					.getAutoindex())
				return m_emps[i];
		}
		return null;
	}

	private void getReport(PayrollColumnHelper helper,
			TaxArt21SubmitEmployeeDetail[] employeeDetails, double[] ttl, int i) {
		TaxArt21SubmitEmployeeDetail empDetail = employeeDetails[i];
		int n = 0;
		rpt[n++] = String.valueOf(i + 1);
		rpt[n++] = employeeDetails[i].getEmployee().getEmployeeno();
		rpt[n++] = employeeDetails[i].getEmployee().getFirstname() + " "
				+ employeeDetails[i].getEmployee().getMidlename() + " "
				+ employeeDetails[i].getEmployee().getLastname();
		rpt[n++] = employeeDetails[i].getJobTitle();
		rpt[n++] = "";

		TaxArt21SubmitComponentDetail[] componentDetails = employeeDetails[i]
				.getComponentDetails();

		double monthly = 0;
		for (int j = 0; j < componentDetails.length; j++) {
			TaxArt21SubmitComponentDetail componentDetail = componentDetails[j];
			int colIdx = helper.getColumnIndex(componentDetail.getComponent()
					.getIndex());

			if (colIdx != -1) {
				Double value = componentDetail.getValue();
				monthly += value.doubleValue();
				if (value != null) {
					String data = m_formatDesimal.format(value.doubleValue());
					if (value.doubleValue() < 0)
						data = "("
								+ m_formatDesimal.format(-value.doubleValue())
								+ ")";
					rpt[n++] = data;

					ttl[j] += value.doubleValue();
					rptTtl[j] = m_formatDesimal.format(ttl[j]);
					if (ttl[j] < 0)
						rptTtl[j] = "(" + m_formatDesimal.format(-ttl[j]) + ")";

					// TaxArt21Component taxComp =
					// componentDetail.getComponent();
				} else {
					rpt[n++] = "";
				}
			}
		}

		addTaxElementReport(monthly, empDetail.getEmployee().getAutoindex(),
				ttl, n);
		m_model.addRow(new Object[] { rpt[0], rpt[1], rpt[2], rpt[3], rpt[4],
				rpt[5], rpt[6], rpt[7], rpt[8], rpt[9], rpt[10], rpt[11],
				rpt[12], rpt[13], rpt[14], rpt[15], rpt[16], rpt[17], rpt[18],
				rpt[19], rpt[20], rpt[21], rpt[22], rpt[23], rpt[24], rpt[25],
				rpt[26], rpt[27], new Integer(1) });
		if (i == (employeeDetails.length - 1)) {
			m_model.addRow(new Object[] { "", "", "", "Total Amount (IDR)", "",
					rptTtl[0], rptTtl[1], rptTtl[2], rptTtl[3], rptTtl[4],
					rptTtl[5], rptTtl[6], rptTtl[7], rptTtl[8], rptTtl[9],
					rptTtl[10], rptTtl[11], rptTtl[12], rptTtl[13], rptTtl[14],
					rptTtl[15], rptTtl[16], rptTtl[17], rptTtl[18], rptTtl[19],
					rptTtl[20], rptTtl[21], rptTtl[22], new Integer(2) });
			m_cekReport = false;
		}
	}

	private void addTaxElementReport(double monthly, long autoindex,
			double[] ttl, int n) {

		String data = m_formatDesimal.format(monthly);
		if (monthly < 0)
			data = "(" + m_formatDesimal.format(-monthly) + ")";
		rpt[n++] = data;
		ttl[16] += monthly;
		rptTtl[16] = m_formatDesimal.format(ttl[16]);
		if (ttl[16] < 0)
			rptTtl[16] = "(" + m_formatDesimal.format(-ttl[16]) + ")";

		double yearly = monthly * 12;
		data = m_formatDesimal.format(yearly);
		if (yearly < 0)
			data = "(" + m_formatDesimal.format(-yearly) + ")";
		rpt[n++] = data;
		ttl[17] += yearly;
		rptTtl[17] = m_formatDesimal.format(ttl[17]);
		if (ttl[17] < 0)
			rptTtl[17] = "(" + m_formatDesimal.format(-ttl[17]) + ")";

		double ptkp = getYearlyPTKP(autoindex);
		data = m_formatDesimal.format(ptkp);
		if (ptkp < 0)
			data = "(" + m_formatDesimal.format(-ptkp) + ")";
		rpt[n++] = data;
		ttl[18] += ptkp;
		rptTtl[18] = m_formatDesimal.format(ttl[18]);
		if (ttl[18] < 0)
			rptTtl[18] = "(" + m_formatDesimal.format(-ttl[18]) + ")";

		double pkp = yearly - ptkp;
		if (pkp < 0)
			pkp = 0;
		data = m_formatDesimal.format(pkp);
		if (pkp < 0)
			data = "(" + m_formatDesimal.format(-pkp) + ")";
		rpt[n++] = data;
		ttl[19] += pkp;
		rptTtl[19] = m_formatDesimal.format(ttl[19]);
		if (ttl[19] < 0)
			rptTtl[19] = "(" + m_formatDesimal.format(-ttl[19]) + ")";

		NumberRounding rounding = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND_DOWN, -3);
		double roundingValue = rounding.round(pkp);
		data = m_formatDesimal.format(roundingValue);
		if (roundingValue < 0)
			data = "(" + m_formatDesimal.format(-roundingValue) + ")";
		rpt[n++] = data;
		ttl[20] += roundingValue;
		rptTtl[20] = m_formatDesimal.format(ttl[20]);
		if (ttl[20] < 0)
			rptTtl[20] = "(" + m_formatDesimal.format(-ttl[20]) + ")";

		TaxArt21Tariff[] tariffs = getTariffs();
		double monthlyTax = getYearlyTax(roundingValue, tariffs);
		data = m_formatDesimal.format(monthlyTax);
		if (monthlyTax < 0)
			data = "(" + m_formatDesimal.format(-monthlyTax) + ")";
		rpt[n++] = data;
		ttl[21] += monthlyTax;
		rptTtl[21] = m_formatDesimal.format(ttl[21]);
		if (ttl[21] < 0)
			rptTtl[21] = "(" + m_formatDesimal.format(-ttl[21]) + ")";

		double yearlyTax = monthlyTax / 12;
		data = m_formatDesimal.format(yearlyTax);
		if (yearlyTax < 0)
			data = "(" + m_formatDesimal.format(-yearlyTax) + ")";
		rpt[n++] = data;
		ttl[22] += yearlyTax;
		rptTtl[22] = m_formatDesimal.format(ttl[22]);
		if (ttl[22] < 0)
			rptTtl[22] = "(" + m_formatDesimal.format(-ttl[22]) + ")";

		transactionDetail = new TransactionDetail(taxAccount, yearlyTax,
				baseCurrency, 1, unit, -1);
		transVector.add(transactionDetail);
	}

	/*
	 * HashMap map; HashMap mapEmployee;
	 */
	ArrayList tax21list;

	private Vector addTaxElement(double monthly, Vector dataVector,
			TaxArt21SubmitEmployeeDetail empdetail, Account acc) {
		long autoindex = empdetail.getEmployee().getAutoindex();
		dataVector.addElement(new Double(monthly));
		double yearly = monthly * 12;
		dataVector.addElement(new Double(yearly));

		double ptkp = getYearlyPTKP(autoindex);
		dataVector.addElement(new Double(ptkp));

		double pkp = yearly - ptkp;
		if (pkp < 0)
			pkp = 0;
		dataVector.addElement(new Double(pkp));

		NumberRounding rounding = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND_DOWN, -3);
		double roundingValue = rounding.round(pkp);
		dataVector.addElement(new Double(roundingValue));

		TaxArt21Tariff[] tariffs = getTariffs();
		double yearlyTax = getYearlyTax(roundingValue, tariffs);
		dataVector.addElement(new Double(yearlyTax));

		double monthlyTax = yearlyTax / 12;
		dataVector.addElement(new Double(monthlyTax));

		Taxart21 taxart21 = new Taxart21();
		taxart21.setEmpdetail(empdetail);
		taxart21.setAcc(acc);
		taxart21.setValue(monthlyTax);
		tax21list.add(taxart21);
		transactionDetail = new TransactionDetail(taxAccount, monthlyTax,
				baseCurrency, 1, unit, -1);
		transVector.add(transactionDetail);

		return dataVector;
	}

	private TaxArt21Tariff[] getTariffs() {
		TaxArt21Tariff[] tariffs = null;
		try {
			tariffs = m_hrmLogic.getAllTaxArt21Tariff(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tariffs;
	}

	private double getYearlyTax(double value, TaxArt21Tariff[] tariffs) {
		double residue = value;
		double tax = 0;
		double taxed = 0;

		for (int i = 0; i < tariffs.length; i++) {
			TaxArt21Tariff tariff = tariffs[i];

			double range = tariff.getMaximum() - tariff.getMinimum() + 1;

			if (residue > range) {
				taxed = range;
			} else {
				taxed = residue;
			}
			residue -= taxed;

			double pctTariff = tariff.getTariff();

			tax += (taxed * (pctTariff / 100));
		}
		return tax;
	}

	private double getYearlyPTKP(long autoindex) {
		HRMSQLSAP sql = new HRMSQLSAP();
		double value = 0;
		try {
			value = sql.getPTKPbyEmployeeIndex(m_conn, autoindex);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	/*
	 * private void setEnabledForReceivablesPayrollComponent(
	 * PayrollColumnHelper helper, PayrollComponent[] components) { m_cols = new
	 * Vector(); for (int i = 0; i < components.length; i++) { if
	 * ((components[i].getPaymentAsString() == PayrollComponent.PAYCHEQUE) &&
	 * (components[i].getSubmitAsString() ==
	 * PayrollComponent.EMPLOYEE_RECEIVABLES)) m_cols.add(new
	 * Integer(helper.getColumnIndex(components[i] .getIndex()))); } //
	 * System.out.println("done"); }
	 */

	private void tableContent(TaxArt21SubmitEmployeeDetail detail, int i) {
		vektor.addElement(String.valueOf(i + 1));
		Employee_n emp = detail.getEmployee();
		String name = emp.getFirstname() + " " + emp.getMidlename() + " "
				+ emp.getLastname();
		vektor.addElement(name); // menampilkan name
		vektor.addElement(detail.getJobTitle());

	}

	private void setTextfieldStatus(int status, TaxArt21Submit taxSubmitted) {
		if (status == PayrollSubmitStatus.NOTSUBMITTED) {
			m_statusTextField.setText(" Not Submitted");
			m_submitdateTextField.setText("");
			// m_verifyDate.setDate(null);
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(false);
		} else if (status == PayrollSubmitStatus.SUBMITTED) {
			m_statusTextField.setText(" Submitted");
			m_submitdateTextField.setText(" "
					+ taxSubmitted.getSubmittedDate().toString());
			// m_verifyDate.setDate(null);
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(true);
		} else if (status == PayrollSubmitStatus.VERIFIED) {
			m_statusTextField.setText(" Verified");
			m_submitdateTextField.setText(" "
					+ taxSubmitted.getSubmittedDate().toString());
			Transaction trans = taxSubmitted.getTransaction();
			// m_verifyDate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" " + trans.getDescription());
			m_verifinoTextField.setText(" " + trans.getReference());
			m_verifyBt.setEnabled(false);
		} else if (status == PayrollSubmitStatus.POSTED) {
			m_statusTextField.setText(" Posted");
			m_submitdateTextField.setText(" "
					+ taxSubmitted.getSubmittedDate().toString());
			Transaction trans = taxSubmitted.getTransaction();
			// m_verifyDate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" " + trans.getDescription());
			m_verifinoTextField.setText(" " + trans.getReference());
			m_verifyBt.setEnabled(false);
		}
	}

	private void tableHeader(TaxArt21Component[] taxArt21Components) {
		TaxArt21ComponentSelectionTree plateTree = new TaxArt21ComponentSelectionTree(
				m_conn, m_sessionid, taxArt21Components);
		if (taxArt21Components.length > 0) {
			DefaultMutableTreeNode rootTable = (DefaultMutableTreeNode) m_table
					.getTableHeaderRoot();
			rootTable.removeAllChildren();
			m_table.resetDefaultColumn();
			rootTable.add((DefaultMutableTreeNode) plateTree.getModel()
					.getRoot());
			m_table.rebuildTable();
			m_table.clearRow();
		}
	}

	public int getMonth() {
		return m_monthComboBox.getMonth() + 1;
	}

	public int getYear() {
		return m_yearField.getValue();
	}

	// private Employee_n[] getEmployee(String date, long unitId) throws
	// Exception{
	// HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
	// Employee_n[] emps = logic.getEmployeeBy_unit(m_sessionid,
	// IDBConstants.MODUL_MASTER_DATA, unitId, date);
	// return emps;
	// }

	// private PayrollCategoryComponent[] getPayrollCategoryComponents
	// (Employee_n emp, PayrollComponent[] components) throws Exception{
	//
	// HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
	// PayrollCategoryComponent[] comps =
	// logic.getSelectedPayrollCategoryComponent(m_sessionid,
	// IDBConstants.MODUL_MASTER_DATA, emp.getAutoindex(), components);
	// return comps;
	// }

	class PayrollTable extends StructuredTable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public TableCellEditor getCellEditor(int row, int column) {
			if (findCol(column))
				return new DblCellEditor();
			return super.getCellEditor(row, column);
		}

		public boolean isCellEditable(int row, int column) {
			return (m_edit && findCol(column));
			// return super.isCellEditable(row, column);
		}

		private boolean findCol(int col) {
			if (m_cols != null) {
				for (Enumeration e = m_cols.elements(); e.hasMoreElements();)
					if (((Integer) e.nextElement()).intValue() + 3 == col) {
						System.out.println("get True for " + col);
						return true;
					}
			}
			return false;
		}

		public PayrollTable(DefaultMutableTreeNode root) {
			super(root);
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			setModel(new PayrollTableModel());
		}

		public void resetDefaultColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("No"));
			root.add(new DefaultMutableTreeNode("Name"));
			root.add(new DefaultMutableTreeNode("Job Title"));
		}

		public void setModelKu() {
			// setModel(m_tableModel);

			Enumeration enumeration = getColumnModel().getColumns();
			while (enumeration.hasMoreElements()) {
				TableColumn column = (TableColumn) enumeration.nextElement();
				column.setPreferredWidth(100);
				// column.setMinWidth(50);
				// column.setMaxWidth(50);
			}

			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);

		}

		public void setColumnSize() {
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);
		}

		public void addRow(Vector data) {
			((DefaultTableModel) getModel()).addRow(data);
		}

		public void clearRow() {
			((DefaultTableModel) getModel()).setRowCount(0);
		}

		public TableCellRenderer getCellRenderer(int row, int col) {
			if (col == 0)
				return new RightAllignmentCellRenderer();
			else if (col > 2)
				return new DoubleCellRenderer(JLabel.RIGHT);
			return super.getCellRenderer(row, col);
		}
	}

	private Employee_n[] getEmployeeBy_Criteria(String date, long unitId,
			String[] attr, String operator) throws Exception {

		String data = "";
		String item = "";
		if (!attr[0].equals("")) {
			if (data.equals(""))
				item = "";
			else
				item = operator;
			data += item
					+ "UPPER(emp.firstname& ' ' & emp.midlename& ' ' & emp.lastname) "
					+ "LIKE '%" + attr[0].toUpperCase() + "%' ";
		}
		if (!attr[1].equals("")) {
			if (data.equals(""))
				item = "";
			else
				item = operator;
			data += item + " UPPER(job.name) like '%" + attr[1].toUpperCase()
					+ "%' ";
		}
		data = "and (" + data + ")";
		if (attr[0].equals("") || attr[0].equals(""))
			data = " ";

		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,"
				+ "job.name as jobtitle, ptkp.name as ptkp "
				+ "from employee emp "
				+ "inner join "
				+ "(select e.* from employeeemployment e, "
				+ "(select employee, max(tmt) tmt from (select *   from employeeemployment where tmt<'"
				+ date
				+ "') "
				+ "group by employee ) lastemp "
				+ "where e.employee=lastemp.employee "
				+ "and e.tmt=lastemp.tmt and unit="
				+ unitId
				+ ") employment "
				+ "on emp.autoindex=employment.employee "
				+ "inner join jobtitle job on employment.jobtitle=job.autoindex "
				+ "inner join ptkp on emp.art21=ptkp.autoindex "
				+ "where emp.autoindex NOT IN( SELECT employee FROM employeeretirement WHERE tmt<'"
				+ date + "') " + data + " order by emp.employeeno";
		System.err.println(query);
		Employee_n[] emps = m_hrmLogic.getEmployeeAndPTKPByCriteria(
				m_sessionid, IDBConstants.MODUL_MASTER_DATA, query);
		return emps;
	}

	class PayrollTableModel extends DefaultTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {

			return false;
		}

	}

	class RightAllignmentCellRenderer extends DefaultTableCellRenderer {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setText((value == null) ? "    " : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);

			return this;
		}
	}

	class Taxart21 {
		TaxArt21SubmitEmployeeDetail empdetail;
		Account acc;
		double value;

		public Taxart21() {
		}

		public Account getAcc() {
			return acc;
		}

		public void setAcc(Account acc) {
			this.acc = acc;
		}

		public TaxArt21SubmitEmployeeDetail getEmpdetail() {
			return empdetail;
		}

		public void setEmpdetail(TaxArt21SubmitEmployeeDetail empdetail) {
			this.empdetail = empdetail;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
	}
}
