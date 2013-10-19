package com.gumunda.titis.appication.additional;

public class Parameter {
	String m_trans;
	String m_referenceno;
	String m_status;
	String m_tanggal;
	String m_desc;
	public Parameter(String trans,String referenceno,String status,String tanggal,String desc){
		m_trans=trans;
		m_referenceno=referenceno;
		m_status=status;
		m_tanggal=tanggal;
		m_desc=desc;
	}
	public String getTrans(){
		return m_trans;
	}
	public String getReferenceno(){
		return m_referenceno;
	}
	public String getStatus(){
		return m_status;
	}
	public String getTanggal(){
		return m_tanggal;
	}
	public String getDesc(){
		return m_desc;
	}
}
