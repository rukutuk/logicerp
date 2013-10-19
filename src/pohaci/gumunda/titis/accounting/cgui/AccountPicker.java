package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.accounting.entity.Account;

public class AccountPicker extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	JTextField m_accountTextField;
	JButton m_browseBt = new JButton("...");
	Connection m_conn = null;
	long m_sessionid = -1;
	Account m_account = null;
	private boolean isGroupSelectionEnable = false;

	public AccountPicker() {
		this(null,0);
	}
	
	public void init(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		
	}
	
	public AccountPicker(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		
		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);
		
		m_accountTextField = new JTextField() {
			private static final long serialVersionUID = 1L;
			public void addMouseListener(
					MouseListener l) { }
			public boolean isFocusTraversable() {
				return false;
			}
		};
		
		setLayout(new BorderLayout(3, 1));
		add(m_accountTextField, BorderLayout.CENTER);
		add(m_browseBt, BorderLayout.EAST);
	}
	
	public AccountPicker(Connection conn, long sessionid, boolean isGroupSelectionEnable) {
		m_conn = conn;
		m_sessionid = sessionid;
		this.isGroupSelectionEnable = isGroupSelectionEnable;
		
		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);
		
		m_accountTextField = new JTextField() {
			private static final long serialVersionUID = 1L;
			public void addMouseListener(
					MouseListener l) { }
			public boolean isFocusTraversable() {
				return false;
			}
		};
		
		setLayout(new BorderLayout(3, 1));
		add(m_accountTextField, BorderLayout.CENTER);
		add(m_browseBt, BorderLayout.EAST);
	}
	
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		m_accountTextField.setEditable(enable);
		m_browseBt.setEnabled(enable);
	}
	
	public Account getAccount() {
		return m_account;
	}
	
	public void setAccount(Account account) {
		Account old = m_account;
		m_account = account;
		if(account != null)
			m_accountTextField.setText(m_account.toString());
		else
			m_accountTextField.setText("");
		firePropertyChange("account", account, old);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_browseBt) {
			AccountTreeDlg dlg = new AccountTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					"Account", m_conn, m_sessionid, isGroupSelectionEnable);
			dlg.setVisible(true);
			setAccount(dlg.getAccount());
		}
	}
}