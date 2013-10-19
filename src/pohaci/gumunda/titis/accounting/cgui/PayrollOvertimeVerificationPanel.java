package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

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
import java.text.SimpleDateFormat;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.jasperreports.engine.JRPrintPage;
import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.titis.accounting.entity.PayrollDeptValue;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.helper.TransactionDetailPreparingHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.application.StructuredTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.EmployeeOvertimeSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSelectionTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSubmitPicker;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollSubmitType;
import pohaci.gumunda.titis.hrm.cgui.report.RptSummaryOvertime;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class PayrollOvertimeVerificationPanel extends PayrollVerificationPanel
		implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;

	JTextField m_statusTextField, m_submitdateTextField, m_verifinoTextField,
	/* m_verifydateTextField, */m_descriptionTextField;
	DatePicker m_verifyDate;
	JButton m_viewBt, m_submitBt, m_saveBt, m_cancelBt, m_calculateBt,
			m_verifyBt, m_summaryBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;
	private PayrollTable m_table;
	private EmployeeOvertimeSubmit[] employeeOvertimeSubmit;
	private Vector m_data;
	private Unit unit;
	private TransactionDetail transactionDetail;
	private Vector transVector = new Vector();
	private JournalStandardSetting m_journalStandardSetting;
	private BaseCurrency baseCurrency;
	private HRMBusinessLogic m_hrmLogic;
	private AccountingBusinessLogic m_accLogic;

	public PayrollOvertimeVerificationPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_hrmLogic = new HRMBusinessLogic(m_conn);
		m_accLogic = new AccountingBusinessLogic(m_conn);
		try {
			new HRMSQLSAP().getDefaultWorkingDay(m_conn);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		constructComponent();
		getJournalStandardSettings();
		baseCurrency = BaseCurrency.createBaseCurrency(m_conn, m_sessionid);
	}

	private void getJournalStandardSettings() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_MASTER_DATA);

		List list = helper
				.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_OVERTIME);
		m_journalStandardSetting = (JournalStandardSetting) list.get(0);
	}

	void constructComponent() {
		m_excellBt = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_excellBt.addActionListener(this);
		m_searchBt = new JToggleButton(new ImageIcon("../images/refresh.gif"));
		m_searchBt.addActionListener(this);
		m_previewBt = new JToggleButton(new ImageIcon("../images/filter2.gif"));
		m_previewBt.addActionListener(this);

		m_statusTextField = new JTextField();
		m_statusTextField.setEditable(false);
		// m_statusTextField.setText(EmployeePayrollSubmit.STR_STATUS1);
		m_submitdateTextField = new JTextField();
		m_submitdateTextField.setEditable(false);
		m_verifinoTextField = new JTextField();
		m_verifinoTextField.setEditable(false);
		m_verifyDate = new DatePicker();
		m_verifyDate.setDate(new Date());
		// m_verifydateTextField.setEditable(false);
		m_descriptionTextField = new JTextField();

		m_unitPicker = new UnitPicker(m_conn, m_sessionid);
		m_monthComboBox = new JMonthChooser(JMonthChooser.NO_SPINNER);

		m_yearField = new JSpinField();
		m_yearField.setMaximum(9999);
		m_yearField.setValue(new java.util.GregorianCalendar()
				.get(java.util.Calendar.YEAR));
		m_componentPicker = new PayrollComponentSubmitPicker(m_conn,
				m_sessionid, PayrollComponentTree.OVERTIME);

		m_viewBt = new JButton("View");
		m_viewBt.addActionListener(this);
		m_submitBt = new JButton("Submit");
		m_submitBt.addActionListener(this);

		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		m_calculateBt = new JButton("Calculate");
		m_calculateBt.addActionListener(this);
		m_verifyBt = new JButton("Verify");
		m_verifyBt.addActionListener(this);
		m_verifyBt.setEnabled(false);

		m_summaryBt = new JButton("Sumarry Report");
		m_summaryBt.addActionListener(this);
		m_summaryBt.setEnabled(false);

		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Viewed by Payroll Component");

		JLabel verifynolabel = new JLabel("Verfication No");
		JLabel verifydatelabel = new JLabel("Verfication Date");
		JLabel descriptionlabel = new JLabel("Description*");

		JPanel centerPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel formPanel = new JPanel();
		JPanel viewPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// buttonPanel.add(m_excellBt);
		// buttonPanel.add(m_searchBt);
		// buttonPanel.add(m_previewBt);
		buttonPanel.add(m_viewBt);
		// buttonPanel.add(m_submitBt);
		// buttonPanel.add(m_saveBt);
		// buttonPanel.add(m_cancelBt);
		// buttonPanel.add(m_calculateBt);
		buttonPanel.add(m_verifyBt);
		buttonPanel.add(m_summaryBt);

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

		Misc.setGridBagConstraints(viewPanel, m_componentPicker,
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

	public String getStrMount(int mount) {
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

	public String getMonthYearString() {
		return getStrMount(m_monthComboBox.getMonth()) + " - "
				+ m_yearField.getValue();
	}

	public void getSummaryreport() {
		try {
			boolean first = false;
			int no = 0;
			// RptSummary jasper = null;
			HashMap map = new HashMap();
			PayrollComponent[] payrollComponents = m_componentPicker
					.getPayrollComponentsSelected();

			RptSummaryOvertime beginjasper = new RptSummaryOvertime(m_conn,
					m_sessionid, this);
			RptSummaryOvertime addjasper = new RptSummaryOvertime(m_conn,
					m_sessionid, this);

			Iterator iterator = getMaperDept();
			List totallist = new ArrayList();
			List deptlist = new ArrayList();
			List dataList2 = new ArrayList();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String transdate = dateFormat.format(new Date());

			// HashMap orgmap = new HashMap();
			while (iterator.hasNext()) {
				List dataList = new ArrayList();
				Organization org = (Organization) iterator.next();
				int sum = employeeOvertimeSubmit.length;
				for (int i = 0; i < sum; i++) {
					if (!map.containsKey(new Long(employeeOvertimeSubmit[i]
							.getEmployeeIndex()))) {
						Employment[] employement = m_hrmLogic
								.getEmployeeEmployment(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										employeeOvertimeSubmit[i]
												.getEmployeeIndex());
						if (employement != null) {
							// orgmap.put(new
							// Long(employement[0].getDepartment().getIndex()),new
							// Long(employeeOvertimeSubmit[i].getEmployeeIndex()));
							if (org.getIndex() == employement[0]
									.getDepartment().getIndex()) {
								map.put(employeeOvertimeSubmit[i], new Long(
										employeeOvertimeSubmit[i]
												.getEmployeeIndex()));
								dataList.add(employeeOvertimeSubmit[i]);
							}
						}
					} else {
						dataList2.add(employeeOvertimeSubmit[i]);
					}
				}
				EmployeeOvertimeSubmit[] emps = (EmployeeOvertimeSubmit[]) dataList
						.toArray(new EmployeeOvertimeSubmit[dataList.size()]);
				if (emps.length > 0) {
					if (!first) {
						beginjasper.setRptSummaryVerivy(payrollComponents,
								emps, m_unitPicker, org, "", transdate);
						first = true;
					}

					addjasper.setRptSummaryVerivy(payrollComponents, emps,
							m_unitPicker, org, getMonthYearString(), transdate);
					totallist.add(addjasper.getTotal());
					deptlist.add(org);

					int size = addjasper.getJasperPrint().getPages().size();
					for (int j = 0; j < size; j++) {
						JRPrintPage page = (JRPrintPage) addjasper
								.getJasperPrint().getPages().get(j);
						beginjasper.getJasperPrint().addPage(no++, page);
					}
				}
			}
			EmployeeOvertimeSubmit[] emps2 = (EmployeeOvertimeSubmit[]) dataList2
					.toArray(new EmployeeOvertimeSubmit[dataList2.size()]);
			if (emps2.length > 0) {
				addjasper.setRptSummaryVerivy(payrollComponents, emps2,
						m_unitPicker, null, getMonthYearString(), transdate);
				totallist.add(addjasper.getTotal());
				deptlist.add(null);
				int size = addjasper.getJasperPrint().getPages().size();
				for (int j = 0; j < size; j++) {
					JRPrintPage page = (JRPrintPage) addjasper.getJasperPrint()
							.getPages().get(j);
					beginjasper.getJasperPrint().addPage(no++, page);
				}
			}

			addjasper.setRptSummarySubmit2(totallist, deptlist, m_unitPicker,
					getMonthYearString(), transdate);
			int size = addjasper.getJasperPrint().getPages().size();
			for (int j = 0; j < size; j++) {
				JRPrintPage page = (JRPrintPage) addjasper.getJasperPrint()
						.getPages().get(j);
				beginjasper.getJasperPrint().addPage(no++, page);
			}

			if (no > 0)
				beginjasper.getJasperPrint().removePage(no);
			beginjasper.getPrintView();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == m_viewBt) {
			presentingSubmittedData();
		} else if (e.getSource() == m_verifyBt) {
			dataVerification();
		} else if (e.getSource() == m_summaryBt) {
			if (employeeOvertimeSubmit.length > 0
					&& employeeOvertimeSubmit != null)
				getSummaryreport();
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	private Iterator getMaperDept() {
		GenericMapper mapper = MasterMap.obtainMapperFor(Organization.class);
		mapper.setActiveConn(m_conn);
		String selectWhere = " 1=1 ";
		List list = mapper.doSelectWhere(selectWhere);
		Iterator iterator = list.iterator();
		return iterator;
	}

	public void setDeptTotalValue() {
		try {
			HashMap map = new HashMap();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker
					.getRootPayrollComponents();

			Iterator iterator = getMaperDept();

			while (iterator.hasNext()) {
				List dataList = new ArrayList();
				Organization org = (Organization) iterator.next();
				for (int i = 0; i < employeeOvertimeSubmit.length; i++) {
					if (!map.containsKey(new Long(employeeOvertimeSubmit[i]
							.getEmployeeIndex()))) {
						Employment[] employement = m_hrmLogic
								.getEmployeeEmployment(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										employeeOvertimeSubmit[i]
												.getEmployeeIndex());
						if (employement != null) {
							if (org.getIndex() == employement[0]
									.getDepartment().getIndex()) {
								if (employeeOvertimeSubmit[i].getValue() > 0) {
									map.put(employeeOvertimeSubmit[i],
											new Long(employeeOvertimeSubmit[i]
													.getEmployeeIndex()));
									dataList.add(employeeOvertimeSubmit[i]);
								}
							}
						}
					}
				}
				EmployeeOvertimeSubmit[] emps = (EmployeeOvertimeSubmit[]) dataList
						.toArray(new EmployeeOvertimeSubmit[dataList.size()]);
				if (emps.length > 0) {
					PayrollColumnHelper helper = new PayrollColumnHelper();
					helper.prepareColumnHelper(payrollComponentsDefault);
					PayrollDeptValue payrollDeptValue = new PayrollDeptValue();
					payrollDeptValue.setOrganization(org);
					payrollDeptValue.setEmployeePayrollSubmit(emps[0]);
					long index = -1;
					for (int i = 0; i < emps.length; i++) {
						if (index != emps[i].getEmployeeIndex()) {
							index = emps[i].getEmployeeIndex();
							PayrollComponent payrollComponent = helper
									.getPayrollComponent(emps[i]
											.getPayrollComponentIndex());
							payrollDeptValue.setAccount(payrollComponent
									.getAccount());
							payrollDeptValue.setAccvalue(emps[i].getValue());
							m_accLogic
									.createPayrollDeptValue(
											m_sessionid,
											pohaci.gumunda.titis.accounting.dbapi.IDBConstants.MODUL_ACCOUNTING,
											payrollDeptValue);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dataVerification() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		if (m_verifyDate.getDate() != null)
			date = (Date) m_verifyDate.getDate();
		java.util.ArrayList list = new java.util.ArrayList();
		String description = m_descriptionTextField.getText();
		if (description.length() == 0)
			list.add("Description");
		if (unit == null)
			list.add("Unit");
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
		ReferenceNoGeneratorHelper helper = new ReferenceNoGeneratorHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_MASTER_DATA, "");
		String refNo = helper.createPayrollReferenceNo(m_journalStandardSetting
				.getJournalStandard().getCode(), date);
		JournalStandard journalStandard = m_journalStandardSetting
				.getJournalStandard();
		Journal journal = m_journalStandardSetting.getJournalStandard()
				.getJournal();
		Transaction transaction = new Transaction(-1, description, date, date,
				null, refNo, journal, journalStandard,
				(short) PayrollSubmitStatus.VERIFIED, unit);
		TransactionDetail[] transDetail = getTransactionDetail();

		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();

		// isi currency... lupa :)
		// cara yang buruk
		for (int i = 0; i < transDetail.length; i++) {
			TransactionDetail det = transDetail[i];
			det.setCurrency(baseCurrency);
		}

		transDetail = transHelper.prepareJournalStandardTransactionDetail(
				m_journalStandardSetting.getJournalStandard()
						.getJournalStandardAccount(), transDetail, unit,
				baseCurrency, 1);

		/*
		 * try { transaction=m_accLogic.createTransactionData(m_sessionid,
		 * IDBConstants.MODUL_MASTER_DATA,transaction,transDetail);
		 * employeeOvertimeSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
		 * m_hrmLogic.updateEmployeePayrollVerified(transaction.getIndex(),m_sessionid,
		 * IDBConstants.MODUL_MASTER_DATA,employeeOvertimeSubmit[0]); } catch
		 * (Exception e1) { e1.printStackTrace(); }
		 */

		try {
			/*
			 * transaction=m_accLogic.createTransactionData(m_sessionid,
			 * IDBConstants.MODUL_MASTER_DATA,transaction,transDetail);
			 */
			employeeOvertimeSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
			m_accLogic.createPayrollTransactionData(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, transaction, transDetail,
					employeeOvertimeSubmit[0]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Unsuccessfully creating transaction data");
			e.printStackTrace();
		}

		/*try {
			employeeOvertimeSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
			m_hrmLogic.updateEmployeePayrollVerified(transaction.getIndex(),
					m_sessionid, IDBConstants.MODUL_MASTER_DATA,
					employeeOvertimeSubmit[0]);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this,
					"Unsuccessfully updating payroll with transaction idx="
							+ transaction.getIndex());
			e1.printStackTrace();
		}*/
		m_statusTextField.setText(" Verified");
		m_verifinoTextField.setText(" " + refNo);
		m_verifyDate.setDate(date);
		m_verifyBt.setEnabled(false);
		setDeptTotalValue();
	}

	private TransactionDetail[] getTransactionDetail() {
		TransactionDetail[] transDetail = new TransactionDetail[transVector
				.size()];
		transVector.copyInto(transDetail);
		return transDetail;
	}

	protected void presentingSubmittedData() {

		PayrollComponent[] payrollComponents = m_componentPicker
				.getPayrollComponentsSelected();
		EmployeeOvertimeSubmit m_employeepayrollSubmit = setPayrollSubmitInitialisation();
		java.util.ArrayList list = new java.util.ArrayList();
		if (payrollComponents.length == 0)
			list.add("Payroll Component");
		if (unit == null)
			list.add("Unit");
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
		tableHeader(payrollComponents);
		try {
			employeeOvertimeSubmit = (EmployeeOvertimeSubmit[]) m_hrmLogic
					.getEmployeePayrollSubmit(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA,
							m_employeepayrollSubmit);
			if (employeeOvertimeSubmit != null
					&& employeeOvertimeSubmit.length > 0) {
				employeeOvertimeSubmit[0].setUnit((Unit) m_unitPicker
						.getObject());
				employeeOvertimeSubmit[0].setMonth(getMonth());
				employeeOvertimeSubmit[0].setYear(getYear());
				employeeOvertimeSubmit[0]
						.setPayrollType(PayrollSubmitType.OVERTIME);
				setTextfieldStatus(employeeOvertimeSubmit[0].getStatus(),
						employeeOvertimeSubmit[0]);
				if (employeeOvertimeSubmit[0].getStatus() == 1)
					m_verifyBt.setEnabled(true);
				else if (employeeOvertimeSubmit[0].getStatus() == 0)
					m_verifyBt.setEnabled(true);
				else
					m_verifyBt.setEnabled(false);
			} else {
				setTextfieldStatus(PayrollSubmitStatus.NOTSUBMITTED, null);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		presentingData(employeeOvertimeSubmit, payrollComponents);
		PayrollComponent[] payrollComponentsDefault = m_componentPicker
				.getRootPayrollComponents();
		preparingData(employeeOvertimeSubmit, payrollComponentsDefault);
	}

	private void preparingData(EmployeePayrollSubmit[] empMealAllowSubmit,
			PayrollComponent[] payrollComponents) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		helper.prepareColumnHelper(payrollComponents);
		m_data = new Vector();
		transVector.clear();
		long tempEmpIndex = -1;
		long tempCompIndex = -1;
		for (int i = 0; i < empMealAllowSubmit.length; i++) {
			long empIdx = empMealAllowSubmit[i].getEmployeeIndex();
			long compIdx = empMealAllowSubmit[i].getPayrollComponentIndex();
			if (!((tempEmpIndex == empIdx) && (tempCompIndex == compIdx))) {
				tempEmpIndex = empIdx;
				tempCompIndex = compIdx;
				int colIdx = helper.getColumnIndex(empMealAllowSubmit[i]
						.getPayrollComponentIndex());
				if (colIdx != -1) {

					double value = empMealAllowSubmit[i].getValue();
					Object content = null;
					if (value != -1) {
						long payindex = empMealAllowSubmit[i]
								.getPayrollComponentIndex();
						double valuesaved = (double) helper.getValue(payindex);
						value = value + valuesaved;
						content = new Double(value);
						helper.putValue(payindex, (Double) content);
					} else {
						// do nothing
					}
				}
			}
		}
		for (int v = 0; v < payrollComponents.length; v++) {
			long payindex = payrollComponents[v].getIndex();
			double dataValue = (double) helper.getValue(payindex);
			Double content = new Double(dataValue);
			transactionDetail = new TransactionDetail(payrollComponents[v]
					.getAccount(), ((Double) content).doubleValue(),
					baseCurrency, 1, unit, -1);
			transVector.add(transactionDetail);
		}
	}

	public void presentingDataSummary(EmployeeOvertimeSubmit[] empPaySub,
			PayrollComponent[] m_payrollComponents, DefaultTableModel model) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex = -1;
		int no = 0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		// transVector.clear();
		DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		double[] total = { 0, 0 };
		double[] subtotal = { 0, 0 };
		for (int i = 0; i < empPaySub.length; i++) {
			if (empPaySub[i] != null) {
				if (tempIndex != empPaySub[i].getEmployeeIndex()) {
					if (m_data.size() > 0) {
						total[0] += subtotal[0];
						total[1] += subtotal[1];
						m_data.addElement(new Integer(0));
						model.addRow(m_data);
					}
					m_data = new Vector();
					subtotal[0] = 0;
					subtotal[1] = 0;
					tableContent(empPaySub[i], no++);
					tempIndex = empPaySub[i].getEmployeeIndex();
					leftMargin = m_data.size();
				}
				summaryContentOvertime(empPaySub[i], empPaySub[i]
						.getMultiplierIndex());
				int colIdx = helper.getColumnIndex(empPaySub[i]
						.getPayrollComponentIndex());
				if (colIdx != -1) {
					double value = empPaySub[i].getValue();
					String content = "";
					if (value != -1) {
						content = formatDesimal.format(value);
						subtotal[colIdx] = value;
						// PayrollComponent
						// payComp=helper.getPayrollComponent(empPaySub[i].getPayrollComponentIndex());
						// transactionDetail = new
						// TransactionDetail(payComp.getAccount(),value,baseCurrency,1,
						// unit, -1);
						// transVector.add(transactionDetail);
					} else {
						content = "";
					}
					System.err.println(colIdx + leftMargin);
					helper
							.addDataAtColumn(m_data, colIdx + leftMargin,
									content);
				}
			}
		}
		if (m_data.size() > 0) {
			total[0] += subtotal[0];
			total[1] += subtotal[1];
			m_data.addElement(new Integer(0));
			model.addRow(m_data);
		}
		String[] strTotal = getStrValue(total);
		model.addRow(new Object[] { "TOTAL PER DEPARTMENT", "", "", "", "", "",
				"", "", strTotal[0], strTotal[1], new Integer(1) });
		setTotal(total);
	}

	public String[] getStrValue(double[] value) {
		DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		String[] str = { "", "" };
		if (value[0] > 0)
			str[0] = formatDesimal.format(value[0]);
		if (value[1] > 0)
			str[1] = formatDesimal.format(value[1]);
		return str;
	}

	double[] m_total = { 0, 0 };

	public void setTotal(double[] total) {
		m_total = total;
	}

	public double[] getTotal() {
		return m_total;
	}

	private void presentingData(EmployeeOvertimeSubmit[] empPaySub,
			PayrollComponent[] m_payrollComponents) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex = -1;
		int no = 0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		transVector.clear();
		for (int i = 0; i < empPaySub.length; i++) {
			if (empPaySub[i] != null) {
				if (tempIndex != empPaySub[i].getEmployeeIndex()) {
					if (m_data.size() > 0)
						m_table.addRow(m_data);
					m_data = new Vector();
					tableContent(empPaySub[i], no++);
					tempIndex = empPaySub[i].getEmployeeIndex();
					leftMargin = m_data.size();
				}
				tableContentOvertime(empPaySub[i], empPaySub[i]
						.getMultiplierIndex());
				int colIdx = helper.getColumnIndex(empPaySub[i]
						.getPayrollComponentIndex());
				if (colIdx != -1) {
					double value = empPaySub[i].getValue();
					Object content = null;
					if (value != -1) {
						content = new Double(value);
						PayrollComponent payComp = helper
								.getPayrollComponent(empPaySub[i]
										.getPayrollComponentIndex());
						transactionDetail = new TransactionDetail(payComp
								.getAccount(),
								((Double) content).doubleValue(), baseCurrency,
								1, unit, -1);
						transVector.add(transactionDetail);
					} else {
						content = "";
					}
					helper
							.addDataAtColumn(m_data, colIdx + leftMargin,
									content);
				}
			}
		}
		if (m_data.size() > 0) {
			m_data.addElement("");
			m_table.addRow(m_data);
		}
		m_table.setModelKu();
		m_summaryBt.setEnabled(true);
	}

	private void tableContent(EmployeeOvertimeSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name = submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getJobTitle());
		for (int k = 0; k < 5; k++) {
			m_data.addElement("");
		}
	}

	private void tableContentOvertime(EmployeeOvertimeSubmit overtimeSubmit,
			long dex) {
		long index = dex;

		float value = overtimeSubmit.getOvertimeValue();
		if (index == 1) {
			m_data.set(3, new Float(value));
		} else if (index == 2) {
			m_data.set(5, new Float(value));
		} else if (index == 3) {
			m_data.set(4, new Float(value));
		} else if (index == 4) {
			m_data.set(6, new Float(value));
		} else if (index == 5) {
			m_data.set(7, new Float(value));
		}
	}

	private void summaryContentOvertime(EmployeeOvertimeSubmit overtimeSubmit,
			long dex) {
		long index = dex;

		float value = overtimeSubmit.getOvertimeValue();
		if (index == 1) {
			m_data.set(3, strValue(value));
		} else if (index == 2) {
			m_data.set(5, strValue(value));
		} else if (index == 3) {
			m_data.set(4, strValue(value));
		} else if (index == 4) {
			m_data.set(6, strValue(value));
		} else if (index == 5) {
			m_data.set(7, strValue(value));
		}
	}

	private String strValue(float val) {
		String str = "";
		if (val > 0)
			str = String.valueOf(val);
		return str;
	}

	private void setTextfieldStatus(int status,
			EmployeePayrollSubmit employeeMealAll) {
		if (status == PayrollSubmitStatus.NOTSUBMITTED) {
			m_statusTextField.setText(" Not Submitted");
			m_submitdateTextField.setText("");
			m_verifyDate.setDate(new Date());
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(false);
		} else if (status == PayrollSubmitStatus.SUBMITTED) {
			m_statusTextField.setText(" Submitted");
			m_submitdateTextField.setText(" "
					+ employeeMealAll.getSubmittedDate().toString());
			m_verifyDate.setDate(new Date());
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(false);
		} else if (status == PayrollSubmitStatus.VERIFIED) {
			m_statusTextField.setText(" Verified");
			m_submitdateTextField.setText(" "
					+ employeeMealAll.getSubmittedDate().toString());
			Transaction trans = getTransaction(employeeMealAll);
			m_verifyDate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" " + trans.getDescription());
			m_verifinoTextField.setText(" " + trans.getReference());
		} else if (status == PayrollSubmitStatus.POSTED) {
			m_statusTextField.setText(" Posted");
			m_submitdateTextField.setText(" "
					+ employeeMealAll.getSubmittedDate().toString());
			Transaction trans = getTransaction(employeeMealAll);
			m_verifyDate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" " + trans.getDescription());
			m_verifinoTextField.setText(" " + trans.getReference());
		}
	}

	private Transaction getTransaction(EmployeePayrollSubmit employ) {
		Transaction transaction = null;
		try {
			transaction = m_accLogic.getTransaction(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, employ.getTransaction());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaction;
	}

	private void tableHeader(PayrollComponent[] payrollComponents) {
		PayrollComponentSelectionTree viewTree = new PayrollComponentSelectionTree(
				m_conn, m_sessionid, null, payrollComponents);
		if (payrollComponents.length > 0) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_table
					.getTableHeaderRoot();
			root.removeAllChildren();
			m_table.resetDefaultColumn();
			root.add((DefaultMutableTreeNode) viewTree.getModel().getRoot());
			m_table.rebuildTable();
			m_table.clearRow();
		}
	}

	private EmployeeOvertimeSubmit setPayrollSubmitInitialisation() {
		unit = (Unit) m_unitPicker.getObject();
		EmployeeOvertimeSubmit employeeOvertimeSubmit = new EmployeeOvertimeSubmit();
		employeeOvertimeSubmit.setMonth(getMonth());
		employeeOvertimeSubmit.setYear(getYear());
		employeeOvertimeSubmit.setUnit(unit);
		employeeOvertimeSubmit.setStatus(PayrollSubmitStatus.SUBMITTED);
		Calendar calendar = Calendar.getInstance();
		employeeOvertimeSubmit.setSubmittedDate(calendar.getTime());
		employeeOvertimeSubmit.setPayrollType(PayrollSubmitType.OVERTIME);
		return employeeOvertimeSubmit;
	}

	public int getMonth() {
		return m_monthComboBox.getMonth() + 1;
	}

	public int getYear() {
		return m_yearField.getValue();
	}

	class PayrollTable extends StructuredTable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public PayrollTable(DefaultMutableTreeNode root) {
			super(root);
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			resetDefaultColumn();
		}

		public void resetDefaultColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("No"));
			root.add(new DefaultMutableTreeNode("Name"));
			root.add(new DefaultMutableTreeNode("Job Title"));
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(
					"Overtime (hours)");
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
					"Working Days");
			node1.add(new DefaultMutableTreeNode("<1"));
			node1.add(new DefaultMutableTreeNode("1-"));
			node.add(node1);
			node1 = new DefaultMutableTreeNode("Non Working Days");
			node1.add(new DefaultMutableTreeNode("1-7"));
			node1.add(new DefaultMutableTreeNode("8"));
			node1.add(new DefaultMutableTreeNode("9-"));
			node.add(node1);
			root.add(node);
		}

		public void clearRow() {
			((DefaultTableModel) getModel()).setRowCount(0);
		}

		public void setModelKu() {

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

			for (int i = 3; i < 8; i++) {
				getColumnModel().getColumn(i).setPreferredWidth(50);
				getColumnModel().getColumn(i).setMinWidth(50);
				getColumnModel().getColumn(i).setMaxWidth(50);
			}
		}

		public void setColumnSize() {
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);
		}

		public void addRow(Vector data) {
			((DefaultTableModel) getModel()).addRow(data);
		}

		public TableCellRenderer getCellRenderer(int row, int col) {
			if (col == 0)
				return new RightAllignmentCellRenderer();
			else if (col > 2)
				return new DoubleCellRenderer(JLabel.RIGHT);
			return super.getCellRenderer(row, col);
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
}
