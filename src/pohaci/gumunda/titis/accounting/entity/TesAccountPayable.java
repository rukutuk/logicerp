package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.Partner;
import junit.framework.TestCase;

public class TesAccountPayable extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(AccountPayable.class);	
		MasterMap.setPrimaryConnection(connection);
		AccountPayable o =
			new AccountPayable();		
		
		Account Account= (Account) MasterMap.queryForEntity(Account.class,new Long(1),connection);
		Partner Partner= (Partner) MasterMap.queryForEntity(Partner.class,new Long(5),connection);
		o.setAccount(Account);
		o.setPartner(Partner);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		AccountPayable obaru = (AccountPayable) list.get(0);
		assertEquals("account berbeda","AKTIVA",obaru.getAccount().getName());
		assertEquals("partner code berbeda","01",obaru.getPartner().getCode());
		
		connection.rollback(); // ga jadi dink	
	}
}
