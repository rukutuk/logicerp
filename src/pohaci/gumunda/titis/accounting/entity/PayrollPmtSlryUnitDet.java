package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class PayrollPmtSlryUnitDet {
	
	PayrollPmtSlryUnit m_payrollPmtSlryUnit;
	Account m_account;
	double m_attrValue;
	Currency m_currency;
	double m_exchangeRate;
	int balanceCode;
	Organization department;
	long subsidiAry;

	public PayrollPmtSlryUnitDet(){		
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

	public PayrollPmtSlryUnit getPayrollPmtSlryUnit() {
		return m_payrollPmtSlryUnit;
	}

	public void setPayrollPmtSlryUnit(PayrollPmtSlryUnit payrollPmtSlryUnit) {
		m_payrollPmtSlryUnit = payrollPmtSlryUnit;
	}

	public int getBalanceCode() {
		return balanceCode;
	}

	public void setBalanceCode(int balanceCode) {
		this.balanceCode = balanceCode;
	}

	public long getSubsidiAry() {
		return subsidiAry;
	}

	public void setSubsidiAry(long subsidiAry) {
		this.subsidiAry = subsidiAry;
	}

	public Organization getDepartment() {
		return department;
	}

	public void setDepartment(Organization department) {
		this.department = department;
	}
}
