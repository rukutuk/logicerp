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

public class SearchProjectPartnerContactDlg extends SearchPersonalDlg {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Partner m_partner = null;

  public SearchProjectPartnerContactDlg(JFrame owner, PersonalListPanel panel,
                                       Connection conn, long sessionid, Partner partner) {
    super(owner, null, conn, sessionid);
    m_partner = partner;
    initData();
  }


  void initData() {
    if(m_partner != null)
      m_table.setValueAt(m_partner.getName(), 7, 1);
  }
}