package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPmtOperationalCostDetail extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtOperationalCostDetail o =
			new PmtOperationalCostDetail();
		
		o.setAccValue(778);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
				
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtOperationalCostDetail obaru = (PmtOperationalCostDetail) list.get(0);
		System.out.println(obaru);
	//	connection.rollback(); // ga jadi dink	
		connection.commit();
		connection.close();
	}
}
