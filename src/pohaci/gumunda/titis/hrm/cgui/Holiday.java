package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.util.Date;

public class Holiday {
  public static final String STR_COLOR1 = "Present Not Late";
  public static final String STR_COLOR2 = "Present Late";
  public static final String STR_COLOR3 = "Absent";
  public static final String STR_COLOR4 = "Field Visit";
  public static final String STR_COLOR5 = "Default Working Day";
  public static final String STR_COLOR6 = "Non Working Day";
  public static final String STR_COLOR7 = "Holiday";

  public static final Color COLOR1 = new Color(210, 255, 210);
  public static final Color COLOR2 = new Color(255, 255, 164);
  public static final Color COLOR3 = new Color(255, 211, 168);
  public static final Color COLOR4 = new Color(255, 128, 255);
  public static final Color COLOR5 = new Color(255, 255, 255);
  public static final Color COLOR6 = new Color(192, 192, 192);
  public static final Color COLOR7 = new Color(255, 0, 0);

  public static String[] m_strColor = new String[]{STR_COLOR1, STR_COLOR2, STR_COLOR3,
  STR_COLOR4, STR_COLOR5, STR_COLOR6, STR_COLOR7};
  public static Color[] m_color = new Color[]{COLOR1, COLOR2, COLOR3, COLOR4, COLOR5, COLOR6, COLOR7};
  protected Date m_date = null;
  protected String m_description ;
  public boolean inserted;
  public boolean updated;
  public boolean deleted;
  

  public Holiday(Date date, String description) {
    m_date = date;
    m_description = description;
  }

  public Date getDate() {
    return m_date;
  }

  public String getDescription() {
    return m_description;
  }
  public void setDescription(String des){
	  m_description=des;
  }
  public void setDate(Date date){
	  m_date=date;
  }
}