package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesRcvEmpReceivable extends TestCase {
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

        	
		GenericMapper mapper = MasterMap.obtainMapperFor(RcvEmpReceivable.class);	
		MasterMap.setPrimaryConnection(connection);
		RcvEmpReceivable objectTest = new RcvEmpReceivable();
		
		objectTest.setIndex(3);
		objectTest.setRemarks("dodol nangka");
		objectTest.setDescription("dodol semangka");
		
		mapper.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		mapper.doInsert(objectTest);		
		List list = mapper.doSelectAll();
		RcvEmpReceivable obaru = (RcvEmpReceivable) list.get(3);
		System.out.println(">>>>>> " +obaru+" ,descr = "+obaru.getReferenceNo());
		assertEquals("description berbeda","dodol semangka",obaru.getDescription());
		assertEquals("remark berbeda","dodol nangka",obaru.getRemarks());
		connection.rollback(); // ga jadi dink	
		//connection.commit();
		connection.close();
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
		GenericMapper m = MasterMap.obtainMapperFor(RcvEmpReceivable.class);
		m.setActiveConn(connection);
		
		MasterMap.setPrimaryConnection(connection);
		
		RcvEmpReceivable o = new RcvEmpReceivable();
		Currency curr = (Currency) MasterMap.queryForEntity(Currency.class, new Long(1), connection);
		
		CashAccount cash = (CashAccount) MasterMap.queryForEntity(CashAccount.class, new Long(1), connection);
		BankAccount bank = (BankAccount) MasterMap.queryForEntity(BankAccount.class, new Long(3), connection);
		
		o.setAmount(1200000.00);
		o.setReceiveTo("BANK");
		o.setCashAccount(cash);
		o.setBankAccount(bank);
		o.setCurrency(curr);
		
		o.setTransactionDate(new Date());
		
		o.submit(-1, connection);
	}
}
