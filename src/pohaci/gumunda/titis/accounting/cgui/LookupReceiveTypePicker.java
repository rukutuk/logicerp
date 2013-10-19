package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

public class LookupReceiveTypePicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LookupReceiveTypePicker(Connection conn, long sessionid) {
	    super(conn, sessionid, "Lookup Receive Type");
	    initData();
	    setSize(800, 300);
	  }

	  void initData() {
	    getModel().addColumn("No.");
	    getModel().addColumn("Receive Code");
	    getModel().addColumn("Receive Description");
	    getTable().getColumnModel().getColumn(2).setPreferredWidth(50);
	    
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


