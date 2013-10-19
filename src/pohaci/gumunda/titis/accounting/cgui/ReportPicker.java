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

public class ReportPicker extends AttributePicker {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
AttributeListDlg m_attrlistDlg = null;
  ReportDesignPanel m_panel;
  String m_title = "";

  public ReportPicker(Connection conn, long sessionid, ReportDesignPanel panel, String title) {
    super(conn, sessionid);
    m_panel = panel;
    m_title = title;

    initComponent();
  }

  void initComponent() {
    m_attrlistDlg = new AttributeListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_title, m_conn, m_sessionid);
  }

  void initData() {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Report[] report = logic.getAllBalanceSheetReport(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      DefaultListModel model = (DefaultListModel)m_attrlistDlg.getAttributeList().getModel();
      model.removeAllElements();
      for(int i = 0; i < report.length; i ++) {
        model.addElement(report[i]);
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
      }
    }
  }
}
