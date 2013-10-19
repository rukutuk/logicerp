package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetEntry;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashDetail;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import junit.framework.TestCase;

public class TesBeginningCashDetail extends TestCase {
	public void testCreate() throws Exception
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
		connection.setAutoCommit(false);
		MasterMap.setPrimaryConnection(connection);
		GenericMapper mapper = MasterMap.obtainMapperFor(BeginningCashDetail.class);
		mapper.setActiveConn(connection);
		BeginningBalanceSheetEntry master = new BeginningBalanceSheetEntry();
		Account account = new Account(36,Account.nullObject());
		master.setAccount(account);
		master.setAccValue(124.5);
		Currency currency = new Currency(1,Currency.nullObject());		
		master.setCurrency(currency);
		master.setExchangeRate(1);
		master.setType(BeginningBalanceSheetEntry.CASH);
		
		GenericMapper entryM = MasterMap.obtainMapperFor(BeginningBalanceSheetEntry.class);
		entryM.setActiveConn(connection);
		entryM.doInsert(master);
		
		BeginningCashDetail detail = new BeginningCashDetail();
		
		CashAccount cashAccount = new CashAccount(4,CashAccount.nullObject());
		detail.setAccount(account);
		detail.setAccValue(350);
		detail.setCurrency(currency);
		detail.setExchangeRate(1);
		detail.setCashAccount(cashAccount);
		mapper.doInsert(detail,master);
		System.out.println(detail.getIndex());
		assertTrue("index unchanged, save failed", detail.getIndex()!=0);
		BeginningCashDetail receivedEntity = (BeginningCashDetail) mapper.doSelectByIndex(new Long(detail.getIndex()));
		assertEquals("cashacct association",receivedEntity.cashAccountName(),"Kas Surabaya Porong");
		
		
		//mapper.doInsert();
		
		connection.rollback();
		
	}
}
