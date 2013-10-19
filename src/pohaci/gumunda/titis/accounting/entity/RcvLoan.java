package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.cgui.Currency;
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

public class RcvLoan extends TransactionTemplateEntity implements GeneralReceipt{
	private long index;
	CompanyLoan companyLoan;
	String receiveTo; // KAS atau BANK
	CashAccount cashAccount;
	BankAccount bankAccount;
	Currency currency;
	//Transaction trans;
	double amount;
	public CompanyLoan getCompanyLoan() {
		return companyLoan;
	}

	public void setCompanyLoan(CompanyLoan companyLoan) {
		this.companyLoan = companyLoan;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public RcvLoan() {
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

	public String toString() {
		return getReferenceNo();
	}

	///////

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
		case 3:
			return "Posted";
		case 2:
			return "Submitted";
		default:
			return "";
		}

		//return new Integer(this.status);
	}
	public Object vgetSubmittedDate() {
		return this.submitDate;
	}

	//-------->

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION

		// bikin si penerimanya
		CashBankAccount receivingAccount = getReceivingAccount();
		TransactionDetail receivingDetail =
			createNewTransDet(receivingAccount.getAccount(), getAmount(), receivingAccount.getIndex());

		ArrayList detailList = new ArrayList();
		detailList.add(receivingDetail);

		// nyang ngasih
		///CompanyLoan loan = getCompanyLoan1();
		CompanyLoan loan = getCompanyLoan();
		TransactionDetail loanDetail =
			createNewTransDet(loan.getAccount(), getAmount(), loan.getIndex());
		detailList.add(loanDetail);

		//String creditorType = loan.getCreditorList().getCreditorType();

		// GET THE STANDARD JOURNAL

		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId,
					IDBConstants.MODUL_ACCOUNTING);

		String type = "";
		String code = "";
		String attr = "";

		if (getBankAccount()!=null){
			type = ReferenceNoGeneratorHelper.BANK_IN;
			code = ((BankAccount)receivingAccount).getCode();
			attr = IConstants.ATTR_RCV_BANK;
		}
		else if (getCashAccount()!=null){
			type = ReferenceNoGeneratorHelper.CASH_IN;
			code = ((CashAccount)receivingAccount).getCode();
			attr = IConstants.ATTR_RCV_CASH;
		}

		/*if(getBankAccount.equalsIgnoreCase("bank")){
			// artinya diterima di bank
			type = ReferenceNoGeneratorHelper.BANK_IN;
			code = ((BankAccount)receivingAccount).getCode();
			attr = IConstants.ATTR_RCV_LOAN_BANK;
		}else{
			// buat yang cash
			type = ReferenceNoGeneratorHelper.BANK_IN;
			code = ((BankAccount)receivingAccount).getCode();
			attr = IConstants.ATTR_RCV_LOAN_NON_BANK;
		}*/

		List journalStdList =
			helper.getJournalStandardSettingWithAccount(
					IConstants.RECEIVE_LOAN,
					attr);
		/*List journalStdList =
			helper.getJournalStandardSettingWithAccount(
					IConstants.RECEIVE_LOAN);*/

		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();

		// get the reference no first
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));

		//	this is waktunya...
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList
										.toArray(new TransactionDetail[detailList.size()]);

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
	}

	private CashBankAccount getReceivingAccount() {
		if(getBankAccount()!=null)
			return getBankAccount();
		if(getCashAccount()!=null)
			return getCashAccount();
		return null;
	}
	//Tambahan cok gung
	public boolean isSourceBank() {
		return "BANK".equalsIgnoreCase(getReceiveTo());
	}
}

