package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public interface GeneralJasperVchr_Salary {
	long getIndex();
	String getReferenceNo();
	Date getTransactionDate();
	Transaction getTrans();
	Date getSubmitDate();
	String getPaymentSource();
	CashAccount getCashAccount();
	BankAccount getBankAccount();
	String getChequeNo();
	Date getChequeDueDate();
	String getPayto();
	Unit getUnit();
	Employee getEmpOriginator();
	Employee getEmpApproved();
	Employee getEmpReceived();
	String getJobTitleApproved();
	String getJobTitleOriginator();
	String getJobTitleReceived();
	Date getDateApproved();
	Date getDateOriginator();
	Date getDateReceived();	
	String getDescription();
}
