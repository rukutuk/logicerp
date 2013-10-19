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

public class ProjectNotes {
  protected long m_index = 0;
  protected Date m_date = null;
  protected String m_description = "";
  protected String m_action = "";
  protected Employee m_responsibility = null;
  protected Employee m_preparedby = null;
  protected Employee m_approver = null;
  protected String m_remark = "";
  protected String m_file = "";
  protected byte[] m_sheet = null;

  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");

  public ProjectNotes(Date date, String description, String action, Employee responsibility,
               Employee preparedby, Employee approver, String remark, String file, byte[] sheet) {
    m_date = date;
    m_description = description;
    m_action = action;
    m_responsibility = responsibility;
    m_preparedby = preparedby;
    m_approver = approver;
    m_remark = remark;
    m_file = file;
    m_sheet = sheet;
  }

  public ProjectNotes(long index, Date date, String description, String action, Employee responsibility,
               Employee preparedby, Employee approver, String remark, String file, byte[] sheet) {
    m_index = index;
    m_date = date;
    m_description = description;
    m_action = action;
    m_responsibility = responsibility;
    m_preparedby = preparedby;
    m_approver = approver;
    m_remark = remark;
    m_file = file;
    m_sheet = sheet;
  }

  public ProjectNotes(long index, ProjectNotes notes) {
    m_index = index;
    m_date = notes.getDate();
    m_description = notes.getDescription();
    m_action = notes.getAction();
    m_responsibility = notes.getResponsibility();
    m_preparedby = notes.getPreparedBy();
    m_approver = notes.getApprover();
    m_remark = notes.getRemark();
    m_file = notes.getFile();
    m_sheet = notes.getSheet();
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

  public String getAction() {
    return m_action;
  }

  public Employee getResponsibility() {
    return m_responsibility;
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

  public String getFile() {
    return m_file;
  }

  public byte[] getSheet() {
   return m_sheet;
  }

  public String toString() {
    return m_dateformat.format(m_date);
  }
}