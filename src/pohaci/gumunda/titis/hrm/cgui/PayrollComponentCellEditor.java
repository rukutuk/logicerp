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
import pohaci.gumunda.titis.application.ObjectCellEditor;

public class PayrollComponentCellEditor extends ObjectCellEditor {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PayrollComponentCellEditor(JFrame owner, Connection conn, long sessionid) {
    super(owner, conn, sessionid);
  }

  public void done() {
    PayrollComponentTreeDlg dlg = new PayrollComponentTreeDlg(m_owner, m_conn, m_sessionid);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      setObject(dlg.getPayrollComponent());
    }
  }
}