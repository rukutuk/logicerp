package pohaci.gumunda.titis.hrm.logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.cgui.PayrollInsuranceAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollInsuranceAllowanceSubmitPanel;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;

public class PayrollInsuranceAllowanceLogic {
    final DefaultTableModel m_model; // model tabel
    final EmployeePayroll m_empPayroll; // payrollcomponent dari employee
    PayrollInsuranceAllowanceSubmitPanel m_panel; // kelass payroll insurance allowance
    final String m_descUnit,m_date; // unit dan tanggal
    final Employee_n[] m_emps; // employee
    String[] m_TAsuransi= {"",""}; // tunjangan asuransi 
    //int nx=0; // nilai untuk 
    final PayrollComponent[] m_payrollComponents;
    EmployeePayrollSubmit[] m_empPaySubmits;
    Vector m_currentRowData = null;
    PayrollInsuranceAllowanceTableRow m_currentRow = new PayrollInsuranceAllowanceTableRow();
	boolean reportMode;
	DecimalFormat m_formatDesimal;
	private PayrollComponent[] m_payrollComponentsDefault;
	PayrollInsuranceAllowanceVerificationPanel m_verivypanel; // kelass payroll insurance allowance
    
	 HRMBusinessLogic m_logic;
	//private Unit m_unit;
	
	public PayrollInsuranceAllowanceLogic(PayrollInsuranceAllowanceSubmitPanel panel,EmployeePayroll empPayroll,DefaultTableModel model,
			 Employee_n[] emps, PayrollComponent[] payrollComponents,Unit unit,
			EmployeePayrollSubmit[] empPaySubmits,PayrollComponent[] payrollComponentsDefault, boolean excel) {
		m_descUnit = panel.getUnitDescription();
		m_date = panel.getMonthYearString();
		this.m_model=model;
		this.m_empPayroll = empPayroll;
		this.m_panel = panel;
		this.m_emps = emps;
		this.m_payrollComponents = payrollComponents;
		//this.m_unit = unit;
		this.m_logic = new HRMBusinessLogic(m_panel.m_conn);
		this.m_empPaySubmits = empPaySubmits;
		this.m_payrollComponentsDefault=payrollComponentsDefault;
		if(excel)
			this.m_formatDesimal = new DecimalFormat("###.00");
		else this.m_formatDesimal = new DecimalFormat("#,###.00");
	}
	
	/*private void storeValue(PayrollCalcResult res, int j) {
		if (reportMode){
            String value = (res==null) ? " " : m_formatDesimal.format(res.value);
            m_TAsuransi[nx % 2] = value;
            nx = nx + 1;
		}else{
			if (res != null) 
				m_currentRowData.addElement(new Double(res.value));
			else 
				m_currentRowData.addElement("");			
		}
	}*/
   
	public PayrollInsuranceAllowanceLogic(PayrollInsuranceAllowanceVerificationPanel panel,EmployeePayroll empPayroll,DefaultTableModel model,
			 Employee_n[] emps, PayrollComponent[] payrollComponents,Unit unit,
			EmployeePayrollSubmit[] empPaySubmits,PayrollComponent[] payrollComponentsDefault, boolean excel) {
		m_descUnit = panel.getUnitDescription();
		m_date = panel.getMonthYearString();
		this.m_model=model;
		this.m_empPayroll = empPayroll;
		this.m_verivypanel = panel;
		this.m_emps = emps;
		this.m_payrollComponents = payrollComponents;
		//this.m_unit = unit;
		this.m_logic = new HRMBusinessLogic(m_verivypanel.m_conn);
		this.m_empPaySubmits = empPaySubmits;
		this.m_payrollComponentsDefault=payrollComponentsDefault;
		if(excel)
			this.m_formatDesimal = new DecimalFormat("###.00");
		else this.m_formatDesimal = new DecimalFormat("#,###.00");
	}
	
    private PayrollCalcResult[] calculatePayroll(PayrollComponent[] payrollComponents, int empIdx) throws Exception {
		PayrollCategoryComponent[] comps;
		PayrollCalcResult[] results = null;
		comps = m_panel.getPayrollCategoryComponents(m_emps[empIdx], payrollComponents);
		if(comps!=null)
			results = m_empPayroll.calcPayrollComponent(comps, m_emps[empIdx].getAutoindex());							
		return results;
	}
			
	private void addToPanelVector(EmployeePayrollSubmit employeepayrollSubmit, PayrollCalcResult[] results, int i) {
		PayrollCalcResult res;
		EmployeePayrollSubmit[] employeepayrollsubmit=new EmployeePayrollSubmit[m_payrollComponentsDefault.length];
		for(int j=0; j<m_payrollComponentsDefault.length; j++){
			res = null;	
			employeepayrollsubmit[j]=m_panel.dummyClone(employeepayrollSubmit);
			employeepayrollsubmit[j].setPayrollComponent(m_payrollComponentsDefault[j]);
			employeepayrollsubmit[j].setJobTitle(m_emps[i].getJobtitle());
			if(results!=null){
				for(int k=0; k<results.length; k++){
					if(m_payrollComponentsDefault[j].getIndex() == results[k].component.getIndex()){
						res = results[k];	
						break;
					}
				}
			}
			if (res != null) {
				employeepayrollsubmit[j].setValue((float) res.value);
			} else {
				employeepayrollsubmit[j].setValue(-1);
			}
			if (m_isnotFindEmployee)
				m_panel.employeePayVector.add(employeepayrollsubmit[j]);
		}
	}
	
	private void printTableRow(EmployeePayrollSubmit row, int i) {
		m_currentRowData.addElement(String.valueOf(i + 1));
		String name=row.getName();		
		m_currentRowData.addElement(name); // menampilkan name
		System.out.println("name = "+name );
		m_currentRowData.addElement(row.getJobTitle());
	}
	
	void addEmptyRow()	{		
    	m_model.addRow(new Object[]{m_descUnit,m_date,"","","","",""});
    }

    private void printReportRow(EmployeePayrollSubmit row, int i) {
        m_currentRow.unitDescription = m_panel.getUnitDescription();
        m_currentRow.monthYearString = m_panel.getMonthYearString();
        m_currentRow.rowNum = i+1;
        m_currentRow.employeeNo = row.getEmployeeNo();
        m_currentRow.name = row.getName();
        m_currentRow.jobTitle = row.getJobTitle();
		m_currentRowData.add(m_panel.getUnitDescription());
		m_currentRowData.add(m_panel.getMonthYearString());
		m_currentRowData.addElement(String.valueOf(i + 1));
		m_currentRowData.addElement(row.getEmployeeNo());
		String name=row.getName();		
		m_currentRowData.addElement(name); // menampilkan name
		System.out.println("name = "+name );
		m_currentRowData.addElement(row.getJobTitle());	
	}
	
    private void setTextfieldStatus(EmployeePayrollSubmit employeeMealAll){
        setTextfieldStatus(employeeMealAll,this.m_panel);
    }
    
	public static void setTextfieldStatus(EmployeePayrollSubmit employeeMealAll, PayrollInsuranceAllowanceSubmitPanel panel){
		if(employeeMealAll.getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
			panel.m_statusTextField.setText(" Not Submitted");
			panel.m_submitdateTextField.setText("");
		}else if(employeeMealAll.getStatus()==PayrollSubmitStatus.SUBMITTED){
			panel.m_statusTextField.setText(" Submitted");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(employeeMealAll.getSubmittedDate());
			panel.m_submitdateTextField.setText(" "+dateString);
			panel.m_submitBt.setEnabled(false);
		}else if(employeeMealAll.getStatus()==PayrollSubmitStatus.VERIFIED){
			panel.m_statusTextField.setText(" Verified");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(employeeMealAll.getSubmittedDate());
			panel.m_submitdateTextField.setText(" "+dateString);
			panel.m_submitBt.setEnabled(false);
		}else if(employeeMealAll.getStatus()==PayrollSubmitStatus.POSTED){
			panel.m_statusTextField.setText(" Posted");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(employeeMealAll.getSubmittedDate());
			panel.m_submitdateTextField.setText(" "+dateString);
			panel.m_submitBt.setEnabled(false);
		}
	}

    PayrollColumnHelper helper = new PayrollColumnHelper();
    
	private void printTableRows(EmployeePayrollSubmit[] empPaySub) {
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_currentRowData = new Vector();
		for(int i=0;i<empPaySub.length;i++){  
			if(empPaySub[i]!=null){	
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){					
					if (m_currentRowData.size()>0){
						m_panel.m_table.addRow(m_currentRowData);
						m_currentRowData = new Vector();
					}
						
					printTableRow(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
					leftMargin = m_currentRowData.size();
				} 
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empPaySub[i].getValue();
					Object content=null;
					if(value!= -1){
						 content = new Double(value);
					}else{
						 content = "";	
					}
					helper.addDataAtColumn(m_currentRowData, colIdx+ leftMargin, content);
				}
			}
		}
		if (m_currentRowData.size()>0) {
			m_panel.m_table.addRow(m_currentRowData);
			//this.m_verivypanel.m_table.addRow(m_currentRowData);
			m_currentRowData.addElement("");
		}
		m_panel.m_table.setModelKu();

	}
	
	private EmployeePayrollSubmit[] getEmpPaysubmit(EmployeePayrollSubmit[] empPaySub) {
		Hashtable map = new Hashtable(); 
		ArrayList datalist = new ArrayList();
		for(int i=0;i<empPaySub.length;i++){
			for(int j=0;j<m_emps.length;j++){
				if (empPaySub[i].getEmployeeIndex()==m_emps[j].getAutoindex()){
					map.put(empPaySub[i],new Long(empPaySub[i].getEmployeeIndex()));
					datalist.add(empPaySub[i]);
				}
			}
		}
		EmployeePayrollSubmit[] emps = (EmployeePayrollSubmit[]) datalist.toArray(new EmployeePayrollSubmit[datalist.size()]);
		return emps;
	}
	
	private void printSummaryRows(EmployeePayrollSubmit[] empPaySub) {
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_currentRowData = new Vector();
		empPaySub = getEmpPaysubmit(empPaySub);
		for(int i=0;i<empPaySub.length;i++){  
			if(empPaySub[i]!=null){	
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){
					if (m_currentRowData.size()>0){
						m_currentRowData.addElement(new Integer(0));
						m_model.addRow(m_currentRowData);
					}
					m_currentRowData = new Vector();
					printTableRow(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
					leftMargin = m_currentRowData.size();
				} 
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empPaySub[i].getValue();
					Object content=null;
					if(value!= -1){
						 m_total[colIdx]+=value;
						 content = m_formatDesimal.format(value);
					}else{
						 content = "";	
					}
					helper.addDataAtColumn(m_currentRowData, colIdx+ leftMargin, content);
				}
			}
		}
		if (m_currentRowData.size()>0) {
			m_currentRowData.addElement(new Integer(0));
			m_model.addRow(m_currentRowData);
		}
		String[] strTotal = getStrValue(m_total);
	  	m_model.addRow(new Object[]{"TOTAL PEMBAYARAN PER DEPARTMENT","","",
	  			strTotal[0] ,strTotal[1],new Integer(1)});
	  	setTotal(m_total);
	}
	
	private void printReportRows(EmployeePayrollSubmit[] empPaySub) { 
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		//int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponentsDefault);
		m_currentRowData = new Vector();		
		for(int i=0;i<empPaySub.length;i++){  
			if(empPaySub[i]!=null){
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){ // jika nama tidak berulang					
					if (m_currentRowData.size()>0){
						m_currentRowData.addElement("");
						m_currentRowData.addElement(m_TAsuransi[0]);
						m_currentRowData.addElement(m_TAsuransi[1]);
						m_model.addRow(m_currentRowData);
					}
					m_currentRowData = new Vector();
					printReportRow(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
			//		leftMargin = m_data.size();
				}
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				//System.err.println("colid :" + colIdx);
//				Object content=null;				
				if(colIdx!=-1){
					double value=empPaySub[i].getValue();					
					if(value!= -1){						
						m_TAsuransi[colIdx] = m_formatDesimal.format(value);
                        m_currentRow.setIndexedColumn(colIdx, value);
					}else{
						m_TAsuransi[colIdx] = "";
                        m_currentRow.setIndexedColumnNull(colIdx);
					}
				}								
			}
		}
		if (m_currentRowData.size()>0) {
			m_currentRowData.addElement("");
			m_currentRowData.addElement(m_TAsuransi[0]);			
			m_currentRowData.addElement(m_TAsuransi[1]);
			m_currentRowData.addElement(Boolean.TRUE);
			m_model.addRow(m_currentRowData);
		}
	}

	private void printRows_submitted(EmployeePayrollSubmit[] m_employeePay) {		
		if (!reportMode){
			if (m_summary){
				printSummaryRows(m_employeePay);
			}else{
				printTableRows(m_employeePay);
			}
		}else{
			printReportRows(m_employeePay);
		}
	}
	private void printRows_unsubmitted(EmployeePayrollSubmit employeepayrollSubmit) {
		PayrollCalcResult[] results;
		if (m_isnotFindEmployee )
			m_panel.employeePayVector.clear();
        //nx = 0;
        helper.prepareColumnHelper(m_payrollComponents);
		try {	  
			for(int i=0; i<m_emps.length; i++){
				m_currentRowData = new Vector();
				results = calculatePayroll(m_payrollComponents, i);
				if (!reportMode){
					if (m_summary){
						m_currentRow.rowNum = i+1;
						m_currentRow.name = m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname();
						m_currentRow.jobTitle = m_emps[i].getJobtitle();
						m_currentRowData.addElement(String.valueOf(i+1));
						m_currentRowData.addElement(m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname());
						m_currentRowData.addElement(m_emps[i].getJobtitle());		
						employeepayrollSubmit.setEmployee_n(m_emps[i]);	 				  	
						PayrollCalcResult res;
						
						for(int j=0; j<m_payrollComponents.length; j++){
							res = null;	
							if(results!=null)  {
								for(int k=0; k<results.length; k++){
									if(m_payrollComponents[j].getIndex() == 
										results[k].component.getIndex()){
										res = results[k];	
										break;
									}
								}
							}
							if (res != null) {
								m_total[j]+=res.value;
								m_currentRowData.addElement(m_formatDesimal.format(res.value));
								m_currentRow.setIndexedColumn(j,res.value);    
							}else {
								m_currentRowData.addElement("");
								m_currentRow.setIndexedColumnNull(j);
							}												
						}
						addToPanelVector(employeepayrollSubmit, results, i);
						m_currentRowData.addElement(new Integer(0));
						m_model.addRow(m_currentRowData);
					}else{
						m_currentRow.rowNum = i+1;
						m_currentRow.name = m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname();
						m_currentRow.jobTitle = m_emps[i].getJobtitle();
						m_currentRowData.addElement(String.valueOf(i+1));
						m_currentRowData.addElement(m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname());
						m_currentRowData.addElement(m_emps[i].getJobtitle());		
						employeepayrollSubmit.setEmployee_n(m_emps[i]);	 				  	
						PayrollCalcResult res;
						
						for(int j=0; j<m_payrollComponents.length; j++){
							res = null;	
							if(results!=null)  {
								for(int k=0; k<results.length; k++){
									if(m_payrollComponents[j].getIndex() == 
										results[k].component.getIndex()){
										res = results[k];	
										break;
									}
								}
							}
							if (res != null){								
								m_currentRowData.addElement(new Double(res.value));
								m_currentRow.setIndexedColumn(j,res.value);    
							}else{
								m_currentRowData.addElement("");
								m_currentRow.setIndexedColumnNull(j);
							}												
						}
						addToPanelVector(employeepayrollSubmit, results, i);
						m_currentRowData.addElement(Boolean.FALSE);
						m_model.addRow(m_currentRowData);
						m_panel.m_table.setModelKu();
					}
				}else{
					m_TAsuransi[0] = " ";
					m_TAsuransi[1] = " ";
                    m_currentRow.unitDescription = m_descUnit;
                    m_currentRow.monthYearString = m_date;
                    m_currentRow.rowNum = i+1;
                    m_currentRow.employeeNo = m_emps[i].getEmployeeno();
                    m_currentRow.name = m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname();
                    m_currentRow.jobTitle = m_emps[i].getJobtitle(); 
					m_currentRowData.addElement(m_descUnit);
					m_currentRowData.addElement(m_date);
					m_currentRowData.addElement(Integer.toString(i+1));
					m_currentRowData.addElement(m_emps[i].getEmployeeno());
					m_currentRowData.addElement(m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname());
					m_currentRowData.addElement(m_emps[i].getJobtitle());
					employeepayrollSubmit.setEmployee_n(m_emps[i]);
					PayrollCalcResult res;
					for(int j=0; j<m_payrollComponents.length; j++){
						/*if (m_payrollComponents[j].getAccount().getCode().equals("5.01.01.13"))
							//nx=0;
						else 
							nx=1;*/
						res = null;	
						if(results!=null){
							for(int k=0; k<results.length; k++){
								if(m_payrollComponents[j].getIndex() == results[k].component.getIndex()){
									res = results[k];	
									break;
								}
							}
						}
					    String value = (res==null) ? " " : m_formatDesimal.format(res.value);
                        m_TAsuransi[j] = value;
                        if (res==null)
                            m_currentRow.setIndexedColumnNull(j);
                        else    
                            m_currentRow.setIndexedColumn(j, res.value);
                        //nx = nx + 1;
                        if (res!=null)
                        	m_total[j] += res.value;									
					}	
					m_currentRowData.addElement("");
					m_currentRowData.addElement(m_TAsuransi[0]);
					m_currentRowData.addElement(m_TAsuransi[1]);
					m_currentRowData.addElement(Boolean.FALSE);
					m_model.addRow(m_currentRowData);
				}
			}
			if (m_summary){
			  	String[] strTotal = getStrValue(m_total);
			  	m_model.addRow(new Object[]{"TOTAL PEMBAYARAN PER DEPARTMENT","","",
			  			strTotal[0] ,strTotal[1],new Integer(1)});
			  	setTotal(m_total);
			  } else {
				m_currentRowData = new Vector();
				String[] strTotal = getStrValue(m_total);
				m_currentRowData.addElement(m_descUnit);
				m_currentRowData.addElement(m_date);
				m_currentRowData.addElement("TOTAL");
				for (int x = 1; x <= 4; x++)
					m_currentRowData.addElement("");
				m_currentRowData.addElement(strTotal[0]);
				m_currentRowData.addElement(strTotal[1]);
				m_currentRowData.addElement(Boolean.TRUE);
				m_model.addRow(m_currentRowData);
			}
		}catch (Exception e1) {
			e1.printStackTrace();
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
   
	boolean m_isnotFindEmployee= false;
	public void setIsnotFindEmployee(boolean find){
		m_isnotFindEmployee=find;
	}
	public void createTableBody(){
		EmployeePayrollSubmit m_employeepayrollSubmit;
		if(m_panel!=null){
			m_employeepayrollSubmit=m_panel.getEmpPayrollSubmit();
			EmployeePayrollSubmit[] employeePaySubm = m_empPaySubmits;	
			try {
	            // logika ini tidak ada hubungannya dengan pembuatan tabel..
				if(employeePaySubm!=null){
					if(employeePaySubm.length>0){
						setTextfieldStatus(employeePaySubm[0]);
						if(employeePaySubm[0].getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
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
			if (m_panel.m_submitBt.isEnabled()){
				printRows_unsubmitted(m_employeepayrollSubmit);
			}else{			
				printRows_submitted(employeePaySubm);
			}
		}
		else{
			m_employeepayrollSubmit=this.m_verivypanel.getEmpPayrollSubmit();
			EmployeePayrollSubmit[] employeePaySubm = m_empPaySubmits;	
			printRows_submitted(employeePaySubm);
		}
		
		
	  }
	
	
	public void createReportBody() throws Exception {
		reportMode = true;
		if (m_emps.length==0){
			  addEmptyRow();	    	  
	      }else {
	    	  createTableBody();		      
	      }
	}
	
	boolean m_summary;
	public void tableBodyReport(boolean summary){
		m_summary = summary;
		reportMode = false;
		if (m_emps.length==0){
			addEmptyRow();	    	  
		}else{
			try {
				createTableBody();
			} catch (Exception e) {
				e.printStackTrace();
			}	    
		}
	}
	
	double[] m_total = {0,0};
	public void setTotal(double[] total){
		m_total =  total;
	}
	
	public double[] getTotal(){
		return m_total;
	}

    static class PayrollInsuranceAllowanceTableRow {

        public String jobTitle;
        public String name;
        public String employeeNo;
        public int rowNum;
        public String monthYearString;
        public String unitDescription;
        Double[] cols = new Double[2];
        public void setIndexedColumn(int colIdx, double value) {
            cols[colIdx]  = new Double(value);            
        }
        public void setIndexedColumnNull(int colIdx) {
            cols[colIdx] = null;
        }
        
    }
}
