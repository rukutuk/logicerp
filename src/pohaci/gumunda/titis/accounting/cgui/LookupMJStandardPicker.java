package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import javax.swing.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;

public class LookupMJStandardPicker extends LookupPicker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LookupMJStandardPicker(Connection conn, long sessionid) {
		    super(conn, sessionid, "Lookup Memorial Jurnal Standard");
		    initData();
		    setSize(800, 300);
		  }

		  void initData() {
		    getModel().addColumn("No.");		    
		    getModel().addColumn("Journal Code");
		    getModel().addColumn("Memorial Jurnal Description");
		    try {
		      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);		      
		      JournalStandard[]result = logic.getSubJournalStandard(m_sessionid, IDBConstants.MODUL_MASTER_DATA,103);

		      for(int i = 0; i < result.length;i++) {		    	  
		        getModel().addRow(new Object[]{new Integer(i+1),result[i].getCode(), result[i]	
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
		      setObject(getModel().getValueAt(rowindex, 2));
		    }
		  }
		}

