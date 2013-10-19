package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.logic.BeginningAccountPayableBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.PurchaseReceiptBusinessLogic;

public class LookupPurchasePicker extends LookupPicker {
	private static final long serialVersionUID = 1L;
	
	public LookupPurchasePicker(Connection conn, long sessionid) {
	    super(conn, sessionid, "Lookup Purchase Receipt");	   
	    initData();
	    setSize(800, 300);
	  }
	
	private void initData() {
		getModel().addColumn("No.");
	    getModel().addColumn("Date");
	    getModel().addColumn("Purchase Receipt No.");
	    getModel().addColumn("Project Code");
	    getModel().addColumn("Invoice No.");
	    getModel().addColumn("Supplier");
	    getModel().addColumn("Submitted Date");
	    getModel().addColumn("Currency");
	    getModel().addColumn("Total Paid");
	    getModel().addColumn("Status");	    
	     
	    PurchaseReceiptBusinessLogic logic = new PurchaseReceiptBusinessLogic(this.m_conn, this.m_sessionid);
	    List list = logic.getOutstandingList();
	    Iterator iterator = list.iterator();
	    int i = 0;
	    while(iterator.hasNext()){
	    	PurchaseReceipt pr = (PurchaseReceipt) iterator.next();
	    	double paid = logic.getAccumulatedPayment(pr);
	    	getModel().addRow(new Object[]{new Integer(++i), pr.getTransactionDate(), pr, 
	    			pr.getProject(), pr.getInvoice(), pr.getSupplier(), pr.getSubmitDate(),
	    			pr.getApCurr(), new Double(paid), pr.vgetStatus()});
	    }
	    
	    BeginningAccountPayableBusinessLogic bbLogic = new BeginningAccountPayableBusinessLogic(this.m_conn, this.m_sessionid);
	    List bbList = bbLogic.getOutstanding();
	    iterator = bbList.iterator();
	    while(iterator.hasNext()){
	    	BeginningAccountPayable bb = (BeginningAccountPayable) iterator.next();	    	
	    	double accumulate = bbLogic.getAccumulatedPayable(bb);	    	
	    		getModel().addRow(new Object[]{
	    				new Integer(++i), 
	    				bb.getTrans().getTransDate(), 
	    				bb, 
	    				bb.getProject().getCode(),
	    				bb.getTrans().getReference(), 
	    				bb.getPartner().toString(),
	    				bb.getTrans().getPostedDate(),
	    				bb.getCurrency(), 
	    				new Double(accumulate),
	    				bb.getTrans().vgetStatus()}); 	    	
	    	}
	}	

	void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 2);
			setObject(obj);
		}
	}

}
