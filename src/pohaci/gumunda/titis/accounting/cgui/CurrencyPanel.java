package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;

public class CurrencyPanel extends JPanel implements ActionListener  {
  /**
	 *
	 */
	private static final long serialVersionUID = 1L;
CurrencyTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  Currency m_currency = null;

  public CurrencyPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new CurrencyTable();
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
      Currency[] currency = logic.getAllCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < currency.length;  i ++) {
        model.addRow(new Object[]{currency[i], currency[i].getCode(), currency[i].getDescription(), currency[i].getSay(),currency[i].getCent(),currency[i].getLanguage()});
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

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    m_currency = (Currency)m_table.getValueAt(m_editedIndex, 0);

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
    String symbol = "", code = "", description = "", say="", cent="", language="";
    if(objValue instanceof Currency)
      symbol = ((Currency)objValue).getSymbol();
    else
      symbol = (String)objValue;

    if(symbol == null || symbol.equals(""))
      list.add("Symbol");

    code = (String)model.getValueAt(m_editedIndex, 1);
    if(code == null || code.equals(""))
      list.add("Code");

    description = (String)model.getValueAt(m_editedIndex, 2);

    say = (String) model.getValueAt(m_editedIndex, 3);
    if (say==null || say.equals(""))
    	list.add("Say");

    cent = (String) model.getValueAt(m_editedIndex, 4);
    if (cent==null || cent.equals(""))
    	list.add("Cent Words");

    language = (String) model.getValueAt(m_editedIndex, 5);
    if (language==null || language.equals(""))
    	list.add("Language");


    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      JOptionPane.showMessageDialog(this, strexc);
      return;
    }



    try {
      AccountingBusinessLogic loic = new AccountingBusinessLogic(m_conn);

      if(m_new){
        Currency currency = loic.createCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new Currency(symbol, code, description, say, cent, language));
        m_table.updateCurrency(currency, m_editedIndex);
      }
      else if(m_edit){
        Currency currency = loic.updateCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_currency.getIndex(), new Currency(symbol, code, description, say, cent, language));
        m_table.updateCurrency(currency, m_editedIndex);
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

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    if(m_new){
      m_new = false;
      model.removeRow(m_editedIndex);
    }
    else if(m_edit){
      m_edit = false;
      m_table.updateCurrency(m_currency, m_editedIndex);
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
    Currency currency = (Currency)model.getValueAt(m_editedIndex, 0);

    try{
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA, currency.getIndex());
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
  class CurrencyTable extends JTable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CurrencyTable() {
      CurrencyTableModel model = new CurrencyTableModel();
      model.addColumn("Symbol");
      model.addColumn("Code");
      model.addColumn("Description");
      model.addColumn("Say");
      model.addColumn("Cents Words");
      model.addColumn("Language");
      setModel(model);

      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
    	if (col==5)
    		return new ComboCellEditor(MoneyTalk.LANGUAGES);
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void updateCurrency(Currency currency, int row) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{currency, currency.getCode(), currency.getDescription(), currency.getSay(),currency.getCent(),currency.getLanguage()});
    }
  }

  /**
   *
   */
  class CurrencyTableModel extends DefaultTableModel implements ListSelectionListener {
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

  private class ComboCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 5524867489154321611L;
	private CellEditorListener listener = null;
	private JComboBox comboBox = new JComboBox();
	private Object[] selections;

	public ComboCellEditor(Object[] selections) {
		super();
		this.selections = selections;
		this.comboBox = new JComboBox(selections);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		if (value == null) {
			comboBox.setSelectedIndex(-1);
		} else {
			comboBox.setSelectedItem(value);
		}
		return comboBox;
	}
	public Object getCellEditorValue() {

		return comboBox.getSelectedItem();
	}
	public void actionPerformed(ActionEvent e) {
		stopCellEditing();
	}

	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}
  }
}