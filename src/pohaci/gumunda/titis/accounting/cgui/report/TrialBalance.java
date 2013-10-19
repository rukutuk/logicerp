package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.TrialBalanceAccountTypeSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodSubsidiaryLedger;
import pohaci.gumunda.titis.application.ReportUtils;

public class TrialBalance {
	JRViewer m_jrv;

	Connection m_conn;

	long m_sessionid;

	PeriodSubsidiaryLedger m_periodStartEnd;

	SimpleDateFormat m_dateFormat, m_dateFormat2;

	DecimalFormat m_desimalFormat;

	AccountingBusinessLogic m_logic;

	JournalStandard bbJS;

	private boolean m_excel = false;
	private JasperPrint m_jasperPrint = null;
	
	public TrialBalance(Connection conn, long sessionid,
			PeriodSubsidiaryLedger periodStartEnd, boolean excel) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_periodStartEnd = periodStartEnd;
		m_excel = excel;
		m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		m_desimalFormat = new DecimalFormat("#,##0.00");
		if(excel)
			m_desimalFormat = new DecimalFormat("###.00");
		m_logic = new AccountingBusinessLogic(m_conn);

		setBeginningBalanceJournalStandard();
		setNonEmpty();
	}

	private void setBeginningBalanceJournalStandard() {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(this.m_conn);
		try {
			BeginningBalance balance = logic.getBeginningBalance(
					this.m_sessionid, IDBConstants.MODUL_ACCOUNTING, (short) 0);
			if (balance == null)
				balance = new BeginningBalance();

			bbJS = balance.getJournalStandard();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TrialBalance(long sessionid, Connection conn) {
		m_sessionid = sessionid;
		m_conn = conn;
		m_logic = new AccountingBusinessLogic(m_conn);
		setEmpty();
	}

	private void setEmpty() {
		try {
			String filename = "trial_balance";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_period", "");

			header(model);

			getEmptyRootAccount(model);
			model.addRow(new Object[] { "TOTAL", "", "", "", "", "", "", "",
					"", "", "", "", new Integer(2) });

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					compiledRptFilename, parameters, ds);

			m_jrv = new JRViewer(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getEmptyRootAccount(DefaultTableModel model) throws Exception {
		Account[] account = m_logic.getSuperAccount(m_sessionid,
				IDBConstants.MODUL_ACCOUNTING);
		for (int i = 0; i < account.length; i++) {
			model.addRow(new Object[] { account[i].getCode(),
					account[i].getName(), "", "", "", "", "", "", "", "", "",
					"", new Integer(1) });
			getEmptySubAccount(account[i], model);
		}
	}

	private void getEmptySubAccount(Account account, DefaultTableModel model)
			throws Exception {
		Account[] subaccount = m_logic.getSubAccount(m_sessionid,
				IDBConstants.MODUL_ACCOUNTING, account.getIndex());
		for (int i = 0; i < subaccount.length; i++) {
			if (subaccount[i].isGroup()) {
				model.addRow(new Object[] { subaccount[i].getCode(),
						subaccount[i].getName(), "", "", "", "", "", "", "",
						"", "", "", new Integer(1) });
				getEmptySubAccount(subaccount[i], model);
			} else {
				model.addRow(new Object[] { subaccount[i].getCode(),
						subaccount[i].getName(), "", "", "", "", "", "", "",
						"", "", "", new Integer(1) });
			}
		}
	}

	private void setNonEmpty() {
		try {
			String filename = "trial_balance";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			parameters.put("param_logo", "../images/TS.gif");
			parameters
					.put("param_period", m_dateFormat
							.format(m_periodStartEnd.m_startDate.getDate())
							+ " - "
							+ m_dateFormat.format(m_periodStartEnd.m_endDate
									.getDate()));

			header(model);
			getRootAccount(model);

			model.addRow(new Object[] { "TOTAL", "", m_strtotal[0],
					m_strtotal[1], m_strtotal[2], m_strtotal[3], m_strtotal[4],
					m_strtotal[5], m_strtotal[6], m_strtotal[7], m_strtotal[8],
					m_strtotal[9], new Integer(2) });

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			m_jasperPrint = JasperFillManager.fillReport(
					compiledRptFilename, parameters, ds);

			if(!m_excel)
				m_jrv = new JRViewer(m_jasperPrint);
			else
				exportToExcel(m_jasperPrint);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getRootAccount(DefaultTableModel model) throws Exception {
		Account[] account = m_logic.getSuperAccount(m_sessionid,
				IDBConstants.MODUL_ACCOUNTING);
		for (int i = 0; i < account.length; i++) {
			model.addRow(new Object[] { account[i].getCode(),
					account[i].getName(), "", "", "", "", "", "", "", "", "",
					"", new Integer(1) });
			getSubAccount(account[i], model);
		}
	}

	double[] m_total = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	String[] m_strtotal = { "", "", "", "", "", "", "", "", "", "" };

	private void getSubAccount(Account account, DefaultTableModel model)
			throws Exception {
		Account[] subaccount = m_logic.getSubAccount(m_sessionid,
				IDBConstants.MODUL_ACCOUNTING, account.getIndex());
		for (int i = 0; i < subaccount.length; i++) {
			if (subaccount[i].isGroup()) {
				model.addRow(new Object[] { subaccount[i].getCode(),
						subaccount[i].getName(), "", "", "", "", "", "", "",
						"", "", "", new Integer(1) });
				getSubAccount(subaccount[i], model);
			} else {
				String[] setvalue = { "", "", "", "", "", "", "", "", "", "" };

				String transDate = "tp."
						+ IDBConstants.ATTR_TRANSACTION_DATE
						+ "< '"
						+ m_dateFormat2.format(m_periodStartEnd.m_startDate
								.getDate()) + "'";
				String queryBeginningBalance = getQuery(subaccount[i],
						TrialBalanceAccountTypeSetting.BEGINNING_BALANCE,
						transDate);

				double beginningBalancevalue = m_logic.getDebitValue(
						m_sessionid, IDBConstants.MODUL_ACCOUNTING,
						queryBeginningBalance);

				if (beginningBalancevalue > 0) {
					setvalue[0] = m_desimalFormat.format(beginningBalancevalue);

					m_total[0] += beginningBalancevalue;
					m_strtotal[0] = m_desimalFormat.format(m_total[0]);
				} else if (beginningBalancevalue < 0) {
					setvalue[1] = m_desimalFormat
							.format(-beginningBalancevalue);

					m_total[1] += beginningBalancevalue;
					m_strtotal[1] = m_desimalFormat.format(-m_total[1]);
				} else {
					if (subaccount[i].getBalance() == 0)
						setvalue[0] = m_desimalFormat
								.format(beginningBalancevalue);
					else
						setvalue[1] = m_desimalFormat
								.format(beginningBalancevalue);

				}

				// transDate = "(tp."+ IDBConstants.ATTR_TRANSACTION_DATE+"
				// BETWEEN '"
				// +m_dateFormat2.format(m_periodStartEnd.m_startDate.getDate())+
				// "' AND '"
				// +m_dateFormat2.format(m_periodStartEnd.m_endDate.getDate())+
				// "')";
				transDate = IDBConstants.ATTR_TRANSACTION_DATE
						+ " BETWEEN '"
						+ m_dateFormat2.format(m_periodStartEnd.m_startDate
								.getDate())
						+ "' AND '"
						+ m_dateFormat2.format(m_periodStartEnd.m_endDate
								.getDate()) + "'";
				
				String operatorPositif = ">";
				//if (subaccount[i].getBalance()==1)
				//	operatorPositif = "<";
				String operatorNegatif = "<";
				//if (subaccount[i].getBalance()==1)
				//	operatorPositif = ">";
					
				
				String queryTranpositif = getQueryTransaction(subaccount[i],
						operatorPositif, transDate, TrialBalanceAccountTypeSetting.TRANSACTION);
				double transValuepositif = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryTranpositif);
				String queryTrannegatif = getQueryTransaction(subaccount[i],
						operatorNegatif, transDate, TrialBalanceAccountTypeSetting.TRANSACTION);
				double tranValuenegatif = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryTrannegatif);
				
				setvalue[2] = m_desimalFormat.format(transValuepositif);
				m_total[2] += transValuepositif;
				m_strtotal[2] = m_desimalFormat.format(m_total[2]);

				setvalue[3] = m_desimalFormat.format(-tranValuenegatif);
				if (tranValuenegatif == 0)
					setvalue[3] = m_desimalFormat.format(tranValuenegatif);
				m_total[3] += tranValuenegatif;
				m_strtotal[3] = m_desimalFormat.format(-m_total[3]);
				
				/*
				 * if (subaccount[i].getIndex()==93) System.err.println("a");
				 */
				/*if (subaccount[i].getBalance() == 0) {
					setvalue[2] = m_desimalFormat.format(transValuepositif);
					m_total[2] += transValuepositif;
					m_strtotal[2] = m_desimalFormat.format(m_total[2]);

					setvalue[3] = m_desimalFormat.format(-tranValuenegatif);
					if (tranValuenegatif == 0)
						setvalue[3] = m_desimalFormat.format(tranValuenegatif);
					m_total[3] += tranValuenegatif;
					m_strtotal[3] = m_desimalFormat.format(-m_total[3]);
				} else {
					
					setvalue[3] = m_desimalFormat.format(-transValuepositif);
					m_total[3] += transValuepositif;
					m_strtotal[3] = m_desimalFormat.format(m_total[3]);

					setvalue[2] = m_desimalFormat.format(-tranValuenegatif);
					if (tranValuenegatif == 0)
						setvalue[2] = m_desimalFormat.format(tranValuenegatif);
					m_total[2] += tranValuenegatif;
					m_strtotal[2] = m_desimalFormat.format(-m_total[2]);
				}*/

				transDate = "tp."
						+ IDBConstants.ATTR_TRANSACTION_DATE
						+ "<='"
						+ m_dateFormat2.format(m_periodStartEnd.m_endDate
								.getDate()) + "'";
				String queryUnadjustedBalance = getQuery(subaccount[i],
						TrialBalanceAccountTypeSetting.UNADJUSTED, transDate);
				double unadjustedBalanceValue = m_logic.getDebitValue(
						m_sessionid, IDBConstants.MODUL_ACCOUNTING,
						queryUnadjustedBalance);
				if (unadjustedBalanceValue > 0) {
					setvalue[4] = m_desimalFormat
							.format(unadjustedBalanceValue);
					m_total[4] += unadjustedBalanceValue;
					m_strtotal[4] = m_desimalFormat.format(m_total[4]);
				} else if (unadjustedBalanceValue < 0) {
					setvalue[5] = m_desimalFormat
							.format(-unadjustedBalanceValue);

					m_total[5] += unadjustedBalanceValue;
					m_strtotal[5] = m_desimalFormat.format(-m_total[5]);
				} else {
					if (subaccount[i].getBalance() == 0)
						setvalue[4] = m_desimalFormat
								.format(unadjustedBalanceValue);
					else
						setvalue[5] = m_desimalFormat
								.format(unadjustedBalanceValue);
				}

				transDate = "(tp."
						+ IDBConstants.ATTR_TRANSACTION_DATE
						+ " BETWEEN '"
						+ m_dateFormat2.format(m_periodStartEnd.m_startDate
								.getDate())
						+ "' AND '"
						+ m_dateFormat2.format(m_periodStartEnd.m_endDate
								.getDate()) + "')";
				/*String queryAdjustments = getQuery(subaccount[i],
						TrialBalanceAccountTypeSetting.ADJUSTMENT, transDate);
				double adjustmentsValue = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryAdjustments);*/
				
				String queryAdjpositif = getQueryTransaction(subaccount[i],
						">", transDate, TrialBalanceAccountTypeSetting.ADJUSTMENT);
				double adjValuepositif = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryAdjpositif);
				String queryAdjnegatif = getQueryTransaction(subaccount[i],
						"<", transDate, TrialBalanceAccountTypeSetting.ADJUSTMENT);
				double adjValuenegatif = m_logic.getDebitValue(m_sessionid,
						IDBConstants.MODUL_ACCOUNTING, queryAdjnegatif);

				/*if (adjustmentsValue > 0) {
					setvalue[6] = m_desimalFormat.format(adjustmentsValue);

					m_total[6] += adjustmentsValue;
					m_strtotal[6] = m_desimalFormat.format(m_total[6]);
				} else if (adjustmentsValue < 0) {
					setvalue[7] = m_desimalFormat.format(-adjustmentsValue);

					m_total[7] += adjustmentsValue;
					m_strtotal[7] = m_desimalFormat.format(-m_total[7]);
				} else {
					if (subaccount[i].getBalance() == 0)
						setvalue[6] = m_desimalFormat.format(adjustmentsValue);
					else
						setvalue[7] = m_desimalFormat.format(adjustmentsValue);
				}*/
				
				setvalue[6] = m_desimalFormat.format(adjValuepositif);
				m_total[6] += adjValuepositif;
				m_strtotal[6] = m_desimalFormat.format(m_total[6]);

				setvalue[7] = m_desimalFormat.format(-adjValuenegatif);
				if (adjValuenegatif == 0)
					setvalue[7] = m_desimalFormat.format(adjValuenegatif);
				m_total[7] += adjValuenegatif;
				m_strtotal[7] = m_desimalFormat.format(-m_total[7]);
				
				/*if (subaccount[i].getBalance() == 0) {
					setvalue[6] = m_desimalFormat.format(adjValuepositif);
					m_total[6] += adjValuepositif;
					m_strtotal[6] = m_desimalFormat.format(m_total[6]);

					setvalue[7] = m_desimalFormat.format(-adjValuenegatif);
					if (adjValuenegatif == 0)
						setvalue[7] = m_desimalFormat.format(adjValuenegatif);
					m_total[7] += adjValuenegatif;
					m_strtotal[7] = m_desimalFormat.format(-m_total[7]);
				} else {

					setvalue[7] = m_desimalFormat.format(adjValuepositif);
					m_total[7] += adjValuepositif;
					m_strtotal[7] = m_desimalFormat.format(m_total[7]);

					setvalue[6] = m_desimalFormat.format(-adjValuenegatif);
					if (adjValuenegatif == 0)
						setvalue[6] = m_desimalFormat.format(adjValuenegatif);
					m_total[6] += adjValuenegatif;
					m_strtotal[6] = m_desimalFormat.format(-m_total[6]);
				}*/

				transDate = "tp."
						+ IDBConstants.ATTR_TRANSACTION_DATE
						+ " <='"
						+ m_dateFormat2.format(m_periodStartEnd.m_endDate
								.getDate()) + "'";
				String queryAdjustmentBalance = getQuery(subaccount[i],
						TrialBalanceAccountTypeSetting.ADJUSTED, transDate);
				double adjustmentBalanceValue = m_logic.getDebitValue(
						m_sessionid, IDBConstants.MODUL_ACCOUNTING,
						queryAdjustmentBalance);
				if (adjustmentBalanceValue > 0) {
					setvalue[8] = m_desimalFormat
							.format(adjustmentBalanceValue);

					m_total[8] += adjustmentBalanceValue;
					m_strtotal[8] = m_desimalFormat.format(m_total[8]);
				} else if (adjustmentBalanceValue < 0) {
					setvalue[9] = m_desimalFormat
							.format(-adjustmentBalanceValue);
					m_total[9] += adjustmentBalanceValue;
					m_strtotal[9] = m_desimalFormat.format(-m_total[9]);
				} else {
					if (subaccount[i].getBalance() == 0)
						setvalue[8] = m_desimalFormat
								.format(adjustmentBalanceValue);
					else
						setvalue[9] = m_desimalFormat
								.format(adjustmentBalanceValue);
				}

				model.addRow(new Object[] { subaccount[i].getCode(),
						subaccount[i].getName(), setvalue[0], setvalue[1],
						setvalue[2], setvalue[3], setvalue[4], setvalue[5],
						setvalue[6], setvalue[7], setvalue[8], setvalue[9],
						new Integer(1) });
			}
		}
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
		model.addColumn("status");
	}

	private String getQuery(Account subaccount, String columnType,
			String transDate) {
		
		String query = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue FROM transvalueposted tv, account ac, transactionposted tp "
				+ "WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted "
				+ "AND ac.TREEPATH LIKE '"
				+ subaccount.getTreePath()
				+ "%' AND "
				+ transDate
				+ "AND tp.journal IN "
				+ "(SELECT journal FROM "
				+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING
				+ " WHERE "
				+ IDBConstants.ATTR_COLUMN_NAME
				+ "=UPPER('"
				+ columnType
				+ "') AND "
				+ IDBConstants.ATTR_USED
				+ "=true)  AND "
				+ "ac.category IN (SELECT category FROM "
				+ IDBConstants.TABLE_TRIAL_BALANCE_ACCOUNT_TYPE_SETTING
				+ " WHERE columnname=UPPER('"
				+ columnType
				+ "') AND "
				+ IDBConstants.ATTR_USED + "=true)";

		
		/*Journal j = null;
		if (bbJS != null)
			j = bbJS.getJournal();

		long idx = -1;
		if (j != null)
			idx = j.getIndex();

		String query1 = "SELECT exchangerate, accvalue, balancecode, treepath, category FROM transvalueposted tv, account ac, transactionposted tp "
				+ "WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted "
				+ "AND "
				+ transDate
				+ "AND tp.journal IN "
				+ "(SELECT journal FROM "
				+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING
				+ " WHERE "
				+ IDBConstants.ATTR_COLUMN_NAME
				+ "='"
				+ columnType
				+ "' AND "
				+ IDBConstants.ATTR_USED
				+ "=true AND journal<>"
				+ idx
				+ ")";
		
		String query2 = "SELECT exchangerate, accvalue, balancecode, treepath, category FROM transvalueposted tv, account ac, transactionposted tp "
			+ "WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted "
			+ "AND tp.journal = " 
			+ idx;
		
		String query = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue FROM (" + query1 + " UNION " + query2 + ") "
			+ " WHERE treepath LIKE '" + subaccount.getTreePath() + "%' AND category IN ("
			+ "SELECT CATEGORY FROM " + IDBConstants.TABLE_TRIAL_BALANCE_ACCOUNT_TYPE_SETTING + " WHERE columnname='"
				+ columnType + "')";*/
		return query;
	}	
	
	private String getQueryTransaction(Account subaccount, String operator,
			String transDate, String columnType) {
		
		String query = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue FROM transvalueposted tv, account ac, transactionposted tp "
			+ "WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted "
			+ "AND ac.TREEPATH LIKE '"
			+ subaccount.getTreePath()
			+ "%' AND "
			+ transDate
			+ " AND (exchangerate*accvalue*(1-2*balancecode)) " 
			+ operator 
			+ "0 "
			+ "AND tp.journal IN "
			+ "(SELECT journal FROM "
			+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING
			+ " WHERE "
			+ IDBConstants.ATTR_COLUMN_NAME
			+ "=UPPER('"
			+ columnType
			+ "') AND "
			+ IDBConstants.ATTR_USED
			+ "=true)  AND "
			+ "ac.category IN (SELECT category FROM "
			+ IDBConstants.TABLE_TRIAL_BALANCE_ACCOUNT_TYPE_SETTING
			+ " WHERE columnname=UPPER('"
			+ columnType
			+ "') AND "
			+ IDBConstants.ATTR_USED + "=true)";
		
		/*String query = "select sum(accvalue*exchangerate) debitvalue from TRANSVALUEPOSTED where account="
				+ subaccount.getIndex()
				+ " and accvalue  "
				+ operator
				+ "0 and transactionposted in (select autoindex from transactionposted where "
				+ transDate + ")";*/
		
		return query;
	}
	
	public JRViewer getPrintView() {
		return m_jrv;
	}
	
	public JasperPrint getJasperPrint() {
		return m_jasperPrint;
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
	
}
