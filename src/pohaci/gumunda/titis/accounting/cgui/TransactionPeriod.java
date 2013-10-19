package pohaci.gumunda.titis.accounting.cgui;

import java.util.Date;

public class TransactionPeriod {
	protected int m_index;
	protected Date m_startdate;
	protected Date m_enddate;
	protected String m_status;

	
	public TransactionPeriod(int Index) {
		m_index = Index;
	}
	
	
	public TransactionPeriod(Date fromDate, Date toDate, String status) {
		m_startdate = fromDate;
		m_enddate = toDate;
		m_status = status;
	}
	
	public TransactionPeriod(int index, Date fromDate, Date toDate, String status) {
		m_index = index;
		m_startdate = fromDate;
		m_enddate = toDate;
		m_status = status;
	}
	
	public int getIndex(){
		return m_index;
	}
	
	public Date getStartDate(){
		return m_startdate;
	}
	
	public Date getEndDate(){
		return m_enddate;
	}
	
	public String getStatus(){
		return m_status;
	}
}
