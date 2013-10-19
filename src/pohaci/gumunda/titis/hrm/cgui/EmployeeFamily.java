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

public class EmployeeFamily {
  protected SimpleEmployeeAttribute m_relation = null;
  protected String m_name = "";
  protected String m_birthplace = "";
  protected Date m_birthdate = null;
  protected Education m_education = null;
  protected String m_jobtitle = "";
  protected String m_company = "";
  protected String m_remark = "";

  public EmployeeFamily(SimpleEmployeeAttribute relation, String name, String birthplace, Date birthdate,
                Education education, String jobtitle, String company, String remark) {
    m_relation = relation;
    m_name = name;
    m_birthplace = birthplace;
    m_birthdate = birthdate;
    m_education = education;
    m_jobtitle = jobtitle;
    m_company = company;
    m_remark = remark;
  }

  public SimpleEmployeeAttribute getRelation() {
    return m_relation;
  }

  public String getName() {
    return m_name;
  }

  public String getBirthPlace() {
    return m_birthplace;
  }

  public Date getBirthDate() {
    return m_birthdate;
  }

  public Education getEducation() {
    return m_education;
  }

  public String getJobTitle() {
    return m_jobtitle;
  }

  public String getCompany() {
    return m_company;
  }

  public String getRemark() {
    return m_remark;
  }

  public String toString() {
    return m_relation.toString();
  }
}