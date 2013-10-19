package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeOfficeHourPermitionPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid =  -1;

  JButton m_searchBt, m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt;
  DatePicker m_proposeDatePicker, m_permissionDatePicker,
  m_checkedDatePicker, m_approvedDatePicker;
  JTextField m_fromDatePicker, m_toDatePicker;
  EmployeePicker m_checkedPicker, m_approvedPicker;
  JComboBox m_reasonComboBox;
  JTextArea m_descriptionTextArea;

  boolean m_new = false, m_edit = false;
  Employee m_employee = null;
  EmployeeOfficeHourPermition m_reason = null;

  SimpleDateFormat m_timeformat = new SimpleDateFormat("HH:mm");
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  SimpleDateFormat m_longdateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
  
  Calendar calendar;
  Date current;
    
  public EmployeeOfficeHourPermitionPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    reloadOfficeHourPermition();
    setEditable(false);
  }

  void constructComponent() {
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

    calendar = Calendar.getInstance(Locale.getDefault());
    current = calendar.getTime();
    
    m_proposeDatePicker = new DatePicker();
    m_permissionDatePicker = new DatePicker();
    m_permissionDatePicker.setDate(current);
    m_fromDatePicker = new JTextField();
    m_toDatePicker = new JTextField();
    m_reasonComboBox = new JComboBox();
    m_checkedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_checkedDatePicker = new DatePicker();
    m_approvedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_approvedDatePicker = new DatePicker();
    m_descriptionTextArea = new JTextArea();
    JScrollPane sp = new JScrollPane(m_descriptionTextArea);
    sp.setPreferredSize(new Dimension(200, 70));

    JLabel proposeLabel = new JLabel("Propose Date");
    JLabel permissiondateLabel = new JLabel("Permission Date");
    JLabel fromLabel = new JLabel("From");
    JLabel toLabel = new JLabel("To");
    JLabel reasonLabel = new JLabel("Reason");
    JLabel checkedLabel = new JLabel("Checked by");
    JLabel checkeddateLabel = new JLabel("Checked Date");
    JLabel approvedLabel = new JLabel("Approved by");
    JLabel approveddateLabel = new JLabel("Approved Date");
    JLabel descriptionLabel = new JLabel("Description");

    JPanel buttonPanel = new JPanel();
    JPanel formPanel = new JPanel();
    //JPanel buttonformPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    buttonPanel.add(m_searchBt);
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 10, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(proposeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_proposeDatePicker, gridBagConstraints);
    
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    formPanel.add(permissiondateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_permissionDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    formPanel.add(fromLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_fromDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    formPanel.add(toLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_toDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    formPanel.add(reasonLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_reasonComboBox, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    formPanel.add(checkedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_checkedPicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    formPanel.add(checkeddateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_checkedDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    formPanel.add(approvedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_approvedPicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    formPanel.add(approveddateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_approvedDatePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    formPanel.add(descriptionLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(sp, gridBagConstraints);

    centerPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    centerPanel.add(buttonPanel, gridBagConstraints);

    gridBagConstraints.gridy = 1;
    centerPanel.add(formPanel, gridBagConstraints);

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
    m_permissionDatePicker.setEditable(editable);
    m_fromDatePicker.setEnabled(editable);
    m_toDatePicker.setEnabled(editable);
    m_reasonComboBox.setEnabled(editable);
    m_checkedPicker.setEditable(editable);
    m_checkedDatePicker.setEditable(editable);
    m_approvedPicker.setEditable(editable);
    m_approvedDatePicker.setEditable(editable);
    m_descriptionTextArea.setEnabled(editable);
  }

  public void setEmployee(Employee employee) {
    m_employee = employee;
  }

  void reloadOfficeHourPermition() {
    m_reasonComboBox.removeAllItems();

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      OfficeHourPermition[] reason = logic.getAllOfficeHourPermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      for(int i = 0; i < reason.length; i ++)
        m_reasonComboBox.addItem(new OfficeHourPermition(reason[i], OfficeHourPermition.CODE_DESCRIPTION));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void search() {
    SearchOfficeHourPermitionDlg dlg = new SearchOfficeHourPermitionDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn, m_sessionid, m_employee);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      setEmployeeOfficeHourPermition(dlg.getEmployeeOfficeHourPermition());
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
    setEditableForm(true);
  }

  void delete() {
    if(!Misc.getConfirmation())
      return;

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.deleteEmployeeOfficeHourPermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
          m_reason.getIndex());
      m_reason = null;
      setEditable(true);
      clear();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void save() {
    EmployeeOfficeHourPermition reason = null;

    try {
      reason = getEmployeeOfficeHourPermition();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_new)
        reason = logic.createEmployeeOfficeHourPermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
            m_employee.getIndex(), reason);
      else
         reason = logic.updateEmployeeOfficeHourPermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
            m_reason.getIndex(), reason);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    setEmployeeOfficeHourPermition(reason);
    m_new = false;
    m_edit = false;
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
      setEmployeeOfficeHourPermition(m_reason);
      m_editBt.setEnabled(true);
      m_deleteBt.setEnabled(true);
    }

    m_searchBt.setEnabled(true);
    m_addBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    setEditableForm(false);
  }

  void clear() {
    m_proposeDatePicker.setDate(null);
    
    m_permissionDatePicker.setDate(current);
    m_fromDatePicker.setText("");
    m_toDatePicker.setText("");
    m_reasonComboBox.setSelectedIndex(0);
    m_checkedPicker.setEmployee(null);
    m_checkedDatePicker.setDate(null);
    m_approvedPicker.setEmployee(null);
    m_approvedDatePicker.setDate(null);
    m_descriptionTextArea.setText("");
  }

  public void setEmployeeOfficeHourPermition(EmployeeOfficeHourPermition reason) {
    m_reason = reason;
    m_proposeDatePicker.setDate(reason.getPropose());
    m_permissionDatePicker.setDate(reason.getPermissionDate());
    m_fromDatePicker.setText(m_timeformat.format(reason.getFrom()));
    m_toDatePicker.setText(m_timeformat.format(reason.getTo()));

    for(int i = 0; i < m_reasonComboBox.getItemCount(); i ++) {
      OfficeHourPermition treason = (OfficeHourPermition)m_reasonComboBox.getItemAt(i);
      if(reason.getReason().getIndex() == treason.getIndex()) {
        m_reasonComboBox.setSelectedIndex(i);
        break;
      }
    }

    m_checkedPicker.setEmployee(reason.getChecked());
    m_checkedDatePicker.setDate(reason.getCheckedDate());
    m_approvedPicker.setEmployee(reason.getApproved());
    m_approvedDatePicker.setDate(reason.getApprovedDate());
    m_descriptionTextArea.setText(reason.getDesciption());
  }

  EmployeeOfficeHourPermition getEmployeeOfficeHourPermition() throws Exception {
    Date propose = null, permission = null;
    Time from = null, to = null;
    OfficeHourPermition reason = null;
    Employee checked = null, approved = null;
    Date checkeddate = null, approveddate = null;
    String description = "";

    java.util.ArrayList list = new java.util.ArrayList();

    propose = m_proposeDatePicker.getDate();
    if(propose == null)
      list.add("Propose Date");
    
    permission = m_permissionDatePicker.getDate();
    if(permission == null)
    	list.add("Permission Date");
   
    try {
    	from = new java.sql.Time(m_timeformat.parse(m_fromDatePicker.getText().trim()).getTime());
	    if(from == null)
	      list.add("From");
    } catch (ParseException e){
    	throw new Exception("Error while parsing time. Format: dd:mm");    	
    }


    to = new java.sql.Time(m_timeformat.parse(m_toDatePicker.getText().trim()).getTime());
    if(to == null)
      list.add("To");

    reason = (OfficeHourPermition)m_reasonComboBox.getSelectedItem();
    if(reason == null)
      list.add("Reason");

    checked = m_checkedPicker.getEmployee();
    checkeddate = m_checkedDatePicker.getDate();
    approved = m_approvedPicker.getEmployee();
    approveddate = m_approvedDatePicker.getDate();
    description = m_descriptionTextArea.getText();

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += "- " + exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new EmployeeOfficeHourPermition(propose, permission, 
    		from, to, reason, checked,
    		checkeddate, approved, approveddate, description);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_searchBt) {
      search();
    }
    else if(e.getSource() == m_addBt) {
      add();
    }
    else if(e.getSource() == m_editBt) {
      edit();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
    else if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
    }
  }
}