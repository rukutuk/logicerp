package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodSubsidiaryLedger;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class Purchase{
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodSubsidiaryLedger m_periodStartEnd;
	UnitPicker m_unitPicker;
	SimpleDateFormat m_dateFormat,m_dateFormat2;
	DecimalFormat m_desimalFormat; 
	AccountingBusinessLogic m_logic;
	
	public Purchase(Connection conn,long sessionid,PeriodSubsidiaryLedger periodStartEnd,UnitPicker unitPicker,boolean isExcel){
		m_conn = conn;
		m_sessionid = sessionid;		
		m_periodStartEnd = periodStartEnd;
		m_unitPicker = unitPicker;
		m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		m_desimalFormat = new DecimalFormat("#,##0.00");		
		setNonEmpty(isExcel);
	}
	
	public Purchase(){
		setEmpty();
	}
	
	private void setEmpty() {
		try {			
			String filename = "purchase";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();	
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			parameters.put("param_period", "");						
			header(model);
			model.addRow(new Object[]{"","","","","","","","","","","","","",new Integer(1)});
			model.addRow(new Object[]{"TOTAL","","","","","","","","","","","","",new Integer(2)});
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void setNonEmpty(boolean isExcel) {
		try {			
			String filename = "purchase";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();	
			
			parameters.put("param_logo", "../images/TS.gif");
			if (m_unitPicker.getUnit()!=null)
				parameters.put("param_unit_code", m_unitPicker.getUnit().toString());
			else
				parameters.put("param_unit_code", "");
			parameters.put("param_period", m_dateFormat.format(m_periodStartEnd.m_startDate.getDate())
					+ " - " + m_dateFormat.format(m_periodStartEnd.m_endDate.getDate()));	
			header(model);
			GenericMapper mapper=MasterMap.obtainMapperFor(PurchaseReceipt.class);
			mapper.setActiveConn(m_conn);
			String selectWhere = IDBConstants.ATTR_TRANSACTION_DATE + " BETWEEN  '" + 
			m_dateFormat2.format(m_periodStartEnd.m_startDate.getDate()) + "' AND '" + 
			m_dateFormat2.format(m_periodStartEnd.m_endDate.getDate()) + "'";
			List list=mapper.doSelectWhere(selectWhere);
			Iterator iterator = list.iterator();
			int i=0;
			double ttlSupplier = 0;
			double ttlPurchase = 0;
			while (iterator.hasNext()){
				PurchaseReceipt purc = (PurchaseReceipt)iterator.next();
				ProjectData proj = purc.getProject();
				String podate = "";
				String curr = purc.getApCurr().getSymbol();
				if (purc.getContractDate()!=null)
					podate = m_dateFormat.format(purc.getContractDate());
				ttlSupplier += (purc.getAmount()+ purc.getVatAmount())*purc.getApexChRate();
				ttlPurchase += purc.getAmount()*purc.getApexChRate();
				if (m_unitPicker.getUnit()!=null){
					if (proj.getUnit().getIndex() == m_unitPicker.getUnit().getIndex()){
						model.addRow(new Object[]{String.valueOf(++i),purc.getSupplier().getName(),proj.toString(),
								purc.getContractNo(),podate,m_dateFormat.format(purc.getInvoiceDate()),purc.getInvoice(),
								curr + " " + m_desimalFormat.format(purc.getAmount()),
								curr + " " + m_desimalFormat.format(purc.getVatAmount()),
								curr + " " + m_desimalFormat.format(purc.getAmount()+ purc.getVatAmount()),
								"Rp " + m_desimalFormat.format(purc.getApexChRate()),
								m_desimalFormat.format((purc.getAmount()+ purc.getVatAmount())*purc.getApexChRate()),
								m_desimalFormat.format(purc.getAmount()*purc.getApexChRate()),new Integer(1)});
					}
				}else{
					model.addRow(new Object[]{String.valueOf(++i),purc.getSupplier().getName(),proj.toString(),
							purc.getContractNo(),podate,m_dateFormat.format(purc.getInvoiceDate()),purc.getInvoice(),
							curr + " " + m_desimalFormat.format(purc.getAmount()),
							curr + " " + m_desimalFormat.format(purc.getVatAmount()),
							curr + " " + m_desimalFormat.format(purc.getAmount()+ purc.getVatAmount()),
							"Rp " + m_desimalFormat.format(purc.getApexChRate()),
							m_desimalFormat.format((purc.getAmount()+ purc.getVatAmount())*purc.getApexChRate()),
							m_desimalFormat.format(purc.getAmount()*purc.getApexChRate()),new Integer(1)});
				}
			}			
			model.addRow(new Object[]{"TOTAL","","","","","","","","","","",m_desimalFormat.format(ttlSupplier),m_desimalFormat.format(ttlPurchase),new Integer(2)});
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
		model.addColumn("status");
	}
	
	public String getStatus(int status){
		if (status==0)	return "Not Submitted";
		else if (status==1) return "Submitted";
		else if (status==2) return "Submitted";
		else if (status==3) return "Posted";		
		return "";
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
