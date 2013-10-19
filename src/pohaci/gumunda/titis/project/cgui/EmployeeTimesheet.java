package pohaci.gumunda.titis.project.cgui;

import java.sql.Date;

public class EmployeeTimesheet {
	private String m_empNo,m_firstName,m_midleName,m_lastName,
		m_qualification,m_workDescription,m_client,m_areacode;
	private int m_days;	
	private String m_jobTitle="";
	private long m_indexPersonal;
	private Date m_entrydate;
	private String m_ipcno;
	private int m_reguler,m_holiday;
	
	public EmployeeTimesheet(long indexPersonal,String employeeNo,String firstName,
			String midleName,String lastName,String jobTitle,String Qualification,
			String workDescription, String client,String ipcno,int days,String areacode,Date entrydate){
		m_indexPersonal = indexPersonal;
		m_empNo = employeeNo;
		m_firstName = firstName;
		m_midleName = midleName;
		m_lastName = lastName;
		m_jobTitle = jobTitle;
		m_qualification = Qualification;
		m_workDescription = workDescription;
		m_client = client;
		m_ipcno = ipcno;
		m_days = days;
		m_areacode = areacode;
		m_entrydate = entrydate;		
	}
	
	public EmployeeTimesheet(long indexPersonal,String employeeNo,String firstName,
			String midleName,String lastName,String jobTitle,String Qualification,
			String workDescription, String client,String ipcno,int days,String areacode,
			Date entrydate,int reguler, int holiday){
		m_indexPersonal = indexPersonal;
		m_empNo = employeeNo;
		m_firstName = firstName;
		m_midleName = midleName;
		m_lastName = lastName;
		m_jobTitle = jobTitle;
		m_qualification = Qualification;
		m_workDescription = workDescription;
		m_client = client;
		m_ipcno = ipcno;
		m_days = days;
		m_areacode = areacode;
		m_entrydate = entrydate;	
		m_reguler = reguler;
		m_holiday = holiday;
	}
	
	
	public long getIndexPersonal(){
		return m_indexPersonal;
	}
	public String getEmployeeno(){
		return m_empNo;
	}
	public String getAllName(){
		return m_firstName + " " + m_midleName + " " + m_lastName;
	}
	public String getJobTitle(){
		return m_jobTitle;
	}
	public String getQualification(){
		return m_qualification;
	}
	public String getWorkdescription(){
		return m_workDescription;
	}
	public String getClient(){
		return m_client;
	}
	public String getIpcNo(){
		return m_ipcno;
	}
	public int getDays(){
		return m_days;
	}
	public String getAreacode(){
		return m_areacode;
	}
	public Date getEntrydate(){
		return m_entrydate;
	}
	public int getReguler(){
		return m_reguler;
	}
	public int getHoliday(){
		return m_holiday;
	}
	
}
