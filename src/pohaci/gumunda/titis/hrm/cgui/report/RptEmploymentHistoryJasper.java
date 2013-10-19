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

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class RptEmploymentHistoryJasper {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;  
	Employee m_employee;
	JRTableModelDataSource m_ds;
	JasperReport m_jasperReport;
	
	public RptEmploymentHistoryJasper(Connection conn,long sessionid,Employee employee) {
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
	
	private void tableHeader(DefaultTableModel model) {
		model.addColumn("jobtitle");
		model.addColumn("department");
		model.addColumn("unit");
		model.addColumn("tmt");
	}
}