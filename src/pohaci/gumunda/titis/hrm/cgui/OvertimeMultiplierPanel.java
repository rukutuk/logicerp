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
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class OvertimeMultiplierPanel extends JPanel implements ActionListener  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
OvertimeMultiplierTable m_table;
  JComboBox m_typeComboBox = new JComboBox(OvertimeMultiplier.m_types);
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  OvertimeMultiplier m_multiplier = null;

  public OvertimeMultiplierPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new OvertimeMultiplierTable();
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

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    setLayout(new BorderLayout());
    add(centerPanel, BorderLayout.CENTER);
  }

  void initData() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      OvertimeMultiplier[] multiplier = logic.getAllOvertimeMultiplier(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < multiplier.length;  i ++) {
        model.addRow(new Object[]{
        multiplier[i], new Integer(multiplier[i].getHourMin()),
        new Integer(multiplier[i].getHourMax()),
        new Float(multiplier[i].getMultiplier())
      });
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void onNew() {
    m_new = true;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.addRow(new Object[]{});
    m_editedIndex = model.getRowCount() - 1;
    m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    m_table.setValueAt(new Integer(0), m_editedIndex, 1);
    m_table.setValueAt(new Integer(0), m_editedIndex, 2);
    m_table.setValueAt(new Float(0.0), m_editedIndex, 3);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    m_multiplier = (OvertimeMultiplier)m_table.getValueAt(m_editedIndex, 0);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    m_table.stopCellEditing();
    java.util.ArrayList list = new java.util.ArrayList();
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();

    Object objValue = model.getValueAt(m_editedIndex, 0);
    String strtype = "";
    if(objValue instanceof OvertimeMultiplier)
      strtype = ((OvertimeMultiplier)objValue).getTypeAsString();
    else
      strtype = (String)objValue;

    if(strtype == null || strtype.equals(""))
      list.add("Day Status");

    int hourmin = ((Integer)model.getValueAt(m_editedIndex, 1)).intValue();
    int hourmax = ((Integer)model.getValueAt(m_editedIndex, 2)).intValue();
    float multiply = ((Float)model.getValueAt(m_editedIndex, 3)).floatValue();

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      JOptionPane.showMessageDialog(this, strexc, "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

      if(m_new){
        OvertimeMultiplier multiplier = logic.createOvertimeMultiplier(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new OvertimeMultiplier(
            OvertimeMultiplier.typeFromStringToID(strtype), hourmin, hourmax, multiply));
        m_table.updateOvertimeMultiplier(multiplier, m_editedIndex);
      }
      else if(m_edit){
        OvertimeMultiplier multiplier = logic.updateOvertimeMultiplier(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_multiplier.getIndex(), new OvertimeMultiplier(
            OvertimeMultiplier.typeFromStringToID(strtype), hourmin, hourmax, multiply));
        m_table.updateOvertimeMultiplier(multiplier, m_editedIndex);
      }
    }
    catch(Exception ex){
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
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
    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    if(m_new){
      m_new = false;
      model.removeRow(m_editedIndex);
    }
    else if(m_edit){
      m_edit = false;
      m_table.updateOvertimeMultiplier(m_multiplier, m_editedIndex);
      m_addBt.setEnabled(true);
      m_editBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
      m_deleteBt.setEnabled(true);
    }
  }

  void onDelete() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    m_editedIndex = m_table.getSelectedRow();
    OvertimeMultiplier multiplier = (OvertimeMultiplier)model.getValueAt(m_editedIndex, 0);

    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    try{
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.deleteOvertimeMultiplier(m_sessionid, IDBConstants.MODUL_MASTER_DATA, multiplier.getIndex());
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
    model.removeRow(m_editedIndex);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      onNew();
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
  class OvertimeMultiplierTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OvertimeMultiplierTable() {
      OvertimeMultiplierTableModel model = new OvertimeMultiplierTableModel();
      model.addColumn("Day Status");
      model.addColumn("Hour Min");
      model.addColumn("Hour Max");
      model.addColumn("Multiplier");
      setModel(model);

      getColumnModel().getColumn(3).setCellRenderer(new FloatCellRenderer(JLabel.CENTER));
      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 0)
        return new DefaultCellEditor(m_typeComboBox);
      else if(col == 1 || col == 2)
        return new IntegerCellEditor();
      else if(col == 3)
        return new FloatCellEditor();
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void updateOvertimeMultiplier(OvertimeMultiplier multiplier, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);

      model.insertRow(row, new Object[]{multiplier, new Integer(multiplier.getHourMin()), new Integer(multiplier.getHourMax()),  new Float(multiplier.getMultiplier())});
      getSelectionModel().setSelectionInterval(row, row);
    }
  }

  /**
   *
   */
  class OvertimeMultiplierTableModel extends DefaultTableModel implements ListSelectionListener {
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