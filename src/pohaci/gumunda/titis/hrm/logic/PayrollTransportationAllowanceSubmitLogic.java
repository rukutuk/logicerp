package pohaci.gumunda.titis.hrm.logic;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeeTransportationAllowance;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollSubmitType;
import pohaci.gumunda.titis.hrm.cgui.PayrollTransportationAllowanceSubmitPanel;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;

public class PayrollTransportationAllowanceSubmitLogic {
	PayrollTransportationAllowanceSubmitPanel m_panel;
	DefaultTableModel m_model;
	Unit m_unit;
	Employee_n[] m_emps;
	PayrollComponent[] m_payrollComponents;
	int m_mode = 0;
	int n = 0;
	String[] m_Transport= new String[2]; // tunjangan transport 
	EmployeePayroll m_empPayroll = null;
	EmployeePayrollSubmit m_employeepayrollSubmit;
	HRMBusinessLogic m_logic;
	EmployeeTransportationAllowance[] empTransportAllow=null;
	Vector m_data = null;
	private PayrollComponent[] m_payrollComponentsDefault;
	EmployeeTransportationAllowance[] m_empTranSubmit;
	DecimalFormat m_formatDesimal;
	public PayrollTransportationAllowanceSubmitLogic(PayrollTransportationAllowanceSubmitPanel panel,
			DefaultTableModel model,PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
			EmployeeTransportationAllowance[] empTranSubmit,PayrollComponent[] m_payrollComponentsDefault, 
			boolean excel){
		this.m_panel = panel;
		this.m_model = model;
		this.m_unit = unit;
		this.m_emps = emps;
		this.m_payrollComponents = payrollComponents;
		this.m_empTranSubmit = empTranSubmit;
		this.m_payrollComponentsDefault=m_payrollComponentsDefault;
		if(excel)
			this.m_formatDesimal = new DecimalFormat("###.00");
		else this.m_formatDesimal = new DecimalFormat("#,##0.00");
	}
	

	private void getValueComponent(EmployeeTransportationAllowance[] transportationAllowance, PayrollCalcResult res, int j) {		
		if (m_mode ==0){ 
			if (res != null) {
				  m_data.addElement(new Double(res.value));
			  } else {
				  m_data.addElement("");
			  }
		}else{
			if (res != null) {
				m_total[n]+=res.value;
				m_Transport[n]="";
				if (res.value>0)
					m_Transport[n] = m_formatDesimal.format(res.value);				
			}else{
				m_Transport[n] = " ";
			}		
		}
	}
	
	void addEmptyRow(){
		m_model.addRow(new Object[]{m_panel.getUnitDescription(),m_panel.getMonthYearString(),"","","","",""});
	}
	
	
	private PayrollCalcResult[] getPayrollCalcResult(int i) throws Exception {
		PayrollCalcResult[] results;
		PayrollCategoryComponent[] comps;
		m_employeepayrollSubmit.setEmployee_n(m_emps[i]);
		  comps = m_panel.getPayrollCategoryComponents(m_emps[i],
				  m_payrollComponents);
		  if (comps != null) {
			  m_empPayroll.setPeriode(m_panel.periodeCombox.getSelectedIndex());
			  results = m_empPayroll.calcPayrollComponent(comps,m_emps[i].getAutoindex());
		  } else
			  results = null;
		return results;
	}
	
	private void loopPayrollCompenents(PayrollCalcResult[] results, int presentlate, int presentNotlate, int presence, EmployeeTransportationAllowance[] transportationAllowance) {
		PayrollCalcResult res;
		
		for (int j = 0; j < m_payrollComponents.length; j++) {
			if (m_payrollComponents[j].getAccount().getCode().equals("5.01.01.09"))
				n=0;
			else
				n=1;
			res = null;
			if (results != null) {
				for (int k = 0; k < results.length; k++) {
					if (m_payrollComponents[j].getIndex() == results[k].component
							.getIndex()) {
						res = results[k];
						break;
					}
				}
			}			
			getValueComponent(transportationAllowance, res, j);
		}
	}
	private void getPayrollCompenentsForSubmit(PayrollCalcResult[] results, int presentlate, int presentNotlate, int presence) {
		PayrollCalcResult res;
		EmployeeTransportationAllowance[] transportationAllowance=new EmployeeTransportationAllowance[m_payrollComponentsDefault.length];
		for (int j = 0; j < m_payrollComponentsDefault.length; j++) {
			res = null;
			transportationAllowance[j] = (EmployeeTransportationAllowance) m_panel.dummyClone(m_employeepayrollSubmit);	
			transportationAllowance[j].setPayrollComponent(m_payrollComponentsDefault[j]);
			transportationAllowance[j].setPresence(presence);
			transportationAllowance[j].setPresenceLate(presentlate);
			transportationAllowance[j].setPresenceNotLate(presentNotlate);
			if (results != null) {
				for (int k = 0; k < results.length; k++) {
					if (m_payrollComponentsDefault[j].getIndex() == results[k].component
							.getIndex()) {
						res = results[k];
						break;
					}
				}
			}
			if (res != null) {						
				transportationAllowance[j].setValue((float)res.value);
			} else {
				transportationAllowance[j].setValue(-1);
			}
			if (m_isnotFindEmployee)
				m_panel.payrollTransportationVector.add(transportationAllowance[j]);
		}
	}
	
	
//	private Employee getEmployee(int k, Employee em) {
//		try {
//			  em = m_logic.getEmployeeByIndex(m_panel.m_sessionid,
//					  IDBConstants.MODUL_MASTER_DATA,empTransportAllow[k].getEmployeeIndex());			  
//		  } catch (Exception e1) {
//			  e1.printStackTrace();
//		  }
//		return em;
//	}
	private void getSubmit(EmployeeTransportationAllowance[] empTransportAllow) {
		
		if (m_mode==0){ 
			presentingData(empTransportAllow);
		}else{ 
			presentingDataReport(empTransportAllow);
		}
		
	}
	PayrollColumnHelper helper = new PayrollColumnHelper();
	private void presentingData(EmployeeTransportationAllowance[] empTransportAllow) {
			long tempIndex=-1;
			int no=0;
			int leftMargin = 0;
			helper.prepareColumnHelper(m_payrollComponents);
			m_data = new Vector();
			for(int i=0;i<empTransportAllow.length;i++){  
				if(empTransportAllow[i]!=null){	
					if(tempIndex!=empTransportAllow[i].getEmployeeIndex()){
						if (m_data.size()>0)
							m_panel.m_table.addRow(m_data);
						m_data = new Vector();
						tableContent(empTransportAllow[i], no++);
						tempIndex= empTransportAllow[i].getEmployeeIndex();
						leftMargin = m_data.size();
					} 
					int colIdx = helper.getColumnIndex(empTransportAllow[i].getPayrollComponentIndex());
					if(colIdx!=-1){
						double value=empTransportAllow[i].getValue();
						Object content=null;
						if(value!= -1){
							 //content = new Float(value);
							 content = new Double(value);							  
						}else{							
							 content = "";	
						}
						helper.addDataAtColumn(m_data, colIdx+ leftMargin, content);
					}
				}
			}
			if (m_data.size()>0) {
				m_data.addElement("");
				m_panel.m_table.addRow(m_data);
			}
			m_panel.m_table.setModelKu();
		
	}
	
	private void presentingDataReport(EmployeeTransportationAllowance[] empTransportAllow) {
		long tempIndex=-1;
		int no=0;
//		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		for(int i=0;i<empTransportAllow.length;i++){  
			if(empTransportAllow[i]!=null){	
				if(tempIndex!=empTransportAllow[i].getEmployeeIndex()){
					if (m_data.size()>0){
						m_data.addElement(m_Transport[0]);
						m_data.addElement(m_Transport[1]);
						m_model.addRow(m_data);
					}
					m_data = new Vector();
					reportContent(empTransportAllow[i], no++);
					tempIndex= empTransportAllow[i].getEmployeeIndex();
//					leftMargin = m_data.size();
				} 
				int colIdx = helper.getColumnIndex(empTransportAllow[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empTransportAllow[i].getValue();					
					if(value!= -1){
						m_Transport[colIdx] = m_formatDesimal.format(value);
					}else{
						 m_Transport[colIdx] = "";						 
					}
				}
			}
		}
		if (m_data.size()>0) {			
			m_data.addElement(m_Transport[0]);
			m_data.addElement(m_Transport[1]);
			m_model.addRow(m_data);
		}		
	
}
	
	private void tableContent(EmployeeTransportationAllowance submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();	
		m_data.addElement(name); 
		m_data.addElement(submit.getJobTitle());
		int presence=submit.getPresence();
		int presenceLate=submit.getPresenceLate();
		int presenceNotLate=submit.getPresenceNotLate();
		m_data.addElement(new Integer(presence));
		m_data.addElement(new Integer(presenceNotLate));
		m_data.addElement(new Integer(presenceLate));
	}
	
	private void reportContent(EmployeeTransportationAllowance submit, int i) {
		m_data.add(m_panel.getUnitDescription());
		m_data.add(m_panel.getMonthYearString());
		m_data.addElement(String.valueOf(i + 1));
		m_data.addElement(submit.getEmployeeNo());
		String name=submit.getName();
		/*if( emps.getMidleName ()!= null){
			name = emps.getFirstName() + " "
			  + emps.getMidleName() +  " "
			  + emps.getLastName();
		}else{
			name = emps.getFirstName() + " "
			  + emps.getLastName();
		}*/
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getJobTitle());
		int presence=submit.getPresence();
		int presenceLate=submit.getPresenceLate();
		int presenceNotLate=submit.getPresenceNotLate();
		m_data.addElement(String.valueOf(new Integer(presence)));
		m_data.addElement(String.valueOf(new Integer(presenceNotLate)));
		m_data.addElement(String.valueOf(new Integer(presenceLate)));
	}
	
	
	private void getNonSubmit() {
		System.err.println("getNonSubmit");
		PayrollCalcResult[] results = null;		
		try {			
			  for (int i = 0; i < m_emps.length; i++) {
				  results = getPayrollCalcResult(i);
				  m_data = new Vector();
				  if (m_mode==0){ // jika table
					  tableContent(i);
					  int presentlate = m_panel.presentLate(m_emps[i],m_panel.periodeCombox.getSelectedIndex());
					  int presentNotlate=m_panel.presentNotLate(m_emps[i],m_panel.periodeCombox.getSelectedIndex());
					  int presence=presentlate+presentNotlate;
					  m_data.addElement(new Integer(presence));
					  m_data.addElement(new Integer(presentNotlate));
					  m_data.addElement(new Integer(presentlate));
					  
					  EmployeeTransportationAllowance[] transportationAllowance=new EmployeeTransportationAllowance[m_payrollComponents.length];
					  loopPayrollCompenents(results, presentlate, presentNotlate, presence, transportationAllowance);
					  getPayrollCompenentsForSubmit(results, presentlate, presentNotlate, presence);
					  m_panel.m_table.addRow(m_data);
					  m_panel.m_table.setModelKu();
				  } else{ // jika report
					  m_Transport[0] = " ";
					  m_Transport[1] = " ";
					  if (m_ceksummary){
						  tableContent(i);
						  int presentlate = m_panel.presentLate(m_emps[i],m_panel.periodeCombox.getSelectedIndex());
						  int presentNotlate=m_panel.presentNotLate(m_emps[i],m_panel.periodeCombox.getSelectedIndex());
						  int presence=presentlate+presentNotlate;
						  String strPresece = "";
						  String strPresentNotlate = "";
						  String strPresentLate = "";
						  if (presence>0)
						  	strPresece = String.valueOf(presence);
						  if (presentNotlate>0)
						  	strPresentNotlate =String.valueOf(presentNotlate);
						  if (presentlate>0)
						  	strPresentLate =String.valueOf(presentlate); 
						  m_data.addElement(strPresece);
						  m_data.addElement(strPresentNotlate);
						  m_data.addElement(strPresentLate);						  	
						  EmployeeTransportationAllowance[] transportationAllowance=new EmployeeTransportationAllowance[m_payrollComponents.length];
						  loopPayrollCompenents(results, presentlate, presentNotlate, presence, transportationAllowance);
						  m_data.addElement(m_Transport[0]);
						  m_data.addElement(m_Transport[1]);
						  m_data.addElement(new Integer(0));
						  m_model.addRow(m_data);
					  }else{						  
						  jasperContent(i);
						  int presentlate = m_panel.presentLate(m_emps[i],m_panel.periodeCombox.getSelectedIndex());
						  int presentNotlate=m_panel.presentNotLate(m_emps[i],m_panel.periodeCombox.getSelectedIndex());
						  int presence=presentlate+presentNotlate;
						  m_data.addElement(String.valueOf(new Integer(presence)));
						  m_data.addElement(String.valueOf(new Integer(presentNotlate)));
						  m_data.addElement(String.valueOf(new Integer(presentlate)));
						  EmployeeTransportationAllowance[] transportationAllowance=new EmployeeTransportationAllowance[m_payrollComponents.length];
						  loopPayrollCompenents(results, presentlate, presentNotlate, presence, transportationAllowance);					  
						  m_data.addElement(m_Transport[0]);
						  m_data.addElement(m_Transport[1]);
						  m_data.addElement(Boolean.FALSE); // i add this: bukan total
						  m_model.addRow(m_data);
					  }
				  }
			  }
			  if (m_ceksummary){
				  setTotal(m_total);
				  String[]  strTotal = getStrValue(m_total);
				  m_model.addRow(new Object[]{"TOTAL PER DEPARTMENT","","","","","",strTotal[0],
						  strTotal[1],new Integer(1)});
			  } else {
				  m_data = new Vector();
				  String[]  strTotal = getStrValue(m_total);
				  m_data.addElement("TOTAL");
				  for(int x = 1; x <= 8; x++)
					  m_data.addElement("");
				  m_data.addElement(strTotal[0]);
				  m_data.addElement(strTotal[1]);
				  m_data.addElement(Boolean.TRUE);
				  m_model.addRow(m_data);
				  
			  }
		  } catch (Exception e1) {
			  e1.printStackTrace();
		  }
	}


	private void tableContent(int i) {
		m_data.addElement(String.valueOf(i + 1));
		  m_data.addElement(m_emps[i].getFirstname() + " "
				  + m_emps[i].getMidlename() + " "
				  + m_emps[i].getLastname());
		  m_data.addElement(m_emps[i].getJobtitle());
	}

	private void jasperContent(int i) {
		m_data.addElement(m_panel.getUnitDescription());
		m_data.addElement(m_panel.getMonthYearString());
		m_data.addElement(String.valueOf(i + 1));
		
		m_data.addElement(m_emps[i].getEmployeeno());
		m_data.addElement(m_emps[i].getFirstname() + " "
				+ m_emps[i].getMidlename() + " "
				+ m_emps[i].getLastname());
		
		m_data.addElement(m_emps[i].getJobtitle());
	}
	
	boolean m_isnotFindEmployee = false;
	public void setIsnotFindEmployee(boolean find){
		m_isnotFindEmployee =find;
	}
	public void tableBody(){
		m_empPayroll = new EmployeePayroll(m_panel.m_conn, m_panel.getYear(), m_panel.getMonth(),m_panel.m_sessionid);
		m_employeepayrollSubmit = m_panel.getEmployeepayrollSubmit();
		m_employeepayrollSubmit.setMonth(m_panel.getMonth());
		m_employeepayrollSubmit.setYear(m_panel.getYear());
		m_employeepayrollSubmit.setUnit(m_unit);
		m_employeepayrollSubmit.setStatus(1);
		Calendar calendar=Calendar.getInstance();
		m_employeepayrollSubmit.setSubmittedDate(calendar.getTime());
		m_employeepayrollSubmit.setPayrollType(PayrollSubmitType.TRANSPORTATION_ALLOWANCE);
		m_employeepayrollSubmit.setPaymentPeriode(m_panel.periodeCombox.getSelectedIndex());
		if (m_isnotFindEmployee )
			m_panel.payrollTransportationVector.clear();
		HRMBusinessLogic logic = new HRMBusinessLogic(m_panel.m_conn);
		EmployeeTransportationAllowance[] employeeTransport=m_empTranSubmit;
		try {
			employeeTransport= (EmployeeTransportationAllowance[]) logic.getEmployeePayrollSubmit(m_panel.m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,m_employeepayrollSubmit);
			
			if(employeeTransport!=null){
				if(employeeTransport.length>0){
					setTextfieldStatus(employeeTransport[0]);
					if(employeeTransport[0].getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
						m_panel.m_submitBt.setEnabled(true);
						m_panel.updateFlag=true;
					} else{
						m_panel.m_submitBt.setEnabled(false);
						m_panel.updateFlag=false;
					}
				}else{
					m_panel.m_submitBt.setEnabled(true);
					m_panel.m_statusTextField.setText(" Not Submitted");
					m_panel.m_submitdateTextField.setText("");
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	  
		if(m_panel.m_submitBt.isEnabled()){
			getNonSubmit();
		}else{			
			if (m_ceksummary)
				getNonSubmit();
			else
				getSubmit( employeeTransport);
		}
	}
	public void setTextfieldStatus(EmployeePayrollSubmit employeeMealAll){
		if(employeeMealAll.getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
			m_panel.m_statusTextField.setText(" Not Submitted");
			m_panel.m_submitdateTextField.setText("");
		}else if(employeeMealAll.getStatus()==PayrollSubmitStatus.SUBMITTED){
			m_panel.m_statusTextField.setText(" Submitted");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(employeeMealAll.getSubmittedDate());
			m_panel.m_submitdateTextField.setText(" "+dateString);
			m_panel.m_submitBt.setEnabled(false);
		}else if(employeeMealAll.getStatus()==PayrollSubmitStatus.VERIFIED){
			m_panel.m_statusTextField.setText(" Verified");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(employeeMealAll.getSubmittedDate());
			m_panel.m_submitdateTextField.setText(" "+dateString);
			m_panel.m_submitBt.setEnabled(false);
		}else if(employeeMealAll.getStatus()==PayrollSubmitStatus.POSTED){
			m_panel.m_statusTextField.setText(" Posted");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(employeeMealAll.getSubmittedDate());
			m_panel.m_submitdateTextField.setText(" "+dateString);
			m_panel.m_submitBt.setEnabled(false);
		}
	}
	double[] m_total = {0,0};
	public void setTotal(double[] total){
		m_total = total;
	}
	public double[] getTotal(){
		return m_total;
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
	
	boolean m_ceksummary=false;
	public void tableBodyReport(boolean ceksummary){
		m_ceksummary=ceksummary;
		m_mode = 1;
		if (m_emps.length==0){
			  addEmptyRow();	    	  
	    }else{
	    	tableBody();	    
	    }
	}

}
