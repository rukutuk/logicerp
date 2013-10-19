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

import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;

public class ProjectProfitabilityDetail {
	
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	String jasperReport;
	AccountingBusinessLogic m_logic;
	public ProjectProfitabilityDetail(Connection conn,long sessionid) {
		try {
			m_conn = conn;
			m_sessionid = sessionid;
			m_logic = new AccountingBusinessLogic(m_conn);
			//String filename = "Project_profitabilitydetail.jrxml";
			//jasperReport = JasperCompileManager.compileReport("report/" + filename);
			String filename2 = "Project_profitabilitydetail";
			//JasperReport jasperReport = JasperCompileManager.compileReport(filename);
			jasperReport = ReportUtils.compileReport(filename2);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setEmpty() {
		DefaultTableModel model = new DefaultTableModel();
		Map parameters = new HashMap();
		parameters.put("param_logo", "../images/TS.gif");
		parameters.put("param_project_code", "");
		parameters.put("param_client", "");
		parameters.put("param_work_desc", "");
		parameters.put("param_act_code", "");
		parameters.put("param_ipcno", "");
		parameters.put("param_ipcdate", "");
		parameters.put("param_period", "");
		header(model);
		model.addRow(new Object[] { "Sales", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		model.addRow(new Object[] { "Field Allowances", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		model.addRow(new Object[] { "Material, Tools & Consumable", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		model.addRow(new Object[] { "Operational Cost", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		model.addRow(new Object[] { "Sub Contract & Supervision", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		model.addRow(new Object[] { "Depreciation & Amortization", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		model.addRow(new Object[] { "Reparation & Maintenance", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		model.addRow(new Object[] { "Other Cost", "", "", "",new Integer(0) });
		model.addRow(new Object[] { "", "", "", "",new Integer(1) });
		model.addRow(new Object[] { "subtotal", "", "", "",new Integer(2) });
		close(parameters,model,false);
	}
	
	SimpleDateFormat m_dateformat, m_dateformat2;
	DatePicker m_fromdate;
	DecimalFormat m_desimalformat;
	
	ProjectData project;
	public void setNonEmpty(Object obj,DatePicker fromdate, boolean isExcel) {
		m_fromdate = fromdate;
		m_desimalformat = new DecimalFormat("#,##0.00");
		m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateformat2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			parameters.put("param_logo", "../images/TS.gif");
			if (obj instanceof ProjectData) {
				project = (ProjectData) obj;
				VariableAccountSetting vasFA = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_FIELD_ALLOWANCE);
				VariableAccountSetting vasPersonnelCost = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_PERSONNEL_COST);
				VariableAccountSetting vasOtherPersonnelCost = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_OTHER_PERSONNEL_COST);
				VariableAccountSetting vasSales = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_SALES);
				VariableAccountSetting vasOperasionalCost = VariableAccountSetting
				.createVariableAccountSetting(m_conn, m_sessionid,
						IConstants.ATTR_VARS_PROJECT_OPERATIONAL_COST);
				VariableAccountSetting vasMTC = VariableAccountSetting
				.createVariableAccountSetting(
						m_conn,
						m_sessionid,
						IConstants.ATTR_VARS_MATERIALS_TOOLS_CONSUMABLES);
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
				String fa = getQueryFAForPeriod(vasFA);
				String sales = getQuery1(project.getIndex());
				String personnelCost = getQuery2(project.getIndex());
				String otherPersonnelCost = getQuery2(project.getIndex());
				String mtc = getQuery2(project.getIndex());
				String operasionalCost = getQuery2(project.getIndex());
				String subContractorsSubversion = getQuery2(project.getIndex()); //3
				String subDepreciationAmortization = getQuery2(project.getIndex()); //4
				String reparationMantenance = getQuery2(project.getIndex()); //3
				String otherCost = getQuery2(project.getIndex()); //3
				AccountingSQLSAP sql = new AccountingSQLSAP();
				List monthYear = sql.getMonthYearPayrollByQuery(m_conn, fa);
				parameters.put("param_project_code", project.toString());
				parameters.put("param_client", project.getCustomer().toString());
				parameters.put("param_work_desc", project.getWorkDescription());
				parameters.put("param_act_code", project.getActivity().toString());
				parameters.put("param_ipcno", project.getIPCNo());
				if (project.getIPCDate() != null)
					parameters.put("param_ipcdate", m_dateformat.format(project.getIPCDate()));
				else
					parameters.put("param_ipcdate", "");
				parameters.put("param_period", "");
				model.addColumn("field0");
				model.addColumn("field1");
				model.addColumn("field2");
				model.addColumn("field3");
				model.addColumn("status");
				model.addRow(new Object[] { "Sales", "", "", "",new Integer(0) });
				double totsales = getDataDetail(model,sales,vasSales.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totsales),new Integer(2) });
				model.addRow(new Object[] { "Field Allowances", "", "", "",new Integer(0) });
				double totfa = countFA(model,project.getIndex(), monthYear, vasFA.getAccount());
				if (vasPersonnelCost!=null)
					totfa += getDataDetail(model, personnelCost, vasPersonnelCost.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totfa),new Integer(2) });
				model.addRow(new Object[] { "Other Personnel Cost", "", "", "",new Integer(0) });
				double totOtherPersonnelCost =0;
				if (vasOtherPersonnelCost!=null)
					totOtherPersonnelCost = getDataDetail(model,otherPersonnelCost,vasOtherPersonnelCost.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totOtherPersonnelCost),new Integer(2) });
				model.addRow(new Object[] { "Material, Tools & Consumable", "", "", "",new Integer(0) });
				double totmtc = getDataDetail(model,mtc,vasMTC.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totmtc),new Integer(2) });
				model.addRow(new Object[] { "Operational Cost", "", "", "",new Integer(0) });
				double totOperationalCost = getDataDetail(model,operasionalCost,vasOperasionalCost.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totOperationalCost),new Integer(2) });
				model.addRow(new Object[] { "Sub Contract & Supervision", "", "", "",new Integer(0) });
				double totsubContractorsSubversion = getDataDetail(model,subContractorsSubversion,vasSubContractorsSubversion.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totsubContractorsSubversion),new Integer(2) });
				model.addRow(new Object[] { "Depreciation & Amortization", "", "", "",new Integer(0) });
				double totsubDepreciationAmortization = getDataDetail(model,subDepreciationAmortization,vasDepreciationAmortization.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totsubDepreciationAmortization),new Integer(2) });
				model.addRow(new Object[] { "Reparation & Maintenance", "", "", "",new Integer(0) });
				double totreparationMantenance = getDataDetail(model,reparationMantenance,vasReparationMantenance.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(totreparationMantenance),new Integer(2) });
				model.addRow(new Object[] { "Other Cost", "", "", "",new Integer(0) });
				double tototherCost = getDataDetail(model,otherCost,vasOtherCost.getAccount());
				model.addRow(new Object[] { "subtotal", "", "", m_desimalformat.format(tototherCost),new Integer(2) });
				close( parameters, model,isExcel);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*private double getSales(DefaultTableModel model,Account acc) {
	 String sales = getQuery1(project.getIndex());
	 GenericMapper mapTrans = MasterMap.obtainMapperFor(Transaction.class);
	 mapTrans.setActiveConn(m_conn);
	 String strWhere = IDBConstants.ATTR_TRANSACTION_DATE + "<='" + m_dateformat2.format(m_fromdate.getDate()) + "' AND "
	 + IDBConstants.ATTR_AUTOINDEX + " IN (" + sales + ") " ;
	 List list = mapTrans.doSelectWhere(strWhere);
	 if (list.size()==0){
	 model.addRow(new Object[] {"","", "","",new Integer(1) });
	 }
	 Iterator iterator = list.iterator();
	 double totsales =0;
	 
	 while (iterator.hasNext()) {
	 Transaction trans= (Transaction) iterator.next();
	 GenericMapper mapTransDetail = MasterMap.obtainMapperFor(TransactionDetail.class);
	 mapTransDetail.setActiveConn(m_conn);
	 strWhere = IDBConstants.ATTR_TRANSACTION+ "=" + trans.getIndex() + " AND " +
	 IDBConstants.ATTR_ACCOUNT + " in (select autoindex from account where treepath like  '"+acc.getTreePath() +"%')";
	 list = mapTransDetail.doSelectWhere(strWhere);
	 Iterator iter = list.iterator();
	 double salesvalue = 0;
	 while (iter.hasNext()) {
	 TransactionDetail transDet= (TransactionDetail) iter.next();
	 salesvalue+=(transDet.getValue()*transDet.getExchangeRate());
	 }
	 model.addRow(new Object[] {m_dateformat.format(trans.getTransDate()), trans.getReference(), trans.getDescription(),
	 m_desimalformat.format(salesvalue),new Integer(1) });
	 totsales +=salesvalue;
	 }
	 
	 return totsales;
	 }*/
	
	
	private double getDataDetail(DefaultTableModel model,String query,Account acc) {
		
		GenericMapper mapTrans = MasterMap.obtainMapperFor(Transaction.class);
		mapTrans.setActiveConn(m_conn);
		String strWhere = IDBConstants.ATTR_TRANSACTION_DATE + "<='" + m_dateformat2.format(m_fromdate.getDate()) + "' AND "
		+ IDBConstants.ATTR_AUTOINDEX + " IN (" + query + ") " ;
		List list = mapTrans.doSelectWhere(strWhere);
		
		Iterator iterator = list.iterator();
		double total =0;
		boolean tampil = false;
		while (iterator.hasNext()) {
			Transaction trans= (Transaction) iterator.next();
			GenericMapper mapTransDetail = MasterMap.obtainMapperFor(TransactionDetail.class);
			mapTransDetail.setActiveConn(m_conn);
			strWhere = IDBConstants.ATTR_TRANSACTION+ "=" + trans.getIndex() + " AND " +
			IDBConstants.ATTR_ACCOUNT + " in (select autoindex from account where treepath like  '"+acc.getTreePath() +"%')";
			list = mapTransDetail.doSelectWhere(strWhere);
			if (list.size()>0){
				Iterator iter = list.iterator();
				double costvalue = 0;
				while (iter.hasNext()) {
					TransactionDetail transDet= (TransactionDetail) iter.next();
					costvalue+=(transDet.getValue()*transDet.getExchangeRate());
				}
				tampil = true;
				model.addRow(new Object[] { m_dateformat.format(trans.getTransDate()), trans.getReference(), trans.getDescription(),
						m_desimalformat.format(costvalue),new Integer(1)});
				total +=costvalue;
			}
		}
		if (!tampil){
			model.addRow(new Object[] {"","", "","",new Integer(1) });
		}
		return total;
	}
	
	
	
	private Transaction getFA(DefaultTableModel model, long indextrans) {
		GenericMapper mapTrans = MasterMap.obtainMapperFor(Transaction.class);
		mapTrans.setActiveConn(m_conn);
		String strWhere = IDBConstants.ATTR_TRANSACTION_DATE + "<='" + m_dateformat2.format(m_fromdate.getDate()) + "' AND "
		+ IDBConstants.ATTR_AUTOINDEX + "=" + indextrans;
		List list = mapTrans.doSelectWhere(strWhere);
		Iterator iterator = list.iterator();
		Transaction trans = null;
		while (iterator.hasNext()) {
			trans= (Transaction) iterator.next();
		}
		return trans;
	}
	
	private double countFA(DefaultTableModel model,long index, List monthYear, Account acc) {
		double total = 0;
		boolean tampil = false;
		if (monthYear.size()>0){
			Iterator iterator = monthYear.iterator();
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			long faIdx = acc.getIndex();
			try {
				AllowenceMultiplier[] allowanceMultiplier = logic.getAllAllowenceMultiplier(m_sessionid,IDBConstants.MODUL_MASTER_DATA);
				while (iterator.hasNext()) {
					long[] mY = (long[]) iterator.next();
					String query = getQueryFA(index, mY);
					AccountingSQLSAP sql = new AccountingSQLSAP();
					List valList = sql.getDaysAndAreaCode(m_conn, query);
					if (valList.size()>0){
						double result = 0;
						Iterator iter = valList.iterator();
						while (iter.hasNext()) {
							TimeSheetSummary tss = (TimeSheetSummary) iter.next();
							float timeSheet = 0;
							for (int j = 0; j < allowanceMultiplier.length; j++) {
								if (tss.getAreaCode().equals(allowanceMultiplier[j].getAreaCode())) {
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
						total +=result;
						Transaction trans = getFA(model,mY[2]);
						tampil =true;
						model.addRow(new Object[] { m_dateformat.format(trans.getTransDate()), trans.getReference(), trans.getDescription(),
								m_desimalformat.format(result),new Integer(1)});
					}				}
				if (!tampil){
					model.addRow(new Object[] {"","", "","",new Integer(1) });
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else{
			model.addRow(new Object[] {"", "", "","",new Integer(1)});
		}
		return total;
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
			+ m_dateformat2.format(m_fromdate.getDate()) + "'";
		return query;
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
	
	private void header(DefaultTableModel model) {
		model.addColumn("field0");
		model.addColumn("field1");
		model.addColumn("field2");
		model.addColumn("field3");
		model.addColumn("status");
	}
	
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
	
	
	private void close(Map parameters, DefaultTableModel model, boolean isExcel) {
		JRTableModelDataSource ds = new JRTableModelDataSource(model);
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
		} catch (JRException e) {
			e.printStackTrace();
		}
		if (isExcel)
			try {
				exportToExcel(jasperPrint);
			} catch (Exception e) {
				e.printStackTrace();
			}
			else
				m_jrv = new JRViewer(jasperPrint);
	}
	
	public JRViewer getPrintView() {
		return m_jrv;
	}
	
}