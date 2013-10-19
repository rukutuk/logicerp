package pohaci.gumunda.titis.application;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;
//import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import pohaci.gumunda.titis.project.cgui.*;
import pohaci.gumunda.titis.project.cgui.report.RptFieldAllowances;
import pohaci.gumunda.titis.project.cgui.report.RptFieldAllowancesLogic;
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;


public class ProjectFieldAllowencesJasper {
	
	
  public ProjectFieldAllowencesJasper(RptFieldAllowances panel, EmployeeTimesheet[] employeeTimesheet,
		  AllowenceMultiplier[] areaCode,PayrollComponent[] payrollComponents,
		  Connection conn, long sessionid,boolean cekExcel) {
    try {
      String filename = "RptFieldAllowances.jrxml";          
      if(filename.equals(""))
        return;      
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      HashMap parameters = new HashMap();        
      DefaultTableModel model = new DefaultTableModel();
      tableHeader(model);      
      
      RptFieldAllowancesLogic logic = new RptFieldAllowancesLogic(model,panel,
    		  employeeTimesheet,areaCode,payrollComponents,conn,sessionid, cekExcel);
      logic.tableBodyReport();   
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Field Allowences");
        view.setVisible(true);
      }

    }
    catch(Exception ex){
      ex.printStackTrace();
    }
  }

  private void tableHeader(DefaultTableModel model) {
	  model.addColumn("Unit");      
      model.addColumn("Department");
      model.addColumn("Activity");
      model.addColumn("Tgl");
      model.addColumn("No");      
      model.addColumn("EmpNo");
      model.addColumn("EmpName");
      model.addColumn("JobTitle");
      model.addColumn("Qualification"); 
      model.addColumn("WorkDescription"); 
      model.addColumn("Client"); 
      model.addColumn("IpcNo");
      model.addColumn("A1");
      model.addColumn("A2");
      model.addColumn("A_Tunjangan");
      model.addColumn("B_Tunjangan");
      model.addColumn("totalA1");
      model.addColumn("totalA2");
      model.addColumn("subtotPenjualan");
      model.addColumn("subtotOperasional");
      model.addColumn("totPenjualan");
      model.addColumn("totOperasional");
      //model.addColumn("FooterA11");
      //model.addColumn("FooterA2");
      //model.addColumn("PrintedBy");
      //model.addColumn("Date");
      model.addColumn("Status");
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

    jfc.setDialogTitle("Simpan Laporan Dalam File Excel");
    jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
      public boolean accept(java.io.File file) {
        String filename = file.getName();
        return (filename.toLowerCase().endsWith(".xls") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
      }
      public String getDescription() {
        return "Laporan *.xls";
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
