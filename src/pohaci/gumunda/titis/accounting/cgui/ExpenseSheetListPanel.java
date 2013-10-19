package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.report.RptEmployeePresence;
import pohaci.gumunda.titis.hrm.cgui.report.RptEmployeeProfilePanel;
import pohaci.gumunda.titis.hrm.cgui.report.RptPaycheques;

public class ExpenseSheetListPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	ExpenseSheetList m_list;
	JToggleButton m_searchBt, m_refreshBt; 
	JButton m_printviewBt;
	int objnya;
	Connection m_conn = null;
	long m_sessionid = -1;
	boolean m_show = false;
	boolean m_printview = false;
	Employee[] m_employee;
	RptEmployeePresence m_panel_empPresence = null;
	public RptPaycheques m_panel_rptPaycheq= null;
	public RptEmployeeProfilePanel m_panel_rptEmployeProfile= null;
	
	public void setObjnya(int i){
		this.objnya=i;
	}
	public ExpenseSheetListPanel(Connection conn, long sessionid,int obj) {
		m_conn = conn;
		m_sessionid = sessionid;
		objnya=obj;
		constructComponent(obj);
	}
	
	void constructComponent(int obj) {
		m_list = new ExpenseSheetList(m_conn, m_sessionid,obj);
		m_list.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 1) {
					// String temp=(String)m_list.getModel().getElementAt(m_list.getSelectedIndex());
					if (m_list.getSelectedIndex()!=-1)
					{ Employee temp=(Employee)m_list.getModel().getElementAt(m_list.getSelectedIndex());
					System.err.println("first name : " +temp.getFirstName());
					setSelectedEmployee(temp);}		
				}
			}
		});
		m_searchBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter2.gif"));
		m_searchBt.setToolTipText("Filter");
		m_searchBt.addActionListener(this);
		m_refreshBt = new javax.swing.JToggleButton(new ImageIcon("../images/refresh.gif"));
		m_refreshBt.setToolTipText("Refresh");
		m_refreshBt.addActionListener(this);    
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(m_searchBt);
		bg.add(m_refreshBt);    
		
		JToolBar toolbar = new JToolBar();
		toolbar.add(m_searchBt);
		toolbar.add(m_refreshBt);
		if (m_printview){
			m_printviewBt =new JButton("PrintPreview");    
			m_printviewBt.addActionListener(this);
			bg.add(m_printviewBt);
			toolbar.add(m_printviewBt);
		}
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		JScrollPane scroll= new JScrollPane(m_list);
		add(scroll, BorderLayout.CENTER);
	}
	
	public JList getList() {
		return m_list;
	}
	Employee m_selected=null;
	public void  setSelectedEmployee(Employee obj){
		m_selected=obj;	  
	}
	
	public void refreshData(){		
		m_list.initData(objnya); 
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_searchBt) {
			if(!m_show) {
				m_show = true;
				/*SearchEmployeeDlg dlg = new SearchEmployeeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				 this, m_conn, m_sessionid,"EMPLOYEE");//SearchEmployeeDlg(
				 dlg.setVisible(true);*/
				m_show = false;
			}
		}
		else if(e.getSource() == m_refreshBt) {
			m_list.refresh(objnya);
			if (m_panel_rptEmployeProfile!=null)
				m_panel_rptEmployeProfile.m_employee = null;
			else if (m_panel_rptPaycheq!=null)
				m_panel_rptPaycheq.m_employee = null;
		}else if (e.getSource() == m_printviewBt){
			if (m_panel_empPresence!=null)
				m_panel_empPresence.setJasper(m_list.getEmployee());
			else if (m_panel_rptEmployeProfile!=null){
				m_panel_rptEmployeProfile.setPrintPreview();
			}else {
				m_panel_rptPaycheq.setPrintPreview();
			}
		}
	}
	
	public Employee getSelectedEmployee(){
		return m_selected;
	}
	
	public void enabledList(boolean bol){
		m_list.setEnabled(bol);
	}
}

