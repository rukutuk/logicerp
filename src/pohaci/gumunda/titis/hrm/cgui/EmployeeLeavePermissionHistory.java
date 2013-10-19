package pohaci.gumunda.titis.hrm.cgui;

import java.util.Date;

public class EmployeeLeavePermissionHistory  {

	protected long m_index = -1;
	protected String m_type = "";
	protected String m_reason = "";
	protected Date m_propose = null;
	protected Date m_from = null;
	protected Date m_to = null;
	protected int m_days = 0;
	protected String m_replaced = "";
	protected String m_checked = "";
	protected Date m_checkeddate = null;
	protected String m_approved = "";
	protected Date m_approveddate = null;
	protected boolean m_isAnnualLeaveDeduction = false;
	
	public EmployeeLeavePermissionHistory(long index, String type, 
			String reason, Date propose, Date from, Date to, int days, 
			String replaced, String checked, Date checkeddate, 
			String approved, Date approveddate, boolean isAnnualLeaveDeduction) {

		this.m_index = index;
		this.m_type = type;
		this.m_reason = reason;
		this.m_propose = propose;
		this.m_from = from;
		this.m_to = to;
		this.m_days = days;
		this.m_replaced = replaced;
		this.m_checked = checked;
		this.m_checkeddate = checkeddate;
		this.m_approved = approved;
		this.m_approveddate = approveddate;
		this.m_isAnnualLeaveDeduction = isAnnualLeaveDeduction;
	}

	public String getApproved() {
		return m_approved;
	}

	public Date getApproveddate() {
		return m_approveddate;
	}

	public String getChecked() {
		return m_checked;
	}

	public Date getCheckeddate() {
		return m_checkeddate;
	}

	public int getDays() {
		return m_days;
	}

	public Date getFrom() {
		return m_from;
	}

	public long getIndex() {
		return m_index;
	}

	public Date getPropose() {
		return m_propose;
	}

	public String getReason() {
		return m_reason;
	}

	public String getReplaced() {
		return m_replaced;
	}

	public Date getTo() {
		return m_to;
	}

	public String getType() {
		return m_type;
	}
	
	public boolean isAnnualLeaveDeduction() {
		return m_isAnnualLeaveDeduction;
	}
	
}
