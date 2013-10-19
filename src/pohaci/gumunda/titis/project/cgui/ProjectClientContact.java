package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class ProjectClientContact {
  protected long m_index = 0;
  protected Personal m_personal = null;

  public ProjectClientContact(Personal personal) {
    m_personal = personal;
  }

  public ProjectClientContact(long index, Personal personal) {
    m_index = index;
    m_personal = personal;
  }

  public long getIndex() {
    return m_index;
  }

  public Personal getPersonal() {
    return m_personal;
  }

  public String toString() {
    return m_personal.getFirstName() + " " + m_personal.getLastName();
  }
}