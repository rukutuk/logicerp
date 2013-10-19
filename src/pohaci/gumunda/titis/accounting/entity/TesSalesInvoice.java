package pohaci.gumunda.titis.accounting.entity;

import java.sql.DriverManager;
import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import junit.framework.TestCase;

public class TesSalesInvoice extends TestCase {
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

		GenericMapper m = MasterMap.obtainMapperFor(SalesInvoice.class);	
		MasterMap.setPrimaryConnection(connection);
		SalesInvoice o =
			new SalesInvoice();
		
		o.setIndex(30);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		BankAccount bankaccount = (BankAccount) MasterMap.queryForEntity(BankAccount.class,new Long(3),connection);		
		CashAccount cashAccount = (CashAccount)MasterMap.queryForEntity(CashAccount.class,new Long(1),connection);
		Employee employee = (Employee)MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		Unit Unit = (Unit)MasterMap.queryForEntity(Unit.class,new Long(1),connection);		
		Customer Customer = (Customer)MasterMap.queryForEntity(Customer.class,new Long(2),connection);
		ProjectData project = (ProjectData)MasterMap.queryForEntity(ProjectData.class,new Long(2),connection);
		
		o.setBankAccount(bankaccount);
		o.setCashAccount(cashAccount);
		o.setEmpAuthorize(employee);
		o.setUnit(Unit);
		o.setCustomer(Customer);
		o.setProject(project);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		SalesInvoice obaru = (SalesInvoice) list.get(2);
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("bank account berbeda","BNID1",obaru.getBankAccount().getCode());
		assertEquals("cash account berbeda","C01",obaru.getCashAccount().getCode());
		assertEquals("employee berbeda","Kadek",obaru.getEmpAuthorize().getFirstName());		
		assertEquals("Unit berbeda","01",obaru.getUnit().getCode());
		assertEquals("customer berbeda","002",obaru.getCustomer().getCode());
		assertEquals("project berbeda","Jasa Inspeksi, verifikasi",obaru.getProject().getWorkDescription());
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
