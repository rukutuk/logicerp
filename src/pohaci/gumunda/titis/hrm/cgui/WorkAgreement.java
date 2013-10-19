package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class WorkAgreement extends EmployeeAttribute {
//  public static final short CODE = 0;
//  public static final short DESCRIPTION = 1;
//  public static final short CODE_DESCRIPTION = 2;

  public WorkAgreement(String code, String description) {
    super(code, description);
  }

  public WorkAgreement(long index, String code, String description) {
    super(index, code, description);
  }

  public WorkAgreement(long index, WorkAgreement work) {
    super(index, work.getCode(), work.getDescription());
  }

  public WorkAgreement(WorkAgreement work, short status) {
    super(work.getIndex(), work.getCode(), work.getDescription());
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