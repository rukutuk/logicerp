package pohaci.gumunda.titis.accounting.cgui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.entity.TransactionPosted;
import pohaci.gumunda.titis.accounting.entity.TransactionPostedDetail;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class Transaction implements Comparable {
	public static final short NOT_SUBMITTED = 0;

	public static final short SUBMITTED = 1;

	public static final short VERIFIED = 2;

	public static final short POSTED = 3;

	protected long m_index = 0;

	protected String m_description = "";

	protected Date m_transdate = null;

	protected Date m_verifydate = null;

	protected Date m_posteddate = null;

	protected String m_reference = "";

	protected Journal m_journal = null;

	protected JournalStandard m_journalstandard = null;

	protected Unit m_unit = null;

	protected short m_status = -1;

	protected TransactionDetail[] m_detail = new TransactionDetail[0];

	protected boolean m_void = false;

	public Transaction(String description, Date transdate, Date verifydate,
			Date posteddate, String reference, Journal journal,
			JournalStandard journalstandard, short status, Unit unit,
			boolean isVoid) {
		m_description = description;
		m_transdate = transdate;
		m_verifydate = verifydate;
		m_posteddate = posteddate;
		m_reference = reference;
		m_journal = journal;
		m_journalstandard = journalstandard;
		m_status = status;
		m_unit = unit;
		m_void = isVoid;
	}

	public Transaction(String description, Date transdate, Date verifydate,
			Date posteddate, String reference, Journal journal,
			JournalStandard journalstandard, short status, Unit unit) {
		m_description = description;
		m_transdate = transdate;
		m_verifydate = verifydate;
		m_posteddate = posteddate;
		m_reference = reference;
		m_journal = journal;
		m_journalstandard = journalstandard;
		m_status = status;
		m_unit = unit;
	}

	public Transaction(long index, String description, Date transdate,
			Date verifydate, Date posteddate, String reference,
			Journal journal, JournalStandard journalstandard, short status,
			Unit unit, boolean isVoid) {
		m_index = index;
		m_description = description;
		m_transdate = transdate;
		m_verifydate = verifydate;
		m_posteddate = posteddate;
		m_reference = reference;
		m_journal = journal;
		m_journalstandard = journalstandard;
		m_status = status;
		m_unit = unit;
		m_void = isVoid;
	}

	public Transaction(long index, String description, Date transdate,
			Date verifydate, Date posteddate, String reference,
			Journal journal, JournalStandard journalstandard, short status,
			Unit unit) {
		m_index = index;
		m_description = description;
		m_transdate = transdate;
		m_verifydate = verifydate;
		m_posteddate = posteddate;
		m_reference = reference;
		m_journal = journal;
		m_journalstandard = journalstandard;
		m_status = status;
		m_unit = unit;
	}

	public Transaction(long index, Transaction trans) {
		m_index = index;
		m_description = trans.getDescription();
		m_transdate = trans.getTransDate();
		m_verifydate = trans.getVerifyDate();
		m_posteddate = trans.getPostedDate();
		m_reference = trans.getReference();
		m_journal = trans.getJournal();
		m_journalstandard = trans.getJournalStandard();
		m_status = trans.getStatus();
		m_detail = trans.getTransactionDetail();
		m_unit = trans.getUnit();
		m_void = trans.isVoid();
	}

	public Transaction(Transaction trans) {
		m_description = trans.getDescription();
		m_transdate = trans.getTransDate();
		m_verifydate = trans.getVerifyDate();
		m_posteddate = trans.getPostedDate();
		m_reference = trans.getReference();
		m_journal = trans.getJournal();
		m_journalstandard = trans.getJournalStandard();
		m_status = trans.getStatus();
		m_detail = trans.getTransactionDetail();
		m_unit = trans.getUnit();
		m_void = trans.isVoid();
	}

	public Transaction() {

	}

	public long getIndex() {
		return m_index;
	}

	public void setIndex(long index) {
		m_index = index;
	}

	public String getDescription() {
		return m_description;
	}

	public Date getTransDate() {
		return m_transdate;
	}

	public Date getVerifyDate() {
		return m_verifydate;
	}

	public Date getPostedDate() {
		return m_posteddate;
	}

	public String getReference() {
		return m_reference;
	}

	public Journal getJournal() {
		return m_journal;
	}

	public JournalStandard getJournalStandard() {
		return m_journalstandard;
	}

	public short getStatus() {
		return m_status;
	}

	public void setTransactionDetail(TransactionDetail[] detail) {
		m_detail = detail;
	}

	public TransactionDetail[] getTransactionDetail() {
		return m_detail;
	}

	public void setUnit(Unit unit) {
		m_unit = unit;
	}

	public Unit getUnit() {
		return m_unit;
	}

	public boolean isVoid() {
		return m_void;
	}

	public void post(Date postedDate) {
		if (m_status == 2) {
			m_status = 3;
			m_posteddate = postedDate;
		}
	}

	public TransactionPosted createTransactionPosted(Date postedDate) {
		TransactionPosted posted = new TransactionPosted(m_description,
				m_transdate, m_verifydate, postedDate, m_reference, m_journal,
				m_journalstandard, m_status, m_unit);

		TransactionDetail[] details = m_detail;
		Vector vec = new Vector();

		if (details.length > 0) {
			for (int i = 0; i < details.length; i++) {
				TransactionDetail detail = details[i];
				TransactionPostedDetail postedDetail = new TransactionPostedDetail(
						detail.getAccount(), detail.getValue(), detail
								.getCurrency(), detail.getExchangeRate(),
						detail.getUnit(), detail.getSubsidiaryAccount());
				vec.add(postedDetail);
			}
		}

		TransactionPostedDetail[] postedDetails = new TransactionPostedDetail[vec
				.size()];

		vec.copyInto(postedDetails);
		posted.setTransactionPostedDetail(postedDetails);

		return posted;
	}

	public String toString() {
		return m_description;
	}

	public void setPostedDate(Date m_posteddate) {
		this.m_posteddate = m_posteddate;
	}

	public void setDescription(String m_description) {
		this.m_description = m_description;
	}

	public String dumpInfo() {
		StringBuffer builder = new StringBuffer();
		builder.append(m_description + " unit " + m_unit);
		builder.append("\r\n");
		int i;
		if (m_detail == null)
			builder.append("  NULL detail!");
		for (i = 0; i < this.m_detail.length; i++) {
			builder.append("  ");
			builder.append(m_detail[i]);
			builder.append("\r\n");
		}
		return builder.toString();
	}

	public Object vgetStatus() {
		if (this.m_status == 0)
			return "Not Submitted";
		else if (this.m_status == 1)
			return "Submitted";
		else if (this.m_status == 2)
			return "Submitted";
		else if (this.m_status == 3)
			return "Posted";
		return "";
	}

	public void isetStatus(short status) {
		m_status = status;
	}

	public void isetVoid(boolean isVoid) {
		m_void = isVoid;
	}

	public Transaction createVoidTransaction(Date voidDate,
			JournalStandard journalStandard, String referenceNo,
			String description) {
		Transaction transaction = new Transaction(this);

		// transaction.m_transdate = voidDate;
		transaction.m_journalstandard = journalStandard;
		transaction.m_journal = journalStandard.getJournal();
		transaction.m_reference = referenceNo;
		transaction.m_description = description;

		TransactionDetail[] details = m_detail;
		Vector vec = new Vector();

		if (details.length > 0) {
			for (int i = 0; i < details.length; i++) {
				TransactionDetail detail = details[i];
				TransactionDetail voidDetail = new TransactionDetail(detail
						.getAccount(), -detail.getValue(),
						detail.getCurrency(), detail.getExchangeRate(), detail
								.getUnit(), detail.getSubsidiaryAccount());
				vec.add(voidDetail);
			}
		}

		TransactionDetail[] voidDetails = new TransactionDetail[vec.size()];

		vec.copyInto(voidDetails);
		transaction.setTransactionDetail(voidDetails);

		return transaction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		Transaction newTransaction = new Transaction(getIndex(), this);

		TransactionDetail[] details = newTransaction.getTransactionDetail();
		List list = new ArrayList();
		for (int i = 0; i < details.length; i++) {
			list.add((TransactionDetail) details[i].clone());
		}

		TransactionDetail[] newDetails = (TransactionDetail[]) list
				.toArray(new TransactionDetail[list.size()]);
		newTransaction.setTransactionDetail(newDetails);

		return newTransaction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg) {
		if (arg instanceof Transaction) {
			Transaction compared = (Transaction) arg;

			int val = getTransDate().compareTo(compared.getTransDate());
			if (val != 0)
				return val;
			else {
				if ((getPostedDate() != null)
						&& (compared.getPostedDate() != null)) {
					val = getPostedDate().compareTo(compared.getPostedDate());
					if (val != 0)
						return val;
					else {
						val = getReference().compareTo(compared.getReference());
						return val;
					}
				} else {
					val = getReference().compareTo(compared.getReference());
					return val;
				}
			}
		}
		return -1;
	}

}