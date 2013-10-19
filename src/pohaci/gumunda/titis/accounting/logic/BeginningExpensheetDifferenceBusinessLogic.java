package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEsDiff;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtESDiff;
import pohaci.gumunda.titis.accounting.entity.RcvESDiff;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;

public class BeginningExpensheetDifferenceBusinessLogic extends
		BeginningBalanceBusinessLogic {
	private Employee employee;

	private int type = -1;

	public static int RECEIVE = 1;

	public static int PAYMENT = 2;

	public BeginningExpensheetDifferenceBusinessLogic(Connection connection,
			long sessionId, int type) {
		super(connection, sessionId);
		this.type = type;
		entityMapper = MasterMap.obtainMapperFor(BeginningEsDiff.class);
		entityMapper.setActiveConn(connection);
	}

	public BeginningExpensheetDifferenceBusinessLogic(Connection connection,
			long sessionId, Employee employee, int type) {
		super(connection, sessionId);
		this.type = type;
		entityMapper = MasterMap.obtainMapperFor(BeginningEsDiff.class);
		entityMapper.setActiveConn(connection);
		this.employee = employee;
	}

	public List getOutstandingList() {
		VariableAccountSetting vas = VariableAccountSetting
				.createVariableAccountSetting(this.connection, this.sessionId,
						IConstants.ATTR_VARS_ES_DIFF);
		List resultList = new ArrayList();
		if (employee == null)
			return resultList;

		String strWhere = IDBConstants.ATTR_EMPLOYEE + "="
				+ employee.getIndex() + " AND " + IDBConstants.ATTR_ACCOUNT
				+ "=" + vas.getAccount().getIndex();

		List list = entityMapper.doSelectWhere(strWhere);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningEsDiff bb = (BeginningEsDiff) iterator.next();

			Transaction trans = getTransaction(bb);
			if (trans != null) {
				bb.setTrans(trans);
				bb.showReferenceNo(true);
				if (isOutstanding(bb)) {
					resultList.add(bb);
				}
			}
		}
		return resultList;
	}

	private Transaction getTransaction(BeginningEsDiff bb) {
		EmployeePicker helpEmp = new EmployeePicker(this.connection,
				this.sessionId);
		Unit unit = helpEmp.findUnitEmployee(bb.getEmployee());
		if (unit == null) {
			UnitPicker unitpicker = new UnitPicker();
			unitpicker.init(this.connection, this.sessionId);
			unit = unitpicker.getDefaultUnit();
		}

		Transaction trans = findTransaction(unit);
		return trans;
	}

	private boolean isOutstanding(BeginningEsDiff bb) {
		GenericMapper mapper = null;
		String whereClause = "BEGINNINGBALANCE=" + bb.getIndex()
				+ " AND STATUS=3";
		if (type == RECEIVE) {
			mapper = MasterMap.obtainMapperFor(RcvESDiff.class);
		} else {
			mapper = MasterMap.obtainMapperFor(PmtESDiff.class);
		}

		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere(whereClause);

		return (list.size() == 0);
	}

	/*
	 * public double getEsDiffPayableBalance(BeginningEsDiff bb) { double
	 * totalReceived = 0; if (type == 1) totalReceived =
	 * getAccumulatedPayableReceive(bb); else totalReceived =
	 * getAccumulatedPayablePayment(bb); double ar = bb.getAccValue();
	 * 
	 * double balance = ar - totalReceived; return balance; }
	 * 
	 * public double getAccumulatedPayableReceive(BeginningEsDiff bb) {
	 * GenericMapper mapper = MasterMap.obtainMapperFor(RcvESDiff.class);
	 * mapper.setActiveConn(connection); String strWhere = "BEGINNINGBALANCE=" +
	 * bb.getIndex() + " AND STATUS=3"; System.err.println(strWhere); List list =
	 * mapper.doSelectWhere(strWhere); Iterator iter = list.iterator(); double
	 * amt = 0; while (iter.hasNext()) { RcvESDiff rcv = (RcvESDiff)
	 * iter.next(); amt += rcv.getAmount(); } return amt; }
	 * 
	 * public double getAccumulatedPayablePayment(BeginningEsDiff bb) {
	 * GenericMapper mapper = MasterMap.obtainMapperFor(PmtESDiff.class);
	 * mapper.setActiveConn(connection); String strWhere = "BEGINNINGBALANCE=" +
	 * bb.getIndex() + " AND STATUS=3"; System.err.println(strWhere); List list =
	 * mapper.doSelectWhere(strWhere); Iterator iter = list.iterator(); double
	 * amt = 0; while (iter.hasNext()) { PmtESDiff pmt = (PmtESDiff)
	 * iter.next(); amt += pmt.getAmount(); } return amt; }
	 */

	public List getESOwner() {
		List resultList = new ArrayList();
		String whereClause = "";
		if (type == RECEIVE)
			whereClause += "accvalue>0"; // kayaknya benul bahwa accvalue>0
		// ==> receive
		else
			whereClause += "accvalue<0";

		List list = entityMapper.doSelectWhere(whereClause);
		Iterator iterator = list.iterator();
		Hashtable hashtable = new Hashtable();
		while (iterator.hasNext()) {
			BeginningEsDiff bb = (BeginningEsDiff) iterator.next();

			Transaction trans = getTransaction(bb);
			if (trans != null) {
				if (trans.getStatus() == 3) {
					if (isOutstanding(bb)) {
						Employee emp = bb.getEmployee();
						if (!hashtable.containsKey(new Long(emp.getIndex()))) {
							hashtable.put(new Long(emp.getIndex()), emp);
							resultList.add(emp);
						}
					}
				}
			}
		}
		return resultList;
	}
}
