package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.AttributeListDlg;
import pohaci.gumunda.titis.application.AttributePicker;

public class LookupJournalTypePicker extends AttributePicker {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AttributeListDlg m_attrlistDlg = null;

	  public LookupJournalTypePicker(Connection conn, long sessionid) {
	    super(conn, sessionid);
	    initComponent();
	    initData();
	  }

	  void initComponent() {
	    m_attrlistDlg = new AttributeListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
	        "Journal", m_conn, m_sessionid);
	  }

	  void initData() {
	    try {
	      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
	      Journal[] journal = logic.getAllJournal(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

	      DefaultListModel model = (DefaultListModel)m_attrlistDlg.getAttributeList().getModel();
	      for(int i = 0; i < journal.length; i ++) {
	        model.addElement(journal[i]);
	      }
	    }
	    catch(Exception ex) {
	      JOptionPane.showMessageDialog(this, ex.getMessage(),
	                                    "Warning", JOptionPane.WARNING_MESSAGE);
	    }
	  }

	  public void done() {
	    m_attrlistDlg.setVisible(true);
	    if(m_attrlistDlg.getResponse() == JOptionPane.OK_OPTION) {
	      Object[] object = m_attrlistDlg.getObject();
	      if(object.length > 0)
	        setObject(object[0]);
	    }
	  }
	}