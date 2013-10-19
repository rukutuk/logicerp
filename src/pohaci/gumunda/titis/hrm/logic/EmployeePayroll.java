/**
 *
 */
package pohaci.gumunda.titis.hrm.logic;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.OvertimeMultiplier;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Component;
import pohaci.gumunda.titis.hrm.cgui.TimeSheetSummary;
import pohaci.gumunda.titis.hrm.cgui.WorkingTimeState;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class EmployeePayroll {
	public static class PayrollCalcResult {
		public long empIdx;
		public PayrollComponent component;
		public double value;
	}
	public static class TaxArt21CalcResult {
		public long empIdx;
		public TaxArt21Component component;
		public Double value;
	}
	HRMBusinessLogic logic;
	Connection conn;
	PayrollTokenParser tokenParser;
	long employeeIndex;
	int year,month; // month: Januari = 1
	Formula formulaFlyweight;
	Hashtable vars=new Hashtable();
	private int periode=-1;
	//to get this @overTimeLESSThanOneHour value u have to run getOvertimeWorkingDay first
	private float overTimeLESSThanOneHour;
    //to get this @overTimeMOREThanOneHourvalue u have to run getOvertimeWorkingDay first
	private float overTimeMOREThanOneHour;
/*	private float MOREThanOneHour;
	private float LESSThanOneHour;
	private float overTimeOfWorkingDay;

	private float overTimeOfNonWorkingDay;*/
	private float overTimeZeroToSevenHour;
	private float overTimeEightHour;
	private float overTimeMoreThanNineHour;

	private float totalOvertime;

	public EmployeePayroll(Connection conn, int year, int month, long sessionid)
	{
		this.conn = conn;
		this.logic = new HRMBusinessLogic(conn);
		this.tokenParser = new PayrollTokenParser(conn);
		this.formulaFlyweight = new Formula();
		this.year = year;
		this.month = month;
		this.session_id= sessionid;
	}
	private void loadVars()
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
		vars.put("@Receivables@",String.valueOf(receivables));

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
	protected float calcOvertime() {
		totalOvertime=0;
		float overtime=0;
		HRMBusinessLogic logic = new HRMBusinessLogic(conn);
		try {
			OvertimeMultiplier[] overtimeMultiplier = logic.getAllOvertimeMultiplier(session_id,IDBConstants.MODUL_MASTER_DATA);
			for(int i=0;i<overtimeMultiplier.length;i++){
				OvertimeMultiplier  overMultip= overtimeMultiplier[i];
				if(overMultip.getType()==OvertimeMultiplier.WORKING_DAY){
					if(overMultip.getHourMin()==0 && overMultip.getHourMax() == 1){
						overtime= overTimeLESSThanOneHour*overMultip.getMultiplier();
					}else if(overMultip.getHourMin()==1 && overMultip.getHourMax() == 24){
						overtime= overTimeMOREThanOneHour*overMultip.getMultiplier();
					}
				}else if(overMultip.getType()==OvertimeMultiplier.NON_WORKING_DAY){
					if(overMultip.getHourMin()==0 && overMultip.getHourMax() == 7){
						overtime= overTimeZeroToSevenHour*overMultip.getMultiplier();
					}else if(overMultip.getHourMin()==7 && overMultip.getHourMax() ==8){
						overtime= overTimeEightHour*overMultip.getMultiplier();
					}else if(overMultip.getHourMin()==8 && overMultip.getHourMax() ==24){
						overtime= overTimeMoreThanNineHour*overMultip.getMultiplier();
					}
				}
				totalOvertime = totalOvertime+overtime;
				//System.out.println(totalOvertime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalOvertime;
	}
	protected float calcReceivables() {
		return 0;
	}

	protected float calcTimeSheet() {
		float timeSheet = 0;

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			TimeSheetSummary[] timeSheetSummary =
				logic.getTimeSheetSummary(session_id,
						IDBConstants.MODUL_MASTER_DATA, employeeIndex,
						month, year);
			AllowenceMultiplier[] allowanceMultiplier =
				logic.getAllAllowenceMultiplier(session_id,
						IDBConstants.MODUL_MASTER_DATA);

			float result = 0;
			if(timeSheetSummary.length>0){
				for(int i=0; i<timeSheetSummary.length; i++){
					for(int j=0; j<allowanceMultiplier.length; j++){
						if(timeSheetSummary[i].getAreaCode()
								.equals(allowanceMultiplier[j].getAreaCode())){
							result += (timeSheetSummary[i].getDays()*
									allowanceMultiplier[j].getMuliplier());
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

	protected int calcPresenceLate() {
		int presentLate=0;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			WorkingTimeState[] empOfficeWorkingTime = logic.getEmployeeOfficeWorkingTime(session_id,
					IDBConstants.MODUL_MASTER_DATA,employeeIndex);
		    presentLate = presentLate(empOfficeWorkingTime);
		    return presentLate;
		} catch (Exception e) {
			e.printStackTrace();
			return presentLate;
		}
	}
	protected int calcFieldVisits() {
		int fieldVisit=0;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			WorkingTimeState[] empOfficeWorkingTime = logic.getEmployeeOfficeWorkingTime(session_id,
					IDBConstants.MODUL_MASTER_DATA,employeeIndex);
			fieldVisit = fieldVisits(empOfficeWorkingTime);
		    return fieldVisit;
		} catch (Exception e) {
			e.printStackTrace();
			return fieldVisit;
		}
	}
	protected int calcPresenceNotLate() {
		int presentNotLate=0;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			WorkingTimeState[] empOfficeWorkingTime = logic.getEmployeeOfficeWorkingTime(session_id,
					IDBConstants.MODUL_MASTER_DATA,employeeIndex);
			presentNotLate=presentNotLate(empOfficeWorkingTime);
		    return presentNotLate;
		} catch (Exception e) {
			e.printStackTrace();
			return presentNotLate;
		}
	}
	protected int presentLate(WorkingTimeState[] empOfficeWorkingTime ) {
		int presentlate = 0;

		for (int j = 0; j < empOfficeWorkingTime.length; j++) {
			Date date = empOfficeWorkingTime[j].getDate();
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			calendar.setTime(date);
			int m_month = calendar.get(Calendar.MONTH);
			boolean isMonth = m_month+1==month;
			int m_year = calendar.get(Calendar.YEAR);
			boolean isYear = m_year==year;
			boolean isPresentLate = empOfficeWorkingTime[j].getState() == 1;
			boolean isDay = false;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			if (periode == 0) {
				isDay = (day < 16) & (day > 0);
				if (isPresentLate && isYear && isMonth && isDay) {
					presentlate++;
				}
			} else if(periode == 1){
				isDay = (day < 32) & (day > 15);
				if (isPresentLate && isYear && isMonth && isDay) {
					presentlate++;
				}
			}else{
				isDay = true;
				if (isPresentLate && isYear && isMonth && isDay) {
					presentlate++;
				}
			}
		}
		return presentlate;
	}
	protected int presentNotLate(WorkingTimeState[] empOfficeWorkingTime ) {
		int presentNotlate = 0;

		try {

		for (int j = 0; j < empOfficeWorkingTime.length; j++) {
				Date date = empOfficeWorkingTime[j].getDate();
				Calendar calendar = Calendar.getInstance(Locale.getDefault());
				calendar.setTime(date);
				int m_month = calendar.get(Calendar.MONTH);
				boolean isMonth = m_month+1==month;
				int m_year = calendar.get(Calendar.YEAR);
				boolean isYear = m_year==year;
				boolean isPresentLate = empOfficeWorkingTime[j].getState() == 0;
				boolean isDay = false;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				if (periode == 0) {
					isDay = (day < 16) & (day > 0);
					if (isPresentLate && isYear && isMonth && isDay) {
						presentNotlate++;
					}
				} else if(periode == 1){
					isDay = (day < 32) & (day > 15);
					if (isPresentLate && isYear && isMonth && isDay) {
						presentNotlate++;
					}
				}else{
					isDay = true;
					if (isPresentLate && isYear && isMonth && isDay) {
						presentNotlate++;
					}
				}
			}
			return presentNotlate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return presentNotlate;
	}
	Calendar cal = Calendar.getInstance();
	protected int fieldVisits(WorkingTimeState[] empOfficeWorkingTime ) {
		int fieldVisit=0;
		for (int j = 0; j < empOfficeWorkingTime.length; j++) {
			cal.setTime(empOfficeWorkingTime[j].getDate());
			boolean isMonth =
				cal.get(Calendar.MONTH) == month;
			boolean isYear =
				cal.get(Calendar.YEAR) == year;
			boolean isFieldVisit = empOfficeWorkingTime[j].getState() == 3;
			if (isFieldVisit && isYear && isMonth) {
				fieldVisit++;
			}
		}
		return fieldVisit;
	}

	/**
	 * Untuk menghitung payroll seorang employee
	 * @param comp
	 * @return
	 */
	public EmployeePayroll.PayrollCalcResult[] calcPayrollComponent(PayrollCategoryComponent[] comp,long empIdx)
	{
		int i;
		employeeIndex = empIdx;
        Hashtable items = new Hashtable();
		EmployeePayroll.PayrollCalcResult[] r = new EmployeePayroll.PayrollCalcResult[comp.length];
		loadVars();
        boolean recalc = true;
        boolean calculatedOne = true;
        Formula formula = formulaFlyweight;
        while (recalc && calculatedOne)
        {
            recalc = false;
            calculatedOne = false;
            //loadItems(); ga perlu lagi
    		for (i=0; i<comp.length;i++)
    		{
                int itemIndex = (int) comp[i].getPayrollComponent().getIndex();
                if (itemIndex ==72){
                	System.err.println("a");
                }
                if (items.containsKey(new Integer(itemIndex)))
                    continue;
                formula.parseFormula(comp[i].getFormulaEntity(),tokenParser);
                if (incompleteItems(formula,items))
                {
                    recalc=true;
                    continue;
                }
    			double result = 0;

    			if(comp[i].getFormulaEntity().getEveryWhichMonth()>0){
    				if(comp[i].getFormulaEntity().getEveryWhichMonth()== month)
    					result = formula.evaluate(vars,items);
    			}else{
    				result = formula.evaluate(vars,items);
    			}
    			if(result>0){
    				NumberRounding numberRounding = comp[i].getFormulaEntity().getNumberRounding();
    				if(numberRounding.getRoundingMode()>-1){
    					result = numberRounding.round(result);
    				} else {
    					NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
    					result = nr.round(result);
    				}
    			}
    			r[i] = new EmployeePayroll.PayrollCalcResult();
    			r[i].empIdx = empIdx;
    			r[i].component = comp[i].getPayrollComponent();
    			r[i].value = result;
    			BigDecimal hasilNya = new BigDecimal(result);
    			items.put(new Integer((int)r[i].component.getIndex()),hasilNya.toString());
                calculatedOne= true;
    		}
        }
    	if (empIdx==29){
			System.err.println("A");
		}
        for (i=0; i<comp.length;i++)
        {
        	if (r[i]!=null)
        		  continue;
        	r[i] = new EmployeePayroll.PayrollCalcResult();
        	r[i].empIdx = empIdx;
        	r[i].component = comp[i].getPayrollComponent();
        	r[i].value = 0;
        }
		return r;
	}
    private boolean incompleteItems(Formula formula, Hashtable items) {
        Integer itemsNeeded[] = formula.readItems();
        for (int x=0; x<itemsNeeded.length; x++)
        {
            Integer itemNeededIdx = itemsNeeded[x];
            if (!items.containsKey(itemNeededIdx))
            {
                return true;
            }
        }
        return false;
    }
	public EmployeePayroll.TaxArt21CalcResult[] calcTaxArt21Component(int monthSelected, int yearSelected, Unit unitSelected,
			Employee_n employeeSelected,
			TaxArt21Component[] taxComp)
	{
		int i;
		employeeIndex = employeeSelected.getAutoindex();
		EmployeePayroll.TaxArt21CalcResult[] r = new EmployeePayroll.TaxArt21CalcResult[taxComp.length];
		//loadVars();//actually there is no "var" that involved within Tax_Art_21
		/**
		 *  employeePayrollSubmit --> cuman d ambil tanggal,tahun,dan unit untuk kondisi
		 *  untuk employeeIndex diisi dengan employee yg ingin dicari
		 */
		//employeePayrollSubmit.setEmployeeIndex(employeeIndex);
        Hashtable items = new Hashtable();
		items = loadItems(monthSelected, yearSelected, unitSelected, employeeSelected);//every employee has payrollComponent
        Formula formula = formulaFlyweight;
		for (i=0; i<taxComp.length;i++)
		{
			if (taxComp[i].getIndex()==14){
				System.err.println("a");
			}
			Double result = null;
            double formulaResult = -1;

			if(taxComp[i].getFormulaEntity()!=null){
				FormulaEntity fE = taxComp[i].getFormulaEntity();
				formula.parseFormula(fE,tokenParser);
				/*if(TaxComp[i].getFormulaEntity().getEveryWhichMonth()>0){
					if(TaxComp[i].getFormulaEntity().getEveryWhichMonth()== month)
						result = formula.evaluate(vars,items);
				}else{
					result = formula.evaluate(vars,items);
				}*/
				formulaResult = formula.evaluate(vars, items);
				if(formulaResult>0){
					NumberRounding numberRounding = taxComp[i].getFormulaEntity().getNumberRounding();
					if(numberRounding.getRoundingMode()>-1){
						formulaResult = numberRounding.round(formulaResult);
					}

					// perlu dicompare kah?
					if(taxComp[i].isComparable()){
						double comparator = taxComp[i].getComparatorValue();
						if(taxComp[i].getComparationMode().equalsIgnoreCase("MAXIMUM")){
							formulaResult = (formulaResult >= comparator) ? formulaResult : comparator;
						}else{
							formulaResult = (formulaResult <= comparator) ? formulaResult : comparator;
						}
					}

					// di negatifken
					if(taxComp[i].isNegative())
						formulaResult = (-formulaResult);
				}

				result = new Double(formulaResult);
			}

			if(items.size()==0)
				result = null;

			r[i] = new EmployeePayroll.TaxArt21CalcResult();
			r[i].empIdx = employeeIndex;
			r[i].component = taxComp[i];
			r[i].value = result;
		}
		return r;
	}

	long session_id;
	private Hashtable loadItems(int monthSelected, int yearSelected, Unit unitSelected, Employee_n employeeSelected) {
		Hashtable items = new Hashtable();
		items.clear();
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			/*EmployeePayrollSubmit[] employeePayrollSubmits = logic.getAllPostedEmployeePayrollSubmit(session_id,
					IDBConstants.MODUL_MASTER_DATA, monthSelected, yearSelected, unitSelected, employeeSelected);

			for(int x=0;x<employeePayrollSubmits.length;x++){
				int itemIdx=(int)employeePayrollSubmits[x].getPayrollComponentIndex();
				//String item = "@ITEM@"+new Long(itemIdx).toString()+"@ITEM@";
				double value = employeePayrollSubmits[x].getValue();
				if(value!=-1)
					items.put(new Integer(itemIdx),String.valueOf(EmployeePayrollSubmitS[x].getValue()));
				items.put(new Integer(itemIdx), String.valueOf(value));
			}*/

			items = logic.getAllValueForTax(session_id, IDBConstants.MODUL_MASTER_DATA, monthSelected, yearSelected, unitSelected, employeeSelected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	public int getPeriode() {
		return periode;
	}
	public void setPeriode(int periode) {
		this.periode = periode;
	}
}
