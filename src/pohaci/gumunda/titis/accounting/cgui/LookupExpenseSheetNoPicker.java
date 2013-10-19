package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEsDiff;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.PmtESDiff;
import pohaci.gumunda.titis.accounting.entity.RcvESDiff;
import pohaci.gumunda.titis.accounting.logic.BeginningExpensheetDifferenceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.ExpenseSheetBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupExpenseSheetNoPicker extends LookupPicker {
	private static final long serialVersionUID = 1L;

	private Employee employee = null;

	private int type = 0;

	public static int RECEIVE = 1;

	public static int PAYMENT = 2;

	public LookupExpenseSheetNoPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Expense Sheet");
		initData();
		setSize(800, 300);
	}

	public LookupExpenseSheetNoPicker(Connection conn, long sessionid,
			Employee employee, int type) {
		super(conn, sessionid, "Lookup Expense Sheet");
		this.employee = employee;
		this.type = type;
		initData();
		setSize(800, 300);
	}

	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("ES No");
		getModel().addColumn("ES Date");
		getModel().addColumn("ES Type");
		getModel().addColumn("Currency");
		getModel().addColumn("Total Amount");
		getModel().addColumn("Originator");
		getModel().addColumn("Approved by");
		getModel().addColumn("Created by");

		int no = 0;
		ExpenseSheetBusinessLogic esLogic = new ExpenseSheetBusinessLogic(
				m_conn, m_sessionid, employee, type);
		List list = esLogic.getOutstandingList();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			ExpenseSheet es = (ExpenseSheet) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), es,
							es.getTransactionDate(), es.getEsProjectType(),
							es.getCurrency(), new Double(es.getAmount()),
							es.getEmpOriginator(), es.getEmpApproved(),
							es.getEmpReceived(), });
		}

		BeginningExpensheetDifferenceBusinessLogic bbLogic = new BeginningExpensheetDifferenceBusinessLogic(
				m_conn, m_sessionid, employee, type);
		list = bbLogic.getOutstandingList();

		iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningEsDiff es = (BeginningEsDiff) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), es,
							es.getTrans().getTransDate(), "Beginning Balance",
							es.getCurrency(), new Double(es.getAccValue()), "",
							"", "", });
		}

		/*try {
		 GenericMapper mapnya = MasterMap.obtainMapperFor(ExpenseSheet.class);
		 mapnya.setActiveConn(m_conn);			
		 String strWhere = IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_ES_OWNER + "=" + m_index;
		 System.err.println(strWhere);
		 Object[] listData=mapnya.doSelectWhere(strWhere).toArray();
		 DecimalFormat formatDesimal = new DecimalFormat("#,###.00"); 
		 for(int i = 0; i < listData.length;  i ++) {
		 ExpenseSheet es=(ExpenseSheet)listData[i];
		 if (m_polanya!=0){
		 PmtCAProject pmt1=null;
		 PmtCAIOUProjectSettled pmt2=null;
		 PmtCAOthers pmt3=null;
		 PmtCAIOUOthersSettled pmt4=null;
		 double amount1 = 0,amount2;
		 amount2=es.getAmount();
		 String a=es.getEsProjectType();
		 if (a.equalsIgnoreCase("General")){
		 if (es.getPmtCaProject()!=null){
		 pmt1=es.getPmtCaProject();
		 amount1=pmt1.getAmount();
		 }
		 else if (es.getPmtCaOthers()!=null){
		 pmt3=es.getPmtCaOthers();
		 amount1=pmt3.getAmount();
		 }
		 }
		 else{   
		 if (es.getPmtCaIouProjectSettled()!=null){
		 pmt2=es.getPmtCaIouProjectSettled();
		 amount1=pmt2.getAmount();
		 }
		 else if (es.getPmtCaIouOthersSettled()!=null){
		 pmt4=es.getPmtCaIouOthersSettled();
		 amount1=pmt4.getAmount();
		 }					
		 }					
		 if ((amount1>amount2) && (m_polanya==1)){//ini receive
		 if (!cekRecHadPay(es))
		 getModel().addRow(new Object[]{						
		 String.valueOf((i + 1)),		 
		 es,
		 es.getTransactionDate(),
		 es.getEsProjectType(),
		 es.getCurrency(),
		 formatDesimal.format(es.getAmount()),
		 es.getEmpOriginator(),
		 es.getEmpApproved(),
		 es.getEmpReceived(),
		 });						
		 }
		 else if ((amount1<amount2) && (m_polanya==2)){//ini payment
		 if (!cekPmtHadPay(es))
		 getModel().addRow(new Object[]{						
		 String.valueOf((i + 1)),		 
		 es,
		 es.getTransactionDate(),
		 es.getEsProjectType(),
		 es.getCurrency(),
		 formatDesimal.format(es.getAmount()),
		 es.getEmpOriginator(),
		 es.getEmpApproved(),
		 es.getEmpReceived(),
		 });							
		 }
		 }
		 
		 else
		 getModel().addRow(new Object[]{						
		 String.valueOf((i + 1)),		 
		 es,
		 es.getTransactionDate(),
		 es.getEsProjectType(),
		 es.getCurrency(),
		 formatDesimal.format(es.getAmount()),
		 es.getEmpOriginator(),
		 es.getEmpApproved(),
		 es.getEmpReceived(),
		 });
		 }			
		 BeginningExpensheetDifferenceBusinessLogic bbLogic = new BeginningExpensheetDifferenceBusinessLogic(m_conn, m_sessionid,m_index,m_polanya);
		 List bbList = bbLogic.getOutstanding();
		 Iterator iterator = bbList.iterator();
		 int i=0;
		 while(iterator.hasNext()){
		 BeginningEsDiff bb = (BeginningEsDiff) iterator.next();			    	
		 getModel().addRow(new Object[]{String.valueOf((++i)),bb,bb.getTrans().getTransDate(),"Beginning Balance",
		 bb.getCurrency(),formatDesimal.format(bb.getAccValue()),"","","",""});				
		 }
		 }
		 catch (Exception ex) {
		 JOptionPane.showMessageDialog(this, ex.getMessage(),
		 "Warning", JOptionPane.WARNING_MESSAGE);
		 }*/
	}

	public boolean cekRecHadPay(ExpenseSheet es) {
		boolean cek = false;
		GenericMapper mapperRcvEsDiff = MasterMap
				.obtainMapperFor(RcvESDiff.class);
		mapperRcvEsDiff.setActiveConn(m_conn);
		String strWhere = IDBConstants.ATTR_ES_NO + "=" + es.getIndex();
		List list = mapperRcvEsDiff.doSelectWhere(strWhere);
		int jum = list.size();
		for (int i = 0; i < jum; i++) {
			RcvESDiff rcv = (RcvESDiff) list.get(i);
			if (rcv.getStatus() == 3)
				cek = true;
		}
		return cek;

	}

	public boolean cekPmtHadPay(ExpenseSheet es) {
		boolean cek = false;
		GenericMapper mapperPmtEsDiff = MasterMap
				.obtainMapperFor(PmtESDiff.class);
		mapperPmtEsDiff.setActiveConn(m_conn);
		String strWhere = IDBConstants.ATTR_ES_NO + "=" + es.getIndex();
		System.err.println(strWhere);
		List list = mapperPmtEsDiff.doSelectWhere(strWhere);
		int jum = list.size();
		for (int i = 0; i < jum; i++) {
			PmtESDiff pmt = (PmtESDiff) list.get(i);
			if (pmt.getStatus() == 3)
				cek = true;
		}
		return cek;
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			setObject(getModel().getValueAt(rowindex, 1));
		}
	}

	protected String getCaption(Object obj) {
		if (obj instanceof ExpenseSheet) {
			ExpenseSheet temp = (ExpenseSheet) obj;
			return temp.getReferenceNo();
		} else if (obj instanceof BeginningEsDiff) {
			BeginningEsDiff temp = (BeginningEsDiff) obj;
			return temp.getTrans().getReference();
		}
		return "";

	}

}
