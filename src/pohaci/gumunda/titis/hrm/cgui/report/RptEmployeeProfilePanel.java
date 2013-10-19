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

public class RptEmployeeProfilePanel extends JPanel implements ListSelectionListener{
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
	public Employee m_employee = null;
	PeriodMount m_mountYear;
	RptEmployeeProfileJasper m_jasper;
	
	JRViewer m_jrv;
	JPanel m_centerPanel = new JPanel();    
	
	
	
	public RptEmployeeProfilePanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_jasper = new RptEmployeeProfileJasper(m_conn,m_sessionid);
		setJasperEmpty(); 
		constructComponent();
		
	}
	
	void constructComponent() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);    
		JPanel listPanel = new JPanel();
		
		JPanel button2Panel = new JPanel();    
		
		m_list = new EmployeeListPanel(this,m_conn, m_sessionid,true);
		m_list.getList().addListSelectionListener(this);
		
		listPanel.setLayout(new BorderLayout());    
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
	
	// digunakan apabila printpreview banyak employee jangan dihapus sebagai sample buat kedepannya
	/*public void setJasper(Employee[] employees){ 
		RptEmployeeProfileJasper beginjasper = new RptEmployeeProfileJasper(m_conn,m_sessionid);					
		RptEmployeeProfileJasper addjasper = new RptEmployeeProfileJasper(m_conn,m_sessionid);
		
		if (employees==null){
			beginjasper.getEmpty(null);
			m_jrv = beginjasper.getPrintView();
			m_centerPanel.add(m_jrv, BorderLayout.CENTER);
			m_centerPanel.validate();
		}else{
			System.err.println("panjang employee :" + employees.length);
			for (int i=0;i<employees.length;i++) {
				if(i == 0){ 							
					beginjasper.getNonEmpty(employees[i]);
				}else { 
				   	addjasper.getNonEmpty(employees[i]);				   
				    JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(0);
				    beginjasper.getJasperPrint().addPage(i,page);				    
				}
			}
				
			if (m_jrv != null)
				m_centerPanel.remove(m_jrv);
			m_jrv = new JRViewer(beginjasper.getJasperPrint());
			m_jrv.repaint();
			m_jrv.revalidate();			
			m_centerPanel.add(m_jrv, BorderLayout.CENTER);
			m_centerPanel.validate();
		}
	}*/
	
	public void setJasperEmpty(){
			m_jasper.getEmpty(null);
			m_jrv = m_jasper.getPrintView();
			m_centerPanel.add(m_jrv, BorderLayout.CENTER);
			m_centerPanel.validate();
	}
	
	public void setPrintPreview(){
		if (m_employee==null){
			JOptionPane.showMessageDialog(this, "Employee non selected",
					  "Information", JOptionPane.INFORMATION_MESSAGE);
		}else{
			m_jasper.getNonEmpty(m_employee);			
			m_centerPanel.remove(m_jrv);
			m_jrv = m_jasper.getPrintView();
			m_centerPanel.add(m_jrv, BorderLayout.CENTER);
			m_centerPanel.validate();
		}
	}	
	
	public void valueChanged(ListSelectionEvent e) {		
		  if(!e.getValueIsAdjusting()){
			  int iRowIndex = m_list.getList().getMinSelectionIndex();
			  
			  if(m_new || m_edit){
				  if(iRowIndex != m_editedIndex){
					  int count = m_list.getList().getModel().getSize() + 1;
					  if(m_editedIndex == -1)
						  m_list.getList().getSelectionModel().setSelectionInterval(m_editedIndex + count,
								  m_editedIndex + count);
					  else
						  m_list.getList().getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
				  }
			  }
			  else{
				  if(iRowIndex != -1){	
					  m_employee = (Employee)m_list.getList().getSelectedValue();
					  m_editedIndex = iRowIndex;
				  }
			  }
		  }
	  }

}