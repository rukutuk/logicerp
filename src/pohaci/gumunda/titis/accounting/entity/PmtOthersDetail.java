package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class PmtOthersDetail {
	
	PmtOthers m_paymentOthers;
	Account m_account;
	String m_description;
	long m_subsidiAry;	
	double m_attrValue;
	Currency m_currency;
	double m_exchangeRate;
	Organization department;
	
	public PmtOthersDetail(){
		
	}
	public Account getAccount() {
		return m_account;
	}
	public void setAccount(Account account) {
		m_account = account;
	}
	public double getAccValue() {
		return m_attrValue;
	}
	public void setAccValue(double attrValue) {
		m_attrValue = attrValue;
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
	public PmtOthers getPmtOthers() {
		return m_paymentOthers;
	}
	public void setPmtOthers(PmtOthers paymentOthers) {
		m_paymentOthers = paymentOthers;
	}
	public long getSubsidiAry() {
		return m_subsidiAry;
	}
	public void setSubsidiAry(long subsidiAry) {
		m_subsidiAry = subsidiAry;
	}
	public String getDescription() {
		return m_description;
	}
	public void setDescription(String description) {
		m_description = description;
	}
	public Organization getDepartment() {
		return department;
	}
	public void setDepartment(Organization department) {
		this.department = department;
	}
}
