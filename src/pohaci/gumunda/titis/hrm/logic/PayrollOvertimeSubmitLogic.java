package pohaci.gumunda.titis.hrm.logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.hrm.cgui.EmployeeOvertimeSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollOvertimeSubmitPanel;
import pohaci.gumunda.titis.hrm.cgui.PayrollSubmitType;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;

public class PayrollOvertimeSubmitLogic {
	PayrollOvertimeSubmitPanel m_panel;
	PayrollComponent[] m_payrollComponents;
	DefaultTableModel m_model;
	Employee_n[] m_emps;
	Unit m_unit;
	Vector m_data = null;
	HRMBusinessLogic m_logic;
	EmployeePayroll m_empPayroll = null;
	int m_mode = 0; // Jika 0= tabel ; Jika 1= report;
	String[] m_Lembur= new String[2]; // Tunjangan Asuransi
	int n=0; // Nilai Untuk
	EmployeeOvertimeSubmit[] m_empOverTimeSubmit;
	private PayrollComponent[] m_payrollComponentsDefault;
	DecimalFormat m_formatDesimal;
	public PayrollOvertimeSubmitLogic(PayrollOvertimeSubmitPanel panel,DefaultTableModel model,
			PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
			EmployeeOvertimeSubmit[] empOverTimeSubmit,PayrollComponent[] m_payrollComponentsDefault,
			boolean excel){
		this.m_payrollComponents = payrollComponents;
		this.m_emps = emps;
		this.m_model = model;
		this.m_panel = panel;
		this.m_unit = unit;
		this.m_empOverTimeSubmit = empOverTimeSubmit;
		this.m_payrollComponentsDefault = m_payrollComponentsDefault;
		this.m_logic = new HRMBusinessLogic(m_panel.m_conn);
		if(excel)
			this.m_formatDesimal = new DecimalFormat("###.00");
		else this.m_formatDesimal = new DecimalFormat("#,##0.00");
	}
	
	void addEmptyRow(){
		m_model.addRow(new Object[]{m_panel.getUnitDescription(),m_panel.getMonthYearString(),"","","","",""});
	}
	
	
	private void getValueComponent(PayrollCalcResult res, EmployeeOvertimeSubmit[] employeePayrollSubmit, int j) {
		if (m_mode==1){
			if (res != null) {
				m_Lembur[n] = m_formatDesimal.format(res.value);
				m_total[n]+=res.value;
				if (n == 0) {
					n = n + 1;
				} else {
					n = n - 1;
				}
			}else{
				m_Lembur[n] = "";;
				if (n == 0) {
					n = n + 1;
				} else {
					n = n - 1;
				}
			}
		}else{
			if (res != null) {
				m_data.addElement(new Double(res.value));
			} else {
				m_data.addElement("");
			}
		}
	}
	
	private PayrollCalcResult[] getResult(EmployeePayroll empPayroll, PayrollCategoryComponent[] comps, int i) {
		PayrollCalcResult[] results;
		if (comps != null) {
			results = empPayroll.calcPayrollComponent(comps,
					m_emps[i].getAutoindex());
		} else
			results = null;
		return results;
	}
	
	private void getLoopPayrollComponent(EmployeeOvertimeSubmit employeepayrollSubmit, PayrollCalcResult[] results) {
		PayrollCalcResult res;
		for (int j = 0; j < m_payrollComponents.length; j++) {
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
			getValueComponent(res, null, j);
		}
	}
	private void getPayrollComponentForSubmit(EmployeeOvertimeSubmit employeepayrollSubmit, PayrollCalcResult[] results) {
		PayrollCalcResult res;
		EmployeeOvertimeSubmit[] employeePayrollSubmit = new EmployeeOvertimeSubmit[m_payrollComponentsDefault.length];
		for (int j = 0; j < m_payrollComponentsDefault.length; j++) {
			res = null;
			employeePayrollSubmit[j] = (EmployeeOvertimeSubmit) m_panel.dummyClone(employeepayrollSubmit);
			employeePayrollSubmit[j].setPayrollComponent(m_payrollComponentsDefault[j]);
			employeePayrollSubmit[j].setOverTime(m_panel.overTimeLESSThanOneHour,
					m_panel.overTimeMOREThanOneHour,m_panel.overTimeZeroToSevenHour,
					m_panel.overTimeEightHour,m_panel.overTimeMoreThanNineHour);
			
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
				//m_data.addElement(new Double(res.value));
				employeePayrollSubmit[j].setValue((double) res.value);
			} else {
				//m_data.addElement("");
				employeePayrollSubmit[j].setValue(-1);
			}
			if (m_isnotFindEmployee)
				m_panel.payrollOvertimeVector.add(employeePayrollSubmit[j]);
		}
	}
	
	private void getNonSubmit(EmployeeOvertimeSubmit employeepayrollSubmit) {
		PayrollCategoryComponent[] comps;
		PayrollCalcResult[] results;
		try {
			for (int i = 0; i < m_emps.length; i++) {
				comps = m_panel.getPayrollCategoryComponents(m_emps[i],
						m_payrollComponents);
				/*m_panel.getOvertimeWorkingDay(m_emps[i]);
				m_panel.getOvertimeNonWorkingDay(m_emps[i]);*/
				
				m_panel.getOvertime(m_emps[i]);
				
				m_empPayroll.setOverTime(m_panel.overTimeLESSThanOneHour,m_panel.overTimeMOREThanOneHour,
						m_panel.overTimeZeroToSevenHour,m_panel.overTimeEightHour,m_panel.overTimeMoreThanNineHour);
				results = getResult(m_empPayroll, comps, i);
				m_data = new Vector();
				if (m_mode==0){
					m_data.addElement(String.valueOf(i + 1));
					m_data.addElement(m_emps[i].getFirstname() + " "+ m_emps[i].getMidlename() + " "
							+ m_emps[i].getLastname()); 	
					employeepayrollSubmit.setEmployee_n(m_emps[i]);
					m_data.addElement(m_emps[i].getJobtitle());
					m_data.addElement(new Float(m_panel.overTimeLESSThanOneHour));
					m_data.addElement(new Float(m_panel.overTimeMOREThanOneHour));
					m_data.addElement(new Float(m_panel.overTimeZeroToSevenHour));
					m_data.addElement(new Float(m_panel.overTimeEightHour));
					m_data.addElement(new Float(m_panel.overTimeMoreThanNineHour));
					getLoopPayrollComponent(employeepayrollSubmit, results);
					getPayrollComponentForSubmit(employeepayrollSubmit, results);
					m_panel.m_table.addRow(m_data);
					m_panel.m_table.setModelKu();
				}else{
					if (m_ceksummary){
						m_data.addElement(Integer.toString(i+1));
						m_data.addElement(m_emps[i].getFirstname() + " " +
								m_emps[i].getMidlename() + " " + m_emps[i].getLastname());
						employeepayrollSubmit.setEmployee_n(m_emps[i]);
						m_data.addElement(m_emps[i].getJobtitle());
						m_data.addElement(getResult(m_panel.overTimeLESSThanOneHour));
						m_data.addElement(getResult(m_panel.overTimeMOREThanOneHour));
						m_data.addElement(getResult(m_panel.overTimeZeroToSevenHour));
						m_data.addElement(getResult(m_panel.overTimeEightHour));
						m_data.addElement(getResult(m_panel.overTimeMoreThanNineHour));
						getLoopPayrollComponent(employeepayrollSubmit, results);
						m_data.addElement(m_Lembur[0]);
						m_data.addElement(m_Lembur[1]);
						m_data.addElement(new Integer(0));
						m_model.addRow(m_data);
					}else{
						m_Lembur[0] = "";
						m_Lembur[1] = "";
						m_data.addElement(m_panel.getUnitDescription());
						m_data.addElement(m_panel.getMonthYearString());
						m_data.addElement(Integer.toString(i+1));
						m_data.addElement(m_emps[i].getEmployeeno());
						m_data.addElement(m_emps[i].getFirstname() + " " +
								m_emps[i].getMidlename() + " " + m_emps[i].getLastname());
						employeepayrollSubmit.setEmployee_n(m_emps[i]);
						m_data.addElement(m_emps[i].getJobtitle());
						m_data.addElement("");
						m_data.addElement(String.valueOf(new Float(m_panel.overTimeLESSThanOneHour)));
						m_data.addElement(String.valueOf(new Float(m_panel.overTimeMOREThanOneHour)));
						m_data.addElement(String.valueOf(new Float(m_panel.overTimeZeroToSevenHour)));
						m_data.addElement(String.valueOf(new Float(m_panel.overTimeEightHour)));
						m_data.addElement(String.valueOf(new Float(m_panel.overTimeMoreThanNineHour)));
						getLoopPayrollComponent(employeepayrollSubmit, results);
						m_data.addElement(m_Lembur[0]);
						m_data.addElement(m_Lembur[1]);
						m_data.addElement(Boolean.FALSE); // i add this
						m_model.addRow(m_data);
					}
				}
			}
			if (m_ceksummary){
				String[] strTotal =  getStrTotal(m_total);
				m_model.addRow(new Object[]{"TOTAL PEMBAYARAN PER DEPARTMENT","","","","","","","",
						strTotal[0],strTotal[1],new Integer(1)});
				setTotal(m_total);
			} else {
				m_data = new Vector();
				String[] strTotal = getStrTotal(m_total);
				m_data.addElement("TOTAL");
				for (int x = 1; x <= 11; x++)
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
	
	
	private void getSubmit(EmployeeOvertimeSubmit[] empOvertimeSubmit) {
		if (m_mode==0){
			presentingData(empOvertimeSubmit);
		}else{
			presentingDataReport(empOvertimeSubmit);
		} 	
	}
	
	private void presentingDataReport(EmployeeOvertimeSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		float valJam = 0;
		if (m_ceksummary){
			List dataList = new ArrayList();
			for (int j=0;j<m_emps.length;j++){
				for(int i=0;i<empPaySub.length;i++){
					if (m_emps[j].getAutoindex()==empPaySub[i].getEmployeeIndex())
						dataList.add(empPaySub[i]);
				}
			}
			empPaySub = (EmployeeOvertimeSubmit[]) dataList.toArray(new EmployeeOvertimeSubmit[dataList.size()]);
			double[] total ={0,0};
			double[] subtotal ={0,0};
			for(int i=0;i<empPaySub.length;i++){
				if(empPaySub[i]!=null){
					
					if(tempIndex !=empPaySub[i].getEmployeeIndex()){
						if (m_data.size()>0){
							total[0]+=subtotal[0];
							total[1]+=subtotal[1];
							m_data.set(8,m_Lembur[0]);
							m_data.set(9,m_Lembur[1]);
							m_data.set(10,new Integer(0));
							m_model.addRow(m_data);
							valJam = 0;
						}
						
						m_data = new Vector();
						m_Lembur[0] ="";
						m_Lembur[1] ="";
						subtotal[0]=0;
						subtotal[1]=0;
						m_data.addElement(Integer.toString(++no));
						m_data.addElement(empPaySub[i].getName());
						m_data.addElement(empPaySub[i].getJobTitle());
						tempIndex= empPaySub[i].getEmployeeIndex();
					}
					m_data.setSize(14);
					valJam+= empPaySub[i].getOvertimeValue();
					reportContentsummary(empPaySub[i],empPaySub[i].getMultiplierIndex() );
					int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
					if(colIdx!=-1){
						double value=empPaySub[i].getValue();
						if(value!= -1){
								subtotal[colIdx]=value;
							if (value>0)	
								m_Lembur[colIdx] = m_formatDesimal.format(value);
						}else{
							m_Lembur[colIdx] = "";
						}
					}
				}				
			}
			if (m_data.size()>0) {
				total[0]+=subtotal[0];
				total[1]+=subtotal[1];
				m_data.set(8,m_Lembur[0]);	
				m_data.set(9,m_Lembur[1]);
				m_data.set(10,new Integer(0));
				m_model.addRow(m_data);
			}
			String[] strTotal =getStrTotal(total);			
			m_model.addRow(new Object[]{"TOTAL PEMBAYARAN PER DEPARTMENT","","","","","","","",
					strTotal[0],strTotal[1],new Integer(1)});
			setTotal(total);
		}else{
			for(int i=0;i<empPaySub.length;i++){
				if(empPaySub[i]!=null){
					if(tempIndex !=empPaySub[i].getEmployeeIndex()){
						if (m_data.size()>0){
							m_data.set(12,m_Lembur[0]);
							m_data.set(13,m_Lembur[1]);
							if (m_ceksummary){
								m_data.addElement(new Integer(0));
							}
							m_model.addRow(m_data);
							valJam = 0;
						}
						m_data = new Vector();
						reportContent(empPaySub[i], no++);
						tempIndex= empPaySub[i].getEmployeeIndex();
					}
					m_data.setSize(14);
					valJam+= empPaySub[i].getOvertimeValue();
					m_data.set(6,String.valueOf(valJam/2));
					
					reportContentOvertime(empPaySub[i],empPaySub[i].getMultiplierIndex() );
					
					int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
					if(colIdx!=-1){
						double value=empPaySub[i].getValue();
						if(value!= -1){
							m_Lembur[colIdx] = m_formatDesimal.format(value);
						}else{
							m_Lembur[colIdx] = "";;
						}
					}
				}
			}
			
			if (m_data.size()>0) {
				m_data.set(12,m_Lembur[0]);
				m_data.set(13,m_Lembur[1]);
				m_model.addRow(m_data);
			}
			m_panel.m_table.setModelKu();
		}
	}
	private void presentingData(EmployeeOvertimeSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		for(int i=0;i<empPaySub.length;i++){
			if(empPaySub[i]!=null){
				if(tempIndex !=empPaySub[i].getEmployeeIndex()){
					if (m_data.size()>0)
						m_panel.m_table.addRow(m_data);
					m_data = new Vector();
					tableContent(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
					leftMargin = m_data.size();
				}
				
				tableContentOvertime(empPaySub[i],empPaySub[i].getMultiplierIndex());
				
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empPaySub[i].getValue();
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
	private void tableContentOvertime(EmployeeOvertimeSubmit overtimeSubmit, long dex){
		long index=dex;
		float value=overtimeSubmit.getOvertimeValue();
		if(index==1){
			m_data.set(3,new Float(value));
		}else if(index==2){
			m_data.set(5,new Float(value));
		}else if(index==3){
			m_data.set(4,new Float(value));
		}else if(index==4){
			m_data.set(6,new Float(value));
		}else if(index==5){
			m_data.set(7,new Float(value));
		}
	}
	
	private void tableContent(EmployeeOvertimeSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getJobTitle());
		
		for(int k=0;k<5;k++){
			m_data.addElement("");
		}
		
	}
	
	private void reportContentsummary(EmployeeOvertimeSubmit overtimeSubmit, long dex){
		long index=dex;
		float value=overtimeSubmit.getOvertimeValue();
		if(index==1){
			String strValue= "";
			if (value>0)
				strValue=String.valueOf(value);
			m_data.set(3,strValue);
		}else if(index==2){
			String strValue= "";
			if (value>0)
				strValue=String.valueOf(value);
			m_data.set(5,strValue);
		}else if(index==3){
			String strValue= "";
			if (value>0)
				strValue=String.valueOf(value);
			m_data.set(4,strValue);
		}else if(index==4){
			String strValue= "";
			if (value>0)
				strValue=String.valueOf(value);
			m_data.set(6,strValue);
		}else if(index==5){
			String strValue= "";
			if (value>0)
				strValue=String.valueOf(value);
			m_data.set(7,strValue);
		}
	}
	
	private void reportContentOvertime(EmployeeOvertimeSubmit overtimeSubmit, long dex){
		long index=dex;
		float value=overtimeSubmit.getOvertimeValue();
		if(index==1){
			m_data.set(7,String.valueOf(value));
		}else if(index==2){
			m_data.set(9,String.valueOf(value));
		}else if(index==3){
			m_data.set(8,String.valueOf(value));
		}else if(index==4){
			m_data.set(10,String.valueOf(value));
		}else if(index==5){
			m_data.set(11,String.valueOf(value));
		}
	}
	
	private void reportContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(m_panel.getUnitDescription());
		m_data.addElement(m_panel.getMonthYearString());
		m_data.addElement(String.valueOf(i + 1));
		m_data.addElement(submit.getEmployeeNo());
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		System.out.println("name = "+name );
		m_data.addElement(submit.getJobTitle());
		m_data.addElement("");
	}
	
	boolean m_isnotFindEmployee = false;
	public void setIsnotFindEmployee(boolean find){
		m_isnotFindEmployee =find;
	}
	
	public void tableBody(){
		EmployeeOvertimeSubmit employeepayrollSubmit=m_panel.getEmpOvertimeSubmit();
		EmployeeOvertimeSubmit[] employeeOvertSub=m_empOverTimeSubmit;
		//EmployeePayrollSubmit[] employeePaySubm = m_empPaySubmits;
		m_empPayroll = new EmployeePayroll(m_panel.m_conn, m_panel.getYear(), m_panel.getMonth(),m_panel.m_sessionid);
		if (m_isnotFindEmployee )
			m_panel.payrollOvertimeVector.clear();
		
		//EmployeeOvertimeSubmit employeepayrollSubmit=null;
		employeepayrollSubmit=new EmployeeOvertimeSubmit(m_panel.m_sessionid,m_panel.m_conn);
		employeepayrollSubmit.setMonth(m_panel.getMonth());
		 employeepayrollSubmit.setYear(m_panel.getYear());
		 employeepayrollSubmit.setUnit(m_unit);
		 employeepayrollSubmit.setStatus(1);
		 Calendar calendar=Calendar.getInstance();
		 employeepayrollSubmit.setSubmittedDate(calendar.getTime());
		 employeepayrollSubmit.setPayrollType(PayrollSubmitType.OVERTIME);		
		m_logic = new HRMBusinessLogic(m_panel.m_conn);
		
		try {
			employeeOvertSub= (EmployeeOvertimeSubmit[]) m_logic.getEmployeePayrollSubmit(m_panel.m_sessionid,
			IDBConstants.MODUL_MASTER_DATA,employeepayrollSubmit);
			
			if(employeeOvertSub!=null){
				if(employeeOvertSub.length>0){
					setTextfieldStatus(employeeOvertSub[0]);
					if(employeeOvertSub[0].getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
						m_panel.m_submitBt.setEnabled(true);
						m_panel.updateFlag=true;
					} else {
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
			getNonSubmit(employeepayrollSubmit);
		}else{
			getSubmit(employeeOvertSub);
		}
	}
	
	boolean m_ceksummary;
	public void tableBodyReport(boolean ceksummary){
		m_ceksummary = ceksummary;
		m_mode = 1;
		if (m_emps.length==0){
			addEmptyRow();
		}else{
			tableBody();
		}
	}
	
	private String getResult(float val){
		String strval = "";
		if (val>0)
			strval = String.valueOf(val);
		return strval;
	}
	public String[] getStrTotal(double[] total){
		String[] strtotal ={"",""}; 
		if (total[0]>0)
			strtotal[0] = m_formatDesimal.format(total[0]);
		if (total[1]>0)
			strtotal[1] = m_formatDesimal.format(total[1]);
		return strtotal;
	}
	
	double[] m_total={0,0};
	public void setTotal(double[] total){
		m_total= total;
	}
	public double[] getTotal(){
		return m_total;
	}
	
}
