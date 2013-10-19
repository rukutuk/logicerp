package pohaci.gumunda.titis.hrm.cgui;

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
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.application.StructuredTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.report.RptSummaryTran;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollTransportationAllowanceSubmitLogic;

public class PayrollTransportationAllowanceSubmitPanel extends JPanel implements ActionListener {
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
	JButton m_viewBt, m_summarybt;
	public JButton m_submitBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;
	public PayrollTable m_table;
	public JComboBox periodeCombox;
	Employee_n[] m_emps = null;
	boolean m_show = false;
	Unit m_unit;
	public Vector payrollTransportationVector=new Vector();
	public boolean updateFlag;
	private PayrollTransportationAllowanceSubmitLogic transLogic;
	HRMBusinessLogic m_hrmLogic;
	EmployeeTransportationAllowance[] m_empTranSubmit;
	
	public PayrollTransportationAllowanceSubmitPanel(Connection conn,
			long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
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
				m_sessionid, PayrollComponentTree.TRANSPORTATION_ALLOWANCE);

		m_viewBt = new JButton("View");
		m_viewBt.addActionListener(this);

		m_submitBt = new JButton("Submit");
		m_submitBt.addActionListener(this);
		m_submitBt.setEnabled(false);
		
		m_summarybt = new JButton("Summary Report");
		m_summarybt.addActionListener(this);
		m_summarybt.setEnabled(false);

		periodeCombox = new JComboBox();
		periodeCombox.addItem("Periode 1 (1-15)");
		periodeCombox.addItem("Periode 2 (16-31)");

		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Viewed by Payroll Component");
		JLabel periodelabel = new JLabel("Periode");

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
		buttonPanel.add(m_summarybt);

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
		// -----------
		Misc.setGridBagConstraints(formPanel, unitlabel, gridBagConstraints, 0,
				2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1,
						2, 1, 1));

		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 1, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(formPanel, m_unitPicker, gridBagConstraints,
				2, 2, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0,
				new Insets(1, 1, 1, 1));
		// -------------new
		Misc.setGridBagConstraints(formPanel, periodelabel, gridBagConstraints,
				3, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0,
				new Insets(1, 2, 1, 1));
		Misc.setGridBagConstraints(formPanel, new JLabel(" "),
				gridBagConstraints, 4, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));
		Misc.setGridBagConstraints(formPanel, periodeCombox,
				gridBagConstraints, 5, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, new Insets(1, 1, 1, 1));

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
			
			EmployeePayrollSubmit submitObject = getEmployeepayrollSubmit();
			m_empTranSubmit = queryEmployeeTransportationAllowance(submitObject);
			PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();
			
			Iterator iterator = getMaperDept();
			List totallist = new ArrayList();
			List deptlist = new ArrayList();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String transdate = dateFormat.format(new Date());
			
			RptSummaryTran beginjasper = new RptSummaryTran(m_conn,m_sessionid,this);
			RptSummaryTran addjasper = new RptSummaryTran(m_conn,m_sessionid,this);
			
			while (iterator.hasNext()){
				List dataList = new ArrayList();
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
						beginjasper.setRptSummarySubmit(payrollComponents,m_empTranSubmit,payrollComponentsDefault,emps,m_unitPicker,org,getMonthYearString(),transdate);						
						first = true;
					}
					
					addjasper.setRptSummarySubmit(payrollComponents,m_empTranSubmit,payrollComponentsDefault,emps,m_unitPicker,org,getMonthYearString(),transdate);
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
				addjasper.setRptSummarySubmit(payrollComponents,m_empTranSubmit,payrollComponentsDefault,emps2,m_unitPicker,null,getMonthYearString(),transdate);
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
		}else if (e.getSource() == m_viewBt) {
			view(true,null,"");
		}else if (e.getSource()==m_summarybt){
			getSummaryreport();
		}else if (e.getSource()==m_searchBt){
			if(!m_show) {
				m_show = true;
				SearchEmployeeDlg dlg = new SearchEmployeeDlg(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), 
						this,m_conn, m_sessionid);
				dlg.setVisible(true);
				m_show = false;
			}
		}else if(e.getSource()==m_submitBt){
			int val = JOptionPane.showConfirmDialog(this,
					"System will submit data for all payroll components with the type of " + 
					":\n" + 
					"transportation allowance and for all employee within selected unit code." +
					":\n" + 
					"Continue to submit?", 
					"Submit Confirmation", JOptionPane.YES_NO_OPTION);
		  if(val==JOptionPane.YES_OPTION)
			submit();
		}
		// From serangkain 
		this.setCursor(Cursor.getDefaultCursor());
	}

	private void submit() {
		  HRMBusinessLogic hrmlogic = new HRMBusinessLogic(m_conn);
		  EmployeeTransportationAllowance[] transportationallowance=null;
		  if(payrollTransportationVector.size()!=0){
			  transportationallowance =new EmployeeTransportationAllowance[payrollTransportationVector.size()];
			  payrollTransportationVector.copyInto(transportationallowance);
		  }else{
			 // mealAllowanceData =new EmployeeMealAllowanceSubmit[0];
		  }		  
		  if(transportationallowance!=null){
			  try {
				if(updateFlag){
					  for (int i = 0; i < transportationallowance.length; i++) {
						  hrmlogic.updateEmployeePayroll(m_sessionid,
								  IDBConstants.MODUL_MASTER_DATA,transportationallowance[i]);
					  }
					  return;
				  }else{
						  hrmlogic.createEmployeePayrollSubmit(m_sessionid,
								  IDBConstants.MODUL_MASTER_DATA,transportationallowance);
				  }
				  
			  } catch (Exception e1) {
				  e1.printStackTrace();
			  }
		  }
		  transLogic.setTextfieldStatus(transportationallowance[0]);
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
		PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();
		new pohaci.gumunda.titis.hrm.cgui.report.HRMPayrollTransportationAllowanceJasper(this,payrollComponents,m_emps,m_unit,
				m_empTranSubmit,payrollComponentsDefault,check);		
	}
	
	public int getMonth(){
		  return m_monthComboBox.getMonth() + 1;
	}
	public int getYear(){
		  return m_yearField.getValue();
	}
	
	public void view(boolean cekEmployee,String[] attr,String operator){
		//payrollTransportationVector.clear(); 
		m_unit = (Unit) m_unitPicker.getObject();		  
		String date = m_yearField.getValue() + "-" + (m_monthComboBox.getMonth() + 2) + "-1";
		if (m_monthComboBox.getMonth()==11)
			date = (m_yearField.getValue()+1) + "-" + (m_monthComboBox.getMonth() + 1) + "-1";
		
		PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
		PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();
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
			EmployeePayrollSubmit submitObject = getEmployeepayrollSubmit();
			m_empTranSubmit = queryEmployeeTransportationAllowance(submitObject);
			transLogic = new PayrollTransportationAllowanceSubmitLogic(
					this,(DefaultTableModel)this.m_table.getModel(),payrollComponents,m_emps,m_unit,
					m_empTranSubmit,payrollComponentsDefault, false);
			transLogic.setIsnotFindEmployee( cekEmployee);
			transLogic.tableBody();
			setEditable(true);
		} else {		
			try{
				m_emps = getEmployeeBy_Criteria(date, m_unit.getIndex(),attr,operator);
			}catch(Exception ex){				    	
			}
			tableHeader(payrollComponents);
			String query = getQuerySubmitByCriteria(attr,operator);
			EmployeePayrollSubmit submitObject = getEmployeepayrollSubmit();
			m_empTranSubmit = queryEmployeeTransportationAllowanceByCriteria(submitObject,query);
			transLogic = new PayrollTransportationAllowanceSubmitLogic(
					this,(DefaultTableModel)this.m_table.getModel(),payrollComponents,m_emps,m_unit,
					m_empTranSubmit,payrollComponentsDefault, false);
			transLogic.setIsnotFindEmployee( cekEmployee);
			transLogic.tableBody();
			m_table.setModelKu();
		}
		m_summarybt.setEnabled(true);
	}
	
	private EmployeeTransportationAllowance[] queryEmployeeTransportationAllowance(EmployeePayrollSubmit submitObject) {
		try {
			EmployeeTransportationAllowance[] employeeTransport= (EmployeeTransportationAllowance[]) m_hrmLogic.getEmployeePayrollSubmit(
					m_sessionid,IDBConstants.MODUL_MASTER_DATA,submitObject);
			return employeeTransport;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private EmployeeTransportationAllowance[] queryEmployeeTransportationAllowanceByCriteria(EmployeePayrollSubmit submitObject,String query) {
		try {
			EmployeeTransportationAllowance[] employeeTransport = (EmployeeTransportationAllowance[])m_hrmLogic.getEmployeePayrollSubmitByCriteria(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,submitObject,query);
			return employeeTransport;
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
	        m_table.resetDefaultColumn(periodeCombox.getSelectedItem().toString());
	        root.add((DefaultMutableTreeNode)viewTree.getModel().getRoot());
	        m_table.rebuildTable();
	        m_table.clearRow();
    	}
	}
	
	public EmployeePayrollSubmit getEmployeepayrollSubmit() {
		EmployeePayrollSubmit employeepayrollSubmit;
		employeepayrollSubmit = new EmployeeTransportationAllowance();
		employeepayrollSubmit.setMonth(getMonth());
		employeepayrollSubmit.setYear(getYear());
		employeepayrollSubmit.setUnit(m_unit);
		employeepayrollSubmit.setStatus(PayrollSubmitStatus.SUBMITTED);
		Calendar calendar=Calendar.getInstance();
		employeepayrollSubmit.setSubmittedDate(calendar.getTime());
		employeepayrollSubmit.setPayrollType(PayrollSubmitType.TRANSPORTATION_ALLOWANCE);
		employeepayrollSubmit.setPaymentPeriode(periodeCombox.getSelectedIndex());
		return employeepayrollSubmit;
	}
	
	public EmployeePayrollSubmit dummyClone(EmployeePayrollSubmit payrollMealAllowanceData) {
		  EmployeePayrollSubmit employeePayrollSubmit=new EmployeeTransportationAllowance();
		  employeePayrollSubmit.setEmployee_n(payrollMealAllowanceData.getEmployee_n());
		  employeePayrollSubmit.setJurnalID(payrollMealAllowanceData.getJurnalID());
		  employeePayrollSubmit.setMonth(payrollMealAllowanceData.getMonth());
		  employeePayrollSubmit.setPaymentPeriode(payrollMealAllowanceData.getPaymentPeriode());
		  employeePayrollSubmit.setStatus(payrollMealAllowanceData.getStatus());
		  employeePayrollSubmit.setSubmittedDate(payrollMealAllowanceData.getSubmittedDate());
		  employeePayrollSubmit.setUnit(payrollMealAllowanceData.getUnit());
		  employeePayrollSubmit.setYear(payrollMealAllowanceData.getYear());
		  employeePayrollSubmit.setPayrollType(payrollMealAllowanceData.getPayrollType());
		  employeePayrollSubmit.setPaymentPeriode(payrollMealAllowanceData.getPaymentPeriode());
		  //System.out.println(employeePayrollSubmit.getPayrollType()+ " = "+payrollMealAllowanceData.getPayrollType());
		  
		return employeePayrollSubmit;
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
		if (attr[0].equals("") || attr[0].equals(""))
			data = " ";
			
		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname," +
					"job.name as jobtitle " +
					"from employee emp " +
					"inner join " +
					"(select e.* from employeeemployment e, " +
					"(select employee, max(tmt) tmt from (select * from employeeemployment where tmt<'" + date + "') " +
					"group by employee ) lastemp " +
					"where e.employee=lastemp.employee " +
					"and e.tmt=lastemp.tmt and unit="+ unitId + ") employment " +
					"on emp.autoindex=employment.employee " +
					"inner join jobtitle job on employment.jobtitle=job.autoindex " +
					"where emp.autoindex NOT IN( SELECT employee FROM employeeretirement WHERE tmt<'" + date + "') " +
					data + "order by emp.employeeno";
		System.err.println("query :" + query);
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
		  String query="select distinct a.*,b.*,b.submitteddate,d.employeeno,c.presence,c.presencelate,c.presencenotlate,d.firstname," +
				"d.midlename,d.lastname from employeepayrolldetail a,employeepayroll b, " +
				"transportationallowattr c,employee d where a.employeepayroll=b.autoindex  " +
				"and b.autoindex=c.employeepayroll and a.employee=d.autoindex "+
				" and c.employee=d.autoindex "+
				" and  b.yearpayrollsubmit="+getEmployeepayrollSubmit().getYear() +
				" and b.monthpayrollsubmit="+getEmployeepayrollSubmit().getMonth() +
				//" and b.unitcode="+ getEmployeepayrollSubmit().getUnit().getIndex()+
				" and b.unit="+ getEmployeepayrollSubmit().getUnit().getIndex()+
				" and b.paymentperiode="+ getEmployeepayrollSubmit().getPaymentPeriode()+//<----
				" and b.payrolltype="+getEmployeepayrollSubmit().getPayrollType() +
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


	public int presentLate(Employee_n emp,int periode) {
		int presentlate = 0;
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		Calendar cal = Calendar.getInstance();
		try {
			WorkingTimeState[] empOfficeWorkingTime = logic
					.getEmployeeOfficeWorkingTime(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA, emp.getAutoindex());
			for (int j = 0; j < empOfficeWorkingTime.length; j++) {
				cal.setTime(empOfficeWorkingTime[j].getDate());
				//int y = cal.getempOfficeWorkingTime[j].getDate().getYear() + 1900;
//				Date date = empOfficeWorkingTime[j].getDate();
				boolean isMonth = cal.get(Calendar.MONTH) == m_monthComboBox.getMonth();
				boolean isYear = cal.get(Calendar.YEAR) == (m_yearField
						.getValue());

				boolean isPresentLate = empOfficeWorkingTime[j].getState() == 1;
				boolean isDay = false;
				int day=cal.get(Calendar.DATE);
				if (periode == 0) {
					isDay = (day < 16) & (day > 0);
					if (isPresentLate && isYear && isMonth && isDay) {
						presentlate++;
						//System.out.println("periode = " + periode + " , day = "+day+"-------------------------------------");
					}
					
				} else if(periode == 1){
					isDay = (day < 32) & (day > 15);
					if (isPresentLate && isYear && isMonth && isDay) {
						presentlate++;
						//System.out.println("periode = " + periode + " , day = "+day+"-------------------------------------");
					}					
				}								
			}
			//System.out.println("presentLate = " + presentlate);
			return presentlate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return presentlate;

	}

	public int presentNotLate(Employee_n emp,int periode) {
		int presentNotlate = 0;
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		try {
			WorkingTimeState[] empOfficeWorkingTime = logic
					.getEmployeeOfficeWorkingTime(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA, emp.getAutoindex());
			Calendar cal = Calendar.getInstance();
			for (int j = 0; j < empOfficeWorkingTime.length; j++) {
				cal.setTime(empOfficeWorkingTime[j].getDate());
				//int y = cal.get(Calendar.YEAR);
				//Date date = empOfficeWorkingTime[j].getDate();
				boolean isMonth = cal.get(Calendar.MONTH) == m_monthComboBox.getMonth();
				boolean isYear = cal.get(Calendar.YEAR)  == (m_yearField
						.getValue());
				boolean isPresentLate = empOfficeWorkingTime[j].getState() == 0;
				boolean isDay = false;
				int day=cal.get(Calendar.DATE);
				if (periode == 0) {
					isDay = (day < 16) & (day > 0);
					if (isPresentLate && isYear && isMonth && isDay) {
						presentNotlate++;
						////System.out.println("periode = " + periode + " , day = "+day+"-------------------------------------");
					}
				} else if(periode == 1){
					isDay = (day < 32) & (day > 15);
					if (isPresentLate && isYear && isMonth && isDay) {
						presentNotlate++;
						////System.out.println("periode = " + periode + " , day = "+day+"-------------------------------------");
					}
				}
			}
			//System.out.println("presentNotLate = " + presentNotlate);
			return presentNotlate;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return presentNotlate;
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

	/*public void tableBody(PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit){
	//String date = year + "-" + (month + 1) + "-1";
	EmployeePayroll empPayroll = null;
	Vector data = null;
	PayrollCalcResult[] results;
	PayrollCategoryComponent[] comps;
	PayrollCalcResult res;
	int month = m_monthComboBox.getMonth() + 1;
	int year = m_yearField.getValue();
	empPayroll = new EmployeePayroll(m_conn, year, month,m_sessionid);
	EmployeePayrollSubmit employeepayrollSubmit = new EmployeeTransportationAllowance();
	employeepayrollSubmit.setMonth(month);
	employeepayrollSubmit.setYear(year);
	employeepayrollSubmit.setUnit(unit);
	employeepayrollSubmit.setStatus(1);
	Calendar calendar=Calendar.getInstance();
	employeepayrollSubmit.setSubmittedDate(calendar.getTime());
	employeepayrollSubmit.setPayrollType(PayrollSubmitType.TRANSPORTATION_ALLOWANCE);
	employeepayrollSubmit.setPaymentPeriode( periodeCombox.getSelectedIndex());
	  HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
	  EmployeeTransportationAllowance[] x=null;
	  try {
		  x= (EmployeeTransportationAllowance[]) logic.getEmployeePayrollSubmit(m_sessionid,
				  IDBConstants.MODUL_MASTER_DATA,employeepayrollSubmit);
		  
		  if(x!=null){
			  if(x.length>0){
				  if(x[0].getStatus()==1){
					  m_submitBt.setEnabled(false);
					  updateFlag=false;
				  } else{
					  m_submitBt.setEnabled(true);
					  updateFlag=true;
				  }
			  }else{
				  m_submitBt.setEnabled(true);
			  }
		  }
	  } catch (Exception e1) {
		  e1.printStackTrace();
	  }	  
	  if(m_submitBt.isEnabled()){
		  try {			
			  for (int i = 0; i < emps.length; i++) {
				  employeepayrollSubmit.setEmployee_n(emps[i]);
				  comps = getPayrollCategoryComponents(emps[i],
						  payrollComponents);
				  if (comps != null) {
					  empPayroll.setPeriode(periodeCombox.getSelectedIndex());
					  results = empPayroll.calcPayrollComponent(comps,emps[i].getAutoindex());
					  ////System.out.println("results " + results);
				  } else
					  results = null;
				  
				  data = new Vector();
				  data.addElement(String.valueOf(i + 1));
				  data.addElement(emps[i].getFirstname() + " "
						  + emps[i].getMidlename() + " "
						  + emps[i].getLastname());
				  data.addElement(emps[i].getJobtitle());
				  int presentlate = presentLate(emps[i],periodeCombox.getSelectedIndex());
				  int presentNotlate=presentNotLate(emps[i],periodeCombox.getSelectedIndex());
				  int presence=presentlate+presentNotlate;
				  data.addElement(new Integer(presence));
				  data.addElement(new Integer(presentNotlate));
				  data.addElement(new Integer(presentlate));
				  
				  EmployeeTransportationAllowance[] transportationAllowance=new EmployeeTransportationAllowance[payrollComponents.length];
				  for (int j = 0; j < payrollComponents.length; j++) {
					  res = null;
					  transportationAllowance[j] = (EmployeeTransportationAllowance) dummyClone(employeepayrollSubmit);	
					  transportationAllowance[j].setPayrollComponent(payrollComponents[j]);
					  transportationAllowance[j].setPresence(presence);
					  transportationAllowance[j].setPresenceLate(presentlate);
					  transportationAllowance[j].setPresenceNotLate(presentNotlate);
					  
					  if (results != null) {
						  for (int k = 0; k < results.length; k++) {
							  if (payrollComponents[j].getIndex() == results[k].component
									  .getIndex()) {
								  res = results[k];
								  break;
							  }
						  }
					  }
					  if (res != null) {						
						  data.addElement(new Double(res.value));
						  transportationAllowance[j].setValue((float)res.value);
						  payrollTransportationVector.add(transportationAllowance[j]);
					  } else {
						  data.addElement("");
					  }
				  }
				  m_table.addRow(data);
				  m_table.setModelKu();
			  }
		  } catch (Exception e1) {
			  e1.printStackTrace();
		  }
	  }else{
		//  Vector data = null;
			for(int k=0;k<x.length;k++){
				data = new Vector();
				  data.addElement(String.valueOf(k + 1));
				  Employee em=null;
				  try {
					  em = logic.getEmployeeByIndex(m_sessionid,
							  IDBConstants.MODUL_MASTER_DATA,x[k].getEmployeeIndex());
					  //System.out.println(x[k].getEmployeeIndex());
				  } catch (Exception e1) {
					  e1.printStackTrace();
				  }
				  String name="";
				  if(em!=null){
					  if(em.getMidleName().equals("")){
						  name=em.getFirstName()+" "+em.getLastName();
					  }else {
						  name=em.getFirstName()+" "+em.getMidleName()+" "+em.getLastName();
					  }
				  }
				  ////System.out.println(name);
				  data.addElement(name);
				  data.addElement(x[k].getJobTitle());
				  Integer presencE = new Integer(x[k].getPresence());
				  data.addElement(presencE);
				  data.addElement(new Integer(x[k].getPresenceNotLate()));
				  data.addElement(new Integer(x[k].getPresenceLate()));
				  for (int j = 0; j < payrollComponents.length; j++) {
					  if(x[k].getPayrollComponentIndex()==payrollComponents[j].getIndex()){
						  data.addElement(new Float(x[k].getValue()));
					  }
				  }
				  m_table.addRow(data);
				  m_table.setModelKu();  
				  
			  }
	  }
}*/
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

		public void resetDefaultColumn(String period) {
			 DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTableHeaderRoot();
		     root.add(new DefaultMutableTreeNode("No"));
		     root.add(new DefaultMutableTreeNode("Name"));
		     root.add(new DefaultMutableTreeNode("Job Title"));
		     DefaultMutableTreeNode node = new DefaultMutableTreeNode("Presence (days)");
		     DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(period);
		     node1.add(new DefaultMutableTreeNode("Presence"));
		     node1.add(new DefaultMutableTreeNode("Not Late"));
		     node1.add(new DefaultMutableTreeNode("Late"));
		     node.add(node1);
		     root.add(node);
		      
		     /*
			m_tableModel = new PayrollTableModel();
			m_tableModel.setRowCount(0);
			m_tableModel.addColumn("No");
			m_tableModel.addColumn("Name");
			m_tableModel.addColumn("Job Title");
			m_tableModel.addColumn("Presence");
			m_tableModel.addColumn("Not Late");
			m_tableModel.addColumn("Late");*/
		}

		public void setModelKu() {
			
			 Enumeration enumeration = getColumnModel().getColumns();
			  while(enumeration.hasMoreElements()){
				  TableColumn column = (TableColumn) enumeration.nextElement();
				  column.setPreferredWidth(100);
				  //column.setMinWidth(50);
				  //column.setMaxWidth(50);
			  }
			//setModel(m_tableModel);

			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);
			
			for(int i=3;i<6;i++){
				getColumnModel().getColumn(i).setPreferredWidth(50);
				getColumnModel().getColumn(i).setMinWidth(50);
				getColumnModel().getColumn(i).setMaxWidth(50);
			}
		}

		public void clearRow(){
			((DefaultTableModel)getModel()).setRowCount(0);
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

}
