package pohaci.gumunda.titis.accounting.cgui;

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
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class UnitPanel extends JPanel implements ActionListener  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
UnitTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  Unit m_unit = null;

  public UnitPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new UnitTable();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    buttonPanel.add(m_addBt);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    buttonPanel.add(m_editBt);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    buttonPanel.add(m_saveBt);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);;
    buttonPanel.add(m_cancelBt);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);;
    buttonPanel.add(m_deleteBt);

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
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Unit[] unit = logic.getAllUnit(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < unit.length;  i ++) {
    	  unit[i] = new Unit(unit[i], Unit.CODE);
        model.addRow(new Object[]{unit[i], unit[i].getDescription()});
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
    }
  }

  void onNew() {
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
    m_unit = (Unit)m_table.getValueAt(m_editedIndex, 0);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    m_table.stopCellEditing();

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    Object objValue = model.getValueAt(m_editedIndex, 0);
    String code = "";
    if(objValue instanceof Unit)
      code = ((Unit)objValue).getCode();
    else
      code = (String)objValue;

    if(code == null || code.equals("")){
      JOptionPane.showMessageDialog(this, "Unit code have to fill.");
      return;
    }

    String note = (String)model.getValueAt(m_editedIndex, 1);

    try {
      AccountingBusinessLogic loic = new AccountingBusinessLogic(m_conn);

      if(m_new){
        Unit unit = loic.createUnit(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new Unit(code, note));
        unit = new Unit(unit, Unit.CODE);
        model.setValueAt(unit, m_editedIndex, 0);
      }
      else if(m_edit){
        Unit unit = loic.updateUnit(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_unit.getIndex(), new Unit(code, note));
        unit = new Unit(unit, Unit.CODE);
        m_table.updateUnit(unit, m_editedIndex);
      }
    }
    catch(Exception ex){
      ex.printStackTrace();
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

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    if(m_new){
      m_new = false;
      model.removeRow(m_editedIndex);
    }
    else if(m_edit){
      m_edit = false;
      m_table.updateUnit(m_unit, m_editedIndex);
      m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
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
    Unit unit = (Unit)model.getValueAt(m_editedIndex, 0);

    try{
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteUnit(m_sessionid, IDBConstants.MODUL_MASTER_DATA, unit.getIndex());
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
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
  class UnitTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnitTable() {
      UnitTableModel model = new UnitTableModel();
      model.addColumn("Unit Code");
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

    public void updateUnit(Unit unit, int row) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
      unit = new Unit(unit, Unit.CODE);
      model.insertRow(row, new Object[]{unit, unit.getDescription()});
    }
  }

  /**
   *
   */
  class UnitTableModel extends DefaultTableModel implements ListSelectionListener {
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