package pohaci.gumunda.titis.accounting.cgui.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.entity.PmtLoan;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;

public class Vchr_loan {
	public Vchr_loan(PmtLoan entity,Object objPaymentCurrComp){
		try {
			
			String filename = "Vchr_Loan";
            String compiledRptFilename = ReportUtils.compileReport(filename);
            if (compiledRptFilename == "") return;
            Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");			
			
			parameters.put("param_logo", "../images/TS.gif");			
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName());
			else if (entity.getCashAccount()!=null)
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName());
			
			parameters.put("param_no", entity.getReferenceNo());      
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));      
			parameters.put("param_cheque_number", entity.getChequeNo());
			
			/*if (entity.getPayTo()!=null)				
				parameters.put("param_pay_to",entity.getPayTo().getCode() + " " + entity.getPayTo().getName());*/
			
			parameters.put("param_transaction_type",IConstants.PAYMENT_LOAN);
			
			if (entity.getLoanReceipt()!=null){
				if (entity.getLoanReceipt().getCompanyLoan()!=null){
					parameters.put("param_loan",entity.getLoanReceipt().toString());					
					if (entity.getLoanReceipt().getCompanyLoan().getCreditorList()!=null){
						parameters.put("param_pay_to",entity.getLoanReceipt().getCompanyLoan().getCreditorList().getCode() + " " +
								entity.getLoanReceipt().getCompanyLoan().getCreditorList().getName());
						parameters.put("param_creditor_type",entity.getLoanReceipt().getCompanyLoan().getCreditorList().getCreditorType());
						parameters.put("param_creditor_address",entity.getLoanReceipt().getCompanyLoan().getCreditorList().getAddress());
					}
				}
			}
			else if (entity.getBeginningBalance()!=null){
				parameters.put("param_pay_to", entity.getBeginningBalance().getCompanyLoan().getCreditorList().getCode() + " " + entity.getBeginningBalance().getCompanyLoan().getCreditorList().getName());
				parameters.put("param_loan",entity.getBeginningBalance().toString());
				parameters.put("param_creditor_type",entity.getBeginningBalance().getCompanyLoan().getCreditorList().getCreditorType());
				parameters.put("param_creditor_address",entity.getBeginningBalance().getCompanyLoan().getCreditorList().getAddress());
			}
			if (entity.getChequeDueDate()!=null)
				parameters.put("param_due_date", dateFormat.format(entity.getChequeDueDate()));	
			else
				parameters.put("param_due_date", "");
			
			if (entity.getUnit()!=null)
				parameters.put("param_unit_code", entity.getUnit().toString());
			else
				parameters.put("param_unit_code", "");
			
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
			
			
			double total = 0;
			
			total = entity.getAmount();
			Currency currency = null;
			String curr = "";
			if (objPaymentCurrComp instanceof Currency) {
				currency = (Currency) objPaymentCurrComp;;
				curr = currency.getSymbol();
				
			}
			//getCurr = entity.getCurrency().toString();
			model.addRow(new Object[]{"1",entity.getDescription(),
					curr+" "+desimalFormat.format(total),new Integer(1)});				
			
			model.addRow(new Object[]{"", "TOTAL",curr+" "+desimalFormat.format(total), new Integer(2)});
			
			
			String moneyTalk = MoneyTalk.say(total,currency,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			
			model.addRow(new Object[]{moneyTalk, "","", new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);			
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Loan Payment Voucher");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
