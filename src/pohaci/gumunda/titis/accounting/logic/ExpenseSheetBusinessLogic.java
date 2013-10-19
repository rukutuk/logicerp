/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.PmtESDiff;
import pohaci.gumunda.titis.accounting.entity.RcvESDiff;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

/**
 * @author dark-knight
 * 
 */
public class ExpenseSheetBusinessLogic extends TransactionBusinessLogic {

	private Employee employee;
	private int type;
	public static int RECEIVE = 1;
	public static int PAYMENT = 2;
	
	public ExpenseSheetBusinessLogic(Connection connection, long sessionId, int type) {
		super(connection, sessionId);

		this.type = type;
		entityMapper = MasterMap.obtainMapperFor(ExpenseSheet.class);
		entityMapper.setActiveConn(connection);
	}
	
	public ExpenseSheetBusinessLogic(Connection connection, long sessionId, Employee employee, int type) {
		super(connection, sessionId);
		
		this.type = type;
		this.employee = employee;
		entityMapper = MasterMap.obtainMapperFor(ExpenseSheet.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstandingList() {
		List resultList = new ArrayList();
		if(this.employee==null)
			return resultList;
		
		List list = entityMapper.doSelectWhere("status=3 and esowner=" + this.employee.getIndex());
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			ExpenseSheet es = (ExpenseSheet) iterator.next();

			double esAmount = es.getAmount();
			double caAmount = 0;

			if (es.getPmtCaProject() != null)
				caAmount = es.getPmtCaProject().getAmount();
			else if (es.getPmtCaOthers() != null)
				caAmount = es.getPmtCaOthers().getAmount();
			else if (es.getPmtCaIouProjectSettled() != null)
				caAmount = es.getPmtCaIouProjectSettled().getAmount();
			else if (es.getPmtCaIouOthersSettled() != null)
				caAmount = es.getPmtCaIouOthersSettled().getAmount();
			else if (es.getBeginningBalance() != null)
				caAmount = es.getBeginningBalance().getAccValue();

			boolean isOk = false;
			
			if(type==RECEIVE)
				isOk = esAmount < caAmount;
			else
				isOk = esAmount > caAmount;
				
			if (isOk) {
				if (isOutstanding(es)) {
					resultList.add(es);
				}
			}
		}
		return resultList;
	}

	private boolean isOutstanding(ExpenseSheet es) {
		GenericMapper mapper;
		if(type==RECEIVE)
			mapper = MasterMap.obtainMapperFor(RcvESDiff.class);
		else
			mapper = MasterMap.obtainMapperFor(PmtESDiff.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("ESNO=" + es.getIndex()
				+ " AND STATUS=3");
		return list.size() == 0;
	}

	public List getESOwner() {
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("status=3");
		Iterator iterator = list.iterator();
		Hashtable hashtable = new Hashtable();
		while (iterator.hasNext()) {
			ExpenseSheet es = (ExpenseSheet) iterator.next();

			double esAmount = es.getAmount();
			double caAmount = 0;

			if (es.getPmtCaProject() != null)
				caAmount = es.getPmtCaProject().getAmount();
			else if (es.getPmtCaOthers() != null)
				caAmount = es.getPmtCaOthers().getAmount();
			else if (es.getPmtCaIouProjectSettled() != null)
				caAmount = es.getPmtCaIouProjectSettled().getAmount();
			else if (es.getPmtCaIouOthersSettled() != null)
				caAmount = es.getPmtCaIouOthersSettled().getAmount();
			else if (es.getBeginningBalance() != null)
				caAmount = es.getBeginningBalance().getAccValue();

			boolean isOk = false;
			
			if(type==RECEIVE)
				isOk = esAmount < caAmount;
			else
				isOk = esAmount > caAmount;
				
			if (isOk) {
				if (isOutstanding(es)) {
					Employee emp = es.getEsOwner();
					if (!hashtable.containsKey(new Long(emp.getIndex()))){
						hashtable.put(new Long(emp.getIndex()), emp);
						resultList.add(emp);
					}
				}
			}
		}
		return resultList;
	}

}
