package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesPurchaseReceiptItem extends TestCase {
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

        	
		GenericMapper m = MasterMap.obtainMapperFor(PurchaseReceiptItem.class);	
		MasterMap.setPrimaryConnection(connection);
		PurchaseReceiptItem o =
			new PurchaseReceiptItem();
		//Unit Unit = (Unit) MasterMap.queryForEntity(Unit.class,new Long(1),connection);
		PurchaseReceipt purchaseReceipt = (PurchaseReceipt) MasterMap.queryForEntity(PurchaseReceipt.class,new Long(2),connection);
		o.setDescription("aku");
		//o.setUnitPrice(Unit);		
		o.setPurchaseReceipt(purchaseReceipt);
		m.setActiveConn(connection);
		connection.setAutoCommit(false);				
		m.doInsert(o);		
		List list = m.doSelectAll();
		PurchaseReceiptItem obaru = (PurchaseReceiptItem) list.get(0);		
		assertEquals("Description berbeda","aku",obaru.getDescription());
		//assertEquals("unit berbeda","01",obaru.getUnitPrice().getCode())w;
		assertEquals("purchase berbeda","P 05 Surabaya001",obaru.getPurchaseReceipt().getReferenceNo());
		
		connection.rollback(); // ga jadi dink
		connection.close();
	}
}
