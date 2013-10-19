package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.Transaction;

public class PayrollPmtTax21HoDet {
	PayrollPmtTax21Ho payrollPmtTax21Ho;
	Account account;
	double accValue;
	Currency currency;
	double exchangeRate;
	Transaction trans;
	Date postedDate;
	String description;

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
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public double getAccValue() {
		return accValue;
	}
	public void setAccValue(double accValue) {
		this.accValue = accValue;
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
	public PayrollPmtTax21Ho getPayrollPmtTax21Ho() {
		return payrollPmtTax21Ho;
	}
	public void setPayrollPmtTax21Ho(PayrollPmtTax21Ho payrollPmtTax21Ho) {
		this.payrollPmtTax21Ho = payrollPmtTax21Ho;
	}
}
