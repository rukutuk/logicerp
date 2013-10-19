package pohaci.gumunda.titis.hrm.cgui;

public class EmployeeOvertime {
	private float workingDayOvertime;
	private float nonWorkingDayOvertime;
	/**
	 * @param workingDayOvertime
	 * @param nonWorkingDayOvertime
	 */
	public EmployeeOvertime(float workingDayOvertime,
			float nonWorkingDayOvertime) {
		super();
		this.workingDayOvertime = workingDayOvertime;
		this.nonWorkingDayOvertime = nonWorkingDayOvertime;
	}
	/**
	 * @return the workingDayOvertime
	 */
	public float getWorkingDayOvertime() {
		return workingDayOvertime;
	}
	/**
	 * @return the nonWorkingDayOvertime
	 */
	public float getNonWorkingDayOvertime() {
		return nonWorkingDayOvertime;
	}


}
