package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodStartEnd;
import pohaci.gumunda.titis.application.ReportUtils;

public class General_Ledger{
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodStartEnd m_periodStartEnd;
	UnitPicker m_unitPicker;
	SimpleDateFormat m_dateFormat,m_dateFormat2;
	DecimalFormat m_desimalFormat; 
	AccountingBusinessLogic m_logic;
	boolean m_root;
	int m_level = 0;
	JasperReport m_jasperReport;
	JasperPrint m_jasperPrint;
	public General_Ledger(Connection conn,long sessionid,Account account,PeriodStartEnd periodStartEnd,UnitPicker unitPicker,boolean excel){
		try {
			
			m_conn = conn;
			m_sessionid = sessionid;	
			//String filename = "GenLedger.jrxml";
			//JasperReport jasperReport = JasperCompileManager.compileReport("../report/" + filename);			
			//if(filename.equals(""))
				//return;      
			String jasperReport = ReportUtils.compileReport("GenLedger");
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn(null);
			model.addRow(new Object[]{null});		
			
			Map parameters = new HashMap();
			General_ladgerChild jasper =null;
			
			if (account==null)
				jasper= new General_ladgerChild(excel);
			else{
				jasper= new General_ladgerChild(conn,sessionid,account,periodStartEnd,unitPicker,excel);
			}
			//parameters.put("datasource1",jasper.getDataSource());
			//parameters.put("subreport1",jasper.getJasperReport());
					
			JRTableModelDataSource ds = new JRTableModelDataSource(model);  
			m_jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, ds);
			
			m_jrv = new JRViewer(m_jasperPrint);
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void exportexcel(JasperPrint jasperPrint){
		try {
			exportToExcel(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public JasperPrint getJasperPrint(){
		return m_jasperPrint;
	}
	
	public JRViewer getPrintView(){
		return m_jrv;
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
	  
	  void exportToExcel(JasperPrint jasperPrint) throws Exception {
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
}
