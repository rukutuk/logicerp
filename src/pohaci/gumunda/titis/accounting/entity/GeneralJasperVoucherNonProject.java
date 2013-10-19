package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.hrm.cgui.Employee;

public interface GeneralJasperVoucherNonProject {
	long getIndex();
	String getDescription();
	Date getChequeDueDate();
	String getChequeNo();
	CashAccount getCashAccount();
	BankAccount getBankAccount();
	Unit getUnit();	
	//Organization getDepartment();	
	String getReferenceNo();
	Date getTransactionDate();
	//String getPayTo();
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
