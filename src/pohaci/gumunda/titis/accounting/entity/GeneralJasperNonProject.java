package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public interface GeneralJasperNonProject {
	long getIndex();
	Date getChequeDueDate();
	String getChequeNo();
	Unit getUnit();	
	Organization getDepartment();	
	String getReferenceNo();
	Date getTransactionDate();
	String getPayTo();
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
