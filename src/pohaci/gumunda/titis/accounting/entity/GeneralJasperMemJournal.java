package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public interface GeneralJasperMemJournal {
	long getIndex();
	Unit getUnit();
	String getReferenceNo();
	Date getTransactionDate();
	Organization getDepartment();	
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
