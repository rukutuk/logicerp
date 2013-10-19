

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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.SearchEmployeeDetailDlg;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectPersonalPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  PersonalTable m_table;
  ProjectOrganizationAttachmentPanel m_filePanel;
  JButton m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt;

  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  ProjectData m_project = null;
  ProjectPersonal m_personal = null;
  protected EmployeeQualification[] m_emQualification = null;

  public ProjectPersonalPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    m_table = new PersonalTable();
    m_filePanel = new ProjectOrganizationAttachmentPanel(m_conn, m_sessionid); 
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

    JPanel northPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_deleteBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    northPanel.setLayout(new BorderLayout());
    northPanel.add(buttonPanel, BorderLayout.NORTH);
    northPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);

    southPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    southPanel.add(m_filePanel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.ipadx = 100;
    southPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
    southPanel.add(new JPanel(), gridBagConstraints);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(northPanel, BorderLayout.CENTER);
    add(southPanel, BorderLayout.SOUTH);
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
    m_filePanel.setProjectData(project);
  }

  public void setProjectPersonal(ProjectData project) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      ProjectPersonal[] personal = logic.getProjectPersonal(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex());
      
      m_table.setProjectPersonal(personal);

      FileAttachment file = logic.getProjectOrganizationAttachment(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex());
      if(file != null)
        m_filePanel.setFileAttactment(file);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void setEditable(boolean editable) {
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_filePanel.setEditable(editable);
  }

  void add() {
    SearchEmployeeDetailDlg dlg = new SearchEmployeeDetailDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      Employee[] employee = dlg.getEmployee();            
      if(employee.length > 0) {        
        m_table.addEmployee(employee[0]);
        m_new = true;
        m_editedIndex = m_table.getRowCount()-1;
        m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        m_addBt.setEnabled(false);
        m_editBt.setEnabled(false);
        m_saveBt.setEnabled(true);
        m_cancelBt.setEnabled(true);
        m_deleteBt.setEnabled(false);
      }
    }
  }

  void delete() {
    m_editedIndex = m_table.getSelectedRow();
    ProjectPersonal person = (ProjectPersonal)m_table.getValueAt(m_editedIndex, 1);
    System.err.println("person :" + person.getIndex());

    if(!Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectPersonal(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
                                  person.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.removeRow(m_editedIndex);
    m_table.updateNumber(0);
  }

  void edit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    m_personal = (ProjectPersonal)m_table.getValueAt(m_editedIndex, 1);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void save() {
    ProjectPersonal person = null;
    try {
      person = m_table.getProjectPersonal();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      if(m_new) {
        m_table.updateProjectPersonal(logic.createProjectPersonal(m_sessionid,
            IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex(), person), m_editedIndex);
      }
      else {
        m_table.updateProjectPersonal(logic.updateProjectPersonal(m_sessionid,
            IDBConstants.MODUL_PROJECT_MANAGEMENT, m_personal.getIndex(), person), m_editedIndex);
      }
    }
    catch(Exception ex) {
        ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    m_new = false;
    m_edit = false;
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
  }

  void cancel() {
    m_table.stopCellEditing();
    if(!Misc.getConfirmation())
      return;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    if(m_new){
      m_new = false;
      model.removeRow(m_editedIndex);
    }
    else {
      m_edit = false;
      m_table.updateProjectPersonal(m_personal, m_editedIndex);
      m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
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

  /**
   *
   */
  class PersonalTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonalTable() {
      PersonalTableModel model = new PersonalTableModel();
      model.addColumn("No");
      model.addColumn("Name");
      model.addColumn("Position");
      model.addColumn("Qualification");
      model.addColumn("Task");
      model.addColumn("Work Description");
      model.addColumn("Employee Status");

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public String getQualification(long index){
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);      
      String Qualification = " ";
      try{          
          m_emQualification = logic.getQualification(m_sessionid, "get qualification" , index);          
          for (int i=0; i < m_emQualification.length ; i++){
                if (i>=m_emQualification.length)
                    Qualification += m_emQualification[i].getCode().toString();                
                else
                    Qualification += m_emQualification[i].getCode().toString() + ",";
          }
      } catch(Exception ex){            
      }     
     
      return Qualification;
    }
    
    public void addEmployee(Employee employee) {        
      DefaultTableModel model = (DefaultTableModel)getModel();             
      model.addRow(new Object[]{              
      String.valueOf(getRowCount() + 1), employee,
      "", this.getQualification(employee.getIndex()), "", "", ""
      });
    }

    public ProjectPersonal getProjectPersonal() throws Exception {
      stopCellEditing();
      int col = 1;
      Object obj = getValueAt(m_editedIndex, col++);
      Employee employee = null;

      if(obj instanceof ProjectPersonal)
        employee = ((ProjectPersonal)obj).getEmployee();
      else
        employee = (Employee)obj;

      String position = (String)getValueAt(m_editedIndex, col++);
      col++;
      String taks = (String)getValueAt(m_editedIndex, col++);
      String workdescription = (String)getValueAt(m_editedIndex, col++);
      String status = (String)getValueAt(m_editedIndex, col++);

      return new ProjectPersonal(employee, position, taks,
                                 workdescription, status);
    }

    public void setProjectPersonal(ProjectPersonal[] person) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      for (int i=0;i<person.length;i++){        
        model.addRow(new Object[]{
        String.valueOf(i + 1), person[i], person[i].getPosition(),
        this.getQualification(person[i].getEmployee().getIndex()), person[i].getTask(), person[i].getWorkDescription(),
        person[i].getStatus()
      });
      }
    }

    public void updateProjectPersonal(ProjectPersonal person, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();      
      model.removeRow(row);            
      model.insertRow(row,new Object[]{       
        String.valueOf(row + 1), person, person.getPosition(),
        this.getQualification(person.getEmployee().getIndex()), person.getTask(), person.getWorkDescription(),
        person.getStatus()
      });
      this.getSelectionModel().setSelectionInterval(row, row);
      updateNumber(0);
    }
    
    void updateNumber(int col) {
        for(int i = 0; i < getRowCount(); i ++)
            setValueAt(String.valueOf(i + 1), i, col); 
    }
  }

  /**
   *
   */
  class PersonalTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0 || col == 1 || col == 3)
        return false;
      if((m_new || m_edit) && row == m_editedIndex)
        return true;
      return false;
    }

    public void valueChanged(ListSelectionEvent e) {
      if(!e.getValueIsAdjusting()){
        int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();

        if(m_new || m_edit){
          if(iRowIndex != m_editedIndex)
            m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        }
        else{
          if(iRowIndex != -1){
            m_addBt.setEnabled(true);
            m_editBt.setEnabled(true);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(true);
          }
          else{
            m_addBt.setEnabled(true);
            m_editBt.setEnabled(false);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(false);
          }
        }
      }
    }
  }
}