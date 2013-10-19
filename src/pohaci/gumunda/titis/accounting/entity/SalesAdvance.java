package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.List;

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
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class SalesAdvance extends TransactionTemplateEntity implements GeneralReceipt {

	long m_index;
	//Transaction m_trans;	
	//Date m_submitDate;
	String m_receiveTo;
	CashAccount m_cashAccount;
	BankAccount m_bankAccount;
	String m_customerStatus;
	ProjectData m_project;
	double m_salesAdvPercent;
	Currency m_salesAdvCurr;
	double m_salesAdvExchRate;
	double m_salesAdvAmount;
	double m_vatPercent;
	Currency m_vatCurr;
	double m_vatExchRate;
	double m_vatAmount;
	double m_tax23Percent;
	Currency m_tax23Curr;
	double m_tax23ExchRate;
	double m_tax23Amount;	
	
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

	public String getCustomerStatus() {
		return m_customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		m_customerStatus = customerStatus;
	}

	
	public long getIndex() {
		return m_index;
	}

	public void setIndex(long index) {
		m_index = index;
	}


	public ProjectData getProject() {
		return m_project;
	}

	public void setProject(ProjectData project) {
		m_project = project;
	}

	public String getReceiveTo() {
		return m_receiveTo;
	}

	public void setReceiveTo(String receiveTo) {
		m_receiveTo = receiveTo;
	}
	
	public double getSalesAdvAmount() {
		return m_salesAdvAmount;
	}

	public void setSalesAdvAmount(double salesAdvAmount) {
		m_salesAdvAmount = salesAdvAmount;
	}

	public Currency getSalesAdvCurr() {
		return m_salesAdvCurr;
	}

	public void setSalesAdvCurr(Currency salesAdvCurr) {
		m_salesAdvCurr = salesAdvCurr;
	}

	public double getSalesAdvExchRate() {
		return m_salesAdvExchRate;
	}

	public void setSalesAdvExchRate(double salesAdvExchRate) {
		m_salesAdvExchRate = salesAdvExchRate;
	}

	public double getSalesAdvPercent() {
		return m_salesAdvPercent;
	}

	public void setSalesAdvPercent(double salesAdvPercent) {
		m_salesAdvPercent = salesAdvPercent;
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
	
	public double getVatAmount() {
		return m_vatAmount;
	}

	public void setVatAmount(double vatAmount) {
		m_vatAmount = vatAmount;
	}

	public Currency getVatCurr() {
		return m_vatCurr;
	}

	public void setVatCurr(Currency vatCurr) {
		m_vatCurr = vatCurr;
	}

	public double getVatExchRate() {
		return m_vatExchRate;
	}

	public void setVatExchRate(double vatExchRate) {
		m_vatExchRate = vatExchRate;
	}

	public double getVatPercent() {
		return m_vatPercent;
	}

	public void setVatPercent(double vatPercent) {
		m_vatPercent = vatPercent;
	}
	
	
	/*public Transaction getTrans() {
		return m_trans;
	}
	public void setTrans(Transaction trans) {
		m_trans = trans;
	}*/
	
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
		return this.m_bankAccount;
	}
	public Object vgetUnitCode() {
		if (unit!=null)
			return this.unit.toString();
		return "";
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

	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		// CREATE THE TRANSACTION
		
		ArrayList detailList = new ArrayList();
		
		/*double rcvAmount = 0;
		double rcvAmountOther = 0;
		double rcvAmountInBase = 0;
		double rcvExchRate = 1;*/
		
		BankAccount receivingAccount = getBankAccount();
		
		// sales advance, but not the details
		double salesAdvance = getSalesAdvAmount();
		double tax = getTax23Amount();
		double vat = getVatAmount();
		double rcvInInvCurr = salesAdvance + vat - tax;
		if(getCustomerStatus().equalsIgnoreCase("wapu")){
			rcvInInvCurr -= vat;
		}
		double rcv = rcvInInvCurr;
		Currency bank = getBankAccount().getCurrency();
		Currency sales = getSalesAdvCurr();
		if(bank.getIndex()!=sales.getIndex()){
			if(sales.getIndex()==igetBaseCurrency().getIndex())
				//	jika bank account $ dan sales advance Rp
				rcv = (rcv / getSalesAdvExchRate());
			else
				//	jika bank account Rp dan sales advance $
				rcv = (rcv * getSalesAdvExchRate());
		}
		
		// 1st, sales advance
		//if(getSalesAdvCurr().getIndex()!=igetBaseCurrency().getIndex())
			// jika sales adv currency != base currency ==> convert ke base currency
		//	salesAdvance = (salesAdvance * getSalesAdvExchRate());
		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_SALES_ADVANCE);
		TransactionDetail salesAdvanceDetail = createNewTransDet(vas.getAccount(), salesAdvance, getSalesAdvCurr(), getSalesAdvExchRate(), getProject().getCustomer().getIndex());
		detailList.add(salesAdvanceDetail);
		
		// 2nd, pph 23
		vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_TAX23);
		TransactionDetail tax23Detail = createNewTransDet(vas.getAccount(), getTax23Amount(), getTax23Curr(), getTax23ExchRate());
		detailList.add(tax23Detail);
		
		// 3rd, vat, if any (if wapu)
		vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_OUT_VAT);
		TransactionDetail vatDetail = createNewTransDet(vas.getAccount(), getVatAmount(), getVatCurr(), getVatExchRate());
		detailList.add(vatDetail);
		if(getCustomerStatus().equalsIgnoreCase("WAPU")){
			TransactionDetail vatDetail2 = createNewTransDet(vas.getAccount(), -getVatAmount(), getVatCurr(), getVatExchRate());
			detailList.add(vatDetail2);
		}
		
		// 4th, nyang nerima
		
		TransactionDetail rcvDetail = createNewTransDet(receivingAccount.getAccount(), rcv, receivingAccount.getCurrency(), getSalesAdvExchRate(), receivingAccount.getIndex());
		detailList.add(rcvDetail);
		
		boolean forceValue = true;
	
		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, IDBConstants.MODUL_ACCOUNTING);

		String attr = "";
		String type = ReferenceNoGeneratorHelper.BANK_IN;
		String code = receivingAccount.getCode();
		if(getCustomerStatus().equalsIgnoreCase("NONWAPU"))
			attr = IConstants.ATTR_CS_NONWAPU;
		else
			attr = IConstants.ATTR_CS_WAPU;
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(IConstants.SALES_ADVANCE,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		// get the reference no first
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		// this is waktunya...
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, forceValue);

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

	public String toString() {
		return getReferenceNo();
	}
	
}
