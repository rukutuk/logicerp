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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import pohaci.gumunda.titis.hrm.cgui.EmployeeMealAllowanceSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSelectionTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSubmitPicker;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollSubmitType;
import pohaci.gumunda.titis.hrm.cgui.report.RptSummaryInsuranceAllowance;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class PayrollInsuranceAllowanceVerificationPanel extends
		PayrollVerificationPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Connection m_conn = null;
	long m_sessionid = -1;

	JTextField m_statusTextField, m_submitdateTextField, m_verifinoTextField,
	/* m_verifydateTextField, */m_descriptionTextField;
	DatePicker m_verifyDate;
	JButton m_viewBt, m_submitBt, m_saveBt, m_cancelBt, m_calculateBt,
			m_verifyBt, m_summaryBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;

	public PayrollTable m_table;
	public Vector m_data;
	private EmployeePayrollSubmit[] employeePayrollSubmit;
	private Unit unit;

	private TransactionDetail transactionDetail;
	private Vector transVector = new Vector();
	private JournalStandardSetting m_journalStandardSetting;
	private BaseCurrency baseCurrency;
	private HRMBusinessLogic m_hrmLogic;
	private AccountingBusinessLogic m_accLogic;
	Employee_n[] m_emps = null;
	EmployeePayrollSubmit[] m_empPayrollSubmits;
	private RptSummaryInsuranceAllowance m_beginjasper, m_addjasper;

	public PayrollInsuranceAllowanceVerificationPanel(Connection conn,
			long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_hrmLogic = new HRMBusinessLogic(m_conn);
		m_accLogic = new AccountingBusinessLogic(m_conn);
		constructComponent();
		getJournalStandardSettings();
		baseCurrency = BaseCurrency.createBaseCurrency(m_conn, m_sessionid);
	}

	private void getJournalStandardSettings() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_MASTER_DATA);

		List list = helper
				.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_INSURANCE_ALLOWANCE);
		m_journalStandardSetting = (JournalStandardSetting) list.get(0);
	}

	void constructComponent() {
		m_excellBt = new javax.swing.JToggleButton(new ImageIcon(
				"../images/excell.gif"));
		m_excellBt.addActionListener(this);
		m_searchBt = new javax.swing.JToggleButton(new ImageIcon(
				"../images/refresh.gif"));
		m_searchBt.addActionListener(this);
		m_previewBt = new javax.swing.JToggleButton(new ImageIcon(
				"../images/filter2.gif"));
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
				m_sessionid, PayrollComponentTree.INSURANCE_ALLOWANCE);

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
		m_summaryBt = new JButton("Summary Report");
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

	public int getMonth() {
		return m_monthComboBox.getMonth() + 1;
	}

	public int getYear() {
		return m_yearField.getValue();
	}

	public void actionPerformed(ActionEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == m_excellBt) {
			long indexunit = 0;
			String tgl = getMount(m_monthComboBox.getMonth()) + " "
					+ m_yearField.getValue();
			Unit unit = (Unit) m_unitPicker.getObject();
			if (unit != null)
				indexunit = unit.getIndex();
			System.err.println("tgl :" + tgl + ", unit :" + indexunit);
			// pohaci.gumunda.titis.application.InsuranceAllowanceSubmit jasper
			// =
			new pohaci.gumunda.titis.application.InsuranceAllowanceSubmit(false);

		} else if (e.getSource() == m_previewBt) {

		} else if (e.getSource() == m_verifyBt) {
			dataVerification();
		} else if (e.getSource() == m_viewBt) {
			presentingSubmittedData();
		} else if (e.getSource() == m_summaryBt) {
			this.getSummaryReport();
		}// /
		this.setCursor(Cursor.getDefaultCursor());
	}

	public void presentingSubmittedData() {
		String date = m_yearField.getValue() + "-"
				+ (m_monthComboBox.getMonth() + 1) + "-1";
		PayrollComponent[] payrollComponents = m_componentPicker
				.getPayrollComponentsSelected();
		EmployeePayrollSubmit m_employeepayrollSubmit = new EmployeePayrollSubmit();
		setPayrollSubmitInitialisation(m_employeepayrollSubmit);
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

		try {
			m_emps = getEmployee(date, unit.getIndex());
		} catch (Exception e) {
			e.printStackTrace();
		}

		tableHeader(payrollComponents);
		try {
			employeePayrollSubmit = (EmployeePayrollSubmit[]) m_hrmLogic
					.getEmployeePayrollSubmit(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA,
							m_employeepayrollSubmit);

			if (employeePayrollSubmit != null
					&& employeePayrollSubmit.length > 0) {
				setPayrollSubmitInitialisation(employeePayrollSubmit[0]);
				setTextfieldStatus(employeePayrollSubmit[0].getStatus(),
						employeePayrollSubmit[0]);
				if (employeePayrollSubmit[0].getStatus() == 1) {
					m_verifyBt.setEnabled(true);
					// m_summaryBt.setEnabled(true);
				} else {
					m_verifyBt.setEnabled(false);
					// m_summaryBt.setEnabled(false);
				}
			} else {
				setTextfieldStatus(PayrollSubmitStatus.NOTSUBMITTED, null);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		presentingData(employeePayrollSubmit, payrollComponents);
		PayrollComponent[] payrollComponentsDefault = m_componentPicker
				.getRootPayrollComponents();
		preparingData(employeePayrollSubmit, payrollComponentsDefault);
	}

	public void presentingDataSummary(
			EmployeeMealAllowanceSubmit[] empMealAllowSubmit,
			PayrollComponent[] payrollComponents, DefaultTableModel model) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex = -1;
		int no = 0;
		int leftMargin = 0;
		helper.prepareColumnHelper(payrollComponents);
		m_data = new Vector();
		// transVector.clear();
		DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		double[] total = { 0, 0 };
		for (int i = 0; i < empMealAllowSubmit.length; i++) {
			if (empMealAllowSubmit[i] != null) {
				if (tempIndex != empMealAllowSubmit[i].getEmployeeIndex()) {
					if (m_data.size() > 0) {
						m_data.addElement(new Integer(0));
						model.addRow(m_data);
					}
					m_data = new Vector();
					summaryRptContent(empMealAllowSubmit[i], no++);
					tempIndex = empMealAllowSubmit[i].getEmployeeIndex();
					leftMargin = m_data.size();
				}
				int colIdx = helper.getColumnIndex(empMealAllowSubmit[i]
						.getPayrollComponentIndex());
				if (colIdx != -1) {
					double value = empMealAllowSubmit[i].getValue();
					String content = "";
					if (value != -1) {
						if (value > 0)
							content = formatDesimal.format(value);
						total[colIdx] += value;
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
			m_data.addElement(new Integer(0));
			model.addRow(m_data);
		}

		String[] strTotal = getStrValue(total);
		model.addRow(new Object[] { "TOTAL PER DEPARTMENT", "", "", "",
				strTotal[0], strTotal[1], new Integer(1) });
		setTotal(total);
	}

	private void summaryRptContent(EmployeeMealAllowanceSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name = submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getEmployeeNo());
		int presence = submit.getPresence();
		String strPresence = "";
		if (presence > 0)
			strPresence = String.valueOf(presence);
		m_data.addElement(strPresence);
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

	private Employee_n[] getEmployee(String date, long unitId) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		Employee_n[] emps = logic.getEmployeeBy_unit(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, unitId, date);
		return emps;
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
				for (int i = 0; i < employeePayrollSubmit.length; i++) {
					if (!map.containsKey(new Long(employeePayrollSubmit[i]
							.getEmployeeIndex()))) {
						Employment[] employement = m_hrmLogic
								.getEmployeeEmployment(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										employeePayrollSubmit[i]
												.getEmployeeIndex());
						if (employement != null) {
							if (org.getIndex() == employement[0]
									.getDepartment().getIndex()) {
								dataList.add(employeePayrollSubmit[i]);
								map.put(employeePayrollSubmit[i], new Long(
										employeePayrollSubmit[i]
												.getEmployeeIndex()));
							}
						}
					}
				}
				EmployeePayrollSubmit[] emps = (EmployeePayrollSubmit[]) dataList
						.toArray(new EmployeePayrollSubmit[dataList.size()]);
				if (emps.length > 0) {
					PayrollColumnHelper helper = new PayrollColumnHelper();
					helper.prepareColumnHelper(payrollComponentsDefault);
					PayrollDeptValue payrollDeptValue = new PayrollDeptValue();
					payrollDeptValue.setOrganization(org);
					payrollDeptValue.setEmployeePayrollSubmit(emps[0]);
					for (int i = 0; i < emps.length; i++) {
						if (emps[i].getValue() > 0) {
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

	public EmployeePayroll createEmpPayroll() {
		int month = m_monthComboBox.getMonth() + 1;
		int year = m_yearField.getValue();
		return new EmployeePayroll(m_conn, year, month, m_sessionid);
	}

	public void getSummaryReport() {
		try {
			boolean first = false;
			int no = 0;
			// RptSummary jasper = null;
			HashMap map = new HashMap();
			List dataList2 = new ArrayList();

			EmployeePayrollSubmit submitObject = getEmpPayrollSubmit();
			this.m_empPayrollSubmits = queryEmpPayrollSubmit(submitObject);
			PayrollComponent[] payrollComponents = m_componentPicker
					.getPayrollComponentsSelected();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker
					.getRootPayrollComponents();

			Iterator iterator = getMaperDept();
			List totallist = new ArrayList();
			List deptlist = new ArrayList();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String transdate = dateFormat.format(new Date());

			m_beginjasper = new RptSummaryInsuranceAllowance(m_conn,
					m_sessionid, this);
			m_addjasper = new RptSummaryInsuranceAllowance(m_conn, m_sessionid,
					this);

			while (iterator.hasNext()) {
				List dataList = new ArrayList();
				Organization org = (Organization) iterator.next();
				for (int i = 0; i < m_emps.length; i++) {
					if (!map.containsKey(new Long(m_emps[i].getAutoindex()))) {
						Employment[] employement = m_hrmLogic
								.getEmployeeEmployment(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										m_emps[i].getAutoindex());
						if (employement != null) {
							if (org.getIndex() == employement[0]
									.getDepartment().getIndex()) {
								map.put(m_emps[i], new Long(m_emps[i]
										.getAutoindex()));
								dataList.add(m_emps[i]);
							}
						}
					} else {
						dataList2.add(m_emps[i]);
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
						m_beginjasper.setRptSummarySubmit(payrollComponents,
								m_empPayrollSubmits, payrollComponentsDefault,
								emps, m_unitPicker, org, getMonthYearString(),
								transdate);
						first = true;
					}

					m_addjasper.setRptSummarySubmit(payrollComponents,
							m_empPayrollSubmits, payrollComponentsDefault,
							emps, m_unitPicker, org, getMonthYearString(),
							transdate);
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
				m_addjasper.setRptSummarySubmit(payrollComponents,
						m_empPayrollSubmits, payrollComponentsDefault, emps2,
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

			m_addjasper.setRptSummarySubmit2(totallist, deptlist, m_unitPicker,
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

	public EmployeePayrollSubmit getEmpPayrollSubmit() {
		EmployeePayrollSubmit submitObject = new EmployeePayrollSubmit();
		submitObject.setMonth(getMount());
		submitObject.setYear(getYear());
		submitObject.setUnit(this.unit);
		submitObject.setStatus(PayrollSubmitStatus.SUBMITTED);
		Calendar calendar = Calendar.getInstance();
		submitObject.setSubmittedDate(calendar.getTime());
		submitObject.setPayrollType(PayrollSubmitType.INSURANCE_ALLOWANCE);
		return submitObject;
	}

	private EmployeePayrollSubmit[] queryEmpPayrollSubmit(
			EmployeePayrollSubmit submitObject) {
		try {
			EmployeePayrollSubmit[] employeePaySubm;
			employeePaySubm = m_hrmLogic.getEmployeePayrollSubmit(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, submitObject);
			return employeePaySubm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getMount() {
		return m_monthComboBox.getMonth() + 1;
	}

	public String getMonthYearString() {
		return getMount(m_monthComboBox.getMonth()) + " - "
				+ m_yearField.getValue();
	}

	public String getUnitDescription() {
		return this.unit.getDescription();
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
		 * try { transaction = m_accLogic.createTransactionData(m_sessionid,
		 * IDBConstants.MODUL_MASTER_DATA, transaction, transDetail);
		 * employeePayrollSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
		 * m_hrmLogic.updateEmployeePayrollVerified(transaction.getIndex(),
		 * m_sessionid, IDBConstants.MODUL_MASTER_DATA,
		 * employeePayrollSubmit[0]); } catch (Exception e1) {
		 * e1.printStackTrace(); }
		 */

		try {
			/*
			 * transaction = m_accLogic.createTransactionData(m_sessionid,
			 * IDBConstants.MODUL_MASTER_DATA, transaction, transDetail);
			 */
			employeePayrollSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
			m_accLogic.createPayrollTransactionData(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, transaction, transDetail,
					employeePayrollSubmit[0]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Unsuccessfully creating transaction data");
			e.printStackTrace();
		}

		/*
		 * try {
		 * employeePayrollSubmit[0].setStatus(PayrollSubmitStatus.VERIFIED);
		 * m_hrmLogic.updateEmployeePayrollVerified(transaction.getIndex(),
		 * m_sessionid, IDBConstants.MODUL_MASTER_DATA,
		 * employeePayrollSubmit[0]); } catch (Exception e1) {
		 * JOptionPane.showMessageDialog(this, "Unsuccessfully updating payroll
		 * with transaction idx=" + transaction.getIndex());
		 * e1.printStackTrace(); }
		 */
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

	private void setPayrollSubmitInitialisation(
			EmployeePayrollSubmit employeepayrollSubmit) {
		unit = (Unit) m_unitPicker.getObject();
		// EmployeeMealAllowanceSubmit m_employeepayrollSubmit = new
		// EmployeeMealAllowanceSubmit();
		employeepayrollSubmit.setMonth(getMonth());
		employeepayrollSubmit.setYear(getYear());
		employeepayrollSubmit.setUnit(unit);
		/*
		 * Calendar calendar=Calendar.getInstance();
		 * employeepayrollSubmit.setSubmittedDate(calendar.getTime());
		 */
		employeepayrollSubmit
				.setPayrollType(PayrollSubmitType.INSURANCE_ALLOWANCE);
		// return employeepayrollSubmit;
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
			m_summaryBt.setEnabled(false);
		} else if (status == PayrollSubmitStatus.SUBMITTED) {
			m_statusTextField.setText(" Submitted");
			m_submitdateTextField.setText(" "
					+ employeeMealAll.getSubmittedDate().toString());
			m_verifyDate.setDate(new Date());
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(true);
			m_summaryBt.setEnabled(true);
		} else if (status == PayrollSubmitStatus.VERIFIED) {
			m_statusTextField.setText(" Verified");
			m_submitdateTextField.setText(" "
					+ employeeMealAll.getSubmittedDate().toString());
			Transaction trans = getTransaction(employeeMealAll);
			m_verifyDate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" " + trans.getDescription());
			m_verifinoTextField.setText(" " + trans.getReference());
			m_summaryBt.setEnabled(true);
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

	private void presentingData(EmployeePayrollSubmit[] empPaySub,
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
	}

	private void tableContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name = submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getJobTitle());
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
		}

		public void resetDefaultColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("No"));
			root.add(new DefaultMutableTreeNode("Name"));
			root.add(new DefaultMutableTreeNode("Job Title"));
		}

		public void setColumnSize() {
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);
		}

		public void clearRow() {
			((DefaultTableModel) getModel()).setRowCount(0);
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
