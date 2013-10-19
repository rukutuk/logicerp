package pohaci.gumunda.titis.accounting.cgui;

import java.text.NumberFormat;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class TransactionDetail{
	protected Account m_account = null;
	protected double m_value = 0.0;
	protected double m_rate = 0.0;
	private Currency currency;
	private  Transaction transaction;
	private long subsidiaryAccount;
	private Unit unit;

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public TransactionDetail(Account account, double value, Currency currency, double rate, Unit unit, long subsidiaryAccount) {
		m_account = (Account) account;
		m_value = value;
		if(currency != null)
			this.currency = currency;
		m_rate = rate;
		this.unit = unit;
		this.subsidiaryAccount = subsidiaryAccount;
	}

	public TransactionDetail(Account account, Transaction trans, double value, Currency currency, double rate, Unit unit, long subsidiaryAccount) {
		m_account = (Account) account;
		m_value = value;
		if(currency != null)
			this.currency = currency;
		m_rate = rate;
		this.transaction = trans;
		this.unit = unit;
		this.subsidiaryAccount = subsidiaryAccount;
	}

	public TransactionDetail(Transaction trans, TransactionDetail detail) {
		transaction = trans;
		m_account = detail.getAccount();
		m_value = detail.getValue();
		currency = detail.getCurrency();
		m_rate = detail.getExchangeRate();
		this.unit = detail.getUnit();
		this.subsidiaryAccount = detail.getSubsidiaryAccount();
	}

	public Account getAccount() {
		return m_account;
	}

	public void setSubsidiaryAccount(long subsidiaryAccount) {
		this.subsidiaryAccount = subsidiaryAccount;
	}

	public double getValue() {
		return m_value;
	}

	public Currency getCurrency(){
		return currency;
	}

	public double getExchangeRate() {
		return m_rate;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setValue(double value) {
		m_value = value;

	}
	//ubah ama cuk gung eh di add euuuy
	public void setAccount(Account acc){
		m_account=acc;
	}
	static NumberFormat defaultFormat = NumberFormat.getInstance();
	public String toString() {
		return m_account.toStringWithCode() + " " + currency + " " + defaultFormat.format(m_value) + "(rate:" + m_rate + ")";
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public long getSubsidiaryAccount() {
		return subsidiaryAccount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		TransactionDetail newDetail = new TransactionDetail(getTransaction(), this);

		return newDetail;
	}


}
