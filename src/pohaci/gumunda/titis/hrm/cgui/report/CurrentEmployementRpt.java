package pohaci.gumunda.titis.hrm.cgui.report;

public class CurrentEmployementRpt {
	String m_jobtitle;
	String m_org;
	String m_code;
	String m_desc;
	public CurrentEmployementRpt(String jobtitle,String org,String code,String desc){
		this.m_jobtitle = jobtitle;
		this.m_org = org;
		this.m_code = code;
		this.m_desc = desc;
	}
	
	public String getJobtitle(){
		return m_jobtitle;
	}
	public String getOrg(){
		return m_org;
	}
	public String getCode(){
		return m_code;
	}
	public String getDes(){
		return m_desc;
	}
}
