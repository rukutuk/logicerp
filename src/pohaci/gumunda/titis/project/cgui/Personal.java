package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.Date;

public class Personal {
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_title = "";
  protected String m_firstname = "";
  protected String m_lastname = "";
  protected String m_nickname = "";
  protected String m_birthplace = "";
  protected Date m_birthdate = null;
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
  protected String m_website = "";

  protected PersonalHome[] m_home = new PersonalHome[0];
  protected PersonalBusiness[] m_business = new PersonalBusiness[0];

  public Personal(String code, String title, String firstname, String lastname,
                  String nickname, String birthplace, Date birthdate, String address,
                  String city, int postcode, String province, String country,
                  String phone1, String phone2, String fax1, String fax2,
                  String email, String website) {

    m_code = code;
    m_title = title;
    m_firstname = firstname;
    m_lastname = lastname;
    m_nickname = nickname;
    m_birthplace = birthplace;
    m_birthdate = birthdate;
    m_address = address;
    m_city = city;
    m_postcode = postcode;
    m_province = province;
    m_country = country;
    m_phone1 = phone1;
    m_phone2 = phone2;
    m_fax1 = fax1;
    m_fax2 = fax2;
    m_email = email;
    m_website = website;
  }

  public Personal(long index, String code, String title, String firstname, String lastname,
                  String nickname, String birthplace, Date birthdate, String address,
                  String city, int postcode, String province, String country,
                  String phone1, String phone2, String fax1, String fax2,
                  String email, String website) {
    m_index = index;
    m_code = code;
    m_title = title;
    m_firstname = firstname;
    m_lastname = lastname;
    m_nickname = nickname;
    m_birthplace = birthplace;
    m_birthdate = birthdate;
    m_address = address;
    m_city = city;
    m_postcode = postcode;
    m_province = province;
    m_country = country;
    m_phone1 = phone1;
    m_phone2 = phone2;
    m_fax1 = fax1;
    m_fax2 = fax2;
    m_email = email;
    m_website = website;
  }

  public Personal(long index, Personal personal) {
    m_index = index;
    m_code = personal.getCode();
    m_title = personal.getTitle();
    m_firstname = personal.getFirstName();
    m_lastname = personal.getLastName();
    m_nickname = personal.getNickName();
    m_birthplace = personal.getBirthPlace();
    m_birthdate = personal.getBirthDate();
    m_address = personal.getAddress();
    m_city = personal.getCity();
    m_postcode = personal.getPostCode();
    m_province = personal.getProvince();
    m_country = personal.getCountry();
    m_phone1 = personal.getPhone1();
    m_phone2 = personal.getPhone2();
    m_fax1 = personal.getFax1();
    m_fax2 = personal.getFax2();
    m_email = personal.getEmail();
    m_website = personal.getWebSite();
    m_home = personal.getPersonalHome();
    m_business = personal.getPersonalBusiness();
  }

  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public String getTitle() {
    return m_title;
  }

  public String getFirstName() {
    return m_firstname;
  }

  public String getLastName() {
    return m_lastname;
  }

  public String getNickName() {
    return m_nickname;
  }

  public String getBirthPlace() {
    return m_birthplace;
  }

  public Date getBirthDate() {
    return m_birthdate;
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

  public String getWebSite() {
    return m_website;
  }

  public void setPersonalHome(PersonalHome[] home) {
    m_home = home;
  }

  public PersonalHome[] getPersonalHome() {
    return m_home;
  }

  public void setPersonalBusiness(PersonalBusiness[] business) {
    m_business = business;
  }

  public PersonalBusiness[] getPersonalBusiness() {
    return m_business;
  }

  public String toString() {
    return m_code + " " + m_firstname + " " + m_lastname;
  }
}


