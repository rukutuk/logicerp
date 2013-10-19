package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Date;

public class CertificationRpt {
	String m_certificateno;
	Date m_certificatedate;
	String m_institute;
	String m_qualification;
	String m_desc;
	Date m_expiredate; 
	
	public CertificationRpt(String certificateno,Date certificationdate,String institute,
			String qualification,String desc,Date expiredate){
		this.m_certificateno = certificateno;
		this.m_certificatedate = certificationdate;
		this.m_institute = institute;
		this.m_qualification = qualification;
		this.m_desc = desc;
		this.m_expiredate = expiredate;
	}
	public String getCertificateno(){
		return m_certificateno;
	}
	public Date getCertificatedate(){
		return m_certificatedate;
	}
	public String getInstitute(){
		return m_institute;
	}
	public String getQualification(){
		return m_qualification;
	}
	public String getDes(){
		return m_desc;		
	}
	public Date getExpiratedate(){
		return m_expiredate;
	}
	
}
