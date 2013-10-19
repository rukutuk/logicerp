package pohaci.gumunda.titis.accounting.cgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupBankAccountPicker extends LookupPicker {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 982779572618583471L;
	Unit m_unit=null;
	
	public LookupBankAccountPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Bank Account");
		initData();
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}
	
	public LookupBankAccountPicker(Connection conn, long sessionid,Unit unit) {
		super(conn, sessionid, "Lookup Bank Account");
		m_unit = unit;
		initData();
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}
	
	public LookupBankAccountPicker(Connection conn, long sessionid,Currency currency) {
		super(conn, sessionid, "Lookup Bank Account");
		initData(currency);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}
	
	public LookupBankAccountPicker(Connection conn, long sessionid,Currency currency,Unit unit) {
		super(conn, sessionid, "Lookup Bank Account");
		m_unit=unit;
		initData(currency);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}
	
	public LookupBankAccountPicker(Connection conn, long sessionid,JournalStandard js) {
		super(conn, sessionid, "Lookup Bank Account");
		initData(js);
		setSize(800, 300);
		hapusActionListenerBrowse();
		m_browseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
	}
	
	public LookupBankAccountPicker(Connection conn, long sessionid,String application) {
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
	
	public LookupBankAccountPicker(Connection conn, long sessionid,Unit unit,String application) {
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
		setColumn();
		try {
			JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
					m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);
			List journalStdList = helper
			.getJournalStandardSettingWithAccount(IConstants.PAYROLL_PAYMENT_TAX21_HO);
			Iterator iterator = journalStdList.iterator();
			Vector vector = new Vector();
			while (iterator.hasNext()) {
				JournalStandardSetting jss = (JournalStandardSetting) iterator.next();
				JournalStandard js = jss.getJournalStandard();
				JournalStandardAccount[] accts = js.getJournalStandardAccount();
				for (int i = 0; i < accts.length; i++) {
					if (!vector.contains(new Long(accts[i].getAccount().getIndex())))
						vector.add(new Long(accts[i].getAccount().getIndex()));
				}
			}
			int no = 1;
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			BankAccount[] result = logic.getAllBankAccount(m_sessionid,IDBConstants.MODUL_MASTER_DATA);
			for (int i = 0; i < result.length; i++) {
				if (vector.contains(new Long(result[i].getAccount().getIndex())))
					if (m_unit!=null){
						if (m_unit.getIndex()==result[i].getUnit().getIndex())
							getModel().addRow(new Object[] { new Integer(no++),
									result[i], result[i].getName(),
									result[i].getAccountNo(),
									result[i].getCurrency(),
									result[i].getAddress(),
									result[i].getUnit(),
									result[i].getAccount() });
					}else{
						getModel().addRow(new Object[] { new Integer(no++),
								result[i], result[i].getName(),
								result[i].getAccountNo(),
								result[i].getCurrency(),
								result[i].getAddress(),
								result[i].getUnit(),
								result[i].getAccount() });
					}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void initData(Currency currency) {
		setColumn();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			BankAccount[] result = logic.getAllBankAccount(m_sessionid,IDBConstants.MODUL_MASTER_DATA);
			int no = 1;
			for (int i = 0; i < result.length; i++) {
				if (currency != null && m_unit!=null) {
					if (result[i].getCurrency().getIndex() == currency.getIndex() && m_unit.getIndex()==result[i].getUnit().getIndex()) {
						getModel().addRow(new Object[] { new Integer((no++)),// new
										result[i], result[i].getName(),
										result[i].getAccountNo(),
										result[i].getCurrency(),
										result[i].getAddress(),
										result[i].getUnit(),
										result[i].getAccount() });
					}
				} 
				else if (currency == null && m_unit!=null) {
					if (m_unit.getIndex()==result[i].getUnit().getIndex()) {
						getModel().addRow(new Object[] { new Integer((no++)),// new
										result[i], result[i].getName(),
										result[i].getAccountNo(),
										result[i].getCurrency(),
										result[i].getAddress(),
										result[i].getUnit(),
										result[i].getAccount() });
					}					
				} 
				else if (currency != null && m_unit==null) {
					if (result[i].getCurrency().getIndex() == currency.getIndex()) {
						getModel().addRow(new Object[] { new Integer((no++)),// new
										result[i], result[i].getName(),
										result[i].getAccountNo(),
										result[i].getCurrency(),
										result[i].getAddress(),
										result[i].getUnit(),
										result[i].getAccount() });
					}					
				} 
				else {
					getModel()
					.addRow(new Object[] { new Integer((no++)),// new
									// Integer(i+1),
									result[i], result[i].getName(),
									result[i].getAccountNo(),
									result[i].getCurrency(),
									result[i].getAddress(),
									result[i].getUnit(),
									result[i].getAccount() });
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void initData(JournalStandard js) {
		setColumn();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			GenericMapper mapper = MasterMap.obtainMapperFor(JournalStandardAccount.class);
			mapper.setActiveConn(m_conn);
			List rs = mapper.doSelectWhere(IDBConstants.ATTR_JOURNAL + "="+ js.getIndex());
			JournalStandardAccount jsAccount;
			Iterator iterator = rs.iterator();
			Vector vector = new Vector();
			while (iterator.hasNext()) {
				jsAccount = (JournalStandardAccount) iterator.next();
				vector.add(new Long(jsAccount.getAccount().getIndex()));
			}
			int no = 1;
			BankAccount[] result = logic.getAllBankAccount(m_sessionid,IDBConstants.MODUL_MASTER_DATA);
			for (int i = 0; i < result.length; i++) {
				if (vector.contains(new Long(result[i].getAccount().getIndex())))
					getModel().addRow(new Object[] { new Integer(no++),
							result[i], result[i].getName(),
							result[i].getAccountNo(),
							result[i].getCurrency(),
							result[i].getAddress(),
							result[i].getUnit(),
							result[i].getAccount() });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void initData() {
		setColumn();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			BankAccount[] result = logic.getAllBankAccount(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			for (int i = 0; i < result.length; i++) {
				if (m_unit!=null){
					if (m_unit.getIndex()==result[i].getUnit().getIndex())
						getModel().addRow(new Object[] { new Integer(i + 1), result[i],
								result[i].getName(), result[i].getAccountNo(),
								result[i].getCurrency(),
								result[i].getAddress(), result[i].getUnit(),
								result[i].getAccount() });
				}else{
					getModel().addRow(new Object[] { new Integer(i + 1), result[i],
							result[i].getName(), result[i].getAccountNo(),
							result[i].getCurrency(),
							result[i].getAddress(), result[i].getUnit(),
							result[i].getAccount() });
				}
				
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/*void initData(Unit unit) {
	 setColumn();
	 try {
	 AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
	 BankAccount[] result = logic.getAllBankAccount(m_sessionid,IDBConstants.MODUL_MASTER_DATA);
	 for (int i = 0; i < result.length; i++) {
	 if (unit.getIndex()==result[i].getUnit().getIndex())
	 getModel().addRow(new Object[] { new Integer(i + 1), result[i],
	 result[i].getName(), result[i].getAccountNo(),
	 result[i].getCurrency(),
	 result[i].getAddress(), result[i].getUnit(),
	 result[i].getAccount() });
	 }
	 } catch (Exception ex) {
	 JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
	 JOptionPane.WARNING_MESSAGE);
	 }
	 }*/
	
	private void setColumn() {
		getModel().addColumn("No.");
		getModel().addColumn("Code");
		getModel().addColumn("Bank Name");
		getModel().addColumn("Bank Account No.");
		getModel().addColumn("Currency");
		getModel().addColumn("Address");
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
	
	protected String getCaption(Object obj) {	
		if (obj instanceof BankAccount) {
			BankAccount bankAcct = (BankAccount) obj;
			String a, b, c;
			if (bankAcct.getCode() == null)
				a = "";
			else
				a = bankAcct.getCode();
			if (bankAcct.getName() == null)
				b = "";
			else
				b = bankAcct.getName();
			if (bankAcct.getUnit().getDescription() == null)
				c = "";
			else
				c = bankAcct.getUnit().getDescription();
			return a + " " + b + " " + c;			
		}
		return "";
		//BankAccount bankAcct = (BankAccount) obj;
		
	}
}
