package pohaci.gumunda.titis.accounting.cgui;

import pohaci.gumunda.titis.accounting.entity.Account;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class JournalStandardAccount {
  public static final String STR_DEBET = "Debit";
  public static final String STR_CREDIT = "Credit";
  public static String m_balances[] = new String[]{
    STR_DEBET, STR_CREDIT};

  protected Account m_account = null;
  protected short m_balance = -1;
  protected boolean m_ishidden = false;
  protected boolean m_iscalculate = false;

  public JournalStandardAccount(Account account, short balance, boolean ishidden, boolean iscalculate) {
    m_account = account;
    m_balance = balance;
    m_ishidden = ishidden;
    m_iscalculate = iscalculate;
  }

  public Account getAccount() {
    return new Account(m_account, Account.DESCRIPTION);
  }

  public short getBalance() {
    return m_balance;
  }

  public String getBalanceAsString(){
   if(m_balance < 0 || m_balance >= m_balances.length)
     return "";
   else
     return m_balances[m_balance];
  }

 public static short balanceFromStringToID(String balance){
   short len = (short)m_balances.length;
   for(short i = 0; i < len; i++){
     if(m_balances[i].equals(balance))
       return i;
   }

   return -1;
  }

  public boolean isHidden() {
    return m_ishidden;
  }

  public boolean isCalculate() {
    return m_iscalculate;
  }

  public String toString() {
    return getAccount().toString();
  }
}