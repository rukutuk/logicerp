package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import junit.framework.TestCase;

public class TesPayrollPmtSlryHo extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PayrollPmtSlryHo.class);	
		MasterMap.setPrimaryConnection(connection);
		PayrollPmtSlryHo o =
			new PayrollPmtSlryHo();
		
		o.setIndex(10);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		BankAccount BankAccount = (BankAccount) MasterMap.queryForEntity(BankAccount.class,new Long(3),connection);	
		CashAccount CashAccount = (CashAccount) MasterMap.queryForEntity(CashAccount.class,new Long(1),connection);
		Employee Employee = (Employee) MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		Unit Unit = (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		o.setBankAccount(BankAccount);
		o.setCashAccount(CashAccount);
		o.setEmpOriginator(Employee);
		o.setUnit(Unit);
		m.setActiveConn(connection);		
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PayrollPmtSlryHo obaru = (PayrollPmtSlryHo) list.get(2);
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("BankAccount berbeda","BNID1",obaru.getBankAccount().getCode());
		assertEquals("CashAccount berbeda","C01",obaru.getCashAccount().getCode());
		assertEquals("Employee berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		assertEquals("Unit berbeda","01",obaru.getUnit().getCode());
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
