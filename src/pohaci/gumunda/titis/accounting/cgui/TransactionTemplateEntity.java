package pohaci.gumunda.titis.accounting.cgui;

import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public abstract class TransactionTemplateEntity extends StateTemplateEntity  {
	protected String description;
	protected Employee empOriginator;
	protected String jobTitleOriginator;
	protected Date dateOriginator = new Date();
	protected Employee empApproved;
	protected String jobTitleApproved;
	protected Date dateApproved = new Date();
	protected Employee empReceived;
	protected String jobTitleReceived;
	protected Date dateReceived = new Date();
	protected String referenceNo = "";
	protected double exchangeRate;
	protected Currency currency;//16 buah
	protected String remarks;
	protected Date transactionDate;
	protected Unit unit;
	protected Date submitDate;
	public final void transTemplateRead(AssignPanel origPanel, 
			AssignPanel approvPanel, AssignPanel receivePanel, JTextField refNoField,
			JTextArea descript ){
		
		if (approvPanel!=null){
			empApproved = approvPanel.m_employeePicker.getEmployee();
			dateApproved = approvPanel.m_datePicker.getDate();
			jobTitleApproved = approvPanel.m_jobTextField.getText();
		}
		
		if (origPanel!=null){
			empOriginator = origPanel.m_employeePicker.getEmployee();
			jobTitleOriginator = origPanel.m_jobTextField.getText();
			dateOriginator = origPanel.m_datePicker.getDate();
		}
		
		if (receivePanel!=null){
			empReceived = receivePanel.m_employeePicker.getEmployee();		
			dateReceived = receivePanel.m_datePicker.getDate();
			jobTitleReceived = receivePanel.m_jobTextField.getText();
		}
		
		referenceNo = refNoField.getText();
		description = descript.getText();
	}
	
	//	diubah ama cok gung penambahan overloading konstruktor
	public final void transTemplateRead(AssignPanel origPanel, 
			AssignPanel approvPanel, AssignPanel receivePanel, String refNoField,
			String descript,String remark, Date date, double exch)	{
		empApproved = approvPanel.m_employeePicker.getEmployee();
		empOriginator = origPanel.m_employeePicker.getEmployee();
		empReceived = receivePanel.m_employeePicker.getEmployee();
		dateApproved = approvPanel.m_datePicker.getDate();
		dateOriginator = origPanel.m_datePicker.getDate();
		dateReceived = receivePanel.m_datePicker.getDate();
		jobTitleApproved = approvPanel.m_jobTextField.getText();
		jobTitleOriginator = origPanel.m_jobTextField.getText();
		jobTitleReceived = receivePanel.m_jobTextField.getText();
		referenceNo = refNoField;
		description = descript;
		remarks = remark;
		transactionDate= date;
		exchangeRate=exch;
	}
	
	public final Date getDateApproved() {
		return dateApproved;
	}
	
	public void setDateApproved(Date dateApproved) {
		this.dateApproved = dateApproved;
	}
	
	public final Date getDateOriginator() {
		return dateOriginator;
	}
	
	public final void setDateOriginator(Date dateOriginator) {
		this.dateOriginator = dateOriginator;
	}
	
	public final Date getDateReceived() {
		return dateReceived;
	}
	
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	
	public final Employee getEmpApproved() {
		return empApproved;
	}
	
	public final void setEmpApproved(Employee empApproved) {
		this.empApproved = empApproved;
	}
	
	public final Employee getEmpOriginator() {
		return empOriginator;
	}
	
	public final void setEmpOriginator(Employee empOriginator) {
		this.empOriginator = empOriginator;
	}
	
	public final Employee getEmpReceived() {
		return empReceived;
	}
	
	public final void setEmpReceived(Employee empReceived) {
		this.empReceived = empReceived;
	}
	
	public final String getJobTitleApproved() {
		return jobTitleApproved;
	}
	
	public final void setJobTitleApproved(String jobTitleApproved) {
		this.jobTitleApproved = jobTitleApproved;
	}
	
	public final String getJobTitleOriginator() {
		return jobTitleOriginator;
	}
	
	public final void setJobTitleOriginator(String jobTitleOriginator) {
		this.jobTitleOriginator = jobTitleOriginator;
	}
	
	public final String getJobTitleReceived() {
		return jobTitleReceived;
	}
	
	public final void setJobTitleReceived(String jobTitleReceived) {
		this.jobTitleReceived = jobTitleReceived;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public final void setDescription(String description) {
		this.description = description;
	}
	
	public Transaction createNewTrans(JournalStandard journalStandard)
	{
		Date now = new Date();
		return new Transaction(description,getTransactionDate(),now,null,referenceNo,
				journalStandard.getJournal(),
				journalStandard,(short)2, unit); // status diganti == 2
	}
	public final TransactionDetail createNewTransDet(Account ac, double amt){  
		return new TransactionDetail(ac, amt, currency, exchangeRate, unit, -1);
	}
	
	public final TransactionDetail createNewTransDet(Account ac, double amt, long sub){  
		return new TransactionDetail(ac, amt, currency, exchangeRate, unit, sub);
	}
	
	public final TransactionDetail createNewTransDet(Account ac, double amt, Currency curr, double ex){
		return new TransactionDetail(ac, amt, curr, ex, unit,  -1);
	}
	
	public final TransactionDetail createNewTransDet(Account ac, double amt, Currency curr, double ex, long sub){
		return new TransactionDetail(ac, amt, curr, ex, unit, sub);
	}
	
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		//if(this.referenceNo.equals(""))
			this.referenceNo = referenceNo;
	}
	public final double getExchangeRate() {
		return exchangeRate;
	}
	public final void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public final String getRemarks() {
		return remarks;
	}
	public final void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public final Date getTransactionDate() {
		return transactionDate;
	}
	public final void setTransactionDate(Date transactiondate) {
		this.transactionDate = transactiondate;
	}
	public final Unit getUnit() {
		return unit;
	}
	public final void setUnit(Unit unit) {
		this.unit = unit;
	}
	public  Currency getCurrency() {
		return currency;
	}
	public  void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	
}
