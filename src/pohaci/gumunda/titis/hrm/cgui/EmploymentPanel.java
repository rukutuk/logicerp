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
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.accounting.cgui.*;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class EmploymentPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  EmploymentTable m_table;
  EmploymentDetailTable m_detailtable;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  boolean m_editable = false;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  Employment m_employment = null;

  public EmploymentPanel(Connection conn, long sessionid) {
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

    m_table = new EmploymentTable();
    scrollpane.getViewport().add(m_table);
    scrollpane.setPreferredSize(new Dimension(350, 161));

    m_detailtable = new EmploymentDetailTable();

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
    m_employment = (Employment)m_detailtable.getValueAt(m_editedIndex, 0);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    Employment employment = null;
    //String enddate=""; 
    try {
      employment = m_table.getEmployment();
      /*if (!employment.getWorkAgreement().getDescription().equals("Permanent"))
    	  enddate = m_dateformat.format(employment.getEndDate());*/
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    if(m_new)
      m_detailtable.addEmployment(employment);
    else
      m_detailtable.updateEmployment(employment, m_editedIndex);

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
    m_detailtable.deleteEmployment(m_editedIndex);
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
       	m_table.setEmployment(m_employment);
      }
      else {
        m_editBt.setEnabled(false);
        m_deleteBt.setEnabled(false);
      }
    }
    else if(m_edit) {
      m_edit = false;
      m_detailtable.updateEmployment(m_employment, m_editedIndex);

      m_addBt.setEnabled(true);
      m_editBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
      m_deleteBt.setEnabled(true);
    }
  }

  public Employment[] getEmployment() {
    return m_detailtable.getEmployment();
  }

  public void setEmployment(Employment[] employment) {
    m_new = false; m_edit = false;
    m_table.clearTable();
    m_detailtable.setEmployment(employment);
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
  class EmploymentTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmploymentTable() {
      EmploymentTableModel model = new EmploymentTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
      model.addRow(new Object[]{"Job Title", ""});
      model.addRow(new Object[]{"Department", ""});
      model.addRow(new Object[]{"Unit Code", ""});
      model.addRow(new Object[]{"Reference No", ""});
      model.addRow(new Object[]{"Reference Date", ""});
      model.addRow(new Object[]{"TMT", ""});
      model.addRow(new Object[]{"End Date", ""});
      model.addRow(new Object[]{"Work Agreement", ""});
      model.addRow(new Object[]{"Description", ""});
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 1) {
        if(row == 0)
          return new JobTitleCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                        "Job Title", m_conn, m_sessionid);
        else if(row == 1)
          return new OrganizationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
              m_conn, m_sessionid);
        else if(row == 2)
          return new UnitCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                    "Unit", m_conn, m_sessionid);
        else if(row == 4 || row == 5 || row == 6)
          return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
        else if(row == 7)
          return new WorkAgreementCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
              "Work Agreement", m_conn, m_sessionid);
      }
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

    public Employment getEmployment() throws Exception {
      stopCellEditing();

      java.util.ArrayList list = new java.util.ArrayList();
      JobTitle jobtitle = null;
      Organization department = null;
      Unit unit = null;
      String refno = "", description = "";
      WorkAgreement agreement = null;
      java.util.Date refdate = null, tmt = null, enddate = null;

      int row = 0;
      Object obj = getValueAt(row++, 1);
      if(obj instanceof JobTitle)
        jobtitle = (JobTitle)obj;
      else
        list.add("Job Title");

      obj = getValueAt(row++, 1);
      if(obj instanceof Organization)
        department = (Organization)obj;
      else
        list.add("Department");

      obj = getValueAt(row++, 1);
      if(obj instanceof Unit)
        unit = (Unit)obj;
      else
        list.add("Unit Code");

      refno = (String)getValueAt(row++, 1);
      if(refno == null || refno.equals(""))
        list.add("Reference No.");

      String strrefdate = (String)getValueAt(row++, 1);
      String strtmt = (String)getValueAt(row++, 1);
      if(strtmt == null || strtmt.equals(""))
        list.add("TMT");
      
      String strenddate = (String)getValueAt(row++, 1);

      try {
        if(!strrefdate.equals(""))
          refdate = m_dateformat.parse(strrefdate);

        if(!strtmt.equals(""))
          tmt = m_dateformat.parse(strtmt);

        if(!strenddate.equals(""))
          enddate = m_dateformat.parse(strenddate);
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      obj = getValueAt(row++, 1);
      if(obj instanceof WorkAgreement)
        agreement = (WorkAgreement)obj;
      else
        list.add("Work Agreement");

      description = (String)getValueAt(row++, 1);

      String strexc = "Please insert description of :\n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int i = 0; i < exception.length; i ++)
          strexc += " - " + exception[i] + "\n";
        throw new Exception(strexc);
      }

      return new Employment(jobtitle, department, unit, refno,
                            refdate, tmt, enddate, agreement, description);
    }

    public void setEmployment(Employment employment) {
      int row = 0;

      setValueAt(employment.getJobTitle(), row++, 1);
      setValueAt(employment.getDepartment(), row++, 1);
      setValueAt(employment.getUnit(), row++, 1);
      setValueAt(employment.getReferenceNo(), row++, 1);

      if(employment.getReferenceDate() != null)
        setValueAt(m_dateformat.format(employment.getReferenceDate()), row++, 1);
      else
        setValueAt("", row++, 1);

      if(employment.getTMT() != null)
        setValueAt(m_dateformat.format(employment.getTMT()), row++, 1);
      else
        setValueAt("", row++, 1);

      if(employment.getEndDate() != null)
        setValueAt(m_dateformat.format(employment.getEndDate()), row++, 1);
      else
        setValueAt("", row++, 1);

      setValueAt(employment.getWorkAgreement(), row++, 1);
      setValueAt(employment.getDescription(), row++, 1);
    }
  }

  /**
   *
   */
  class EmploymentTableModel extends DefaultTableModel {
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

    public void setValueAt(Object obj, int row, int col) {
      if(obj instanceof JobTitle)
        super.setValueAt(new JobTitle((JobTitle)obj, JobTitle.NAME), row, col);
      else if(obj instanceof Organization)
        super.setValueAt(new Organization((Organization)obj, Organization.NAME), row, col);
      else if(obj instanceof WorkAgreement)
        super.setValueAt(new WorkAgreement((WorkAgreement)obj, WorkAgreement.DESCRIPTION), row, col);
      else
        super.setValueAt(obj, row, col);
    }
  }

  /**
   *
   */
  class EmploymentDetailTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmploymentDetailTable() {
      EmploymentDetailTableModel model = new EmploymentDetailTableModel();
      model.addColumn("Job Title");
      model.addColumn("Department");
      model.addColumn("Unit Code");
      model.addColumn("Ref. No");
      model.addColumn("Ref. Date");
      model.addColumn("TMT");
      model.addColumn("End Date");
      model.addColumn("Work Agreement");
      model.addColumn("Description");

      setModel(model);
      getSelectionModel().addListSelectionListener(model);
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }

    void addEmployment(Employment employment) {
      String refdate = "", tmt = "", enddate = "";
      if(employment.getReferenceDate() != null)
        refdate = m_dateformat.format(employment.getReferenceDate());

      if(employment.getTMT() != null)
        tmt = m_dateformat.format(employment.getTMT());

      if(employment.getEndDate() != null)
        enddate = m_dateformat.format(employment.getEndDate());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        employment, employment.getDepartment(), employment.getUnit(), employment.getReferenceNo(),
        refdate, tmt, enddate, employment.getWorkAgreement(), employment.getDescription()
      });

      m_editedIndex = getRowCount() -1;
      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void updateEmployment(Employment employment, int row) {
      String refdate = "", tmt = "", enddate = "";
      if(employment.getReferenceDate() != null)
        refdate = m_dateformat.format(employment.getReferenceDate());

      if(employment.getTMT() != null)
        tmt = m_dateformat.format(employment.getTMT());

      if(employment.getEndDate() != null)
        enddate = m_dateformat.format(employment.getEndDate());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        employment, employment.getDepartment(), employment.getUnit(), employment.getReferenceNo(),
        refdate, tmt, enddate, employment.getWorkAgreement(), employment.getDescription()
      });

      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void deleteEmployment(int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
    }

    public Employment[] getEmployment() {
      Vector vresult = new Vector();
      for(int i = 0; i < getRowCount(); i ++)
        vresult.addElement((Employment)getValueAt(i, 0));

      Employment[] result = new Employment[vresult.size()];
      vresult.copyInto(result);
      return result;
    }

    public void setEmployment(Employment[] employment) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      m_editedIndex = -1;

      for(int i = 0; i < employment.length; i ++) {
        String refdate = "", tmt = "", enddate = "";
        if(employment[i].getReferenceDate() != null)
          refdate = m_dateformat.format(employment[i].getReferenceDate());

        if(employment[i].getTMT() != null)
          tmt = m_dateformat.format(employment[i].getTMT());

        if(employment[i].getEndDate() != null)
          enddate = m_dateformat.format(employment[i].getEndDate());

        model.addRow(new Object[]{
          employment[i], employment[i].getDepartment(), employment[i].getUnit(), employment[i].getReferenceNo(),
          refdate, tmt, enddate, employment[i].getWorkAgreement(), employment[i].getDescription()
        });
      }
    }
  }

  /**
   *
   */
  class EmploymentDetailTableModel extends DefaultTableModel implements ListSelectionListener {
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

            m_employment = (Employment)getValueAt(iRowIndex, 0);
            m_table.setEmployment(m_employment);
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