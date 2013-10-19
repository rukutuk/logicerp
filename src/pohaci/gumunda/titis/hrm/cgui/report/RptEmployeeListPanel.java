package pohaci.gumunda.titis.hrm.cgui.report;

import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeList;
import pohaci.gumunda.titis.hrm.cgui.EmployeeListPanel;
import pohaci.gumunda.titis.hrm.cgui.RptEmployeeListJasper;

public class RptEmployeeListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EmployeeListPanel m_list;
	JPanel m_centerPanel = new JPanel();
    RptEmployeeListJasper m_jasper;
	JRViewer m_jrv;
	
	public EmployeeList m_employees = null;
		
	Connection m_conn = null;
	long m_sessionid = -1;
	
	public RptEmployeeListPanel(Connection conn, long sessionid){
		m_conn = conn;
	    m_sessionid = sessionid;
	    constructComponent();
	    setJasperEmpty();
	}

	private void constructComponent() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    JPanel listPanel = new JPanel();
	    JPanel centerPanel = new JPanel();
	    
	    m_jasper = new RptEmployeeListJasper(m_conn, m_sessionid);
		m_list = new EmployeeListPanel(this,m_conn, m_sessionid,true);
			
	    listPanel.setLayout(new BorderLayout());
	    listPanel.add(m_list, BorderLayout.CENTER);
	    
	    centerPanel.setLayout(new BorderLayout());
	    centerPanel.add(m_centerPanel, BorderLayout.CENTER);
	    
	    splitPane.setLeftComponent(listPanel);
	    splitPane.setRightComponent(m_centerPanel);
	    splitPane.setDividerLocation(200);

	    setLayout(new BorderLayout());
	    add(splitPane, BorderLayout.CENTER);
	}

	public void setJasperEmpty(){
		m_jasper.getEmpty();
		m_jrv = m_jasper.getPrintView();
		m_centerPanel.setLayout(new BorderLayout());
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}

	public void setPrintPreview(Employee[] employees){
		m_jasper.getNonEmpty(employees);			
		m_centerPanel.remove(m_jrv);
		m_jrv = m_jasper.getPrintView();
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}

}
