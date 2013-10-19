package pohaci.gumunda.titis.accounting.entity;

import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class MemJournalNonStrd extends TransactionTemplateEntity implements GeneralJournalStandar{
	long m_index;
	Journal m_journal;	
	//Date m_submitDate;
	ProjectData m_project;	
	Organization m_department;		
	MemJournalNonStrdDet[] memJournalNonStrdDets; 
	String m_submitType;
	int departmentgroup;
	
	public MemJournalNonStrd(){
		
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
	public Journal getJournal() {
		return m_journal;
	}
	public void setJournal(Journal journal) {
		m_journal = journal;
	}
	public ProjectData getProject() {
		return m_project;
	}
	public void setProject(ProjectData project) {
		m_project = project;
	}
	
	public String getSubmitType() {
		return m_submitType;
	}

	public void setSubmitType(String submitType) {
		m_submitType = submitType;
	}

	public void setMemJournalNonStrdDets(
			MemJournalNonStrdDet[] memJournalNonStrdDets) {
		this.memJournalNonStrdDets = memJournalNonStrdDets;
	}
	
	public Object vgetTransactionType() {		
		return "Memorial Journal Non Standar";
	}
	public Object vgetTransactionCode() {		
		return "";
	}
	public Object vgetTransactionDate() {		
		return this.transactionDate;
	}
	public Object vgetJournalNo() {
		return this.referenceNo;
	}
	public Object vgetProjectCode() {
		return this.m_project;
	}
	public Object vgetUnitCode() {
		if (unit!=null)
			return this.unit.getCode() + " " + this.unit.getDescription();
		return "";
	}
	public Object vgetDepartment() {	
		if (m_department!=null)
			return this.m_department.getName();
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
		return this.submitDate;
	}

	public MemJournalNonStrdDet[] getMemJournalNonStrdDets() {
		return memJournalNonStrdDets;
	}

	public void setMemJournalNonStrdDet(MemJournalNonStrdDet[] memJournalNonStrdDet) {
		this.memJournalNonStrdDets = memJournalNonStrdDet;
	}
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		Currency baseCurr = logic.getBaseCurrency(sessionId,IDBConstants.MODUL_ACCOUNTING);
		setCurrency(baseCurr);
		setExchangeRate(1.0);
		int i;
		TransactionDetail[] transDetails = new TransactionDetail[memJournalNonStrdDets.length];
		
		for (i=0; i< memJournalNonStrdDets.length; i++)
			if (memJournalNonStrdDets[i] != null){
				MemJournalNonStrdDet detail = memJournalNonStrdDets[i];
				String debCre;
				if (detail.getBalanceCode().equals(Account.STR_DEBET)){
					debCre = "Debit";					
				}else{
					debCre = "Credit";
				}
				double ammt;
				if (detail.getAccount().getBalanceAsString().equalsIgnoreCase(debCre)){
					ammt = detail.getAccValue();
				}else{
					ammt = (-detail.getAccValue());
				}
					
				TransactionDetail transDet = createNewTransDet(detail.getAccount(),ammt,detail.getCurrency(),
							detail.getExchangeRate(), detail.getSubsidiAry());
				
				transDetails[i] = transDet;				
			}
		JournalStandardSettingPickerHelper helper =new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		// dicek tipe nya
		String typeSubmit;
		if (getSubmitType().equals("1"))
			typeSubmit = IConstants.MJ_NONSTANDARD_PROJECT;
		else
			typeSubmit = IConstants.MJ_NONSTANDARD_OTHERS;
				
			
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(typeSubmit);
		
		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();		
		
		
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createMemorialJournalReferenceNo(getTransactionDate()));
		
		Transaction trans = createNewTrans(journal);

		trans.setTransactionDetail(transDetails);
		
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		setStatus((short)StateTemplateEntity.State.SUBMITTED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);
	}

	public int getDepartmentgroup() {
		return departmentgroup;
	}

	public void setDepartmentgroup(int departmentgroup) {
		this.departmentgroup = departmentgroup;
	}
	
}
