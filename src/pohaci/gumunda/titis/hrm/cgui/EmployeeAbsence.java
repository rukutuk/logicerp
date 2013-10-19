package pohaci.gumunda.titis.hrm.cgui;

public class EmployeeAbsence {
	long m_present;
	long m_present_not_late;
	long m_present_late;
	long m_absent;
	long m_field_visit;
	long m_other;
	public EmployeeAbsence(long present,long present_not_late,long present_late,
			long absent,long field_visit,long other){
		this.m_present = present;
		this.m_present_not_late=present_not_late;
		this.m_present_late = present_late;
		this.m_absent = absent;
		this.m_field_visit = field_visit;
		this.m_other = other;		
	}
	public long getPresent(){
		return m_present;
	}
	public long getPresentNotLate(){
		return m_present_not_late;
	}
	public long getPresentLate(){
		return m_present_late;
	}
	public long getAbsent(){
		return m_absent;		
	}
	public long getFieldVisit(){
		return m_field_visit;
	}
	public long getOther(){
		return m_other;
	}
}
