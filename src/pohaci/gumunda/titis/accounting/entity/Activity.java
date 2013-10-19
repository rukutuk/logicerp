package pohaci.gumunda.titis.accounting.entity;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Activity {
  public static final short CODE = 0;
  public static final short NAME = 1;
  public static final short CODE_NAME = 2;

  protected long m_index = 0;
  protected String m_code = "";
  protected String m_name = "";
  protected String m_description = "";
  protected short m_status = -1;

  protected Activity m_parent = null;

  public Activity(String code, String name, String description) {
    m_code = code;
    m_name = name;
    m_description = description;
  }

  public Activity(long index, String code, String name, String description) {
    m_code = code;
    m_index = index;
    m_name = name;
    m_description = description;
  }

  public Activity(Activity activity, short status) {
    this(activity.getIndex(), activity.getCode(), activity.getName(), activity.getDescription());
    m_status = status;
  }

  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public String getName() {
    return m_name;
  }

  public String getDescription() {
    return m_description;
  }

  public void setParent(Activity activity) {
    m_parent = activity;
  }

  public Activity getParent() {
    return m_parent;
  }

  public String toString() {
    if(m_status == CODE)
      return m_code;
    else if(m_status == NAME)
      return m_name;
    return m_code + " " + m_name;
  }
}