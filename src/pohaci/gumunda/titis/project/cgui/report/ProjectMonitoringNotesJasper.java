package pohaci.gumunda.titis.project.cgui.report;

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

public class ProjectMonitoringNotesJasper {
  private SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");     
  public ProjectMonitoringNotesJasper(Connection conn , ProjectNotes[] notes, boolean cekExcel) {
    try {
      String filename = "RptProjectMonitoringNotes.jrxml";             
      if(filename.equals(""))
        return;            
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      Map parameters = new HashMap();
      DefaultTableModel model = new DefaultTableModel();
      model.addColumn("No");
      model.addColumn("Date");      
      model.addColumn("Description"); 
      model.addColumn("Action"); 
      model.addColumn("Responsibility"); 
      model.addColumn("PreparedBy"); 
      model.addColumn("AprovedBy");       
      model.addColumn("Remark");       
      model.addColumn("Attachment");   
      if (notes.length==0){    	  
    	  model.addRow(new Object[]{"","","","","","","","",""});
      }else{
	     for (int i=0; i<notes.length; i++ ){                                
	          String date = "";
	          if(notes[i].getDate() != null)
	             date = m_dateformat.format(notes[i].getDate());          
	          
	          model.addRow(new Object[]{String.valueOf(i + 1),date,
	          notes[i].getDescription(),notes[i].getAction(),notes[i].getResponsibility().toString(),
	          notes[i].getPreparedBy().toString(),notes[i].getApprover().toString(),
	          notes[i].getRemark(),notes[i].getFile()});
	      }
      }
      

      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Monitoring Notes");
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