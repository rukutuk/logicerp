package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.cgui.PayrollOtherAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodStartEnd;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollOtherAllowanceSubmitPanel;

public class RptSummaryOtherAllowance{
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodStartEnd m_periodStartEnd;
	SimpleDateFormat m_dateFormat;
	DecimalFormat m_desimalFormat; 
	AccountingBusinessLogic m_logic;
	boolean m_root;
	int m_level = 0;
	JasperReport m_jasperReport;
	JasperPrint m_jasperPrint;
	PrintingViewer m_view;
	String[] m_strTotal;
	private double[] m_total;
	private PayrollOtherAllowanceSubmitPanel m_panelMealSubmit;
	RptSummaryOthersAllowanceSub m_jasper =null;
	String m_filename;
	boolean m_cekverivy;
	public RptSummaryOtherAllowance(Connection conn,long sessionid,PayrollOtherAllowanceSubmitPanel panel){
		m_conn = conn;
		m_sessionid = sessionid;
		m_panelMealSubmit = panel;
		m_jasper= new RptSummaryOthersAllowanceSub(m_panelMealSubmit);
		m_filename = "RptSummaryOthersAllowance.jrxml";
		m_cekverivy = false;
	}
	
	PayrollOtherAllowanceVerificationPanel m_panelmealverivy;
	public RptSummaryOtherAllowance(Connection conn,long sessionid,PayrollOtherAllowanceVerificationPanel panel){
		m_conn = conn;
		m_sessionid = sessionid;
		m_panelmealverivy = panel;
		m_jasper= new RptSummaryOthersAllowanceSub(m_panelmealverivy);
		m_filename = "RptSummaryOthersAllowance.jrxml";
		m_cekverivy=true;
	}


	
	public void setRptSummarySubmit(PayrollComponent[] payrollComponents,EmployeePayrollSubmit[] empMealAllowSubmit,
			PayrollComponent[] payrollComponentsDefault,Employee_n[] emp,
			UnitPicker unitPicker,Organization org,String period,String date){
		try {
						
			m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			if(m_filename.equals(""))
				return;      
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn(null);
			model.addRow(new Object[]{null});
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + m_filename);
			Map parameters = new HashMap();
			
			m_jasper.RptSummaryOtherAllowanceSubmit(payrollComponents,empMealAllowSubmit,payrollComponentsDefault,unitPicker.getUnit(),
					m_conn,m_sessionid,emp);
			double[] dblTotal = m_jasper.getTotal();
			setTotal(dblTotal);
			
			Unit unit = new Unit(unitPicker.getUnit(), Unit.CODE_DESCRIPTION);
			org = new Organization(org, Organization.CODE_NAME);
			
			if (unitPicker.getUnit()!=null)
				parameters.put("param_unit_code",unit.toString());
			else
				parameters.put("param_unit_code","");
			if (org!=null)
				parameters.put("param_dept_code",org.toString());
			else
				parameters.put("param_dept_code","");
			
			parameters.put("param_period",period);
			
			parameters.put("param_date",date);
			
			parameters.put("datasource1",m_jasper.getDataSource());
			parameters.put("subreport1",m_jasper.getJasperReport());
			JRTableModelDataSource ds = new JRTableModelDataSource(model);  
			m_jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void setRptSummarySubmit2(List totalList,List deptlist,UnitPicker unitPicker,String period,String date){
		try {
			
			if(m_filename.equals(""))
				return;      
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn(null);
			model.addRow(new Object[]{null});
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + m_filename);
			Map parameters = new HashMap();
			RptSummaryOthersAllowanceSub jasper =null;
			jasper= new RptSummaryOthersAllowanceSub(m_conn,m_sessionid,totalList,deptlist);
			
			if (unitPicker.getUnit()!=null)
				parameters.put("param_unit_code",unitPicker.getUnit().toString());
			else
				parameters.put("param_unit_code","");
			parameters.put("param_dept_code","ALL");
			
			parameters.put("param_period",period);
			
			parameters.put("param_date",date);
			
			parameters.put("datasource1",jasper.getDataSource());
			parameters.put("subreport1",jasper.getJasperReport());
			JRTableModelDataSource ds = new JRTableModelDataSource(model);  
			m_jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	

	public void setRptSummaryVerivy(PayrollComponent[] payrollComponents, EmployeePayrollSubmit[] empMealAllowSubmit,UnitPicker unitPicker,Organization org,String period,String date) {
		try {
			m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			if(m_filename.equals(""))
				return;      
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn(null);
			model.addRow(new Object[]{null});
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + m_filename);
			Map parameters = new HashMap();	
			
			m_jasper.getRptSummaryVerivysub(payrollComponents,empMealAllowSubmit);
			 double[] dblTotal = m_jasper.getTotal();
			 setTotal(dblTotal);
			
			Unit unit = new Unit(unitPicker.getUnit(), Unit.CODE_DESCRIPTION);
			org = new Organization(org, Organization.CODE_NAME);
			
			if (unitPicker.getUnit()!=null)
				parameters.put("param_unit_code",unit.toString());
			else
				parameters.put("param_unit_code","");
			if (org!=null)
				parameters.put("param_dept_code",org.toString());
			else
				parameters.put("param_dept_code","");
			
			parameters.put("param_period",period);
			
			parameters.put("param_date",date);
			
			parameters.put("datasource1",m_jasper.getDataSource());
			parameters.put("subreport1",m_jasper.getJasperReport());
			JRTableModelDataSource ds = new JRTableModelDataSource(model);  
			m_jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void setTotal(double[] total){
		m_total = total;
	}
	
	public double[] getTotal(){
		return m_total;
	}
		
	public JasperPrint getJasperPrint(){
		return m_jasperPrint;
	}
	
	public void getPrintView(){
		try {
			m_view = new PrintingViewer(m_jasperPrint);
			m_view.setTitle("Summary meal report");
			m_view.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
