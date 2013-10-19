package pohaci.gumunda.titis.accounting.entity;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import pohaci.gumunda.titis.project.cgui.Partner;

public class AccountPayable {
  protected Partner m_partner = null;
  protected Account m_account = null;

  public AccountPayable(Partner partner, Account account) {
    m_partner = partner;
    m_account = account;
  }
  
  public AccountPayable() {	
  }

  public Partner getPartner() {
    return m_partner;
  }
  
  public Account getAccount() {
	  return m_account;
  }
  
  public void setAccount(Account account) {
	  m_account = account;
  }
  
  public void setPartner(Partner partner) {
	  m_partner = partner;
  }
}