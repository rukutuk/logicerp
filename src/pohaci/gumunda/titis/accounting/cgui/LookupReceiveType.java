package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupReceiveType extends LookupPicker {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String m_cashbank;
	public LookupReceiveType(Connection conn, long sessionid,String title) {
		super(conn, sessionid, "Loan Receive Type");
		setSize(800, 300);
		initData(title);		
	}
	
	public LookupReceiveType(Connection conn, long sessionid,String title,String cashbank) {
		super(conn, sessionid, "Loan Receive Type");
		setSize(800, 300);
		m_cashbank = cashbank;
		initDataCashBank(title);		
	}
	void initDataCashBank(String title) {
		getModel().addColumn("No.");
		getModel().addColumn("Receive Code");
		getModel().addColumn("Description");
		
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			JournalStandardSetting[] jss=logic.getJournalStandardSetting(m_sessionid, IDBConstants.MODUL_MASTER_DATA,title,getCahsBank());
			JournalStandard temp;			
			int k=0;
			for (int j=0;j< jss.length;j++){
				temp=(JournalStandard)jss[j].getJournalStandard();
				getModel().addRow(new Object[]{
						new Integer(k+1),temp.getCode(),temp.getDescription(),temp});
				k=k+1;
			}
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void initData(String title) {
		getModel().addColumn("No.");
		getModel().addColumn("Receive Code");
		getModel().addColumn("Description");
		
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			JournalStandardSetting[] jss=logic.getJournalStandardSetting(m_sessionid, IDBConstants.MODUL_MASTER_DATA,title);
			JournalStandard temp;			
			int k=0;
			for (int j=0;j< jss.length;j++){
				temp=(JournalStandard)jss[j].getJournalStandard();
				getModel().addRow(new Object[]{
						new Integer(k+1),temp.getCode(),temp.getDescription(),temp});
				k=k+1;
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
			Object obj = getModel().getValueAt(rowindex, 3);
			setObject(obj);
		}
	}
	protected String getCaption(Object obj){
		JournalStandard Acct = (JournalStandard) obj;
		return Acct.getDescription() == null ? "" : Acct.getDescription();
	}
	
	public void setCahsbank(String cashBank){
		m_cashbank = cashBank;
	}
	public String getCahsBank(){
		return m_cashbank;
	}
}
