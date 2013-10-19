package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

import junit.framework.TestCase;
import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TesPmtUnitBankCashTrans extends TestCase {
	public void testSubmit() throws Exception
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

		GenericMapper m = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
		m.setActiveConn(connection);

		PmtUnitBankCashTrans o = new PmtUnitBankCashTrans();

		Currency curr = new Currency(0, "Rp", "IDR", "Indonesian Rupiah", "Rupiah", "Sen", MoneyTalk.LANGUAGE_INDONESIAN);
		Unit unit1 = new Unit(1, "01", "Jakarta");
		Unit unit2 = new Unit(2, "02", "Entah");
		Account acc1 =
			new Account(12, "1.01.01.02", "Bank Rupiah", (short)-1, false,
					(short)0, "", "");
		Account acc2 =
			new Account(4, "1.01.01.01", "Kas Kecil", (short)-1, false,
					(short)0, "", "");


		//CashBankAccount source = new BankAccount(0, "BNI", "BNI", "123.456.789",
		//		curr, "X", "Y", "Z", unit1, acc1);
		CashBankAccount source = new CashAccount(0, "C01", "Kas Jakarta" , curr, unit1, acc2);
		//CashBankAccount target = new BankAccount(1, "BCA", "BCA", "987.654.321",
		//		curr, "X", "Y", "Z", unit2, acc);
		CashBankAccount target = new CashAccount(1, "C01", "Kas Jakarta" , curr, unit2, acc1);

		o.setAmount(1000000.00);
		//o.setBankAccount((BankAccount)source);
		o.setCashAccount((CashAccount)source);
		//o.setRcvBankAccount((BankAccount)target);
		o.setRcvCashAccount((CashAccount)target);
		o.setCurrency(curr);
		o.setTransactionDate(new Date());

		o.submit(-1, connection);


	}

	public void testUpdate() throws Exception
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

		GenericMapper m = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
		m.setActiveConn(connection);

		PmtUnitBankCashTrans p = (PmtUnitBankCashTrans)
				MasterMap.queryForEntity(PmtUnitBankCashTrans.class,
				new Long(15), connection);

		p.setDescription("555 faiz");
		m.doUpdate(p);
	}
}
