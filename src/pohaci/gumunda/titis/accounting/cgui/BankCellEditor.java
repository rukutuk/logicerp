package pohaci.gumunda.titis.accounting.cgui;

import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;

import pohaci.gumunda.titis.application.ObjectCellEditor;

public class BankCellEditor  extends ObjectCellEditor implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LookupBankAccountPicker m_bankdlg = null;
	
	public BankCellEditor(JFrame owner, Connection conn, long sessionid){
		super(owner, conn, sessionid);		
	}
	public void done() {
		m_bankdlg = new LookupBankAccountPicker(m_conn, m_sessionid);
		m_bankdlg.done();
		setObject(m_bankdlg.getObject());	
		
	}

}
