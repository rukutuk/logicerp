package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
//import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.RcvEmpReceivable;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class Rcpt_EmpReceivable {	
	Connection m_conn;
	long m_sessionid;
	public Rcpt_EmpReceivable(RcvEmpReceivable entity,Connection conn, long sessionid,Employee employee){
		m_conn=conn;
		m_sessionid = sessionid;
		try {
			String filename = "Rcpt_EmpReceivable";
            String compiledRptFilename = ReportUtils.compileReport(filename);
            if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			DefaultTableModel model = new DefaultTableModel();      
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			if (entity.getTransactionDate()!=null)
				parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
			else
				parameters.put("", "");
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName());
			else
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName());
			
			if (employee!=null){				
				String empName = employee.getFirstName() + " " + employee.getMidleName() + " " + employee.getLastName();
				parameters.put("param_receive_from", empName);					
				Employment[] employs = getEmployeeEmployment(employee.getIndex());
				Employment  employment = getMaxEmployment(employs);
				if (employment!=null){					
					parameters.put("param_job_tittle", employment.getJobTitle().getName());
					parameters.put("param_department_code", employment.getDepartment().getName());
				}else{
					parameters.put("param_job_tittle", "");
					parameters.put("param_department_code", "");
				}				
				if (entity.getEmpReceivable()!=null)
					parameters.put("param_referenceno", entity.getEmpReceivable().getReferenceNo());				
			}
			
			parameters.put("param_transactiont_type", "Employee Receivable Receive");			
			
			if (entity.getEmpApproved()!=null){
				String empApproved = entity.getEmpApproved().getFirstName() + " " +
				entity.getEmpApproved().getMidleName() + " " + entity.getEmpApproved().getLastName();
				parameters.put("param_nama_approver", empApproved);
			}else
				parameters.put("param_nama_approver", "");
			parameters.put("param_jabatan_approver", entity.getJobTitleApproved());
			if (entity.getDateApproved()!=null)
				parameters.put("param_date_approver", dateFormat.format(entity.getDateApproved()));
			else
				parameters.put("param_date_approver", "");
			
			if (entity.getEmpOriginator()!=null){
				String empOriginator = entity.getEmpOriginator().getFirstName() + " " +
				entity.getEmpOriginator().getMidleName() + " " + entity.getEmpOriginator().getLastName();
				parameters.put("param_nama_originator", empOriginator);
			}else
				parameters.put("param_nama_originator", "");			
			parameters.put("param_jabatan_originator", entity.getJobTitleOriginator());
			if (entity.getDateOriginator()!=null)
				parameters.put("param_date_originator", dateFormat.format(entity.getDateOriginator()));
			else
				parameters.put("param_date_originator", "");
			
			if (entity.getEmpReceived()!=null){
				String empReceived = entity.getEmpReceived().getFirstName() + " " +
				entity.getEmpReceived().getMidleName() + " " + entity.getEmpReceived().getLastName();
				parameters.put("param_nama_receiver", empReceived);
			}else
				parameters.put("param_nama_receiver", "");			
			parameters.put("param_jabatan_receiver", entity.getJobTitleReceived());
			if (entity.getDateReceived()!=null)
				parameters.put("param_date_receiver", dateFormat.format(entity.getDateReceived()));
			else
				parameters.put("param_date_receiver", "");
			
			
			model.addColumn("col_no");
			model.addColumn("col_desc");
			model.addColumn("col_amount");
			model.addColumn("status");
			
			      
			double total = 0;     
			
			Currency currency = entity.getCurrency();
			String curr = currency.getSymbol();
			total = entity.getAmount();
			
			
			model.addRow(new Object[]{
					"1", entity.getDescription(), 
					curr + " " + desimalFormat.format(total),new Integer(0)});
			
			model.addRow(new Object[]{"", "TOTAL",curr  + " " + desimalFormat.format(total), new Integer(1)});
			

			String moneyTalk = MoneyTalk.say(total,currency,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			
			model.addRow(new Object[]{"Amount in word : " + moneyTalk ,"","", new Integer(2)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Employee Receivable Receipt");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
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
}
