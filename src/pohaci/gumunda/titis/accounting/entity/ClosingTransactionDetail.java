/**
 * 
 */
package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;

/**
 * @author dark-knight
 * 
 */
public class ClosingTransactionDetail {
	private long autoindex;
	
	private long closingTransaction;
	
	private Account account;

	private double accValue;

	private Currency currency;

	private double exchangeRate;
	
	private int balanceCode;

	public long getAutoindex() {
		return autoindex;
	}

	public void setAutoindex(long autoindex) {
		this.autoindex = autoindex;
	}

	public int getBalanceCode() {
		return balanceCode;
	}

	public void setBalanceCode(int balanceCode) {
		this.balanceCode = balanceCode;
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

	public long getClosingTransaction() {
		return closingTransaction;
	}

	public void setClosingTransaction(long closingTransaction) {
		this.closingTransaction = closingTransaction;
	}
}
