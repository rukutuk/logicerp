package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class BeginningCashAdvanceBusinessLogic extends BeginningBalanceBusinessLogic{
	
	
	public BeginningCashAdvanceBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(BeginningCashAdvance.class);
		entityMapper.setActiveConn(connection);
	}
	
	public List getOutstanding(ProjectData project) {
		List list = entityMapper.doSelectWhere(IDBConstants.ATTR_PROJECT+"="+project.getIndex());
		Iterator iterator = list.iterator();
		
		ArrayList resultList = new ArrayList();
		while (iterator.hasNext()) {
			BeginningCashAdvance bb = (BeginningCashAdvance) iterator.next();
			resultList.add(bb);
		}				
		return resultList;
	}	
	
	public List getOutstandingProject(String selectwhere) {
		List list = entityMapper.doSelectWhere(selectwhere);
		Iterator iterator = list.iterator();		
		ArrayList resultList = new ArrayList();
		while (iterator.hasNext()) {
			BeginningCashAdvance bb = (BeginningCashAdvance) iterator.next();
			if (isOutstanding(bb))
				resultList.add(bb);
		}				
		return resultList;
	}	
	
	private boolean isOutstanding(BeginningCashAdvance bb) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ExpenseSheet.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere(IDBConstants.ATTR_BEGINNING_BALANCE +"=" + bb.getIndex() + " AND STATUS=3");
		return list.size() == 0;
	}	
}
