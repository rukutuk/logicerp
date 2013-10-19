package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;

public class BeginningESBusinessLogic extends BeginningBalanceBusinessLogic {
	private Employee employee;

	private String varAccountSetting = "";


	public BeginningESBusinessLogic(Connection connection, long sessionId,
			Employee employee, String varAccountSetting) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(BeginningCashAdvance.class);
		entityMapper.setActiveConn(connection);
		this.employee = employee;
		this.varAccountSetting = varAccountSetting;
	}

	public BeginningESBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(BeginningCashAdvance.class);
		entityMapper.setActiveConn(connection);
	}
	
	public BeginningESBusinessLogic(Connection connection, long sessionId,
			String varAccountSetting) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(BeginningCashAdvance.class);
		entityMapper.setActiveConn(connection);
		this.varAccountSetting = varAccountSetting;
	}

	public List getOutstanding() {
		VariableAccountSetting vas = VariableAccountSetting
				.createVariableAccountSetting(this.connection, this.sessionId,
						varAccountSetting);
		List resultList = new ArrayList();
		if (this.employee == null)
			return resultList;

		String strWhere = IDBConstants.ATTR_EMPLOYEE + "="
				+ employee.getIndex() + " AND " + IDBConstants.ATTR_ACCOUNT
				+ "=" + vas.getAccount().getIndex();

		List list = entityMapper.doSelectWhere(strWhere);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningCashAdvance bb = (BeginningCashAdvance) iterator.next();
			EmployeePicker helpEmp = new EmployeePicker(this.connection,
					this.sessionId);
			Unit unit = helpEmp.findUnitEmployee(bb.getEmployee());
			if (unit == null) {
				UnitPicker unitpicker = new UnitPicker();
				unitpicker.init(this.connection, this.sessionId);
				unit = unitpicker.getDefaultUnit();
			}
			Transaction trans = findTransaction(unit);
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

	public List getESOwner() {
		VariableAccountSetting vas = VariableAccountSetting
		.createVariableAccountSetting(this.connection, this.sessionId,
				varAccountSetting);
		
		List resultList = new ArrayList();
		
		String whereClause = IDBConstants.ATTR_ACCOUNT
			+ "=" + vas.getAccount().getIndex();
		System.err.println(whereClause);
		
		List list = entityMapper.doSelectWhere(whereClause);
		Hashtable hashtable = new Hashtable();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningCashAdvance bca = (BeginningCashAdvance) iterator.next();

			EmployeePicker helpEmp = new EmployeePicker(this.connection,
					this.sessionId);
			Unit unit = helpEmp.findUnitEmployee(bca.getEmployee());
			if (unit == null) {
				UnitPicker unitpicker = new UnitPicker();
				unitpicker.init(this.connection, this.sessionId);
				unit = unitpicker.getDefaultUnit();
			}

			Transaction trans = findTransaction(unit);
			if (trans != null) {
				if (trans.getStatus() == 3) {
					if (isOutstanding(bca)) {
						if (!hashtable.containsKey(new Long(bca.getEmployee()
								.getIndex()))) {
									hashtable.put(new Long(bca.getEmployee()
											.getIndex()), bca.getEmployee());
									resultList.add(bca.getEmployee());	
						}
					}
				}
			}
		}

		return resultList;
	}

	private boolean isOutstanding(BeginningCashAdvance bca) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ExpenseSheet.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("BEGINNINGBALANCE=" + bca.getIndex()
				+ " AND STATUS=3");
		return list.size() == 0;
	}
	
	public void getTransaction(BeginningCashAdvance bca){
		EmployeePicker helpEmp = new EmployeePicker(this.connection,
				this.sessionId);
		Unit unit = helpEmp.findUnitEmployee(bca.getEmployee());
		if (unit == null) {
			UnitPicker unitpicker = new UnitPicker();
			unitpicker.init(this.connection, this.sessionId);
			unit = unitpicker.getDefaultUnit();
		}
		Transaction trans = findTransaction(unit);
		if (trans != null) {
			bca.setTrans(trans);
			bca.showReferenceNo(true);
		}
	}
}
