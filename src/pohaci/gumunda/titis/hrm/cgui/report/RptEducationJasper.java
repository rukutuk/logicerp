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
import java.text.SimpleDateFormat;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class RptEducationJasper {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;  
	Employee m_employee;
	JRTableModelDataSource m_ds;
	JasperReport m_jasperReport;
	public RptEducationJasper(Connection conn,long sessionid,Employee employee) {
		try {
			this.m_conn= conn;
			this.m_sessionid = sessionid;
			this.m_employee = employee;
			String filename = "RptEducation.jrxml";			
			if(filename.equals(""))
				return;      
			
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			//Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			tableHeader(model);      
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
	
	public JasperReport getJasperReport(){
		return m_jasperReport;
	}
	private void getEmpty(DefaultTableModel model) {
		model.addRow(new Object[]{"","","",""});		
	}
	
	private void getNonEmpty(DefaultTableModel model) {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		EducationRpt[] EducationRpt = null;
		try {
			EducationRpt = logic.getEducationRpt(m_sessionid,"",m_employee.getIndex());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (EducationRpt.length>0){
			for (int i=0;i<EducationRpt.length;i++){
				//System.err.println(EducationRpt[i].getGrade());
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				model.addRow(new Object[]{
						String.valueOf(EducationRpt[i].getGrade()),
						String.valueOf(EducationRpt[i].getMajorstudy()),
						String.valueOf(EducationRpt[i].getInstite()),
						dateFormat.format(EducationRpt[i].getGraduate())});
			}
		}else{
			getEmpty(model);
		}
		
	}
	
	private void tableHeader(DefaultTableModel model) {
		model.addColumn("grade");
		model.addColumn("majorstudy");
		model.addColumn("institute");
		model.addColumn("graduate");
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