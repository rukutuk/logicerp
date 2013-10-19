package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

/**
 * 
 * @author dark-knight
 *
 */
public class BaseCurrency extends Currency{

	public BaseCurrency(Currency currency, short status) {
		super(currency, status);
	}

	public static BaseCurrency createBaseCurrency(Connection conn, long sessionId){
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
	
		try {
			Currency currency = logic.getBaseCurrency(sessionId, IDBConstants.MODUL_ACCOUNTING);
			return new BaseCurrency(currency, Currency.SYMBOL);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
