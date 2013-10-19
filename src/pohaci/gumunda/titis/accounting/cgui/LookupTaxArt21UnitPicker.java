package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupTaxArt21UnitPicker extends LookupPicker{
	private static final long serialVersionUID = 1L;
	String ATR_JOURNAL;
	private ArrayList accList;  
	public LookupTaxArt21UnitPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Unit Tax Art 21 Account");
		setSize(800, 300);
		initData();		
	}
	  
	void initData() {
		getModel().addColumn("No");
		getModel().addColumn("Account");
		getModel().addColumn("Description");
		getModel().addColumn("Normal Balance");

		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			JournalStandardSetting[] jss = logic.getJournalStandardSetting(
					m_sessionid, IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT,
					IConstants.PAYROLL_PAYMENT_TAX21_UNIT);
			JournalStandard journalStandard;
			JournalStandardAccount[] JSAccount;
			accList = new ArrayList();
			int k = 0;
			for (int j = 0; j < jss.length; j++) {
				journalStandard = (JournalStandard) jss[j].getJournalStandard();
				JSAccount = logic.getJournalStandardAccount(m_sessionid,
						IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT,
						journalStandard.getIndex());
				for (int i = 0; i < JSAccount.length; i++) {
					if (!JSAccount[i].isHidden()) {
						if (!inList(JSAccount[i])) {
							accList.add(JSAccount[i]);
							getModel().addRow(
									new Object[] { new Integer(k + 1),
											JSAccount[i].getAccount().getCode(),
											JSAccount[i],
											JSAccount[i].getBalanceAsString() });
							k = k + 1;
						}
					}
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private boolean inList(JournalStandardAccount account) {
		Iterator iterator = accList.iterator();
		while (iterator.hasNext()) {
			JournalStandardAccount jsa = (JournalStandardAccount) iterator
					.next();

			if ((jsa.getAccount().getIndex() == account.getAccount().getIndex())
					&& (jsa.getAccount().getCode().equals(account.getAccount()
							.getCode()))
					&& (jsa.getBalance() == account.getBalance()))
				return true;
		}
		return false;
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 2);
			setObject(obj);
		}
	}
}
