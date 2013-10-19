package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.logic.BeginningESBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUProjecSettledtBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAProjectBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupESCashAdvanceProject extends LookupPicker {
	private static final long serialVersionUID = 1L;

	private Employee owner;

	public LookupESCashAdvanceProject(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Expense Sheet Cash Advance for Project");
		initData();
		setSize(800, 300);
	}

	public LookupESCashAdvanceProject(Connection conn, long sessionid,
			Employee owner) {
		super(conn, sessionid, "Lookup Expense Sheet Cash Advance for Project");
		this.owner = owner;
		initData();
		setSize(800, 300);
	}

	void initData() {
		setColumn();
		int no = 0;

		CAProjectBusinessLogic logic1 = new CAProjectBusinessLogic(
				m_conn, m_sessionid, owner);
		List list = logic1.getOutstandingList();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAProject pmt = (PmtCAProject) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), pmt,
							pmt.getCashAccount(), pmt.getBankAccount(),
							pmt.getProject(), new Double(pmt.getAmount()),
							new Double(pmt.getExchangeRate()), pmt.getUnit(),
							"General" });
		}

		CAIOUProjecSettledtBusinessLogic logic2 = new CAIOUProjecSettledtBusinessLogic(
				m_conn, m_sessionid, owner);
		list = logic2.getOutstandingList();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			PmtCAIOUProjectSettled pmt = (PmtCAIOUProjectSettled) iterator
					.next();
			getModel().addRow(
					new Object[] { new Integer(++no), pmt,
							pmt.getCashAccount(), pmt.getBankAccount(),
							pmt.getPmtcaiouproject().getProject(),
							new Double(pmt.getAmount()),
							new Double(pmt.getExchangeRate()),
							pmt.getPmtcaiouproject().getUnit(), "IOU" });
		}

		BeginningESBusinessLogic bbLogic = new BeginningESBusinessLogic(
				this.m_conn, this.m_sessionid, owner,
				IConstants.ATTR_VARS_CA_PROJECT);
		list = bbLogic.getOutstanding();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningCashAdvance bb = (BeginningCashAdvance) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), bb, "", "",
							bb.getProject(), new Double(bb.getAccValue()),
							new Double(bb.getExchangeRate()), bb.getTrans().getUnit(), "BEGINNING BALANCE" });
		}

		bbLogic = new BeginningESBusinessLogic(this.m_conn, this.m_sessionid,
				owner, IConstants.ATTR_VARS_CA_IOU_PROJECT);
		list = bbLogic.getOutstanding();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningCashAdvance bb = (BeginningCashAdvance) iterator.next();
			getModel().addRow(
					new Object[] { new Integer(++no), bb, "", "",
							bb.getProject(), new Double(bb.getAccValue()),
							new Double(bb.getExchangeRate()),  bb.getTrans().getUnit(), "BEGINNING BALANCE" });
		}

		/*
		 * try { GenericMapper mapnya =
		 * MasterMap.obtainMapperFor(PmtCAProject.class);
		 * mapnya.setActiveConn(m_conn); String strWhere =
		 * IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_PAY_TO + "=" +
		 * index; Object[] listData=mapnya.doSelectWhere(strWhere).toArray();
		 * DecimalFormat formatDesimal = new DecimalFormat("#,###.00"); int no =
		 * 0; for(int i = 0; i < listData.length; i ++) { PmtCAProject
		 * result=(PmtCAProject)listData[i]; getModel().addRow(new Object[]{ new
		 * Integer(++no), result, result.getCashAccount(),
		 * result.getBankAccount(), result.getProject(),
		 * formatDesimal.format(result.getAmount()),
		 * formatDesimal.format(result.getExchangeRate()), result.getUnit(),
		 * "General" }); }
		 * 
		 * //Di bawah ini proses untuk mencari nilai pada PMTCAIOUProject
		 * Settled mapnya =
		 * MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
		 * mapnya.setActiveConn(m_conn); strWhere = IDBConstants.ATTR_STATUS +
		 * "=3"; listData=mapnya.doSelectWhere(strWhere).toArray(); for(int i =
		 * 0; i < listData.length; i ++) { PmtCAIOUProjectSettled
		 * pmtCAIOUProjectSettled=(PmtCAIOUProjectSettled)listData[i]; if
		 * (pmtCAIOUProjectSettled.getPmtcaiouproject()!=null) if
		 * (pmtCAIOUProjectSettled.getPmtcaiouproject().getPayTo()!=null) if
		 * (pmtCAIOUProjectSettled.getPmtcaiouproject().getPayTo().getIndex()==index)
		 * getModel().addRow(new Object[]{ new Integer(++no),
		 * pmtCAIOUProjectSettled, pmtCAIOUProjectSettled.getCashAccount(),
		 * pmtCAIOUProjectSettled.getBankAccount(),
		 * pmtCAIOUProjectSettled.getPmtcaiouproject().getProject(),
		 * formatDesimal.format(pmtCAIOUProjectSettled.getAmount()),
		 * formatDesimal.format(pmtCAIOUProjectSettled.getExchangeRate()),
		 * pmtCAIOUProjectSettled.getPmtcaiouproject().getUnit(), "IOU" }); }
		 * 
		 * BeginningESBusinessLogic bbLogic = new
		 * BeginningESBusinessLogic(this.m_conn, this.m_sessionid,
		 * this.index,IConstants.ATTR_VARS_EXPENSE_PROJECT); List bbList =
		 * bbLogic.getOutstanding(); Iterator iterator = bbList.iterator();
		 * while(iterator.hasNext()){ BeginningCashAdvance bb =
		 * (BeginningCashAdvance) iterator.next(); getModel().addRow(new
		 * Object[]{ new Integer(++no), bb, "", "", bb.getProject(),
		 * formatDesimal.format(bb.getAccValue()),
		 * formatDesimal.format(bb.getExchangeRate()), "", "" }); }
		 *  } catch (Exception ex) { JOptionPane.showMessageDialog(this,
		 * ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE); }
		 */
	}

	private void setColumn() {
		getModel().addColumn("No.");
		getModel().addColumn("ReferenceNo");
		getModel().addColumn("CashAccount");
		getModel().addColumn("BankAccount");
		getModel().addColumn("Project");
		getModel().addColumn("Amount");
		getModel().addColumn("ExchangeRate");
		getModel().addColumn("Unit");
		getModel().addColumn("Cash Advance Type");
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 1);
			setObject(obj);
		}
	}

	protected String getCaption(Object obj) {
		if (obj instanceof PmtCAProject) {
			PmtCAProject PmtCAProject = (PmtCAProject) obj;
			return PmtCAProject.getReferenceNo() == null ? "" : PmtCAProject
					.getReferenceNo();
		} else if (obj instanceof PmtCAIOUProjectSettled) {
			PmtCAIOUProjectSettled PmtCAProject = (PmtCAIOUProjectSettled) obj;
			return PmtCAProject.getReferenceNo() == null ? "" : PmtCAProject
					.getReferenceNo();
		} else if (obj instanceof BeginningCashAdvance) {
			BeginningCashAdvance bb = (BeginningCashAdvance) obj;
			return bb.getTrans().getReference();
		} else
			return null;
	}

}
