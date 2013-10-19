package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.framework.TestCase;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetEntry;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBankDetail;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesBeginningBankDetail extends TestCase {
	
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
		GenericMapper mapper = MasterMap.obtainMapperFor(BeginningBankDetail.class);
		mapper.setActiveConn(connection);
		BeginningBalanceSheetEntry master = new BeginningBalanceSheetEntry();
		Account account = new Account(36,Account.nullObject());
		master.setAccount(account);
		master.setAccValue(124.5);
		Currency currency = new Currency(1,Currency.nullObject());		
		master.setCurrency(currency);
		master.setExchangeRate(1);
		master.setType(BeginningBalanceSheetEntry.BANK);
		
		GenericMapper entryM = MasterMap.obtainMapperFor(BeginningBalanceSheetEntry.class);
		entryM.setActiveConn(connection);
		entryM.doInsert(master);
		
		BeginningBankDetail detail = new BeginningBankDetail();
		
		BankAccount bankAccount = new BankAccount(14,BankAccount.nullObject());
		detail.setAccount(account);
		detail.setAccValue(350);
		detail.setBankAccount(bankAccount);
		detail.setCurrency(currency);
		detail.setExchangeRate(1);
		mapper.doInsert(detail,master);
		assertTrue("index unchanged, save failed", detail.getIndex()!=0);
		System.out.println(detail.getIndex());
		//mapper.doInsert();
		BeginningBankDetail receivedEntity = 
		(BeginningBankDetail) mapper.doSelectByIndex(new Long(detail.getIndex()));
		assertEquals("bank account association mismatch" , 
				"Bank Negara Indonesia",(String) receivedEntity.subsidiaryStr());
		connection.commit();
		
	}
	
}
