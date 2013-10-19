package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class PaychequeLabel {
  public static final String STR_TYPE1 = "Positif";
  public static final String STR_TYPE2 = "Negatif";
  public static String m_types[] = new String[]{STR_TYPE1, STR_TYPE2};

  protected long m_index = 0;
  protected String m_label = "";
  protected short m_type = -1;
  protected boolean m_show = false;

  protected PaychequeLabel m_parent = null;

  public PaychequeLabel(String label, short type, boolean show) {
    m_label = label;
    m_type = type;
    m_show = show;
  }

  public PaychequeLabel(long index, String label, short type, boolean show) {
    m_index = index;
    m_label = label;
    m_type = type;
    m_show = show;
  }

  public PaychequeLabel(long index, PaychequeLabel label) {
    m_index = index;
    m_label = label.getLabel();
    m_type = label.getType();
    m_show = label.isShow();
  }

  public long getIndex() {
    return m_index;
  }

  public String getLabel() {
    return m_label;
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

  public boolean isShow() {
    return m_show;
  }

  public void setParent(PaychequeLabel parent) {
    m_parent = parent;
  }

  public PaychequeLabel getParent() {
    return m_parent;
  }

  public String toString() {
    return getLabel();
  }
}