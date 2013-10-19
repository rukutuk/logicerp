package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import junit.framework.TestCase;

public class TesPmtCAProject extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtCAProject.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtCAProject o =
			new PmtCAProject();
		
		o.setIndex(3);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		ProjectData project = (ProjectData)MasterMap.queryForEntity(ProjectData.class,new Long(2),connection);
		Unit unit = (Unit)MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		Employee employee = (Employee)MasterMap.queryForEntity(Employee.class,new Long(5),connection);
		BankAccount bankAccount = (BankAccount)MasterMap.queryForEntity(BankAccount.class,new Long(3),connection);
		CashAccount cashAccount = (CashAccount)MasterMap.queryForEntity(CashAccount.class,new Long(1),connection);
		o.setProject(project);
		o.setUnit(unit);
		o.setEmpOriginator(employee);
		o.setBankAccount(bankAccount);		
		o.setCashAccount(cashAccount);
		
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtCAProject obaru = (PmtCAProject) list.get(2);
		assertEquals("project berbeda","Jasa Inspeksi, verifikasi",obaru.getProject().getWorkDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("Bank berbeda","BNID1",obaru.getBankAccount().getCode());
		assertEquals("Employee berbeda","Kadek",obaru.getEmpOriginator().getFirstName());
		assertEquals("CashAccount berbeda","C01",obaru.getCashAccount().getCode());
		
		connection.rollback(); // ga jadi dink
		connection.close();
		//connection.commit();
	}

}
