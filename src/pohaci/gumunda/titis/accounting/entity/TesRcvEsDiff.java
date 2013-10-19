package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesRcvEsDiff extends TestCase {

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

        	
		GenericMapper m = MasterMap.obtainMapperFor(RcvESDiff.class);	
		MasterMap.setPrimaryConnection(connection);
		RcvESDiff o =
			new RcvESDiff();
		
		o.setIndex(2);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		RcvESDiff obaru = (RcvESDiff) list.get(1);
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("description berbeda","latih",obaru.getDescription());
		
		connection.rollback(); // ga jadi dink
		connection.close();
		//connection.commit();
	}
	
	public void testSubmit() throws Exception
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

		GenericMapper m = MasterMap.obtainMapperFor(RcvESDiff.class);
		m.setActiveConn(connection);
		RcvESDiff o = new RcvESDiff();
		
		//Currency curr = (Currency) MasterMap.queryForEntity(Currency.class, new Long(1), connection);
		
		CashAccount cash = (CashAccount) MasterMap.queryForEntity(CashAccount.class, new Long(1), connection);
		BankAccount bank = (BankAccount) MasterMap.queryForEntity(BankAccount.class, new Long(3), connection);
		
		o.setAmount(1000000.00);
		o.setCashAccount(cash);
		o.setBankAccount(bank);
		//o.setCurrency(curr);
		
		o.setTransactionDate(new Date());
		o.submit(-1, connection);
	}
}
