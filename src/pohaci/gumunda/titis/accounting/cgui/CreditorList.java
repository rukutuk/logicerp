package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class CreditorList {
  public static final short CODE = 0;
  public static final short NAME = 1;
  public static final short CODE_NAME = 2;
  protected short m_status = -1;

  protected long m_index = 0;
  protected String m_code = "";
  protected String m_name = "";
  protected String m_address = "";
  protected String m_province = "";
  protected String m_country = "";
  protected String m_contact = "";
  protected String m_phone = "";
  protected String m_bankaccount = "";
  protected String m_bankname = "";
  protected Currency m_currency = null;
  protected String m_bankaddress = "";
  protected String m_type = "";
  
  public static final String CREDITOR_TYPE_BANK = "BANK";
  public static final String CREDITOR_TYPE_NONBANK = "NONBANK";

  public CreditorList(String code, String name, String address, String province, String country,
                      String contact, String phone, String bankaccount, String bankname,
                      Currency currency, String bankaddress, String creditorType) {
    m_code = code;
    m_name = name;
    m_address = address;
    m_province = province;
    m_country = country;
    m_contact = contact;
    m_phone = phone;
    m_bankaccount = bankaccount;
    m_bankname = bankname;
    m_currency = currency;
    m_bankaddress = bankaddress;
    m_type = creditorType;
  }

  public CreditorList(long index, String code, String name, String address, String province, String country,
                      String contact, String phone, String bankaccount, String bankname,
                      Currency currency, String bankaddress, String creditorType) {
    m_index = index;
    m_code = code;
    m_name = name;
    m_address = address;
    m_province = province;
    m_country = country;
    m_contact = contact;
    m_phone = phone;
    m_bankaccount = bankaccount;
    m_bankname = bankname;
    m_currency = currency;
    m_bankaddress = bankaddress;
    m_type = creditorType;
  }

  public CreditorList(long index, CreditorList bank) {
    m_index = index;
    m_code = bank.getCode();
    m_name = bank.getName();
    m_address = bank.getAddress();
    m_province = bank.getProvince();
    m_country = bank.getCountry();
    m_contact = bank.getContact();
    m_phone = bank.getPhone();
    m_bankaccount = bank.getBankAccount();
    m_bankname = bank.getBankName();
    m_currency = bank.getCurrency();
    m_bankaddress = bank.getBankAddress();
    m_type = bank.getCreditorType();
  }
  
  public CreditorList(CreditorList bank, short status) {
	    this(bank.getIndex(), bank);
	    m_status = status;
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

  public String getProvince() {
    return m_province;
  }

  public String getCountry() {
    return m_country;
  }

  public String getContact() {
    return m_contact;
  }

  public String getPhone() {
    return m_phone;
  }

  public String getBankAccount() {
    return m_bankaccount;
  }

  public String getBankName() {
    return m_bankname;
  }

  public Currency getCurrency() {
    return m_currency;
  }

  public String getBankAddress() {
    return m_bankaddress;
  }
  
  public String getCreditorType() {
	  return m_type;
  }
  
  public String toString() {
	  if(m_status == CODE_NAME)
		  return m_code + " " + m_name;
	  else if(m_status == NAME)
		  return m_name;
	  return m_code;
  }
}