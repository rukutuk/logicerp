package pohaci.gumunda.titis.project.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.application.AttributePicker;

public class PartnerDataPicker extends AttributePicker {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PartnerDataPicker(Connection conn, long sessionid) {
		    super(conn, sessionid);
		  }

	 public void done() {
		 SearchPartnerDetailDlg dlg = new SearchPartnerDetailDlg(
				 pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				 m_conn, m_sessionid);
		 dlg.setVisible(true);
		 if(dlg.getResponse() == JOptionPane.OK_OPTION) {
		      Partner[] partner = dlg.getPartner();
		      System.err.println("partner :" + partner[0].getCode());
		      if(partner.length > 0) {
		    	  setObject(partner[0]);
		      }
		 }
	 }
}
