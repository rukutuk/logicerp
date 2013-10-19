package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Date;

public class FamilyRpt {
	String m_relation;
	String m_name;
	String m_birthplace;
	Date m_birhtdate;
	String m_education;
	String m_job;
	public FamilyRpt(String relation,String name,String birthplace,Date birthdate,String education,String job){
		this.m_relation = relation;
		this.m_name = name;
		this.m_birthplace = birthplace;
		this.m_birhtdate = birthdate;
		this.m_education = education;
		this.m_job = job;
	}
	public String getRelation(){
		return m_relation;
	}
	public String getName(){
		return m_name;		
	}
	public String getBirthplace(){
		return m_birthplace;
	}
	public Date getBirthdate(){
		return m_birhtdate;
	}
	public String getEducation(){
		return m_education;		
	}
	public String getJob(){
		return m_job;
	}
}
