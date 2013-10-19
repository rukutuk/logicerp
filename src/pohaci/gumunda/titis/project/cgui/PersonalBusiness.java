package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class PersonalBusiness {
  protected String m_company = "";
  protected String m_address = "";
  protected String m_city = "";
  protected int m_postcode = 0;
  protected String m_province = "";
  protected String m_country = "";
  protected String m_website = "";
  protected String m_phone1 = "";
  protected String m_phone2 = "";
  protected String m_fax = "";
  protected String m_jobtitle = "";
  protected String m_departement = "";
  protected String m_office = "";

  public PersonalBusiness(String company, String address, String city, int postcode, String province,
                          String country, String website, String phone1, String phone2,
                          String fax, String jobtitle, String departement, String office) {
    m_company = company;
    m_address = address;
    m_city = city;
    m_postcode = postcode;
    m_province = province;
    m_country = country;
    m_website = website;
    m_phone1 = phone1;
    m_phone2 = phone2;
    m_fax = fax;
    m_jobtitle = jobtitle;
    m_departement = departement;
    m_office = office;
  }

  public String getCompany() {
    return m_company;
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

  public String getFax(){
    return m_fax;
  }

  public String getJobTitle() {
    return m_jobtitle;
  }

  public String getDepartement() {
    return m_departement;
  }

  public String getOffice() {
    return m_office;
  }

  public String toString() {
    return m_company;
  }
}