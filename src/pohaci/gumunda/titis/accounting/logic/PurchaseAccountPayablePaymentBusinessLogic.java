/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class PurchaseAccountPayablePaymentBusinessLogic extends
		TransactionBusinessLogic {

	public PurchaseAccountPayablePaymentBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
		entityMapper.setActiveConn(connection);
	}

	public boolean hasUnpostedTransaction(Object entity) throws Exception {
		if(entity==null)
			throw new Exception("There is no entity set");		
		PurchaseApPmt payment = (PurchaseApPmt)entity;
		String whereClause = "";
		if (payment.getPurchaseReceipt()!=null){
			PurchaseReceipt receipt = payment.getPurchaseReceipt();
			whereClause= IDBConstants.ATTR_STATUS + "<3 AND " + IDBConstants.ATTR_PURCHASE_RECEIPT + "=" + receipt.getIndex();
			if(receipt.getIndex()>0)
				whereClause += " AND " + IDBConstants.ATTR_AUTOINDEX + "!="+ payment.getIndex();			
		}
		else if (payment.getBeginningBalance()!=null){
			BeginningAccountPayable bb = payment.getBeginningBalance();
			whereClause= IDBConstants.ATTR_STATUS + "<3 AND " + IDBConstants.ATTR_BEGINNING_BALANCE+ "=" + bb.getIndex() + " AND " +
				IDBConstants.ATTR_AUTOINDEX + "!="+ payment.getIndex();;			
		}
		List list = entityMapper.doSelectWhere(whereClause);		
		if (list.size()>0){
			return true;		 
		}
		return false;
	}	
}
