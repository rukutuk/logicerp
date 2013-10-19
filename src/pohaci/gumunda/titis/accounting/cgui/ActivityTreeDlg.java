package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;

import pohaci.gumunda.titis.accounting.entity.Activity;

public class ActivityTreeDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	JFrame m_mainframe;
	ActivityTree m_tree;
	JButton m_okBt, m_cancelBt;
	
	int m_iResponse = JOptionPane.NO_OPTION;
	Activity m_activity = null;
	
	public ActivityTreeDlg(JFrame owner, Connection conn, long sessionid) {
		super(owner, "Activity", true);
		setSize(350, 350);
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
	}
	
	void constructComponent() {
		m_tree = new ActivityTree(m_conn, m_sessionid);
		m_okBt = new JButton("OK");
		m_okBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_okBt);
		buttonPanel.add(m_cancelBt);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(m_tree), BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		super.setVisible(flag);
	}
	
	void onOK() {
		TreePath path = m_tree.getSelectionPath();
		if(path == null) {
			JOptionPane.showMessageDialog(this, "Activity not yet been selected.");
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		if(node.getChildCount() > 0)
			return;
		if(node.getUserObject() instanceof Activity) {
			m_activity = (Activity)node.getUserObject();
			m_iResponse = JOptionPane.OK_OPTION;
			dispose();
		}
	}
	
	public Activity getActivity() {
		return m_activity;
	}
	
	public int getResponse() {
		return m_iResponse;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_okBt) {
			onOK();
		}
		else if(e.getSource() == m_cancelBt) {
			dispose();
		}
	}
}