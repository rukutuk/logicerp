package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class LeaveType extends AbsenceAttribute {
  public static final short CODE = 0;
  public static final short DESCRIPTION = 1;
  public static final short CODE_DESCRIPTION = 2;

  public LeaveType(String code, String description, boolean deduction) {
    super(code, description, deduction);
  }

  public LeaveType(long index, String code, String description, boolean deduction) {
    super(index, code, description, deduction);
  }

  public LeaveType(long index, LeaveType type) {
    super(index, type.getCode(), type.getDescription(), type.getDeduction());
  }

  public LeaveType(LeaveType type, short status) {
    super(type.getIndex(), type.getCode(), type.getDescription(), type.getDeduction());
    m_status = status;
  }

  public String toString() {
    if(m_status == CODE)
      return m_code;
    else if(m_status == DESCRIPTION)
      return m_description;
    else if(m_status == CODE_DESCRIPTION)
      return m_code + " " + m_description;
    return super.toString();
  }
  
  public String getType() {
      return "Leave";
  }
}