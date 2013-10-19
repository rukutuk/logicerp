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
import pohaci.gumunda.titis.hrm.cgui.EmployeePayroll;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeeTransportationAllowance;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSelectionTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSubmitPicker;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentTree;
import pohaci.gumunda.titis.hrm.cgui.PayrollSubmitType;
import pohaci.gumunda.titis.hrm.cgui.report.RptSummaryTran;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class PayrollTransportationAllowanceVerificationPanel extends PayrollVerificationPanel implements
ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;

	JTextField m_statusTextField, m_submitdateTextField, m_verifinoTextField,
	m_descriptionTextField;
	DatePicker m_verifyDate;
	JButton m_viewBt, m_submitBt, m_saveBt, m_cancelBt, m_calculateBt, m_verifyBt,m_summarybt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;

	private PayrollTable m_table;
	private Vector m_data;
	private EmployeeTransportationAllowance[] employeeTransportation;
	private Unit unit;
	private JComboBox periodeCombox;

	private TransactionDetail transactionDetail;
	private Vector transVector=new Vector();
	private JournalStandardSetting m_journalStandardSetting;
	private BaseCurrency baseCurrency;
	private HRMBusinessLogic m_hrmLogic;
	private AccountingBusinessLogic m_accLogic;

	public PayrollTransportationAllowanceVerificationPanel(Connection conn,
			long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_hrmLogic = new HRMBusinessLogic(m_conn);
		m_accLogic = new  AccountingBusinessLogic(m_conn);
		constructComponent();
		getJournalStandardSettings();
		baseCurrency = BaseCurrency.createBaseCurrency(m_conn, m_sessionid);
	}

	private void getJournalStandardSettings() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_MASTER_DATA);

		List list = helper
		.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES);
		m_journalStandardSetting = (JournalStandardSetting) list.get(0);
	}

	void constructComponent() {
		m_excellBt = new javax.swing.JToggleButton(new ImageIcon("../images/excell.gif"));
		m_excellBt.addActionListener(this);
		m_searchBt = new javax.swing.JToggleButton(new ImageIcon("../images/refresh.gif"));
		m_searchBt.addActionListener(this);
		m_previewBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter2.gif"));
		m_previewBt.addActionListener(this);

		m_statusTextField = new JTextField();
		m_statusTextField.setEditable(false);
		m_submitdateTextField = new JTextField();
		m_submitdateTextField.setEditable(false);
		m_verifinoTextField = new JTextField();
		m_verifinoTextField.setEditable(false);
		m_verifyDate = new DatePicker();
		m_verifyDate.setDate(new Date());
		m_descriptionTextField = new JTextField();

		m_unitPicker = new UnitPicker(m_conn, m_sessionid);
		m_monthComboBox=new JMonthChooser(JMonthChooser.NO_SPINNER);

		m_yearField = new JSpinField();
		m_yearField.setMaximum(9999);
		m_yearField.setValue(new java.util.GregorianCalendar().get(java.util.Calendar.YEAR));
		m_componentPicker = new PayrollComponentSubmitPicker(m_conn, m_sessionid, PayrollComponentTree.TRANSPORTATION_ALLOWANCE);

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

		m_summarybt = new JButton("Summary Report");
		m_summarybt.addActionListener(this);
		m_summarybt.setEnabled(false);

		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Viewed by Payroll Component");
		JLabel periodeLabel = new JLabel("Periode");

		JLabel verifynolabel = new JLabel("Verfication No");
		JLabel verifydatelabel = new JLabel("Verfication Date");
		JLabel descriptionlabel = new JLabel("Description*");

		JPanel centerPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel formPanel = new JPanel();
		JPanel viewPanel = new JPanel();

		periodeCombox = new JComboBox();
		periodeCombox.addItem("Periode 1 (1-15)");
		periodeCombox.addItem("Periode 2 (16-31)");

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// buttonPanel.add(m_excellBt);
		//buttonPanel.add(m_searchBt);
		//buttonPanel.add(m_previewBt);
		buttonPanel.add(m_viewBt);
		//buttonPanel.add(m_submitBt);
		//buttonPanel.add(m_saveBt);
		//buttonPanel.add(m_cancelBt);
		//buttonPanel.add(m_calculateBt);
		buttonPanel.add(m_verifyBt);
		buttonPanel.add(m_summarybt);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		formPanel.setLayout(new GridBagLayout());
		Misc.setGridBagConstraints(formPanel, submitlabel, gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_submitdateTextField, gridBagConstraints, 2, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, monthlabel, gridBagConstraints, 3, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_monthComboBox, gridBagConstraints, 5, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, verifynolabel, gridBagConstraints, 0, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_verifinoTextField, gridBagConstraints, 2, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, yearlabel, gridBagConstraints, 3, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_yearField, gridBagConstraints, 5, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, statuslabel, gridBagConstraints, 0, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_statusTextField, gridBagConstraints, 2, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, unitlabel, gridBagConstraints, 3, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_unitPicker, gridBagConstraints, 5, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, verifydatelabel, gridBagConstraints, 0, 3,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 3,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_verifyDate, gridBagConstraints, 2, 3,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, periodeLabel, gridBagConstraints, 3, 3,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, periodeCombox, gridBagConstraints, 5, 3,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, viewPanel, gridBagConstraints, 0, 4,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(formPanel, descriptionlabel, gridBagConstraints, 0, 5,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 5,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_descriptionTextField, gridBagConstraints, 2,5,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		viewPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(viewPanel, viewlabel, gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(viewPanel, new JLabel(" "), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(viewPanel, m_componentPicker, gridBagConstraints, 2, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		northPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		JPanel northleftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northleftPanel.add(northPanel);
		Misc.setGridBagConstraints(northPanel, buttonPanel, gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

		formPanel.setPreferredSize(new Dimension(600, 135));
		Misc.setGridBagConstraints(northPanel, formPanel, gridBagConstraints, 0, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(northPanel, new JPanel(), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEtchedBorder());

		m_table = new PayrollTable(new DefaultMutableTreeNode("Column"));
		centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);

		setLayout(new BorderLayout());
		add(northleftPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}

	public int getMonth(){
		return m_monthComboBox.getMonth() + 1;
	}
	public int getYear(){
		return m_yearField.getValue();
	}
	public void actionPerformed(ActionEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == m_viewBt) {
			presentingSubmittedData();
		}else if(e.getSource() == m_verifyBt){
			dataVerification();
		}
		else if (e.getSource()==m_summarybt){
			if (employeeTransportation.length>0 && employeeTransportation!=null){
				getSummaryreport();
			}
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	 public String getStrMount(int mount){
		  String m_mount = "";
		  switch (mount){
		  	case 0: m_mount = "January";break;
		  	case 1: m_mount = "February";break;
		  	case 2: m_mount = "March";break;
		  	case 3: m_mount = "April";break;
		  	case 4: m_mount = "May";break;
		  	case 5: m_mount = "June";break;
		  	case 6: m_mount = "July";break;
		  	case 7: m_mount = "August";break;
		  	case 8: m_mount = "September";break;
		  	case 9: m_mount = "October";break;
		  	case 10: m_mount = "November";break;
		  	case 11: m_mount = "December";break;
		  }
		  return m_mount;
	  }

	  public String getMonthYearString() {
		  return getStrMount(m_monthComboBox.getMonth()) + " - " + m_yearField.getValue();
	  }

	private Iterator getMaperDept() {
		GenericMapper mapper=MasterMap.obtainMapperFor(Organization.class);
		mapper.setActiveConn(m_conn);
		String selectWhere = " 1=1 ";
		List list=mapper.doSelectWhere(selectWhere);
		Iterator iterator = list.iterator();
		return iterator;
	}

	public void getSummaryreport(){
		try {
			boolean first = false;
			int no=0;
			//RptSummary jasper = null;
			HashMap map = new HashMap();
			PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();

			RptSummaryTran beginjasper = new RptSummaryTran(m_conn,m_sessionid,this);
			RptSummaryTran addjasper = new RptSummaryTran(m_conn,m_sessionid,this);

			Iterator iterator = getMaperDept();
			List totallist = new ArrayList();
			List deptlist = new ArrayList();
			List dataList2 = new ArrayList();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String transdate = dateFormat.format(new Date());

			while (iterator.hasNext()){
				List dataList = new ArrayList();
				Organization org = (Organization)iterator.next();

				for (int i=0;i<employeeTransportation.length;i++){
					if(!map.containsKey(new Long(employeeTransportation[i].getEmployeeIndex()))){
						Employment[] employement = m_hrmLogic.getEmployeeEmployment(m_sessionid,IDBConstants.MODUL_MASTER_DATA,employeeTransportation[i].getEmployeeIndex());
						if (employement!=null){
							if (org.getIndex() == employement[0].getDepartment().getIndex()){
								map.put(employeeTransportation[i],new Long(employeeTransportation[i].getEmployeeIndex()));
								dataList.add(employeeTransportation[i]);
							}
						}
					}
					else{
						dataList2.add(employeeTransportation[i]);
					}
				}
				EmployeeTransportationAllowance[] emps = (EmployeeTransportationAllowance[]) dataList.toArray(new EmployeeTransportationAllowance[dataList.size()]);
				if (emps.length>0){
					if (!first){
						beginjasper.setRptSummaryVerivy(payrollComponents,emps,m_unitPicker,org,getMonthYearString(),transdate);
						first = true;
					}

					addjasper.setRptSummaryVerivy(payrollComponents,emps,m_unitPicker,org,getMonthYearString(),transdate);
					totallist.add(addjasper.getTotal());
					deptlist.add(org);

					int size = addjasper.getJasperPrint().getPages().size();
					for (int j=0;j<size;j++){
						JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
						beginjasper.getJasperPrint().addPage(no++,page);
					}
				}
			}
			EmployeeTransportationAllowance[] emps2 = (EmployeeTransportationAllowance[]) dataList2.toArray(new EmployeeTransportationAllowance[dataList2.size()]);
			if (emps2.length>0){
				addjasper.setRptSummaryVerivy(payrollComponents,emps2,m_unitPicker,null,getMonthYearString(),transdate);
				totallist.add(addjasper.getTotal());
				deptlist.add(null);
				int size = addjasper.getJasperPrint().getPages().size();
				for (int j=0;j<size;j++){
					JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
					beginjasper.getJasperPrint().addPage(no++,page);
				}
			}

			addjasper.setRptSummarySubmit2(totallist,deptlist,m_unitPicker,getMonthYearString(),transdate);
			int size = addjasper.getJasperPrint().getPages().size();
			for (int j=0;j<size;j++){
				JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
				beginjasper.getJasperPrint().addPage(no++,page);
			}

			if (no>0)
				beginjasper.getJasperPrint().removePage(no);
			beginjasper.getPrintView();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void setDeptTotalValue(){
		try {
			HashMap map = new HashMap();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();

			Iterator iterator = getMaperDept();

			while (iterator.hasNext()){
				List dataList = new ArrayList();
				Organization org = (Organization)iterator.next();
				for (int i=0;i<employeeTransportation.length;i++){
					if(!map.containsKey(new Long(employeeTransportation[i].getEmployeeIndex()))){
						Employment[] employement = m_hrmLogic.getEmployeeEmployment(m_sessionid,IDBConstants.MODUL_MASTER_DATA,employeeTransportation[i].getEmployeeIndex());
						if (employement!=null){
							if (org.getIndex() == employement[0].getDepartment().getIndex()){
								dataList.add(employeeTransportation[i]);
								map.put(employeeTransportation[i],new Long(employeeTransportation[i].getEmployeeIndex()));
							}
						}
					}
				}
				EmployeeTransportationAllowance[] emps = (EmployeeTransportationAllowance[]) dataList.toArray(new EmployeeTransportationAllowance[dataList.size()]);
				if (emps.length>0){
					PayrollColumnHelper helper = new PayrollColumnHelper();
					helper.prepareColumnHelper(payrollComponentsDefault);
					PayrollDeptValue payrollDeptValue = new PayrollDeptValue();
					payrollDeptValue.setOrganization(org);
					payrollDeptValue.setEmployeePayrollSubmit(employeeTransportation[0]);
					for (int i=0;i<emps.length;i++){
						if (emps[i].getValue()>0){
							PayrollComponent payrollComponent = helper.getPayrollComponent(emps[i].getPayrollComponentIndex());
							payrollDeptValue.setAccount(payrollComponent.getAccount());
							payrollDeptValue.setAccvalue(emps[i].getValue());
							m_accLogic.createPayrollDeptValue(m_sessionid,pohaci.gumunda.titis.accounting.dbapi.IDBConstants.MODUL_ACCOUNTING,payrollDeptValue);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dataVerification() {
		Calendar cal=Calendar.getInstance();
		Date date=cal.getTime();
		if (m_verifyDate.getDate()!=null)
			date = (Date) m_verifyDate.getDate();

		java.util.ArrayList list = new java.util.ArrayList();
		String description=m_descriptionTextField.getText();
		if (description.length()==0)
			list.add("Description");
		if(unit==null)
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
		ReferenceNoGeneratorHelper helper = new ReferenceNoGeneratorHelper(m_conn, m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, "");
		String refNo = helper.createPayrollReferenceNo(m_journalStandardSetting.getJournalStandard().getCode(), date);
		JournalStandard journalStandard = m_journalStandardSetting.getJournalStandard();
		Journal  journal=m_journalStandardSetting.getJournalStandard(). getJournal();
		Transaction transaction = new Transaction(-1,description,date,date,null,refNo,
				journal,journalStandard,(short) PayrollSubmitStatus.VERIFIED, unit);
		TransactionDetail[] transDetail = getTransactionDetail();

		// isi currency... lupa :)
		// cara yang buruk
		for(int i=0; i<transDetail.length; i++){
			TransactionDetail det = transDetail[i];
			det.setCurrency(baseCurrency);
		}

		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		transDetail = transHelper.prepareJournalStandardTransactionDetail(
				m_journalStandardSetting.getJournalStandard().getJournalStandardAccount(),
				transDetail, unit, baseCurrency, 1);

		/*try {
			transaction = m_accLogic.createTransactionData(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,transaction,transDetail);
			employeeTransportation[0].setStatus(PayrollSubmitStatus.VERIFIED);
			m_hrmLogic.updateEmployeePayrollVerified(transaction.getIndex(),m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,employeeTransportation[0]);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
*/
		try {
			/*transaction = m_accLogic.createTransactionData(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,transaction,transDetail);*/
			employeeTransportation[0].setStatus(PayrollSubmitStatus.VERIFIED);
			m_accLogic.createPayrollTransactionData(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,transaction,transDetail, employeeTransportation[0]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Unsuccessfully creating transaction data");
			e.printStackTrace();
		}

		/*try {
			employeeTransportation[0].setStatus(PayrollSubmitStatus.VERIFIED);
			m_hrmLogic.updateEmployeePayrollVerified(transaction.getIndex(),m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,employeeTransportation[0]);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Unsuccessfully updating payroll with transaction idx=" + transaction.getIndex());
			e1.printStackTrace();
		}*/
		m_statusTextField.setText(" Verified");
		m_verifinoTextField.setText(" " + refNo);
		m_verifyDate.setDate(date);
		m_verifyBt.setEnabled(false);
		setDeptTotalValue();
	}

	private TransactionDetail[] getTransactionDetail() {
		TransactionDetail[] transDetail=new TransactionDetail[transVector.size()];
		transVector.copyInto(transDetail);
		return transDetail;
	}

	protected void presentingSubmittedData() {
		PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
		EmployeeTransportationAllowance m_employeepayrollSubmit = setPayrollSubmitInitialisation();

		java.util.ArrayList list = new java.util.ArrayList();
		if (payrollComponents.length == 0)
			list.add("Payroll Component");
		if (unit ==null)
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
			employeeTransportation= (EmployeeTransportationAllowance[]) m_hrmLogic.getEmployeePayrollSubmit(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,m_employeepayrollSubmit);
			if(employeeTransportation!=null && employeeTransportation.length>0){
				employeeTransportation[0].setUnit((Unit) m_unitPicker.getObject());
				employeeTransportation[0].setMonth(getMonth());
				employeeTransportation[0].setYear(getYear());
				employeeTransportation[0].setPayrollType(PayrollSubmitType.TRANSPORTATION_ALLOWANCE);
				employeeTransportation[0].setPaymentPeriode(periodeCombox.getSelectedIndex());
				setTextfieldStatus(employeeTransportation[0].getStatus(),employeeTransportation[0]);
				if(employeeTransportation[0].getStatus( )== 1){
					m_verifyBt.setEnabled(true);
					m_summarybt.setEnabled(true);
				}
				else if (employeeTransportation[0].getStatus( )== 0)
					m_verifyBt.setEnabled(true);
				else{
					m_verifyBt.setEnabled(false);
					m_summarybt.setEnabled(true);
				}
			}else{
				setTextfieldStatus(PayrollSubmitStatus.NOTSUBMITTED,null);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		presentingData(employeeTransportation,payrollComponents);
		PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();
		preparingData(employeeTransportation,payrollComponentsDefault);
	}
	private void preparingData(EmployeePayrollSubmit[] empMealAllowSubmit,PayrollComponent[] payrollComponents ) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		helper.prepareColumnHelper(payrollComponents);
		m_data = new Vector();
		transVector.clear();
		for(int i=0;i<empMealAllowSubmit.length;i++){
			int colIdx = helper.getColumnIndex(empMealAllowSubmit[i].getPayrollComponentIndex());
			if(colIdx!=-1){
				double value=empMealAllowSubmit[i].getValue();
				Object content=null;
				if(value!= -1){
					long payindex=empMealAllowSubmit[i].getPayrollComponentIndex();
					float valuesaved=(float) helper.getValue(payindex);
					value = value + valuesaved;
					content = new Double(value);
					helper.putValue(payindex,(Double) content);
				}else{
					//do nothing
				}
			}
		}
		for(int v=0;v<payrollComponents.length;v++){
			long payindex = payrollComponents[v].getIndex();
			float dataValue=(float) helper.getValue(payindex);
			Double content =new Double(dataValue);
			transactionDetail = new TransactionDetail(payrollComponents[v].getAccount(),((Double)content).doubleValue(),baseCurrency,1, unit, -1);
			transVector.add(transactionDetail);
		}
	}

	private EmployeeTransportationAllowance setPayrollSubmitInitialisation() {
		unit = (Unit) m_unitPicker.getObject();
		EmployeeTransportationAllowance m_employeepayrollSubmit = new EmployeeTransportationAllowance();
		m_employeepayrollSubmit.setMonth(getMonth());
		m_employeepayrollSubmit.setYear(getYear());
		m_employeepayrollSubmit.setUnit(unit);
		m_employeepayrollSubmit.setStatus(PayrollSubmitStatus.SUBMITTED);
		Calendar calendar=Calendar.getInstance();
		m_employeepayrollSubmit.setSubmittedDate(calendar.getTime());
		m_employeepayrollSubmit.setPaymentPeriode(periodeCombox.getSelectedIndex());
		m_employeepayrollSubmit.setPayrollType(PayrollSubmitType.TRANSPORTATION_ALLOWANCE);
		return m_employeepayrollSubmit;
	}

	private void setTextfieldStatus(int status,EmployeePayrollSubmit employeeMealAll){
		if(status==PayrollSubmitStatus.NOTSUBMITTED){
			m_statusTextField.setText(" Not Submitted");
			m_submitdateTextField.setText("");
			m_verifyDate.setDate(new Date());
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(false);
		}else if(status==PayrollSubmitStatus.SUBMITTED){
			m_statusTextField.setText(" Submitted");
			m_submitdateTextField.setText(" "+employeeMealAll.getSubmittedDate().toString());
			m_verifyDate.setDate(new Date());
			m_verifinoTextField.setText("");
			m_descriptionTextField.setText("");
			m_verifyBt.setEnabled(false);
		}else if(status==PayrollSubmitStatus.VERIFIED){
			m_statusTextField.setText(" Verified");
			m_submitdateTextField.setText(" "+employeeMealAll.getSubmittedDate().toString());
			Transaction trans=getTransaction(employeeMealAll);
			m_verifyDate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" "+trans.getDescription());
			m_verifinoTextField.setText(" "+trans.getReference());
		}else if(status==PayrollSubmitStatus.POSTED){
			m_statusTextField.setText(" Posted");
			m_submitdateTextField.setText(" "+employeeMealAll.getSubmittedDate().toString());
			Transaction trans=getTransaction(employeeMealAll);
			m_verifyDate.setDate(trans.getVerifyDate());
			m_descriptionTextField.setText(" "+trans.getDescription());
			m_verifinoTextField.setText(" "+trans.getReference());
		}
	}
	private Transaction getTransaction(EmployeePayrollSubmit employ){
		Transaction transaction = null;
		try {
			transaction = m_accLogic.getTransaction(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,employ.getTransaction());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaction;
	}

	private void tableHeader(PayrollComponent[] payrollComponents) {
		PayrollComponentSelectionTree viewTree =
			new PayrollComponentSelectionTree(m_conn, m_sessionid, null, payrollComponents);
		if (payrollComponents.length > 0) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)m_table.getTableHeaderRoot();
			root.removeAllChildren();
			m_table.resetDefaultColumn();
			root.add((DefaultMutableTreeNode)viewTree.getModel().getRoot());
			m_table.rebuildTable();
			m_table.clearRow();
		}
		m_table.setModelKu();
	}

	public void presentingDataSummary(EmployeeTransportationAllowance[] empTransportAllow, PayrollComponent[] m_payrollComponents,DefaultTableModel model ) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		//transVector.clear();
		DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		double[] total ={0,0};
		double[] subtotal = {0,0};
		for(int i=0;i<empTransportAllow.length;i++){
			if(empTransportAllow[i]!=null){
				if(tempIndex!=empTransportAllow[i].getEmployeeIndex()){
					if (m_data.size()>0){
						total[0]+=subtotal[0];
						total[1]+=subtotal[1];
						m_data.addElement(new Integer(0));
						model.addRow(m_data);
					}
					m_data = new Vector();
					subtotal[0] =0;
					subtotal[1] = 0;
					summaryContent(empTransportAllow[i], no++);
					tempIndex= empTransportAllow[i].getEmployeeIndex();
					leftMargin = m_data.size();
				}
				int colIdx = helper.getColumnIndex(empTransportAllow[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empTransportAllow[i].getValue();
					String content=null;
					if(value!= -1){
						content = formatDesimal.format(value);
						subtotal[colIdx]=value;
						//PayrollComponent payComp=helper.getPayrollComponent(empTransportAllow[i].getPayrollComponentIndex());
						//transactionDetail = new TransactionDetail(payComp.getAccount(),value,baseCurrency,1, unit, -1);
						//transVector.add(transactionDetail);
					}else{
						content = "";
					}
					helper.addDataAtColumn(m_data, colIdx+ leftMargin, content);
				}
			}
		}
		if (m_data.size()>0) {
			total[0]+=subtotal[0];
			total[1]+=subtotal[1];
			m_data.addElement(new Integer(0));
			model.addRow(m_data);
		}
		String[] strTotal = getStrValue(total);
		model.addRow(new Object[]{"TOTAL PER DEPARTMENT","","","","","",strTotal[0],strTotal[1],new Integer(1)});
		setTotal(total);
	}

	public String[] getStrValue(double[] value){
		DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		String[] str ={"",""};
		if (value[0]>0)
			str[0]=formatDesimal.format(value[0]);
		if (value[1]>0)
			str[1]=formatDesimal.format(value[1]);
		return str;
	}

	double[] m_total ={0,0};
	public void setTotal(double[] total){
		m_total=total;
	}
	public double[] getTotal(){
		return m_total;
	}

	private void presentingData(EmployeeTransportationAllowance[] empTransportAllow, PayrollComponent[] m_payrollComponents) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		transVector.clear();
		for(int i=0;i<empTransportAllow.length;i++){
			if(empTransportAllow[i]!=null){
				if(tempIndex!=empTransportAllow[i].getEmployeeIndex()){
					if (m_data.size()>0)
						m_table.addRow(m_data);
					m_data = new Vector();
					tableContent(empTransportAllow[i], no++);
					tempIndex= empTransportAllow[i].getEmployeeIndex();
					leftMargin = m_data.size();
				}
				int colIdx = helper.getColumnIndex(empTransportAllow[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empTransportAllow[i].getValue();
					Object content=null;
					if(value!= -1){
						content = new Double(value);
						PayrollComponent payComp=helper.getPayrollComponent(empTransportAllow[i].getPayrollComponentIndex());
						transactionDetail = new TransactionDetail(payComp.getAccount(),((Double)content).doubleValue(),baseCurrency,1, unit, -1);
						transVector.add(transactionDetail);
					}else{
						content = "";
					}
					helper.addDataAtColumn(m_data, colIdx+ leftMargin, content);
				}
			}
		}
		if (m_data.size()>0) {
			m_data.addElement("");
			m_table.addRow(m_data);
		}
		m_table.setModelKu();
	}


	private void tableContent(EmployeeTransportationAllowance submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getJobTitle());
		int presence=submit.getPresence();
		int presenceLate=submit.getPresenceLate();
		int presenceNotLate=submit.getPresenceNotLate();
		m_data.addElement(new Integer(presence));
		m_data.addElement(new Integer(presenceNotLate));
		m_data.addElement(new Integer(presenceLate));
	}

	private void summaryContent(EmployeeTransportationAllowance submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getJobTitle());
		int presence=submit.getPresence();
		int presenceLate=submit.getPresenceLate();
		int presenceNotLate=submit.getPresenceNotLate();
		String strPresence="";
		String strPresenceLate = "";
		String strPresenceNotLate = "";
		if (presence>0)
			strPresence = String.valueOf(presence);
		if (presenceNotLate>0)
			strPresenceNotLate =String.valueOf(presenceNotLate);
		if (presenceLate>0)
			strPresenceLate =String.valueOf(presenceLate);
		m_data.addElement(strPresence);
		m_data.addElement(strPresenceNotLate);
		m_data.addElement(strPresenceLate);
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

		public void clearRow() {
			((DefaultTableModel)getModel()).setRowCount(0);
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

			for(int i=3;i<6;i++){
				getColumnModel().getColumn(i).setPreferredWidth(50);
				getColumnModel().getColumn(i).setMinWidth(50);
				getColumnModel().getColumn(i).setMaxWidth(50);
			}
		}

		public void resetDefaultColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("No"));
			root.add(new DefaultMutableTreeNode("Name"));
			root.add(new DefaultMutableTreeNode("Job Title"));
			root.add(new DefaultMutableTreeNode("Presence"));
			root.add(new DefaultMutableTreeNode("Not Late"));
			root.add(new DefaultMutableTreeNode("Late"));
		}

		public void setColumnSize() {
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);
		}

		public void addRow(Vector data) {
			((DefaultTableModel)getModel()).addRow(data);
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

	public void openAndLoadObject(StateTemplateEntity obj) {
		EmployeePayroll payroll = (EmployeePayroll) obj;
		m_monthComboBox.setMonth(payroll.getMonth()-1);
		m_yearField.setValue(payroll.getYear());
		m_unitPicker.setObject(payroll.getUnit());
		periodeCombox.setSelectedIndex(payroll.getPaymentPeriode());
		PayrollComponent rootComponent = new PayrollComponent(-1, "", "Payroll Component", true, null,
				Short.parseShort("-1"), Short.parseShort("-1"), null,"");
		m_componentPicker.setObject(rootComponent);
		presentingSubmittedData();
	}

}
