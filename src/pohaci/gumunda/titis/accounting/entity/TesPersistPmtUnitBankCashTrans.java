package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesPersistPmtUnitBankCashTrans extends TestCase {
	public void testGetCurrency() throws Exception
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
        Long id = new Long(1);
        Currency c = (Currency) MasterMap.queryForEntity(Currency.class,id,connection);
        System.out.println(c);
        connection.close();
	}
	private void removeObjects(GenericMapper m)
	{
		List list = m.doSelectAll();
		Iterator iter = list.iterator();
		while (iter.hasNext())
		{
			Object o = iter.next();
			m.doDelete(o);
		}
	}
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

        Connection connection = DriverManager.getConnection (url,
            username, password);

		GenericMapper m = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
		m.setActiveConn(connection);
		removeObjects(m);
		connection.commit();
		PmtUnitBankCashTrans o =
			new PmtUnitBankCashTrans();
		Long id =new Long(1);
		Currency c = (Currency) MasterMap.queryForEntity(Currency.class,id,connection);
		//BankAccount bacct = 
        o.setAmount(123.45);
		o.setCurrency(c);
		connection.setAutoCommit(false);
		o.setDateOriginator(new Date());
		o.setChequeNo("1244325-11-AX");
		//o.setBankAccount(bacct);
		m.doInsert(o);
		//connection.commit();
		List list = m.doSelectAll();
		PmtUnitBankCashTrans obaru = (PmtUnitBankCashTrans) list.get(0);
		m.doDelete(obaru);
		if (list.size()>1)
			m.doDelete(list.get(1));
		System.err.println(list.size());
		connection.commit();
		assertEquals("amount berbeda",123.45,obaru.getAmount(),0.01);
		connection.close();
		//connection.rollback(); // ga jadi dink
		
	}
	public void testSelectAll() throws Exception
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
        GenericMapper m = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
        m.setActiveConn(connection);
		List list = m.doSelectAll();
		Iterator iter = list.iterator();
		while (iter.hasNext())
		{
			Object o = iter.next();
			System.out.println(o);
		}
		connection.close();
	}
}
