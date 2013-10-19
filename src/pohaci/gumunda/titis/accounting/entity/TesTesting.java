package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesTesting extends TestCase{
	
	
	public void testInsert() throws Exception
	{
		System.out.println("** testInsert **");
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

		GenericMapper m = MasterMap.obtainMapperFor(Testing.class);	
		MasterMap.setPrimaryConnection(connection);
		Testing o = new Testing();
		
		o.setTest("cannizaro");
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		Iterator iter = list.iterator();
		while (iter.hasNext())
		{
			Testing ddo = (Testing) iter.next();
			System.out.println(ddo.getTest());
		}
		connection.commit(); // ga jadi dink		
	}
	
	public void testGetObject() throws Exception
	{
		System.out.println("** testGetObject **");
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
        Long id = new Long(5);
        Testing c = (Testing) MasterMap.queryForEntity(Testing.class,id,connection);
        System.out.println(c);
	}

}
