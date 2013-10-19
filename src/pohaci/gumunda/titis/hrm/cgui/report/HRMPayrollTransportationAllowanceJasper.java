package pohaci.gumunda.titis.hrm.cgui.report;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

//import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JRDataSource;
//import java.util.Map;
import java.util.HashMap;
//import java.sql.*;
//import java.text.SimpleDateFormat;
//import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.hrm.logic.PayrollTransportationAllowanceSubmitLogic;

import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperPrintManager;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
//import jxl.JXLException;
//import pohaci.gumunda.titis.project.cgui.ProjectData;
//import java.sql.*;
//import pohaci.gumunda.titis.project.cgui.*;
//import pohaci.gumunda.titis.accounting.cgui.*;
//import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class HRMPayrollTransportationAllowanceJasper {
       
  public HRMPayrollTransportationAllowanceJasper(PayrollTransportationAllowanceSubmitPanel panel,
		  PayrollComponent[] payrollComponents,Employee_n[] emps,
		  Unit unit,
		  EmployeeTransportationAllowance[] empTranSubmit,
		  PayrollComponent[] payrollComponentsDefault ,boolean cekExcel) {
	
    try {
      String filename = "RptPayrollTransportationAllowance.jrxml";     
      //String name = "";
      if(filename.equals(""))
        return;      
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      HashMap parameters = new HashMap();        
      DefaultTableModel model = new DefaultTableModel();      
      tableHeader(model);
      PayrollTransportationAllowanceSubmitLogic transLogic = new PayrollTransportationAllowanceSubmitLogic(
    		  panel,model,payrollComponents,emps,unit,empTranSubmit,payrollComponentsDefault,cekExcel);
      transLogic.tableBodyReport(false);
    /*  if (emps.length==0){
    	  model.addRow(new Object[]{descUnit,date,"","","","",""});
      }else {
	      for (int i=0;i<emps.length;i++){
	    	  name = emps[i].getFirstname() + " " + emps[i].getMidlename() + " " + emps[i].getLastname();	    	  
	    		  model.addRow(new Object[]{descUnit,date,String.valueOf(i+1),emps[i].getEmployeeno(),name,emps[i].getJobtitle(),""});
	    	  
	      }	
      }*/
	  
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Transportation Allowances summary");
        view.setVisible(true);
      }

    }
    catch(Exception ex){
      ex.printStackTrace();
    }
  }


private void tableHeader(DefaultTableModel model) {
	model.addColumn("Unit");
      model.addColumn("Tgl");
      model.addColumn("No");  
      model.addColumn("EmpID");
      model.addColumn("Name");
      model.addColumn("JobTitle");
      model.addColumn("Presence");
      model.addColumn("PresenceNotLate");
      model.addColumn("Late");
      model.addColumn("A_Transport");
      model.addColumn("B_Transport");
      model.addColumn("istotal");
}

  public static String changeFileExtension(String filename, String newExtension ) {
    if (!newExtension.startsWith("."))
      newExtension = "." + newExtension;
    if (filename == null || filename.length()==0 ) {
      return newExtension;
    }

    int index = filename.lastIndexOf(".");
    if (index >= 0) {
      filename = filename.substring(0,index);
    }
    return filename += newExtension;
  }

  void exportToExcel(JasperPrint jasperPrint) throws Exception {
    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser("reportsample/");

    jfc.setDialogTitle("Save to Excel");
    jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
      public boolean accept(java.io.File file) {
        String filename = file.getName();
        return (filename.toLowerCase().endsWith(".xls") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
      }
      public String getDescription() {
        return "Report *.xls";
      }
    });

    jfc.setMultiSelectionEnabled(false);

    jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
    if  (jfc.showSaveDialog(null) == javax.swing.JOptionPane.OK_OPTION) {

      JExcelApiExporter exporter = new JExcelApiExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
      exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(), "xls"));
      exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
      exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
      exporter.exportReport();    
    }
  }
}
