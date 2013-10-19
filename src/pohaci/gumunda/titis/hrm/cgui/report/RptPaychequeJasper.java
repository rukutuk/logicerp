package pohaci.gumunda.titis.hrm.cgui.report;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;

import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class RptPaychequeJasper {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	Employee m_employee;
	DefaultTableModel m_model;
	//JasperReport m_jasperReport;
	String m_jasperReport;
	JasperPrint m_jasperPrint;
	Map m_parameters;
	HRMBusinessLogic m_hrmlogic;
	RptWagesJasper m_jasper_1;
	RptDeductionsJasper m_jasper_2;
	RptBalance m_jasper_3;
	RptWagesTotal m_jasper_4;
	RptDeductionsTotal m_jasper_5;
	DecimalFormat m_formatDesimal;
	public RptPaychequeJasper(Connection conn,long sessionid) {
		try {
			this.m_sessionid = sessionid;
			this.m_conn = conn;
			m_hrmlogic = new HRMBusinessLogic(m_conn);
			m_formatDesimal = new DecimalFormat("#,##0.00");
			String filename = "RptPaycheques.jrxml";			
			
			m_jasperReport = ReportUtils.compileReport("RptPaycheques"); 
				//JasperCompileManager.compileReport("report/" + filename);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void getEmpty(Employee employee,String bln_thn) {
		m_employee = employee;
		m_model= new DefaultTableModel();
		tableHeader(m_model);
		getParamMap(m_conn, m_sessionid,bln_thn);
		m_model.addRow(new Object[]{"","","","","","",""});
		close();
	}

	public void getNonEmpty(/*Employee employee*/Object objemp,String month,String bln_thn) {
		if (objemp instanceof Employee) {
			m_employee = (Employee) objemp;
		}
		//m_employee = employee;
		m_model= new DefaultTableModel();
		tableHeader(m_model);
		getParamMap(m_conn, m_sessionid,bln_thn);
		CurrentEmployementRpt currentEmployementRpt = null;
		try {
			currentEmployementRpt = m_hrmlogic.getCurrentEmployementRpt(m_sessionid,"",m_employee.getIndex());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String StrUnit = "";
		String strOrg = "";
		String strJobTitle = "";
		if (currentEmployementRpt!=null){
			StrUnit = currentEmployementRpt.getCode() + " " + currentEmployementRpt.getDes();
			strOrg = currentEmployementRpt.getOrg();
			strJobTitle = currentEmployementRpt.getJobtitle();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_model.addRow(new Object[]{
				m_employee.getFirstName() + " " + m_employee.getMidleName() + " " + m_employee.getLastName(),
				m_employee.getEmployeeNo(),
				StrUnit,
				strOrg,
				strJobTitle,
				month,dateFormat.format(new Date())});
		close();
	}

	private void getParamMap(Connection conn, long sessionid,String bln_thn) {
		m_parameters = new HashMap();
		m_jasper_1 = new RptWagesJasper(conn,sessionid,m_employee,bln_thn);
		m_jasper_2 = new RptDeductionsJasper(conn,sessionid,m_employee,bln_thn);
		m_jasper_3 = new RptBalance(conn,sessionid,m_employee);
		m_jasper_4 = new RptWagesTotal(conn,sessionid,m_jasper_1.getTotal(),m_jasper_2.getTotal());
		m_jasper_5 = new RptDeductionsTotal(conn,sessionid,m_jasper_2.getTotal());
		m_parameters.put("datasource1",m_jasper_1.getDataSource());
		m_parameters.put("subreport1",m_jasper_1.getJasperReport());
		m_parameters.put("datasource2",m_jasper_2.getDataSource());
		m_parameters.put("subreport2",m_jasper_2.getJasperReport());
		m_parameters.put("datasource3",m_jasper_3.getDataSource());
		m_parameters.put("subreport3",m_jasper_3.getJasperReport());
		m_parameters.put("datasource4",m_jasper_4.getDataSource());
		m_parameters.put("subreport4",m_jasper_4.getJasperReport());
		m_parameters.put("datasource5",m_jasper_5.getDataSource());
		m_parameters.put("subreport5",m_jasper_5.getJasperReport());
	}
	private void close() {
		JRTableModelDataSource ds = new JRTableModelDataSource(m_model);
		try {
			m_jasperPrint = JasperFillManager.fillReport(m_jasperReport,
					m_parameters, ds);
		} catch (JRException e) {
			e.printStackTrace();
		}
		m_jrv = new JRViewer(m_jasperPrint);
	}

	private void tableHeader(DefaultTableModel model) {
		model.addColumn("name");
		model.addColumn("employeeno");
		model.addColumn("unit");
		model.addColumn("department");
		model.addColumn("position");
		model.addColumn("month");
		model.addColumn("date");
	}

	public JRViewer getPrintView(){
		return m_jrv;
	}

	public JasperPrint getJasperPrint(){
		return m_jasperPrint;
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

		jfc.setDialogTitle("Send Report to Excel");
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
