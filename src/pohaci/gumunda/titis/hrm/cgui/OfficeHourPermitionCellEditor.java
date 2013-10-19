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

public class OfficeHourPermitionCellEditor extends AttributeCellEditor {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public OfficeHourPermitionCellEditor(JFrame owner, String title, Connection conn, long sessionid) {
    super(owner, title, conn, sessionid);
  }

  public void initData() {
    DefaultListModel model = this.getAttributeListModel();
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      OfficeHourPermition[] object = logic.getAllOfficeHourPermition(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      for(int i = 0; i < object.length; i ++)
        model.addElement(new OfficeHourPermition(object[i], OfficeHourPermition.CODE_DESCRIPTION));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(m_owner, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
}