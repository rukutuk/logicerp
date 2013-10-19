package pohaci.gumunda.titis.accounting.entity;

import java.text.DecimalFormat;
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
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class PmtProjectCost extends TransactionTemplateEntity  implements GeneralVoucherToGetTotal,GeneralJasperVoucherProject{
	long index=0;
	String paymentsource;
	CashAccount cashAccount;
	BankAccount bankAccount;
	String chequeno;
	Date chequeDueDate;
	String payTo;
	double amount;
	ProjectData projectData;
	ProjectData project;
	PmtProjectCostDetail[] pmtProjectCostDetail;
	double total;
	//Tambahan Cok Gung 25 Mei 2007 Tolong dicek DiMappernya a bos!!!
	public void setProjectDetail(PmtProjectCostDetail[] temp){
		pmtProjectCostDetail=temp;
	}
	public PmtProjectCostDetail[] getProjectDetail(){
		return pmtProjectCostDetail;
	}
	/*public ProjectData getProjectData(){
		return projectData;
	}
	public void setProjectData(ProjectData projectdata){
		this.projectData=projectdata;
	}*/
/*	public long getProject(){
		return projectData.getIndex();
	}
	public void setProject(long projectdata){
		this.projectData.setIndex(projectdata);
	}*/
	public ProjectData getProject(){
		return project;
	}
	public void setProject(ProjectData projectdata){
		this.project=projectdata;
	}

	public PmtProjectCostDetail[] getPmtProjectCostDetail() {
		return pmtProjectCostDetail;
	}

	public void setPmtProjectCostDetail(PmtProjectCostDetail[] pmtProjectCostDetail) {
		this.pmtProjectCostDetail = pmtProjectCostDetail;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public PmtProjectCost() {

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

	public void setCashAccount(CashAccount cashAccount) {
		this.cashAccount = cashAccount;
	}

	public Date getChequeDueDate() {
		return chequeDueDate;
	}

	public void setChequeDueDate(Date chequeduedate) {
		this.chequeDueDate = chequeduedate;
	}

	public String getChequeno() {
		return chequeno;
	}

	public void setChequeno(String chequeno) {
		this.chequeno = chequeno;
	}

	public String getPaymentSource() {
		return paymentsource;
	}

	public void setPaymentSource(String paymentsource) {
		this.paymentsource = paymentsource;
	}

    /*
	public long getProject() {
		return project;
	}

	public void setProject(long project) {
		this.project = project;
	}*/

//////////////////////////

//	 View part - common voucher columns
	public Object vgetPaymentSource() {
		if (isSourceBank())
			return getBankAccount();
		else
			return getCashAccount();
	}

	public Object vgetVoucherType() {
		return "Project Cost";
	}
	public Object vgetVoucherNo() {
		return this.getReferenceNo();
	}

	public Object vgetVoucherDate() {
		return getTransactionDate();
	}
	public Object vgetPayTo() {
		/*boolean isPayToBank= "BANK".equalsIgnoreCase(getPayTo());   //equalsIgnoreCase(getPayTo());
		if (isPayToBank)
			return getBankAccount();
		else
			return getCashAccount();*/
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

	public long vgetIndex() {
		return this.index;
	}

	public String vgetSymbolCurrency() {
		return this.currency.getSymbol();
	}

	public CashAccount vgetCashAccount() {
		return this.cashAccount;
	}
	public BankAccount vgetBankAccount() {
		return this.bankAccount;
	}


	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION
		// siapkan khusus untuk work in progress
		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_WORK_IN_PROGRESS);
		Account wipAcct = null;
		if (vas!=null)
			wipAcct = vas.getAccount();

		// bikin si pemberi
		// amount-nya diminus soalnya ga pada tempatnya
		ArrayList detailList = new ArrayList();

		// nyang nerima/buat apa (syusah....)
		PmtProjectCostDetail[] dets = getPmtProjectCostDetail();
		double tempamount=0;
		CashBankAccount sourceAccount = getSourceAccount();
		for (int i = 0; i < dets.length; i++) {
			TransactionDetail detail = null;
			if (dets[i].getAccount().equals(sourceAccount.getAccount()))
				detail = createNewTransDet(dets[i].getAccount(), dets[i]
						.getaccValue(), dets[i].getCurrency(), dets[i]
						.getExchangeRate(), sourceAccount.getIndex());
			else if (dets[i].getAccount().equals(wipAcct))
				detail = createNewTransDet(dets[i].getAccount(), dets[i]
				        .getaccValue(), dets[i].getCurrency(), dets[i]
				        .getExchangeRate(), getProject().getIndex());
			else
				detail = createNewTransDet(dets[i].getAccount(), dets[i]
						.getaccValue(), dets[i].getCurrency(), dets[i]
						.getExchangeRate());
			tempamount = tempamount + dets[i].getaccValue();
			detailList.add(detail);
		}
		//setAmount(tempamount);

		/*	TransactionDetail sourceDetail =
		createNewTransDet(sourceAccount.getAccount(), -getAmount(), sourceAccount.getIndex());
		detailList.add(sourceDetail);*/

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
					IConstants.PAYMENT_PROJECT_COST,
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
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, false);

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

	private CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	public String getPayTo() {
		return payTo;
	}
	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}
	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#igetPaymentSource()
	 */
	public String igetPaymentSource() {
		return getPaymentSource();
	}
	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}
	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#vgetTotal()
	 */
	public String vgetTotal() {
		DecimalFormat dm = new DecimalFormat("#,##0.00");

		return dm.format(getTotal());
	}

}
