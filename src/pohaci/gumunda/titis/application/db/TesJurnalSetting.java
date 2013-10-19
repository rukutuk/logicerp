package pohaci.gumunda.titis.application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class TesJurnalSetting extends TestCase {
	String host = "172.18.9.1";
	String dbname = "sampurna";
	String username = "sampurnauser";
	String password = "calamari";

    public void test1() throws Exception
    {
    		Class.forName ("com.sap.dbtech.jdbc.DriverSapDB");
    	    String url = "jdbc:sapdb://"
    	        + host + "/"
    	        + dbname;

    	    Connection connection = DriverManager.getConnection (url,
    	        username, password);

    	JournalStandardSettingPickerHelper helper
		= new JournalStandardSettingPickerHelper(connection, 0, 
				IDBConstants.MODUL_MASTER_DATA);
    	List list = helper.getJournalStandardSetting(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES);
    	System.out.println(list.size());
    	Iterator iterator = list.iterator();
    	while (iterator.hasNext())
    	{
    		JournalStandardSetting setting =
    		(JournalStandardSetting) iterator.next();
    		System.out.println(setting.getJournalStandard().toString());
    	}
    }
}
