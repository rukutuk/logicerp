package pohaci.gumunda.titis.hrm.cgui.payrollcategory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PayrollCategoryBackupMaster {
	private long id;
	private int month;
	private int year;
	private String description;
	private Date workingDate;
	public PayrollCategoryBackupMaster(long id, int month, int year,
			String description, Date workingDate) {
		super();
		this.id = id;
		this.month = month;
		this.year = year;
		this.description = description;
		this.workingDate = workingDate;
	}
	public long getId() {
		return id;
	}
	public int getMonth() {
		return month;
	}
	public int getYear() {
		return year;
	}
	public String getDescription() {
		return description;
	}
	public Date getWorkingDate() {
		return workingDate;
	}

	public String getPeriod() {
		Calendar cal = Calendar.getInstance();
		cal.set(getYear(), getMonth(), 1);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		return dateFormat.format(cal.getTime());
	}
}
