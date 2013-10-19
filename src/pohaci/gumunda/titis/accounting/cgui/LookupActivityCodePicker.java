package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.application.AttributePicker;

public class LookupActivityCodePicker extends AttributePicker {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ActivityTreeDlg m_treetDlg;
	   //JButton m_okBt, m_cancelBt;

	  public LookupActivityCodePicker(Connection conn, long sessionid) {
	    super(conn, sessionid);
	  }

	  public void done() {
		m_treetDlg= new  ActivityTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
	    m_conn, m_sessionid);
		m_treetDlg.setVisible(true);
	    if(m_treetDlg.getResponse() == JOptionPane.OK_OPTION) {
	      setObject(m_treetDlg.getActivity());
	      	       
	    }
	  }

	}

