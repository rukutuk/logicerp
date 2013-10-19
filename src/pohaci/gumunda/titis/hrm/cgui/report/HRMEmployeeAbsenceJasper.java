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
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeAbsence;
import pohaci.gumunda.titis.hrm.cgui.EmployeeLeave;
import pohaci.gumunda.titis.hrm.cgui.EmployeeOvertime;
import pohaci.gumunda.titis.hrm.cgui.EmployeePermition;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class HRMEmployeeAbsenceJasper {
  //private Connection m_conn = null;
  //private ProjectData[] m_project = null;
  //private Unit m_unit = null;
  //private Customer m_customer = null;
  //private SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  JRViewer m_jrv;
  Connection m_conn;
  long m_sessionid;
  String m_tglBetween;
  public HRMEmployeeAbsenceJasper(Connection conn,long sessionid,Employee[] employee,String tglBetween) {
	  this.m_conn = conn;
	  this.m_sessionid = sessionid;
	  this.m_tglBetween = tglBetween;
    try {
      String filename = "RptEmployeePresence.jrxml";
      if(filename.equals(""))
        return;

      JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
      Map parameters = new HashMap();
      DefaultTableModel model = new DefaultTableModel();
      tableHeader(model);
      if (employee==null){
    	  getEmptyValue(model); // ambil data
      }else{
    	  getNotEmptyValue(employee,model); // ambil data
      }
      JRTableModelDataSource ds = new JRTableModelDataSource(model);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, ds);
      m_jrv = new JRViewer(jasperPrint);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
  }


	private void getNotEmptyValue(Employee[] employee, DefaultTableModel model) {
		String index = "";
		if (employee.length==0){
			getEmptyValue(model);
		}else{
			HRMBusinessLogic logic = new  HRMBusinessLogic(m_conn);
			for (int i=0;i<employee.length;i++){
				if (i==(employee.length-1))
					index = index + employee[i].getIndex();
				else
					index = index + employee[i].getIndex() + ",";
			}

			Employee_n[] employee_n = null;
			try {
				employee_n = logic.getEmployeeBy_Criteria(m_sessionid,IDBConstants.MODUL_MASTER_DATA,queryJobtitle(index));

			} catch (Exception e) {
				e.printStackTrace();
			}
			index = "";
			for (int i=0;i<employee_n.length;i++){
				if (i==(employee_n.length-1))
					index = index + employee_n[i].getAutoindex();
				else
					index = index + employee_n[i].getAutoindex() + ",";
			}
			System.err.println(index);

			EmployeeLeave[] employeeLeaves = null;
			EmployeeAbsence[] employeeAbsence = null;
			EmployeePermition[] employeePermitions = null;
			//EmployeeWorkTime[] employeeWorkTime = null;
			EmployeeOvertime[] employeeOvertimes = null;
			try {
				employeeAbsence = logic.getEmployeeAbsence(m_sessionid,IDBConstants.MODUL_MASTER_DATA,queryAbsence(index));
				employeeLeaves = logic.getEmployeeLeave(m_sessionid,IDBConstants.MODUL_MASTER_DATA,queryLeave(index));
				employeePermitions = logic.getEmployeePermition(m_sessionid,IDBConstants.MODUL_MASTER_DATA,queryPermition(index));
				employeeOvertimes = logic.getEmployeeOvertime(m_sessionid, IDBConstants.MODUL_MASTER_DATA, queryOvertime(index));

			} catch (Exception e) {
				e.printStackTrace();
			}

		/*	System.err.println("panjang employee :" + employee_n.length);
			System.err.println("panjang leave :" + employeeAbsence.length);
			System.err.println("panjang absence :" + employeeAbsence.length);
			System.err.println("panjang permitions :" + employeePermitions.length);*/
			for (int i=0;i<employee_n.length;i++){
				// deprecated hahahaaa...
				/*int jumTimeIn = 0;
				int jumTimeOff = 0;
				try {
					employeeWorkTime = logic.getEmployeeWorkTime(m_sessionid,IDBConstants.MODUL_MASTER_DATA,queryWorkTime(employee_n[i].getAutoindex()));
				} catch (Exception e) {
					//
					e.printStackTrace();
				}
				for (int j=0;j<employeeWorkTime.length;j++){
					int days = employeeWorkTime[j].getToday();
					if ((days==1) || (days==2) ||(days==3) ||(days==4) ||(days==5) ){
						jumTimeIn += employeeWorkTime[j].getOverTime();
					}else{
						jumTimeOff += employeeWorkTime[j].getOverTime();
					}
				}*/
				String name ="";
				name = employee_n[i].getFirstname() + " " + employee_n[i].getMidlename() +
				" " + employee_n[i].getLastname();
				System.err.println(name);
				model.addRow(new Object[]{"",
						String.valueOf(i+1),
						employee_n[i].getEmployeeno(),
						name,
						employee_n[i].getJobtitle(),
						String.valueOf(employeeAbsence[i].getPresent()),
						String.valueOf(employeeAbsence[i].getPresentNotLate()),
						String.valueOf(employeeAbsence[i].getPresentLate()),
						String.valueOf(employeeAbsence[i].getAbsent()),
						String.valueOf(employeeAbsence[i].getFieldVisit()),
						String.valueOf(employeeAbsence[i].getOther()),
						String.valueOf(employeeOvertimes[i].getWorkingDayOvertime()),
						String.valueOf(employeeOvertimes[i].getNonWorkingDayOvertime()),
						String.valueOf(employeeLeaves[i].getLv_1()),
						String.valueOf(employeeLeaves[i].getLv_2()),
						String.valueOf(employeeLeaves[i].getLv_3()),
						String.valueOf(employeeLeaves[i].getLv_4()),
						String.valueOf(employeePermitions[i].getPM_1()),
						String.valueOf(employeePermitions[i].getPM_2()),
						String.valueOf(employeePermitions[i].getPM_3()),
						String.valueOf(employeePermitions[i].getPM_4()),
						String.valueOf(employeePermitions[i].getPM_5()),
						String.valueOf(employeePermitions[i].getPM_6()),
						String.valueOf(employeePermitions[i].getPM_99()),
				""});
			}
		}
	}

	public int getSumWorktime(int days){
		int sumday = 0;
		if ( (days ==1) || (days==2) || (days==3) || (days==4) || (days==2)){
		}
		return sumday;

	}


	public String queryJobtitle(String index){
		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,jobt.name jobtitle " +
				"from " +
				"employee emp," +
				"(select a.autoindex ,max(b.tmt) tmt from employeeemployment b, employee a where b.employee=a.autoindex  group by a.autoindex) b," +
				"employeeemployment c," +
				"jobtitle jobt " +
				"where " +
				"b.autoindex=c.employee and b.tmt=c.tmt " +
				"and emp.autoindex=b.autoindex and c.jobtitle=jobt.autoindex " +
				"and emp.autoindex in (" + index + ") order by b.autoindex ";
		System.err.println(query);
		return query;
	}

	public String queryAbsence(String index){
		String query= "select emp.autoindex, (a.present_not_late+b.present_late) present,a.present_not_late, b.present_late, c.absent, d.field_visit, e.other from " +
				"(select autoindex from employee) emp, " +
				"(select employeeid, count(absstatus) present_not_late from (select * from empabsoffworktime where workingdate " + m_tglBetween + " and absstatus=0) group by employeeid) a," +
				"(select employeeid, count(absstatus) present_late from (select * from empabsoffworktime where workingdate " + m_tglBetween + " and absstatus=1) group by employeeid) b," +
				"(select employeeid, count(absstatus) absent from (select * from empabsoffworktime where workingdate " + m_tglBetween + " and absstatus=2) group by employeeid) c," +
				"(select employeeid, count(absstatus) field_visit from (select * from empabsoffworktime where workingdate " + m_tglBetween + " and absstatus=3) group by employeeid) d," +
				"(select employeeid, count(absstatus) other from (select * from empabsoffworktime where workingdate " + m_tglBetween + " and absstatus=4) group by employeeid) e " +
				"where " +
				"emp.autoindex = a.employeeid(+) " +
				"and emp.autoindex = b.employeeid(+) " +
				"and emp.autoindex = c.employeeid(+) " +
				"and emp.autoindex = d.employeeid(+) " +
				"and emp.autoindex = e.employeeid(+) " +
				"and emp.autoindex in (" + index + ") order by emp.autoindex";
		System.err.println("query absence :" + query);
		return query;
	}

	public String queryWorkTime(long index){
		String query = "select employeeid,workingdate,overtime,dayofweek(workingdate) today " +
				"from empabsoffworktime " +
				"where workingdate " + m_tglBetween +
				" and employeeid in (" + index + ") order by employeeid";
		return query;
	}

	public String queryOvertime(String index) {
		String query =
		"select emp.autoindex, ov1.workingday, ov2.nonworkingday from " +
		"employee emp, " +
		"(select o.employee, sum(o.overtime) workingday from overtime o, overtimemultiplier om where o.multiplier=om.autoindex and om.type=0 " +
		"group by o.employee, om.type) ov1, " +
		"(select o.employee, sum(o.overtime) nonworkingday from overtime o, overtimemultiplier om where o.multiplier=om.autoindex and om.type=1 " +
		"group by o.employee, om.type) ov2 " +
		"where " +
		"emp.autoindex = ov1.employee(+) " +
		"and " +
		"emp.autoindex = ov2.employee(+) " +
		"and " +
		"emp.autoindex in (" + index + ") " +
		"order by emp.autoindex";

		return query;
	}

	public String queryLeave(String index){
		String query = "select emp.autoindex, a.lv_1, b.lv_2, c.lv_3, d.lv_4 from " +
				"(select autoindex from employee) emp, " +
				"(select employee, sum(days) lv_1 from (select * from employeeleave where proposedate " + m_tglBetween + " and reason=1) group by employee) a, " +
				"(select employee, sum(days) lv_2 from (select * from employeeleave where proposedate " + m_tglBetween + " and reason=2) group by employee) b, " +
				"(select employee, sum(days) lv_3 from (select * from employeeleave where proposedate " + m_tglBetween + " and reason=3) group by employee) c, " +
				"(select employee, sum(days) lv_4 from (select * from employeeleave where proposedate " + m_tglBetween + " and reason=4) group by employee) d " +
				"where " +
				"emp.autoindex = a.employee(+) " +
				"and emp.autoindex = b.employee(+) " +
				"and emp.autoindex = c.employee(+) " +
				"and emp.autoindex = d.employee(+) " +
				"and emp.autoindex in (" + index + ") order by emp.autoindex";
		System.err.println("query leaves :" + query);
		return query;
	}


	public String queryPermition(String index){
		String query ="select emp.autoindex, a.pm_1, b.pm_2, c.pm_3, d.pm_4, e.pm_5, f.pm_6, g.pm_99 from " +
				"(select autoindex from employee) emp, " +
				"(select employee, sum(days) pm_1 from (select * from employepermition where proposedate " + m_tglBetween + " and reason=1) group by employee) a, " +
				"(select employee, sum(days) pm_2 from (select * from employepermition where proposedate " + m_tglBetween + " and reason=2) group by employee) b, " +
				"(select employee, sum(days) pm_3 from (select * from employepermition where proposedate " + m_tglBetween + " and reason=3) group by employee) c, " +
				"(select employee, sum(days) pm_4 from (select * from employepermition where proposedate " + m_tglBetween + " and reason=4) group by employee) d, " +
				"(select employee, sum(days) pm_5 from (select * from employepermition where proposedate " + m_tglBetween + " and reason=5) group by employee) e, " +
				"(select employee, sum(days) pm_6 from (select * from employepermition where proposedate " + m_tglBetween + " and reason=6) group by employee) f, " +
				"(select employee, sum(days) pm_99 from (select * from employepermition where proposedate " + m_tglBetween + " and reason=7) group by employee) g " +
				"where emp.autoindex = a.employee(+) " +
				"and emp.autoindex = b.employee(+) " +
				"and emp.autoindex = c.employee(+) " +
				"and emp.autoindex = d.employee(+) " +
				"and emp.autoindex = e.employee(+) " +
				"and emp.autoindex = f.employee(+) " +
				"and emp.autoindex = g.employee(+) " +
				"and emp.autoindex in (" + index + ") order by emp.autoindex";
		System.err.println("query permition :" + query);
		return query;
	}
	public void getEmptyValue(DefaultTableModel model) {
		model.addRow(new Object[]{"","","","","","","","","","","","","","","","","","","","","","","",""});
	}

	private void tableHeader(DefaultTableModel model) {
		model.addColumn("Tgl");
		model.addColumn("No");
		model.addColumn("EmpID");
		model.addColumn("Name");
		model.addColumn("JobTitle");
		model.addColumn("Present");
		model.addColumn("NotLate");
		model.addColumn("Late");
		model.addColumn("Absent");
		model.addColumn("FieldVisit");
		model.addColumn("Other");
		model.addColumn("WorkIn");
		model.addColumn("WorkOff");
		model.addColumn("lv_1");
		model.addColumn("lv_2");
		model.addColumn("lv_3");
		model.addColumn("lv_4");
		model.addColumn("pm_1");
		model.addColumn("pm_2");
		model.addColumn("pm_3");
		model.addColumn("pm_4");
		model.addColumn("pm_5");
		model.addColumn("pm_6");
		model.addColumn("pm_99");
	    model.addColumn("temp");
	}

	public JRViewer getPrintView(){
		return m_jrv;
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