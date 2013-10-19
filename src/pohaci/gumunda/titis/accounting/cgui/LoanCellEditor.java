package pohaci.gumunda.titis.accounting.cgui;

import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.ObjectCellEditor;

public class LoanCellEditor extends ObjectCellEditor implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LookupLoanPicker  m_loanPicker = null;
	private Account  account = null;
	
	public LoanCellEditor(JFrame owner, Connection conn, long sessionid){
		  super(owner, conn, sessionid);
	}
	
	public LoanCellEditor(JFrame owner, Connection conn, long sessionid, Account account){
		  super(owner, conn, sessionid);
		  this.account = account;
	}
	
	public void done() {
		m_loanPicker = new LookupLoanPicker(m_conn, m_sessionid, account);
		m_loanPicker.done();
		setObject(m_loanPicker.getObject());		
	}
}
