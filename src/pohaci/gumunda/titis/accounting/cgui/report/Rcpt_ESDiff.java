package pohaci.gumunda.titis.accounting.cgui.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.entity.RcvESDiff;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class Rcpt_ESDiff {
	public Rcpt_ESDiff(RcvESDiff entity){
		try {
			String filename = "Rcpt_ESDiff.jrxml";
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);   
			Map parameters = new HashMap();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			DefaultTableModel model = new DefaultTableModel();     
			
			parameters.put("param_logo", "../images/TS.GIF");
			parameters.put("param_no", entity.getReferenceNo());
			if (entity.getTransactionDate()!=null)
				parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
			else
				parameters.put("", "");			
			
			if (entity.getEsNo()!=null){
				if (entity.getEsNo().getEsOwner()!=null){
					String receive_from = entity.getEsNo().getEsOwner().getFirstName() + " " + 
					entity.getEsNo().getEsOwner().getMidleName() + " " + entity.getEsNo().getEsOwner().getLastName();
					parameters.put("param_receive_from",receive_from);			
					parameters.put("param_job_tittle", entity.getEsNo().getEsOwner().getJobTitleName());
				}
				if (entity.getEsNo().getProject()!=null){
					ProjectData project = entity.getEsNo().getProject();
					if (project.getDepartment()!=null)
						parameters.put("param_department_code", project.getDepartment().getName());
					else
						parameters.put("param_department_code", "");
					if (project.getUnit()!=null)
						parameters.put("param_unit_code", entity.getEsNo().getProject().getUnit().getDescription());
					else
						parameters.put("param_unit_code", "");
				}
				parameters.put("param_expense_sheet_no", entity.getEsNo().toString());
			}
			else if (entity.getBeginningBalance()!=null){
				parameters.put("param_expense_sheet_no", entity.getBeginningBalance().toString());
			}
			
			
			parameters.put("param_type", "Expense Sheet Difference Receive");
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName());
			else
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName());
			
			if (entity.getEmpApproved()!=null){
				String empApproved = entity.getEmpApproved().getFirstName() + " " + 
				entity.getEmpApproved().getMidleName() + " " +entity.getEmpApproved().getLastName(); 
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
			      
			double total = 0;     			
			
			total = entity.getAmount();
			Currency currency = entity.getCurrency();
			String curr = currency.getSymbol();			
			
			model.addRow(new Object[]{
					"1", entity.getDescription(), 
					curr  +" "+ desimalFormat.format(total), new Integer(1)});
			model.addRow(new Object[]{"", "TOTAL",curr +" "+ desimalFormat.format(total), new Integer(2)});

			String moneyTalk = MoneyTalk.say(total,currency,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{"Amount in word : " + moneyTalk , "", "", new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);			
			
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Expense Sheet Difference Receipt");
			view.setVisible(true);
		}
		catch(Exception ex){	
			ex.printStackTrace();
		}
	}
	
}
