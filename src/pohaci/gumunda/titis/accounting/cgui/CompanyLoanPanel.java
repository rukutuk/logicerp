package pohaci.gumunda.titis.accounting.cgui;

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
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.DblCellEditor;

public class CompanyLoanPanel extends JPanel implements ActionListener  {
 
	private static final long serialVersionUID = 1L;
BankLoanTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  CompanyLoan m_loan = null;

  public CompanyLoanPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    m_table = new BankLoanTable();
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
		  CompanyLoan[] loans = logic.getAllCompanyLoan(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
		  
		  for(int i = 0; i < loans.length;  i ++) {
			  CompanyLoan loan = loans[i];
			  loan.isetType(CompanyLoan.CODE);
			  
			  //String creditorCode=loan[i].getCreditorList().getCode()+" "+loan[i].getCreditorList().getName();
			  model.addRow(new Object[]{
					  new CreditorList(loan.getCreditorList(), CreditorList.CODE_NAME), loan, loan.getName(), loan.getCurrency(), 
					  new Double(loan.getInitial()),
					  loan.getAccount(), loan.getUnit(), loan.getRemarks(),
					  loan
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
    model.setValueAt(new Double(0.0), m_editedIndex, 4);
    m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();m_table.getColumnCount();
    m_loan = (CompanyLoan)m_table.getValueAt(m_editedIndex, 1);

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
    CreditorList creditor = null;
    Currency currency = null;
    String code = "", name = "";
    double initial = 0.0;
    Account account = null;
    Unit unit = null;
    String remarks = "";

    int col = 0;

    Object object = model.getValueAt(m_editedIndex, col++);
    /*if(object instanceof CompanyLoan)
      creditor = ((CompanyLoan)object).getCreditorList();
    else
      creditor = (CreditorList)object;

    if(creditor == null)
      list.add("Creditor Code");*/
    
    if (object == null){
    	list.add("Creditor Code");
    } else {
    	creditor = (CreditorList) object;
    }

    object = model.getValueAt(m_editedIndex, col++);
    if (object instanceof CompanyLoan)
    	code = ((CompanyLoan)object).getCode();
    else
    	code = (String) object;
    
    if(code == null || code.equals(""))
      list.add("Loan Code");

    name = (String)model.getValueAt(m_editedIndex, col++);
    if(name == null || name.equals(""))
      list.add("Company Loan Name");

    object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof Currency)
      currency = (Currency)object;
    else
      list.add("Currency");

    initial = ((Double)model.getValueAt(m_editedIndex, col++)).doubleValue();

    object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof Account)
      account = (Account)object;
    else
      list.add("Account");

    object = model.getValueAt(m_editedIndex, col++);
    if(object instanceof Unit)
      unit = (Unit)object;
    else
      list.add("Unit");

    remarks = (String)model.getValueAt(m_editedIndex, col++);

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
        CompanyLoan loan = logic.createBankLoan(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new CompanyLoan(creditor, code,
            name, currency, initial, account, unit, remarks));
        m_table.updateCompanyLoan(loan, m_editedIndex);
      }
      else if(m_edit){
        CompanyLoan loan = logic.updateCompanyLoan(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_loan.getIndex(),
            new CompanyLoan(creditor, code, name, currency, initial, account, unit, remarks));
        m_table.updateCompanyLoan(loan, m_editedIndex);
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
      m_table.updateCompanyLoan(m_loan, m_editedIndex);
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
    CompanyLoan loan = (CompanyLoan)model.getValueAt(m_editedIndex, 1);

    try{
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteCompanyLoan(m_sessionid, IDBConstants.MODUL_MASTER_DATA, loan.getIndex());
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


  class BankLoanTable extends JTable {
 	private static final long serialVersionUID = 1L;

	public BankLoanTable() {
      BankLoanTableModel model = new BankLoanTableModel();
      model.addColumn("Creditor Code");
      model.addColumn("Loan Code");
      model.addColumn("Company Loan Name");
      model.addColumn("Currency");
      model.addColumn("Initial Loan");
      model.addColumn("Account");
      model.addColumn("Unit");
      model.addColumn("Remarks");
      setModel(model);

      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 0)
        return new CreditorListCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Creditor List", m_conn, m_sessionid);
      else if(col == 3)
        return new CurrencyCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Currency", m_conn, m_sessionid);
      else if(col == 4)
        return new DblCellEditor();
      else if(col == 5)
        return new AccountTreeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            m_conn, m_sessionid);
      else if(col == 6)
        return new UnitCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Unit", m_conn, m_sessionid);
      return new BaseTableCellEditor();
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
      if(col == 4)
        return new DoubleCellRenderer(JLabel.RIGHT);
      return super.getCellRenderer(row, col);
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void updateCompanyLoan(CompanyLoan loan, int row) {
    	loan.isetType(CompanyLoan.CODE);
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        new CreditorList(loan.getCreditorList(), CreditorList.CODE_NAME), loan, loan.getName(), loan.getCurrency(), new Double(loan.getInitial()),
        loan.getAccount(), loan.getUnit(), loan.getRemarks(),loan
      });

      this.getSelectionModel().setSelectionInterval(row, row);
    }
  }

 
  class BankLoanTableModel extends DefaultTableModel implements ListSelectionListener {
 	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if((m_new || m_edit)&& row == m_editedIndex)
        return true;
      return false;
    }

    public void setValueAt(Object obj, int row, int col) {
      if(obj instanceof CreditorList) {
        super.setValueAt(new CreditorList((CreditorList)obj, CreditorList.CODE_NAME), row, col);
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