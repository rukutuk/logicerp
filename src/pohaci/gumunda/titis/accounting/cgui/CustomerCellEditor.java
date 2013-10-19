package pohaci.gumunda.titis.accounting.cgui;

import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.application.ObjectCellEditor;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.SearchCustomerDetailDlg;

public class CustomerCellEditor extends ObjectCellEditor implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerCellEditor(JFrame owner, Connection conn, long sessionid) {
		super(owner, conn, sessionid);		
	}

	public void done() {
		SearchCustomerDetailDlg dlg = new SearchCustomerDetailDlg(m_owner, m_conn, m_sessionid);
	    dlg.setVisible(true);
	    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
	      Customer[] customer = dlg.getCustomer();
	      if(customer.length > 0)
	        setObject(customer[0]);
	    }
	}
}
