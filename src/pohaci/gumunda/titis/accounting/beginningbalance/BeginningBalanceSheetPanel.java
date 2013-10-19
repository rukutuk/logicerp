package pohaci.gumunda.titis.accounting.beginningbalance;

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

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.CurrencyPicker;
import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.cgui.JournalPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.cgui.*;

public class BeginningBalanceSheetPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JButton m_viewBt, m_editBt, m_saveBt, m_cancelBt, m_submitBt;
  DatePicker m_datePicker;
  CurrencyPicker m_currencyPicker;
  JournalPicker m_journalPicker;
  BeginningTable m_table;

  boolean m_edit = false;
  BeginningBalance m_balance = null;  //  @jve:decl-index=0:

  public BeginningBalanceSheetPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    initComponents();
   // initData();
  }

  void constructComponent() {
    m_datePicker = new DatePicker();
    m_currencyPicker = new CurrencyPicker(m_conn, m_sessionid);
    m_journalPicker = new JournalPicker(m_conn, m_sessionid);
    m_table = new BeginningTable();

    m_viewBt = new JButton("View");
    m_viewBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    m_submitBt = new JButton("Submit");
    m_submitBt.addActionListener(this);


    JPanel northPanel = new JPanel();
    northPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    Misc.setGridBagConstraints(northPanel, new JLabel("Date"), gridBagConstraints,
                               0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(northPanel, new JLabel(" "), gridBagConstraints,
                               1, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
    Misc.setGridBagConstraints(northPanel, m_datePicker, gridBagConstraints,
                               2, 0, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);

    Misc.setGridBagConstraints(northPanel, new JLabel("Currency"), gridBagConstraints,
                               0, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(northPanel, new JLabel(" "), gridBagConstraints,
                               1, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
    Misc.setGridBagConstraints(northPanel, m_currencyPicker, gridBagConstraints,
                               2, 1, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);

    Misc.setGridBagConstraints(northPanel, new JLabel("Journal"), gridBagConstraints,
                               0, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(0, 1, 1, 1));
    Misc.setGridBagConstraints(northPanel, new JLabel(" "), gridBagConstraints,
                               1, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
    Misc.setGridBagConstraints(northPanel, m_journalPicker, gridBagConstraints,
                               2, 2, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);

    Misc.setGridBagConstraints(northPanel, m_viewBt, gridBagConstraints,
                               3, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(0, 6, 1, 1) );
    Misc.setGridBagConstraints(northPanel, m_editBt, gridBagConstraints,
                               4, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(0, 1, 1, 1) );
    Misc.setGridBagConstraints(northPanel, m_saveBt, gridBagConstraints,
                               5, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(0, 1, 1, 1) );
    Misc.setGridBagConstraints(northPanel, m_cancelBt, gridBagConstraints,
                               6, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(0, 1, 1, 1) );
    Misc.setGridBagConstraints(northPanel, m_submitBt, gridBagConstraints,
                               7, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(0, 1, 1, 1) );


    setLayout(new BorderLayout(0, 5));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    add(northPanel, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  void initComponents() {
    m_viewBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_submitBt.setEnabled(true);
    m_currencyPicker.setEnabled(false);

    m_datePicker.setDate(new java.util.Date());
  }

  void initData() {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      m_currencyPicker.setObject(logic.getBaseCurrency(m_sessionid, IDBConstants.MODUL_ACCOUNTING));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
  }

  void view() {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      m_balance = logic.getBeginningBalance(m_sessionid, IDBConstants.MODUL_ACCOUNTING,
          BeginningBalanceSheetDetail.typeFromStringToID(BeginningBalanceSheetDetail.BALANCE_SHEET));

      long index = 0;
      if(m_balance != null) {
        index = m_balance.getIndex();
        //TransactionDetail[] detail = logic.getBeginningBalanceSheet(m_sessionid, IDBConstants.MODUL_ACCOUNTING,
        //  index, Account.categoryFromStringToID(Account.STR_CATEGORY_1));
        //m_balance.setBeginningBalanceDetail(detail);
      }

      BeginningBalanceSheetEntry[] view = logic.getBeginningBalanceSheetView(m_sessionid, IDBConstants.MODUL_ACCOUNTING,
          index, Account.categoryFromStringToID(Account.STR_CATEGORY_1));
       m_table.setBeginningDetail(view);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void edit() {
    m_edit = true;
    m_viewBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_submitBt.setEnabled(false);
  }

  void save() {
//    BeginningBalance balance = new BeginningBalance(m_datePicker.getDate(),
//        BeginningBalanceSheetDetail.typeFromStringToID(BeginningBalanceSheetDetail.BALANCE_SHEET));
//    try {
//      balance.setBeginningBalancSheeteDetail(m_table.getBeginningBalanceSheetDetail(m_currencyPicker));
//    }
//    catch(Exception ex) {
//      JOptionPane.showMessageDialog(this, ex.getMessage(),
//                                    "Information", JOptionPane.INFORMATION_MESSAGE);
//      return;
//    }
//
//    try {
//      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
//      if(m_balance == null)
//        m_balance = logic.createBeginningBalance(m_sessionid, IDBConstants.MODUL_ACCOUNTING, balance);
//      else
//        m_balance = logic.updateBeginningBalance(m_sessionid, IDBConstants.MODUL_ACCOUNTING, m_balance.getIndex(), balance);
//    }
//    catch(Exception ex) {
//      JOptionPane.showMessageDialog(this, ex.getMessage(),
//                                    "Warning", JOptionPane.WARNING_MESSAGE);
//      return;
//    }
//
    m_edit = false;
    m_viewBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_submitBt.setEnabled(true);
  }

  void cancel() {

  }

  void submit() {
    if(m_balance.getTrans() != null) {
      JOptionPane.showMessageDialog(this, "Balance Sheet has been submit");
      return;
    }

    Journal journal = (Journal)m_journalPicker.getObject();
    if(journal == null) {
      JOptionPane.showMessageDialog(this, "Please insert:\n Journal");
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.submitBeginningBalance(m_sessionid, IDBConstants.MODUL_ACCOUNTING,
                                   m_datePicker.getDate(), journal, m_balance);
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_viewBt) {
      view();
    }
    else if(e.getSource() == m_editBt) {
      edit();
    }
    else if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
    }
    else if(e.getSource() == m_submitBt) {
      //submit();
    }
  }

  /**
   *
   */
  static class BeginningTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BeginningTable() {
      BeginningTableModel model = new BeginningTableModel();
      model.addColumn("Account");
      model.addColumn("Debit");
      model.addColumn("Credit");
      setModel(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col != 0)
        return new DblCellEditor();
      return new BaseTableCellEditor();
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
      if(col != 0)
        return new DoubleCellRenderer(JLabel.RIGHT);
      return super.getCellRenderer(row, col);
    }

    void setBeginningDetail(BeginningBalanceSheetEntry[] detail) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < detail.length; i ++) {
        if(detail[i].getAccValue() > 0.0)
          model.addRow(new Object[]{detail[i], new Double(detail[i].getAccValue()), new Double(0.0)});
        else
          model.addRow(new Object[]{detail[i], new Double(0.0), new Double(Math.abs(detail[i].getAccValue()))});
      }
    }

//    private BeginningBalanceSheetEntry[] getBeginningBalanceSheetDetail(CurrencyPicker currencyPicker) {
//      java.util.Vector vresult = new java.util.Vector();
//      //Currency currency = (Currency)currencyPicker.getObject();
//      double rate = 0.0;
//
//      for(int i = 0; i < getRowCount(); i ++) {
//        double value = 0.0;
//
//        BeginningBalanceSheetEntry sheet = (BeginningBalanceSheetEntry)getValueAt(i, 0);
//        double dvalue = ((Double)getValueAt(i, 1)).doubleValue();
//        double cvalue = ((Double)getValueAt(i, 2)).doubleValue();
//
//        if(dvalue > 0.0)
//          value = dvalue;
//        else if(cvalue > 0.0)
//          value = -cvalue;
//
//        if(value != 0.0) {
//          //sheet.setCurrency(currency);
//          sheet.setAccValue(value);
//          sheet.setExchangeRate(rate);
//          vresult.addElement(sheet);
//        }
//      }
//
//      BeginningBalanceSheetEntry[] result = new BeginningBalanceSheetEntry[vresult.size()];
//      vresult.copyInto(result);
//      return result;
//    }
  }

  /**
   *
   */
  static class BeginningTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      //if(m_edit)
      //  return true;
      return false;
    }

    public void setValueAt(Object objvalue, int rowindex, int columnindex){
      if(columnindex == 1){
        Double dvalue = (Double)objvalue;
        if(dvalue.doubleValue() >= 0.0 ){
          super.setValueAt(dvalue, rowindex, columnindex);
          super.setValueAt(new Double(0.0), rowindex, 2);
        }
      }
      else if(columnindex == 2){
        Double dvalue = (Double)objvalue;
        if(dvalue.doubleValue() >= 0.0 ){
          super.setValueAt(dvalue, rowindex, columnindex);
          super.setValueAt(new Double(0.0), rowindex, 1);
        }
      }
      else
        super.setValueAt(objvalue, rowindex, columnindex);
    }
  }
}