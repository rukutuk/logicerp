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

import pohaci.gumunda.titis.accounting.cgui.PayrollTaxArt21VerificationPanel;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.PayrollTaxArt21SubmitPanel;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21SubmitEmployeeDetail;
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

public class HRMPayrollTaxArt21Jasper {
       
  public HRMPayrollTaxArt21Jasper(PayrollTaxArt21SubmitPanel panel,String descUnit,String date, Employee_n[] emps,boolean cekExcel) {
	
    try {
      String filename = "RptPayrollTaxArt21.jrxml";
      if(filename.equals(""))
        return;      
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      HashMap parameters = new HashMap();
      parameters.put("param_unit",descUnit);
      parameters.put("param_date",date);
      DefaultTableModel model = new DefaultTableModel();
      header(model);   
      
      if (emps.length==0){
    	  model.addRow(new Object[]{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""});
      } else {
    	  panel.tableBodyReport(model, cekExcel);	      
      }
	    	  
	  
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Tax Art 21 Summary");
        view.setVisible(true);
      }

    }
    catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
  public HRMPayrollTaxArt21Jasper(PayrollTaxArt21VerificationPanel panel,String descUnit,String date, TaxArt21SubmitEmployeeDetail[] emps,boolean cekExcel) {
		
	    try {
	      String filename = "RptPayrollTaxArt21.jrxml";     
	      //String name = "";
	      if(filename.equals(""))
	        return;      
	      
	      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
	      HashMap parameters = new HashMap();
	      parameters.put("param_unit",descUnit);
	      parameters.put("param_date",date);
	      DefaultTableModel model = new DefaultTableModel();
	      header(model);   
	      
	      if (emps.length==0){
	    	  model.addRow(new Object[]{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""});
	      } else {
	    	  panel.tableBodyReport(model,cekExcel);
		      /*for (int i=0;i<emps.length;i++){	    	  
		    	  
		      }*/
	      }
		    	  
		  
	      JRTableModelDataSource ds = new JRTableModelDataSource(model);
	      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
	          parameters, ds);

	      if(cekExcel){
	        exportToExcel(jasperPrint);
	      }else{
	        PrintingViewer view = new PrintingViewer(jasperPrint);
	        view.setTitle("Tax Art 21 Summary");
	        view.setVisible(true);
	      }

	    }
	    catch(Exception ex){
	      ex.printStackTrace();
	    }
	  }


private void header(DefaultTableModel model) {
      model.addColumn("No");  
      model.addColumn("EmpID");      
      model.addColumn("Name");
      model.addColumn("JobTitle");
      model.addColumn("ptkp");
      model.addColumn("gapok");
      model.addColumn("kes");
      model.addColumn("mgmt");
      model.addColumn("tek");
      model.addColumn("rmh");
      model.addColumn("thr");
      model.addColumn("lap");      
      model.addColumn("mkn");
      model.addColumn("transp");
      model.addColumn("lembur");
      model.addColumn("insertif");
      model.addColumn("bonus");
      model.addColumn("as_peg");
      model.addColumn("lain");      
      model.addColumn("jab"); 
      model.addColumn("pot_as_peg");
      model.addColumn("income_mounth");
      model.addColumn("income_year");
      model.addColumn("as_peg_percent");
      model.addColumn("pkp_amounth");
      model.addColumn("pkp_rounded");
      model.addColumn("tax_mounthly");
      model.addColumn("tax_year");
      model.addColumn("status");
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
