package pohaci.gumunda.titis.accounting.cgui;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class CompanyLoan {
  protected long m_index = 0;
  protected CreditorList m_creditor = null;
  protected String m_code = "";
  protected String m_name = "";
  protected Currency m_currency = null;
  protected double m_initial = 0.0;
  protected Account m_account = null;
  protected Unit m_unit = null;
  protected String m_remarks = "";
  protected short m_type = CODE_NAME;
  public static final short CODE = 0;
  public static final short NAME = 1;
  public static final short CODE_NAME = 2;

  public CompanyLoan(CreditorList creditor, String code, String name, Currency currency, double initial,
                  Account account, Unit unit, String remarks) {
    m_creditor = creditor;
    m_code = code;
    m_name = name;
    m_currency = currency;
    m_initial = initial;
    m_account = account;
    m_unit = unit;
    m_remarks = remarks;
  }

  public CompanyLoan(long index, CreditorList creditor, String code, String name, Currency currency, double initial,
                  Account account, Unit unit, String remarks) {
    m_index = index;
    m_creditor = creditor;
    m_code = code;
    m_name = name;
    m_currency = currency;
    m_initial = initial;
    m_account = account;
    m_unit = unit;
    m_remarks = remarks;
  }

  public CompanyLoan(long index, CompanyLoan loan) {
    m_index = index;
    m_creditor = loan.getCreditorList();
    m_code = loan.getCode();
    m_name = loan.getName();
    m_currency = loan.getCurrency();
    m_initial = loan.getInitial();
    m_account = loan.getAccount();
    m_unit = loan.getUnit();
    m_remarks = loan.getRemarks();
  }
 public CompanyLoan(){
	 
 }
  public long getIndex() {
    return m_index;
  }

  public CreditorList getCreditorList() {
    return m_creditor;
  }

  public String getCode() {
    return m_code;
  }

  public String getName() {
    return m_name;
  }

  public Currency getCurrency() {
    return m_currency;
  }

  public double getInitial() {
    return m_initial;
  }

  public Account getAccount() {
    return m_account;
  }

  public Unit getUnit() {
    return m_unit;
  }

  public String getRemarks() {
    return m_remarks;
  }

  public String toString() {
    //return m_creditor.getCode()+" "+ m_creditor.getName();
	  if (m_type==CODE)
		  return getCode();
	  else if (m_type==NAME)
		  return getName();
	  
	  return getCode() + " " + getName();
  }

public short igetType() {
	return m_type;
}

public void isetType(short m_type) {
	this.m_type = m_type;
}
}