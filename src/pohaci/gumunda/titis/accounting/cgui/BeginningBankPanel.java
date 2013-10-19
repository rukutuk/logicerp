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

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBankDetail;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.cgui.*;

public class BeginningBankPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JButton m_viewBt, m_editBt, m_saveBt, m_cancelBt, m_submitBt;
  UnitPicker m_unitPicker;
  DatePicker m_datePicker;
  JournalPicker m_journalPicker;
  BeginningTable m_table;
  boolean m_edit = false;

  BeginningBalance m_balance = null;

  public BeginningBankPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    initComponents();
  }

  void constructComponent() {
    m_unitPicker = new UnitPicker(m_conn, m_sessionid);
    m_datePicker = new DatePicker();
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

    Misc.setGridBagConstraints(northPanel, new JLabel("Unit Code"), gridBagConstraints,
                               0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(northPanel, new JLabel(" "), gridBagConstraints,
                               1, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
    Misc.setGridBagConstraints(northPanel, m_unitPicker, gridBagConstraints,
                               2, 0, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);

    Misc.setGridBagConstraints(northPanel, new JLabel("Date"), gridBagConstraints,
                               0, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(northPanel, new JLabel(" "), gridBagConstraints,
                               1, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
    Misc.setGridBagConstraints(northPanel, m_datePicker, gridBagConstraints,
                               2, 1, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);

    Misc.setGridBagConstraints(northPanel, new JLabel("Journal"), gridBagConstraints,
                               0, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
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
    m_datePicker.setDate(new java.util.Date());

  }

  void view() {
    Unit unit = (Unit)m_unitPicker.getObject();
    if(unit == null) {
      JOptionPane.showMessageDialog(this, "Please insert:\nUnit Code");
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      m_balance = logic.getBeginningBalance(m_sessionid, IDBConstants.MODUL_ACCOUNTING,
          BeginningBalanceSheetDetail.typeFromStringToID(BeginningBalanceSheetDetail.BANK));

      long index = 0;
      if(m_balance != null) {
        index = m_balance.getIndex();
        //TransactionDetail[] detail = logic.getBeginningBalanceSheet(m_sessionid, IDBConstants.MODUL_ACCOUNTING,
        //    index, Account.categoryFromStringToID(Account.STR_CATEGORY_1));
        //m_balance.setBeginningBalanceDetail(detail);
      }

      BeginningBankDetail[] view = logic.getBeginningBankView(m_sessionid, IDBConstants.MODUL_ACCOUNTING,
          index, unit.getIndex());
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
//        BeginningBalanceSheetDetail.typeFromStringToID(BeginningBalanceSheetDetail.BANK));
//    try {
//     // balance.setBeginningBankDetail(m_table.getBeginningBankDetail());
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
      submit();
    }
  }

  /**
   *
   */
  class BeginningTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BeginningTable() {
      BeginningTableModel model = new BeginningTableModel();
      model.addColumn("Code");
      model.addColumn("Bank Name");
      model.addColumn("Bank Account No.");
      model.addColumn("Currency");
      model.addColumn("Account");
      model.addColumn("Value");
      model.addColumn("Exchange Rate");
      setModel(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col >= 5)
        return new DblCellEditor();
      return new BaseTableCellEditor();
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
      if(col >= 5)
        return new DoubleCellRenderer(JLabel.RIGHT);
      return super.getCellRenderer(row, col);
    }

    void setBeginningDetail(BeginningBankDetail[] detail) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < detail.length; i ++)
        model.addRow(new Object[]{
      detail[i], detail[i].bankAccountName(), detail[i].bankAccountNo(),
      detail[i].getCurrency(), detail[i].getAccount(),
      new Double(detail[i].getAccValue()), new Double(detail[i].getExchangeRate())
      });
    }

    BeginningBankDetail[] getBeginningBankDetail() {
      java.util.Vector vresult = new java.util.Vector();
      //Currency currency = null;
      for(int i = 0; i < getRowCount(); i ++) {
        BeginningBankDetail bank = (BeginningBankDetail)getValueAt(i, 0);
        double value = ((Double)getValueAt(i, 5)).doubleValue();
        double rate = ((Double)getValueAt(i, 6)).doubleValue();

        if(value > 0.0) {
          bank.setAccValue(value);
          bank.setExchangeRate(rate);
          vresult.addElement(bank);
        }
      }

      BeginningBankDetail[] result = new BeginningBankDetail[vresult.size()];
      vresult.copyInto(result);
      return result;
    }
  }

  /**
   *
   */
  class BeginningTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col <= 4)
        return false;
      if(m_edit)
        return true;
      return false;
    }

    /*public void setValueAt(Object objvalue, int rowindex, int columnindex){
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
    }*/
  }
}