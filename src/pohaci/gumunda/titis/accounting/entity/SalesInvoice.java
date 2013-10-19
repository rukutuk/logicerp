package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
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
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.ProjectData;


public class SalesInvoice extends TransactionTemplateEntity implements GeneralInvoice {
	long m_index;
	//Transaction m_trans;
	//Date m_submitDate;
	ProjectData m_project;
	Currency m_salesCurr;
	double m_salesExchRate;
	double salesAmount;
	String receiveFrom;
	CashAccount m_cashAccount;
	BankAccount m_bankAccount;
	double m_downPaymentAmount;  // double apa long ya.....
	Currency m_vatCurr;
	double m_vatAmount;
	String m_description;
	String m_remarks;
	Employee m_empAuthorize;
	String m_jobTitleAuthorize;
	Date m_dateAuthorize;
	Customer m_customer;
	Organization m_department;
	Activity m_activity;
	String m_briefDesc;
	double m_vatPercent;
	Currency m_downPaymentCurr;
	double m_vatExchRate;
	SalesAdvance m_salesAdvance;
	String m_attention;
	BeginningAccountReceivable beginningBalance;
	

	SalesItem[] salesItem;

	public SalesItem[] getSalesItem() {
		return salesItem;
	}

	public void setSalesItem(SalesItem[] salesItem) {
		this.salesItem = salesItem;
	}

	public double getVatExchRate() {
		return m_vatExchRate;
	}

	public void setVatExchRate(double exchRate) {
		m_vatExchRate = exchRate;
	}

	public double getVatPercent() {
		return m_vatPercent;
	}

	public void setVatPercent(double percent) {
		m_vatPercent = percent;
	}

	public String getBriefDesc() {
		return m_briefDesc;
	}

	public void setBriefDesc(String desc) {
		m_briefDesc = desc;
	}

	public SalesInvoice(){

	}

	public BankAccount getBankAccount() {
		return m_bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.m_bankAccount = bankAccount;
	}

	public CashAccount getCashAccount() {
		return m_cashAccount;
	}

	public void setCashAccount(CashAccount cashAccount) {
		this.m_cashAccount = cashAccount;
	}

	public double getDownPaymentAmount() {
		return m_downPaymentAmount;
	}

	public void setDownPaymentAmount(double downPaymentAmount) {
		m_downPaymentAmount = downPaymentAmount;
	}

	public Employee getEmpAuthorize() {
		return m_empAuthorize;
	}

	public void setEmpAuthorize(Employee empAuthorize) {
		m_empAuthorize = empAuthorize;
	}

	public long getIndex() {
		return m_index;
	}

	public void setIndex(long index) {
		m_index = index;
	}

	public String getJobTitleAuthorize() {
		return m_jobTitleAuthorize;
	}

	public void setJobTitleAuthorize(String jobTitleAuthorize) {
		m_jobTitleAuthorize = jobTitleAuthorize;
	}

	public ProjectData getProject() {
		return m_project;
	}

	public void setProject(ProjectData project) {
		m_project = project;
	}
	public Currency getSalesCurr() {
		return m_salesCurr;
	}

	public void setSalesCurr(Currency salesCurr) {
		m_salesCurr = salesCurr;
	}

	public double getSalesExchRate() {
		return m_salesExchRate;
	}

	public void setSalesExchRate(double salesExchRate) {
		m_salesExchRate = salesExchRate;
	}




	/*public Transaction getTrans() {
		return m_trans;
	}

	public void setTrans(Transaction trans) {
		m_trans = trans;
	}*/


	/*public void setTransactionDate(String transactionDate) {
		m_transactionDate = transactionDate;
	}*/

	public Currency getVatCurr() {
		return m_vatCurr;
	}

	public void setVatCurr(Currency vatCurr) {
		m_vatCurr = vatCurr;
	}

	public double getVatAmount() {
		return m_vatAmount;
	}

	public void setVatAmount(double vatAmount) {
		m_vatAmount = vatAmount;
	}
	public Customer getCustomer() {
		return m_customer;
	}

	public void setCustomer(Customer customer) {
		m_customer = customer;
	}

	public Object vgetInvoiceDate() {
		return this.transactionDate;
	}

	public Object vgetInvoiceNo() {
		return this.referenceNo;
	}

	public Object vgetCostumer() {
		return this.m_customer;
	}

	public Object vgetUnitCode() {
		return this.unit.getCode() + " " + unit.getDescription();
	}

	public Activity getActivity() {
		return m_activity;
	}

	public void setActivity(Activity activity) {
		m_activity = activity;
	}

	public Organization getDepartment() {
		return m_department;
	}

	public void setDepartment(Organization department) {
		m_department = department;
	}

	public Object vgetActivityCode() {
		return this.m_activity;
	}

	public Object vgetDepartment() {
		return this.m_department.getName();
	}

	public Object vgetContractNo() {
		return this.m_project.getPONo();
	}

	public Object vgetWorkDesc() {
		return this.m_description;
	}

	public Object vgetTotAmount() {
		return new Double(getSalesAmount());
	}

	public Object vgetStatus() {
		if (this.status==0)	return "Not Submitted";
		else if (this.status==1) return "Submitted";
		else if (this.status==2) return "Submitted";
		else if (this.status==3) return "Posted";
		return "";
	}

	public Object vgetSubmittedDate() {
		return this.submitDate;
	}

	public Date getDateAuthorize() {
		return m_dateAuthorize;
	}

	public void setDateAuthorize(Date authorize) {
		m_dateAuthorize = authorize;
	}

	public Currency getDownPaymentCurr() {
		return m_downPaymentCurr;
	}

	public void setDownPaymentCurr(Currency paymentCurr) {
		m_downPaymentCurr = paymentCurr;
	}

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION 1 : PENJUALAN

		ArrayList detailList = new ArrayList();

		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);

		// siapkan var amount
		double sales = getSalesAmount();
		sales = nr.round(sales);
		double dp = getDownPaymentAmount();
		dp = nr.round(dp);
		sales = sales - dp;  // subtotal

		double vat = getVatAmount();
		vat = nr.round(vat);

		double ar = sales + vat;
		ar = nr.round(ar);

		double totalSales = sales + dp;
		totalSales = nr.round(totalSales);



		//boolean forceValue = false;

		setCurrency(getSalesCurr());
		setExchangeRate(getSalesExchRate());

		VariableAccountSetting vas = null;

		// 1st, piutang usaha
		ar = nr.round(ar);
		vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_AR);
		TransactionDetail arDetail = createNewTransDet(vas.getAccount(), ar, getProject().getCustomer().getIndex());
		detailList.add(arDetail);

		// 2nd, ppn keluaran
		vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_OUT_VAT);
		TransactionDetail vatDetail = createNewTransDet(vas.getAccount(), vat);
		detailList.add(vatDetail);

		if(dp>0){
			// 3rd, uang muka penjualan
			vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_SALES_ADVANCE);
			TransactionDetail dpDetail = createNewTransDet(vas.getAccount(), dp, getSalesAdvance().getSalesAdvCurr(), getSalesAdvance().getSalesAdvExchRate(),
					getProject().getCustomer().getIndex());
			detailList.add(dpDetail);
		}

		// 4th, penjualan
		vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_SALES);
		TransactionDetail salesDetail = createNewTransDet(vas.getAccount(), totalSales, getProject().getCustomer().getIndex());
		detailList.add(salesDetail);

		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, IDBConstants.MODUL_ACCOUNTING);

		List journalStdList =
			helper.getJournalStandardSettingWithAccount(IConstants.SALES_INVOICE);

		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();

		// get the reference no lah
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createInvoiceReferenceNo(getUnit().getCode(), getTransactionDate()));

		// this is waktunya...
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
	}

	public double getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}

	public SalesAdvance getSalesAdvance() {
		return m_salesAdvance;
	}

	public void setSalesAdvance(SalesAdvance salesAdvance) {
		this.m_salesAdvance = salesAdvance;
	}

	public String toString() {
		if (this.referenceNo==null)
			return "";

		return this.referenceNo;
	}

	public String getAttention() {
		return m_attention;
	}

	public void setAttention(String attention) {
		m_attention = attention;
	}

	public BeginningAccountReceivable getBeginningBalance() {
		return beginningBalance;
	}

	public void setBeginningBalance(BeginningAccountReceivable beginningBalance) {
		this.beginningBalance = beginningBalance;
	}

	public String getReceiveFrom() {
		return receiveFrom;
	}

	public void setReceiveFrom(String receiveFrom) {
		this.receiveFrom = receiveFrom;
	}

	public CashBankAccount igetCashBankAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		else
			return getBankAccount();
	}

}
