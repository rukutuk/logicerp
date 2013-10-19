package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupCashAccountPicker extends LookupPicker {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	int m_iResponse = JOptionPane.NO_OPTION;

	CashAccount[] m_cashAccount;

	public LookupCashAccountPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Cash Account");
		initData();
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}

	public LookupCashAccountPicker(Connection conn, long sessionid,Unit unit) {
		super(conn, sessionid, "Lookup Cash Account");
		initData(unit);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}

	public LookupCashAccountPicker(Connection conn, long sessionid,Currency currency) {
		super(conn, sessionid, "Lookup Cash Account");
		initData(currency);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//done2();
				done();
			}
		});
	}

	public LookupCashAccountPicker(Connection conn, long sessionid,Currency currency,Unit unit) {
		super(conn, sessionid, "Lookup Cash Account");
		m_unit = unit;
		initData(currency);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//done2();
				done();
			}
		});
	}

	public LookupCashAccountPicker(Connection conn, long sessionid,
			JournalStandard journalStandard) {
		super(conn, sessionid, "Lookup Bank Account");
		initData(journalStandard);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}

	public LookupCashAccountPicker(Connection conn, long sessionid, String application) {
		super(conn, sessionid, "Lookup Bank Account");
		initData(application);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}

	Unit m_unit=null;
	public LookupCashAccountPicker(Connection conn, long sessionid,Unit unit, String application) {
		super(conn, sessionid, "Lookup Bank Account");
		m_unit = unit;
		initData(application);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}


	private void initData(String application) {
		if (application == null)
			return;
		setColumns();
		try {
			JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
					m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);

			List journalStdList = helper
			.getJournalStandardSettingWithAccount(IConstants.PAYROLL_PAYMENT_TAX21_HO);

			Iterator iterator = journalStdList.iterator();
			Vector vektor = new Vector();
			while (iterator.hasNext()) {
				JournalStandardSetting jss = (JournalStandardSetting) iterator.next();
				JournalStandard js = jss.getJournalStandard();

				JournalStandardAccount[] accts = js.getJournalStandardAccount();
				for (int i = 0; i < accts.length; i++) {
					if (!vektor.contains(new Long(accts[i].getAccount().getIndex())))
						vektor.add(new Long(accts[i].getAccount().getIndex()));
				}
			}

			int no = 1;
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			CashAccount[] result = logic.getAllCashAccount(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			for (int i = 0; i < result.length; i++) {
				if (vektor.contains(new Long(result[i].getAccount().getIndex()))){
					if (m_unit!=null){
						if (m_unit.getIndex()==result[i].getUnit().getIndex())
							getModel().addRow(new Object[] { new Integer(no++),
									result[i], result[i].getName(),
									result[i].getCurrency(),
									result[i].getUnit(),
									result[i].getAccount() });
					}else{
						getModel().addRow(new Object[] { new Integer(no++),
								result[i], result[i].getName(),
								result[i].getCurrency(),
								result[i].getUnit(),
								result[i].getAccount() });
					}
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	void initData() {
		setColumns();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			m_cashAccount = logic.getAllCashAccount(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			for (int i = 0; i < m_cashAccount.length; i++) {
				getModel().addRow(
						new Object[] { new Integer((i + 1)),//new Integer(i+1),
								m_cashAccount[i], m_cashAccount[i].getName(),
								m_cashAccount[i].getCurrency(),
								m_cashAccount[i].getUnit(),
								m_cashAccount[i].getAccount() });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	void initData(Unit unit) {
		setColumns();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			m_cashAccount = logic.getAllCashAccount(m_sessionid,IDBConstants.MODUL_MASTER_DATA);
			for (int i = 0; i < m_cashAccount.length; i++) {
				if (unit == m_cashAccount[i].getUnit())
					getModel().addRow(new Object[] { new Integer((i + 1)),
							m_cashAccount[i], m_cashAccount[i].getName(),
							m_cashAccount[i].getCurrency(),
							m_cashAccount[i].getUnit(),
							m_cashAccount[i].getAccount()});
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}


	void initData(JournalStandard journalStandard) {
		if(journalStandard==null)
			return;

		setColumns();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			GenericMapper mapper=MasterMap.obtainMapperFor(JournalStandardAccount.class);
			mapper.setActiveConn(m_conn);
			List rs=mapper.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+journalStandard.getIndex());
			JournalStandardAccount jsAccount;

			Iterator iterator = rs.iterator();
			Vector v = new Vector();
			while(iterator.hasNext()){
				jsAccount = (JournalStandardAccount) iterator.next();
				v.add(new Long(jsAccount.getAccount().getIndex()));
			}

			int no = 1;
			CashAccount[] result = logic.getAllCashAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			for(int i = 0; i < result.length;  i ++) {
				if (v.contains(new Long(result[i].getAccount().getIndex())))
					getModel()
					.addRow(
							new Object[] { new Integer(no++),
									result[i], result[i].getName(),
									result[i].getCurrency(),
									result[i].getUnit(),
									result[i].getAccount() });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	//Ini overloading void init data untuk proses filter currency
	void initData(Currency curr) {
		setColumns();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			m_cashAccount = logic.getAllCashAccount(m_sessionid,IDBConstants.MODUL_MASTER_DATA);
			int k = 0;
			for (int i = 0; i < m_cashAccount.length; i++) {
				if (curr!=null && m_unit!=null){
					if (m_cashAccount[i].getCurrency().getIndex() == curr.getIndex() && m_unit.getIndex()==m_cashAccount[i].getUnit().getIndex()){
						getModel().addRow(new Object[]{new Integer((k + 1)),
								m_cashAccount[i],
								m_cashAccount[i].getName(),
								m_cashAccount[i].getCurrency(),
								m_cashAccount[i].getUnit(),
								m_cashAccount[i].getAccount() });
						k = k + 1;
					}
				}
				else if (curr!=null && m_unit==null){
					if (m_cashAccount[i].getCurrency().getIndex() == curr.getIndex()){
						getModel().addRow(new Object[]{new Integer((k + 1)),
								m_cashAccount[i],
								m_cashAccount[i].getName(),
								m_cashAccount[i].getCurrency(),
								m_cashAccount[i].getUnit(),
								m_cashAccount[i].getAccount() });
						k = k + 1;
					}
				}
				else if (curr==null && m_unit!=null){
					if (m_unit.getIndex()==m_cashAccount[i].getUnit().getIndex()){
						getModel().addRow(new Object[]{new Integer((k + 1)),
								m_cashAccount[i],
								m_cashAccount[i].getName(),
								m_cashAccount[i].getCurrency(),
								m_cashAccount[i].getUnit(),
								m_cashAccount[i].getAccount() });
						k = k + 1;
					}
				}
				else{
					getModel().addRow(new Object[]{new Integer((k + 1)),
							m_cashAccount[i],
							m_cashAccount[i].getName(),
							m_cashAccount[i].getCurrency(),
							m_cashAccount[i].getUnit(),
							m_cashAccount[i].getAccount() });
					k = k + 1;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setColumns() {
		getModel().addColumn("No.");
		getModel().addColumn("Code");
		getModel().addColumn("Cash Name");
		getModel().addColumn("Currency");
		getModel().addColumn("Unit Code");
		getModel().addColumn("Account");
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 1);
			setObject(obj);
		}
	}

	public void aturKolom() {
		if (getTable().getColumnModel().getColumnCount()==0)
			return;

		getTable().getColumnModel().getColumn(0).setPreferredWidth(40);
		getTable().getColumnModel().getColumn(0).setMaxWidth(40);
		getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
		getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		getTable().getColumnModel().getColumn(3).setPreferredWidth(60);
		getTable().getColumnModel().getColumn(3).setMaxWidth(60);
		getTable().getColumnModel().getColumn(4).setPreferredWidth(100);
		getTable().getColumnModel().getColumn(5).setPreferredWidth(100);
	}

	protected String getCaption(Object obj) {
		if (obj instanceof CashAccount) {
			CashAccount cashAcct = (CashAccount) obj;
			String a, b, c;
			if (cashAcct.getCode() == null)
				a = "";
			else
				a = cashAcct.getCode();
			if (cashAcct.getName() == null)
				b = "";
			else
				b = cashAcct.getName();
			if (cashAcct.getUnit().getDescription() == null)
				c = "";
			else
				c = cashAcct.getUnit().getDescription();
			return a + " " + b + " " + c;
		}
		return "";
	}
}
