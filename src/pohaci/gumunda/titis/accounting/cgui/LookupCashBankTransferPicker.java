package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.PmtUnitBankCashTrans;
import pohaci.gumunda.titis.accounting.logic.PaymentUnitBankCashTransferBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupCashBankTransferPicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LookupCashBankTransferPicker(Connection conn, long sessionid) {
	    super(conn, sessionid, "Lookup Cash/Bank Transfer List");
	    setColumn();
	    initData();
	    setSize(800, 300);
	  }

	void initData() {
		getModel().clearRows();
		  
	    PaymentUnitBankCashTransferBusinessLogic logic = new PaymentUnitBankCashTransferBusinessLogic(m_conn, m_sessionid);
	    List list = new ArrayList();
		try {
			list = logic.getOutstandingList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    Iterator iterator = list.iterator();
	    int no = 0;
	    while(iterator.hasNext()){
	    	PmtUnitBankCashTrans data = (PmtUnitBankCashTrans) iterator.next();
	    	
	    	CashBankAccount source = null;
	    	CashBankAccount target = null;
	    	if(data.isSourceBank())
	    		source = data.getBankAccount();
	    	else
	    		source = data.getCashAccount();
	    	if(data.isReceivingBank())
	    		target = data.getRcvBankAccount();
	    	else
	    		target = data.getRcvCashAccount();
	    	
	    	getModel().addRow(new Object[]{ 
	    			new Integer(no++),
					data.getReferenceNo(),
					data.getTransactionDate(), 
					source,
					source.getUnit(),
	    			target,
	    			target.getUnit(),
					data.getCurrency().getSymbol(),
					new Double(data.getAmount()),
					data.getEmpApproved(),
					data.statusInString(), 
					data });
	    }
	 }
	  
	public void refreshData(){
		initData();
	}

	private void setColumn() {
		getModel().addColumn("No.");
	    getModel().addColumn("Voucher No");
	    getModel().addColumn("Voucher Date");
	    getModel().addColumn("Transfer From");
	    getModel().addColumn("From Unit Code");
	    getModel().addColumn("Transfer To");
	    getModel().addColumn("To Unit Code");
	    getModel().addColumn("Currency");
	    getModel().addColumn("Amount");
	    getModel().addColumn("Approved by");
	    getModel().addColumn("Status");
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			setObject(getModel().getValueAt(rowindex, 11));
		}
	}
	  
	protected String getCaption(Object object) {
		PmtUnitBankCashTrans obj = (PmtUnitBankCashTrans) object;
		return obj.getReferenceNo() == null ? "" : obj.getReferenceNo();
	}

	public void findData(long index) {
		GenericMapper mapnya = MasterMap
				.obtainMapperFor(PmtUnitBankCashTrans.class);
		// UnitBankCashTransferLoader loader= new
		// UnitBankCashTransferLoader(m_conn,PmtUnitBankCashTrans.class);
		Object obj = mapnya.doSelectByIndex(new Long(index));
		setObject(obj);

	}

}

