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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.DateCellEditor;
import pohaci.gumunda.cgui.IntegerCellEditor;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.Qualification;
import pohaci.gumunda.titis.hrm.cgui.QualificationCellEditor;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class TimeSheetDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JTextField m_codeTextField, m_custTextField, m_unitTextField, m_actTextField, m_departTextField;
  JTextArea m_wrkdescTextArea;
  JTextField m_PONoTextField, m_IPCNoTextField;
  JTextField m_PODateTextField, m_IPCDateTextField;

  JTextField m_approvedTextField;
  EmployeePicker m_preparedPicker, m_checkedPicker;
  DatePicker m_preparedDatePicker, m_checkedDatePicker, m_entryDatePicker, m_approvedDatePicker;
  JTextArea m_descriptTextArea;

  JTextField m_regulerTextField, m_holidayTextField;
  JComboBox m_personalComboBox,m_personalArea;
  JButton m_save2Bt, m_cancel2Bt;

  JButton m_addBt, /*m_editBt,*/ m_deleteBt;
  TimeSheetTable m_table;

  Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;
  TimeSheet m_timesheet = null;
  int m_iResponse = JOptionPane.NO_OPTION;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  boolean m_new = false, m_edit = false;;
  int m_editedIndex = -1;
  TimeSheetDetail m_detail = null;
  
  protected Customer m_customer = null;
  protected Unit m_unit = null;
  protected Activity m_activity = null;
  protected Organization m_org = null;

  public TimeSheetDlg(JFrame owner, Connection conn, long sessionid, ProjectData project) {
    super(owner, "Time Sheet", true);
    setSize(800, 600);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_project = project;

    constructComponent();
    initData();
  }

  public TimeSheetDlg(JFrame owner, Connection conn, long sessionid,
                      ProjectData project, TimeSheet timesheet) {
    super(owner, "Time Sheet", true);
    setSize(800, 600);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_project = project;
    m_timesheet = timesheet;

    constructComponent();
    initData();
  }

  void constructComponent() {
    m_codeTextField = new JTextField();
    m_codeTextField.setEditable(false);
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

    m_PONoTextField = new JTextField();
    m_PONoTextField.setEditable(false);
    m_IPCNoTextField = new JTextField();
    m_IPCNoTextField.setEditable(false);
    m_PODateTextField = new JTextField();
    m_PODateTextField.setEditable(false);
    m_IPCDateTextField = new JTextField();
    m_IPCDateTextField.setEditable(false);

    m_approvedTextField = new JTextField();
    m_preparedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_checkedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_preparedDatePicker = new DatePicker();
    m_checkedDatePicker = new DatePicker();
    m_entryDatePicker = new DatePicker();
    m_approvedDatePicker = new DatePicker();

    m_descriptTextArea = new JTextArea();
    JScrollPane sp2 = new JScrollPane(m_descriptTextArea);
    sp2.setPreferredSize(new Dimension(100, 70));
    m_descriptTextArea.setLineWrap(true);
  
    m_personalComboBox = new JComboBox();
    m_personalArea = new JComboBox();
    m_regulerTextField = new JTextField();
    m_holidayTextField = new JTextField();
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    //m_editBt = new JButton("Edit");
    //m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);

    //m_addBt.setEnabled(true);
    //m_editBt.setEnabled(true);
    //m_deleteBt.setEnabled(true);

    m_save2Bt = new JButton("Save");
    m_save2Bt.addActionListener(this);
    m_cancel2Bt = new JButton("Cancel");
    m_cancel2Bt.addActionListener(this);
    m_table = new TimeSheetTable();

    JLabel prjLabel = new JLabel("Project Code*");
    JLabel custLabel = new JLabel("Customer");
    JLabel wrkdescLabel = new JLabel("Work Desc.");
    JLabel unitLabel = new JLabel("Unit Code");
    JLabel actLabel = new JLabel("Activity Code");
    JLabel departLabel = new JLabel("Department");
    //JLabel ORNoLabel = new JLabel("O.R No");
    JLabel PONoLabel = new JLabel("PO No.");
    JLabel IPCNoLabel = new JLabel("IPC No.");
    //JLabel ORDateLabel = new JLabel("O.R Date");
    JLabel PODateLabel = new JLabel("PO Date");
    JLabel IPCDateLabel = new JLabel("IPC Date*");

    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel formPanel = new JPanel();

    leftPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    leftPanel.add(prjLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(m_codeTextField, gridBagConstraints);

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

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    leftPanel.add(PONoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_PONoTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    leftPanel.add(PODateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(m_PODateTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    leftPanel.add(IPCNoLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    leftPanel.add(m_IPCNoTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 3, 1, 3);
    leftPanel.add(IPCDateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    leftPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    leftPanel.add(m_IPCDateTextField, gridBagConstraints);

    // rightpanel
    // preparedpanel
    JPanel preparedPanel = new JPanel();
    preparedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Prepared By",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    preparedPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    preparedPanel.add(new JLabel("Name"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    preparedPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    preparedPanel.add(m_preparedPicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    preparedPanel.add(new JLabel("Date "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    preparedPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    preparedPanel.add(m_preparedDatePicker, gridBagConstraints);

    // entry panel
    JPanel entryPanel = new JPanel();
    entryPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    entryPanel.add(new JLabel("Entry Date "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    entryPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    entryPanel.add(m_entryDatePicker, gridBagConstraints);

    // checkedpanel
    JPanel checkedPanel = new JPanel();
    checkedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Checked By",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    checkedPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    checkedPanel.add(new JLabel("Name "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    checkedPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    checkedPanel.add(m_checkedPicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    checkedPanel.add(new JLabel("Date "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    checkedPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    checkedPanel.add(m_checkedDatePicker, gridBagConstraints);

    // approved panel
    JPanel approvedPanel = new JPanel();
    approvedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Approved By Client",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    approvedPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    approvedPanel.add(new JLabel("Name "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    approvedPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    approvedPanel.add(m_approvedTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    approvedPanel.add(new JLabel("Date "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    approvedPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    approvedPanel.add(m_approvedDatePicker, gridBagConstraints);

    // descript panel
    JPanel descriptPanel = new JPanel();
    descriptPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    descriptPanel.add(new JLabel("Description of work "), gridBagConstraints);

    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    descriptPanel.add(sp2, gridBagConstraints);

    JPanel northrightPanel = new JPanel(new GridLayout(2, 2, 2, 2));
    northrightPanel.add(preparedPanel);
    northrightPanel.add(entryPanel);
    northrightPanel.add(checkedPanel);
    northrightPanel.add(approvedPanel);

    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(northrightPanel, BorderLayout.NORTH);
    rightPanel.add(descriptPanel, BorderLayout.SOUTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_save2Bt);
    buttonPanel.add(m_cancel2Bt);

    JPanel centernorthPanel = new JPanel(new GridLayout(1, 2, 10, 0));
    centernorthPanel.add(leftPanel);
    centernorthPanel.add(rightPanel);

    northPanel.setLayout(new BorderLayout());
    northPanel.add(buttonPanel, BorderLayout.NORTH);
    northPanel.add(centernorthPanel, BorderLayout.CENTER);

    
    //overtime panel
    /*JPanel overtimePanel = new JPanel();
    overtimePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Overtime",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    overtimePanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    overtimePanel.add(new JLabel("Reguler "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    overtimePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    overtimePanel.add(m_regulerTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = gridBagConstraints.REMAINDER;
    overtimePanel.add(new JLabel("  Hours "), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    overtimePanel.add(new JLabel("Holiday "), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    overtimePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    overtimePanel.add(m_holidayTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = gridBagConstraints.REMAINDER;
    overtimePanel.add(new JLabel("  Hours "), gridBagConstraints);*/

    // button panel
    
    JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel2.add(m_addBt);
    //buttonPanel2.add(m_editBt);
    buttonPanel2.add(m_deleteBt);

    // northcenter panel
    JPanel northcenterPanel = new JPanel();
    northcenterPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    //gridBagConstraints.insets = new Insets(20, 5, 20, 5);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    
    //northcenterPanel.add(overtimePanel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = GridBagConstraints.SOUTHEAST;
    northcenterPanel.add(buttonPanel2, gridBagConstraints);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEtchedBorder());
    centerPanel.add(northcenterPanel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);

    // form panel
    formPanel.setLayout(new BorderLayout(0, 20));
    formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    formPanel.add(northPanel, BorderLayout.NORTH);
    formPanel.add(centerPanel, BorderLayout.CENTER);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(formPanel, BorderLayout.CENTER);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  void initData() {
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    getNullTextField();
    m_codeTextField.setText(m_project.getCode());    
    try{
        m_customer = logic.getCustomerByCriteria1(m_project.getIndexcust(),m_conn);
    } catch (Exception ex){        
    }
    
    if (m_customer!=null)
        m_custTextField.setText(m_customer.getName());
        //m_custTextField.setText(m_project.getCustomer().toString());
    
    m_wrkdescTextArea.setText(m_project.getWorkDescription());
    
    m_unit = logic.getUnit(m_project.getIndexunit());
    
    if (m_unit!= null)
        m_unitTextField.setText(m_unit.getCode() + " " + m_unit.getDescription());
    
    m_activity = logic.getActivity(m_project.getIndexact());    
    if (m_activity!=null)
        m_actTextField.setText(m_activity.getName());
    
    m_org = logic.getOrganization(m_project.getIndexdept());
    if (m_org!=null)
        m_departTextField.setText(m_org.getName());

    m_PONoTextField.setText(m_project.getPONo());
    m_IPCNoTextField.setText(m_project.getIPCNo());

    if(m_project.getPODate() != null)
      m_PODateTextField.setText(dateformat.format(m_project.getPODate()));
    if(m_project.getIPCDate() != null)
      m_IPCDateTextField.setText(dateformat.format(m_project.getIPCDate()));

    if(m_timesheet != null) {
      m_entryDatePicker.setDate(m_timesheet.getEntryDate());
      m_preparedPicker.setEmployee(m_timesheet.getPreparedBy());
      m_preparedDatePicker.setDate(m_timesheet.getPreparedDate());
      m_checkedPicker.setEmployee(m_timesheet.getCheckedBy());
      m_checkedDatePicker.setDate(m_timesheet.getCheckedDate());
      m_approvedTextField.setText(m_timesheet.getApproved());
      m_approvedDatePicker.setDate(m_timesheet.getApprovedDate());
      m_descriptTextArea.setText(m_timesheet.getDescription());

      setTimeSheetDetail(m_timesheet);
    }
  }
  
  public void getNullTextField(){
    m_codeTextField.setText(" ");    
    m_custTextField.setText(" ");
    m_wrkdescTextArea.setText(" ");    
    m_unitTextField.setText(" ");
    m_actTextField.setText(" ");
    m_departTextField.setText(" ");
    m_PONoTextField.setText(" ");
    m_IPCNoTextField.setText(" ");    
    m_PODateTextField.setText(" ");    
    m_IPCDateTextField.setText(" ");    
    m_entryDatePicker.setDate(null);
    m_preparedPicker.setEmployee(null);
    m_preparedDatePicker.setDate(null);    
    m_checkedDatePicker.setDate(null);
    m_approvedTextField.setText(" ");
    m_approvedDatePicker.setDate(null);
    m_descriptTextArea.setText(" ");      
  }

  void reloadPersonal() {
    m_personalComboBox.removeAllItems();
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      ProjectPersonal[] personal = logic.getProjectPersonal(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex());

      for(int i = 0; i < personal.length; i ++)
        m_personalComboBox.addItem(personal[i].getEmployee());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
  
  void reloadArea(){
	  m_personalArea.removeAllItems();
	    try {
	    HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);	      
	    AllowenceMultiplier[] allowence = logic.getAllAllowenceMultiplier(m_sessionid,
	          "modul allowence");

	      for(int i = 0; i < allowence.length; i ++)
	    	  m_personalArea.addItem(allowence[i].getAreaCode());
	    }
	    catch(Exception ex) {
	      JOptionPane.showMessageDialog(this, ex.getMessage(),
	                                    "Warning", JOptionPane.WARNING_MESSAGE);
	    }
  }

  void setTimeSheetDetail(TimeSheet sheet) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      TimeSheetDetail[] detail = logic.getTimeSheetDetail(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, sheet.getIndex());
      m_table.setTimeSheetDetail(detail);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public int getResponse() {
    return m_iResponse;
  }

  public TimeSheet getTimeSheet() {
    return m_timesheet;
  }

  private TimeSheet getDefineTimeSheet() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    Employee preparedBy = null, checkedBy = null;
    java.util.Date preparedDate = null, entryDate = null, checkedDate = null, approvedDate = null;
    String approved = "", descript = "";

    entryDate = m_entryDatePicker.getDate();
    if(entryDate == null)
      list.add("Entry date");

    preparedBy = m_preparedPicker.getEmployee();
    if(preparedBy == null)
      list.add("Name of prepared By");

    preparedDate = m_preparedDatePicker.getDate();
    if(preparedDate == null)
      list.add("Date of prepared by");

    checkedBy = m_checkedPicker.getEmployee();
    if(checkedBy == null)
      list.add("Name of checked By");

    checkedDate = m_checkedDatePicker.getDate();
    if(checkedDate == null)
      list.add("Date of checked by");

    approved = m_approvedTextField.getText();
    if(approved.equals(""))
      list.add("Name of approved by");

    approvedDate = m_approvedDatePicker.getDate();
    if(approvedDate == null)
      list.add("Date of approved by");

    descript = m_descriptTextArea.getText();

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += "- " + exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new TimeSheet(entryDate, preparedBy, preparedDate, checkedBy, checkedDate,
                         approved, approvedDate, descript);
  }

  void save() {
    TimeSheet timesheet = null;
    try {
      timesheet = getDefineTimeSheet();
      timesheet.setTimeSheetDetail(m_table.getTimeSheetDetail());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      if(m_timesheet == null)
        m_timesheet = logic.createTimeSheet(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_project.getIndex(), timesheet);
      else
        m_timesheet = logic.updateTimeSheet(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_timesheet.getIndex(), timesheet);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  void cancel() {
    if(!Misc.getConfirmation())
      return;
    dispose();
  }

  void add() {
    m_new = true;
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.addRow(new Object[]{});
    m_editedIndex = model.getRowCount() - 1;
    model.setValueAt(String.valueOf(m_editedIndex + 1), m_editedIndex, 0);
    model.setValueAt(new Integer(0), m_editedIndex, 6);
    model.setValueAt(new Integer(0), m_editedIndex, 7);
    model.setValueAt(new Integer(0), m_editedIndex, 8);
    m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
  }

  void edit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    m_detail = (TimeSheetDetail)m_table.getValueAt(m_editedIndex, 1);
    m_addBt.setEnabled(false);
    //m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
  }

  void delete() {
    int row = m_table.getSelectedRow();
    if(row != -1) {
      if(!Misc.getConfirmation())
        return;

      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
    }
    m_table.updateNumber(0);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_save2Bt) {
      save();
    }
    else if(e.getSource() == m_cancel2Bt) {
      cancel();
    }
    else if(e.getSource() == m_addBt) {
      add();
    }
   // else if(e.getSource() == m_editBt) {
      //edit();
    //}
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
  }

  public int getDay(java.util.Date enddate, java.util.Date startdate) {
    java.util.Calendar calendar = new java.util.GregorianCalendar();
    /*
    int start = 0, end = 0, days = 0;
    calendar.setTime(startdate);
    start = calendar.get(java.util.Calendar.DAY_OF_YEAR);

    calendar.setTime(enddate);
    end = calendar.get(java.util.Calendar.DAY_OF_YEAR);
    days = start - end;
    */
    /**
     * this is the correct one... maybe....
     */
    long lStart = 0, lEnd = 0, lDays = 0;
    
    calendar.setTime(startdate);
    lStart = startdate.getTime() + calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
    
    calendar.setTime(enddate);
    lEnd = enddate.getTime() + calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
    
    lDays = (long)((lEnd - lStart) / (24*60*60*1000));
    lDays++;
    
    return (int)lDays;
  }

  /**
   *
   */
  class TimeSheetTable extends JTable {    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeSheetTable() {
      TimeSheetTableModel model = new TimeSheetTableModel();
      model.addColumn("No");
      model.addColumn("Personel");
      model.addColumn("Area Code");
      model.addColumn("Qualification");
      model.addColumn("Start Date");
      model.addColumn("Finish Date");
      model.addColumn("Days");
      model.addColumn("Overtime Reguler");
      model.addColumn("Overtime Holiday");
      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
      
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 1) {
        reloadPersonal();
        return new DefaultCellEditor(m_personalComboBox);
      }
      else if(col == 2){
    	reloadArea();
        return new DefaultCellEditor(m_personalArea);      
      }
      else if(col == 3)
        return new QualificationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Qualification", m_conn, m_sessionid);
      else if(col == 4 || col == 5)
        return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
      else if(col == 6 || col == 7 || col == 8)
        return new IntegerCellEditor();
      return new BaseTableCellEditor();
    }

    public TimeSheetDetail getDefineTimeSheetDetail(int row) throws Exception {
      stopCellEditing();
      java.util.ArrayList list = new java.util.ArrayList();
      int col = 1;
      Employee personel = null;
      Object obj = getValueAt(row, col++);
      if(obj instanceof TimeSheetDetail)
        personel = ((TimeSheetDetail)obj).getPersonel();
      else
        personel = (Employee)obj;  
      
      if (personel==null)
    	  list.add("Personal");

      String areacode = (String)getValueAt(row, col++);
      if(areacode == null || areacode.equals(""))
        list.add("Area Code");

      obj = getValueAt(row, col++);
      Qualification qua = null;
      if(obj instanceof Qualification)
        qua = (Qualification)obj;
      else
        list.add("Qualification");

      java.util.Date start = null, finish = null;
      String strstart = (String)getValueAt(row, col++);
      if(strstart == null || strstart.equals(""))
        list.add("Start date");

      String strfinish = (String)getValueAt(row, col++);
      if(strfinish == null || strfinish.equals(""))
        list.add("Finish date");

      String strexc = "Please insert for row " + (row + 1) + ": \n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int j = 0; j < exception.length; j ++)
          strexc += "- " + exception[j] + "\n";
        throw new Exception(strexc);
      }

      try {
        start = m_dateformat.parse(strstart);
        finish = m_dateformat.parse(strfinish);
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      int days = ((Integer)getValueAt(row, col++)).intValue();
      String m_days = String.valueOf(days);
      if (m_days==null || m_days.equals(""))
    	  list.add("days");      
      
      int reguler = ((Integer)getValueAt(row, col++)).intValue();      
      	
      int holiday = ((Integer)getValueAt(row, col++)).intValue();;
      	
      
      /*try {
        reguler = Integer.parseInt(m_regulerTextField.getText().trim());
        holiday = Integer.parseInt(m_holidayTextField.getText().trim());
      }
      catch(Exception ex) {
        throw new Exception("Reguler and Holiday have to fill in numeric");
      }*/

      return new TimeSheetDetail(personel, areacode, qua, start, finish, days, reguler, holiday);
    }

    public TimeSheetDetail[] getTimeSheetDetail() throws Exception {
      java.util.Vector vresult = new java.util.Vector();
      for(int i = 0; i < getRowCount(); i ++) {
        vresult.addElement(getDefineTimeSheetDetail(i));
      }

      TimeSheetDetail[] result = new TimeSheetDetail[vresult.size()];
      vresult.copyInto(result);
      return result;
    }

    public void setTimeSheetDetail(TimeSheetDetail[] detail) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < detail.length; i ++) {
        model.addRow(new Object[]{
        String.valueOf(i + 1),
        detail[i],
        detail[i].getAreaCode(),
        detail[i].getQualification(),
        m_dateformat.format(detail[i].getStartDate()),
        m_dateformat.format(detail[i].getFinishDate()),
        new Integer(detail[i].getDays()),new Integer(detail[i].getReguler()), 
        new Integer(detail[i].getHoliday())
      });
      }
    }

    public void updateTimeSheetDetail(TimeSheetDetail detail, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);

      model.addRow(new Object[]{
        String.valueOf(row + 1),
        detail,
        detail.getQualification(),
        m_dateformat.format(detail.getStartDate()),
        m_dateformat.format(detail.getFinishDate()),
        new Integer(detail.getDays())
      });
    }
    
    void updateNumber(int col) {
        for(int i = 0; i < getRowCount(); i ++)
            setValueAt(String.valueOf(i + 1), i, col); 
    }
    
  }

  /**
   *
   */
  class TimeSheetTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      return true;
    }

    public void setValueAt(Object obj, int row, int col) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      //java.util.Calendar calendar = new java.util.GregorianCalendar();

      if(col == 4) {
        java.util.Date startdate = null;
        java.util.Date enddate = null;
        //int istart = 0, iend = 0;
        int idays = 0;

        try {
          String strstart = (String)obj;
          if(strstart != null && !strstart.equals(""))
            startdate = sdf.parse(strstart);
          String strend = (String)getValueAt(row, col + 1);
          if(strend != null && !strend.equals(""))
            enddate = sdf.parse(strend);

          if(enddate == null || startdate == null)
            idays = 0;
          else {
            idays = getDay(enddate, startdate);
          }
        }
        catch(Exception ex) {
          JOptionPane.showMessageDialog(TimeSheetDlg.this, ex.getMessage());
        }

        super.setValueAt(obj, row, col);
        super.setValueAt(new Integer(idays), row, col + 2);
      }
      else if(col == 5) {
        java.util.Date startdate = null;
        java.util.Date enddate = null;
        //int istart = 0, iend = 0;
        int idays = 0;

        try {
          String strend = (String)obj;
          if(strend != null && !strend.equals(""))
            enddate = sdf.parse(strend);
          String strstart = (String)getValueAt(row, col - 1);
          if(strstart != null && !strstart.equals(""))
            startdate = sdf.parse(strstart);

          if(startdate == null || enddate == null)
            idays = 0;
          else {
            idays = getDay(enddate, startdate);
          }
        }
        catch(Exception ex) {
          JOptionPane.showMessageDialog(TimeSheetDlg.this, ex.getMessage());
        }

        super.setValueAt(obj, row, col);
        super.setValueAt(new Integer(idays), row, col + 1);
      }
      else
        super.setValueAt(obj, row, col);
    }    
  }
}