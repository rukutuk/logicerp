package pohaci.gumunda.titis.hrm.cgui;

public class EmployeeMealAllowanceSubmit  extends EmployeePayrollSubmit{
	private int presence;
	
	public EmployeeMealAllowanceSubmit(){
		super();
	}
	public EmployeeMealAllowanceSubmit(long index,short status){
		super(index,status);
	}

	public int getPresence() {
		return presence;
	}

	public void setPresence(int presence) {
		this.presence = presence;
	}
}
