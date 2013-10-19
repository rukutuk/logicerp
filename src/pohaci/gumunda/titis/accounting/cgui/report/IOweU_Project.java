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
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class IOweU_Project {
	public IOweU_Project(PmtCAIOUProject entity,EmployeePicker payToComp){
		try {
			String filename = "IOweU_project.jrxml";
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


			String payto = entity.getPayTo().getFirstName() + " " + entity.getPayTo().getMidleName() + " " +
			entity.getPayTo().getLastName();
			parameters.put("param_pay_to", payto);
			parameters.put("param_job_title",payToComp.getJobTitle());
			//System.err.println("Job Title :" + entity.getPayTo().getJobTitleName());

			ProjectData a = entity.getProject();
			parameters.put("param_project_code",a.toString());

			if (a.getCustomer()!=null)
				parameters.put("param_client",
						String.valueOf(a.getCustomer().toString()));
			else
				parameters.put("param_client", "");

			parameters.put("param_contract_no", a.getPONo());

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

			parameters.put("param_work_description",String.valueOf(a.getWorkDescription()));
			parameters.put("param_ipc_no", a.getIPCNo());

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

			String curr = "";
			double total = entity.getAmount();

			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("status");

			Currency cr = null;
			if (entity.getCurrency()!=null) {
				curr = entity.getCurrency().getSymbol();
				cr = entity.getCurrency();
			}
			model.addRow(new Object[]{"1","" + entity.getDescription(),
					curr+" " +desimalFormat.format(total), new Integer(1)});

			model.addRow(new Object[]{"", "TOTAL",curr+" "+desimalFormat.format(total), new Integer(2)});
			
			String moneyTalk = MoneyTalk.say((total),cr,
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{moneyTalk, "","", new Integer(3)});

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("I OWE U Project");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
