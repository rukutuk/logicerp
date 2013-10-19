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
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.helper.TransactionDetailPreparingHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class PurchaseReceipt  extends TransactionTemplateEntity implements GeneralPurchaseReceipt {
	long index;
	//Transaction trans;
	String invoice;
	Date invoiceDate;
	Partner supplier;
	String supplierType;
	ProjectData project;
	String projectType;
	Currency apCurr;
	double amount;
	double apexChRate;
	double vatPercent;
	Currency vatCurr;
	double vatAmount;
	String bankAccount;
	Date duedate;
	String contractNo;
	Date contractDate;
	PurchaseReceiptItem[] purchaseDetail;
	public PurchaseReceipt(){		
	}
	
	public PurchaseReceiptItem[] getPurchaseDetail() {
		return purchaseDetail;
	}


	public void setPurchaseDetail(PurchaseReceiptItem[] purchaseDetail) {
		this.purchaseDetail = purchaseDetail;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}


	public Currency getApCurr() {		
		return apCurr;
	}

	public void setApCurr(Currency apCurr) {		
		this.apCurr = apCurr;		
	}

	public double getApexChRate() {
		return apexChRate;
	}

	public void setApexChRate(double apexChRate) {
		this.apexChRate = apexChRate;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Date getDuedate() {
		return duedate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}


	public ProjectData getProject() {
		return project;
	}

	public void setProject(ProjectData project) {
		this.project = project;
	}
	
	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

		

	public Partner getSupplier() {
		System.err.println("getSupplier : " + supplier);
		return supplier;
	}

	public void setSupplier(Partner supplier) {
		this.supplier = supplier;
	}

	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	/*public Transaction getTrans() {
		return trans;
	}

	public void setTrans(Transaction trans) {
		this.trans = trans;
	}*/

	
	
	public double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public Currency getVatCurr() {
		return vatCurr;
	}

	public void setVatCurr(Currency vatCurr) {
		this.vatCurr = vatCurr;
	}

	public double getVatPercent() {
		return vatPercent;
	}

	public void setVatPercent(double vatPercent) {
		this.vatPercent = vatPercent;
	}

	public Object vgetReceiptNo() {		
		return this.referenceNo;
	}

	public Object vgetReceiptDate() {
		return this.transactionDate;
	}

	public Object vgetProjectCode() {		
		return this.project;
	}

	public Object vgetInvoiceNo() {
		return this.invoice;
	}

	public Object vgetInvoiceDate() {
		return this.invoiceDate;
	}

	public Object vgetSupplier() {
		return this.supplier;
	}

	public Object vgetSubmittedDate() {		
		return this.submitDate;
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
	
	public long vgetIndex() {
		return this.index;
	}
	
	public String vgetSymbolCurrency() {
		return this.vatCurr.getSymbol();
	}
	
	public String toString(){
		return this.referenceNo;
	}

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		
		setCurrency(getApCurr());
		//setExchangeRate(getExchangeRate());
		setExchangeRate(getApexChRate());
		VariableAccountSetting account1 = null;
		TransactionDetail transDet1 = null;
		if (getProjectType().equals("CURRENT")) {
			account1 = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_MTRL_TOOLS_CONS);
			transDet1 = createNewTransDet(account1.getAccount(), getAmount());
		} else {
			account1 = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_WORK_IN_PROGRESS);
			transDet1 = createNewTransDet(account1.getAccount(), getAmount(), getProject().getIndex());
		}
		 
		ArrayList detailList = new ArrayList();
		detailList.add(transDet1);
		
		VariableAccountSetting account2 = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_AP);
		TransactionDetail transDet2 = createNewTransDet(account2.getAccount(), (getAmount()+getVatAmount()), getSupplier().getIndex());
		detailList.add(transDet2);
		
		if (getSupplierType().equals("PKP")){
			VariableAccountSetting account3 = 
				VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_IN_VAT);
			TransactionDetail transDet3 = createNewTransDet(account3.getAccount(), getVatAmount());
			detailList.add(transDet3);
		}
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String attr = "";
		if (getSupplierType().equals("PKP")){	
			attr = IConstants.ATTR_PURCHASE_RECEIPT_PKP;			
		}else{			
			attr = IConstants.ATTR_PURCHASE_RECEIPT_NONPKP;
		}
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PURCHASE_RECEIPT,
					attr);
		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		//posisinya disni
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createPurchaseReceiptReferenceNo(getUnit().getCode(), getTransactionDate()));
		
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, true);

		trans.setTransactionDetail(details);
		
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		setStatus((short)StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
}
