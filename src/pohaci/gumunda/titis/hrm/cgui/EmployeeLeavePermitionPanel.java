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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Document;

import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.FilePanel;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeLeavePermitionPanel extends JPanel implements ActionListener,
  PropertyChangeListener, DocumentListener {
  /**
	 *
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JButton m_searchBt, m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt;
  JRadioButton m_leaveRadioButton, m_permitionRadioButton;
  JTextField m_annualLeaveDeductionTextField;
  DatePicker m_proposeDatePicker, m_fromDatePicker, m_toDatePicker,
  m_checkedDatePicker, m_approvedDatePicker;
  EmployeePicker m_replacedPicker, m_checkedPicker, m_approvedPicker;
  JComboBox m_reasonComboBox;
  JTextArea m_descriptionTextArea;

  JTextField m_daysTextField, m_addressTextField, m_phoneTextField;
  JCheckBox m_referenceCheckBox;
  FilePanel m_filepanel;

  AnualLeaveTable m_table;

  boolean m_new = false, m_edit = false;
  Employee m_employee = null;
  EmployeeLeavePermition m_reason = null;
  short m_type = -1;

  AbsenceAttribute absenceType;
  AbsenceAttribute[] absenceTypes;
  boolean isDeduction;
  boolean isReady = false;

  public EmployeeLeavePermitionPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    reloadLeaveType();
    isReady = true;
    setEditable(false);
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // i change RIGHT to Left
    JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel formPanel = new JPanel();
    JPanel leavePanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel gridPanel = new JPanel();

    m_searchBt = new JButton("Search");
    m_searchBt.addActionListener(this);
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    buttonPanel.add(m_searchBt);
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    m_leaveRadioButton = new JRadioButton("Leave");
    m_leaveRadioButton.addActionListener(this);
    m_leaveRadioButton.setSelected(true);
    m_permitionRadioButton = new JRadioButton("Permission");
    m_permitionRadioButton.addActionListener(this);

//  i add this
	m_leaveRadioButton.setEnabled(false);
	m_permitionRadioButton.setEnabled(false);

    ButtonGroup bg = new ButtonGroup();
    bg.add(m_leaveRadioButton);
    bg.add(m_permitionRadioButton);
    radioPanel.add(m_leaveRadioButton);
    radioPanel.add(m_permitionRadioButton);

    m_reasonComboBox = new JComboBox();
    m_reasonComboBox.addActionListener(this);
    m_annualLeaveDeductionTextField = new JTextField();
    m_annualLeaveDeductionTextField.setEnabled(false);

    m_proposeDatePicker = new DatePicker();
    m_fromDatePicker = new DatePicker();
    m_fromDatePicker.addPropertyChangeListener("date", this);
    m_toDatePicker = new DatePicker();
    m_toDatePicker.addPropertyChangeListener("date", this);

    m_replacedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_checkedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_checkedDatePicker = new DatePicker();
    m_approvedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_approvedDatePicker = new DatePicker();
    m_descriptionTextArea = new JTextArea();
    JScrollPane sp = new JScrollPane(m_descriptionTextArea);
    sp.setPreferredSize(new Dimension(200, 70));

    m_daysTextField = new JTextField();
    Document doc = m_daysTextField.getDocument();
    doc.addDocumentListener(this);

    m_addressTextField = new JTextField();
    m_phoneTextField = new JTextField();
    m_referenceCheckBox = new JCheckBox();
    m_referenceCheckBox.addActionListener(this);
    m_filepanel = new FilePanel("Reference Document Attachment");

    /*
     * we do not need these anymore
    m_table = new AnualLeaveTable();
    JScrollPane sp2 = new JScrollPane();
    sp2.getViewport().add(m_table);
    sp2.setPreferredSize(new Dimension(300, 81));
    */

    JLabel reasonLabel = new JLabel("Reason");
    JLabel deductionLabel = new JLabel("Annual Leave Deduction");
    JLabel proposeLabel = new JLabel("Propose Date");
    JLabel fromLabel = new JLabel("From");
    JLabel toLabel = new JLabel("To");
    JLabel replacedLabel = new JLabel("Replaced by");
    JLabel checkedLabel = new JLabel("Checked by");
    JLabel checkeddateLabel = new JLabel("Checked/ Date");
    JLabel approvedLabel = new JLabel("Approved by");
    JLabel approveddateLabel = new JLabel("Approved Date");
    JLabel descriptionLabel = new JLabel("Description");
    JLabel daysLabel = new JLabel("Days #");
    JLabel addressLabel = new JLabel("Address");
    JLabel phoneLabel = new JLabel("Phone");
    JLabel referenceLabel = new JLabel("Reference");

    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    formPanel.setLayout(new GridBagLayout());

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(radioPanel, gridBagConstraints);

    //1
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(reasonLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_reasonComboBox, gridBagConstraints);

    // 2
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(deductionLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_annualLeaveDeductionTextField, gridBagConstraints);

    //3
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(proposeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_proposeDatePicker, gridBagConstraints);

    // 4
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(fromLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_fromDatePicker, gridBagConstraints);

    //5
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(toLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_toDatePicker, gridBagConstraints);

    //6
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(daysLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_daysTextField, gridBagConstraints);

    //7
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(addressLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_addressTextField, gridBagConstraints);

    //8
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(phoneLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_phoneTextField, gridBagConstraints);

    //9
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(replacedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_replacedPicker, gridBagConstraints);

    //10
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(checkedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_checkedPicker, gridBagConstraints);

    //11
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 11;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(checkeddateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_checkedDatePicker, gridBagConstraints);

    //12
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 12;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(approvedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_approvedPicker, gridBagConstraints);

    //13
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(approveddateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_approvedDatePicker, gridBagConstraints);

    //14
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 14;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    formPanel.add(descriptionLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(sp, gridBagConstraints);

    //15
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 15;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    formPanel.add(referenceLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_referenceCheckBox, gridBagConstraints);

    //16
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 16;
    formPanel.add(m_filepanel, gridBagConstraints);

    leavePanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    // do not need these lines
    /*
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new Insets(1, 0, 2, 0);
    leavePanel.add(new JLabel("Annual Leave"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(3, 0, 0, 0);
    gridBagConstraints.gridwidth = gridBagConstraints.REMAINDER;
    // and this...
    //leavePanel.add(new JSeparator(JSeparator.HORIZONTAL), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(1, 0, 5, 0);
    // and this...
    //leavePanel.add(sp2, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    leavePanel.add(new JPanel(), gridBagConstraints);
    */

    gridPanel.setLayout(new GridLayout(1, 2, 10, 1));
    gridPanel.add(formPanel);
    //gridPanel.add(leavePanel);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(buttonPanel, BorderLayout.NORTH);
    centerPanel.add(gridPanel, BorderLayout.CENTER);

    setLayout(new FlowLayout(FlowLayout.LEFT));
    add(centerPanel);
  }

  void setEditable(boolean editable) {
    m_searchBt.setEnabled(editable);
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    setEditableForm(false);
  }

  void setEditableForm(boolean editable) {
    m_proposeDatePicker.setEditable(editable);
    m_fromDatePicker.setEditable(editable);
    m_toDatePicker.setEditable(editable);
    m_daysTextField.setEnabled(editable);
    m_reasonComboBox.setEnabled(editable);
    m_addressTextField.setEnabled(editable);
    m_phoneTextField.setEnabled(editable);
    m_replacedPicker.setEditable(editable);
    m_checkedPicker.setEditable(editable);
    m_checkedDatePicker.setEditable(editable);
    m_approvedPicker.setEditable(editable);
    m_approvedDatePicker.setEditable(editable);
    m_descriptionTextArea.setEnabled(editable);
    m_referenceCheckBox.setEnabled(editable);
    m_filepanel.setEditable(false);
  }

  void setDeduction(){
      absenceType = (AbsenceAttribute) m_reasonComboBox.getSelectedItem();
      if(absenceType!=null){
          if(absenceType.getDeduction()){
              m_annualLeaveDeductionTextField.setText("Yes");
              isDeduction = true;
          } else {
              m_annualLeaveDeductionTextField.setText("No");
              isDeduction = false;
          }
      }
  }

  void reloadLeaveType() {

    m_reasonComboBox.removeAllItems();
    m_type = EmployeeLeavePermition.typeFromStringToID(EmployeeLeavePermition.STR_TYPE1);

    // i frustated :(
    //m_daysTextField.setText("");

    m_searchBt.setEnabled(true);
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      LeaveType[] type = logic.getAllLeaveType(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      absenceTypes = type;


      for(int i = 0; i < type.length; i ++)
        m_reasonComboBox.addItem(new LeaveType(type[i], LeaveType.CODE_DESCRIPTION));

     // setDeduction(); // i add this
      clear();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }


  }

  void reloadPermitionType() {

    m_reasonComboBox.removeAllItems();
    m_type = EmployeeLeavePermition.typeFromStringToID(EmployeeLeavePermition.STR_TYPE2);

    // i frustated :(
    //m_daysTextField.setText("");

    m_searchBt.setEnabled(true);
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      PermitionType[] type = logic.getAllPermitionType(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      absenceTypes = type;


      for(int i = 0; i < type.length; i ++)
        m_reasonComboBox.addItem(new PermitionType(type[i], PermitionType.CODE_DESCRIPTION));

      clear();

      //setDeduction(); // i add this
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }

  }

  public void setEmployee(Employee employee) {
	// i add this
	m_leaveRadioButton.setEnabled(true);
	m_permitionRadioButton.setEnabled(true);

    m_employee = employee;

    /*
     * we do not need this part
    try {
      annualLeaveRight(employee.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
    */
  }

  /*
   * phew, we do not this crazy method...
  void annualLeaveRight(long index) throws Exception {
    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    Date currentday = calendar.getTime();
    int year = calendar.get(Calendar.YEAR);
    int n = 0;

    HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
    Date minday = logic.getMinTMTEmployement(m_sessionid, IDBConstants.MODUL_MASTER_DATA, index);

    if(minday != null) {
      calendar.setTime(minday);
      Date tempday = minday;
      while(tempday.before(currentday) && calendar.get(Calendar.YEAR) < year) {
        calendar.add(Calendar.YEAR, 1);
        tempday = calendar.getTime();
        n++;
      }
    }

    int total = 0, used = 0, next = 0, remain = 0;
    AnnualLeaveRight right = logic.getAnnualLeaveRight(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

    if(right != null) {
      if(n > right.getMaxYear())
        total = right.getMaxRight();
      else if(n >= right.getMinYear() && n <= right.getMaxYear())
        total = right.getMinRight();
    }

    if(minday!=null){
    	if(n>=right.getMinYear()){
	    	calendar.setTime(minday);

	    	//annual right is start whenever minYear reached
	    	calendar.add(Calendar.YEAR, right.getMinYear());
	    	Date startRight = calendar.getTime();
	    	calendar.add(Calendar.MONTH, 18); // 1.5 year = 18 month
	    	Date endRight = calendar.getTime();

	    	//get last start and end right
	    	while(startRight.before(currentday)&&endRight.before(currentday)){
	    		calendar.setTime(endRight);
	    		startRight = calendar.getTime();
	    		calendar.add(Calendar.MONTH, 18);
	    		endRight = calendar.getTime();
	    	}

	        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	        System.out.println(dateformat.format(startRight) + " - " +
	        		dateformat.format(endRight));
    	}
    }



    int leave = logic.getEmployeeLeavePermitionUsed(m_sessionid, IDBConstants.MODUL_MASTER_DATA, index, (short)year);
    used = leave;

    remain = total - used;

    m_table.setValueAt(new Integer(total), 0, 1);
    m_table.setValueAt(new Integer(used), 1, 1);
    m_table.setValueAt(new Integer(next), 2, 1);
    m_table.setValueAt(new Integer(remain), 3, 1);
  }
  */

  /*
   * also these methods
  void assignLeaveUsed(int willUsed) throws Exception {
      // i add this
      Integer temp;
      temp = (Integer)m_table.getValueAt(0, 1);
      int total = temp.intValue();
      temp = (Integer)m_table.getValueAt(1, 1);
      int used = temp.intValue();
      int next;

      if(isDeduction){
          next = willUsed;
      } else {
          next = 0;
      }

      int remain = total - used - next;
      // set values
      m_table.setValueAt(new Integer(next), 2, 1);
      m_table.setValueAt(new Integer(remain), 3, 1);
      // cek
      if(remain<0){
        String message = "Leave proposed exceeds the annual leave right";
        throw new Exception(message);
      }
  }

    void assignLeaveUsed(int willUsed, boolean search) throws Exception {
      // i add this
      Integer temp;
      temp = (Integer)m_table.getValueAt(0, 1);
      int total = temp.intValue();
      temp = (Integer)m_table.getValueAt(1, 1);
      int used = temp.intValue();
      int next;

      if(isDeduction){
          next = willUsed;
      } else {
          next = 0;
      }

      if(search){
          used -= next;
          m_table.setValueAt(new Integer(used), 1, 1);
      }

      int remain = total - used - next;
      // set values
      m_table.setValueAt(new Integer(next), 2, 1);
      m_table.setValueAt(new Integer(remain), 3, 1);
      // cek
      if(remain<0){
        String message = "Leave proposed exceeds the annual leave right";
        throw new Exception(message);
      }
  }
*/

  void search() {
    SearchLeavePermitionDlg dlg = null;
    if(m_leaveRadioButton.isSelected())
      dlg = new SearchLeavePermitionDlg(
          pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn, m_sessionid, m_employee,
          m_type);
    else
      dlg = new SearchLeavePermitionDlg(
          pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn, m_sessionid, m_employee,
          m_type);

    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
    	/*
    	 * and this part of course
            try {
                annualLeaveRight(m_employee.getIndex());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            */
          setEmployeeLeavePermition(dlg.getEmployeeLeavePermition());
          m_searchBt.setEnabled(true);
          m_addBt.setEnabled(true);
          m_editBt.setEnabled(true);
          m_deleteBt.setEnabled(true);
          m_saveBt.setEnabled(false);
          m_cancelBt.setEnabled(false);
    }
  }

  void add() {
    m_new = true;
    clear();
    m_leaveRadioButton.setEnabled(false);
    m_permitionRadioButton.setEnabled(false);
    m_searchBt.setEnabled(false);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    setEditableForm(true);
  }

  void edit() {
    m_edit = true;
    m_searchBt.setEnabled(false);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_leaveRadioButton.setEnabled(false);
    m_permitionRadioButton.setEnabled(false);
    // i add this
    setEditableForm(true);
  }

  void delete() {
    if(!Misc.getConfirmation())
      return;

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_leaveRadioButton.isSelected())
        logic.deleteEmployeeLeave(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
                                  m_reason.getIndex());
      else
        logic.deleteEmployeePermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
                                      m_reason.getIndex());

      // how bout this???
      //annualLeaveRight(m_employee.getIndex());
      m_reason = null;
      setEditable(true);
      clear();

    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
  }

  void save() {
    EmployeeLeavePermition reason = null;

    try {
      reason = this.getEmployeeLeavePermition();
    }
    catch(Exception ex) {
    	//throw ex;
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    //checking annual leave - no need
    /*
    int remain = ((Integer)m_table.getValueAt(3, 1)).intValue();
    if(remain<0){
    	throw new Exception("Leave proposed exceeds the annual leave right");
    	//JOptionPane.showConfirmDialog(this, "Leave proposed exceeds the annual leave right",
    	//		"Warning", JOptionPane.WARNING_MESSAGE);
    	//return;
    }
    */

    System.out.println("approved" + reason.getApprovedDate());

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_new) {
        if(m_leaveRadioButton.isSelected())
          reason = logic.createEmployeeLeave(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
              m_employee.getIndex(), reason);
        else
          reason = logic.createEmployeePermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
              m_employee.getIndex(), reason);
      }
      else {
        if(m_leaveRadioButton.isSelected()) {
          reason = logic.updateEmployeeLeave(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
              m_reason.getIndex(), reason);
        }
        else
          reason = logic.updateEmployeePermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
              m_reason.getIndex(), reason);
      }
      // and this?
      //annualLeaveRight(m_employee.getIndex());
    }
    catch(Exception ex) {
      //throw ex;
    	JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }


    m_new = false;
    //if(m_edit) { // i comment it
      m_leaveRadioButton.setEnabled(true);
      m_permitionRadioButton.setEnabled(true);
    //}
    m_edit = false;
    setEmployeeLeavePermition(reason);
    m_searchBt.setEnabled(true);
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_deleteBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    setEditableForm(false);
  }

  void cancel() {
    if(!Misc.getConfirmation())
      return;

    if(m_new) {
      m_new = false;
      clear();
      m_editBt.setEnabled(false);
      m_deleteBt.setEnabled(false);
    }
    else {
      m_edit = false;
      setEmployeeLeavePermition(m_reason);
      m_editBt.setEnabled(true);
      m_deleteBt.setEnabled(true);

    }

    // i moved here
    m_leaveRadioButton.setEnabled(true);
    m_permitionRadioButton.setEnabled(true);

    m_searchBt.setEnabled(true);
    m_addBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    setEditableForm(false);
  }

  void clear() {
    m_proposeDatePicker.setDate(null);

    m_fromDatePicker.setDate(null);
    m_toDatePicker.setDate(null);
    m_daysTextField.setText("");
    m_addressTextField.setText("");
    m_phoneTextField.setText("");
    m_replacedPicker.setEmployee(null);
    m_checkedPicker.setEmployee(null);
    m_checkedDatePicker.setDate(null);
    m_approvedPicker.setEmployee(null);
    m_approvedDatePicker.setDate(null);
    m_descriptionTextArea.setText("");
    m_referenceCheckBox.setSelected(false);

    if(m_reasonComboBox.getItemCount() > 0)
      m_reasonComboBox.setSelectedIndex(0);

    m_filepanel.clear();
  }

  public void setEmployeeLeavePermition(EmployeeLeavePermition reason) {
    m_reason = reason;

    m_proposeDatePicker.setDate(reason.getPropose());
    m_fromDatePicker.setDate(reason.getFrom());
    m_toDatePicker.setDate(reason.getTo());

    for(int i = 0; i < m_reasonComboBox.getItemCount(); i ++) {
      if(m_leaveRadioButton.isSelected()) {
        LeaveType treason = (LeaveType)m_reasonComboBox.getItemAt(i);
        LeaveType type = (LeaveType)reason.getLeaveReason(m_sessionid,m_conn); // i add this
        //if(reason.getIndex() == treason.getIndex()) {
        if(type.getIndex()==treason.getIndex()) {// i change with it
            m_reasonComboBox.setSelectedIndex(i);
            break;
        }
      }
      else {
        PermitionType treason = (PermitionType)m_reasonComboBox.getItemAt(i);
        PermitionType type = (PermitionType)reason.getPermissionReason(m_sessionid, m_conn); // i add this
        //if(reason.getIndex() == treason.getIndex()) {
        if(type.getIndex()==treason.getIndex()){ // i change with it
            m_reasonComboBox.setSelectedIndex(i);
            break;
        }
      }
    }

    if(m_new || m_edit){
    	// or this?
    	/*
	    try {
	        assignLeaveUsed(reason.getDays(),true);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    */
    }
    m_daysTextField.setText(String.valueOf(reason.getDays()));

    m_addressTextField.setText(reason.getAddress());
    m_phoneTextField.setText(reason.getPhone());
    m_replacedPicker.setEmployee(reason.getReplacedEmployee(m_sessionid, m_conn));
    m_checkedPicker.setEmployee(reason.getCheckedEmployee(m_sessionid, m_conn));
    m_checkedDatePicker.setDate(reason.getCheckedDate());
    m_approvedPicker.setEmployee(reason.getApprovedEmployee(m_sessionid, m_conn));
    m_approvedDatePicker.setDate(reason.getApprovedDate());
    m_descriptionTextArea.setText(reason.getDescription());
    m_referenceCheckBox.setSelected(reason.isReference());
    m_filepanel.setFileByte(reason.getReference());
    m_filepanel.setFileName(reason.getFile());
  }

  EmployeeLeavePermition getEmployeeLeavePermition() throws Exception {
    Date propose = null, from = null, to = null;
    int days = 0;
    short reasontype = -1;
    Object reason = null;
    long reasonindex = -1;
    Employee replaced = null, checked = null, approved = null;
    Date checkeddate = null, approveddate = null;
    String address, phone, description = "", file = "";
    boolean isreference = false;
    byte[] reference = null;

    long replacedIndex = 0;
    long checkedIndex = 0;
    long approvedIndex = 0;

    java.util.ArrayList list = new java.util.ArrayList();

    propose = m_proposeDatePicker.getDate();
    if(propose == null)
      list.add("Propose Date");
    from = m_fromDatePicker.getDate();
    if(from == null)
      list.add("From");
    to = m_toDatePicker.getDate();
    if(to == null)
      list.add("To");

    if(m_daysTextField.getText().length()>0){
        days = Integer.parseInt(m_daysTextField.getText().trim());
    }


    if(m_leaveRadioButton.isSelected()){
    	reasontype = 0;
    	reason = (LeaveType)m_reasonComboBox.getSelectedItem();
    	reasonindex = ((LeaveType)reason).getIndex();
    }else{
    	reasontype = 1;
    	reason = (PermitionType)m_reasonComboBox.getSelectedItem();
    	reasonindex = ((PermitionType)reason).getIndex();
    }
     if(reason == null)
      list.add("Reason");

    address = m_addressTextField.getText();
    phone = m_phoneTextField.getText();

    replaced = m_replacedPicker.getEmployee();
    if(replaced!=null)
    	replacedIndex = replaced.getIndex();
    checked = m_checkedPicker.getEmployee();
    if(checked!=null)
    	checkedIndex = checked.getIndex();
//    if(checked == null){
//        list.add("Checked By");
//    }
    checkeddate = m_checkedDatePicker.getDate();
//    if(checkeddate == null){
//        list.add("Checked Date");
//    }
    approved = m_approvedPicker.getEmployee();
    if(approved!=null)
    	approvedIndex = approved.getIndex();
//    if(approved == null){
//        list.add("Approved By");
//    }
    approveddate = m_approvedDatePicker.getDate();
//    if(approveddate == null){
//        list.add("Approved Date");
//    }
    description = m_descriptionTextArea.getText();

    if(m_referenceCheckBox.isSelected())
      isreference = true;

    file = m_filepanel.getFileName();
    reference = m_filepanel.getFileByte();

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += "- " + exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new EmployeeLeavePermition(reasontype, propose, from, to, days,
    		reasonindex,
    		address, phone, replacedIndex, checkedIndex,
            checkeddate, approvedIndex, approveddate, description,
            isreference, file, reference);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_leaveRadioButton) {
    	/*
    	 * we do not need these
        try {
            //clear();
            annualLeaveRight(m_employee.getIndex());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */
        reloadLeaveType();
        //m_reasonComboBox.setSelectedIndex(-1);
    }
    else if(e.getSource() == m_permitionRadioButton) {
    	/*
    	 * we do not need these
        try {
            //clear();
            annualLeaveRight(m_employee.getIndex());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */
        reloadPermitionType();
        //m_reasonComboBox.setSelectedIndex(-1);
    }
    else if(e.getSource() == m_searchBt) {
      search();
    }
    else if(e.getSource() == m_addBt) {
    	/*
    	 * we do not need these
        try {
            annualLeaveRight(m_employee.getIndex());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */
        add();
    }
    else if(e.getSource() == m_editBt) {
    	/*
    	 *	we do not need these
    	try {
	        assignLeaveUsed(m_reason.getDays(),true);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    */
	    edit();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
    else if(e.getSource() == m_saveBt) {
    	//try {
			save();
		//} catch (Exception e1) {
		//	e1.printStackTrace();
		//}
    	/*
    	 * we do not need these
    	try {
			annualLeaveRight(m_employee.getIndex());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
		*/
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
      /*
       * and these
      try {
          annualLeaveRight(m_employee.getIndex());
      } catch (Exception ex) {
          ex.printStackTrace();
      }
      */

    }
    else if(e.getSource() == m_referenceCheckBox) {
      m_filepanel.setEditable(m_referenceCheckBox.isSelected());
      if(!m_referenceCheckBox.isSelected())
        m_filepanel.clear();
    }
    else if(e.getSource() == m_reasonComboBox) {
        setDeduction();
        absenceType = (AbsenceAttribute) m_reasonComboBox.getSelectedItem();
        if(absenceType!=null){
            if(absenceType.getType()=="Permission"){
                //PermitionType type = (PermitionType) m_reasonComboBox.getSelectedItem();
                m_daysTextField.setText(String.valueOf(absenceType.getDays()));
            }
        }
        if(isReady){
            Document doc = m_daysTextField.getDocument();
            setLeaveUsed(doc);
        }
    }
  }

    public void propertyChange(PropertyChangeEvent evt) {
        if((evt.getSource()==m_fromDatePicker) || (evt.getSource()==m_toDatePicker)){
            Date m_dateFrom;
            Date m_dateTo;
            try {
                m_dateFrom = m_fromDatePicker.getDate();
                m_dateTo = m_toDatePicker.getDate();

                if ((m_dateFrom!=null) && (m_dateTo!=null)) {
                    if (m_dateFrom.before(m_dateTo) || m_dateFrom.equals(m_dateTo)) {

//                        int start = 0, end = 0, days = 0;
//                        Calendar calendar = new GregorianCalendar();
//                        calendar.setTime(m_dateFrom);
//                        start = calendar.get(Calendar.DAY_OF_YEAR);
//                        calendar.setTime(m_dateTo);
//                        end = calendar.get(Calendar.DAY_OF_YEAR);
//                        days = end - start + 1;

                    	long start = 0, end = 0;
                    	int days = 0;
                    	start = m_dateFrom.getTime();
                    	end = m_dateTo.getTime();

                    	days = (int) ((end - start)/(1000*60*60*24))+1;

                        m_daysTextField.setText(new Integer(days).toString());

                        absenceType = (AbsenceAttribute) m_reasonComboBox.getSelectedItem();
                        if(absenceType!=null){
                            if(absenceType.getType()=="Permission"){
                                if(days>absenceType.getDays()){
                                    JOptionPane.showMessageDialog(this,
                                            "Days proposed exceed the right (" + absenceType.getDays() +
                                            " days)", "Warning", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    void setLeaveUsed(Document doc) {
        /*int len = doc.getLength();
        //boolean isNumeric = true;
        //int willUsed = 0;
        if(len>0){
            try {
                //willUsed = new Integer(doc.getText(0, len)).intValue();
            } catch (NumberFormatException ex) {
                //isNumeric = false;
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                            JOptionPane.WARNING_MESSAGE);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

         * i do not need these
        if(isNumeric) {
        	if(m_new || m_edit){
	            try {
	                assignLeaveUsed(willUsed);
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
	                        JOptionPane.WARNING_MESSAGE);
	            }
        	}
        }
        */
    }

    public void insertUpdate(DocumentEvent e) {
        Document doc = e.getDocument();
        setLeaveUsed(doc);
    }

    public void removeUpdate(DocumentEvent e) {
        Document doc = e.getDocument();
        setLeaveUsed(doc);
    }

    public void changedUpdate(DocumentEvent e) {
        Document doc = e.getDocument();
        setLeaveUsed(doc);
    }

  /**
   *
   */
  class AnualLeaveTable extends JTable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AnualLeaveTable() {
      AnualLeaveTableModel model = new AnualLeaveTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
      model.addRow(new Object[]{"Total Annual Leaves Right", ""});
      model.addRow(new Object[]{"Leaves Used", ""});
      model.addRow(new Object[]{"Next Leaves Taken", ""});
      model.addRow(new Object[]{"Remaining", ""});
      setModel(model);
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
      return new BaseTableCellRenderer();
    }
  }

  /**
   *
   */
  class AnualLeaveTableModel extends DefaultTableModel {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}