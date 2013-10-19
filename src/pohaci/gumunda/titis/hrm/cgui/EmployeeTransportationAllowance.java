package pohaci.gumunda.titis.hrm.cgui;

public class EmployeeTransportationAllowance extends EmployeePayrollSubmit{
	
	private int presence;
	private int presenceLate;
	private int presenceNotLate;
	public EmployeeTransportationAllowance(){
		super();
	}
	public EmployeeTransportationAllowance(long ind,short status){
		super(ind,status);
	}
	public int getPresence() {
		return presence;
	}
	public void setPresence(int presence) {
		this.presence = presence;
	}
	public int getPresenceLate() {
		return presenceLate;
	}
	public void setPresenceLate(int presenceLate) {
		this.presenceLate = presenceLate;
	}
	public int getPresenceNotLate() {
		return presenceNotLate;
	}
	public void setPresenceNotLate(int presenceNotLate) {
		this.presenceNotLate = presenceNotLate;
	}
	

}
