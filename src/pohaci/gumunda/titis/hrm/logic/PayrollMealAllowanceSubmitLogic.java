package pohaci.gumunda.titis.hrm.logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.hrm.cgui.EmployeeMealAllowanceSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollMealAllowanceSubmitPanel;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;

public class PayrollMealAllowanceSubmitLogic {
	PayrollMealAllowanceSubmitPanel m_panel;
	PayrollComponent[] m_payrollComponents;
	Employee_n[] m_emps;
	EmployeePayroll m_empPayroll;
	DefaultTableModel m_model;
	EmployeeMealAllowanceSubmit m_employeepayrollSubmit;		
	HRMBusinessLogic m_logic;
	EmployeeMealAllowanceSubmit[] m_empMealAllowSubmit;	
	Unit m_unit;
	Vector m_data = null;
	int m_mode = 0; // jika 0= tabel ; jika 1= report;
	String[] m_GajiPokok= new String[2]; // tunjangan asuransi 
    //int n=0; // nilai untuk 
    PayrollComponent[] m_payrollComponentsDefaullt;
    DecimalFormat m_formatDesimal;
	
	public PayrollMealAllowanceSubmitLogic(PayrollMealAllowanceSubmitPanel panel,DefaultTableModel model,
			PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
			EmployeeMealAllowanceSubmit[] empMealAllowSubmit,PayrollComponent[] m_payrollComponentsDefaullt, boolean excel){		
		this.m_panel = panel;
		this.m_model = model;
		this.m_empMealAllowSubmit = empMealAllowSubmit;		
		this.m_payrollComponents = payrollComponents;
		this.m_emps = emps;
		this.m_unit = unit;		
		this.m_payrollComponentsDefaullt=m_payrollComponentsDefaullt;
		if(excel)
			this.m_formatDesimal = new DecimalFormat("##0.00");
		else this.m_formatDesimal = new DecimalFormat("#,##0.00");
	}
	
	void addEmptyRow(){
		if (m_summary){
			m_model.addRow(new Object[]{"","","","","",""});
		}else{
		 m_model.addRow(new Object[]{m_panel.getUnitDescription(),m_panel.getMonthYearString(),"","","","",""});
		}
	}
	
	private void getValueComponent(PayrollCalcResult res, EmployeeMealAllowanceSubmit[] employeePayrollSubmit, int j) {
		if (m_mode == 1){
			if (res != null) {
				m_GajiPokok[j] = m_formatDesimal.format(res.value);
				m_total[j]+=res.value;
				/*if (n == 0) {
					n = n + 1;
				} else {
					n = n - 1;
				}		*/	
			}else{
				m_GajiPokok[j] = " ";
				/*if (n == 0) {
					n = n + 1;
				} else {
					n = n - 1;
				}		*/	
			}			
		}else {
			if (res != null) {
				  m_data.addElement(new Double(res.value));
			  } else {
				  m_data.addElement("");
			  }
		}
	}
			
	private int getPresenceEnabledSubmit(Employee_n emps) {
		int pResence= m_panel.presence(emps);
		Integer presencE=new Integer(pResence);
		m_data.addElement(String.valueOf(presencE)); // untuk menampilkan presence
		return pResence;
	}
	
	private int getPresenceEnabledSubmitSummary(Employee_n emps) {
		int pResence= m_panel.presence(emps);
		Integer presencE=new Integer(pResence);
		String strPresence = "";
		if (pResence>0)
			strPresence = String.valueOf(presencE);
		m_data.addElement(strPresence); // untuk menampilkan presence
		return pResence;
	}

	private PayrollCalcResult[] getResult(PayrollComponent[] payrollComponents,
			Employee_n[] emps, EmployeePayroll empPayroll, int i) throws Exception {
		PayrollCategoryComponent[] comps;
		PayrollCalcResult[] results;
		m_employeepayrollSubmit.setEmployee_n(emps[i]);
		comps = m_panel.getPayrollCategoryComponents(emps[i],
				payrollComponents);
		if (comps != null) { 
			results = empPayroll.calcPayrollComponent(comps, emps[i].getAutoindex());
		} else
			results = null;
		return results;
	}
		
	
	private void getPayrollComponentResultEnabledSubmit(PayrollComponent[] payrollComponents, PayrollCalcResult[] results, int pResence) {
		PayrollCalcResult res;
		for (int i = 0; i < payrollComponents.length; i++) {
			res = null;
			if (results != null) {
				for (int j = 0; j < results.length; j++) {
					if (payrollComponents[i].getIndex() == results[j].component.getIndex()) {
						res = results[j];
						break;
					}
				}
			}
			getValueComponent(res, null, i);	  
		}
	}
	private void getPayrollComponentForSubmit(PayrollComponent[] payrollComponents, PayrollCalcResult[] results, 
			int pResence) {
		PayrollCalcResult res;
		EmployeeMealAllowanceSubmit[] employeePayrollSubmit = new EmployeeMealAllowanceSubmit[payrollComponents.length];
		for (int i = 0; i < payrollComponents.length; i++) {
			res = null;
			employeePayrollSubmit[i] = (EmployeeMealAllowanceSubmit) m_panel.dummyClone(m_employeepayrollSubmit);	
			employeePayrollSubmit[i].setPayrollComponent(payrollComponents[i]);
			employeePayrollSubmit[i].setPresence(pResence);
			
			if (results != null) {
				for (int j = 0; j < results.length; j++) {
					if (payrollComponents[i].getIndex() == results[j].component
							.getIndex()) {
						res = results[j];
						break;
					}
				}
			}
			if (res != null) {
				employeePayrollSubmit[i].setValue((float) res.value);
			} else {
				employeePayrollSubmit[i].setValue(-1);
			}
			if (m_isnotFindEmployee)
				m_panel.m_payrollMealVector.add(employeePayrollSubmit[i]);
		}
	}

	private void getDisabledSubmit() {		
//		Employee emps=null;
		m_data = new Vector();			  
		if (m_mode==1){
			presentingDataReport();
		}else{
			presentingData();
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
	
	
	private void presentingData() {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		for(int i=0;i<m_empMealAllowSubmit.length;i++){  
			if(m_empMealAllowSubmit[i]!=null){	
				if(tempIndex!=m_empMealAllowSubmit[i].getEmployeeIndex()){
					if (m_data.size()>0)
						m_panel.m_table.addRow(m_data);
					m_data = new Vector();
					tableContent(m_empMealAllowSubmit[i], no++);
					tempIndex = m_empMealAllowSubmit[i].getEmployeeIndex();
					leftMargin = m_data.size();
				} 
				int colIdx = helper.getColumnIndex(m_empMealAllowSubmit[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value = m_empMealAllowSubmit[i].getValue();
					Object content=null;
					if(value!= -1)
						content = new Double(value);
					else
						content = "";	
					
					helper.addDataAtColumn(m_data, colIdx + leftMargin, content);
				}
			}
		}//end  for
		if (m_data.size()>0) {
			m_data.addElement("");
			m_panel.m_table.addRow(m_data);
		}
		m_panel.m_table.setModelKu();
	}
	
	private void presentingDataReport() {		
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
//		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		for(int i=0;i<m_empMealAllowSubmit.length;i++){  
			if(m_empMealAllowSubmit[i]!=null){	
				if(tempIndex!=m_empMealAllowSubmit[i].getEmployeeIndex()){
					if (m_summary){
						if (m_data.size()>0){
							m_data.addElement(m_GajiPokok[0]);
							m_data.addElement(m_GajiPokok[1]);
							m_data.addElement(new Integer(0));
							m_model.addRow(m_data);
						}
						m_data = new Vector();
						summaryContent(m_empMealAllowSubmit[i], no++);
						tempIndex= m_empMealAllowSubmit[i].getEmployeeIndex();
					}else{
						if (m_data.size()>0){
							m_data.addElement(m_GajiPokok[0]);
							m_data.addElement(m_GajiPokok[1]);
							m_model.addRow(m_data);
						}
						m_data = new Vector();
						reportContent(m_empMealAllowSubmit[i], no++);
						tempIndex= m_empMealAllowSubmit[i].getEmployeeIndex();
					}
//					leftMargin = m_data.size();
				} 
				int colIdx = helper.getColumnIndex(m_empMealAllowSubmit[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=m_empMealAllowSubmit[i].getValue();					
					if(value!= -1){
						m_total[colIdx] +=value;
						m_GajiPokok[colIdx] = m_formatDesimal.format(value);
					}else{
						m_GajiPokok[colIdx]= "";						 
					}

				}
			}
		}
		if (m_data.size()>0) {
			m_data.addElement(m_GajiPokok[0]);
			m_data.addElement(m_GajiPokok[1]);
			if (m_summary){
				m_data.addElement(new Integer(0));
			}
			m_model.addRow(m_data);
		}		
	}

	private void tableContent(EmployeeMealAllowanceSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();		
		m_data.addElement(name); 
		m_data.addElement(submit.getJobTitle());
		int presence=submit.getPresence();
		m_data.addElement(new Integer(presence));
	}
	
	private void reportContent(EmployeeMealAllowanceSubmit submit, int i) {
		m_data.add(m_panel.getUnitDescription());
		m_data.add(m_panel.getMonthYearString());
		m_data.addElement(String.valueOf(i + 1));
		m_data.addElement(submit.getEmployeeNo());
		String name=submit.getName();	
		m_data.addElement(name); 
		m_data.addElement(submit.getJobTitle());
		int presence=submit.getPresence();
		m_data.addElement(String.valueOf(new Integer(presence)));
	}
	
	private void summaryContent(EmployeeMealAllowanceSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));		
		String name=submit.getName();	
		m_data.addElement(name);
		m_data.addElement(submit.getEmployeeNo());
		int presence=submit.getPresence();
		m_data.addElement(String.valueOf(new Integer(presence)));
	}

	private void getEnabledSubmit() {
		PayrollCalcResult[] results;		
		try {			
			  for (int i = 0; i < m_emps.length; i++) {				  
				  results = getResult(m_payrollComponents, m_emps, m_empPayroll, i);
				  m_data = new Vector();
				  if (m_mode==1){ // tampilkan ke report
					  m_GajiPokok[0] = " ";
			          m_GajiPokok[1] = " ";	
			          if (m_summary){
			        	  summaryContent(m_emps[i],i);
			        	  int pResence = getPresenceEnabledSubmitSummary(m_emps[i]);			        	  
			        	  getPayrollComponentResultEnabledSubmit(m_payrollComponents, results, pResence);
			        	  m_data.addElement(m_GajiPokok[0]);
			        	  m_data.addElement(m_GajiPokok[1]);
			        	  m_data.addElement(new Integer(0));
			          }
			          else{
			        	  jasperContent(m_emps[i],i);
			        	  int pResence = getPresenceEnabledSubmit(m_emps[i]);
			        	  getPayrollComponentResultEnabledSubmit(m_payrollComponents, results, pResence);
			        	  m_data.addElement(m_GajiPokok[0]);
			        	  m_data.addElement(m_GajiPokok[1]);
			        	  m_data.addElement(Boolean.FALSE); // i add this: bukan total
			          }
			          
					  m_model.addRow(m_data);
				  }else{ // tampilkan ke table
					  tableContent(m_emps[i],i);
					  int pResence = getPresenceEnabledSubmit(m_emps[i]);
					  getPayrollComponentResultEnabledSubmit(m_payrollComponents, results, pResence);
					  getPayrollComponentForSubmit(m_payrollComponentsDefaullt, results, pResence);
					  m_panel.m_table.addRow(m_data);
					  m_panel.m_table.setModelKu();
				  }				  
			  }
			  if (m_summary){
			  	String[] strTotal = getStrValue(m_total);
			  	m_model.addRow(new Object[]{"TOTAL PEMBAYARAN PER DEPARTMENT","","","",
			  			strTotal[0] ,strTotal[1],new Integer(1)});
			  	setTotal(m_total);
			  } else {
				  m_data = new Vector();
				  String[]  strTotal = getStrValue(m_total);
				  m_data.addElement("TOTAL");
				  for(int x = 1; x <= 6; x++)
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
	
	boolean m_isnotFindEmployee=false;
	public void setIsnotFindEmployee(boolean find){
		m_isnotFindEmployee =find;
	}
	
	public void tableBody(){
		
		m_empPayroll = new EmployeePayroll(m_panel.m_conn, m_panel.getYear(), m_panel.getMonth(), m_panel.m_sessionid);
		m_employeepayrollSubmit = m_panel.getEmpMealAllowanceSubmit();
		m_logic = new HRMBusinessLogic(m_panel.m_conn);
		if (m_isnotFindEmployee)
			m_panel.m_payrollMealVector.clear();
		if(m_empMealAllowSubmit==null)
			return;
		
		//apakah sudah ada di db/submit already ato blum
		if(m_empMealAllowSubmit.length>0){
			setTextfieldStatus(m_empMealAllowSubmit[0]);
			if(m_empMealAllowSubmit[0].getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
				m_panel.m_submitBt.setEnabled(true);
				m_panel.m_updateFlag = true;
			} else{	
				m_panel.m_submitBt.setEnabled(false);
				m_panel.m_updateFlag = false;
			}
		}else{
			m_panel.m_submitBt.setEnabled(true);
			m_panel.m_statusTextField.setText(" Not Submitted");
			m_panel.m_submitdateTextField.setText("");
		}
		
		if(!m_panel.m_submitBt.isEnabled()){
			//ambil data dari DB kemudian tampilkan
			if (m_summary){
				getEnabledSubmit();
			}else{
				getDisabledSubmit();
			}
		}else{
			//calculate 
			getEnabledSubmit();
		}
	}

	private void jasperContent(Employee_n emps,int i) {		
		String name = emps.getFirstname() + " " + 
				emps.getMidlename() + " " + emps.getLastname();			  
		m_data.addElement(m_panel.getUnitDescription());
		m_data.addElement(m_panel.getMonthYearString());
		m_data.addElement(Integer.toString(i+1));
		m_data.addElement(emps.getEmployeeno());
		m_data.addElement(name);
		m_data.addElement(emps.getJobtitle());
	}
	
	private void summaryContent(Employee_n emps,int i) {		
		String name = emps.getFirstname() + " " + 
				emps.getMidlename() + " " + emps.getLastname();			  
		m_data.addElement(Integer.toString(i+1));
		m_data.addElement(name);
		m_data.addElement(emps.getEmployeeno());
	}

	private void tableContent(Employee_n emps,int i) {		
		m_data.addElement(String.valueOf(i + 1));
		m_data.addElement(emps.getFirstname() + " "
				  + emps.getMidlename() +  " "
				  + emps.getLastname()); // menampilkan name
		m_data.addElement(emps.getJobtitle()); // menampilkan job title
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
	
	double[] m_total = {0,0};
	public void setTotal(double[] total){
		m_total =  total;
	}
	
	public double[] getTotal(){
		return m_total;
	}

	boolean m_summary;
	public void tableBodyReport(boolean summary){
		m_summary = summary;
		m_mode = 1;
		if (m_emps.length==0){
			addEmptyRow();	    	  
		}else{
			tableBody();	    
		}
	}

}
