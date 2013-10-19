package pohaci.gumunda.titis.hrm.cgui.report;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.sql.Connection;
import java.text.DecimalFormat;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PaychequesValueRpt;

public class RptWagesJasper {
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	Employee m_employee;
	JRTableModelDataSource m_ds;
	String m_jasperReport;
	DecimalFormat m_formatDesimal;
	int m_total = 0;
	String m_bln_thn;
	public RptWagesJasper(Connection conn,long sessionid,Employee employee,String bln_thn) {
		try {
			m_bln_thn = bln_thn;
			m_sessionid = sessionid;
			m_conn = conn;
			m_employee = employee;
			m_formatDesimal = new DecimalFormat("#,##0.00");
//			String filename = "RptWages.jrxml";
//			if(filename.equals(""))
//				return;
//
//			m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			m_jasperReport = ReportUtils.compileReport("RptWages");
			DefaultTableModel model = new DefaultTableModel();
			tableHeader(model);
			// superlabel tanyakan ke irwan
			// i: terpaksa deh bikin ga jelas
			// untuk kelas ini, superlabel-nya yang pertama muncul alias index 0
			long superlabel = getSuperlabel(0);
			if (employee!=null){
				getNonEmpty(model,superlabel);
			}
			else
				getEmpty(model,superlabel);

			m_ds = new JRTableModelDataSource(model);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	// i create ini
	private long getSuperlabel(int indexPosition) {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		long index = -1;
		try {
			index = logic.getPaychequeSuperlabelAtIndex(m_sessionid, "", indexPosition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}

	public JRTableModelDataSource getDataSource(){
		return m_ds;
	}

	public String getJasperReport(){
		return m_jasperReport;
	}
	private void getEmpty(DefaultTableModel model,long superlabel) {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		PaychequesRpt[] WagesRpt = null;
		try {
			WagesRpt = logic.getPaychequesRpt(m_sessionid,"",superlabel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (WagesRpt.length>0){
			m_total = 0;
			for (int i=0;i<WagesRpt.length;i++){
				if (i==0){
					model.addRow(new Object[]{
							WagesRpt[i].getSupername(),""});
					model.addRow(new Object[]{
							WagesRpt[i].getSubname(),""});
				}else{
					model.addRow(new Object[]{
							WagesRpt[i].getSubname(),""});
				}
			}
		}
	}

	private void getNonEmpty(DefaultTableModel model,long superlabel) {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		PaychequesRpt[] WagesRpt = null;
		try {
			WagesRpt = logic.getPaychequesRpt(m_sessionid,"",superlabel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (WagesRpt.length>0){
			m_total = 0;
			for (int i=0;i<WagesRpt.length;i++){
				long value = 0;
				PaychequesValueRpt[] PaychequesValueRpt = null;
				try {
					PaychequesValueRpt = logic.getPaychequesValueRpt(m_sessionid,"",m_employee.getIndex(),superlabel,WagesRpt[i].getSublabel(),m_bln_thn);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (PaychequesValueRpt!=null){
					for (int j = 0 ;j<PaychequesValueRpt.length;j++){
						if (PaychequesValueRpt[j].getValuesubmit()>0){
							value +=PaychequesValueRpt[j].getValuesubmit();
						}
					}
				}
				if (WagesRpt[i].getType()==0)
					m_total +=value;
				else
					m_total -= value;

				if (i==0){
					model.addRow(new Object[]{
							WagesRpt[i].getSupername(),""});
					model.addRow(new Object[]{
							WagesRpt[i].getSubname(),m_formatDesimal.format(value)});
				}else{
					model.addRow(new Object[]{
							WagesRpt[i].getSubname(),m_formatDesimal.format(value)});
				}
			}
		}else{
			getEmpty(model,superlabel);
		}
	}

	public int getTotal(){
		return m_total;
	}

	private void tableHeader(DefaultTableModel model) {
		model.addColumn("wagesfield");
		model.addColumn("temp");
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