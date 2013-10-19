package pohaci.gumunda.titis.hrm.cgui.report;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
//import javax.swing.event.*;

import net.sf.jasperreports.view.JRViewer;

import java.awt.*;
//import java.awt.event.*;
import java.sql.Connection;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;


import pohaci.gumunda.titis.application.*;
//import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.report.HRMEmployeeAbsenceJasper;
//import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.cgui.*;;

public class RptEmployeePresence extends JPanel {
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
	
	JRViewer m_jrv;
	
	Connection m_conn = null;
	long m_sessionid = -1;
	boolean m_new = false, m_edit = false;
	int m_editedIndex = -1;
	
	Employee[] m_employee = null;
	
	PeriodMount m_mountYear;
	JPanel m_centerPanel = new JPanel();   
	public RptEmployeePresence(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		setJasper(null); 
		constructComponent();
		
	}	
	
	void constructComponent() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);    
		JPanel listPanel = new JPanel();
		JPanel button2Panel = new JPanel();    
		
		m_list = new EmployeeListPanel(this,m_conn, m_sessionid,true);    
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
	
	public void setJasper(Employee[] employees){
		String tglBetween = "";
		if (employees!=null){		  
			if (m_mountYear.m_monthComboBox.getMonth()==11){
				tglBetween = "between '" + m_mountYear.m_yearField.getValue() + "-" 
				+ (m_mountYear.m_monthComboBox.getMonth() + 1) + "-1' and '" + 
				(m_mountYear.m_yearField.getValue()+1) + "-" 
				+ "1" + "-1'";  
			}else{
				tglBetween = "between '" + m_mountYear.m_yearField.getValue() + "-" 
				+ (m_mountYear.m_monthComboBox.getMonth() + 1) + "-1' and '" + 
				m_mountYear.m_yearField.getValue() + "-" 
				+ (m_mountYear.m_monthComboBox.getMonth() + 2) + "-1'";
			}
		}		
		HRMEmployeeAbsenceJasper jasper= new HRMEmployeeAbsenceJasper(m_conn,m_sessionid,employees,tglBetween);
		if (m_jrv != null)
			m_centerPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}
	
}