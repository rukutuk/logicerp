package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class TesSalesReceived extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(SalesReceived.class);	
		MasterMap.setPrimaryConnection(connection);
		SalesReceived o =
			new SalesReceived();
		
		o.setIndex(1);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		BankAccount bankaccount = (BankAccount) MasterMap.queryForEntity(BankAccount.class,new Long(3),connection);
		Employee employee = (Employee)MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		CashAccount cashAccount = (CashAccount)MasterMap.queryForEntity(CashAccount.class,new Long(1),connection);
		Unit Unit = (Unit)MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		o.setBankAccount(bankaccount);
		o.setEmpOriginator(employee);
		o.setCashAccount(cashAccount);
		o.setUnit(Unit);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		SalesReceived obaru = (SalesReceived) list.get(0);
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("bank account berbeda","BNID1",obaru.getBankAccount().getCode());
		assertEquals("employee berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		assertEquals("cashaccount berbeda","C01",obaru.getCashAccount().getCode());
		assertEquals("Unit berbeda","01",obaru.getUnit().getCode());
		connection.rollback(); // ga jadi dink	
	}

}
