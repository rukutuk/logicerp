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
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.EmployeeOvertimeSubmit;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollOvertimeSubmitPanel;
import pohaci.gumunda.titis.hrm.logic.PayrollOvertimeSubmitLogic;


public class HRMPayrollOvertimeJasper {
      

   public HRMPayrollOvertimeJasper(PayrollOvertimeSubmitPanel panel,
	  PayrollComponent[] payrollComponents,Employee_n[] emps,
	  Unit unit,EmployeeOvertimeSubmit[] empOverTimeSubmit,
	  PayrollComponent[] payrollComponentsDefault,boolean cekExcel){
	
    try {
      String filename = "RptPayrollOvertime.jrxml";      
      if(filename.equals(""))
    	  return;     
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      HashMap parameters = new HashMap();        
      DefaultTableModel model = new DefaultTableModel();      
      tableHeader(model);      
      PayrollOvertimeSubmitLogic timeLogic = new PayrollOvertimeSubmitLogic(panel,model,payrollComponents,emps,
    		  unit,empOverTimeSubmit,payrollComponentsDefault,cekExcel);
      timeLogic.tableBodyReport(false);    	  
	  
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Overtime Summary");
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
	model.addColumn("temp");
	model.addColumn("hour1");
	model.addColumn("hour2");
	model.addColumn("hour3");
	model.addColumn("hour4");
	model.addColumn("hour5");
	model.addColumn("A_Lembur");
	model.addColumn("B_Lembur");
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
