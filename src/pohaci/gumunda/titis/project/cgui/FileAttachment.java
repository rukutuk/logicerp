package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class FileAttachment {
  protected String m_name = "";
  protected byte[] m_bytes = null;

  public FileAttachment(String name, byte[] bytes) {
    m_name = name;
    m_bytes = bytes;
  }

  public String getName() {
    return m_name;
  }

  public byte[] getBytes() {
    return m_bytes;
  }
}