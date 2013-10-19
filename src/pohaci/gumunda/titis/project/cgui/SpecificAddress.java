package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class SpecificAddress {
  protected String m_type = "";
  protected String m_address = "";
  protected String m_city = "";
  protected int m_postcode = 0;
  protected String m_province = "";
  protected String m_country = "";
  protected String m_phone1 = "";
  protected String m_phone2 = "";
  protected String m_fax1 = "";
  protected String m_fax2 = "";
  protected String m_email = "";

  public SpecificAddress(String type, String address, String city, int postcode,
                         String province, String country, String phone1, String phone2,
                         String fax1, String fax2, String email) {
    m_type = type;
    m_address = address;
    m_city = city;
    m_postcode = postcode;
    m_province = province;
    m_country = country;
    m_phone1 = phone1;
    m_phone2 = phone2;
    m_fax1 = fax1;
    m_email = email;
  }

  public String getType() {
    return m_type;
  }

  public String getAddress() {
    return m_address;
  }

  public String getCity() {
    return m_city;
  }

  public int getPostCode() {
    return m_postcode;
  }

  public String getProvince() {
    return m_province;
  }

  public String getCountry() {
    return m_country;
  }

  public String getPhone1() {
    return m_phone1;
  }

  public String getPhone2() {
    return m_phone2;
  }

  public String getFax1() {
    return m_fax1;
  }

  public String getFax2() {
    return m_fax2;
  }

  public String getEmail() {
    return m_email;
  }

  public String toString() {
    return m_type;
  }
}