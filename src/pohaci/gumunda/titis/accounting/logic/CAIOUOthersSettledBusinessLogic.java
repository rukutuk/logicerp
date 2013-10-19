/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

/**
 * @author dark-knight
 *
 */
public class CAIOUOthersSettledBusinessLogic extends
		TransactionBusinessLogic {

	private Employee owner;

	public CAIOUOthersSettledBusinessLogic(Connection connection,
			long sessionId, Employee owner) {
		super(connection, sessionId);
		this.owner = owner;
		entityMapper = MasterMap.obtainMapperFor(PmtCAIOUOthersSettled.class);
		entityMapper.setActiveConn(connection);
	}
	
	public CAIOUOthersSettledBusinessLogic(Connection connection,
			long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(PmtCAIOUOthersSettled.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstandingList() {
		List resultList = new ArrayList();
		if (this.owner == null)
			return resultList;
		List list = entityMapper.doSelectWhere("status=3");
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAIOUOthersSettled pmt = (PmtCAIOUOthersSettled) iterator.next();
			if (pmt.getPmtcaiouothers().getPayTo().equals(this.owner)) {
				if (isOutstanding(pmt)) {
					resultList.add(pmt);
				}
			}
		}
		return resultList;
	}

	public List getOutstandingByTransDateList(String date) {
		List resultList = new ArrayList();
		if (this.owner == null)
			return resultList;
		List list = entityMapper.doSelectWhere(IDBConstants.ATTR_TRANSACTION_DATE +"<='" + date + "' And status=3");
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAIOUOthersSettled pmt = (PmtCAIOUOthersSettled) iterator.next();
			if (pmt.getPmtcaiouothers().getPayTo().equals(this.owner)) {
				if (isOutstanding(pmt)) {
					resultList.add(pmt);
				}
			}
		}
		return resultList;
	}

	private boolean isOutstanding(PmtCAIOUOthersSettled pmt) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ExpenseSheet.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("PMTCAIOUOTHERSSETTLED="
				+ pmt.getIndex() + " AND STATUS=3");
		return list.size() == 0;
	}

	public List getESOwner() {
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("status=3");
		Hashtable hashtable = new Hashtable();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAIOUOthersSettled pmt = (PmtCAIOUOthersSettled) iterator.next();
			if (isOutstanding(pmt)) {
				Employee payto = pmt.getPmtcaiouothers().getPayTo();
				if (!hashtable.containsKey(new Long(payto.getIndex()))) {
					hashtable.put(new Long(payto.getIndex()), payto);
					resultList.add(payto);
				}
			}
		}

		return resultList;
	}

}
