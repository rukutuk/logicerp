package pohaci.gumunda.titis.project.cgui.report;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectlistJasper {
  //private Connection m_conn = null;
  //private ProjectData[] m_project = null;
  //private Unit m_unit = null;
  private Customer m_customer = null;
  private SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");     
  public ProjectlistJasper(Connection conn,ProjectData[] project, boolean cekExcel) {
    try {
      String filename = "RptProjectlist.jrxml";
      String customer = "";       
      if(filename.equals(""))
        return;

      
      ProjectBusinessLogic logic = new ProjectBusinessLogic(conn);      
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      Map parameters = new HashMap();
      DefaultTableModel model = new DefaultTableModel();
      model.addColumn("No");
      model.addColumn("ProjectCode");      
      model.addColumn("Customer"); 
      model.addColumn("WorkDesc"); 
      model.addColumn("ActCode"); 
      model.addColumn("Orno"); 
      model.addColumn("Ordate"); 
      model.addColumn("Pono"); 
      model.addColumn("Podate"); 
      model.addColumn("Ipcno"); 
      model.addColumn("StartDate"); 
      model.addColumn("EndDate"); 
      model.addColumn("ValidationDate"); 
      if (project.length==0){    
    	  model.addRow(new Object[]{"","","","","","","","","","","","","",""});
      }else{
	      for (int i=0; i<project.length ; i++ ){
	          
	          m_customer = logic.getCustomerByIndex(project[i].getIndexcust());
	          if (m_customer!=null)          
	               customer = m_customer.getName();          
	        
	          String Ordate = "";
	          if(project[i].getORDate() != null)
	             Ordate = m_dateformat.format(project[i].getORDate());
	          
	          String Podate = "";
	          if(project[i].getPODate()!= null)
	             Podate = m_dateformat.format(project[i].getPODate());                  
	      
	          String Enddate = "";
	          if (project[i].getEnddate()!=null)
	              Enddate = m_dateformat.format(project[i].getEnddate());
	          
	          String Startdate = "";
	          if (project[i].getStartdate()!=null)
	              Enddate = m_dateformat.format(project[i].getStartdate());
	          
	          String Validation = "";
	          if (project[i].getValidation()!=null)
	              Validation = m_dateformat.format(project[i].getValidation());
	          
	          model.addRow(new Object[]{String.valueOf(i + 1), project[i].getCode(),
	          customer,project[i].getWorkDescription(),project[i].getAct(),project[i].getORNo(),
	          Ordate,project[i].getPONo(),Podate,project[i].getIPCNo(),Startdate,Enddate,Validation});
	      }
      
      }
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
    	  exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Project List");
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
    	//JExcelApiExporter exporterXLS = new JExcelApiExporter();
    	JExcelApiExporter exporter = new JExcelApiExporter();
    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    	exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(), "xls"));
    	exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
    	exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
    	exporter.exportReport();           
    }
  }
}