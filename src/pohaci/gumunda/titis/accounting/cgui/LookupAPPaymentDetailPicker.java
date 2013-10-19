package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
//import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupAPPaymentDetailPicker extends LookupPicker {
	private static final long serialVersionUID = 1L;		
	Object m_obj;
	public LookupAPPaymentDetailPicker(Connection conn, long sessionid,Object obj, PurchaseApPmt purchaseAPPayment) {
		super(conn, sessionid, "Lookup Account Payable Payment");		
		m_obj = obj;
	    initData();
	    setSize(800, 300);		
	}
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Purch Rcpt No");
		getModel().addColumn("Supplier");
		getModel().addColumn("Project Code");
		getModel().addColumn("Voucher No");
		getModel().addColumn("Voucher Date");
		getModel().addColumn("Currency");
		getModel().addColumn("Amount");
		getModel().addColumn("Approved by");
		getModel().addColumn("Status");
		try {	
			GenericMapper mapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
			mapper.setActiveConn(m_conn);
			
			String strWhere = "";			
			if (m_obj instanceof PurchaseReceipt) {
				PurchaseReceipt tempClass = (PurchaseReceipt) m_obj;
				strWhere = IDBConstants.ATTR_PURCHASE_RECEIPT + " = " + tempClass.getIndex() + " AND STATUS = 3";
				List list = mapper.doSelectWhere(strWhere);			
				PurchaseApPmt[] result = (PurchaseApPmt[]) list.toArray(new PurchaseApPmt[list.size()]);				
				for(int i = 0; i < result.length;  i ++) {
					String empApprove = "";
					if (result[i].getEmpApproved()!=null)
						empApprove = result[i].getEmpApproved().getFirstName() + " " + result[i].getEmpApproved().getMidleName() + " " +
						result[i].getEmpApproved().getLastName();
					getModel().addRow(new Object[]{
							String.valueOf((i + 1)),
							result[i].getPurchaseReceipt(),
							result[i].getPurchaseReceipt().getSupplier(),
							result[i].getPurchaseReceipt().getProject(),
							result[i].getReferenceNo(),
							result[i].getTransactionDate(), 
							result[i].getAppMtCurr(),
							new Double(result[i].getAppMtAmount()),
							empApprove,
							getStatus(result[i].getPurchaseReceipt().getStatus())});
				}				
			}
			
			else if (m_obj instanceof BeginningAccountPayable) {
				BeginningAccountPayable tempClass = (BeginningAccountPayable) m_obj;
				strWhere = IDBConstants.ATTR_BEGINNING_BALANCE + " = " + tempClass.getIndex() + " AND STATUS = 3";
				List list = mapper.doSelectWhere(strWhere);
				Iterator iter = list.iterator();
				int i=0;
				while (iter.hasNext()) {
					PurchaseApPmt rcv = (PurchaseApPmt) iter.next();
					String empApprove = "";
					if (rcv.getEmpApproved()!=null)
						empApprove = rcv.getEmpApproved().getFirstName() + " " + rcv.getEmpApproved().getMidleName() + " " +
						rcv.getEmpApproved().getLastName();
					getModel().addRow(new Object[]{
							String.valueOf((++i)),
							rcv.getBeginningBalance(),
							rcv.getBeginningBalance().getPartner(),
							rcv.getBeginningBalance().getProject(),
							rcv.getTrans().getReference(),
							rcv.getTrans().getTransDate(),
							rcv.getAppMtCurr(),								
							new Double(rcv.getAppMtAmount()),
							empApprove,
							getStatus(rcv.getStatus())});
				}				
			}			
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	private String getStatus(int status) {
		String strStatus = "";
		if (status==0)
			strStatus = "Not Submitted";
		else if (status==1)
			strStatus = "Submitted";
		else if (status==2)
			strStatus = "Submitted";
		else if (status == 3)
			strStatus = "Posted";
		return strStatus;
	}
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
	    if(rowindex != -1) {
	      Object obj = getModel().getValueAt(rowindex, 1);
	      setObject(obj);
	    }
	}
}

