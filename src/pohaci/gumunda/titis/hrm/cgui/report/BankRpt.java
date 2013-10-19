package pohaci.gumunda.titis.hrm.cgui.report;

public class BankRpt {
	String m_name;
	String m_accountno;
	String m_bank;
	String m_currency;
	public BankRpt(String name,String accountno,String bank, String currency){
		this.m_name = name;
		this.m_accountno = accountno;
		this.m_bank = bank;
		this.m_currency = currency;		
	}
	public String getName(){
		return m_name;
	}
	public String getAccountno(){
		return m_accountno;
	}
	public String getBank(){
		return m_bank;
	}
	public String getCurrency(){
		return m_currency;
	}
}
