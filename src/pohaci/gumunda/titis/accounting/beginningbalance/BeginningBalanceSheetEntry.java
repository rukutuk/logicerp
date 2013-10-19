package pohaci.gumunda.titis.accounting.beginningbalance;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.entity.Account;

/**
 */

public class BeginningBalanceSheetEntry implements Comparable {
	public static final short ACCOUNTPAYABLE = 1,
	  ACCOUNTRECEIVABLE = 2,
	  BANK = 3,
	  CASH = 4,
	  CASHADVANCE = 5,
	  EMPRECEIVABLE = 6,
	  ESDIFF = 7,
	  LOAN = 8, // Company loan
	  WORKINPROGRESS = 9;
	protected Account m_account = Account.nullObject();

	protected Currency m_currency = Currency.nullObject();

	protected double m_accValue = 0.0;

	protected double m_exchangeRate = 0.0;

	protected short m_type = 0;
	
	protected boolean m_calculate;
	
	protected long m_index;
	
	protected BeginningBalanceSheetDetail[] m_details = new BeginningBalanceSheetDetail[0];
	
	protected Transaction trans;
	
	/**
	 * @return the trans
	 */
	public Transaction getTrans() {
		return trans;
	}

	/**
	 * @param trans the trans to set
	 */
	public void setTrans(Transaction trans) {
		this.trans = trans;
	}

	public void beforeSave()
	{
		if (m_type != 0)
		{
			m_calculate = true;
			recalculate();
		}
	}
	
    public void recalculate()
    {
    	if (m_details.length==0)
    	{
    		m_accValue = 0;
    		return;
    	}
    	int i;
    	double total = 0;
    	Currency lastCurr = null;
    	boolean normalized = false;
    	for (i=0; i< m_details.length;i++)
    	{
    		double v1 = m_details[i].getAccValue() * m_details[i].getExchangeRate();
    		if (m_details[i].getCurrency()==null || m_details[i].getCurrency().getIndex()==0)
    			JOptionPane.showMessageDialog(null,"Warning: currency in one of the details are unset!");
    		else
    		{
    			if (lastCurr == null)
    				lastCurr = m_details[i].getCurrency();
    			else if (!lastCurr.equals(m_details[i].getCurrency()))
    			{  // beda euy!
    				normalized = true;
    				lastCurr = null;
    			}
    		}
    		total += v1;
    	}
		if (!normalized && m_details.length>0)
		{
			setCurrency(m_details[0].getCurrency()); // assume same currency
			total = total / m_details[0].getExchangeRate();
			setExchangeRate(m_details[0].getExchangeRate());
		}
		else if (m_details.length>0){
			setExchangeRate(1.0);
		}
    	setAccValue(total);
    	
    }
    
	public String toString() {
		return m_account.getName() + " [" + m_account.getCode() + "]";
	}

	public Account getAccount() {
		return m_account;
	}

	public void setAccount(Account account) {
		m_account = account;
	}

	public Currency getCurrency() {
		return m_currency;
	}

	public void setCurrency(Currency currency) {
		m_currency = currency;
	}

	public BeginningBalanceSheetDetail[] getDetails() {
		return m_details;
	}

	public void setDetails(BeginningBalanceSheetDetail[] details) {
		m_details = details;
		int i;
		if (details!=null)
			for (i=0; i<details.length;i++)
			{
				details[i].m_entry = this;
			}
	}

	public double getExchangeRate() {
		return m_exchangeRate;
	}

	public void setExchangeRate(double rate) {
		m_exchangeRate = rate;
	}

	public double getAccValue() {
		return m_accValue;
	}

	public void setAccValue(double value) {
		m_accValue = value;
	}

	public short getType() {
		return m_type;
	}

	public void setType(short typeCode) {
		m_type = typeCode;
	}
    public boolean equals(Object o)
    {
        if (o instanceof BeginningBalanceSheetEntry)
        {
            BeginningBalanceSheetEntry o1 = (BeginningBalanceSheetEntry) o;            
            return m_account.equals(o1.m_account);
        }
        return false;        
    }
	public int compareTo(Object o) {
		if (o instanceof BeginningBalanceSheetEntry)
		{
			BeginningBalanceSheetEntry o1 = (BeginningBalanceSheetEntry) o;
			int result = m_account.getCode().compareTo(o1.m_account.getCode());
			if (result!=0) return result;
			return (m_account.hashCode() - o1.m_account.hashCode());
		} else if (o instanceof BeginningBalanceSheetDetail)
		{
			BeginningBalanceSheetDetail o1 = (BeginningBalanceSheetDetail) o;
			return compareTo(o1.entry());
		}
		else return 1;
	}

	public boolean isCalculate() {
		return m_calculate;
	}

	public void setCalculate(boolean calculate) {
		m_calculate = calculate;
	}

	public long getIndex() {
		return m_index;
	}

	public void setIndex(long index) {
		m_index = index;
	}
    static class TypeCodeInfo {
    	Class childClass;
    	Class formatClass;
    	Object createChild() throws Exception
    	{
    		if (childClass==null) return null;
    		return childClass.getConstructor(new Class[0]).newInstance(null);
    	}
    	Object createFormat() throws Exception
    	{
    		if (formatClass==null) return null;
    		return formatClass.getConstructor(new Class[0]).newInstance(null);
    	}
    }
    public TypeCodeInfo typeCodeToInfo() {
    	TypeCodeInfo result = new TypeCodeInfo();
    	Class childClass = null, formatClass = null;
    	switch (getType())
		  {
		  case BeginningBalanceSheetEntry.ACCOUNTPAYABLE:
			  childClass = BeginningAccountPayable.class;
			  formatClass = BeginningBalanceChildFormats.AccountPayableFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.ACCOUNTRECEIVABLE:
			  childClass = BeginningAccountReceivable.class;
			  formatClass = BeginningBalanceChildFormats.AccountReceivableFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.BANK:
			  childClass = BeginningBankDetail.class;
			  formatClass = BeginningBalanceChildFormats.BankAccountFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.CASH:
			  childClass = BeginningCashDetail.class;
			  formatClass = BeginningBalanceChildFormats.CashAccountFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.CASHADVANCE:
			  childClass = BeginningCashAdvance.class;
			  formatClass = BeginningBalanceChildFormats.CashAdvanceFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.EMPRECEIVABLE:
			  childClass = BeginningEmpReceivable.class;
			  formatClass = BeginningBalanceChildFormats.EmployeeReceivableFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.ESDIFF:
			  childClass = BeginningEsDiff.class;
			  formatClass = BeginningBalanceChildFormats.CashAdvanceFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.LOAN:
			  childClass = BeginningLoan.class;
			  formatClass = BeginningBalanceChildFormats.LoanFmt.class;
			  break;
		  case BeginningBalanceSheetEntry.WORKINPROGRESS:
			  childClass = BeginningWorkInProgress.class;
			  formatClass = BeginningBalanceChildFormats.WorkInProgressFmt.class;
		  }
	    result.childClass=childClass;
	    result.formatClass=formatClass;
    	return result;
    }
	public Class typeCodeToClass() {
		return typeCodeToInfo().childClass;
	}
	public Object createChildTableFormat() throws Exception {
		return typeCodeToInfo().createFormat();
	}
	public BeginningBalanceSheetDetail createChild() throws Exception {
		
		Object object = typeCodeToInfo().createChild();
		BeginningBalanceSheetDetail retval = (BeginningBalanceSheetDetail) object;
		retval.setAccount(this.m_account);
		retval.m_entry = this;
		return retval;
	}
	public BeginningBalanceSheetEntry()
	{
	}
	public BeginningBalanceSheetEntry(BeginningBalanceSheetEntry from)
	{
		this.m_account = from.m_account;
		this.m_accValue  = from.m_accValue;
		this.m_calculate = from.m_calculate;
		this.m_currency = from.m_currency;
		this.m_details = new BeginningBalanceSheetDetail[from.m_details.length];
		int i;
		for (i=0; i<m_details.length;i++)
			m_details[i] = (BeginningBalanceSheetDetail) from.m_details[i].clone();
		this.m_exchangeRate = from.m_exchangeRate;
		this.m_index = from.m_index;
		this.m_type = from.m_type;
	}
	
}