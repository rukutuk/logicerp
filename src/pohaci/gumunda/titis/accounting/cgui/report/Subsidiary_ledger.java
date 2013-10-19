package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.TransactionPosted;
import pohaci.gumunda.titis.accounting.entity.TransactionPostedDetail;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodSubsidiaryLedger;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class Subsidiary_ledger {
	JRViewer m_jrv;
	JasperPrint m_jasperPrint;
	Connection m_conn;
	long m_sessionid;
	PeriodSubsidiaryLedger m_periodStartEnd;
	UnitPicker m_unitPicker;
	Account m_account;
	SimpleDateFormat m_dateFormat, m_dateFormat2;
	DecimalFormat m_desimalFormat, m_desimalFormat2;
	AccountingBusinessLogic m_logic;

	SubsidiaryAccountSetting m_subsidiariAcc;
	Object m_list;
	boolean m_excel;

	public Subsidiary_ledger(Connection conn, long sessionid,
			SubsidiaryAccountSetting subsidiariAcc,
			PeriodSubsidiaryLedger periodStartEnd, Object list, boolean excel) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_subsidiariAcc = subsidiariAcc;
		m_periodStartEnd = periodStartEnd;
		m_list = list;
		m_excel = excel;
		m_logic = new AccountingBusinessLogic(m_conn);
		m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		m_desimalFormat = new DecimalFormat("#,##0.00");
		if (m_excel == true)
			m_desimalFormat = new DecimalFormat("###.00");
		setNonEmpty();
	}

	public Subsidiary_ledger() {
		setEmpty();
	}

	private void setEmpty() {
		try {
			String filename = "Subsidiary_ledger";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_akunname", "");
			parameters.put("param_akuncode", "");
			parameters.put("param_subsidiary_name", "");
			parameters.put("param_period", "");

			header(model);

			model
					.addRow(new Object[] { "", "", "", "", "", "",
							new Integer(0) });
			model.addRow(new Object[] { "", "", "Total", "", "", "",
					new Integer(2) });

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					compiledRptFilename, parameters, ds);

			m_jrv = new JRViewer(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void header(DefaultTableModel model) {
		model.addColumn("field1");
		model.addColumn("field2");
		model.addColumn("field3");
		model.addColumn("field4");
		model.addColumn("field5");
		model.addColumn("field6");
		model.addColumn("status");
	}

	private void setNonEmpty() {
		try {
			String filename = "Subsidiary_ledger";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_akunname", m_subsidiariAcc.getAccount()
					.getName());
			parameters.put("param_akuncode", m_subsidiariAcc.getAccount()
					.getCode());
			parameters
					.put("param_period", m_dateFormat
							.format(m_periodStartEnd.m_startDate.getDate())
							+ " - "
							+ m_dateFormat.format(m_periodStartEnd.m_endDate
									.getDate()));

			header(model);

			long index = -1;
			String subsidiaryName = "";
			if (m_list instanceof BankAccount) {
				BankAccount bank = (BankAccount) m_list;
				subsidiaryName = bank.toString();
				index = bank.getIndex();
			} else if (m_list instanceof CashAccount) {
				CashAccount cash = (CashAccount) m_list;
				subsidiaryName = cash.toString();
				index = cash.getIndex();
			} else if (m_list instanceof CompanyLoan) {
				CompanyLoan loan = (CompanyLoan) m_list;
				subsidiaryName = loan.toString();
				index = loan.getIndex();
			} else if (m_list instanceof Employee) {
				Employee emp = (Employee) m_list;
				subsidiaryName = emp.toString();
				index = emp.getIndex();
			} else if (m_list instanceof Partner) {
				Partner partner = (Partner) m_list;
				subsidiaryName = partner.toString();
				index = partner.getIndex();
			} else if (m_list instanceof Customer) {
				Customer cust = (Customer) m_list;
				subsidiaryName = cust.toString();
				index = cust.getIndex();
			} else if (m_list instanceof ProjectData) {
				ProjectData project = (ProjectData) m_list;
				subsidiaryName = project.toString();
				index = project.getIndex();
			}

			parameters.put("param_subsidiary_name", subsidiaryName);

			String queryBegining = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue "
					+ "FROM transvalueposted tv, account ac, transactionposted tp "
					+ "WHERE "
					+ "ac.autoindex = tv.account AND tp.autoindex=transactionposted AND ac.TREEPATH LIKE '"
					+ m_subsidiariAcc.getAccount().getTreePath()
					+ "%' "
					+ "AND tp.TRANSACTIONDATE < '"
					+ m_dateFormat2.format(m_periodStartEnd.m_startDate
							.getDate()) + "' AND tv.subsidiaryaccount=" + index;

			double debitValue = m_logic.getDebitValue(m_sessionid,
					IDBConstants.MODUL_ACCOUNTING, queryBegining);

			System.err.println(debitValue + " : " + queryBegining);
			/*
			 * String strDeb = ""; String strCre = ""; double saldoAwal = 0;
			 */
			double valBalance = debitValue;
			double totDeb = 0;
			double totCre = 0;

			/*
			 * if (debitValue>0){ strDeb = m_desimalFormat.format(debitValue);
			 * saldoAwal =debitValue;
			 * 
			 * //totDeb = debitValue; }else if (debitValue<0){ //totCre =
			 * debitValue; strCre = m_desimalFormat.format(-debitValue);
			 * saldoAwal =debitValue; }else{ if
			 * (m_subsidiariAcc.getAccount().getBalance()==0){ strDeb =
			 * m_desimalFormat.format(debitValue); }else{ strCre =
			 * m_desimalFormat.format(debitValue); } }
			 */

			// if (debitValue<0)
			// valBalance = -debitValue;
			double begBal = valBalance;
			if (m_subsidiariAcc.getAccount().getBalance() == 1) {
				if (valBalance != 0)
					valBalance = -valBalance;
				if (begBal != 0)
					begBal = -begBal;
				if (debitValue != 0)
					debitValue = -debitValue;
			}

			// String value = m_desimalFormat.format(valBalance);
			String value = m_desimalFormat.format(debitValue);
			// model.addRow(new Object[]{"","Saldo
			// Awal","",strDeb,strCre,m_desimalFormat.format(saldoAwal),new
			// Integer(1)});
			model.addRow(new Object[] { "", "Saldo Awal", "", "", "", value,
					new Integer(0) });
			GenericMapper mapper = MasterMap
					.obtainMapperFor(TransactionPostedDetail.class);
			mapper.setActiveConn(m_conn);
			String strWhere = IDBConstants.ATTR_ACCOUNT + "="
					+ m_subsidiariAcc.getAccount().getIndex() + " AND "
					+ IDBConstants.ATTR_SUBSIDIARY_ACCOUNT + "=" + index;
			List RS = mapper.doSelectWhere(strWhere);

			List tableList = new ArrayList();

			if (RS.size() > 0) {
				for (int i = 0; i < RS.size(); i++) {
					TransactionPostedDetail transPostVal = (TransactionPostedDetail) RS
							.get(i);
					GenericMapper mapper2 = MasterMap
							.obtainMapperFor(TransactionPosted.class);
					mapper2.setActiveConn(m_conn);
					strWhere = IDBConstants.ATTR_AUTOINDEX
							+ "="
							+ transPostVal.getTransactionPostedId()
							+ " AND "
							+ IDBConstants.ATTR_TRANSACTION_DATE
							+ " BETWEEN '"
							+ m_dateFormat2.format(m_periodStartEnd.m_startDate
									.getDate())
							+ "' AND '"
							+ m_dateFormat2.format(m_periodStartEnd.m_endDate
									.getDate()) + "' order by transactiondate";
					List RS2 = mapper2.doSelectWhere(strWhere);
					if (RS2.size() > 0) {
						for (int j = 0; j < RS2.size(); j++) {
							TransactionPosted transPost = (TransactionPosted) RS2
									.get(j);

							SubsidiaryLedgerEntity entity = new SubsidiaryLedgerEntity();
							entity.setTransactionDate(transPost.getTransDate());
							entity.setPostedDate(transPost.getPostedDate());
							entity.setDescription(transPost.getDescription());
							entity.setReferenceNo(transPost.getReference());
							entity.setValue(transPostVal.getValue());
							entity.setExchangeRate(transPostVal
									.getExchangeRate());
							tableList.add(entity);
						}
					}
				}
			}

			// urutkan!
			Collections.sort(tableList);

			Iterator iterator = tableList.iterator();
			while (iterator.hasNext()) {
				SubsidiaryLedgerEntity entity = (SubsidiaryLedgerEntity) iterator
						.next();

				m_desimalFormat.format(entity.getValue()
						* entity.getExchangeRate());
				valBalance += (entity.getValue() * entity.getExchangeRate());
				String strDebit = "";
				String strCredit = "";
				String strBalance = "";
				if (m_subsidiariAcc.getAccount().getBalance() == 0) {
					if (entity.getValue() > 0) {
						strDebit = m_desimalFormat.format(entity.getValue()
								* entity.getExchangeRate());
						totDeb += (entity.getValue() * entity.getExchangeRate());
					} else {
						strCredit = m_desimalFormat.format(-entity.getValue()
								* entity.getExchangeRate());
						totCre += (-entity.getValue() * entity
								.getExchangeRate());
					}
				} else {
					if (entity.getValue() > 0) {
						strCredit = m_desimalFormat.format(entity.getValue()
								* entity.getExchangeRate());
						totCre += (entity.getValue() * entity.getExchangeRate());
					} else {
						strDebit = m_desimalFormat.format(-entity.getValue()
								* entity.getExchangeRate());
						totDeb += (-entity.getValue() * entity
								.getExchangeRate());
					}
				}
				strBalance = m_desimalFormat.format(valBalance);
				model.addRow(new Object[] {
						m_dateFormat.format(entity.getTransactionDate()),
						entity.getDescription(), entity.getReferenceNo(),
						strDebit, strCredit, strBalance, new Integer(1) });
			}
			model
					.addRow(new Object[] { "", "", "", "", "", "",
							new Integer(2) });
			String strTotDeb = "";
			String strTotCre = "";
			double totBalance = 0;
			if (totCre < 0)
				totCre = -totCre;
			strTotDeb = m_desimalFormat.format(totDeb);
			strTotCre = m_desimalFormat.format(totCre);

			if (m_subsidiariAcc.getAccount().getBalance() == 0) {
				totBalance = begBal + totDeb - totCre;
			} else {
				totBalance = begBal + totCre - totDeb;
			}

			model.addRow(new Object[] { "", "", "Total", strTotDeb, strTotCre,
					m_desimalFormat.format(totBalance), new Integer(2) });

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			m_jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);

			if (m_excel)
				exportToExcel(m_jasperPrint);
			else
				m_jrv = new JRViewer(m_jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void exportToExcel(JasperPrint jasperPrint) throws Exception {
		javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(
				"reportsample/");

		jfc.setDialogTitle("Simpan Laporan Dalam File Excel");
		jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File file) {
				String filename = file.getName();
				return (filename.toLowerCase().endsWith(".xls")
						|| file.isDirectory() || filename.toLowerCase()
						.endsWith(".jrxml"));
			}

			public String getDescription() {
				return "Laporan *.xls";
			}
		});

		jfc.setMultiSelectionEnabled(false);
		jfc.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
		if (jfc.showSaveDialog(null) == javax.swing.JOptionPane.OK_OPTION) {
			// JExcelApiExporter exporterXLS = new JExcelApiExporter();
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter
					.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
							changeFileExtension(
									jfc.getSelectedFile().getPath(), "xls"));
			exporter.setParameter(
					JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.TRUE);
			exporter.setParameter(
					JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED,
					Boolean.TRUE);
			exporter.exportReport();
		}
	}

	public static String changeFileExtension(String filename,
			String newExtension) {
		if (!newExtension.startsWith("."))
			newExtension = "." + newExtension;
		if (filename == null || filename.length() == 0) {
			return newExtension;
		}

		int index = filename.lastIndexOf(".");
		if (index >= 0) {
			filename = filename.substring(0, index);
		}
		return filename += newExtension;
	}

	public JRViewer getPrintView() {
		return m_jrv;
	}

	public JasperPrint getJasperPrint() {
		return m_jasperPrint;
	}
}
