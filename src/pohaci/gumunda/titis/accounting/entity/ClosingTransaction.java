/**
 * 
 */
package pohaci.gumunda.titis.accounting.entity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 * 
 */
public class ClosingTransaction {
	private long autoindex;

	private Date transactionDate;

	private String referenceNo;

	private Transaction trans;

	private Date periodFrom;

	private Date periodTo;

	private ClosingTransactionDetail[] details;

	private Currency baseCurrency;

	private Unit unit;

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public ClosingTransactionDetail[] getDetails() {
		return details;
	}

	public void setDetails(ClosingTransactionDetail[] detials) {
		this.details = detials;
	}

	public long getAutoindex() {
		return autoindex;
	}

	public void setAutoindex(long index) {
		this.autoindex = index;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Transaction getTrans() {
		return trans;
	}

	public void setTrans(Transaction trans) {
		this.trans = trans;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public static List igetAccount(Connection connection) {
		List resultList = new ArrayList();

		GenericMapper mapper = MasterMap.obtainMapperFor(Account.class);
		mapper.setActiveConn(connection);
		resultList = mapper
				.doSelectWhere("CATEGORY>=3 AND ISGROUP=FALSE ORDER BY ACCOUNTCODE");

		return resultList;
	}

	public static BigDecimal igetDebitValue(Connection connection,
			Account account, Date dateFrom, Date dateTo) {
		AccountingSQLSAP sql = new AccountingSQLSAP();

		try {
			return sql.getPeriodicDebitValueByAccount(connection, account,
					dateFrom, dateTo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
	}

	private final TransactionDetail createNewTransDet(Account ac, double amt,
			Currency curr, double ex) {
		return new TransactionDetail(ac, amt, curr, ex, getUnit(), -1);
	}

	private Transaction createNewTrans(JournalStandard journalStandard) {
		Date now = new Date();
		return new Transaction("Closing Transaction", getTransactionDate(),
				now, null, getReferenceNo(), journalStandard.getJournal(),
				journalStandard, (short) 2, getUnit()); // status diganti == 2
		// UNTUK SEMENTARA UNIT-NYA NULL
		// GA TAU KEDEPANNYA
	}

	public void submit(long sessionId, Connection connection) throws Exception {
		// bikin transaction detail
		ClosingTransactionDetail[] details = getDetails();
		Vector vector = new Vector();
		double[] balance = { 0, 0 };
		for (int i = 0; i < details.length; i++) {
			ClosingTransactionDetail detail = details[i];

			balance[detail.getBalanceCode()] += detail.getAccValue();
			double val = detail.getAccValue();
			if (detail.getAccount().getBalance() != detail.getBalanceCode())
				val = -val;

			TransactionDetail transDet = createNewTransDet(detail.getAccount(),
					val, detail.getCurrency(), detail.getExchangeRate());
			vector.addElement(transDet);
		}

		VariableAccountSetting vas = VariableAccountSetting
				.createVariableAccountSetting(connection, sessionId,
						IConstants.ATTR_VARS_RETAINED_EARNINGS);

		double val = 0;
		double bal = 0;
		if (balance[0] > balance[1]) { // debit > credit
			val = balance[0] - balance[1];
			bal = 1;
		} else {
			val = balance[1] - balance[0];
			bal = 0;
		}

		Account acct = vas.getAccount();
		if (acct.getBalance() != bal)
			val = -val;

		TransactionDetail incSumDet = createNewTransDet(acct, val,
				igetBaseCurrency(), 1);
		vector.addElement(incSumDet);

		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				connection, sessionId, IDBConstants.MODUL_ACCOUNTING);

		List journalStdList = helper
				.getJournalStandardSettingWithAccount(IConstants.CLOSING_TRANSACTION);
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList
				.get(0);
		JournalStandard journal = setting.getJournalStandard();

		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(
				connection, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		String refNo = noGenerator
				.createClosingTransactionReferenceNo(transactionDate);

		setReferenceNo(refNo);

		Transaction trans = createNewTrans(journal);
		TransactionDetail[] transDets = (TransactionDetail[]) vector
				.toArray(new TransactionDetail[vector.size()]);
		trans.setTransactionDetail(transDets);

		AccountingBusinessLogic logic = new AccountingBusinessLogic(connection);
		trans = logic.createTransactionData(sessionId,
				IDBConstants.MODUL_ACCOUNTING, trans, trans
						.getTransactionDetail());

		// get everything related

		setTrans(trans);
	}

	public Date getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(Date periodFrom) {
		this.periodFrom = periodFrom;
	}

	public Date getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(Date periodTo) {
		this.periodTo = periodTo;
	}

	public void isetBaseCurrency(Currency baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Currency igetBaseCurrency() {
		return this.baseCurrency;
	}
}
