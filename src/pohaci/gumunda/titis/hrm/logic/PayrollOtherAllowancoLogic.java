package pohaci.gumunda.titis.hrm.logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollColumnHelper;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollOtherAllowanceSubmitPanel;
import pohaci.gumunda.titis.hrm.cgui.report.HRMPayrollOtherAllowanceJasper;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;

public class PayrollOtherAllowancoLogic {
	DefaultTableModel m_model;  // model tabel
	EmployeePayroll m_empPayroll; // payrollcomponent dari employee
	PayrollOtherAllowanceSubmitPanel m_panel; // kelass payroll insurance allowance
	String m_descUnit,m_date; // unit dan tanggal
	Employee_n[] m_emps; // employee
	String[] m_tunjangan_lainnya= new String[2]; // tunjangan asuransi
	PayrollComponent[] m_payrollComponents;
	Vector m_data = null;
	int m_mode = 0; // jika 0= tabel ; jika 1= report;
	Unit m_unit;
	EmployeePayrollSubmit[] m_empPaySubmits;
	DecimalFormat m_formatDesimal;

	HRMBusinessLogic m_logic;
	private  PayrollComponent[] m_payrollComponentsDefault;

	public PayrollOtherAllowancoLogic(PayrollOtherAllowanceSubmitPanel panel, EmployeePayroll empPayroll,DefaultTableModel model,
			Employee_n[] emps, PayrollComponent[] payrollComponents,
			Unit unit, EmployeePayrollSubmit[] empPayrollSubmits, PayrollComponent[] m_payrollComponentsDefault,
			boolean excel){
		m_descUnit = panel.getUnitDescription();
		m_date = panel.getMonthYearString();
		this.m_model=model;
		this.m_empPayroll = empPayroll;
		this.m_panel = panel;
		this.m_emps = emps;
		this.m_empPaySubmits = empPayrollSubmits;
		this.m_payrollComponents = payrollComponents;
		this.m_unit = unit;
		this.m_logic = new HRMBusinessLogic(m_panel.m_conn);
		this.m_payrollComponentsDefault=m_payrollComponentsDefault;
		if(excel)
			this.m_formatDesimal = new DecimalFormat("###.00");
		else this.m_formatDesimal = new DecimalFormat("#,###.00");
	}

	public PayrollOtherAllowancoLogic(PayrollOtherAllowanceSubmitPanel m_submitpanel, DefaultTableModel model, PayrollComponent[] payrollComponents, Employee_n[] emp, Unit unit, EmployeePayrollSubmit[] empMealAllowSubmit, PayrollComponent[] payrollComponentsDefault, boolean b) {
	}

	void addEmptyRow()	{
		m_model.addRow(new Object[]{m_descUnit,m_date,"","","","",""});
	}


	private String getName(int i) {
		String name;
		name = m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + m_emps[i].getLastname();
		return name;
	}

	private void store(PayrollCalcResult res, int j) {
		if (m_mode == 1) {//
			if (res != null) {
				m_tunjangan_lainnya[j] = m_formatDesimal.format(res.value);
			}else{
				m_tunjangan_lainnya[j] = " ";
			}
		} else {
			if (res != null) {
				if (m_summary){
					m_data.addElement(m_formatDesimal.format(res.value));
					m_total[j] += res.value;
				}else
					m_data.addElement(new Double(res.value));
			} else {
				m_data.addElement("");
			}
		}
	}

	private PayrollCalcResult[] getPayrollCalcResult(PayrollComponent[] payrollComponents,Employee_n emp) throws Exception {
		PayrollCategoryComponent[] comps;
		PayrollCalcResult[] results;
		comps = m_panel.getPayrollCategoryComponents(emp, payrollComponents);
		if(comps!=null)
			results = m_empPayroll.calcPayrollComponent(comps, emp.getAutoindex());
		else
			results = null;
		return results;
	}


	private void getPayrollComponentsForSubmit(EmployeePayrollSubmit employeepayrollSubmit,
			PayrollCalcResult[] results, int i) {
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
				m_panel.m_employeePayVector.add(employeepayrollsubmit[j]);
		}
	}
	private void getSubmit(EmployeePayrollSubmit[] m_employeeMeal) {
		if (m_mode == 0){
			if (m_summary)
				presentingDataSummary(m_employeeMeal);
			else
				presentingData(m_employeeMeal);
		}else{
			presentingDataReport(m_employeeMeal);
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

	private void presentingData(EmployeePayrollSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		for(int i=0;i<empPaySub.length;i++){
			if(empPaySub[i]!=null){
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){
					if (m_data.size()>0)
						m_panel.m_table.addRow(m_data);
					m_data = new Vector();
					tableContent(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
					leftMargin = m_data.size();
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

	private void presentingDataSummary(EmployeePayrollSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
//		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		empPaySub = getEmpPaysubmit(empPaySub);
		for(int i=0;i<empPaySub.length;i++){
			if(empPaySub[i]!=null){
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){
					if (m_data.size()>0){
						m_data.addElement(m_tunjangan_lainnya[0]);
						m_data.addElement(m_tunjangan_lainnya[1]);
						m_data.addElement(new Integer(0));
						m_model.addRow(m_data);
					}
					m_data = new Vector();
					summaryContent(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
//					leftMargin = m_data.size();
				}
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empPaySub[i].getValue();
					if(value!= -1){
						m_total[colIdx]+=value;
						m_tunjangan_lainnya[colIdx] = m_formatDesimal.format(value);
					}else{
						m_tunjangan_lainnya[colIdx] = "";
					}
				}
			}
		}
		if (m_data.size()>0) {
			m_data.addElement(m_tunjangan_lainnya[0]);
			m_data.addElement(m_tunjangan_lainnya[1]);
			m_data.addElement(new Integer(0));
			m_model.addRow(m_data);
		}


		String[] strTotal = getStrValue(m_total);
	  	m_model.addRow(new Object[]{"TOTAL PEMBAYARAN PER DEPARTMENT","","",
	  			strTotal[0] ,strTotal[1],new Integer(1)});
	  	setTotal(m_total);
	}

	private void presentingDataReport(EmployeePayrollSubmit[] empPaySub) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
//		int leftMargin = 0;
		helper.prepareColumnHelper(m_payrollComponents);
		m_data = new Vector();
		for(int i=0;i<empPaySub.length;i++){
			if(empPaySub[i]!=null){
				if(tempIndex!=empPaySub[i].getEmployeeIndex()){
					if (m_data.size()>0){
						m_data.addElement(m_tunjangan_lainnya[0]);
						m_data.addElement(m_tunjangan_lainnya[1]);
						m_model.addRow(m_data);
					}
					m_data = new Vector();
					reportContent(empPaySub[i], no++);
					tempIndex= empPaySub[i].getEmployeeIndex();
//					leftMargin = m_data.size();
				}
				int colIdx = helper.getColumnIndex(empPaySub[i].getPayrollComponentIndex());
				if(colIdx!=-1){
					double value=empPaySub[i].getValue();
					if(value!= -1){
						m_tunjangan_lainnya[colIdx] = m_formatDesimal.format(value);
					}else{
						m_tunjangan_lainnya[colIdx] = "";
					}
				}
			}
		}
		if (m_data.size()>0) {
			m_data.addElement("");
			m_data.addElement(m_tunjangan_lainnya[0]);
			m_data.addElement(m_tunjangan_lainnya[1]);
			m_model.addRow(m_data);
		}

	}

	private void tableContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		System.out.println("name = "+name );
		m_data.addElement(submit.getJobTitle());
	}

	private void reportContent(EmployeePayrollSubmit submit, int i) {
		m_data.add(m_panel.getUnitDescription());
		m_data.add(m_panel.getMonthYearString());
		m_data.addElement(String.valueOf(i + 1));
		m_data.addElement(submit.getEmployeeNo());
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		System.out.println("name = "+name );
		m_data.addElement(submit.getJobTitle());

	}

	private void summaryContent(EmployeePayrollSubmit submit, int i) {
		m_data.addElement(String.valueOf(i + 1));
		String name=submit.getName();
		m_data.addElement(name); // menampilkan name
		m_data.addElement(submit.getEmployeeNo());

	}
	//every employee has one or more PayrollComponent


	private void getNonSubmit(EmployeePayrollSubmit employeepayrollSubmit) {
		//PayrollCalcResult[] results;
		if (m_isnotFindEmployee)
			m_panel.m_employeePayVector.clear();
		try {
			for(int i=0; i<m_emps.length; i++){
				m_data = new Vector();
				PayrollCalcResult[] results = getPayrollCalcResult(m_payrollComponents, m_emps[i]);
				if (m_mode==0){
					if (m_summary){
						m_data.addElement(String.valueOf(i+1));
						m_data.addElement(getName(i));
						m_data.addElement(m_emps[i].getEmployeeno());
						employeepayrollSubmit.setEmployee_n(m_emps[i]);
						if (m_emps[i].getAutoindex()==35){
							System.err.println(m_emps[i].getAutoindex() + " " + getName(i));
						}
						storeAll(results, i);
						getPayrollComponentsForSubmit(employeepayrollSubmit, results, i);
						m_data.addElement(new Integer(0));
						m_model.addRow(m_data);
					}else{
						m_data.addElement(String.valueOf(i+1));
						m_data.addElement(getName(i));
						m_data.addElement(m_emps[i].getJobtitle());
						employeepayrollSubmit.setEmployee_n(m_emps[i]);
						storeAll(results, i);
						getPayrollComponentsForSubmit(employeepayrollSubmit, results, i);
						m_data.addElement(Boolean.FALSE); // i add this
						m_model.addRow(m_data);
						m_panel.m_table.setModelKu();
					}
				}else{
					m_tunjangan_lainnya[0] = " ";
					m_tunjangan_lainnya[1] = " ";
					m_data.addElement(m_descUnit);
					m_data.addElement(m_date);
					m_data.addElement(Integer.toString(i+1));
					m_data.addElement(m_emps[i].getEmployeeno());
					m_data.addElement(getName(i));
					m_data.addElement(m_emps[i].getJobtitle());
					employeepayrollSubmit.setEmployee_n(m_emps[i]);
					PayrollCalcResult res;
					for(int j=0; j<m_payrollComponents.length; j++){
						res = null;
						if(results!=null){
							for(int k=0; k<results.length; k++){
								if(m_payrollComponents[j].getIndex() == results[k].component.getIndex()){
									res = results[k];
									break;
								}
							}
						}
						store(res, j);
					}
					m_data.addElement(m_tunjangan_lainnya[0]);
					m_data.addElement(m_tunjangan_lainnya[1]);
					m_data.addElement(Boolean.FALSE); // i add this
					m_model.addRow(m_data);
				}
			}
			if (m_summary){
			  	String[] strTotal = getStrValue(m_total);
			  	m_model.addRow(new Object[]{"TOTAL PEMBAYARAN PER DEPARTMENT","","",
			  			strTotal[0] ,strTotal[1],new Integer(1)});
			  	setTotal(m_total);
			  } else {
				m_data = new Vector();
				String[] strTotal = getStrValue(m_total);
				m_data.addElement("TOTAL");
				for (int x = 1; x <= 5; x++)
					m_data.addElement("");
				m_data.addElement(strTotal[0]);
				m_data.addElement(strTotal[1]);
				m_data.addElement(Boolean.TRUE);
				m_model.addRow(m_data);
			}
		}catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	double[] m_total ={0,0};
	public void setTotal(double[] total){
		m_total =  total;
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

	private void storeAll(PayrollCalcResult[] results, int i) {
		PayrollCalcResult res;
		for(int j=0; j<m_payrollComponents.length; j++){
			res = null;
			if(results!=null){
				for(int k=0; k<results.length; k++){
					if(m_payrollComponents[j].getIndex() == results[k].component.getIndex()){
						res = results[k];
						break;
					}
				}
			}
			store(res, j);
		}
	}

	boolean m_isnotFindEmployee= false;
	public void setIsnotFindEmployee(boolean find){
		m_isnotFindEmployee=find;
	}
	public void createTableBody(){
		EmployeePayrollSubmit employeepayrollSubmit = m_panel.getEmpPayrollSubmit();
		EmployeePayrollSubmit[] employeePaySubm = m_empPaySubmits;
		try {
			if(employeePaySubm!=null){
				if(employeePaySubm.length>0){
					setTextfieldStatus(employeePaySubm[0]);
					if(employeePaySubm[0].getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
						m_panel.m_submitBt.setEnabled(true);
						m_panel.m_updateFlag=true;
					} else{
						m_panel.m_submitBt.setEnabled(false);
						m_panel.m_updateFlag=false;
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
			getNonSubmit(employeepayrollSubmit);
		}else{
			getSubmit(employeePaySubm);
		}
	}

	boolean m_summary;
	public void tableBodyReport(boolean summary){
		m_summary = summary;
		m_mode = 0;
		if (m_emps.length==0){
			addEmptyRow();
		}else{
			createTableBody();
		}
	}


	public void createReportBody(HRMPayrollOtherAllowanceJasper jasper) throws Exception {
		m_mode  = 1;
		if (m_emps.length==0){
			addEmptyRow();
		}else {
			createTableBody();
			/* for (int i=0;i<m_emps.length;i++){
			 PayrollCalcResult[] results;
			 m_GajiPokok[0] = " ";
			 m_GajiPokok[1] = " ";
			 results = getPayrollComponentResult(m_payrollComponents, i);
			 m_data = new Vector();
			 m_data.addElement(m_descUnit);
			 m_data.addElement(m_date);
			 m_data.addElement(Integer.toString(i+1));
			 m_data.addElement(m_emps[i].getEmployeeno());
			 m_data.addElement(getName(i));
			 m_data.addElement(m_emps[i].getJobtitle());
			 loopAllComponent(results);
			 m_data.addElement("");
			 m_data.addElement(m_GajiPokok[0]);
			 m_data.addElement(m_GajiPokok[1]);
			 m_model.addRow(m_data);
			 }	*/
		}
	}
}
