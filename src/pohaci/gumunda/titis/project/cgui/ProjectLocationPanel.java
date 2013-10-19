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
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.*;

public class ProjectLocationPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  LocationTable m_table;
  JButton m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt;

  boolean m_editable = false;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  ProjectData m_project = null;
  ProjectLocation m_location;

  public ProjectLocationPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    m_table = new LocationTable();

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

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(new JScrollPane(m_table), BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
  }

  public void setProjectLocation(ProjectData project) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      ProjectLocation[] location = logic.getProjectLocation(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex());

      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.setRowCount(0);

      for(int i = 0; i < location.length; i ++) {
        model.addRow(new Object[]{location[i], location[i].getDescription()});
      }

    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void setEditable(boolean editable) {
    m_editable = editable;
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void onAdd() {
    m_new = true;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.addRow(new Object[]{});
    m_editedIndex = model.getRowCount() - 1;
    m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    m_location = (ProjectLocation)m_table.getValueAt(m_editedIndex, 0);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);

  }

  void onDelete() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    m_editedIndex = m_table.getSelectedRow();
    ProjectLocation location = (ProjectLocation)model.getValueAt(m_editedIndex, 0);
    
    if(!Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectLocation(m_sessionid, IDBConstants.MODUL_MASTER_DATA, location.getIndex());
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
      return;
    }
    model.removeRow(m_editedIndex);
  }

  void onSave() {
    m_table.stopCellEditing();
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
     Object objValue = model.getValueAt(m_editedIndex, 0);
    System.err.println("location1 :" + m_table.getValueAt(m_editedIndex, 0));
    String location = "";
   try{
        if(objValue instanceof CompanyGroup)
          location = ((CompanyGroup)objValue).getName();
        else
          location = (String)objValue;
    }catch (Exception ex){        
    }
    
   if(location == null || location.equals("")){
      JOptionPane.showMessageDialog(this, "Location have to fill.");
      return;
    }

    String note = (String)model.getValueAt(m_editedIndex, 1);

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      if(m_new){
        ProjectLocation newlocation = logic.createProjectLocation(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
            m_project.getIndex(), new ProjectLocation(location, note));
        model.setValueAt(newlocation, m_editedIndex, 0);
      }
      else {
        ProjectLocation newlocation = logic.updateProjectLocation(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
            m_location.getIndex(), new ProjectLocation(location, note));
        model.setValueAt(newlocation, m_editedIndex, 0);
        model.setValueAt(newlocation.getDescription(), m_editedIndex, 1);
      }
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
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

  void onCancel() {
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
      m_table.updateProjectLocation(m_editedIndex, m_location);
      m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
    else if(e.getSource() == m_saveBt) {
      onSave();
    }
    else if(e.getSource() == m_cancelBt) {
      onCancel();
    }
  }

  /**
   *
   */
  class LocationTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocationTable() {
      DetailTableModel model = new DetailTableModel();
      model.addColumn("Location");
      model.addColumn("Description");
      setModel(model);
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

    public void updateProjectLocation(int row, ProjectLocation location) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{location, location.getDescription()});
    }
  }

  /**
   *
   */
  class DetailTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if((m_new || m_edit)&& row == m_editedIndex)
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
          else {
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