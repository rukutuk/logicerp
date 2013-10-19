package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class PayrollDeptValue {

	/**
	 * @param args
	 */
	long index;
	EmployeePayrollSubmit employeePayrollSubmit;
	Organization organization;
	Account account;
	double accvalue;
	public PayrollDeptValue(long index,EmployeePayrollSubmit employeePayrollSubmit,Organization organization,Account account,double accvalue){		
		this.index = index;
		this.employeePayrollSubmit=employeePayrollSubmit;
		this.organization = organization;
		this.account = account;
		this.accvalue = accvalue;
	}
	
	public PayrollDeptValue(){
	}
	
	public Account getAccount() {
		return this.account;
	}
	public Organization getDepartment() {
		return this.organization;
	}
	public long getIndex() {
		return this.index;
	}
	
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public double getAccvalue() {
		return this.accvalue;
	}
	public void setAccvalue(double accvalue) {
		this.accvalue = accvalue;
	}
	public EmployeePayrollSubmit getEmployeePayrollSubmit() {
		return employeePayrollSubmit;
	}
	public void setEmployeePayrollSubmit(EmployeePayrollSubmit employeePayrollSubmit) {
		this.employeePayrollSubmit = employeePayrollSubmit;
	}

}
