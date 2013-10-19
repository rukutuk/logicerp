package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPayrollPmtTax21HoPay extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PayrollPmtTax21HoPay.class);	
		MasterMap.setPrimaryConnection(connection);
		PayrollPmtTax21HoPay o =
			new PayrollPmtTax21HoPay();
		
		
		PayrollPmtTax21Ho PayrollPmtTax21Ho = (PayrollPmtTax21Ho) MasterMap.queryForEntity(PayrollPmtTax21Ho.class,new Long(1),connection);	
		o.setPayrollPmtTax21Ho(PayrollPmtTax21Ho);		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PayrollPmtTax21HoPay obaru = (PayrollPmtTax21HoPay) list.get(0);
		
		assertEquals("employee berbeda","Aku",obaru.getPayrollPmtTax21Ho().getDescription());
		connection.rollback(); // ga jadi dink	
		//connection.commit();
		connection.close();
	}
}
