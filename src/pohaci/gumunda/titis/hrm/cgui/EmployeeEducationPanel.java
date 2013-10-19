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

public class EmployeeEducationPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  EducationTable m_table;
  EducationDetailTable m_detailtable;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;
  FilePanel m_filepanel;

  boolean m_editable = false;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  EmployeeEducation m_education = null;

  public EmployeeEducationPanel(Connection conn, long sessionid) {
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

    m_table = new EducationTable();
    scrollpane.getViewport().add(m_table);
    scrollpane.setPreferredSize(new Dimension(400, 145));

    m_filepanel = new FilePanel("Certificate Attachment");
    m_detailtable = new EducationDetailTable();

    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(false);

    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(scrollpane, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
    formPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    formPanel.add(m_filepanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
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
      m_filepanel.setEditable(editable);
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
    m_table.clear();
    m_table.setValueAt(new Float(0.0f), 5, 1);
    m_table.setValueAt(new Float(0.0f), 6, 1);
    m_filepanel.clear();
    m_filepanel.setEditable(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_detailtable.getSelectedRow();
    m_education = (EmployeeEducation)m_detailtable.getValueAt(m_editedIndex, 0);
    m_filepanel.setEditable(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    EmployeeEducation education = null;
    try {
      education = m_table.getEducation();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    if(m_new)
      m_detailtable.addEducation(education);
    else {
      m_detailtable.updateEducation(education, m_editedIndex);
    }

    m_new = false;
    m_edit = false;
    m_filepanel.setEditable(false);
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
  }

  void onDelete() {
    if(!Misc.getConfirmation())
      return;
    m_detailtable.deleteEducation(m_editedIndex);
  }

  void onCancel() {
    m_table.stopCellEditing();

    if(!Misc.getConfirmation())
      return;

    if(m_new) {
      m_new = false;
      m_table.clear();
      m_filepanel.clear();
      m_filepanel.setEditable(false);
      m_addBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);

      if(m_editedIndex != -1) {
        m_editBt.setEnabled(true);
        m_deleteBt.setEnabled(true);
        m_table.setEducation(m_education);
      }
      else {
        m_editBt.setEnabled(false);
        m_deleteBt.setEnabled(false);
      }
    }
    else if(m_edit) {
      m_edit = false;
      m_detailtable.updateEducation(m_education, m_editedIndex);
      m_filepanel.setEditable(false);
      m_addBt.setEnabled(true);
      m_editBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
      m_deleteBt.setEnabled(true);
    }
  }

  public EmployeeEducation[] getEducation() {
    return m_detailtable.getEducation();
  }

  public void setEducation(EmployeeEducation[] education) {
    m_new = false; m_edit = false;
    m_table.clear();
    m_filepanel.clear();
    m_detailtable.setEducation(education);
  }

  public void clear() {
    m_table.clear();
    m_filepanel.clear();
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
  class EducationTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EducationTable() {
      EducationTableModel model = new EducationTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
      model.addRow(new Object[]{"Grade", ""});
      model.addRow(new Object[]{"Major Study", ""});
      model.addRow(new Object[]{"Institute", ""});
      model.addRow(new Object[]{"From", ""});
      model.addRow(new Object[]{"To", ""});
      model.addRow(new Object[]{"GPA", ""});
      model.addRow(new Object[]{"Max. GPA", ""});
      model.addRow(new Object[]{"Description", ""});
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(row == 0 && col == 1)
        return new EducationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                       "Education", m_conn, m_sessionid);
      else if((row == 3 || row == 4) && col == 1)
        return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
      else if((row == 5 || row == 6) && col == 1)
        return new FloatCellEditor();
      return new BaseTableCellEditor();
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
      if((row == 5 || row == 6) && col == 1)
        return new FloatCellRenderer(JLabel.LEFT);
      return super.getCellRenderer(row, col);
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void clear() {
      int count = getRowCount();
      for(int i = 0; i < count; i ++)
        setValueAt("", i, 1);
    }

    public EmployeeEducation getEducation() throws Exception {
      stopCellEditing();
      java.util.ArrayList list = new java.util.ArrayList();
      Education grade = null;
      String major = "", institute = "";
      java.util.Date from = null, to = null;
      float gpa = 0.0f, maxgpa = 0.0f;
      String description = "", file = "";
      byte[] certificate = null;

      int row = 0;
      Object obj  = getValueAt(row++, 1);
      if(obj instanceof Education)
        grade = (Education)obj;
      else
        list.add("Grade");

      major = (String)getValueAt(row++, 1);
      // sorry aku ngehapus mandatory field...
      //if(major == null || major.equals(""))
      //  list.add("Major study");
      institute = (String)getValueAt(row++, 1);
      //if(institute == null || institute.equals(""))
      //  list.add("Institute");
      String strfrom = (String)getValueAt(row++, 1);
      String strto = (String)getValueAt(row++, 1);
      if(strto.equals(""))
        list.add("To");

      String strexc = "Please insert description of :\n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int i = 0; i < exception.length; i ++)
          strexc += "- " + exception[i] + "\n";
        throw new Exception(strexc);
      }

      try {
        if(!strfrom.equals(""))
          from = m_dateformat.parse(strfrom);
        if (!strto.equals("")) 
          to = m_dateformat.parse(strto);
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      gpa = ((Float)getValueAt(row++, 1)).floatValue();
      maxgpa = ((Float)getValueAt(row++, 1)).floatValue();
      description = (String)getValueAt(row++, 1);
      file = m_filepanel.getFileName();
      certificate = m_filepanel.getFileByte();


      return new EmployeeEducation(grade, major, institute, from, to,
                                   gpa, maxgpa, description, file, certificate);
    }

    public void setEducation(EmployeeEducation education) {
      int row = 0;

      setValueAt(education.getGrade(), row++, 1);
      setValueAt(education.getMajorStudy(), row++, 1);
      setValueAt(education.getInstitute(), row++, 1);
      if(education.getFrom() != null)
        setValueAt(m_dateformat.format(education.getFrom()), row++, 1);
      else
        setValueAt("", row++, 1);
      if(education.getTo() != null)
        setValueAt(m_dateformat.format(education.getTo()), row++, 1);
      else
        setValueAt("", row++, 1);
      setValueAt(new Float(education.getGPA()), row++, 1);
      setValueAt(new Float(education.getGPA()), row++, 1);
      setValueAt(education.getDescription(), row++, 1);

      m_filepanel.setFileName(education.getFile());
      m_filepanel.setFileByte(education.getCertificate());
    }
  }

  /**
   *
   */
  class EducationTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      if(row == 8)
        return false;
      if(m_new || m_edit)
        return true;
      return false;
    }
  }

  /**
   *
   */
  class EducationDetailTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EducationDetailTable() {
      EducationDetailTableModel model = new EducationDetailTableModel();
      model.addColumn("Grade");
      model.addColumn("Major Study");
      model.addColumn("Institute");
      model.addColumn("From");
      model.addColumn("To");
      model.addColumn("GPA");
      model.addColumn("Max. GPA");
      model.addColumn("Description");

      setModel(model);
      getSelectionModel().addListSelectionListener(model);
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }

    void addEducation(EmployeeEducation education) {
      String from = "", to = "";
      if(education.getFrom() != null)
        from = m_dateformat.format(education.getFrom());
      if(education.getTo() != null)
        to = m_dateformat.format(education.getTo());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        education, education.getMajorStudy(), education.getInstitute(), from,
        to, new Float(education.getGPA()), new Float(education.getMaxGPA()), education.getDescription()
      });

      m_editedIndex = getRowCount() -1;
      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void updateEducation(EmployeeEducation education, int row) {
      String from = "", to = "";
      if(education.getFrom() != null)
        from = m_dateformat.format(education.getFrom());
      if(education.getTo() != null)
        to = m_dateformat.format(education.getTo());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        education, education.getMajorStudy(), education.getInstitute(), from,
        to, new Float(education.getGPA()), new Float(education.getMaxGPA()), education.getDescription()
      });

      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void deleteEducation(int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
    }

    public EmployeeEducation[] getEducation() {
      Vector vresult = new Vector();
      for(int i = 0; i < getRowCount(); i ++)
        vresult.addElement((EmployeeEducation)getValueAt(i, 0));

      EmployeeEducation[] result = new EmployeeEducation[vresult.size()];
      vresult.copyInto(result);
      return result;
    }

    public void setEducation(EmployeeEducation[] education) {
      m_editedIndex = -1;
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      for(int i = 0; i < education.length; i ++) {
        String from = "", to = "";
        if(education[i].getFrom() != null)
          from = m_dateformat.format(education[i].getFrom());
        if(education[i].getTo() != null)
          to = m_dateformat.format(education[i].getTo());

        model.addRow(new Object[]{
          education[i], education[i].getMajorStudy(), education[i].getInstitute(), from,
          to, new Float(education[i].getGPA()), new Float(education[i].getMaxGPA()), education[i].getDescription()
        });
      }
    }
  }

  /**
   *
   */
  class EducationDetailTableModel extends DefaultTableModel implements ListSelectionListener {
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

            m_education = (EmployeeEducation)getValueAt(iRowIndex, 0);
            m_table.setEducation(m_education);
            m_editedIndex = iRowIndex;
          }
          else{
            m_addBt.setEnabled(true);
            m_editBt.setEnabled(false);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(false);

            m_table.clear();
            m_filepanel.clear();
          }
        }
      }
    }
  }
}