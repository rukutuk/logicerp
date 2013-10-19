package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class OrganizationTreeDlg extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	JFrame m_mainframe;
	OrganizationTree m_tree;
	JButton m_okBt, m_cancelBt;
	
	int m_iResponse = JOptionPane.NO_OPTION;
	Organization m_org = null;

	boolean m_isroot;
	public OrganizationTreeDlg(JFrame owner, Connection conn, long sessionid,boolean isroot) {		
		super(owner, "Department", true);
		m_isroot = isroot;
		setSize(350, 350);
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		
		constructComponent();
	}

	void constructComponent() {
		m_tree = new OrganizationTree(m_conn, m_sessionid);   
		
		m_tree.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					onOK();
				}
			}
		});
		
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
			JOptionPane.showMessageDialog(this, "Departement not yet been selected");
			return;
		}
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		
		if (!m_isroot){
			if(node.getChildCount() > 0)
				return;
			
			if(node.getUserObject() instanceof Organization) {
				m_org = (Organization)node.getUserObject();
				m_iResponse = JOptionPane.OK_OPTION;
				dispose();
			}
		}
		else{
			if(node.getUserObject() instanceof Organization) {
				m_org = (Organization)node.getUserObject();
				m_iResponse = JOptionPane.OK_OPTION;
				dispose();
			}
			else{
				m_org = new Organization("","","Department");
				m_iResponse = JOptionPane.OK_OPTION;
				dispose();
			}
		}
	}

	public Organization getOrganization() {
		return m_org;
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