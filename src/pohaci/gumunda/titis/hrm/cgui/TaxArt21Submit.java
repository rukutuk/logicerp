/**
 * 
 */
package pohaci.gumunda.titis.hrm.cgui;

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.entity.Account;

public class TaxArt21Submit  extends TransactionTemplateEntity{
	private Transaction transaction;
	private Date submittedDate;
	private int monthSubmitted;
	private int yearSubmitted;
	private Account taxAccount;
	private TaxArt21SubmitEmployeeDetail[] employeeDetails;
	private long index;
	
	public TaxArt21SubmitEmployeeDetail[] getEmployeeDetails() {
		return employeeDetails;
	}
	public void setEmployeeDetails(TaxArt21SubmitEmployeeDetail[] details) {
		this.employeeDetails = details;
	}
	public int getMonthSubmitted() {
		return monthSubmitted;
	}
	public void setMonthSubmitted(int monthSubmitted) {
		this.monthSubmitted = monthSubmitted;
	}
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	public Account getTaxAccount() {
		return taxAccount;
	}
	public void setTaxAccount(Account taxAccount) {
		this.taxAccount = taxAccount;
	}
	public int getYearSubmitted() {
		return yearSubmitted;
	}
	public void setYearSubmitted(int yearSubmitted) {
		this.yearSubmitted = yearSubmitted;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
}
