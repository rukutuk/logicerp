package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class JournalStandard {
  protected long m_index = 0;
  protected String m_code = "";
  protected String m_description = "";
  protected boolean m_isgroup = false;
  protected Journal m_journal = null;

  protected JournalStandard m_parent = null;
  protected JournalStandardAccount[] m_account = new JournalStandardAccount[0];

  public JournalStandard(String code, String description, boolean isgroup, Journal journal) {
    m_code = code;
    m_description = description;
    m_isgroup = isgroup;
    m_journal = journal;
  }

  public JournalStandard(long index, String code, String description, boolean isgroup, Journal journal) {
    m_index = index;
    m_code = code;
    m_description = description;
    m_isgroup = isgroup;
    m_journal = journal;
  }

  public JournalStandard(long index, JournalStandard journal) {
    m_index = index;
    m_code = journal.getCode();
    m_description = journal.getDescription();
    m_isgroup = journal.isGroup();
    m_parent = journal.getParent();
    m_account = journal.getJournalStandardAccount();
    m_journal = journal.getJournal();
  }
  
  public JournalStandard() {
	  
  }

  public long getIndex() {
    return m_index;
  }
  
  public void setIndex(long index){
	  m_index = index;
  }

  public String getCode() {
    return m_code;
  }
  
  public void setCode(String code){
	  m_code = code;
  }

  public String getDescription() {
    return m_description;
  }
  
  public void setDescription(String description){
	  m_description = description;
  }

  public boolean isGroup() {
    return m_isgroup;
  }

  public void setGroup(boolean isGroup) {
	  m_isgroup = isGroup;
  }
  
  public Journal getJournal() {
    return m_journal;
  }
  
  public void setJournal(Journal journal) {
	  m_journal = journal;
  }

  public void setParent(JournalStandard journal) {
    m_parent = journal;
  }

  public JournalStandard getParent() {
    return m_parent;
  }

  public void setJournalStandardAccount(JournalStandardAccount[] account) {
    m_account = account;
  }

  public JournalStandardAccount[] getJournalStandardAccount() {
    return m_account;
  }

  public String toString() {
    if(isGroup())
      return m_description;
    else
      return m_description + "[" + m_code + "]";
  }
}