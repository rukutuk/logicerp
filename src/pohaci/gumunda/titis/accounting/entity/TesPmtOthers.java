package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import junit.framework.TestCase;

public class TesPmtOthers extends TestCase {
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

        System.out.println("*** preparing connection begin");
        Connection connection = DriverManager.getConnection (url,
            username, password);
        System.out.println("*** connection is ready");

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtOthers.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtOthers o =
			new PmtOthers();
		
		
		CashAccount CashAccount = (CashAccount) MasterMap.queryForEntity(CashAccount.class,new Long(1),connection);
		BankAccount BankAccount = (BankAccount) MasterMap.queryForEntity(BankAccount.class,new Long(3),connection);	
		Unit Unit = (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		Organization Organization = (Organization) MasterMap.queryForEntity(Organization.class,new Long(1),connection);
		Employee Employee = (Employee) MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		o.setCashAccount(CashAccount);	
		o.setBankAccount(BankAccount);
		o.setUnit(Unit);
		o.setDepartment(Organization);
		o.setEmpOriginator(Employee);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtOthers obaru = (PmtOthers) list.get(0);		
		assertEquals("Cashaccount berbeda","C01",obaru.getCashAccount().getCode());
		assertEquals("BankAccount berbeda","BNID1",obaru.getBankAccount().getCode());
		assertEquals("Unit berbeda","01",obaru.getUnit().getCode());
		assertEquals("department berbeda","Manajemen",obaru.getDepartment().getName());
		assertEquals("employee berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		connection.rollback(); // ga jadi dink	
		//connection.commit();
		connection.close();
	}
}
