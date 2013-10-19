package pohaci.gumunda.titis.accounting.cgui;

import java.io.Serializable;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class SimpleAccount implements Comparable, Serializable {
  private static final long serialVersionUID = 1L;
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_name = "";

  public SimpleAccount() {}

  public SimpleAccount(long index, String code, String name) {
    m_index = index;
    m_code = code;
    m_name = name;
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

  public String toString() {
    return m_name + " [" + m_code + "]";
  }

  public int compareTo(Object o) {
	SimpleAccount oacc;
	if (o == null)
		return 0;
	if (this.getClass().isAssignableFrom(o.getClass()))
	{
		oacc = (SimpleAccount) o;
		return m_code.compareTo(oacc.m_code);
	}
	return 0;
}
  
  
}