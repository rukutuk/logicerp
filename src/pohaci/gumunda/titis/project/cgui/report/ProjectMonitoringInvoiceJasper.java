package pohaci.gumunda.titis.project.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectData;




public class ProjectMonitoringInvoiceJasper {
	private SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
	private DecimalFormat m_desimalFormat =  new DecimalFormat("#,##0.00");
	
	public ProjectMonitoringInvoiceJasper(Connection conn, ProjectData project,boolean cekExcel){
		try{
			String filename = "RptProjectMonitoringInvoice.jrxml";
			if(filename.equals(""))
				return;
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("No");
		    model.addColumn("InvoiceNo");
		    model.addColumn("InvoiceDate");
		    model.addColumn("Customer");						
		    model.addColumn("InvoicingAddress");
		    model.addColumn("Description");
		    model.addColumn("TotalAmount");
		    model.addColumn("PPN");
		    model.addColumn("Status");
		    
		    GenericMapper mapper = MasterMap.obtainMapperFor(SalesInvoice.class);
			mapper.setActiveConn(conn);
			
			if(cekExcel)
				m_desimalFormat =  new DecimalFormat("###.00");
			
			/*String query = IDBConstants.ATTR_CUSTOMER+ " = (select "+ IDBConstants.ATTR_CUSTOMER +"  from " +
				pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_DATA+ " where " + IDBConstants.ATTR_CODE + " = ' " + project.getCode() +"')";
			*/String query = "customer = (select customer  from projectdata where code = '"+ project.getCode() +"')";
			System.err.println(query);
			List list = mapper.doSelectWhere(query);
			
			Iterator iter = list.iterator();
			int i=1;
			while(iter.hasNext()){
				SalesInvoice inv = (SalesInvoice) iter.next();				
				model.addRow(new Object[]{String.valueOf(i++),inv.getReferenceNo(),m_dateFormat.format(inv.getTransactionDate()),
					inv.getCustomer().getName(),inv.getCustomer().getAddress(),inv.getDescription(), 
					m_desimalFormat.format(inv.getSalesAmount()),m_desimalFormat.format(inv.getVatAmount()), getStatus(inv.getStatus())});
			}

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
		catch(Exception e){
			e.printStackTrace();
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
	  
	  private String getStatus(long status){
		  String  strStatus = "";
		  if (status==0)
			  strStatus = "Not Submitted";
		  else if (status==1 || status==2)
			  strStatus = "Submitted";
		  else 
			  strStatus = "Posted";
		  return strStatus;
	  }
}
