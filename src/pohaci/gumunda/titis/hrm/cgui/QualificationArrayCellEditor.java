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
import java.util.Vector;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class QualificationArrayCellEditor extends AttributeCellEditor {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Qualification[] m_qua = new Qualification[0];

  public QualificationArrayCellEditor(JFrame owner, String title, Connection conn, long sessionid) {
    super(owner, title, conn, sessionid);
  }

  public void initData() {
    DefaultListModel model = this.getAttributeListModel();
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      Qualification[] object = logic.qualification.getAllQualification(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      for(int i = 0; i < object.length; i ++)
        model.addElement(new Qualification(object[i], Qualification.CODE_NAME));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(m_owner, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public Qualification[] getQualification() {
    return m_qua;
  }

  public void setQualification(Qualification[] qua) {
    m_qua = qua;
  }

  public void done() {
    m_attrdlg.setVisible(true);
    if(m_attrdlg.getResponse() == JOptionPane.OK_OPTION) {
      Object[] object = m_attrdlg.getObject();
      String str = "";
      Vector vresult = new Vector();
      for(int i = 0; i < object.length; i ++) {
        vresult.addElement((Qualification)object[i]);
        if(i == 0)
          str += new Qualification((Qualification)object[i], Qualification.NAME).toString();
        else
          str += ", " + new Qualification((Qualification)object[i], Qualification.NAME).toString();
      }

      m_qua = new Qualification[vresult.size()];
      vresult.copyInto(m_qua);
      m_model.setValueAt(str, m_row, m_col);
    }
  }
}
