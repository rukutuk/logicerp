package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import pohaci.gumunda.titis.accounting.cgui.Currency;

public class EmployeeAccount {
  protected String m_accountname = "";
  protected String m_accountno = "";
  protected String m_bankname = "";
  protected String m_bankaddress = "";
  protected Currency m_currency = null;
  protected String m_remark = "";

  public EmployeeAccount(String accountname, String accountno,
                         String bankname, String bankaddress,
                         Currency currency, String remark) {
    m_accountname = accountname;
    m_accountno = accountno;
    m_bankname = bankname;
    m_bankaddress = bankaddress;
    m_currency = currency;
    m_remark = remark;
  }

  public String getAccountName() {
    return m_accountname;
  }

  public String getAccountNo() {
    return m_accountno;
  }

  public String getBankName() {
    return m_bankname;
  }

  public String getBankAddress() {
    return m_bankaddress;
  }

  public Currency getCurrency() {
    return m_currency;
  }

  public String getRemark() {
    return m_remark;
  }

  public String toString() {
    return m_accountname;
  }
}