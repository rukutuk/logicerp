package pohaci.gumunda.titis.hrm.cgui.report;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.sql.Connection;
import java.text.DecimalFormat;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class RptBalance {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;  
	Employee m_employee;
	JRTableModelDataSource m_ds;
	String m_jasperReport;
	DecimalFormat m_formatDesimal;
	int m_total = 0;
	String m_bln_thn;
	public RptBalance(Connection conn,long sessionid,Employee employee) {
		try {
			m_sessionid = sessionid;
			m_conn = conn;
			m_employee = employee;
			m_formatDesimal = new DecimalFormat("#,##0.00");
//			String filename = "RptBalance.jrxml";			
//			if(filename.equals(""))
//				return;      
//			
//			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			
			m_jasperReport = ReportUtils.compileReport("RptBalance");
			DefaultTableModel model = new DefaultTableModel();
			tableHeader(model); 
			// superlabel tanyakan ke irwan
			// i: terpaksa deh bikin ga jelas
			// untuk kelas ini, superlabel-nya yang pertama muncul alias index 0
			if (employee!=null){				
				getNonEmpty(model);		
			}
			else
				getEmpty(model);
			
			m_ds = new JRTableModelDataSource(model);			   
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public JRTableModelDataSource getDataSource(){
		return m_ds;
	}
	
	public String getJasperReport(){
		return m_jasperReport;
	}
	private void getEmpty(DefaultTableModel model) {
		model.addRow(new Object[]{"PREVIOUS BAL",""});
		model.addRow(new Object[]{"LOAN",""});
		model.addRow(new Object[]{"",""});
		model.addRow(new Object[]{"NEW BALANCE",""});
	}
	
	private void getNonEmpty(DefaultTableModel model) {
		model.addRow(new Object[]{"PREVIOUS BAL","0"});
		model.addRow(new Object[]{"LOAN","0"});
		model.addRow(new Object[]{"",""});
		model.addRow(new Object[]{"NEW BALANCE","0"});
	}
	
	public int getTotal(){
		return m_total;
	}
	
	private void tableHeader(DefaultTableModel model) {
		model.addColumn("balance");
		model.addColumn("temp");		
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
			
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(), "xls"));
			exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
			exporter.exportReport();     
			
		}
	}
}