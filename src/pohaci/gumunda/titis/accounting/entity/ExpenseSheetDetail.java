package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class ExpenseSheetDetail {	
	ExpenseSheet m_expenseSheet;
	Account m_account;
	double accValue ;
	Currency m_currency;
	double m_exchangeRate;
	String description;	
	Organization department;
	
	public void setDescription(String temp){
		this.description=temp;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public ExpenseSheetDetail(){
		super();
	}
	
	public Account getAccount() {
		return m_account;
	}
	public void setAccount(Account account) {
		m_account = account;
	}
	public double getaccValue() {
		return accValue;
	}
	public void setaccValue(double attrValue) {
		accValue = attrValue;
	}
	public Currency getCurrency() {
		return m_currency;
	}
	public void setCurrency(Currency currency) {
		m_currency = currency;
	}
	public double getExchangeRate() {
		return m_exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		m_exchangeRate = exchangeRate;
	}
	public ExpenseSheet getExpenseSheet() {
		return m_expenseSheet;
	}
	public void setExpenseSheet(ExpenseSheet expenseSheet) {
		m_expenseSheet = expenseSheet;
	}

	public Organization getDepartment() {
		return department;
	}

	public void setDepartment(Organization department) {
		this.department = department;
	}
}
