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
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodSubsidiaryLedger;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class Sales{
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodSubsidiaryLedger m_periodStartEnd;
	UnitPicker m_unitPicker;
	SimpleDateFormat m_dateFormat,m_dateFormat2;
	DecimalFormat m_desimalFormat; 
	AccountingBusinessLogic m_logic;
	
	public Sales(Connection conn,long sessionid,PeriodSubsidiaryLedger periodStartEnd,UnitPicker unitPicker , boolean toexcel){
		m_conn = conn;
		m_sessionid = sessionid;		
		m_periodStartEnd = periodStartEnd;
		m_unitPicker = unitPicker;
		m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		m_desimalFormat = new DecimalFormat("#,##0.00");		
		setNonEmpty(toexcel);
	}
	
	public Sales(){
		setEmpty();
	}
	
	private void setEmpty() {
		try {			
			String filename = "Sales";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();	
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_unit_code", "");
			parameters.put("param_period", "");						
			
			header(model);
			
			model.addRow(new Object[]{"","","","","","","","","","","","","","","",new Integer(1)});			
			model.addRow(new Object[]{"","","","","","","","","","","","","TOTAL","","",new Integer(2)});			
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			
			m_jrv = new JRViewer(jasperPrint);
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
		model.addColumn("field14");
		model.addColumn("field15");
		model.addColumn("status");
	}
	
	private void setNonEmpty(boolean toexcel) {
		try {			
			String filename = "Sales";
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
			
			GenericMapper mapper=MasterMap.obtainMapperFor(SalesInvoice.class);
			mapper.setActiveConn(m_conn);
			String selectWhere = IDBConstants.ATTR_TRANSACTION_DATE + " BETWEEN  '" + 
			m_dateFormat2.format(m_periodStartEnd.m_startDate.getDate()) + "' AND '" + 
			m_dateFormat2.format(m_periodStartEnd.m_endDate.getDate()) + "' AND Status=3";
			System.err.println(selectWhere);
			List list=mapper.doSelectWhere(selectWhere);
			Iterator iterator = list.iterator();
			int i=0;
			double totalInv = 0;
			double totalSales = 0;
			while (iterator.hasNext()){
				SalesInvoice inv = (SalesInvoice)iterator.next();
				
				if (inv.getProject()!=null){					
					ProjectData a = inv.getProject();
					String projectCode = "";						
					String customer = "";		
					projectCode = a.toString();
					if (a.getCustomer()!=null)
						customer = a.getCustomer().toString();
					totalInv +=((inv.getSalesAmount()-inv.getDownPaymentAmount())+inv.getVatAmount())*inv.getSalesExchRate();					
					totalSales +=inv.getSalesAmount()*inv.getSalesExchRate();					
					if (m_unitPicker.getUnit()!=null){						
						if (a.getUnit().getIndex() == m_unitPicker.getUnit().getIndex()){						
							model.addRow(new Object[]{String.valueOf(++i),customer,projectCode,a.getIPCNo(),a.getWorkDescription(),
									m_dateFormat.format(inv.getTransactionDate()),inv.getReferenceNo(),
									inv.getSalesCurr().getSymbol() + " "  + m_desimalFormat.format(inv.getSalesAmount()) ,
									inv.getDownPaymentCurr().getSymbol() + " " + m_desimalFormat.format(inv.getDownPaymentAmount()),
									inv.getSalesCurr().getSymbol() + " " +m_desimalFormat.format(inv.getSalesAmount()-inv.getDownPaymentAmount()),
									inv.getVatCurr().getSymbol() + " "  + m_desimalFormat.format(inv.getVatAmount()),
									inv.getSalesCurr().getSymbol() + " " + m_desimalFormat.format((inv.getSalesAmount()-inv.getDownPaymentAmount())+inv.getVatAmount()),
									"Rp " + m_desimalFormat.format(inv.getSalesExchRate()), 
									m_desimalFormat.format(((inv.getSalesAmount()-inv.getDownPaymentAmount())+inv.getVatAmount())*inv.getSalesExchRate()),
									m_desimalFormat.format(inv.getSalesAmount()*inv.getSalesExchRate()),
									new Integer(1)});
						}
					}else{
						model.addRow(new Object[]{String.valueOf(++i),customer,projectCode,a.getIPCNo(),a.getWorkDescription(),
								m_dateFormat.format(inv.getTransactionDate()),inv.getReferenceNo(),
								inv.getSalesCurr().getSymbol() + " "  + m_desimalFormat.format(inv.getSalesAmount()) ,
								inv.getDownPaymentCurr().getSymbol() + " " + m_desimalFormat.format(inv.getDownPaymentAmount()),
								inv.getSalesCurr().getSymbol() + " " +m_desimalFormat.format(inv.getSalesAmount()-inv.getDownPaymentAmount()),
								inv.getVatCurr().getSymbol() + " "  + m_desimalFormat.format(inv.getVatAmount()),
								inv.getSalesCurr().getSymbol() + " " + m_desimalFormat.format((inv.getSalesAmount()-inv.getDownPaymentAmount())+inv.getVatAmount()),
								"Rp " + m_desimalFormat.format(inv.getSalesExchRate()), 
								m_desimalFormat.format(((inv.getSalesAmount()-inv.getDownPaymentAmount())+inv.getVatAmount())*inv.getSalesExchRate()),
								m_desimalFormat.format(inv.getSalesAmount()*inv.getSalesExchRate()),
								new Integer(1)});						
					}					
				}				
			}		
			
			model.addRow(new Object[]{"","","","","","","","","","","","","TOTAL",m_desimalFormat.format(totalInv),m_desimalFormat.format(totalSales),new Integer(2)});			
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			
			if(!toexcel)
				m_jrv = new JRViewer(jasperPrint);
			else
				exportToExcel(jasperPrint);
			
			//m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
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
