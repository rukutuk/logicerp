package pohaci.gumunda.titis.accounting.cgui;

import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;
import pohaci.gumunda.titis.application.ObjectCellEditor;

public class CashCellEditor extends ObjectCellEditor implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LookupCashAccountPicker m_cashdlg = null;
	
	public CashCellEditor(JFrame owner, Connection conn, long sessionid){
		  super(owner, conn, sessionid);
	}
	public void done() {
		m_cashdlg = new LookupCashAccountPicker(m_conn, m_sessionid);
		m_cashdlg.done();
		setObject(m_cashdlg.getObject());		
	}
}
