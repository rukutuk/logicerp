package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class MemJournalStrdDet {	
	MemJournalStrd m_memJournalStrd;
	Account m_account;
	long m_subsidiAry;
	double m_accValue;
	String m_balanceCode;
	Currency m_currency;
	double m_exchangeRate;	
	Organization department;
	public MemJournalStrdDet(){
		
	}
	public Account getAccount() {
		return m_account;
	}
	public void setAccount(Account account) {
		m_account = account;
	}
	public double getAccValue() {
		return m_accValue;
	}
	public void setAccValue(double accValue) {		
		m_accValue = accValue;
	}
	public String getBalanceCode() {
		return m_balanceCode;
	}
	public void setBalanceCode(String balanceCode) {
		m_balanceCode = balanceCode;	
	}
	public Currency getCurrency() {
		return m_currency;
	}
	public void setCurrency(Currency currency) {
		m_currency = currency;
	}
	public double getExchangeRate() {
		return m_exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		m_exchangeRate = exchangeRate;
	}
	public MemJournalStrd getMemJournalStrd() {
		return m_memJournalStrd;
	}
	public void setMemJournalStrd(MemJournalStrd memJournalStrd) {
		m_memJournalStrd = memJournalStrd;
	}
	public long getSubsidiAry() {
		return m_subsidiAry;
	}
	public void setSubsidiAry(long subsidiAry) {
		m_subsidiAry = subsidiAry;
	}
	public Organization getDepartment() {
		return department;
	}
	public void setDepartment(Organization department) {
		this.department = department;
	}

}
