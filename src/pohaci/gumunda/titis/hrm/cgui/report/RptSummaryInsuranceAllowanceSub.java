package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.PayrollInsuranceAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollMealAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.cgui.EmployeeMealAllowanceSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollInsuranceAllowanceSubmitPanel;
import pohaci.gumunda.titis.hrm.logic.PayrollInsuranceAllowanceLogic;

public class RptSummaryInsuranceAllowanceSub {	
	Connection m_conn;
	long m_sessionid;
	JRTableModelDataSource m_ds;
	JasperReport m_jasperReport;
	String[] m_strTotal;
	private double[] m_total;
	private PayrollInsuranceAllowanceSubmitPanel m_submitpanel;
	PayrollMealAllowanceVerificationPanel m_verivypanel;
	public RptSummaryInsuranceAllowanceSub(PayrollInsuranceAllowanceSubmitPanel panel){
		m_submitpanel = panel;
	}
	
	public RptSummaryInsuranceAllowanceSub(PayrollMealAllowanceVerificationPanel panel){
		m_verivypanel = panel;
	}
	
	PayrollInsuranceAllowanceVerificationPanel m_verivyinspanel;
	public RptSummaryInsuranceAllowanceSub(PayrollInsuranceAllowanceVerificationPanel panel) {
		m_verivyinspanel = panel;
	}
	
	public void RptSummaryInsuranceSubmit(PayrollComponent[] payrollComponents,EmployeePayrollSubmit[] empMealAllowSubmit,
			PayrollComponent[] payrollComponentsDefault,Unit unit,Connection conn, long sessionid,Employee_n[] emp){
		m_conn=conn;
		m_sessionid = sessionid;
		try {
			String filename = "RptSummaryInsuranceAllowancesub.jrxml";      
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			DefaultTableModel model = new DefaultTableModel();
			header(model);
			PayrollInsuranceAllowanceLogic summaryLogic=null;
			if(m_submitpanel!=null)
				summaryLogic = new PayrollInsuranceAllowanceLogic(m_submitpanel,m_submitpanel.createEmpPayroll(),model,
						emp,payrollComponents,unit,empMealAllowSubmit,payrollComponentsDefault,false);
			else
				summaryLogic = new PayrollInsuranceAllowanceLogic(m_verivyinspanel,m_verivyinspanel.createEmpPayroll(),model,
						emp,payrollComponents,unit,empMealAllowSubmit,payrollComponentsDefault,false);
			
			summaryLogic.tableBodyReport(true);
			double[] dblTotal =  summaryLogic.getTotal();
			setTotal(dblTotal);
			m_ds = new JRTableModelDataSource(model);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public RptSummaryInsuranceAllowanceSub(Connection conn, long sessionid,List totalList,List deptlist){
		m_conn=conn;
		m_sessionid = sessionid;
		//DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		double[] ttldept = {0,0};
		try {
			String filename = "RptSumDeptPayrollInsuranceAllowance.jrxml";
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			DefaultTableModel model = new DefaultTableModel();
			headertotal(model);
			int totalsize = totalList.size();			
			for (int i =0;i<totalsize;i++){
				if (totalList!=null){
					double[] total = (double[])totalList.get(i);
					Organization org = null;
					String orgName ="";
					String orgCode ="";
					if (deptlist!=null){
						org = (Organization)deptlist.get(i);
						orgName = org.getName();
						orgCode = org.getCode();
					}
					ttldept[0]+=total[0];
					ttldept[1]+=total[1];
					String[] strTotal=getStrValue(total);					
					model.addRow(new Object[]{String.valueOf(i+1),orgName,orgCode,strTotal[0],strTotal[1],new Integer(0)});
				}				
			}		
			String[] strTtlDept = getStrValue(ttldept);
			model.addRow(new Object[]{"GRAND TOTAL","","",strTtlDept[0],strTtlDept[1],new Integer(1)});
			m_ds = new JRTableModelDataSource(model);
			/*	new PayrollSummaryLogic(model,totalList,deptlist);
			 m_ds = new JRTableModelDataSource(model);*/
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	public void getRptSummaryVerivysub(PayrollComponent[] payrollComponents,EmployeeMealAllowanceSubmit[] empMealAllowSubmit){
		try {
			String filename = "RptSummaryOthersAllowancesub.jrxml";      
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			DefaultTableModel model = new DefaultTableModel();
			header(model);
			
			double[] dblTotal = null;
			if(m_verivypanel!=null){
				m_verivypanel.presentingDataSummary(empMealAllowSubmit,payrollComponents,model);
				dblTotal =  m_verivypanel.getTotal();
			}
			else if(m_verivypanel!=null){
				this.m_verivyinspanel.presentingDataSummary(empMealAllowSubmit,payrollComponents,model);
				dblTotal =  m_verivyinspanel.getTotal();
			}
				
			setTotal(dblTotal);
			m_ds = new JRTableModelDataSource(model);
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	public String[] getStrValue(double[] value){
		DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		String[] str ={"",""};
		if (value[0]>0)
			str[0]=formatDesimal.format(value[0]);
		if (value[1]>0)
			str[1]=formatDesimal.format(value[1]);
		return str;
	}
	
	public void setTotal(double[] total){
		m_total = total;
	}
	
	public double[] getTotal(){
		return m_total;
	}
	
	private void header(DefaultTableModel model) {
		model.addColumn("Field0");
		model.addColumn("Field1");
		model.addColumn("Field2");
		model.addColumn("Field3");
		model.addColumn("Field4");
		model.addColumn("status");
	}
	
	private void headertotal(DefaultTableModel model) {
		model.addColumn("Field0");
		model.addColumn("Field1");
		model.addColumn("Field2");
		model.addColumn("Field3");
		model.addColumn("Field4");
		model.addColumn("status");
	}
	
	public JRTableModelDataSource getDataSource(){
		return m_ds;
	}
	
	public JasperReport getJasperReport(){
		return m_jasperReport;
	}
}
