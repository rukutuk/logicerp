package pohaci.gumunda.titis.accounting.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
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
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class PmtOthers extends TransactionTemplateEntity implements GeneralVoucherToGetTotal{	
	long m_index;
	String m_paymentSource;
	CashAccount m_cashAccount;
	BankAccount m_bankAccount;
	String m_chequeNo;
	Date m_chequeDueDate;
	JournalStandard m_journal;
	String m_payTo;
	double m_exchangeRate;
	Organization m_department;
	int departmentgroup; 
	double amount;
	double total;
	
	PmtOthersDetail[] pmtOthersDetail;
	
	
	public PmtOthers(){
		
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

	public Organization getDepartment() {
		return m_department;
	}

	public void setDepartment(Organization department) {
		m_department = department;
	}	

	public long getIndex() {
		return m_index;
	}

	public void setIndex(long index) {
		m_index = index;
	}	

	public JournalStandard getJournal() {
		return m_journal;
	}

	public void setJournal(JournalStandard journal) {
		m_journal = journal;
	}

	public String getPaymentSource() {
		return m_paymentSource;
	}

	public void setPaymentSource(String paymentSource) {
		m_paymentSource = paymentSource;
	}

	public String getPayTo() {
		return m_payTo;
	}

	public void setPayTo(String payTo) {
		m_payTo = payTo;
	}

	public Object vgetPaymentSource() {
		if (isSourceBank())
			return getBankAccount();
		else
			return getCashAccount();
	}

	public Object vgetVoucherType() {
		return "Others";
	}
	public Object vgetVoucherNo() {
		return this.getReferenceNo();
	}
	
	public Object vgetVoucherDate() {
		return getTransactionDate();
	}
	public Object vgetPayTo() {
		return m_payTo;
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
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		
		CashBankAccount sourceAccount = getSourceAccount();
		ArrayList detailList = new ArrayList();
		PmtOthersDetail[] dets = getPmtOthersDetail();
		for(int i=0; i<dets.length; i++){
			JournalStandard js = getJournal();
			GenericMapper mapper=MasterMap.obtainMapperFor(JournalStandardAccount.class);
			mapper.setActiveConn(conn);		
			List rs=mapper.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+js.getIndex() + " AND " + 
					IDBConstants.ATTR_ACCOUNT + "=" + dets[i].getAccount().getIndex());			
			JournalStandardAccount jsAccount;
			TransactionDetail detail;
			if (rs.size()>0){
				jsAccount = (JournalStandardAccount)rs.get(0);
				if (!jsAccount.isCalculate()){
					if (dets[i].getAccount().getIndex()==jsAccount.getAccount().getIndex()){	
						if (jsAccount.getBalance()==dets[i].getAccount().getBalance())
							detail=createNewTransDet(dets[i].getAccount(), dets[i].getAccValue(),
									dets[i].getCurrency(), dets[i].getExchangeRate(), dets[i].getSubsidiAry());
						else
							detail=createNewTransDet(dets[i].getAccount(),(-dets[i].getAccValue()),
									dets[i].getCurrency(), dets[i].getExchangeRate(), dets[i].getSubsidiAry());
						detailList.add(detail);		
					}	
				}
			}			
		}		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);		
		String type = "";
		String code = "";
		if(sourceAccount instanceof BankAccount){
			type = ReferenceNoGeneratorHelper.BANK_OUT;
			code = ((BankAccount)sourceAccount).getCode();
		}else{
			type = ReferenceNoGeneratorHelper.CASH_OUT;
			code = ((CashAccount)sourceAccount).getUnit().getCode();
		}	
		JournalStandard journal = getJournal();
		journal = helper.getJournalStandardWithAccount(journal.getIndex());
	
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
	protected CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}

	public double getAmount() {
		double amt = 0;
		if(pmtOthersDetail!=null){
			for(int i=0; i<pmtOthersDetail.length; i++){
				amt += pmtOthersDetail[i].getAccValue();
			}
		}
		amount = amt;
		return amount;
	}

	public PmtOthersDetail[] getPmtOthersDetail() {
		return pmtOthersDetail;
	}

	public void setPmtOthersDetail(PmtOthersDetail[] pmtOthersDetail) {
		this.pmtOthersDetail = pmtOthersDetail;
	}

	public int getDepartmentgroup() {
		return departmentgroup;
	}

	public void setDepartmentgroup(int departmentgroup) {
		this.departmentgroup = departmentgroup;
	}

	/**
	 * @return Returns the total.
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total The total to set.
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	public String vgetTotal() {
		DecimalFormat dm = new DecimalFormat("#,##0.00");

		return dm.format(getTotal());
	}
	
	public String igetPaymentSource() {
		return getPaymentSource();
		
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#vgetBankAccount()
	 */
	public BankAccount vgetBankAccount() {
		return getBankAccount();
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#vgetCashAccount()
	 */
	public CashAccount vgetCashAccount() {
		return getCashAccount();
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#vgetIndex()
	 */
	public long vgetIndex() {
		return getIndex();
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal#vgetSymbolCurrency()
	 */
	public String vgetSymbolCurrency() {
		return getCurrency().getSymbol();
	}
}
