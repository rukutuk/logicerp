package pohaci.gumunda.titis.hrm.cgui;

import java.util.Date;

import pohaci.gumunda.titis.accounting.entity.Unit;

public class EmployeePayrollSubmit implements Cloneable {

	private Employee_n emp;
	private int status;
	private Date submitedDate;
	private Unit unit;
	private int month;
	private int year;
	private PayrollComponent payrollComponent;
	private long PayrollComponentIndex;
	private double value=-1;
	private int paymentPeriode;
	private long jurnalID;
	private int payrollType;
	private int paychequeType;
	private long index;
	private long employeeIndex;
	private long transaction;
	private String jobTitle;
	private String name;
	private String employeeNo;
	private long employeePayroll;

	// i add this
	private int rowComponentAtTable;
	private int colComponentAtTable;
	public Object getIndex;

	public EmployeePayrollSubmit(long index,int status){
		this.index=index;
		this.status = status;
	}
	public EmployeePayrollSubmit(long index,Employee_n emp, int status,
			Date submitedDate, Unit unit, int month, int year,
			PayrollComponent payrollComponent,float value ,int paymentPeriode,long jurnalID,int payrollType,long transaction) {
		this.emp = emp;
		this.status = status;
		this.submitedDate = submitedDate;
		this.unit = unit;
		this.month = month;
		this.year = year;
		this.payrollComponent=payrollComponent;
		this.value=value;
		this.paymentPeriode=paymentPeriode;
		this.jurnalID=jurnalID;
		this.payrollType=payrollType;
		this.index=index;
		this.transaction=transaction;
	}

	public EmployeePayrollSubmit() {
	}

	public Employee_n getEmployee_n() {
		return emp;
	}

	public void setEmployee_n(Employee_n emp) {
		this.emp = emp;
		setJobTitle(emp.getJobtitle());
		setEmployeeIndex(emp.getAutoindex());
		if(emp.getMidlename().equals("")){
			name= emp.getFirstname()+" "+emp.getLastname();
		}else{
			name= emp.getFirstname()+" "+emp.getMidlename()+" "+emp.getLastname();
		}
		setName(name);
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSubmittedDate() {
		return submitedDate;
	}

	public void setSubmittedDate(Date submitedDate) {
		this.submitedDate = submitedDate;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public PayrollComponent getPayrollComponent() {
		return payrollComponent;
	}

	public void setPayrollComponent(PayrollComponent payrollComponent) {
		this.payrollComponent = payrollComponent;
		setPayrollComponentIndex(payrollComponent.getIndex());
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	public String getMonthYear(){
		return getYear()+"-0"+getMonth();
	}

	public int getPaymentPeriode() {
		return paymentPeriode;
	}

	public void setPaymentPeriode(int paymentPeriode) {
		this.paymentPeriode = paymentPeriode;
	}

	public long getJurnalID() {
		return jurnalID;
	}

	public void setJurnalID(long jurnalID) {
		this.jurnalID = jurnalID;
	}

	public Object clone(){
		EmployeePayrollSubmit emp=null;
		try {
			 emp=(EmployeePayrollSubmit) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return emp;
	}

	public int getPayrollType() {
		return payrollType;
	}

	public void setPayrollType(int payrollType) {
		this.payrollType = payrollType;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}


	public long getTransaction() {
		return transaction;
	}

	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public long getEmployeeIndex() {
		return employeeIndex;
	}

	public void setEmployeeIndex(long employeeIndex) {
		this.employeeIndex = employeeIndex;
	}

	public long getPayrollComponentIndex() {
		return PayrollComponentIndex;
	}

	public void setPayrollComponentIndex(long payrollComponentIndex) {
		PayrollComponentIndex = payrollComponentIndex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public int getColComponentAtTable() {
		return colComponentAtTable;
	}
	public void setColComponentAtTable(int colAtTable) {
		this.colComponentAtTable = colAtTable;
	}
	public int getRowComponentAtTable() {
		return rowComponentAtTable;
	}
	public void setRowComponentAtTable(int rowAtTable) {
		this.rowComponentAtTable = rowAtTable;
	}
	public int getPaychequeType() {
		return paychequeType;
	}
	public void setPaychequeType(int paychequeType) {
		this.paychequeType = paychequeType;
	}
	public long getEmployeePayroll() {
		return employeePayroll;
	}
	public void setEmployeePayroll(long employeePayroll) {
		this.employeePayroll = employeePayroll;
	}

}
