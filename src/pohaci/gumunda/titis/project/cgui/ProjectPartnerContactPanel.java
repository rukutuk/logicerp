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
import javax.swing.*;

public class ProjectPartnerContactPanel extends PersonalContactPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Partner m_partner = null;

  public ProjectPartnerContactPanel(Connection conn, long sessionid) {
    super(conn, sessionid);
  }

  public void setPartner(Partner partner) {
    m_partner = partner;
  }

  void onSearch() {
    SearchProjectPartnerContactDetailDlg dlg = new SearchProjectPartnerContactDetailDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn, m_sessionid, m_partner);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION){
      Personal[] personal = dlg.getPersonal();
      m_table.addPersonalContact(personal);
    }
  }
}