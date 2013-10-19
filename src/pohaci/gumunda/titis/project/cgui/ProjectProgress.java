package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.Date;
import java.text.SimpleDateFormat;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class ProjectProgress {
	protected long m_index = 0;
	protected Date m_date = null;
	protected String m_description = "";
	protected float m_completion = 0.0f;
	protected Employee m_preparedby = null;
	protected Employee m_approver = null;
	protected String m_remark = "";
	
	SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
	
	public ProjectProgress(Date date, String description, float completion,
			Employee preparedby, Employee approver, String remark) {
		m_date = date;
		m_description = description;
		m_completion = completion;
		m_preparedby = preparedby;
		m_approver = approver;
		m_remark = remark;
	}
	
	public ProjectProgress(long index, Date date, String description, float completion,
			Employee preparedby, Employee approver, String remark) {
		m_index = index;
		m_date = date;
		m_description = description;
		m_completion = completion;
		m_preparedby = preparedby;
		m_approver = approver;
		m_remark = remark;
	}
	
	public ProjectProgress(long index, ProjectProgress progress) {
		m_index = index;
		m_date = progress.getDate();
		m_description = progress.getDescription();
		m_completion = progress.getCompletion();
		m_preparedby = progress.getPreparedBy();
		m_approver = progress.getApprover();
		m_remark = progress.getRemark();
	}
	
	public long getIndex() {
		return m_index;
	}
	
	public Date getDate() {
		return m_date;
	}
	
	public String getDescription() {
		return m_description;
	}
	
	public float getCompletion() {
		return m_completion;
	}
	
	public Employee getPreparedBy() {
		return m_preparedby;
	}
	
	public Employee getApprover() {
		return m_approver;
	}
	
	public String getRemark() {
		return m_remark;
	}
	
	public String toString() {
		return m_dateformat.format(m_date);
	}
}