package pohaci.gumunda.titis.accounting.cgui.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectInstall;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.JasperViewerDlg;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class Vcr_IOUInstProject {
	public Vcr_IOUInstProject(JDialog owner,PmtCAIOUProject entityParent,PmtCAIOUProjectInstall entity,EmployeePicker payToComp){
		try {      
			String filename = "Vchr_IOUInstProject.jrxml";
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			
			parameters.put("param_logo", "../images/TS.gif");
			parameters.put("param_no", entity.getReferenceNo());
			if (entity.getTransactionDate()!=null)
				parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
			else
				parameters.put("param_date", "");
			
			if (entityParent.getCashAccount()!=null)
				parameters.put("param_cash_bank",entityParent.getCashAccount().getCode() + " " + 
						entityParent.getCashAccount().getName());
			
			String payto = entityParent.getPayTo().getFirstName() + " " + entityParent.getPayTo().getMidleName() + " " +
				entityParent.getPayTo().getLastName();
			parameters.put("param_pay_to", payto);			
			parameters.put("param_job_title",payToComp.getJobTitle());
			parameters.put("param_IOU_no",entityParent.igetIOweUNo());
			if (entityParent.igetIOweUDate()!=null)
				parameters.put("param_IOU_date",dateFormat.format(entityParent.igetIOweUDate()));
			else
				parameters.put("param_IOU_date","");
			if (entityParent.getProject()!=null){
				ProjectData p = entityParent.getProject();
				parameters.put("param_client",p.getCustomer().getName());
				parameters.put("param_SOPO_contract",p.getPONo());
				parameters.put("param_unit_code",p.getUnit().getDescription());
				parameters.put("param_activity_code",p.getActivity().getName());
				parameters.put("param_department_code",p.getDepartment().getName());				
				parameters.put("param_work_description",p.getWorkDescription());
				parameters.put("param_ipc_no",p.getIPCNo());
			}
			parameters.put("param_transaction_type","I Owe U Installment - Project Payment");
			
			if (entity.getEmpOriginator()!=null){
				String namaOri = entity.getEmpOriginator().getFirstName() + " " + entity.getEmpOriginator().getMidleName() + 
				" " + entity.getEmpOriginator().getLastName();
				parameters.put("param_nama_originator", namaOri);
			}else
				parameters.put("param_nama_originator", "");			
			
			parameters.put("param_jabatan_originator", entity.getJobTitleOriginator());
			
			if (entity.getDateOriginator()!=null)
				parameters.put("param_date_originator", dateFormat.format(entity.getDateOriginator()));
			else
				parameters.put("param_date_originator", "");
			
			if (entity.getEmpApproved()!=null){
				String empApp = entity.getEmpApproved().getFirstName() + " " + entity.getEmpApproved().getMidleName() + 
				" " + entity.getEmpApproved().getLastName();
				parameters.put("param_nama_approver", empApp);
			}else
				parameters.put("param_nama_approver", "");			
			parameters.put("param_jabatan_approver", entity.getJobTitleApproved());
			if (entity.getDateApproved()!=null)
				parameters.put("param_date_approver", dateFormat.format(entity.getDateApproved()));
			else
				parameters.put("param_date_approver", "");
			
			if (entity.getEmpReceived()!=null){
				String empRec = entity.getEmpReceived().getFirstName() + " " + entity.getEmpReceived().getMidleName() 
				+ " " + entity.getEmpReceived().getLastName();
				parameters.put("param_nama_receiver", empRec);
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
			
			model.addRow(new Object[]{"1", entity.getDescription(),entity.getCurrency().getSymbol()+ " " +
					desimalFormat.format(entity.getAmount()), new Integer(1)});
			model.addRow(new Object[]{"", "TOTAL",entity.getCurrency().getSymbol()+" " +
					desimalFormat.format(entity.getAmount()), new Integer(2)});
			
			String moneyTalk = MoneyTalk.say(entity.getAmount(),entity.getCurrency(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			
			model.addRow(new Object[]{moneyTalk, "","", new Integer(3)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);
			//PrintingViewer view = new PrintingViewer(jasperPrint);
            JasperViewerDlg view = new JasperViewerDlg(jasperPrint,false,owner);
			view.setTitle("I Owe U Installment Project Voucher");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
