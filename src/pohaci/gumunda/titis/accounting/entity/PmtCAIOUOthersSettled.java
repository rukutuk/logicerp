package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class PmtCAIOUOthersSettled extends TransactionTemplateEntity{
	
	long index ;
	PmtCAIOUOthers pmtcaiouothers;
	String chequeno;
	Date chequeduedate;
	String paymentsource      ;
	CashAccount cashAccount  ;
	BankAccount bankAccount  ;
	double amount   ;
	public PmtCAIOUOthersSettled() {

	}
	public Date getChequeduedate() {
		return chequeduedate;
	}

	public void setChequeduedate(Date chequeduedate) {
		this.chequeduedate = chequeduedate;
	}

	public String getChequeno() {
		return chequeno;
	}

	public void setChequeno(String chequeno) {
		this.chequeno = chequeno;
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
	public void setBankAccount(BankAccount bankaccount) {
		this.bankAccount = bankaccount;
	}
	public CashAccount getCashAccount() {
		return cashAccount;
	}
	public void setCashAccount(CashAccount cashaccount) {
		this.cashAccount = cashaccount;
	}
	public String getPaymentsource() {
		return paymentsource;
	}
	public void setPaymentsource(String paymentsource) {
		this.paymentsource = paymentsource;
	}
	public PmtCAIOUOthers getPmtcaiouothers() {
		return pmtcaiouothers;
	}
	public void setPmtcaiouothers(PmtCAIOUOthers pmtcaiouothers) {
		this.pmtcaiouothers = pmtcaiouothers;
	}
	
	
	public String toString() {		
		return referenceNo;
	}
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION
		
		// 1st TRANSACTION: PEMBERIAN UANG MUKA PROYEK
		
		// bikin si pemberi
		BankAccount sourceAccount = getBankAccount(); 
		
		TransactionDetail sourceDetail = 
			createNewTransDet(sourceAccount.getAccount(), -getAmount(), sourceAccount.getIndex());
		ArrayList detailList = new ArrayList();
		detailList.add(sourceDetail);
			
		
		// nyang nerima
		// yaitu...... uang muka project i owe you
		VariableAccountSetting vas = 
			VariableAccountSetting
			.createVariableAccountSetting(conn, sessionId, 
					IConstants.ATTR_VARS_CA_OTHER);
		TransactionDetail caiou = createNewTransDet(vas.getAccount(), getAmount(), getPmtcaiouothers().getPayTo().getIndex());
		detailList.add(caiou);
		 System.out.println("Ini source Uang Muka Normal"+vas.getAccount().getBalanceAsString());
			
		
		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String type = ReferenceNoGeneratorHelper.BANK_OUT;
		String code = sourceAccount.getCode();
		String attr = IConstants.ATTR_CA_SETTLED;
			
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT,
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
		
		//short ubah=1;
		//Ini digunakan unutk meminus nilai amount Bank
		//details[0].setValue(-details[0].getValue());
		
	
		trans.setTransactionDetail(details);
		
		// save lah
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
	 // 2nd TRANSACTION: PEMBERIAN UANG MUKA PROYEK
		
		// bikin si pemberi = cash advance i owe you proyek
		// tapi ntar aja deh di ceking
		/*==detailList = new ArrayList();
		vas = VariableAccountSetting
			.createVariableAccountSetting(conn, sessionId, 
					IConstants.ATTR_VARS_CA_OTHER);
		sourceDetail = createNewTransDet(vas.getAccount(), getAmount());
		detailList.add(sourceDetail);
				
		// nyang nerima
		// yaitu...... cash
		// aku serahin ke ceking aja deh
		
		// GET THE STANDARD JOURNAL
		
		/*
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		*/
		
		// nyang ini tipenya cash in
	////	type = ReferenceNoGeneratorHelper.CASH_IN;
		// code = sourceAccount.getCode();
	/*==	attr = IConstants.ATTR_CA_SETTLED_REC;
			
		journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		setting = (JournalStandardSetting) journalStdList.get(0);
		journal = setting.getJournalStandard();
		
		// get the reference no first
		//noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING);
		//setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		//	this is waktunya...
		trans = createNewTrans(journal, getUnit());
		details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getCurrency(), getExchangeRate(), true);

		trans.setTransactionDetail(details);
		
		// save lah
		logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		===*/
		
		// get everything related
		setStatus((short)StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
		
		// UNTESTED !!!!
		// UNTESTED !!!!
		// UNTESTED !!!!
	}
}
