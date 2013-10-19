package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPayrollPmtEmpInsurance extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PayrollPmtEmpInsurance.class);	
		MasterMap.setPrimaryConnection(connection);
		PayrollPmtEmpInsurance o =
			new PayrollPmtEmpInsurance();
		
		o.setIndex(1);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		BankAccount BankAccount = (BankAccount) MasterMap.queryForEntity(BankAccount.class,new Long(3),connection);	
		Unit Unit = (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		o.setBankAccount(BankAccount);
		o.setUnit(Unit);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PayrollPmtEmpInsurance obaru = (PayrollPmtEmpInsurance) list.get(0);
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("supplier berbeda","BNID1",obaru.getBankAccount().getCode());
		assertEquals("Unit berbeda","01",obaru.getUnit().getCode());
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
