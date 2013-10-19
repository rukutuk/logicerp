package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPmtCAIOUProjectSettled extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtCAIOUProjectSettled o =
			new PmtCAIOUProjectSettled();
		
		o.setRemarks("dodol nanas");
		o.setDescription("dodol pepaya");
		o.setAmount(10000.00);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
				
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtCAIOUProjectSettled obaru = (PmtCAIOUProjectSettled) list.get(0);
		assertEquals("description berbeda","dodol pepaya",obaru.getDescription());
		assertEquals("remark berbeda","dodol nanas",obaru.getRemarks());
	//	connection.rollback(); // ga jadi dink	
		connection.commit();
		connection.close();
	}
}
