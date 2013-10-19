package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Education extends EmployeeAttribute {

  public Education(String code, String description) {
    super(code, description);
  }

  public Education(long index, String code, String description) {
    super(index, code, description);
  }

  public Education(long index, Education edu) {
    super(index, edu.getCode(), edu.getDescription());
  }
  
  public Education(Education education, short status) {
	  super(education.getIndex(), education);
	  m_status = status;
  }
}