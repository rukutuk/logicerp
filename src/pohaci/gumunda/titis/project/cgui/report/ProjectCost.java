package pohaci.gumunda.titis.project.cgui.report;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.cgui.IConstants;
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
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.TimeSheetSummary;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollTokenParser;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;

/**
 * Perhitungan Project Cost
 * Catatan. khusus untuk perhitungan field allowance perhitungannya
 * ada kemungkinan tidak 100% tepat.
 * Terutama karena efek penjumlahan ulang.
 * Efeknya yaitu sebagai berikut:
 * ** Perhitungan field allowance dihitung khusus dari time sheet.
 * ** Jika ternyata akun field allowance terhitung suatu nilai tertentu
 *    berdasarkan transaction posted, maka akan ditambahkan dengan
 *    perhitungan field allowance di atas. Hal inilah yang menjadikan
 *    tidak benar.
 *    Namun langkah ini tetap ditempuh karena (1) males booo dan (2)
 *    jika tidak ada perubahan proses bisnis, transaksi yang melibatkan
 *    gaji dan tunjangan tidak pernah memiliki relasi dengan project,
 *    sementara project cost dihitung berdasarkan project tertentu.
 *    Jadi, untuk sementara cara ini masih AMAN.
 * @author dark-knight
 *
 */

public class ProjectCost {

	JRViewer m_jrv;

	Connection m_conn;

	long m_sessionid;

	Account vasFA = null;

	double faValue = 0;

	public ProjectCost() {
		try {
			//String filename = "../report/Project_cost.jrxml";
			String filename2 = "Project_cost";
			//JasperReport jasperReport = JasperCompileManager.compileReport(filename);
			String jasperReport = ReportUtils.compileReport(filename2);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_project_code", "");
			parameters.put("param_client", "");
			parameters.put("param_work_desc", "");
			parameters.put("param_act_code", "");
			parameters.put("param_ipcno", "");
			parameters.put("param_ipcdate", "");
			parameters.put("param_period", "");

			model.addColumn("field0");
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("status");

			model.addRow(new Object[] { "", "", "", new Integer(0) });
			model.addRow(new Object[] { "Total", "", "", new Integer(1) });

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			m_jrv = new JRViewer(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setVAS() {
		VariableAccountSetting vas = VariableAccountSetting
		.createVariableAccountSetting(m_conn, m_sessionid,
				IConstants.ATTR_VARS_FIELD_ALLOWANCE);
		vasFA = vas.getAccount();
	}

	SimpleDateFormat m_dateformat, m_dateformat2;

	DatePicker m_fromdate, m_todate;

	DecimalFormat m_desimalformat;

	AccountingBusinessLogic m_logic;

	// Account m_account;
	public ProjectCost(Connection conn, long sessionid, Object obj,
			Account account, DatePicker fromdate, DatePicker todate, boolean isExcel) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_fromdate = fromdate;
		m_todate = todate;
		// m_account = account;
		m_desimalformat = new DecimalFormat("#,##0.00");
		m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateformat2 = new SimpleDateFormat("yyyy-MM-dd");
		m_logic = new AccountingBusinessLogic(m_conn);
		setVAS();
		try {
			//String filename = "Project_cost.jrxml";
			//JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			String filename2 = "Project_cost";
			String jasperReport = ReportUtils.compileReport(filename2);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			parameters.put("param_logo", "../images/TS.gif");
			if (obj instanceof ProjectData) {
				ProjectData project = (ProjectData) obj;
				parameters.put("param_project_code", project.toString());
				parameters
				.put("param_client", project.getCustomer().toString());
				parameters.put("param_work_desc", project.getWorkDescription());
				parameters.put("param_act_code", project.getActivity()
						.toString());
				parameters.put("param_ipcno", project.getIPCNo());
				if (project.getIPCDate() != null)
					parameters.put("param_ipcdate", m_dateformat.format(project
							.getIPCDate()));
				else
					parameters.put("param_ipcdate", "");

				parameters.put("param_period", m_dateformat.format(fromdate
						.getDate())
						+ " to " + m_dateformat.format(todate.getDate()));

				model.addColumn("field0");
				model.addColumn("field1");
				model.addColumn("field2");
				model.addColumn("status");

				if (vasFA != null) {
					faValue = countFA(project.getIndex(), vasFA);
				}

				String query = getQuery(project, account);

				double debitValue = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, query);

				String path = account.getTreePath();
				if (vasFA.getTreePath().startsWith(path)){
					debitValue += faValue;
				}

				model.addRow(new Object[] { account.getCode(),
						account.getName(), m_desimalformat.format(debitValue),
						new Integer(0) });

				if (account.isGroup())
					setSubAccount(model, account, project);
				model.addRow(new Object[] { "Total", "",
						m_desimalformat.format(debitValue), new Integer(1) });
			} else {
				model.addRow(new Object[] { "", "", "", new Integer(0) });
				model.addRow(new Object[] { "Total", "", "", new Integer(1) });
			}

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			if (isExcel)
				exportToExcel(jasperPrint);
			else
				m_jrv = new JRViewer(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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


	int m_level = 0;

	void setSubAccount(DefaultTableModel model, Account account,
			ProjectData project) {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		try {
			Account[] subaccount = logic.getSubAccount(m_sessionid,
					IDBConstants.MODUL_ACCOUNTING, account.getIndex());


			Hashtable hashtable = new Hashtable();

			for (int i = 0; i < subaccount.length; i++) {

				String query = getQuery(project, subaccount[i]);

				String m_spance = "      ";
				for (int j = 0; j < m_level; j++) {
					m_spance += "       ";
				}

				double debitValue = 0;

				if (subaccount[i].getIndex() == vasFA.getIndex()) {
					debitValue = faValue;
				} else {
					debitValue = m_logic.getDebitValue(m_sessionid,
							IDBConstants.MODUL_ACCOUNTING, query);

					String path = subaccount[i].getTreePath();
					if (vasFA.getTreePath().startsWith(path)){
						debitValue += faValue;
					}
				}

				hashtable.put(new Integer(i), new Double(debitValue));

				model.addRow(new Object[] { subaccount[i].getCode(),
						m_spance + subaccount[i].getName(),
						m_desimalformat.format(debitValue), new Integer(0) });

				if (subaccount[i].isGroup()) {
					m_level = m_level + 1;
					setSubAccount(model, subaccount[i], project);
					m_level = m_level - 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String getQuery(ProjectData project, Account account) {
		String query = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue FROM transactionvalue tv, account ac, acctransaction acct "
			+ "WHERE ac.autoindex = tv.account AND acct.autoindex=tv.trans AND ac.treepath LIKE '"
			+ account.getTreePath()
			+ "%' AND (acct.TRANSACTIONDATE between '"
			+ m_dateformat2.format(m_fromdate.getDate())
			+ "' AND '"
			+ m_dateformat2.format(m_todate.getDate())
			+ "')  AND "
			+ "acct.autoindex in (select trans from (select autoindex,trans,project from pmtprojectcost where status =3 "
			+ "union select autoindex,trans,project from  purchasereceipt where status=3 "
			+ "union select autoindex,trans,project from expensesheet where status =3 "
			+ "union select autoindex,trans,project from memjournalnonstrd where status=3 ) projjoin where project="
			+ project.getIndex() + ")";
		return query;
	}

	public JRViewer getPrintView() {
		return m_jrv;
	}

	private double countFA(long index, Account fa) {
		String queryFA = getQueryFAForPeriod(fa);

		AccountingSQLSAP sql = new AccountingSQLSAP();
		List monthYear;

		double result = 0;

		try {
			monthYear = sql.getMonthYearPayrollByQuery(m_conn, queryFA);

			Iterator iterator = monthYear.iterator();

			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

			AllowenceMultiplier[] allowanceMultiplier = logic
			.getAllAllowenceMultiplier(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);

			while (iterator.hasNext()) {
				long[] mY = (long[]) iterator.next();

				String query = getQueryFA(index, mY);

				List valList = sql.getDaysAndAreaCode(m_conn, query);

				Iterator iter = valList.iterator();
				while (iter.hasNext()) {
					TimeSheetSummary tss = (TimeSheetSummary) iter.next();

					float timeSheet = 0;
					for (int j = 0; j < allowanceMultiplier.length; j++) {
						if (tss.getAreaCode().equals(
								allowanceMultiplier[j].getAreaCode())) {
							timeSheet += (tss.getDays() * allowanceMultiplier[j]
							                                                  .getMuliplier());
							break;
						}
					}

					FormulaEntity formulaEntity = sql.getFormula(m_conn, tss
							.getEmployeeID(), fa.getIndex());
					Formula formula = new Formula();
					formula.parseFormula(formulaEntity, new PayrollTokenParser(
							m_conn));

					double res = 0;

					Hashtable vars = new Hashtable();
					Hashtable items = new Hashtable();
					vars.put("@TimeSheet@", String.valueOf(timeSheet));

					if (formulaEntity.getEveryWhichMonth() > 0) {
						if (formulaEntity.getEveryWhichMonth() == mY[0])
							res = formula.evaluate(vars, items);
					} else {
						res = formula.evaluate(vars, items);
					}
					if (result > 0) {
						NumberRounding numberRounding = formulaEntity
						.getNumberRounding();
						if (numberRounding.getRoundingMode() > -1) {
							res = numberRounding.round(res);
						}
					}

					result += res;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private String getQueryFA(long index, long[] monthYear) {
		long month = monthYear[0];
		long year = monthYear[1];

		String to = "'" + year + "-" + month + "-20'";
		if (month == 1) {
			month = 12;
			year--;
		} else
			month--;

		String from = "'" + year + "-" + month + "-21'";

		String query = "SELECT "
			+ "td."
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_PERSONAL
			+ ", "
			+ "td."
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_AREA_CODE
			+ ", "
			+ "td."
			+ pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_DAYS
			+ " "
			+ "FROM "
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_TIME_SHEET
			+ " t, "
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_TIME_SHEET_DETAIL
			+ " td "
			+ "WHERE t."
			+ IDBConstants.ATTR_AUTOINDEX
			+ " = td."
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_TIME_SHEET
			+ " AND t."
			+ IDBConstants.ATTR_PROJECT
			+ " = "
			+ index
			+ " AND t."
			+ pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_ENTRY_DATE
			+ " BETWEEN " + from + " AND " + to;
		return query;
	}

	private String getQueryFAForPeriod(Account vasFA) {
		String query = "SELECT e.* FROM "
			+ pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE_PAYROLL
			+ " e, " + IDBConstants.TABLE_TRANSACTION + " t, "
			+ IDBConstants.TABLE_TRANSACTION_VALUE + " tv " + "WHERE "
			+ "t." + IDBConstants.ATTR_AUTOINDEX + " = tv."
			+ IDBConstants.ATTR_TRANSACTION + " AND " + "t."
			+ IDBConstants.ATTR_AUTOINDEX + " = e."
			+ IDBConstants.ATTR_TRANSACTION + " AND " + "tv."
			+ IDBConstants.ATTR_ACCOUNT + " = " + vasFA.getIndex()
			+ " AND " + "t." + IDBConstants.ATTR_TRANSACTION_DATE + " BETWEEN '"
			+ m_dateformat2.format(m_fromdate.getDate())
			+ "' AND '"
			+ m_dateformat2.format(m_todate.getDate())
			+ "'";
		return query;
	}

}