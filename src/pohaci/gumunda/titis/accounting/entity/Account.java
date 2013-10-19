package pohaci.gumunda.titis.accounting.entity;

import java.io.Serializable;

import pohaci.gumunda.titis.accounting.cgui.SimpleAccount;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dwx
 * @version 1.0
 */

public class Account extends SimpleAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final short CODE = 0;

	public static final short DESCRIPTION = 1;

	public static final short CODE_DESCRIPTION = 2;

	public static final String STR_CATEGORY_1 = "ASSETS";

	public static final String STR_CATEGORY_2 = "LIABILITIES";

	public static final String STR_CATEGORY_3 = "EQUITY";

	public static final String STR_CATEGORY_4 = "INCOME";

	public static final String STR_CATEGORY_5 = "EXPENSE";

	public static final String STR_CATEGORY_6 = "OTHERS";

	public static String m_categories[] = new String[] { STR_CATEGORY_1,
			STR_CATEGORY_2, STR_CATEGORY_3, STR_CATEGORY_4, STR_CATEGORY_5,
			STR_CATEGORY_6 };

	public static final String STR_DEBET = "Debit";

	public static final String STR_CREDIT = "Credit";

	public static String m_balances[] = new String[] { STR_DEBET, STR_CREDIT };

	protected short m_category = -1;

	protected boolean m_group = false;

	protected short m_balance = -1;

	protected String m_note = "";

	protected Account m_parent = null;

	protected short m_status = -1;

	protected String m_path = "";

	public Account(String code, String name, short category, boolean group,
			short balance, String note, String path) {
		m_code = code;
		m_name = name;
		m_category = category;
		m_group = group;
		m_balance = balance;
		m_note = note;
		m_path = path;
	}

	public Account(long index, String code, String name, short category,
			boolean group, short balance, String note, String path) {
		m_index = index;
		m_code = code;
		m_name = name;
		m_category = category;
		m_group = group;
		m_balance = balance;
		m_note = note;
		m_path = path;
	}

	public Account(long index, Account account) {
		m_index = index;
		m_code = account.getCode();
		m_name = account.getName();
		m_category = account.getCategory();
		m_group = account.isGroup();
		m_balance = account.getBalance();
		m_note = account.getNote();
		m_path = account.getTreePath();
	}

	public Account(Account account, short status) {
		this(account.getIndex(), account);
		m_status = status;
	}

	public Account() {
	}

	public static String[] getCategories() {
		return m_categories;
	}

	public short getCategory() {
		return m_category;
	}

	public String getCategoryAsString() {
		if (m_category < 0 || m_category >= m_categories.length)
			return "";
		else
			return m_categories[m_category];
	}

	public static short categoryFromStringToID(String category) {
		short len = (short) m_categories.length;
		for (short i = 0; i < len; i++) {
			if (m_categories[i].equals(category))
				return i;
		}

		return -1;
	}

	public boolean isGroup() {
		return m_group;
	}

	public short getBalance() {
		return m_balance;
	}

	public String getBalanceAsString() {
		if (m_balance < 0 || m_balance >= m_balances.length)
			return "";
		else
			return m_balances[m_balance];
	}

	public static short balanceFromStringToID(String balance) {
		short len = (short) m_balances.length;
		for (short i = 0; i < len; i++) {
			if (m_balances[i].equals(balance))
				return i;
		}

		return -1;
	}

	public String getNote() {
		return m_note;
	}

	public void setParent(Account account) {
		m_parent = account;
	}

	public Account getParent() {
		return m_parent;
	}

	public String toString() {
		if (m_status == DESCRIPTION)
			return m_name;
		return toStringWithCode();
	}

	public String toStringWithCode() {
		return m_name + " [" + m_code + "]";
	}

	public void setBalance(short a) {
		m_balance = a;
	}

	private static Account m_nullObject;

	public static Account nullObject() {
		if (m_nullObject == null)
			m_nullObject = new Account("?", "?", (short) 0, false, (short) 0,
					"", "");
		return m_nullObject;
	}

	public String getTreePath() {
		//if (isGroup())
		//	return m_path + "/";
		return m_path;
	}

	public void setTreePath(String p) {
		m_path = p;
	}

	public void isetStatus(short m_status) {
		this.m_status = m_status;
	}

	public boolean equals(Object obj) {
		if(obj instanceof Account){
			if((this.getIndex()==0)||(((Account)obj).getIndex()==0))
				return false;
			return ((Account)obj).getIndex()==this.getIndex();
		}
		return false;
	}
}