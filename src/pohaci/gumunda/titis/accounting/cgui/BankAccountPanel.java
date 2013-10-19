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
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class BankAccountPanel extends JPanel implements ActionListener  {
  private static final long serialVersionUID = 1L;
  BankAccountTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  BankAccount m_bank = null;

  public BankAccountPanel(Connection conn, long sessionid) {
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
      BankAccount[] bank = logic.getAllBankAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < bank.length;  i ++) {
    	model.addRow(new Object[]{
        bank[i], bank[i].getName(), bank[i].getAccountNo(), bank[i].getCurrency(),
        bank[i].getAddress(), bank[i].getContact(), bank[i].getPhone(),
        bank[i].getUnit(), bank[i].getAccount()
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
    m_bank = (BankAccount)m_table.getValueAt(m_editedIndex, 0);

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

    String code = "", name = "", accountno = "";
    String address = "", contact = "", phone = "";
    Currency currency = null;
    Unit unit = null;
    Account account = null;

    int col = 0;

    Object object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof BankAccount)
      code = ((BankAccount)object).getCode();
    else
      code = (String)object;

    if(code == null || code.equals(""))
      list.add("Code");

    name = (String)model.getValueAt(m_editedIndex, col++);
    if(name == null || name.equals(""))
      list.add("Bank Name");

    accountno = (String)model.getValueAt(m_editedIndex, col++);
    if(accountno == null || accountno.equals(""))
      list.add("Bank Account No.");

    object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof Currency)
      currency = (Currency)object;
    else
      list.add("Currency");

    address = (String)model.getValueAt(m_editedIndex, col++);
    contact = (String)model.getValueAt(m_editedIndex, col++);
    phone = (String)model.getValueAt(m_editedIndex, col++);

    object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof Unit)
      unit = (Unit)object;
    else
      list.add("Unit Code");

    object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof Account)
      account = (Account)object;
    else
      list.add("Account");

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
        BankAccount bank = logic.createBankAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new BankAccount(code, name,
            accountno, currency, address, contact, phone, unit, account));
        m_table.updateBankAccount(bank, m_editedIndex);
      }
      else if(m_edit){
        BankAccount bank = logic.updateBankAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_bank.getIndex(),
            new BankAccount(code, name, accountno, currency, address, contact, phone, unit, account));
        m_table.updateBankAccount(bank, m_editedIndex);
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
      m_table.updateBankAccount(m_bank, m_editedIndex);
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
    BankAccount bank = (BankAccount)model.getValueAt(m_editedIndex, 0);

    try{
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteBankAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA, bank.getIndex());
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
      model.addColumn("Code");
      model.addColumn("Bank Name");
      model.addColumn("Bank Account No.");
      model.addColumn("Currency");
      model.addColumn("Address");
      model.addColumn("Contact");
      model.addColumn("Phone");
      model.addColumn("Unit Code");
      model.addColumn("Account");
      setModel(model);

      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 3)
        return new CurrencyCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                  "Currency", m_conn, m_sessionid);
      else if(col == 7)
        return new UnitCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                  "Unit Code", m_conn, m_sessionid);
      else if(col == 8)
        return new AccountTreeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            m_conn, m_sessionid);
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void updateBankAccount(BankAccount bank, int row) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        bank, bank.getName(), bank.getAccountNo(), bank.getCurrency(),
        bank.getAddress(), bank.getContact(), bank.getPhone(),
        bank.getUnit(), bank.getAccount()
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
        super.setValueAt(new Unit((Unit)obj, Unit.CODE_DESCRIPTION), row, col);
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