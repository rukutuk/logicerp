package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Qualification extends EmployeeAttribute {
//  public static final short CODE = 0;
//  public static final short NAME = 1;
//  public static final short CODE_NAME = 2;

  public Qualification(String code, String name, String description) {
    super(code, name, description);
  }

  public Qualification(long index, String code, String name, String description) {
    super(index, code, name, description);
  }

  public Qualification(long index, Qualification qua) {
    super(index, qua.getCode(), qua.getName(), qua.getDescription());
  }

  public Qualification(Qualification qua, short status) {
    super(qua.getIndex(), qua.getCode(), qua.getName(), qua.getDescription());
    m_status = status;
  }

  public String toString() {
//    if(m_status == CODE)
//      return m_code;
//    else if(m_status == NAME)
//      return m_name;
//    else if(m_status == CODE_NAME)
//      return m_code + " " + m_name;
    return super.toString();
  }
}