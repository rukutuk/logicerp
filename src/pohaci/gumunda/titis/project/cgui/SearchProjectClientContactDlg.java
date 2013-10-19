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

import javax.swing.JFrame;

public class SearchProjectClientContactDlg extends SearchPersonalDlg {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
ProjectData m_project = null;
  protected Customer m_customer = null;  
  protected Connection m_conn;
          
  public SearchProjectClientContactDlg(JFrame owner, PersonalListPanel panel,
                                       Connection conn, long sessionid, ProjectData project) {
    super(owner, null, conn, sessionid);    
    setSize(350, 400);
    m_project = project;
    m_conn = conn;

    constructComponent();
   // initData();
  }

  /*void initData() {
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    try{
        m_customer = logic.getCustomerByCriteria1(m_project.getIndexcust(),m_conn);
    } catch (Exception ex) {        
    }
    
    //if (m_customer!=null)
    //    m_table.setValueAt(m_customer.getName(), 7, 1);        
  }*/
}