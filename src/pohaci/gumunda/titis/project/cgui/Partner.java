package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Partner {
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_name = "";
  protected String m_address = "";
  protected String m_city = "";
  protected int m_postCode = 0;
  protected String m_province = "";
  protected String m_country = "";
  protected String m_phone1 = "";
  protected String m_phone2 = "";
  protected String m_fax1 = "";
  protected String m_fax2 = "";
  protected String m_email = "";
  protected String m_website = "";

  protected CompanyGroup[] m_group = new CompanyGroup[0];
  protected SpecificAddress[] m_speaddress = new SpecificAddress[0];
  protected Personal[] m_personal = new Personal[0];

  public Partner(String code, String name, String address, String city,
                  int postcode, String province, String country,
                  String phone1, String phone2, String fax1, String fax2,
                  String email, String website) {

    m_code = code;
    m_name = name;
    m_address = address;
    m_city = city;
    m_postCode = postcode;
    m_province = province;
    m_country = country;
    m_phone1 = phone1;
    m_phone2 = phone2;
    m_fax1 = fax1;
    m_fax2 = fax2;
    m_email = email;
    m_website = website;
  }

  public Partner(long index, String code, String name, String address, String city,
                  int postcode, String province, String country,
                  String phone1, String phone2, String fax1, String fax2,
                  String email, String website) {
    m_index = index;
    m_code = code;
    m_name = name;
    m_address = address;
    m_city = city;
    m_postCode = postcode;
    m_province = province;
    m_country = country;
    m_phone1 = phone1;
    m_phone2 = phone2;
    m_fax1 = fax1;
    m_fax2 = fax2;
    m_email = email;
    m_website = website;
  }

  public Partner(long index, Partner partner) {
    m_index = index;
    m_code = partner.getCode();
    m_name = partner.getName();
    m_address = partner.getAddress();
    m_city = partner.getCity();
    m_postCode = partner.getPostCode();
    m_province = partner.getProvince();
    m_country = partner.getCountry();
    m_phone1 = partner.getPhone1();
    m_phone2 = partner.getPhone2();
    m_fax1 = partner.getFax1();
    m_fax2 = partner.getFax2();
    m_email = partner.getEmail();
    m_website = partner.getWebsite();
    m_group = partner.getCompanyGroup();
    m_speaddress = partner.getSpecificAddress();
    m_personal = partner.getPersonal();
  }

  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public String getName() {
    return m_name;
  }

  public String getAddress() {
    return m_address;
  }

  public String getCity() {
    return m_city;
  }

  public int getPostCode() {
    return m_postCode;
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

  public String getWebsite() {
    return m_website;
  }

  public void setCompanyGroup(CompanyGroup[] group) {
    m_group = group;
  }

  public CompanyGroup[] getCompanyGroup() {
    return m_group;
  }

  public String getCompanyToString() {
    String strgroup = "";
    for(int i = 0; i < m_group.length; i ++) {
      if(i == 0)
        strgroup += m_group[i].toString();
      else
        strgroup += ", " + m_group[i].toString();
    }
    return strgroup;
  }

  public void setSpecificAddress(SpecificAddress[] address) {
    m_speaddress = address;
  }

  public SpecificAddress[] getSpecificAddress() {
    return m_speaddress;
  }

  public Personal[] getPersonal() {
    return m_personal;
  }

  public void setPersonal(Personal[] personal) {
    m_personal = personal;
  }

  public String toString() {
    return m_code + " " + m_name;
  }
  private static Partner m_nullObject;
  public static Partner nullObject() {
	if (m_nullObject == null) {
		m_nullObject = new Partner("","-","","",0,"","","","","","","","");
		
	}
	return m_nullObject;
}

/*public void setPostCode(int postCode) {
	m_postCode = postCode;
}*/
}
