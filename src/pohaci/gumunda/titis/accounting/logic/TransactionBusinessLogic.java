/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import pohaci.gumunda.titis.application.db.GenericMapper;

/**
 * @author dark-knight
 *
 */
public abstract class TransactionBusinessLogic {
	protected Connection connection;
	protected long sessionId;
	protected GenericMapper entityMapper;

	public TransactionBusinessLogic(Connection connection, long sessionId) {
		this.connection = connection;
		this.sessionId = sessionId;
	}
	
	public List getOutstandingList() throws Exception {
		return new ArrayList();
	}
	
	public boolean hasUnpostedTransaction(Object entity) throws Exception {
		return false;
	}
}
