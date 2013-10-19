package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.SimpleEmployee;

public class Signature {
  public static final String STR_ORIGINATOR = "Originator by";
  public static final String STR_APPROVED = "Approved by";
  public static final String STR_APPROVED1 = "Approved1 by";
  public static final String STR_APPROVED2 = "Approved2 by";
  public static final String STR_RECEIVED = "Received by";
  public static final String STR_CREATED = "Created by";
  public static final String STR_AUTHORIZED = "Authorized by";
  
  public static final short SIGN_ORIGINATOR = 0;
  public static final short SIGN_APPROVED = 1;
  public static final short SIGN_APPROVED1 = 2;
  public static final short SIGN_APPROVED2 = 3;
  public static final short SIGN_RECEIVED = 4;
  public static final short SIGN_CREATED = 5;
  public static final short SIGN_AUTHORIZED = 6;

  public static String[] m_atype = new String[]{STR_ORIGINATOR, STR_APPROVED,
    STR_APPROVED1, STR_APPROVED2, STR_RECEIVED, STR_CREATED, STR_AUTHORIZED};

  protected String m_app = "";
  protected short m_type = -1;
  protected SimpleEmployee m_employee = null;

  public Signature(short type, SimpleEmployee employee) {
    m_type = type;
    m_employee = employee;
  }

  public Signature(String app, short type, SimpleEmployee employee) {
    m_app = app;
    m_type = type;
    m_employee = employee;
  }
  
  public Signature(String app, short type, Employee employee) {
	    m_app = app;
	    m_type = type;
	    m_employee = employee;
	  }
  
  public Employee getFullEmployee(){
	  return (Employee) m_employee;
  }
  
  public void setFullEmployee(Employee employee){
	  m_employee = employee;
  }

  public String getApp() {
    return m_app;
  }

  public short getType() {
    return m_type;
  }

  public SimpleEmployee getEmployee() {
    return m_employee;
  }

  public String getTypeAsString(){
    if(m_type < 0 || m_type >= m_atype.length)
      return "";
    else
      return m_atype[m_type];
  }

  public static short typeFromStringToID(String type){
    short len = (short)m_atype.length;
    for(short i = 0; i < len; i++){
      if(m_atype[i].equals(type))
        return i;
    }

    return -1;
  }
  
  public static Signature getSignature(Connection conn, String formIdentity, short type){
	  GenericMapper mapper = MasterMap.obtainMapperFor(Signature.class);
	  mapper.setActiveConn(conn);
	  	
	  String whereClause = IDBConstants.ATTR_APPLICATION + "='" + formIdentity + "' AND " +
	  						IDBConstants.ATTR_SIGNATURE + "=" + type;
	  
	  List list = mapper.doSelectWhere(whereClause);
	  
	  if(list.size()>0)
		  return (Signature) list.get(0);
	  
	  return null;
	  
  }
}