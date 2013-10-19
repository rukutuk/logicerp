package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.hrm.cgui.Organization;
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

public class DepartmentCost {
	JRViewer m_jrv=null;
	Connection m_conn;
	long m_sessionid;
	Account vasFA = null;
	double faValue = 0;
	String filename;
	JasperReport m_jasperReport;
	JasperPrint m_jasperPrint;
	SimpleDateFormat m_dateformat, m_dateformat2;
	DatePicker m_fromdate, m_todate;	
	DecimalFormat m_desimalformat;	
	AccountingBusinessLogic m_logic;
	int m_level = 0;
	Organization m_org;
	JMonthChooser m_mount;
	JSpinField m_year;
	Unit m_unit=null;
	double totval1,totval2,totval3;
	
	public DepartmentCost(){		
		filename = "Department_cost.jrxml";
		try {
			//JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			String jasperReport = ReportUtils.compileReport("Department_cost");
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_department","");
			parameters.put("param_period","");
			parameters.put("param_first","");
			parameters.put("param_second","");
			parameters.put("param_third","");
			
			model.addColumn("field0");
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("field4");
			model.addColumn("status");
			
			model.addRow(new Object[] {"", "", "", "","",new Integer(0)});
			model.addRow(new Object[] {"", "TOTAL BIAYA", "", "","",new Integer(1)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			m_jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			m_jrv = new JRViewer(m_jasperPrint);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public DepartmentCost(Connection conn, long sessionid, Organization org, Unit unit, Account account, 
			JMonthChooser mount, JSpinField year,boolean isExcel) {
		m_conn = conn;
		m_sessionid = sessionid;
		filename = "Department_cost.jrxml";
		m_org = org;
		m_mount = mount;
		m_unit = unit;
		m_year = year;
		m_desimalformat = new DecimalFormat("#,##0.00");
		m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateformat2 = new SimpleDateFormat("yyyy-MM-dd");
		m_logic = new AccountingBusinessLogic(m_conn);
		try {
			String filename = "Department_cost.jrxml";
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_department", org.toString());
			parameters.put("param_period", getFirstPeriod(mount.getMonth()+1,year.getValue()));
			parameters.put("param_first", getFirstPeriod(mount.getMonth(),year.getValue()));
			parameters.put("param_second", getFirstPeriod(mount.getMonth()+1,year.getValue()));
			parameters.put("param_third", String.valueOf(year.getValue()));
			
			model.addColumn("field0");
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("field4");
			model.addColumn("status");			
			
			totval1 =0;
			totval2=0;
			totval3=0;
			if (account.isGroup())
				model.addRow(new Object[] { account.getCode(),account.getName(), "","","",new Integer(0) });				
			else{				
				double[] valarray1=getValue1(account);
				double[] valarray2=getValue2(account);
				double[] valarray3=getValue3(account);
				double val1 =valarray1[0]+valarray1[1]+valarray1[2]+valarray1[3]+valarray1[4]+valarray1[5]+valarray1[6]+valarray1[7]; 
				double val2 = valarray2[0]+valarray2[1]+valarray2[2]+valarray2[3]+valarray2[4]+valarray2[5]+valarray2[6]+valarray2[7];
				double val3 =valarray3[0]+valarray3[1]+valarray3[2]+valarray3[3]+valarray3[4]+valarray3[5]+valarray3[6]+valarray3[7];
				
				totval1 +=val1;totval2+=val2;totval3+=val3;
				model.addRow(new Object[] { account.getCode(),account.getName(), m_desimalformat.format(val1),
						m_desimalformat.format(val2),m_desimalformat.format(val3),new Integer(0) });
			}
			if (account.isGroup())
				setSubAccount(model, account);
			model.addRow(new Object[] {"","TOTAL BIAYA", m_desimalformat.format(totval1),m_desimalformat.format(totval2),m_desimalformat.format(totval3),new Integer(1) });
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			m_jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			/*if (isExcel)
			 exportToExcel(m_jasperPrint);
			 else*/
			m_jrv = new JRViewer(m_jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void setSubAccount(DefaultTableModel model, Account account) {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		try {
			Account[] subaccount = logic.getSubAccount(m_sessionid,
					IDBConstants.MODUL_ACCOUNTING, account.getIndex());
			for (int i = 0; i < subaccount.length; i++) {
				String m_spance = "      ";
				for (int j = 0; j < m_level; j++) {
					m_spance += "       ";
				}
				if (subaccount[i].isGroup())
					model.addRow(new Object[] {subaccount[i].getCode(),m_spance + subaccount[i].getName(),
							"","","", new Integer(0)});
				else{
					double[] valarray1=getValue1(subaccount[i]);
					double[] valarray2=getValue2(subaccount[i]);
					double[] valarray3=getValue3(subaccount[i]);
					double val1 =valarray1[0]+valarray1[1]+valarray1[2]+valarray1[3]+valarray1[4]+valarray1[5]+valarray1[6]+valarray1[7]; 
					double val2 = valarray2[0]+valarray2[1]+valarray2[2]+valarray2[3]+valarray2[4]+valarray2[5]+valarray2[6]+valarray2[7];
					double val3 =valarray3[0]+valarray3[1]+valarray3[2]+valarray3[3]+valarray3[4]+valarray3[5]+valarray3[6]+valarray3[7];
					totval1 +=val1;totval2+=val2;totval3+=val3;
					model.addRow(new Object[] {subaccount[i].getCode(),m_spance + subaccount[i].getName(),
							m_desimalformat.format(val1),m_desimalformat.format(val2),
							m_desimalformat.format(val3), new Integer(0)});
				}
				if (subaccount[i].isGroup()) {
					m_level = m_level + 1;
					setSubAccount(model, subaccount[i]);
					m_level = m_level - 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double[] getValue1(Account account) throws Exception {
		String fromdate = "";
		if (m_mount.getMonth()>0){
			fromdate = m_year.getValue()+"-"+(m_mount.getMonth()) +"-1";
		}else{
			fromdate = (m_year.getValue()-1)+"-12-1";			
		}
		String todate = m_year.getValue()+"-"+(m_mount.getMonth()+1) +"-1";	
		double[] debitValue = getQueryValue(account, fromdate, todate);
		return debitValue;
	}
	
	private double[] getValue2(Account account) throws Exception {
		String fromdate = m_year.getValue()+"-"+(m_mount.getMonth()+1) +"-1";
		String todate =m_year.getValue()+"-"+(m_mount.getMonth()+2) +"-1";
		if (m_mount.getMonth()==11)
			todate = (m_year.getValue()+1)+"-1-1";
		double[] debitValue = getQueryValue(account, fromdate, todate);
		return debitValue;
	}
	
	private double[] getValue3(Account account) throws Exception {
		String fromdate = m_year.getValue()+"-1-1";
		String todate = m_year.getValue()+"-"+(m_mount.getMonth()+2) +"-1";
		if (m_mount.getMonth()==11)
			todate = (m_year.getValue()+1)+"-1-1";
		double[] debitValue = getQueryValue(account, fromdate, todate);
		return debitValue;
	}
	
	private double[] getQueryValue(Account account, String fromdate, String todate) throws Exception {
		String query0 = getQuery(account,fromdate,todate);
		String query1 = getQueryPayroll(account,fromdate,todate);
		String query2 = getQueryTaxArt21(account,fromdate,todate);
		String query3 = getQueryOperationalCost(account,fromdate,todate);
		String query4 = getQueryExpensesheeothers(account,fromdate,todate);
		String query5 = getQueryMemjournalstrd(account,fromdate,todate);
		String query6 = getQueryMemjournalnonstrd(account,fromdate,todate);
		String query7 = getQueryTaxArt21(account,fromdate,todate);
		double[] debitValue = {0,0,0,0,0,0,0,0};
		debitValue[0] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query0);
		debitValue[1] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query1);
		debitValue[2] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query2);
		debitValue[3] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query3);
		debitValue[4] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query4);
		debitValue[5] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query5);
		debitValue[6] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query6);
		debitValue[7] = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING, query7);
		return debitValue;
	}
	
	public String getQuery(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "acct.unit=" + m_unit.getIndex() + " and ";
		String query = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue FROM transactionvalue tv, account ac, acctransaction acct "
			+ "WHERE ac.autoindex = tv.account AND acct.autoindex=tv.trans AND ac.treepath LIKE '"
			+ account.getTreePath()
			+ "%' AND (acct.TRANSACTIONDATE>='"+fromdate+"' " +
			"AND acct.TRANSACTIONDATE<'"+todate+"') AND "
			+ "" + strunit + " acct.autoindex in (select trans from (" 
			+ "select autoindex,trans from pmtprojectcost where status =3 and project in (select autoindex from projectdata where autoindex in(select project from pmtprojectcost where status =3)and department="+m_org.getIndex() + ")" // pmtprojectcost 
			//+ " union select autoindex,trans from pmtoperationalcost where status =3 and autoindex in (select pmtoperationalcost  from pmtoperationalcostdetail where department="+m_org.getIndex() + ")" // pmtothersdetail
			+ " union select autoindex,trans from expensesheet where status=3 and (pmtcaproject in (select autoindex from pmtcaproject where project in (select autoindex from projectdata where department="+m_org.getIndex() + "))) "// expensesheet pmtcaproj
			+ " union select autoindex,trans from expensesheet where status=3 and pmtcaothers in (select autoindex from pmtcaothers where department="+m_org.getIndex() + ")" // expensesheet pmtcaothers
			+ " union select autoindex,trans from expensesheet where status=3 and pmtcaiouotherssettled in(select autoindex from pmtcaiouotherssettled where pmtcaiouothers in(select autoindex from pmtcaiouothers where department="+m_org.getIndex() + "))" // expensesheet pmtcaotherssettled
			//+ "union select autoindex,trans from expensesheet where beginningbalance in (select autoindex from beginningcashadvance)" untuk beginning balance tanya ke irwan
			//+ " union select autoindex,trans from expensesheet where status=3" //untuk beginning balance tanya ke irwan
			+ " union select autoindex,trans from expensesheet where status=3 and pmtcaiouprojectsettled in (select autoindex from pmtcaiouprojectsettled where pmtcaiouproject in (select autoindex from pmtcaiouproject where project in (select autoindex from projectdata where autoindex in (select project from pmtcaiouproject) and department="+m_org.getIndex()+"))) "// expensesheet pmtcaiouprojectsettled
			//+ "union select autoindex,trans from pmtothers where status=3 and autoindex in (select pmtothers from pmtothersdetail where department="+m_org.getIndex() + ") " // ptmothers
			+ " union select autoindex,trans from memjournalstrd where status=3 and department="+m_org.getIndex() // mjstandard
			+ " union select autoindex,trans from memjournalnonstrd where status=3 and department="+m_org.getIndex() // mjnonstrdproje
			//+ " union select autoindex,trans from payrolldeptvalue where department="+m_org.getIndex() // departement
			+"))";
		return query;
	}
	
	public String getQueryPayroll(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "and unit=" + m_unit.getIndex();
		String query="select sum(accvalue) debitvalue from "+IDBConstants.TABLE_PAYROLL_DEPARTMENT_VALUE+" where account="+account.getIndex()+" and " +
		"department="+m_org.getIndex()+" and employeepayroll in (select autoindex from employeepayroll where trans in(select autoindex from acctransaction where " +
		"transactiondate>='"+fromdate+"' and transactiondate<'"+todate+"' "+strunit+") and " + IDBConstants.ATTR_STATUS + "=3)";
		return query;
	}
	
	public String getQueryTaxArt21(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "and unit=" + m_unit.getIndex();
		String query="select sum(accvalue) debitvalue from "+IDBConstants.TABLE_TAXART21_DEPARTMENT_VALUE+" where account="+account.getIndex()+" and " +
		"department="+m_org.getIndex()+" and taxart21submit in (select autoindex from taxart21submit where trans in(select autoindex from acctransaction where " +
		"transactiondate>='"+fromdate+"' and transactiondate<'"+todate+"' "+strunit+"))";
		return query;
	}
	
	public String getQueryOperationalCost(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "and acct.unit=" + m_unit.getIndex();
		String query="select sum(accvalue) debitvalue from pmtoperationalcost a, pmtoperationalcostdetail b where  " +
		"a.autoindex=b.pmtoperationalcost and b.account="+account.getIndex()+" and a.trans in " +
		"(SELECT acct.autoindex FROM transactionvalue tv, account ac, acctransaction acct WHERE " +
		"ac.autoindex = tv.account AND acct.autoindex=tv.trans AND ac.treepath LIKE '"+account.getTreePath()+"%' " +
		"AND (acct.TRANSACTIONDATE>='"+fromdate+"' AND acct.TRANSACTIONDATE<'"+todate+"') "+strunit+
		") and " +
		"a.status=3 and " + 
		"b.department=" + m_org.getIndex();
		return query;
	}
	
	public String getQueryExpensesheeothers(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "and acct.unit=" + m_unit.getIndex();
		String query="select sum(accvalue) debitvalue from expensesheet a, expensesheetdetail b where  " +
		"a.autoindex=b.expensesheet and a.project is null and b.account="+account.getIndex()+" and a.trans in " +
		"(SELECT acct.autoindex FROM transactionvalue tv, account ac, acctransaction acct WHERE " +
		"ac.autoindex = tv.account AND acct.autoindex=tv.trans AND ac.treepath LIKE '"+account.getTreePath()+"%' " +
		"AND (acct.TRANSACTIONDATE>='"+fromdate+"' AND acct.TRANSACTIONDATE<'"+todate+"') "+strunit+
		") and " +
		"a.status=3 and " + 
		"b.department=" + m_org.getIndex();
		return query;
	}
	
	public String getQueryMemjournalstrd(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "and acct.unit=" + m_unit.getIndex();
		String query="select sum(accvalue) debitvalue from memjournalstrd a, memjournalstrddet b where  " +
		"a.autoindex=b.memjournalstrd and b.account="+account.getIndex()+" and a.trans in " +
		"(SELECT acct.autoindex FROM transactionvalue tv, account ac, acctransaction acct WHERE " +
		"ac.autoindex = tv.account AND acct.autoindex=tv.trans AND ac.treepath LIKE '"+account.getTreePath()+"%' " +
		"AND (acct.TRANSACTIONDATE>='"+fromdate+"' AND acct.TRANSACTIONDATE<'"+todate+"') "+strunit+
		") and " +
		"a.status=3 and " + 
		"b.department=" + m_org.getIndex();
		return query;
	}
	
	
	public String getQueryMemjournalnonstrd(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "and acct.unit=" + m_unit.getIndex();
		String query="select sum(accvalue) debitvalue from PayrollPmtTax21Unit a, PayrollPmtTax21Unitdet b where  " +
		"a.autoindex=b.PayrollPmtTax21Unit and b.account="+account.getIndex()+" and a.trans in " +
		"(SELECT acct.autoindex FROM transactionvalue tv, account ac, acctransaction acct WHERE " +
		"ac.autoindex = tv.account AND acct.autoindex=tv.trans AND ac.treepath LIKE '"+account.getTreePath()+"%' " +
		"AND (acct.TRANSACTIONDATE>='"+fromdate+"' AND acct.TRANSACTIONDATE<'"+todate+"') "+strunit+
		") and " +
		"a.status=3 and " + 
		"b.department=" + m_org.getIndex();
		return query;
	}
	
	public String getQueryPayrolltax21unit(Account account,String fromdate,String todate){
		String strunit = "";
		if (m_unit!=null)
			strunit = "and acct.unit=" + m_unit.getIndex();
		String query="select sum(accvalue) debitvalue from memjournalnonstrd a, memjournalnonstrddet b where  " +
		"a.autoindex=b.memjournalnonstrd and a.project is null and b.account="+account.getIndex()+" and a.trans in " +
		"(SELECT acct.autoindex FROM transactionvalue tv, account ac, acctransaction acct WHERE " +
		"ac.autoindex = tv.account AND acct.autoindex=tv.trans AND ac.treepath LIKE '"+account.getTreePath()+"%' " +
		"AND (acct.TRANSACTIONDATE>='"+fromdate+"' AND acct.TRANSACTIONDATE<'"+todate+"') "+strunit+
		") and " +
		"a.status=3 and " + 
		"b.department=" + m_org.getIndex();
		return query;
	}
	
	
	public String getFirstPeriod(int month,int yearvalue){
		String areturn = "";
		if (month==12)
			areturn = "December" + " " + String.valueOf(yearvalue);
		else if (month==1)
			areturn = "January" + " " + String.valueOf(yearvalue);
		else if (month==2)
			areturn = "February" + " " + String.valueOf(yearvalue);
		else if (month==3)
			areturn = "March" + " " + String.valueOf(yearvalue);
		else if (month==4)
			areturn = "April" + " " + String.valueOf(yearvalue);
		else if (month==5)
			areturn = "May" + " " + String.valueOf(yearvalue);
		else if (month==6)
			areturn = "June" + " " + String.valueOf(yearvalue);
		else if (month==7)
			areturn = "July" + " " + String.valueOf(yearvalue);
		else if (month==8)
			areturn = "August" + " " + String.valueOf(yearvalue);
		else if (month==9)
			areturn = "September" + " " + String.valueOf(yearvalue);
		else if (month==10)
			areturn = "October" + " " + String.valueOf(yearvalue);
		else if (month==11)
			areturn = "November" + " " + String.valueOf(yearvalue);
		return areturn;
	}
	
	public JRViewer getPrintView() {
		return m_jrv;
	}
	
	public void setEmpty() {
		m_jrv.removeAll();
	}
	
	public JasperPrint getJasperPrint(){
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