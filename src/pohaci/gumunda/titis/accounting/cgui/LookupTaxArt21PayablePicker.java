package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21Ho;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21HoDet;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;


public class LookupTaxArt21PayablePicker extends LookupPicker{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ATR_JOURNAL;
	private Vector paidTaxPayableIdxVector;
	private Unit unit;
	
	public LookupTaxArt21PayablePicker(Connection conn, long sessionid, Unit unit) {
		super(conn, sessionid, "Tax Art 21 Payable");
		this.unit = unit;
		setSize(800, 300);

		initPaidTaxPayable();
		setColumn();
		initData();		
	}
	
	
	  
	private void initPaidTaxPayable() {
		// cara yang aneh
		// tapi sudahlah...
		
		paidTaxPayableIdxVector = new Vector();
    	GenericMapper mapper = MasterMap.obtainMapperFor(PayrollPmtTax21HoDet.class);
    	mapper.setActiveConn(m_conn);
    	
		List payrollPaymentDetailList =  mapper.doSelectAll();
		Iterator iterator = payrollPaymentDetailList.iterator();
		while(iterator.hasNext()){
			PayrollPmtTax21HoDet payrollPmtTax21HoDet = (PayrollPmtTax21HoDet) iterator.next();
			PayrollPmtTax21Ho payrollPmtTax21Ho = payrollPmtTax21HoDet.getPayrollPmtTax21Ho();
			
			if(payrollPmtTax21Ho!=null){
				if(payrollPmtTax21Ho.getStatus()==StateTemplateEntity.State.POSTED){
					if(payrollPmtTax21HoDet.getTrans()!=null)
						paidTaxPayableIdxVector.add(new Long(payrollPmtTax21HoDet.getTrans().getIndex()));
				}
			}
		}
	}
	
	private void setColumn() {
		getModel().addColumn("No");
    	getModel().addColumn("Date");
    	getModel().addColumn("Description");
    	getModel().addColumn("Unit Code");   
    	getModel().addColumn("Account");
    	getModel().addColumn("Amount");
	}

	void initData() {
		String journalStandardListOfIndex = getJournalStandardListOfIndex();
		String accountAllowedListOfIndex = geAccountAllowedListOfIndex();
		
		GenericMapper mapper = MasterMap.obtainMapperFor(Transaction.class);
		mapper.setActiveConn(m_conn);
		
		String whereClause = IDBConstants.ATTR_UNIT + "=" + unit.getIndex() + " AND " +
				IDBConstants.ATTR_STATUS + "=3" + " AND " + IDBConstants.ATTR_ISVOID + "=false" + " AND " +
 				IDBConstants.ATTR_TRANSACTION_CODE + " IN (" + journalStandardListOfIndex + ")";
		List transactionList = mapper.doSelectWhere(whereClause);
		
		Iterator iterator = transactionList.iterator();
		int no = 1;
		while(iterator.hasNext()){
			Transaction transaction = (Transaction) iterator.next();
			
			if(isPaid(transaction))
				continue;
			
			mapper = MasterMap.obtainMapperFor(TransactionDetail.class);
			mapper.setActiveConn(m_conn);
			
			whereClause = "TRANS=" + transaction.getIndex() + " AND ACCOUNT IN (" + accountAllowedListOfIndex + ")";
			List detailList = mapper.doSelectWhere(whereClause);
			Iterator iter = detailList.iterator();
			while(iter.hasNext()){
				TransactionDetail detail = (TransactionDetail) iter.next();
				detail.setTransaction(transaction);
				
				getModel().addRow(new Object[]{
						new Integer(no),
						transaction.getPostedDate(),
						transaction,
						unit,
						detail.getAccount().getName(),
						new Double(detail.getValue()),
						detail}
				);
				
				no++;
			}
		}
	}
	
	private boolean isPaid(Transaction transaction) {
		return paidTaxPayableIdxVector.contains(new Long(transaction.getIndex()));
	}

	private String geAccountAllowedListOfIndex() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);
		
		List journalStandardSettingList = helper.getJournalStandardSettingWithAccount(IConstants.PAYROLL_PAYMENT_TAX21_HO);
		
		String listOfIndex = "";
		Iterator iterator = journalStandardSettingList.iterator();
		while(iterator.hasNext()){
			JournalStandardSetting journalStandardSetting = (JournalStandardSetting) iterator.next();
			JournalStandard journalStandard = journalStandardSetting.getJournalStandard();
			
			if(journalStandard!=null){
				JournalStandardAccount[] journalStandardAccounts = journalStandard.getJournalStandardAccount();
				
				for(int i=0; i<journalStandardAccounts.length; i++){
					if(journalStandardAccounts[i].getBalance()==0) // maksa
						listOfIndex += ", " + journalStandardAccounts[i].getAccount().getIndex();
				}
			}
		}
		
		listOfIndex = listOfIndex.replaceFirst(", ", "");

		return listOfIndex;
	}

	private String getJournalStandardListOfIndex() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);
		
		String whereClausa = 
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_TAX21 + "'";
		
		List journalStandardSettingList = helper.getJournalStandardSettingList(whereClausa);
		
		String journalStandardListOfIndex = "";
		Iterator iterator = journalStandardSettingList.iterator();
		while(iterator.hasNext()){
			JournalStandardSetting journalStandardSetting = (JournalStandardSetting) iterator.next();
			
			journalStandardListOfIndex += journalStandardSetting.getJournalStandard().getIndex();
			
			if(iterator.hasNext())
				journalStandardListOfIndex += ", ";
		}
		
		// khusus beginning balance
		AccountingSQLSAP sql = new AccountingSQLSAP();
		BeginningBalance begBal = sql.getBeginningBalance((short)1, m_conn);
		JournalStandard jsBB = null;
		if (begBal!=null)
			jsBB = begBal.getJournalStandard();
		
		if (jsBB!=null){
			if(journalStandardListOfIndex.equals(""))
				journalStandardListOfIndex += jsBB.getIndex();
			else
				journalStandardListOfIndex += (", " + jsBB.getIndex());
		}
		
		return journalStandardListOfIndex;
	}

	
	int rowindex ;
    public void select() {
    	rowindex = m_table.getSelectedRow();
    	if(rowindex != -1) {
    		Object obj = getModel().getValueAt(rowindex, 6);
    		setObject(obj);
    	}
    }
}
