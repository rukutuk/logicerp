package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class ProjectDataDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  JTabbedPane m_tabbedPane;
  ProjectListPanel m_panel;
  ProjectDataPanel m_projectpanel;
  ProjectPersonalPanel m_personalpanel;
  ProjectContractPanel m_contractpanel;
  ProjectPartnerPanel m_partnerpanel;
  ProjectClientContactPanel m_clientpanel;
  ProjectLocationPanel m_locationpanel;
  JButton m_saveBt, m_closeBt;

  ProjectData m_project = null;
  boolean m_new = false, m_edit = false;

  public ProjectDataDlg(JFrame owner, ProjectListPanel panel, Connection conn, long sessionid) {
    super(owner, "Project Information", true);
    setSize(1000, 600);
    m_mainframe = owner;
    m_panel = panel;
    m_conn = conn;
    m_sessionid = sessionid;
    m_new = true;

    constructComponent();
    setEditable(false);
  }
  

  public ProjectDataDlg(JFrame owner, ProjectListPanel panel,
                        Connection conn, long sessionid, ProjectData project) {
    super(owner, "Project Information", true);
    setSize(1000, 600);
    m_mainframe = owner;
    m_panel = panel;
    m_conn = conn; 
    m_sessionid = sessionid;
    m_project = project;
    m_edit = true;

    constructComponent();
    setEditable(true);
    initData(project);
  }

  void constructComponent() {
    m_projectpanel = new ProjectDataPanel(this, m_conn, m_sessionid);
    m_personalpanel = new ProjectPersonalPanel(m_conn, m_sessionid);
    m_contractpanel = new ProjectContractPanel(m_conn, m_sessionid);
    m_partnerpanel = new ProjectPartnerPanel(m_conn, m_sessionid);
    m_clientpanel = new ProjectClientContactPanel(m_conn, m_sessionid);
    m_locationpanel = new ProjectLocationPanel(m_conn, m_sessionid);

    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_closeBt = new JButton("Close");
    m_closeBt.addActionListener(this);

    m_tabbedPane = new JTabbedPane();
    m_tabbedPane.setBackground(Color.white);
    m_tabbedPane.setForeground(Color.blue.darker().darker());

    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel buttonPanel= new JPanel();

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_closeBt);

    northPanel.setLayout(new BorderLayout());
    northPanel.add(m_projectpanel, BorderLayout.CENTER);
    northPanel.add(buttonPanel, BorderLayout.SOUTH);

    m_tabbedPane.addTab("Personel",  m_personalpanel);
    m_tabbedPane.addTab("Contract", m_contractpanel);
    m_tabbedPane.addTab("Partner",  m_partnerpanel);
    m_tabbedPane.addTab("Client Contact", m_clientpanel);
    m_tabbedPane.addTab("Location", m_locationpanel);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(northPanel, BorderLayout.NORTH);
    centerPanel.add(m_tabbedPane);

    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }

  public void initData(ProjectData project) {
    m_project = project;    
    m_projectpanel.setProjectData(project);
    m_personalpanel.setProjectData(project);
    m_personalpanel.setProjectPersonal(project);

    m_contractpanel.setProjectData(project);
    m_contractpanel.setProjectContract(project);

    m_partnerpanel.setProjectData(project);
    m_partnerpanel.setProjectPartner(project);

    m_clientpanel.setProjectData(project);
    m_clientpanel.setProjectClientContact(project);
    m_locationpanel.setProjectData(project);
    m_locationpanel.setProjectLocation(project);
  }

  void setEditable(boolean editable) {
    m_projectpanel.setEditable(editable);
    m_personalpanel.setEditable(editable);
    m_contractpanel.setEditable(editable);
    m_partnerpanel.setEditable(editable);
    m_locationpanel.setEditable(editable);
    m_clientpanel.setEditable(editable);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  void save() {
    ProjectData project = null;    
    try {
      project = m_projectpanel.getProjectData();      
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }    
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      if(m_new) {
        project = logic.createProjectData(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project);                      
        setEditable(true);
        JOptionPane.showMessageDialog(this, "Data successfully save",
                                    "Warning", JOptionPane.WARNING_MESSAGE);        
      }
      else {                
        project = logic.updateProjectData(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex(), project);                              
      }          
      m_panel.refresh(); 
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning 1", JOptionPane.WARNING_MESSAGE);
    }

    m_new = false;
    m_edit = false;
    m_personalpanel.setProjectData(project);
    m_contractpanel.setProjectData(project);
    m_clientpanel.setProjectData(project);
    m_partnerpanel.setProjectData(project);
    m_locationpanel.setProjectData(project);
    m_project = project;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_closeBt) {
      dispose();
    }
  }
}