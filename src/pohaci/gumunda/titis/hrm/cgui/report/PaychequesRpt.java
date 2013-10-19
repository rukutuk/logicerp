package pohaci.gumunda.titis.hrm.cgui.report;

public class PaychequesRpt {
	String m_subname;
	int m_type;
	long m_superlabel;	
	String m_supername;
	long m_sublabel;	
	public PaychequesRpt(String subname,int type,long superlabel,String supername,long sublabel){
		m_type = type;
		m_subname = subname;
		m_superlabel = superlabel;
		m_supername = supername;
		m_sublabel = sublabel;
	}
	public String getSubname(){
		return m_subname;
	}
	public int getType(){
		return m_type;
	}
	public long getSuperlabel(){
		return m_superlabel;
	}
	public String getSupername(){
		return m_supername;
	}
	public long getSublabel(){
		return m_sublabel;
	}
}
