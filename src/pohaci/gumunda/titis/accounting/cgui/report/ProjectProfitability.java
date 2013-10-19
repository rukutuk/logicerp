package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.TimeSheetSummary;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollTokenParser;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class ProjectProfitability {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	UnitPicker m_unitPicker;
	DecimalFormat m_desimalFormat;
	AccountingBusinessLogic m_logic;
	DatePicker m_datePicker;
	String m_projectStatusCB, m_keyword;
	SimpleDateFormat m_dateFormat, m_dateFormat2;
	
	public ProjectProfitability() {
		setEmpty();
	}
	
	public ProjectProfitability(Connection conn, long sessionid,
			UnitPicker unitPicker, DatePicker date, String projectStatusCB, String keyword,boolean isExcel) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_unitPicker = unitPicker;
		m_datePicker = date;
		m_projectStatusCB = projectStatusCB;
		m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		m_desimalFormat = new DecimalFormat("#,##0.00");
		m_logic = new AccountingBusinessLogic(m_conn);
		m_keyword = keyword;
		getNonEmpty(isExcel);
	}
	
	private void setEmpty() {
		try {
			String filename = "project_profitability";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			parameters.put("param_date", "");
			
			header(model);
			
			model.addRow(new Object[] { "", "", "", "", "", "", "", "", "", "",
					"", "", "", "","",new Integer(1) });
			model.addRow(new Object[] { "TOTAL", "","", "", "", "", "", "", "",
					"", "", "", "", "", new Integer(2) });
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					compiledRptFilename, parameters, ds);
			
			m_jrv = new JRViewer(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void getNonEmpty(boolean isExcel) {
		try {
			String filename = "project_profitability";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			if (m_unitPicker.getUnit() != null)
				parameters.put("param_unit_code", m_unitPicker.getUnit()
						.toString());
			parameters.put("param_date", m_dateFormat.format(m_datePicker
					.getDate()));
			
			header(model);
			
			GenericMapper mapper = MasterMap.obtainMapperFor(ProjectData.class);
			mapper.setActiveConn(m_conn);
			
			String strWhere = "";
			String keyword = " AND "
				+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_CODE
				+ " LIKE '%" + m_keyword + "%'";
			String strUnit = "";
			if (m_unitPicker.getUnit() != null)
				strUnit = " AND " + IDBConstants.ATTR_UNIT + "="
				+ m_unitPicker.getUnit().getIndex();
			if (m_projectStatusCB.equals("On Going")) {
				strWhere = IDBConstants.ATTR_AUTOINDEX
				+ " IN (SELECT "
				+ IDBConstants.ATTR_PROJECT
				+ " FROM "
				+ pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_CONTRACT
				+ " WHERE "
				+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_ACTUAL_END_DATE
				+ " is null or "
				+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_ACTUAL_END_DATE
				+ "<'" + m_dateFormat2.format(m_datePicker.getDate()) + "') " + strUnit;
			} else if (m_projectStatusCB.equals("Past")) {
				strWhere = IDBConstants.ATTR_AUTOINDEX
				+ " IN (SELECT "
				+ IDBConstants.ATTR_PROJECT
				+ " FROM "
				+ pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_CONTRACT
				+ " WHERE "
				+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_ACTUAL_END_DATE
				+ ">='" + m_dateFormat2.format(m_datePicker.getDate())
				+ "') " + strUnit;
			} else {
				if (m_unitPicker.getUnit()==null)
					strWhere = "1=1";
				else
					strWhere =IDBConstants.ATTR_UNIT +"="+m_unitPicker.getUnit().getIndex();
			}
			
			strWhere += keyword;
			List list = mapper.doSelectWhere(strWhere);
			Iterator iterator = list.iterator();
			int i = 0;
			
			double totSalesValue = 0;
			double totFA = 0;
			double totOtherPersonnelCostValue = 0;
			double totMTCValue = 0;
			double totOperasionalCostValue = 0;
			double totSubContractorsSubversionValue = 0;
			double totDepreciationAmortizationValue = 0;
			double totReparationMantenanceValue = 0;
			double totOtherCostValue = 0;
			double totTotal = 0;
			double totGrossPropit = 0;
			while (iterator.hasNext()) {
				ProjectData proj = (ProjectData) iterator.next();
				String cust = "";
				if (proj.getCustomer() != null)
					cust = proj.getCustomer().getName();
				
				VariableAccountSetting vasFA = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_FIELD_ALLOWANCE);
				VariableAccountSetting vasRemunerationCost = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_PERSONNEL_COST);
				VariableAccountSetting vasOtherPersonnelCost = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_OTHER_PERSONNEL_COST);
				VariableAccountSetting vasSales = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_SALES);
				VariableAccountSetting vasMTC = VariableAccountSetting
				.createVariableAccountSetting(
						m_conn,
						m_sessionid,
						IConstants.ATTR_VARS_MATERIALS_TOOLS_CONSUMABLES);
				VariableAccountSetting vasOperasionalCost = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_PROJECT_OPERATIONAL_COST);
				VariableAccountSetting vasSubContractorsSubversion = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_THIRD_PARTY_COST);
				VariableAccountSetting vasDepreciationAmortization = VariableAccountSetting
				.createVariableAccountSetting(
						m_conn,
						m_sessionid,
						IConstants.ATTR_VARS_DEPRECIATION_AND_AMORTISATION);
				VariableAccountSetting vasReparationMantenance = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_REPAIRMENT_COST);
				VariableAccountSetting vasOtherCost = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_OTHERS_COGS);
				
				
				
				String sales = getQuery1(proj.getIndex());
				System.err.println(sales);
				
				String fa = getQueryFAForPeriod(vasFA);
				String personnelCost = getQuery2(proj.getIndex());
				String otherPersonnelCost = getQuery2(proj.getIndex());
				String MTC = getQuery2(proj.getIndex());
				String operasionalCost = getQuery2(proj.getIndex());
				String subContractorsSubversion = getQuery2(proj.getIndex()); //3
				String subDepreciationAmortization = getQuery2(proj.getIndex()); //4
				String reparationMantenance = getQuery2(proj.getIndex()); //4
				String otherCost = getQuery2(proj.getIndex()); //2
				
				AccountingSQLSAP sql = new AccountingSQLSAP();
				
				// baca month,year dan trans dari employeepayroll yang transaksinya sama dengan transaksi account  
				List monthYear = sql.getMonthYearPayrollByQuery(m_conn, fa);
				
				String querySales = getQuery(vasSales, sales);
				String queryPersonnelCost = getQuery(vasRemunerationCost, personnelCost);
				String queryOtherPersonnelCost = getQuery(vasOtherPersonnelCost, otherPersonnelCost);
				String queryMTC = getQuery(vasMTC, MTC);
				String queryoperasionalCost = getQuery(vasOperasionalCost,
						operasionalCost);
				String querySubContractorsSubversion = getQuery(
						vasSubContractorsSubversion, subContractorsSubversion);
				String queryDepreciationAmortization = getQuery(
						vasDepreciationAmortization,
						subDepreciationAmortization);
				String queryReparationMantenance = getQuery(
						vasReparationMantenance, reparationMantenance);
				String queryOtherCost = getQuery(vasOtherCost, otherCost);
				
				System.err.println(querySales);
				
				double faValue = countFA(proj.getIndex(), monthYear, vasFA.getAccount().getIndex());
				double salesValue = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, querySales);
				double personnelCostValue = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryPersonnelCost);
				double otherPersonnelCostValue = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryOtherPersonnelCost);
				double MTCValue = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryMTC);
				double operasionalCostValue = m_logic.getDebitValue(
						m_sessionid, IDBConstants.MODUL_ACCOUNTING,
						queryoperasionalCost);
				double subContractorsSubversionValue = m_logic.getDebitValue(
						m_sessionid, IDBConstants.MODUL_ACCOUNTING,
						querySubContractorsSubversion);
				double depreciationAmortizationValue = m_logic.getDebitValue(
						m_sessionid, IDBConstants.MODUL_ACCOUNTING,
						queryDepreciationAmortization);
				double reparationMantenanceValue = m_logic.getDebitValue(
						m_sessionid, IDBConstants.MODUL_ACCOUNTING,
						queryReparationMantenance);
				double otherCostValue = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryOtherCost);
				faValue += personnelCostValue; // memang begitu
				double total = faValue
				+ otherPersonnelCostValue
				+ MTCValue
				+ operasionalCostValue
				+ subContractorsSubversionValue
				+ depreciationAmortizationValue
				+ reparationMantenanceValue + otherCostValue;
				
				if (salesValue < 0)
					salesValue = -salesValue;
				double grossPropit = salesValue - total;
				
				// String strSales = m_desimalFormat.format(salesValue);
				
				totFA += faValue;
				totSalesValue += salesValue;
				totOtherPersonnelCostValue += otherPersonnelCostValue;
				totMTCValue += MTCValue;
				totOperasionalCostValue += operasionalCostValue;
				totSubContractorsSubversionValue += subContractorsSubversionValue;
				totDepreciationAmortizationValue += depreciationAmortizationValue;
				totReparationMantenanceValue += reparationMantenanceValue;
				totOtherCostValue += otherCostValue;
				totTotal += total;
				totGrossPropit += grossPropit;
				if (faValue>0 || salesValue > 0 || MTCValue > 0 || operasionalCostValue > 0
						|| subContractorsSubversionValue > 0
						|| depreciationAmortizationValue > 0
						|| reparationMantenanceValue > 0 || otherCostValue > 0)
					model.addRow(new Object[] {
							String.valueOf(++i),
							cust,
							proj.getIPCNo(),
							proj.getWorkDescription(),
							m_desimalFormat.format(salesValue),
							m_desimalFormat.format(faValue),
							m_desimalFormat.format(otherPersonnelCostValue), //new
							m_desimalFormat.format(MTCValue),
							m_desimalFormat.format(operasionalCostValue),
							m_desimalFormat.format(subContractorsSubversionValue),
							m_desimalFormat.format(depreciationAmortizationValue),
							m_desimalFormat.format(reparationMantenanceValue),
							m_desimalFormat.format(otherCostValue),
							m_desimalFormat.format(total),
							m_desimalFormat.format(grossPropit),
							new Integer(1)});
			}
			
			// String strTotSales = m_desimalFormat.format(totSalesValue);
			// if (totSalesValue<0)
			// strTotSales = m_desimalFormat.format(-totSalesValue);
			model.addRow(new Object[] { "TOTAL",
					m_desimalFormat.format(totSalesValue),
					m_desimalFormat.format(totFA),
					m_desimalFormat.format(totOtherPersonnelCostValue), //new
					m_desimalFormat.format(totMTCValue),
					m_desimalFormat.format(totOperasionalCostValue),
					m_desimalFormat.format(totSubContractorsSubversionValue),
					m_desimalFormat.format(totDepreciationAmortizationValue),
					m_desimalFormat.format(totReparationMantenanceValue),
					m_desimalFormat.format(totOtherCostValue),
					m_desimalFormat.format(totTotal),
					m_desimalFormat.format(totGrossPropit), "", "", "",
					new Integer(2) });
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					compiledRptFilename, parameters, ds);
			if (isExcel)
				exportToExcel(jasperPrint);
			else
				m_jrv = new JRViewer(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private double countFA(long index, List monthYear, long faIdx) {
		Iterator iterator = monthYear.iterator();
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		double result = 0;
		try {
			// baca allowance multiplier
			AllowenceMultiplier[] allowanceMultiplier = logic
			.getAllAllowenceMultiplier(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			while (iterator.hasNext()) { // untuk setiap transaksi
				long[] mY = (long[]) iterator.next();
				String query = getQueryFA(index, mY); // query baca days dan areacode pada timesheet
				AccountingSQLSAP sql = new AccountingSQLSAP();
				List valList = sql.getDaysAndAreaCode(m_conn, query);
				Iterator iter = valList.iterator();
				while (iter.hasNext()) {
					TimeSheetSummary tss = (TimeSheetSummary) iter.next();
					float timeSheet = 0;
					for (int j = 0; j < allowanceMultiplier.length; j++) {
						if (tss.getAreaCode().equals(allowanceMultiplier[j].getAreaCode())) { // jika area codetimesheet sama dengem area multiplier
							timeSheet += (tss.getDays() * allowanceMultiplier[j].getMuliplier());
							break;
						}
					}
					FormulaEntity formulaEntity = sql.getFormula(m_conn, tss.getEmployeeID(), faIdx);
					Formula formula = new Formula();
					formula.parseFormula(formulaEntity, new PayrollTokenParser(m_conn));
					double res = 0;
					Hashtable vars = new Hashtable();
					Hashtable items = new Hashtable();
					vars.put("@TimeSheet@", String.valueOf(timeSheet));
					if(formulaEntity.getEveryWhichMonth()>0){
						if(formulaEntity.getEveryWhichMonth()== mY[0])
							res = formula.evaluate(vars,items);
					}else{
						res = formula.evaluate(vars,items);
					}
					if(result>0){
						NumberRounding numberRounding = formulaEntity.getNumberRounding();
						if(numberRounding.getRoundingMode()>-1){
							res = numberRounding.round(res);
						}
					}
					result += res;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	private String getQueryFA(long index, long[] monthYear) {
		long month = monthYear[0];
		long year = monthYear[1];
		
		String to = "'" + year + "-" + month + "-20'";
		if(month==1){
			month = 12;
			year--;
		}else
			month--;
		
		String from = "'" + year + "-" + month + "-21'";
		
		String query = "SELECT "
			+ "td." + pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_PERSONAL + ", "
			+ "td." + pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_AREA_CODE + ", "
			+ "td." + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_DAYS + " "
			+ "FROM " + pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_TIME_SHEET + " t, "
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_TIME_SHEET_DETAIL + " td "
			+ "WHERE t." + IDBConstants.ATTR_AUTOINDEX + " = td." + pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_TIME_SHEET
			+ " AND t."
			+ IDBConstants.ATTR_PROJECT + " = " + index
			+ " AND t."
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_ENTRY_DATE + " BETWEEN "
			+ from + " AND " + to;
		return query;
	}
	
	private String getQuery(VariableAccountSetting vasSales,
			String indexAccTrans) {
		/*String strUnit = "";
		 if (m_unitPicker.getUnit() != null)
		 strUnit = "AND acct." + IDBConstants.ATTR_UNIT + "="
		 + m_unitPicker.getUnit().getIndex();*/
		String treepath = "";
		if (vasSales!=null)
			if (vasSales.getAccount()!=null)
				treepath = "ac." + IDBConstants.ATTR_PATH + " LIKE '" + vasSales.getAccount().getTreePath() + "%' AND ";		
		String query = "SELECT SUM(" + IDBConstants.ATTR_EXCHANGE_RATE + "*"
		+ IDBConstants.ATTR_VALUE + "*(1-2*"
		+ IDBConstants.ATTR_BALANCE_CODE + ")) debitvalue FROM "
		+ IDBConstants.TABLE_TRANSACTION_VALUE + " tv, "
		+ IDBConstants.ATTR_ACCOUNT + " ac, "
		+ IDBConstants.TABLE_TRANSACTION + " acct " + "WHERE " + "ac."
		+ IDBConstants.ATTR_AUTOINDEX + " = tv."
		+ IDBConstants.ATTR_ACCOUNT + " AND " + "acct."
		+ IDBConstants.ATTR_AUTOINDEX + "="
		+ IDBConstants.ATTR_TRANSACTION + " AND " + treepath + "acct."
		+ IDBConstants.ATTR_TRANSACTION_DATE + "<='"
		+ m_dateFormat2.format(m_datePicker.getDate()) + "' "
		+ " AND acct." + IDBConstants.ATTR_AUTOINDEX + " in ("
		+ indexAccTrans + ")";
		return query;
	}
	
	private String getQueryFAForPeriod(VariableAccountSetting vasFA) {
		String query = "SELECT e.* FROM "
			+ pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE_PAYROLL
			+ " e, " + IDBConstants.TABLE_TRANSACTION + " t, "
			+ IDBConstants.TABLE_TRANSACTION_VALUE + " tv " + "WHERE "
			+ "t." + IDBConstants.ATTR_AUTOINDEX + " = tv."
			+ IDBConstants.ATTR_TRANSACTION + " AND " + "t."
			+ IDBConstants.ATTR_AUTOINDEX + " = e."
			+ IDBConstants.ATTR_TRANSACTION + " AND " + "tv."
			+ IDBConstants.ATTR_ACCOUNT + " = "
			+ vasFA.getAccount().getIndex() + " AND " + "t."
			+ IDBConstants.ATTR_TRANSACTION_DATE + "<='"
			+ m_dateFormat2.format(m_datePicker.getDate()) + "'";
		return query;
	}
	
	private String getQuery1(long projectIndex) {
		String query = "select a." + IDBConstants.ATTR_AUTOINDEX + " from "
		+ IDBConstants.TABLE_TRANSACTION + " a," + "(SELECT "
		+ IDBConstants.ATTR_TRANSACTION + ","
		+ IDBConstants.ATTR_PROJECT + " FROM " + "(SELECT "
		+ IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + " FROM "
		+ IDBConstants.TABLE_SALES_INVOICE + " UNION " + "SELECT "
		+ IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + "  FROM "
		+ IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + " UNION "
		+ "SELECT " + IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + "  FROM "
		+ IDBConstants.ATTR_MEMORIAL_JOURNAL_NONSTANDARD
		+ ") where project=" + projectIndex + ") b " + "WHERE a."
		+ IDBConstants.ATTR_AUTOINDEX + "=b."
		+ IDBConstants.ATTR_TRANSACTION + "";
		return query;
	}
	
	/**
	 * pembebanan di: Pmt Project Cost; Purchase Receipt; Expense Sheet;
	 * Memorial Journal Standard; Memorial Journal Non Standard
	 * @param projectIndex
	 * @return
	 */
	private String getQuery2(long projectIndex) {
		String query = "SELECT a." + IDBConstants.ATTR_AUTOINDEX + " FROM "
		+ IDBConstants.TABLE_TRANSACTION + " a," + "(SELECT "
		+ IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + " FROM (SELECT "
		+ IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + " FROM pmtprojectcost "
		+ "UNION " + "SELECT " + IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + "  FROM "
		+ IDBConstants.TABLE_PURCHASE_RECEIPT + " UNION " + "SELECT "
		+ IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + "  FROM "
		+ IDBConstants.TABLE_EXPENSE_SHEET + " UNION " + "SELECT "
		+ IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + "  FROM "
		+ IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + " UNION "
		+ "SELECT " + IDBConstants.ATTR_TRANSACTION + ", "
		+ IDBConstants.ATTR_PROJECT + "  FROM "
		+ IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD + ") WHERE "
		+ IDBConstants.ATTR_PROJECT + "=" + projectIndex + ") b "
		+ "WHERE a." + IDBConstants.ATTR_AUTOINDEX + "=b."
		+ IDBConstants.ATTR_TRANSACTION;
		return query;
	}
	
	/**
	 * pembebanan di: Pmt Project Cost, Expense Sheet, Memorial Journal Standard,
	 * Memorial Journal Non Standard
	 * @param projectIndex
	 * @return
	 */
	// JANGAN DIHAPUS
	/*private String getQuery3(long projectIndex) {
	 String query = "SELECT a." + IDBConstants.ATTR_AUTOINDEX + " FROM "
	 + IDBConstants.TABLE_TRANSACTION + " a," + "(SELECT "
	 + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + " FROM (SELECT "
	 + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + " FROM pmtprojectcost "
	 + " UNION " + "SELECT " + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + "  FROM "
	 + IDBConstants.TABLE_EXPENSE_SHEET + " UNION " + "SELECT "
	 + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + "  FROM "
	 + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + " UNION "
	 + "SELECT " + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + "  FROM "
	 + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD + ") WHERE "
	 + IDBConstants.ATTR_PROJECT + "=" + projectIndex + ") b "
	 + "WHERE a." + IDBConstants.ATTR_AUTOINDEX + "=b."
	 + IDBConstants.ATTR_TRANSACTION;
	 return query;
	 }*/
	
	/**
	 * pembebanan di: Memorial Journal Standard, Memorial Journal Non Standard
	 * @param projectIndex
	 * @return
	 */
	// JANGAN DIHAPUS
	/*private String getQuery4(long projectIndex) {
	 String query = "SELECT a." + IDBConstants.ATTR_AUTOINDEX + " FROM "
	 + IDBConstants.TABLE_TRANSACTION + " a," + "(SELECT "
	 + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + " FROM (SELECT "
	 + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + "  FROM "
	 + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + " UNION "
	 + "SELECT " + IDBConstants.ATTR_TRANSACTION + ", "
	 + IDBConstants.ATTR_PROJECT + "  FROM "
	 + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD
	 + ") WHERE project=" + projectIndex + ") b " + "WHERE a."
	 + IDBConstants.ATTR_AUTOINDEX + "=b."
	 + IDBConstants.ATTR_TRANSACTION;
	 return query;
	 }*/
	
	public void exportToExcel(JasperPrint jasperPrint) throws Exception {
		javax.swing.JFileChooser jfc = new javax.swing.JFileChooser("reportsample/");
		
		jfc.setDialogTitle("Simpan Laporan Dalam File Excel");
		jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File file) {
				String filename = file.getName();
				return (filename.toLowerCase().endsWith(".xls") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
			}
			public String getDescription() {
				return "Laporan *.xls";
			}
		});
		
		jfc.setMultiSelectionEnabled(false);    
		jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
		if  (jfc.showSaveDialog(null) == javax.swing.JOptionPane.OK_OPTION) {
			//JExcelApiExporter exporterXLS = new JExcelApiExporter();
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(), "xls"));
			exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
			exporter.exportReport();           
		}
	}
	
	public static String changeFileExtension(String filename, String newExtension ) {
		if (!newExtension.startsWith("."))
			newExtension = "." + newExtension;
		if (filename == null || filename.length()==0 ) {
			return newExtension;
		}
		
		int index = filename.lastIndexOf(".");
		if (index >= 0) {
			filename = filename.substring(0,index);
		}
		return filename += newExtension;
	}
	
	private void header(DefaultTableModel model) {
		model.addColumn("field1");
		model.addColumn("field2");
		model.addColumn("field3");
		model.addColumn("field4");
		model.addColumn("field5");
		model.addColumn("field6");
		model.addColumn("field7");
		model.addColumn("field8");
		model.addColumn("field9");
		model.addColumn("field10");
		model.addColumn("field11");
		model.addColumn("field12");
		model.addColumn("field13");
		model.addColumn("field14");
		model.addColumn("field15");
		model.addColumn("status");
	}
	
	public JRViewer getPrintView() {
		return m_jrv;
	}
}
