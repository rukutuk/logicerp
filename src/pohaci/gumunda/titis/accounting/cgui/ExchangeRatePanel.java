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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.ExchangeRate;
import pohaci.gumunda.titis.accounting.helper.ExchangeRateHelper;
import pohaci.gumunda.titis.application.DblCellEditor;

public class ExchangeRatePanel extends JPanel implements ActionListener  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
ExchangeRateTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  ExchangeRate m_exchange = null;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  
  private Currency baseCurrency;
  private ExchangeRateHelper helper;

  public ExchangeRatePanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    
    setBaseCurrency(conn, sessionid);
    helper = new ExchangeRateHelper(this.m_conn, this.m_sessionid);

    constructComponent();
    initData();
  }

  private void setBaseCurrency(Connection conn, long sessionid) {
	this.baseCurrency = BaseCurrency.createBaseCurrency(conn, sessionid);
  }

void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new ExchangeRateTable();
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
      ExchangeRate[] exchange = logic.getAllExchangeRate(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < exchange.length;  i ++) {
        model.addRow(new Object[]{
        exchange[i], exchange[i].getBaseCurrency(), new Double(exchange[i].getExchangeRate()),
        m_dateformat.format(exchange[i].getValidFrom()), m_dateformat.format(exchange[i].getValidTo())
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
    
    ExchangeRate rate = helper.getExchangeRateWithLastValidTo();
    String validFrom = null;
    if(rate!=null){
    	Calendar calendar = Calendar.getInstance(Locale.getDefault());
    	calendar.setTime(rate.getValidTo());
    	calendar.add(Calendar.DATE, 1);
    	Date from = calendar.getTime();
    	validFrom = m_dateformat.format(from);
    }
    
    model.addRow(new Object[]{null, this.baseCurrency, null, validFrom, null});
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
    m_exchange = (ExchangeRate)m_table.getValueAt(m_editedIndex, 0);

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

    Currency reference = null, base = null;
    double amount = 0.0;
    Date validFrom = null, validTo = null;

    int col = 0;

    Object object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof ExchangeRate)
      reference = ((ExchangeRate)object).getReferenceCurrency();
    else
      reference = (Currency)object;

    if(reference == null)
      list.add("Reference Currency");

    base = (Currency)model.getValueAt(m_editedIndex, col++);
    if(base == null)
      list.add("Base Currency");

    amount = ((Double)model.getValueAt(m_editedIndex, col++)).doubleValue();

    String strfrom = (String)model.getValueAt(m_editedIndex, col++);
    if(strfrom == null || strfrom.equals(""))
      list.add("Valid From");

    String strto = (String)model.getValueAt(m_editedIndex, col++);
    if(strto == null || strto.equals(""))
      list.add("Valid To");

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
      validFrom = m_dateformat.parse(strfrom);
      validTo = m_dateformat.parse(strto);
      if(validFrom.compareTo(validTo)>0)
    	  throw new Exception("Valid From > ValidTo");
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      
      if(isConflicted(m_exchange, reference, base, validFrom, validTo))
    	  throw new Exception("This exchange rate data is conflicted to others."); 

      if(m_new){
    	ExchangeRate exchange = logic.createExchangeRate(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
            new ExchangeRate(reference, base, amount, validFrom, validTo));
        m_table.updateExchangeRate(exchange, m_editedIndex);
      }
      else if(m_edit){
        ExchangeRate exchange = logic.updateExchangeRate(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_exchange.getIndex(),
            new ExchangeRate(reference, base, amount, validFrom, validTo));
        m_table.updateExchangeRate(exchange, m_editedIndex);
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

  private boolean isConflicted(ExchangeRate rate, Currency reference, Currency base, Date validFrom, Date validTo) {
	  List list = helper.getConflictedExchangeRate(rate, reference, base, validFrom, validTo);
	  
	  if(list.size()==0)
		  return false;
	  
	  return true;
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
      m_table.updateExchangeRate(m_exchange, m_editedIndex);
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
    ExchangeRate exchange = (ExchangeRate)model.getValueAt(m_editedIndex, 0);

    try{
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteExchangeRate(m_sessionid, IDBConstants.MODUL_MASTER_DATA, exchange.getIndex());
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
  class ExchangeRateTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExchangeRateTable() {
      ExchangeRateTableModel model = new ExchangeRateTableModel();
      model.addColumn("Reference Currency");
      model.addColumn("Base Currency");
      model.addColumn("Exchange Rate");
      model.addColumn("Valid From");
      model.addColumn("Valid To");
      setModel(model);

      getSelectionModel().addListSelectionListener(model);
      getColumnModel().getColumn(2).setCellRenderer(new DoubleCellRenderer(JLabel.RIGHT));
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 0) // yang base currency i matiin
        return new CurrencyCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                      "Currency", m_conn, m_sessionid);
      else if(col == 2)
        return new DblCellEditor();
      else if(col == 3 || col == 4)
        return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

	public void updateExchangeRate(ExchangeRate exchange, int row) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        exchange, exchange.getBaseCurrency(), new Double(exchange.getExchangeRate()),
        m_dateformat.format(exchange.getValidFrom()), m_dateformat.format(exchange.getValidTo())
      });
      this.getSelectionModel().setSelectionInterval(row, row);
    }
  }

  /**
   *
   */
  class ExchangeRateTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if((m_new || m_edit)&& row == m_editedIndex){
    	  if(col==1)
    		  return false;
    	  return true;
      }
      return false;
    }

    public void setValueAt(Object obj, int row, int col) {
      if(obj instanceof Currency) {
        super.setValueAt(new Currency((Currency)obj, Currency.SYMBOL), row, col);
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