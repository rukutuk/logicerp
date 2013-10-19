package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.SalesReceived;
import pohaci.gumunda.titis.accounting.logic.SalesInvoiceBusinessLogic;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class LookupSalesARReceivedPicker extends LookupPicker {
	private static final long serialVersionUID = 1L;
	Object object;

	public LookupSalesARReceivedPicker(Connection conn, long sessionid, Object object, SalesReceived entity) {
		super(conn, sessionid, "Lookup Account Receivable Received");
		this.object = object;
		initData(entity);
		setSize(800, 300);
	}

	private void initData(SalesReceived entity) {
		setColumn();

		SalesInvoiceBusinessLogic logic = new SalesInvoiceBusinessLogic(m_conn,
				m_sessionid);
		List list = logic.getAccountReceivableList(this.object, entity);

		Iterator iter = list.iterator();
		int no = 0;
		while (iter.hasNext()) {
			SalesReceived rcv = (SalesReceived) iter.next();

			ProjectData project = null;
			if(rcv.getInvoice()!=null)
				project = rcv.getInvoice().getProject();
			else
				project = rcv.getBeginningBalance().getProject();

			no++;
			getModel().addRow(
					new Object[] {
							new Integer(no++),
							project.getCustomer()
									.getCode(),
							project.getCustomer()
									.getName(), rcv.getReferenceNo(),
							rcv.getTransactionDate(), rcv.getSalesARCurr(),
							new Double(rcv.getSalesARAmount() + rcv.getVatAmount() - rcv.getRetentionAmount()),
							rcv.getEmpReceived(), rcv.vgetStatus() });
		}
	}

	private void setColumn() {
		getModel().addColumn("No.");
		getModel().addColumn("Customer ID");
		getModel().addColumn("Customer Name");
		getModel().addColumn("Receipt No");
		getModel().addColumn("Receipt Date");
		getModel().addColumn("Currency");
		getModel().addColumn("Amount");
		getModel().addColumn("Received By");
		getModel().addColumn("Status");
	}

	void select() {

	}

}
