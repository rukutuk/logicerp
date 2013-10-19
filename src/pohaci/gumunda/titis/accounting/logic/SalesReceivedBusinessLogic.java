/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesReceived;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class SalesReceivedBusinessLogic extends TransactionBusinessLogic {

	public SalesReceivedBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(SalesReceived.class);
		entityMapper.setActiveConn(connection);
	}

	public boolean hasUnpostedTransaction(Object entity) throws Exception{
		if(entity==null)
			throw new Exception("There is no entity set");
		
		SalesReceived salesReceived = (SalesReceived) entity;
		SalesInvoice salesInvoiceUsed = salesReceived.getInvoice();
		
		if(salesInvoiceUsed==null)
			return false;
		
		String whereClausa = "INVOICE=" + salesInvoiceUsed.getIndex() + " AND STATUS<3";
		if(salesReceived.getIndex()>0)
			whereClausa += " AND AUTOINDEX!=" + salesReceived.getIndex();
		
		List list = entityMapper.doSelectWhere(whereClausa);
		
		if(list.size()==0)
			return false;
		
		return true;
	}
}
