package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class TesPmtLoan extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtLoan.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtLoan o =
			new PmtLoan();
		
		o.setIndex(8);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		CashAccount CashAccount = (CashAccount) MasterMap.queryForEntity(CashAccount.class,new Long(1),connection);
		BankAccount BankAccount = (BankAccount) MasterMap.queryForEntity(BankAccount.class,new Long(3),connection);		
		Employee Employee = (Employee) MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		o.setCashAccount(CashAccount);
		o.setBankAccount(BankAccount);
		o.setEmpOriginator(Employee);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtLoan obaru = (PmtLoan) list.get(7);
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("Cash Account berbeda","C01",obaru.getCashAccount().getCode());
		assertEquals("Bank Account berbeda","BNID1",obaru.getBankAccount().getCode());
		assertEquals("employee berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		connection.rollback(); // ga jadi dink	
		connection.commit();
		connection.close();
	}
}
