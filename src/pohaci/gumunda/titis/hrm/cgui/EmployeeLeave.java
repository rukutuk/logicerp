package pohaci.gumunda.titis.hrm.cgui;

public class EmployeeLeave {
	long m_lv_1;
	long m_lv_2;
	long m_lv_3;
	long m_lv_4;
	public EmployeeLeave(long lv_1,long lv_2,long lv_3,long lv_4){
		this.m_lv_1 = lv_1;
		this.m_lv_2 = lv_2;
		this.m_lv_3 = lv_3;
		this.m_lv_4 = lv_4;
	}	
	
	public long getLv_1(){
		return m_lv_1;
	}
	public long getLv_2(){
		return m_lv_2;
	}
	public long getLv_3(){
		return m_lv_3;
	}
	public long getLv_4(){
		return m_lv_4;
	}
}
