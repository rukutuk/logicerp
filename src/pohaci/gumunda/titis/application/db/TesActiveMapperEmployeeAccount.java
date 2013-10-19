package pohaci.gumunda.titis.application.db;

import java.sql.Connection;
import java.sql.DriverManager;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeAccount;
import junit.framework.TestCase;

public class TesActiveMapperEmployeeAccount extends TestCase {
	public void testSatu()
	{
		DataMapping mpg = DataMapping.create(EmployeeAccount.class);
		mpg.ownedByClass(Employee.class);
		mpg.setWithUnderscore(true);
		GenericMapper m = new GenericMapper(mpg);
		m.ddlCreate();
	}
	public void testDua() throws Exception
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

		DataMapping mpg = DataMapping.create(EmployeeAccount.class);
		mpg.ownedByClass(Employee.class);
		//mpg.setWithUnderscore(true);
		GenericMapper m = new GenericMapper(mpg);
		Currency cy = new Currency(1,"IDR","3","ABCD", "Rupiah", "Sen", MoneyTalk.LANGUAGE_INDONESIAN);
		EmployeeAccount ea = new EmployeeAccount("ABCD","1234","BANKX","BDG",cy,"");
		m.setActiveConn(connection);
		m.doInsert(ea, new Object[] {new Integer(44) } );
		connection.commit();

	}
}
