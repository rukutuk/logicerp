package pohaci.gumunda.titis.hrm.cgui;

import java.awt.Color;
import java.util.Date;

public class WorkingTimeState extends Holiday {
	  public static final int PRESENT_NOT_LATE =0;
	  public static final int PRESENT_LATE = 1;
	  public static final int ABSENT = 2;
	  public static final int FIELD_VISIT = 3;
	  public static final int OTHER = 4;
	  public static final int CLEAER = 5;
	  

	public static final Color PresentNotLate = new Color(210, 255, 210);

	public static final Color PresentLate = new Color(255, 255, 164);

	public static final Color Absent = new Color(255, 211, 168);

	public static final Color FieldVisit = new Color(255, 128, 255);

	public static final Color DefaultWorkingDay = new Color(255, 255, 255);

	public static final Color NonWorkingDay = new Color(192, 192, 192);

	public static final Color Holiday = new Color(255, 0, 0);
	
	public static final Color Other=new Color(153,204,255);

	private int state;
	private float overTime;

	public WorkingTimeState(Date date) {
		super(date, null);

	}

	public WorkingTimeState(Date date, int astate) {
		super(date, null);
		state = astate;
	}
	public WorkingTimeState(Date date, int astate,float overtime) {
		super(date, null);
		state = astate;
		overTime=overtime;
	}

	public void setState(int astate) {
		state = astate;
	}

	public int getState() {
		return state;
	}
	public void setOverTime(float overtime){
		overTime=overtime;
	}
	public float getOverTime(){
		return overTime;
	}

}
