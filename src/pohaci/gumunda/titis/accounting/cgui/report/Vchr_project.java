package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCost;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCostDetail;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.ProjectData;
public class Vchr_project {
	Connection m_conn=null;
	long m_sessionid=-1;
/*	public Vchr_project(PurchaseApPmt entity,Connection conn) {	
		
		try {      
			String filename = "Vchr_project";      
            String compiledRptFilename = ReportUtils.compileReport(filename);
            if (compiledRptFilename == "") return;
            Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));			
									
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
									
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + 
						entity.getBankAccount().getName());
			else
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + 
						entity.getCashAccount().getName());				
			
			parameters.put("param_cheque_number", entity.getChequeNo());
			if (entity.getChequeDueDate()!=null)
				parameters.put("param_due_date", dateFormat.format(entity.getChequeDueDate()));
			else
				parameters.put("param_due_date", "");
			parameters.put("param_pay_to", String.valueOf(entity.getPurchaseReceipt().getSupplier()));
			
			parameters.put("param_invoice_no", entity.getPurchaseReceipt().getInvoice());
			if (entity.getPurchaseReceipt().getInvoiceDate()!=null)
				parameters.put("param_invoice_date", dateFormat.format(entity.getPurchaseReceipt().getInvoiceDate()));
			else
				parameters.put("param_invoice_date", "");    	  
			parameters.put("param_purchase_order_no", entity.getPurchaseReceipt().getReferenceNo());
			
			if (entity.getPurchaseReceipt().getUnit()!=null)
				parameters.put("param_unit_code", String.valueOf(entity.getPurchaseReceipt().getUnit()));
			else 
				parameters.put("param_unit_code", "");
			
			if (entity.getPurchaseReceipt().getProject().getActivity()!=null)
				parameters.put("param_activity_code", String.valueOf(entity.getPurchaseReceipt().getProject().getActivity()));
			else
				parameters.put("param_activity_code", "");
			
			if (entity.getPurchaseReceipt().getProject().getDepartment()!=null)
				parameters.put("param_department_code", 
						String.valueOf(entity.getPurchaseReceipt().getProject().getDepartment()));
			else
				parameters.put("param_department_code", "");
			
			if (entity.getPurchaseReceipt().getProject().getCustomer()!=null)
				parameters.put("param_client", 
						String.valueOf(entity.getPurchaseReceipt().getProject().getCustomer()));
			else
				parameters.put("param_client", "");
			
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
			
			parameters.put("param_work_description",String.valueOf(entity.getPurchaseReceipt().getProject().getWorkDescription()));				
			parameters.put("param_ipc_no", entity.getPurchaseReceipt().getProject().getIPCNo());
			parameters.put("param_contract_no", entity.getPurchaseReceipt().getProject().getPONo());				
			
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
			curr = entity.getAppMtCurr().getSymbol();
			model.addRow(new Object[]{"",entity.getDescription() ,"","", new Integer(1)});
			model.addRow(new Object[]{"1","Invoice No   : " + invoiceNo,"","", new Integer(1)});
			model.addRow(new Object[]{"","Date            : " + invoiceDate,"","", new Integer(1)});
			model.addRow(new Object[]{"","Amount       : " + curr  + " " + desimalFormat.format(entity.getAppMtAmount()),
					curr,desimalFormat.format(entity.getAppMtAmount()), new Integer(1)});
			model.addRow(new Object[]{"2","PPH 23",curr,desimalFormat.format(entity.getTax23Amount()), new Integer(1)});
			
			total = entity.getAppMtAmount() - entity.getTax23Amount();
			model.addRow(new Object[]{"", "TOTAL",curr, desimalFormat.format(total),new Integer(2)});
			
			String currency = "";
			String cents = "";
			int language = 0;      
			if (curr.equals("Rp")){
				currency = "Rupiah";
				cents = "Sen";
				language = MoneyTalk.INDONESIAN;    	  
			}else {
				currency = "Dollar";
				cents = "Cent";
				language = MoneyTalk.ENGLISH;
			}
			String moneyTalk = MoneyTalk.say((total),currency,cents,language,
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
	}*/
	
	
	public Vchr_project(PmtProjectCost entity,Connection conn,long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		try {      
			String filename = "Vchr_project.jrxml";      
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");  
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));	
			
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
			
			ProjectData a = entity.getProject();			
			
			parameters.put("param_project_code", a.toString());
			
			if (a.getUnit()!=null)	
				parameters.put("param_unit_code", String.valueOf(a.getUnit()));
			else 
				parameters.put("param_unit_code", "");
			
			if (a.getActivity()!=null)
				parameters.put("param_activity_code", String.valueOf(a.getActivity()));
			else
				parameters.put("param_activity_code", "");
			
			if (a.getDepartment()!=null)
				parameters.put("param_department_code", 
						String.valueOf(a.getDepartment()));
			else
				parameters.put("param_department_code", "");
			
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("status");
			parameters.put("param_work_description",String.valueOf(a.getWorkDescription()));				
			parameters.put("param_ipc_no", a.getIPCNo());
			parameters.put("param_contract_no", a.getPONo());
			String curr = "";
			double total = 0;	
			
			parameters.put("param_transaction_type", "Project Cost Payment");
			parameters.put("param_pay_to", entity.getPayTo());
				parameters.put("param_cheque_number", entity.getChequeno());
			if (entity.getChequeDueDate()!=null)
				parameters.put("param_due_date", dateFormat.format(entity.getChequeDueDate()));
			else
				parameters.put("param_due_date","");
			if (entity.getCurrency()!=null)
				curr = entity.getCurrency().getSymbol();
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + 
						entity.getBankAccount().getName());
			else if (entity.getCashAccount()!=null)
				parameters.put("param_cash_bank", entity.getCashAccount().getCode()+ " " + 
						entity.getCashAccount().getName());
			
			Customer cust =
				entity.getProject().getCustomer();
			
			if (cust!=null) // search dari project click
				parameters.put("param_client", cust.toString());
			else 
				parameters.put("param_client", "");
			
			GenericMapper mapper2=MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
			mapper2.setActiveConn(conn);
			String strWhere = IDBConstants.ATTR_PMT_PROJECT_COST+"="+entity.getIndex();
			List rs=mapper2.doSelectWhere(strWhere);
			PmtProjectCostDetail detail=null;			
			
				String attr = "";		
				if (entity.getCashAccount()!=null) 					
					attr = IConstants.ATTR_PMT_CASH;
				else if (entity.getBankAccount()!=null) 
					attr = IConstants.ATTR_PMT_BANK;
				else {
					if (entity.getPaymentSource().equalsIgnoreCase("CASH"))
						attr = IConstants.ATTR_PMT_CASH;
					else
						attr = IConstants.ATTR_PMT_BANK;
				}
					
				JournalStandardAccount[] jsAccounts = getJournalStandardAccount(attr);
			
				int no = 0;
				for(int i=0;i<rs.size();i++){
					detail=(PmtProjectCostDetail)rs.get(i);
					for (int j=0;j<jsAccounts.length;j++)
						if (detail.getAccount().getIndex() == jsAccounts[j].getAccount().getIndex())
							if (!jsAccounts[j].isCalculate() && !jsAccounts[j].isHidden()){	
								JournalStandardAccount jsAccount = jsAccounts[j];							
								double amount = detail.getaccValue();
								if (jsAccount.getBalance()==1)
									amount= -detail.getaccValue();
								total +=amount;
								model.addRow(new Object[]{String.valueOf(++no),"" + detail.getDescription(),
										curr+" "+desimalFormat.format(amount), new Integer(1)});								
							}
				}
			
			model.addRow(new Object[]{"", "TOTAL",curr+" "+desimalFormat.format(total), new Integer(2)});
			/*String currency = "";
			String cents = "";
			int language = 0;      
			if (curr.equals("Rp")){
				currency = "Rupiah";
				cents = "Sen";
				language = MoneyTalk.INDONESIAN;    	  
			}else {
				currency = "Dollar";
				cents = "Cent";
				language = MoneyTalk.ENGLISH;
			}*/
			String moneyTalk = MoneyTalk.say((total),entity.getCurrency(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{moneyTalk, "","", new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
			
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Project Cost Payment Voucher");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public JournalStandardAccount[] getJournalStandardAccount(String attr){			
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(m_conn,m_sessionid, 
					IDBConstants.MODUL_ACCOUNTING);
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_PROJECT_COST,
					attr);		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		JournalStandardAccount[] jsAcc = journal.getJournalStandardAccount();
		return jsAcc;			
	}
	
}