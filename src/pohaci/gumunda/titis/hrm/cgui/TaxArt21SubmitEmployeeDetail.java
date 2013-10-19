/**
 * 
 */
package pohaci.gumunda.titis.hrm.cgui;

/**
 * @author dark-knight
 *
 */
public class TaxArt21SubmitEmployeeDetail {
	private long autoindex;
	private Employee_n employee; // menyebalkan harus pake ini.....
	private String jobTitle;
	private TaxArt21SubmitComponentDetail[] componentDetails;
	
	public TaxArt21SubmitComponentDetail[] getComponentDetails() {
		return componentDetails;
	}
	public void setComponentDetails(TaxArt21SubmitComponentDetail[] componentDetails) {
		this.componentDetails = componentDetails;
	}
	public long getAutoindex() {
		return autoindex;
	}
	public void setAutoindex(long autoindex) {
		this.autoindex = autoindex;
	}
	public Employee_n getEmployee() {
		return employee;
	}
	public void setEmployee(Employee_n employee) {
		this.employee = employee;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
}
