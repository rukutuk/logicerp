package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodSubsidiaryLedger;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class DirectCashFlow{
	JRViewer m_jrv;
	JasperPrint m_jasperPrint;
	Connection m_conn;
	long m_sessionid;
	PeriodSubsidiaryLedger m_periodStartEnd;
	UnitPicker m_unitPicker;
	SimpleDateFormat m_dateFormat,m_yearFormat,m_dateFormat2;
	DecimalFormat m_desimalFormat; 
	boolean m_excel = false;
	
	AccountingBusinessLogic m_logic;
	String[] m_month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	String[] m_monthValue = {"01","02","03","04","05","06","07","08","09","10","11","12"};
	String[] m_received = {"Sales Advance","Account Receivable","Expense Sheet Difference","Unit Bank/Cash Transfer","Employee Receivables","Loan",
			"Cash Advance - I Owe You - Project Settlement Received","Cash Advance - I Owe You - Others Settlements Received",
	"Others (Receive Others Transaction)", "Others (Memorial Journal)", "Void"};
	String[] m_payment = {"Account payable","Project Cost","Operational Cost","Cash Advance - I Owe You - Project Installments",
			"Cash Advance - I Owe You - Others Installments","Cash Advance - I Owe You - Project Settlements",
			"Cash Advance - I Owe You - Others Settlements","Cash Advance Project","Cash Advance Others",
			"Expense Sheet Difference","Unit Bank/Cash Transfer","Employee Receivables","Loan","Salary - Head Office",
			"Salary - Unit","Tax Art 21 - Head Office","Tax Art 21 - Unit","Employee Insurance","Others (Receive Others Transaction)",
			"Others (Memorial Journal)", "Void"};
	
	int m_level = 0;
	String[] m_setTotRec = {"","","","","","","","","","","",""};// string settotrec
	String[] m_setTotPay = {"","","","","","","","","","","",""}; // string settotpay
	String[] m_surpluse = {"","","","","","","","","","","",""}; // string surplus
	
	String[] m_col = {"","","","","","","","","","","",""}; // string kolom
	String[] m_cashEquivalet = {"","","","","","","","","","","",""}; // hitung total account kas dan setara kas 
	
	double[] m_totRec = {0,0,0,0,0,0,0,0,0,0,0,0};	// hitung total Receive
	double[] m_totPay = {0,0,0,0,0,0,0,0,0,0,0,0}; // hitung tot Payment
	
	String[] m_strTotal ={"","","","","","","","","","","",""}; // untuk string total
	double[] m_total = {0,0,0,0,0,0,0,0,0,0,0,0}; // untuk hitung total
	double m_totalVal = 0; // untuk total keseluruhan
	
	public DirectCashFlow(Connection conn,long sessionid,PeriodSubsidiaryLedger periodStartEnd,UnitPicker unitPicker,boolean excel){
		m_conn = conn;
		m_sessionid = sessionid;
		m_periodStartEnd = periodStartEnd;
		m_unitPicker = unitPicker;
		m_excel = excel;
		m_logic = new AccountingBusinessLogic(m_conn);
		m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_yearFormat = new SimpleDateFormat("yyyy");		
		m_desimalFormat = new DecimalFormat("#,##0.00");		
		setNonEmpty();
	}
	
	public DirectCashFlow(Connection conn,long sessionid){
		m_conn = conn;
		m_sessionid = sessionid;
		m_logic = new AccountingBusinessLogic(m_conn);
		m_yearFormat = new SimpleDateFormat("yy");
		setEmpty();
	}
	
	private void setEmpty() {
		try {			
			String filename = "cashflow_langsung";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();	
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			parameters.put("param_period", "");		
			
			for (int i=1;i<=12;i++)
				parameters.put("param_col" + i, m_month[i-1] + " - " + m_yearFormat.format(new Date()));	
			
			header(model);
			
			model.addRow(new Object[]{"1","Received","","","","","","","","","","","","","",new Integer(1)});
			for (int i=0;i<m_received.length;i++){
				model.addRow(new Object[]{"",m_received[i],"","","","","","","","","","","","","",new Integer(2)});
			}
			model.addRow(new Object[]{"","Total Received","","","","","","","","","","","","","",new Integer(3)});
			
			model.addRow(new Object[]{"2","Payment","","","","","","","","","","","","","",new Integer(1)});
			for (int i=0;i<m_payment.length;i++){
				model.addRow(new Object[]{"",m_payment[i],"","","","","","","","","","","","","",new Integer(2)});
			}
			model.addRow(new Object[]{"","Total Paid","","","","","","","","","","","","","",new Integer(3)});
			
			model.addRow(new Object[]{"","Surplus/Defisit","","","","","","","","","","","","","",new Integer(1)});
			model.addRow(new Object[]{"","Cash and Cash Equivalents Beginning Balance","","","","","","","","","","","","","",new Integer(1)});
			model.addRow(new Object[]{"","Cash and Cash Equivalents Ending Balance","","","","","","","","","","","","","",new Integer(1)});
			model.addRow(new Object[]{"","Cash and Cash Equivalents Details:","","","","","","","","","","","","","",new Integer(1)});
			getEmptySubAccount(3,model);
			model.addRow(new Object[]{"","TOTAL","","","","","","","","","","","","","",new Integer(4)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			
			m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getEmptySubAccount(long index, DefaultTableModel model){
		try {
			Account[] subaccount = m_logic.getSubAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING, index);
			for (int i=0;i<subaccount.length;i++){
				String m_spance = "";
				for (int j=0;j<m_level;j++){
					m_spance +="       ";
				}
				if (subaccount[i].isGroup()){
					model.addRow(new Object[]{"",m_spance + subaccount[i].getName(),"","","","","","","","","","","","","",new Integer(2)});
					m_level = m_level +1;
					getEmptySubAccount(subaccount[i].getIndex(),model);
					m_level = m_level -1;
				}else{				
					model.addRow(new Object[]{"",m_spance + subaccount[i].getName(),m_cashEquivalet[0],m_cashEquivalet[1],m_cashEquivalet[2],
							m_cashEquivalet[3],m_cashEquivalet[4],m_cashEquivalet[5],m_cashEquivalet[6],m_cashEquivalet[7],
							m_cashEquivalet[8],m_cashEquivalet[9],m_cashEquivalet[10],m_cashEquivalet[11],"",new Integer(2)});
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void setNonEmpty() {
		try {			
			String filename = "cashflow_langsung";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			if(m_excel)
				m_desimalFormat = new DecimalFormat("###.00");
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			if (m_unitPicker.getUnit()!=null)
				parameters.put("param_unit_code", m_unitPicker.getUnit().toString());
			
			parameters.put("param_period", m_dateFormat.format(m_periodStartEnd.m_startDate.getDate())
					+ " - " + m_dateFormat.format(m_periodStartEnd.m_endDate.getDate()));			
			
			Date startDate = m_periodStartEnd.m_startDate.getDate();
			Date endDate = m_periodStartEnd.m_endDate.getDate();
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			
			//int startMonth = m_periodStartEnd.m_startDate.getDate().getMonth();
			int startMonth = startCal.get(Calendar.MONTH);
			//int endMonth = m_periodStartEnd.m_endDate.getDate().getMonth();
			int endMonth = endCal.get(Calendar.MONTH);
			//int startYear = Integer.parseInt(m_yearFormat.format(m_periodStartEnd.m_startDate.getDate()));//m_periodStartEnd.m_startDate.getDate().getYear();
			int startYear = startCal.get(Calendar.YEAR);
			//int endYear = Integer.parseInt(m_yearFormat.format(m_periodStartEnd.m_endDate.getDate()));//m_periodStartEnd.m_endDate.getDate().getYear();
			int endYear = endCal.get(Calendar.YEAR);
			//int startDay = m_periodStartEnd.m_startDate.getDate().getDate();
			int startDay = startCal.get(Calendar.DATE);
			//int endDay = m_periodStartEnd.m_endDate.getDate().getDate();
			int endDay = endCal.get(Calendar.DATE);
			
			int j=1;
			// untuk kolom atas
			if (startYear == endYear){
				for (int i=startMonth;i<=endMonth;i++){				
					parameters.put("param_col" + (j++), m_month[i] + " - " + m_yearFormat.format(m_periodStartEnd.m_startDate.getDate()));
				}
			}else{				
				for (int k=startMonth;k<12;k++){
					parameters.put("param_col" + (j++), m_month[k] + " - " + m_yearFormat.format(m_periodStartEnd.m_startDate.getDate()));
				}
				for (int k=0;k<=endMonth;k++){
					parameters.put("param_col" + (j++), m_month[k] + " - " + m_yearFormat.format(m_periodStartEnd.m_endDate.getDate()));
				}
			}			
			header(model);
			
			VariableAccountSetting account = 
				VariableAccountSetting.createVariableAccountSetting(m_conn, m_sessionid, IConstants.ATTR_VARS_CASH_AND_CASH_EQUIVALENTS);
			
			// received
			model.addRow(new Object[]{"1","Received","","","","","","","","","","","","","",new Integer(1)});
			for (int i=0;i<m_received.length;i++){
				String application = "";
				if (i==0)
					application = "application='"+ IConstants.SALES_ADVANCE +"'";
				else if (i==1)
					application = "application='"+ IConstants.SALES_AR_RECEIVE +"'";
				if (i==2)
					application = "application='" + IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE + "'";
				else if (i==3)
					application = "(application='" + IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER + "' OR application='" + 
					IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER+ "')";
				else if (i==4)
					application = "application='" + IConstants.RECEIVE_EMPLOYEE_RECEIVABLE + "'";
				else if (i==5)
					application = "application='" + IConstants.RECEIVE_LOAN + "'";
				else if (i==6)					
					application = "application='" + IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT + "' AND " + IDBConstants.ATTR_ATTRIBUTE + 
					"='"+IConstants.ATTR_CA_SETTLED_REC+"'";
				else if (i==7)					
					application = "application='" + IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT + "' AND " + IDBConstants.ATTR_ATTRIBUTE + 
					"='"+IConstants.ATTR_CA_SETTLED_REC+"'";
				else if (i==8)
					application = "application='" + IConstants.RECEIVE_OTHERS + "'";
				else if (i==9)
					application = "(application='" + IConstants.MJ_NONSTANDARD_OTHERS+ "' OR application='" + IConstants.MJ_NONSTANDARD_PROJECT + "' OR application='" + IConstants.MJ_STANDARD+ "')";
				else if (i==10)
					application = "application='" + IConstants.VOID_TRANSACTION + "'";
				
				double totRec = 0;
				if (startYear == endYear){
					int l=0;
					for (int k=startMonth;k<=endMonth;k++){
						String query ="";
						if (k==startMonth)
							query = getQueryFirstReceived(application, account.getAccount().getTreePath(),startYear,k);
						else if (k==endMonth)
							query = getQueryLastReceived(application, account.getAccount().getTreePath(),startYear,k);
						else
							query = getQueryReceived(application, account.getAccount().getTreePath(),startYear,k);
						
						double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
						
						totRec+=debitValue;
						
						m_col[l] = m_desimalFormat.format(debitValue);					
						if (debitValue<0)
							if(m_excel)
								m_col[l] = m_desimalFormat.format(-debitValue);
							else m_col[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
						
						m_totRec[l]+=debitValue;
						m_setTotRec[l] = m_desimalFormat.format(m_totRec[l]);
						if (m_totRec[l]<0)
							if(m_excel)
								m_setTotRec[l] = m_desimalFormat.format(-m_totRec[l]);
							else m_setTotRec[l] = "(" + m_desimalFormat.format(-m_totRec[l]) + ")";
						
						l++;
					}
				}
				else{		
					int l=0;
					for (int k=startMonth;k<12;k++){
						String query ="";
						if (k==startMonth)
							query = getQueryFirstReceived(application, account.getAccount().getTreePath(),startYear,k);
						else if (k==11)
							query = getQueryLastReceived(application, account.getAccount().getTreePath(),startYear,k);
						else
							query = getQueryReceived(application, account.getAccount().getTreePath(),startYear,k);
						
						double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
						
						totRec+=debitValue;
						
						m_col[l] = m_desimalFormat.format(debitValue);
						if (debitValue<0)
							if(m_excel)
								m_col[l] = m_desimalFormat.format(-debitValue);
							else m_col[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
						
						m_totRec[l]+=debitValue;
						m_setTotRec[l] = m_desimalFormat.format(m_totRec[l]);
						if (m_totRec[l]<0)
							if(m_excel)
								m_setTotRec[l] = m_desimalFormat.format(-m_totRec[l]);
							else m_setTotRec[l] = "(" + m_desimalFormat.format(-m_totRec[l]) + ")";
						l++;
					}
					for (int k=0;k<=endMonth;k++){
						String query ="";
						if (k==0)
							query = getQueryFirstReceived(application, account.getAccount().getTreePath(),endYear,k);
						else if (k==endMonth)
							query = getQueryLastReceived(application, account.getAccount().getTreePath(),endYear,k);
						else
							query = getQueryReceived(application, account.getAccount().getTreePath(),endYear,k);
						
						double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
						
						totRec+=debitValue;
						
						m_col[l] = m_desimalFormat.format(debitValue);						
						if (debitValue<0)
							if(m_excel)
								m_col[l] = m_desimalFormat.format(-debitValue);
							else m_col[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
						m_totRec[l]+=debitValue;
						m_setTotRec[l] = m_desimalFormat.format(m_totRec[l]);
						if (m_totRec[l]<0)
							if(m_excel)
								m_setTotRec[l] = m_desimalFormat.format(-m_totRec[l]);	
							else m_setTotRec[l] = "(" + m_desimalFormat.format(-m_totRec[l]) + ")";
						l++;
					}
				}
				
				String strTotRec = m_desimalFormat.format(totRec);
				if (totRec<0){
					strTotRec = "(" + m_desimalFormat.format(-totRec) + ")";
					if(m_excel)
						strTotRec = m_desimalFormat.format(-totRec);
				}
				model.addRow(new Object[]{"",m_received[i],m_col[0],m_col[1],m_col[2],m_col[3],m_col[4],m_col[5],m_col[6],
						m_col[7],m_col[8],m_col[9],m_col[10],m_col[11],strTotRec,new Integer(2)});
			}
			double getTotRec = 0;
			for (int l=0;l<m_totRec.length;l++)
				getTotRec +=m_totRec[l];	
			String strGetTotRec = m_desimalFormat.format(getTotRec);
			if (getTotRec<0){
				strGetTotRec = "(" + m_desimalFormat.format(-getTotRec) +")";
				if(m_excel)
					strGetTotRec = m_desimalFormat.format(-getTotRec);
			}
			model.addRow(new Object[]{"","Total Received",m_setTotRec[0],m_setTotRec[1],m_setTotRec[2],m_setTotRec[3],
					m_setTotRec[4],m_setTotRec[5],m_setTotRec[6],m_setTotRec[7],m_setTotRec[8],m_setTotRec[9],m_setTotRec[10],
					m_setTotRec[11],strGetTotRec,new Integer(3)});
			
			// payment 
			model.addRow(new Object[]{"2","Payment","","","","","","","","","","","","","",new Integer(1)});			
			for (int i=0;i<m_payment.length;i++){				
				GenericMapper mapper=MasterMap.obtainMapperFor(JournalStandardSetting.class);
				mapper.setActiveConn(m_conn);
				String application = "";
				if (i==0)										
					application ="application='" + IConstants.PURCHASE_AP_PAYMENT + "'";
				else if (i==1)										
					application ="application='" + IConstants.PAYMENT_PROJECT_COST + "'";
				else if (i==2)
					application ="application='" + IConstants.PAYMENT_OPERASIONAL_COST + "'";
				else if (i==3)					
					application ="application='" + IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT + "'";
				else if (i==4)						
					application ="application='" + IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT + "'";
				else if (i==5)					
					application ="application='" + IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT + "' AND " + IDBConstants.ATTR_ATTRIBUTE + "=' "
						+ IConstants.ATTR_CA_SETTLED_REC + "'";
				else if (i==6)					
					application ="application='" + IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT + "' AND " + IDBConstants.ATTR_ATTRIBUTE + "=' "
					+ IConstants.ATTR_CA_SETTLED_REC + "'";
				else if (i==7)					
					application ="application='" + IConstants.PAYMENT_CASHADVANCE_PROJECT + "'";				
				else if (i==8)					
					application ="application='" + IConstants.PAYMENT_CASHADVANCE_OTHERS + "'";				
				else if (i==9)					
					application ="application='" + IConstants.PAYMENT_EXPENSESHEET_DIFFERENCE + "'";				
				else if (i==10)					
					application ="application='" + IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER + "'";				
				else if (i==11)
					application ="application='" + IConstants.PAYMENT_EMPLOYEE_RECEIVABLE + "'";			
				else if (i==12)					
					application ="application='" + IConstants.PAYMENT_LOAN + "'";
				else if (i==13)
					application ="application='" + IConstants.PAYROLL_PAYMENT_SALARY_HO + "'";
				else if (i==14)
					application ="application='" + IConstants.PAYROLL_PAYMENT_SALARY_UNIT + "'";
				else if (i==15)
					application ="application='" + IConstants.PAYROLL_PAYMENT_TAX21_HO + "'";
				else if (i==16)
					application ="application='" + IConstants.PAYROLL_PAYMENT_TAX21_UNIT + "'";
				else if (i==17)
					application ="application='" + IConstants.PAYROLL_PAYMENT_EMPLOYEE_INSURANCE + "'";
				else if (i==18)
					application ="application='" + IConstants.PAYMENT_OTHERS + "'";
				else if (i==19)
					application = "(application='" + IConstants.MJ_NONSTANDARD_OTHERS+ "' OR application='" + IConstants.MJ_NONSTANDARD_PROJECT + "' OR application='" + IConstants.MJ_STANDARD+ "')";				
				else if (i==20)
					application = "application='" + IConstants.VOID_TRANSACTION + "'";
				
				double totPay = 0;
				if (startYear == endYear){
					int l=0;
					for (int k=startMonth;k<=endMonth;k++){
						
						String query ="";
						if (k==startMonth)
							query = getQueryFirstPayment(application, account.getAccount().getTreePath(),startYear,k);						
						else if (k==endMonth)
							query = getQueryLastPayment(application, account.getAccount().getTreePath(),startYear,k);
						else
							query = getQueryPayment(application, account.getAccount().getTreePath(),startYear,k);
						
						//System.err.println(query);
						
						double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
						
						totPay+=debitValue;
						
						m_col[l] = m_desimalFormat.format(debitValue);					
						if (debitValue<0){
							m_col[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
							if(m_excel)
								m_col[l] = m_desimalFormat.format(-debitValue);
						}
						m_totPay[l]+=debitValue;
						m_setTotPay[l] = m_desimalFormat.format(m_totPay[l]);
						if (m_totPay[l]<0){
							m_setTotPay[l] = "(" + m_desimalFormat.format(-m_totPay[l]) + ")";
							if(m_excel)
								m_setTotPay[l] = m_desimalFormat.format(-m_totPay[l]);
						}
						l++;				
					}
				}
				else{
					int l=0;
					for (int k=startMonth;k<12;k++){
						String query = getQueryPayment(application,account.getAccount().getTreePath(), startYear,k);
						
						double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
						
						totPay+=debitValue;
						
						m_col[l] = m_desimalFormat.format(debitValue);					
						if (debitValue<0){
							m_col[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
							if(m_excel)
								m_col[l] = m_desimalFormat.format(-debitValue);
						}
						m_totPay[l]+=debitValue;
						m_setTotPay[l] = m_desimalFormat.format(m_totPay[l]);
						if (m_totPay[l]<0){
							m_setTotPay[l] = "(" + m_desimalFormat.format(-m_totPay[l]) + ")";
							if(m_excel)
								m_setTotPay[l] = m_desimalFormat.format(-m_totPay[l]);
						}
						l++;
					}
					for (int k=0;k<=endMonth;k++){
						String query = getQueryPayment(application, account.getAccount().getTreePath(),endYear,k);
						
						double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
						
						totPay+=debitValue;
						
						m_col[l] = m_desimalFormat.format(debitValue);					
						if (debitValue<0){
							m_col[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
							if(m_excel)
								m_col[l] = m_desimalFormat.format(-debitValue);
						}
						m_totPay[l]+=debitValue;
						m_setTotPay[l] = m_desimalFormat.format(m_totPay[l]);
						if (m_totPay[l]<0){
							m_setTotPay[l] = "(" + m_desimalFormat.format(-m_totPay[l]) + ")";
							if(m_excel)
								m_setTotPay[l] = m_desimalFormat.format(-m_totPay[l]);
						}
						l++;
					}
				}
				
				String strTotPay = m_desimalFormat.format(totPay);
				if (totPay<0){
					strTotPay = "(" + m_desimalFormat.format(-totPay) + ")";
					if(m_excel)
						strTotPay = m_desimalFormat.format(-totPay);
				}
				model.addRow(new Object[]{"",m_payment[i],m_col[0],m_col[1],m_col[2],m_col[3],m_col[4],m_col[5],m_col[6],
						m_col[7],m_col[8],m_col[9],m_col[10],m_col[11],strTotPay,new Integer(2)});
			}
			
			double getTotPay = 0;			
			for (int l=0;l<m_totPay.length;l++)
				getTotPay +=m_totPay[l];			
			
			String strgetTotPay = m_desimalFormat.format(getTotPay);
			if (getTotPay<0){
				strgetTotPay = "(" + m_desimalFormat.format(-getTotPay) + ")";	
				if(m_excel)
					strgetTotPay = m_desimalFormat.format(-getTotPay);	
			}
			model.addRow(new Object[]{"","Total Paid",m_setTotPay[0],m_setTotPay[1],m_setTotPay[2],m_setTotPay[3],
					m_setTotPay[4],m_setTotPay[5],m_setTotPay[6],m_setTotPay[7],m_setTotPay[8],m_setTotPay[9],m_setTotPay[10],
					m_setTotPay[11],strgetTotPay,new Integer(3)});
			
			// surplus
			double getTotSurplus = 0;
			for (int l=0;l<m_totPay.length;l++){
				getTotSurplus += (m_totPay[l]+m_totRec[l]);			
			}
			String strgetTotSurplus = m_desimalFormat.format(getTotSurplus);
			if (getTotSurplus<0){
				strgetTotSurplus = "(" + m_desimalFormat.format(-getTotSurplus) + ")";			
				if(m_excel)
					strgetTotSurplus = m_desimalFormat.format(-getTotSurplus);
			}
			getSurplus();
			
			model.addRow(new Object[]{"","Surplus/Defisit",m_surpluse[0],m_surpluse[1],m_surpluse[2],m_surpluse[3],m_surpluse[4],
					m_surpluse[5],m_surpluse[6],m_surpluse[7],m_surpluse[8],m_surpluse[9],m_surpluse[10],m_surpluse[11],
					strgetTotSurplus,new Integer(1)});
			
			// bagining balance dan ending balance
			String[] strBeginBalance ={"","","","","","","","","","","",""}; 
			String[] strEndBalance ={"","","","","","","","","","","",""};
			double debitValueBegin = 0;			
			double debitValueEnd = 0;
			if (startYear == endYear){
				int l=0;
				for (int k=startMonth;k<=endMonth;k++){					
					String queryTransDateBegin = "";
					String queryTransDateEnding = "";
					
					
					if (k==startMonth){
						queryTransDateBegin = " AND tp.transactiondate <'" + startYear + "-" + 
						m_monthValue[k] + "-" + startDay + "'";
					
						if (k<11){
							if (k==endMonth)
								queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
								m_monthValue[k] + "-" + endDay + "'";
							else
								queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
								m_monthValue[k+1] + "-1'";
						}else{
							queryTransDateEnding = " AND tp.transactiondate <='" + (startYear+1) + "-" + 
							m_monthValue[k] + "-" + endDay + "'";
						}							
					}else{
						queryTransDateBegin = " AND tp.transactiondate <'" + startYear + "-" + 
						m_monthValue[k] + "-1'";						
						if (k<11){
							if (k==endMonth)
								queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
								m_monthValue[k] + "-" + endDay + "'";
							else
								queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
								m_monthValue[k+1] + "-1'";
						}else{
							queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
							m_monthValue[k] + "-" + endDay + "'";
							
						}
					}
					
					String queryBegin = getQueryBalance(account.getAccount().getTreePath(), queryTransDateBegin);					
					String queryEnd = getQueryBalance(account.getAccount().getTreePath(), queryTransDateEnding);
					
					debitValueBegin = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryBegin);
					debitValueEnd = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryEnd);				
					
					
					strBeginBalance[l]=m_desimalFormat.format(debitValueBegin);
					if (debitValueBegin<0){
						strBeginBalance[l]="(" + m_desimalFormat.format(-debitValueBegin) + ")";
						if(m_excel)
							strBeginBalance[l]=m_desimalFormat.format(-debitValueBegin);
					}
					strEndBalance[l] = m_desimalFormat.format(debitValueEnd);
					if (debitValueEnd<0){
						strEndBalance[l]="(" + m_desimalFormat.format(-debitValueEnd) + ")";
						if(m_excel)
							strEndBalance[l]=m_desimalFormat.format(-debitValueEnd);
					}
					l++;
				}
			}else{
				int l=0;
				for (int k=startMonth;k<12;k++){
					String queryTransDateBegin = "";
					String queryTransDateEnding = "";
					
					if (k==startMonth){
						queryTransDateBegin = " AND tp.transactiondate <'" + startYear + "-" + 
						m_monthValue[k] + "-" + startDay + "'";
						
						if (k<11){
							if (k==endMonth)
								queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
								m_monthValue[k] + "-" + endDay + "'";
							else
								queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
								m_monthValue[k+1] + "-1'";
						}else{
							if (k==endMonth)
								queryTransDateEnding = " AND tp.transactiondate <='" + (startYear+1) + "-" + 
								m_monthValue[k] + "-" + endDay + "'";
							else
								queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
								m_monthValue[k] + "-1'";
						}
					}else{
						queryTransDateBegin = " AND tp.transactiondate <'" + startYear + "-" + 
						m_monthValue[k] + "-1'";
						
						if (k<11){
							if (k==endMonth)
								queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
								m_monthValue[k] + "-" + endDay + "'";
							else
								queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
								m_monthValue[k+1] + "-1'";
						}else{
							if (k==endMonth)
								queryTransDateEnding = " AND tp.transactiondate <='" + (startYear+1) + "-" + 
								m_monthValue[k] + "-" + endDay + "'";
							else
								queryTransDateEnding = " AND tp.transactiondate <'" + (startYear+1) + "-" + 
								m_monthValue[k] + "-1'";
						}	
						
					}
					
					String queryBegin = getQueryBalance(account.getAccount().getTreePath(),queryTransDateBegin);
					String queryEnd = getQueryBalance(account.getAccount().getTreePath(), queryTransDateEnding);
					
					//System.err.println("2 " + queryBegin);
															
					debitValueBegin = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryBegin);
					debitValueEnd = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryEnd);
					
					strBeginBalance[l]=m_desimalFormat.format(debitValueBegin);
					if (debitValueBegin<0){
						strBeginBalance[l]="(" + m_desimalFormat.format(-debitValueBegin) + ")";	
						if(m_excel)
							strBeginBalance[l]=m_desimalFormat.format(-debitValueBegin);	
					}
					strEndBalance[l] = m_desimalFormat.format(debitValueEnd);
					if (debitValueEnd<0){
						strEndBalance[l]="(" + m_desimalFormat.format(-debitValueEnd) + ")";	
						if(m_excel)
							strEndBalance[l]=m_desimalFormat.format(-debitValueEnd);
					}
					l++;
				}
				for (int k=0;k<=endMonth;k++){
					
					String queryTransDateBegin = " AND tp.transactiondate <'" + endYear + "-" + 
					m_monthValue[k] + "-1'";
					String queryTransDateEnding = "";
					if (k<11){
						if (k==endMonth)
							queryTransDateEnding = " AND tp.transactiondate <='" + endYear + "-" + 
							m_monthValue[k] + "-" + endDay + "'";
						else
							queryTransDateEnding = " AND tp.transactiondate <'" + endYear + "-" + 
							m_monthValue[k+1] + "-1'";
					}else{
						if (k==endMonth)
							queryTransDateEnding = " AND tp.transactiondate <='" + endYear + "-" + 
							m_monthValue[k] + "-" + endDay + "'";
						else
							queryTransDateEnding = " AND tp.transactiondate <='" + endYear + "-" + 
							m_monthValue[k] + "-1'";
					}
					
					String queryBegin = getQueryBalance(account.getAccount().getTreePath(),queryTransDateBegin);
					String queryEnd = getQueryBalance(account.getAccount().getTreePath(), queryTransDateEnding);
					
					//System.err.println("3 " + queryBegin);
										
					debitValueBegin = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryBegin);
					debitValueEnd = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryEnd);
					
					strBeginBalance[l]=m_desimalFormat.format(debitValueBegin);					
					if (debitValueBegin<0){
						strBeginBalance[l]="(" + m_desimalFormat.format(-debitValueBegin) + ")";
						if(m_excel)
							strBeginBalance[l] = m_desimalFormat.format(-debitValueBegin);	
					}
					strEndBalance[l] = m_desimalFormat.format(debitValueEnd);
					if (debitValueEnd<0){
						strEndBalance[l]="(" + m_desimalFormat.format(-debitValueEnd) + ")";
						if(m_excel)
							strEndBalance[l]=m_desimalFormat.format(-debitValueEnd);
					}
					l++;
				}				
			}
			
			
			String queryTransDateTotBegin = " AND tp.transactiondate <'" + m_yearFormat.format(m_periodStartEnd.m_startDate.getDate()) + "-" + 
			m_monthValue[startMonth] + "-" + startDay + "'";
			String queryTotBegin = getQueryBalance(account.getAccount().getTreePath(), queryTransDateTotBegin);
			double debitTotValueBegin= m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryTotBegin);
			String totbegin = m_desimalFormat.format(debitTotValueBegin);
			if (debitTotValueBegin<0){
				totbegin = "(" + m_desimalFormat.format(-debitTotValueBegin) + ")";
				if(m_excel)
					totbegin = m_desimalFormat.format(-debitTotValueBegin);
			}
			
			model.addRow(new Object[]{"","Cash and Cash Equivalents Beginning Balance",strBeginBalance[0],strBeginBalance[1],
					strBeginBalance[2],strBeginBalance[3],strBeginBalance[4],strBeginBalance[5],strBeginBalance[6],strBeginBalance[7],
					strBeginBalance[8],strBeginBalance[9],strBeginBalance[10],strBeginBalance[11],totbegin,
					new Integer(1)});
			model.addRow(new Object[]{"","Cash and Cash Equivalents Ending Balance",strEndBalance[0],strEndBalance[1],
					strEndBalance[2],strEndBalance[3],strEndBalance[4],strEndBalance[5],strEndBalance[6],strEndBalance[7],
					strEndBalance[8],strEndBalance[9],strEndBalance[10],strEndBalance[11],m_desimalFormat.format(debitValueEnd),new Integer(1)});
			model.addRow(new Object[]{"","Cash and Cash Equivalents Details:","","","","","","","","","","","","","",new Integer(1)});
			
			// cash and cash equivalent
			VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(m_conn, m_sessionid, IConstants.ATTR_VARS_CASH_AND_CASH_EQUIVALENTS);
			Account cashEquivalent = vas.getAccount();
			getSubAccount(cashEquivalent.getIndex(),model);
			String strTotalVal = m_desimalFormat.format(m_totalVal);
			if (m_totalVal<0){
				strTotalVal = "(" + m_desimalFormat.format(-m_totalVal) +")";
				if(m_excel)
					strTotalVal = m_desimalFormat.format(-m_totalVal);
			}
			model.addRow(new Object[]{"","TOTAL",m_strTotal[0],m_strTotal[1],m_strTotal[2],m_strTotal[3],m_strTotal[4],m_strTotal[5],
					m_strTotal[6],m_strTotal[7],m_strTotal[8],m_strTotal[9],m_strTotal[10],m_strTotal[11],strTotalVal,
					new Integer(4)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			m_jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			
			m_jrv = new JRViewer(m_jasperPrint);
			if(m_excel)
				exportToExcel(m_jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getSubAccount(long index, DefaultTableModel model){
		try {
			Account[] subaccount = m_logic.getSubAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING, index);
			for (int i=0;i<subaccount.length;i++){
				String m_spance = "";
				for (int j=0;j<m_level;j++){
					m_spance +="       ";
				}
				if (subaccount[i].isGroup()){
					model.addRow(new Object[]{"",m_spance + subaccount[i].getName(),"","","","","","","","","","","","","",new Integer(2)});
					m_level = m_level +1;
					getSubAccount(subaccount[i].getIndex(),model);
					m_level = m_level -1;
				}else{
					Calendar startCal = Calendar.getInstance();
					startCal.setTime(m_periodStartEnd.m_startDate.getDate());
					Calendar endCal = Calendar.getInstance();
					endCal.setTime(m_periodStartEnd.m_endDate.getDate());
					
					
					//int startMonth = m_periodStartEnd.m_startDate.getDate().getMonth();
					int startMonth = startCal.get(Calendar.MONTH);
					//int endMonth = m_periodStartEnd.m_endDate.getDate().getMonth();
					int endMonth = endCal.get(Calendar.MONTH);
					//int startYear = Integer.parseInt(m_yearFormat.format(m_periodStartEnd.m_startDate.getDate()));//m_periodStartEnd.m_startDate.getDate().getYear();
					int startYear = startCal.get(Calendar.YEAR);
					//int endYear = Integer.parseInt(m_yearFormat.format(m_periodStartEnd.m_endDate.getDate()));//m_periodStartEnd.m_endDate.getDate().getYear();
					int endYear = endCal.get(Calendar.YEAR);
					//int endDay = m_periodStartEnd.m_endDate.getDate().getDate();
					int endDay = endCal.get(Calendar.DATE);
					
					double totCashEquivalent = 0;
					if (startYear == endYear){					
						int l =0;
						
						for (int k=startMonth;k<=endMonth;k++){
							String queryTransDateEnding = "";
							
							if (k==startMonth){	
								if (k<11){
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
										m_monthValue[k+1] + "-1'";
								}else{
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + (startYear+1) + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
										m_monthValue[k] + "-1'";
								}
							}else{
								if (k<11){
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
										m_monthValue[k+1] + "-1'";
								}else{
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + (startYear+1) + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + (startYear+1) + "-" + 
										m_monthValue[k] + "-1'";
								}	
								
							}
							
							//String query = getQueryAccount(subaccount[i],startYear, k);
							String query = getQueryBalance(subaccount[i].getTreePath(), queryTransDateEnding);
							double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
							totCashEquivalent =debitValue;							
							m_cashEquivalet[l] = m_desimalFormat.format(debitValue);
							if (debitValue<0){
								m_cashEquivalet[l] = "(" + m_desimalFormat.format(-debitValue) + ")";							
								if(m_excel)
									m_cashEquivalet[l] = m_desimalFormat.format(-debitValue);
							}
							m_total[l] +=debitValue;							
							m_strTotal[l] = m_desimalFormat.format(m_total[l]);
							if (m_total[l]<0){
								m_strTotal[l] = "(" + m_desimalFormat.format(-m_total[l]) + ")";
								if(m_excel)
									m_strTotal[l] = m_desimalFormat.format(-m_total[l]);
							}
							m_totalVal = m_total[l];							
							l++;							
						}
					}else{
						int l=0;						
						for (int k=startMonth;k<12;k++){
							String queryTransDateEnding = "";
							
							if (k==startMonth){	
								if (k<11){
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
										m_monthValue[k+1] + "-1'";
								}else{
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + (startYear+1) + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
										m_monthValue[k] + "-1'";
								}
							}else{
								if (k<11){
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + startYear + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + startYear + "-" + 
										m_monthValue[k+1] + "-1'";
								}else{
									if (k==endMonth)
										queryTransDateEnding = " AND tp.transactiondate <='" + (startYear+1) + "-" + 
										m_monthValue[k] + "-" + endDay + "'";
									else
										queryTransDateEnding = " AND tp.transactiondate <'" + (startYear+1) + "-" + 
										m_monthValue[k] + "-1'";
								}	
								
							}
							
							//String query = getQueryAccount(subaccount[i],startYear, k);
							String query = getQueryBalance(subaccount[i].getTreePath(), queryTransDateEnding);
							double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
							totCashEquivalent =debitValue;
							m_cashEquivalet[l] = m_desimalFormat.format(debitValue);
							if (debitValue<0)
								if(m_excel)
									m_cashEquivalet[l] = m_desimalFormat.format(-debitValue);
								else m_cashEquivalet[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
							m_total[l] =debitValue;
							m_strTotal[l] = m_desimalFormat.format(m_total[l]);
							if (m_total[l]<0)
								if(m_excel)
									m_strTotal[l] = m_desimalFormat.format(-m_total[l]);
								else m_strTotal[l] = "(" + m_desimalFormat.format(-m_total[l]) + ")";
							m_totalVal = m_total[l];
							l++;
						}
						for (int k=0;k<=endMonth;k++){
							String query = getQueryAccount(subaccount[i],endYear, k);
							double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
							totCashEquivalent =debitValue;
							m_cashEquivalet[l] = m_desimalFormat.format(debitValue);
							if (debitValue<0)
								if(m_excel)
									m_cashEquivalet[l] = m_desimalFormat.format(-debitValue);
								else m_cashEquivalet[l] = "(" + m_desimalFormat.format(-debitValue) + ")";
							
							m_total[l] =debitValue;
							m_strTotal[l] = m_desimalFormat.format(m_total[l]);
							if (m_total[l]<0)
								if(m_excel)
									m_strTotal[l] = m_desimalFormat.format(-m_total[l]);
								else m_strTotal[l] = "(" + m_desimalFormat.format(-m_total[l]) + ")";
							
							m_totalVal = m_total[l];
							l++;
						}
					}
					String strTotal = m_desimalFormat.format(totCashEquivalent);
					if (totCashEquivalent <0)
						if(m_excel)
							strTotal = m_desimalFormat.format(-totCashEquivalent);
						else strTotal = "("+m_desimalFormat.format(-totCashEquivalent)+")";
					model.addRow(new Object[]{"",m_spance + subaccount[i].getName(),m_cashEquivalet[0],m_cashEquivalet[1],m_cashEquivalet[2],
							m_cashEquivalet[3],m_cashEquivalet[4],m_cashEquivalet[5],m_cashEquivalet[6],m_cashEquivalet[7],
							m_cashEquivalet[8],m_cashEquivalet[9],m_cashEquivalet[10],m_cashEquivalet[11],strTotal,new Integer(2)});
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void getSurplus() {
		double surplus=0;
		if (!m_setTotPay[0].equals("")){				
			surplus = m_totPay[0]+m_totRec[0];
			m_surpluse[0] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[0] = m_desimalFormat.format(-surplus);
				else m_surpluse[0] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[1].equals("")){				
			surplus = m_totPay[1]+m_totRec[1];
			m_surpluse[1] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[1] = m_desimalFormat.format(-surplus);
				else m_surpluse[1] = "(" + m_desimalFormat.format(-surplus) + ")";
		}		
		if (!m_setTotPay[2].equals("")){				
			surplus = m_totPay[2]+m_totRec[2];
			m_surpluse[2] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[2] = m_desimalFormat.format(-surplus);
				else m_surpluse[2] = "(" + m_desimalFormat.format(-surplus) + ")";
		}		
		if (!m_setTotPay[3].equals("")){				
			surplus = m_totPay[3]+m_totRec[3];
			m_surpluse[3] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[3] = m_desimalFormat.format(-surplus);
				else m_surpluse[3] = "(" + m_desimalFormat.format(-surplus) + ")";
		}		
		if (!m_setTotPay[4].equals("")){				
			surplus = m_totPay[4]+m_totRec[4];
			m_surpluse[4] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[4] = m_desimalFormat.format(-surplus);
				else m_surpluse[4] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[5].equals("")){				
			surplus = m_totPay[5]+m_totRec[5];
			m_surpluse[5] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[5] = m_desimalFormat.format(-surplus);
				else m_surpluse[5] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[6].equals("")){				
			surplus = m_totPay[6]+m_totRec[6];
			m_surpluse[6] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[6] = m_desimalFormat.format(-surplus);
				else m_surpluse[6] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[7].equals("")){				
			surplus = m_totPay[7]+m_totRec[7];
			m_surpluse[7] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[7] = m_desimalFormat.format(-surplus);
				else m_surpluse[7] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[8].equals("")){				
			surplus = m_totPay[8]+m_totRec[8];
			m_surpluse[8] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[8] = m_desimalFormat.format(-surplus);
				else m_surpluse[8] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[9].equals("")){				
			surplus = m_totPay[9]+m_totRec[9];
			m_surpluse[9] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[9] = m_desimalFormat.format(-surplus);
				else m_surpluse[9] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[10].equals("")){				
			surplus = m_totPay[10]+m_totRec[10];
			m_surpluse[10] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[10] = m_desimalFormat.format(-surplus);
				else m_surpluse[10] = "(" + m_desimalFormat.format(-surplus) + ")";
		}
		if (!m_setTotPay[11].equals("")){				
			surplus = m_totPay[11]+m_totRec[11];
			m_surpluse[11] = m_desimalFormat.format(surplus);
			if (surplus<0)
				if(m_excel)
					m_surpluse[11] = m_desimalFormat.format(-surplus);
				else m_surpluse[11] = "(" + m_desimalFormat.format(-surplus) + ")";
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
		model.addColumn("field13");	
		model.addColumn("field14");
		model.addColumn("field15");
		model.addColumn("status");
	}
	
	public JRViewer getPrintView(){
		return m_jrv;
	}
	
	private String getQueryFirstReceived(String application,String treePath,int year, int k) {
		String strUnit = " ";
		if (m_unitPicker.getUnit()!=null)
			strUnit =  " AND tp.unit=" + m_unitPicker.getUnit().getIndex();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(m_periodStartEnd.m_startDate.getDate());
		
		//int startDay = m_periodStartEnd.m_startDate.getDate().getDate();
		int startDay = startCal.get(Calendar.DATE);
		
		String queryTransDate ="";		
		if (k<11)
			queryTransDate = "tp.transactiondate >='" + year + "-" + 
			m_monthValue[k] + "-" + startDay+ "' AND tp.transactiondate<'" + year + "-" + 
			m_monthValue[k+1] + "-1'";
		else
			queryTransDate = "tp.transactiondate >='" + year + "-" + 
			m_monthValue[k] + "-" + startDay+ "' AND tp.transactiondate<'" + (year+1) + 
			"-01-01'";
		
		/*String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND tv.accvalue>0";*/
		
		String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND (tv.accvalue*tv.exchangerate)>0";
		return query;
	}
	
	private String getQueryReceived(String application,String treePath, int year,int k) {
		String strUnit = " ";
		if (m_unitPicker.getUnit()!=null)
			strUnit =  " AND tp.unit=" + m_unitPicker.getUnit().getIndex();
		String queryTransDate ="";		
		if (k<11)
			queryTransDate = "tp.transactiondate >='" + year + "-" + 
			m_monthValue[k] + "-1' AND tp.transactiondate<'" + year + "-" + 
			m_monthValue[k+1] + "-1'";
		else
			queryTransDate = "tp.transactiondate >='" + year + "-" +  
			m_monthValue[k] + "-1' AND tp.transactiondate<'" + (year+1) + 
			"-01-01'";
		
		/*String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit +") AND tv.accvalue>0";*/
		
		String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit +") AND (tv.accvalue*tv.exchangerate)>0";
		return query;
	}
	
	private String getQueryLastReceived(String application,String treePath, int year,int k) {
		String strUnit = " ";
		if (m_unitPicker.getUnit()!=null)
			strUnit =  " AND tp.unit=" + m_unitPicker.getUnit().getIndex();
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(m_periodStartEnd.m_endDate.getDate());
		
		//int endDay = m_periodStartEnd.m_endDate.getDate().getDate();		
		int endDay = endCal.get(Calendar.DATE);
		
		String queryTransDate = "tp.transactiondate >='" + year + "-" + 
		m_monthValue[k] + "-1' AND tp.transactiondate<='" + year + "-" + 
		m_monthValue[k] + "-" + endDay + "'";
		
	/*	String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND tv.accvalue>0";	*/
		
		String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND (tv.accvalue*tv.exchangerate)>0";	
		return query;
	}
	
	private String getQueryFirstPayment(String application,String treePath,int year, int k) {
		String strUnit = " ";
		if (m_unitPicker.getUnit()!=null)
			strUnit =  " AND tp.unit=" + m_unitPicker.getUnit().getIndex();
		String queryTransDate ="";
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(m_periodStartEnd.m_startDate.getDate());
		
		//int startDay = m_periodStartEnd.m_startDate.getDate().getDate();
		int startDay = startCal.get(Calendar.DATE);
		
		if (k<11)
			queryTransDate = "tp.transactiondate >='" + year + "-" + 
			m_monthValue[k] + "-" + startDay+ "' AND tp.transactiondate<'" + year + "-" + 
			m_monthValue[k+1] + "-1'";
		else
			queryTransDate = "tp.transactiondate >='" + year + "-" + 
			m_monthValue[k] + "-" + startDay+ "' AND tp.transactiondate<'" + (year+1) + 
			"-01-01'";

		
		/*String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit+ ") AND tv.accvalue<0";*/
		
		String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit+ ") AND (tv.accvalue*tv.exchangerate)<0";
		return query;
	}
	
	private String getQueryPayment(String application,String treePath, int year,int k) {
		String strUnit = " ";
		if (m_unitPicker.getUnit()!=null)
			strUnit =  " AND tp.unit=" + m_unitPicker.getUnit().getIndex();
		String queryTransDate ="";		
		if (k<11)
			queryTransDate = "tp.transactiondate >='" + year + "-" + 
			m_monthValue[k] + "-1' AND tp.transactiondate<'" + year + "-" + 
			m_monthValue[k+1] + "-1'";
		else
			queryTransDate = "tp.transactiondate >='" + year + "-" + 
			m_monthValue[k] + "-1' AND tp.transactiondate<'" + (year+1) + 
			"-01-01'";

		/*String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND tv.accvalue<0";*/
		
		String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND (tv.accvalue*tv.exchangerate)<0";
		return query;
	}
	
	private String getQueryLastPayment(String application,String treePath,int year, int k) {
		String strUnit = " ";
		if (m_unitPicker.getUnit()!=null)
			strUnit =  " AND tp.unit=" + m_unitPicker.getUnit().getIndex();
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(m_periodStartEnd.m_endDate.getDate());
		
		//int startDay = m_periodStartEnd.m_endDate.getDate().getDate();		
		int startDay = startCal.get(Calendar.DATE);
		
		String queryTransDate = "tp.transactiondate >='" + year + "-" + 
		m_monthValue[k] + "-1' AND tp.transactiondate<='" + year + "-" + 
		m_monthValue[k] + "-" + startDay + "'";
		
		/*String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND tv.accvalue<0";*/
		
		String query = "SELECT SUM(tv.accvalue*tv.exchangerate) debitvalue FROM transvalueposted tv,account ac WHERE tv.account=ac.autoindex " +
		"AND  ac.treepath LIKE '" + treePath + "%' AND transactionposted IN" +
		"(SELECT tp.autoindex FROM journalstandardsetting js, transactionposted tp " +
		"WHERE js.journal=tp.transactioncode AND " + application + " AND " + queryTransDate + 
		strUnit + ") AND (tv.accvalue*tv.exchangerate)<0";
		return query;		
	}
	
	private String getQueryAccount(Account account,int year, int k) {
		String strUnit = " ";
		if (m_unitPicker.getUnit()!=null)
			strUnit =  " AND tp.unit=" + m_unitPicker.getUnit().getIndex();
		String query = "SELECT SUM(tv.accvalue*tv.exchangerate) AS debitvalue FROM transactionposted tp,transvalueposted tv " +
		"WHERE tv.transactionposted=tp.autoindex AND tp.transactiondate LIKE '" + year + "-" + 
		m_monthValue[k] + "-%' AND tv.account=" + account.getIndex() + 
		strUnit;
		return query;
	}	
	
	private String getQueryBalance(String treePath,String queryTransDate){		
		String query = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue FROM " +
				"transvalueposted tv, account ac, transactionposted tp " +
				"WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted AND " +
				"ac.TREEPATH LIKE '" + treePath + "%'" + queryTransDate;
		return query;		
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
	
	public JasperPrint getJasperPrint() {
		return m_jasperPrint;
	}
}
