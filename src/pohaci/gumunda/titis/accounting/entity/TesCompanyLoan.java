package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

import junit.framework.TestCase;

public class TesCompanyLoan extends TestCase {
    public void testLoadLoan() throws ClassNotFoundException, SQLException
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
		MasterMap.setPrimaryConnection(connection);
		GenericMapper mapper = MasterMap.obtainMapperFor(CompanyLoan.class);
		mapper.setActiveConn(connection);
		Object hasil = mapper.doSelectByIndex(new Long(2));
		CompanyLoan hasil1 = (CompanyLoan) hasil;
		System.out.println(hasil1.getName());
		System.out.println(hasil1.getAccount().toStringWithCode());

    }
}
