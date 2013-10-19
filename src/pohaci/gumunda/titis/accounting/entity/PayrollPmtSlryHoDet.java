package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.Transaction;

public class PayrollPmtSlryHoDet {
	PayrollPmtSlryHo m_payrollPmtSlryHo;
	Account m_account;
	double m_attrValue;
	Currency m_currency;
	double m_exChangeRate;	
	String description;
	Date postedDate;
	Transaction trans;
	
	public PayrollPmtSlryHoDet(){
		
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
	public double getExChangeRate() {
		return m_exChangeRate;
	}
	public void setExChangeRate(double exChangeRate) {
		m_exChangeRate = exChangeRate;
	}
	public PayrollPmtSlryHo getPayrollPmtSlryHo() {
		return m_payrollPmtSlryHo;
	}
	public void setPayrollPmtSlryHo(PayrollPmtSlryHo payrollPmtSlryHo) {
		m_payrollPmtSlryHo = payrollPmtSlryHo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	public Transaction getTrans() {
		return trans;
	}
	public void setTrans(Transaction trans) {
		this.trans = trans;
	}

}
