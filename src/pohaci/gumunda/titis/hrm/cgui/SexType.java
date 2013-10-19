package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class SexType extends EmployeeAttribute {
//  public static final short CODE = 0;
//  public static final short DESCRIPTION = 1;
//  public static final short CODE_DESCRIPTION = 2;

  public SexType(String code, String description) {
    super(code, description);
  }

  public SexType(long index, String code, String description) {
    super(index, code, description);
  }

  public SexType(long index, SexType type) {
    super(index, type.getCode(), type.getDescription());
  }

  public SexType(SexType type, short status) {
    super(type.getIndex(), type.getCode(), type.getDescription());
    m_status = status;
  }

  public String toString() {
//    if(m_status == CODE)
//      return m_code;
//    else if(m_status == DESCRIPTION)
//      return m_description;
//    else if(m_status == CODE_DESCRIPTION)
//      return m_code + " " + m_description;
    return super.toString();
  }
}