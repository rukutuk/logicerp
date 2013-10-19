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

public class Employee extends SimpleEmployee {	
	protected String m_nickName = "";
	protected String m_birthPlace = "";
	protected Date m_birthDate = null;
	//protected SexType m_sex = null;
	//protected SimpleEmployeeAttribute religion = null;
	protected String m_nationality = "";
	//protected SimpleEmployeeAttribute m_marital = null;
	//protected PTKP m_art21 = null;
	protected String m_address = "";
	protected String m_city = "";
	protected int m_postcode = 0;
	protected String m_province = "";
	protected String m_country = "";
	protected String m_phone = "";
	protected String m_mobilePhone1 = "";
	protected String m_mobilePhone2 = "";	
	protected String m_fax = "";
	protected String m_email = "";
	
	protected Qualification[] m_qualification = new Qualification[0];
	protected Employment[] m_employment = new Employment[0];
	protected EmployeeEducation[] m_education = new EmployeeEducation[0];
	protected Certification[] m_certificate = new Certification[0];
	protected EmployeeFamily[] m_family = new EmployeeFamily[0];
	protected EmployeeAccount[] m_account = new EmployeeAccount[0];
	protected Retirement m_retirement;
	
	protected long n_sex;
	protected long n_marital;
	protected long n_religion;
	protected long n_art21;
	
	public Employee() {
		super("","","");
	}
	public Employee(String no, String firstname, String midlename, String lastname, String nickname,
			String birthplace, Date birthdate, SexType sex,
			SimpleEmployeeAttribute religion, String nationality, SimpleEmployeeAttribute marital,
			PTKP art21, String address, String city, int postcode, String province,
			String country, String phone, String mobphone1, String mobphone2, String fax, String email) {
		super(firstname, midlename, lastname);
		m_no = no;
		m_nickName = nickname;
		m_birthPlace = birthplace;
		m_birthDate = birthdate;
		if (sex!=null) n_sex = sex.getIndex();
		// m_sex = sex;
		//m_religion = religion;
		if (religion!=null) n_religion = religion.getIndex();
		
		m_nationality = nationality;
		//m_marital = marital;
		if (marital!=null) n_marital = marital.getIndex();
		
		//m_art21 = art21;
		if (art21!=null) n_art21 = art21.getIndex();
		m_address = address;
		m_city = city;
		m_postcode = postcode;
		m_province = province;
		m_country = country;
		m_phone = phone;
		m_mobilePhone1 = mobphone1;
		m_mobilePhone2 = mobphone2;
		m_fax = fax;
		m_email = email;
	}
	
	public Employee(long index, String no, String firstname, String midlename, String lastname, String nickname,
			String birthplace, Date birthdate, SexType sex,
			SimpleEmployeeAttribute religion, String nationality, SimpleEmployeeAttribute marital,
			PTKP art21, String address, String city, int postcode, String province,
			String country, String phone, String mobphone1, String mobphone2, String fax, String email) {
		super(index, firstname, midlename, lastname);
		m_no = no;
		m_nickName = nickname;
		m_birthPlace = birthplace;
		m_birthDate = birthdate;
		//  m_sex = sex;
		//  m_religion = religion;
		if (religion!=null) n_religion = religion.getIndex();
		m_nationality = nationality;
		if (marital!=null) n_marital = marital.getIndex();
		//m_marital = marital;
		if (art21!=null) n_art21 = art21.getIndex();
//		m_art21 = art21;
		m_address = address;
		m_city = city;
		m_postcode = postcode;
		m_province = province;
		m_country = country;
		m_phone = phone;
		m_mobilePhone1 = mobphone1;
		m_mobilePhone2 = mobphone2;
		m_fax = fax;
		m_email = email;
	}
	
	// tambahah from nunung
	public Employee(long index, String no, String firstname, String midlename, String lastname, String nickname,
			String birthplace, Date birthdate, long sex,
			long religion, String nationality, long marital,
			long art21, String address, String city, int postcode, String province,
			String country, String phone, String mobphone1, String mobphone2, String fax, String email) {
		super(index, firstname, midlename, lastname);
		m_no = no;
		m_nickName = nickname;
		m_birthPlace = birthplace;
		m_birthDate = birthdate;
		n_sex = sex;
		n_religion = religion;
		m_nationality = nationality;
		n_marital = marital;
		n_art21 = art21;
		m_address = address;
		m_city = city;
		m_postcode = postcode;
		m_province = province;
		m_country = country;
		m_phone = phone;
		m_mobilePhone1 = mobphone1;
		m_mobilePhone2 = mobphone2;
		m_fax = fax;
		m_email = email;
	}
	
	public Employee(long index, Employee employee) {
		super(index, employee.getFirstName(), employee.getMidleName(), employee.getLastName());
		m_no = employee.getEmployeeNo();
		m_nickName = employee.getNickName();
		m_birthPlace = employee.getBirthPlace();
		m_birthDate = employee.getBirthDate();
		
		/*m_sex = employee.getSex();
		 m_religion = employee.getReligion();
		 m_nationality = employee.getNationality();
		 m_marital = employee.getMarital();
		 m_art21 = employee.getArt21();*/
		
		n_sex = employee.getNSex();
		n_religion = employee.getNReligion();
		m_nationality = employee.getNationality();
		n_marital = employee.getNMarital();
		n_art21 = employee.getNArt21();
		
		m_address = employee.getAddress();
		m_city = employee.getCity();
		m_postcode = employee.getPostCode();
		m_province = employee.getProvince();
		m_country = employee.getCountry();
		m_phone = employee.getPhone();
		m_mobilePhone1 = employee.getMobilePhone1();
		m_mobilePhone2 = employee.getMobilePhone2();
		m_fax = employee.getFax();
		m_email = employee.getEmail();
		m_qualification = employee.getQualification();
		m_employment = employee.getEmployment();
		m_education = employee.getEducation();
		m_certificate = employee.getCertification();
		m_family = employee.getFamily();
		m_account = employee.getAccount();
		m_retirement = employee.getRetirement();
	}
	
	// method tambahan nunung
	public long getNSex(){
		return n_sex;
	}
	public void setNSex(long nSex){
		n_sex = nSex;
	}
	public long getNMarital(){
		return n_marital;
	}
	public void setNMarital(long nMarital){
		n_marital = nMarital;
	}
	public long getNReligion(){
		return n_religion;
	}
	public void setNReligion(long nReligion){
		n_religion = nReligion;
	}
	public long getNArt21(){
		return n_art21;
	}
	public void setNArt21(long nArt21){
		n_art21 = nArt21;
	}
	
	
	//public long getIndex() {
	//	return m_index;
	//}
	

	
	public String getNickName() {
		return m_nickName;
	}
	
	public String getBirthPlace() {
		return m_birthPlace;
	}
	
	public Date getBirthDate() {
		return m_birthDate;
	}
	
	// public SexType getSex() {
	//   return new SexType(m_sex, SexType.DESCRIPTION);
	// }
	
	//public SimpleEmployeeAttribute getReligion() {
	//  return m_religion;
	//}
	
	public String getNationality() {
		return m_nationality;
	}
	
	//public SimpleEmployeeAttribute getMarital() {
	//  return m_marital;
	//}
	
	//public PTKP getArt21() {
	//  return m_art21;
	//}
	
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
	
	public String getPhone() {
		return m_phone;
	}
	
	public String getMobilePhone1() {
		return m_mobilePhone1;
	}
	
	public String getMobilePhone2() {
		return m_mobilePhone2;
	}
	
	public String getFax() {
		return m_fax;
	}
	
	public String getEmail() {
		return m_email;
	}
	
	public void setQualification(Qualification[] qua) {
		m_qualification = qua;
	}
	
	public Qualification[] getQualification() {
		return m_qualification;
	}
	
	public String getQualificationToString() {
		String strqua = "";
		for(int i = 0; i < m_qualification.length; i ++) {
			if(i == 0)
				strqua += new Qualification(m_qualification[i], Qualification.NAME).toString();
			else
				strqua += ", " + new Qualification(m_qualification[i], Qualification.NAME).toString();
		}
		System.err.println("qualification2 :" + strqua);
		return strqua;
	}
	
	public void setEmployment(Employment[] employment) {
		m_employment = employment;
	}
	
	public Employment[] getEmployment() {
		return m_employment;
	}
	private Employment m_lastEmployment;
    public Employment maxEmployment() {
        return m_lastEmployment;
    }
    String m_temp = "";
    public void maxEmployment(Employment emp){
    	m_lastEmployment = emp;
    }
	public void setEducation(EmployeeEducation[] education) {
		m_education = education;
	}
	
	public EmployeeEducation[] getEducation() {
		return m_education;
	}
	
	public void setCertification(Certification[] certificate) {
		m_certificate = certificate;
	}
	
	public Certification[] getCertification() {
		return m_certificate;
	}
	
	public void setFamily(EmployeeFamily[] family) {
		m_family = family;
	}
	
	public EmployeeFamily[] getFamily() {
		return m_family;
	}
	
	public void setAccount(EmployeeAccount[] account) {
		m_account = account;
	}
	
	public EmployeeAccount[] getAccount() {
		return m_account;
	}
	
	public void setRetirement(Retirement retirement) {
		m_retirement = retirement;
	}
	
	public Retirement getRetirement() {
		return m_retirement;
	}
	
	public String toString() {
		/*if (m_temp.equals(IConstants.EMPLOYEENO)){
			return m_no.substring(0, 3);
			
		}*/
		return m_no + " " + (m_firstName + " " + m_midleName + " " + m_lastName).trim();
	}
	
	public void setAddress(String address) {
		m_address = address;
	}
	
	public void setCity(String city) {
		m_city = city;
	}
	
	public void setCountry(String country) {
		m_country = country;
	}
	
	public void setEmail(String email) {
		m_email = email;
	}
	
	public void setFax(String fax) {
		m_fax = fax;
	}
	
	public void setNationality(String nationality) {
		m_nationality = nationality;
	}
	
	public void setPhone(String phone) {
		m_phone = phone;
	}
	
	public void setProvince(String province) {
		m_province = province;
	}
	public void setPostCode(int postCode) {
		m_postcode = postCode;
	}
	public String getMobPhone1() {
		return m_mobilePhone1;
	}
	public void setMobPhone1(String mobphone1) {
		m_mobilePhone1 = mobphone1;
	}
	public void setBirthDate(Date birthDate) {
		m_birthDate = birthDate;
	}
	public void setBirthPlace(String birthPlace) {
		m_birthPlace = birthPlace;
	}
	public void setMobilePhone2(String mobilePhone2) {
		m_mobilePhone2 = mobilePhone2;
	}
	public void setMobilePhone1(String mobilePhone1) {
		m_mobilePhone1 = mobilePhone1;
	}
	public void setNickName(String nickName) {
		m_nickName = nickName;
	}
	public String igetFullName() {
		if((m_firstName==null)&&(m_midleName==null)&&(m_lastName==null))
			return null;
		
		String fullName = m_firstName + " " + m_midleName + " " + m_lastName;
		return fullName.replaceAll("  ", " ");
	}
	public boolean equals(Object obj) {
		if(obj instanceof Employee){
			return (this.getIndex()==((Employee)obj).getIndex());
		}
		return false;
	}
}

