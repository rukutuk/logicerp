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
import pohaci.gumunda.titis.hrm.cgui.Qualification;

public class TimeSheetDetail {
  protected Employee m_personel = null;
  protected String m_areacode = "";
  protected Qualification m_qua = null;
  protected Date m_start = null;
  protected Date m_finish = null;
  protected int m_days = 0;
  protected int m_reguler = 0;
  protected int m_holiday = 0;

  public TimeSheetDetail(Employee personel, String areacode, Qualification qua,
                         Date start, Date finish, int days, int reguler, int holiday) {

    m_personel = personel;
    m_areacode = areacode;
    m_qua = qua;
    m_start = start;
    m_finish = finish;
    m_days = days;
    m_reguler = reguler;
    m_holiday = holiday;
  }

  public Employee getPersonel() {
    return m_personel;
  }

  public String getAreaCode() {
    return m_areacode;
  }

  public Qualification getQualification() {
    return new Qualification(m_qua, Qualification.NAME);
  }

  public Date getStartDate() {
    return m_start;
  }

  public Date getFinishDate() {
    return m_finish;
  }

  public int getDays() {
    return m_days;
  }

  public int getReguler() {
    return m_reguler;
  }

  public int getHoliday() {
    return m_holiday;
  }

  public String toString() {
    return m_personel.toString();
  }
}