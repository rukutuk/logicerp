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
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class TimeSheet {
  protected long m_index = -1;
  protected Date m_entryDate = null;
  protected Employee m_preparedBy = null;
  protected Date m_preparedDate = null;
  protected Employee m_checkedBy = null;
  protected Date m_checkedDate = null;
  protected String m_approved = "";
  protected Date m_approvedDate = null;
  protected String m_descript = "";

  protected TimeSheetDetail[] m_detail = new TimeSheetDetail[0];

  public TimeSheet(Date entryDate, Employee preparedBy, Date preparedDate,
                   Employee checkedBy, Date checkedDate, String approved,
                   Date approvedDate, String descript) {
    m_entryDate = entryDate;
    m_preparedBy = preparedBy;
    m_preparedDate = preparedDate;
    m_checkedBy = checkedBy;
    m_checkedDate = checkedDate;
    m_approved = approved;
    m_approvedDate = approvedDate;
    m_descript = descript;
  }

  public TimeSheet(long index, Date entryDate, Employee preparedBy, Date preparedDate,
                   Employee checkedBy, Date checkedDate, String approved,
                   Date approvedDate, String descript) {
    m_index = index;
    m_entryDate = entryDate;
    m_preparedBy = preparedBy;
    m_preparedDate = preparedDate;
    m_checkedBy = checkedBy;
    m_checkedDate = checkedDate;
    m_approved = approved;
    m_approvedDate = approvedDate;
    m_descript = descript;
  }

  public TimeSheet(long index, TimeSheet sheet) {
    m_index = index;
    m_entryDate = sheet.getEntryDate();
    m_preparedBy = sheet.getPreparedBy();
    m_preparedDate = sheet.getPreparedDate();
    m_checkedBy = sheet.getCheckedBy();
    m_checkedDate = sheet.getCheckedDate();
    m_approved = sheet.getApproved();
    m_approvedDate = sheet.getApprovedDate();
    m_descript = sheet.getDescription();
    m_detail = sheet.getTimeSheetDetail();
  }

  public long getIndex() {
    return m_index;
  }

  public Employee getPreparedBy() {
    return m_preparedBy;
  }

  public Date getPreparedDate() {
    return m_preparedDate;
  }

  public Date getEntryDate() {
    return m_entryDate;
  }

  public Employee getCheckedBy() {
    return m_checkedBy;
  }

  public Date getCheckedDate() {
    return m_checkedDate;
  }

  public String getApproved() {
    return m_approved;
  }

  public Date getApprovedDate() {
    return m_approvedDate;
  }

  public String getDescription() {
    return m_descript;
  }

  public void setTimeSheetDetail(TimeSheetDetail[] detail) {
    m_detail = detail;
  }

  public TimeSheetDetail[] getTimeSheetDetail() {
    return m_detail;
  }

  public String toString() {
    return new java.text.SimpleDateFormat("dd-MM-yyyy").format(m_entryDate);
  }
}