package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */
/* ah nambahin satu baris yuk
*/
import java.util.Date;

public class Certification {
  protected String m_no = "";
  protected Date m_date = null;
  protected String m_institute = "";
  protected Qualification m_qua = null;
  protected String m_description = "";
  protected Date m_startdate = null;
  protected Date m_enddate = null;
  protected Date m_expiredate = null;
  protected String m_result = "";
  protected String m_file = "";
  protected byte[] m_certificate = null;

  public Certification(String no, Date date, String institute,
                       Qualification qua, String description,
                       Date startdate, Date enddate, Date expiredate,
                       String result, String file, byte[] certificate) {
    m_no = no;
    m_date = date;
    m_institute = institute;
    m_qua = qua;
    m_description = description;
    m_startdate = startdate;
    m_enddate = enddate;
    m_expiredate = expiredate;
    m_result = result;
    m_file = file;
    m_certificate = certificate;
  }

  public String getNo() {
    return m_no;
  }

  public Date getDate() {
    return m_date;
  }

  public String getInstitute() {
    return m_institute;
  }

  public Qualification getQualification() {
    return new Qualification(m_qua, Qualification.NAME);
  }

  public String getDescription() {
    return m_description;
  }

  public Date getStartDate() {
    return m_startdate;
  }

  public Date getEndDate() {
    return m_enddate;
  }

  public Date getExpireDate() {
    return m_expiredate;
  }

  public String getResult() {
    return m_result;
  }

  public String getFile() {
    return m_file;
  }

  public byte[] getCertificate() {
    return m_certificate;
  }

  public String toString() {
    return getNo();
  }
}