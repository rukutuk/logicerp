package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPmtCAOthers extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtCAOthers.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtCAOthers o =
			new PmtCAOthers();
		
		o.setIndex(8);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtCAOthers obaru = (PmtCAOthers) list.get(1);
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("description berbeda","latih",obaru.getDescription());
		
		connection.rollback(); // ga jadi dink
		connection.close();
		//connection.commit();
	}

}
