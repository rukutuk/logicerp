/**
 *
 */
package pohaci.gumunda.titis.hrm.cgui;


/**
 * @author dark-knight
 *
 */
public class Overtime {
	private long index;
	private Employee employee;
	private int month;
	private int year;
	private OvertimeMultiplier multiplier;
	private float overtime;
	/**
	 * @return the index
	 */
	public long getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(long index) {
		this.index = index;
	}
	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}
	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the multiplier
	 */
	public OvertimeMultiplier getMultiplier() {
		return multiplier;
	}
	/**
	 * @param multiplier the multiplier to set
	 */
	public void setMultiplier(OvertimeMultiplier multiplier) {
		this.multiplier = multiplier;
	}
	/**
	 * @return the overtime
	 */
	public float getOvertime() {
		return overtime;
	}
	/**
	 * @param overtime the overtime to set
	 */
	public void setOvertime(float overtime) {
		this.overtime = overtime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		Overtime newOvertime = new Overtime();
		newOvertime.setEmployee(employee);
		newOvertime.setIndex(index);
		newOvertime.setMonth(month);
		newOvertime.setMultiplier(multiplier);
		newOvertime.setOvertime(overtime);
		newOvertime.setYear(year);
		return newOvertime;
	}



}
