package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;

public class RcvOthersDetail {
	
	Account account;
	RcvOthers receiveothers;
	String description;
	long m_subsidiAry;	
	double attrValue;
	Currency currency;
	double exchangerate;


	public RcvOthersDetail() {
	}

	public Currency getCurrency() {
		return currency;
	}


	public void setCurrency(Currency currency) {
		this.currency = currency;
	}


	public double getExchangerate() {
		return exchangerate;
	}


	public void setExchangerate(double exchangerate) {
		this.exchangerate = exchangerate;
	}


	public double getaccValue() {
		return attrValue;
	}


	public void setaccValue(double attrValue) {
		this.attrValue = attrValue;
	}

	public RcvOthers getReceiveothers() {
		return receiveothers;
	}

	public void setReceiveothers(RcvOthers receiveothers) {
		this.receiveothers = receiveothers;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public long getSubsidiAry() {
		return m_subsidiAry;
	}

	public void setSubsidiAry(long subsidiAry) {
		m_subsidiAry = subsidiAry;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



}
