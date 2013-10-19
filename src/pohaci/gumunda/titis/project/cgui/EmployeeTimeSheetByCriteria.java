package pohaci.gumunda.titis.project.cgui;

public class EmployeeTimeSheetByCriteria {
	long m_qualification,m_indexPersonal;
	String m_workDescription,m_areaCode,m_customer,m_ipcno;
	int m_days;
	public EmployeeTimeSheetByCriteria(long indexPersonal,String workDescription,String AreaCode,
			int days,long qualification,String customer,String ipcno){
		m_indexPersonal = indexPersonal;
		m_workDescription = workDescription;
		m_areaCode = AreaCode;
		m_days = days;
		m_qualification = qualification;
		m_areaCode = AreaCode;
		m_customer = customer;
		m_ipcno = ipcno;
	}
	
	public String getWorkdescription(){
		return m_workDescription;
	}
	public String getAreacode(){
		return m_areaCode;
	}
	public int getDays(){
		return m_days;
	}
	public long getQualification(){
		return m_qualification;
	}
	public String getCustomer(){
		return m_customer;
	}
	public String getIpcno(){
		return m_ipcno;
	}
}
