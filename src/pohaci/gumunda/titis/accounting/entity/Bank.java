package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Bank {
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_name = "";
  protected String m_accountNo = "";
  protected Currency m_currency = null;
  protected String m_address = "";
  protected String m_contact = "";
  protected String m_phone = "";
  protected Unit m_unit = null;

  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public String getName() {
    return m_name;
  }

  public String getAccountNo() {
    return m_accountNo;
  }

  public Currency getCurrency() {
    return new Currency(m_currency, Currency.SYMBOL);
  }

  public String getAddress() {
    return m_address;
  }

  public String getContact() {
    return m_contact;
  }

  public String getPhone() {
    return m_phone;
  }

  public Unit getUnit() {
    return m_unit;
  }

  public String toString() {
    return m_code;
  }

public void setAccountNo(String accountNo) {
	m_accountNo = accountNo;
}

public void setAddress(String address) {
	m_address = address;
}

public void setCode(String code) {
	m_code = code;
}

public void setContact(String contact) {
	m_contact = contact;
}

public void setCurrency(Currency currency) {
	m_currency = currency;
}

public void setIndex(long index) {
	m_index = index;
}

public void setName(String name) {
	m_name = name;
}

public void setPhone(String phone) {
	m_phone = phone;
}

public void setUnit(Unit unit) {
	m_unit = unit;
}
}