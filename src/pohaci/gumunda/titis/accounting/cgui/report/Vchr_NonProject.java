package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.GeneralJasperVoucherNonProject;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCost;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCostDetail;
import pohaci.gumunda.titis.accounting.entity.PmtOthers;
import pohaci.gumunda.titis.accounting.entity.PmtOthersDetail;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class Vchr_NonProject {
	Connection m_conn;
	long m_sessionid = -1;
	JasperReport jreport = null;
	JasperPrint jprint = null;
	Map param = null;
	
	public Vchr_NonProject(GeneralJasperVoucherNonProject entity,Connection conn,long sessionid) {
	m_conn = conn;
	m_sessionid = sessionid;
		try {			
			String filename = "Vchr_NonProject";
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
			String title ="";
			Currency currency =null;
			if (entity instanceof PmtOthers) {	
				title = "Others Payment Voucher";
				PmtOthers pmtOther = (PmtOthers) entity;	
				currency = pmtOther.getCurrency();
				if (pmtOther.getDepartment()!=null)
					parameters.put("param_department_code", pmtOther.getDepartment().toString());
				else
					parameters.put("param_department_code", "");
				parameters.put("param_payto", pmtOther.getPayTo());
				parameters.put("param_transaction_type", "Others Payment");
				
				GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOthersDetail.class);
				mapper2.setActiveConn(conn);
				List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_PMT_OTHERS+"="+entity.getIndex());
				PmtOthersDetail detail;
				int length = detailList.size();
				int j=0;
				for(int i=0;i<length;i++){
					detail=(PmtOthersDetail)detailList.get(i);				
					long indexAcc = detail.getAccount().getIndex();
					if (indexAcc!=218 && indexAcc!=211 && indexAcc!=12 && indexAcc!=4 && indexAcc!=22){
						total += detail.getAccValue();			
						if (detail.getCurrency()!=null)
							currency = detail.getCurrency();
						model.addRow(new Object[]{String.valueOf(++j),detail.getDescription(),
								currency.getSymbol()+" "+desimalFormat.format(detail.getAccValue()),new Integer(1)});						
					}					
				}											
			}			
			else if (entity instanceof PmtOperationalCost) {
				title = "Operational Cost Payment Voucher";
				PmtOperationalCost pmtOperCost = (PmtOperationalCost) entity;
				currency = pmtOperCost.getCurrency();
				if (pmtOperCost.getDepartment()!=null)
					parameters.put("param_department_code", pmtOperCost.getDepartment().toString());
				else
					parameters.put("param_department_code", "");				
				parameters.put("param_payto", pmtOperCost.getPayTo());
				parameters.put("param_transaction_type", IConstants.PAYMENT_OPERASIONAL_COST);
				
				GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
				mapper2.setActiveConn(conn);
				List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_PMT_OPERASIONAL_COST+"="+entity.getIndex());
				PmtOperationalCostDetail detail;
				int length = detailList.size();
				if (length>0){
					int no = 0;
					JournalStandardAccount[] jsAccounts = getJournalStandardAccount(pmtOperCost);
					for(int i=0;i<detailList.size();i++){					
						detail=(PmtOperationalCostDetail)detailList.get(i);						
						for (int j=0;j<jsAccounts.length;j++)
							if (detail.getAccount().getIndex() == jsAccounts[j].getAccount().getIndex())
								if (!jsAccounts[j].isCalculate() && !jsAccounts[j].isHidden()){	
									JournalStandardAccount jsAccount = jsAccounts[j];	
									double amount= detail.getAccValue();
									if (jsAccount.getBalance()==1)
										amount = -detail.getAccValue();
									total += amount;
									model.addRow(new Object[]{String.valueOf(++no),"" + detail.getDescription(),
											currency.getSymbol()+" "+desimalFormat.format(amount), new Integer(1)});								
								}
					}
				}else{
					total = 0;
					model.addRow(new Object[]{"","","",new Integer(1)});
				}				
			}   
			
			model.addRow(new Object[]{"", "TOTAL",currency.getSymbol()+ " " + desimalFormat.format(total), new Integer(2)});
			
			String moneyTalk = MoneyTalk.say(total,currency,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			
			model.addRow(new Object[]{moneyTalk, "", "",new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			jprint = jasperPrint;
			param = parameters;
			
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle(title);
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public JournalStandardAccount[] getJournalStandardAccount(PmtOperationalCost entity){			
		String attr = "";		
		if (entity.getCashAccount()!=null) {					
			attr = IConstants.ATTR_PMT_CASH;
		}else if (entity.getBankAccount()!=null) {	
			attr = IConstants.ATTR_PMT_BANK;
		}else if (entity.getPaymentSource().equalsIgnoreCase("BANK")){
			attr = IConstants.ATTR_PMT_BANK;
		}else if (entity.getPaymentSource().equalsIgnoreCase("CASH")){
			attr = IConstants.ATTR_PMT_CASH;
		}
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(m_conn,m_sessionid, 
					IDBConstants.MODUL_ACCOUNTING);
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_OPERASIONAL_COST,
					attr);		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		JournalStandardAccount[] jsAcc = journal.getJournalStandardAccount();
		return jsAcc;			
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
