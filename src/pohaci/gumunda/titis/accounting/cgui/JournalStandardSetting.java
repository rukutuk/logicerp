package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */ 

public class JournalStandardSetting {
  protected String m_application;
  protected short m_number = -1;
  protected JournalStandard m_journal = null;
  protected String m_attribute = null;

  public JournalStandardSetting(short number, JournalStandard journal, String attribute) {
    m_number = number;
    m_journal = journal;
    m_attribute = attribute;
  }
  
  public JournalStandardSetting(String application, short number, JournalStandard journal, String attribute) {
	m_application = application;
	m_number = number;
	m_journal = journal;
	m_attribute = attribute;
  }
  
  public String getApplication() {
	  return m_application;
  }

  public short getNumber() {
    return m_number;
  }

  public JournalStandard getJournalStandard() {
    return m_journal;
  }
  
  public String getAttribute() {
	  return m_attribute;
  }
  
  public String toString(){
	  return m_journal.getCode() + " " + m_journal.getDescription();
  }
}