package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

public class LookupEmpARListPicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LookupEmpARListPicker(Connection conn, long sessionid) {
	    super(conn, sessionid, "Lookup Employee Receivable  List");
	    initData();
	    setSize(800, 300);
	  }

	  void initData() {
	    getModel().addColumn("No.");
	    getModel().addColumn("Employee ID");
	    getModel().addColumn("Employee Name");
	    getModel().addColumn("Voucher No");
	    getModel().addColumn("Voucher Date");
	    getModel().addColumn("AR Amount");
	    getModel().addColumn("Approved by");
	    getModel().addColumn("Status");
	    getModel().addColumn("Total Paid");
	//    getTable().getColumnModel().getColumn(0).setPreferredWidth(50);
	    
/*
	    try {
	      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
	      CashAccount[] result = logic.getAllCashAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

	      for(int i = 0; i < result.length;  i ++) {
	        getModel().addRow(new Object[]{
	        String.valueOf((i + 1)),
	        result[i], result[i].getName(), result[i].getCurrency(),
	        result[i].getUnit(), result[i].getAccount()
	      });
	      }
	    }
	    catch (Exception ex) {
	      JOptionPane.showMessageDialog(this, ex.getMessage(),
	                                    "Warning", JOptionPane.WARNING_MESSAGE);
	    }*/
	  }

	  public void select() {
	    int rowindex = m_table.getSelectedRow();
	    if(rowindex != -1) {
	      setObject(getModel().getValueAt(rowindex, 1));
	    }
	  }

}


