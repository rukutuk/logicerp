package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IncomeStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportContext;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.application.ReportUtils;

public class Income_statement {

	JRViewer viewer;
	private ReportContext context = null;
	private IncomeStatementDesign design = null;
	private Connection connection = null;
	private JasperPrint m_jasperPrint = null;
	private boolean m_excel = false;
	
	public Income_statement() {
		setJasper();
	}

	public Income_statement(ReportContext context, IncomeStatementDesign design, Connection connection, boolean excel) {
		this.context = context;
		this.design = design;
		this.connection = connection;
		this.m_excel = excel;
		setJasper();
	}

	private void setJasper() {
		try {
			//String filename = "Income_statement.jrxml";
			//JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			String jasperReport = ReportUtils.compileReport("Income_statement");
			
			Map parameters = new HashMap();

			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_title", (context != null) ? context
					.getReportTitle() : "INCOME STATEMENT");
			parameters.put("param_label_desc", (context != null) ? context
					.getReportDescription() : "");

			parameters.put("param_present", (context != null) ? context
					.getReportPeriodLabel() : "");
			parameters.put("param_comparative", (context != null) ? (context
					.isViewComparative() ? context.getComparativePeriodLabel()
					: "") : "");

			List rootList = new ArrayList();
	
			if (design != null) {
				ReportRow rootRow = design.getRootRow();
				Enumeration enumeration = rootRow.children();
				while (enumeration.hasMoreElements()) {
					ReportRow row = (ReportRow) enumeration.nextElement();
					rootList.add(row);
				}
			}
			
			AccountingSQLSAP sql = new AccountingSQLSAP();
			
			List reportRows = new ArrayList();
			
			if(connection!=null)
				reportRows = ReportRow.initializeReportData(context, rootList, sql, connection, m_excel);
			
			JRDataSource ds = new JRBeanCollectionDataSource(reportRows);

			m_jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);

			viewer = new JRViewer(m_jasperPrint);
			if(m_excel)
				exportToExcel(m_jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public JRViewer getViewer() {
		return viewer;
	}
	
	public JasperPrint getJasperPrint(){
		return m_jasperPrint;
	}
	
	public void exportToExcel(JasperPrint jasperPrint) throws JRException{
		javax.swing.JFileChooser jfc = new javax.swing.JFileChooser("reportsample/");
		
		jfc.setDialogTitle("Simpan Laporan Dalam File Excel");
		jfc.setFileFilter( new javax.swing.filechooser.FileFilter()
		{
			public boolean accept(java.io.File file){
				String filename = file.getName();
				return(filename.toLowerCase().endsWith(".xls")||file.isDirectory()||filename.toLowerCase().endsWith(".jrxml"));
			}

			public String getDescription() {
				return "Laporan *.xls";
			}
		});
		
		jfc.setMultiSelectionEnabled(false);
		jfc.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
		if(jfc.showSaveDialog(null) == javax.swing.JOptionPane.OK_OPTION){
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(),"xls"));
			exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
			exporter.exportReport();
		}
	}
	
	private String changeFileExtension(String filename, String newExtension) {
		if(!newExtension.startsWith("."))
			newExtension = "." + newExtension;
		if(filename == null || filename.length() == 0){
			return newExtension;
		}
		int index = filename.lastIndexOf(".");
		if(index >= 0){
			filename = filename.substring(0, index);
		}
		return filename += newExtension;
	}
}