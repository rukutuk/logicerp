package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.accounting.logic.BeginningEmpReceivableBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.EmployeeReceivablePaymentBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupEmployeeRcvPicker extends LookupPicker {	
	private static final long serialVersionUID = 1L;
	private Employee m_employee;
	
	public LookupEmployeeRcvPicker(Connection conn, long sessionid, Employee employee) {
		super(conn, sessionid, "Lookup Employee Receivable  List");
		this.m_employee = employee;
		initData();
		setSize(850, 300);
	}
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Employee ID");
		getModel().addColumn("Employee Name");
		getModel().addColumn("Voucher No");
		getModel().addColumn("Voucher Date");
		getModel().addColumn("Curr");
		getModel().addColumn("ER Amount");
		getModel().addColumn("Exchange Rate");
		getModel().addColumn("Approved by");
		getModel().addColumn("Status");
		getModel().addColumn("Total Paid");
		getModel().clearRows();
		
		if(this.m_employee==null)
			return;
		
		EmployeeReceivablePaymentBusinessLogic logic = new EmployeeReceivablePaymentBusinessLogic(m_conn, m_sessionid);
		logic.setOwner(this.m_employee);
		List list = logic.getOutstanding();		
		int i = 0;
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtEmpReceivable pmt = (PmtEmpReceivable) iterator.next();
			getModel().addRow(
					new Object[] {new Integer(++i),
							pmt.getPayTo().getEmployeeNo(),
							pmt.getPayTo().igetFullName(), 
							pmt.getReferenceNo(),
							pmt.getTransactionDate(),
							pmt.getCurrency().getSymbol(),
							new Double(pmt.getAmount()),
							new Double(pmt.getExchangeRate()),
							pmt.getEmpApproved(), 
							pmt.statusInString(),
							new Double(logic.getEmployeeReceivablesReceived(pmt)), 
							pmt });
		}
		BeginningEmpReceivableBusinessLogic bbLogic = new BeginningEmpReceivableBusinessLogic(m_conn,m_sessionid,m_employee.getIndex());
		List bbList = bbLogic.getOutstanding();
		Iterator iterator1 = bbList.iterator();					
		while(iterator1.hasNext()){
			BeginningEmpReceivable bb = (BeginningEmpReceivable) iterator1.next();
			Employee emp = bb.getEmployee();
				
			double accumulate = bbLogic.getAccumulatedPayable(bb);				
			if (accumulate<bb.getAccValue()){
				getModel().addRow(new Object[]{ new Integer(++i),
						emp.getEmployeeNo(),
						emp.igetFullName(), 
						bb.getTrans().getReference(),
						bb.getTrans().getTransDate(),
						bb.getCurrency().getSymbol(),
						new Double(bb.getAccValue()),
						new Double(bb.getExchangeRate()),
						"",
						getStatus(bb.getTrans().getStatus()),
						new Double(bbLogic.getAccumulatedPayable(bb)),
						bb});
			}
		}		
	}
	
	String getStatus(short status){
		String strStatus = "";
		if (status==0)
			strStatus = "Not Submitted";
		else if (status==1 || status==2)
			strStatus = "Submitted";
		else 
			strStatus = "Posted";
		return strStatus;
	}	
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			setObject(getModel().getValueAt(rowindex, 11));
		}
	}
	
	protected String getCaption(Object obj){
		if (obj instanceof PmtEmpReceivable) {
			PmtEmpReceivable a = (PmtEmpReceivable) obj;
			return a.getReferenceNo();			
		}else if (obj instanceof BeginningEmpReceivable) {
			BeginningEmpReceivable a= (BeginningEmpReceivable) obj;
			return a.getTrans().getReference();
		}
		return "";
	}
}