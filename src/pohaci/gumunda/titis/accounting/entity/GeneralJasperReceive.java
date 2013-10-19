package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.hrm.cgui.Employee;

public interface GeneralJasperReceive {
	String getReferenceNo();
	Date getTransactionDate();
	String getDescription();
	//Unit getUnit();
	CashAccount getCashAccount();
	BankAccount getBankAccount();
	long getIndex();
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
