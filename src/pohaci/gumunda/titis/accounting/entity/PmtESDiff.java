package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;
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
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class PmtESDiff extends TransactionTemplateEntity implements
		GeneralVoucher {

	// TransactionPosted esNo;
	private ExpenseSheet esNo;

	BeginningEsDiff beginningBalance;

	double amount;

	long index;

	String paymentSource;

	CashAccount cashAccount;

	BankAccount bankAccount;

	String chequeNo;

	Date chequeDueDate;

	Employee payTo;

	public PmtESDiff() {

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

	public Date getChequeDueDate() {
		return chequeDueDate;
	}

	public void setChequeDueDate(Date chequedueDate) {
		this.chequeDueDate = chequedueDate;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getPaymentSource() {
		return paymentSource;
	}

	public void setPaymentSource(String paymentsource) {
		this.paymentSource = paymentsource;
	}

	public Employee getPayTo() {
		return payTo;
	}

	public void setPayTo(Employee payTo) {
		this.payTo = payTo;
	}

	/*
	 * public TransactionPosted getEsNo() { return esNo; } public void
	 * setEsNo(TransactionPosted esNo) { this.esNo = esNo; }
	 */
	public ExpenseSheet getEsNo() {
		return esNo;
	}

	public void setEsNo(ExpenseSheet esNo) {
		this.esNo = esNo;
	}

	// View part - common voucher columns
	public Object vgetPaymentSource() {
		if (isSourceBank())
			return getBankAccount();
		else
			return getCashAccount();
	}

	public Object vgetVoucherType() {
		return "Expense Sheet Difference";
	}

	public Object vgetVoucherNo() {
		return this.getReferenceNo();
	}

	public Object vgetVoucherDate() {
		return getTransactionDate();
	}

	public Object vgetPayTo() {
		/*
		 * boolean isPayToBank= "BANK".equalsIgnoreCase(getPayTo());
		 * //equalsIgnoreCase(getPayTo()); if (isPayToBank) return
		 * getBankAccount(); else return getCashAccount();
		 */
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

		switch (status) {
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

	public void submit(long sessionId, java.sql.Connection conn)
			throws Exception {
		// CREATE THE TRANSACTION

		// bikin si pemberi
		CashBankAccount sourceAccount = getSourceAccount();
		TransactionDetail sourceDetail = createNewTransDet(sourceAccount
				.getAccount(), -getAmount(), sourceAccount.getIndex());
		ArrayList detailList = new ArrayList();
		detailList.add(sourceDetail);

		// nyang nerima
		// yaitu...... es diff
		/*
		 * VariableAccountSetting vas =
		 * VariableAccountSetting.createVariableAccountSetting(conn, sessionId,
		 * IConstants.ATTR_VARS_ES_DIFF); TransactionDetail esdiff =
		 * createNewTransDet(vas.getAccount(), getAmount());
		 * detailList.add(esdiff);
		 */
		VariableAccountSetting vas = VariableAccountSetting
				.createVariableAccountSetting(conn, sessionId,
						IConstants.ATTR_VARS_ES_DIFF);
		TransactionDetail detailESDiff = null;
		/*
		 * createNewTransDet(vas.getAccount(), -getEsNo().getAmount(),
		 * getEsNo().getCurrency(), getEsNo().getExchangeRate());
		 */
		if(getEsNo()!=null)
			detailESDiff = createNewTransDet(vas.getAccount(), getAmount(),
					getEsNo().getCurrency(), getEsNo().getExchangeRate(),
					getPayTo().getIndex());
		else
			detailESDiff = createNewTransDet(vas.getAccount(), getAmount(),
					getBeginningBalance().getCurrency(), getBeginningBalance().getExchangeRate(),
					getBeginningBalance().getEmployee().getIndex());
		detailList.add(detailESDiff);

		// GET THE STANDARD JOURNAL

		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				conn, sessionId, IDBConstants.MODUL_ACCOUNTING);

		String type = "";
		String code = "";
		String attr = "";
		if (sourceAccount instanceof BankAccount) {
			// artinya dikeluarken dari bank
			type = ReferenceNoGeneratorHelper.BANK_OUT;
			code = ((BankAccount) sourceAccount).getCode();
			attr = IConstants.ATTR_PMT_BANK;
		} else {
			// buat yang cash
			type = ReferenceNoGeneratorHelper.CASH_OUT;
			code = ((CashAccount) sourceAccount).getUnit().getCode();
			attr = IConstants.ATTR_PMT_CASH;
		}

		List journalStdList = helper.getJournalStandardSettingWithAccount(
				IConstants.PAYMENT_EXPENSESHEET_DIFFERENCE, attr);

		// harusnya dapat satu, yang lain abaikan aja... berarti user salah
		// milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList
				.get(0);
		JournalStandard journal = setting.getJournalStandard();

		// get the reference no first
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(
				conn, sessionId, IDBConstants.MODUL_ACCOUNTING,
				getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code,
				getTransactionDate()));

		// this is waktunya...
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList
				.toArray(new TransactionDetail[detailList.size()]);

		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(journal
				.getJournalStandardAccount(), details, getUnit(),
				igetBaseCurrency(), 1, true);

		trans.setTransactionDetail(details);

		// save lah
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,
				IDBConstants.MODUL_ACCOUNTING, trans, trans
						.getTransactionDetail());

		// get everything related
		setStatus((short) StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");

		// UNTESTED !!!!
		// UNTESTED !!!!
		// UNTESTED !!!!
	}

	protected CashBankAccount getSourceAccount() {
		if (getCashAccount() != null)
			return getCashAccount();
		if (getBankAccount() != null)
			return getBankAccount();
		return null;
	}

	public BeginningEsDiff getBeginningBalance() {
		return beginningBalance;
	}

	public void setBeginningBalance(BeginningEsDiff beginningBalance) {
		this.beginningBalance = beginningBalance;
	}
}
