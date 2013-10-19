package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class StandardJournalSetting {
  protected String m_application = "";
  protected short m_number = 0;
  protected JournalStandard m_journal = null;

  public StandardJournalSetting(String application, short number, JournalStandard journal) {
    m_application = application;
    m_number = number;
    m_journal = journal;
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
}