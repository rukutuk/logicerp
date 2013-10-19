package pohaci.gumunda.titis.hrm.logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentReportLabel;
import pohaci.gumunda.titis.hrm.cgui.PayrollPaychequeSubmitPanel;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;

public class PayrollPaychequeSubmitLogic {
	PayrollPaychequeSubmitPanel m_panel;
	DefaultTableModel m_model;
	PayrollComponent[] m_payrollComponents;
	Employee_n[] m_emps;
	Unit m_unit;
	Vector m_data = null;
	int m_mode = 0;
	EmployeePayrollSubmit[] m_empPayrollSubmit;
	String[] m_GajiPokok={"",""}; // tunjangan asuransi
	String[] m_Kes= {"",""}; // tunjangan Kesehatan
	String[] m_Mgmt= {"",""}; // tunjangan mgmt
	String[] m_Tek= {"",""}; // tunjangan tek
	String[] m_Rmh= {"",""}; // tunjangan rmh
	String[] m_Lap= {"",""}; // tunjangan lap
	String[] m_THR= {"",""}; // tunjangan 	thr
	String[] m_Insertif= {"",""}; // tunjangan insertif 
	String[] m_Bonus= {"",""}; // tunjangan bonus
	String m_asuransiPeg=""; 
	String m_piutKePerusahaan="";
	String m_piutKeYayasan="";
	int n=0; // nilai untuk 
	EmployeePayrollSubmit[] m_employeepayrollsubmit;
	private PayrollComponent[] m_payrollComponentsDefault;
//	private HRMBusinessLogic m_logic;
	private EmployeePayroll empPayroll;
	private EmployeePayrollSubmit m_employeepayrollSubmit;
	DecimalFormat m_formatDesimal;
	
	private double total = 0;
	
	private JournalStandardAccount[] jsa ;
	private double grandTotal;
	private JComboBox m_typecombo;
	
	public PayrollPaychequeSubmitLogic(PayrollPaychequeSubmitPanel panel,DefaultTableModel model,
			PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
			EmployeePayrollSubmit[] empPayrollSubmit,PayrollComponent[] m_payrollComponentsDefault){
		this.m_empPayrollSubmit = empPayrollSubmit;
		this.m_model = model;
		this.m_panel = panel;
		this.m_emps = emps;
		this.m_unit = unit;
		this.m_payrollComponents = payrollComponents;
		this.m_payrollComponentsDefault=m_payrollComponentsDefault;
//		m_logic = new HRMBusinessLogic(m_panel.m_conn);
		this.m_formatDesimal = new DecimalFormat("#,###.00");
	}
	
	public void setType(JComboBox type){
		m_typecombo = type;
	}
	
	public PayrollPaychequeSubmitLogic(PayrollPaychequeSubmitPanel panel,DefaultTableModel model,
			PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
			EmployeePayrollSubmit[] empPayrollSubmit,PayrollComponent[] m_payrollComponentsDefault, boolean excel){
		this.m_empPayrollSubmit = empPayrollSubmit;
		this.m_model = model;
		this.m_panel = panel;
		this.m_emps = emps;
		this.m_unit = unit;
		this.m_payrollComponents = payrollComponents;
		this.m_payrollComponentsDefault=m_payrollComponentsDefault;
//		m_logic = new HRMBusinessLogic(m_panel.m_conn);
		if(excel)
			this.m_formatDesimal = new DecimalFormat("###.00");
		else this.m_formatDesimal = new DecimalFormat("#,###.00");
	}
	
	void addEmptyRow(){
		m_model.addRow(new Object[]{m_panel.getUnitDescription(),m_panel.getMonthYearString()," "," "," "," "," "});
	}
	
	private void getValueComponents(PayrollCalcResult res,int i, PayrollComponent payrollComponent) {
		if(m_mode==1){
			if (res != null) {
				double value = getValue(payrollComponent, res);
				getValueNotNullComponentNonSubmit(value, i);
				getTotal(payrollComponent, value);
				
			}else{
				getValueNullComponents(i);
			}			
		}else{
			if(res!=null){								
				double value = getValue(payrollComponent, res);
				if (value>0)
					m_data.addElement(new Double(value));
				else
					m_data.addElement("");
				
				Account accHelper = payrollComponent.getAccount();
				Account acc = getStandardAccount(jsa, accHelper);
				
				if(acc!=null){
					if(acc.equals(accHelper)){
						if(acc.getBalance() == 0)
							total += value;
						else 
							total -= value;
					}
				}				
			}else{
				m_data.addElement("");
			}
		}
	}
	
	/**
	 * @param payrollComponent
	 * @param value
	 * @return
	 */
	private double getValue(PayrollComponent payrollComponent, PayrollCalcResult res) {
		double value = res.value;
		if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[0])){
			if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS) || payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR ))
				value = 0;
		}else if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[1])){
			if (!payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR ))
					value=0;
		}else if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[2])){
			if (!payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS ))
						value=0; 
		}
		return value;
	}
	
	private void getValueNullComponents(int i) {
		if (i == 0) {					
			m_GajiPokok[0] = "";
		}else if (i == 1) {					
			m_Kes[0] = "";
		}else if (i == 2) {					
			m_Mgmt[0] = "";
		}else if (i == 3) {					
			m_Tek[0] = "";
		}else if (i == 4) {					
			m_Rmh[0] ="";
		}else if (i == 5) {					
			m_THR[0] = "";
		}else if (i == 6) {					
			m_Lap[0] = "";
		}else if (i == 7) {					
			m_Insertif[0] = "";
		}else if (i == 8) {					
			m_Bonus[0] = "";
		}		
		else if (i == 9) {					
			m_GajiPokok[1] = "";
		}else if (i == 10) {					
			m_Kes[1] = "";
		}else if (i == 11) {					
			m_Mgmt[1] = "";
		}else if (i == 12) {					
			m_Tek[1] = "";
		}else if (i == 13) {					
			m_Rmh[1] = "";
		}else if (i == 14) {					
			m_THR[1] = "";
		}else if (i == 15) {					
			m_Lap[1] = "";
		}else if (i == 16) {					
			m_Insertif[1] = "";
		}else if (i == 17) {					
			m_Bonus[1] = "";
		}
		
		else if (i == 18) {					
			m_asuransiPeg = "";
		}else if (i == 19) {					
			m_piutKePerusahaan ="";
		}else if (i == 20) {					
			m_piutKeYayasan = "";
		}
	}
	
	private void getValueNotNullComponentNonSubmit(double value, int i) {
		String strValue = getStrValue(value);
		if (i == 0) {
			m_GajiPokok[0] = strValue;
		}else if (i == 1) {					
			m_Kes[0] = strValue;
		}else if (i == 2) {					
			m_Mgmt[0] = strValue;
		}else if (i == 3) {					
			m_Tek[0] = strValue;
		}else if (i == 4) {					
			m_Rmh[0] = strValue;
		}else if (i == 5) {					
			m_THR[0] = strValue;
		}else if (i == 6) {					
			m_Lap[0] = strValue;
		}else if (i == 7) {					
			m_Insertif[0] = strValue;
		}else if (i == 8) {					
			m_Bonus[0] = strValue;
		}
		else if (i == 9) {					
			m_GajiPokok[1] = strValue;
		}else if (i == 10) {					
			m_Kes[1] = strValue;
		}else if (i == 11) {					
			m_Mgmt[1] = strValue;
		}else if (i == 12) {					
			m_Tek[1] = strValue;
		}else if (i == 13) {					
			m_Rmh[1] = strValue;
		}else if (i == 14) {					
			m_THR[1] = strValue;
		}else if (i == 15) {					
			m_Lap[1] = strValue;
		}else if (i == 16) {					
			m_Insertif[1] = strValue;
		}else if (i == 17) {					
			m_Bonus[1] = strValue;
		}
		else if (i == 18) {					
			m_asuransiPeg = strValue;
		}else if (i == 19) {					
			m_piutKePerusahaan =strValue;
		}else if (i == 20) {					
			m_piutKeYayasan = strValue;
		}
	}	
	
	/**
	 * @param value
	 * @return
	 */
	private String getStrValue(double value) {
		String strValue = "";
		if (value>0)
			strValue = m_formatDesimal.format(value);
		return strValue;
	}
	
	private PayrollCalcResult[] getResult(EmployeePayroll empPayroll, int i) throws Exception {
		PayrollCategoryComponent[] comps;
		PayrollCalcResult[] results;
		comps = m_panel.getPayrollCategoryComponents(m_emps[i], m_payrollComponents);
		if(comps!=null)
			results = empPayroll.calcPayrollComponent(comps, m_emps[i].getAutoindex());
		else
			results = null;
		return results;
	}		
	
	private void tableContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		System.out.println("name = " + name);
		m_data.addElement(submit.getJobTitle());
	}
	
	
	private void reportContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(m_panel.getUnitDescription());
		m_data.addElement(m_panel.getMonthYearString());
		m_data.addElement(String.valueOf(i + 1));
		m_data.addElement(submit.getEmployeeNo());
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name		
		m_data.addElement(submit.getJobTitle());		
	}
	
	private void instaceReport(){
		m_GajiPokok[0] = " ";
		m_GajiPokok[1] = " ";
		m_Kes[0] = " ";
		m_Kes[1] = " ";
		m_Mgmt[0] = " ";
		m_Mgmt[1] = " ";
		m_Rmh[0] = " ";
		m_Rmh[1] = " ";
		m_Tek[0] = " ";
		m_Tek[1] = " ";
		m_Lap[0] = " ";
		m_Lap[1] = " ";
		m_THR[0] = " ";
		m_THR[1] = " ";
		m_Insertif[0] = " ";
		m_Insertif[1] = " ";
		m_Bonus[0] = " ";
		m_Bonus[1] = " ";
		m_asuransiPeg = " ";
		m_piutKePerusahaan = " ";
		m_piutKeYayasan  = " ";
	}
	private void valueReportComponents(){	
		m_data.addElement(m_GajiPokok[0]);
		m_data.addElement(m_GajiPokok[1]);
		m_data.addElement(m_Kes[0]);
		m_data.addElement(m_Kes[1]);
		m_data.addElement(m_Mgmt[0]);
		m_data.addElement(m_Mgmt[1]);
		m_data.addElement(m_Tek[0]);					
		m_data.addElement(m_Tek[1]);
		m_data.addElement(m_Rmh[0]);					
		m_data.addElement(m_Rmh[1]);
		m_data.addElement(m_THR[0]);
		m_data.addElement(m_THR[1]);
		m_data.addElement(m_Lap[0]);
		m_data.addElement(m_Lap[1]);					
		m_data.addElement(m_Insertif[0]);
		m_data.addElement(m_Insertif[1]);
		m_data.addElement(m_Bonus[0]);
		m_data.addElement(m_Bonus[1]);	
		m_data.addElement(m_asuransiPeg);
		m_data.addElement(m_piutKePerusahaan);
		m_data.addElement(m_piutKeYayasan);
		String strTotal = "";
		if (total>0){
			strTotal = m_formatDesimal.format(total);
			grandTotal += total;
		}
		m_data.addElement(strTotal);
	}
	
	private void loopPayrollComponents(PayrollCalcResult[] results1) {
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
			getValueComponents(res,j, m_payrollComponents[j]);
		}
	}
	
	private void getTableSubmitPaycheque(EmployeePayrollSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		grandTotal =0;
		for(int i=0;i<empPaySub.length;i++){  
			if(empPaySub[i]!=null){	
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){
					if (m_data.size()>0){						
						if (total>0){
							grandTotal+=total;
							m_data.addElement(new Double(total));
							m_panel.m_table.addRow(m_data);
						}else
							m_data.addElement("");
						
					}
					total = 0;
					m_data = new Vector();
					tableContent(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
					leftMargin = m_data.size();
				} 				
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				PayrollComponent payrollComponent= helper.getPayrollComponent(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					int colomn = colIdx+ leftMargin;
					double value=empPaySub[i].getValue();
					Object content = getContent(payrollComponent, value);
					helper.addDataAtColumn(m_data, colomn, content);
				}		
			}
		}
		if (m_data.size()>0) {
			grandTotal+=total;
			m_data.addElement(new Double(total));
			m_panel.m_table.addRow(m_data);	
		}
		m_data = new Vector();
		m_data.addElement("");
		m_data.addElement("GRAND TOTAL");
		m_data.addElement("");
		loopEmptyByPayrollComponent();
		m_data.addElement(new Double(grandTotal));
		m_panel.m_table.addRow(m_data);	
		m_panel.m_table.setModelKu();
	}

	
	private void getTableSubmitTHR(EmployeePayrollSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		int colomn = 0;
		grandTotal=0;
		for(int i=0;i<empPaySub.length;i++){  
			if(empPaySub[i]!=null){	
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){
					if (m_data.size()>0){						
						if (total>0){
							grandTotal+=total;
							helper.addDataAtColumn(m_data, (m_payrollComponents.length+leftMargin ) , new Double(total));
						}
						else
							m_data.addElement("");
						m_panel.m_table.addRow(m_data);
					}
					total = 0;
					m_data = new Vector();
					tableContent(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
					leftMargin = m_data.size();
				}
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				PayrollComponent payrollComponent= helper.getPayrollComponent(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					colomn = colIdx+ leftMargin;					
					double value=empPaySub[i].getValue();					
					Object content = getContent(payrollComponent, value);					
					helper.addDataAtColumn(m_data, colomn, content);
				}				
			}
		}
		if (m_data.size()>0) {
			m_data.addElement(new Double(total));
			m_panel.m_table.addRow(m_data);	
		}
		m_data = new Vector();
		m_data.addElement("");
		m_data.addElement("GRAND TOTAL");
		m_data.addElement("");
		loopEmptyByPayrollComponent();
		m_data.addElement(new Double(grandTotal));
		m_panel.m_table.addRow(m_data);	
	}
	/**
	 * @param payrollComponent
	 * @param value
	 * @return
	 */
	private double getValueCekType(PayrollComponent payrollComponent,double value){
		if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[0])){
			if (payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS) || 
				payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR ))
				value = 0;
		}else if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[1])){
			if (!payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.THR))
				value = 0;
		} else  if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[2])){
			if (!payrollComponent.getReportLabel().equals(PayrollComponentReportLabel.BONUS))
				value = 0;
		}
		return value;
	}
	
	private Object getContent(PayrollComponent payrollComponent, double value) {
		Object content=null;
		if(value!= -1){			
			value = getValueCekType(payrollComponent,value);
			getTotal(payrollComponent, value);
			if (value>0){				
				content = new Double(value);				
			}else
				content = "";
		}else{
			content = "";	
		}
		return content;
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
	
	double totalpayrol = 0;
	private void getReportSubmit(EmployeePayrollSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		helper.prepareColumnHelper(m_payrollComponentsDefault);		
		m_data = new Vector();
		
		List dataList = new ArrayList();
		for (int i=0;i<m_emps.length;i++){
			for(int j=0;j<empPaySub.length;j++){
				if (m_emps[i].getAutoindex() == empPaySub[j].getEmployeeIndex()){
					dataList.add(empPaySub[j]);
				}					
			}
		}		
	
		empPaySub = (EmployeePayrollSubmit[]) dataList.toArray(new EmployeePayrollSubmit[dataList.size()]);
		
		grandTotal =0;
		for(int i=0;i<empPaySub.length;i++){			
			if(empPaySub[i]!=null){
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){
					if (m_data.size()>0){
						valueReportComponents();
						m_model.addRow(m_data);
						instaceReport();
					}
					total = 0;
					m_data = new Vector();
					reportContent(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
				} 
				
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				PayrollComponent payrollComponent = helper.getPayrollComponent(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empPaySub[i].getValue();
					if(value!= -1){
						//int colomn = colIdx;
						value = getValueCekType( payrollComponent,value);						
						getTotal(payrollComponent, value);
						String strValue = "";
						if (value>0)
							strValue = m_formatDesimal.format(value);
						if (colIdx == 0) {					
							m_GajiPokok[0] = strValue;	
						}else if (colIdx == 1) {					
							m_Kes[0] = strValue;	
						}else if (colIdx == 2) {					
							m_Mgmt[0] = strValue;	
						}else if (colIdx == 3) {					
							m_Tek[0] = strValue;	
						}else if (colIdx == 4) {					
							m_Rmh[0] = strValue;	
						}else if (colIdx == 5) {					
							m_THR[0] = strValue;	
						}else if (colIdx == 6) {					
							m_Lap[0] = strValue;	
						}else if (colIdx == 7) {					
							m_Insertif[0] = strValue;	
						}else if (colIdx == 8) {					
							m_Bonus[0] = strValue;
						}
						if (colIdx == 9) {					
							m_GajiPokok[1] = strValue;	
						}else if (colIdx == 10) {					
							m_Kes[1] = strValue;	
						}else if (colIdx == 11) {					
							m_Mgmt[1] = strValue;	
						}else if (colIdx == 12) {					
							m_Tek[1] = strValue;	
						}else if (colIdx == 13) {					
							m_Rmh[1] = strValue;	
						}else if (colIdx == 14) {					
							m_THR[1] = strValue;	
						}else if (colIdx == 15) {					
							m_Lap[1] = strValue;	
						}else if (colIdx == 16) {					
							m_Insertif[1] = strValue;	
						}else if (colIdx == 17) {					
							m_Bonus[1] = strValue;
						}
						else if (colIdx == 18) {					
							m_asuransiPeg = strValue;
						}else if (colIdx == 19) {					
							m_piutKePerusahaan = strValue;
						}else if (colIdx == 20) {					
							m_piutKeYayasan = strValue;
						}
					}				
				}
			}
		}
		if (m_data.size()>0) {
			valueReportComponents();
			m_model.addRow(m_data);
		}
		m_data = new Vector();
		m_data.addElement("");
		m_data.addElement("");
		m_data.addElement("");
		m_data.addElement("");
		m_data.addElement("GRAND TOTAL");		
		loopEmptyByPayrollComponent();
		m_data.addElement(m_formatDesimal.format(grandTotal));
		m_model.addRow(m_data);
	}
	
	private void getTotal(PayrollComponent payrollComponent, double value) {
		Account accHelper = payrollComponent.getAccount();
		Account acc = getStandardAccount(jsa, accHelper);
		
		if(acc!=null){
			if(acc.equals(accHelper)){
				if(acc.getBalance() == 0)
					total += value;
				else 
					total -= value;
			}
		}	
	}
	
	private void getSubmit(EmployeePayrollSubmit[] empPayrollSubmit) {		
		if (m_mode == 0){
			if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[0])){
				getTableSubmitPaycheque(empPayrollSubmit);
			}
			else if (cekType().equals(IDBConstants.ATTR_PAYCHEQUE[1]) || cekType().equals(IDBConstants.ATTR_PAYCHEQUE[2])){
				getTableSubmitTHR (empPayrollSubmit);
			}
		}else{
			getReportSubmit(empPayrollSubmit);
		}
	}
	
	private void getEnableSubmit() {
		PayrollCalcResult[] results1;
		if (m_isfindEmployee )
			m_panel.employeePayVector.clear();
		grandTotal = 0;
		try {			
			for(int i=0; i<m_emps.length; i++){
				results1 = getResult(empPayroll, i);
				total = 0;
				m_data = new Vector();
				if (m_mode==0){	
					m_data.addElement(String.valueOf(i+1));
					m_data.addElement(m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + 
							m_emps[i].getLastname());
					m_data.addElement(m_emps[i].getJobtitle());		
					m_employeepayrollSubmit.setEmployee_n(m_emps[i]); 		
					loopPayrollComponent(results1);
					getPayrollComponentForSubmit(results1, i);
					m_model.addRow(m_data);
				}else{		
					instaceReport();
					m_data.addElement(m_panel.getUnitDescription());
					m_data.addElement(m_panel.getMonthYearString());
					m_data.addElement(String.valueOf(i+1));			
					m_data.addElement(m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + 
							m_emps[i].getLastname());
					m_data.addElement(m_emps[i].getEmployeeno());
					m_data.addElement(m_emps[i].getJobtitle());
					m_employeepayrollSubmit.setEmployee_n(m_emps[i]);
					loopPayrollComponents(results1);
					valueReportComponents();
					//m_data.addElemex`nt(new Double(total));
					m_model.addRow(m_data);	
					getPayrollComponentForSubmit(results1, i);
				}
				grandTotal += total;
			}
				m_data = new Vector();
				m_data.addElement("");
				m_data.addElement("GRAND TOTAL");
				m_data.addElement("");
				loopEmptyByPayrollComponent();
				m_data.addElement(new Double(grandTotal));
				m_panel.m_table.addRow(m_data);
				//m_panel.m_table.setModelKu();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void loopEmptyByPayrollComponent() {
		for(int j=0; j<m_payrollComponents.length; j++){
			m_data.addElement("");			
		}
	}
	
	private void loopPayrollComponent(PayrollCalcResult[] results1) {
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
			getValueComponents(res, j, m_payrollComponents[j]);
		}
		m_data.addElement(new Double(total));
		//m_panel.m_table.addRow(m_data);
	}
	
	public String cekType(){
		String strType = (String)m_typecombo.getSelectedItem();
		return strType;
	}

	private void getPayrollComponentForSubmit(PayrollCalcResult[] results1, int i) {
		m_employeepayrollsubmit=new EmployeePayrollSubmit[m_payrollComponentsDefault.length];
		PayrollCalcResult res;
		for(int j=0; j<m_payrollComponentsDefault.length; j++){
			res = null;
			m_employeepayrollsubmit[j]=m_panel.dummyClone(m_employeepayrollSubmit);
			m_employeepayrollsubmit[j].setPayrollComponent(m_payrollComponentsDefault[j]);
			m_employeepayrollsubmit[j].setJobTitle(m_emps[i].getJobtitle());
			if(results1!=null){
				for(int k=0; k<results1.length; k++){
					if(m_payrollComponentsDefault[j].getIndex()==results1[k].component.getIndex()){
						res = results1[k];
						break;
					}
				}
			}
			if(res!=null){								
				m_employeepayrollsubmit[j].setValue((float) res.value);
			}else{
				m_employeepayrollsubmit[j].setValue(-1);
			}
			if(cekType().equals(IDBConstants.ATTR_PAYCHEQUE[0])){
				if (!m_payrollComponentsDefault[j].getReportLabel().equals(PayrollComponentReportLabel.BONUS) &&
					!m_payrollComponentsDefault[j].getReportLabel().equals(PayrollComponentReportLabel.THR)){
					if (m_isfindEmployee )
						m_panel.employeePayVector.add(m_employeepayrollsubmit[j]);
				}
			}
			else if(cekType().equals(IDBConstants.ATTR_PAYCHEQUE[1])){
				if (m_payrollComponentsDefault[j].getReportLabel().equals(PayrollComponentReportLabel.THR)){
					if (m_isfindEmployee )
						m_panel.employeePayVector.add(m_employeepayrollsubmit[j]);
				}
			}
			else if(cekType().equals(IDBConstants.ATTR_PAYCHEQUE[2])){
				if (m_payrollComponentsDefault[j].getReportLabel().equals(PayrollComponentReportLabel.BONUS)){
					if (m_isfindEmployee )
						m_panel.employeePayVector.add(m_employeepayrollsubmit[j]);
				}
			}		
			
		}
	}
	
	boolean m_isfindEmployee = false;
	public void setIsFindEmployee( boolean isfindEmployee){
		m_isfindEmployee=isfindEmployee;
	}
	public void tableBody(){
		
		int month = m_panel.m_monthComboBox.getMonth() + 1;
		int year = m_panel.m_yearField.getValue();
		empPayroll = new EmployeePayroll(m_panel.m_conn, year, month,m_panel.m_sessionid);		
		m_employeepayrollSubmit = m_panel.getEmpPayrollSubmit();
		JournalStandardSettingPickerHelper help =
			new JournalStandardSettingPickerHelper(m_panel.m_conn, m_panel.m_sessionid, IDBConstants.ATTR_PAYROLL_COMPONENT);
		List jsList = 
			help.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES);
		JournalStandardSetting jss = (JournalStandardSetting) jsList.get(0);
		JournalStandard js = jss.getJournalStandard();
		jsa = js.getJournalStandardAccount();
		
		try {			
			if(m_empPayrollSubmit!=null){
				if(m_empPayrollSubmit.length>0){
					setTextfieldStatus(m_empPayrollSubmit[0]);
					if(m_empPayrollSubmit[0].getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
						m_panel.m_submitBt.setEnabled(true);
						m_panel.updateFlag=true;
					} else{
						if (m_panel.getPaychequeType()==m_empPayrollSubmit[0].getPaychequeType()){
							m_panel.m_submitBt.setEnabled(false);
							m_panel.updateFlag=true;
						}else{	
							m_panel.m_submitBt.setEnabled(true);
							m_panel.updateFlag=false;
						}
					}
				}else{					
					m_panel.m_submitBt.setEnabled(true);
					m_panel.updateFlag=false;
					m_panel.m_statusTextField.setText(" Not Submitted");
					m_panel.m_submitdateTextField.setText("");
				}
			}   
		} catch (Exception e1) {
			e1.printStackTrace();
		}	  
		if (m_panel.m_submitBt.isEnabled()){
			getEnableSubmit();	
		}else{
			getSubmit(m_empPayrollSubmit);
		}
	}
	
	private Account getStandardAccount(JournalStandardAccount[] jsa, Account acc){
		Account accResult = null;
		for(int i=0; i<jsa.length;i++){
			if(jsa[i].getAccount().equals(acc))
				accResult = jsa[i].getAccount();
		}
		return accResult;
	}
	
	public void tableBodyReport(){
		m_mode = 1;
		if (m_emps.length==0)
			addEmptyRow();
		else
			tableBody();
	}	
}
