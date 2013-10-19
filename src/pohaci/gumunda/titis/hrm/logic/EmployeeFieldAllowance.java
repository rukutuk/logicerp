package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;

import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class EmployeeFieldAllowance extends EmployeePayroll {
	private String m_areaCode;
	private int m_timeSheetDays;

	public EmployeeFieldAllowance(Connection conn, int year, int month,
			long sessionid) {
		super(conn, year, month, sessionid);
	}

	/*private
	void loadVars()
	{
		vars.clear();
		int notLate = calcPresenceNotLate();
		int late = calcPresenceLate();
		int fieldVisits = calcFieldVisits();
		float overtime = calcOvertime();
		float timeSheet = calcTimeSheet();
		float receivables = calcReceivables();
 		vars.put("@Presence@",String.valueOf(notLate+late));
		vars.put("@PresenceLate@",String.valueOf(late));
		vars.put("@PresenceNotLate@",String.valueOf(notLate));
		vars.put("@FieldVisit@",String.valueOf(fieldVisits));
		vars.put("@Overtime@",String.valueOf(overtime));
		vars.put("@TimeSheet@",String.valueOf(timeSheet));
	}*/

	protected int calcFieldVisits() {
		return 0;
	}

	protected float calcOvertime() {
		return 0;
	}

	protected int calcPresenceLate() {
		return 0;
	}

	protected int calcPresenceNotLate() {
		return 0;
	}

	protected float calcReceivables() {
		return 0;
	}

	protected float calcTimeSheet() {
		float timeSheet = 0;

			HRMBusinessLogic logic = new HRMBusinessLogic(conn);

			try {
				AllowenceMultiplier[] allowanceMultiplier =
					logic.getAllAllowenceMultiplier(session_id,
							IDBConstants.MODUL_MASTER_DATA);

				for(int i=0; i<allowanceMultiplier.length; i++){
					if(m_areaCode.equals(allowanceMultiplier[i].getAreaCode())){
						timeSheet = m_timeSheetDays *
							allowanceMultiplier[i].getMuliplier();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return timeSheet;
		//return super.calcTimeSheet();
	}

	public PayrollCalcResult[] calcPayrollComponent(PayrollCategoryComponent[] comp, long empIdx,
			String areaCode, int timeSheetDays) {
		m_areaCode = areaCode;
		m_timeSheetDays = timeSheetDays;
		return super.calcPayrollComponent(comp, empIdx);
	}

}
