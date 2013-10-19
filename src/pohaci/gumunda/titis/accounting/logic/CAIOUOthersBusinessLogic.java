package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.application.db.MasterMap;

public class CAIOUOthersBusinessLogic  extends
TransactionBusinessLogic {

	public CAIOUOthersBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(PmtCAIOUOthers.class);
		entityMapper.setActiveConn(connection);
	}
	
	public List getOutstandingByTransDateList(String date) {
		String strWhere = IDBConstants.ATTR_TRANSACTION_DATE + "<='" + date + "'";
		List list = entityMapper.doSelectWhere(strWhere);			
		return list;		
	}

}
