package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import junit.framework.TestCase;

public class TesMemJournalNonStrd extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(MemJournalNonStrd.class);	
		MasterMap.setPrimaryConnection(connection);
		MemJournalNonStrd o =
			new MemJournalNonStrd();		
		
		Unit Unit= (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		Journal Journal= (Journal) MasterMap.queryForEntity(Journal.class,new Long(1),connection);
		Organization department= (Organization) MasterMap.queryForEntity(Organization.class,new Long(1),connection);
		Employee Employee= (Employee) MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		o.setJournal(Journal);		
		o.setUnit(Unit);
		o.setDepartment(department);
		o.setEmpOriginator(Employee);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		MemJournalNonStrd obaru = (MemJournalNonStrd) list.get(2);		
		assertEquals("Jurnal berbeda","Beginning Journal",obaru.getJournal().getName());
		assertEquals("Unit berbeda","01",obaru.getUnit().getCode());
		assertEquals("Department berbeda","Manajemen",obaru.getDepartment().getName());
		assertEquals("Employee berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
