package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Date;

public class EmployeeSubOrdinat extends Employee {
	private JobTitle m_jobTitle;
	private Date tmt;
	
	public EmployeeSubOrdinat(Employee employee, JobTitle jobTitle,Date tmt){
		super(employee.getIndex(), employee);
		m_jobTitle = jobTitle;
		this.tmt=tmt;
	}
	public  EmployeeSubOrdinat(Employee employee,Employment[] employment){
		super(employee.getIndex(), employee);
		setEmployment(employment);
	}
	
	public JobTitle getJobTitle(){
		return m_jobTitle;
	}
	public Date getTMT(){
		return tmt;
	}
}
