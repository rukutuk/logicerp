package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Time;

public class DefaultWorkingTime {
  public static final String STR_TYPE1 = "Use default";
  public static final String STR_TYPE2 = "Non default Working Time";
  public static String m_types[] = new String[]{STR_TYPE1, STR_TYPE2};

  protected short m_type = -1;
  protected Time m_from = null;
  protected Time m_to = null;

  public DefaultWorkingTime(short type, Time from, Time to) {
    m_type = type;
    m_from = from;
    m_to = to;
  }

  public short getType() {
    return m_type;
  }

  public String getTypeAsString(){
    if(m_type < 0 || m_type >= m_types.length)
      return "";
    else
      return m_types[m_type];
  }

  public static short typeFromStringToID(String type){
    short len = (short)m_types.length;
    for(short i = 0; i < len; i++){
      if(m_types[i].equals(type))
        return i;
    }

    return -1;
  }

  public Time getFrom() {
    return m_from;
  }

  public Time getTo() {
    return m_to;
  }
}