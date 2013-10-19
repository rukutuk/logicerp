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

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.application.AttributePicker;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.SearchCustomerDetailDlg;

public class CustomerPicker extends AttributePicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CustomerPicker(Connection conn, long sessionid) {
		super(conn, sessionid);
	}
	
	public void done() {
		SearchCustomerDetailDlg dlg = new SearchCustomerDetailDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				m_conn, m_sessionid);
		dlg.setVisible(true);
		
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			Customer[] customer = dlg.getCustomer();
			if(customer.length > 0) {
				setObject(customer[0]);
			}
		}
	}
	
}
