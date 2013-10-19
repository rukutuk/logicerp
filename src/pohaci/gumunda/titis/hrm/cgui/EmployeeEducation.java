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

public class EmployeeEducation {
  protected Education m_grade = null;
  protected String m_major = "";
  protected String m_institute = "";
  protected Date m_from = null;
  protected Date m_to = null;
  protected float m_gpa = 0.0f;
  protected float m_maxgpa = 0.0f;
  protected String m_description = "";
  protected String m_file = "";
  protected byte[] m_certificate = null;

  public EmployeeEducation(Education grade, String major, String institute,
                           Date from, Date to, float gpa, float maxgpa,
                           String description, String file, byte[] certificate) {
    m_grade = grade;
    m_major = major;
    m_institute = institute;
    m_from = from;
    m_to = to;
    m_gpa = gpa;
    m_maxgpa = maxgpa;
    m_description = description;
    m_file = file;
    m_certificate = certificate;
  }

  public Education getGrade() {
    return m_grade;
  }

  public String getMajorStudy() {
    return m_major;
  }

  public String getInstitute() {
    return m_institute;
  }

  public Date getFrom() {
    return m_from;
  }

  public Date getTo() {
    return m_to;
  }

  public float getGPA() {
    return m_gpa;
  }

  public float getMaxGPA() {
    return m_maxgpa;
  }

  public String getDescription() {
    return m_description;
  }

  public String getFile() {
    return m_file;
  }

  public byte[] getCertificate() {
    return m_certificate;
  }

  public String toString() {
    return m_grade.toString();
  }
}