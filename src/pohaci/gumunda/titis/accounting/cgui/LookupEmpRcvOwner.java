package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import pohaci.gumunda.titis.accounting.logic.BeginningEmpReceivableBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.EmployeeReceivablePaymentBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupEmpRcvOwner extends LookupPicker {
	private static final long serialVersionUID = 1L;
	String ATR_JOURNAL;  
	public LookupEmpRcvOwner(Connection conn, long sessionid) {
		super(conn, sessionid, "Employee Receivable Owner");
		setSize(400, 300);
		initData();		
	}	
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Employee");
		getModel().clearRows();		
		EmployeeReceivablePaymentBusinessLogic logic = new EmployeeReceivablePaymentBusinessLogic(m_conn, m_sessionid);
		List list = logic.getEmployeeReceivablesOwner();	
		Iterator iterator = list.iterator();
		
		Hashtable tbl = new Hashtable();
		List dataList = new ArrayList();
		while (iterator.hasNext()) {
			Employee payTo = (Employee) iterator.next();
			tbl.put(new Long(payTo.getIndex()), payTo); // aku ubah juga
			dataList.add(payTo);
		}
		
		// AKU UBAH!!!
		BeginningEmpReceivableBusinessLogic bbLogic = new BeginningEmpReceivableBusinessLogic(m_conn, m_sessionid);
		List bbList = bbLogic.getEmployeeReceivablesOwner();
		Iterator iter = bbList.iterator();
		
		while(iter.hasNext()){
			Employee payTo = (Employee) iter.next();
			if(!tbl.containsKey(new Long(payTo.getIndex()))){
				tbl.put(new Long(payTo.getIndex()), payTo);
				dataList.add(payTo);
			}
		}
		
		Iterator iter2 = dataList.iterator();
		int k = 0;
		while(iter2.hasNext()){
			Employee payTo = (Employee) iter2.next();
			getModel().addRow(new Object[]{new Integer(++k), payTo});
		}
			
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 1);
			setObject(obj);
		}
	}
	protected String getCaption(Object obj){
		Employee emp = (Employee) obj;
		return emp.getFirstName()+" "+emp.getLastName();
	}		
}