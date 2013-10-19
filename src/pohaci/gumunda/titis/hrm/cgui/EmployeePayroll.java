/**
 * 
 */
package pohaci.gumunda.titis.hrm.cgui;

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;

public class EmployeePayroll extends TransactionTemplateEntity {
	private long index = -1;
	private Date submittedDate = null;
	//private Unit unit = null;
	private short month = -1;
	private short year = -1;
	private short paymentPeriod = -1;
	private short payrollType = -1;
	
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public short getMonth() {
		return month;
	}
	public void setMonth(short month) {
		this.month = month;
	}
	public short getPaymentPeriode() {
		return paymentPeriod;
	}
	public void setPaymentPeriode(short paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}
	public short getPayrollType() {
		return payrollType;
	}
	public void setPayrollType(short payrollType) {
		this.payrollType = payrollType;
	}
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	/*public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}*/
	public short getYear() {
		return year;
	}
	public void setYear(short year) {
		this.year = year;
	}
	public EmployeePayroll() {
		
	}
	
	
}
