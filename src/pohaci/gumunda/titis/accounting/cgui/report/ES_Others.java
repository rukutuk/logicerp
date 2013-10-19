package pohaci.gumunda.titis.accounting.cgui.report;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

//import com.sun.corba.se.internal.core.OSFCodeSetRegistry.Entry;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheetDetail;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
//import pohaci.gumunda.titis.accounting.entity.MemJournalStrdDet;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class ES_Others {

  public ES_Others(ExpenseSheet entity,Connection conn) {
    try {
      String filename = "ES_Others.jrxml";
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      Map parameters = new HashMap();
      DefaultTableModel model = new DefaultTableModel();

      parameters.put("param_logo", "../images/TS.gif");
      parameters.put("param_no", entity.getReferenceNo());
      if (entity.getTransactionDate()!=null)
    	  parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
      else
    	  parameters.put("param_date", "");
      
      String name = entity.getEsOwner().getFirstName() + " " + entity.getEsOwner().getMidleName() + " " +
      	entity.getEsOwner().getLastName();
      System.err.println("name :" + name);
      parameters.put("param_name", name);
      double advanceAmount = 0;
     
      if (entity.getEsProjectType()!=null){
    	  if (entity.getPmtCaIouOthersSettled()!=null){
    		  PmtCAIOUOthersSettled a = entity.getPmtCaIouOthersSettled();
    		  parameters.put("param_voucher_number", a.getReferenceNo());
    		  if (a.getTransactionDate()!=null)
    			  parameters.put("param_voucher_date", dateFormat.format(a.getTransactionDate()));
    		  else
    			  parameters.put("param_voucher_date", "");
    		  parameters.put("param_job_title", "");
    		  parameters.put("param_unit_code", a.getPmtcaiouothers().getUnit().toString());
    		  parameters.put("param_department_code", a.getPmtcaiouothers().getDepartment().toString());
    		  advanceAmount = a.getAmount();
    		 
    	  }else if (entity.getPmtCaOthers()!=null){
    		  PmtCAOthers a = entity.getPmtCaOthers();
    		  parameters.put("param_voucher_number", a.getReferenceNo());
    		  if (a.getTransactionDate()!=null)
    			  parameters.put("param_voucher_date", dateFormat.format(a.getTransactionDate()));
    		  else
    			  parameters.put("param_voucher_date", "");    		  
    		  if (a.getUnit()!=null)
    			  parameters.put("param_unit_code", a.getUnit().getDescription());
    		  if (a.getDepartment()!=null)
    			  parameters.put("param_department_code", a.getDepartment().getName());
    		  advanceAmount = a.getAmount();
    	  }else if (entity.getBeginningBalance()!= null) {
    		  BeginningCashAdvance a = entity.getBeginningBalance();
    		  if (a.getTrans() != null) {
    			  parameters.put("param_voucher_number", a.getTrans().getReference());
	    		  if (a.getTrans().getTransDate()!=null)
	    			  parameters.put("param_voucher_date", dateFormat.format(a.getTrans().getTransDate()));
	    		  else
	    			  parameters.put("param_voucher_date", "");    		  
	    		  if (a.getTrans().getUnit()!=null)
	    			  parameters.put("param_unit_code", a.getTrans().getUnit().getDescription());
	    		 /* if (a.!=null)
	    			  parameters.put("param_department_code", a.getDepartment().getName());*/
    		  }
    		  advanceAmount = a.getAccValue();
    	  }
      } 
      if (entity.getDepartment()!=null) {
			  parameters.put("param_department_code", entity.getDepartment().getName());
      }
      
      parameters.put("param_job_title", entity.getEsOwner().getJobTitleName());
      if (entity.getEmpOriginator()!=null){
    	  String nama = entity.getEmpOriginator().getFirstName() + " " + entity.getEmpOriginator().getMidleName() + " " +
    	  		entity.getEmpOriginator().getLastName();
    	  parameters.put("param_nama_originator", nama);
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
    	  parameters.put("param_nama_cost_center", empApp);
      }else
    	  parameters.put("param_nama_cost_center", "");
      parameters.put("param_jabatan_cost_center", entity.getJobTitleApproved());
      if (entity.getDateApproved()!=null)
    	  parameters.put("param_date_cost_center", dateFormat.format(entity.getDateApproved()));
      else
    	  parameters.put("param_date_cost_center", "");
      
      String curr = "";
      if (entity.getCurrency()!=null)
    	  curr = entity.getCurrency().getSymbol();

      model.addColumn("field1");
      model.addColumn("field2");
      model.addColumn("field3");
      model.addColumn("status");
      
      GenericMapper mapper2=MasterMap.obtainMapperFor(ExpenseSheetDetail.class);
		mapper2.setActiveConn(conn);
		List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_EXPENSE_SHEET+"="+entity.getIndex());
		ExpenseSheetDetail detail;
		double total = 0;
		for (int i=0;i<detailList.size();i++){
			detail=(ExpenseSheetDetail)detailList.get(i);
			model.addRow(new Object[]{String.valueOf(i+1), detail.getDescription(), 
					curr + " " + desimalFormat.format(detail.getaccValue()), new Integer(1)});
			total +=detail.getaccValue();			
		}
      model.addRow(new Object[]{"Total Expense", curr + " " + desimalFormat.format(total), "", new Integer(2)});
      model.addRow(new Object[]{"Advance Amount", curr + " " + desimalFormat.format(advanceAmount), "", new Integer(2)});
      model.addRow(new Object[]{"Expense Sheet Difference", curr + " " + desimalFormat.format(advanceAmount-total), "", new Integer(2)});

      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);
      PrintingViewer view = new PrintingViewer(jasperPrint);
      view.setTitle("Expense Sheet");
      view.setVisible(true);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
  } 
}
