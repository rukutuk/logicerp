package pohaci.gumunda.titis.accounting.beginningbalance;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.NumberRounding;

public class BeginningBalance {
	protected long m_index = 0;
	protected JournalStandard m_journalStandard;
//	protected long m_trans = 0;
	protected Transaction m_trans;
	protected Date m_balanceDate = null;
//	protected short m_type = -1;
	protected Unit m_defaultUnit;
	protected Account m_bridgeAccount;
	long m_sessionId;
	protected BeginningBalanceSheetEntry[] m_detail= new BeginningBalanceSheetEntry[0];
	private NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
	
	
	/*public BeginningBalance(Date date, short type) {
	 m_date = date;
	 m_type = type;
	 }
	 
	 public BeginningBalance(long index, Date date, short type, long trans) {
	 m_index = index;
	 m_date = date;
	 m_type = type;
	 m_trans = trans;
	 }
	 
	 public BeginningBalance(long index, BeginningBalance balance) {
	 m_index = index;
	 m_date = balance.getDate();
	 m_type = balance.getType();
	 m_trans = balance.getTransaction();
	 }*/
	
	public long getIndex() {
		return m_index;
	}
	
	
	public Date getBalanceDate() {
		return m_balanceDate;
	}
	
//	public short getType() {
//	return m_type;
//	}
	
//	private String getTypeAsString(){
//	if(m_type < 0 || m_type >= BeginningBalanceSheetDetail.m_atype.length)
//	return "";
//	else
//	return BeginningBalanceSheetDetail.m_atype[m_type];
//	}
	
	public void setEntries(BeginningBalanceSheetEntry[] detail) {
		m_detail = detail;
	}
	
	public BeginningBalanceSheetEntry[] getBeginningBalanceSheetDetail() {
		return m_detail;
	}
	double totDebit,totCredit;
	void accumulateDC(TransactionDetail trans)
	{
		Account acct = trans.getAccount();
		double value = trans.getValue() * trans.getExchangeRate();
		value = nr.round(value);
		short balanceCode = acct.getBalance();
		boolean isCredit = (balanceCode != 0);
		if (value < 0)
		{
			isCredit = !isCredit;
			value = -value;
		}
		value = nr.round(value);
		if (isCredit)
			totCredit += value;
		else
			totDebit += value;
		
	}
	private void submitGroupByUnit(Hashtable unitToDetailList, BeginningBalanceSheetDetail detail)
	{
		
		Unit unit = detail.unit();
		if (unit==null)
			unit = m_defaultUnit;
		if (!unitToDetailList.containsKey(unit))
		{
			unitToDetailList.put(unit, new ArrayList());
		}
		List list = (List) unitToDetailList.get(unit);
		list.add(detail);
	}
	public List submit(Connection conn, long sessionId, Hashtable unitToDetailList, ArrayList entriesWithoutUnit) throws NotBalancedException
	{
		int i,j;
		//Hashtable unitToDetailList = new Hashtable(); // i remove it
		//ArrayList entriesWithoutUnit = new ArrayList(); // i remove it too
		totDebit = 0.0; totCredit = 0.0;
		for (i=0; i<m_detail.length; i++)
		{
			BeginningBalanceSheetDetail[] lowestDetails = m_detail[i].getDetails();
			if (lowestDetails.length>0)
			{
				for (j=0; j< lowestDetails.length; j++)
					submitGroupByUnit(unitToDetailList,lowestDetails[j]);
			} else {
				entriesWithoutUnit.add(m_detail[i]); 
			}
		}
		
		Date now = new Date();
		Unit unit = null;
		Enumeration enumeration = unitToDetailList.keys();
		ArrayList transactionList = new ArrayList();
		ArrayList allTransactionList = new ArrayList();
		while (enumeration.hasMoreElements())
		{
			unit = (Unit) enumeration.nextElement();
			Unit unitWithCode = new Unit(unit,Unit.CODE_DESCRIPTION);
			
			// i add these two lines
			ReferenceNoGeneratorHelper helper = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, "");
			String refNo = helper.createBeginningBalanceReferenceNo(unitWithCode.getCode(), now);
			
			Transaction t = new Transaction("Beginning Balance " + unitWithCode.toString(), getBalanceDate(), now, null, refNo, m_journalStandard.getJournal(),m_journalStandard, Transaction.VERIFIED, unit);
			List detailList = (List) unitToDetailList.get(unit);
			ArrayList transDetailList = new ArrayList();
			Iterator detailIter = detailList.iterator();
			totDebit = 0.0; totCredit = 0.0;
			while (detailIter.hasNext())
			{
				BeginningBalanceSheetDetail entryItem = (BeginningBalanceSheetDetail) detailIter.next();
				TransactionDetail trans = entryItem.createTransactionDetail();
				transDetailList.add(trans);
				allTransactionList.add(trans);
				accumulateDC(trans);
			}
			
			if (unit.equals(m_defaultUnit))
			{
				detailIter = entriesWithoutUnit.iterator();
				while (detailIter.hasNext())
				{
					// yang ga ada subsidiary-nya ya?
					BeginningBalanceSheetEntry entryItem = (BeginningBalanceSheetEntry) detailIter.next();
					TransactionDetail trans = new TransactionDetail(entryItem.getAccount(), entryItem.getAccValue(), entryItem.getCurrency(),
							entryItem.getExchangeRate(),
							m_defaultUnit, -1);
					transDetailList.add(trans);
					allTransactionList.add(trans);
					accumulateDC(trans);
				}
				
			}
			TransactionDetail trans = createBridgeTransaction();
			transDetailList.add(trans);          
			accumulateDC(trans);
			totDebit = nr.round(totDebit);
			totCredit = nr.round(totCredit);
			if (totDebit != totCredit)
			//if (totDebit - totCredit > 1E-4 || totCredit - totDebit > 1E-4)
			{
				throw new RuntimeException("Bridge transaction has incorrect value or side! " + Double.toString(totDebit-totCredit));
			}
			TransactionDetail[] transDetailArray 
			= (TransactionDetail[]) transDetailList.toArray(new TransactionDetail[transDetailList.size()]);
			// i add these
			for(int x=0; x<transDetailArray.length; x++){
				TransactionDetail det = transDetailArray[x];
				det.setUnit(unit);
			}
			t.setTransactionDetail(transDetailArray);
			t.setUnit(unit);
			
			transactionList.add(t);
			
			System.out.println(t.dumpInfo());
		}
		totDebit = 0.0; totCredit = 0.0;
		Iterator iterator = allTransactionList.iterator();
		while (iterator.hasNext())
		{
			TransactionDetail transactionDetail= (TransactionDetail) iterator.next();
			accumulateDC(transactionDetail);
		}
		//if (totDebit - totCredit > 1E-4 || totCredit - totDebit > 1E-4)
		totDebit = nr.round(totDebit);
		totCredit = nr.round(totCredit);
		if (totDebit != totCredit)
		{
			throw new NotBalancedException(totDebit, totCredit);
		}
		return transactionList;
	}
	AccountingBusinessLogic m_logic;
	AccountingBusinessLogic logic()
	{
		return m_logic;
	}
	private TransactionDetail createBridgeTransaction() {
		double delta = totDebit-totCredit;
		if (m_bridgeAccount.getBalance()==0)
		{
			delta = -delta;
		}
		Currency baseCurrency = null;
		try {
			baseCurrency = logic().getBaseCurrency(m_sessionId,IDBConstants.MODUL_ACCOUNTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ini buat apa ya?
		return new TransactionDetail(m_bridgeAccount,delta, baseCurrency, 1.0, m_defaultUnit, -1 );
	}
	
	public Account getBridgeAccount() {
		return m_bridgeAccount;
	}
	
	public void setBridgeAccount(Account bridgeAccount) {
		m_bridgeAccount = bridgeAccount;
	}
	
	public Unit getDefaultUnit() {
		return m_defaultUnit;
	}
	
	public void setDefaultUnit(Unit defaultUnit) {
		m_defaultUnit = defaultUnit;
	}
	
	public void setBalanceDate(Date date) {
		m_balanceDate = date;
	}
	
	public void init(Connection conn, long sessionid) {
		this.m_logic = new AccountingBusinessLogic(conn);
		this.m_sessionId = sessionid;
	}
	public static class NotBalancedException extends Exception {
		private static final long serialVersionUID = 1L;
		double debitValue,creditValue;
		
		NotBalancedException(double d, double c)
		{
			super("Begginning Balance not Balanced");
			this.debitValue = d;
			this.creditValue = c;
		}
		
		public double getDebitValue() {
			return debitValue;
		}
		
		public double getCreditValue() {
			return creditValue;
		}
	}
	public Transaction getTrans() {
		return m_trans;
	}
	
	public void setTrans(Transaction trans) {
		m_trans = trans;
	}
	
	public void setIndex(long index) {
		m_index = index;
	}
	
	
	public JournalStandard getJournalStandard() {
		return m_journalStandard;
	}
	
	
	public void setJournalStandard(JournalStandard journal) {
		m_journalStandard = journal;
	}
}