package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.project.cgui.Customer;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */


public class AccountReceivable {
  protected Customer m_customer = null;
  protected Account m_account = null;

  public AccountReceivable(Customer customer, Account account) {
    m_customer = customer;
    m_account = account;
  }

  public Customer getCustomer() {
    return m_customer;
  }

  public Account getAccount() {
    return m_account;
  }
}