package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class PTKP {
  protected long m_index = 0;
  protected String m_name = "";
  protected String m_description = "";
  protected double m_value = 0.0;

  public PTKP(String name, String description, double value) {
    m_name = name;
    m_description = description;
    m_value = value;
  }

  public PTKP(long index, String name, String description, double value) {
    m_index = index;
    m_name = name;
    m_description = description;
    m_value = value;
  }

  public PTKP(long index, PTKP ptkp) {
    m_index = index;
    m_name = ptkp.getName();
    m_description = ptkp.getDescription();
    m_value = ptkp.getValue();
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

  public double getValue() {
    return m_value;
  }

  public String toString() {
    return m_name;
  }
}