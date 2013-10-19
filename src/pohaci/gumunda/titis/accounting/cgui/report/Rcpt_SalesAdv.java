package pohaci.gumunda.titis.accounting.cgui.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.entity.SalesAdvance;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class Rcpt_SalesAdv {
	
	public Rcpt_SalesAdv(SalesAdvance entity) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			String filename = "Rcpt_SalesAdv";      
            String compiledRptFilename = ReportUtils.compileReport(filename);
            if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();      
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			if (entity.getTransactionDate()!=null)
				parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));				
			else
				parameters.put("param_date", "");
			
			String cashBank = "";
			if (entity.getCashAccount()!=null)
				cashBank = entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName();
			else if (entity.getBankAccount()!=null)
				cashBank = entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName();
			
			parameters.put("param_cash_bank", cashBank);
			
			ProjectData p = entity.getProject();
			if (p!=null){
				parameters.put("param_project_code", p.toString());
				parameters.put("param_receive_from", p.getCustname());
				parameters.put("param_ipc_no", p.getIPCNo());
				parameters.put("param_unit_code", p.getUnit().getDescription());
				parameters.put("param_activity_code", p.getActivity().toString());
				parameters.put("param_department_code", p.getDepartment().getName());
				parameters.put("param_work_description", p.getWorkDescription());
			}

			parameters.put("param_transaction_type", "Sales Advance Receive");			
			
			if (entity.getEmpOriginator()!=null){
				String namaOriginator = entity.getEmpOriginator().getFirstName() + " " + entity.getEmpOriginator().getMidleName() + 
				" " + entity.getEmpOriginator().getLastName();
				parameters.put("param_nama_originator", namaOriginator);
			}else
				parameters.put("param_nama_originator", "");
			
			parameters.put("param_jabatan_originator", entity.getJobTitleOriginator());
			
			if (entity.getDateOriginator()!=null)
				parameters.put("param_date_originator", dateFormat.format(entity.getDateOriginator()));			
			else
				parameters.put("param_date_originator", "");
			
			if (entity.getEmpApproved()!=null){
				String namaApprover = entity.getEmpApproved().getFirstName() + " " + entity.getEmpApproved().getMidleName() + 
				" " + entity.getEmpApproved().getLastName();
				parameters.put("param_nama_approver", namaApprover);
			}else
				parameters.put("param_nama_approver", "");			
			parameters.put("param_jabatan_approver", entity.getJobTitleOriginator());			
			if (entity.getDateApproved()!=null)
				parameters.put("param_date_approver", dateFormat.format(entity.getDateApproved()));				
			else
				parameters.put("param_date_approver", "");
			
			if (entity.getEmpReceived()!=null){
				String namaReceiver = entity.getEmpReceived().getFirstName() + " " + entity.getEmpReceived().getMidleName() + 
				" " + entity.getEmpReceived().getLastName();
				parameters.put("param_nama_receiver",namaReceiver);
			}else
				parameters.put("param_nama_receiver","");
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
			int i = 1;
			String curr = "";      
			/*model.addRow(new Object[]{"", "We have received your payment payable for :", "", new Integer(1)});*/
			
			if (entity.getSalesAdvCurr()!=null)
				curr = entity.getSalesAdvCurr().getSymbol();
			/*model.addRow(new Object[]{"", entity.getDescription(), "", new Integer(1)});*/
			model.addRow(new Object[]{"", "", "", new Integer(1)});
			model.addRow(new Object[]{String.valueOf(i++), "Sales Advance",  curr + " " + desimalFormat.format(entity.getSalesAdvAmount()), new Integer(1)});
			if (entity.getCustomerStatus().equalsIgnoreCase("NONWAPU")){
				model.addRow(new Object[]{String.valueOf(i++), "VAT " + entity.getVatPercent() + "%", desimalFormat.format(entity.getVatAmount()), new Integer(1)});
				total +=entity.getVatAmount() ;
			}
			model.addRow(new Object[]{String.valueOf(i++), "Tax Art " + entity.getTax23Percent() + "%"," (" + desimalFormat.format(entity.getTax23Amount()) + ") ", new Integer(1)});
			total += entity.getSalesAdvAmount() - entity.getTax23Amount();
			
	
			String moneyTalk = MoneyTalk.say(total,entity.getCurrency(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			
			model.addRow(new Object[]{"", "", "", new Integer(2)});
			model.addRow(new Object[]{"", "TOTAL", curr + " " + desimalFormat.format(total), new Integer(3)});
			model.addRow(new Object[]{"Amount in word : " + moneyTalk, "", "", new Integer(4)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Sales Advance Receipt");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}	
	
}


