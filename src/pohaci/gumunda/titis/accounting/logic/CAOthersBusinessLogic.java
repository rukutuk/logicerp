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
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

/**
 * @author dark-knight
 * 
 */
public class CAOthersBusinessLogic extends TransactionBusinessLogic {

	private Employee owner;

	public CAOthersBusinessLogic(Connection connection, long sessionId,
			Employee owner) {
		super(connection, sessionId);
		this.owner = owner;
		entityMapper = MasterMap.obtainMapperFor(PmtCAOthers.class);
		entityMapper.setActiveConn(connection);
	}
	
	public CAOthersBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(PmtCAOthers.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstandingList() {
		List resultList = new ArrayList();
		if (this.owner == null)
			return resultList;
		List list = entityMapper.doSelectWhere("status=3 and payto="
				+ this.owner.getIndex());
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAOthers pmt = (PmtCAOthers) iterator.next();
			if (isOutstanding(pmt)) {
				resultList.add(pmt);
			}
		}
		return resultList;
	}
	
	public List getOutstandingByTransDateList(String date) {
		List resultList = new ArrayList();
		String strWhere = IDBConstants.ATTR_TRANSACTION_DATE + "<='" + date + "' AND " + 
		IDBConstants.ATTR_STATUS + "=3";
		List list = entityMapper.doSelectWhere(strWhere);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAOthers pmt = (PmtCAOthers)iterator.next();
			if (isOutstanding(pmt)) {
				resultList.add(pmt);
			}
		}
		return resultList;
	}

	private boolean isOutstanding(PmtCAOthers pmt) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ExpenseSheet.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("PMTCAOTHERS=" + pmt.getIndex()
				+ " AND STATUS=3");
		return list.size() == 0;
	}

	public List getESOwner() {
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("status=3");
		Hashtable hashtable = new Hashtable();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAOthers pmt = (PmtCAOthers) iterator.next();
			if (isOutstanding(pmt)) {
				if (!hashtable.containsKey(new Long(pmt.getPayTo().getIndex()))) {
					hashtable.put(new Long(pmt.getPayTo().getIndex()), pmt
							.getPayTo());
					resultList.add(pmt.getPayTo());
				}
			}
		}

		return resultList;
	}
}
