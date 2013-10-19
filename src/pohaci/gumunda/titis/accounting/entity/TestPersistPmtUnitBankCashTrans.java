package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TestPersistPmtUnitBankCashTrans extends TestCase {
	public void testGetCurrency() throws Exception
	{
		System.out.println("testGetCurrency");
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
        Long id = new Long(1);
        Currency c = (Currency) MasterMap.queryForEntity(Currency.class,id,connection);
        System.out.println(c);
	}
	public void testInsert() throws Exception
	{
		System.out.println("testInsert");
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

		GenericMapper m = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
		//String fName = "XYZ";
		PmtUnitBankCashTrans o =
			new PmtUnitBankCashTrans();
		Long id =new Long(1);
		Currency c = (Currency) MasterMap.queryForEntity(Currency.class,id,connection);
        o.setAmount(123.45);
		o.setCurrency(c);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		o.setDateOriginator(new Date());
		o.setChequeNo("1244325-11-AX");
		m.doInsert(o);
		connection.commit();
		List list = m.doSelectAll();
		PmtUnitBankCashTrans obaru = (PmtUnitBankCashTrans) list.get(0);
		System.out.println(obaru);
		//assertEquals("amount berbeda",123.45,obaru.getAmount(),0.01);
		connection.rollback(); // ga jadi dink
		//connection.commit();
		
		System.out.println("berhasil");	
		}
	public void testSelectAll() throws Exception
	{
		System.out.println("testSelectAll");
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
        GenericMapper m = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
        m.setActiveConn(connection);
		List list = m.doSelectAll();
		Iterator iter = list.iterator();
		while (iter.hasNext())
		{
			Object o = iter.next();
			System.out.println(o);
		}
	}
}
