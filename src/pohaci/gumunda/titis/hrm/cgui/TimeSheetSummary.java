package pohaci.gumunda.titis.hrm.cgui;

public class TimeSheetSummary {
	long employeeID;
	String areaCode;
	int month;
	int year;
	int days;
	
	public TimeSheetSummary(long employeeID, String areaCode, 
			int month, int year, int days) {
		this.employeeID = employeeID;
		this.areaCode = areaCode;
		this.month = month;
		this.year = year;
		this.days = days;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(long employeeID) {
		this.employeeID = employeeID;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	
}
