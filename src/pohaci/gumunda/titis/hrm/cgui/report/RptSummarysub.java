package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Connection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
//import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.logic.PayrollSummaryLogic;

public class RptSummarysub {	
	Connection m_conn;
	long m_sessionid;
	JRTableModelDataSource m_ds;
	JasperReport m_jasperReport;
	String[] m_strTotal;
	private double[] m_total;
	private Object m_panel;
	public RptSummarysub(Object panel){
		m_panel = panel;
	}
	JComboBox m_typecombo;
	public void setType(JComboBox m_type){
		m_typecombo = m_type;
	}
	public void getRptSummarysub(PayrollComponent[] payrollComponents,EmployeePayrollSubmit[] empPayrollSubmit,
			PayrollComponent[] payrollComponentsDefault,Unit unit,Connection conn, long sessionid,Employee_n[] emp){
		m_conn=conn;
		m_sessionid = sessionid;
		try {
			String filename = "RptSummarysub.jrxml";      
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			DefaultTableModel model = new DefaultTableModel();
			header(model);
			PayrollSummaryLogic summaryLogic = new PayrollSummaryLogic(m_panel,model,
		    		  payrollComponents,emp,unit,empPayrollSubmit,payrollComponentsDefault);
			summaryLogic.setType(m_typecombo);
			summaryLogic.tableBodyReport();
			double[] dblTotal =  summaryLogic.getTotal();
			setTotal(dblTotal);
			m_ds = new JRTableModelDataSource(model);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public RptSummarysub(Connection conn, long sessionid,List totalList,List deptlist){
		m_conn=conn;
		m_sessionid = sessionid;
		try {
			String filename = "RptSummarysub.jrxml";      
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			DefaultTableModel model = new DefaultTableModel();
			header(model);
			new PayrollSummaryLogic(model,totalList,deptlist);
			m_ds = new JRTableModelDataSource(model);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void setTotal(double[] total){
		m_total = total;
	}
	
	public double[] getTotal(){
		return m_total;
	}
	
	
	private void header(DefaultTableModel model) {
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
		model.addColumn("Field13");
		model.addColumn("status");
	}
	
	
	
	
	public JRTableModelDataSource getDataSource(){
		return m_ds;
	}
	
	public JasperReport getJasperReport(){
		return m_jasperReport;
	}
}
