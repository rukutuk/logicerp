package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningLoan;
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
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class PmtLoan  extends TransactionTemplateEntity implements GeneralVoucher{
	long m_index;
	//Date m_submitDate;
	String m_paymentSource;
	CashAccount m_cashAccount;
	BankAccount m_bankAccount;
	String m_chequeNo;
	Date m_chequeDueDate;
	CompanyLoan m_payTo;
	RcvLoan loanReceipt;// transactionPosted harus tanyain irwan dulu kelasnya ada atau belom
	BeginningLoan beginningBalance;
	double m_amount;
	double m_exchangeRate;
	Currency baseCurrency;

	public PmtLoan(){
	}


	public double getAmount() {
		return m_amount;
	}

	public void setAmount(double amount) {
		m_amount = amount;
	}

	public BankAccount getBankAccount() {
		return m_bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		m_bankAccount = bankAccount;
	}

	public CashAccount getCashAccount() {
		return m_cashAccount;
	}

	public void setCashAccount(CashAccount cashAccount) {
		m_cashAccount = cashAccount;
	}

	public Date getChequeDueDate() {
		return m_chequeDueDate;
	}

	public void setChequeDueDate(Date chequeDueDate) {
		m_chequeDueDate = chequeDueDate;
	}

	public String getChequeNo() {
		return m_chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		m_chequeNo = chequeNo;
	}

	public long getIndex() {
		return m_index;
	}

	public void setIndex(long index) {
		m_index = index;
	}



	public String getPaymentSource() {
		return m_paymentSource;
	}

	public void setPaymentSource(String paymentSource) {
		m_paymentSource = paymentSource;
	}

	public CompanyLoan getPayTo() {
		return m_payTo;
	}

	public void setPayTo(CompanyLoan payTo) {
		m_payTo = payTo;
	}

	///
//	 View part - common voucher columns
	public Object vgetPaymentSource() {
		if (isSourceBank())
			return getBankAccount();
		else
			return getCashAccount();
	}

	public Object vgetVoucherType() {
		return "Loan";
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
		case 3:
			return "Posted";
		case 2:
			return "Submitted";
		default:
			return "";
		}
	}
	public Object vgetSubmitDate() {
		return getSubmitDate();
	}


	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION

		CashBankAccount sourceAccount = getSourceAccount();
		TransactionDetail sourceDetail =
			createNewTransDet(sourceAccount.getAccount(), getAmount(), sourceAccount.getIndex()); // berubah
		ArrayList detailList = new ArrayList();
		detailList.add(sourceDetail);

		GenericMapper m_entityMapper = MasterMap.obtainMapperFor(PmtLoan.class);
		m_entityMapper.setActiveConn(conn);

		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		TransactionDetail caiou = null;
		if (getLoanReceipt()!=null){
			CompanyLoan companyLoan = logic.getCompanyLoan(sessionId, IDBConstants.MODUL_MASTER_DATA
					,getLoanReceipt().getCompanyLoan().getIndex());
			caiou = createNewTransDet(companyLoan.getAccount(), getAmount(),getLoanReceipt().getCurrency(),
					getLoanReceipt().getExchangeRate(), companyLoan.getIndex());
		}
		else if (getBeginningBalance()!=null){
			caiou = createNewTransDet(getBeginningBalance().getCompanyLoan().getAccount(), getAmount(),getBeginningBalance().getCurrency(),
					getBeginningBalance().getExchangeRate(), getBeginningBalance().getCompanyLoan().getIndex());
			currency = getBeginningBalance().getCurrency();
		}
		detailList.add(caiou);
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId,
					IDBConstants.MODUL_ACCOUNTING);

		String type = "";
		String code = "";
		String attr = "";
		if(sourceAccount instanceof BankAccount){
			// artinya dikeluarken dari bank
			BankAccount source = (BankAccount)sourceAccount;
			source.getAccount();
			//if(companyLoan.getCreditorList().getCreditorType().equalsIgnoreCase("bank"))//ccreditor type from company loan that u selected
			type = ReferenceNoGeneratorHelper.BANK_OUT;
			code = (source).getCode();
			attr = IConstants.ATTR_PMT_BANK;
		}else{
			// buat yang cash
			type = ReferenceNoGeneratorHelper.CASH_OUT;
			code = ((CashAccount)sourceAccount).getUnit().getCode();
			attr = IConstants.ATTR_PMT_CASH;
		}

		List journalStdList =
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_LOAN,
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

	public RcvLoan getLoanReceipt() {
		return loanReceipt;
	}

	public void setLoanReceipt(RcvLoan loanReceipt) {
		this.loanReceipt = loanReceipt;
	}


	public Currency igetBaseCurrency() {
		return baseCurrency;
	}


	public void isetBaseCurrency(Currency baseCurrency) {
		this.baseCurrency = baseCurrency;
	}


	public BeginningLoan getBeginningBalance() {
		return beginningBalance;
	}


	public void setBeginningBalance(BeginningLoan beginningBalance) {
		this.beginningBalance = beginningBalance;
	}


}
