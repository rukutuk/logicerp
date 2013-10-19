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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.report.RptEmployeeListPanel;
import pohaci.gumunda.titis.hrm.cgui.report.RptEmployeePresence;
import pohaci.gumunda.titis.hrm.cgui.report.RptEmployeeProfilePanel;
import pohaci.gumunda.titis.hrm.cgui.report.RptPaycheques;

public class EmployeeListPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	EmployeeList m_list;
	JToggleButton m_searchBt, m_refreshBt; 
	JButton m_printviewBt;	
	Connection m_conn = null;
	long m_sessionid = -1;
	boolean m_show = false;
	boolean m_printview = false;
	Employee[] m_employee;
	RptEmployeePresence m_panel_empPresence = null;
	public RptPaycheques m_panel_rptPaycheq= null;
	public RptEmployeeProfilePanel m_panel_rptEmployeProfile= null;
	public RptEmployeeListPanel m_panel_rptEmployeList= null;
	
	private String numberString = "Viewed Employee: ";
	private JLabel numberLabel;
	
	//Tambahan cok gung
	int buatRcvEmp=0;
	
	public void setEnabledList(boolean bool){
		m_list.setEnabled(bool);
	}
	
	public EmployeeListPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		
		constructComponent();
	}
	public EmployeeListPanel(Connection conn, long sessionid,int i) {
		m_conn = conn;
		m_sessionid = sessionid;
		buatRcvEmp=i;
		constructComponent();
	}
	
	public EmployeeListPanel(RptEmployeePresence panel,Connection conn, long sessionid, boolean printview) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_printview = printview;
		m_panel_empPresence = panel;
		constructComponent();
	}
	
	public EmployeeListPanel(RptPaycheques panel,Connection conn, long sessionid, boolean printview) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_printview = printview;
		m_panel_rptPaycheq = panel;
		constructComponent();
	}
	
	
	public EmployeeListPanel(RptEmployeeProfilePanel panel, Connection conn, long sessionid, boolean printview) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_printview = printview;
		m_panel_rptEmployeProfile = panel;
		constructComponent();
	}
	
	public EmployeeListPanel(RptEmployeeListPanel panel, Connection conn, long sessionid, boolean printview) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_printview = printview;
		m_panel_rptEmployeList = panel;
		constructComponent();
		buatRcvEmp=2;
	}
	
	void constructComponent() {
		if (buatRcvEmp==0)
			m_list = new EmployeeList(m_conn, m_sessionid);
		else if(buatRcvEmp==1)
			m_list=new EmployeeList(m_conn, m_sessionid,1);	
		else
			m_list=new EmployeeList(m_conn, m_sessionid,2);	
		//Tambahan cok gung 9 Juni
		m_list.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 1) {
					Employee temp=(Employee)m_list.getModel().getElementAt(m_list.getSelectedIndex());
                                        System.out.println("=======================" + temp.getFirstName());
					setSelectedEmployee(temp);	
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
		
		JPanel centerListPanel = new JPanel();
		centerListPanel.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
		
		numberLabel = new JLabel();
		setLabel();
		northPanel.add(numberLabel, BorderLayout.CENTER);
		
		centerListPanel.add(northPanel, BorderLayout.NORTH);
		centerListPanel.add(new JScrollPane(m_list), BorderLayout.CENTER);
		//add(new JScrollPane(m_list), BorderLayout.CENTER);
		add(centerListPanel, BorderLayout.CENTER);
	}
	
	public JList getList() {
		return m_list;
	}
	
	public void reset(Employee[] employee) {
		m_list.reset(employee);
		setLabel();
	}
	
	private void setLabel() {
		numberLabel.setText(numberString + m_list.getModel().getSize());
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_searchBt) {
			if(!m_show) {
				m_show = true;
				SearchEmployeeDlg dlg = new SearchEmployeeDlg(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						this, m_conn, m_sessionid);
				dlg.setVisible(true);
				m_show = false;
			}
		}
		else if(e.getSource() == m_refreshBt) {
			m_list.refresh();
			setLabel();
			if (m_panel_rptEmployeProfile!=null)
				m_panel_rptEmployeProfile.m_employee = null;
			else if (m_panel_rptPaycheq!=null)
				m_panel_rptPaycheq.m_employee = null;
			else if(m_panel_rptEmployeList != null)
				m_panel_rptEmployeList.m_employees = null;
		}else if (e.getSource() == m_printviewBt){
			if (m_panel_empPresence!=null)
				m_panel_empPresence.setJasper(m_list.getEmployee());
			else if (m_panel_rptEmployeProfile!=null){
				m_panel_rptEmployeProfile.setPrintPreview();
				//m_panel_rptEmployeProfile.setJasper(m_list.getEmployee());
			}else if(m_panel_rptEmployeList != null){
				m_panel_rptEmployeList.setPrintPreview(m_list.getEmployee());
			}else {
				m_panel_rptPaycheq.setPrintPreview();
			}
			
		}
	}
	//Tambahan cok gung 9 Juni
	Employee m_selected=null;
	public void setSelectedEmployee(Employee obj){
		m_selected=obj;	  
	}
	public Employee getSelectedEmployee(){
		return m_selected;
	}
	
	public void ambilEmployeeReceivable()
	{ 
		GenericMapper mapnya = MasterMap.obtainMapperFor(PmtEmpReceivable.class);
		Object[] listData=mapnya.doSelectAll().toArray();
		DefaultListModel model = (DefaultListModel)m_list.getModel();
		model.clear();
		for(int i = 0; i < listData.length; i ++){
			PmtEmpReceivable data=(PmtEmpReceivable)listData[i];
			model.addElement((Employee)data.getPayTo());
			
		}
	}
}