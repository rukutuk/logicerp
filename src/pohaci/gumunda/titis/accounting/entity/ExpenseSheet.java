package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
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
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class ExpenseSheet extends TransactionTemplateEntity implements GeneralExpeseSheet {
	long m_index;
	String m_esProjectType;
	PmtCAIOUProjectSettled m_pmtCaIouProjectSettled;
	PmtCAProject m_pmtCaProject;
	PmtCAIOUOthersSettled m_pmtCaIouOthersSettled;
	PmtCAOthers m_pmtCaOthers;
	BeginningCashAdvance beginningBalance;
	Employee m_esOwner;	
	Organization department ;
	int departmentgroup;
	double m_amount;
	
	ProjectData m_project;
	ExpenseSheetDetail[] expenseSheetDetail;
	
	public ExpenseSheetDetail[] getExpenseSheetDetail() {
		return expenseSheetDetail;
	}

	public void setExpenseSheetDetail(ExpenseSheetDetail[] expenseSheetDetail) {
		this.expenseSheetDetail = expenseSheetDetail;
	}
	
	public ExpenseSheet(){		
	}
	
	
	public double getAmount() {
		return m_amount;
	}
	public void setAmount(double amount) {
		m_amount = amount;
	}
	
	
	public Employee getEsOwner() {
		return m_esOwner;
	}
	
	public void setEsOwner(Employee esOwner) {
		m_esOwner = esOwner;
	}

	public String getEsProjectType() {
		return m_esProjectType;
	}
	
	public void setEsProjectType(String esProjectType) {
		m_esProjectType = esProjectType;
	}


	public long getIndex() {
		return m_index;
	}


	public void setIndex(long index) {
		m_index = index;
	}


	

	public PmtCAIOUOthersSettled getPmtCaIouOthersSettled() {
		return m_pmtCaIouOthersSettled;
	}


	public void setPmtCaIouOthersSettled(PmtCAIOUOthersSettled pmtCaIouOthersSettled) {
		m_pmtCaIouOthersSettled = pmtCaIouOthersSettled;
	}


	public PmtCAIOUProjectSettled getPmtCaIouProjectSettled() {
		return m_pmtCaIouProjectSettled;
	}


	public void setPmtCaIouProjectSettled(PmtCAIOUProjectSettled pmtCaIouProjectSettled) {
		m_pmtCaIouProjectSettled = pmtCaIouProjectSettled;
	}


	public PmtCAOthers getPmtCaOthers() {
		return m_pmtCaOthers;
	}


	public void setPmtCaOthers(PmtCAOthers pmtCaOthers) {
		m_pmtCaOthers = pmtCaOthers;
	}


	public PmtCAProject getPmtCaProject() {
		return m_pmtCaProject;
	}


	public void setPmtCaProject(PmtCAProject pmtCaProject) {
		m_pmtCaProject = pmtCaProject;
	}


	public ProjectData getProject() {
		return m_project;
	}


	public void setProject(ProjectData project) {
		m_project = project;
	}


	

	public Object vgetESType() {		
		return "Expense Sheet Project";
	}
	
	public Object vgetESNo() {
		return this.referenceNo;
	}
	
	public Object vgetESDate() {
		return this.transactionDate;
	}
	
	public Object vgetProjectCode() {
		return this.m_project;
	}
	
	public Object vgetUnitCode() {
		return this.unit.getCode() + " " + this.unit.getDescription();
	}
	
	public Object vgetOriginator() {
		return this.empOriginator;
	}
	
	public Object vgetApprovedBy() {		
		return this.empApproved;
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
	
	public Object vgetSubmittedDate() {
		//return this.m_submitDate;
		return submitDate;
	}
	
	public Object vgetAdvanceAmount() {
		if (m_pmtCaIouOthersSettled!=null)
			return new Double(m_pmtCaIouOthersSettled.getAmount());
		else if (m_pmtCaIouProjectSettled!=null)
			return new Double(m_pmtCaIouProjectSettled.getAmount());
		else if (m_pmtCaOthers!=null)
			return new Double(m_pmtCaOthers.getAmount());
		else if (m_pmtCaProject!=null)
			return new Double(m_pmtCaProject.getAmount());
		else if (beginningBalance!=null)
			return new Double(beginningBalance.getAccValue());
		else
			return null;
	}
	
	public Object vgetTotalExp() {
		return new Double(this.m_amount);
	}
	
	public boolean isIouEsProjectType() {
		return "IOU".equalsIgnoreCase(getEsProjectType());
	}
	
	public String toString(){
		return this.getReferenceNo();
	}
  
	public Object vgetCAExchangeRate() {
		if (m_pmtCaIouOthersSettled != null) {
			if (m_pmtCaIouOthersSettled.getExchangeRate() == 0)
				return new Double(1);
			return new Double(m_pmtCaIouOthersSettled.getExchangeRate());
		} else if (m_pmtCaIouProjectSettled != null) {
			if (m_pmtCaIouProjectSettled.getExchangeRate() == 0)
				return new Double(1);
			return new Double(m_pmtCaIouProjectSettled.getExchangeRate());
		} else if (m_pmtCaOthers != null){
			if (m_pmtCaOthers.getExchangeRate() == 0)
				return new Double(1);
			return new Double(m_pmtCaOthers.getExchangeRate());
		} else if (m_pmtCaProject != null){
			if (m_pmtCaProject.getExchangeRate() == 0)
				return new Double(1);
			return new Double(m_pmtCaProject.getExchangeRate());
		} else if (beginningBalance != null){
			if (beginningBalance.getExchangeRate() == 0)
				return new Double(1);
			return new Double(beginningBalance.getExchangeRate());
		}	
		return new Double(1);
	}
	
	public Object vgetCashAdvance(){
		if (m_pmtCaIouOthersSettled!=null)
			return m_pmtCaIouOthersSettled;
		else if (m_pmtCaIouProjectSettled!=null)
			return m_pmtCaIouProjectSettled;
		else if (m_pmtCaOthers!=null)
			return m_pmtCaOthers;
		else if (m_pmtCaProject!=null)
			return m_pmtCaProject;
		else if (beginningBalance!=null)
			return beginningBalance;
		return null;
	}
	
	public String vgetType(){
		if (m_pmtCaIouOthersSettled!=null)
			return "OTHERS";
		else if (m_pmtCaIouProjectSettled!=null)
			return "PROJECT";
		else if (m_pmtCaOthers!=null)
			return "OTHERS";
		else if (m_pmtCaProject!=null)
			return "PROJECT";
		else if (beginningBalance!=null){
			if(beginningBalance.getProject()!=null)
				return "PROJECT";
			else
				return "OTHERS";
		}
		return "";
	}
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {

		// special buat work in progress
		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_WORK_IN_PROGRESS);
		Account wipAcct = null;
		if (vas!=null)
			wipAcct = vas.getAccount();
		
		ArrayList detailList = new ArrayList();
		ExpenseSheetDetail[] dets = getExpenseSheetDetail();
		double tempamount=0;
		for(int i=0; i<dets.length; i++){
			TransactionDetail detail = null;
			if (dets[i].getAccount().equals(wipAcct)) {
				if (getProject() != null)
					detail = createNewTransDet(dets[i].getAccount(), dets[i]
							.getaccValue(), dets[i].getCurrency(), dets[i]
							.getExchangeRate(), getProject().getIndex());
				else
					detail = createNewTransDet(dets[i].getAccount(), dets[i]
							.getaccValue(), dets[i].getCurrency(), dets[i]
							.getExchangeRate());
			} else
				detail = createNewTransDet(dets[i].getAccount(), dets[i]
						.getaccValue(), dets[i].getCurrency(), dets[i]
						.getExchangeRate());
			tempamount = tempamount + dets[i].getaccValue();
			detailList.add(detail);
		}
		setAmount(tempamount);
		
		double a=((Number)vgetAdvanceAmount()).doubleValue();
		
		String vars = "";
		if(vgetType().equals("PROJECT"))
			vars = IConstants.ATTR_VARS_CA_PROJECT;
		else
			vars = IConstants.ATTR_VARS_CA_OTHER;
		
		vas = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, vars);
		TransactionDetail exp = 
			createNewTransDet(vas.getAccount(), a, getCurrency(), ((Double)vgetCAExchangeRate()).doubleValue(), 
					getEsOwner().getIndex());
		detailList.add(exp);
		
		a=a-tempamount;
		 vas = 
			VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_ES_DIFF);
		 exp = createNewTransDet(vas.getAccount(), a,getCurrency(), getExchangeRate(), 
				 getEsOwner().getIndex());
		detailList.add(exp);

		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);

		String type = "";
		if(vgetType().equals("PROJECT"))
			type = IConstants.EXPENSE_SHEET_PROJECT;
		else
			type = IConstants.EXPENSE_SHEET_OTHERS;
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					type);
		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createExpenseSheetReferenceNo(getTransactionDate()));
		
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
			
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, false);
        			
	   trans.setTransactionDetail(details);
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		setStatus((short)StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);
	}

	public BeginningCashAdvance getBeginningBalance() {
		return beginningBalance;
	}

	public void setBeginningBalance(BeginningCashAdvance beginningBalance) {
		this.beginningBalance = beginningBalance;
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
