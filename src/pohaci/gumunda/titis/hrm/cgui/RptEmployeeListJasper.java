package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.hrm.cgui.report.RptEmploymentHistoryJasper;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class RptEmployeeListJasper {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;			
	RptEmploymentHistoryJasper m_jasper_1;
	Map m_parameters =new HashMap();
	JasperPrint m_jasperPrint = new JasperPrint();
	JasperReport m_jasperReport;
	DefaultTableModel m_model;
	HRMBusinessLogic m_hrmlogic;
	
	Employee[] m_employees;
	
	public RptEmployeeListJasper(Connection conn,long sessionid) {
		
		try {			
			this.m_sessionid = sessionid;			
			this.m_conn = conn;
			m_hrmlogic = new HRMBusinessLogic(m_conn);
			String filename = "RptEmployeeList.jrxml";
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	public JasperPrint getJasperPrint(){
		return m_jasperPrint;
	}
	
	public void getEmpty() {
		m_model= new DefaultTableModel();	
		tableHeader(m_model); 
		m_model.addRow(new Object[]{"","","","","","","","","","","","",""});
		m_parameters.put("param_logo", "../images/TS.gif");
	    m_parameters.put("param_date", "");
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
	
	public void getNonEmpty(Employee[] employees) {
		m_model= new DefaultTableModel();
		SimpleEmployeeAttribute marital = null;
		EmployeeEducation[] education = null;
		Employment[] employment = null;
		tableHeader(m_model); 
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    Date date = new Date();
		
	    m_parameters.put("param_logo", "../images/TS.gif");
	    m_parameters.put("param_date", dateFormat.format(date));
	    
	    for(int i=0; i<employees.length; i++){
	    	try {
	    		marital = m_hrmlogic.getMaritalStatus(employees[i].getNMarital());	
	    		education = m_hrmlogic.getEmployeeEducation(m_sessionid, "", employees[i].getIndex());
	    		if(education.length==0)
	    			education = null;
	    		employment = m_hrmlogic.getEmployeeEmployment(m_sessionid, "", employees[i].getIndex());
	    		if(employment.length==0)
	    			employment = null;
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		
	    	String strMarital = "";
	    	String strJobTitle = "";
			String strEdu = "";
			String strEnterDate = "";
			String strRemark = "";
			
	    	if (marital!=null)
	    		strMarital = marital.toString();
	    	if(education != null)
	    		strEdu = education[0].getGrade().getCode();
	    	if(employment != null){
	    		strJobTitle = employment[0].getJobTitle().getDescription();
	    		strEnterDate = dateFormat.format(employment[0].getTMT());
	    		strRemark = employment[0].getWorkAgreement().getCode();
	    	}
	    	
			m_model.addRow(new Object[]{
				String.valueOf(i+1),
				String.valueOf(employees[i].getEmployeeNo()),
				employees[i].getFirstName() + " " + employees[i].getMidleName() + " " + employees[i].getLastName(),
				strJobTitle,
				employees[i].getAddress(),
				employees[i].getBirthPlace(),
				dateFormat.format(employees[i].getBirthDate()),
				strEnterDate,
				employees[i].getPhone(),
				employees[i].getMobilePhone1(),
				strEdu,
				strMarital,
				strRemark});
	    }
		close();
	}
	
	private void tableHeader(DefaultTableModel model) {
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
	}
	
	public JRViewer getPrintView(){
		return m_jrv;
	}
}
