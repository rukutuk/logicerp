package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesRcvOthers extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(RcvOthers.class);	
		MasterMap.setPrimaryConnection(connection);
		RcvOthers o =
			new RcvOthers();
		
		o.setIndex(19);
		o.setRemarks("dodol nangka");
		o.setDescription("dodol semangka");
		Unit unit = (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		o.setUnit(unit);	
		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		RcvOthers obaru = (RcvOthers) list.get(17);
		assertEquals("description berbeda","dodol semangka",obaru.getDescription());
		assertEquals("remark berbeda","dodol nangka",obaru.getRemarks());
		connection.rollback(); // ga jadi dink	
		//connection.commit();
		connection.close();
	}
}
