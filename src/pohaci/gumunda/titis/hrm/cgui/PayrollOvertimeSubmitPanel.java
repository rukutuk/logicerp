package pohaci.gumunda.titis.hrm.cgui;

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
import java.util.Locale;
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
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.application.StructuredTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.report.RptSummaryOvertime;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollOvertimeSubmitLogic;

public class PayrollOvertimeSubmitPanel extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Connection m_conn = null;
	public long m_sessionid = -1;
	public JTextField m_statusTextField;
	public JTextField m_submitdateTextField;
	UnitPicker m_unitPicker;
	JMonthChooser m_monthComboBox;
	JSpinField m_yearField;
	PayrollComponentSubmitPicker m_componentPicker;
	public JButton m_viewBt, m_submitBt,m_summaryBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;
	public PayrollTable m_table;
	//to get this @overTimeLESSThanOneHour value u have to run getOvertimeWorkingDay first
	public float overTimeLESSThanOneHour;
	Calendar cal = Calendar.getInstance();
    //to get this @overTimeMOREThanOneHourvalue u have to run getOvertimeWorkingDay first
	public float overTimeMOREThanOneHour;
	private float overTimeOfWorkingDay;
	private Vector tempVector;
	private Vector objecTempVector;
	public float overTimeZeroToSevenHour;
	public float overTimeEightHour;
	public float overTimeMoreThanNineHour;
	Employee_n[] m_emps = null;
	boolean m_show = false;
	public Vector payrollOvertimeVector=new Vector();
	public boolean updateFlag;
	Unit m_unit;
	EmployeeOvertimeSubmit[] m_empOverTimeSubmit;
	HRMBusinessLogic m_hrmLogic;
	private PayrollOvertimeSubmitLogic overLogic;

	public PayrollOvertimeSubmitPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		try {
			new HRMSQLSAP().getDefaultWorkingDay(m_conn);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		constructComponent();
		setEditable(false);
		m_hrmLogic = new HRMBusinessLogic(m_conn);
	}

	void constructComponent() {
		m_excellBt = new javax.swing.JToggleButton(new ImageIcon("../images/excell.gif"));
		m_excellBt.addActionListener(this);
		m_searchBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter2.gif"));
		m_searchBt.addActionListener(this);
		m_previewBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter.gif"));
		m_previewBt.addActionListener(this);

		m_statusTextField = new JTextField();
		m_statusTextField.setEditable(false);
		m_submitdateTextField = new JTextField();
		m_submitdateTextField.setEditable(false);
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
		m_submitBt.setEnabled(false); //disable it first

		m_summaryBt = new JButton("Summary Report");
		m_summaryBt.addActionListener(this);
		m_summaryBt.setEnabled(false);

		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Viewed by Payroll Component");

		JPanel centerPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel formPanel = new JPanel();
		JPanel viewPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(m_excellBt);
		buttonPanel.add(m_previewBt);
		buttonPanel.add(m_searchBt);
		buttonPanel.add(m_viewBt);
		buttonPanel.add(m_submitBt);
		buttonPanel.add(m_summaryBt);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		formPanel.setLayout(new GridBagLayout());
		Misc.setGridBagConstraints(formPanel, statuslabel, gridBagConstraints,
				0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_statusTextField,
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

		Misc.setGridBagConstraints(formPanel, submitlabel, gridBagConstraints,
				0, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_submitdateTextField,
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

		Misc.setGridBagConstraints(formPanel, unitlabel, gridBagConstraints, 0,
				2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1,
						2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_unitPicker, gridBagConstraints,
				2, 2, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, viewPanel, gridBagConstraints, 0,
				3, GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER,
				1, 1.0, 0.0, new Insets(1, 1, 1, 1));

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

		formPanel.setPreferredSize(new Dimension(450, 100));
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
		tempVector=new Vector();
		objecTempVector=new Vector();
	}

	public void setEditable(boolean editable){
		m_searchBt.setEnabled(editable);
		m_excellBt.setEnabled(editable);
		m_previewBt.setEnabled(editable);
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
			List dataList2 = new ArrayList();

			EmployeeOvertimeSubmit submitObject = getEmpOvertimeSubmit();
			  m_empOverTimeSubmit = queryEmpPayrollSubmit(submitObject);
			PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();

			Iterator iterator = getMaperDept();
			List totallist = new ArrayList();
			List deptlist = new ArrayList();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String transdate = dateFormat.format(new Date());

			RptSummaryOvertime beginjasper = new RptSummaryOvertime(m_conn,m_sessionid,this);
			RptSummaryOvertime addjasper = new RptSummaryOvertime(m_conn,m_sessionid,this);

			while (iterator.hasNext()){
				List dataList = new ArrayList();
				//List datalist2 = new ArrayList();
				Organization org = (Organization)iterator.next();
				for (int i=0;i<m_emps.length;i++){
					if(!map.containsKey(new Long(m_emps[i].getAutoindex()))){
						Employment[] employement = m_hrmLogic.getEmployeeEmployment(m_sessionid,IDBConstants.MODUL_MASTER_DATA,m_emps[i].getAutoindex());
						if (employement!=null){
							if (org.getIndex() == employement[0].getDepartment().getIndex()){
								map.put(m_emps[i],new Long(m_emps[i].getAutoindex()));
								dataList.add(m_emps[i]);
							}
						}
					}
					else{
						dataList2.add(m_emps[i]);
					}
				}
				Employee_n[] emps = (Employee_n[]) dataList.toArray(new Employee_n[dataList.size()]);
				List empList = Arrays.asList(emps);
				Collections.sort(empList);
				emps = (Employee_n[]) empList.toArray(new Employee_n[empList.size()]);

				if (emps.length>0){
					if (!first){
						beginjasper.setRptSummarySubmit(payrollComponents,m_empOverTimeSubmit,payrollComponentsDefault,emps,m_unitPicker,org,getMonthYearString(),transdate);
						first = true;
					}

					addjasper.setRptSummarySubmit(payrollComponents,m_empOverTimeSubmit,payrollComponentsDefault,emps,m_unitPicker,org,getMonthYearString(),transdate);
					totallist.add(addjasper.getTotal());
					deptlist.add(org);

					int size = addjasper.getJasperPrint().getPages().size();
					for (int j=0;j<size;j++){
						JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
						beginjasper.getJasperPrint().addPage(no++,page);
					}
				}
			}

			Employee_n[] emps2 = (Employee_n[]) dataList2.toArray(new Employee_n[dataList2.size()]);
			if (emps2.length>0){
				addjasper.setRptSummarySubmit(payrollComponents,m_empOverTimeSubmit,payrollComponentsDefault,emps2,m_unitPicker,null,getMonthYearString(),transdate);
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


	public void actionPerformed(ActionEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == m_excellBt){
			jaspertReport(true);
		}else if (e.getSource()==m_previewBt){
			jaspertReport(false);
		}else if (e.getSource()==m_summaryBt){
			getSummaryreport();
		}else if (e.getSource() == m_viewBt) {
			view(true,null,"");
		}else if(e.getSource()==m_submitBt){
			int val = JOptionPane.showConfirmDialog(this,
					"System will submit data for all payroll components with the type of " +
					"\n" +
					"overtime and for all employee within selected unit code." +
					"\n" +
					"Continue to submit?",
					"Submit Confirmation", JOptionPane.YES_NO_OPTION);
			if(val==JOptionPane.YES_OPTION)
				submit();
		}else if (e.getSource()==m_searchBt){
			if(!m_show) {
				m_show = true;
				SearchEmployeeDlg dlg = new SearchEmployeeDlg(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						this,m_conn, m_sessionid);
				dlg.setVisible(true);
				m_show = false;
			}
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	private void submit() {
		  HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		  EmployeeOvertimeSubmit[] employeeOvertimeSubmit=null;
		  if(payrollOvertimeVector.size()!=0){
			  employeeOvertimeSubmit =new EmployeeOvertimeSubmit[payrollOvertimeVector.size()];
			  payrollOvertimeVector.copyInto(employeeOvertimeSubmit);
		  }else{
			 // mealAllowanceData =new EmployeeMealAllowanceSubmit[0];
		  }
		  if(employeeOvertimeSubmit!=null){
			  try {
				  //boolean updateFlag=false;
				if(updateFlag){
					  for (int i = 0; i < employeeOvertimeSubmit.length; i++) {
						  logic.updateEmployeePayroll(m_sessionid,
								  IDBConstants.MODUL_MASTER_DATA,employeeOvertimeSubmit[i]);
					  }
					  return;
				  }else{
						  logic.createEmployeePayrollSubmit(m_sessionid,
								  IDBConstants.MODUL_MASTER_DATA,employeeOvertimeSubmit);
				  }
			  } catch (Exception e1) {
				  e1.printStackTrace();
			  }
		  }

		  overLogic.setTextfieldStatus(employeeOvertimeSubmit[0]);
	}


	public int getMonth(){
		return m_monthComboBox.getMonth() + 1;
	}
	public int getYear(){
		return m_yearField.getValue();
	}

	public  String getUnitDescription() {
		Unit unit = (Unit) m_unitPicker.getObject();
		if (unit==null) return "";
		String unitDesc =unit.getDescription();
		return unitDesc;
	}

	public String getMonthYearString() {
		return getMount(m_monthComboBox.getMonth()) + " - " + m_yearField.getValue();
	}

	void jaspertReport(boolean check){
		PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
		PayrollComponent[] payrollComponentsDefault = m_componentPicker
        .getRootPayrollComponents();
		new pohaci.gumunda.titis.hrm.cgui.report.HRMPayrollOvertimeJasper(this,payrollComponents,m_emps,m_unit,
				m_empOverTimeSubmit,payrollComponentsDefault,check);
	}

	public void view(boolean cekEmployee,String[] attr,String operator){
		//payrollOvertimeVector.clear();
		  m_unit = (Unit) m_unitPicker.getObject();
	      PayrollComponent[] payrollComponents = m_componentPicker
	          .getPayrollComponentsSelected();
	      PayrollComponent[] payrollComponentsDefault = m_componentPicker
          .getRootPayrollComponents();
	      
	      String date = m_yearField.getValue() + "-" + (m_monthComboBox.getMonth() + 2) + "-1";
			if (m_monthComboBox.getMonth()==11)
				date = (m_yearField.getValue()+1) + "-" + (m_monthComboBox.getMonth() + 1) + "-1";
			
	      java.util.ArrayList list = new java.util.ArrayList();
		  if (payrollComponents.length == 0)
			  list.add("Payroll Component");
		  if (m_unit ==null)
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
		  if (cekEmployee){
			  try{
				  m_emps = getEmployee(date, m_unit.getIndex());
			  }catch(Exception ex){
			  }
			  tableHeader(payrollComponents);
			  EmployeeOvertimeSubmit submitObject = getEmpOvertimeSubmit();
			  m_empOverTimeSubmit = queryEmpPayrollSubmit(submitObject);
			  overLogic = new PayrollOvertimeSubmitLogic(this,
					  (DefaultTableModel)this.m_table.getModel(),payrollComponents,m_emps,m_unit,
					  m_empOverTimeSubmit,payrollComponentsDefault,false);
			  overLogic.setIsnotFindEmployee( cekEmployee);
			  overLogic.tableBody();
			  setEditable(true);
		  } else {
			  try{
				  m_emps = getEmployeeBy_Criteria(date, m_unit.getIndex(),attr,operator);
			  }catch(Exception ex){
			  }
			  tableHeader(payrollComponents);
			  String query = getQuerySubmitByCriteria(attr,operator);
			  EmployeeOvertimeSubmit submitObject = getEmpOvertimeSubmit();
			  m_empOverTimeSubmit = queryEmpPayrollSubmitByCriteria(submitObject,query);
			  PayrollOvertimeSubmitLogic overLogic = new PayrollOvertimeSubmitLogic(this,
			    		(DefaultTableModel)this.m_table.getModel(),payrollComponents,m_emps,m_unit,
			    		m_empOverTimeSubmit,payrollComponentsDefault,false);
			  overLogic.setIsnotFindEmployee( cekEmployee);
			  overLogic.tableBody();
			  m_table.setModelKu();
		  }
		  m_summaryBt.setEnabled(true);
	  }

	private EmployeeOvertimeSubmit[] queryEmpPayrollSubmit(EmployeePayrollSubmit submitObject) {
		try {

			EmployeeOvertimeSubmit[] employeeOvertSub = (EmployeeOvertimeSubmit[])m_hrmLogic.getEmployeePayrollSubmit(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,submitObject);
			return employeeOvertSub;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private EmployeeOvertimeSubmit[] queryEmpPayrollSubmitByCriteria(EmployeePayrollSubmit submitObject,String query) {
		try {

			EmployeeOvertimeSubmit[] employeeOvertSub = (EmployeeOvertimeSubmit[])m_hrmLogic.getEmployeePayrollSubmitByCriteria(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,submitObject,query);
			return employeeOvertSub;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void tableHeader(PayrollComponent[] payrollComponents){
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
	}


	public EmployeePayrollSubmit dummyClone(EmployeePayrollSubmit eps) {
		  EmployeeOvertimeSubmit employeePayrollSubmit=new EmployeeOvertimeSubmit(m_sessionid,m_conn);
		  employeePayrollSubmit.setEmployee_n(eps.getEmployee_n());
		  employeePayrollSubmit.setJurnalID(eps.getJurnalID());
		  employeePayrollSubmit.setMonth(eps.getMonth());
		  employeePayrollSubmit.setPaymentPeriode(eps.getPaymentPeriode());
		  employeePayrollSubmit.setStatus(eps.getStatus());
		  employeePayrollSubmit.setSubmittedDate(eps.getSubmittedDate());
		  employeePayrollSubmit.setUnit(eps.getUnit());
		  employeePayrollSubmit.setYear(eps.getYear());
		  employeePayrollSubmit.setPayrollType(eps.getPayrollType());
		return employeePayrollSubmit;
	}

	public EmployeeOvertimeSubmit getEmpOvertimeSubmit() {
		EmployeeOvertimeSubmit submitObject = new EmployeeOvertimeSubmit(m_sessionid,m_conn);
		submitObject.setMonth(getMonth());
		submitObject.setYear(getYear());
		submitObject.setUnit(m_unit);
		submitObject.setStatus(PayrollSubmitStatus.SUBMITTED);
		Calendar calendar=Calendar.getInstance();
		submitObject.setSubmittedDate(calendar.getTime());
		submitObject.setPayrollType(PayrollSubmitType.OVERTIME);
		return submitObject;
	}

	private Employee_n[] getEmployee(String date, long unitId) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		Employee_n[] emps = logic.getEmployeeBy_unit(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, unitId, date);
		return emps;
	}

	private Employee_n[] getEmployeeBy_Criteria(String date, long unitId,String[] attr,String operator) throws Exception{
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

		String data = "";
		String item = "";
		if (!attr[0].equals("")){
			if (data.equals(""))
				item = "";
			else
				item = operator;
			data += item + "UPPER(emp.firstname& ' ' & emp.midlename& ' ' & emp.lastname) " +
			"LIKE '%"+ attr[0].toUpperCase() +"%' ";
		}
		if (!attr[1].equals("")){
			if (data.equals(""))
				item = "";
			else
				item = operator;
			data +=  item + " UPPER(job.name) like '%" + attr[1].toUpperCase() + "%' ";
		}
		data = "and (" + data + ")";
		if (attr[0].equals("") && attr[1].equals(""))
			data = " ";

		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname," +
					"job.name as jobtitle " +
					"from employee emp " +
					"inner join " +
					"(select e.* from employeeemployment e, " +
					"(select employee, max(tmt) tmt from (select *   from employeeemployment where tmt<'" + date + "') " +
					"group by employee ) lastemp " +
					"where e.employee=lastemp.employee " +
					"and e.tmt=lastemp.tmt and unit="+ unitId + ") employment " +
					"on emp.autoindex=employment.employee " +
					"inner join jobtitle job on employment.jobtitle=job.autoindex " +
					"where emp.autoindex NOT IN( SELECT employee FROM employeeretirement WHERE tmt<'" + date + "') " +
					data + " order by emp.employeeno";
		Employee_n[] emps = logic.getEmployeeBy_Criteria(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA,query);
		return emps;
	}

	public String getQuerySubmitByCriteria(String[] attr,String operator){
		  String data = "";
		  String item = "";
		  if (!attr[0].equals("")){
			  if (data.equals(""))
				  item = "";
			  else
				  item = operator;
			  data += item + "UPPER(d.firstname& ' ' & d.midlename& ' ' & d.lastname) " +
			  "LIKE '%"+ attr[0].toUpperCase() +"%' ";
		  }

		  if (!attr[1].equals("")){
			  if (data.equals(""))
				  item = "";
			  else
				  item = operator;
			  data +=  item + " UPPER(a.jobTitle) like '%" + attr[1].toUpperCase() + "%' ";
		  }

		  data = " and (" + data + ")";
		  if (attr[0].equals("") && attr[1].equals(""))
			  data = " ";
		  String query="select distinct a.*,b.*,b.submitteddate,d.employeeno,c.overtimemultiplier,c.overtimevalue,d.firstname," +
				"d.midlename,d.lastname from employeepayrolldetail a,employeepayroll b," +
				"overtimeattribute c,employee d where a.employeepayroll=b.autoindex  and " +
				"b.autoindex=c.employeepayroll and a.employee=d.autoindex"+
				" and c.employee=d.autoindex "+
				" and  b.yearpayrollsubmit="+getYear() +
				" and b.monthpayrollsubmit="+getMonth() +
				//" and b.unitcode="+ m_unit.getIndex() +
				" and b.unit="+ m_unit.getIndex() +
				" and b.payrolltype="+PayrollSubmitType.OVERTIME +
				data + " order by employee";
		  System.err.println(query);

		  return query;
	 }

	public PayrollCategoryComponent[] getPayrollCategoryComponents(
			Employee_n emp, PayrollComponent[] components) throws Exception {

		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		PayrollCategoryComponent[] comps = logic
				.getSelectedPayrollCategoryComponent(m_sessionid,
						IDBConstants.MODUL_MASTER_DATA, emp.getAutoindex(),
						components);
		return comps;
	}
//------------------------HOLIDAY
	public void initHoliday() {
		Locale locale = Locale.getDefault();
		Calendar calendar = Calendar.getInstance(locale);
		Date beginDate = null, endDate = null;
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(m_yearField.getValue(),m_monthComboBox.getMonth(),1);
		beginDate = roundToDate(tmpCalendar.getTime());
		tmpCalendar.set(m_yearField.getValue(),m_monthComboBox.getMonth(),31);
		endDate = roundToDate(tmpCalendar.getTime());

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			Holiday[] holidays = logic.getHoliday(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, beginDate, endDate);
			int i;
			clearVector();
			for (i = 0; i < holidays.length; i++) {
				Holiday h = holidays[i];
				addHoliday(h);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
	public Date roundToDate(Date d) {
		long rounded = d.getTime() - (d.getTime() % (24L * 60L * 60 * 1000L));
		return new Date(rounded);
	}
	private void addHoliday(Holiday holiday) {
		Date d = roundToDateForAddHoliday(holiday.getDate());
		if (!tempVector.contains(d)) {
			tempVector.add(d);
			objecTempVector.addElement(holiday);
		}
	}
	public void clearVector() {
		objecTempVector.clear();
		tempVector.clear();
	}
	private Date roundToDateForAddHoliday(Date d) {
		long rounded = d.getTime() + (7L * 60L * 60 * 1000L);
		return new Date(rounded);
	}
	public boolean isHoliday(Date date) {
		 date = roundToDateForAddHoliday(date);
		// System.out.println( " emplo = "+ date);
		if (tempVector.contains(date)) {
				return true;
		}
		return false;
	}
//-------------------------HOLIDAY

	public float getOvertimeWorkingDay(Employee_n emp) {
		overTimeOfWorkingDay = 0;
		overTimeLESSThanOneHour=0;
		overTimeMOREThanOneHour=0;
		initHoliday();
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			WorkingTimeState[] empOfficeWorkingTime = logic
					.getEmployeeOfficeWorkingTime(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA, emp.getAutoindex());
			for (int j = 0; j < empOfficeWorkingTime.length; j++) {
				WorkingTimeState workingTimeState=empOfficeWorkingTime[j];
				cal.setTime(workingTimeState.getDate());
				int y = cal.get(Calendar.YEAR);
				boolean isMonth =
					cal.get(Calendar.MONTH) == m_monthComboBox.getMonth();
				boolean isYear =
					y == m_yearField.getValue();

				if (isMonth && isYear &&  !isHoliday(workingTimeState.getDate())) {
					Date d= new Date(workingTimeState.getDate().getTime());
					Calendar cal = Calendar.getInstance();
					cal.setTime(d);
					//int day= d.getDay();
					int day = cal.get(Calendar.DAY_OF_WEEK);
					if(day==0 && new DefaultWorkingDay().getSunday() ){
						countingOverTimeOfWorkingDay(workingTimeState.getOverTime());
					}else if(day==1 && new DefaultWorkingDay().getMonday() ){
						countingOverTimeOfWorkingDay(workingTimeState.getOverTime());
					}else if(day==2 && new DefaultWorkingDay().getTuesday() ){
						countingOverTimeOfWorkingDay(workingTimeState.getOverTime());
					}else if(day==3 && new DefaultWorkingDay().getThursday() ){
						countingOverTimeOfWorkingDay(workingTimeState.getOverTime());
					}else if(day==4 && new DefaultWorkingDay().getWednesday() ){
						countingOverTimeOfWorkingDay(workingTimeState.getOverTime());
					}else if(day==5 && new DefaultWorkingDay().getFriday() ){
						countingOverTimeOfWorkingDay(workingTimeState.getOverTime());
					}else if(day==6 && new DefaultWorkingDay().getSaturday() ){
						countingOverTimeOfWorkingDay(workingTimeState.getOverTime());
					}
				}
			}
			return overTimeOfWorkingDay;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return overTimeOfWorkingDay;
	}
	private void countingOverTimeOfWorkingDay(float empOverTime){
		overTimeOfWorkingDay = overTimeOfWorkingDay + empOverTime;
		countingElementOfOvertime(empOverTime);
	}

	private void countingElementOfOvertime(float empOverTime) {
		if((empOverTime-1) <1){
			overTimeLESSThanOneHour=overTimeLESSThanOneHour+empOverTime;
		}else{
			overTimeLESSThanOneHour=overTimeLESSThanOneHour+1;
			overTimeMOREThanOneHour=overTimeMOREThanOneHour+empOverTime-1;
		}
	}

	public  float getOvertimeNonWorkingDay(Employee_n emp){
		overTimeZeroToSevenHour=0;
		overTimeEightHour=0;
		overTimeMoreThanNineHour=0;
		initHoliday();
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			WorkingTimeState[] empOfficeWorkingTime = logic
					.getEmployeeOfficeWorkingTime(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA, emp.getAutoindex());
			for (int j = 0; j < empOfficeWorkingTime.length; j++) {
				WorkingTimeState workingTimeState=empOfficeWorkingTime[j];
				cal.setTime(workingTimeState.getDate());
				int y = cal.get(Calendar.YEAR);
				boolean isMonth =
					cal.get(Calendar.MONTH) == m_monthComboBox.getMonth();
				boolean isYear =
					y == (m_yearField.getValue());

				if (isMonth && isYear  ) {
					Date d= new Date(workingTimeState.getDate().getTime());
					Calendar cal = Calendar.getInstance();
					cal.setTime(d);
					
					//int day= d.getDay();
					int day = cal.get(Calendar.DAY_OF_WEEK);
					
					if(day==0 && ! new DefaultWorkingDay().getSunday() ){
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}else if(day==1 && !new DefaultWorkingDay().getMonday() ){
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}else if(day==2 && !new DefaultWorkingDay().getTuesday() ){
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}else if(day==3 && !new DefaultWorkingDay().getThursday() ){
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}else if(day==4 && !new DefaultWorkingDay().getWednesday() ){
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}else if(day==5 && !new DefaultWorkingDay().getFriday() ){
						//System.out.println(" IN  froday");
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}else if(day==6 && !new DefaultWorkingDay().getSaturday() ){
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}else if(isHoliday(workingTimeState.getDate() )){
						//System.out.println(" holoday");
						countingOverTimeOfNonWorkingDay(workingTimeState.getOverTime());
					}
				}
			}
			return overTimeOfWorkingDay;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return overTimeOfWorkingDay;
	}

	private void countingOverTimeOfNonWorkingDay(float empOverTime){
		overTimeOfWorkingDay = overTimeOfWorkingDay + empOverTime;
		countingElementOfOvertimeOfNonWorkingDay(empOverTime);
	}

	private void countingElementOfOvertimeOfNonWorkingDay(float empOverTime) {

		if((empOverTime-7) <1){
			overTimeZeroToSevenHour=overTimeZeroToSevenHour+empOverTime;
		}else if( (empOverTime - 8)< 1 ){
			overTimeEightHour=overTimeEightHour+1;
			overTimeZeroToSevenHour=overTimeZeroToSevenHour+7;
		}else{
			overTimeEightHour=overTimeEightHour+1;
			overTimeZeroToSevenHour=overTimeZeroToSevenHour+7;
			overTimeMoreThanNineHour=overTimeMoreThanNineHour+empOverTime-8;

		}
	}

	public String getMount(int mount){
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

	public class PayrollTable extends StructuredTable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		PayrollTableModel m_tableModel;

		public PayrollTable(DefaultMutableTreeNode root) {
			super(root);
			((DefaultTableModel)getModel()).setRowCount(0);
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			//resetDefaultColumn();
		}

		public void resetDefaultColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTableHeaderRoot();
		      root.add(new DefaultMutableTreeNode("No"));
		      root.add(new DefaultMutableTreeNode("Name"));
		      root.add(new DefaultMutableTreeNode("Job Title"));
		      DefaultMutableTreeNode node = new DefaultMutableTreeNode("Overtime (hours)");
		      DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Working Days");
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

		public void setModelKu(){
			//setModel(m_tableModel);
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

			getColumnModel().getColumn(0).setPreferredWidth(200);

			for(int i=3;i<8;i++){
				getColumnModel().getColumn(i).setPreferredWidth(30);
				getColumnModel().getColumn(i).setMinWidth(50);
				getColumnModel().getColumn(i).setMaxWidth(50);
			}
		}

		public void clearRow(){
			((DefaultTableModel)getModel()).setRowCount(0);
		}

		public void addRow(Vector data){
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

	public void getOvertime(Employee_n employee_n) {
		clearOvertime();

		GenericMapper map = MasterMap.obtainMapperFor(Overtime.class);
		map.setActiveConn(m_conn);

		int month = m_monthComboBox.getMonth();
		int year = m_yearField.getValue();
		long empIdx = employee_n.getAutoindex();

		String whereClause = "periodMonth=" + month + " and periodYear=" + year + " and employee=" + empIdx;
		List overtimes = map.doSelectWhere(whereClause);

		Iterator iterator = overtimes.iterator();
		while(iterator.hasNext()) {
			Overtime overtime = (Overtime) iterator.next();

			OvertimeMultiplier multiplier = overtime.getMultiplier();

			if (multiplier.getType() == OvertimeMultiplier.WORKING_DAY) {
				if (multiplier.getHourMax() <= 1) {
					overTimeLESSThanOneHour = overtime.getOvertime();
				} else {
					overTimeMOREThanOneHour = overtime.getOvertime();
				}
			} else if (multiplier.getType() == OvertimeMultiplier.NON_WORKING_DAY) {
				if (multiplier.getHourMax() <= 7) {
					overTimeZeroToSevenHour = overtime.getOvertime();
				} else if ((multiplier.getHourMin() >= 7) && (multiplier.getHourMax() <= 8)) {
					overTimeEightHour = overtime.getOvertime();
				} else {
					overTimeMoreThanNineHour = overtime.getOvertime();
				}
			}
		}
	}

	private void clearOvertime() {
		overTimeLESSThanOneHour = 0;
		overTimeMOREThanOneHour = 0;
		overTimeZeroToSevenHour = 0;
		overTimeEightHour = 0;
		overTimeMoreThanNineHour = 0;
	}
}
