/**
 * 
 */
package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;
import java.util.Hashtable;

import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.TimeSheetSummary;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

/**
 * @author dark-knight
 * 
 */
public class TimeSheetCalculator {

	private Connection conn;

	private long sessionId;

	private long employeeIndex;

	private int year;

	private int month;

	/**
	 * @param conn
	 * @param sessionId
	 * @param employeeIndex
	 * @param year
	 * @param month
	 */
	public TimeSheetCalculator(Connection conn, long sessionId,
			long employeeIndex, int year, int month) {
		this.conn = conn;
		this.sessionId = sessionId;
		this.employeeIndex = employeeIndex;
		this.year = year;
		this.month = month;
	}

	public double calcTimeSheet() {
		double timeSheet = 0;
		
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			TimeSheetSummary[] timeSheetSummary = logic.getTimeSheetSummary(
					sessionId, IDBConstants.MODUL_MASTER_DATA, employeeIndex,
					month, year);
			AllowenceMultiplier[] allowanceMultiplier = logic
					.getAllAllowenceMultiplier(sessionId,
							IDBConstants.MODUL_MASTER_DATA);
			if (employeeIndex == 8){
				System.err.println("a");
			}
			double result = 0;
			if (timeSheetSummary.length > 0) {
				for (int i = 0; i < timeSheetSummary.length; i++) {
					for (int j = 0; j < allowanceMultiplier.length; j++) {
						if (timeSheetSummary[i].getAreaCode().equals(
								allowanceMultiplier[j].getAreaCode())) {
							result += (timeSheetSummary[i].getDays() * allowanceMultiplier[j]
									.getMuliplier());
							break;
						}
					}
				}
			}
			timeSheet = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeSheet;
	}
	
	/**
	 * Berhubung ada permintaan baru yang mak bedunduk... maka dibuatlah method ini.
	 * Method ini akan mengevaluasi nilai rate dari formula yang terkait dengan field allowances.
	 * Caranya konyol sekali:
	 * 1. dari formula component, diabaikan yang bervariable '@Time Sheet@' dengan cara
	 *    variable tersebut diganti nilai 1
	 * 2. setelah di-evaluate, maka akan didapatkan rate
	 * 
	 * Masalahnya:
	 * 1. hanya dapat diperuntukkan untuk field allowance saja
	 * 2. formulanya tidak bisa jika terdapat acuan ke component yang lain
	 * 3. kalau terjadi perubahan formula akan menjadi runyam, artinya rate-nya pasti akan berubah.
	 * 	  tentunya akan beda nilainya dengan hasil field allowance hasil submit-an
	 * 
	 * Added by dark-knight
	 * @param component
	 * @return
	 */
	public double rate(PayrollCategoryComponent component) {
		Formula formula = new Formula();
		
		formula.parseFormulaForTimeSheetRate(component.getFormulaEntity().getFormulaCode(), new PayrollTokenParser(conn));
		double result = formula.evaluate(new Hashtable(), new Hashtable());
		
		return result;
	}
}
