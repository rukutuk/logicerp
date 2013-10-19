package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Report {
  protected long m_index = 0;
  protected String m_name = "";

  public Report(String name) {
    m_name = name;
  }

  public Report(long index, String name) {
    m_index = index;
    m_name = name;
  }

  public long getIndex() {
    return m_index;
  }

  public String getName() {
    return m_name;
  }

  public String toString() {
    return m_name;
  }
}