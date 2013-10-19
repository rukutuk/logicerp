package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class TimeSheetPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTextField m_codeTextField, m_custTextField, m_unitTextField, m_actTextField, m_departTextField;
  JButton m_codeBt;
  JTextArea m_wrkdescTextArea;
  JTextField m_PONoTextField, m_IPCNoTextField;
  JTextField m_PODateTextField, m_IPCDateTextField;
  TimeSheetDetailPanel m_detailPanel;

  Connection m_conn = null;
  long m_sessionid = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  ProjectData m_project = null;

  protected Customer m_customer=null;
  protected Unit m_unit = null;
  protected Activity m_activity = null;
  protected Organization m_org =null;
  
  public TimeSheetPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
  }

  void constructComponent(){        
    // definisikan input text
    m_codeTextField = new JTextField();
    m_codeTextField.setRequestFocusEnabled(false);
    m_custTextField = new JTextField();
    m_custTextField.setEditable(false);
    m_unitTextField = new JTextField();
    m_unitTextField.setEditable(false);
    m_actTextField = new JTextField();
    m_actTextField.setEditable(false);
    m_departTextField = new JTextField();
    m_departTextField.setEditable(false);
    m_wrkdescTextArea = new JTextArea();
    m_wrkdescTextArea.setEditable(false);
    m_wrkdescTextArea.setBackground(Color.lightGray);
    m_wrkdescTextArea.setForeground(Color.black);
    JScrollPane sp = new JScrollPane(m_wrkdescTextArea);
    sp.setPreferredSize(new Dimension(100, 70));

    // definisikan button
    m_codeBt = new JButton("...");
    m_codeBt.addActionListener(this);
    m_codeBt.setPreferredSize(new Dimension(22, 18));
    
    m_PONoTextField = new JTextField();
    m_PONoTextField.setEditable(false);
    m_IPCNoTextField = new JTextField();
    m_IPCNoTextField.setEditable(false);
    m_PODateTextField = new JTextField();
    m_PODateTextField.setEditable(false);
    m_IPCDateTextField = new JTextField();
    m_IPCDateTextField.setEditable(false);
    
    // set detail panel
    m_detailPanel = new TimeSheetDetailPanel(m_conn, m_sessionid);
    
    // definisikan label
    JLabel prjLabel = new JLabel("Project Code*");
    JLabel custLabel = new JLabel("Customer");
    JLabel wrkdescLabel = new JLabel("Work Desc.");
    JLabel unitLabel = new JLabel("Unit Code");
    JLabel actLabel = new JLabel("Activity Code");
    JLabel departLabel = new JLabel("Department");
    JLabel PONoLabel = new JLabel("SO/PO/Contract No.");
    JLabel IPCNoLabel = new JLabel("IPC No.");
    JLabel PODateLabel = new JLabel("SO/PO/Contract Date");
    JLabel IPCDateLabel = new JLabel("IPC Date");
    
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel northrightPanel = new JPanel();

    // left panel
    leftPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
    // project code component
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    leftPanel.add(prjLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_codeTextField, gridBagConstraints);
    
    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 3, 1, 3);
    leftPanel.add(m_codeBt, gridBagConstraints);

    // customer komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(custLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(m_custTextField, gridBagConstraints);
    
    // work desc komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    leftPanel.add(wrkdescLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(sp, gridBagConstraints);

    // northright panel
    northrightPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    
    // unit code komponent
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    northrightPanel.add(unitLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northrightPanel.add(m_unitTextField, gridBagConstraints);
    
    // SO/PO/Contract No. komponen
    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northrightPanel.add(PONoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northrightPanel.add(m_PONoTextField, gridBagConstraints);
    
    // activity code komponen
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    northrightPanel.add(actLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northrightPanel.add(m_actTextField, gridBagConstraints);

    // SO/PO/Contract Date komponen
    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northrightPanel.add(PODateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northrightPanel.add(m_PODateTextField, gridBagConstraints);
    
    // departement komponen
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    northrightPanel.add(departLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northrightPanel.add(m_departTextField, gridBagConstraints);

    // IPC No. komponen
    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northrightPanel.add(IPCNoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northrightPanel.add(m_IPCNoTextField, gridBagConstraints);

    // IPC Date komponen
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northrightPanel.add(IPCDateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northrightPanel.add(m_IPCDateTextField, gridBagConstraints);
    
    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(northrightPanel, BorderLayout.NORTH);

    northPanel.setLayout(new GridLayout(1, 2, 10, 0));
    northPanel.add(leftPanel);
    northPanel.add(rightPanel);

    setLayout(new BorderLayout(0, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    add(northPanel, BorderLayout.NORTH);
    add(m_detailPanel, BorderLayout.CENTER);
  }

  void project() {
    SearchProjectDetailDlg dlg = new SearchProjectDetailDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      ProjectData[] project = dlg.getProjectData();
      if(project.length > 0) {        
        m_project = project[0];
        
        setNullFieldText();
        m_codeTextField.setText(m_project.getCode());
        
        ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
        
        try{            
            m_customer = logic.getCustomerByCriteria1(m_project.getIndexcust(),m_conn);
        }catch(Exception ex){            
        }
        
        if (m_customer!=null)
            m_custTextField.setText(m_customer.getName());                  
        
        if (m_project.getWorkDescription()!=null)
            m_wrkdescTextArea.setText(m_project.getWorkDescription());
        
        m_unit = logic.getUnit(m_project.getIndexunit());
        if (m_unit!=null)
            m_unitTextField.setText(m_unit.getCode() + " " + m_unit.getDescription());
        
        m_activity = logic.getActivity(m_project.getIndexact());
        if (m_activity!=null)
            m_actTextField.setText(m_activity.getName());
        
        m_org = logic.getOrganization(m_project.getIndexdept());
        if (m_org!=null)
            m_departTextField.setText(m_org.getName());
            
        if (m_project.getPONo()!=null)
            m_PONoTextField.setText(m_project.getPONo());
        
        if (m_project.getIPCNo()!=null)
            m_IPCNoTextField.setText(m_project.getIPCNo());

        if(m_project.getPODate() != null)
          m_PODateTextField.setText(m_dateformat.format(m_project.getPODate()));
        if(m_project.getIPCDate() != null)
          m_IPCDateTextField.setText(m_dateformat.format(m_project.getIPCDate()));

        m_detailPanel.setEditable(true);
        m_detailPanel.setProjectData(m_project);
        m_detailPanel.setTimeSheet(m_project);
      }
    }
  }

  public void setNullFieldText(){
      m_custTextField.setText(" ");
      m_wrkdescTextArea.setText(" ");
      m_codeTextField.setText(" ");
      m_unitTextField.setText(" ");
      m_actTextField.setText(" ");
      m_departTextField.setText(" ");

      m_PONoTextField.setText(" ");
      m_IPCNoTextField.setText(" ");
      m_PODateTextField.setText(" ");
      m_IPCNoTextField.setText(" ");
      m_PODateTextField.setText(" ");
      m_IPCDateTextField.setText(" ");
  }
  
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_codeBt) {
      project();
    }
  }
}