package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.Date;

public class Retirement {
  public static final String STR_STATUS1 = "Active";
  public static final String STR_STATUS2 = "Retired";
  public static String m_statuss[] = new String[]{STR_STATUS1, STR_STATUS2};

  protected String m_reference = "";
  protected Date m_date = null;
  protected String m_reason = "";
  protected String m_remarks = "";
  protected Date m_tmt = null;
  protected short m_status = -1;

  public Retirement(String reference, Date date, String reason, String remarks, Date tmt, short status) {
    m_reference = reference;
    m_date = date;
    m_reason = reason;
    m_remarks = remarks;
    m_tmt = tmt;
    m_status = status;
  }
  
  public Retirement(String reference, Date date, String reason, String remarks, Date tmt) {
	    m_reference = reference;
	    m_date = date;
	    m_reason = reason;
	    m_remarks = remarks;
	    m_tmt = tmt;
	  }

  public String getRetirementReference() {
    return m_reference;
  }

  public Date getRetirementDate() {
    return m_date;
  }

  public String getReason() {
    return m_reason;
  }

  public String getRemarks() {
    return m_remarks;
  }

  public Date getTMT() {
    return m_tmt;
  }

  public short getStatus() {
    return m_status;
  }

  public String getStatusAsString(){
    if(m_status < 0 || m_status >= m_statuss.length)
      return "";
    else
      return m_statuss[m_status];
  }

  public static short statusFromStringToID(String status){
    short len = (short)m_statuss.length;
    for(short i = 0; i < len; i++){
      if(m_statuss[i].equals(status))
        return i;
    }

    return -1;
  }

  public String toString() {
    return m_reference;
  }
}