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
import pohaci.gumunda.titis.application.DblCellEditor;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class PTKPPanel extends JPanel implements ActionListener  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PTKPTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  PTKP m_ptkp = null;

  public PTKPPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new PTKPTable();
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
      PTKP[] ptkp = logic.getAllPTKP(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < ptkp.length;  i ++) {
        model.addRow(new Object[]{ptkp[i], ptkp[i].getDescription(), new Double(ptkp[i].getValue())});
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
    m_table.setValueAt(new Double(0.0), m_editedIndex, 2);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    m_ptkp = (PTKP)m_table.getValueAt(m_editedIndex, 0);

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
    String name = "";
    if(objValue instanceof PTKP)
      name = ((PTKP)objValue).getName();
    else
      name = (String)objValue;

    if(name == null || name.equals("")){
      JOptionPane.showMessageDialog(this, "Name of PTKP have to fill.");
      return;
    }

    String desc = (String)model.getValueAt(m_editedIndex, 1);
    if(desc == null || desc.equals("")){
      JOptionPane.showMessageDialog(this, "Description of PTKP have to fill.");
      return;
    }

    double value = ((Double)model.getValueAt(m_editedIndex, 2)).doubleValue();

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

      if(m_new){
        PTKP ptkp = logic.createPTKP(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new PTKP(name, desc, value));
        model.setValueAt(ptkp, m_editedIndex, 0);
      }
      else if(m_edit){
        PTKP ptkp = logic.updatePTKP(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_ptkp.getIndex(), new PTKP(name, desc, value));
        model.setValueAt(ptkp, m_editedIndex, 0);
      }
    }
    catch(Exception ex){
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
      m_table.updatePTKP(m_ptkp, m_editedIndex);
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
    PTKP ptkp = (PTKP)model.getValueAt(m_editedIndex, 0);

    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    try{
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.deletePTKP(m_sessionid, IDBConstants.MODUL_MASTER_DATA, ptkp.getIndex());
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
  class PTKPTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PTKPTable() {
      PTKPTableModel model = new PTKPTableModel();
      model.addColumn("Name");
      model.addColumn("Description");
      model.addColumn("Value (Yearly)");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(80);
      getColumnModel().getColumn(0).setMaxWidth(80);
      getColumnModel().getColumn(2).setCellRenderer(new DoubleCellRenderer(JLabel.RIGHT));
      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 2)
        return new DblCellEditor();
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void updatePTKP(PTKP ptkp, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);

      model.insertRow(row, new Object[]{ptkp, ptkp.getDescription(), new Double(ptkp.getValue())});
      getSelectionModel().setSelectionInterval(row, row);
    }
  }

  /**
   *
   */
  class PTKPTableModel extends DefaultTableModel implements ListSelectionListener {
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