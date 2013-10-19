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

//import pohaci.gumunda.cross.cgui.Unit;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
//import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.EmployeeMealAllowanceSubmit;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollMealAllowanceSubmitPanel;
import pohaci.gumunda.titis.hrm.logic.PayrollMealAllowanceSubmitLogic;
//import pohaci.gumunda.titis.hrm.logic.EmployeePayroll;
//import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
//import pohaci.gumunda.titis.hrm.logic.EmployeePayroll.PayrollCalcResult;
//import pohaci.gumunda.titis.project.dbapi.IDBConstants;
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

public class HRMPayrollMealAllowanceJasper {

  public HRMPayrollMealAllowanceJasper(PayrollMealAllowanceSubmitPanel panel,
		  PayrollComponent[] payrollComponents,Employee_n[] emps,Unit unit,
		  EmployeeMealAllowanceSubmit[] empMealAllowSubmit,PayrollComponent[] payrollComponentsDefault,
		  boolean cekExcel) {	
    try {     
    	
      String filename = "RptPayrollMealAllowance.jrxml";
      if(filename.equals(""))
        return;      
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      HashMap parameters = new HashMap();        
      DefaultTableModel model = new DefaultTableModel();      
      tableHeader(model);
      PayrollMealAllowanceSubmitLogic mealLogic = new PayrollMealAllowanceSubmitLogic(panel,model,payrollComponents,emps,unit,
    		  empMealAllowSubmit,payrollComponentsDefault,cekExcel);
      mealLogic.tableBodyReport(false);
      
      JasperPrint jasperPrint=null;
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      try{
       jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);
      }catch(Exception e){
    	  e.printStackTrace();
      }

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Meal Allowances Summary");
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
      model.addColumn("A_Gapok");
      model.addColumn("B_Gapok");
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
