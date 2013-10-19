package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPmtProjectCostDetail extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PmtProjectCostDetail.class);	
		MasterMap.setPrimaryConnection(connection);
		PmtProjectCostDetail o =
			new PmtProjectCostDetail();
		
		o.setaccValue(778);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);		
		List list = m.doSelectAll();
		PmtProjectCostDetail obaru = (PmtProjectCostDetail) list.get(0);
		System.out.println(obaru);
		//assertEquals("remark berbeda",778,obaru.getAttrValue(), 0);
		connection.rollback(); // ga jadi dink
		connection.close();
		//connection.commit();
	}
}
