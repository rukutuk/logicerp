package pohaci.gumunda.titis.accounting.entity;

import junit.framework.TestCase;

public class tesProjectData extends TestCase {
	public void testInsert() throws Exception{
		
		/*String host = "172.18.9.1";
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtOperationalCost.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtOperationalCost o =
			new PmtOperationalCost();
		
		o.setRemarks("dodol nanas");
		o.setDescription("dodol pepaya");
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
				
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtOperationalCost obaru = (PmtOperationalCost) list.get(0);
		assertEquals("description berbeda","dodol pepaya",obaru.getDescription());
		assertEquals("remark berbeda","dodol nanas",obaru.getRemarks());
	//	connection.rollback(); // ga jadi dink	
		connection.commit();
		connection.close();*/
	}
}
