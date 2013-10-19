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

import pohaci.gumunda.titis.accounting.entity.Unit;

public class Employment {
  protected JobTitle m_jobtitle = null;
  protected Organization m_department = null;
  protected Unit m_unit = null;
  protected String m_refno = "";
  protected Date m_refdate = null;
  protected Date m_tmt = null;
  protected Date m_enddate = null;
  protected WorkAgreement m_agreement = null;
  protected String m_description = "";

  public Employment(JobTitle jobtitle, Organization department, Unit unit,
                    String refno, Date refdate, Date tmt, Date enddate,
                    WorkAgreement agreement, String description) {
    m_jobtitle = jobtitle;
    m_department = department;
    m_unit = unit;
    m_refno = refno;
    m_refdate = refdate;
    m_tmt = tmt;
    m_enddate = enddate;
    m_agreement = agreement;
    m_description = description;
  }

  public JobTitle getJobTitle() {
    return new JobTitle(m_jobtitle, JobTitle.NAME);
  }

  public Organization getDepartment() {
    return new Organization(m_department, Organization.NAME);
  }

  public Unit getUnit() {
    return m_unit;
  }

  public String getReferenceNo() {
    return m_refno;
  }

  public Date getReferenceDate() {
    return m_refdate;
  }

  public Date getTMT() {
    return m_tmt;
  }

  public Date getEndDate() {
    return m_enddate;
  }

  public WorkAgreement getWorkAgreement() {
    return new WorkAgreement(m_agreement, WorkAgreement.DESCRIPTION);
  }

  public String getDescription() {
    return m_description;
  }

  public String toString() {
    return getJobTitle().toString();
  }
}