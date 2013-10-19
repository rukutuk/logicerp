package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryHo;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryHoDet;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupPayrollCompPayablePicker extends LookupPicker{
	private static final long serialVersionUID = 1L;
	String ATR_JOURNAL;
	private Unit unit;
	private GenericMapper m_entityMapper;  
	private Vector paidPayrollPayableTransactionIdxVector;
	
	public LookupPayrollCompPayablePicker(Connection conn, long sessionid,Unit unit) {
		super(conn, sessionid, "Payroll Component Payable");
		setSize(800, 300);
		m_entityMapper = MasterMap.obtainMapperFor(PayrollPmtSlryHo.class);
		m_entityMapper.setActiveConn(m_conn);
		this.unit=unit;
		
		initPaidPayrollPayable();
		setColumn();
		showData();
	}


	private void initPaidPayrollPayable() {
		// cara yang aneh
		// tapi sudahlah...
		
		paidPayrollPayableTransactionIdxVector = new Vector();
    	GenericMapper mapper = MasterMap.obtainMapperFor(PayrollPmtSlryHoDet.class);
    	mapper.setActiveConn(m_conn);
    	
		List payrollPaymentDetailList =  mapper.doSelectAll();
		Iterator iterator = payrollPaymentDetailList.iterator();
		while(iterator.hasNext()){
			PayrollPmtSlryHoDet payrollPmtSlryHoDet = (PayrollPmtSlryHoDet) iterator.next();
			PayrollPmtSlryHo payrollPmtSlryHo = payrollPmtSlryHoDet.getPayrollPmtSlryHo();
			
			if(payrollPmtSlryHo!=null){
				if(payrollPmtSlryHo.getStatus()==StateTemplateEntity.State.POSTED){
					if(payrollPmtSlryHoDet.getTrans()!=null)
						paidPayrollPayableTransactionIdxVector.add(new Long(payrollPmtSlryHoDet.getTrans().getIndex()));
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
	
	private void showData() {
		String journalStandardListOfIndex = getJournalStandardListOfIndex();
		String accountAllowedListOfIndex = geAccountAllowedListOfIndex();
		
		GenericMapper mapper = MasterMap.obtainMapperFor(Transaction.class);
		mapper.setActiveConn(m_conn);
		
		String strWhere = IDBConstants.ATTR_UNIT + "=" + unit.getIndex()
				+ " AND " + IDBConstants.ATTR_STATUS + "=3" + " AND "
				+ IDBConstants.ATTR_ISVOID + " = FALSE AND "
				+ IDBConstants.ATTR_TRANSACTION_CODE + " IN ("
				+ journalStandardListOfIndex + ")";
		System.err.println(strWhere);
		String whereClause = strWhere;
		List transactionList = mapper.doSelectWhere(whereClause);
		
		Iterator iterator = transactionList.iterator();
		int no = 1;
		while(iterator.hasNext()){
			Transaction transaction = (Transaction) iterator.next();
			
			if(isPaid(transaction))
				continue;
			
			mapper = MasterMap.obtainMapperFor(TransactionDetail.class);
			mapper.setActiveConn(m_conn);
			
			strWhere = "TRANS=" + transaction.getIndex() + " AND ACCOUNT IN (" + accountAllowedListOfIndex + ")";
			System.err.println(strWhere);
			List detailList = mapper.doSelectWhere(strWhere);
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
		return paidPayrollPayableTransactionIdxVector.contains(new Long(transaction.getIndex()));
	}

	private String geAccountAllowedListOfIndex() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);		
		List journalStandardSettingList = helper.getJournalStandardSettingWithAccount(IConstants.PAYROLL_PAYMENT_SALARY_HO);		
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
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_PAYCHEQUES + "' OR " +
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_MEAL_ALLOWANCES + "' OR " +
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES + "' OR " +
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_OVERTIME + "' OR " +
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_OTHER_ALLOWANCE + "'";
		System.err.println(whereClausa);
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
    
    public TransactionDetail getSelectedTransactionDetail(){
    	if(rowindex != -1) {
    		return (TransactionDetail) getModel().getValueAt(rowindex, 6);
    	}
    	return null;
    }
	
	
   /* private void initAllowedJStandardJournal() {
		JournalStandardSettingPickerHelper helper = 
			new JournalStandardSettingPickerHelper(m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);
		
		String whereClausa = 
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_PAYCHEQUES + "' OR " +
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_MEAL_ALLOWANCES + "' OR " +
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES + "' OR " +
			IDBConstants.ATTR_APPLICATION + "='" + IConstants.PAYROLL_VERIFICATION_OVERTIME + "'";			
		
		List list = helper.getJournalStandardSettingList(whereClausa);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			JournalStandardSetting object = (JournalStandardSetting) iterator.next();
			
			allowedStandardJournalHashtable.put(new Long(object.getJournalStandard().getIndex()), object);
		}
	}
	//just to make sure that he is not kill him self 
	public boolean isPayrollPmtSlryHo(Transaction t){
		return payrollPmtSlryHo_vector.contains(new Long(t.getIndex()));
	}
	
	//apakah sudah dibayarkan
	public boolean isPaid(Transaction a){
		return payrollPmtSlryHoDET_vector.contains(new Long(a.getIndex()));
	}
	
    private void initPayrollPmtSlryHoDETPosted(){
    	payrollPmtSlryHoDET_vector = new Vector();
    	entityMapper = MasterMap.obtainMapperFor(PayrollPmtSlryHoDet.class);
		entityMapper.setActiveConn(m_conn);
		List payrollPmtSlryHoDets =  entityMapper.doSelectAll();
		Iterator iter = payrollPmtSlryHoDets.iterator();
		while(iter.hasNext()){
			PayrollPmtSlryHoDet payrollPmtSlryHoDet2 = (PayrollPmtSlryHoDet)iter.next();
			PayrollPmtSlryHo payrollPmtSlryHo3 = payrollPmtSlryHoDet2.getPayrollPmtSlryHo();
			//masukkan yg udah di posted
			if(payrollPmtSlryHo3!=null && (payrollPmtSlryHo3.getStatus()== StateTemplateEntity.State.POSTED))
				payrollPmtSlryHoDET_vector.add(new Long(payrollPmtSlryHoDet2.getTrans().getIndex()));
		}
		
    }
	  
    void initPayrollPmtSlryHo() {
    	payrollPmtSlryHo_vector = new Vector();
    	List payrollPmtSlryHo_List = m_entityMapper.doSelectAll();
    	Iterator iter = payrollPmtSlryHo_List.iterator();
    	while(iter.hasNext()){
    		PayrollPmtSlryHo payrollPmtSlryHo2 = (PayrollPmtSlryHo)iter.next();
    		if(payrollPmtSlryHo2.getTrans()!=null)
    			payrollPmtSlryHo_vector.add(new Long(payrollPmtSlryHo2.getTrans().getIndex()));
    	}
    }
	private void headerColumnFirst() {
		getModel().addColumn("No");
    	getModel().addColumn("Date");
    	getModel().addColumn("Description");
    	getModel().addColumn("Unit Code");   
    	getModel().addColumn("Account");
    	getModel().addColumn("Amount");
	}

	private void showData() {
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			JournalStandardSetting[] jss = logic.getJournalStandardSetting(
					m_sessionid, IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT,
					IConstants.PAYROLL_PAYMENT_SALARY_HO);
			
			for (int v = 0; v < jss.length; v++) {
				//if (jss[j].getAttribute().equalsIgnoreCase(ATR_JOURNAL)) {
				JournalStandard journalStandard = (JournalStandard) jss[v].getJournalStandard();
				JournalStandardAccount[] JSAccount = logic.getJournalStandardAccount(m_sessionid,
						IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT, journalStandard
						.getIndex());
				//+ harus nya d ambil transacion yg bukan dirinya sendiri
				Transaction[] transaction = logic.getTransactionByUnit(m_sessionid,
						IDBConstants.MODUL_MASTER_DATA, unit.getIndex());
				// Coba aku ganti;
				GenericMapper mapper = MasterMap.obtainMapperFor(Transaction.class);
				// coba dipersempit aja...
				List list = mapper.doSelectWhere("UNIT=" + unit.getIndex());
				Transaction[] transaction = (Transaction[]) list.toArray(new Transaction[list.size()]);
				
				TransactionDetail[] transactionDetail;
				int k=0;
				for(int t=0;t<JSAccount.length;t++){
					if(JSAccount[t].getBalanceAsString().equalsIgnoreCase("debit"))
						for (int j=0;j< transaction.length;j++){
							//ambil transaction yg sudah posted
							if(transaction[j].getStatus()==StateTemplateEntity.State.POSTED){
								//apakah transaksi berasal dari PayrollPmtSlryHo (Accounting)
								if(isPayrollPmtSlryHo(transaction[j]))
									continue;
					
								//apakah transaksi yg berasal dari  HRM sudah diBayarkan
								if(isPaid(transaction[j]))
									continue;
								
								// apakah transaksi yang berasal dari HRM yang memang tidak 
								// untuk dibayarkan
								if(isNotPaid(transaction[j]))
									continue;
							
								
								//ambil posted_Transaction yg detail2-nya ajah
								transactionDetail=logic.getTransactionDetail(m_sessionid,
										IDBConstants.MODUL_MASTER_DATA, transaction[j].getIndex());	      
								for(int i = 0; i < transactionDetail.length;  i++) {
									transactionDetail[i].setTransaction(transaction[j]);
									//get transactionDetail which has interested Account
									if(transactionDetail[i].getAccount().getCode().
											equals(JSAccount[t].getAccount().getCode())){
										getModel().
										addRow(new Object[]{
												new Integer(k+1),
												transaction[j].getPostedDate(),
												transaction[j],
												unit,
												transactionDetail[i].getAccount().getName(),
												new Double(transactionDetail[i].getValue()),
												transactionDetail[i]}
										);
										k=k+1;
									}
								}
							}
						}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
    private boolean isNotPaid(Transaction transaction) {
		
		return !allowedStandardJournalHashtable.containsKey(new Long(transaction.getJournalStandard().getIndex()));
	}
    
	int rowindex ;
    public void select() {
    	rowindex = m_table.getSelectedRow();
    	if(rowindex != -1) {
    		Object obj = getModel().getValueAt(rowindex, 6);
    		setObject(obj);
    	}
    }
    
    public TransactionDetail getSelectedTransactionDetail(){
    	if(rowindex != -1) {
    		return (TransactionDetail) getModel().getValueAt(rowindex, 6);
    	}
    	return null;
    }*/
}
