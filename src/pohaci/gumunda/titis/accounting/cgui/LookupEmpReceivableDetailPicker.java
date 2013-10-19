package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.RcvEmpReceivable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupEmpReceivableDetailPicker extends LookupPicker {

	private static final long serialVersionUID = 1L;

	double accumulatedpayment;

	Object m_obj;

	public LookupEmpReceivableDetailPicker(Connection conn, long sessionid,
			Object obj) {
		super(conn, sessionid, "Lookup Employee Receivable Detail");
		m_obj = obj;
		initData();
		setSize(830, 300);
	}

	void initData() {
		setColumn();
		GenericMapper mapnya = MasterMap
				.obtainMapperFor(RcvEmpReceivable.class);
		mapnya.setActiveConn(m_conn);
		
		String whereClause = "status=3";
		if(m_obj instanceof PmtEmpReceivable){
			PmtEmpReceivable rcv = (PmtEmpReceivable) m_obj;
			whereClause += " and " + IDBConstants.ATTR_EMP_RECEIVABLE + "=" + rcv.getIndex();
		}else{
			BeginningEmpReceivable rcv = (BeginningEmpReceivable) m_obj;
			whereClause += " and " + IDBConstants.ATTR_BEGINNING_BALANCE + "=" + rcv.getIndex();
		}
		
		List list = mapnya.doSelectWhere(whereClause);
		viewList(list);
		
		/*Object[] listData = mapnya.doSelectWhere("status=3").toArray();
		accumulatedpayment = 0;
		for (int i = 0; i < listData.length; i++) {
			RcvEmpReceivable data = (RcvEmpReceivable) listData[i];
			if (m_obj instanceof PmtEmpReceivable) {
				lookupPmtEmpReceivable(i, data);
			} else if (m_obj instanceof BeginningEmpReceivable) {
				lookupBeginningEmpReceivable(i, data);
			}

		}*/
	}

	private void viewList(List list) {
		Iterator iterator = list.iterator();
		int i=0;
		
		Employee emp = null;
		String refNo = null;
		
		if(m_obj instanceof PmtEmpReceivable){
			PmtEmpReceivable ref = (PmtEmpReceivable) m_obj;
			emp = ref.getPayTo();
			refNo = ref.getReferenceNo();
		}else{
			BeginningEmpReceivable ref = (BeginningEmpReceivable) m_obj;
			emp = ref.getEmployee();
			refNo = ref.getTrans().getReference();
		}
		
		accumulatedpayment = 0;
		while(iterator.hasNext()){
			RcvEmpReceivable rcv = (RcvEmpReceivable) iterator.next();
			accumulatedpayment += rcv.getAmount();
			
			
			getModel().addRow(new Object[]{
					new Integer(++i),
					emp.getEmployeeNo(),
					emp.igetFullName(),
					refNo,
					rcv.getReferenceNo(),
					rcv.getSubmitDate(),
					rcv.getCurrency().getSymbol(),
					new Double(rcv.getAmount()),
					rcv.getEmpApproved(),
					rcv.statusInString(),
					rcv
			});
		}
	}

	/*private void lookupPmtEmpReceivable(int i, RcvEmpReceivable data) {
		PmtEmpReceivable tempClass = (PmtEmpReceivable) m_obj;
		if ((tempClass != null) && (data != null)) {
			if (tempClass.getIndex() == tempClass.getIndex()
					&& (data.getStatus() == 3)) {
				accumulatedpayment = accumulatedpayment + data.getAmount();
				getModel().addRow(
						new Object[] {
								new Integer((i + 1)),
								tempClass.getPayTo().getEmployeeNo(),
								tempClass.getPayTo().igetFullName(),
								tempClass.getReferenceNo(),
								data.getReferenceNo(), 
								data.getSubmitDate(),
								data.getCurrency().getSymbol(),
								new Double(data.getAmount()), 
								data.empApproved,
								data.statusInString(), 
								data });
			}
		}
	}

	private void lookupBeginningEmpReceivable(int i, RcvEmpReceivable data) {
		BeginningEmpReceivable tempClass = (BeginningEmpReceivable) m_obj;
		if ((tempClass != null) && (data != null)) {
			if (tempClass.getIndex() == tempClass.getIndex()
					&& (data.getStatus() == 3)) {
				accumulatedpayment = accumulatedpayment + data.getAmount();
				getModel()
						.addRow(
								new Object[] {
										new Integer((i + 1)),
										tempClass.getEmployee().getEmployeeNo(),
										tempClass.getEmployee().igetFullName(),
										tempClass.getTrans().getReference(),
										data.getReferenceNo(),
										data.getSubmitDate(),
										data.getCurrency().getSymbol(),
										new Double(data.getAmount()),
										data.empApproved,
										data.statusInString(), 
										data });
			}
		}
	}*/

	private void setColumn() {
		getModel().addColumn("No.");
		getModel().addColumn("Employee ID");
		getModel().addColumn("Employee Name");
		getModel().addColumn("Voucher No");
		getModel().addColumn("Receipt No");
		getModel().addColumn("Receipt Date");
		getModel().addColumn("Currency");
		getModel().addColumn("Receive Amount");
		getModel().addColumn("Receive by");
		getModel().addColumn("Status");
	}
	
	public double getAccumulatedReceived(Object obj) {
		GenericMapper mapnya = MasterMap
				.obtainMapperFor(RcvEmpReceivable.class);
		mapnya.setActiveConn(m_conn);

		String whereClause = "status=3";
		if (m_obj instanceof PmtEmpReceivable) {
			PmtEmpReceivable rcv = (PmtEmpReceivable) m_obj;
			whereClause += " and " + IDBConstants.ATTR_EMP_RECEIVABLE + "="
					+ rcv.getIndex();
		} else {
			BeginningEmpReceivable rcv = (BeginningEmpReceivable) m_obj;
			whereClause += " and " + IDBConstants.ATTR_BEGINNING_BALANCE + "="
					+ rcv.getIndex();
		}

		List list = mapnya.doSelectWhere(whereClause);
		
		Iterator iterator = list.iterator();
		accumulatedpayment = 0;
		while(iterator.hasNext()){
			RcvEmpReceivable rcv = (RcvEmpReceivable) iterator.next();			
			accumulatedpayment += rcv.getAmount();
		}
		
		return accumulatedpayment;
	}

	/*public double findData(long index) {
		getModel().clearRows();
		GenericMapper mapnya = MasterMap
				.obtainMapperFor(RcvEmpReceivable.class);
		Object[] listData = mapnya.doSelectWhere("status =3").toArray();
		accumulatedpayment = 0;
		for (int i = 0; i < listData.length; i++) {
			RcvEmpReceivable data = (RcvEmpReceivable) listData[i];
			if (data.getEmpReceivable() != null && data.getIndex() == index) {
				PmtEmpReceivable tempClass = data.getEmpReceivable();
				if ((tempClass != null) && (data != null)) {
					if (tempClass.getIndex() == index && data.getStatus() == 3) {
						accumulatedpayment = accumulatedpayment
								+ data.getAmount();
						getModel().addRow(
								new Object[] {
										String.valueOf((i + 1)),
										tempClass.getPayTo().getEmployeeNo(),
										tempClass.getPayTo().getFirstName()
												+ ' '
												+ tempClass.getPayTo()
														.getLastName(),
										tempClass.getReferenceNo(),
										data.getReferenceNo(),
										data.getSubmitDate(),
										data.getCurrency().getSymbol(),
										new Double(data.getAmount()),
										data.empApproved,
										data.statusInString(), data });
					}
				}
			}
		}
		return accumulatedpayment;
	}

	public double getAccumulate(long index) {
		GenericMapper mapnya = MasterMap
				.obtainMapperFor(RcvEmpReceivable.class);
		Object[] listData = mapnya.doSelectWhere(
				"status=3 and beginningbalance=" + index).toArray();
		accumulatedpayment = 0;
		for (int i = 0; i < listData.length; i++) {
			RcvEmpReceivable data = (RcvEmpReceivable) listData[i];
			if (data.getBeginningBalance() != null) {
				accumulatedpayment = accumulatedpayment + data.getAmount();
			}
		}
		return accumulatedpayment;
	}*/

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			RcvEmpReceivable data = (RcvEmpReceivable) getModel().getValueAt(
					rowindex, 9);
			data.setAmount(accumulatedpayment);
			setObject(data);
		}
	}
}
