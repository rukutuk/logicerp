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
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.SexType;
import pohaci.gumunda.titis.hrm.cgui.SimpleEmployeeAttribute;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.EmployeeQualification;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class RptEmployeeProfileJasper {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;			
	RptEmploymentHistoryJasper m_jasper_1;
	RptEducationJasper m_jasper_2;
	RptFamilyJasper m_jasper_3;
	RptCertificationJasper m_jasper_4;
	RptBankAccountJasper m_jasper_5;
	Employee m_employee;
	Employee[] m_employees;
	Map m_parameters;
	JasperPrint m_jasperPrint;
	JasperReport m_jasperReport;
	DefaultTableModel m_model;
	HRMBusinessLogic m_hrmlogic;
	ProjectBusinessLogic m_projectlogic;
	
	public RptEmployeeProfileJasper(Connection conn,long sessionid) {
		
		try {			
			this.m_sessionid = sessionid;			
			this.m_conn = conn;
			m_hrmlogic = new HRMBusinessLogic(m_conn);
			m_projectlogic = new ProjectBusinessLogic(m_conn);
			//this.m_employees = employees;
			String filename = "RptEmployeeProfile.jrxml";
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);			
			/*if (m_employees!=null){								
				for (int i=0;i<m_employees.length;i++){
					if (i==0){						
						getNonEmpty(m_employees[i]);
					}else{						
						getNonEmpty(m_employees[i]);
						JRPrintPage page = (JRPrintPage)m_jasperPrint.getPages().get(0);
						m_jasperPrint.addPage(0, page);
					}
				}				
			}else{
				getEmpty(null);
			}
			m_jrv = new JRViewer(m_jasperPrint);*/
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getParamMap(Connection conn, long sessionid) {		
		m_parameters = new HashMap();
		m_jasper_1 = new RptEmploymentHistoryJasper(conn,sessionid,m_employee);		
		m_jasper_2 = new RptEducationJasper(conn,sessionid,m_employee);
		m_jasper_3 = new RptFamilyJasper(conn,sessionid,m_employee);
		m_jasper_4 = new RptCertificationJasper(conn,sessionid,m_employee);
		m_jasper_5 = new RptBankAccountJasper(conn,sessionid,m_employee,2);
		//RptEmploymentHistoryJasper(conn,sessionid,m_employee);	
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
	
	/*// batas
	
	public void RptEmploymentHistoryJasper(Connection conn,long sessionid,Employee employee) {
		try {
			m_sessionid = sessionid;
			m_conn = conn;
			m_employee = employee;
			String filename = "RptEmploymentHistory.jrxml";			
			if(filename.equals(""))
				return;      
			
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			//Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			tableHeader1(model);      
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
	
	private void tableHeader1(DefaultTableModel model) {
		//model.addColumn("jobtitle");
		model.addColumn("department");
		model.addColumn("unit");
		//model.addColumn("tmt");
	}
	
	public JasperReport getJasperReport(){
		return m_jasperReport;
	}
	
	JRTableModelDataSource m_ds;
	public JRTableModelDataSource getDataSource(){
		return m_ds;
	}
	
	private void getEmpty(DefaultTableModel model) {
		model.addRow(new Object[]{"",""});		
	}
	
	private void getNonEmpty(DefaultTableModel model) {
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			EmploymentHistoryRpt[] EmploymentHistoryRpt = null;
			EmploymentHistoryRpt = logic.getEmploymentHistoryRpt(m_sessionid,"",m_employee.getIndex());
			if (EmploymentHistoryRpt.length>0){
				for (int i=0;i<EmploymentHistoryRpt.length;i++){
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					model.addRow(new Object[]{
							String.valueOf(EmploymentHistoryRpt[i].getJobTitle()),
							String.valueOf(EmploymentHistoryRpt[i].getOrg()),
							String.valueOf(EmploymentHistoryRpt[i].getCode() + " " + EmploymentHistoryRpt[i].getDesc()),
							dateFormat.format(EmploymentHistoryRpt[i].getTMT())});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// batas
*/	
	public JasperPrint getJasperPrint(){
		return m_jasperPrint;
	}
	
	public void getEmpty(Employee employee) {
		m_employee = employee;
		m_model= new DefaultTableModel();	
		tableHeader(m_model); 
		getParamMap(m_conn, m_sessionid);
		m_model.addRow(new Object[]{"","","","","","","","","","","","","","","","","","","",""});
		close(); 
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
	
	public void getNonEmpty(Employee employee) {
		m_employee = employee;
		m_model= new DefaultTableModel();
		SexType sex = null	;
		SimpleEmployeeAttribute religion = null;
		SimpleEmployeeAttribute marital = null;		
		CurrentEmployementRpt currentEmployementRpt = null;
		EmployeeQualification[] qualifications = null;
		tableHeader(m_model); 
		getParamMap(m_conn, m_sessionid);
		try {
			sex = m_hrmlogic.getSexType(employee.getNSex());
			religion = m_hrmlogic.getReligion(employee.getNReligion());
	        marital = m_hrmlogic.getMaritalStatus(employee.getNMarital());	        	      
	        currentEmployementRpt = m_hrmlogic.getCurrentEmployementRpt(m_sessionid,"",m_employee.getIndex());
	        qualifications = m_projectlogic.getEmployeeQualificationUsingName(m_sessionid,"",m_employee.getIndex());
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		String strSex = "";
		String strReligion ="";
		String strMarital = "";
		String strQualification = "";
		
		if (sex!=null)
			strSex = sex.toString();
		if (religion!=null)
			strReligion = religion.toString();
		if (marital!=null)
			strMarital = marital.toString();
		if (qualifications!=null){
			for (int i=0;i<qualifications.length;i++){
				// aneh dalam tabel isinya ada tpi namenya null				
				if (i==(qualifications.length-1)){
					if (qualifications[i].getName()!=null)
						strQualification += strQualification + qualifications[i].getName();
				}else{
					if (qualifications[i].getName()!=null)
						strQualification += strQualification + qualifications[i].getName() + ",";
				}
			}
		}
		
		String strJobTitle = "";
		String strDept = "";
		String strUnit = "";
		if (currentEmployementRpt!=null){
			strJobTitle = currentEmployementRpt.getJobtitle();
			strDept = currentEmployementRpt.getOrg();
			strUnit = currentEmployementRpt.getCode() + " " + currentEmployementRpt.getDes();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		m_model.addRow(new Object[]{
				String.valueOf(
						m_employee.getEmployeeNo()),
						m_employee.getFirstName() + " " + m_employee.getMidleName() + " " + m_employee.getLastName(),
						dateFormat.format(m_employee.getBirthDate()) + " / " + m_employee.getBirthPlace(),
						strSex,
						strReligion,
						m_employee.getNationality(),
						strMarital,
						m_employee.getAddress(),
						m_employee.getCity(),
						String.valueOf(m_employee.getPostCode()),
						m_employee.getProvince(),
						m_employee.getPhone(),
						m_employee.getMobilePhone1(),
						m_employee.getFax(),
						m_employee.getEmail(),
						strQualification,
						strJobTitle,
						strDept,
						strUnit,						
						""});
		close();
		
	}
	
	private void tableHeader(DefaultTableModel model) {
		model.addColumn("EmpId");	
		model.addColumn("name");
		model.addColumn("birth");
		model.addColumn("sex");
		model.addColumn("religion");
		model.addColumn("nationality");
		model.addColumn("marital");
		model.addColumn("address");
		model.addColumn("city");
		model.addColumn("postcode");
		model.addColumn("province");
		model.addColumn("phone");
		model.addColumn("mobile_phone");
		model.addColumn("fax");
		model.addColumn("email");
		model.addColumn("qualification");
		model.addColumn("jobTitle");
		model.addColumn("department");
		model.addColumn("unit");		
		model.addColumn("temp");
		
		
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
			
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(), "xls"));
			exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
			exporter.exportReport();     
			
		}
	}
}