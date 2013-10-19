package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.CustomerPicker;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.BeginningAccountReceivableBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.SalesInvoiceBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.PeriodStartEnd;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.Customer;

public class AccountReceivable {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodStartEnd m_periodStartEnd;
	UnitPicker m_unitPicker;
	Account m_account;
	DecimalFormat m_desimalFormat;
	AccountingBusinessLogic m_logic;
	DatePicker m_datePicker;
	Account arVas = null;
	CustomerPicker m_customerpicker;
	
	public AccountReceivable() {
		setEmpty();
	}
	
	public AccountReceivable(Connection conn, long sessionid,
			UnitPicker unitPicker, DatePicker date,CustomerPicker custpicker,boolean isExcel) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_unitPicker = unitPicker;
		m_datePicker = date;
		m_customerpicker = custpicker;
		getARVAS();
		setNonEmpty(isExcel);
	}
	
	private void getARVAS() {
		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(m_conn, m_sessionid, IConstants.ATTR_VARS_AR);
		arVas = vas.getAccount();
	}
	
	private void setEmpty() {
		try {
			String filename = "AccountReceivable";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			parameters.put("param_date", "");
			header(model);
			model.addRow(new Object[] { "", "", "", "", "","", "", "", "","","","","",
					new Integer(0) });
			model.addRow(new Object[] { "Total", "", "", "", "","", "", "", "","","","","",
					new Integer(1) });
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					compiledRptFilename, parameters, ds);
			
			m_jrv = new JRViewer(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void setNonEmpty(boolean isExcel) {
		try {
			//HashMap map = new HashMap();
			String filename = "AccountReceivable";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "")
				return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			if (m_unitPicker.getUnit() != null)
				parameters.put("param_unit_code", m_unitPicker.getUnit().toString());
			parameters.put("param_date", dateFormat.format(m_datePicker.getDate()));
			header(model);
			List arList = getARBySalesInvoice(dateFormat2);
			List bbList = getARByBeginningAR(dateFormat2);
			List list = new ArrayList();
			addBBToList(bbList, list);
			addInvToList(arList, list);
			Collections.sort(list);
			int i = 0;
			double totArBalance = 0;
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				ARReport ar = (ARReport) iterator.next();
				if (ar.getAr()!=0 && !desimalFormat.format(ar.getAr()).equals("0.00") ){
					totArBalance += (ar.getAr()*ar.getRate());
					model.addRow(new Object[] {
							String.valueOf(++i), // 0
							ar.getCustomer(), // 1
							ar.getProjectCode(), // 2
							ar.getActivityCode(), // 3
							dateFormat.format(ar.getInvDate()), // 4
							ar.getInvNo(), // 5
							ar.getCurr(), //6
							desimalFormat.format(ar.getDpp()),//7
							desimalFormat.format(ar.getVat()),//8
							desimalFormat.format(ar.getTotinv()), // 9
							ar.getCurr() + " " + desimalFormat.format(ar.getRetention()), // 10
							desimalFormat.format(ar.getRate()),//11
							desimalFormat.format(ar.getTotarr() * ar.getRate()),//12
							desimalFormat.format(ar.getAr()*ar.getRate()), // 13
							String.valueOf(ar.getDay()), // 14
							new Integer(0) }); // status
				}
			}
			model.addRow(new Object[] { "TOTAL", "", "", "", "", "","","","","","","","",
					desimalFormat.format(totArBalance), "", new Integer(1) });
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					compiledRptFilename, parameters, ds);
			if(!isExcel)
				m_jrv = new JRViewer(jasperPrint);
			else
				exportToExcel(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void addBBToList(List bbList, List list) {
		Iterator iter = bbList.iterator();
		while(iter.hasNext()){
			BeginningAccountReceivable bar = (BeginningAccountReceivable) iter.next();
			String customer = "";
			String projCode = "";
			String activity = "";
			Date transDate = null;
			String refNo = "";
			BeginningAccountReceivableBusinessLogic logic = new BeginningAccountReceivableBusinessLogic(m_conn, m_sessionid);
			if (bar.getProject()!=null){
				projCode = bar.getProject().getCode();
				activity = bar.getProject().getActivity().getName();
				Transaction trans = null;
				Unit unit = bar.getProject().getUnit();
				if (unit!=null)
					trans = logic.findTransaction(unit);
				if (trans!=null){
					transDate = trans.getTransDate();
					refNo = trans.getReference();
				}
			}
			double val = logic.getAccountReceivableBalance(bar);
			double totalarreceive = logic.getAccumulatedReceived(bar);
			double retention = logic.getRetention(bar);
			double dpp = 0;
			double vat = 0;
			double totinv = bar.getAccValue();
			long day = (m_datePicker.getDate().getTime() - bar
					.getTrans().getTransDate().getTime())
					/ (24 * 60 * (60 * 1000));
			if (bar.getCustomer()!=null){
				if (m_customerpicker.getObject()!=null){
					Object obj = m_customerpicker.getObject();
					if (obj instanceof Customer) {
						Customer cust = (Customer) obj;
						if (cust.getIndex() == bar.getCustomer().getIndex()){
							customer = bar.getCustomer().getName();
							setAr(bar,list, customer, projCode, activity, transDate, refNo, val, retention, dpp, totinv, totalarreceive,vat, day);
						}
					}
				}else{
					customer = bar.getCustomer().getName();
					setAr(bar,list, customer, projCode, activity, transDate, refNo, val, retention, dpp, totinv, totalarreceive, vat, day);
				}
			}
		}
	}
	
	private void addInvToList(List bbList, List list) {
		Iterator iter = bbList.iterator();
		while(iter.hasNext()){
			SalesInvoice inv = (SalesInvoice) iter.next();
			String customer = "";
			String projCode = "";
			String activity = "";
			Date transDate = null;
			String refNo = "";
			if (inv.getProject()!=null){
				projCode = inv.getProject().getCode();
				activity = inv.getProject().getActivity().getName();
			}
			transDate = inv.getTransactionDate();
			refNo = inv.getReferenceNo();
			SalesInvoiceBusinessLogic logic = new SalesInvoiceBusinessLogic(m_conn, m_sessionid);
			double val = logic.getAccountReceivableBalance(inv);
			double retention = logic.getRetention(inv);
			double totalarreceive = logic.getAccumulatedReceived(inv);
			double dpp = inv.getSalesAmount();
			double vat = inv.getVatAmount();
			double totinv = dpp + vat;
			long day = (m_datePicker.getDate().getTime() - inv
					.getTransactionDate().getTime())
					/ (24 * 60 * (60 * 1000));
			if (inv.getCustomer()!=null){
				if (m_customerpicker.getObject()!=null){
					Object obj = m_customerpicker.getObject();
					if (obj instanceof Customer) {
						Customer cust = (Customer) obj;
						if (cust.getIndex()==inv.getCustomer().getIndex())					{
							customer = inv.getCustomer().getName();
							setAr(inv,list, customer, projCode, activity, transDate, refNo, val, retention, dpp, totinv, totalarreceive,vat, day);
						}
					}
				}
				else{
					customer = inv.getCustomer().getName();
					setAr(inv,list, customer, projCode, activity, transDate, refNo, val, retention, dpp, totinv, totalarreceive, vat, day);
				}
			}
		}
	}
	
	private void setAr(Object obj,List list, String customer, String projCode, String activity, Date transDate, String refNo, double val, double retention, double dpp, double totalinvoice, double totarreceive,double vat, long day) {
		ARReport ar = new ARReport(customer, projCode, activity, transDate, refNo, dpp,vat,totalinvoice, totarreceive,val,retention, day);
		if (obj instanceof SalesInvoice) {
			SalesInvoice inv = (SalesInvoice) obj;
			if (inv.getSalesCurr()!=null)
				ar.setCurr(inv.getSalesCurr().getSymbol());
			ar.setRate(inv.getSalesExchRate());
		}
		else if (obj instanceof BeginningAccountReceivable) {
			BeginningAccountReceivable bar = (BeginningAccountReceivable) obj;
			if (bar.getCurrency()!=null)
				ar.setCurr(bar.getCurrency().getSymbol());
			ar.setRate(bar.getExchangeRate());
		}
		list.add(ar);
	}
	
	private List getARByBeginningAR(SimpleDateFormat dateFormat) {
		GenericMapper mapper = MasterMap.obtainMapperFor(BeginningAccountReceivable.class);
		mapper.setActiveConn(m_conn);
		List list = mapper.doSelectAll();
		List resultList = new ArrayList();
		Iterator iterator = list.iterator();
		BeginningAccountReceivableBusinessLogic logic = new BeginningAccountReceivableBusinessLogic(m_conn, m_sessionid);
		while (iterator.hasNext()) {
			BeginningAccountReceivable bar = (BeginningAccountReceivable) iterator.next();
			if (bar.getAccount().getIndex()==arVas.getIndex()) {
				Transaction trans = logic.findTransaction(bar.getProject().getUnit());
				if (trans != null) {
					bar.setTrans(trans);
					bar.showReferenceNo(true);
					if (logic.isOutstanding(bar))
						resultList.add(bar);
				}
			}
		}
		return resultList;
	}
	
	private List getARBySalesInvoice(SimpleDateFormat dateFormat) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesInvoice.class);
		mapper.setActiveConn(m_conn);
		String strUnit = "";
		if (m_unitPicker.getUnit() != null)
			strUnit = " AND " + IDBConstants.ATTR_UNIT + "="
			+ m_unitPicker.getUnit().getIndex();
		String strquery = IDBConstants.ATTR_TRANSACTION_DATE + "<='"
		+ dateFormat.format(m_datePicker.getDate()) + "'"
		+ " AND " + IDBConstants.ATTR_STATUS + "=3 "
		+ strUnit;
		strquery += " ORDER BY "
			+ IDBConstants.ATTR_TRANSACTION_DATE;
		List list = mapper.doSelectWhere(strquery);
		List resultList = new ArrayList();
		Iterator iterator = list.iterator();
		SalesInvoiceBusinessLogic logic = new SalesInvoiceBusinessLogic(m_conn,
				m_sessionid);
		while (iterator.hasNext()) {
			SalesInvoice inv = (SalesInvoice) iterator.next();
			if (logic.isOutstanding(inv))
				resultList.add(inv);
		}
		return resultList;
	}
	
	
	private void header(DefaultTableModel model) {
		model.addColumn("Field0");
		model.addColumn("Field1");
		model.addColumn("Field2");
		model.addColumn("Field3");
		model.addColumn("Field4");
		model.addColumn("Field5");
		model.addColumn("Field6");
		model.addColumn("Field7");
		model.addColumn("Field8");
		model.addColumn("Field9");
		model.addColumn("Field10");
		model.addColumn("Field11");
		model.addColumn("Field12");
		model.addColumn("Field13");
		model.addColumn("Field14");
		model.addColumn("status");
	}
	
	
	protected class ARReport implements Comparable {
		private String customer = "";
		private String projectCode = "";
		private String activityCode = "";
		private Date invDate = new Date();
		private String invNo = "";
		private double dpp = 0;
		private double vat = 0;
		private double ar = 0;
		private double totarr = 0;
		private double retention =0;
		private long day = 0;
		private String curr = "";
		private double rate = 0;
		private double totinv = 0;
		
		public ARReport(String customer, String projectCode, String activityCode, Date invDate, String invNo, double dpp, double vat, double totinv, double totarr,double ar,double retention, long day) {
			this.customer = customer;
			this.projectCode = projectCode;
			this.activityCode = activityCode;
			this.invDate = invDate;
			this.invNo = invNo;
			this.dpp = dpp;
			this.vat = vat;
			this.totinv = totinv;
			this.totarr = totarr;
			this.ar = ar;
			this.retention = retention;
			this.day = day;
		}
		
		public String getActivityCode() {
			return activityCode;
		}
		
		public double getVat(){
			return vat;
		}
		public double getDpp(){
			return dpp;
		}
		
		public double getAr() {
			return ar;
		}
		
		public double getRetention(){
			return retention;
		}
		
		public String getCustomer() {
			return customer;
		}
		
		public long getDay() {
			return day;
		}
		
		public Date getInvDate() {
			return invDate;
		}
		
		public String getInvNo() {
			return invNo;
		}
		
		public String getProjectCode() {
			return projectCode;
		}
		
		
		public int compareTo(Object arg0) {
			if (arg0 instanceof ARReport) {
				ARReport rpt = (ARReport) arg0;
				int comp = 0;
				comp = this.toString().compareTo(rpt.toString());
				
				if (comp == 0)
					comp = this.getProjectCode().compareTo(rpt.getProjectCode());
				
				if (comp == 0)
					comp = this.getInvDate().compareTo(rpt.getInvDate());
				
				return comp;
			} else
				return 0;
		}
		
		public String toString() {
			return customer;
		}
		
		public String getCurr() {
			return curr;
		}
		
		public void setCurr(String curr) {
			this.curr = curr;
		}
		
		public double getRate() {
			return rate;
		}
		
		public void setRate(double rate) {
			this.rate = rate;
		}
		
		public double getTotarr() {
			return totarr;
		}
		
		public double getTotinv() {
			return totinv;
		}
	}
	
	public JRViewer getPrintView() {
		return m_jrv;
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
