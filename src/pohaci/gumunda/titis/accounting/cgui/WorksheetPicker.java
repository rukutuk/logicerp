package pohaci.gumunda.titis.accounting.cgui;

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
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.AttributeListDlg;
import pohaci.gumunda.titis.application.AttributePicker;

public class WorksheetPicker extends AttributePicker {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
AttributeListDlg m_attrlistDlg = null;
  WorksheetDesignPanel m_panel;

  public WorksheetPicker(Connection conn, long sessionid, WorksheetDesignPanel panel) {
    super(conn, sessionid);
    m_panel = panel;
    initComponent();
  }

  void initComponent() {
    m_attrlistDlg = new AttributeListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        "Worksheet", m_conn, m_sessionid);
  }

  void initData() {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Worksheet[] worksheet = logic.getAllWorksheet(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      DefaultListModel model = (DefaultListModel)m_attrlistDlg.getAttributeList().getModel();
      model.removeAllElements();
      for(int i = 0; i < worksheet.length; i ++) {
        model.addElement(worksheet[i]);
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void done() {
    initData();
    m_attrlistDlg.setVisible(true);
    if(m_attrlistDlg.getResponse() == JOptionPane.OK_OPTION) {
      Object[] object = m_attrlistDlg.getObject();
      if(object.length > 0) {
       setObject(object[0]);
       m_panel.m_tree.reloadWorksheetColumn((Worksheet)object[0]);
      }
    }
  }
}
