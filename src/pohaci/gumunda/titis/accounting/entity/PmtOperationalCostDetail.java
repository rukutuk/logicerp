package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.hrm.cgui.Organization;

//import pohaci.gumunda.cross.cgui.Currency;

public class PmtOperationalCostDetail {
	PmtOperationalCost pmtOperationalCost;
	long subsidiAry;	
	Account account;
	double accValue; 
	Currency currency ;
	double exchangerate ;
	String description;
	Organization department;

	public PmtOperationalCostDetail() {
		super();
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

	public PmtOperationalCost getPmtOperationalCost() {
		return pmtOperationalCost;
	}

	public void setPmtOperationalCost(PmtOperationalCost pmtoperasionalcost) {
		this.pmtOperationalCost = pmtoperasionalcost;
	}

	public double getExchangeRate() {
		return exchangerate;
	}

	public void setExchangeRate(double exchangerate) {
		this.exchangerate = exchangerate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Organization getDepartment() {
		return department;
	}

	public void setDepartment(Organization department) {
		this.department = department;
	}

	/**
	 * @return Returns the subsidiAry.
	 */
	public long getSubsidiAry() {
		return subsidiAry;
	}
	/**
	 * @param subsidiAry The subsidiAry to set.
	 */
	public void setSubsidiAry(long subsidiAry) {
		this.subsidiAry = subsidiAry;
	}

}
