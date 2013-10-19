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
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
public class Vchr_APPayment {

	public Vchr_APPayment(PurchaseApPmt entity,Connection conn) {
		try {
			String filename = "Vchr_APPayment";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");

			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));

			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " +
						entity.getBankAccount().getName());
			else if (entity.getCashAccount()!=null)
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " +
						entity.getCashAccount().getName());

			parameters.put("param_cheque_number", entity.getChequeNo());
			if (entity.getChequeDueDate()!=null)
				parameters.put("param_due_date", dateFormat.format(entity.getChequeDueDate()));
			else
				parameters.put("param_due_date", "");


			parameters.put("param_pay_to", "");
			if(entity.getBeginningBalance()!= null) {
				if(entity.getBeginningBalance().getPartner()!= null)
					parameters.put("param_pay_to", String.valueOf(entity.getBeginningBalance().getPartner()));
			} else {
				if(entity.getPurchaseReceipt().getSupplier() != null)
					parameters.put("param_pay_to", String.valueOf(entity.getPurchaseReceipt().getSupplier()));
			}

			parameters.put("param_invoice_no", "");
			if(entity.getBeginningBalance()!= null) {
				if(entity.getBeginningBalance().getTrans().getReference()!= null)
					parameters.put("param_invoice_no", entity.getBeginningBalance().getTrans().getReference());
			} else {
				if(entity.getPurchaseReceipt().getInvoice()!= null)
					parameters.put("param_invoice_no", entity.getPurchaseReceipt().getInvoice());
			}

			parameters.put("param_invoice_date", "");
			if(entity.getBeginningBalance()!= null) {
				if (entity.getBeginningBalance().getTrans().getTransDate()!=null)
					parameters.put("param_invoice_date", dateFormat.format(entity.getBeginningBalance().getTrans().getTransDate()));
			} else {
				if (entity.getPurchaseReceipt().getInvoiceDate()!=null)
					parameters.put("param_invoice_date", dateFormat.format(entity.getPurchaseReceipt().getInvoiceDate()));
			}

			parameters.put("param_purchase_order_no", "");
			if(entity.getBeginningBalance()!= null) {
				if(entity.getBeginningBalance().getTrans().getReference()!= null)
					parameters.put("param_invoice_no", entity.getBeginningBalance().getTrans().getReference());
			} else {
				if(entity.getPurchaseReceipt().getReferenceNo()!= null)
					parameters.put("param_purchase_order_no", entity.getPurchaseReceipt().getReferenceNo());
			}

			parameters.put("param_project_code", "");
			if(entity.getBeginningBalance() != null) {
				if(entity.getBeginningBalance().getProject().getCode()!= null)
					parameters.put("param_project_code", entity.getBeginningBalance().getProject().getCode());
			} else {
				if(entity.getPurchaseReceipt().getProject().getCode()!= null)
					parameters.put("param_project_code", entity.getPurchaseReceipt().getProject().getCode());
			}

			parameters.put("param_unit_code", "");
			if(entity.getBeginningBalance() != null) {
				if(entity.getBeginningBalance().getProject().getUnit() != null)
					parameters.put("param_unit_code", String.valueOf(entity.getBeginningBalance().getProject().getUnit()));
			} else {
				if (entity.getPurchaseReceipt().getUnit()!=null)
					parameters.put("param_unit_code", String.valueOf(entity.getPurchaseReceipt().getUnit()));
			}

			parameters.put("param_activity_code", "");
			if(entity.getBeginningBalance() != null) {
				if(entity.getBeginningBalance().getProject().getActivity() != null )
					parameters.put("param_activity_code",String.valueOf(entity.getBeginningBalance().getProject().getActivity()));
			} else {
				if (entity.getPurchaseReceipt().getProject().getActivity()!=null)
					parameters.put("param_activity_code", String.valueOf(entity.getPurchaseReceipt().getProject().getActivity()));
			}

			parameters.put("param_department_code", "");
			if(entity.getBeginningBalance() != null) {
				if(entity.getBeginningBalance().getProject().getDepartment()!= null)
					parameters.put("param_department_code", String.valueOf(entity.getBeginningBalance().getProject().getDepartment()));
			} else {
				if (entity.getPurchaseReceipt().getProject().getDepartment()!= null)
					parameters.put("param_department_code", String.valueOf(entity.getPurchaseReceipt().getProject().getDepartment()));
			}

			parameters.put("param_client", "");
			if(entity.getBeginningBalance() != null) {
				if(entity.getBeginningBalance().getProject().getCustomer() != null)
					parameters.put("param_client", String.valueOf(entity.getBeginningBalance().getProject().getCustomer()));
			} else {
				if (entity.getPurchaseReceipt().getProject().getCustomer()!=null)
					parameters.put("param_client", String.valueOf(entity.getPurchaseReceipt().getProject().getCustomer()));
			}

			parameters.put("param_work_description","");
			parameters.put("param_ipc_no","");
			parameters.put("param_contract_no", "");
			if(entity.getBeginningBalance() != null){
				if(entity.getBeginningBalance().getProject().getWorkDescription()!= null)
					parameters.put("param_work_description",String.valueOf(entity.getBeginningBalance().getProject().getWorkDescription()));
				if(entity.getBeginningBalance().getProject().getWorkDescription()!= null)
					parameters.put("param_ipc_no", entity.getBeginningBalance().getProject().getIPCNo());
				if(entity.getBeginningBalance().getProject().getWorkDescription()!= null)
					parameters.put("param_contract_no", entity.getBeginningBalance().getProject().getPONo());
			}
			else{
				if(entity.getPurchaseReceipt().getProject().getWorkDescription()!= null)
					parameters.put("param_work_description",String.valueOf(entity.getPurchaseReceipt().getProject().getWorkDescription()));
				if(entity.getPurchaseReceipt().getProject().getWorkDescription()!= null)
					parameters.put("param_ipc_no", entity.getPurchaseReceipt().getProject().getIPCNo());
				if(entity.getPurchaseReceipt().getProject().getWorkDescription()!= null)
					parameters.put("param_contract_no", entity.getPurchaseReceipt().getProject().getPONo());
			}

			parameters.put("param_transaction_type", "Account Payable Payment");

			if (entity.getEmpReceived()!=null){
				String empReceived = entity.getEmpReceived().getFirstName() + " " +
				entity.getEmpReceived().getMidleName() + " " + entity.getEmpReceived().getLastName();
				parameters.put("param_nama_receiver", empReceived);
			}else
				parameters.put("param_nama_receiver","");
			parameters.put("param_jabatan_receiver", entity.getJobTitleReceived());

			if (entity.getDateReceived()!=null)
				parameters.put("param_date_receiver", dateFormat.format(entity.getDateReceived()));
			else
				parameters.put("param_date_receiver", "");

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

			if (entity.getEmpApproved()!=null){
				String empApproved = entity.getEmpApproved().getFirstName() + " " +
				entity.getEmpApproved().getMidleName() + " " + entity.getEmpApproved().getLastName();
				parameters.put("param_nama_approver", empApproved);
			}else
				parameters.put("param_nama_approver", "");
			parameters.put("param_jabatan_approver", entity.getJobTitleApproved());
			if(entity.getDateApproved()!=null)
				parameters.put("param_date_approver", dateFormat.format(entity.getDateApproved()));
			else
				parameters.put("param_date_approver", "");

			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("field4");
			model.addColumn("status");

			// ini adalah yang sudah direvisi mba
			String curr = "";
			double total = 0;
			String invoiceNo = "";
			String invoiceDate = "";
			if (entity.getPurchaseReceipt()!=null){
				invoiceNo = entity.getPurchaseReceipt().getInvoice();
				invoiceDate = dateFormat.format(entity.getPurchaseReceipt().getInvoiceDate());
			}
			if (entity.getAppMtCurr()!=null)
				curr = entity.getAppMtCurr().getSymbol();
			model.addRow(new Object[]{"",entity.getDescription() ,"","", new Integer(1)});
			model.addRow(new Object[]{"1","Invoice No   : " + invoiceNo,"","", new Integer(1)});
			model.addRow(new Object[]{"","Date            : " + invoiceDate,"","", new Integer(1)});
			model.addRow(new Object[]{"","Amount       : " + curr  + " " + desimalFormat.format(entity.getAppMtAmount()),
					curr,desimalFormat.format(entity.getAppMtAmount()), new Integer(1)});
			if (entity.getTax23Amount()>0)
				model.addRow(new Object[]{"2","PPH 23 	      : " + desimalFormat.format(entity.getTax23Percent()) + " %",curr,desimalFormat.format(entity.getTax23Amount()), new Integer(1)});

			total = entity.getAppMtAmount() - entity.getTax23Amount();
			model.addRow(new Object[]{"", "TOTAL",curr, desimalFormat.format(total),new Integer(2)});

				String moneyTalk = MoneyTalk.say((total),entity.getAppMtCurr(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{moneyTalk, "", "","", new Integer(3)});

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Account Payable Payment Voucher");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}