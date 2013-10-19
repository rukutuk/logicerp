package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.RcvEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;

public class BeginningEmpReceivableBusinessLogic extends BeginningBalanceBusinessLogic{

	private long m_indexEmployee;
	public BeginningEmpReceivableBusinessLogic(Connection connection, long sessionId,long indexEmployee) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(BeginningEmpReceivable.class);
		entityMapper.setActiveConn(connection);
		m_indexEmployee = indexEmployee;
	}
	
	public BeginningEmpReceivableBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(BeginningEmpReceivable.class);
		entityMapper.setActiveConn(connection);
		m_indexEmployee = -1;
	}
	
	public List getOutstanding() {
		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(this.connection, this.sessionId,IConstants.ATTR_VARS_EMP_REC);
		List resultList = new ArrayList();
		String strWhere = IDBConstants.ATTR_EMPLOYEE + "=" + m_indexEmployee + " AND "+
		IDBConstants.ATTR_ACCOUNT+"="+ vas.getAccount().getIndex();
		System.err.println(strWhere);
		List list = entityMapper.doSelectWhere(strWhere);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {			
			BeginningEmpReceivable bb = (BeginningEmpReceivable)iterator.next();			
			EmployeePicker helpEmp=new EmployeePicker(this.connection,this.sessionId);			
			Unit unit = helpEmp.findUnitEmployee(bb.getEmployee());
			if (unit==null){				
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
	
	private boolean isOutstanding(BeginningEmpReceivable bb) {
		double balance = getEmpReceivableBalance(bb);
		return (balance > 0);
	}
	
	public double getEmpReceivableBalance(BeginningEmpReceivable bb) {
		double totalReceived = getAccumulatedPayable(bb);
		double ar = bb.getAccValue();
		
		double balance = ar - totalReceived;
		return balance;
	}
	
	public double getAccumulatedPayable(BeginningEmpReceivable bb) { 
		GenericMapper mapper = MasterMap.obtainMapperFor(RcvEmpReceivable.class);
		mapper.setActiveConn(connection);
		String strWhere = "BEGINNINGBALANCE=" + bb.getIndex()+ " AND STATUS=3";
		System.err.println(strWhere);
		List list = mapper.doSelectWhere(strWhere);
		Iterator iter = list.iterator();
		double amt = 0;
		while (iter.hasNext()) {
			RcvEmpReceivable rcv = (RcvEmpReceivable) iter.next();
			amt += rcv.getAmount();
		}
		
		return amt;
	}
	
	public List getEmployeeReceivablesOwner(){
		List resultList = new ArrayList();
		List list = entityMapper.doSelectAll();
		Hashtable hashtable = new Hashtable();		
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			BeginningEmpReceivable ber = (BeginningEmpReceivable) iterator.next();		
			
			EmployeePicker helpEmp=new EmployeePicker(this.connection,this.sessionId);			
			Unit unit = helpEmp.findUnitEmployee(ber.getEmployee());
			if (unit==null){				
				UnitPicker unitpicker = new UnitPicker();
				unitpicker.init(this.connection, this.sessionId);				
				unit = unitpicker.getDefaultUnit();
			}
			
			Transaction trans = findTransaction(unit);
			if (trans != null) {
				if (trans.getStatus() == 3) {
					if (isOutstanding(ber)) {
						if (!hashtable.containsKey(new Long(ber.getEmployee()
								.getIndex()))) {
							hashtable.put(
									new Long(ber.getEmployee().getIndex()), ber
											.getEmployee());
							resultList.add(ber.getEmployee());
						}
					}
				}
			}
		}
		
		return resultList;
	}
}
