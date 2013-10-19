package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeOvertimeSubmit extends EmployeePayrollSubmit{
	
    //to get this @overTimeLESSThanOneHour value u have to run getOvertimeWorkingDay first
	private float overTimeLESSThanOneHour;
    //to get this @overTimeMOREThanOneHourvalue u have to run getOvertimeWorkingDay first
	private float overTimeMOREThanOneHour;
	private float overTimeOfWorkingDay;
	private float overTimeZeroToSevenHour;
	private float overTimeEightHour;
	private float overTimeMoreThanNineHour;
	private long overtimeMultiIndex;
	private float overtimeValue;
	
	private long multipOverTimeLESSThanOneHour;
	private long multipOverTimeMOREThanOneHour;
	private long multipOverTimeZeroToSevenHour;
	private long multipOverTimeEightHour;
	private long multipOverTimeMoreThanNineHour;
	private long multiplierIndex;

	
	
	public EmployeeOvertimeSubmit(long session_id,Connection conn ){
		HRMBusinessLogic logic = new HRMBusinessLogic(conn);
		try {
			OvertimeMultiplier[] overtimeMultiplier = logic.getAllOvertimeMultiplier(session_id,IDBConstants.MODUL_MASTER_DATA);
			for(int i=0;i<overtimeMultiplier.length;i++){
				OvertimeMultiplier  overMultip= overtimeMultiplier[i];
				if(overMultip.getType()==OvertimeMultiplier.WORKING_DAY){
					if(overMultip.getHourMin()==0 && overMultip.getHourMax() == 1){
						multipOverTimeLESSThanOneHour= overMultip.getIndex();
					}else if(overMultip.getHourMin()==1 && overMultip.getHourMax() == 24){
						multipOverTimeMOREThanOneHour= overMultip.getIndex();
					}
				}else if(overMultip.getType()==OvertimeMultiplier.NON_WORKING_DAY){
					if(overMultip.getHourMin()==0 && overMultip.getHourMax() == 7){
						multipOverTimeZeroToSevenHour= overMultip.getIndex();
					}else if(overMultip.getHourMin()==7 && overMultip.getHourMax() ==8){
						multipOverTimeEightHour= overMultip.getIndex();
					}else if(overMultip.getHourMin()==8 && overMultip.getHourMax() ==24){
						multipOverTimeMoreThanNineHour= overMultip.getIndex();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public EmployeeOvertimeSubmit(long ind,short status){
		super(ind,status);
	}
	public EmployeeOvertimeSubmit(){
		super();
	}
	
	public void setOverTime(float overTimeLESSThanOneHour, float overTimeMOREThanOneHour, float overTimeZeroToSevenHour,
			float overTimeEightHour,
			float overTimeMoreThanNineHour){
		this.overTimeLESSThanOneHour=overTimeLESSThanOneHour;
		this.overTimeMOREThanOneHour=overTimeMOREThanOneHour;
		this.overTimeZeroToSevenHour=overTimeZeroToSevenHour;
		this.overTimeEightHour=overTimeEightHour;
		this.overTimeMoreThanNineHour=overTimeMoreThanNineHour;
	}

	public float getOverTimeEightHour() {
		return overTimeEightHour;
	}

	public void setOverTimeEightHour(float overTimeEightHour) {
		this.overTimeEightHour = overTimeEightHour;
	}

	public float getOverTimeLESSThanOneHour() {
		return overTimeLESSThanOneHour;
	}

	public void setOverTimeLESSThanOneHour(float overTimeLESSThanOneHour) {
		this.overTimeLESSThanOneHour = overTimeLESSThanOneHour;
	}

	public float getOverTimeMoreThanNineHour() {
		return overTimeMoreThanNineHour;
	}

	public void setOverTimeMoreThanNineHour(float overTimeMoreThanNineHour) {
		this.overTimeMoreThanNineHour = overTimeMoreThanNineHour;
	}

	public float getOverTimeMOREThanOneHour() {
		return overTimeMOREThanOneHour;
	}

	public void setOverTimeMOREThanOneHour(float overTimeMOREThanOneHour) {
		this.overTimeMOREThanOneHour = overTimeMOREThanOneHour;
	}

	public float getOverTimeOfWorkingDay() {
		return overTimeOfWorkingDay;
	}

	public void setOverTimeOfWorkingDay(float overTimeOfWorkingDay) {
		this.overTimeOfWorkingDay = overTimeOfWorkingDay;
	}

	public float getOverTimeZeroToSevenHour() {
		return overTimeZeroToSevenHour;
	}

	public void setOverTimeZeroToSevenHour(float overTimeZeroToSevenHour) {
		this.overTimeZeroToSevenHour = overTimeZeroToSevenHour;
	}

	public long getOvertimeMultiIndex() {
		return overtimeMultiIndex;
	}

	public void setOvertimeMultiIndex(long overtimeMultiIndex) {
		this.overtimeMultiIndex = overtimeMultiIndex;
	}

	public float getOvertimeValue() {
		return overtimeValue;
	}

	public void setOvertimeValue(float overtimeValue) {
		this.overtimeValue = overtimeValue;
	}

	public long getMultipOverTimeEightHour() {
		return multipOverTimeEightHour;
	}

	public long getMultipOverTimeLESSThanOneHour() {
		return multipOverTimeLESSThanOneHour;
	}

	public long getMultipOverTimeMoreThanNineHour() {
		return multipOverTimeMoreThanNineHour;
	}

	public long getMultipOverTimeMOREThanOneHour() {
		return multipOverTimeMOREThanOneHour;
	}

	public long getMultipOverTimeZeroToSevenHour() {
		return multipOverTimeZeroToSevenHour;
	}
	public long getMultiplierIndex() {
		return multiplierIndex;
	}
	public void setMultiplierIndex(long multiplierIndex) {
		this.multiplierIndex = multiplierIndex;
	}
	
	
	
}
