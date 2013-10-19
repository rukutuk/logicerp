package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.List;
import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.helper.TransactionDetailPreparingHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class RcvOthers extends TransactionTemplateEntity implements GeneralReceipt{

	long index;	
	//Transaction trans ;        
	String receiveTo ;  
	CashAccount cashAccount ;        
	BankAccount bankAccount ;       
	JournalStandard journal ;        
	String receiveFrom  ; 
	Organization department ;     
	double amount;
	RcvOthersDetail[] rcvOthersDetail;	
	
	public double getAmount() {
		double amt = 0;
		if(rcvOthersDetail!=null){
			for(int i=0; i<rcvOthersDetail.length; i++){
				amt += rcvOthersDetail[i].getaccValue();
			}
		}
		amount = amt;
		return amount;
	}

	public RcvOthers() {
		super();
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long autoindex) {
		this.index = autoindex;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public CashAccount getCashAccount() {
		return cashAccount;
	}

	public void setCashAccount(CashAccount cashAccount) {
		this.cashAccount = cashAccount;
	}

	public Organization getDepartment() {
		return department;
	}

	public void setDepartment(Organization department) {
		this.department = department;
	}

	public JournalStandard getJournal() {
		return journal;
	}

	public void setJournal(JournalStandard journal) {
		this.journal = journal;
	}

/*	public Bank getReceivefrom() {
		return receivefrom;
	}

	public void setReceivefrom(Bank receivefrom) {
		this.receivefrom = receivefrom;
	}*/

	public String getReceiveFrom() {
		return receiveFrom;
	}

	public void setReceiveFrom(String receiveFrom) {
		this.receiveFrom = receiveFrom;
	}

	public String getReceiveTo() {
		return receiveTo;
	}

	public void setReceiveTo(String receiveTo) {
		this.receiveTo = receiveTo;
	}

	/*public Transaction getTrans() {
		return trans;
	}

	public void setTrans(Transaction trans) {
		this.trans = trans;
	}*/

	public Object vgetReceiveType() {		
		return "Unit Bank/Cash Transfer";
	}

	public Object vgetReceiptNo() {
		return this.referenceNo;
	}
	
	public Object vgetReceiptDate() {	
		return this.transactionDate;
	}
	
	public Object vgetReceiveFrom() {		
		return this.empReceived;
	}
	
	public Object vgetReceiveAccount() {
		return this.bankAccount;
	}
	
	public Object vgetUnitCode() {
		return this.unit;
	}
	
	public Object vgetStatus() {
		int status = getStatus();
		switch (status){
		case 0:
			return "Not Submitted";
		case 1:
			return "Submitted";
		case 2:
			return "Submitted";
		case 3:
			return "Posted";
		default:
			return "";
		}
	}
	public Object vgetSubmittedDate() {
		return this.submitDate;
	}

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		CashBankAccount receivingAccount = getReceivingAccount();
		ArrayList detailList = new ArrayList();
		RcvOthersDetail[] dets = getRcvOthersDetail();		
		for(int i=0; i<dets.length; i++){
			
			JournalStandard js = getJournal();		
			
			GenericMapper mapper=MasterMap.obtainMapperFor(JournalStandardAccount.class);
			mapper.setActiveConn(conn);		
			List rs=mapper.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+js.getIndex() + " AND " + 
					IDBConstants.ATTR_ACCOUNT + "=" + dets[i].getAccount().getIndex());			
			JournalStandardAccount jsAccount;
			
			TransactionDetail detail;
			if (rs.size()>0){
				jsAccount = (JournalStandardAccount)rs.get(0);
				if (!jsAccount.isCalculate()){
					if (dets[i].getAccount().getIndex()==jsAccount.getAccount().getIndex()){	
						if (jsAccount.getBalance()==dets[i].getAccount().getBalance())
							detail=createNewTransDet(dets[i].getAccount(), dets[i].getaccValue(),
									dets[i].getCurrency(), dets[i].getExchangerate(), dets[i].getSubsidiAry());
						else
							detail=createNewTransDet(dets[i].getAccount(),(-dets[i].getaccValue()),
									dets[i].getCurrency(), dets[i].getExchangerate(), dets[i].getSubsidiAry());
						detailList.add(detail);		
					}	
				}
			}
		}
	
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String type = "";
		String code = "";
		if(receivingAccount instanceof BankAccount){
			type = ReferenceNoGeneratorHelper.BANK_IN;
			code = ((BankAccount)receivingAccount).getCode();
		}else{
			type = ReferenceNoGeneratorHelper.CASH_IN;
			code = ((CashAccount)receivingAccount).getUnit().getCode();
		}
	
		JournalStandard journal = getJournal();
		journal = helper.getJournalStandardWithAccount(journal.getIndex());
				
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);		
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();

		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, true);
		
		trans.setTransactionDetail(details);
		
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		setStatus((short)StateTemplateEntity.State.SUBMITTED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);
	}
	
	private CashBankAccount getReceivingAccount() {
		if(getBankAccount()!=null)
			return getBankAccount();
		if(getCashAccount()!=null)
			return getCashAccount();
		return null;
	}

	public RcvOthersDetail[] getRcvOthersDetail() {
		return rcvOthersDetail;
	}
	
	public boolean isSourceBank() {
		return "BANK".equalsIgnoreCase(getReceiveTo());
	}

	public void setRcvOthersDetail(RcvOthersDetail[] rcvOthersDetail) {
		this.rcvOthersDetail = rcvOthersDetail;
	}
}
