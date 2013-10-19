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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import net.sf.jasperreports.engine.JRPrintPage;

import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.titis.accounting.cgui.report.AccPayrollPaycheque;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.PayrollDeptValue;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.helper.TransactionDetailPreparingHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.DblCellEditor;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.application.StructuredTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.EmployeeAccount;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentReportLabel;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSelectionTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSubmitPicker;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollSubmitType;
import pohaci.gumunda.titis.hrm.cgui.SearchEmployeeDlg;
import pohaci.gumunda.titis.hrm.cgui.report.RptSummary;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollSummaryLogic;

public class PayrollPaychequeVerificationPanel extends PayrollVerificationPanel
		implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Connection m_conn = null;
	public long m_sessionid = -1;

	JTextField m_statusTextField, m_submitdateTextField, m_verifinoTextField,
			m_descriptionTextField;
	JButton m_viewBt, m_editBt, m_saveBt, m_cancelBt, m_calculateBt,
			m_verifyBt, m_summaryBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;
	DatePicker m_verifydate;
	private PayrollTable m_table;
	private Unit unit;
	private EmployeePayrollSubmit[] m_empPayrollSubmit;
	public Vector m_data;
	public Vector m_cols = null;
	public Vector m_rows = null;
	boolean m_edit = false;
	private PayrollComponent[] m_components;

	private TransactionDetail transactionDetail;
	public Vector transVector = new Vector();

	private JournalStandardSetting m_journalStandardSetting = null;

	private BaseCurrency baseCurrency = null;
	private HRMBusinessLogic m_hrmLogic;

	Integer status = new Integer(1);
	private RptSummary m_beginjasper, m_addjasper;
	JComboBox m_typecombo;

	public PayrollPaychequeVerificationPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_hrmLogic = new HRMBusinessLogic(m_conn);
		m_beginjasper = new RptSummary(m_conn, m_sessionid, this);
		m_addjasper = new RptSummary(m_conn, m_sessionid, this);

		constructComponent();
		getJournalStandardSettings();
		baseCurrency = BaseCurrency.createBaseCurrency(m_conn, m_sessionid);
	}

	private void getJournalStandardSettings() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_MASTER_DATA);

		List list = helper
				.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES);
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
		m_verifydate = new DatePicker();
		m_verifydate.setDate(new java.util.Date());
		// m_verifydate.setEditable(false);
		m_descriptionTextField = new JTextField();

		m_unitPicker = new UnitPicker(m_conn, m_sessionid);
		m_monthComboBox = new JMonthChooser(JMonthChooser.NO_SPINNER);
		m_typecombo = new JComboBox(new Object[] {
				IDBConstants.ATTR_PAYCHEQUE[0], IDBConstants.ATTR_PAYCHEQUE[1],
				IDBConstants.ATTR_PAYCHEQUE[2] });

		m_yearField = new JSpinField();
		m_yearField.setMaximum(9999);
		m_yearField.setValue(new java.util.GregorianCalendar()
				.get(java.util.Calendar.YEAR));
		m_componentPicker = new PayrollComponentSubmitPicker(m_conn,
				m_sessionid, PayrollComponentTree.PAYCHEQUE_RECEIVABLES);

		m_viewBt = new JButton("View");
		m_viewBt.addActionListener(this);
		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		m_summaryBt = new JButton("Summary Report");
		m_summaryBt.addActionListener(this);

		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		m_calculateBt = new JButton("Calculate");
		m_calculateBt.addActionListener(this);
		m_verifyBt = new JButton("Verify");
		m_verifyBt.addActionListener(this);

		m_editBt.setEnabled(false);
		edit(false);
		m_verifyBt.setEnabled(false);
		m_summaryBt.setEnabled(false);

		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Viewed by Payroll Component");
		JLabel typelabel = new JLabel("Type");

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
		buttonPanel.add(m_editBt);
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);
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

		Misc.setGridBagConstraints(formPanel, m_verifydate, gridBagConstraints,
				2, 3, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, viewPanel, gridBagConstraints, 3,
				3, GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER,
				1, 1.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, typelabel, gridBagConstraints, 0,
				4, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1,
						2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_typecombo, gridBagConstraints,
				2, 4, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, descriptionlabel,
				gridBagConstraints, 0, 5, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 5, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_descriptionTextField,
				gridBagConstraints, 2, 5, GridBagConstraints.HORIZONTAL,
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

		formPanel.setPreferredSize(new Dimension(600, 140));
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

	public void summaryReport() {
		try {
			boolean first = false;
			int no = 0;
			// RptSummary jasper = null;
			HashMap map = new HashMap();
			List dataList2 = new ArrayList();
			m_beginjasper.setType(m_typecombo);
			m_addjasper.setType(m_typecombo);

			PayrollComponent[] payrollComponents = m_componentPicker
					.getPayrollComponentsSelected();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker
					.getRootPayrollComponents();

			Iterator iterator = getMaperDept();
			List totallist = new ArrayList();
			List deptlist = new ArrayList();

			String transdate = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			transdate = dateFormat.format(new Date());

			if (m_empPayrollSubmit[0] != null && m_empPayrollSubmit.length > 0) {
				m_empPayrollSubmit[0].setUnit((Unit) m_unitPicker.getObject());
				m_empPayrollSubmit[0].setMonth(getMonth());
				m_empPayrollSubmit[0].setYear(getYear());
				m_empPayrollSubmit[0]
						.setPayrollType(PayrollSubmitType.PAYCHEQUE_SUBMIT);
				m_empPayrollSubmit = queryEmpPayrollSubmit(submitObject);
				submitObject = getEmpPayrollSubmit();
				/*
				 * if (m_empPayrollSubmit[0].getTransaction()>0){ Transaction
				 * trans=getTransaction(m_empPayrollSubmit[0]); }
				 */
			}

			while (iterator.hasNext()) {
				List dataList = new ArrayList();
				Organization org = (Organization) iterator.next();
				for (int i = 0; i < m_empSub.length; i++) {
					if (!map.containsKey(new Long(m_empSub[i].getAutoindex()))) {
						Employment[] employement = m_hrmLogic
								.getEmployeeEmployment(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										m_empSub[i].getAutoindex());
						if (employement != null) {
							if (org.getIndex() == employement[0]
									.getDepartment().getIndex()) {
								map.put(m_empSub[i], new Long(m_empSub[i]
										.getAutoindex()));
								dataList.add(m_empSub[i]);
							}
						}
					} else {
						dataList2.add(m_empSub[i]);
					}
				}
				Employee_n[] emps = (Employee_n[]) dataList
						.toArray(new Employee_n[dataList.size()]);
				List empList = Arrays.asList(emps);
				Collections.sort(empList);
				emps = (Employee_n[]) empList.toArray(new Employee_n[empList
						.size()]);

				if (emps.length > 0) {
					if (!first) {
						m_beginjasper.setRptSummary(payrollComponents,
								m_empPayrollSubmit, payrollComponentsDefault,
								emps, m_unitPicker, org, getMonthYearString(),
								transdate);
						first = true;
					}

					m_addjasper.setRptSummary(payrollComponents,
							m_empPayrollSubmit, payrollComponentsDefault, emps,
							m_unitPicker, org, getMonthYearString(), transdate);
					totallist.add(m_addjasper.getTotal());
					deptlist.add(org);

					int size = m_addjasper.getJasperPrint().getPages().size();
					for (int j = 0; j < size; j++) {
						JRPrintPage page = (JRPrintPage) m_addjasper
								.getJasperPrint().getPages().get(j);
						m_beginjasper.getJasperPrint().addPage(no++, page);
					}
				}
			}

			Employee_n[] emps2 = (Employee_n[]) dataList2
					.toArray(new Employee_n[dataList2.size()]);
			if (emps2.length > 0) {
				m_addjasper.setRptSummary(payrollComponents,
						m_empPayrollSubmit, payrollComponentsDefault, emps2,
						m_unitPicker, null, getMonthYearString(), transdate);
				totallist.add(m_addjasper.getTotal());
				deptlist.add(null);
				int size = m_addjasper.getJasperPrint().getPages().size();
				for (int j = 0; j < size; j++) {
					JRPrintPage page = (JRPrintPage) m_addjasper
							.getJasperPrint().getPages().get(j);
					m_beginjasper.getJasperPrint().addPage(no++, page);
				}
			}

			m_addjasper.setRptSummary2(totallist, deptlist, m_unitPicker,
					getMonthYearString(), transdate);
			int size = m_addjasper.getJasperPrint().getPages().size();
			for (int j = 0; j < size; j++) {
				JRPrintPage page = (JRPrintPage) m_addjasper.getJasperPrint()
						.getPages().get(j);
				m_beginjasper.getJasperPrint().addPage(no++, page);
			}

			if (no > 0)
				m_beginjasper.getJasperPrint().removePage(no);
			m_beginjasper.getPrintView();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDeptTotalValue() {
		try {
			HashMap map = new HashMap();
			// List dataList2 = new ArrayList();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker
					.getRootPayrollComponents();

			Iterator iterator = getMaperDept();

			while (iterator.hasNext()) {
				List dataList = new ArrayList();
				Organization org = (Organization) iterator.next();
				for (int i = 0; i < m_empPayrollSubmit.length; i++) {
					if (!map.containsKey(new Long(m_empPayrollSubmit[i]
							.getEmployeeIndex()))) {
						map.get(new Long(m_empPayrollSubmit[i]
								.getEmployeeIndex()));
						Employment[] employement = m_hrmLogic
								.getEmployeeEmployment(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										m_empPayrollSubmit[i]
												.getEmployeeIndex());
						if (employement != null) {
							if (org.getIndex() == employement[0]
									.getDepartment().getIndex()) {
								dataList.add(m_empPayrollSubmit[i]);
								map.put(m_empPayrollSubmit[i], new Long(
										m_empPayrollSubmit[i]
												.getEmployeeIndex()));
							}
						}
					}
				}
				EmployeePayrollSubmit[] empPayrollSubmit = (EmployeePayrollSubmit[]) dataList
						.toArray(new EmployeePayrollSubmit[dataList.size()]);
				// List empList = Arrays.asList(empPayrollSubmit);
				// Collections.sort(empList);
				// emps = (Employee_n[]) empList.toArray(new
				// Employee_n[empList.size()]);

				if (empPayrollSubmit.length > 0) {
					PayrollDeptValue payrollDeptValue = new PayrollDeptValue();
					payrollDeptValue.setOrganization(org);
					payrollDeptValue
							.setEmployeePayrollSubmit(empPayrollSubmit[0]);
					PayrollSummaryLogic summaryLogic = new PayrollSummaryLogic(
							this, payrollComponents, null, unit,
							empPayrollSubmit, payrollComponentsDefault);
					summaryLogic.setType(m_typecombo);
					summaryLogic.setTotalDeptValue(payrollDeptValue, true);
				}
			}
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

	boolean m_cekreport = false;
	PayrollComponent[] payrollComponents;
	EmployeePayrollSubmit submitObject;
	Employee_n[] m_empSub = null;

	public void actionPerformed(ActionEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == m_viewBt) {
			m_ceksearch = false;
			m_cekreport = false;
			payrollComponents = m_componentPicker
					.getPayrollComponentsSelected();
			submitObject = getEmpPayrollSubmit();
			presentingSubmittedData();
			m_summaryBt.setEnabled(true);
		} else if (e.getSource() == m_verifyBt) {
			dataVerification();
		} else if (e.getSource() == m_editBt) {
			edit(true);
			m_editBt.setEnabled(false);
		} else if (e.getSource() == m_cancelBt) {
			cancel();
		} else if (e.getSource() == m_summaryBt) {
			if (m_empPayrollSubmit.length != 0 && m_empPayrollSubmit != null) {
				if (!m_ceksearch)
					m_empSub = getEmployeeByCriteria();
				if (m_empSub.length != 0)
					summaryReport();
			}
		} else if (e.getSource() == m_saveBt) {
			save();
			presentingSubmittedData();
		} else if (e.getSource() == m_excellBt) {
			payrollComponents = m_componentPicker
					.getPayrollComponentsSelected();
			submitObject = getEmpPayrollSubmit();
			new AccPayrollPaycheque(this, payrollComponents, submitObject, true);
		} else if (e.getSource() == m_searchBt) {
			m_cekreport = false;
			payrollComponents = m_componentPicker
					.getPayrollComponentsSelected();
			submitObject = getEmpPayrollSubmit();
			SearchEmployeeDlg dlg = new SearchEmployeeDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), this,
					m_conn, m_sessionid);
			dlg.setVisible(true);
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	public void SetEmpSub(String[] attr, String operator) {
		String data = "";
		String item = "";
		if (!attr[0].equals("")) {
			if (data.equals(""))
				item = "";
			else
				item = operator;
			data += item + "UPPER(firstname& ' ' & midlename& ' ' & lastname) "
					+ "LIKE '%" + attr[0].toUpperCase() + "%' ";
		}
		if (!attr[1].equals("")) {
			if (data.equals(""))
				item = "";
			else
				item = operator;
			data += item + " UPPER(jobtitle) like '%" + attr[1].toUpperCase()
					+ "%' ";
		}
		data = "and (" + data + ")";
		if (attr[0].equals("") && attr[1].equals(""))
			data = " ";

		String query = "select distinct * from (select a.autoindex,a.firstname,a.midlename,a.lastname,a.employeeno,c.name jobtitle from employee a,employeeemployment b,jobtitle c "
				+ "where a.autoindex=b.employee and b.jobtitle=c.autoindex) where autoindex in (select distinct employee from employeepayrolldetail where "
				+ "employeepayroll in (select a.autoindex from employeepayroll a where a.monthpayrollsubmit="
				+ (m_monthComboBox.getMonth() + 1)
				+ " and a.yearpayrollsubmit="
				+ m_yearField.getValue()
				+ " and a.unit="
				+ m_unitPicker.getUnit().getIndex()
				+ "))"
				+ data;
		try {
			m_empSub = m_hrmLogic.getEmployeeBy_Criteria(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Employee_n[] getEmployeeByCriteria() {
		Employee_n[] emps = null;
		try {
			String query = "select distinct * from (select a.autoindex,a.firstname,a.midlename,a.lastname,a.employeeno,c.name jobtitle from employee a,employeeemployment b,jobtitle c "
					+ "where a.autoindex=b.employee and b.jobtitle=c.autoindex) where autoindex in (select distinct employee from employeepayrolldetail where "
					+ "employeepayroll in (select a.autoindex from employeepayroll a where a.monthpayrollsubmit="
					+ (m_monthComboBox.getMonth() + 1)
					+ " and a.yearpayrollsubmit="
					+ m_yearField.getValue()
					+ " and a.unit=" + m_unitPicker.getUnit().getIndex() + "))";
			emps = m_hrmLogic.getEmployeeBy_Criteria(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emps;
	}

	private void instaceReport() {
		m_GajiPokok[0] = " ";
		m_GajiPokok[1] = " ";
		m_Kes[0] = " ";
		m_Kes[1] = " ";
		m_Mgmt[0] = " ";
		m_Mgmt[1] = " ";
		m_Rmh[0] = " ";
		m_Rmh[1] = " ";
		m_Tek[0] = " ";
		m_Tek[1] = " ";
		m_Lap[0] = " ";
		m_Lap[1] = " ";
		m_THR[0] = " ";
		m_THR[1] = " ";
		m_Insertif[0] = " ";
		m_Insertif[1] = " ";
		m_Bonus[0] = " ";
		m_Bonus[1] = " ";
		m_asuransiPeg = " ";
		m_piutKePerusahaan = " ";
		m_piutKeYayasan = " ";
		m_total = " ";
	}

	private void valueReportComponents() {
		m_data.addElement(m_GajiPokok[0]);
		m_data.addElement(m_GajiPokok[1]);
		m_data.addElement(m_Kes[0]);
		m_data.addElement(m_Kes[1]);
		m_data.addElement(m_Mgmt[0]);
		m_data.addElement(m_Mgmt[1]);
		m_data.addElement(m_Tek[0]);
		m_data.addElement(m_Tek[1]);
		m_data.addElement(m_Rmh[0]);
		m_data.addElement(m_Rmh[1]);
		m_data.addElement(m_THR[0]);
		m_data.addElement(m_THR[1]);
		m_data.addElement(m_Lap[0]);
		m_data.addElement(m_Lap[1]);
		m_data.addElement(m_Insertif[0]);
		m_data.addElement(m_Insertif[1]);
		m_data.addElement(m_Bonus[0]);
		m_data.addElement(m_Bonus[1]);
		m_data.addElement(m_asuransiPeg);
		m_data.addElement(m_piutKePerusahaan);
		m_data.addElement(m_piutKeYayasan);
		m_data.addElement(m_total);
		m_data.addElement(status);
	}

	private void save() {
		boolean done = true;
		if (m_empPayrollSubmit != null) {
			if (m_empPayrollSubmit.length > 0) {
				if (m_cols != null) {
					for (int i = 0; i < m_empPayrollSubmit.length; i++) {
						if (checkReceivablesBasedComponent(m_empPayrollSubmit[i]
								.getPayrollComponentIndex())) {
							int row = m_empPayrollSubmit[i]
									.getRowComponentAtTable();
							int col = m_empPayrollSubmit[i]
									.getColComponentAtTable();

							Object obj = m_table.getValueAt(row, col);
							// System.out.println(m_empPayrollSubmit[i].getIndex()
							// + ":: " + row + ", " + col + ": " +
							// obj.toString());
							double res = m_empPayrollSubmit[i].getValue();
							if (obj instanceof Double)
								res = ((Double) obj).floatValue();

							m_empPayrollSubmit[i].setValue(res);

							HRMBusinessLogic logic = new HRMBusinessLogic(
									m_conn);
							try {
								logic.updateValueEmployeePayrollDetail(
										m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										m_empPayrollSubmit[i]);
							} catch (Exception e) {
								done = false;
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		if (done) {
			edit(false);
			m_editBt.setEnabled(true);
		}
	}

	private boolean checkReceivablesBasedComponent(long payrollComponentIndex) {
		if (m_components.length > 0) {
			for (int i = 0; i < m_components.length; i++) {
				if ((payrollComponentIndex == m_components[i].getIndex())
						&& (m_components[i].getPaymentAsString() == PayrollComponent.PAYCHEQUE)
						&& (m_components[i].getSubmitAsString() == PayrollComponent.EMPLOYEE_RECEIVABLES))
					return true;
			}
		}
		return false;
	}

	private void edit(boolean editable) {
		m_edit = editable;
		m_saveBt.setEnabled(editable);
		m_cancelBt.setEnabled(editable);
		// m_verifydate.setEditable(editable);
		m_viewBt.setEnabled(!editable);
		m_verifyBt.setEnabled(!editable);
	}

	private void cancel() {
		presentingSubmittedData();
		edit(false);
	}

	private void dataVerification() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		if (m_verifydate.getDate() != null)
			date = (Date) m_verifydate.getDate();
		java.util.ArrayList list = new java.util.ArrayList();

		list.clear();
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

		AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
		try {
			/*
			 * transaction = accLogic.createTransactionData(m_sessionid,
			 * IDBConstants.MODUL_MASTER_DATA, transaction, transDetail);
			 */
			m_empPayrollSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
			accLogic.createPayrollTransactionData(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, transaction, transDetail,
					m_empPayrollSubmit[0]);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Unsuccessfully creating transaction data");
			e.printStackTrace();
		}

		/*
		 * HRMBusinessLogic logic = new HRMBusinessLogic(m_conn); try {
		 * m_empPayrollSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
		 *
		 * logic.updateEmployeePayrollVerified(transaction.getIndex(),
		 * m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_empPayrollSubmit[0]); }
		 * catch (Exception e1) { JOptionPane .showMessageDialog(this,
		 * "Unsuccessfully updating payroll with transaction idx=" +
		 * transaction.getIndex() + "\r\n" + e1.getMessage());
		 * e1.printStackTrace(); }
		 */
		m_statusTextField.setText(" Verified");
		m_verifydate.setDate(date);
		m_verifinoTextField.setText(" " + refNo);
		m_verifyBt.setEnabled(false);
		m_editBt.setEnabled(false);

		if (m_empPayrollSubmit.length != 0 || m_empPayrollSubmit != null) {
			if (!m_ceksearch)
				m_empSub = getEmployeeByCriteria();
			if (m_empSub.length != 0)
				setDeptTotalValue();
		}
	}

	private TransactionDetail[] getTransactionDetail() {
		TransactionDetail[] transDetail = new TransactionDetail[transVector
				.size()];
		transVector.copyInto(transDetail);
		return transDetail;
	}

	protected void presentingSubmittedData() {
		if (payrollComponents == null) {
			payrollComponents = m_componentPicker
					.getPayrollComponentsSelected();
		}
		if (unit == null) {
			unit = m_unitPicker.getUnit();
		}
		if (submitObject == null) {
			submitObject = getEmpPayrollSubmit();
		}
		m_components = payrollComponents;
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
		getEmployeePayroll(payrollComponents, submitObject,
				(DefaultTableModel) this.m_table.getModel(), false);
	}

	public void getEmployeePayroll(PayrollComponent[] payrollComponents,
			EmployeePayrollSubmit submitObject, DefaultTableModel model,
			boolean cekreport) {
		m_empPayrollSubmit = queryEmpPayrollSubmit(submitObject);

		if (!cekreport) {
			try {
				if (m_empPayrollSubmit != null && m_empPayrollSubmit.length > 0) {
					m_empPayrollSubmit[0].setUnit((Unit) m_unitPicker
							.getObject());
					m_empPayrollSubmit[0].setMonth(getMonth());
					m_empPayrollSubmit[0].setYear(getYear());
					m_empPayrollSubmit[0]
							.setPayrollType(PayrollSubmitType.PAYCHEQUE_SUBMIT);
					setTextfieldStatus(m_empPayrollSubmit[0].getStatus(),
							m_empPayrollSubmit[0]);
					if (m_empPayrollSubmit[0].getStatus() == 1) {
						m_verifyBt.setEnabled(true);
						m_editBt.setEnabled(true);

					} else if (m_empPayrollSubmit[0].getStatus() == 0) {
						m_verifyBt.setEnabled(true);
						m_editBt.setEnabled(true);
					} else {
						m_verifyBt.setEnabled(false);
						m_editBt.setEnabled(false);
					}
				} else {
					m_editBt.setEnabled(false);
					setTextfieldStatus(PayrollSubmitStatus.NOTSUBMITTED, null);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			presentingDataTable(payrollComponents);
		} else
			presentingDataReport(m_empPayrollSubmit, payrollComponents, model);
		PayrollComponent[] payrollComponentsDefault = m_componentPicker
				.getRootPayrollComponents();
		preparingData(m_empPayrollSubmit, payrollComponentsDefault);
	}

	public EmployeePayrollSubmit[] queryEmpPayrollSubmit(
			EmployeePayrollSubmit submitObject) {
		try {
			EmployeePayrollSubmit[] employeePaySubm = m_hrmLogic
					.getEmployeePayrollSubmit(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA, submitObject);
			return employeePaySubm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void preparingData(EmployeePayrollSubmit[] empMealAllowSubmit,
			PayrollComponent[] payrollComponents) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		helper.prepareColumnHelper(payrollComponents);
		m_data = new Vector();
		transVector.clear();
		for (int i = 0; i < empMealAllowSubmit.length; i++) {
			int colIdx = helper.getColumnIndex(empMealAllowSubmit[i]
					.getPayrollComponentIndex());
			if (colIdx != -1) {
				double value = empMealAllowSubmit[i].getValue();
				Object content = null;
				if (value != -1) {
					long payindex = empMealAllowSubmit[i]
							.getPayrollComponentIndex();
					float valuesaved = (float) helper.getValue(payindex);
					value = value + valuesaved;
					content = new Double(value);
					helper.putValue(payindex, (Double) content);
				} else {
					// do nothing
				}
			}
		}
		for (int v = 0; v < payrollComponents.length; v++) {
			long payindex = payrollComponents[v].getIndex();
			float dataValue = (float) helper.getValue(payindex);
			Double content = new Double(dataValue);

			transactionDetail = new TransactionDetail(payrollComponents[v]
					.getAccount(), ((Double) content).doubleValue(),
					baseCurrency, 1, unit, -1);
			transVector.add(transactionDetail);
		}
	}

	Employee_n[] m_emps;
	boolean m_ceksearch = false;

	public void doSearch(String[] attr, String operator) {
		java.util.ArrayList list = new java.util.ArrayList();
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

		String date = m_yearField.getValue() + "-"
				+ (m_monthComboBox.getMonth() + 1) + "-1";
		Unit m_unit = (Unit) m_unitPicker.getObject();
		try {
			m_emps = getEmployeeBy_Criteria(date, m_unit.getIndex(), attr,
					operator);
		} catch (Exception ex) {
		}
		m_ceksearch = true;
		presentingSubmittedData();
	}

	private EmployeePayrollSubmit[] getEmpPaySubHasSearch(
			EmployeePayrollSubmit[] empPaySub, Employee_n[] emps) {
		Hashtable tbl = new Hashtable();
		List resultList = new ArrayList();
		resultList.clear();
		for (int i = 0; i < emps.length; i++)
			tbl.put(new Long(emps[i].getAutoindex()), emps[i].getEmployeeno());
		for (int i = 0; i < empPaySub.length; i++) {
			if (tbl.containsKey(new Long(empPaySub[i].getEmployeeIndex())))
				resultList.add(empPaySub[i]);
		}
		empPaySub = (EmployeePayrollSubmit[]) resultList
				.toArray(new EmployeePayrollSubmit[resultList.size()]);
		return empPaySub;
	}

	public double m_grandtotal = 0;

	private void presentingDataTable(PayrollComponent[] m_payrollComponents) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex = -1;
		int no = 0;
		int leftMargin = 0;
		int row = -1; // i add this
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		transVector.clear();

		m_rows = new Vector();

		double total = 0;

		m_grandtotal = 0;
		JournalStandardSettingPickerHelper help = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.ATTR_PAYROLL_COMPONENT);
		List jsList = help
				.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES);
		JournalStandardSetting jss = (JournalStandardSetting) jsList.get(0);
		JournalStandard js = jss.getJournalStandard();
		JournalStandardAccount[] jsa = js.getJournalStandardAccount();

		if (m_ceksearch) {
			m_empPayrollSubmit = getEmpPaySubHasSearch(m_empPayrollSubmit,
					m_emps);
		}

		if (m_empPayrollSubmit != null)
			for (int i = 0; i < m_empPayrollSubmit.length; i++) {
				if (m_empPayrollSubmit[i] != null) {
					if (tempIndex != m_empPayrollSubmit[i].getEmployeeIndex()) {
						if (m_data.size() > 0) {
							m_grandtotal += total;
							m_table.addRow(m_data);
						}
						m_data = new Vector();
						tableContent(m_empPayrollSubmit[i], no++);
						tempIndex = m_empPayrollSubmit[i].getEmployeeIndex();
						leftMargin = m_data.size();
						row++; // i add this
						total = 0;
					}

					int colIdx = helper.getColumnIndex(m_empPayrollSubmit[i]
							.getPayrollComponentIndex());
					PayrollComponent payrollComponent = helper
							.getPayrollComponent(m_empPayrollSubmit[i]
									.getPayrollComponentIndex());

					if (colIdx != -1) {
						Account accHelper = m_payrollComponents[colIdx]
								.getAccount();
						Account acc = getStandardAccount(jsa, accHelper);

						double value = m_empPayrollSubmit[i].getValue();
						value = getValueCekType(payrollComponent, value);

						Object content = null;
						if (value != -1) {
							if (acc != null) {
								if (acc.equals(accHelper)) {
									if (acc.getBalance() == 0)
										total += value;
									else
										total -= value;
								}
							}

							PayrollComponent payComp = helper
									.getPayrollComponent(m_empPayrollSubmit[i]
											.getPayrollComponentIndex());

							// tambahan untuk penanda row
							if ((payComp.getPaymentAsString() == PayrollComponent.PAYCHEQUE)
									&& (payComp.getSubmitAsString() == PayrollComponent.EMPLOYEE_RECEIVABLES)) {
								HRMBusinessLogic logic = new HRMBusinessLogic(
										m_conn);
								try {
									PayrollCategoryComponent[] pccs = logic
											.getSelectedEmployeePayrollComponent(
													m_sessionid,
													IDBConstants.MODUL_MASTER_DATA,
													m_empPayrollSubmit[i]
															.getEmployeeIndex(),
													payComp.getIndex());
									if (pccs.length > 0)
										m_rows.add(new Integer(row));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							content = new Double(value);
							transactionDetail = new TransactionDetail(payComp
									.getAccount(), ((Double) content)
									.doubleValue(), baseCurrency, 1, unit, -1);
							transVector.add(transactionDetail);
						} else {
							content = "";
						}
						if (value > 0)
							content = new Double(value);
						else
							content = "";
						helper.addDataAtColumn(m_data, colIdx + leftMargin,
								content);
						if (total > 0) {
							helper.addDataAtColumn(m_data, leftMargin
									+ helper.getColumnCount(),
									new Double(total));
						} else if (total < 0) {
							helper.addDataAtColumn(m_data, leftMargin
									+ helper.getColumnCount(),
									new Double(total));
						} else {
							helper.addDataAtColumn(m_data, leftMargin
									+ helper.getColumnCount(), "");
						}

						m_empPayrollSubmit[i].setRowComponentAtTable(row);
						m_empPayrollSubmit[i].setColComponentAtTable(colIdx
								+ leftMargin);

						// }
					}
				}
			}
		if (m_data.size() > 0) {
			m_grandtotal += total;
			m_data.addElement(new Double(total));
			m_table.addRow(m_data);
		}
		m_data = new Vector();
		m_data.addElement("");
		m_data.addElement("GRAND TOTAL");
		m_data.addElement("");
		loopEmptyByPayrollComponent(m_payrollComponents);
		m_data.addElement(new Double(m_grandtotal));
		m_table.addRow(m_data);
		m_table.setModelKu();

	}

	private void loopEmptyByPayrollComponent(
			PayrollComponent[] m_payrollComponents) {
		for (int j = 0; j < m_payrollComponents.length; j++) {
			m_data.addElement("");
		}
	}

	private double getValueCekType(PayrollComponent payrollComponent,
			double value) {
		if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[0])) {
			if (payrollComponent.getReportLabel().equals(
					PayrollComponentReportLabel.BONUS)
					|| payrollComponent.getReportLabel().equals(
							PayrollComponentReportLabel.THR))
				value = 0;
		} else if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[1])) {
			if (!payrollComponent.getReportLabel().equals(
					PayrollComponentReportLabel.THR))
				value = 0;
		} else {
			if (!payrollComponent.getReportLabel().equals(
					PayrollComponentReportLabel.BONUS))
				value = 0;
		}
		return value;
	}

	public String cekType() {
		String strType = (String) m_typecombo.getSelectedItem();
		return strType;
	}

	String[] m_GajiPokok = { "", "" }; // tunjangan asuransi
	String[] m_Kes = { "", "" }; // tunjangan Kesehatan
	String[] m_Mgmt = { "", "" }; // tunjangan mgmt
	String[] m_Tek = { "", "" }; // tunjangan tek
	String[] m_Rmh = { "", "" }; // tunjangan rmh
	String[] m_Lap = { "", "" }; // tunjangan lap
	String[] m_THR = { "", "" }; // tunjangan thr
	String[] m_Insertif = { "", "" }; // tunjangan insertif
	String[] m_Bonus = { "", "" }; // tunjangan bonus
	String m_asuransiPeg;
	String m_piutKePerusahaan;
	String m_piutKeYayasan;
	String m_total; // total

	private void presentingDataReport(EmployeePayrollSubmit[] empPaySub,
			PayrollComponent[] m_payrollComponents, DefaultTableModel model) {
		DecimalFormat m_formatDesimal = new DecimalFormat("#,##0.00");
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex = -1;
		int no = 0;
		helper.prepareColumnHelper(m_payrollComponents);

		m_data = new Vector();

		// transVector.clear();
		if (m_ceksearch) {
			empPaySub = getEmpPaySubHasSearch(empPaySub, m_emps);
		}
		int i = 0;
		int colIdx = 0;
		status = new Integer(1);

		JournalStandardSettingPickerHelper help = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.ATTR_PAYROLL_COMPONENT);
		List jsList = help
				.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES);
		JournalStandardSetting jss = (JournalStandardSetting) jsList.get(0);
		JournalStandard js = jss.getJournalStandard();
		JournalStandardAccount[] jsa = js.getJournalStandardAccount();

		double total = 0;
		m_grandtotal = 0;
		for (i = 0; i < empPaySub.length; i++) {
			if (empPaySub[i] != null) {
				if (tempIndex != empPaySub[i].getEmployeeIndex()) {
					if (m_data.size() > 0) {
						m_grandtotal += total;
						valueReportComponents();
						model.addRow(m_data);
						instaceReport();
					}
					total = 0;
					m_data = new Vector();
					reportContent(empPaySub[i], no++);
					tempIndex = empPaySub[i].getEmployeeIndex();
				}
				colIdx = helper.getColumnIndex(empPaySub[i]
						.getPayrollComponentIndex());
				PayrollComponent payrollComponent = helper
						.getPayrollComponent(empPaySub[i]
								.getPayrollComponentIndex());

				if (colIdx != -1) {
					Account accHelper = m_payrollComponents[colIdx]
							.getAccount();
					Account acc = getStandardAccount(jsa, accHelper);
					double value = empPaySub[i].getValue();
					value = getValueCekType(payrollComponent, value);
					if (value != -1) {
						if (acc != null) {
							if (acc != null)
								if (acc.equals(accHelper)) {
									if (acc.getBalance() == 0)
										total += value;
									else
										total -= value;
								}
						}

						if (colIdx == 0) {
							m_GajiPokok[0] = m_formatDesimal.format(value);
						} else if (colIdx == 1) {
							m_Kes[0] = m_formatDesimal.format(value);
						} else if (colIdx == 2) {
							m_Mgmt[0] = m_formatDesimal.format(value);
						} else if (colIdx == 3) {
							m_Tek[0] = m_formatDesimal.format(value);
						} else if (colIdx == 4) {
							m_Rmh[0] = m_formatDesimal.format(value);
						} else if (colIdx == 5) {
							m_THR[0] = m_formatDesimal.format(value);
						} else if (colIdx == 6) {
							m_Lap[0] = m_formatDesimal.format(value);
						} else if (colIdx == 7) {
							m_Insertif[0] = m_formatDesimal.format(value);
						} else if (colIdx == 8) {
							m_Bonus[0] = m_formatDesimal.format(value);
						}
						if (colIdx == 9) {
							m_GajiPokok[1] = m_formatDesimal.format(value);
						} else if (colIdx == 10) {
							m_Kes[1] = m_formatDesimal.format(value);
						} else if (colIdx == 11) {
							m_Mgmt[1] = m_formatDesimal.format(value);
						} else if (colIdx == 12) {
							m_Tek[1] = m_formatDesimal.format(value);
						} else if (colIdx == 13) {
							m_Rmh[1] = m_formatDesimal.format(value);
						} else if (colIdx == 14) {
							m_THR[1] = m_formatDesimal.format(value);
						} else if (colIdx == 15) {
							m_Lap[1] = m_formatDesimal.format(value);
						} else if (colIdx == 16) {
							m_Insertif[1] = m_formatDesimal.format(value);
						} else if (colIdx == 17) {
							m_Bonus[1] = m_formatDesimal.format(value);
						} else if (colIdx == 18) {
							m_asuransiPeg = m_formatDesimal.format(value);
						} else if (colIdx == 19) {
							m_piutKePerusahaan = m_formatDesimal.format(value);
						} else if (colIdx == 20) {
							m_piutKeYayasan = m_formatDesimal.format(value);
						}
						m_total = m_formatDesimal.format(total);
					}
				}
			}
			// tmpEmp = empPaySub[i];
		}
		if (m_data.size() > 0) {
			m_grandtotal += total;
			valueReportComponents();
			model.addRow(m_data);
		}

		m_data = new Vector();
		m_data.addElement("");
		m_data.addElement("");
		m_data.addElement("");
		m_data.addElement("");
		m_data.addElement("GRAND TOTAL");
		m_data.addElement("");
		m_data.addElement("");
		loopEmptyByPayrollComponent(m_payrollComponents);
		m_data.addElement(m_formatDesimal.format(m_grandtotal));
		m_data.addElement(status);
		model.addRow(m_data);
	}

	private Account getStandardAccount(JournalStandardAccount[] jsa, Account acc) {
		Account accResult = null;
		for (int i = 0; i < jsa.length; i++) {
			if (jsa[i].getAccount().equals(acc))
				accResult = jsa[i].getAccount();
		}
		return accResult;
	}

	private Employee_n[] getEmployeeBy_Criteria(String date, long unitId,
			String[] attr, String operator) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
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
		if (attr[0].equals("") && attr[1].equals(""))
			data = " ";
		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,"
				+ "job.name as jobtitle "
				+ "from employee emp "
				+ "inner join "
				+ "(select e.* from employeeemployment e, "
				+ "(select employee, max(tmt) tmt from (select * from employeeemployment where tmt<'"
				+ date
				+ "') "
				+ "group by employee ) lastemp "
				+ "where e.employee=lastemp.employee "
				+ "and e.tmt=lastemp.tmt and unit="
				+ unitId
				+ ") employment "
				+ "on emp.autoindex=employment.employee "
				+ "inner join jobtitle job on employment.jobtitle=job.autoindex "
				+ "where not exists (select employee from employeeretirement ret where ret.tmt<='"
				+ date
				+ "' and emp.autoindex=ret.employee) "
				+ data
				+ "order by emp.employeeno";
		System.out.println(query);
		Employee_n[] emps = logic.getEmployeeBy_Criteria(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, query);
		return emps;
	}

	private void setEnabledForReceivablesPayrollComponent(
			PayrollColumnHelper helper, PayrollComponent[] components) {
		m_cols = new Vector();
		for (int i = 0; i < components.length; i++) {
			if ((components[i].getPaymentAsString() == PayrollComponent.PAYCHEQUE)
					&& (components[i].getSubmitAsString() == PayrollComponent.EMPLOYEE_RECEIVABLES))
				m_cols.add(new Integer(helper.getColumnIndex(components[i]
						.getIndex())));
		}
	}

	private void tableContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name = submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getJobTitle());
	}

	private void reportContent(EmployeePayrollSubmit empSubmit, int i) {
		m_data.addElement(getUnitDescription());
		m_data.addElement(getMonthYearString());
		m_data.addElement(String.valueOf(i + 1));
		m_data.addElement(empSubmit.getEmployeeNo());
		String name = empSubmit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(empSubmit.getJobTitle());
		EmployeeAccount[] accounts = getEmpAccount(empSubmit.getEmployeeIndex());
		if (accounts.length > 0) {
			m_data.addElement(accounts[0].getAccountNo());
			m_data.addElement(accounts[0].getAccountName());
		} else {
			m_data.addElement("");
			m_data.addElement("");
		}
	}

	/*
	 * private void reportContent() { m_data.addElement("");
	 * m_data.addElement(""); m_data.addElement(""); m_data.addElement("");
	 * m_data.addElement(""); // menampilkan name m_data.addElement("");
	 * m_data.addElement(""); m_data.addElement("TOTAL"); }
	 */

	private EmployeeAccount[] getEmpAccount(long index) {
		EmployeeAccount[] account = null;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			account = logic.getEmployeeAccount(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;

	}

	public String getUnitDescription() {
		Unit unit = (Unit) m_unitPicker.getObject();
		if (unit == null)
			return "";
		String unitDesc = unit.getDescription();
		return unitDesc;
	}

	public String getMonthYearString() {
		return getMount(m_monthComboBox.getMonth()) + " - "
				+ m_yearField.getValue();
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

	private void setTextfieldStatus(int status,
			EmployeePayrollSubmit empPayrollSubmit) {
		if (status == PayrollSubmitStatus.NOTSUBMITTED) {
			m_statusTextField.setText(" Not Submitted");
			m_submitdateTextField.setText("");
			m_verifydate.setDate(new java.util.Date());
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(false);
		} else if (status == PayrollSubmitStatus.SUBMITTED) {
			m_statusTextField.setText(" Submitted");
			m_submitdateTextField.setText(" "
					+ empPayrollSubmit.getSubmittedDate().toString());
			m_verifydate.setDate(new java.util.Date());
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(false);
		} else if (status == PayrollSubmitStatus.VERIFIED) {
			m_statusTextField.setText(" Verified");
			m_submitdateTextField.setText(" "
					+ empPayrollSubmit.getSubmittedDate().toString());
			Transaction trans = getTransaction(empPayrollSubmit);
			m_verifydate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" " + trans.getDescription());
			m_verifinoTextField.setText(" " + trans.getReference());
		} else if (status == PayrollSubmitStatus.POSTED) {
			m_statusTextField.setText(" Posted");
			m_submitdateTextField.setText(" "
					+ empPayrollSubmit.getSubmittedDate().toString());
			Transaction trans = getTransaction(empPayrollSubmit);
			m_verifydate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" " + trans.getDescription());
			m_verifinoTextField.setText(" " + trans.getReference());
		}
	}

	public Transaction getTransaction(EmployeePayrollSubmit employ) {
		AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
		Transaction transaction = null;
		try {
			transaction = accLogic.getTransaction(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, employ.getTransaction());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaction;
	}

	public EmployeePayrollSubmit getEmpPayrollSubmit() {
		unit = (Unit) m_unitPicker.getObject();
		EmployeePayrollSubmit employeepayroll = new EmployeePayrollSubmit();
		employeepayroll.setMonth(getMonth());
		employeepayroll.setYear(getYear());
		employeepayroll.setUnit(unit);
		Calendar calendar = Calendar.getInstance();
		employeepayroll.setSubmittedDate(calendar.getTime());
		employeepayroll.setPayrollType(PayrollSubmitType.PAYCHEQUE_SUBMIT);
		employeepayroll.setPaychequeType(getPaychequeType());
		return employeepayroll;
	}

	public int getPaychequeType() {
		String strval = (String) m_typecombo.getSelectedItem();
		if (strval.equals(IDBConstants.ATTR_PAYCHEQUE[0]))
			return 0;
		else if (strval.equals(IDBConstants.ATTR_PAYCHEQUE[1]))
			return 1;
		return -1;
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
			m_table.addTotalColumn();
			PayrollColumnHelper helper = new PayrollColumnHelper();
			helper.prepareColumnHelper(payrollComponents);
			setEnabledForReceivablesPayrollComponent(helper, payrollComponents);
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

	class PayrollTable extends StructuredTable {
		private static final long serialVersionUID = 1L;

		public TableCellEditor getCellEditor(int row, int column) {
			if (findCol(column) && findRow(row))
				return new DblCellEditor();
			return super.getCellEditor(row, column);
		}

		public boolean isCellEditable(int row, int column) {
			return (m_edit && findCol(column) && findRow(row));
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

		private boolean findRow(int row) {
			if (m_rows != null) {
				for (Enumeration e = m_rows.elements(); e.hasMoreElements();) {
					if (((Integer) e.nextElement()).intValue() == row) {
						return true;
					}
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

		public void addTotalColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("Total"));
		}

		public void setModelKu() {
			Enumeration enumeration = getColumnModel().getColumns();
			while (enumeration.hasMoreElements()) {
				TableColumn column = (TableColumn) enumeration.nextElement();
				column.setPreferredWidth(100);
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

	class PayrollTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {

			return false;
		}
	}

	class RightAllignmentCellRenderer extends DefaultTableCellRenderer {
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
