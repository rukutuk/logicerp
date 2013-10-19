package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtEmpInsDet;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtEmpInsurance;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupEmpInsPayablePicker extends LookupPicker {
	private static final long serialVersionUID = 1L;

	String ATR_JOURNAL;

	private Unit unit;

	private Vector paidEmpInsuranceIdxVector;

	public LookupEmpInsPayablePicker(Connection conn, long sessionid, Unit unit) {
		super(conn, sessionid, "Employee Insurance payable");
		setSize(800, 300);
		this.unit = unit;
		initPaidEmployeeInsurancePayable();
		setColumn();
		initData();
	}
	
	private void setColumn() {
		getModel().addColumn("No");
    	getModel().addColumn("Date");
    	getModel().addColumn("Description");
    	getModel().addColumn("Unit Code");   
    	getModel().addColumn("Account");
    	getModel().addColumn("Amount");
	}

	private void initPaidEmployeeInsurancePayable() {
		// cara yang aneh
		// tapi sudahlah...
		
		paidEmpInsuranceIdxVector = new Vector();
    	GenericMapper mapper = MasterMap.obtainMapperFor(PayrollPmtEmpInsDet.class);
    	mapper.setActiveConn(m_conn);
    	
		List payrollPaymentDetailList =  mapper.doSelectAll();
		Iterator iterator = payrollPaymentDetailList.iterator();
		while(iterator.hasNext()){
			PayrollPmtEmpInsDet payrollPmtEmpInsDet = (PayrollPmtEmpInsDet) iterator.next();
			PayrollPmtEmpInsurance payrollPmtEmpInsurance = payrollPmtEmpInsDet.getPayrollPmtEmpIns();
			
			if(payrollPmtEmpInsurance!=null){
				if(payrollPmtEmpInsurance.getStatus()==StateTemplateEntity.State.POSTED){
					if(payrollPmtEmpInsDet.getTrans()!=null)
						paidEmpInsuranceIdxVector.add(new Long(payrollPmtEmpInsDet.getTrans().getIndex()));
				}
			}
		}
	}

	void initData() {
		String journalStandardListOfIndex = getJournalStandardListOfIndex();
		String accountAllowedListOfIndex = geAccountAllowedListOfIndex();

		GenericMapper mapper = MasterMap.obtainMapperFor(Transaction.class);
		mapper.setActiveConn(m_conn);

		String whereClause = IDBConstants.ATTR_UNIT + "=" + unit.getIndex()
				+ " AND " + IDBConstants.ATTR_STATUS + "=3" + " AND "
				+ IDBConstants.ATTR_TRANSACTION_CODE + " IN ("
				+ journalStandardListOfIndex + ") AND " 
				+ IDBConstants.ATTR_ISVOID + "=false";
		
		List transactionList = mapper.doSelectWhere(whereClause);

		Iterator iterator = transactionList.iterator();
		int no = 1;
		while (iterator.hasNext()) {
			Transaction transaction = (Transaction) iterator.next();

			if (isPaid(transaction))
				continue;

			mapper = MasterMap.obtainMapperFor(TransactionDetail.class);
			mapper.setActiveConn(m_conn);

			List detailList = mapper.doSelectWhere("TRANS="
					+ transaction.getIndex() + " AND ACCOUNT IN ("
					+ accountAllowedListOfIndex + ")");
			Iterator iter = detailList.iterator();
			while (iter.hasNext()) {
				TransactionDetail detail = (TransactionDetail) iter.next();
				detail.setTransaction(transaction);

				getModel().addRow(
						new Object[] { new Integer(no),
								transaction.getPostedDate(), transaction, unit,
								detail.getAccount().getName(),
								new Double(detail.getValue()), detail });

				no++;
			}
		}
	}

	private boolean isPaid(Transaction transaction) {
		return paidEmpInsuranceIdxVector
				.contains(new Long(transaction.getIndex()));
	}

	private String geAccountAllowedListOfIndex() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);

		List journalStandardSettingList = helper
				.getJournalStandardSettingWithAccount(IConstants.PAYROLL_PAYMENT_EMPLOYEE_INSURANCE);

		String listOfIndex = "";
		Iterator iterator = journalStandardSettingList.iterator();
		while (iterator.hasNext()) {
			JournalStandardSetting journalStandardSetting = (JournalStandardSetting) iterator
					.next();
			JournalStandard journalStandard = journalStandardSetting
					.getJournalStandard();

			if (journalStandard != null) {
				JournalStandardAccount[] journalStandardAccounts = journalStandard
						.getJournalStandardAccount();

				for (int i = 0; i < journalStandardAccounts.length; i++) {
					if (journalStandardAccounts[i].getBalance() == 0) // maksa
						listOfIndex += ", "
								+ journalStandardAccounts[i].getAccount()
										.getIndex();
				}
			}
		}

		listOfIndex = listOfIndex.replaceFirst(", ", "");

		return listOfIndex;
	}

	private String getJournalStandardListOfIndex() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);

		String whereClausa = 
				  IDBConstants.ATTR_APPLICATION + "='"
				+ IConstants.PAYROLL_VERIFICATION_PAYCHEQUES + "' OR "
				+ IDBConstants.ATTR_APPLICATION + "='"
				+ IConstants.PAYROLL_VERIFICATION_INSURANCE_ALLOWANCE + "' OR " 
				+ IDBConstants.ATTR_APPLICATION + "='" 
				+ IConstants.PAYROLL_PAYMENT_SALARY_UNIT + "'";

		List journalStandardSettingList = helper
				.getJournalStandardSettingList(whereClausa);

		String journalStandardListOfIndex = "";
		Iterator iterator = journalStandardSettingList.iterator();
		while (iterator.hasNext()) {
			JournalStandardSetting journalStandardSetting = (JournalStandardSetting) iterator
					.next();

			journalStandardListOfIndex += journalStandardSetting
					.getJournalStandard().getIndex();

			if (iterator.hasNext())
				journalStandardListOfIndex += ", ";
		}
		
		// khusus beginning balance
		AccountingSQLSAP sql = new AccountingSQLSAP();
		BeginningBalance begBal = sql.getBeginningBalance((short)1, m_conn);
		JournalStandard jsBB = null;
		if (begBal!=null)
			jsBB = begBal.getJournalStandard();
		
		if (jsBB!=null){
			if(journalStandardListOfIndex.equals(""))
				journalStandardListOfIndex += jsBB.getIndex();
			else
				journalStandardListOfIndex += (", " + jsBB.getIndex());
		}

		return journalStandardListOfIndex;
	}

	int rowindex;

	public void select() {
		rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 6);
			setObject(obj);
		}
	}
}
