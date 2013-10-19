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
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtOthers;
import pohaci.gumunda.titis.accounting.entity.PmtOthersDetail;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class Vchr_Others {
	JasperReport jreport = null;
	JasperPrint jprint = null;
	Map param = null;

	public Vchr_Others(PmtOthers entity,Connection conn,long sessionid) {
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


			if (entity.getDepartment()!=null)
				parameters.put("param_department_code", entity.getDepartment().toString());
			else
				parameters.put("param_department_code", "");
			parameters.put("param_payto", entity.getPayTo());
			parameters.put("param_transaction_type", "Others Payment");

			GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOthersDetail.class);
			mapper2.setActiveConn(conn);
			List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_PMT_OTHERS+"="+entity.getIndex());
			PmtOthersDetail detail;

			String curr = "";
			Currency currency = null;
			double total = 0;
			int length = detailList.size();
			int j=0;
			//short cekdebit = -1;
			ArrayList det = new ArrayList();
			ArrayList jsAcclist = new ArrayList();
			for(int i=0;i<length;i++){
				detail=(PmtOthersDetail)detailList.get(i);
				long indexAcc = detail.getAccount().getIndex();
				GenericMapper mapper3=MasterMap.obtainMapperFor(JournalStandardAccount.class);
				mapper3.setActiveConn(conn);
				List rs3=mapper3.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+entity.getJournal().getIndex() +
						" AND " + IDBConstants.ATTR_ACCOUNT + "=" + indexAcc);
				JournalStandardAccount jsAccount=(JournalStandardAccount)rs3.get(0);
				/*if (jsAccount.isHidden() || jsAccount.isCalculate()){
					cekdebit = jsAccount.getBalance();
				}else{
					det.add(detail);
					jsAcclist.add(jsAccount);
				}*/
				if (jsAccount.isHidden() && !jsAccount.isCalculate()) {

				} else {
					if (detail.getAccValue() != 0) {
						det.add(detail);
						jsAcclist.add(jsAccount);
					}
				}
			}

			for(int i=0;i<det.size();i++){
				detail=(PmtOthersDetail)det.get(i);
				JournalStandardAccount jsAccount=(JournalStandardAccount)jsAcclist.get(i);
				/*if (jsAccount.getBalance()==cekdebit)
					total -= detail.getAccValue();
				else if (cekdebit>=0)
					total += detail.getAccValue();*/
				if (jsAccount.getBalance()==0)
					total += detail.getAccValue();
				else
					total -= detail.getAccValue();
				if (detail.getCurrency()!=null){
					currency = detail.getCurrency();
					curr = currency.getSymbol();
				}

				if (jsAccount.getBalance()==0)
					model.addRow(new Object[]{String.valueOf(++j),detail.getDescription(),
						curr+" " +desimalFormat.format(detail.getAccValue()),new Integer(1)});
				else
					model.addRow(new Object[]{String.valueOf(++j),detail.getDescription(),
							"(" + curr+" " +desimalFormat.format(detail.getAccValue()) + ")",new Integer(1)});

			}

			if (j==0 || detailList.size()==0)
				model.addRow(new Object[]{"", "","", new Integer(1)});

			model.addRow(new Object[]{"", "TOTAL",curr+" " +desimalFormat.format(total), new Integer(2)});

			String moneyTalk = MoneyTalk.say(total,currency,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);

			model.addRow(new Object[]{moneyTalk, "", "", new Integer(3)});

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			jprint = jasperPrint;
			param = parameters;

			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Others Payment");
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
