package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class NonPaychequePeriod {
  protected short m_from1 = 0;
  protected short m_to1 = 0;
  protected short m_from2 = 0;
  protected short m_to2 = 0;

  public NonPaychequePeriod(short from1, short to1, short from2, short to2) {
    m_from1 = from1;
    m_to1 = to1;
    m_from2 = from2;
    m_to2 = to2;
  }

  public short getFrom1() {
    return m_from1;
  }

  public short getTo1() {
    return m_to1;
  }

  public short getFrom2() {
    return m_from2;
  }

  public short getTo2() {
    return m_to2;
  }
}