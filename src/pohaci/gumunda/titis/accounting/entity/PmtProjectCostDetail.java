package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;


public class PmtProjectCostDetail {
	PmtProjectCost pmtProjectCost;
	long subsidiAry;
	Account account ;
	double accValue ;
	Currency currency;
	double exchangeRate ;
	String description;

	public void setDescription(String description){
		this.description=description;
	}

	public String getDescription(){
		return this.description;
	}

	public PmtProjectCostDetail() {
		super();
	}

	public double getaccValue() {
		return accValue;
	}

	public void setaccValue(double attrValue) {
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

	public PmtProjectCost getPmtProjectCost() {
		return pmtProjectCost;
	}

	public void setPmtProjectCost(PmtProjectCost pmtProjectCost) {
		this.pmtProjectCost = pmtProjectCost;
	}



	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}



	public long getSubsidiAry() {
		return subsidiAry;
	}

	public void setSubsidiAry(long subsidiAry) {
		this.subsidiAry = subsidiAry;
	}
}
