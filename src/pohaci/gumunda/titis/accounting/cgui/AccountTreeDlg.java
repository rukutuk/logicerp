package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.AccountTree;

public class AccountTreeDlg extends JDialog implements ActionListener, TreeSelectionListener{
	private static final long serialVersionUID = 1L;
	JFrame m_owner;
	AccountTree m_accTree;
	JButton m_ok, m_cancel;
	
	Connection m_conn = null;
	long m_sessionid = -1;
	Account m_account = null;
	
	int m_iResponse = JOptionPane.NO_OPTION;
	
	private boolean isGroupSelectionEnable = false;
	
	public AccountTreeDlg(JFrame owner, String title, Connection conn, long sessionid) {
		super(owner, ( title == null ) ? "Chart of Account" : title, true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(350, 350);
		m_owner = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponents();
	}
	
	public AccountTreeDlg(JFrame owner, String title, Connection conn, long sessionid, boolean isGroupSelectionEnable) {
		super(owner, ( title == null ) ? "Chart of Account" : title, true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(350, 350);
		m_owner = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		this.isGroupSelectionEnable = isGroupSelectionEnable;
		constructComponents();
	}

	void constructComponents(){
		m_accTree = new AccountTree(m_conn, m_sessionid);
		m_accTree.addTreeSelectionListener(this);
		JScrollPane sp = new JScrollPane();
		sp.getViewport().add(m_accTree);
		
		m_ok = new JButton("OK");
		m_cancel = new JButton("Cancel");
		m_ok.addActionListener(this);
		m_cancel.addActionListener(this);
		
		JPanel btpanel = new JPanel();
		btpanel.add(m_ok);
		btpanel.add(m_cancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(sp, BorderLayout.CENTER);
		getContentPane().add(btpanel, BorderLayout.SOUTH);
	}
	
	public void setVisible(boolean flag){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width/2)-(getWidth()/2),(dim.height/2)-(getHeight()/2));
		super.setVisible(flag);
	}
	
	public Account getAccount(){
		return m_account;
	}
	
	public int getResponse() {
		return m_iResponse;
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_ok){
			if(m_account == null)
				return;
			
			if(m_account.isGroup()){
				if(!isGroupSelectionEnable){
					JOptionPane.showMessageDialog(this, "Please select non group account");
					return;
				}
			}
			m_iResponse = JOptionPane.OK_OPTION;
			dispose();
		}
		else if(e.getSource() == m_cancel){
			m_account = null;
			dispose();
		}
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if(path == null)
			return;
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		if(node != m_accTree.getModel().getRoot()) {
			m_account = (Account)node.getUserObject();
		}
		
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getClickCount() == 2){
			if(m_account == null)
				return;
			
			if(m_account.isGroup()){
				if(!isGroupSelectionEnable){
					JOptionPane.showMessageDialog(this, "Please select non group account");
					return;
				}
			}
			m_iResponse = JOptionPane.OK_OPTION;
			dispose();
		}
	}
}