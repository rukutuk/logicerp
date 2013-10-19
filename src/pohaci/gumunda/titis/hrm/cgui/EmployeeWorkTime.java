package pohaci.gumunda.titis.hrm.cgui;


public class EmployeeWorkTime {
	String m_workingdate;
	int m_overtime;
	int m_today;
	
	public EmployeeWorkTime(String workingdate,int overtime,int today){
		this.m_workingdate = workingdate;
		this.m_overtime = overtime;
		this.m_today = today;
	}
	
	public String getWorkingDate(){
		return m_workingdate;
	}
	public int getOverTime(){
		return m_overtime;
	}
	public int getToday(){
		return m_today;
	}

}
