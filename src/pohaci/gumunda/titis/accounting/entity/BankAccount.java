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

public class BankAccount extends Bank implements Comparable, CashBankAccount {
	protected Account m_account = null;
	protected boolean m_cekToString = false;
	
	public BankAccount(String code, String name, String accountNo, Currency currency,
			String address, String contact, String phone, Unit unit, Account account) {
		m_code = code;
		m_name = name;
		m_accountNo = accountNo;
		m_currency = currency;
		m_address = address;
		m_contact = contact;
		m_phone = phone;
		m_unit = unit;
		m_account = account;
	}
	
	public BankAccount(long index, String code, String name, String accountNo, Currency currency,
			String address, String contact, String phone, Unit unit, Account account) {
		m_index = index;
		m_code = code;
		m_name = name;
		m_accountNo = accountNo;
		m_currency = currency;
		m_address = address;
		m_contact = contact;
		m_phone = phone;
		m_unit = unit;
		m_account = account;
	}
	
	public BankAccount(long index, String code, String name, String accountNo, Currency currency,
			String address, String contact, String phone, Unit unit, Account account, boolean cekToString) {
		m_index = index;
		m_code = code;
		m_name = name;
		m_accountNo = accountNo;
		m_currency = currency;
		m_address = address;
		m_contact = contact;
		m_phone = phone;
		m_unit = unit;
		m_account = account;
		m_cekToString = cekToString;
	}
	
	public BankAccount(long index, BankAccount bank) {
		m_index = index;
		m_code = bank.getCode();
		m_name = bank.getName();
		m_accountNo = bank.getAccountNo();
		m_currency = bank.getCurrency();
		m_address = bank.getAddress();
		m_contact = bank.getContact();
		m_phone = bank.getPhone();
		m_unit = bank.getUnit();
		m_account = bank.getAccount();
	}
	public BankAccount(){
		
	}
	public Account getAccount() {
		return m_account;
	}
	
	public int compareTo(Object o) {
		if (o==null)
			return 0;
		if (this.getClass().isAssignableFrom(o.getClass()))
		{
			BankAccount bacc = (BankAccount) o;
			return this.m_account.compareTo(bacc.m_account);
		}
		return 0;
	}
	public String toString() {
		if (m_cekToString){
			return m_code+" "+m_name;
		}		
		return m_code;//+" "+m_name+" "+m_account;
		
	}
	
	public void setAccount(Account account) {
		m_account = account;
	}

	private static BankAccount m_nullObject;
	public static BankAccount nullObject() {
		if (m_nullObject==null)
			m_nullObject = new BankAccount("-","-","",Currency.nullObject(),"","","",Unit.nullObject(),Account.nullObject());
		return m_nullObject;
	}
}