package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class AbsenceAttribute {
  protected short m_status = -1;
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_description = "";
  protected int m_days = 0;
  protected boolean m_deduction = false;

  public AbsenceAttribute(String code, String description) {
    m_code = code;
    m_description = description;
  }

  public AbsenceAttribute(long index, String code, String description) {
    m_index = index;
    m_code = code;
    m_description = description;
  }

  public AbsenceAttribute(String code, String description, boolean deduction) {
    m_code = code;
    m_description = description;
    m_deduction = deduction;
  }

  public AbsenceAttribute(long index, String code, String description, boolean deduction) {
    m_index = index;
    m_code = code;
    m_description = description;
    m_deduction = deduction;
  }

  public AbsenceAttribute(String code, String description, int days, boolean deduction) {
    m_code = code;
    m_description = description;
    m_days = days;
    m_deduction = deduction;
  }

  public AbsenceAttribute(long index, String code, String description, int days, boolean deduction) {
    m_index = index;
    m_code = code;
    m_description = description;
    m_days = days;
    m_deduction = deduction;
  }

  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public String getDescription() {
    return m_description;
  }

  public int getDays() {
    return m_days;
  }

  public boolean getDeduction() {
    return m_deduction;
  }

  public String toString() {
    return m_code;
  }
  
  public String getType() {
      return "AbsenceAttribute";
  }


}