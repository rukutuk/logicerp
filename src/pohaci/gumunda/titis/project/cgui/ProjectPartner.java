package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class ProjectPartner {
  protected long m_index = 0;
  protected Partner m_partner = null;

  public ProjectPartner(long index, Partner partner) {
    m_index = index;
    m_partner = partner;
  }

  public long getIndex() {
    return m_index;
  }

  public Partner getPartner() {
    return m_partner;
  }

  public String toString() {
    return m_partner.getName();
  }
}