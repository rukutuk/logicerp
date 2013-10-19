package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupOtherPaymentPicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ATR_JOURNAL;

	public LookupOtherPaymentPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Others Type");
		setSize(800, 300);
		initData();
	}

	// Perubahan biar yang tampil cuma satu jurna aja pada pickernya
	public LookupOtherPaymentPicker(Connection conn, long sessionid,
			String atr_journal) {
		super(conn, sessionid, "Others Type");
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
					"Others Payment");
			JournalStandard journalStandard;
			JournalStandardAccount[] JSAccount;
			int k = 0;
			for (int j = 0; j < jss.length; j++) {
				//if (jss[j].getAttribute().equalsIgnoreCase(ATR_JOURNAL)) {
					journalStandard = (JournalStandard) jss[j].getJournalStandard();
					JSAccount = logic.getJournalStandardAccount(m_sessionid,
							IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT, journalStandard
									.getIndex());
					for (int i = 0; i < JSAccount.length; i++) {
						Account account = JSAccount[i].getAccount();
						account.setBalance(JSAccount[i].getBalance());
						/*if (JSAccount[i].getBalanceAsString().equalsIgnoreCase(
								"Debit"))*/
							getModel().addRow( new Object[] { new Integer(k + 1),
											   account.getCode(), account,
											   JSAccount[i].getBalanceAsString() });
						k = k + 1;
					}
				//}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
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
