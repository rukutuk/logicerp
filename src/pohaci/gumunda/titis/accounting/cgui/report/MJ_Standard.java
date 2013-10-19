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
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.MemJournalStrd;
import pohaci.gumunda.titis.accounting.entity.MemJournalStrdDet;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class MJ_Standard {
	
	public MJ_Standard(MemJournalStrd entity,Connection conn,Currency basecurr ) {
		try {			
			String filename = "MJ_Standard.jrxml";      
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));			
			
			if (entity.getUnit()!=null){				
				parameters.put("param_unit_code", entity.getUnit().getDescription());
			}else
				parameters.put("param_unit_code", "");			
			
			parameters.put("param_transaction_code", entity.getTransactionCode().toString());
			
			if (entity.getDepartment()!=null)
				parameters.put("param_department_code", String.valueOf(entity.getDepartment()));
			else
				parameters.put("param_department_code", "");
			
			parameters.put("param_penjualan_activa", "");
			
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
			
			parameters.put("param_note",entity.getDescription());
			
			
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("field4");
			model.addColumn("field5");
			model.addColumn("status");
			
			parameters.put("param_work_description", "");
			GenericMapper mapper2=MasterMap.obtainMapperFor(MemJournalStrdDet.class);
			mapper2.setActiveConn(conn);
			List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_MEMORIAL_JOURNAL_STANDARD+"="+entity.getIndex());
			MemJournalStrdDet detail;
			int maxloop = detailList.size();
			double ttlCredit = 0;
			double ttlDebit = 0;
			int num = 1;
			String curr =  basecurr.getSymbol();
			for(int i=0;i<maxloop;i++){
				detail=(MemJournalStrdDet)detailList.get(i);
				if (detail.getBalanceCode().equalsIgnoreCase(Account.STR_DEBET)){
					double accValue = 0;
					if (curr.equals(detail.getCurrency().getSymbol()))
						accValue = detail.getAccValue();
					else
						accValue = (detail.getAccValue()*detail.getExchangeRate());						
					ttlDebit += accValue;						
					model.addRow(new Object[]{
							String.valueOf(num++), detail.getAccount().getName(),  
							curr + " " + desimalFormat.format(accValue), null, detail.getAccount().getCode(), 
							new Integer(1)});
				}
			}				
			for(int i=0;i<maxloop;i++){
				detail=(MemJournalStrdDet)detailList.get(i);
				if (detail.getBalanceCode().equalsIgnoreCase(Account.STR_CREDIT)){
					double accValue = 0;
					if (curr.equals(detail.getCurrency().getSymbol()))
						accValue = detail.getAccValue();
					else
						accValue = (detail.getAccValue()*detail.getExchangeRate());
					ttlCredit +=accValue;
					model.addRow(new Object[]{
							String.valueOf(num++),detail.getAccount().getName(), null, curr + " " + desimalFormat.format(accValue), 
							detail.getAccount().getCode(), new Integer(1)});
				}
			}			
			model.addRow(new Object[]{"", "Total", curr + " " + desimalFormat.format(ttlDebit), 
					curr + " " + desimalFormat.format(ttlCredit), "", new Integer(2)});				
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Memorial Journal");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}