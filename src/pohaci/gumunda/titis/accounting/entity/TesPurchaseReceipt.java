package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import junit.framework.TestCase;

public class TesPurchaseReceipt extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PurchaseReceipt.class);	
		MasterMap.setPrimaryConnection(connection);
		PurchaseReceipt o =
			new PurchaseReceipt();
		
		o.setIndex(15);
		o.setRemarks("latih lagi");
		o.setDescription("latih");
		Partner suppliers = (Partner) MasterMap.queryForEntity(Partner.class,new Long(5),connection);
		Employee employee = (Employee) MasterMap.queryForEntity(Employee.class,new Long(7),connection);
		ProjectData ProjectData = (ProjectData) MasterMap.queryForEntity(ProjectData.class,new Long(2),connection);
		o.setEmpOriginator(employee);
		o.setSupplier(suppliers);
		o.setProject(ProjectData);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);
		
		m.doInsert(o);
		List list = m.doSelectAll();
		PurchaseReceipt obaru = (PurchaseReceipt) list.get(2);
		assertEquals("description berbeda","latih",obaru.getDescription());
		assertEquals("remark berbeda","latih lagi",obaru.getRemarks());
		assertEquals("supplier berbeda","01",obaru.getSupplier().getCode());
		assertEquals("employee berbeda","Sayogi",obaru.getEmpOriginator().getFirstName());
		assertEquals("unit berbeda","Jasa Inspeksi, verifikasi",obaru.getProject().getWorkDescription());
		connection.rollback(); // ga jadi dink	
		connection.commit();
		connection.close();
	}
}
