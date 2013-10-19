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
import java.text.SimpleDateFormat;
import javax.swing.*;

import pohaci.gumunda.cgui.JNumberField;
import pohaci.gumunda.titis.accounting.cgui.*;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectMonitoringPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTextField m_codeTextField, m_custTextField, m_unitTextField,
  m_actTextField, m_departTextField;
  JButton m_codeBt;
  JTextArea m_wrkdescTextArea;
  JTextField m_ORNoTextField, m_PONoTextField, m_IPCNoTextField;
  JTextField m_ORDateTextField, m_PODateTextField, m_IPCDateTextField;
  JTextField m_startdateTextField, m_enddateTextField, m_currencyTextField, m_valdateTextField;
  JNumberField m_valueNumberField;
  VoucherPanel m_voucherPanel;
  InvoicePanel m_invoicePanel;
  ProjectProgressPanel m_progressPanel;
  ProjectNotesPanel m_notesPanel;  
  protected Unit m_unit; 
  protected Activity m_activity;
  protected Organization m_org;
  protected ProjectContract  m_contract;
  protected Currency m_currency;

  Connection m_conn = null;
  long m_sessionid = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  ProjectData m_project = null;

  public ProjectMonitoringPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {       
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
    m_wrkdescTextArea.setBackground(Color.lightGray);
    m_wrkdescTextArea.setForeground(Color.black);
    m_wrkdescTextArea.setEditable(false);
    JScrollPane sp = new JScrollPane(m_wrkdescTextArea);
    sp.setPreferredSize(new Dimension(100, 70));

    m_codeBt = new JButton("...");
    m_codeBt.addActionListener(this);
    m_codeBt.setPreferredSize(new Dimension(22, 18));

    m_ORNoTextField = new JTextField();
    m_ORNoTextField.setEditable(false);
    m_PONoTextField = new JTextField();
    m_PONoTextField.setEditable(false);
    m_IPCNoTextField = new JTextField();
    m_IPCNoTextField.setEditable(false);
    m_ORDateTextField = new JTextField();
    m_ORDateTextField.setEditable(false);
    m_PODateTextField = new JTextField();
    m_PODateTextField.setEditable(false);
    m_IPCDateTextField = new JTextField();
    m_IPCDateTextField.setEditable(false);

    m_startdateTextField = new JTextField();
    m_startdateTextField.setEditable(false);
    m_enddateTextField = new JTextField();
    m_enddateTextField.setEditable(false);
    m_currencyTextField = new JTextField(6);
    m_currencyTextField.setEditable(false);
    m_valueNumberField = new JNumberField();
    m_valueNumberField.setEditable(false);
    m_valdateTextField = new JTextField();
    m_valdateTextField.setEditable(false);
    
    // component voucher
    m_voucherPanel = new VoucherPanel(m_conn, m_sessionid);
    // component invoice
    m_invoicePanel = new InvoicePanel(m_conn, m_sessionid);
    
    // component Progress
    m_progressPanel = new ProjectProgressPanel(m_conn, m_sessionid);
    // component Notes
    m_notesPanel = new ProjectNotesPanel(m_conn, m_sessionid);
    
    // untuk label
    JLabel prjLabel = new JLabel("Project Code*");
    JLabel custLabel = new JLabel("Customer*");
    JLabel wrkdescLabel = new JLabel("Work Desc.");
    JLabel unitLabel = new JLabel("Unit Code*");
    JLabel actLabel = new JLabel("Activity Code*");
    JLabel departLabel = new JLabel("Department*");
    JLabel ORNoLabel = new JLabel("O.R No");
    JLabel PONoLabel = new JLabel("SO/PO/Contract No.");
    JLabel IPCNoLabel = new JLabel("IPC No.");
    JLabel ORDateLabel = new JLabel("O.R Date");
    JLabel PODateLabel = new JLabel("SO/PO/Contract Date");
    JLabel IPCDateLabel = new JLabel("IPC Date");
    JLabel startLabel = new JLabel("Project Start Date");
    JLabel endLabel = new JLabel("Project End Date");
    JLabel valueLabel = new JLabel("Total Value");
    JLabel validationLabel = new JLabel("Contract Validation");
    
    // instant panel
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel northrightPanel = new JPanel();
    JPanel centerrightPanel = new JPanel();
    JPanel valuePanel = new JPanel();
    JTabbedPane tabbedPane = new JTabbedPane();
    
    leftPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0; 
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    leftPanel.add(prjLabel, gridBagConstraints);
    
    // project code component
    gridBagConstraints.gridx = 0;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_codeTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 3, 1, 3);
    leftPanel.add(m_codeBt, gridBagConstraints);

    // customer componen
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
    
    // unit code komponen
   gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(unitLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(m_unitTextField, gridBagConstraints);
    
    //activity label komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(actLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(m_actTextField, gridBagConstraints);
    
    // departement komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(departLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(m_departTextField, gridBagConstraints);

    
    northrightPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    
    // O.R no komponen
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    northrightPanel.add(ORNoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northrightPanel.add(new JLabel(" "), gridBagConstraints);    
    
    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northrightPanel.add(m_ORNoTextField, gridBagConstraints);    
    
    // O.R date komponent
    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    northrightPanel.add(ORDateLabel, gridBagConstraints);
    
    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    northrightPanel.add(new JLabel(" "), gridBagConstraints);
            
    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northrightPanel.add(m_ORDateTextField, gridBagConstraints);

    // SO/PO/Contract No.
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    northrightPanel.add(PONoLabel, gridBagConstraints);
    
    gridBagConstraints.gridx = 1;
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northrightPanel.add(m_PONoTextField, gridBagConstraints);
    
    // SO/PO/Contract date komponent
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

    // IPC No komponen
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    northrightPanel.add(IPCNoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northrightPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    northrightPanel.add(m_IPCNoTextField, gridBagConstraints);

    // IPC Date komponent
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
    
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    northrightPanel.add(centerrightPanel, gridBagConstraints);
    
    valuePanel.setLayout(new BorderLayout(3, 0));
    valuePanel.add(m_currencyTextField, BorderLayout.WEST);
    valuePanel.add(m_valueNumberField, BorderLayout.CENTER);
    
    centerrightPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    
    // Project Start Date komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    centerrightPanel.add(startLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerrightPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerrightPanel.add(m_startdateTextField, gridBagConstraints);
    
    // Project End Date komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerrightPanel.add(endLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerrightPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerrightPanel.add(m_enddateTextField, gridBagConstraints);

    // Total value komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerrightPanel.add(valueLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerrightPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerrightPanel.add(valuePanel, gridBagConstraints);

    // contract validation komponent
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerrightPanel.add(validationLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerrightPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerrightPanel.add(m_valdateTextField, gridBagConstraints);
    
    // set nortrightpanel pada righpanel
    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(northrightPanel, BorderLayout.NORTH);       
    
    // set left panel dan righpanel pada northpanel
    northPanel.setLayout(new GridLayout(1, 2, 10, 0));
    northPanel.add(leftPanel); 
    northPanel.add(rightPanel);
    
    
    tabbedPane.addTab("Voucher", m_voucherPanel);
    tabbedPane.addTab("Invoice", m_invoicePanel);
    tabbedPane.addTab("Progress", m_progressPanel);
    tabbedPane.addTab("Notes", m_notesPanel);

    // set northpanel dan tab panel
    setLayout(new BorderLayout(0, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    add(northPanel, BorderLayout.NORTH);
    add(tabbedPane, BorderLayout.CENTER);
  }
  
    
  void project() {
    SearchProjectDetailDlg dlg = new SearchProjectDetailDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      ProjectData[] project = dlg.getProjectData();
      if(project.length > 0) {         
        ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
        m_project = project[0];
        setNullFieldText();
        m_codeTextField.setText(m_project.getCode());
        m_custTextField.setText(m_project.getCustname());
        m_wrkdescTextArea.setText(m_project.getWorkDescription());                              
                
        m_unit = logic.getUnit(m_project.getIndexunit());           
        if (m_unit!=null)
            m_unitTextField.setText(m_unit.getCode() + " " + m_unit.getDescription());       
        
        m_activity = logic.getActivity(m_project.getIndexact());    
        if (m_activity!=null)
            m_actTextField.setText(m_activity.getCode() + " " + m_activity.getName());
      
        m_org = logic.getOrganization(m_project.getIndexdept());    
        if (m_org!=null)
            m_departTextField.setText(m_org.getName());
     
        if (m_project.getORNo()!=null)
            m_ORNoTextField.setText(m_project.getORNo());
             
        if (m_project.getPONo()!=null)
            m_PONoTextField.setText(m_project.getPONo());
             
        if(m_project.getIPCNo()!=null)
            m_IPCNoTextField.setText(m_project.getIPCNo());      
        
        if(m_project.getORDate() != null)
          m_ORDateTextField.setText(m_dateformat.format(m_project.getORDate()));
            
        if(m_project.getPODate() != null)
            m_PODateTextField.setText(m_dateformat.format(m_project.getPODate()));
                  
        if(m_project.getIPCDate() != null)
            m_IPCDateTextField.setText(m_dateformat.format(m_project.getIPCDate()));        
        
        
        m_startdateTextField.setText("data");
        if(m_project.getStartdate()!= null)
           m_startdateTextField.setText(m_dateformat.format(m_project.getStartdate()));
    
        if(m_project.getEnddate() != null)
            m_enddateTextField.setText(m_dateformat.format(m_project.getEnddate()));
       
        if(m_project.getValidation() != null)
            m_valdateTextField.setText(m_dateformat.format(m_project.getValidation()));
   
        try{ 
            m_contract = logic.getProjectContract(m_sessionid,"get project contract",m_project.getIndex());
        } catch (Exception ex){            
        }
              
        if (m_contract!=null){                              
           m_currency = logic.getCurrecy(m_sessionid,m_contract.getCurr());
           if (m_currency.getCode()!=null)
               m_currencyTextField.setText(m_currency.getSymbol());                            
           
           if (m_contract.getValue()!=0)
                m_valueNumberField.setValue(m_contract.getValue());             
        }   

        m_progressPanel.setProjectData(m_project);
        m_progressPanel.setProgress(m_project);
        m_progressPanel.setEditable(true);
        m_notesPanel.setProjectData(m_project);
        m_notesPanel.setNotes(m_project);
        m_notesPanel.setEditable(true);
        m_invoicePanel.setProjectData(m_project);
        m_invoicePanel.setInvoice(m_project);
        m_invoicePanel.setEditable(true);
        m_voucherPanel.setProjectData(m_project);
        m_voucherPanel.setVoucher(m_project);
        m_voucherPanel.setEditable(true);
      }
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
      m_ORDateTextField.setText(" ");
      m_PODateTextField.setText(" ");
      m_IPCDateTextField.setText(" ");            
      m_startdateTextField.setText(" ");
      m_enddateTextField.setText(" ");
      m_valdateTextField.setText(" ");
      m_currencyTextField.setText(" ");          
      m_valueNumberField.setValue(0.0);  
        
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_codeBt) {
      project();
    }
  }
}