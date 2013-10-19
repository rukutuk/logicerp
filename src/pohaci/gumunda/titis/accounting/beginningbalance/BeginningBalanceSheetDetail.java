package pohaci.gumunda.titis.accounting.beginningbalance;

import java.sql.Connection;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;

public abstract class BeginningBalanceSheetDetail implements Comparable,Cloneable {
	protected long m_index = 0;
	protected Account m_account = Account.nullObject();
	protected Transaction m_trans;
	protected double m_accValue;
	protected double m_exchangeRate = 1.0;
	protected Currency m_currency = Currency.nullObject();
	public static final String ESDIFF = "Expense Sheet Difference";
	public static final String CASHINADVANCE = "Cash In Advance";
	public static final String EMPRECEIVABLE = "Employee Receivable";
	public static final String ACCRECEIVABLE = "Account Receivable";
	public static final String ACCPAYABLE = "Account Payable";
	public static final String BANK = "Bank";
	public static final String CASH = "Cash";
	public static final String BALANCE_SHEET = "Balance Sheet";
	protected static final String[] m_atype = new String[] { BALANCE_SHEET, CASH, BANK,
			ACCPAYABLE, ACCRECEIVABLE, EMPRECEIVABLE };	
	
	private boolean showReferenceNo = false;

	public Account getAccount() {
		return m_account;
	}

	public void setAccount(Account account) {
		m_account = account;
	}

	public double getAccValue() {
		return m_accValue;
	}

	public void setAccValue(double accvalue) {
		m_accValue = accvalue;
	}

	public Currency getCurrency() {
		return m_currency;
	}

	public void setCurrency(Currency currency) {
		m_currency = currency;
	}

	public long getIndex() {
		return m_index;
	}

	public void setIndex(long index) {
		m_index = index;
	}

	public double getExchangeRate() {
		return m_exchangeRate;
	}

	public void setExchangeRate(double rate) {
		m_exchangeRate = rate;
	}

	public static short typeFromStringToID(String type) {
		short len = (short) m_atype.length;
		for (short i = 0; i < len; i++) {
			if (m_atype[i].equals(type))
				return i;
		}
		return -1;
	}

	public String toString() {
		// diubah dikit
		if(this.showReferenceNo){
			if(this.m_trans!=null)
				return m_trans.getReference();
		}
		return m_account.toStringWithCode();
	}

	public Object subsidiaryStr() {
		return null;
	}

	public int compareTo(Object o) {
		if (o instanceof BeginningBalanceSheetDetail)
		{
			BeginningBalanceSheetDetail o1 = (BeginningBalanceSheetDetail) o;
			if (getAccValue() > o1.getAccValue())
				return 1;
			else if (getAccValue() < o1.getAccValue())
				return -1;
			else 
				return hashCode() - o1.hashCode();
		}
		else if (o instanceof BeginningBalanceSheetEntry)
		{
			return entry().compareTo(o);
		}
		return 1;
	}
    BeginningBalanceSheetEntry m_entry;
	public BeginningBalanceSheetEntry entry() {
		return m_entry;
	}
	public Object clone()
	{
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error("Cloning failure!",e);
		}
	}

    public void preload(Connection connection, long sessionid, String modul) {
        
    }
    protected abstract Unit unit();

    public TransactionDetail createTransactionDetail()
    {
    	// changed
        TransactionDetail det = 
        	new TransactionDetail(m_account, m_accValue, m_currency, m_exchangeRate,
        			unit(), indexSubsidiary());
        return det;
    }

	public Transaction getTrans() {
		return m_trans;
	}

	public void setTrans(Transaction trans) {
		m_trans = trans;
	}

	// nambah
	protected abstract long indexSubsidiary();
	
	// nambah lagi
	public void showReferenceNo(boolean show){
		this.showReferenceNo = show;
	}
}
