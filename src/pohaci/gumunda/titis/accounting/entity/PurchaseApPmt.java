package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
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

public class PurchaseApPmt extends TransactionTemplateEntity implements GeneralVoucher {	
	long m_index;
	//Transaction m_trans;	
	//Date m_submitDate;
	String m_paymentSource;
	CashAccount m_cashAccount;
	BankAccount m_bankAccount;
	String m_chequeNo;
	Date m_chequeDueDate;
	PurchaseReceipt m_purchaseReceipt;
	BeginningAccountPayable beginningBalance;
	Currency m_appMtCurr;
	double m_appMtExchRate;
	double m_appMtAmount;
	double m_tax23Percent;
	Currency m_tax23Curr;
	double m_tax23ExchRate;
	double m_tax23Amount;	
	public PurchaseApPmt(){
	}
	public double getAppMtAmount() {
		return m_appMtAmount;
	}
	public void setAppMtAmount(double appMtAmount) {
		m_appMtAmount = appMtAmount;
	}
	public Currency getAppMtCurr() {
		return m_appMtCurr;
	}
	public void setAppMtCurr(Currency appMtCurr) {
		m_appMtCurr = appMtCurr;
	}
	public double getAppMtExchRate() {
		return m_appMtExchRate;
	}
	public void setAppMtExchRate(double appMtExchRate) {
		m_appMtExchRate = appMtExchRate;
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
	public PurchaseReceipt getPurchaseReceipt() {
		return m_purchaseReceipt;
	}
	public void setPurchaseReceipt(PurchaseReceipt purchaseReceipt) {
		m_purchaseReceipt = purchaseReceipt;
	}
	
	public double getTax23Amount() {
		return m_tax23Amount;
	}
	public void setTax23Amount(double tax23Amount) {
		m_tax23Amount = tax23Amount;
	}
	public Currency getTax23Curr() {
		return m_tax23Curr;
	}
	public void setTax23Curr(Currency tax23Curr) {
		m_tax23Curr = tax23Curr;
	}
	public double getTax23ExchRate() {
		return m_tax23ExchRate;
	}
	public void setTax23ExchRate(double tax23ExchRate) {
		m_tax23ExchRate = tax23ExchRate;
	}
	public double getTax23Percent() {
		return m_tax23Percent;
	}
	public void setTax23Percent(double tax23Percent) {
		m_tax23Percent = tax23Percent;
	}
	/*public Transaction getTrans() {
		return m_trans;
	}
	public void setTrans(Transaction trans) {
		m_trans = trans;
	}*/
	
	public boolean isSourceBank() {
		return "BANK".equalsIgnoreCase(getPaymentSource());
	}
	
	public Object vgetPaymentSource() {		
		return this.m_paymentSource;
	}
	public Object vgetVoucherType() {		
		return "Account Payable Payment";
	}
	public Object vgetVoucherNo() {
		return this.referenceNo;
	}
	public Object vgetVoucherDate() {		
		return this.transactionDate;
	}
	public Object vgetPayTo() {
		if (m_purchaseReceipt!=null)
			return this.m_purchaseReceipt.getSupplier();
		return "";
	}
	public Object vgetOriginator() {		
		return this.empOriginator;
	}
	public Object vgetApprovedBy() {		
		return this.empApproved;
	}
	public Object vgetReceivedBy() {		
		return this.empReceived;
	}
	public Object vgetUnitCode() {
		if (unit!=null)
			return this.unit.getCode() + " " + unit.getDescription();
		return "";
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
		return this.getSubmitDate();
	}
	
	public String toString(){
		return this.referenceNo;
	}
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		
		CashBankAccount sourceAccount = getSourceAccount();
		VariableAccountSetting account1 = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_AP);

		double excrate = 0;
		TransactionDetail transDet1 = null;
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		
		double amt = getAppMtAmount();
	
		if (getPurchaseReceipt()!=null){
			excrate = getPurchaseReceipt().getApexChRate();
			if (!getAppMtCurr().getSymbol().equals(
					getPurchaseReceipt().getApCurr().getSymbol())) {
				// hanya yang beda
				if (getAppMtCurr().getSymbol().equals(
						igetBaseCurrency().getSymbol())) {
					// pr = $, pmt = Rp
					excrate = 1;
				} else {
					// pr = Rp; pmt = $
					excrate  = getAppMtExchRate();
				}
			}
			
			amt = nr.round(amt);
			transDet1 = 
				createNewTransDet(account1.getAccount(), (-amt),getAppMtCurr(),excrate, getPurchaseReceipt().getSupplier().getIndex());
			// jika getsymbol currency != basecurrency maka excrate = 1.0 kalo sama seperti dibawah
			//if (getPurchaseReceipt().getApCurr().getSymbol().equals())
			
		}else if (getBeginningBalance()!=null){
			excrate = getBeginningBalance().getExchangeRate();
			if (!getAppMtCurr().getSymbol().equals(
					getBeginningBalance().getCurrency().getSymbol())) {
				// hanya yang beda
				if (getAppMtCurr().getSymbol().equals(
						igetBaseCurrency().getSymbol())) {
					// amountnya harus dalam currency purchase receipt
					// pr = $, pmt = Rp
					excrate = 1;
				} else {
					// excrate = getPurchaseReceipt().getApexChRate();
					excrate  = getAppMtExchRate();
				}
			}
			
			amt = nr.round(amt);
			transDet1 = 
				createNewTransDet(account1.getAccount(), (-amt),getAppMtCurr(),excrate, getBeginningBalance().getPartner().getIndex());
			
		}
			
		ArrayList detailList = new ArrayList();
		detailList.add(transDet1);
		
		VariableAccountSetting account2 = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_TAX23_PAYABLE);
		System.err.println("2 : getTax23Amount() :" + getTax23Amount() + " getTax23Curr() ;" + getTax23Curr().getSymbol() + 
				"getAppMtExchRate() :" + getAppMtExchRate());
		
		amt = getTax23Amount();
		amt = nr.round(amt);
		
		TransactionDetail transDet2 = createNewTransDet(account2.getAccount(),amt,getTax23Curr(),getAppMtExchRate());
		detailList.add(transDet2);
				
		String type = "";
		String code = "";
		String attr = "";	
		
		if(sourceAccount instanceof BankAccount){
			type = ReferenceNoGeneratorHelper.BANK_OUT;
			code = ((BankAccount)sourceAccount).getCode();
			attr = IConstants.ATTR_PMT_BANK;						
		}else{
			type = ReferenceNoGeneratorHelper.CASH_OUT;
			code = ((CashAccount)sourceAccount).getUnit().getCode();
			attr = IConstants.ATTR_PMT_CASH;
		}

		System.err.println("3 : getAppMtAmount()-getTax23Amount() :" + (getAppMtAmount()-getTax23Amount()) + 
				" sourceAccount.getCurrency() :" + sourceAccount.getCurrency().getSymbol() + " getAppMtExchRate():" + getAppMtExchRate());
		
		amt = getAppMtAmount()-getTax23Amount();
		amt = nr.round(amt);
		
		TransactionDetail transDet3 = createNewTransDet(sourceAccount.getAccount(), (-amt),sourceAccount.getCurrency(),getAppMtExchRate(),
				sourceAccount.getIndex());
		detailList.add(transDet3);
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PURCHASE_AP_PAYMENT,
					attr);
		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		//posisinya disni
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
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
	
	private CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	public BeginningAccountPayable getBeginningBalance() {
		return beginningBalance;
	}
	public void setBeginningBalance(BeginningAccountPayable beginningBalance) {
		this.beginningBalance = beginningBalance;
	}
}
