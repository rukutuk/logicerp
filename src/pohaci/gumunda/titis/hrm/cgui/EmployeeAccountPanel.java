package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.titis.accounting.cgui.AccountTreeCellEditor;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class EmployeeAccountPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  AccountTable m_table;
  AccountDetailTable m_detailtable;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;
  JComboBox m_currencyComboBox = new JComboBox();

  boolean m_editable = false;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  EmployeeAccount m_account = null;

  public EmployeeAccountPanel(Connection conn, long sessionid) {
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

    m_table = new AccountTable();
    scrollpane.getViewport().add(m_table);
    scrollpane.setPreferredSize(new Dimension(350, 113));

    m_detailtable = new AccountDetailTable();

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

  void reloadCurrencyCombo() {
    m_currencyComboBox.removeAllItems();

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Currency[] currency = logic.getAllCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < currency.length; i ++)
        m_currencyComboBox.addItem(currency[i]);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
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
    m_account = (EmployeeAccount)m_detailtable.getValueAt(m_editedIndex, 0);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    EmployeeAccount account = null;
    try {
      account = m_table.getAccount();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    if(m_new)
      m_detailtable.addAccount(account);
    else
      m_detailtable.updateAccount(account, m_editedIndex);

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
    m_detailtable.deleteAccount(m_editedIndex);
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
        m_table.setAccount(m_account);
      }
      else {
        m_editBt.setEnabled(false);
        m_deleteBt.setEnabled(false);
      }
    }
    else if(m_edit) {
      m_edit = false;
      m_detailtable.updateAccount(m_account, m_editedIndex);

      m_addBt.setEnabled(true);
      m_editBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
      m_deleteBt.setEnabled(true);
    }
  }

  public EmployeeAccount[] getAccount() {
    return m_detailtable.geAccount();
  }

  public void setAccount(EmployeeAccount[] account) {
    m_new = false; m_edit = false;
    m_table.clearTable();
    m_detailtable.setAccount(account);
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
  class AccountTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountTable() {
      AccountTableModel model = new AccountTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
      model.addRow(new Object[]{"Account Name", ""});
      model.addRow(new Object[]{"Account No", ""});
      model.addRow(new Object[]{"Bank Name", ""});
      model.addRow(new Object[]{"Bank Address", ""});
      model.addRow(new Object[]{"Currency", ""});
      model.addRow(new Object[]{"Remark", ""});
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public TableCellEditor getCellEditor(int row, int col) {
        System.out.println("aaaaa");
      if(row == 4 && col == 1) {
	//return new AccountTreeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn, m_sessionid);
        reloadCurrencyCombo();
        return new DefaultCellEditor(m_currencyComboBox);
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

    public EmployeeAccount getAccount() throws Exception {
      stopCellEditing();

      java.util.ArrayList list = new java.util.ArrayList();
      String accountname = "", accountno = "",
      bankname = "", bankaddress = "", remark = "";
      Currency currency = null;

      int row = 0;
      accountname = (String)getValueAt(row++, 1);
      if(accountname == null || accountname.equals(""))
        list.add("Account Name");

      accountno = (String)getValueAt(row++, 1);
      if(accountno == null || accountno.equals(""))
        list.add("Account No");

      bankname = (String)getValueAt(row++, 1);
      if(bankname == null || bankname.equals(""))
        list.add("Bank Name");

      bankaddress = (String)getValueAt(row++, 1);

      Object obj = getValueAt(row++, 1);
      if(obj instanceof Currency)
        currency = (Currency)obj;
      else
        list.add("Currency");

      remark = (String)getValueAt(row++, 1);

      String strexc = "Please insert description of :\n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int i = 0; i < exception.length; i ++)
          strexc += " - " + exception[i] + "\n";
        throw new Exception(strexc);
      }

      return new EmployeeAccount(accountname, accountno, bankname,
                                 bankaddress, currency, remark);
    }

    public void setAccount(EmployeeAccount account) {
      int row = 0;

      setValueAt(account.getAccountName(), row++, 1);
      setValueAt(account.getAccountNo(), row++, 1);
      setValueAt(account.getBankName(), row++, 1);
      setValueAt(account.getBankAddress(), row++, 1);
      setValueAt(account.getCurrency(), row++, 1);
      setValueAt(account.getRemark(), row++, 1);
    }
  }

  /**
   *
   */
  class AccountTableModel extends DefaultTableModel {
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
  class AccountDetailTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountDetailTable() {
      AccountDetailTableModel model = new AccountDetailTableModel();
      model.addColumn("Account Name");
      model.addColumn("Account No");
      model.addColumn("Bank Name");
      model.addColumn("Bank Address");
      model.addColumn("Currency");
      model.addColumn("Remark");

      setModel(model);
      getSelectionModel().addListSelectionListener(model);
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }

    void addAccount(EmployeeAccount account) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        account, account.getAccountNo(), account.getBankName(),
        account.getBankAddress(), account.getCurrency(), account.getRemark()
      });

      m_editedIndex = getRowCount() -1;
      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void updateAccount(EmployeeAccount account, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        account, account.getAccountNo(), account.getBankName(),
        account.getBankAddress(), account.getCurrency(), account.getRemark()
      });

      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void deleteAccount(int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
    }

    public EmployeeAccount[] geAccount() {
      Vector vresult = new Vector();
      for(int i = 0; i < getRowCount(); i ++)
        vresult.addElement((EmployeeAccount)getValueAt(i, 0));

      EmployeeAccount[] result = new EmployeeAccount[vresult.size()];
      vresult.copyInto(result);
      return result;
    }

    public void setAccount(EmployeeAccount[] account) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      m_editedIndex = -1;
      for(int i = 0; i < account.length; i ++) {
        model.addRow(new Object[]{
        account[i], account[i].getAccountNo(), account[i].getBankName(),
        account[i].getBankAddress(), account[i].getCurrency(), account[i].getRemark()
      });
      }
    }
  }

  /**
   *
   */
  class AccountDetailTableModel extends DefaultTableModel implements ListSelectionListener {
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

            m_account = (EmployeeAccount)getValueAt(iRowIndex, 0);
            m_table.setAccount(m_account);
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