package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEsDiff;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.PmtESDiff;
import pohaci.gumunda.titis.accounting.logic.BeginningESBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class Vchr_ESDiff {
	public Vchr_ESDiff(Connection conn,long sessionid,PmtESDiff entity,Object objPayto,Object objEsno){
		try {			
			String filename = "Vchr_ESDiff";
            String compiledRptFilename = ReportUtils.compileReport(filename);
            if (compiledRptFilename == "") return;
            Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,###.00");
			
			parameters.put("param_logo", "../images/TS.gif");			
			parameters.put("param_no", entity.getReferenceNo());
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName());
			else if (entity.getCashAccount()!=null)
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName());
			
			if (objPayto!=null){
				Employee emp = (Employee)objPayto;
				parameters.put("param_pay_to", emp.getFirstName() + " " + emp.getMidleName() + " " + emp.getLastName());			
				parameters.put("param_job_title", "");
			}
			
			if (objEsno instanceof ExpenseSheet) {
				ExpenseSheet es = (ExpenseSheet)objEsno;
				parameters.put("param_ES_no", es.getReferenceNo());
				if (es.getProject()!=null){
					ProjectData pro = es.getProject();
					parameters.put("param_unit_code", pro.getUnit().toString());
					if (pro.getDepartment()!=null)
						parameters.put("param_department_code", pro.getDepartment().toString());
				}else{
					if (es.getPmtCaOthers()!=null){
						parameters.put("param_unit_code",es.getPmtCaOthers().getUnit().getDescription()); 
						if (es.getPmtCaOthers().getDepartment()!=null)
							parameters.put("param_unit_code",es.getPmtCaOthers().getDepartment().getDescription());
					}else if (es.getPmtCaIouOthersSettled()!=null){
						if (es.getPmtCaIouOthersSettled().getPmtcaiouothers()!=null){
							if (es.getPmtCaIouOthersSettled().getPmtcaiouothers().getUnit()!=null)
								parameters.put("param_unit_code",es.getPmtCaIouOthersSettled().getPmtcaiouothers().getUnit().getDescription());
							if (es.getPmtCaIouOthersSettled().getPmtcaiouothers().getDepartment()!=null)
								parameters.put("param_department_code", es.getPmtCaIouOthersSettled().getPmtcaiouothers().getDepartment().getDescription());
						}
					}
					else if (es.getBeginningBalance()!=null){
						BeginningCashAdvance bca = es.getBeginningBalance();
						BeginningESBusinessLogic logic = new BeginningESBusinessLogic(conn, sessionid);
						logic.getTransaction(bca);
						parameters.put("param_unit_code",bca.getTrans().getUnit().toString());
					}
				}
			}
			else if (objEsno instanceof BeginningEsDiff) {
				BeginningEsDiff BB = (BeginningEsDiff) objEsno;
				EmployeePicker helpEmp=new EmployeePicker(conn,sessionid);			
				helpEmp.findEmployee(BB.getEmployee());		
				parameters.put("param_unit_code",helpEmp.getUnit());
				parameters.put("param_department_code",helpEmp.getDepartment());
			}
			
			parameters.put("param_transaction_type","Expense Sheet Difference Payment");
						
			if (entity.getEmpApproved()!=null){
				String empApproved =entity.getEmpApproved().getFirstName() + " " +
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
			
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("status");
			
			Currency currency = entity.getCurrency();
			double total = 0;
			String title ="";
			
					total = entity.getAmount();    		 
			model.addRow(new Object[]{"1",entity.getDescription(),
					currency.getSymbol() + " " + desimalFormat.format(total),new Integer(1)});
			
			
			model.addRow(new Object[]{"", "TOTAL",currency.getSymbol() + " " + desimalFormat.format(total), new Integer(2)});

			String moneyTalk = MoneyTalk.say(total,currency,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			
			model.addRow(new Object[]{moneyTalk, "", "", new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);	
			
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle(title);
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
