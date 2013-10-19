package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.cgui.DefaultUnit;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.BeginningCashAdvanceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUOthersBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUOthersSettledBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUProjecSettledtBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUProjectBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAOthersBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAProjectBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.PeriodStartEnd;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.cgui.SimpleEmployee;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class CashAdvance{
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodStartEnd m_periodStartEnd;
	UnitPicker m_unitPicker;
	Account m_account;
	DecimalFormat m_desimalFormat; 
	AccountingBusinessLogic m_logic;
	DatePicker m_datePicker;
	String m_CAcombo;
	boolean m_isExcel=false;
	public CashAdvance(){
		setEmpty();
	}
	
	public CashAdvance(Connection conn,long sessionid,UnitPicker unitPicker,DatePicker date,String CAcombo,boolean isExcel){
		m_conn = conn;
		m_sessionid = sessionid;
		m_unitPicker = unitPicker;
		m_CAcombo = CAcombo;
		m_datePicker = date;
		m_isExcel=isExcel;
		if (CAcombo.equals("Project"))
			getPmtCAIOUProject();			
		else
			getPmtCAIOUOthers();
	}
	
	private void setEmpty() {
		try {			
			String filename = "cash_advance_project";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			parameters.put("param_logo", "../images/TS.gif");			
			parameters.put("param_CA","");
			parameters.put("param_unit_code","");			
			parameters.put("param_date", "");		
			HeaderProject(model);
			model.addRow(new Object[]{"","","","","","","","","","",new Integer(1)});
			model.addRow(new Object[]{"TOTAL","","","","","","","","","",new Integer(2)});
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getPmtCAIOUProject() {
		try {			
			String filename = "cash_advance_project";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			String usaDate = dateFormat2.format(m_datePicker.getDate());
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_CA","PROJECT CASH ADVANCE REPORT");
			parameters.put("param_unit_code", "");
			if(m_unitPicker.getUnit() != null)
				parameters.put("param_unit_code",m_unitPicker.getUnit().toString());			
			parameters.put("param_date", dateFormat.format(m_datePicker.getDate()));
			DefaultTableModel model = new DefaultTableModel();
			HeaderProject(model);			
			int i=0;
			double total = 0;
			String strUnit = "";
			if(m_unitPicker.getUnit() != null)
				strUnit = " and unit = "+ m_unitPicker.getUnit().getIndex();
			HRMBusinessLogic hrmlogic = new HRMBusinessLogic(m_conn);
			BeginningCashAdvanceBusinessLogic beginCALogic = new BeginningCashAdvanceBusinessLogic(m_conn,m_sessionid);
			String selectwhere = IDBConstants.ATTR_PROJECT +" is not null";
			List beginlist = beginCALogic.getOutstandingProject(selectwhere);
			Iterator beginiterator = beginlist.iterator();			
			while (beginiterator.hasNext()){
				BeginningCashAdvance bb = (BeginningCashAdvance)beginiterator.next();
				bb.setTrans(beginCALogic.findTransaction(getUnit(bb)));
				String query = setQueryEmployee(usaDate, strUnit, bb.getEmployee());				
				SimpleEmployee emp = hrmlogic.getEmployeeReceivableReportByUnit(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
				if (emp!=null){				
					total += bb.getAccValue()*bb.getExchangeRate();
					String name = bb.getEmployee().getFirstName() + " " + bb.getEmployee().getMidleName() + " " + bb.getEmployee().getLastName();
					if (bb.getTrans()!=null)
						model.addRow(new Object[]{String.valueOf(++i),name,bb.getEmployee().getEmployeeNo(),
								emp.getDepartment(),bb.getProject().getCode(),
								dateFormat.format(bb.getTrans().getTransDate()),bb.getTrans().getReference(),
								bb.getCurrency().getSymbol() + " " + desimalFormat.format(bb.getAccValue()),
								"Rp " + desimalFormat.format(bb.getExchangeRate()),
								"Rp " + desimalFormat.format(bb.getAccValue()*bb.getExchangeRate()),new Integer(1)});
					else
						model.addRow(new Object[]{String.valueOf(++i),name,bb.getEmployee().getEmployeeNo(),
								emp.getDepartment(),bb.getProject().getCode(),
								"","",
								bb.getCurrency().getSymbol() + " " + desimalFormat.format(bb.getAccValue()),
								"Rp " + desimalFormat.format(bb.getExchangeRate()),
								"Rp " + desimalFormat.format(bb.getAccValue()*bb.getExchangeRate()),new Integer(1)});
				}
			}
			
			CAIOUProjectBusinessLogic logic = new CAIOUProjectBusinessLogic(m_conn,m_sessionid);
			List list = logic.getOutstandingByTransDateList(usaDate);
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){		
				PmtCAIOUProject CaIouPro = (PmtCAIOUProject)iterator.next();
				String query = setQueryEmployee(usaDate, strUnit, CaIouPro.getPayTo());				
				SimpleEmployee emp = hrmlogic.getEmployeeReceivableReportByUnit(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);				
				if (emp!=null){
					CAIOUProjecSettledtBusinessLogic setledlogic = new CAIOUProjecSettledtBusinessLogic(m_conn,m_sessionid,CaIouPro.getPayTo());
					List settledlist = setledlogic.getOutstandingByTransDateList(usaDate);
					Iterator settlediterator = settledlist.iterator();
					while (settlediterator.hasNext()){
						PmtCAIOUProjectSettled empCaIouProSetl = (PmtCAIOUProjectSettled)settlediterator.next();
						String name =CaIouPro.getPayTo().getFirstName() + " " + CaIouPro.getPayTo().getMidleName() + " " + CaIouPro.getPayTo().getLastName();						
						double exchrate =1;
						total += empCaIouProSetl.getAmount()*exchrate;
						model.addRow(new Object[]{String.valueOf(++i),name,emp.getEmployeeNo(),
								emp.getDepartment(),CaIouPro.getProject().getCode(),
								dateFormat.format(empCaIouProSetl.getTransactionDate()),empCaIouProSetl.getReferenceNo(),
								empCaIouProSetl.getCurrency().getSymbol() + " " + desimalFormat.format(empCaIouProSetl.getAmount()),
								"Rp " + desimalFormat.format(exchrate),
								"Rp " + desimalFormat.format(empCaIouProSetl.getAmount()*exchrate),new Integer(1)});
					}
				}				
			}
			
			getPmtCAProject(model,compiledRptFilename,parameters,total,i);	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getPmtCAProject(DefaultTableModel model,String compiledRptFilename,Map parameters,double total,int i) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");		
			String usaDate = dateFormat2.format(m_datePicker.getDate());
			
			CAProjectBusinessLogic logic = new CAProjectBusinessLogic(m_conn,m_sessionid);
			List list = logic.getOutstandingByTransDateList(usaDate);
			Iterator iterator = list.iterator();
			
			String strUnit = "";
			if(m_unitPicker.getUnit() != null)
				strUnit = " and unit = "+ m_unitPicker.getUnit().getIndex();
			while(iterator.hasNext()){		
				PmtCAProject empCaPro = (PmtCAProject)iterator.next();
				String query = setQueryEmployee(usaDate, strUnit, empCaPro.getPayTo());				
				HRMBusinessLogic hrmlogic = new HRMBusinessLogic(m_conn);
				SimpleEmployee emp = hrmlogic.getEmployeeReceivableReportByUnit(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);				
				if (emp!=null){
					total +=(empCaPro.getAmount()*empCaPro.getExchangeRate());
					String name = emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
					model.addRow(new Object[]{String.valueOf(++i),name,emp.getEmployeeNo(),
							emp.getDepartment(),empCaPro.getProject().getCode(),
							dateFormat.format(empCaPro.getTransactionDate()),empCaPro.getReferenceNo(),
							empCaPro.getCurrency().getSymbol() + " " + desimalFormat.format(empCaPro.getAmount()),
							"Rp " + desimalFormat.format(empCaPro.getExchangeRate()),
							"Rp " + desimalFormat.format(empCaPro.getAmount()*empCaPro.getExchangeRate()),new Integer(1)});
				}
			}						
			model.addRow(new Object[]{"TOTAL","","","","","","","","","Rp " + desimalFormat.format(total),new Integer(2)});			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			if (m_isExcel)
				exportToExcel(jasperPrint);
			else
				m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getPmtCAIOUOthers() {
		try {			
			String filename = "cash_advance_other";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") 
				return;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			String usaDate = dateFormat2.format(m_datePicker.getDate());
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			HRMBusinessLogic hrmlogic = new HRMBusinessLogic(m_conn);
			int i=0;
			double total =0;
			String strUnit = "";
			if(m_unitPicker.getUnit() != null)
				strUnit = " and unit = "+ m_unitPicker.getUnit().getIndex();
			Map parameters = new HashMap();
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_CA","OTHERS CASH ADVANCE REPORT");
			parameters.put("param_unit_code", "");
			if(m_unitPicker.getUnit() != null)
				parameters.put("param_unit_code",m_unitPicker.getUnit().toString());			
			parameters.put("param_date", dateFormat.format(m_datePicker.getDate()));	
			DefaultTableModel model = new DefaultTableModel();
			HeaderOthers(model);		
			BeginningCashAdvanceBusinessLogic beginCALogic = new BeginningCashAdvanceBusinessLogic(m_conn,m_sessionid);
			String selectwhere = IDBConstants.ATTR_PROJECT +" is null";
			List beginlist = beginCALogic.getOutstandingProject(selectwhere);
			Iterator beginiterator = beginlist.iterator();
			while (beginiterator.hasNext()){
				BeginningCashAdvance bb = (BeginningCashAdvance)beginiterator.next();
				Transaction trans = beginCALogic.findTransaction(getUnit(bb));
				String query = setQueryEmployee(usaDate, strUnit, bb.getEmployee());				
				SimpleEmployee emp = hrmlogic.getEmployeeReceivableReportByUnit(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
				if (emp!=null){				
					total += bb.getAccValue()*bb.getExchangeRate();
					String name = bb.getEmployee().getFirstName() + " " + bb.getEmployee().getMidleName() + " " + bb.getEmployee().getLastName();
					model.addRow(new Object[]{String.valueOf(++i),name,bb.getEmployee().getEmployeeNo(),
							emp.getDepartment(),dateFormat.format(trans.getTransDate()),trans.getReference(),
							bb.getCurrency().getSymbol() + " " + desimalFormat.format(bb.getAccValue()),
							"Rp " + desimalFormat.format(bb.getExchangeRate()),
							"Rp " + desimalFormat.format(bb.getAccValue()*bb.getExchangeRate()),new Integer(1)});
				}
			}
			CAIOUOthersBusinessLogic logic = new CAIOUOthersBusinessLogic(m_conn,m_sessionid);
			List list = logic.getOutstandingByTransDateList(usaDate);
			Iterator iterator = list.iterator();		
			while(iterator.hasNext()){
				PmtCAIOUOthers CaIouOther = (PmtCAIOUOthers)iterator.next();
				String query = setQueryEmployee(usaDate, strUnit, CaIouOther.getPayTo());				
				SimpleEmployee emp = hrmlogic.getEmployeeReceivableReportByUnit(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
				if (emp!=null){
					CAIOUOthersSettledBusinessLogic settlelogic = new CAIOUOthersSettledBusinessLogic(m_conn,m_sessionid,CaIouOther.getPayTo());
					List settledlist = settlelogic.getOutstandingByTransDateList(usaDate);
					Iterator settlediterator = settledlist.iterator();
					while (settlediterator.hasNext()){
						PmtCAIOUOthersSettled empCaIouOthSetl = (PmtCAIOUOthersSettled)settlediterator.next();
						String name = emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();						
						int exchrate = 1;
						total += (empCaIouOthSetl.getAmount()*exchrate);
						model.addRow(new Object[]{String.valueOf(++i),name,emp.getEmployeeNo(),emp.getDepartment(),
								dateFormat.format(empCaIouOthSetl.getTransactionDate()),empCaIouOthSetl.getReferenceNo(),
								empCaIouOthSetl.getCurrency().getSymbol() + " " + desimalFormat.format(empCaIouOthSetl.getAmount()),
								"Rp " + desimalFormat.format(exchrate),
								"Rp " + desimalFormat.format(empCaIouOthSetl.getAmount()*exchrate),new Integer(1)});
					}
				}
			}
			getPmtCAOthers(model,compiledRptFilename,parameters,total,i);	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getPmtCAOthers(DefaultTableModel model,String compiledRptFilename,Map parameters,double total,int i) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			String usaDate = dateFormat2.format(m_datePicker.getDate());
			CAOthersBusinessLogic logic = new CAOthersBusinessLogic(m_conn,m_sessionid);
			List list = logic.getOutstandingByTransDateList(usaDate);		
			Iterator iterator = list.iterator();
			String strUnit = "";
			if(m_unitPicker.getUnit() != null)
				strUnit = " and unit = "+ m_unitPicker.getUnit().getIndex();

			while(iterator.hasNext()){		
				PmtCAOthers empCaOthr = (PmtCAOthers)iterator.next();
				String query = setQueryEmployee(usaDate, strUnit, empCaOthr.getPayTo());
				HRMBusinessLogic hrmlogic = new HRMBusinessLogic(m_conn);
				SimpleEmployee emp = hrmlogic.getEmployeeReceivableReportByUnit(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);				
				if (emp!=null){
					total +=(empCaOthr.getAmount()*empCaOthr.getExchangeRate());
					String name = emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
					model.addRow(new Object[]{String.valueOf(++i),name,emp.getEmployeeNo(),emp.getDepartment(),
							dateFormat.format(empCaOthr.getTransactionDate()),empCaOthr.getReferenceNo(),
							empCaOthr.getCurrency().getSymbol() + " " + desimalFormat.format(empCaOthr.getAmount()),
							"Rp " + desimalFormat.format(empCaOthr.getExchangeRate()),
							"Rp " + desimalFormat.format(empCaOthr.getAmount()*empCaOthr.getExchangeRate()),new Integer(1)});					
				}
			}						
			model.addRow(new Object[]{"TOTAL","","","","","","","","Rp " + desimalFormat.format(total),new Integer(2)});			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			if (m_isExcel)
				exportToExcel(jasperPrint);
			else
				m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private String setQueryEmployee(String date, String strUnit, Employee emp) {
		String query ="select * from (select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename," +
		"emp.lastname,job.name jobtitle,dept.name department " +
		"from employee emp " +
		"inner join (select e.* from employeeemployment e, " +
		"(select employee, max(tmt) tmt from " +
		"(select * from employeeemployment where tmt<'" +date+ "') group by employee ) " +
		"lastemp where e.employee=lastemp.employee and e.tmt=lastemp.tmt " +
		strUnit+") employment " +
		"on emp.autoindex=employment.employee " +
		"inner join jobtitle job on employment.jobtitle=job.autoindex " +
		"inner join organization dept on employment.department=dept.autoindex " +
		"where not exists " +
		"(SELECT employee FROM employeeretirement ret WHERE ret.tmt<='"+date+"' and emp.autoindex=ret.employee) ) " +
		"where autoindex=" + emp.getIndex();
		return query;
	}
	
	public Unit getUnit(BeginningCashAdvance bb) {
		EmployeePicker helpEmp = new EmployeePicker(m_conn,
				m_sessionid);
		Unit unit = null;
		if (bb.getProject() == null) {
			unit = helpEmp.findUnitEmployee(bb.getEmployee());
		} else {
			unit = bb.getProject().getUnit();
		}
		if (unit == null) {
			UnitPicker unitpicker = new UnitPicker();
			unitpicker.init(m_conn,
					m_sessionid);
			unit = unitpicker.getDefaultUnit();
		}
		return unit;
	}
	
	public Unit findUnitEmployee(Employee employee){
		if (employee!=null){
			Employment[] employs = getEmployeeEmployment(employee.getIndex());
			Employment  employment = getMaxEmployment(employs);
			if (employment!=null) {
				return employment.getUnit();
			}
			else				
				return setDefaultUnit();			
		}
		else
			return null;
	}
	
	public Unit setDefaultUnit(){
		Unit unit = DefaultUnit.createDefaultUnit(m_conn, m_sessionid);
		return unit;		
	}
	
	private Employment[] getEmployeeEmployment(long index) {
		Employment[] employment = null;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			employment = logic.getEmployeeEmployment(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, index);
			
			return employment;
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	private Employment getMaxEmployment(Employment[] employ) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		Date currentday = calendar.getTime();  
		java.util.Date befdate = null, afterdate = null;
		int index = -1;
		for (int i = 0; i < employ.length; i++) {
			if (befdate == null) {
				befdate = employ[i].getTMT();
				index = i;
			}
			if (i + 1 < employ.length) {
				afterdate = employ[i + 1].getTMT();
				if(afterdate.after(currentday)) {
					break;
				}
				if (befdate.compareTo(afterdate) == -1) {
					befdate = afterdate;
					index = i + 1;
				}
			}
		}
		Employment emp = null;
		if (index != -1) {
			emp = employ[index];
		}
		return emp;
	}
	
	private void HeaderOthers(DefaultTableModel model) {	
		model.addColumn("field1");
		model.addColumn("field2");
		model.addColumn("field3");
		model.addColumn("field4");
		model.addColumn("field5");
		model.addColumn("field6");
		model.addColumn("field7");
		model.addColumn("field8");
		model.addColumn("field9");
		model.addColumn("status");
	}
	
	private void HeaderProject(DefaultTableModel model) {	
		model.addColumn("field1");
		model.addColumn("field2");
		model.addColumn("field3");
		model.addColumn("field4");
		model.addColumn("field5");
		model.addColumn("field6");
		model.addColumn("field7");
		model.addColumn("field8");
		model.addColumn("field9");
		model.addColumn("field10");
		model.addColumn("status");
	}
	
	public void exportToExcel(JasperPrint jasperPrint) throws Exception {
		javax.swing.JFileChooser jfc = new javax.swing.JFileChooser("reportsample/");
		
		jfc.setDialogTitle("Simpan Laporan Dalam File Excel");
		jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File file) {
				String filename = file.getName();
				return (filename.toLowerCase().endsWith(".xls") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
			}
			public String getDescription() {
				return "Laporan *.xls";
			}
		});
		
		jfc.setMultiSelectionEnabled(false);    
		jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
		if  (jfc.showSaveDialog(null) == javax.swing.JOptionPane.OK_OPTION) {
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(), "xls"));
			exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
			exporter.exportReport();           
		}
	}
	
	public static String changeFileExtension(String filename, String newExtension ) {
		if (!newExtension.startsWith("."))
			newExtension = "." + newExtension;
		if (filename == null || filename.length()==0 ) {
			return newExtension;
		}
		
		int index = filename.lastIndexOf(".");
		if (index >= 0) {
			filename = filename.substring(0,index);
		}
		return filename += newExtension;
	}
	
	public JRViewer getPrintView(){
		return m_jrv;
	}
}
