package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.helper.TransactionDetailPreparingHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class RcvEmpReceivable extends TransactionTemplateEntity implements GeneralReceipt{
	
	long index;
	//Transaction trans;
	BeginningEmpReceivable beginningBalance;
	String receiveTo;           
	CashAccount cashAccount;           
	BankAccount bankAccount;         
	PmtEmpReceivable empReceivable;          
	double amount;

//	Tambahan dari CokGung
	
	private String paymentSource;
	
	public String getPaymentSource() {
		return paymentSource;
	}
	public void setPaymentSource(String paymentSource) {
		this.receiveTo=paymentSource;
		this.paymentSource = paymentSource;
	}
	public boolean isBank() {
		return "BANK".equalsIgnoreCase(getReceiveTo());
	}

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		CashBankAccount receivingAccount = getReceivingAccount();
		
		double amount = getAmount();
		if(receivingAccount.getCurrency().getIndex()!=getCurrency().getIndex())
			amount = (getAmount() * getExchangeRate());
		
		TransactionDetail receivingDetail = createNewTransDet(receivingAccount.getAccount(), amount, 
					receivingAccount.getCurrency(), getExchangeRate(), receivingAccount.getIndex());
		
		ArrayList detailList = new ArrayList();
		detailList.add(receivingDetail);		

		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, 
					IConstants.ATTR_VARS_EMP_REC);
		TransactionDetail rcDetail=null;
		if (!this.isBank()){
			if (getEmpReceivable()!=null)
				rcDetail =createNewTransDet(vas.getAccount(), getAmount(), getEmpReceivable().getCurrency(), getEmpReceivable().getExchangeRate(), getEmpReceivable().getPayTo().getIndex());
			else if (getBeginningBalance()!=null)
				rcDetail =createNewTransDet(vas.getAccount(), getAmount(), getBeginningBalance().getCurrency(), getBeginningBalance().getExchangeRate(), getBeginningBalance().getEmployee().getIndex());
		}else{
			if (getEmpReceivable()!=null)
				rcDetail =createNewTransDet(vas.getAccount(), getAmount(),getEmpReceivable().getCurrency(), getEmpReceivable().getExchangeRate(), getEmpReceivable().getPayTo().getIndex());
			else if (getBeginningBalance()!=null)
				rcDetail =createNewTransDet(vas.getAccount(), getAmount(),getBeginningBalance().getCurrency(), getBeginningBalance().getExchangeRate(), getBeginningBalance().getEmployee().getIndex());
		}
		
		vas.getAccount().setBalance((short)1);
		detailList.add(rcDetail);
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String type = "";
		String code = "";
		String attr = "";
		if(receivingAccount instanceof BankAccount){

			type = ReferenceNoGeneratorHelper.BANK_IN;
			code = ((BankAccount)receivingAccount).getCode();
			attr = IConstants.ATTR_RCV_BANK;
		}else{
			// buat yang cash
			type = ReferenceNoGeneratorHelper.CASH_IN;
			code = ((CashAccount)receivingAccount).getUnit().getCode();
			attr = IConstants.ATTR_RCV_CASH;
		}
	
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.RECEIVE_EMPLOYEE_RECEIVABLE,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		if(journalStdList.size()==0){
			throw new Exception("No journal standard");
		}		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		// get the reference no first
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		//	this is waktunya...
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		for (int i=0;i<details.length;i++){
			System.out.println("Nama"+details[i].getAccount().getName());
			System.out.println(details[i].getAccount().getBalanceAsString());
			System.out.println(details[i].getValue());
		}		
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, false);
		
		for (int i=0;i<details.length;i++){
			System.out.println("Nama"+details[i].getAccount().getName());
			System.out.println(details[i].getAccount().getBalanceAsString());
			System.out.println(details[i].getValue());
		}
		
		trans.setTransactionDetail(details);
		
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		setStatus((short)StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
		
	}
	private CashBankAccount getReceivingAccount() {
		if(getBankAccount()!=null)
			return getBankAccount();
		if(getCashAccount()!=null)
			return getCashAccount();
		return null;
	}
	
	//============================
	public RcvEmpReceivable() {
	
	}

	
	
   /////
	
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
	////
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
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
	public PmtEmpReceivable getEmpReceivable(){
		return empReceivable;
	}
	public void setEmpReceivable(PmtEmpReceivable empReceivable) {
		this.empReceivable = empReceivable;
	}
	/*public Employee getEmpReceivable() {
		return empReceivable;
	}
	public void setEmpReceivable(Employee empReceivable) {
		this.empReceivable = empReceivable;
	}
	*/
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
	public BeginningEmpReceivable getBeginningBalance() {
		return beginningBalance;
	}
	public void setBeginningBalance(BeginningEmpReceivable beginningBalance) {
		this.beginningBalance = beginningBalance;
	}
	
}
