package pohaci.gumunda.titis.hrm.cgui;

public class EmployeePermition {
	long m_pm_1;
	long m_pm_2;
	long m_pm_3;
	long m_pm_4;
	long m_pm_5;
	long m_pm_6;
	long m_pm_99;
	public EmployeePermition(long pm_1,long pm_2,long pm_3,long pm_4,long pm_5,long pm_6,long pm_99){
		this.m_pm_1 = pm_1;
		this.m_pm_2 = pm_2;
		this.m_pm_3 = pm_3;
		this.m_pm_4 = pm_4;
		this.m_pm_5 = pm_5;
		this.m_pm_6 = pm_6;
		this.m_pm_99 = pm_99;		
	}	
	public long getPM_1(){
		return m_pm_1;
	}
	public long getPM_2(){
		return m_pm_2;
	}
	public long getPM_3(){
		return m_pm_3;
	}
	public long getPM_4(){
		return m_pm_4;
	}
	public long getPM_5(){
		return m_pm_5;
	}
	public long getPM_6(){
		return m_pm_6;
	}
	public long getPM_99(){
		return m_pm_99;
	}
}
