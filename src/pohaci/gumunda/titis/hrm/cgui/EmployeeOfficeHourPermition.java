package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.Date;
import java.sql.Time;

public class EmployeeOfficeHourPermition {
  protected long m_index = 0;
  protected Date m_propose = null;
  protected Date m_permission = null;
  protected Time m_from = null;
  protected Time m_to = null;
  protected OfficeHourPermition m_reason = null;
  protected Employee m_checked = null;
  protected Date m_checkeddate = null;
  protected Employee m_approved = null;
  protected Date m_approveddate = null;
  protected String m_description = null;

  public EmployeeOfficeHourPermition(Date propose, Date permission, Time from, Time to, OfficeHourPermition reason,
                                     Employee checked, Date checkeddate, Employee approved, Date approveddate,
                                     String description) {
    m_propose = propose;
    m_permission = permission;
    m_from = from;
    m_to = to;
    m_reason = reason;
    m_checked = checked;
    m_checkeddate = checkeddate;
    m_approved = approved;
    m_description = description;
  }

  public EmployeeOfficeHourPermition(long index, Date propose, Date permission, Time from, Time to, OfficeHourPermition reason,
                                     Employee checked, Date checkeddate, Employee approved, Date approveddate,
                                     String description) {
    m_index = index;
    m_propose = propose;
    m_permission = permission;
    m_from = from;
    m_to = to;
    m_reason = reason;
    m_checked = checked;
    m_checkeddate = checkeddate;
    m_approved = approved;
    m_description = description;
  }

  public EmployeeOfficeHourPermition(long index, EmployeeOfficeHourPermition reason) {
    m_index = index;
    m_propose = reason.getPropose();
    m_permission = reason.getPermissionDate();
    m_from = reason.getFrom();
    m_to = reason.getTo();
    m_reason = reason.getReason();
    m_checked = reason.getChecked();
    m_checkeddate = reason.getCheckedDate();
    m_approved = reason.getApproved();
    m_description = reason.getDesciption();
  }

  public long getIndex() {
    return m_index;
  }

  public Date getPropose() {
    return m_propose;
  }

  public Time getFrom() {
    return m_from;
  }

  public Time getTo() {
    return m_to;
  }

  public OfficeHourPermition getReason() {
    return new OfficeHourPermition(m_reason, OfficeHourPermition.DESCRIPTION);
  }

  public Employee getChecked() {
    return m_checked;
  }

  public Date getCheckedDate() {
    return m_checkeddate;
  }

  public Employee getApproved() {
    return m_approved;
  }

  public Date getApprovedDate() {
    return m_checkeddate;
  }

  public String getDesciption() {
    return m_description;
  }

  public String toString() {
    return getReason().toString();
  }

  public Date getPermissionDate() {
	return m_permission;
  }
}