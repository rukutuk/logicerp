package pohaci.gumunda.titis.accounting.cgui.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;

public class IOweU_Others {
	public IOweU_Others(PmtCAIOUOthers entity,EmployeePicker PayToComp){
		try {
			String filename = "IOweU_Others.jrxml";

			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");

			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
			parameters.put("param_cash_bank","");
			if (entity.getCashAccount()!=null)
				parameters.put("param_cash_bank",entity.getCashAccount().getCode() + " " +
						entity.getCashAccount().getName());

			if (entity.getPayTo()!=null){
				String empPayto = entity.getPayTo().getFirstName() + " " +
					entity.getPayTo().getMidleName() + " " + entity.getPayTo().getLastName();
				parameters.put("param_payto", empPayto);
				parameters.put("param_job_title", PayToComp.getJobTitle());
			}

			if (entity.getUnit()!=null)
				parameters.put("param_unit_code", entity.getUnit().getDescription());
			else
				parameters.put("param_unit_code", "");

			if (entity.getDepartment()!=null)
				parameters.put("param_department_code", entity.getDepartment().getName());
			else
				parameters.put("param_department_code", "");

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

			String getCurr = "";
			Currency curr = null;
			if (entity.getCurrency()!=null) {
				getCurr = entity.getCurrency().getSymbol();
				curr = entity.getCurrency();
			}
			double total = entity.getAmount();
			model.addRow(new Object[]{"1",entity.getDescription(),
					getCurr+ " " + desimalFormat.format(total),new Integer(1)});
			model.addRow(new Object[]{"", "TOTAL",getCurr+ " " + desimalFormat.format(total), new Integer(2)});

			String moneyTalk = MoneyTalk.say(total,curr,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);

			model.addRow(new Object[]{moneyTalk, "", "",new Integer(3)});

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("I OWE U Others");
			view.setVisible(true);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
