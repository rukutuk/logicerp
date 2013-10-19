package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class CompanyGroup {
  protected long m_index = 0;
  protected String m_name = "";
  protected String m_description = "";

  public CompanyGroup(String name, String description) {
    m_name = name;
    m_description = description;
  }

  public CompanyGroup(long index, String name, String description) {
    m_index = index;
    m_name = name;
    m_description = description;
  }

  public long getIndex() {
    return m_index;
  }

  public String getName() {
    return m_name;
  }

  public String getDescription() {
    return m_description;
  }

  public String toString() {
    return m_name;
  }
}