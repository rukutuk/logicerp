package pohaci.gumunda.titis.accounting.cgui.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.entity.PmtUnitBankCashTrans;
import pohaci.gumunda.titis.accounting.entity.RcvUnitBankCashTrns;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class Rcpt_UnitCashBankTrans {
	public Rcpt_UnitCashBankTrans(RcvUnitBankCashTrns entity) {
		try {
			String filename = "Rcpt_UnitCashBankTrans";
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
			
			GenericMapper mapnya = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
			//UnitBankCashTransferLoader loader= new  UnitBankCashTransferLoader(m_conn,PmtUnitBankCashTrans.class);
			Object obj=mapnya.doSelectByIndex(new Long(entity.getTransferFrom()));
			PmtUnitBankCashTrans a = (PmtUnitBankCashTrans)obj;
			if (a.getBankAccount()!=null){
				parameters.put("param_receive_from", a.getBankAccount().getName());
				parameters.put("param_receive_from_unit_code", a.getBankAccount().getUnit().getDescription());
			}else if (a.getCashAccount()!=null){
				parameters.put("param_receive_from", a.getCashAccount().getName());
				parameters.put("param_receive_from_unit_code", a.getCashAccount().getUnit().getDescription());
			}else{
				parameters.put("param_receive_from", "");
				parameters.put("param_receive_from_unit_code", a.getCashAccount().getUnit().getDescription());
			}
			CashBankAccount cb = null;
			if(a.getPayTo().equalsIgnoreCase("bank")){					
				cb = a.getRcvBankAccount();
			}else{					
				cb = a.getRcvCashAccount();
			}				
			parameters.put("param_receive_to_unit_code", cb.getUnit().getDescription());
			parameters.put("param_transaction_type", "Unit Bank/Cash Transfer Receive");
			
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
			
			String curr = "";
			if (a.getCurrency()!=null)
				curr = a.getCurrency().getSymbol();
			
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("status");
			
			double total = entity.getAmount();
			
			model.addRow(new Object[]{
					"1", entity.getDescription(), 
					curr+ " " +desimalFormat.format(total), new Integer(1)});
			model.addRow(new Object[]{"", "TOTAL",curr +" " +desimalFormat.format(total), new Integer(2)});
			
			
			String moneyTalk = MoneyTalk.say(total,a.getCurrency(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{"Amount in word : " + moneyTalk , "","", new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Unit Cash Bank Transfer Receipt");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
