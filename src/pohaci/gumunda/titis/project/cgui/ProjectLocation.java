package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class ProjectLocation {
  protected long m_index = 0;
  protected String m_location = "";
  protected String m_description = "";

  public ProjectLocation(String location, String description) {
    m_location = location;
    m_description = description;
  }

  public ProjectLocation(long index, String location, String description) {
    m_index = index;
    m_location = location;
    m_description = description;
  }

  public ProjectLocation(long index, ProjectLocation location) {
    m_index = index;
    m_location = location.getLocation();
    m_description = location.getDescription();
  }

  public long getIndex() {
    return m_index;
  }

  public String getLocation() {
    return m_location;
  }

  public String getDescription() {
    return m_description;
  }

  public String toString() {
    return m_location;
  }
}