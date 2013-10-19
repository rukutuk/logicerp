package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class PayrollPmtTax21UnitDet {
	
	PayrollPmtTax21Unit m_payrollPmtTax21Unit;
	Account m_account;
	double m_attrValue;
	Currency m_currency;
	double m_exchangeRate;
	int balanceCode;
	Organization department;
	long subsidiAry;
	
	public long getSubsidiAry() {
		return subsidiAry;
	}

	public void setSubsidiAry(long subsidiAry) {
		this.subsidiAry = subsidiAry;
	}

	public int getBalanceCode() {
		return balanceCode;
	}

	public void setBalanceCode(int balanceCode) {
		this.balanceCode = balanceCode;
	}

	public PayrollPmtTax21UnitDet(){		
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

	public double getExchangeRate() {
		return m_exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		m_exchangeRate = exchangeRate;
	}

	public PayrollPmtTax21Unit getPayrollPmtTax21Unit() {
		return m_payrollPmtTax21Unit;
	}

	public void setPayrollPmtTax21Unit(PayrollPmtTax21Unit payrollPmtTax21Unit) {
		m_payrollPmtTax21Unit = payrollPmtTax21Unit;
	}

	public Currency getCurrency() {
		return m_currency;
	}

	public void setCurrency(Currency currency) {
		m_currency = currency;
	}

	public Organization getDepartment() {
		return department;
	}

	public void setDepartment(Organization department) {
		this.department = department;
	}
	
}	
