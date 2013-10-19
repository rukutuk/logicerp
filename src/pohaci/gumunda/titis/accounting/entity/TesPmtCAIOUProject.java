package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import junit.framework.TestCase;

public class TesPmtCAIOUProject extends TestCase {
	public void testInsert() throws Exception
	{
		System.out.println("** testInsert()");
		String host = "172.18.9.1";
		String dbname = "sampurna";
		String username = "sampurnauser";
		String password = "calamari";
		Class.forName ("com.sap.dbtech.jdbc.DriverSapDB");
        String url = "jdbc:sapdb://"
            + host + "/"
            + dbname;

        System.out.println("*** preparing connection begin");
        Connection connection = DriverManager.getConnection (url,
            username, password);
        System.out.println("*** connection is ready");

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtCAIOUProject.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtCAIOUProject o =
			new PmtCAIOUProject();
		
		o.setIndex(13);
		Employee employee= (Employee) MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		Unit Unit= (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		Organization department= (Organization) MasterMap.queryForEntity(Organization.class,new Long(1),connection);
		ProjectData project = (ProjectData) MasterMap.queryForEntity(ProjectData.class,new Long(2),connection);
		o.setUnit(Unit);
		o.setEmpOriginator(employee);
		o.setProject(project);
		o.setDepartment(department);
		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtCAIOUProject obaru = (PmtCAIOUProject) list.get(8);
		assertEquals("employee berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		assertEquals("Project berbeda","Jasa Inspeksi, verifikasi",obaru.getProject().getWorkDescription());
		assertEquals("unit berbeda","01",obaru.getUnit().getCode());
		assertEquals("Organization berbeda","Manajemen",obaru.getDepartment().getDescription());
		connection.rollback(); // ga jadi dink	
		//connection.commit();
		connection.close();
	}
}
