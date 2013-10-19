/*package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class TesExpenseSheet extends TestCase {
	public void testInsert() throws Exception
	{
		String host = "172.18.9.1";
		String dbname = "sampurna";
		String username = "sampurnauser";
		String password = "calamari";
		Class.forName ("com.sap.dbtech.jdbc.DriverSapDB");
		String url = "jdbc:sapdb://"
			+ host + "/"
			+ dbname;
		
		Connection connection = DriverManager.getConnection (url,
				username, password);
		
		GenericMapper m = MasterMap.obtainMapperFor(ExpenseSheet.class);	
		MasterMap.setPrimaryConnection(connection); 		
		ExpenseSheet o =
			new ExpenseSheet();		
		//o.setIndex(56);
		Employee employee= (Employee) MasterMap.queryForEntity(Employee.class,new Long(5),connection);		
		ProjectData Project= (ProjectData) MasterMap.queryForEntity(ProjectData.class,new Long(2),connection);
		PmtCAIOUProject pmtiou = (PmtCAIOUProject) MasterMap.queryForEntity(PmtCAIOUProject.class,new Long(5),connection);
		PmtCAProject pmtca = (PmtCAProject) MasterMap.queryForEntity(PmtCAProject.class,new Long(5),connection);
		o.setEmpOriginator(employee);
		o.setProject(Project);
		o.setPmtCaIouProjectSettled(pmtiouse);
		o.setPmtCaProject(pmtca);
		m.setActiveConn(connection);		
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		ExpenseSheet obaru = (ExpenseSheet) list.get(2);		
		assertEquals("project berbeda","Jasa Inspeksi, verifikasi",obaru.getProject().getWorkDescription());
		assertEquals("Employee berbeda","Denpasar",obaru.getEmpOriginator().getBirthPlace());
		assertEquals("PmtCAIOUProject berbeda","desc2",obaru.getPmtCaIouProject().getDescription());
		assertEquals("PmtCAIOUProject berbeda","no",obaru.getPmtCaProject().getReferenceNo());
		//connection.commit();
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
*/