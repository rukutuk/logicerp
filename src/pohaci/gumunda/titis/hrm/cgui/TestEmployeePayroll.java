package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TestEmployeePayroll extends TestCase {
	public void testInsert() throws Exception
	{
		System.out.println("testInsert");
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

		GenericMapper m = MasterMap.obtainMapperFor(EmployeePayroll.class);
		//String fName = "XYZ";
		EmployeePayroll o =
			new EmployeePayroll();
		Long id =new Long(1);
		
		o.setMonth((short)5);
		o.setPaymentPeriode((short)0);
		o.setPayrollType((short)0);
		o.setStatus((short)0);
		o.setSubmittedDate(Calendar.getInstance().getTime());
		Transaction t = (Transaction) MasterMap.queryForEntity(Transaction.class, id, connection);
		o.setTrans(t);
		Unit u = (Unit) MasterMap.queryForEntity(Unit.class, id, connection);
		o.setUnit(u);
		o.setYear((short)0);
		m.setActiveConn(connection);
		m.doInsert(o);
		connection.commit();
		List list = m.doSelectAll();
		EmployeePayroll obaru = (EmployeePayroll) list.get(0);
		System.out.println(obaru);
		//assertEquals("amount berbeda",123.45,obaru.getAmount(),0.01);
		connection.rollback(); // ga jadi dink
		//connection.commit();
		System.out.println("berhasil");	
	}
	/*
	public void testGetEmployeePayroll() throws Exception
	{
		System.out.println("testGetEmployeePayroll");
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
        Long id = new Long(1);
        Currency c = (Currency) MasterMap.queryForEntity(Currency.class,id,connection);
        System.out.println(c);
	}
	*/
}
