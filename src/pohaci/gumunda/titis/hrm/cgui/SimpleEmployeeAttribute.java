package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class SimpleEmployeeAttribute {
  protected long m_index = 0;
  protected String m_description = "";

  public SimpleEmployeeAttribute(String description) {
    m_description = description;
  }

  public SimpleEmployeeAttribute(long index, String description) {
    m_index = index;
    m_description = description;
  }

  public long getIndex() {
    return m_index;
  }

  public String getDescription() {
    return m_description;
  }

  public String toString() {
    return getDescription();
  }
}