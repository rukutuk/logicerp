package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.hrm.cgui.Employee;

public interface GeneralVoucherProject {
	long getIndex();
	Date getChequeDueDate();
	String getChequeNo();
	CashAccount getCashAccount();
	BankAccount getBankAccount();		
	String getReferenceNo();
	Date getTransactionDate();	
	Employee getEmpOriginator();
	Employee getEmpApproved();
	Employee getEmpReceived();
	String getJobTitleApproved();
	String getJobTitleOriginator();
	String getJobTitleReceived();
	Date getDateApproved();
	Date getDateOriginator();
	Date getDateReceived();	
}
