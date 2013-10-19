package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupCreditListPicker extends LookupPicker {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public LookupCreditListPicker(Connection conn, long sessionid) {
    super(conn, sessionid, "Lookup Credit List");
    initData();
    setSize(800, 300);
  }

  void initData() {
    getModel().addColumn("No.");
    getModel().addColumn("Creditor Code");
    getModel().addColumn("Creditor Name");
    getModel().addColumn("Address");
    getModel().addColumn("Bank Account No.");
    getModel().addColumn("Bank Name");
    getModel().addColumn("Currency");
    getModel().addColumn("Bank Account Address");

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      CreditorList[] result = logic.getAllCreditorList(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < result.length;  i ++) {
        getModel().addRow(new Object[]{
        String.valueOf((i + 1)),
        result[i], result[i].getName(), result[i].getAddress(),
        result[i].getBankAccount(), result[i].getBankName(),
        result[i].getCurrency(), result[i].getBankAddress()
      });
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void select() {
    int rowindex = m_table.getSelectedRow();
    if(rowindex != -1) {
      setObject(getModel().getValueAt(rowindex, 1));
    }
  }
}