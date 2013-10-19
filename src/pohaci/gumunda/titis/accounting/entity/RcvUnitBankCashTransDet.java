package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;

public class RcvUnitBankCashTransDet {
	RcvUnitBankCashTrns rcvUnitBankCashTrns;
	Account account;
	double attrValue;
	Currency currency;
	double exchangeRate;
	public RcvUnitBankCashTransDet()
	{

	}
	public RcvUnitBankCashTransDet(RcvUnitBankCashTrns rcvUnitBankCashTrns, Account account, double attrValue, Currency currency, double exchangeRate) {
		super();
		this.rcvUnitBankCashTrns = rcvUnitBankCashTrns;
		this.account = account;
		this.attrValue = attrValue;
		this.currency = currency;
		this.exchangeRate = exchangeRate;
	}
	/*
 CREATE TABLE "SAMPURNAUSER"."RCVUNITBANKCASHTRNSDET"
(
	"RCVUNITBANKCASHTRNS"  Fixed (38,0),
	"ACCOUNT"  Fixed (38,0),
	"ATTRVALUE"  Fixed (38,2),
	"CURRENCY"  Fixed (38,0),
	"EXCHANGERATE"  Fixed (38,2),
	FOREIGN KEY "RCVUNITBANKCASHTRNS_RCVUNITBANKC"	("RCVUNITBANKCASHTRNS") REFERENCES "SAMPURNAUSER"."RCVUNITBANKCASHTRNS" ("AUTOINDEX") ON DELETE  CASCADE,
	FOREIGN KEY "ACCOUNT_RCVUNITBANKCASHTRNSDET"	("ACCOUNT") REFERENCES "SAMPURNAUSER"."ACCOUNT" ("AUTOINDEX") ON DELETE  RESTRICT,
	FOREIGN KEY "CURRENCY_RCVUNITBANKCASHTRNSDET"	("CURRENCY") REFERENCES "SAMPURNAUSER"."CURRENCY" ("AUTOINDEX") ON DELETE  RESTRICT
)
 */
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public double getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(double attrValue) {
		this.attrValue = attrValue;
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
	public RcvUnitBankCashTrns getRcvUnitBankCashTrns() {
		return rcvUnitBankCashTrns;
	}
	public void setRcvUnitBankCashTrns(RcvUnitBankCashTrns rcvUnitBankCashTrns) {
		this.rcvUnitBankCashTrns = rcvUnitBankCashTrns;
	}
}
