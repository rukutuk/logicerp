package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Date;

public class EducationRpt {
	String m_grade;
	String m_majorstudy;
	String m_institute;
	Date m_graduate;
	public EducationRpt(String grade,String majorstudy,String institue,Date graduate){
		this.m_grade = grade;
		this.m_majorstudy = majorstudy;
		this.m_institute = institue;
		this.m_graduate = graduate;
	}
	public String getGrade(){
		return m_grade;
	}
	public String getMajorstudy(){
		return m_majorstudy;
	}
	public String getInstite(){
		return m_majorstudy;
	}
	public Date getGraduate(){
		return m_graduate;
	}
}
