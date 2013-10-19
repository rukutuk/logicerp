package pohaci.gumunda.titis.accounting.entity;

import java.text.DecimalFormat;
import java.util.Date;
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
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class PmtOperationalCost extends TransactionTemplateEntity implements GeneralVoucherToGetTotal,GeneralJasperVoucherNonProject{
	long index ;
	String paymentSource ;
	CashAccount cashAccount;
	BankAccount bankAccount;
	String chequeNo ;
	Date chequeDueDate ;
	String payTo ;
	//Unit unit ; sds
	//Transaction trans;
	Organization department ;
	int departmentgroup;
	PmtOperationalCostDetail[] pmtOperationalCostDetail;
	double total;

	double amount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public PmtOperationalCost(){

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
/*
	public long getDepartment() {
		return department;
	}

	public void setDepartment(long department) {
		this.department = department;
	}*/

	public String getPaymentSource() {
		return paymentSource;
	}

	public void setPaymentSource(String paymentsource) {
		this.paymentSource = paymentsource;
	}

	/*public Employee getPayTo() {
		return payTo;
	}

	public void setPayTo(Employee payTo) {
		this.payTo = payTo;
	}
*/
	public Organization getDepartment() {
		return department;
	}

	public void setDepartment(Organization department) {
		this.department = department;
	}

//	 View part - common voucher columns
	public Object vgetPaymentSource() {
		if (isSourceBank())
			return getBankAccount();
		else
			return getCashAccount();
	}

	public Object vgetVoucherType() {
		return "Operational Cost";
	}
	public Object vgetVoucherNo() {
		return this.getReferenceNo();
	}

	public Object vgetVoucherDate() {
		return getTransactionDate();
	}
	public Object vgetPayTo() {
	/*	boolean isPayToBank= "BANK".equalsIgnoreCase(getPayTo());   //equalsIgnoreCase(getPayTo());
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

	public BankAccount vgetBankAccount() {
		return this.bankAccount;
	}

	public CashAccount vgetCashAccount() {
		return this.cashAccount;
	}

	public String vgetSymbolCurrency() {
		return this.currency.getSymbol();
	}

	public long vgetIndex() {
		return this.index;
	}

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		ArrayList detailList = new ArrayList();
		PmtOperationalCostDetail[] dets = getPmtOperationalCostDetail();
		amount=0;
		CashBankAccount sourceAccount = getSourceAccount();
		for (int i = 0; i < dets.length; i++) {
			TransactionDetail detail = null;
			if (dets[i].getAccount().equals(sourceAccount.getAccount()))
				detail = createNewTransDet(dets[i].getAccount(), dets[i]
						.getAccValue(), dets[i].getCurrency(), dets[i]
						.getExchangeRate(), sourceAccount.getIndex());
			else
				detail = createNewTransDet(dets[i].getAccount(), dets[i]
						.getAccValue(), dets[i].getCurrency(), dets[i]
						.getExchangeRate());
			detailList.add(detail);
			amount = amount + dets[i].getAccValue();
		}

		// bikin si pemberi
		// amount-nya diminus soalnya ga pada tempatnya

		/*TransactionDetail sourceDetail =
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
			// artinya diberiken dari bank
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
					IConstants.PAYMENT_OPERASIONAL_COST,
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
		setStatus((short)StateTemplateEntity.State.SUBMITTED);
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

	public PmtOperationalCostDetail[] getPmtOperationalCostDetail() {
		return pmtOperationalCostDetail;
	}

	public void setPmtOperationalCostDetail(
			PmtOperationalCostDetail[] pmtOperationalCostDetail) {
		this.pmtOperationalCostDetail = pmtOperationalCostDetail;
	}

	public String getPayTo() {
		return payTo;
	}

	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

	public int getDepartmentgroup() {
		return departmentgroup;
	}

	public void setDepartmentgroup(int departmentgroup) {
		this.departmentgroup = departmentgroup;
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#igetPaymentSource()
	 */
	public String igetPaymentSource() {
		return getPaymentSource();
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#vgetTotal()
	 */
	public String vgetTotal() {
		DecimalFormat dm = new DecimalFormat("#,##0.00");

		return dm.format(getTotal());
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

	/*public Transaction getTrans() {
		return trans;
	}

	public void setTrans(Transaction trans) {
		this.trans = trans;
	}*/

}
