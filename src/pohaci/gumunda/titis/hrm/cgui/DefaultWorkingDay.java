package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class DefaultWorkingDay {
  public static final String STR_TYPE1 = "Use default";
  public static final String STR_TYPE2 = "Non default Working Day";
  public static String m_types[] = new String[]{STR_TYPE1, STR_TYPE2};

  protected short m_type = -1;
  protected static boolean m_monday = false;
  protected static boolean m_tuesday = false;
  protected static boolean m_wednesday = false;
  protected static boolean m_thursday = false;
  protected static boolean m_friday = false;
  protected static boolean m_saturday = false;
  protected static boolean m_sunday = false;

  public DefaultWorkingDay(short type, boolean monday, boolean tuesday, boolean wednesday,
                           boolean thursday, boolean friday, boolean saturday, boolean sunday) {
    m_type = type;
    m_monday = monday;
    m_tuesday = tuesday;
    m_wednesday = wednesday;
    m_thursday = thursday;
    m_friday = friday;
    m_saturday = saturday;
    m_sunday = sunday;
  }

  public DefaultWorkingDay() {
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

  public boolean getMonday() {
    return m_monday;
  }

  public boolean getTuesday() {
    return m_tuesday;
  }

  public boolean getWednesday() {
    return m_wednesday;
  }

  public boolean getThursday() {
    return m_thursday;
  }

  public boolean getFriday() {
    return m_friday;
  }

  public boolean getSaturday() {
    return m_saturday;
  }

  public boolean getSunday() {
    return m_sunday;
  }

  public boolean[] getDays() {
    boolean[] days = new boolean[7];
    days[0] = getMonday();
    days[1] = getTuesday();
    days[2] = getWednesday();
    days[3] = getThursday();
    days[4] = getFriday();
    days[5] = getSaturday();
    days[6] = getSunday();
    return days;
  }
}