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

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class PersonalList extends JList {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;
  Personal[] m_personal = new Personal[0];

  public PersonalList(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    setModel(new DefaultListModel());
    initData();
  }

  void initData() {
    System.out.println("get data personal");
    DefaultListModel model = (DefaultListModel)getModel();
    model.removeAllElements();

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_personal = logic.getAllPersonal(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      reset(m_personal);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void reset(Personal[] personal) {
    DefaultListModel model = (DefaultListModel)getModel();
    model.removeAllElements();

    for(int i = 0; i < personal.length; i ++)
      model.addElement(personal[i]);
  }

  public void refresh() {
    initData();
  }
}