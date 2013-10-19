package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.Transaction;

public class PayrollPmtEmpInsDet {
	PayrollPmtEmpInsurance payrollPmtEmpIns;
	Account account;
	double accValue;
	Currency currency;
	double exchangeRate;
	String description;
	Date postedDate;
	Transaction trans;
	
	public Transaction getTrans() {
		return trans;
	}
	public void setTrans(Transaction trans) {
		this.trans = trans;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public double getAccValue() {
		return accValue;
	}
	public void setAccValue(double attrValue) {
		this.accValue = attrValue;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public PayrollPmtEmpInsurance getPayrollPmtEmpIns() {
		return payrollPmtEmpIns;
	}
	public void setPayrollPmtEmpIns(PayrollPmtEmpInsurance payrollPmtEmpIns) {
		this.payrollPmtEmpIns = payrollPmtEmpIns;
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
}
