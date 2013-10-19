package pohaci.gumunda.titis.accounting.cgui.report;

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
import pohaci.gumunda.titis.accounting.cgui.PayrollPaychequeVerificationPanel;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
public class AccPayrollPaycheque {
	PayrollPaychequeVerificationPanel m_panel;  
	JasperPrint m_jasperPrint;
	
  public AccPayrollPaycheque(PayrollPaychequeVerificationPanel panel,PayrollComponent[] payrollComponents,EmployeePayrollSubmit submitObject,boolean cekExcel) {
			  
	this.m_panel = panel;
    try {
      String filename = "RptPayrollPaychequesAcc.jrxml";          
      if(filename.equals(""))
        return;      
      
      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      HashMap parameters = new HashMap();        
      DefaultTableModel model = new DefaultTableModel();
      tableHeader(model);
      panel.getEmployeePayroll(payrollComponents,submitObject,model,true);
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);      
      
      if(cekExcel){
        exportToExcel(jasperPrint);
      }else{
        PrintingViewer view = new PrintingViewer(jasperPrint);
        view.setTitle("Paycheques Summary");
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
		model.addColumn("acc_code");
		model.addColumn("acc_name");
		model.addColumn("A_GajiPokok");
		model.addColumn("B_GajiPokok");
		model.addColumn("A_Kes");
		model.addColumn("B_Kes");
		model.addColumn("A_Mgmt");
		model.addColumn("B_Mgmt");
		model.addColumn("A_Tek");
		model.addColumn("B_Tek");
		model.addColumn("A_Rmh");
		model.addColumn("B_Rmh");
		model.addColumn("A_THR");
		model.addColumn("B_THR");
		model.addColumn("A_Lap");
		model.addColumn("B_Lap");		
		model.addColumn("A_Insertif");
		model.addColumn("B_Insertif");
		model.addColumn("A_Bonus");
		model.addColumn("B_Bonus");
		model.addColumn("AsuransiPeg");
		model.addColumn("PiutKePerusahaan");
		model.addColumn("PiutKeYayasan");
		model.addColumn("temp");
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
