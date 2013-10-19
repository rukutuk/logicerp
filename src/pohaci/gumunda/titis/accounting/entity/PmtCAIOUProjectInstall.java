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

public class PmtCAIOUProjectInstall extends TransactionTemplateEntity{
	PmtCAIOUProject pmtcaiouproject;
	//Date submitdate  ;
	String paymentsource;
	private CashAccount cashAccount ;
	private BankAccount bankAccount  ;
	double amount;
	private long index;
	public PmtCAIOUProjectInstall() {
	}
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
	public String getPaymentsource() {
		return paymentsource;
	}
	public void setPaymentsource(String paymentsource) {
		this.paymentsource = paymentsource;
	}
	public PmtCAIOUProject getPmtcaiouproject() {
		return pmtcaiouproject;
	}
	public void setPmtcaiouproject(PmtCAIOUProject pmtcaiouproject) {
		this.pmtcaiouproject = pmtcaiouproject;
	}
	public String toString() {    
		return this.transactionDate.toString()+" Amount :"+this.getAmount()+" Status :"+this.statusInString();
	}
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION
		
		// bikin si pemberi
		CashBankAccount sourceAccount = getSourceAccount();
		TransactionDetail sourceDetail = 
			createNewTransDet(sourceAccount.getAccount(), -getAmount(), sourceAccount.getIndex());
		ArrayList detailList = new ArrayList();
		detailList.add(sourceDetail);
		
		// nyang nerima
		// yaitu...... uang muka project i owe you
		VariableAccountSetting vas = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_CA_IOU_PROJECT);
		TransactionDetail caiou = createNewTransDet(vas.getAccount(), getAmount(), getPmtcaiouproject().getPayTo().getIndex());
		detailList.add(caiou);
		
		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String type = "";
		String code = "";
		String attr = "";
		if(sourceAccount instanceof BankAccount){
			// artinya dikeluarken dari bank
			type = ReferenceNoGeneratorHelper.BANK_OUT;
			code = ((BankAccount)sourceAccount).getCode();
			attr = IConstants.ATTR_PMT_BANK;
		}else{
			// buat yang cash
			type = ReferenceNoGeneratorHelper.CASH_OUT;
			code = ((CashAccount)sourceAccount).getUnit().getCode();
			attr = IConstants.ATTR_PMT_CASH;
		}
	
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT,
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
		setStatus((short)StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
		
		// UNTESTED !!!!
		// UNTESTED !!!!
		// UNTESTED !!!!
	}
	protected CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
}
