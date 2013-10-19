package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import junit.framework.TestCase;

public class TesMemJournalStrd extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(MemJournalStrd.class);	
		MasterMap.setPrimaryConnection(connection);
		MemJournalStrd o =
			new MemJournalStrd();
		Unit Unit = (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		Organization department = (Organization) MasterMap.queryForEntity(Organization.class,new Long(1),connection);
		Employee Employee = (Employee) MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		ProjectData project= (ProjectData) MasterMap.queryForEntity(ProjectData.class,new Long(2),connection);
		JournalStandard Journal= (JournalStandard) MasterMap.queryForEntity(JournalStandard.class,new Long(3),connection);
		o.setIndex(7);
		o.setRemarks("latih lagi");
		o.setDescription("latih");			
		o.setUnit(Unit);
		o.setDepartment(department);
		o.setEmpOriginator(Employee);
		o.setProject(project);
		o.setTransactionCode(Journal);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		MemJournalStrd obaru = (MemJournalStrd) list.get(2);
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("unit berbeda","01",obaru.getUnit().getCode());		
		assertEquals("department berbeda","Manajemen",obaru.getDepartment().getName());
		assertEquals("unit berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		assertEquals("project berbeda","Jasa Inspeksi, verifikasi",obaru.getProject().getWorkDescription());
		assertEquals("journal berbeda","S",obaru.getTransactionCode().getCode());
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
