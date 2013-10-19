package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class PersonalHome {
  protected String m_address = "";
  protected String m_city = "";
  protected int m_postcode = 0;
  protected String m_province = "";
  protected String m_country = "";
  protected String m_website = "";
  protected String m_phone1 = "";
  protected String m_phone2 = "";
  protected String m_mobile1 = "";
  protected String m_mobile2 = "";
  protected String m_fax = "";

  public PersonalHome(String address, String city, int postcode, String province,
                      String country, String website, String phone1, String phone2,
                      String mobile1, String mobile2, String fax) {
    m_address = address;
    m_city = city;
    m_postcode = postcode;
    m_province = province;
    m_country = country;
    m_website = website;
    m_phone1 = phone1;
    m_phone2 = phone2;
    m_mobile1 = mobile1;
    m_mobile2 = mobile2;
    m_fax = fax;
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

  public String getWebsie() {
    return m_website;
  }

  public String getPhone1() {
    return m_phone1;
  }

  public String getPhone2() {
    return m_phone2;
  }

  public String getMobile1() {
    return m_mobile1;
  }

  public String getMobile2() {
    return m_mobile2;
  }

  public String getFax(){
    return m_fax;
  }

  public String toString() {
    return m_address;
  }
}