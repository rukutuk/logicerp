package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesPayrollPmtSlryHoPay extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PayrollPmtSlryHoPay.class);	
		MasterMap.setPrimaryConnection(connection);
		PayrollPmtSlryHoPay o =
			new PayrollPmtSlryHoPay();
		
		PayrollPmtSlryHo payrollPmtSlryHo= (PayrollPmtSlryHo) MasterMap.queryForEntity(PayrollPmtSlryHo.class,new Long(1),connection);
		o.setPayrollPmtSlryHo(payrollPmtSlryHo);	
		String desc = payrollPmtSlryHo.getDescription();
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PayrollPmtSlryHoPay obaru = (PayrollPmtSlryHoPay) list.get(0);		
		assertEquals("PayrollPmtSlryHo berbeda",desc,obaru.getPayrollPmtSlryHo().getDescription());
		
		connection.rollback(); // ga jadi dink	
		connection.close();
	}
}
