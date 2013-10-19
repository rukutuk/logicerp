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

import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.accounting.cgui.*;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
//import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
//import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class ProjectDataPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
  long m_sessionid = -1;

  ProjectDataDlg m_projectdataDlg;
  JTextField m_codeTextField, m_custTextField, m_unitTextField,
  m_actTextField, m_departTextField;
  JButton m_codeBt, m_custBt, m_unitBt, m_actBt, m_departBt;
  JTextArea m_wrkdescTextArea;

  JTextField m_ORNoTextField, m_PONoTextField, m_IPCNoTextField;
  DatePicker m_regDatePicker, m_ORDatePicker, m_PODatePicker, m_IPCDatePicker;
  FilePanel m_filePanel;

  ProjectData m_project = null;
  Customer m_customer = null;
  Unit m_unit = null;
  Activity m_activity = null;
  Organization m_org = null;

  boolean m_editable = false;   

  public ProjectDataPanel(ProjectDataDlg projectdataDlg, Connection conn, long sessionid) {
    m_projectdataDlg = projectdataDlg;
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();   
  }
  
  public ProjectDataPanel(Connection conn){      
    m_conn = conn;  
  }
  
  public void setEditable(boolean editable) {
    m_editable = editable;
    m_codeBt.setEnabled(editable);
    if(!m_editable)
      m_regDatePicker.setDate(new java.util.Date());
  }

  void constructComponent() {
    m_codeTextField = new JTextField();
    m_custTextField = new JTextField();
    m_custTextField.setRequestFocusEnabled(false);
    m_unitTextField = new JTextField();
    m_unitTextField.setRequestFocusEnabled(false);
    m_actTextField = new JTextField();
    m_actTextField.setRequestFocusEnabled(false);
    m_departTextField = new JTextField();
    m_departTextField.setRequestFocusEnabled(false);
    m_wrkdescTextArea = new JTextArea();

    m_codeBt = new JButton("...");
    m_codeBt.setPreferredSize(new Dimension(22, 18));
    m_codeBt.addActionListener(this);
    m_custBt = new JButton("...");
    m_custBt.setPreferredSize(new Dimension(22, 18));
    m_custBt.addActionListener(this);
    m_unitBt = new JButton("...");
    m_unitBt.setPreferredSize(new Dimension(20, 18));
    m_unitBt.addActionListener(this);
    m_actBt = new JButton("...");
    m_actBt.setPreferredSize(new Dimension(20, 18));
    m_actBt.addActionListener(this);
    m_departBt = new JButton("...");
    m_departBt.setPreferredSize(new Dimension(20, 18));
    m_departBt.addActionListener(this);

    JScrollPane sp = new JScrollPane(m_wrkdescTextArea);
    m_wrkdescTextArea.setLineWrap(true);
    sp.setPreferredSize(new Dimension(100, 70));

    m_regDatePicker = new DatePicker();
    m_ORNoTextField = new JTextField();
    m_PONoTextField = new JTextField();
    m_IPCNoTextField = new JTextField();
    m_ORDatePicker = new DatePicker();
    m_PODatePicker = new DatePicker();
    m_IPCDatePicker = new DatePicker();

    m_filePanel = new FilePanel("Calculation Sheet Attachment");

    JLabel prjLabel = new JLabel("Project Code*");
    JLabel custLabel = new JLabel("Customer*");
    JLabel wrkdescLabel = new JLabel("Work Desc.");
    JLabel unitLabel = new JLabel("Unit Code*");
    JLabel actLabel = new JLabel("Activity Code*");
    JLabel departLabel = new JLabel("Department*");

    JLabel regDateLabel = new JLabel("Reg Date*");
    JLabel ORNoLabel = new JLabel("O.R No");
    JLabel PONoLabel = new JLabel("SO/PO/Contract No.");
    JLabel IPCNoLabel = new JLabel("IPC No.");
    JLabel ORDateLabel = new JLabel("O.R Date");
    JLabel PODateLabel = new JLabel("SO/PO/Contract Date");
    JLabel IPCDateLabel = new JLabel("IPC Date");

    JPanel centerPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel northPanel = new JPanel();

    leftPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    leftPanel.add(prjLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_codeTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 3, 1, 3);
    leftPanel.add(m_codeBt, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(custLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_custTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 3, 1, 3);
    leftPanel.add(m_custBt, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    leftPanel.add(wrkdescLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(sp, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(unitLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_unitTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 3, 1, 3);
    leftPanel.add(m_unitBt, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(actLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_actTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 3, 1, 3);
    leftPanel.add(m_actBt, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(departLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_departTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 3, 1, 3);
    leftPanel.add(m_departBt, gridBagConstraints);

    // rightpanel
    northPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northPanel.add(regDateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    northPanel.add(m_regDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    northPanel.add(ORNoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northPanel.add(m_ORNoTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northPanel.add(ORDateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    northPanel.add(m_ORDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    northPanel.add(PONoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northPanel.add(m_PONoTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northPanel.add(PODateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    northPanel.add(m_PODatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    northPanel.add(IPCNoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northPanel.add(m_IPCNoTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northPanel.add(IPCDateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    northPanel.add(m_IPCDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(4, 0, 1, 0);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(m_filePanel, gridBagConstraints);

    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(northPanel, BorderLayout.NORTH);

    centerPanel.setLayout(new GridLayout(1, 2, 5, 0));
    centerPanel.add(leftPanel);
    centerPanel.add(rightPanel);
    
    setLayout(new BorderLayout());
    add(centerPanel, BorderLayout.CENTER);
  }

  void clear() {

  }

  void project() {   
    SearchProjectDetailDlg dlg = new SearchProjectDetailDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);
      //SearchPersonalDlg dlg = new SearchPersonalDlg(
              //pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        //m_conn, m_sessionid);
     //dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      ProjectData[] project = dlg.getProjectData();
      if(project.length > 0) {
        m_project = project[0];
        setProjectData(m_project);
        m_projectdataDlg.initData(m_project);
      }
    }
  }

  void customer() {
    SearchCustomerDetailDlg dlg = new SearchCustomerDetailDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      Customer[] customer = dlg.getCustomer();
      if(customer.length > 0) {
        m_customer = customer[0];
        m_custTextField.setText(m_customer.getName());
      }
    }
  }

  void unit() {
    UnitListDlg dlg = new UnitListDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      m_unit = dlg.getUnit();
      m_unitTextField.setText(m_unit.getCode() + " " + m_unit.getDescription());
    }
  }

  void activity() {
    ActivityTreeDlg dlg = new ActivityTreeDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      m_activity = dlg.getActivity();
      m_actTextField.setText(m_activity.toString());
    }
  }

  void organization() {
    OrganizationTreeDlg dlg = new OrganizationTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid,false);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      m_org = dlg.getOrganization();
      m_departTextField.setText(m_org.getName());
    }
  }
  
  
  public void setProjectData(ProjectData project) {    
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    //java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("dd-MM-yyyy");
    setNullFieldText();
    m_codeTextField.setText(project.getCode());    
    try{
        m_customer = logic.getCustomerByCriteria1(project.getIndexcust(),m_conn);
    } catch(Exception e) {        
    }
    if (m_customer!=null)
        m_custTextField.setText(m_customer.getName());
    
    m_wrkdescTextArea.setText(project.getWorkDescription());
    
    m_unit = logic.getUnit(project.getIndexunit());   
    if (m_unit!=null)        
        m_unitTextField.setText(m_unit.getCode() + " " + m_unit.getDescription());
    
    m_activity = logic.getActivity(project.getIndexact());
    if (m_activity!=null)        
        m_actTextField.setText(m_activity.getCode() + " " + m_activity.getName());
    
    m_org = logic.getOrganization(project.getIndexdept());
    if (m_org!=null)
        m_departTextField.setText(m_org.getName());
    
    if(project.getRegDate() != null)
      m_regDatePicker.setDate(project.getRegDate());

    m_ORNoTextField.setText(project.getORNo());
    m_PONoTextField.setText(project.getPONo());
    m_IPCNoTextField.setText(project.getIPCNo());

    if(project.getORDate() != null)
      m_ORDatePicker.setDate(project.getORDate());
    if(project.getPODate() != null)
      m_PODatePicker.setDate(project.getPODate());
    if(project.getIPCDate() != null)
      m_IPCDatePicker.setDate(project.getIPCDate());
    
    m_filePanel.setFileName(project.getFile());
    m_filePanel.setFileByte(project.getSheet());
  }

  public ProjectData getProjectData() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    String code = m_codeTextField.getText().trim();    
    if(code.equals(""))
      list.add("Project Code");

    if(m_customer == null)    
      list.add("Customer");

    String wrkdesc = m_wrkdescTextArea.getText().trim();
    if(m_unit == null)   
      list.add("Unit Code");

    if(m_activity == null)    
      list.add("Activity Code");

    if(m_org == null)    
      list.add("Department");

    java.util.Date regdate = null, ordate = null, podate = null, ipcdate = null;
    regdate = m_regDatePicker.getDate();
    if(regdate == null)
      list.add("Reg Date");

    String orno = m_ORNoTextField.getText().trim();
    ordate = m_ORDatePicker.getDate();

    String pono = m_PONoTextField.getText().trim();
    podate = m_PODatePicker.getDate();

    String ipcno = m_IPCNoTextField.getText().trim();
    ipcdate = m_IPCDatePicker.getDate();

    String file = m_filePanel.getFileName();
    byte[] sheet = m_filePanel.getFileByte();

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += "- " + exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new ProjectData(code, m_customer, m_unit, m_activity, m_org, wrkdesc, regdate,
                           orno, pono, ipcno, ordate, podate, ipcdate, file, sheet);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_codeBt) {
      project();
    }
    else if(e.getSource() == m_custBt) {
      customer();
    }
    else if(e.getSource() == m_unitBt) {
      unit();
    }
    else if(e.getSource() == m_actBt) {
      activity();
    }
    else if(e.getSource() == m_departBt) {
      organization();
    }
  }
  
     public void setNullFieldText(){
        m_codeTextField.setText(" ");    
        m_custTextField.setText(" ");    
        m_wrkdescTextArea.setText(" ");
        m_unitTextField.setText(" ");
        m_actTextField.setText(" ");
        m_departTextField.setText(" ");
        m_ORNoTextField.setText(" ");
        m_PONoTextField.setText(" ");
        m_IPCNoTextField.setText(" ");    
        m_ORDatePicker.setDate(null);     
        m_PODatePicker.setDate(null);
        m_IPCDatePicker.setDate(null);    
     }
 }