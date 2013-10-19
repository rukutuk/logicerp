package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.application.AttributePicker;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.dbapi.ProjectSQLSAP;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectDataPicker extends AttributePicker {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public ProjectDataPicker(Connection conn, long sessionid) {
    super(conn, sessionid);
  }

  public void done() {
    SearchProjectDetailDlg dlg = new SearchProjectDetailDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      ProjectData[] project = dlg.getProjectData();
      if(project.length > 0)
      {
    	  findProjectData(project[0].m_index);
      }
    }
  }
  private void findProjectData(long project){
	  ProjectData temp=null;
	  ProjectData[] m_project;
	  try {		 
		  String query=builtQuery(project);
	      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
	      m_project = logic.getProjectDataByCriteria(m_sessionid,
	          IDBConstants.MODUL_PROJECT_MANAGEMENT, query);
	      if(m_project.length>0)
		    	temp=m_project[0];
	      try{
	    	  temp.setUnit(new AccountingSQLSAP().getUnitByIndex(temp.getIndexunit(),m_conn));//.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
	    	  temp.setActivity(new AccountingSQLSAP().getActivityByIndex(temp.getIndexact(), m_conn));//.getActivityByIndex(rs.getLong(IDBConstants.ATTR_ACTIVITY), conn),
	    	  temp.setDepartment(new HRMSQLSAP().getOrganizationByIndex(temp.getIndexdept(), m_conn));	    	  
	    	  temp.setCustomer(new ProjectSQLSAP().getCustomerByIndex(temp.getIndexcust(), m_conn));
	      }
	      catch(Exception exc) {
	    	  
	      }
	  }
	    catch(Exception ex) {
	      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
	                                    JOptionPane.WARNING_MESSAGE);
	    }
	    setObject(temp);
  }
  public String builtQuery(long projectcode) throws Exception {
	  String criteria = "";
	  String queryselect = "SELECT * FROM (" + 
	  "SELECT pd." + IDBConstants.ATTR_AUTOINDEX +
	  ", pd." + IDBConstants.ATTR_CODE +
	  ", cust." + IDBConstants.ATTR_NAME + " AS CUSTOMERNAME" +
	  ", pd." + IDBConstants.ATTR_WORK_DESCRIPTION +
	  ", act." + IDBConstants.ATTR_NAME + " AS ACTIVITYNAME" +
	  ", pd." + IDBConstants.ATTR_ORNO +
	  ", pd." + IDBConstants.ATTR_ORDATE +
	  ", pd." + IDBConstants.ATTR_PONO  +
	  ", pd." + IDBConstants.ATTR_PODATE +
	  ", pd." + IDBConstants.ATTR_IPCNO + 
	  ", pd." + IDBConstants.ATTR_IPCDATE +
	  ", pc." + IDBConstants.ATTR_ACTUAL_START_DATE +
	  ", pc." + IDBConstants.ATTR_ACTUAL_END_DATE +
	  ", pc." + IDBConstants.ATTR_VALIDATION +
	  ", pd." + IDBConstants.ATTR_REGDATE +
	  ", pd." + IDBConstants.ATTR_CUSTOMER +
	  ", pd." + IDBConstants.ATTR_UNIT +
	  ", pd." + IDBConstants.ATTR_FILE + 
	  ", pd." + IDBConstants.ATTR_SHEET +
	  ", pd." + IDBConstants.ATTR_ACTIVITY +
	  ", pd." + IDBConstants.ATTR_DEPARTMENT + " ";
	  
	  String querytable = "FROM " + IDBConstants.TABLE_PROJECT_DATA + " pd " +
	  "LEFT JOIN " + IDBConstants.TABLE_CUSTOMER + " cust " +
	  "ON pd." + IDBConstants.ATTR_CUSTOMER + "=cust." + IDBConstants.ATTR_AUTOINDEX + " " +
	  "LEFT JOIN " + IDBConstants.TABLE_ACTIVITY + " act " +
	  "ON pd." + IDBConstants.ATTR_ACTIVITY + "=act." + IDBConstants.ATTR_AUTOINDEX + " " +
	  "LEFT JOIN " + IDBConstants.TABLE_PROJECT_CONTRACT + " pc " +
	  "ON pd." + IDBConstants.ATTR_AUTOINDEX + "=pc." + IDBConstants.ATTR_PROJECT + ") proj";
	  
	  
	  String value = new Long(projectcode).toString();  
	  if(!value.equals("")) {
		  criteria = " WHERE " + IDBConstants.ATTR_AUTOINDEX+"="+value;
	  }
	  String query = queryselect + querytable + criteria ;     
	  System.err.println("query :" + query);
	  return query;                
  }
}
