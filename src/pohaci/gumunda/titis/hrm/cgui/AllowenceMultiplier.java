package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class AllowenceMultiplier {
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_description = "";
  protected float m_multiplier = 0.0f;

  public AllowenceMultiplier(String code, String description, float multiplier) {
    m_code = code;
    m_description = description;
    m_multiplier = multiplier;
  }

  public AllowenceMultiplier(long index, String code, String description, float multiplier) {
    m_index = index;
    m_code = code;
    m_description = description;
    m_multiplier = multiplier;
  }

  public AllowenceMultiplier(long index, AllowenceMultiplier multiplier) {
    m_index = index;
    m_code = multiplier.getAreaCode();
    m_description = multiplier.getDescription();
    m_multiplier = multiplier.getMuliplier();
  }

  public long getIndex() {
    return m_index;
  }

  public String getAreaCode() {
    return m_code;
  }

  public String getDescription() {
    return m_description;
  }

  public float getMuliplier() {
    return m_multiplier;
  }

  public String toString() {
    return m_code;
  }
}
