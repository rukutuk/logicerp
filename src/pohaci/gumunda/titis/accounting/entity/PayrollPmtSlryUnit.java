package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
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

public class PayrollPmtSlryUnit extends TransactionTemplateEntity implements GeneralVoucher,GeneralJasperVchr_Salary {		
	long m_index;
	String m_paymentSource;
	CashAccount m_cashAccount;
	BankAccount m_bankAccount;
	String m_chequeNo;
	Date m_chequeDueDate;
	String m_payto;		
	Organization department;
	int departmentgroup; 
	PayrollPmtSlryUnitDet[] payrollPmtSlryUnitDet;
	
	public PayrollPmtSlryUnit(){
		
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

	/*public Transaction getTrans() {
		return m_trans;
	}
	public void setTrans(Transaction trans) {
		m_trans = trans;
	}*/

	public Object vgetPaymentSource() {
		return this.m_paymentSource;
	}
	public Object vgetVoucherType() {
		return "Salary Payment - Unit";
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
		if (unit!=null)
			return this.unit.getCode() + " " + unit.getDescription();
		return "";
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
	public PayrollPmtSlryUnitDet[] getPayrollPmtSlryUnitDet() {
		return payrollPmtSlryUnitDet;
	}
	public void setPayrollPmtSlryUnitDet(
			PayrollPmtSlryUnitDet[] payrollPmtSlryUnitDet) {
		this.payrollPmtSlryUnitDet = payrollPmtSlryUnitDet;
	}
	
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		CashBankAccount sourceAccount = getSourceAccount();		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String type = "";
		String code = "";
		String attr = "";
		if(sourceAccount instanceof BankAccount){
			type = ReferenceNoGeneratorHelper.BANK_OUT;
			code = ((BankAccount)sourceAccount).getCode();
			attr = IConstants.ATTR_PMT_BANK;
		}else if (sourceAccount instanceof CashAccount) {			
			type = ReferenceNoGeneratorHelper.CASH_OUT;
			code = ((CashAccount)sourceAccount).getUnit().getCode();
			attr = IConstants.ATTR_PMT_CASH;
		}
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYROLL_PAYMENT_SALARY_UNIT,attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		// cek journal standarnya yang membalance apakah debet ato kredit
		JournalStandardAccount[] journalstrd = journal.getJournalStandardAccount();
		for (int i=0;i<journalstrd.length;i++){
			if (journalstrd[i].getAccount().getIndex()==12){
				if (journalstrd[i].getBalance()==0) {
				} else {
				}
			}
		}
		
		ArrayList detailList = new ArrayList();
		PayrollPmtSlryUnitDet[] dets = getPayrollPmtSlryUnitDet();
		double amount =0;
		for(int i=0; i<dets.length; i++){			 
			double accValue= dets[i].getAccValue();			
			// apakah account balance adalah credit
			/*if (dets[i].getAccount().getIndex()==12 && cekJournalStandard==JournalStandardAccount.STR_CREDIT)
				accValue = - dets[i].getAccValue();	*/			
			
			TransactionDetail detail =
				createNewTransDet(dets[i].getAccount(), accValue,
						dets[i].getCurrency(), dets[i].getExchangeRate(), dets[i].getSubsidiAry());
			detailList.add(detail);
			amount = amount + dets[i].getAccValue();
		}
		
		
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
	}
	
	private CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	public Organization getDepartment() {
		return department;
	}
	public void setDepartment(Organization department) {
		this.department = department;
	}
	public int getDepartmentgroup() {
		return departmentgroup;
	}
	public void setDepartmentgroup(int departmentgroup) {
		this.departmentgroup = departmentgroup;
	}
}
