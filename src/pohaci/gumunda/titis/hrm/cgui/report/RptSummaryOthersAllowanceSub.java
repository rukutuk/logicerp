package pohaci.gumunda.titis.hrm.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.PayrollOtherAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollOtherAllowanceSubmitPanel;
import pohaci.gumunda.titis.hrm.logic.PayrollOtherAllowancoLogic;

public class RptSummaryOthersAllowanceSub {	
	Connection m_conn;
	long m_sessionid;
	JRTableModelDataSource m_ds;
	JasperReport m_jasperReport;
	String[] m_strTotal;
	private double[] m_total;
	private PayrollOtherAllowanceSubmitPanel m_submitpanel;
	PayrollOtherAllowanceVerificationPanel m_verivypanel;
	public RptSummaryOthersAllowanceSub(PayrollOtherAllowanceSubmitPanel panel){
		m_submitpanel = panel;
	}
	
	public RptSummaryOthersAllowanceSub(PayrollOtherAllowanceVerificationPanel panel){
		m_verivypanel = panel;
	}
	
	public void RptSummaryOtherAllowanceSubmit(PayrollComponent[] payrollComponents,EmployeePayrollSubmit[] empMealAllowSubmit,
			PayrollComponent[] payrollComponentsDefault,Unit unit,Connection conn, long sessionid,Employee_n[] emp){
		m_conn=conn;
		m_sessionid = sessionid;
		try {
			String filename = "RptSummaryOthersAllowancesub.jrxml";      
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			DefaultTableModel model = new DefaultTableModel();
			header(model);
			PayrollOtherAllowancoLogic summaryLogic = new PayrollOtherAllowancoLogic(m_submitpanel,m_submitpanel.createEmpPayroll(),model,
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
	
	public RptSummaryOthersAllowanceSub(Connection conn, long sessionid,List totalList,List deptlist){
		m_conn=conn;
		m_sessionid = sessionid;
		//DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
		double[] ttldept = {0,0};
		try {
			String filename = "RptSumDeptPayrollOthersAllowance.jrxml";      
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
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void getRptSummaryVerivysub(PayrollComponent[] payrollComponents,EmployeePayrollSubmit[] empMealAllowSubmit){
		try {
			String filename = "RptSummaryOthersAllowancesub.jrxml";      
			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			DefaultTableModel model = new DefaultTableModel();
			header(model);
			
			m_verivypanel.presentingDataSummary(empMealAllowSubmit,payrollComponents,model);
			
			double[] dblTotal =  m_verivypanel.getTotal();
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
