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

public class PartnerList extends JList {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;
  Partner[] m_partner = new Partner[0];

  public PartnerList(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    setModel(new DefaultListModel());
    initData();
  }

  void initData() {
    System.out.println("get data partner");
    DefaultListModel model = (DefaultListModel)getModel();
    model.removeAllElements();

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_partner = logic.getAllPartner(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      reset(m_partner);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void reset(Partner[] partner) {
    DefaultListModel model = (DefaultListModel)getModel();
    model.removeAllElements();

    for(int i = 0; i < partner.length; i ++)
      model.addElement(partner[i]);
  }

  public void refresh() {
    initData();
  }
}