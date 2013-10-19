package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

/**
 * sorry i ubah...
 * semua yang ngelink ke object sekarang cuman index-nya aja yang disimpan
 * cuman kalo butuh objectnya kita sediain deh
 */
import java.sql.Connection;
import java.util.Date;

import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeLeavePermition {
  public static final String STR_TYPE1 = "Leave";
  public static final String STR_TYPE2 = "Permission";
  public static String m_types[] = new String[]{STR_TYPE1, STR_TYPE2};

  protected short m_type = -1;
  protected long m_index = 0;
  protected Date m_propose = null;
  protected Date m_from = null;
  protected Date m_to = null;
  protected int m_days = 0;
  protected long m_reason = 0;
  protected String m_address = "";
  protected String m_phone = "";
  protected long m_replaced = 0;
  protected long m_checked = 0;
  protected Date m_checkeddate = null;
  protected long m_approved = 0;
  protected Date m_approveddate = null;
  protected String m_description = "";
  protected boolean m_isreference = false;
  protected String m_file = "";
  protected byte[] m_reference = null;


  public EmployeeLeavePermition(short type, Date propose, Date from, Date to, int days,
                                long reason, String address, String phone, long replaced,
                                long checked, Date checkeddate, long approved,
                                Date approveddate, String description, boolean isreference,
                                String file, byte[] reference) {
    m_type = type;
	m_propose = propose;
    m_from = from;
    m_to = to;
    m_days = days;
    m_reason = reason;
    m_address = address;
    m_phone = phone;
    m_replaced = replaced;
    m_checked = checked;
    m_checkeddate = checkeddate;
    m_approved = approved;
    m_approveddate = approveddate;
    m_description = description;
    m_isreference = isreference;
    m_file = file;
    m_reference = reference;
  }

  public EmployeeLeavePermition(long index, short type, Date propose, Date from, Date to, int days,
                                long reason, String address, String phone, long replaced,
                                long checked, Date checkeddate, long approved,
                                Date approveddate, String description, boolean isreference,
                                String file, byte[] reference) {
    m_index = index;
    m_type = type;
    m_propose = propose;
    m_from = from;
    m_to = to;
    m_days = days;
    m_reason = reason;
    m_address = address;
    m_phone = phone;
    m_replaced = replaced;
    m_checked = checked;
    m_checkeddate = checkeddate;
    m_approved = approved;
    m_approveddate = approveddate;
    m_description = description;
    m_isreference = isreference;
    m_file = file;
    m_reference = reference;
  }

  public EmployeeLeavePermition(long index, EmployeeLeavePermition reason) {
    m_index = index;
    m_type = reason.getType();
    m_propose = reason.getPropose();
    m_from = reason.getFrom();
    m_to = reason.getTo();
    m_days = reason.getDays();
    m_reason = reason.getReason();
    m_address = reason.getAddress();
    m_phone = reason.getPhone();
    m_replaced = reason.getReplaced();
    m_checked = reason.getChecked();
    m_checkeddate = reason.getCheckedDate();
    m_approved = reason.getApproved();
    m_approveddate = reason.getApprovedDate();
    m_description = reason.getDescription();
    m_isreference = reason.isReference();
    m_file = reason.getFile();
    m_reference = reason.getReference();
  }

  public static String getTypeAsString(short type){
    if(type < 0 || type >= m_types.length)
      return "";
    else
      return m_types[type];
  }

  public static short typeFromStringToID(String type){
    short len = (short)m_types.length;
    for(short i = 0; i < len; i++){
      if(m_types[i].equals(type))
        return i;
    }

    return -1;
  }

  public long getIndex() {
    return m_index;
  }

  public short getType() {
	  return m_type;
  }

  public Date getPropose() {
    return m_propose;
  }

  public Date getFrom() {
    return m_from;
  }

  public Date getTo() {
    return m_to;
  }

  public int getDays() {
    return m_days;
  }

  public LeaveType getLeaveReason(long sessionid, Connection conn) {
	  if (m_type == 0) {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			LeaveType result = null;
			try {
				result = logic.getLeaveType(sessionid,
						IDBConstants.MODUL_MASTER_DATA, m_reason);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new LeaveType(result, LeaveType.DESCRIPTION);
		} else {
			return null;
		}
  }

  public PermitionType getPermissionReason(long sessionid, Connection conn) {
	  if (m_type == 1) {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			PermitionType result = null;
			try {
				result = logic.getPermissionType(sessionid,
						IDBConstants.MODUL_MASTER_DATA, m_reason);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new PermitionType(result, PermitionType.DESCRIPTION);
		} else {
			return null;
		}
  }

  public String getAddress() {
    return m_address;
  }

  public String getPhone() {
    return m_phone;
  }

  public long getReplaced() {
    return m_replaced;
  }

  public Employee getReplacedEmployee(long sessionid, Connection conn) {
	  return getEmployee(sessionid, conn, m_replaced);
  }

  public long getReason() {
    return m_reason;
  }

  public Object getReasonAsObject(long sessionid, Connection conn) {
	  if (m_type == 0) {
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			LeaveType result = null;
			try {
				result = logic.getLeaveType(sessionid,
						IDBConstants.MODUL_MASTER_DATA, m_reason);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new LeaveType(result, LeaveType.DESCRIPTION);
		} else if (m_type==1){
			HRMBusinessLogic logic = new HRMBusinessLogic(conn);
			PermitionType result = null;
			try {
				result = logic.getPermissionType(sessionid,
						IDBConstants.MODUL_MASTER_DATA, m_reason);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new PermitionType(result, PermitionType.DESCRIPTION);
		} else {
			return null;
		}
  }

  public long getChecked() {
    return m_checked;
  }

  public Employee getCheckedEmployee(long sessionid, Connection conn) {
	  return getEmployee(sessionid, conn, m_checked);
  }

  public Date getCheckedDate() {
    return m_checkeddate;
  }

  public long getApproved() {
    return m_approved;
  }

  public Employee getApprovedEmployee(long sessionid, Connection conn) {
	  return getEmployee(sessionid, conn, m_approved);
  }

  public Date getApprovedDate() {
    return m_approveddate;
  }

  public String getDescription() {
    return m_description;
  }

  public boolean isReference() {
    return m_isreference;
  }

  public String getFile() {
    return m_file;
  }

  public byte[] getReference() {
    return m_reference;
  }

  public String toString(){
	  return getTypeAsString(m_type);
  }

  private Employee getEmployee(long sessionid, Connection conn, long index){
	  HRMBusinessLogic logic = new HRMBusinessLogic(conn);
	  Employee emp = null;

	  try {
			emp = logic.getEmployeeByIndex(sessionid,
					IDBConstants.MODUL_MASTER_DATA, index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	  return emp;
  }
}