package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesMemJournalStrdDet extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(MemJournalStrdDet.class);	
		MasterMap.setPrimaryConnection(connection);
		MemJournalStrdDet o =
			new MemJournalStrdDet();
		
		
		Account account = (Account) MasterMap.queryForEntity(Account.class,new Long(1),connection);	
		o.setAccount(account);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		MemJournalStrdDet obaru = (MemJournalStrdDet) list.get(0);
		assertEquals("account berbeda","AKTIVA",obaru.getAccount().getName());
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
