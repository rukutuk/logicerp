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

public class CreditorListPanel extends JPanel implements ActionListener  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
BankAccountTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  CreditorList m_creditor = null;

  public CreditorListPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new BankAccountTable();
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
      CreditorList[] bank = logic.getAllCreditorList(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < bank.length;  i ++) {
        model.addRow(new Object[]{
        bank[i], bank[i].getCreditorType(), bank[i].getName(), bank[i].getAddress(), bank[i].getProvince(), bank[i].getCountry(),
        bank[i].getContact(), bank[i].getPhone(), bank[i].getBankAccount(),
        bank[i].getBankName(), bank[i].getCurrency(), bank[i].getBankAddress()
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

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    m_creditor = (CreditorList)m_table.getValueAt(m_editedIndex, 0);

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

    String code = "", type = "", name = "", address = "", province = "", country = "";
    String contact = "", phone = "", bankaccount = "", bankname = "", bankaddress = "";
    Currency currency = null;

    int col = 0;

    Object object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof CreditorList)
      code = ((CreditorList)object).getCode();
    else
      code = (String)object;

    if(code == null || code.equals(""))
      list.add("Code");
    
    type = (String)model.getValueAt(m_editedIndex, col++);
    if(type==null || type.equals(""))
    	list.add("Creditor Type");

    name = (String)model.getValueAt(m_editedIndex, col++);
    if(name == null || name.equals(""))
      list.add("Bank Name");

    address = (String)model.getValueAt(m_editedIndex, col++);
    province = (String)model.getValueAt(m_editedIndex, col++);
    country = (String)model.getValueAt(m_editedIndex, col++);
    contact = (String)model.getValueAt(m_editedIndex, col++);
    phone = (String)model.getValueAt(m_editedIndex, col++);
    bankaccount = (String)model.getValueAt(m_editedIndex, col++);
    bankname = (String)model.getValueAt(m_editedIndex, col++);

    object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof Currency)
      currency = (Currency)object;
    else
      list.add("Currency");

    bankaddress = (String)model.getValueAt(m_editedIndex, col++);

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
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);

      if(m_new){
        CreditorList bank = logic.createCreditorList(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new CreditorList(code, name,
            address, province, country, contact, phone, bankaccount, bankname, currency, bankaddress, type));
        m_table.updateCreditorList(bank, m_editedIndex);
      }
      else if(m_edit){
        CreditorList bank = logic.updateCreditorList(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_creditor.getIndex(),
            new CreditorList(code, name,
            address, province, country, contact, phone, bankaccount, bankname, currency, bankaddress, type));
        m_table.updateCreditorList(bank, m_editedIndex);
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
      m_table.updateCreditorList(m_creditor, m_editedIndex);
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
    CreditorList bank = (CreditorList)model.getValueAt(m_editedIndex, 0);

    try{
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteCreditorList(m_sessionid, IDBConstants.MODUL_MASTER_DATA, bank.getIndex());
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
  class BankAccountTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankAccountTable() {
      BankAccountTableModel model = new BankAccountTableModel();
      model.addColumn("Creditor Code");
      model.addColumn("Creditor Type");
      model.addColumn("Creditor Name");
      model.addColumn("Address");
      model.addColumn("Province");
      model.addColumn("Country");
      model.addColumn("Contact");
      model.addColumn("Phone");
      model.addColumn("Bank Account No.");
      model.addColumn("Bank Name");
      model.addColumn("Currency");
      model.addColumn("Bank Account Address");
      setModel(model);

      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 1)
    	  return new DefaultCellEditor(new JComboBox(
    			  new String[]{CreditorList.CREDITOR_TYPE_BANK,
    	                     CreditorList.CREDITOR_TYPE_NONBANK}));
      if(col == 10)
        return new CurrencyCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                      "Currency", m_conn, m_sessionid);
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void updateCreditorList(CreditorList bank, int row) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        bank, bank.getCreditorType(), bank.getName(), bank.getAddress(), bank.getProvince(), bank.getCountry(),
        bank.getContact(), bank.getPhone(), bank.getBankAccount(),
        bank.getBankName(), bank.getCurrency(), bank.getBankAddress()
      });

      this.getSelectionModel().setSelectionInterval(row, row);
    }
  }

  /**
   *
   */
  class BankAccountTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if((m_new || m_edit)&& row == m_editedIndex)
        return true;
      return false;
    }

    public void setValueAt(Object obj, int row, int col) {
      if(obj instanceof Unit) {
        super.setValueAt(new Unit((Unit)obj, Unit.CODE), row, col);
      }
      else
        super.setValueAt(obj, row, col);
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