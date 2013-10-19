package pohaci.gumunda.titis.project.cgui.report;

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
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.text.SimpleDateFormat;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import pohaci.gumunda.titis.project.cgui.*;
import pohaci.gumunda.titis.application.PrintingViewer;

public class ProjectMonitoringProgressJasper {
  
  private SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");     
  public ProjectMonitoringProgressJasper(Connection conn, ProjectProgress[] progress,boolean cekExcel) {
    try {
      String filename = "RptProjectMonitoringProgress.jrxml";             
      if(filename.equals(""))
        return;
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      Map parameters = new HashMap();
      DefaultTableModel model = new DefaultTableModel();
      model.addColumn("No");
      model.addColumn("Date");      
      model.addColumn("Description"); 
      model.addColumn("Complete"); 
      model.addColumn("PreparedBy"); 
      model.addColumn("AprovedBy"); 
      model.addColumn("Remark");       
      if (progress.length==0){    	  
    	  model.addRow(new Object[]{"","","","","","",""});
      }else{
	      for (int i=0; i<progress.length ; i++ ){                                
	          String date = "";
	          if(progress[i].getDate() != null)
	             date = m_dateformat.format(progress[i].getDate());
	          model.addRow(new Object[]{String.valueOf(i + 1),date,
	          progress[i].getDescription(),String.valueOf(new Float(progress[i].getCompletion())),progress[i].getPreparedBy().toString(),
	          progress[i].getApprover().toString(),progress[i].getRemark()});
	      }
      }
      

      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Project Progress");
        view.setVisible(true);
      }

    }
    catch(Exception ex){
      ex.printStackTrace();
    }
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