package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.logic.BeginningAccountReceivableBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.SalesInvoiceBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupInvoicePicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GenericMapper mapper;
	
	public LookupInvoicePicker(Connection conn, long sessionid) {
	    super(conn, sessionid, "Lookup Sales Invoice");
	    mapper = MasterMap.obtainMapperFor(SalesInvoice.class);
	    mapper.setActiveConn(conn);
	    setColumn();
	    initData();
	    setSize(800, 300);
	  }
	
	private void initData() {
		getModel().clearRows();
		
	    SalesInvoiceBusinessLogic logic = new SalesInvoiceBusinessLogic(this.m_conn, this.m_sessionid);
	    List list = logic.getOutstandingList();
	    
	    Iterator iterator = list.iterator();
	    int i = 1;
	    while(iterator.hasNext()){
	    	SalesInvoice inv = (SalesInvoice) iterator.next();
	    	
	    	getModel().addRow(new Object[]{
	    			new Integer(i), 
	    			inv.getTransactionDate(), 
	    			inv, 
    				inv.getCustomer(), 
    				inv.getUnit(), 
    				inv.getActivity(), 
    				inv.getDepartment(), 
    				inv.getProject().getPONo(), 
    				inv.getProject().getWorkDescription(), 
    				new Double(inv.getSalesAmount()), 
    				inv.vgetStatus()});
	    	
	    	i++;
	    }
	    
	    BeginningAccountReceivableBusinessLogic bbLogic = new BeginningAccountReceivableBusinessLogic(this.m_conn, this.m_sessionid);
	    List bbList = bbLogic.getOutstanding();
	    iterator = bbList.iterator();
	    while(iterator.hasNext()){
	    	BeginningAccountReceivable bb = (BeginningAccountReceivable) iterator.next();
	    	getModel().addRow(new Object[]{
	    			new Integer(i), 
	    			bb.getTrans().getTransDate(),
	    			bb, 
	    			bb.getCustomer(),
	    			bb.getProject().getUnit(),
    				bb.getProject().getActivity(),
    				bb.getProject().getDepartment(),
    				bb.getProject().getPONo(), 
    				bb.getProject().getWorkDescription(), 
    				new Double(bb.getAccValue()), 
    				bb.getTrans().vgetStatus()});
	    	i++;
	    }
	}

	private void setColumn() {
		getModel().addColumn("No.");
	    getModel().addColumn("Date");
	    getModel().addColumn("Invoice No.");
	    getModel().addColumn("Customer");
	    getModel().addColumn("Unit Code");
	    getModel().addColumn("Activity Code");
	    getModel().addColumn("Department");
	    getModel().addColumn("SO/PO/Contract No.");
	    getModel().addColumn("Work Description");
	    getModel().addColumn("Total Amount");
	    getModel().addColumn("Status");
	}
	
	public void refreshData(){
		initData();
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		  if(rowindex != -1) {
			  Object obj = getModel().getValueAt(rowindex, 2);
			  setObject(obj);
		  }
	}
}
