package pohaci.gumunda.titis.accounting.entity;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Unit implements Comparable {
  public static final short CODE = 0;
  public static final short DESCRIPTION = 1;
  public static final short CODE_DESCRIPTION = 2;

  protected long m_index = 0;
  protected String m_code = "";
  protected String m_description = "";
  protected short m_status = -1;

  public Unit(String code, String description) {
    m_code = code;
    m_description = description;
  }

  public Unit(long index, String code, String description) {
    m_index = index;
    m_code = code;
    m_description = description;
  }

  public Unit(Unit unit, short status) {
    this(unit.getIndex(), unit.getCode(), unit.getDescription());
    m_status = status;
  }

  public Unit(String description){
	  m_description = description;
  }
  
  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public String getDescription() {
    return m_description;
  }

  public String toString() {
    if(m_status == DESCRIPTION)
      return m_description;
    else if(m_status == CODE_DESCRIPTION)
      return m_code + " " + m_description;
    else if(m_status == CODE)
    	return m_code;
    return m_code + " " + m_description;
  }

public int compareTo(Object o) {
	if (o instanceof Unit)
	{
		Unit ou = (Unit) o;
		return m_code.compareTo(ou.m_code);
	}
	return 0;
}

private static Unit m_nullObject;
public static Unit nullObject() {
	if (m_nullObject == null) {
		m_nullObject = new Unit("-","-");		
	}
	return m_nullObject;
}

public boolean equals(Object obj) {
	if (obj instanceof Unit)
	{
		Unit objU = (Unit) obj;
		return objU.m_index == this.m_index;
	}
	return super.equals(obj);
}

public int hashCode() {
	return (int) m_index;
}

}