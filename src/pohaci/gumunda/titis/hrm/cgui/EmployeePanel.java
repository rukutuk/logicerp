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
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeePanel extends JPanel implements ActionListener, ListSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
EmployeeListPanel m_list;
  EmployeeTable m_table;
  EmploymentPanel m_employmentpanel;
  EmployeeEducationPanel m_edupanel;
  EmployeeCertificationPanel m_certificatepanel;
  EmployeeFamilyPanel m_familypanel;
  EmployeeAccountPanel m_accountpanel;
  EmployeeRetirementPanel m_retirementpanel;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;

  Employee m_employee = null;
private JButton m_copyBt;

  public EmployeePanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JTabbedPane tabPane = new JTabbedPane();
    JPanel listPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel button1Panel = new JPanel();
    JPanel button2Panel = new JPanel();

    m_list = new EmployeeListPanel(m_conn, m_sessionid);
    
    m_list.getList().addListSelectionListener(this);
    m_table = new EmployeeTable(m_conn, m_sessionid);
    m_employmentpanel = new EmploymentPanel(m_conn, m_sessionid);
    m_edupanel = new EmployeeEducationPanel(m_conn, m_sessionid);
    m_certificatepanel = new EmployeeCertificationPanel(m_conn, m_sessionid);
    m_familypanel = new EmployeeFamilyPanel(m_conn, m_sessionid);
    m_accountpanel = new EmployeeAccountPanel(m_conn, m_sessionid);
    m_retirementpanel = new EmployeeRetirementPanel(m_conn, m_sessionid);

    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    button1Panel.add(m_addBt);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    button1Panel.add(m_editBt);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    button1Panel.add(m_deleteBt);
    m_copyBt = new JButton("Copy");
    m_copyBt.addActionListener(this);
    button1Panel.add(m_copyBt);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    button2Panel.add(m_saveBt);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    button2Panel.add(m_cancelBt);

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(false);

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 8, 10));
    tablePanel.add(new JScrollPane(m_table));

    listPanel.setLayout(new BorderLayout());
    listPanel.add(m_list, BorderLayout.CENTER);
    listPanel.add(button1Panel, BorderLayout.SOUTH);

    tabPane.addTab("Profile", tablePanel);
    tabPane.addTab("Employement", m_employmentpanel);
    tabPane.addTab("Education", m_edupanel);
    tabPane.addTab("Family", m_familypanel);
    tabPane.addTab("Certification", m_certificatepanel);
    tabPane.addTab("Account", m_accountpanel);
    tabPane.addTab("Retirement", m_retirementpanel);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(tabPane, BorderLayout.CENTER);
    centerPanel.add(button2Panel, BorderLayout.SOUTH);

    splitPane.setLeftComponent(listPanel);
    splitPane.setRightComponent(centerPanel);
    splitPane.setDividerLocation(200);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }

  void onAdd() {
    m_new = true;
    m_table.m_editable = true;
    m_table.clearTable();
    m_employmentpanel.setEnabled(true);
    m_employmentpanel.clear();
    m_edupanel.setEnabled(true);
    m_edupanel.clear();
    m_certificatepanel.setEnabled(true);
    m_certificatepanel.clear();
    m_familypanel.setEnabled(true);
    m_familypanel.clear();
    m_accountpanel.setEnabled(true);
    m_accountpanel.clear();
    m_retirementpanel.setEnabled(true);
    m_retirementpanel.clear();

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
    m_copyBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_table.m_editable = true;
    m_employmentpanel.setEnabled(true);
    m_edupanel.setEnabled(true);
    m_certificatepanel.setEnabled(true);
    m_familypanel.setEnabled(true);
    m_accountpanel.setEnabled(true);
    m_retirementpanel.setEnabled(true);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
    m_copyBt.setEnabled(false);
  }

  void onSave() {
	  Employee employee = null;
	  try {
		  employee = m_table.getEmployee();
		  employee.setEmployment(m_employmentpanel.getEmployment());
		  employee.setEducation(m_edupanel.getEducation());
		  employee.setCertification(m_certificatepanel.getCertification());
		  employee.setFamily(m_familypanel.getFamily());
		  employee.setAccount(m_accountpanel.getAccount());
		  employee.setRetirement(m_retirementpanel.getRetirement());
	  }
	  catch(Exception ex) {
		  JOptionPane.showMessageDialog(this, ex.getMessage(),
				  "Information", JOptionPane.INFORMATION_MESSAGE);
		  return;
	  }
	  
	  if(m_new) {
		  try {
			  m_new = false;
			  createEmployee(employee);
		  }
		  catch(Exception ex) {
			  ex.printStackTrace();
			  m_new = true;
			  JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					  JOptionPane.WARNING_MESSAGE);
		  }
	  }
	  else {
		  try {
			  
			  updateEmployee(new Employee(m_employee.getIndex(), employee));
		  }
		  catch(Exception ex) {
			  JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					  JOptionPane.WARNING_MESSAGE);
		  }
	  }
  }

  void onCancel() {
    m_table.stopCellEditing();
    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    if(m_employee != null)
      setSelectedObject(m_employee);
    else
      objectViewNoSelection();
  }

  void onDelete() {
    deleteEmployee();
  }

  void setSelectedObject(Employee employee) {	  
    m_employee = employee;
    setEmployeeQualification(m_employee.getIndex());
    m_table.setEmployee(m_employee);
    setEmployeeEmployment(m_employee.getIndex());
    setEmployeeEducation(m_employee.getIndex());
    setEmployeeCertification(m_employee.getIndex());
    setEmployeeFamily(m_employee.getIndex());
    setEmployeeAccount(m_employee.getIndex());
    setEmployeeRetirement(m_employee.getIndex());

    m_new = false;
    m_edit = false;
    m_table.m_editable = false;
    m_employmentpanel.setEnabled(false);
    m_edupanel.setEnabled(false);
    m_certificatepanel.setEnabled(false);
    m_familypanel.setEnabled(false);
    m_accountpanel.setEnabled(false);
    m_retirementpanel.setEnabled(false);

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
    m_cancelBt.setEnabled(false);
    m_copyBt.setEnabled(true);
  }

  void objectViewNoSelection(){
    m_new = false;
    m_edit = false;
    m_table.m_editable = false;
    m_table.clearTable();
    m_employmentpanel.setEnabled(false);
    m_employmentpanel.clear();
    m_edupanel.setEnabled(false);
    m_edupanel.clear();
    m_certificatepanel.setEnabled(false);
    m_certificatepanel.clear();
    m_familypanel.setEnabled(false);
    m_familypanel.clear();
    m_accountpanel.setEnabled(false);
    m_accountpanel.clear();
    m_retirementpanel.setEnabled(false);
    m_retirementpanel.clear();

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_copyBt.setEnabled(true);
  }

  void createEmployee(Employee employee) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
    Employee object = logic.createEmployee(m_sessionid, IDBConstants.MODUL_MASTER_DATA, employee);

    model.addElement(object);
    m_list.getList().setSelectedValue(object, true);
  }

  void updateEmployee(Employee employee) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
    Employee object = logic.updateEmployee(m_sessionid, IDBConstants.MODUL_MASTER_DATA, employee.getIndex(), employee);

    model.setElementAt(object, m_editedIndex);
    setSelectedObject(object);
  }

  void deleteEmployee() {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    Employee employee = (Employee)m_list.getList().getSelectedValue();

    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.deleteEmployee(m_sessionid, IDBConstants.MODUL_MASTER_DATA, employee.getIndex());
      model.removeElementAt(m_editedIndex);
      m_employee = null;
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void setEmployeeQualification(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      Qualification[] qua = logic.getEmployeeQualification(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);

      m_employee.setQualification(qua);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setEmployeeEmployment(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      Employment[] employment = logic.getEmployeeEmployment(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);

      setMaxEmployment(employment);
      m_employmentpanel.setEmployment(employment);
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setMaxEmployment(Employment[] employ) {
	Calendar calendar = Calendar.getInstance(Locale.getDefault());
	Date currentday = calendar.getTime();  
	  
    java.util.Date befdate = null, afterdate = null;
    int index = -1;

    for(int i = 0; i < employ.length; i ++) {
      if(befdate == null) {
        befdate = employ[i].getTMT();
        index = i;
      }

      if(i + 1 < employ.length) {
        afterdate = employ[i + 1].getTMT();
        if(afterdate.after(currentday)) {
        	break;
        }
        if(befdate.compareTo(afterdate) == -1) {
          befdate = afterdate;
          index = i + 1;
        }
      }
    }

    if(index != -1) {
      m_table.setValueAt(employ[index].getJobTitle(), 8, 1);
      m_table.setValueAt(employ[index].getDepartment(), 9, 1);
      m_table.setValueAt(employ[index].getUnit(), 10, 1);
    }
  }

  void setEmployeeEducation(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      EmployeeEducation[] education = logic.getEmployeeEducation(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);

      setMaxEducation(education);
      m_edupanel.setEducation(education);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setMaxEducation(EmployeeEducation[] education) {
    java.util.Date befdate = null, afterdate = null;
    int index = -1;

    for(int i = 0; i < education.length; i ++) {
      if(befdate == null) {
        befdate = education[i].getTo();
        index = i;
      }

      if(i + 1 < education.length) {
        afterdate = education[i + 1].getTo();
        if(befdate.compareTo(afterdate) == -1) {
          befdate = afterdate;
          index = i + 1;
        }
      }
    }

    if(index != -1)
      m_table.setValueAt(education[index].getGrade(), 11, 1);
  }

  void setEmployeeCertification(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      Certification[] certificate = logic.getEmployeeCertification(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);

      m_certificatepanel.setCertification(certificate);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setEmployeeFamily(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      EmployeeFamily[] family = logic.getEmployeeFamily(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);

      m_familypanel.setFamily(family);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setEmployeeAccount(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      EmployeeAccount[] account = logic.getEmployeeAccount(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);

      m_accountpanel.setAccount(account);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setEmployeeRetirement(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      Retirement retirement = logic.getEmployeeRetirement(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);

      m_retirementpanel.setRetirement(retirement);
      setRetirementStatus(retirement);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
  
  void setRetirementStatus(Retirement retirement){
     
	  if(retirement!=null){
		  Calendar calendar = Calendar.getInstance(Locale.getDefault());
		  Date current = calendar.getTime();
		  //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		  
		  Date dateTMT = null;
		  if (retirement.getTMT()!=null){
			  dateTMT = retirement.getTMT();
			  
			  //  System.out.println(sdf.format(current) + " and " + sdf.format(dateTMT));
			  if(current.before(dateTMT))
				  m_table.setValueAt("Active", 27, 1);
			  else
				  m_table.setValueAt("Retired", 27, 1);
		  }else{
			  m_table.setValueAt("Active", 27, 1);
		  }
	  }else{
		  m_table.setValueAt("Active", 27, 1);
	  }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_saveBt) {
      onSave();
    }
    else if(e.getSource() == m_cancelBt) {
      onCancel();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
    else if(e.getSource() == m_copyBt) {
    	onCopy();
    }
  }

  private void onCopy() {
	  CopyEmployeeDialog dlg = new CopyEmployeeDialog(GumundaMainFrame.getMainFrame(), m_conn, m_sessionid);
	  dlg.setModal(true);
	  dlg.setVisible(true);
  }

public void valueChanged(ListSelectionEvent e) {
	  if(!e.getValueIsAdjusting()){
		  int iRowIndex = m_list.getList().getMinSelectionIndex();
		  
		  if(m_new || m_edit){
			  if(iRowIndex != m_editedIndex){
				  int count = m_list.getList().getModel().getSize() + 1;
				  if(m_editedIndex == -1)
					  m_list.getList().getSelectionModel().setSelectionInterval(m_editedIndex + count,
							  m_editedIndex + count);
				  else
					  m_list.getList().getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
			  }
		  }
		  else{
			  if(iRowIndex != -1){
				  // Dipakai untuk mengambil data employe perselected        	
				  Employee employee = (Employee)m_list.getList().getSelectedValue();
				  System.err.println("index employee :" + employee.getIndex());
				  setSelectedObject(employee);
				  m_editedIndex = iRowIndex;
			  }
			  else
				  objectViewNoSelection();
		  }
	  }
  }
}