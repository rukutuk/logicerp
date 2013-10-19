package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupEspenseSheetPicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String ATR_JOURNAL;

	public LookupEspenseSheetPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Project Cost Type");
		setSize(800, 300);
		initData();

	}

	public LookupEspenseSheetPicker(Connection conn, long sessionid,
			String atr_journal) {
		super(conn, sessionid, "Expense Sheet Account");
		setSize(800, 300);
		ATR_JOURNAL = atr_journal;
		initData();

	}

	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Account");
		getModel().addColumn("Description");
		getModel().addColumn("Normal Balance");

		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			JournalStandardSetting[] jss = logic.getJournalStandardSetting(
					m_sessionid, IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT,
					ATR_JOURNAL);
			JournalStandard temp;
			JournalStandardAccount[] result;
			int k = 0;
			for (int j = 0; j < jss.length; j++) {
				//	if (jss[j].getAttribute().equalsIgnoreCase(ATR_JOURNAL)) {
				temp = (JournalStandard) jss[j].getJournalStandard();
				result = logic.getJournalStandardAccount(m_sessionid,
						IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT, temp
								.getIndex());
				for (int i = 0; i < result.length; i++) {
					Account temp1 = result[i].getAccount();
					temp1.setBalance(result[i].getBalance());
					if (!result[i].isHidden())
						getModel().addRow(
								new Object[] {
								//new Integer(k+1),temp1.getCode(),temp1.getName(),temp1.getBalanceAsString(),temp1
										new Integer(k + 1), temp1.getCode(),
										temp1, temp1.getBalanceAsString() });
					k = k + 1;
				}
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 2);
			setObject(obj);
		}
	}

	protected String getCaption(Object obj) {
		Account Acct = (Account) obj;
		return Acct.getName() == null ? "" : Acct.getName();
	}

}
