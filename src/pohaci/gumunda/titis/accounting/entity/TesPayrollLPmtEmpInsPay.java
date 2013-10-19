package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPayrollLPmtEmpInsPay extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PayrollPmtEmpInsPay.class);	
		MasterMap.setPrimaryConnection(connection);
		PayrollPmtEmpInsPay o =
			new PayrollPmtEmpInsPay();
		
		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);				
		m.doInsert(o);		
		List list = m.doSelectAll();
		PayrollPmtEmpInsPay obaru = (PayrollPmtEmpInsPay) list.get(0);
		System.out.println(obaru);
			
		
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
