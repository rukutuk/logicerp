package pohaci.gumunda.titis.application.db;

import java.sql.Connection;
import java.sql.DriverManager;

import pohaci.gumunda.titis.project.cgui.Customer;
import junit.framework.TestCase;

public class TesMapperCustomer extends TestCase {
	
	public void testDDL()
	{
		DataMapping mpg = 
			MasterMap.obtainMapperFor(Customer.class)
				.mapping;
		System.out.println(mpg.createTableSql());
	}
	public void testInsert() throws Exception
	{
		String host = "172.18.9.1"; // initialisasi server
		String dbname = "sampurna"; // initialisasi database
		String username = "sampurnauser"; // initialisasi user
		String password = "calamari"; // initialisasi password
		Class.forName ("com.sap.dbtech.jdbc.DriverSapDB"); // initialisasi 
        String url = "jdbc:sapdb://" // inisialisasi jdbc
            + host + "/"
            + dbname;

        Connection connection = DriverManager.getConnection (url,
            username, password); // open connection

		GenericMapper m = MasterMap.obtainMapperFor(Customer.class);
		String fName = "XYZ";
		Customer o = new Customer(0,"AB","EFG","HIJ", fName, 40135, fName, fName, fName, fName, fName, fName, fName, fName);
		m.setActiveConn(connection); // 
		m.doInsert(o);
		connection.rollback(); // ga jadi dink
		//connection.commit();
	}
	public void testSelectByIndex() throws Exception
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

		GenericMapper m = MasterMap.obtainMapperFor(Customer.class);
		m.setActiveConn(connection);
		Object obj = m.doSelectByIndex(new Long(32));
		System.out.println(obj.toString());
	}
}
