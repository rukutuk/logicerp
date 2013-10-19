package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Currency;
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
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class MemJournalStrd extends TransactionTemplateEntity implements GeneralJournalStandar,GeneralJasperMemJournal {	
	long m_index;
	//Transaction m_trans;	
	JournalStandard m_transactionCode;	
	//Date m_submitDate;
	Organization m_department;
	ProjectData m_project; 
	MemJournalStrdDet[] memJournalStrdDet;
	int departmentgroup;
		
	public MemJournalStrd(){
		
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
	
	/*public Transaction getTrans() {
		return m_trans;
	}

	public void setTrans(Transaction trans) {
		m_trans = trans;
	}*/


	public JournalStandard getTransactionCode() {
		return m_transactionCode;
	}

	public void setTransactionCode(JournalStandard transactionCode) {
		m_transactionCode = transactionCode;
	}

	public ProjectData getProject() {
		return m_project;
	}

	public void setProject(ProjectData project) {
		m_project = project;
	}

	public Object vgetTransactionType() {		
		return "Memorial Journal Standar";
	}

	public Object vgetTransactionCode() {
		if (m_transactionCode!=null)			
			return this.m_transactionCode.getCode();
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
			return this.unit.getCode() + " " + unit.getDescription();
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
		case 3:
			return "Posted";
		default:
			return "";
		}
	}

	public Object vgetSubmitDate() {		
		return this.submitDate;
	}


	public MemJournalStrdDet[] getMemJournalStrdDet() {
		return memJournalStrdDet;
	}


	public void setMemJournalStrdDet(MemJournalStrdDet[] memJournalStrdDet) {
		this.memJournalStrdDet = memJournalStrdDet;
	}
public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		Currency baseCurr = logic.getBaseCurrency(sessionId,IDBConstants.MODUL_ACCOUNTING);
		setCurrency(baseCurr);
		setExchangeRate(1.0);
		int i;
		TransactionDetail[] transDetails = new TransactionDetail[memJournalStrdDet.length];
		
		for (i=0; i< memJournalStrdDet.length; i++)			
			if (memJournalStrdDet[i] != null){
				MemJournalStrdDet detail = memJournalStrdDet[i];
				double accValue = detail.getAccValue();					
				TransactionDetail transDet = 
					createNewTransDet(detail.getAccount(),accValue,
							detail.getCurrency(),
							detail.getExchangeRate(),
							detail.getSubsidiAry());
				transDetails[i] = transDet;
				
			}
		
		JournalStandard journal = m_transactionCode;
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(conn,sessionId,
				IDBConstants.MODUL_ACCOUNTING);
		JournalStandardAccount[] journalAccts = helper.getJournalStandardAccount(m_transactionCode.getCode());
		//posisinya disni
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createMemorialJournalReferenceNo(getTransactionDate()));
		
		Transaction trans = createNewTrans(journal);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		transDetails = transHelper.prepareJournalStandardTransactionDetail(
				journalAccts, transDetails, getUnit(), igetBaseCurrency(), 1, false);

		trans.setTransactionDetail(transDetails);
		
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		setStatus((short)StateTemplateEntity.State.SUBMITTED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
						

	}


public int getDepartmentgroup() {
	return departmentgroup;
}


public void setDepartmentgroup(int departmentgroup) {
	this.departmentgroup = departmentgroup;
}


}
