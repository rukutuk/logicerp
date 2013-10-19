package pohaci.gumunda.titis.hrm.cgui;

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
import java.util.Vector;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.application.*;

public class EmployeeFamilyPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  FamilyTable m_table;
  FamilyDetailTable m_detailtable;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  boolean m_editable = false;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  EmployeeFamily m_family = null;

  public EmployeeFamilyPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    setEnabled(false);
  }

  void constructComponent() {
    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JScrollPane scrollpane = new JScrollPane();
    JPanel buttonPanel = new JPanel();

    m_table = new FamilyTable();
    scrollpane.getViewport().add(m_table);
    scrollpane.setPreferredSize(new Dimension(350, 145));

    m_detailtable = new FamilyDetailTable();

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    buttonPanel.add(m_addBt);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    buttonPanel.add(m_editBt);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    buttonPanel.add(m_deleteBt);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    buttonPanel.add(m_saveBt);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    buttonPanel.add(m_cancelBt);

    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(scrollpane, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridheight = 2;
    formPanel.add(new JPanel(), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(0, 3, 0, -3);
    formPanel.add(buttonPanel, gridBagConstraints);

    northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    northPanel.add(formPanel);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(northPanel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(m_detailtable), BorderLayout.CENTER);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(15, 10, 8, 10));
    add(centerPanel, BorderLayout.CENTER);
  }

  public void setEnabled(boolean editable) {
    m_editable = editable;
    if(!editable) {
      m_addBt.setEnabled(editable);
      m_editBt.setEnabled(editable);
      m_saveBt.setEnabled(editable);
      m_cancelBt.setEnabled(editable);
      m_deleteBt.setEnabled(editable);
    }
    else {
      m_addBt.setEnabled(editable);
      if(m_editedIndex != -1) {
        m_editBt.setEnabled(editable);
        m_deleteBt.setEnabled(editable);
      }
      else {
        m_editBt.setEnabled(!editable);
        m_deleteBt.setEnabled(!editable);
      }
      m_saveBt.setEnabled(!editable);
      m_cancelBt.setEnabled(!editable);

    }
  }

  void onAdd() {
    m_new = true;
    m_table.clearTable();
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_detailtable.getSelectedRow();
    m_family = (EmployeeFamily)m_detailtable.getValueAt(m_editedIndex, 0);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    EmployeeFamily family = null;
    try {
      family = m_table.getFamily();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    if(m_new)
      m_detailtable.addFamily(family);
    else
      m_detailtable.updateFamily(family, m_editedIndex);

    m_new = false;
    m_edit = false;
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
  }

  void onDelete() {
    if(!Misc.getConfirmation())
      return;
    m_detailtable.deleteFamily(m_editedIndex);
  }

  void onCancel() {
    m_table.stopCellEditing();

    if(!Misc.getConfirmation())
      return;

    if(m_new) {
      m_new = false;
      m_table.clearTable();
      m_addBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);

      if(m_editedIndex != -1) {
        m_editBt.setEnabled(true);
        m_deleteBt.setEnabled(true);
        m_table.setFamily(m_family);
      }
      else {
        m_editBt.setEnabled(false);
        m_deleteBt.setEnabled(false);
      }
    }
    else if(m_edit) {
      m_edit = false;
      m_detailtable.updateFamily(m_family, m_editedIndex);

      m_addBt.setEnabled(true);
      m_editBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
      m_deleteBt.setEnabled(true);
    }
  }

  public EmployeeFamily[] getFamily() {
    return m_detailtable.geFamily();
  }

  public void setFamily(EmployeeFamily[] family) {
    m_new = false; m_edit = false;
    m_table.clearTable();
    m_detailtable.setFamily(family);
  }

  public void clear() {
    m_table.clearTable();
    m_detailtable.clear();
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
  }


  /**
   *
   */
  class FamilyTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FamilyTable() {
      FamilyTableModel model = new FamilyTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
      model.addRow(new Object[]{"Relation", ""});
      model.addRow(new Object[]{"Name", ""});
      model.addRow(new Object[]{"Birthplace", ""});
      model.addRow(new Object[]{"Birthdate", ""});
      model.addRow(new Object[]{"Education", ""});
      model.addRow(new Object[]{"Job Title", ""});
      model.addRow(new Object[]{"Company", ""});
      model.addRow(new Object[]{"Remark", ""});
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(row == 0 && col == 1)
        return new FamilyRelationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Family Relation", m_conn, m_sessionid);
      else if(row == 3 && col == 1)
        return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
      else if(row == 4 && col == 1)
        return new EducationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Family Relation", m_conn, m_sessionid);
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void clearTable() {
      int count = getRowCount();
      for(int i = 0; i < count; i ++)
        setValueAt("", i, 1);
    }

    public EmployeeFamily getFamily() throws Exception {
      stopCellEditing();

      java.util.ArrayList list = new java.util.ArrayList();
      SimpleEmployeeAttribute relation = null;
      String name = "", birthplace = "";
      java.util.Date birthdate = null;
      Education education = null;
      String jobtitle = "", company = "", remark = "";

      int row = 0;
      Object obj = getValueAt(row++, 1);
      if(obj instanceof SimpleEmployeeAttribute)
        relation = (SimpleEmployeeAttribute)obj;
      else
        list.add("Relation");

      name = (String)getValueAt(row++, 1);
      if(name == null || name.equals(""))
        list.add("Name");

      birthplace = (String)getValueAt(row++, 1);
      String strdate = (String)getValueAt(row++, 1);
      try {
        if(!strdate.equals(""))
          birthdate = m_dateformat.parse(strdate);
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      obj = getValueAt(row++, 1);
      if(obj instanceof Education)
        education = (Education)obj;
      else
        list.add("Education");

      jobtitle = (String)getValueAt(row++, 1);
      company = (String)getValueAt(row++, 1);
      remark = (String)getValueAt(row++, 1);

      String strexc = "Please insert description of :\n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int i = 0; i < exception.length; i ++)
          strexc += " - " + exception[i] + "\n";
        throw new Exception(strexc);
      }

      return new EmployeeFamily(relation, name, birthplace, birthdate, education,
                        jobtitle, company, remark);
    }

    public void setFamily(EmployeeFamily family) {
      int row = 0;

      setValueAt(family.getRelation(), row++, 1);
      setValueAt(family.getName(), row++, 1);
      setValueAt(family.getBirthPlace(), row++, 1);
      if(family.getBirthDate() != null)
        setValueAt(m_dateformat.format(family.getBirthDate()), row++, 1);
      else
        setValueAt("", row++, 1);
      setValueAt(family.getEducation(), row++, 1);
      setValueAt(family.getJobTitle(), row++, 1);
      setValueAt(family.getCompany(), row++, 1);
      setValueAt(family.getRemark(), row++, 1);
    }
  }

  /**
   *
   */
  class FamilyTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      if(m_new || m_edit)
        return true;
      return false;
    }
  }

  /**
   *
   */
  class FamilyDetailTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FamilyDetailTable() {
      FamilyDetailTableModel model = new FamilyDetailTableModel();
      model.addColumn("Relation");
      model.addColumn("Name");
      model.addColumn("Birthplace");
      model.addColumn("Birthdate");
      model.addColumn("Education");
      model.addColumn("Job");
      model.addColumn("Company");
      model.addColumn("Remark");

      setModel(model);
      getSelectionModel().addListSelectionListener(model);
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }

    void addFamily(EmployeeFamily family) {
      String birthdate = "";
      if(family.getBirthDate() != null)
        birthdate = m_dateformat.format(family.getBirthDate());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        family, family.getName(), family.getBirthPlace(), birthdate,
        family.getEducation(), family.getJobTitle(), family.getCompany(), family.getRemark()
      });

      m_editedIndex = getRowCount() -1;
      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void updateFamily(EmployeeFamily family, int row) {
      String birthdate = "";
      if(family.getBirthDate() != null)
        birthdate = m_dateformat.format(family.getBirthDate());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        family, family.getName(), family.getBirthPlace(), birthdate,
        family.getEducation(), family.getJobTitle(), family.getCompany(), family.getRemark()
      });

      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void deleteFamily(int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
    }

    public EmployeeFamily[] geFamily() {
      Vector vresult = new Vector();
      for(int i = 0; i < getRowCount(); i ++)
        vresult.addElement((EmployeeFamily)getValueAt(i, 0));

      EmployeeFamily[] result = new EmployeeFamily[vresult.size()];
      vresult.copyInto(result);
      return result;
    }

    public void setFamily(EmployeeFamily[] family) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      m_editedIndex = -1;
      for(int i = 0; i < family.length; i ++) {
        String birthdate = "";
        if(family[i].getBirthDate() != null)
          birthdate = m_dateformat.format(family[i].getBirthDate());

        model.addRow(new Object[]{
        family[i], family[i].getName(), family[i].getBirthPlace(), birthdate,
        family[i].getEducation(), family[i].getJobTitle(), family[i].getCompany(), family[i].getRemark()
      });
      }
    }
  }

  /**
   *
   */
  class FamilyDetailTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }

    public void valueChanged(ListSelectionEvent e) {
      if(!e.getValueIsAdjusting()){
        int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();

        if(m_new || m_edit){
          if(iRowIndex != m_editedIndex)
            m_detailtable.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        }
        else{
          if(iRowIndex != -1){
            if(m_editable) {
              m_addBt.setEnabled(true);
              m_editBt.setEnabled(true);
              m_saveBt.setEnabled(false);
              m_cancelBt.setEnabled(false);
              m_deleteBt.setEnabled(true);
            }

            m_family = (EmployeeFamily)getValueAt(iRowIndex, 0);
            m_table.setFamily(m_family);
            m_editedIndex = iRowIndex;
          }
          else{
            m_addBt.setEnabled(true);
            m_editBt.setEnabled(false);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(false);

            m_table.clearTable();
          }
        }
      }
    }
  }
}