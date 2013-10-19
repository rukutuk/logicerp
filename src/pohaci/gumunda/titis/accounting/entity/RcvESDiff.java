package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEsDiff;
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

public class RcvESDiff extends TransactionTemplateEntity implements GeneralReceipt{
	private long index;
	private String receiveTo;
	BeginningEsDiff beginningBalance;
	private CashAccount cashAccount;
	private BankAccount bankAccount;
	private ExpenseSheet esNo;
	private double amount;
		

	
	public String getPaymentSource() {
		return receiveTo;
	}
	public void setPaymentSource(String paymentSource) {
		this.receiveTo = paymentSource;
	}
	public boolean isBank() {
		return "BANK".equalsIgnoreCase(getPaymentSource());
	}
	//============================
	public RcvESDiff(){	
		
	}
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

	public ExpenseSheet getEsNo() {
		return esNo;
	}
	
	public void setEsNo(ExpenseSheet esNo) {
		this.esNo = esNo;
	}
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTIONS
		
		// buat untuk receiving bank/cash-nya
		CashBankAccount receivingAcct = getReceivingAccount();
		
		TransactionDetail detailSource = createNewTransDet(receivingAcct.getAccount(), getAmount(), receivingAcct.getIndex());
		ArrayList detailList = new ArrayList();
		detailList.add(detailSource);
		
		// buat yang selisih expense sheet
		VariableAccountSetting vas = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_ES_DIFF);
		TransactionDetail transDetail =null;
		if (getEsNo()!=null){
			transDetail = 
				/*createNewTransDet(vas.getAccount(), -getEsNo().getAmount(), 
						getEsNo().getCurrency(), getEsNo().getExchangeRate());*/
				createNewTransDet(vas.getAccount(), -getAmount(), 
						getEsNo().getCurrency(), getEsNo().getExchangeRate(),
						getEsNo().getEsOwner().getIndex());
		}
		else{
			transDetail = 
				/*createNewTransDet(vas.getAccount(), -getEsNo().getAmount(), 
						getEsNo().getCurrency(), getEsNo().getExchangeRate());*/
				createNewTransDet(vas.getAccount(), -getAmount(), 
						getBeginningBalance().getCurrency(), getBeginningBalance().getExchangeRate(),
						getBeginningBalance().getEmployee().getIndex());
		}
		detailList.add(transDetail);
		
		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, IDBConstants.MODUL_ACCOUNTING);
		
		String attr = "";
		String type = "";
		String code = "";
		if(receivingAcct instanceof BankAccount){
			// ke bank
			type = ReferenceNoGeneratorHelper.BANK_IN;
			code = ((BankAccount)receivingAcct).getCode();
			attr = IConstants.ATTR_RCV_BANK;
		} else {
			// ke kas
			type = ReferenceNoGeneratorHelper.CASH_IN;
			code = ((CashAccount)receivingAcct).getUnit().getCode();
			attr = IConstants.ATTR_RCV_CASH;
		}
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		// get the reference no first
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		// this is waktunya...
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
		setStatus((short)StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
		
		// UNTESTED
		// UNTESTED
		// UNTESTED
	}

	CashBankAccount getReceivingAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	
	////
	
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
	public Transaction getTrans() {
		return trans;
	}
	public void setTrans(Transaction trans) {
		this.trans = trans;
	}
	public String getReceiveTo() {
		return receiveTo;
	}
	public void setReceiveTo(String receiveTo) {
		this.receiveTo = receiveTo;
	}
	public BeginningEsDiff getBeginningBalance() {
		return beginningBalance;
	}
	public void setBeginningBalance(BeginningEsDiff temp) {
		this.beginningBalance = temp;
	}
	
}
