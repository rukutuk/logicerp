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
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.GeneralJasperVchr_Salary;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtEmpInsDet;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtEmpInsurance;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryHo;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryHoDet;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryUnit;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryUnitDet;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21Ho;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21HoDet;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21Unit;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21UnitDet;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;


public class Vchr_Salary {
	public Vchr_Salary(GeneralJasperVchr_Salary entity,Connection conn, long sessionId){
		try {			
			String filename = "Vchr_Salary";
            String compiledRptFilename = ReportUtils.compileReport(filename);
            if (compiledRptFilename == "") return;
            Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,###.00");
			
			parameters.put("param_logo", "images/TS.gif");			
			parameters.put("param_no", entity.getReferenceNo());
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName());
			else if (entity.getCashAccount()!=null)
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName());
			
			parameters.put("param_payto", entity.getPayto());			
			parameters.put("param_cheque_number", entity.getChequeNo());
			
			if (entity.getChequeDueDate()!=null)
				parameters.put("param_due_date", dateFormat.format(entity.getChequeDueDate()));	
			else
				parameters.put("param_due_date", "");
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_unit_code", entity.getBankAccount().getUnit().toString());
			else if (entity.getCashAccount()!=null)
				parameters.put("param_unit_code", entity.getCashAccount().getUnit().toString());						
			
			if (entity.getUnit()!=null)
			parameters.put("param_salary_unit_code", entity.getUnit().toString());
			
			
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
			model.addColumn("field4");
			model.addColumn("status");
			
			
			double total = 0;
			String title ="";
			String curr = "";
			Currency currency = null;
			if (entity instanceof PayrollPmtSlryHo) {
				parameters.put("param_transaction_type", IConstants.PAYROLL_PAYMENT_SALARY_HO);
				title = "Salary Payment-Head Office Voucher";
				GenericMapper mapper=MasterMap.obtainMapperFor(PayrollPmtSlryHoDet.class);
				mapper.setActiveConn(conn);
				List detailList=mapper.doSelectWhere(IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO +"="+entity.getIndex());
				PayrollPmtSlryHoDet detail;
				int length = detailList.size();
				if (length>0){
					for(int i=0;i<length;i++){
						detail=(PayrollPmtSlryHoDet)detailList.get(i);
						total += detail.getAccValue();
						currency = detail.getCurrency();
						model.addRow(new Object[]{String.valueOf(i+1),detail.getDescription(),
								currency.toString(),desimalFormat.format(detail.getAccValue()), new Integer(1)});
					}				
				}else
					model.addRow(new Object[]{"","","","", new Integer(1)});					
			}
			
			else if (entity instanceof PayrollPmtSlryUnit) {
				parameters.put("param_transaction_type", IConstants.PAYROLL_PAYMENT_SALARY_UNIT);				
				String attribute = "";
				if (entity.getBankAccount()!=null)
					attribute = IConstants.ATTR_PMT_BANK;
				else
					attribute = IConstants.ATTR_PMT_CASH;
				
				JournalStandardSettingPickerHelper helper =
					new JournalStandardSettingPickerHelper(conn, sessionId, 
							IDBConstants.MODUL_ACCOUNTING);
				
				List journalStdList = 
					helper.getJournalStandardSettingWithAccount(
							IConstants.PAYROLL_PAYMENT_SALARY_UNIT, attribute);
				
				JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
				
				title = "Salary Payment-Unit Voucher";			
				GenericMapper mapper2=MasterMap.obtainMapperFor(PayrollPmtSlryUnitDet.class);
				mapper2.setActiveConn(conn);
				List detailList=mapper2.doSelectWhere(IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT +"="+entity.getIndex());
				PayrollPmtSlryUnitDet detail;
				
				model.addRow(new Object[]{"",entity.getDescription(),"","", new Integer(1)});		
				
				int length = detailList.size();
				int no = 0;
				if (length>0){
					for(int i=0;i<length;i++){
						detail=(PayrollPmtSlryUnitDet)detailList.get(i);
						JournalStandardAccount jsa = getJournalStandardAccount(detail, setting);
						if (!isHidden(detail, jsa)) {
							double value = detail.getAccValue();
							if (jsa.getBalance()!=0)
								value = -detail.getAccValue();;
								
							total += value;
							currency = detail.getCurrency();
							model.addRow(new Object[] { String.valueOf(++no),
									detail.getAccount().getName(), currency.toString(),
									desimalFormat.format(value),
									new Integer(1) });
						}
					}	
				}else
					model.addRow(new Object[]{"","","","", new Integer(1)});				
			}
			
			if (entity instanceof PayrollPmtTax21Unit) {
				
				parameters.put("param_transaction_type", IConstants.PAYROLL_PAYMENT_TAX21_UNIT);
				title = "Tax At 21 Payment-Head Office Voucher";			
				
				String attribute = "";
				if (entity.getBankAccount()!=null)
					attribute = IConstants.ATTR_PMT_BANK;
				else
					attribute = IConstants.ATTR_PMT_CASH;
				
				JournalStandardSettingPickerHelper helper =
					new JournalStandardSettingPickerHelper(conn, sessionId, 
							IDBConstants.MODUL_ACCOUNTING);
				
				List journalStdList = 
					helper.getJournalStandardSettingWithAccount(
							IConstants.PAYROLL_PAYMENT_TAX21_UNIT, attribute);
				
				JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
				
				GenericMapper mapper=MasterMap.obtainMapperFor(PayrollPmtTax21UnitDet.class);
				mapper.setActiveConn(conn);
				List detailList=mapper.doSelectWhere(IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT+"="+entity.getIndex());
				PayrollPmtTax21UnitDet detail;
				int length = detailList.size();
				
				if (length>0){
					int no = 0;
					for(int i=0;i<length;i++){
						detail=(PayrollPmtTax21UnitDet)detailList.get(i);
						JournalStandardAccount jsa = getJournalStandardAccount(detail, setting);
						if (!isHidden(detail, jsa)) {			
							double value = detail.getAccValue();
							if (jsa.getBalance()!=0)
								value = -detail.getAccValue();;
								
							total += value;
							
							if (detail.getCurrency()!=null){
								currency = detail.getCurrency();
								curr = currency.getSymbol();
							}else 
								curr = "";
							model.addRow(new Object[]{String.valueOf(++no),detail.getAccount().getName(),
									curr,desimalFormat.format(detail.getAccValue()), new Integer(1)});
						}
					}	
				}else
					model.addRow(new Object[]{"","",
							"","", new Integer(1)});	
			}
			
			if (entity instanceof PayrollPmtEmpInsurance) {
				parameters.put("param_transaction_type", IConstants.PAYROLL_PAYMENT_EMPLOYEE_INSURANCE);
				title = "Employee Insurance Payment Voucher";			
				GenericMapper mapper2=MasterMap.obtainMapperFor(PayrollPmtEmpInsDet.class);
				mapper2.setActiveConn(conn);
				List detailList=mapper2.doSelectWhere("payrollpmtempins"+"="+entity.getIndex());
				PayrollPmtEmpInsDet detail;
				int length = detailList.size();
				if (length>0){
					for(int i=0;i<length;i++){
						detail=(PayrollPmtEmpInsDet)detailList.get(i);
						total += detail.getAccValue();
						if (detail.getCurrency()!=null){
							currency = detail.getCurrency();
							curr = currency.toString();
						}else 
							curr = "";
						model.addRow(new Object[]{String.valueOf(i+1),detail.getDescription(),
								curr,desimalFormat.format(detail.getAccValue()), new Integer(1)});
					}	
				}else
					model.addRow(new Object[]{"","",
							"","", new Integer(1)});					
			}
			
			else if (entity instanceof PayrollPmtTax21Ho) {
				parameters.put("param_transaction_type", IConstants.PAYROLL_PAYMENT_TAX21_HO);
				title = "Tax At 21 Payment-Head Office Voucher";
				GenericMapper mapper2=MasterMap.obtainMapperFor(PayrollPmtTax21HoDet.class);
				mapper2.setActiveConn(conn);
				List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_PAYROLL_PMT_TAX21_HO+"="+entity.getIndex());
				int length = detailList.size();
				if (length>0){
					for(int i=0;i<length;i++){
						PayrollPmtTax21HoDet detail=(PayrollPmtTax21HoDet)detailList.get(i);
						total += detail.getAccValue();
						if (detail.getCurrency()!=null){
							currency = detail.getCurrency();
							curr = currency.toString();
						}else 
							curr = "";
						model.addRow(new Object[]{String.valueOf(i+1),detail.getDescription(),
								curr,desimalFormat.format(detail.getAccValue()), new Integer(1)});
					}	
				}else
					model.addRow(new Object[]{"","",
							"","", new Integer(1)});
				
			}
			
			model.addRow(new Object[]{"", "TOTAL",curr,desimalFormat.format(total), new Integer(2)});
		
			String moneyTalk = MoneyTalk.say(total,currency,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			
			model.addRow(new Object[]{moneyTalk, "", "","", new Integer(3)});
			
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
	

	private boolean isHidden(Object detail, JournalStandardAccount settingAccount) {
		Account acc = null;
		if (detail instanceof PayrollPmtSlryUnitDet) {
			PayrollPmtSlryUnitDet det = (PayrollPmtSlryUnitDet) detail;
			acc = det.getAccount();
			
		}
		else if (detail instanceof PayrollPmtTax21UnitDet) {
			PayrollPmtTax21UnitDet det = (PayrollPmtTax21UnitDet) detail;
			acc = det.getAccount();
		}
		if (settingAccount.getAccount().getIndex()==acc.getIndex()){
			if (settingAccount.isHidden())
				return true;
		}
		return false;	
	}
	
	private JournalStandardAccount getJournalStandardAccount(Object detail, JournalStandardSetting setting){
		Account acc = null;
		if (detail instanceof PayrollPmtSlryUnitDet) {
			PayrollPmtSlryUnitDet det = (PayrollPmtSlryUnitDet) detail;
			acc = det.getAccount();			
		}
		else if (detail instanceof PayrollPmtTax21UnitDet) {
			PayrollPmtTax21UnitDet det = (PayrollPmtTax21UnitDet) detail;
			acc = det.getAccount();			
		}
		if (setting!=null){
			JournalStandard js = setting.getJournalStandard();
			if (js!=null){
				JournalStandardAccount[] jsas = js.getJournalStandardAccount();
				for(int i=0; i< jsas.length; i++){
					if (acc!=null){
						if (jsas[i].getAccount().getIndex()==acc.getIndex()){
							return jsas[i];
						}
					}
				}
			}
		}
		return null;
	}
}
