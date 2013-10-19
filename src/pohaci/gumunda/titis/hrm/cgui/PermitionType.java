
package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class PermitionType extends AbsenceAttribute {
  public static final short CODE = 0;
  public static final short DESCRIPTION = 1;
  public static final short CODE_DESCRIPTION = 2;

  public PermitionType(String code, String description, int days, boolean deduction) {
    super(code, description, days, deduction);
  }

  public PermitionType(long index, String code, String description, int days, boolean deduction) {
    super(index, code, description, days, deduction);
  }

  public PermitionType(long index, PermitionType type) {
    super(index, type.getCode(), type.getDescription(), type.getDays(), type.getDeduction());
  }

  public PermitionType(PermitionType type, short status) {
   super(type.getIndex(), type.getCode(), type.getDescription(), type.getDays(), type.getDeduction());
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
      return "Permission";
  }
}