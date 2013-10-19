package pohaci.gumunda.titis.accounting.entity;


public class SubsidiaryAccountSetting {
	long m_autoindex;
	Account m_account;
	int m_transcode;
	String m_subsidiaryAccount;
	public static String EMPLOYEE = "Employee";
	public static String PARTNER  = "Partner";
	public static String CUSTOMER = "Customer";
	public static String BANK = "Bank";
	public static String CASH = "Cash";
	public static String LOAN = "Loan";
	public static String PROJECT = "Project";
	public Account getAccount() {
		return m_account;
	}
	public void setAccount(Account account) {
		m_account = account;
	}
	public long getAutoindex() {
		return m_autoindex;
	}
	public void setAutoindex(long autoindex) {
		m_autoindex = autoindex;
	}
	public String getSubsidiaryAccount() {
		return m_subsidiaryAccount;
	}
	public void setSubsidiaryAccount(String subsidiaryAccount) {
		m_subsidiaryAccount = subsidiaryAccount;
	}
	public String toString(){
		return m_account.toString();
	}
	public int getTranscode() {
		return m_transcode;
	}
	public void setTranscode(int transcode) {
		m_transcode = transcode;
	}
	
}	
