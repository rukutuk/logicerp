package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.framework.TestCase;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesAccount extends TestCase {
	public void testGetAccount() throws Exception
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
        Long id = new Long(2);
        GenericMapper m = MasterMap.obtainMapperFor(Account.class);
        m.setActiveConn(connection);
        Account c = (Account) m.doSelectByIndex(id);
        //Account c = (Account) MasterMap.queryForEntity(Account.class,id,connection);
        System.out.println(c);
	}
	/*
	public void testGetJournalStandardAccount() throws Exception{
		String host = "localhost";
		String dbname = "sampurna";
		String username = "sampurnauser";
		String password = "calamari";
		Class.forName ("com.sap.dbtech.jdbc.DriverSapDB");
        String url = "jdbc:sapdb://"
            + host + "/"
            + dbname;

        Connection connection = DriverManager.getConnection (url,
            username, password);
        Long id = new Long(1);
        GenericMapper m = MasterMap.obtainMapperFor(Account.class);
        m.setActiveConn(connection);
        JournalStandardAccount j = (JournalStandardAccount) m.doSelectByIndex(id);
        System.out.println(j.getAccount());
	}*/
}
