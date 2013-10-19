package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupOperationalCostTypePicker extends LookupPicker {
	
	private static final long serialVersionUID = 1L;
	String ATR_JOURNAL;

	public LookupOperationalCostTypePicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Project Cost Type");
		setSize(800, 300);
		initData();
	}

	public LookupOperationalCostTypePicker(Connection conn, long sessionid,	String atr_journal) {
		super(conn, sessionid, "Operational Cost Type");
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
					"Operational Cost Payment");
			JournalStandard journalStandard;
			JournalStandardAccount[] jsAccount;
			int k = 0;
			for (int j = 0; j < jss.length; j++) {
				if (jss[j].getAttribute().equalsIgnoreCase(ATR_JOURNAL)) {
					journalStandard = (JournalStandard) jss[j].getJournalStandard();
					jsAccount = logic.getJournalStandardAccount(m_sessionid,IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT, journalStandard.getIndex());
					for (int i = 0; i < jsAccount.length; i++) {
						Account account = jsAccount[i].getAccount();
						account.setBalance(jsAccount[i].getBalance());
						if (!jsAccount[i].isHidden())
							getModel().addRow(new Object[]{new Integer(k+1),account.getCode(),jsAccount[i],jsAccount[i].getBalanceAsString()});
						k = k + 1;
					}
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
		JournalStandardAccount jsAcc = (JournalStandardAccount)obj;
		return jsAcc.getAccount().getName() ==null ?"":jsAcc.getAccount().getName();	
	}
}
