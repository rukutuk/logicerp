/**
 * 
 */
package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;

/**
 * @author dark-knight
 *
 */
public class TransactionPostedDetail {
	private Account account = null;
	private double value = 0.0;
	private double rate = 0.0;
	private Currency currency;
	private long transid = -1;
	private Unit unit = null;
	private long subsidiary;

	public TransactionPostedDetail(Account account, double value, Currency currency,
			double rate, Unit unit, long subsidiaryAccount) {
		this.account = (Account) account;
		this.value = value;
		if (currency != null)
			this.currency = currency;
		this.rate = rate;
		this.unit = unit;
		this.subsidiary = subsidiaryAccount;
	}	
	
	

	public TransactionPostedDetail(long transactionPostedId, Account account, 
			double value, Currency currency,
			double rate,
			Unit unit, long subsidiaryAccount) {
		this.account = (Account) account;
		this.value = value;
		if (currency != null)
			this.currency = currency;
		this.rate = rate;
		transid = transactionPostedId;
		this.unit = unit;
		this.subsidiary = subsidiaryAccount;
	}
	
	public TransactionPostedDetail(long transactionPostedId, TransactionPostedDetail detail) {
		transid = transactionPostedId;
		account = detail.getAccount();
		value = detail.getValue();
		currency = detail.getCurrency();
		rate = detail.getExchangeRate();	
		this.unit = detail.getUnit();
		this.subsidiary = detail.getSubsidiaryAccount();
	}
	
	public long getTransactionPostedId() {
		return transid;
	}
	
	public Account getAccount() {
		return account;
	}

	public double getValue() {
		return value;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getExchangeRate() {
		return rate;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setValue(double value) {
		this.value = value;

	}



	public long getSubsidiaryAccount() {
		return subsidiary;
	}



	public void setSubsidiaryAccount(long subsidiary) {
		this.subsidiary = subsidiary;
	}



	public Unit getUnit() {
		return unit;
	}



	public void setUnit(Unit unit) {
		this.unit = unit;
	}
}
