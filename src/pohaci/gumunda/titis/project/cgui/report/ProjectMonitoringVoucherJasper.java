package pohaci.gumunda.titis.project.cgui.report;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.project.cgui.VoucherPanel.VoucherTableModel;

public class ProjectMonitoringVoucherJasper {
	
	
	public ProjectMonitoringVoucherJasper(Connection conn, VoucherTableModel model, boolean cekExcel){
		try{
			String filename = "RptProjectMonitoringVoucher.jrxml";
			if(filename.equals(""))
				return;
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			
			/*GenericMapper mapper = MasterMap.obtainMapperFor(PmtCAProject.class);
		  	mapper.setActiveConn(conn);
		  	
		  	String strSelect = IDBConstants.ATTR_PROJECT + " = (select " + IDBConstants.ATTR_AUTOINDEX + 
		  		" from " + " projectdata where " + IDBConstants.ATTR_CODE + " = '" + project.getCode() + "')";
		  		  	
		  	DefaultTableModel model = new DefaultTableModel();
		      model.addColumn("No");
		      model.addColumn("VoucherNo");
		      model.addColumn("VoucherDate");
		      model.addColumn("Description");
		      model.addColumn("Status");
		      model.addColumn("ApprovedBy");
		      model.addColumn("ApprovalDate");
		      model.addColumn("ApprovedAmount");
		  	model.setRowCount(0);
		  	
		  	System.err.println(strSelect);
		  	List list = mapper.doSelectWhere(strSelect);
		  		  		
		  	//PmtCAProject
		  	Iterator iter = list.iterator();
		  	int i=1;
		  	while(iter.hasNext()){
		  		PmtCAProject voucher = (PmtCAProject) iter.next();
		  		model.addRow(new Object[] {String.valueOf(i++), voucher.getReferenceNo(), m_dateFormat.format(voucher.getTransactionDate()),
		  				voucher.getDescription(), getStatus(voucher.getStatus()), voucher.getEmpApproved().getFirstName(), m_dateFormat.format(voucher.getDateApproved()),
		  				m_decimalFormat.format(voucher.getAmount())});
		  	}*/
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
		    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
		    		parameters, ds);

		    if(cekExcel){
		    	exportToExcel(jasperPrint);
		    }else{
		        PrintingViewer view = new PrintingViewer(jasperPrint);
		        view.setTitle("Project invoice");
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
