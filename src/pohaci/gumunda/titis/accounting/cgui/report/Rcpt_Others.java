package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.RcvOthers;
import pohaci.gumunda.titis.accounting.entity.RcvOthersDetail;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class Rcpt_Others {
	public Rcpt_Others(RcvOthers entity,Connection conn){
		try {
			// .jxml dibuang			
			String filename = "Rcpt_Others";
			// ditambahkan 2 baris
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
			
			parameters.put("param_receive_from", entity.getReceiveFrom());
			
			if (entity.getUnit()!=null)
				parameters.put("param_unit_code",entity.getUnit().toString());
			else
				parameters.put("param_unit_code", "");
			if (entity.getDepartment()!=null)
				parameters.put("param_department_code", entity.getDepartment().toString());
			else
				parameters.put("param_department_code", "");
			
			parameters.put("param_transaction_type", "Others Receive");			
			
			if (entity.getBankAccount()!=null)
				parameters.put("param_cash_bank", entity.getBankAccount().getCode() + " " + entity.getBankAccount().getName());
			else
				parameters.put("param_cash_bank", entity.getCashAccount().getCode() + " " + entity.getCashAccount().getName());		
			
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
			
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("status");
			
			GenericMapper mapper2=MasterMap.obtainMapperFor(RcvOthersDetail.class);
			mapper2.setActiveConn(conn);
			List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_RECEIVE_OTHERS+"="+entity.getIndex());
			int length = detailList.size();
			
			RcvOthersDetail detail; 
			String curr = "";
			double total = 0;
			short cekdebit = -1;
			int j=0;			
			ArrayList det = new ArrayList();
			ArrayList jsAcclist = new ArrayList();
			for(int i=0;i<length;i++){
				detail=(RcvOthersDetail)detailList.get(i);
				long indexAcc = detail.getAccount().getIndex();
				GenericMapper mapper3=MasterMap.obtainMapperFor(JournalStandardAccount.class);
				mapper3.setActiveConn(conn);
				List rs3=mapper3.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+entity.getJournal().getIndex() +
						" AND " + IDBConstants.ATTR_ACCOUNT + "=" + indexAcc);	
				JournalStandardAccount jsAccount=(JournalStandardAccount)rs3.get(0);				
				if (jsAccount.isHidden() || jsAccount.isCalculate()){
					cekdebit = jsAccount.getBalance();					
				}else{
					det.add(detail);
					jsAcclist.add(jsAccount);
				}
			}
			
			for(int i=0;i<det.size();i++){
				detail=(RcvOthersDetail)det.get(i);
				JournalStandardAccount jsAccount=(JournalStandardAccount)jsAcclist.get(i);
				if (jsAccount.getBalance()==cekdebit)
					total -= detail.getaccValue();
				else if (cekdebit>=0)
					total += detail.getaccValue();
				if (detail.getCurrency()!=null)
					curr = detail.getCurrency().getSymbol();
				model.addRow(new Object[]{String.valueOf(++j),detail.getDescription(),
						curr+" " +desimalFormat.format(detail.getaccValue()),new Integer(1)});
				
			}	
			
			/*String curr = "";
			
			for(int i=0;i<length;i++){
				detail=(RcvOthersDetail)detailList.get(i);
				long indexAcc = detail.getAccount().getIndex();
				GenericMapper mapper3=MasterMap.obtainMapperFor(JournalStandardAccount.class);
				mapper3.setActiveConn(conn);		
				List rs3=mapper3.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+entity.getJournal().getIndex() 
						+ " AND " + IDBConstants.ATTR_ACCOUNT + "=" + indexAcc);			
				JournalStandardAccount jsAccount=(JournalStandardAccount)rs3.get(0);
				if (!jsAccount.isHidden() && !jsAccount.isCalculate()){
					if (detail.getCurrency()!=null)
						curr = detail.getCurrency().getSymbol();
					total += detail.getaccValue();		
					model.addRow(new Object[]{
							String.valueOf(++j), detail.getDescription(), 
							curr +" "+desimalFormat.format(detail.getaccValue()), new Integer(1)});										
				}
			}*/			
			if (j==0 || detailList.size()==0)
				model.addRow(new Object[]{"", "","", new Integer(1)});
			
			model.addRow(new Object[]{"", "TOTAL",curr+ " " + desimalFormat.format(total), new Integer(2)});
	
			String moneyTalk = MoneyTalk.say(total,entity.getCurrency(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{"Amount in word : " + moneyTalk , "", "",new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
            
			//  diganti compiledRptFilename 
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,parameters,ds); 
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Others Receipt");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
