package pohaci.gumunda.titis.hrm.cgui;

public class Employee_n implements Comparable {
	String m_employeeno,m_firsname,m_midlename,m_lastname,m_jobtitle;
	long m_autoindex;
	String m_ptkpStatus;
	
	public Employee_n(long autoindex,String employeeno,String firstname,String midlename,String lastname, 
			String jobtitle){		
		m_autoindex = autoindex;
		m_employeeno = employeeno; 
		m_firsname = firstname;
		m_midlename = midlename;
		m_lastname = lastname;
		m_jobtitle = jobtitle;		
	}
	
		
	public Employee_n(long autoindex,String employeeno,String firstname,String midlename,String lastname, 
			String jobtitle, String ptkpStatus){		
		m_autoindex = autoindex;
		m_employeeno = employeeno; 
		m_firsname = firstname;
		m_midlename = midlename;
		m_lastname = lastname;
		m_jobtitle = jobtitle;
		m_ptkpStatus = ptkpStatus;
	}
	
	public String getPTKPStatus(){
		return m_ptkpStatus;
	}
	public String getFirstname(){
		return m_firsname;
	}
	public String getEmployeeno(){
		return m_employeeno;
	}
	public String getMidlename(){
		return m_midlename;
	}
	public String getLastname(){
		return m_lastname;
	}
	public String getJobtitle(){
		return m_jobtitle;		
	}
	public long getAutoindex(){
		return m_autoindex;
		
	}

	public String toString() {
		return getEmployeeno() + " " + getFirstname() + " " + getMidlename() + " " + getLastname();
	}


	public int compareTo(Object arg0) {
		if (arg0 instanceof Employee_n){
			Employee_n comparator = (Employee_n) arg0;
			return this.getEmployeeno().compareTo(comparator.getEmployeeno());
		}
		return 0;
	}
}
