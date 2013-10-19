package pohaci.gumunda.titis.application;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

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
import pohaci.gumunda.titis.project.cgui.EmployeeTimesheet;

public class ProjectPersonalUtilizationJasper {
  String m_unitDesc,m_dept,m_act,m_tglChar;
  String m_idEmployee,m_empName,m_jobTitle;
  DefaultTableModel m_model;
  String[] m_tempEmpId = null;
  EmployeeTimesheet[] m_employeeTimesheet;
  int m_getMergeRow = 0;  
  int m_totA1 = 0;
  int m_totA2 = 0;
  int m_totRegular = 0;
  int m_totHoliday = 0;
  public ProjectPersonalUtilizationJasper(String unitDesc,String dept,String act,String tglChar, EmployeeTimesheet[] employeeTimesheet, boolean cekExcel) {
	  this.m_employeeTimesheet = employeeTimesheet;
	  this.m_unitDesc = unitDesc;
	  this.m_dept = dept;
	  this.m_act = act;
	  this.m_tglChar = tglChar;
	  int sheetLength = m_employeeTimesheet.length;
	  
	  m_tempEmpId = new String[m_employeeTimesheet.length];	
	  //String qualification,m_no,area,A1,A2; 
	  
	  int no = 0;
	  try {
      String filename = "rptPersonalUtilization.jrxml";
             
      if(filename.equals(""))
        return;      
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      Map parameters = new HashMap();
      DefaultTableModel m_model = new DefaultTableModel();      
      tableHeader(m_model); 
      if (sheetLength==0)
    	  m_model.addRow(new Object[]{m_unitDesc,m_dept,m_act,m_tglChar,"","","","","","","","","","","",""});         	  
      else{
	      for (int i=0;i<sheetLength;i++){	    	  
	    	  m_tempEmpId[i] = String.valueOf(m_employeeTimesheet[i].getIndexPersonal());    	  
	    	  m_idEmployee = m_employeeTimesheet[i].getEmployeeno();
	    	  m_empName    = m_employeeTimesheet[i].getAllName();
	    	  m_jobTitle  = m_employeeTimesheet[i].getJobTitle();	    	  
	    	
	    	  if (i==0){	    		  
	    	  }else{	    		  
	    		  if (m_idEmployee.equals(m_employeeTimesheet[i-1].getEmployeeno())){
	    			  m_getMergeRow = 1;
	    		  }else{
	    			  if (i<(sheetLength-1))
		    			  if (m_idEmployee.equals(m_employeeTimesheet[i+1].getEmployeeno())){
		    				  m_getMergeRow = 2;
		    				  getAddModel(m_getMergeRow, no, m_model, i);
		    				  m_totA1 = 0;
		    				  m_totA2 = 0;
		    				  m_totRegular = 0;
		    				  m_totHoliday = 0;
		    				  m_getMergeRow = 0;		    				  
		    			  }
	    		  }
	    	  }
	    	  getAddModel(m_getMergeRow, no, m_model, i);
	    	  if (i==(sheetLength-1)){
    			  m_getMergeRow = 2;	    		  	  
    			  getAddModel(m_getMergeRow, no, m_model, i);
    		  }
	      }     
      }
      JRTableModelDataSource ds = new JRTableModelDataSource(m_model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);

      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Perzonal Utilization");
        view.setVisible(true);
      }

    }
    catch(Exception ex){
      ex.printStackTrace();
    }
  }

private void getAddModel(int getMergeRow, int no, DefaultTableModel m_model, int i) {
	String qualification = "";
		String m_no = "";
		String area = "";
		String A1 = "";
		String A2;
		qualification = m_employeeTimesheet[i].getQualification();
		  area = m_employeeTimesheet[i].getAreacode().trim();
		  no = no + 1;
		  m_no = String.valueOf(no);
		  
		  if (m_jobTitle==null){
			  m_jobTitle = " ";
		  }
		  if (qualification==null){
			  qualification = " ";
		  }   	
		  
		  A1 = " ";
		  A2 = " ";
		  if (area.equals("A1")){
			  A1 =  String.valueOf(m_employeeTimesheet[i].getDays());
			  if (m_getMergeRow!=2){
				  m_totA1 +=m_employeeTimesheet[i].getDays();
			  }
		  }else{ 
			  A2 =  String.valueOf(m_employeeTimesheet[i].getDays());
			  if (m_getMergeRow!=2){
				  m_totA2 +=m_employeeTimesheet[i].getDays();
			  }
		  }
		  
		  if (i>0)
			  if (m_tempEmpId[i].equals(m_tempEmpId[i-1])){
				  m_idEmployee = " ";    	
				  m_empName  = " ";    			
				  no = no-1; 
				  m_no = " ";
				  m_jobTitle = " ";
			  }
		  if (m_getMergeRow!=2){
			  m_totRegular +=m_employeeTimesheet[i].getReguler();
			  m_totHoliday +=m_employeeTimesheet[i].getHoliday();
		  }
		 m_model.addRow(new Object[]{m_unitDesc,m_dept,m_act,m_tglChar,m_no,m_idEmployee,m_empName,
				  m_jobTitle,qualification,m_employeeTimesheet[i].getWorkdescription(),
				  m_employeeTimesheet[i].getClient(),
				  m_employeeTimesheet[i].getIpcNo(),A1,A2,
				  String.valueOf(m_employeeTimesheet[i].getReguler()),
				  String.valueOf(m_employeeTimesheet[i].getHoliday()),
				  String.valueOf(m_totA1),String.valueOf(m_totA2),
				  String.valueOf(m_totRegular),String.valueOf(m_totHoliday),
				  new Integer(getMergeRow)
			  });
	//return no;
}

/*private void addRow(int no,int i) {
	
}*/

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
      model.addColumn("Reguler"); 
      model.addColumn("Holiday");
      model.addColumn("totalA1");
      model.addColumn("totalA2");
      model.addColumn("totHoliday");
      model.addColumn("totReguler");
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
