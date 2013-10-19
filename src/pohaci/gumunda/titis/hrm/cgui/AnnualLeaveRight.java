package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class AnnualLeaveRight {
  protected long m_index = 0;
  protected short m_from = 0;
  protected short m_thru = 0;
  protected short m_minyear = 0;
  protected short m_maxyear = 0;
  protected short m_maxright = 0;
  protected short m_minright = 0;

  public AnnualLeaveRight(short from, short thru, short minyear, short maxyear, short minright, short maxright) {
    m_from = from;
    m_thru = thru;
    m_minyear = minyear;
    m_minyear = minyear;
    m_maxyear = maxyear;
    m_minright = minright;
    m_maxright = maxright;
  }

  public AnnualLeaveRight(long index, short from, short thru, short minyear, short maxyear, short minright, short maxright) {
    m_index = index;
    m_from = from;
    m_thru = thru;
    m_minyear = minyear;
    m_maxyear = maxyear;
    m_minright = minright;
    m_maxright = maxright;
  }

  public AnnualLeaveRight(long index, AnnualLeaveRight leave) {
    m_index = index;
    m_from = leave.getFrom();
    m_thru = leave.getThru();
    m_minyear = leave.getMinYear();
    m_maxyear = leave.getMaxYear();
    m_minright = leave.getMinRight();
    m_maxright = leave.getMaxRight();
  }

  public long getIndex() {
    return m_index;
  }

  public short getFrom() {
    return m_from;
  }

  public short getThru() {
    return m_thru;
  }

  public short getMinYear() {
    return m_minyear;
  }

  public short getMaxYear() {
    return m_maxyear;
  }

  public short getMinRight() {
    return m_minright;
  }

  public short getMaxRight() {
    return m_maxright;
  }

  public String toString() {
    return String.valueOf(getFrom());
  }
}