package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesCurrency extends TestCase {
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

        System.out.println("*** preparing connection begin");
        Connection connection = DriverManager.getConnection (url,
            username, password);
        System.out.println("*** connection is ready");


		GenericMapper m = MasterMap.obtainMapperFor(Currency.class);
		MasterMap.setPrimaryConnection(connection);
		Currency o =
			new Currency(3,"Rp","IDR","Rupiah Indonesia", "Rupiah", "Sen", MoneyTalk.LANGUAGE_INDONESIAN);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);

		m.doInsert(o);
		List list = m.doSelectAll();
		Currency obaru = (Currency) list.get(0);
		assertEquals("Code berbeda","IDR",obaru.getCode());
		connection.rollback(); // ga jadi dink
		//connection.commit();
		connection.close();
	}
}
