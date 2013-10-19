package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Date;

public class EmploymentHistoryRpt {
	String m_jobtitle;
	String m_org;
	String m_code;
	String m_desc;
	Date m_tmt;
	public EmploymentHistoryRpt(String jobtitle,String org,String code,String desc,Date tmt){
		this.m_jobtitle = jobtitle;
		this.m_org = org;
		this.m_code = code;
		this.m_desc = desc;
		this.m_tmt = tmt;		
	}
	public String getJobTitle(){
		return m_jobtitle;
	}
	public String getOrg(){
		return m_org;
	}
	public String getCode(){
		return m_code;
	}
	public String getDesc(){
		return m_desc;		
	}
	public Date getTMT(){
		return m_tmt;
	}
}
