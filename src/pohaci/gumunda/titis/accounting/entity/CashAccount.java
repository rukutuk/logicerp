package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.Currency;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class CashAccount implements Comparable, CashBankAccount {
	protected long m_index = 0;
	protected String m_code = "";
	protected String m_name = "";
	protected Currency m_currency = null;
	protected Unit m_unit = null;
	protected Account m_account = null;
	protected boolean m_cekToString = false;
	
	public CashAccount(String code, String name, Currency currency,  Unit unit, Account account) {
		m_code = code;
		m_name = name;
		m_currency = currency;
		m_unit = unit;
		m_account = account;
	}
	
	public CashAccount(long index, String code, String name, Currency currency, Unit unit, Account account) {
		m_index = index;
		m_code = code;
		m_name = name;
		m_currency = currency;
		m_unit = unit;
		m_account = account;
	}
	
	public CashAccount(long index, String code, String name, Currency currency, Unit unit, 
			Account account,boolean cekToString) {
		m_index = index;
		m_code = code;
		m_name = name;
		m_currency = currency;
		m_unit = unit;
		m_account = account;
		m_cekToString = cekToString;
	}
	
	public CashAccount(long index, CashAccount cash) {
		m_index = index;
		m_code = cash.getCode();
		m_name = cash.getName();
		m_currency = cash.getCurrency();
		m_unit = cash.getUnit();
		m_account = cash.getAccount();
	}
	//tambahan dari cokgung
	public CashAccount(CashAccount cashaccount) {
		this(cashaccount.getIndex(), cashaccount.getCode(), cashaccount.getName(),cashaccount.getCurrency(),cashaccount.getUnit(),cashaccount.getAccount());
	}
	//tambahan dari nunung  
	public CashAccount(){
		
	}
	
	public long getIndex() {
		return m_index;
	}
	
	public String getCode() {
		return m_code;
	}
	
	public String getName() {
		return m_name;
	}
	
	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.cgui.CashBankAccount#getCurrency()
	 */
	public Currency getCurrency() {
		return m_currency;
	}
	
	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.cgui.CashBankAccount#getUnit()
	 */
	public Unit getUnit() {
		if (m_unit!=null)
			return m_unit;
		return null;
	}
	
	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.cgui.CashBankAccount#getAccount()
	 */
	public Account getAccount() {
		return m_account;
	}
	
	public String toString() {    
		if (m_cekToString)
			return m_code + " " + m_name;
		return m_code;
		
	}
	
	public int compareTo(Object o) {
		if (o==null)
			return 0;
		if (this.getClass().isAssignableFrom(o.getClass())){
			CashAccount acc = (CashAccount) o;
			return this.m_code.compareTo(acc.m_code);
		}
		return 0;
	}
	
	public BankAccount getBankAccount(PmtProjectCost cost) {
		return cost.bankAccount;
	}
	
	public void setAccount(Account account) {
		m_account = account;
	}
	
	public void setCode(String code) {
		m_code = code;
	}
	
	public void setCurrency(Currency currency) {
		m_currency = currency;
	}
	
	public void setIndex(long index) {
		m_index = index;
	}
	
	public void setName(String name) {
		m_name = name;
	}
	
	public void setUnit(Unit unit) {
		m_unit = unit;
	}
	static CashAccount m_nullObject;
	public static CashAccount nullObject() {
		if (m_nullObject==null)
			m_nullObject = new CashAccount("-","-",null,null,Account.nullObject());
		return m_nullObject;
	}
}
