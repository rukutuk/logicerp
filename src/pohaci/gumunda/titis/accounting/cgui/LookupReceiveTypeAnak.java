package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import javax.swing.JOptionPane;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupReceiveTypeAnak extends LookupPicker {
	private static final long serialVersionUID = 1L;
	JournalStandard js;
	/*public LookupReceiveTypeAnak(Connection conn, long sessionid,Object obj) {
		super(conn, sessionid, "Loan Receive Type");
		setSize(800, 300);
		js=(JournalStandard)obj;
		initData();
	}*/
	
	public LookupReceiveTypeAnak(Connection conn, long sessionid,JournalStandard jStandard,String title) {
		super(conn, sessionid, title);
		setSize(800, 300);
		js=jStandard;
		initData();
	}
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Account");
		getModel().addColumn("Description");
		getModel().addColumn("Normal Balance");      
		
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			JournalStandardAccount[] result;
			int k=0;
			result=logic.getJournalStandardAccountNonCalHidd(m_sessionid,IDBConstants.MODUL_MASTER_DATA,js.getIndex());
			for(int i = 0; i < result.length;  i ++) {
				getModel().addRow(new Object[]{new Integer(i+1),result[i].getAccount().getCode(),
						result[i],result[i].getBalanceAsString()});				
				k=k+1;
			}
		}catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 2);
			setObject(obj);
		}
	}
	
	protected String getCaption(Object obj){
		JournalStandardAccount journal = (JournalStandardAccount) obj;
		return journal.getAccount().getName() == null ? "" : journal.getAccount().getName();
	}
}
