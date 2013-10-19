
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
import pohaci.gumunda.titis.hrm.cgui.report.RptSummary;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollPaychequeSubmitLogic;
//import pohaci.gumunda.cgui.StructuredTable;

public class PayrollPaychequeSubmitPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Connection m_conn = null;
	public long m_sessionid = -1;
	
	public JTextField m_statusTextField;
	public JTextField m_submitdateTextField;
	UnitPicker m_unitPicker;
	public JMonthChooser m_monthComboBox;
	public JSpinField m_yearField;
	PayrollComponentSubmitPicker m_componentPicker;
	JButton m_viewBt, m_summaryreport;
	public JButton m_submitBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;
	JComboBox m_typecombo;
	
	public PayrollTable m_table;
	boolean m_show = false;
	Employee_n[] m_emps = null;
	Unit m_unit;
	public Vector employeePayVector=new Vector();
	public boolean updateFlag;
	private PayrollPaychequeSubmitLogic paycheqLogic;
	private HRMBusinessLogic m_hrmLogic;
	EmployeePayrollSubmit[] m_empPayrollSubmit;
	//private Employee_n[] dataList;
	private RptSummary m_beginjasper,m_addjasper;
	
	public PayrollPaychequeSubmitPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_beginjasper = new RptSummary(m_conn,m_sessionid,this);
		m_addjasper = new RptSummary(m_conn,m_sessionid,this);
		
		constructComponent(false);
		setEditable(true);
		m_hrmLogic = new HRMBusinessLogic(m_conn);
	}
	
	public void constructComponent(boolean check) {
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
		
		m_monthComboBox=new JMonthChooser(JMonthChooser.NO_SPINNER);
		m_typecombo = new JComboBox(new Object[]{IDBConstants.ATTR_PAYCHEQUE[0],IDBConstants.ATTR_PAYCHEQUE[1],IDBConstants.ATTR_PAYCHEQUE[2]});
		
		m_yearField = new JSpinField();
		m_yearField.setMaximum(9999);
		m_yearField.setValue(new java.util.GregorianCalendar().get(java.util.Calendar.YEAR));
		m_componentPicker = new PayrollComponentSubmitPicker(m_conn, m_sessionid, PayrollComponentTree.PAYCHEQUE_RECEIVABLES);
		
		m_viewBt = new JButton("View");
		m_viewBt.addActionListener(this);
		
		m_submitBt = new JButton("Submit");
		m_submitBt.addActionListener(this);
		m_submitBt.setEnabled(false);
		
		m_summaryreport = new  JButton("Summary report");
		m_summaryreport.addActionListener(this);
		m_summaryreport.setEnabled(false);
		
		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Viewed by Payroll Component");
		JLabel typelabel = new JLabel("Type");
		
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
		buttonPanel.add(m_summaryreport);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		formPanel.setLayout(new GridBagLayout());
		Misc.setGridBagConstraints(formPanel, statuslabel, gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_statusTextField, gridBagConstraints, 2, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, monthlabel, gridBagConstraints, 3, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_monthComboBox, gridBagConstraints, 5, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, submitlabel, gridBagConstraints, 0, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_submitdateTextField, gridBagConstraints, 2, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, yearlabel, gridBagConstraints, 3, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_yearField, gridBagConstraints, 5, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, typelabel, gridBagConstraints, 3, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_typecombo, gridBagConstraints, 5, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, unitlabel, gridBagConstraints, 0, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_unitPicker, gridBagConstraints, 2, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, viewPanel, gridBagConstraints, 0, 3,
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
		
		formPanel.setPreferredSize(new Dimension(450, 100));
		Misc.setGridBagConstraints(northPanel, formPanel, gridBagConstraints, 0, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(northPanel, new JPanel(), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEtchedBorder());
		//if (check){
		m_table = new PayrollTable(new DefaultMutableTreeNode("Column"));
		centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		//}else{
		//PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
		//pohaci.gumunda.titis.application.HRMPayrollPaychequeJasper jasper =
		//new pohaci.gumunda.titis.application.HRMPayrollPaychequeJasper(this);	    	
		//centerPanel.add(jasper.getPrintView(), BorderLayout.CENTER);
		//}
		
		setLayout(new BorderLayout());
		add(northleftPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}
	
	public void setEditable(boolean editable){  	
		m_searchBt.setEnabled(editable);
		m_excellBt.setEnabled(editable);
		m_previewBt.setEnabled(editable);
		m_summaryreport.setEnabled(editable);
	}
	
	// JANGAN DIHAPUS DULU
	/*private String getQuery(long index){
		String where = "";
		if (index<0)
			where = " department is null";
		else
			where = " department=" + index;
		if (m_unitPicker.getUnit()!=null)
			where += " and unit=" + m_unitPicker.getUnit().getIndex();
		String query ="select * from (select a.autoindex,a.firstname,a.midlename,a.lastname,a.employeeno,b.department,b.unit from employee a left join employeeemployment b on a.autoindex=b.employee) where " + where +
		" order by autoindex";
		return query;
	}*/
	
	// JANGAN DIHAPUS DULU
	/*private SimpleEmployee[] getSimpleEmployee(String query){
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);		
			SimpleEmployee[] employee= logic.getEmployeeForPayroll(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, query);
			return employee;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}*/
	
	public void getSummaryreport(){
		try {			
			boolean first = false;
			int no=0;
			m_beginjasper.setType(m_typecombo);
			m_addjasper.setType(m_typecombo);
			HashMap map = new HashMap();
			List dataList2 = new ArrayList();
			
			PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
			PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();
			
			Iterator iterator = getMaperDept();
			List totallist = new ArrayList();
			List deptlist = new ArrayList();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String transdate = dateFormat.format(new Date());
			
			while (iterator.hasNext()){
				List dataList = new ArrayList();
				Organization org = (Organization)iterator.next();				
				for (int i=0;i<m_empSub.length;i++){
					if(!map.containsKey(new Long(m_empSub[i].getAutoindex()))){
						Employment[] employement = m_hrmLogic.getEmployeeEmployment(m_sessionid,IDBConstants.MODUL_MASTER_DATA,m_empSub[i].getAutoindex());					
						if (employement!=null){
							if (org.getIndex() == employement[0].getDepartment().getIndex()){								
								map.put(m_empSub[i],new Long(m_empSub[i].getAutoindex()));
								dataList.add(m_empSub[i]);
							}
						}
					}
					else{
						dataList2.add(m_empSub[i]);
					}
				}				
				Employee_n[] emps = (Employee_n[]) dataList.toArray(new Employee_n[dataList.size()]);
				List empList = Arrays.asList(emps);
				Collections.sort(empList);
				emps = (Employee_n[]) empList.toArray(new Employee_n[empList.size()]);
				
				if (emps.length>0){				
					if (!first){
						m_beginjasper.setRptSummary(payrollComponents,m_empPayrollSubmit,payrollComponentsDefault,emps,m_unitPicker,org,getMonthYearString(),transdate);
						first = true;
					}
					
					m_addjasper.setRptSummary(payrollComponents,m_empPayrollSubmit,payrollComponentsDefault,emps,m_unitPicker,org,getMonthYearString(),transdate);
					m_addjasper.setType(m_typecombo);
					totallist.add(m_addjasper.getTotal());
					deptlist.add(org);
					
					int size = m_addjasper.getJasperPrint().getPages().size();
					for (int j=0;j<size;j++){
						JRPrintPage page = (JRPrintPage)m_addjasper.getJasperPrint().getPages().get(j);
						m_beginjasper.getJasperPrint().addPage(no++,page);
					}
				}
			}
			
			Employee_n[] emps2 = (Employee_n[]) dataList2.toArray(new Employee_n[dataList2.size()]);			
			if (emps2.length>0){
				m_addjasper.setRptSummary(payrollComponents,m_empPayrollSubmit,payrollComponentsDefault,emps2,m_unitPicker,null,getMonthYearString(),transdate);
				totallist.add(m_addjasper.getTotal());
				deptlist.add(null);
				int size = m_addjasper.getJasperPrint().getPages().size();
				for (int j=0;j<size;j++){
					JRPrintPage page = (JRPrintPage)m_addjasper.getJasperPrint().getPages().get(j);
					m_beginjasper.getJasperPrint().addPage(no++,page);
				}			
			}	
			
			m_addjasper.setRptSummary2(totallist,deptlist,m_unitPicker,getMonthYearString(),transdate);
			int size = m_addjasper.getJasperPrint().getPages().size();
			for (int j=0;j<size;j++){
				JRPrintPage page = (JRPrintPage)m_addjasper.getJasperPrint().getPages().get(j);
				m_beginjasper.getJasperPrint().addPage(no++,page);
			}
			
			if (no>0)
				m_beginjasper.getJasperPrint().removePage(no);
			m_beginjasper.getPrintView();
			
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	private Iterator getMaperDept() {
		GenericMapper mapper=MasterMap.obtainMapperFor(Organization.class);
		mapper.setActiveConn(m_conn);
		String selectWhere = " 1=1 ";
		List list=mapper.doSelectWhere(selectWhere);
		Iterator iterator = list.iterator();
		return iterator;
	}
	
	/*private void getSummarysubmit(){
	 try {
	 boolean first = false;
	 int no=0;
	 List dataList2 = new ArrayList();
	 EmployeePayrollSubmit submitObject = getEmpPayrollSubmit();
	 m_empPayrollSubmit = queryEmpPayrollSubmit(submitObject);
	 RptSummary jasper = null;
	 HashMap map = new HashMap();
	 PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();
	 Iterator iterator = getMaperDept();
	 
	 while (iterator.hasNext()){
	 List dataList = new ArrayList();				
	 Organization org = (Organization)iterator.next();
	 for (int i=0;i<m_emps.length;i++){					
	 if(!map.containsKey(new Long(m_emps[i].getAutoindex()))){
	 Employment[] employement;
	 employement = m_hrmLogic.getEmployeeEmployment(m_sessionid,IDBConstants.MODUL_MASTER_DATA,m_emps[i].getAutoindex());
	 if (employement!=null)
	 if (org.getIndex() == employement[0].getDepartment().getIndex()){								
	 map.put(m_emps[i],new Long(m_emps[i].getAutoindex()));
	 dataList.add(m_emps[i]);
	 }
	 }
	 else{
	 dataList2.add(m_emps[i]);
	 }
	 }

	 Employee_n[] emps = (Employee_n[]) dataList.toArray(new Employee_n[dataList.size()]);
	 if (emps.length>0){				
	 if (!first){
	 jasper = new RptSummary(this,emps,m_empPayrollSubmit,payrollComponentsDefault,m_conn,m_sessionid,m_unitPicker,org,getMonthYearString());
	 first = true;
	 }else{
	 RptSummary addjasper = new RptSummary(this,emps,m_empPayrollSubmit,payrollComponentsDefault,m_conn,m_sessionid,m_unitPicker,org,getMonthYearString());
	 int size = addjasper.getJasperPrint().getPages().size();
	 for (int j=0;j<size;j++){
	 JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
	 jasper.getJasperPrint().addPage(no++,page);
	 }
	 }
	 }
	 }
	 
	 Employee_n[] emp = (Employee_n[]) dataList2.toArray(new Employee_n[dataList2.size()]);			
	 if (emp.length>0){
	 RptSummary addjasper = new RptSummary(this,emp,m_empPayrollSubmit,payrollComponentsDefault,m_conn,m_sessionid,m_unitPicker,null,getMonthYearString());
	 int size = addjasper.getJasperPrint().getPages().size();
	 for (int j=0;j<size;j++){
	 JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
	 jasper.getJasperPrint().addPage(no++,page);
	 }			
	 }
	 //jasper.getJasperPrint().removePage(no);			
	  jasper.getPrintView();
	  
	  } catch (Exception e) {
	  e.printStackTrace();
	  }					
	  }
	  */
	Employee_n[] m_empSub = null;
	public void actionPerformed(ActionEvent e) {	
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));	
		if (e.getSource()==m_previewBt){
			jaspertReport(false);
		}else if (e.getSource() == m_excellBt){
			jaspertReport(true);
		}else if (e.getSource()==m_searchBt){
			if(!m_show) {
				m_show = true;
				SearchEmployeeDlg dlg = new SearchEmployeeDlg(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), 
						this,m_conn, m_sessionid);
				dlg.setVisible(true);
				m_show = false;
			}
		}else if(e.getSource()==m_summaryreport){
			if (m_emps!=null){
					//if (!m_ceksummary)// apakah belum dilakukan search terhadap employ submit 
						//m_empSub =getEmployeeByCriteria();
					//if (m_empSub.length==0)// apakah blum submit(cek jika empsubmitnya kosong)
						m_empSub = m_emps;
				getSummaryreport();		
			}
		}else if(e.getSource()==m_viewBt){	
			view(true,null,"");	
		}else if(e.getSource()==m_submitBt){
			
			int val = JOptionPane.showConfirmDialog(this,
					"System will submit data for all payroll components with the type of " + 
					"\n" + 
					"paycheque and for all employee within selected unit code." +
					"\n" + 
					"Continue to submit?", 
					"Submit Confirmation", JOptionPane.YES_NO_OPTION);
			if(val==JOptionPane.YES_OPTION)	
				submit();
		}
		setCursor(Cursor.getDefaultCursor());
	}
	
	public void SetEmpSub(String[] attr,String operator){
		String data = "";
		String item = "";
		if (!attr[0].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data += item + "UPPER(firstname& ' ' & midlename& ' ' & lastname) " +
			"LIKE '%"+ attr[0].toUpperCase() +"%' ";
		} 
		if (!attr[1].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data +=  item + " UPPER(jobtitle) like '%" + attr[1].toUpperCase() + "%' ";
		}
		data = "and (" + data + ")";
		if (attr[0].equals("") && attr[1].equals(""))
			data = " ";
		
		String query = "select distinct * from (select a.autoindex,a.firstname,a.midlename,a.lastname,a.employeeno,c.name jobtitle from employee a,employeeemployment b,jobtitle c " +
		"where a.autoindex=b.employee and b.jobtitle=c.autoindex) where autoindex in (select distinct employee from employeepayrolldetail where " +
		"employeepayroll in (select a.autoindex from employeepayroll a where a.monthpayrollsubmit="+(m_monthComboBox.getMonth()+1)+" and a.yearpayrollsubmit="+m_yearField.getValue()+" and a.unit="+m_unitPicker.getUnit().getIndex()+")) " + data;
		try {
			m_empSub = m_hrmLogic.getEmployeeBy_Criteria(m_sessionid,IDBConstants.MODUL_MASTER_DATA,query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public Employee_n[] getEmployeeByCriteria(){
		Employee_n[] emps = null;
		try {
			String query = "select distinct * from (select a.autoindex,a.firstname,a.midlename,a.lastname,a.employeeno,c.name jobtitle from employee a,employeeemployment b,jobtitle c " +
			"where a.autoindex=b.employee and b.jobtitle=c.autoindex) where autoindex in (select distinct employee from employeepayrolldetail where " + 
			"employeepayroll in (select a.autoindex from employeepayroll a where a.monthpayrollsubmit="+(m_monthComboBox.getMonth()+1)+" and a.yearpayrollsubmit="+m_yearField.getValue()+" and a.unit="+m_unitPicker.getUnit().getIndex()+"))";
			emps = m_hrmLogic.getEmployeeBy_Criteria(m_sessionid,IDBConstants.MODUL_MASTER_DATA,query);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return emps;
	}*/
	
	
	//boolean m_ceksummary=false;
	public void submit(){
		HRMBusinessLogic hrmlogic = new HRMBusinessLogic(m_conn); 
		EmployeePayrollSubmit[] empPayrollSubmit=null;
		if(employeePayVector.size()!=0){
			empPayrollSubmit =new EmployeePayrollSubmit[employeePayVector.size()];
			employeePayVector.copyInto(empPayrollSubmit);
		}
		
		if(empPayrollSubmit!=null){
			try {
				if(updateFlag){
					for (int i = 0; i < empPayrollSubmit.length; i++) {
						hrmlogic.updateEmployeePayroll(m_sessionid,
								IDBConstants.MODUL_MASTER_DATA,empPayrollSubmit[i]);
						this.setCursor(Cursor.getDefaultCursor());
					}
					return;
				}else{
					hrmlogic.createEmployeePayrollSubmit(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA,empPayrollSubmit);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		paycheqLogic.setTextfieldStatus(empPayrollSubmit[0]);
	}
	
	public String getUnitDescription() {
		Unit unit = (Unit) m_unitPicker.getObject();
		if (unit==null) return "";
		String unitDesc =unit.getDescription();
		return unitDesc;
	}
	
	public String getMonthYearString() {
		return getMount(m_monthComboBox.getMonth()) + " - " + m_yearField.getValue();
	}
	
	void jaspertReport(boolean check){		
		EmployeePayrollSubmit submitObject = getEmpPayrollSubmit();
		m_empPayrollSubmit = queryEmpPayrollSubmit(submitObject);
		PayrollComponent[] payrollComponents = m_componentPicker.getPayrollComponentsSelected();
		PayrollComponent[] payrollComponentsDefault = m_componentPicker.getRootPayrollComponents();
		new pohaci.gumunda.titis.hrm.cgui.report.HRMPayrollPaychequeJasper(this,payrollComponents,m_unit,m_emps,
				m_empPayrollSubmit,payrollComponentsDefault,m_typecombo,check);
	}
	
	public void view(boolean cekEmployee,String[] attr,String operator){
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
			//m_ceksummary = false;
			try{
				m_emps = getEmployee(date, m_unit.getIndex());
			}catch(Exception ex){				    	
			}
			tableHeader(payrollComponents);
			EmployeePayrollSubmit submitObject = getEmpPayrollSubmit();
			m_empPayrollSubmit = queryEmpPayrollSubmit(submitObject);
			paycheqLogic = new PayrollPaychequeSubmitLogic(this,
					(DefaultTableModel)this.m_table.getModel(),payrollComponents,m_emps,m_unit,
					m_empPayrollSubmit,payrollComponentsDefault);
			paycheqLogic.setType(m_typecombo);
			paycheqLogic.setIsFindEmployee( cekEmployee);
			paycheqLogic.tableBody();
			setEditable(true);
		} else {
			//m_ceksummary = true;
			try{
				m_emps = getEmployeeBy_Criteria(date, m_unit.getIndex(),attr,operator);
			}catch(Exception ex){				    	
			}
			tableHeader(payrollComponents);
			String query = getQuerySubmitByCriteria(attr,operator);		  
			EmployeePayrollSubmit submitObject = getEmpPayrollSubmit();	
			m_empPayrollSubmit = queryEmpPayrollSubmitByCriteria(submitObject,query);
			PayrollPaychequeSubmitLogic paycheqLogic = new PayrollPaychequeSubmitLogic(this,
					(DefaultTableModel)this.m_table.getModel(),payrollComponents,m_emps,m_unit,
					m_empPayrollSubmit,payrollComponentsDefault);
			paycheqLogic.setType(m_typecombo);
			paycheqLogic.setIsFindEmployee(cekEmployee);
			paycheqLogic.tableBody();
			//tableBody(payrollComponents,m_emps,m_unit);
			m_table.setModelKu();
		}
	}
	
	public EmployeePayrollSubmit[] queryEmpPayrollSubmit(EmployeePayrollSubmit submitObject) {
		try {			  
			EmployeePayrollSubmit[] employeePaySubm = m_hrmLogic.getEmployeePayrollSubmit(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,submitObject);
			return employeePaySubm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private EmployeePayrollSubmit[] queryEmpPayrollSubmitByCriteria(EmployeePayrollSubmit submitObject,String query) {
		try {			  
			EmployeePayrollSubmit[] employeePaySubm = m_hrmLogic.getEmployeePayrollSubmitByCriteria(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,submitObject,query);
			return employeePaySubm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getMonth(){
		return m_monthComboBox.getMonth() + 1;
	}
	
	public int getYear(){
		return m_yearField.getValue();
	}
	
	public EmployeePayrollSubmit getEmpPayrollSubmit() {
		EmployeePayrollSubmit employeepayrollSubmit = new EmployeePayrollSubmit();
		employeepayrollSubmit.setMonth(getMonth());
		employeepayrollSubmit.setYear(getYear());
		employeepayrollSubmit.setUnit(m_unit);
		employeepayrollSubmit.setStatus(PayrollSubmitStatus.SUBMITTED);
		Calendar calendar=Calendar.getInstance();
		employeepayrollSubmit.setSubmittedDate(calendar.getTime());
		employeepayrollSubmit.setPayrollType(PayrollSubmitType.PAYCHEQUE_SUBMIT);
		employeepayrollSubmit.setPaychequeType(getPaychequeType());
		return employeepayrollSubmit;
	}

	public int getPaychequeType(){
		String strval = (String)m_typecombo.getSelectedItem();
		if (strval.equals(IDBConstants.ATTR_PAYCHEQUE[0]))
			return 0;
		else if (strval.equals(IDBConstants.ATTR_PAYCHEQUE[1]))
				return 1;
		return -1;
	}
	
	public void tableHeader(PayrollComponent[] payrollComponents){
		m_table.rebuildTable();
		PayrollComponentSelectionTree viewTree = 
			new PayrollComponentSelectionTree(m_conn, m_sessionid, null, payrollComponents);
		if(payrollComponents.length>0){
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)m_table.getTableHeaderRoot();
			root.removeAllChildren();	       
			m_table.resetDefaultColumn();
			root.add((DefaultMutableTreeNode)viewTree.getModel().getRoot());			
		}
		m_table.addTotalColumn();
		m_table.rebuildTable();
		m_table.clearRow();
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
		"(select employee, max(tmt) tmt from (select * from employeeemployment where tmt<'" + date + "') " +
		"group by employee ) lastemp " +
		"where e.employee=lastemp.employee " +
		"and e.tmt=lastemp.tmt and unit="+ unitId + ") employment " +
		"on emp.autoindex=employment.employee " +
		"inner join jobtitle job on employment.jobtitle=job.autoindex " +
		"where not exists (select employee from employeeretirement ret where ret.tmt<='" + date + "' and emp.autoindex=ret.employee) " +
		data + "order by emp.employeeno";		
		System.out.println(query);
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
		String query="select a.*,b.*,b.submitteddate,d.employeeno,d.firstname,d.midlename,d.lastname " +
		"from employeepayrolldetail a,employeepayroll b, " +
		"employee d where a.employeepayroll=b.autoindex  " +
		"and a.employee=d.autoindex"+
		" and  b.yearpayrollsubmit="+getYear() +
		" and b.monthpayrollsubmit="+getMonth() +
		//" and b.unitcode="+ m_unit.getIndex()+
		" and b.unit=" + m_unit.getIndex() +
		" and b.payrolltype=" + PayrollSubmitType.PAYCHEQUE_SUBMIT +
		data;
		System.err.println(query);	  
		return query;
	}
	
	private Employee_n[] getEmployee(String date, long unitId) throws Exception{
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		Employee_n[] emps = logic.getEmployeeBy_unit(m_sessionid, 
				IDBConstants.MODUL_MASTER_DATA, unitId, date);
		return emps;
	}
	
	public PayrollCategoryComponent[] getPayrollCategoryComponents
	(Employee_n emp, PayrollComponent[] components) throws Exception{
		
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		PayrollCategoryComponent[] comps = 
			logic.getSelectedPayrollCategoryComponent(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, emp.getAutoindex(), components);
		return comps;
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
		
		private static final long serialVersionUID = 1L;
		PayrollTableModel m_tableModel;
		public PayrollTable(DefaultMutableTreeNode root) {
			super(root);
			((DefaultTableModel)getModel()).setRowCount(0);
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		
		public void resetDefaultColumn(){
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("No"));
			root.add(new DefaultMutableTreeNode("Name"));
			root.add(new DefaultMutableTreeNode("Job Title"));
		}
		
		public void setModelKu(){
			Enumeration enumeration = getColumnModel().getColumns();
			while (enumeration.hasMoreElements()) {
				TableColumn column = (TableColumn) enumeration.nextElement();
				column.setPreferredWidth(100);
			}
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);
		}
		
		public void addTotalColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("Total"));
		}
		
		public void clearRow(){
			((DefaultTableModel)getModel()).setRowCount(0);
		}
		
		public void addRow(Vector data){
			((DefaultTableModel)getModel()).addRow(data);
		}
		
		public TableCellRenderer getCellRenderer(int row, int col) {
			if(col == 0)
				return new RightAllignmentCellRenderer();
			else if(col > 2)
				return new DoubleCellRenderer(JLabel.RIGHT);
			return super.getCellRenderer(row, col);
		}
	}
	
	class PayrollTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col){
			return false;
		}
	}
	
	class RightAllignmentCellRenderer extends DefaultTableCellRenderer{
		
		private static final long serialVersionUID = 1L;
		public Component getTableCellRendererComponent(JTable table,
				Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			setText((value == null) ? "    " : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);	 
			return this;
		}
	}
	
	public EmployeePayrollSubmit dummyClone(EmployeePayrollSubmit empPayrollSubmit) {
		EmployeePayrollSubmit employeePayrollSubmit=new EmployeePayrollSubmit();
		employeePayrollSubmit.setEmployee_n(empPayrollSubmit.getEmployee_n());
		//System.out.println(empPayrollSubmit.getEmployee_n().getAutoindex());
		employeePayrollSubmit.setJurnalID(empPayrollSubmit.getJurnalID());
		employeePayrollSubmit.setMonth(empPayrollSubmit.getMonth());
		employeePayrollSubmit.setPaymentPeriode(empPayrollSubmit.getPaymentPeriode());
		employeePayrollSubmit.setStatus(empPayrollSubmit.getStatus());
		employeePayrollSubmit.setSubmittedDate(empPayrollSubmit.getSubmittedDate());
		employeePayrollSubmit.setUnit(empPayrollSubmit.getUnit());
		employeePayrollSubmit.setYear(empPayrollSubmit.getYear());
		employeePayrollSubmit.setPayrollType(empPayrollSubmit.getPayrollType());
		employeePayrollSubmit.setPaychequeType(empPayrollSubmit.getPaychequeType());
		return employeePayrollSubmit;
	}
	
}



