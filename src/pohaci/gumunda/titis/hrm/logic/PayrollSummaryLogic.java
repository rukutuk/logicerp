package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.cgui.PayrollPaychequeVerificationPanel;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.PayrollDeptValue;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentReportLabel;
import pohaci.gumunda.titis.hrm.cgui.PayrollPaychequeSubmitPanel;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;

public class PayrollSummaryLogic {
	Object m_panel;
	DefaultTableModel m_model;
	PayrollComponent[] m_payrollComponents;
	Employee_n[] m_emps;
	Unit m_unit;
	Vector m_data = null;
	int m_mode = 0;
	EmployeePayrollSubmit[] m_empPayrollSubmit;
	String[] m_summary= {"","","","","","","","","",""};
	EmployeePayrollSubmit[] m_employeepayrollsubmit;
	private PayrollComponent[] m_payrollComponentsDefault;
	private EmployeePayroll m_empPayroll;
	DecimalFormat m_formatDesimal;
	EmployeePayrollSubmit[] m_empPayrollComp;
	
	public PayrollSummaryLogic(Object panel,DefaultTableModel model,
			PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
			EmployeePayrollSubmit[] empPayrollSubmit,PayrollComponent[] payrollComponentsDefault){
		this.m_empPayrollSubmit = empPayrollSubmit;
		this.m_model = model;
		this.m_panel = panel;
		this.m_emps = emps;
		this.m_unit = unit;
		this.m_payrollComponents = payrollComponents;
		this.m_payrollComponentsDefault=payrollComponentsDefault;
		this.m_formatDesimal = new DecimalFormat("#,##0.00");
	}
	
	public PayrollSummaryLogic(Object panel,PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
			EmployeePayrollSubmit[] empPayrollSubmit,PayrollComponent[] payrollComponentsDefault){
		this.m_empPayrollSubmit = empPayrollSubmit;
		this.m_panel = panel;
		this.m_emps = emps;
		this.m_unit = unit;
		this.m_payrollComponents = payrollComponents;
		this.m_payrollComponentsDefault=payrollComponentsDefault;
		this.m_formatDesimal = new DecimalFormat("#,##0.00");
	}
	
	JComboBox m_typecombo;
	public void setType(JComboBox m_type){
		m_typecombo = m_type;
	}
	
	List m_totalList= new ArrayList();
	List m_deptList= new ArrayList();
	public PayrollSummaryLogic(DefaultTableModel model,List totalList,List deptlist){
		this.m_model = model;
		m_totalList = totalList;
		m_deptList = deptlist;
		this.m_formatDesimal = new DecimalFormat("#,##0.00");
		this.count = false;
		setContentTotal();
	}
	
	public void setContentTotal(){
		for (int i=0;i<m_totalList.size();i++){
			if (m_totalList!=null){
				double[] total = (double[])m_totalList.get(i);
				
				instaceReport();
				setValue(total);
				
				Organization org = null;
				String orgName ="";
				String orgCode ="";
				if (m_deptList!=null){
					org = (Organization)m_deptList.get(i);
					orgName = org.getName();
					orgCode = org.getCode();
				}
				getSumarry();
				
				m_model.addRow(new Object[]{String.valueOf(i+1),orgName,orgCode,
						m_summary[0],m_summary[1],m_summary[2],m_summary[3],m_summary[4],
						m_summary[5],m_summary[6],m_summary[7],m_summary[8],m_summary[9],new Integer(0)});				
			}
		}
		
		getTotalInString();
		m_model.addRow(new Object[]{"","","GRAND TOTAL",m_strtotal[0],m_strtotal[1],m_strtotal[2],m_strtotal[3],m_strtotal[4],m_strtotal[5],
				m_strtotal[6],m_strtotal[7],m_strtotal[8],m_strtotal[9],new Integer(1)});
	}	
	
	private void setValue(double[] total) {
		for (int i=0; i<total.length; i++) {
			m_sumarrivalue[i] = total[i];
			m_total[i] += total[i];
		}
	}
	
	private void getValueComponents(PayrollCalcResult res,int i, Employee_n employee) {
		if (res != null)	{
			
			getValueNotNullComponentNonSubmit(res.value, i, employee);
		}
	}
	
	public String cekType(){
		String strType = (String)m_typecombo.getSelectedItem();
		return strType;
	}
	
	double[] m_sumarrivalue = {0,0,0,0,0,0,0,0,0,0};
	double[] m_total = {0,0,0,0,0,0,0,0,0,0};
	private void getValueNotNullComponentNonSubmit(double value, int i, Employee_n employee) {
		value = getValue(value, i);		
		if (m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.BASIC)){
			m_sumarrivalue[0]+=value;			
			m_total[0] += value;
		}
		else if (m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.ALLOWANCE)){
			if (value > 0) {
				if (m_panel instanceof PayrollPaychequeSubmitPanel) {					
					PayrollPaychequeSubmitPanel panel = (PayrollPaychequeSubmitPanel) m_panel;
					int month = panel.m_monthComboBox.getMonth() + 1;
					int year = panel.m_yearField.getValue();
					m_sumarrivalue[1] += calcTimeSheetDay(employee, panel.m_conn, panel.m_sessionid, month, year);
					m_sumarrivalue[2] += calcTimeSheetRate(employee, panel.m_conn, panel.m_sessionid, month, year, m_payrollComponents[i]);
				}
			}
			
			m_sumarrivalue[3]+=value;
			m_total[3] += value;
		}
		else if (m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.MEDICAL)){
			m_sumarrivalue[4]+=value;
			m_total[4] += value;
		}
		else if (m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.BENEFIT)){
			m_sumarrivalue[5]+=value;
			m_total[5] += value;
		}
		else if (m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.BONUS) || 
				m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.THR)){
			m_sumarrivalue[6]+=value;
			m_total[6] += value;
		}
		else if (m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.JAMSOSTEK)){
			m_sumarrivalue[8]+=value;
			m_total[8] += value;
		} 
		
		
	}
		
	private double getValue(double value, int i) {
		if (cekType().equals((pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[0]))){
			if (m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.BONUS) ||
					m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.THR )){
				value = 0;
			}
		}
		else if(cekType().equals((pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[1]))){
			if (!m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.THR )){
				value =0; 	
			}
		}
		else if(cekType().equals((pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[2]))){
			if (!m_payrollComponents[i].getReportLabel().equals(PayrollComponentReportLabel.BONUS )){
				value =0;
			}
		}
		return value;
	}	
	
	private PayrollCalcResult[] getResult(EmployeePayroll empPayroll, int i) throws Exception {
		PayrollCategoryComponent[] comps = null;
		PayrollCalcResult[] results;
		if (m_panel instanceof PayrollPaychequeSubmitPanel) {
			PayrollPaychequeSubmitPanel panel = (PayrollPaychequeSubmitPanel) m_panel;
			comps = panel.getPayrollCategoryComponents(m_emps[i], m_payrollComponents);			
		}
		
		if(comps!=null)
			results = empPayroll.calcPayrollComponent(comps, m_emps[i].getAutoindex());
		else
			results = null;
		return results;
	}		
	
	
	/*private void reportContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));		
		m_data.addElement(submit.getName()); 
		m_data.addElement(submit.getEmployeeNo());
	}*/
	
	private void instaceReport(){
		m_sumarrivalue[0] = 0;
		m_sumarrivalue[1] = 0;
		m_sumarrivalue[2] = 0;
		m_sumarrivalue[3] = 0;
		m_sumarrivalue[4] = 0;
		m_sumarrivalue[5] = 0;
		m_sumarrivalue[6] = 0;
		m_sumarrivalue[7] = 0;
		m_sumarrivalue[8] = 0;
		m_sumarrivalue[9] = 0;
		m_summary[0] = "";
		m_summary[1] = "";
		m_summary[2] = "";
		m_summary[3] = "";
		m_summary[4] = "";
		m_summary[5] = "";
		m_summary[6] = "";
		m_summary[7] = "";
		m_summary[8] = "";
		m_summary[9] = "";
	}
	
	private void loopPayrollComponents(PayrollCalcResult[] results1, Employee_n employee) {
		PayrollCalcResult res;
		
		for(int j=0; j<m_payrollComponents.length; j++){
			res = null;
			if(results1!=null){
				for(int k=0; k<results1.length; k++){
					if(m_payrollComponents[j].getIndex()==results1[k].component.getIndex()){
						res = results1[k];
						break;
					}
				}
			}
			getValueComponents(res,j, employee);
		}
	}
	
	
	String[] m_strtotal = {"","","","","","","","","",""};
	private boolean count = true;
	
	private void getEnableSubmit() {
		PayrollCalcResult[] results1;
		try {			
			for(int i=0; i<m_emps.length; i++){
				results1 = getResult(m_empPayroll, i);
				m_data = new Vector();
				instaceReport();
				String name = m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + 
				m_emps[i].getLastname();
				loopPayrollComponents(results1, m_emps[i]);
				
				getSumarry();
				
				m_model.addRow(new Object[]{String.valueOf(i+1),name,m_emps[i].getEmployeeno(),m_summary[0],m_summary[1],m_summary[2],m_summary[3],
						m_summary[4],m_summary[5],m_summary[6],m_summary[7],m_summary[8],m_summary[9],new Integer(0)});					
			}
			getTotalInString();
			m_model.addRow(new Object[]{"","","TOTAL PEMBAYARAN PER DEPARTMENT",m_strtotal[0],m_strtotal[1],m_strtotal[2],m_strtotal[3],m_strtotal[4],m_strtotal[5],
					m_strtotal[6],m_strtotal[7],m_strtotal[8],m_strtotal[9],new Integer(1)});
			setTotal(m_total);		
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void getReportSubmit() {
		m_data = new Vector();		
		PayrollColumnHelper helper = new PayrollColumnHelper();
		helper.prepareColumnHelper(m_payrollComponentsDefault);
		if (m_panel instanceof PayrollPaychequeSubmitPanel) {
			PayrollPaychequeSubmitPanel panel = (PayrollPaychequeSubmitPanel) m_panel;
			EmployeePayrollSubmit submitObject = panel.getEmpPayrollSubmit();
			m_empPayrollComp = panel.queryEmpPayrollSubmit(submitObject);
		}
		else if (m_panel instanceof PayrollPaychequeVerificationPanel) {
			PayrollPaychequeVerificationPanel panel = (PayrollPaychequeVerificationPanel) m_panel;
			EmployeePayrollSubmit submitObject = panel.getEmpPayrollSubmit();
			m_empPayrollComp = panel.queryEmpPayrollSubmit(submitObject);
		} 
		
		if (m_isdeptval){
			for(int i=0;i<m_empPayrollSubmit.length;i++){
				instaceReport();
				getComponentTotOrgValue(m_empPayrollSubmit[i],helper);
				setTotal(m_total);
			}
		}
		else{
			for(int i=0;i<m_emps.length;i++){	
				instaceReport();
				getComponentReport(m_emps[i],helper);
				getSumarry();
				String name=m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname();
				m_model.addRow(new Object[]{String.valueOf(i+1),name,m_emps[i].getEmployeeno(),
						m_summary[0],m_summary[1],m_summary[2],m_summary[3],m_summary[4],m_summary[5],m_summary[6],m_summary[7],m_summary[8],m_summary[9],
						new Integer(0)});			
				
			}
			getTotalInString();
			m_model.addRow(new Object[]{"","","TOTAL PEMBAYARAN PER DEPARTMENT",m_strtotal[0],m_strtotal[1],m_strtotal[2],m_strtotal[3],m_strtotal[4],m_strtotal[5],
					m_strtotal[6],m_strtotal[7],m_strtotal[8],m_strtotal[9],new Integer(1)});
			setTotal(m_total);
		}
	}
	
	
	Account[] m_account =null;
	public Account[] getAccount(){
		return m_account;
	}
	public void setAccount(Account[] account){
		m_account = account;
	}
	
	private void getComponentReport(Employee_n emps,PayrollColumnHelper helper){
		
		for (int i=0;i<m_empPayrollComp.length;i++){
			if (emps.getAutoindex()==m_empPayrollComp[i].getEmployeeIndex()){
				PayrollComponent payrollComponent = helper.getPayrollComponent(m_empPayrollComp[i].getPayrollComponentIndex());
				
				double value =m_empPayrollComp[i].getValue();
				if (value>0){
					System .err.println("");
				}
				if (cekType().equals(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[0])){
					if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS) || 
							payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR )){
						value = 0;
					}
				}
				else if (cekType().equals(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[1])){
					if (!payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR)){
						value = 0;
					}
				}
				else if (cekType().equals(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[2])){
					if (!payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS)){
						value = 0;
					}
				}
				//Account[] acc = null;
				//int noacc=0;
				if (value>0){					
					if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BASIC)){
						m_sumarrivalue[0]+=value;			
						m_total[0] += value;
						System.err.println(value + " " + payrollComponent.getAccount().getName());
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.ALLOWANCE)){
						if (value > 0) {
							int month = 0;
							int year = 0;
							Connection conn = null;
							long sessionId = -1;
							
							if (m_panel instanceof PayrollPaychequeSubmitPanel) {
								PayrollPaychequeSubmitPanel panel = (PayrollPaychequeSubmitPanel) m_panel;
								month = panel.m_monthComboBox.getMonth() + 1;
								year = panel.m_yearField.getValue();
								conn = panel.m_conn;
								sessionId = panel.m_sessionid;
							} else if (m_panel instanceof PayrollPaychequeVerificationPanel) {
								PayrollPaychequeVerificationPanel panel = (PayrollPaychequeVerificationPanel) m_panel;
								month = panel.m_monthComboBox.getMonth() + 1;
								year = panel.m_yearField.getValue();
								conn = panel.m_conn;
								sessionId = panel.m_sessionid;
							}
							
							//	field allowance time sheet:
							
							double day = calcTimeSheetDay(emps, conn, sessionId, month, year);
							m_sumarrivalue[1] += day;
							
							// field allowance rate:
							double rate = calcTimeSheetRate(emps, conn, sessionId, month, year, payrollComponent);
							if ((rate * day) == value) {
								m_sumarrivalue[2] += rate;
							} else {
								// cara ini konyol:
								// untuk mengantisipasi jika ada perubahan rate
								// yaitu diganti dengan value/day
								if (day > 0) {
									m_sumarrivalue[2] += (value / day);
								}
							}
							
						}
						m_sumarrivalue[3]+=value;
						m_total[3] += value;
						System.err.println(value + " " + payrollComponent.getAccount().getName());
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.MEDICAL)){
						m_sumarrivalue[4]+=value;
						m_total[4] += value;
						System.err.println(value + " " + payrollComponent.getAccount().getName());
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BENEFIT)){
						m_sumarrivalue[5]+=value;
						m_total[5] += value;
						System.err.println(value + " " + payrollComponent.getAccount().getName());
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS) || 
							payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR)){
						m_sumarrivalue[6]+=value;
						m_total[6] += value;
						System.err.println(value + " " + payrollComponent.getAccount().getName());
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.JAMSOSTEK)){
						m_sumarrivalue[8]+=value;
						m_total[8] += value;
						System.err.println(value + " " + payrollComponent.getAccount().getName());
					}
				}
			}
		}
	}
	
	private void getComponentTotOrgValue(EmployeePayrollSubmit  empPayroll,PayrollColumnHelper helper){
		int month = 0;
		int year = 0;
		Connection conn = null;
		long sessionId = -1;
		if (m_panel instanceof PayrollPaychequeSubmitPanel) {
			PayrollPaychequeSubmitPanel panel = (PayrollPaychequeSubmitPanel) m_panel;
			month = panel.m_monthComboBox.getMonth() + 1;
			year = panel.m_yearField.getValue();
			conn = panel.m_conn;
			sessionId = panel.m_sessionid;
		} else if (m_panel instanceof PayrollPaychequeVerificationPanel) {
			PayrollPaychequeVerificationPanel panel = (PayrollPaychequeVerificationPanel) m_panel;
			month = panel.m_monthComboBox.getMonth() + 1;
			year = panel.m_yearField.getValue();
			conn = panel.m_conn;
			sessionId = panel.m_sessionid;
		}
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		//for (int i=0;i<m_empPayrollComp.length;i++){
			//if (emps.getAutoindex()==m_empPayrollComp[i].getEmployeeIndex()){
				PayrollComponent payrollComponent = helper.getPayrollComponent(empPayroll.getPayrollComponentIndex());
				
				double value =empPayroll.getValue();				
				
				//value = getValue(value, i);	
				/*if (cekType().equals((pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[0]))){
					if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS)){
						value = 0;
					}
				}
				else if(cekType().equals((pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PAYCHEQUE[1]))){
					if (!payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS)){
						value =0;
					}
				}	*/
				if (value>0){
					//if (m_empPayrollComp[i].getPayrollComponent().)
					if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BASIC)){
						m_total[0] += value;
						//m_payrolldeptvalue.setAccount(payrollComponent.getAccount());
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.ALLOWANCE)){
						//if (value > 0) {
						
						//	field allowance time sheet:
						if (empPayroll.getEmployee_n()!=null){
							double day = calcTimeSheetDay(empPayroll.getEmployee_n() , conn, sessionId, month, year);
							m_sumarrivalue[1] += day;
							
							// field allowance rate:
							double rate = calcTimeSheetRate(empPayroll.getEmployee_n() , conn, sessionId, month, year, payrollComponent);
							if ((rate * day) == value) {
								m_sumarrivalue[2] += rate;
							} else {
								// cara ini konyol:
								// untuk mengantisipasi jika ada perubahan rate
								// yaitu diganti dengan value/day
								if (day > 0) {
									m_sumarrivalue[2] += (value / day);
								}
							}
						}
						
						m_total[3] += value;
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.MEDICAL)){
						m_total[4] += value;
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BENEFIT)){
						m_total[5] += value;
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS) || 
							payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR)){
						m_total[6] += value;
					}
					else if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.JAMSOSTEK)){
						m_total[8] += value;
					}
					m_payrolldeptvalue.setAccvalue(value);
					m_payrolldeptvalue.setAccount(payrollComponent.getAccount());
					logic.createPayrollDeptValue(sessionId,IDBConstants.MODUL_ACCOUNTING,m_payrolldeptvalue);
				}
			//}
	//	}
	}
	
	private double calcTimeSheetDay(Employee_n emps, Connection conn, long sessionId, int month, int year) {
		TimeSheetCalculator calc = new TimeSheetCalculator(
				conn, sessionId, emps.getAutoindex(), year, month);
		double calcValue = calc.calcTimeSheet();
		return calcValue;
	}
	
	private double calcTimeSheetRate(Employee_n emps,
			Connection conn, long sessionId, int month, int year,
			PayrollComponent payrollComponent) {
		TimeSheetCalculator calc = new TimeSheetCalculator(conn,
				sessionId, emps.getAutoindex(), year, month);
		
		HRMBusinessLogic logic = new HRMBusinessLogic(conn);
		
		double rate = 0;
		try {
			PayrollCategoryComponent component = logic
			.getSelectedAPayrollCategoryComponent(sessionId,
					IDBConstants.MODUL_MASTER_DATA,
					emps.getAutoindex(), payrollComponent.getIndex());
			rate = calc.rate(component);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rate;
	}
	
	
	public void tableBody(){	
		if (m_panel instanceof PayrollPaychequeSubmitPanel) {
			PayrollPaychequeSubmitPanel panel = (PayrollPaychequeSubmitPanel) m_panel;
			int month = panel.m_monthComboBox.getMonth() + 1;
			int year = panel.m_yearField.getValue();
			m_empPayroll = new EmployeePayroll(panel.m_conn, year, month,panel.m_sessionid);
			if (panel.m_submitBt.isEnabled()){
				getEnableSubmit();	
			}else{
				getReportSubmit();
			}
		}else if (m_panel instanceof PayrollPaychequeVerificationPanel) {
			PayrollPaychequeVerificationPanel panel = (PayrollPaychequeVerificationPanel) m_panel;
			int month = panel.m_monthComboBox.getMonth() + 1;
			int year = panel.m_yearField.getValue();
			m_empPayroll = new EmployeePayroll(panel.m_conn, year, month,panel.m_sessionid);		
			/*if (panel.m_submitBt.isEnabled()){
			 getEnableSubmit();	
			 }else{
			 */	getReportSubmit();
			 //}
		}
		
	}
	
	public void tableBodyReport(){
		tableBody();
	}
	
	boolean m_isdeptval =  false;
	PayrollDeptValue m_payrolldeptvalue;
	public void setTotalDeptValue(PayrollDeptValue payrollDeptValue,boolean isdeptval){
		m_payrolldeptvalue = payrollDeptValue;
		m_isdeptval = isdeptval;
		tableBody();
	}
	
	private void getSumarry() {
		if (count ) {
			m_sumarrivalue[7]=m_sumarrivalue[0]+m_sumarrivalue[3]+m_sumarrivalue[4]+m_sumarrivalue[5]+m_sumarrivalue[6];
			m_total[7] += m_sumarrivalue[7];
			m_sumarrivalue[9]+=m_sumarrivalue[7]-m_sumarrivalue[8];				
			m_total[9] += m_sumarrivalue[7]-m_sumarrivalue[8];
		}
		if (m_sumarrivalue[0]>0)
			m_summary[0] = m_formatDesimal.format(m_sumarrivalue[0]);
		if (m_sumarrivalue[1]>0)
			m_summary[1] = m_formatDesimal.format(m_sumarrivalue[1]);
		if (m_sumarrivalue[2]>0)
			m_summary[2] = m_formatDesimal.format(m_sumarrivalue[2]);
		if (m_sumarrivalue[3]>0)
			m_summary[3] = m_formatDesimal.format(m_sumarrivalue[3]);
		if (m_sumarrivalue[4]>0)
			m_summary[4] = m_formatDesimal.format(m_sumarrivalue[4]);
		if (m_sumarrivalue[5]>0)
			m_summary[5] = m_formatDesimal.format(m_sumarrivalue[5]);
		if (m_sumarrivalue[6]>0)					
			m_summary[6] = m_formatDesimal.format(m_sumarrivalue[6]);
		if (m_sumarrivalue[7]>0)					
			m_summary[7] = m_formatDesimal.format(m_sumarrivalue[7]);
		if (m_sumarrivalue[8]>0)
			m_summary[8] = m_formatDesimal.format(m_sumarrivalue[8]);
		if (m_sumarrivalue[9]>0)
			m_summary[9] = m_formatDesimal.format(m_sumarrivalue[9]);
	}
	
	public void setTotal(double[] total){
		m_total =  total;
	}
	
	public double[] getTotal(){
		return m_total;
	}
	
	private void getTotalInString() {
		if (m_total[0]>0)
			m_strtotal[0] = m_formatDesimal.format(m_total[0]);
		if (m_total[1]>0)
			m_strtotal[1] = m_formatDesimal.format(m_total[1]);
		if (m_total[2]>0)
			m_strtotal[2] = m_formatDesimal.format(m_total[2]);
		if (m_total[3]>0)
			m_strtotal[3] = m_formatDesimal.format(m_total[3]);
		if (m_total[4]>0)
			m_strtotal[4] = m_formatDesimal.format(m_total[4]);
		if (m_total[5]>0)
			m_strtotal[5] = m_formatDesimal.format(m_total[5]);
		if (m_total[6]>0)
			m_strtotal[6] = m_formatDesimal.format(m_total[6]);
		if (m_total[7]>0)
			m_strtotal[7] = m_formatDesimal.format(m_total[7]);
		if (m_total[8]>0)
			m_strtotal[8] = m_formatDesimal.format(m_total[8]);
		if (m_total[9]>0)
			m_strtotal[9] = m_formatDesimal.format(m_total[9]);
	}
	
}
