package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceiptLoader;
import pohaci.gumunda.titis.application.AttributePicker;


public class PurchaseReceiptPicker  extends AttributePicker {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PurchaseReceiptPicker(Connection conn, long sessionid) {
		    super(conn, sessionid);
		  }
	public void done() {
		SearchPurchaseReceiptDialog dlg = new SearchPurchaseReceiptDialog(GumundaMainFrame.getMainFrame(), "Search Purchase Receipt", m_conn, m_sessionid,new PurchaseReceiptLoader(m_conn));
		dlg.setVisible(true);
		 if(dlg.getResponse() == JOptionPane.OK_OPTION) {
		      Object object = dlg.getObjet();		      
		      setObject(object);		      
		 }
		
	}

}
