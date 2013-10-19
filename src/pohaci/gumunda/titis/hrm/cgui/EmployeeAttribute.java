package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class EmployeeAttribute {
  protected short m_status = -1;
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_name = "";
  protected String m_description = "";
  public static final short CODE = 0;
  public static final short NAME = 1;
  public static final short DESCRIPTION = 2;  // i add this
  public static final short CODE_NAME = 3; // i change the code
  public static final short CODE_DESCRIPTION = 4; // i add this

  public EmployeeAttribute(String code, String name, String description) {
    m_code = code;
    m_name = name;
    m_description = description;
  }

  public EmployeeAttribute(long index, String code, String name, String description) {
    m_index = index;
    m_code = code;
    m_name = name;
    m_description = description;
  }

  public EmployeeAttribute(String code, String description) {
    m_code = code;
    m_description = description;
  }

  public EmployeeAttribute(long index, String code, String description) {
    m_index = index;
    m_code = code;
    m_description = description;
  }

  public EmployeeAttribute(long index, EmployeeAttribute attr) {
    m_index = index;
    m_code = attr.getCode();
    m_name = attr.getName();
    m_description = attr.getDescription();
  }

  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public String getName() {
    return m_name;
  }

  public String getDescription() {
    return m_description;
  }

  public String toString() {
   if(m_status == CODE)
	   return m_code;
   else if(m_status == NAME)
	   return m_name;
   else if(m_status == DESCRIPTION)
	   return m_description;
   else if(m_status == CODE_NAME)
	   return m_code + " " + m_name;
   else if(m_status == CODE_DESCRIPTION)
	   return m_code + " " + m_description;
   else 
	 //return m_code;
	  return m_description;
 }
 
 void setToStringStatus(short status){
 	m_status = status;
 }
}