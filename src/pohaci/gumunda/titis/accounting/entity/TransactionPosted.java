package pohaci.gumunda.titis.accounting.entity;

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;

/**
 * 
 * @author dark-knight
 *
 */
public class TransactionPosted {
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

	  protected TransactionPostedDetail[] m_detail = new TransactionPostedDetail[0];

	  public TransactionPosted(String description, Date transdate,
	                     Date verifydate, Date posteddate, String reference, Journal journal,
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
	  
	  public TransactionPosted(long index,String description, Date transdate,String reference,Unit unit ) {
		  m_index = index;
		  m_description = description;
		  m_transdate = transdate;
		  m_reference = reference;
		  m_unit = unit;
		  
	  }

	  public TransactionPosted(long index, String description, Date transdate,
	                     Date verifydate, Date posteddate, String reference, Journal journal,
	                     JournalStandard journalstandard, short status, Unit unit) {
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

	  public TransactionPosted(long index, TransactionPosted trans) {
	    m_index = index;
	    m_description = trans.getDescription();
	    m_transdate = trans.getTransDate();
	    m_verifydate = trans.getVerifyDate();
	    m_posteddate = trans.getPostedDate();
	    m_reference = trans.getReference();
	    m_journal = trans.getJournal();
	    m_journalstandard = trans.getJournalStandard();
	    m_status = trans.getStatus();
	    m_detail = trans.getTransactionPostedDetail();
	  }
	  
	  public long getIndex() {
	    return m_index;
	  }
	  public void setIndex(long index){
		  m_index=index;
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

	  public void setTransactionPostedDetail(TransactionPostedDetail[] detail) {
	    m_detail = detail;
	  }

	  public TransactionPostedDetail[] getTransactionPostedDetail() {
	    return m_detail;
	  }
	  
	  public void setUnit(Unit unit) {
		  m_unit = unit;
	  }
	  
	  public Unit getUnit() {
		  return m_unit;
	  }

	  public void setStatus(short status) {
		  m_status = status;
	  }
}
