/**
 *
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.RcvEmpReceivable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

/**
 * @author dark-knight
 *
 */
public class EmployeeReceivablePaymentBusinessLogic extends
		TransactionBusinessLogic {

	private Employee owner = null;

	public EmployeeReceivablePaymentBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(PmtEmpReceivable.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstandingList() throws Exception {
		return super.getOutstandingList();
	}

	public List getEmployeeReceivablesOwner(){
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("status=3");
		Hashtable hashtable = new Hashtable();
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			PmtEmpReceivable pmt = (PmtEmpReceivable) iterator.next();
			if (isOutstanding(pmt)){
				if (!hashtable.containsKey(new Long(pmt.getPayTo().getIndex()))) {
					hashtable.put(new Long(pmt.getPayTo().getIndex()), pmt.getPayTo());
					resultList.add(pmt.getPayTo());
				}
			}
		}

		return resultList;
	}

	public List getOutstanding(){
		List resultList = new ArrayList();
		if(this.owner==null)
			return resultList;
		return getOutstandingEmployeeReceivables(this.owner);
	}

	private List getOutstandingEmployeeReceivables(Employee employee){
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("status=3 and payto=" + employee.getIndex());
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			PmtEmpReceivable pmt = (PmtEmpReceivable) iterator.next();

			if(isOutstanding(pmt))
				resultList.add(pmt);
		}

		return resultList;
	}

	private boolean isOutstanding(PmtEmpReceivable pmt) {
		double balance = getEmployeeReceivablesBalance(pmt);
		return (balance > 0);
	}

	private double getEmployeeReceivablesBalance(PmtEmpReceivable pmt) {
		double totalReceived = getEmployeeReceivablesReceived(pmt);
		double payment = pmt.getAmount();

		double balance = payment - totalReceived;
		return balance;
	}

	public double getEmployeeReceivablesReceived(PmtEmpReceivable pmt) {
		GenericMapper mapper = MasterMap.obtainMapperFor(RcvEmpReceivable.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("STATUS=3 and EMPRECEIVABLE=" + pmt.getIndex());

		double received = 0;
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			RcvEmpReceivable rcv = (RcvEmpReceivable) iterator.next();
			received += rcv.getAmount();
		}
		return received;
	}

	public void setOwner(Employee owner) {
		this.owner = owner;
	}
}
