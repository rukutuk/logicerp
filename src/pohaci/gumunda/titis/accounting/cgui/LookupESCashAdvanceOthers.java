package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.accounting.logic.BeginningESBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUOthersSettledBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAOthersBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupESCashAdvanceOthers extends LookupPicker {
	private static final long serialVersionUID = 1L;

	private Employee owner;

	public LookupESCashAdvanceOthers(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Expense Sheet Cash Advance");
		initData();
		setSize(800, 300);
	}

	public LookupESCashAdvanceOthers(Connection conn, long sessionid,
			Employee owner) {
		super(conn, sessionid, "Lookup Expense Sheet Cash Advance");
		this.owner = owner;
		initData();
		setSize(800, 300);
	}

	void initData() {
		setColumn();
		int no = 0;

		CAOthersBusinessLogic logic1 = new CAOthersBusinessLogic(
				m_conn, m_sessionid, owner);
		List list = logic1.getOutstandingList();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAOthers pmt = (PmtCAOthers) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), pmt,
							pmt.getCashAccount(), pmt.getBankAccount(),
							new Double(pmt.getAmount()),
							new Double(pmt.getExchangeRate()), pmt.getUnit(),
							"General" });
		}

		CAIOUOthersSettledBusinessLogic logic2 = new CAIOUOthersSettledBusinessLogic(
				m_conn, m_sessionid, owner);
		list = logic2.getOutstandingList();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAIOUOthersSettled pmt = (PmtCAIOUOthersSettled) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), pmt,
							pmt.getCashAccount(), pmt.getBankAccount(),
							new Double(pmt.getAmount()),
							new Double(pmt.getExchangeRate()),
							pmt.getPmtcaiouothers().getUnit(), "IOU" });
		}

		BeginningESBusinessLogic bbLogic = new BeginningESBusinessLogic(
				this.m_conn, this.m_sessionid, owner,
				IConstants.ATTR_VARS_CA_OTHER);
		list = bbLogic.getOutstanding();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningCashAdvance bb = (BeginningCashAdvance) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), bb, "", "",
							new Double(bb.getAccValue()),
							new Double(bb.getExchangeRate()), bb.getTrans().getUnit(), "BEG.BAL" });
		}

		bbLogic = new BeginningESBusinessLogic(this.m_conn, this.m_sessionid,
				owner, IConstants.ATTR_VARS_CA_IOU_OTHER);
		list = bbLogic.getOutstanding();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningCashAdvance bb = (BeginningCashAdvance) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), bb, "", "",
							new Double(bb.getAccValue()),
							new Double(bb.getExchangeRate()), bb.getTrans().getUnit(), "BEG.BAL" });
		}

		/*try {
		 GenericMapper mapnya = MasterMap.obtainMapperFor(PmtCAOthers.class);
		 mapnya.setActiveConn(m_conn);			
		 String strWhere = IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_PAY_TO + "=" + index;
		 System.err.println(strWhere);
		 Object[] listData=mapnya.doSelectWhere(strWhere).toArray();
		 DecimalFormat formatDesimal = new DecimalFormat("#,###.00"); 
		 int no = 0;
		 for(int i = 0; i < listData.length;i++) {
		 PmtCAOthers pmtCAOthers=(PmtCAOthers)listData[i];
		 strWhere = IDBConstants.ATTR_PMT_CA_OTHERS + "=" + pmtCAOthers.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 if (cekHadPayable(strWhere)){
		 String bankaccount = "";
		 String cashaccount = "";
		 if (pmtCAOthers.getCashAccount()!=null)
		 bankaccount = pmtCAOthers.getCashAccount().getName();
		 if (pmtCAOthers.getBankAccount()!=null)
		 cashaccount = pmtCAOthers.getBankAccount().getName();
		 
		 getModel().addRow(new Object[]{						
		 new Integer(++no),		 
		 pmtCAOthers,
		 cashaccount,
		 bankaccount,
		 formatDesimal.format(pmtCAOthers.getAmount()),
		 formatDesimal.format(pmtCAOthers.getExchangeRate()),
		 pmtCAOthers.getUnit(),
		 "General"
		 });
		 }
		 }
		 mapnya = MasterMap.obtainMapperFor(PmtCAIOUOthersSettled.class);
		 mapnya.setActiveConn(m_conn);
		 strWhere = IDBConstants.ATTR_STATUS + "=3";
		 listData=mapnya.doSelectWhere(strWhere).toArray();			
		 for(int i = 0; i < listData.length;  i ++) {
		 PmtCAIOUOthersSettled pmtCAIOUOthersSettled=(PmtCAIOUOthersSettled)listData[i];
		 if (pmtCAIOUOthersSettled.getPmtcaiouothers()!=null)
		 if (pmtCAIOUOthersSettled.getPmtcaiouothers().getPayTo()!=null)
		 if (pmtCAIOUOthersSettled.getPmtcaiouothers().getPayTo().getIndex()== index){
		 strWhere = IDBConstants.ATTR_PMT_CA_IOU_OTHERS_SETTLED + "=" + pmtCAIOUOthersSettled.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 if (cekHadPayable(strWhere)){
		 getModel().addRow(new Object[]{
		 new Integer(++no),		 
		 pmtCAIOUOthersSettled,
		 pmtCAIOUOthersSettled.getCashAccount(),
		 pmtCAIOUOthersSettled.getBankAccount(),
		 formatDesimal.format(pmtCAIOUOthersSettled.getAmount()),
		 formatDesimal.format(pmtCAIOUOthersSettled.getExchangeRate()),
		 pmtCAIOUOthersSettled.getUnit(),
		 "IOU"				
		 });
		 }
		 }
		 }
		 
		 
		 mapnya = MasterMap.obtainMapperFor(BeginningCashAdvance.class);
		 mapnya.setActiveConn(m_conn);
		 strWhere = IDBConstants.ATTR_STATUS + "=3";
		 listData=mapnya.doSelectWhere(strWhere).toArray();		
		 for (int i=0;i<listData.length;i++){
		 BeginningCashAdvance bb = (BeginningCashAdvance) listData[i];	
		 strWhere = IDBConstants.ATTR_BEGINNING_BALANCE + "=" + bb.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 if (cekHadPayable(strWhere)){
		 getModel().addRow(new Object[]{
		 new Integer(++no),		 
		 bb,
		 "",
		 "",								        
		 formatDesimal.format(bb.getAccValue()),
		 formatDesimal.format(bb.getExchangeRate()),
		 "",
		 ""
		 });			
		 }
		 }			
		 }
		 catch (Exception ex) {
		 JOptionPane.showMessageDialog(this, ex.getMessage(),
		 "Warning", JOptionPane.WARNING_MESSAGE);
		 }*/
	}

	private void setColumn() {
		getModel().addColumn("No.");
		getModel().addColumn("ReferenceNo");
		getModel().addColumn("CashAccount");
		getModel().addColumn("BankAccount");
		getModel().addColumn("Amount");
		getModel().addColumn("ExchangeRate");
		getModel().addColumn("Unit");
		getModel().addColumn("Cash Advance Type");
	}

	public boolean cekHadPayable(String strWhere) {
		boolean cek = false;
		GenericMapper mapnya = MasterMap.obtainMapperFor(ExpenseSheet.class);
		mapnya.setActiveConn(m_conn);
		List list = mapnya.doSelectWhere(strWhere);
		System.err.println(strWhere);
		if (list.size() == 0)
			cek = true;
		return cek;
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 1);
			setObject(obj);
		}
	}

	protected String getCaption(Object obj) {
		if (obj instanceof PmtCAOthers) {
			PmtCAOthers PmtCAProject = (PmtCAOthers) obj;
			return PmtCAProject.getReferenceNo() == null ? "" : PmtCAProject
					.getReferenceNo();
		} else if (obj instanceof PmtCAIOUOthersSettled) {
			PmtCAIOUOthersSettled PmtCAProject = (PmtCAIOUOthersSettled) obj;
			return PmtCAProject.getReferenceNo() == null ? "" : PmtCAProject
					.getReferenceNo();
		} else if (obj instanceof BeginningCashAdvance) {
			BeginningCashAdvance a = (BeginningCashAdvance) obj;
			return a.getTrans().getReference() == null ? "" : a.getTrans()
					.getReference();
		} else
			return null;
	}

}
