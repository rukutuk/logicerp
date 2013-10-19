package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class OvertimeMultiplier {
  public static final String STR_TYPE1 = "Working Day";
  public static final String STR_TYPE2 = "Non Working Day";
  public static String m_types[] = new String[]{STR_TYPE1, STR_TYPE2};
  
  public static final int WORKING_DAY = 0;
  public static final int NON_WORKING_DAY = 1;

  protected long m_index = 0;
  protected short m_type = -1;
  protected int m_min = 0;
  protected int m_max = 0;
  protected float m_multiplier = 0.0f;

  public OvertimeMultiplier(short type, int min, int max, float multiplier) {
    m_type = type;
    m_min = min;
    m_max = max;
    m_multiplier = multiplier;
  }

  public OvertimeMultiplier(long index, short type, int min, int max, float multiplier) {
    m_index = index;
    m_type = type;
    m_min = min;
    m_max = max;
    m_multiplier = multiplier;
  }

  public OvertimeMultiplier(long index, OvertimeMultiplier multiplier) {
    m_index = index;
    m_type = multiplier.getType();
    m_min = multiplier.getHourMin();
    m_max = multiplier.getHourMax();
    m_multiplier = multiplier.getMultiplier();
  }

  public long getIndex() {
    return m_index;
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

  public int getHourMin() {
    return m_min;
  }

  public int getHourMax() {
    return m_max;
  }

  public float getMultiplier() {
    return m_multiplier;
  }

  public String toString() {
    return getTypeAsString();
  }
}