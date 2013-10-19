package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;

public class Rcpt_Loan {
	JasperReport jreport = null;
	JasperPrint jprint = null;
	Map param = null;
	
	public Rcpt_Loan(RcvLoan entity,Connection conn) {
		try {
			String filename = "Rcpt_Loan";
            String compiledRptFilename = ReportUtils.compileReport(filename);
            if (compiledRptFilename == "") return;
            
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
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName());
			else
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName());
			
			if (entity.getCompanyLoan()!=null){
				parameters.put("param_receive_from", entity.getCompanyLoan().getCreditorList().getCode() + " " +
						entity.getCompanyLoan().getCreditorList().getName());
				if (entity.getCompanyLoan().getCreditorList()!=null){
					parameters.put("param_loan", entity.getCompanyLoan().getName());
					parameters.put("param_address", entity.getCompanyLoan().getCreditorList().getAddress());
					parameters.put("param_creditor_type", entity.getCompanyLoan().getCreditorList().getCreditorType());
				}
			}
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_unit_code", entity.getBankAccount().getUnit().getDescription());
			else if (entity.getCashAccount()!=null)
				parameters.put("param_unit_code", entity.getCashAccount().getUnit().getDescription());
			else
				parameters.put("param_unit_code", "");
			parameters.put("param_type", "Loan Receive");
			
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
			
			String curr = "";      
			double total = 0;     			
			
			total = entity.getAmount();
			curr = entity.getCurrency().getSymbol();				
			
			model.addRow(new Object[]{
					"1", entity.getDescription(), 
					curr + " " + desimalFormat.format(total), new Integer(1)});
			model.addRow(new Object[]{"", "TOTAL",curr + " " + desimalFormat.format(total), new Integer(2)});
			
			String moneyTalk = MoneyTalk.say(total,entity.getCurrency(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{moneyTalk , "","", new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			jprint = jasperPrint;
			param = parameters;
			
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Voucher Loan Payment");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public JasperPrint getJasperPrint(){
		return jprint;
	}
	
	public Map getParameters(){
		return param;
	}
	
	public JasperReport getJasperReport(){
		return jreport;
	}
}