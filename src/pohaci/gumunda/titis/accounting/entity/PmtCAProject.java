package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.Date;
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
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class PmtCAProject extends TransactionTemplateEntity implements GeneralVoucher{	
	String chequeNo ;
	Date chequedueDate ;
	Employee payTo;
	ProjectData project;
	double requestedAmount ;
	long index ;
	//Transaction trans   ;
	CashAccount cashAccount ;
	BankAccount bankAccount  ;
	double amount;
	String paymentSource;
	
	
	public PmtCAProject() {
		super();
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

	public void setBankAccount(BankAccount bankaccount) {
		this.bankAccount = bankaccount;
	}
	
	public CashAccount getCashAccount() {
		return cashAccount;
	}

	public void setCashAccount(CashAccount cashaccount) {
		this.cashAccount = cashaccount;
	}

	public Date getChequedueDate() {
		return chequedueDate;
	}
	public void setChequedueDate(Date chequedueDate) {
		this.chequedueDate = chequedueDate;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public Employee getPayTo() {
		return payTo;
	}
	public void setPayTo(Employee payTo) {
		this.payTo = payTo;
	}
	
	public double getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(double requestedAmount) {
		this.requestedAmount = requestedAmount;
	}
	public Transaction getTrans() {
		return trans;
	}
	public void setTrans(Transaction trans) {
		this.trans = trans;
	}
	public ProjectData getProject() {
		return project;
	}
	public void setProject(ProjectData project) {
		this.project = project;
	}
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long autoindex) {
		this.index = autoindex;
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
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_CA_PROJECT);
		TransactionDetail caiou = createNewTransDet(vas.getAccount(), getAmount(), getPayTo().getIndex());
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
					IConstants.PAYMENT_CASHADVANCE_PROJECT,
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
	////////////

	
		/*public Object vgetVoucherDate() {
			return this.getDateOriginator();
			JournalStandardSettingPickerHelper helper;
			Object attr;
			List journalStdList = 
				helper.getJournalStandardSettingWithAccount(
						IConstants.PAYMENT_OPERASIONAL_COST,
						attr);
			
			// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
			JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
			JournalStandard journal = setting.getJournalStandard();
			
			// get the reference no first
			ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING);
			setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
			
			//	this is waktunya...
			Transaction trans = createNewTrans(journal, getUnit());
			TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
			
			TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
			details = transHelper.prepareJournalStandardTransactionDetail(
					journal.getJournalStandardAccount(), details, getCurrency(), getExchangeRate(), true);
			
			trans.setTransactionDetail(details);
			
			// save lah
			AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
			trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
			
			// get everything related
			setStatus((short)StateTemplateEntity.State.SUBMITTED);
			setSubmitDate(trans.getVerifyDate());
			setTrans(trans);
			
			System.out.println("DONE");
			
			// UNTESTED !!!!
			// UNTESTED !!!!
			// UNTESTED !!!!
		}
*/
	public void setPaymentSource(String paymentSource) {
		this.paymentSource = paymentSource;
	}
	public String getPaymentSource() {
		return paymentSource;
	}
	

	private CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	
//	 View part - common voucher columns
	public Object vgetPaymentSource() {
		if (isSourceBank())
			return getBankAccount();
		else
			return getCashAccount();
	}

	public Object vgetVoucherType() {
		return "Cash Advance Project";
	}
	public Object vgetVoucherNo() {
		return this.getReferenceNo();
	}
	
	public Object vgetVoucherDate() {
		return getTransactionDate();
	}
	public Object vgetPayTo() {
		return getPayTo();
	}

	public boolean isSourceBank() {
		return "BANK".equalsIgnoreCase(getPaymentSource());
	}
	
	public Object vgetOriginator() {
		return this.getEmpOriginator();
	}
	public Object vgetApprovedBy() {
		return this.getEmpApproved();
	}
	public Object vgetReceivedBy() {
		return this.getEmpReceived();
	}
	public Object vgetUnitCode() {
		return getUnit();
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
	public Object vgetSubmitDate() {
		return getSubmitDate();
	}
	
	public String toString() {    
		return this.referenceNo;
	}
}
