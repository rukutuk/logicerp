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

public class PayrollPmtEmpInsurance extends TransactionTemplateEntity  implements GeneralVoucher,GeneralJasperVchr_Salary{
	long m_index;
	String m_paymentSource;
	CashAccount m_cashAccount;
	BankAccount m_bankAccount;
	String m_chequeNo;
	Date m_chequeDueDate;
	String m_payto;	
	
	PayrollPmtEmpInsDet[] payrollPmtEmpInsDet;
	public PayrollPmtEmpInsurance(){
		
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
	public String getPayto() {
		return m_payto;
	}
	public void setPayto(String payto) {
		m_payto = payto;
	}
	public Object vgetPaymentSource() {	
		return this.m_paymentSource;
	}
	public Object vgetVoucherType() {
		return "Employee Insurance Payment";
	}
	public Object vgetVoucherNo() {	
		return this.referenceNo;
	}
	public Object vgetVoucherDate() {
		return this.transactionDate;
	}
	public Object vgetPayTo() {	
		return this.m_payto;
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
		return this.unit.getCode() + " " + unit.getDescription();
	}
	public Object vgetStatus() {	
		if (this.status==0)	return "Not Submitted";
		else if (this.status==1) return "Submitted";
		else if (this.status==3) return "Posted";		
		return "";
	}
	public Object vgetSubmitDate() {	
		return this.submitDate;
	}
	
	public boolean isSourceBank() {
		return "BANK".equalsIgnoreCase(getPaymentSource());
	}
	public PayrollPmtEmpInsDet[] getPayrollPmtEmpInsDet() {
		return payrollPmtEmpInsDet;
	}
	public void setPayrollPmtEmpInsDet(PayrollPmtEmpInsDet[] payrollPmtEmpInsDet) {
		this.payrollPmtEmpInsDet = payrollPmtEmpInsDet;
	}
	
	private CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		ArrayList detailList = new ArrayList();		
		PayrollPmtEmpInsDet[] dets = getPayrollPmtEmpInsDet();
		double amount = 0;
		for(int i=0; i<dets.length; i++){
			TransactionDetail detail  = 
				createNewTransDet(dets[i].getAccount(), dets[i].getAccValue(),
						dets[i].getCurrency(), dets[i].getExchangeRate());
			detailList.add(detail);
			amount = amount + dets[i].getAccValue();
		}
		
		CashBankAccount sourceAccount = getSourceAccount();
		TransactionDetail sourceDetail = 
			createNewTransDet(sourceAccount.getAccount(), amount, sourceAccount.getIndex());
		detailList.add(sourceDetail);
		
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
					IConstants.PAYROLL_PAYMENT_EMPLOYEE_INSURANCE,
					attr);
		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, false);

		trans.setTransactionDetail(details);
		
		// save lah
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,
				trans,trans.getTransactionDetail());
		
		// get everything related
		setStatus((short)StateTemplateEntity.State.SUBMITTED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);
	}
	
}
