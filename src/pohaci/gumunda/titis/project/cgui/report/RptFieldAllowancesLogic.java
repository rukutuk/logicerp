package pohaci.gumunda.titis.project.cgui.report;


import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.logic.EmployeeFieldAllowance;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;
import pohaci.gumunda.titis.project.cgui.EmployeeTimesheet;

public class RptFieldAllowancesLogic {
	DefaultTableModel m_model; // model tabel
    //EmployeePayroll m_empPayroll; // payrollcomponent dari employee
    RptFieldAllowances m_panel; // kelass payroll insurance allowance
    //String m_descUnit,m_date; // unit dan tanggal
    EmployeeTimesheet[] m_employeeTimesheet = null; // employee time sheet    
    int n=0; // nilai array menentukan beban penjualan atau beban operasional dari tunjangan lapangan
    PayrollComponent[] m_payrollComponents;
    Vector m_data = null; 
	int m_mode = 0;	// jika mode 0 = report; jika mode 1 = tabel
	String[] m_Pendapatan= new String[2]; // tunjangan lapangan 
	AllowenceMultiplier[] m_areaCode = null; // areacode A1 atau A2
	EmployeeFieldAllowance empFieldAllowance = null; // 
	String m_descUnit;
	String m_descDept;
	String m_actName;
	String m_date;	
	
	int m_month;
	int m_year;
	Connection m_conn = null;
	long m_sessionid = -1;
	int m_totA1 = 0;
	int m_totA2 = 0;
	double m_sub_totPenjualan = 0.00;
	double m_sub_totOperasional = 0.00;
	double m_totPenjualan = 0.00;
	double m_totOperasional = 0.00;
	int m_getMergRow = 0;
	DecimalFormat m_formatDesimal;
	
	public RptFieldAllowancesLogic(DefaultTableModel model,RptFieldAllowances panel, EmployeeTimesheet[] employeeTimesheet,
			AllowenceMultiplier[] areacode,PayrollComponent[] payrollComponents, Connection conn, long sessionid, boolean cekExcel){
		this.m_model	= model;
		this.m_descUnit = panel.getUnitDescription();
		this.m_descDept = panel.getDeptDescription();
		this.m_actName 	= panel.getActivityName();
		this.m_date		= panel.getMonthYearString();
		this.m_areaCode = areacode;
		this.m_panel = panel;
		this.m_employeeTimesheet = employeeTimesheet;
		this.m_payrollComponents = payrollComponents;				
		this.m_month = panel.getMonth();
		this.m_year = panel.getYear();
		this.m_conn = conn;
		this.m_sessionid = sessionid;
		if(cekExcel)
			this.m_formatDesimal = new DecimalFormat("###.00");
		else this.m_formatDesimal = new DecimalFormat("#,###.00");
	}
	
	 void addEmptyRow()	{
		 if (m_mode==0) // JASPER
			 m_model.addRow(new Object[]{m_descUnit,m_descDept,m_actName,m_date,"","","","","","","","","","","","","",""});
	}
	
	private void processValueResult(PayrollCalcResult res) {
		if (m_mode == 0) {
			if (res != null) {				
				m_Pendapatan[n] = m_formatDesimal.format(res.value);
				if (n == 0) {
					if (m_getMergRow!=3){
						m_sub_totPenjualan += res.value;
						if (m_getMergRow!=4)
							m_totPenjualan +=res.value;
					}						
					n = n + 1;
				} else {
					if (m_getMergRow!=3){	
						m_sub_totOperasional +=res.value;
						if (m_getMergRow!=4)
							m_totOperasional +=res.value;
					}
					n = n - 1;
				}
			}else{
				m_Pendapatan[n] = " ";
				if (n == 0) {
					n = n + 1;
				} else {
					n = n - 1;
				}
			}
		} else {
			if (res != null) {
				m_data.addElement(new Double(res.value));
			} else {
				m_data.addElement("");
			}
		}
	}
	
	private PayrollCalcResult[] getPayrollComponentResult(PayrollComponent[] payrollComponents, int i) throws Exception {
		PayrollCategoryComponent[] comps;
		PayrollCalcResult[] results;
		comps = m_panel.getPayrollCategoryComponents(m_employeeTimesheet[i], payrollComponents);	
		
		empFieldAllowance = new EmployeeFieldAllowance(m_conn, m_year, m_month, m_sessionid);
		
		if(comps!=null){	
			results = empFieldAllowance.calcPayrollComponent(comps, m_employeeTimesheet[i].getIndexPersonal(),
					m_employeeTimesheet[i].getAreacode(), m_employeeTimesheet[i].getDays());
		}else{			
			results = null;			
		}			
		return results;
	}
	
	private void loopAllComponent(PayrollCalcResult[] results) {		
		PayrollCalcResult res;		
		for(int j=0; j<m_payrollComponents.length; j++){
			res = null;			
			if(results!=null){
				for(int k=0; k<results.length; k++){
					if(m_payrollComponents[j].getIndex()==results[k].component.getIndex()){
						res = results[k];												
						break;
					}
				}
			}
			processValueResult(res);												
		}
	}
	
	public void tableBody(){		
		m_mode = 1;		
		for(int i=0; i<m_employeeTimesheet.length; i++){			
			m_data = new Vector();
			m_data.addElement(String.valueOf(i+1));
			if (i>0){
				if (m_employeeTimesheet[i].getEmployeeno().equals(m_employeeTimesheet[i-1].getEmployeeno())){
					
				}
			}
			m_data.addElement(m_employeeTimesheet[i].getEmployeeno());
			m_data.addElement(m_employeeTimesheet[i].getAllName());
			m_data.addElement(m_employeeTimesheet[i].getJobTitle());
			m_data.addElement(m_employeeTimesheet[i].getQualification());
			m_data.addElement(m_employeeTimesheet[i].getWorkdescription());
			m_data.addElement(m_employeeTimesheet[i].getClient());
			m_data.addElement(m_employeeTimesheet[i].getIpcNo());
			for(int j=0;j<m_areaCode.length;j++){						
				if ((j+1)==Integer.parseInt(m_employeeTimesheet[i].getAreacode().substring(1))){
					m_data.addElement(new Integer(m_employeeTimesheet[i].getDays()));
				} else {
					m_data.addElement(" ");
				}
			}	
			PayrollCalcResult[] results = null;
			try{				
				results = getPayrollComponentResult(m_payrollComponents, i);
							
			}catch(Exception ex){				
			}			
			loopAllComponent(results);
			m_model.addRow(m_data);
		}
		
	}
	
	public void tableBodyReport() {
		m_mode = 0;
		int i = 0;
		int no = 0;
		String strNo = "";
		int employRow = m_employeeTimesheet.length;
		String empNo = "";
		String empName;
		String empJob;
		if (employRow == 0) {
			m_totPenjualan = 0;
			addEmptyRow();
		} else {
			while (i < employRow) {
				empNo = m_employeeTimesheet[i].getEmployeeno();
				empName = m_employeeTimesheet[i].getAllName();
				empJob = m_employeeTimesheet[i].getJobTitle();
				m_data = new Vector();
				m_Pendapatan[0] = " ";
				m_Pendapatan[1] = " ";
				m_data.addElement(m_descUnit);
				m_data.addElement(m_descDept);
				m_data.addElement(m_actName);
				m_data.addElement(m_date);
				//addRowData(i, m_getMergRow, strNo, empNo, empName, empJob);
				if (i == 0) {
					if (!empNo.equals(m_employeeTimesheet[i + 1]
					 									.getEmployeeno()))
						m_getMergRow = 2;
					strNo = String.valueOf(++no);
					addRowData(i, m_getMergRow, strNo, empNo, empName, empJob);
				} else {
					if (empNo
							.equals(m_employeeTimesheet[i - 1].getEmployeeno())) { // if
																					// satu
																					// merge
						
						m_getMergRow = 1;
						if (i < (employRow - 1)) {
							if (!empNo.equals(m_employeeTimesheet[i + 1]
									.getEmployeeno()))
								m_getMergRow = 2;
						} 
						else
							m_getMergRow = 2;
						
						strNo = "";
						empNo = "";
						empName = "";
						empJob = "";
						
						addRowData(i, m_getMergRow, strNo, empNo, empName,
								empJob);
					} else {
						strNo = String.valueOf(++no);
						if (i < (employRow - 1)) {
							if (empNo.equals(m_employeeTimesheet[i + 1]
									.getEmployeeno())) { // if akhir merge
								m_getMergRow = 4;
								addRowData(i, m_getMergRow, "", "", "", ""); // masih
																				// 12
								m_getMergRow = 0;
								m_data = new Vector();
								m_Pendapatan[0] = " ";
								m_Pendapatan[1] = " ";
								m_totA1 = 0;
								m_totA2 = 0;
								m_sub_totPenjualan = 0.00;
								m_sub_totOperasional = 0.00;
								m_data.addElement(m_descUnit);
								m_data.addElement(m_descDept);
								m_data.addElement(m_actName);
								m_data.addElement(m_date);
								addRowData(i, m_getMergRow, strNo, empNo,
										empName, empJob);
							} else {
								m_getMergRow = 4;
								addRowData(i, m_getMergRow, "", "", "", ""); // masih
																				// 12
								m_getMergRow = 2;
								m_data = new Vector();
								m_Pendapatan[0] = " ";
								m_Pendapatan[1] = " ";
								m_totA1 = 0;
								m_totA2 = 0;
								m_sub_totPenjualan = 0.00;
								m_sub_totOperasional = 0.00;
								m_data.addElement(m_descUnit);
								m_data.addElement(m_descDept);
								m_data.addElement(m_actName);
								m_data.addElement(m_date);
								addRowData(i, m_getMergRow, strNo, empNo,
										empName, empJob);
							}
						} else {
							m_getMergRow = 2;
							
							addRowData(i, m_getMergRow, strNo, empNo, empName, empJob);
						}
					}
					if (i == (employRow - 1)) { // apakah dah nyampe ujung
						m_getMergRow = 4;
						m_data = new Vector();
						m_Pendapatan[0] = " ";
						m_Pendapatan[1] = " ";
						m_data.addElement(m_descUnit);
						m_data.addElement(m_descDept);
						m_data.addElement(m_actName);
						m_data.addElement(m_date);
						addRowData(i, m_getMergRow, strNo, empNo, empName,
								empJob);
						m_getMergRow = 5;
						m_data = new Vector();
						m_Pendapatan[0] = " ";
						m_Pendapatan[1] = " ";
						m_data.addElement(m_descUnit);
						m_data.addElement(m_descDept);
						m_data.addElement(m_actName);
						m_data.addElement(m_date);
						addRowData(i, m_getMergRow, strNo, empNo, empName,
								empJob);
					}
				}
				i += 1;
			}
		}
	}

	private void addRowData(int i, int getMergRow, String strNo, String empNo, String empName, String empJob) {
		try{
		java.util.Date date = new java.util.GregorianCalendar().getTime();
		java.text.DateFormat formatThn = new java.text.SimpleDateFormat("yyyy");
		java.text.DateFormat formatBln = new java.text.SimpleDateFormat("MM");
		java.text.DateFormat formatTgl = new java.text.SimpleDateFormat("dd");
		String thn = formatThn.format(date);
		String bln = formatBln.format(date);
		String tgl = formatTgl.format(date);
		String today = tgl + "-" + bln + "-" + thn;
		String A1;
		String A2;
		m_data.addElement(strNo);
		m_data.addElement(empNo);
		m_data.addElement(empName);
		m_data.addElement(empJob);		
		m_data.addElement(m_employeeTimesheet[i].getQualification());
		m_data.addElement(m_employeeTimesheet[i].getWorkdescription());
		m_data.addElement(m_employeeTimesheet[i].getClient());
		m_data.addElement(m_employeeTimesheet[i].getIpcNo());		
		
		String area = m_employeeTimesheet[i].getAreacode().trim();
		A1 = "";
		A2 = "";
		if (area.equals("A1")){
			  A1 =  String.valueOf(m_employeeTimesheet[i].getDays());
			  if (getMergRow!=3)
			  m_totA1 +=  m_employeeTimesheet[i].getDays();
		}else{ 
			  A2 =  String.valueOf(m_employeeTimesheet[i].getDays());
			  if (getMergRow!=3)
			  m_totA2 +=  m_employeeTimesheet[i].getDays();
		}
		
		loopAllComponent(getPayrollComponentResult(m_payrollComponents, i));
		
		m_data.addElement(A1);
		m_data.addElement(A2);
		m_data.addElement(m_Pendapatan[0]);
		m_data.addElement(m_Pendapatan[1]);
		
		m_data.addElement(String.valueOf(m_totA1));
		m_data.addElement(String.valueOf(m_totA2));	
		if (m_sub_totPenjualan!=0)
			m_data.addElement(m_formatDesimal.format(m_sub_totPenjualan));
		else
			m_data.addElement(" ");
		if(m_sub_totOperasional!=0)
			m_data.addElement(m_formatDesimal.format(m_sub_totOperasional));
		else
			m_data.addElement("");
		
		m_data.addElement(m_formatDesimal.format(m_totPenjualan));
		m_data.addElement(m_formatDesimal.format(m_totOperasional));		
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			AllowenceMultiplier[] allowenceMultiplier = logic.getAllAllowenceMultiplier(m_sessionid,"");
		
		float data1 = 0,data2 = 0;
		for (int j=0;j<allowenceMultiplier.length;j++){
			if (allowenceMultiplier[j].getAreaCode().equals("A1"))
				data1 =  allowenceMultiplier[j].getMuliplier();
			else
				data2 =  allowenceMultiplier[j].getMuliplier();
		}
		m_data.addElement(String.valueOf(data1));
		m_data.addElement(String.valueOf(data2));
		m_data.addElement("");
		m_data.addElement(today);		
		m_data.addElement(new Integer(getMergRow));
		
		m_model.addRow(m_data);
		}catch(Exception ex){				
		}				
	
	}
}
