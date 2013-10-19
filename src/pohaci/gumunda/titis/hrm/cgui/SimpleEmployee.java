package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class SimpleEmployee {
	protected long m_index = 0;
	protected String m_no = "";
	protected String m_firstName = "";
	protected String m_midleName = "";
	protected String m_lastName = "";
	protected String m_jobtitle = "";
	protected String m_department = "";
	protected long m_dept;
	
	public SimpleEmployee(String firstname, String midlename, String lastname) {
		m_firstName = firstname;
		m_midleName = midlename;
		m_lastName = lastname;
	}
	
	/*public SimpleEmployee(String firstname, String midlename, String lastname, String jobtitle) {
	 m_firstName = firstname;
	 m_midleName = midlename;
	 m_lastName = lastname;
	 m_jobtitle = jobtitle;
	 }*/
	
	public SimpleEmployee(long index, String firstname, String midlename, String lastname) {
		m_index = index;
		m_firstName = firstname;
		m_midleName = midlename;
		m_lastName = lastname;
	}
	
	public SimpleEmployee(long index, String no,String firstname, String midlename, String lastname, 
			String jobtitle,String department) {
		m_index = index;
		m_no = no;
		m_firstName = firstname;
		m_midleName = midlename;
		m_lastName = lastname;
		m_jobtitle = jobtitle;
		m_department = department;
	}
	
	public SimpleEmployee(long index, String no,String firstname, String midlename, String lastname, 
			long department) {
		m_index = index;
		m_no = no;
		m_firstName = firstname;
		m_midleName = midlename;
		m_lastName = lastname;
		m_dept = department;
	}
	public String getDepartment(){
		return m_department;
	}
	
	public long getIndex() {
		return m_index;
	}
	
	public String getEmployeeNo() {
		return m_no;
	}
	
	
	public String getFirstName() {
		return m_firstName;
	}
	
	public String getMidleName() {
		return m_midleName;
	}
	
	public String getLastName() {
		return m_lastName;
	}
	
	public String getJobTitleName() {
		return m_jobtitle;
	}
	
	public String toString() {
		return m_firstName + " " + m_midleName + " " + m_lastName;
	}
	
	public void setIndex(long index) {
		m_index = index;
	}
	
	public void setEmployeeNo(String employeeNo){
		m_no = employeeNo;
	}
	
	
	public void setFirstName(String firstName) {
		m_firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		m_lastName = lastName;
	}
	
	public void setMidleName(String midleName) {
		m_midleName = midleName;
	}
}