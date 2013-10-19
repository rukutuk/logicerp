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
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.cgui.BaseCurrency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.BeginningAccountPayableBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.PeriodStartEnd;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.Partner;

public class AccountPayable{
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodStartEnd m_periodStartEnd;
	UnitPicker m_unitPicker;
	Account m_account;
	DecimalFormat m_desimalFormat; 
	AccountingBusinessLogic m_logic;
	DatePicker m_datePicker;
	
	Account apVas = null;
	
	public AccountPayable(){
		setEmpty();
	}
	
	public AccountPayable(Connection conn,long sessionid,UnitPicker unitPicker,DatePicker date,boolean isExcel){
		m_conn = conn;
		m_sessionid = sessionid;
		m_unitPicker = unitPicker;
		m_datePicker = date;
		getAPVAS();
		setNonEmpty(isExcel);
	}
	
	private void setEmpty() {
		try {			
			String filename = "account_payable";			
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			parameters.put("param_logo", "../images/TS.gif");			
			parameters.put("param_unit_code","");			
			parameters.put("param_date", "");
			header(model);
			model.addRow(new Object[]{"","","","","","","","","","","",new Integer(1)});
			model.addRow(new Object[]{"TOTAL","","","","","","","","","","",new Integer(2)});
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getAPVAS() {
		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(m_conn, m_sessionid, IConstants.ATTR_VARS_AP);
		apVas = vas.getAccount();
	}
	
	private void setNonEmpty(boolean isExcel) {
		try {			
			String filename = "account_payable";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			parameters.put("param_logo", "../images/TS.gif");		
			parameters.put("param_unit_code","");			
			if(m_unitPicker.getUnit()!= null)
				parameters.put("param_unit_code",m_unitPicker.getUnit().getDescription());			
			parameters.put("param_date", dateFormat.format(m_datePicker.getDate()));			
			header(model);
			List apList = getAPByPurchaseReceipt(dateFormat2);
			List bbList = getAPByBeginningAP(dateFormat2);
			List list = new ArrayList();
			addBBToList(bbList, list);
			addPurchToList(apList, list);
			Collections.sort(list);
			double total = 0;
			int i = 0;
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				APReport ap = (APReport) iterator.next();
				total += ap.getAPBalance()*ap.getExchRate();
				String invDate = "";
				String dueDate = "";
				if(ap.getInvDate()!=null)
					invDate = dateFormat.format(ap.getInvDate());
				if(ap.getDueDate()!=null)
					dueDate = dateFormat.format(ap.getDueDate());
				if (total != 0)
					model.addRow(new Object[]{String.valueOf(++i),ap.toString(),
							invDate, ap.getInvNo(), 
							ap.getCurrency() + " " + desimalFormat.format(ap.getAP()),
							ap.getCurrency() + " " + desimalFormat.format(ap.getVAT()),
							"Rp " + desimalFormat.format(ap.getExchRate()),
							ap.getCurrency() + " " + desimalFormat.format(ap.getAPBalance()),
							desimalFormat.format(ap.getAPBalance()*ap.exchRate),	
							dueDate,
							String.valueOf(ap.getDay()),new Integer(1)});// status
			}
			model.addRow(new Object[]{"TOTAL","","","","","","","",desimalFormat.format(total),"","",new Integer(2)});
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			if(!isExcel)
				m_jrv = new JRViewer(jasperPrint);
			else
				exportToExcel(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void addPurchToList(List apList, List list) {
		Iterator iter = apList.iterator();
		while(iter.hasNext()){
			PurchaseReceipt purcRec = (PurchaseReceipt) iter.next();
			String curr = purcRec.getApCurr().getSymbol();
			Partner supplier = null;
			long day = 0;
			GenericMapper mapper2 = MasterMap.obtainMapperFor(PurchaseApPmt.class);
			mapper2.setActiveConn(m_conn);
			String strWhere2 = IDBConstants.ATTR_PURCHASE_RECEIPT + "=" + purcRec.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3" ;
			System.err.println(strWhere2);
			List list2 = mapper2.doSelectWhere(strWhere2);
			Iterator iterator2 = list2.iterator();							
			double totAmountPurcApp = 0;
			while(iterator2.hasNext()){		
				PurchaseApPmt purcApp = (PurchaseApPmt)iterator2.next();
				totAmountPurcApp +=purcApp.getAppMtAmount();
			}
			double RpaccountPayable = (purcRec.getAmount()+purcRec.getVatAmount())-totAmountPurcApp;
			if (!curr.equals(BaseCurrency.createBaseCurrency(m_conn,m_sessionid).getSymbol()))
				RpaccountPayable = ((purcRec.getAmount()+purcRec.getVatAmount())-totAmountPurcApp)*purcRec.getApexChRate();
			if (purcRec.getSupplier()!=null)
				supplier = purcRec.getSupplier();
			day = (m_datePicker.getDate().getTime()-purcRec.getTransactionDate().getTime())/(24*60*(60*1000));
			APReport ap = null;
			if(RpaccountPayable!= 0){
				ap = new APReport(supplier, purcRec.getTransactionDate(), purcRec.getInvoice(), 
						purcRec.getAmount(),(purcRec.getAmount()+purcRec.getVatAmount())-totAmountPurcApp, 
						purcRec.getVatAmount(), purcRec.getApexChRate(), purcRec.getDuedate(), day, curr );
				list.add(ap);
			}
		}
	}
	
	private void addBBToList(List bbList, List list) {
		Iterator iter = bbList.iterator();
		while(iter.hasNext()){
			BeginningAccountPayable bap = (BeginningAccountPayable) iter.next();
			String curr = bap.getCurrency().getSymbol();
			Partner supplier = null;
			Date invDate = null;
			Date dueDate = null;
			String invNo = "";
			long day = 0;
			if (bap.getPartner()!=null)
				supplier = bap.getPartner();
			BeginningAccountPayableBusinessLogic logic = new BeginningAccountPayableBusinessLogic(m_conn, m_sessionid);
			Transaction trans = null;
			Unit unit = bap.getProject().getUnit();
			if (unit!=null)
				trans = logic.findTransaction(unit);
			if (trans!=null){
				invDate = trans.getTransDate();
				invNo = trans.getDescription();
				day = (m_datePicker.getDate().getTime()-trans.getTransDate().getTime())/(24*60*(60*1000));
			}
			GenericMapper mapper2 = MasterMap.obtainMapperFor(PurchaseApPmt.class);
			mapper2.setActiveConn(m_conn);
			String strWhere2 = IDBConstants.ATTR_BEGINNING_BALANCE + "=" + bap.getIndex();
			System.err.println(strWhere2);
			List list2 = mapper2.doSelectWhere(strWhere2);
			Iterator iterator2 = list2.iterator();							
			double totAmountPurcApp = 0;
			while(iterator2.hasNext()){		
				PurchaseApPmt purcApp = (PurchaseApPmt)iterator2.next();
				totAmountPurcApp +=purcApp.getAppMtAmount();
			}
			double RpaccountPayable = (bap.getAccValue())-totAmountPurcApp;
			if (!curr.equals(BaseCurrency.createBaseCurrency(m_conn,m_sessionid).getSymbol()))
				RpaccountPayable = ((bap.getAccValue())-totAmountPurcApp)*bap.getExchangeRate();
			double val = logic.getAccountPayableBalance(bap)*bap.getExchangeRate();
			APReport ap = null;
			if(m_unitPicker.getUnit()!=null){
				if(unit.getIndex() == m_unitPicker.getUnit().getIndex()){
					ap = new APReport(supplier, invDate, invNo, RpaccountPayable-(RpaccountPayable/11),val,
							RpaccountPayable/11, 1.00, dueDate, day, "Rp" );
					list.add(ap);
				}
			}
			else{
				ap = new APReport(supplier, invDate, invNo, RpaccountPayable-(RpaccountPayable/11),val,
						RpaccountPayable/11, 1.00, dueDate, day, "Rp" );
				list.add(ap);
			}
		}
	}
	
	private List getAPByBeginningAP(SimpleDateFormat dateFormat2) {
		GenericMapper mapper = MasterMap.obtainMapperFor(BeginningAccountPayable.class);
		mapper.setActiveConn(m_conn);
		List list = mapper.doSelectAll();
		List resultList = new ArrayList();
		Iterator iterator = list.iterator();
		BeginningAccountPayableBusinessLogic logic = new BeginningAccountPayableBusinessLogic(m_conn, m_sessionid);
		while (iterator.hasNext()) {
			BeginningAccountPayable bap = (BeginningAccountPayable) iterator.next();
			if (bap.getAccount().getIndex()==apVas.getIndex()) {
				Transaction trans = logic.findTransaction(bap.getProject().getUnit());
				if (trans != null) {
					bap.setTrans(trans);
					bap.showReferenceNo(true);
					if (logic.isOutstanding(bap))
						resultList.add(bap);
				}
			}
		}
		return resultList;
	}
	
	private List getAPByPurchaseReceipt(SimpleDateFormat dateFormat2) {
		GenericMapper mapper = MasterMap.obtainMapperFor(PurchaseReceipt.class);
		mapper.setActiveConn(m_conn);
		List resultList = new ArrayList();
		String strUnit = "";
		if (m_unitPicker.getUnit() != null)
			strUnit = "AND " + IDBConstants.ATTR_UNIT + "="
			+ m_unitPicker.getUnit().getIndex();
		String strQuery = IDBConstants.ATTR_TRANSACTION_DATE + "<='"
		+ dateFormat2.format(m_datePicker.getDate()) + "' AND "
		+ IDBConstants.ATTR_STATUS + "=3" + strUnit;
		List list = mapper.doSelectWhere(strQuery);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PurchaseReceipt purcRec = (PurchaseReceipt) iterator.next();
			String curr = purcRec.getApCurr().getSymbol();
			GenericMapper mapper2 = MasterMap
			.obtainMapperFor(PurchaseApPmt.class);
			mapper2.setActiveConn(m_conn);
			String strWhere2 = IDBConstants.ATTR_PURCHASE_RECEIPT + "="
			+ purcRec.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
			List list2 = mapper2.doSelectWhere(strWhere2);
			Iterator iterator2 = list2.iterator();
			double totAmountPurcApp = 0;
			while (iterator2.hasNext()) {
				PurchaseApPmt purcApp = (PurchaseApPmt) iterator2.next();
				totAmountPurcApp += purcApp.getAppMtAmount();
			}
			double RpaccountPayable = (purcRec.getAmount() + purcRec
					.getVatAmount())
					- totAmountPurcApp;
			if (!curr.equals(BaseCurrency.createBaseCurrency(m_conn,
					m_sessionid).getSymbol()))
				RpaccountPayable = ((purcRec.getAmount() + purcRec
						.getVatAmount()) - totAmountPurcApp)
						* purcRec.getApexChRate();
			if (RpaccountPayable != 0)
				resultList.add(purcRec);
		}
		return resultList;
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
		model.addColumn("status");
	}
	
	protected class APReport implements Comparable {
		private Partner supplier = null;
		private Date invDate = new Date();
		private Date dueDate = new Date();
		private String invNo = "";
		private double ap = 0;
		private double apBalance = 0;
		private double vat = 0;
		private double exchRate = 0;
		private long day = 0;
		private String curr = "";
		public APReport(Partner supplier, Date invDate, String invNo, double ap, double apBalance, 
				double vat, double exchRate, Date dueDate, long day, String curr ) {
			this.supplier = supplier;
			this.invDate = invDate;
			this.invNo = invNo;
			this.ap = ap;
			this.apBalance = apBalance;
			this.vat = vat;
			this.exchRate = exchRate;
			this.dueDate = dueDate;
			this.day = day;
			this.curr = curr;
		}
		
		public Partner getSupplier(){
			return supplier;
		}
		
		public Date getInvDate(){
			return invDate;
		}
		
		public String getInvNo(){
			return invNo;
		}
		
		public double getAP(){
			return ap;
		}
		
		public double getAPBalance(){
			return apBalance;
		}
		
		public double getVAT(){
			return vat;
		}
		
		public double getExchRate(){
			return exchRate;
		}
		
		public Date getDueDate(){
			return dueDate;
		}
		
		public long getDay(){
			return day;
		}
		
		public String getCurrency(){
			return curr;
		}
		
		public int compareTo(Object arg0) {
			if (arg0 instanceof APReport) {
				APReport rpt = (APReport) arg0;
				int comp = 0;
				comp = this.toString().compareTo(rpt.toString());
				if (comp == 0)
					comp = this.getSupplier().getCode().compareTo(rpt.getSupplier().getCode());
				if (comp == 0)
					comp = this.getInvDate().compareTo(rpt.getInvDate());
				return comp;
			} else
				return 0;
		}
		public String toString() {
			return supplier.getName();
		}	
	}
	
	public JRViewer getPrintView(){
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
