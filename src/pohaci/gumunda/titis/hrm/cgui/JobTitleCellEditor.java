package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
import java.sql.Connection;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class JobTitleCellEditor extends AttributeCellEditor {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public JobTitleCellEditor(JFrame owner, String title, Connection conn, long sessionid) {
    super(owner, title, conn, sessionid);
  }

  public void initData() {
    DefaultListModel model = this.getAttributeListModel();
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      JobTitle[] object = logic.jobTitleLogic.getAllJobTitle(logic,m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      for(int i = 0; i < object.length; i ++)
        model.addElement(new JobTitle(object[i], JobTitle.CODE_NAME));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(m_owner, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
}