package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import pohaci.gumunda.titis.hrm.cgui.*;

public class ProjectPersonal {
  protected long m_index = 0;
  protected Employee m_employee = null;
  protected String m_position = "";
  protected String m_taks = "";
  protected String m_workdescription = "";
  protected String m_status = "";

  public ProjectPersonal(Employee employee, String position,
                         String taks, String workdescription, String status) {
    m_employee = employee;
    m_position = position;
    m_taks = taks;
    m_workdescription = workdescription;
    m_status = status;
  }

  public ProjectPersonal(long index, Employee employee, String position,
                         String taks, String workdescription, String status) {
    m_index = index;
    m_employee = employee;
    m_position = position;
    m_taks = taks;
    m_workdescription = workdescription;
    m_status = status;
  }

  public ProjectPersonal(long index, ProjectPersonal person) {
    m_index = index;
    m_employee = person.getEmployee();
    m_position = person.getPosition();
    m_taks = person.getTask();
    m_workdescription = person.getWorkDescription();
    m_status = person.getStatus();
  }

  public long getIndex() {
    return m_index;
  }

  public Employee getEmployee() {
    return m_employee;
  }

  public String getPosition() {
    return m_position;
  }

  public String getTask() {
    return m_taks;
  }

  public String getWorkDescription() {
    return m_workdescription;
  }

  public String getStatus() {
    return m_status;
  }

  public String toString() {
    return m_employee.toString();
  }
}