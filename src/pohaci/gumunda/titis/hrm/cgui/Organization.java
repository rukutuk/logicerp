package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Organization extends EmployeeAttribute {
//  public static final short CODE = 0;
//  public static final short NAME = 1;
  protected Organization m_parent = null;

  public Organization(String code, String name, String description) {
    super(code, name, description);
  }

  public Organization(long index, String code, String name, String description) {
    super(index, code, name, description);
  }

  public Organization(long index, Organization org) {
    super(index, org.getCode(), org.getName(), org.getDescription());
  }

  public Organization(Organization org, short status) {
    super(org.getIndex(), org.getCode(), org.getName(), org.getDescription());
    m_status = status;
  }
  
  public Organization(Organization org, String status) {
	    super(org.getIndex(), org.getCode(), org.getName(), org.getDescription());
	    m_status = CODE_DESCRIPTION;
  }
  
  public void setParent(Organization parent) {
    m_parent = parent;
  }

  public Organization getParent() {
    return m_parent;
  }

  public String toString() {
//    if(m_status == CODE)
//      return m_code;
//    else if(m_status == NAME)
//      return m_name;
//    return m_code + " " + m_name;
	  // dapatkan string dari description
	  return super.toString();
  }
}
