package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import junit.framework.TestCase;

public class TesBeginningEmpRecv extends TestCase {
	void testCreate() throws ClassNotFoundException, SQLException {
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
	
	GenericMapper m = MasterMap.obtainMapperFor(BeginningEmpReceivable.class);
	GenericMapper empMapper = MasterMap.obtainMapperFor(Employee.class);
	BeginningEmpReceivable record = new BeginningEmpReceivable();
	MasterMap.setPrimaryConnection(connection); 
	Employee anEmployee = (Employee) empMapper.doSelectByIndex(new Long(1));
	record.setEmployee(anEmployee);
	m.doInsert(record);
	
	connection.rollback();
	//m.do
	//}
}
}