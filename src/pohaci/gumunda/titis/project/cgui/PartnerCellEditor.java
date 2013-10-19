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
import pohaci.gumunda.titis.application.ObjectCellEditor;

public class PartnerCellEditor extends ObjectCellEditor {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PartnerCellEditor(JFrame owner, Connection conn, long sessionid) {
    super(owner, conn, sessionid);
  }

  public void done() {
    SearchPartnerDetailDlg dlg = new SearchPartnerDetailDlg(m_owner, m_conn, m_sessionid);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      Partner[] partner = dlg.getPartner();
      if(partner.length > 0)
        setObject(partner[0]);
    }
  }
}