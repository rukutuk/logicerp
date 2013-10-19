package pohaci.gumunda.titis.accounting.entity;
import java.util.ArrayList;
import java.util.List;

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

public class RcvUnitBankCashTrns extends TransactionTemplateEntity implements GeneralReceipt{
	public RcvUnitBankCashTrns() {
		
	}
	
	long index;
	String referenceNo;
	Unit rcvUnitCode;
	String receiveTo; // KAS atau BANK
	CashAccount cashAccount;
	BankAccount bankAccount;
	long transferFrom;
	PmtUnitBankCashTrans transferFrom1; // ga tau dari kelas apa, trus darimana donks?? meneketehe :)
	double exchangeRate;
	double amount;
	String description;
	//Unit unit;
	
	public long getTransferFrom(){
		return this.transferFrom;
	}
	public void setTransferFrom(long a){
		this.transferFrom=a;
	}
//	Tambahan dari CokGung
	/*
	private String paymentSource;
	
	//ga ada paymentsource atribute di table RcvUnitBankCashTrns
	private String paymentSource;
	
	public String getPaymentSource() {
		return paymentSource;
	}
	public void setPaymentSource(String paymentSource) {
		this.paymentSource = paymentSource;
	}
	public boolean isBank() {
		return "BANK".equalsIgnoreCase(getPaymentSource());
	}
	//============================*/
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
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
	
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public Unit getRcvUnitCode() {
		return rcvUnitCode;
	}
	public void setRcvUnitCode(Unit rcvUnitCode) {
		this.rcvUnitCode = rcvUnitCode;
	}
	public String getReceiveTo() {
		return receiveTo;
	}
	public void setReceiveTo(String receiveTo) {
		this.receiveTo = receiveTo;
	}
	
	public PmtUnitBankCashTrans getTransferFrom1() { // kayaknya ini deh classnya :)
		return transferFrom1;
	}
	public void setTransferFrom1(PmtUnitBankCashTrans transferFrom) { // kayaknya ini deh classnya :)
		this.transferFrom1 = transferFrom;
	}
	
	////
	public Object vgetReceiveType() {		
		return "Unit Bank/Cash Transfer";
	}
	public Object vgetReceiptNo() {
		return getReferenceNo();
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
		return getRcvUnitCode();
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
	
	/*
	public RcvUnitBankCashTrns(long index, Transaction trans, String referenceNo, 
			Date transactionDate, short status, Date submitDate, 
			Unit rcvUnitCode, String receiveTo, CashAccount cashAccount, 
			BankAccount bankAccount, PmtUnitBankCashTrans transferFrom, Currency currency, 
			double exchangeRate, double amount, String description, 
			String remarks, Employee empOriginator, 
			String jobTitleOriginator, Date dateOriginator, Employee empApproved,
			String jobTitleApproved, Date dateApproved, Employee empReceived,
			String jobTitleReceived, Date dateReceived) {
		super();
		
		this.index = index;
		this.trans = trans;
		this.referenceNo = referenceNo;
		this.transactionDate = transactionDate;
		this.status = status;
		this.submitDate = submitDate;
		this.rcvUnitCode = rcvUnitCode;
		this.receiveTo = receiveTo;
		this.cashAccount = cashAccount;
		this.bankAccount = bankAccount;
		this.transferFrom = transferFrom;
		this.currency = currency;
		this.exchangeRate = exchangeRate;
		this.amount = amount;
		this.description = description;
		this.remarks = remarks;
		this.empOriginator = empOriginator;
		this.jobTitleOriginator = jobTitleOriginator;
		this.dateOriginator = dateOriginator;
		this.empApproved = empApproved;
		this.jobTitleApproved = jobTitleApproved;
		this.dateApproved = dateApproved;
		this.empReceived = empReceived;
		this.jobTitleReceived = jobTitleReceived;
		this.dateReceived = dateReceived;
	}
*/

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION
		
		PmtUnitBankCashTrans transferFrom = getTransferFrom1();
		double rcvExchangeRate = transferFrom.getExchangeRate();
		
		// oke, buat si penerimanya dulu
		CashBankAccount receivingAccount = getReceivingAccount();
		double amt = getAmount();
		
		CashBankAccount sourceAccount = transferFrom.getSourceAccount();
	
		if (sourceAccount.getCurrency().getIndex() != receivingAccount
				.getCurrency().getIndex()) {
			if (receivingAccount.getCurrency().getIndex() == igetBaseCurrency()
					.getIndex()) {
				// rcv = Rp; src = $
				amt = amt * rcvExchangeRate;
			} else {
				// rcv = $; src = Rp
				amt = amt / rcvExchangeRate;
			}
		}
		
		double exchRateRcv = rcvExchangeRate;
		if (receivingAccount.getCurrency().getIndex() == igetBaseCurrency()
				.getIndex())
			exchRateRcv = 1;
		
		TransactionDetail receivingDetail = 
			createNewTransDet(receivingAccount.getAccount(), amt, receivingAccount.getCurrency(), exchRateRcv, receivingAccount.getIndex());
		ArrayList detailList = new ArrayList();
		detailList.add(receivingDetail);
		
		// nyang hubungan rc:
		// dicalculate aja deh....
		/*VariableAccountSetting vas = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, 
					IConstants.ATTR_VARS_RC_REL);
		TransactionDetail rcDetail = 
			createNewTransDet(vas.getAccount(), getAmount());
		detailList.add(rcDetail);*/
		
		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String type = "";
		String code = "";
		String attr = "";
		if(receivingAccount instanceof BankAccount){
			// artinya diterima di bank
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
					IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		// get the reference no first
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		//	this is waktunya...
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, true);

		trans.setTransactionDetail(details);
		
		// save lah
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		// get everything related
		setStatus((short)StateTemplateEntity.State.SUBMITTED);
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
	
//	Tambahan cok gung
	public boolean isSourceBank() {
		return "BANK".equalsIgnoreCase(getReceiveTo());
	}
}
