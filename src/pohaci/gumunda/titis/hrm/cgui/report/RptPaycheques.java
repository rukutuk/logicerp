package pohaci.gumunda.titis.hrm.cgui.report;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.application.PeriodMount;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeAccountPanel;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCertificationPanel;
import pohaci.gumunda.titis.hrm.cgui.EmployeeEducationPanel;
import pohaci.gumunda.titis.hrm.cgui.EmployeeFamilyPanel;
import pohaci.gumunda.titis.hrm.cgui.EmployeeListPanel;
import pohaci.gumunda.titis.hrm.cgui.EmployeeRetirementPanel;
import pohaci.gumunda.titis.hrm.cgui.EmployeeTable;
import pohaci.gumunda.titis.hrm.cgui.EmploymentPanel;

public class RptPaycheques extends JPanel implements ListSelectionListener  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
EmployeeListPanel m_list;
  EmployeeTable m_table;
  EmploymentPanel m_employmentpanel;
  EmployeeEducationPanel m_edupanel;
  EmployeeCertificationPanel m_certificatepanel;
  EmployeeFamilyPanel m_familypanel;
  EmployeeAccountPanel m_accountpanel;
  EmployeeRetirementPanel m_retirementpanel;
  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  
  public Employee m_employee;
  public Object[] m_objlist;
  PeriodMount m_mountYear;
  JRViewer m_jrv;
  JPanel m_centerPanel = new JPanel();
  RptPaychequeJasper m_beginjasper;
  RptPaychequeJasper m_addjasper;
  
  public RptPaycheques(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    m_beginjasper = new RptPaychequeJasper(m_conn,m_sessionid);
    m_addjasper = new RptPaychequeJasper(m_conn,m_sessionid);
    setJasperEmpty(); 
    constructComponent();
  }

  void constructComponent() {
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);    
    JPanel listPanel = new JPanel();        
    JPanel button2Panel = new JPanel();    
    
    m_list = new EmployeeListPanel(this,m_conn, m_sessionid,true);    
    m_list.getList().addListSelectionListener(this);
    m_mountYear = new PeriodMount("Period");        

    listPanel.setLayout(new BorderLayout());
    listPanel.add(m_mountYear, BorderLayout.NORTH);
    listPanel.add(m_list, BorderLayout.CENTER);

    m_centerPanel.setLayout(new BorderLayout());
    m_centerPanel.add(m_jrv, BorderLayout.CENTER);
    m_centerPanel.add(button2Panel, BorderLayout.SOUTH);

    splitPane.setLeftComponent(listPanel);
    splitPane.setRightComponent(m_centerPanel);
    splitPane.setDividerLocation(200);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }
  
  public void setJasperEmpty(){
	  java.util.Date date = new java.util.GregorianCalendar().getTime();
	  java.text.DateFormat formatThn = new java.text.SimpleDateFormat("yyyy");
	  java.text.DateFormat formatBln = new java.text.SimpleDateFormat("MM");	  
	  String thn = formatThn.format(date);
	  String bln = formatBln.format(date);
	  String bln_thn = "b.monthpayrollSubmit=" + bln + " and b.yearpayrollsubmit=" + thn;
	  m_beginjasper.getEmpty(null,bln_thn);
	  m_jrv = m_beginjasper.getPrintView();
	  m_centerPanel.add(m_jrv, BorderLayout.CENTER);
	  m_centerPanel.validate();
  }
   
  public void setPrintPreview(){	 
	  if (m_objlist==null){
		  JOptionPane.showMessageDialog(this, "Employee is not selected",
				  "Information", JOptionPane.INFORMATION_MESSAGE);
	  }else{	  		  
		  String bln_thn = "b.monthpayrollSubmit="+ (m_mountYear.m_monthComboBox.getMonth()+1) +" and b.yearpayrollsubmit=" + m_mountYear.m_yearField.getValue();		  
		  String mount = getMount(m_mountYear.m_monthComboBox.getMonth()+1) + " " + m_mountYear.m_yearField.getValue();
		  for (int i=0;i<m_objlist.length;i++){
			  System.err.println(i);
			  if (i==0){
				  m_beginjasper.getNonEmpty(m_objlist[i],mount,bln_thn);
			  }else{
				  m_addjasper.getNonEmpty(m_objlist[i],mount,bln_thn);
				  JRPrintPage page = (JRPrintPage)m_addjasper.getJasperPrint().getPages().get(0);
				  m_beginjasper.getJasperPrint().addPage(i,page);				    
			  }
		  }
		  if (m_jrv != null)
			  m_centerPanel.remove(m_jrv);
		  m_jrv = new JRViewer(m_beginjasper.getJasperPrint());
		  m_jrv.repaint();
		  m_jrv.revalidate();	
		  m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		  m_centerPanel.validate();
	  }
  } 	  
	
  public void valueChanged(ListSelectionEvent e) {		
	  if(!e.getValueIsAdjusting()){
		  //int iRowIndex = m_list.getList().getMinSelectionIndex();
		  m_objlist = m_list.getList().getSelectedValues();		
		  //m_employee = (Employee)m_list.getList().getSelectedValue();				  
	  }
  }
  
	public String getMount(int month){
		String bln = "";
		if (month==1){
			bln = "January";
		}else if (month==2){
			bln = "February";
		}else if (month==3){
			bln = "March";
		}else if (month==4){
			bln = "April";
		}else if (month==5){
			bln = "May";
		}else if (month==6){
			bln = "June";
		}else if (month==7){
			bln = "July";
		}else if (month==8){
			bln = "August";
		}else if (month==9){
			bln = "September";
		}else if (month==10){
			bln = "October";
		}else if (month==11){
			bln = "November";
		}else {
			bln = "December";
		}
		return bln;
	}
 
}