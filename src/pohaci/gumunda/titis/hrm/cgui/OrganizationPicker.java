package pohaci.gumunda.titis.hrm.cgui;

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
import pohaci.gumunda.titis.application.AttributePicker;

public class OrganizationPicker extends AttributePicker {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean m_isroot;
public OrganizationPicker(Connection conn, long sessionid,boolean isroot) {	
    super(conn, sessionid);
    m_isroot = isroot;
  }

  public void done() {
    OrganizationTreeDlg dlg = new OrganizationTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid,m_isroot);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      setObject(dlg.getOrganization());
    }
  }
}