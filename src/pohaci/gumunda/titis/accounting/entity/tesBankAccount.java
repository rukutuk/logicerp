package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class tesBankAccount extends TestCase {
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
		
		
		GenericMapper m = MasterMap.obtainMapperFor(BankAccount.class);	
		MasterMap.setPrimaryConnection(connection);
		BankAccount o =
			new BankAccount();
		o.setName("ini akun A");
		Unit Unit = (Unit)MasterMap.queryForEntity(Unit.class,new Long(1),connection);		
		o.setUnit(Unit);		
		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		BankAccount obaru = (BankAccount) list.get(0);		
		assertEquals("Unit berbeda","01",obaru.getUnit().getCode());
		assertEquals("Name salah","Bank Negara Indonesia",obaru.getName());
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
