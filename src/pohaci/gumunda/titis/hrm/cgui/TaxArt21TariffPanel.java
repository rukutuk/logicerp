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
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class TaxArt21TariffPanel extends JPanel implements ActionListener  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
TaxArt21TariffTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  TaxArt21Tariff m_tariff = null;

  public TaxArt21TariffPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new TaxArt21TariffTable();
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
      TaxArt21Tariff[] tariff = logic.getAllTaxArt21Tariff(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < tariff.length;  i ++) {
        model.addRow(new Object[]{tariff[i], new Double(tariff[i].getMaximum()), new Double(tariff[i].getTariff())});
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
    m_table.setValueAt(new Double(0.0), m_editedIndex, 0);
    m_table.setValueAt(new Double(0.0), m_editedIndex, 1);
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
    m_tariff = (TaxArt21Tariff)m_table.getValueAt(m_editedIndex, 0);

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
    double minimum = 0.0;
    if(objValue instanceof TaxArt21Tariff)
      minimum = ((TaxArt21Tariff)objValue).getMinimum();
    else
      minimum = ((Double)objValue).doubleValue();

    if(minimum == 0.0)
      list.add("Mimimum > 0,00");

    double maximum = ((Double)model.getValueAt(m_editedIndex, 1)).doubleValue();
    if(maximum == 0.0)
      list.add("Maximum > 0,00");

    float value = ((Double)model.getValueAt(m_editedIndex, 2)).floatValue();

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
        TaxArt21Tariff tariff = logic.createTaxArt21Tariff(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new TaxArt21Tariff(minimum, maximum, value));
        m_table.updateTaxArt21Tariff(tariff, m_editedIndex);
      }
      else if(m_edit){
        TaxArt21Tariff tariff = logic.updateTaxArt21Tariff(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_tariff.getIndex(), new TaxArt21Tariff(minimum, maximum, value));
        m_table.updateTaxArt21Tariff(tariff, m_editedIndex);
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
      m_table.updateTaxArt21Tariff(m_tariff, m_editedIndex);
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
    TaxArt21Tariff tariff = (TaxArt21Tariff)model.getValueAt(m_editedIndex, 0);

    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    try{
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.deleteTaxArt21Tariff(m_sessionid, IDBConstants.MODUL_MASTER_DATA, tariff.getIndex());
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
  class TaxArt21TariffTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaxArt21TariffTable() {
      TaxArt21TariffTableModel model = new TaxArt21TariffTableModel();
      model.addColumn("Mimimum");
      model.addColumn("Maximum");
      model.addColumn("Tariff");
      setModel(model);

      getColumnModel().getColumn(2).setPreferredWidth(80);
      getColumnModel().getColumn(2).setMaxWidth(80);
      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      return new DblCellEditor();
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
    	if(col==0)
    		return new StandardFormatCellRenderer(JLabel.RIGHT);
      if(col == 2)
        return new PercentCellRenderer(JLabel.RIGHT);
      return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void updateTaxArt21Tariff(TaxArt21Tariff tariff, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);

      model.insertRow(row, new Object[]{tariff, new Double(tariff.getMaximum()), new Double(tariff.getTariff())});
      getSelectionModel().setSelectionInterval(row, row);
    }
  }
  
  private static class StandardFormatCellRenderer extends 
	DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int horizontalAlignment = JLabel.LEFT;
	
	
	public StandardFormatCellRenderer(int horizontalAlignment){
		this.horizontalAlignment = horizontalAlignment;
	}
	
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
			super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		
		setText((value == null) ? "    " : value.toString());
		setHorizontalAlignment(this.horizontalAlignment);
	
		return this;
	}
}

  /**
   *
   */
  class TaxArt21TariffTableModel extends DefaultTableModel implements ListSelectionListener {
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