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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class TaxArt21PayrollComponentTreePanelPlus extends JDialog implements
		ActionListener, TreeSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Connection m_conn = null;

	long m_sessionid = -1;

	PayrollComponentTree m_tree;

	JPanel okCancelPanel;

	JButton okButton, cancelButton;

	JPanel pctPanel;

	PayrollComponent m_payComp;

	private int m_iResponse;

	public TaxArt21PayrollComponentTreePanelPlus(JFrame owner, Connection conn,
			long sessionid) {
		super(owner, "Payroll Component", true);
		m_conn = conn;
		m_sessionid = sessionid;

		constructComponent();
	}

	void constructComponent() {
		pctPanel = new JPanel();
		pctPanel.setLayout(new BorderLayout());
		okCancelPanel = new JPanel();
		okButton = new JButton("ok");
		okButton.addActionListener(this);
		cancelButton = new JButton("cancel");
		cancelButton.addActionListener(this);
		okCancelPanel.add(okButton);
		okCancelPanel.add(cancelButton);

		m_tree = new PayrollComponentTree(m_conn, m_sessionid,PayrollComponentTree.NONE);
		m_tree.addTreeSelectionListener(this);
		// m_tree.addMouseListener(new TreeMouseAdapter());

		pctPanel.add(new JScrollPane(m_tree), BorderLayout.CENTER);
		pctPanel.add(okCancelPanel, BorderLayout.SOUTH);

		setSize(250, 450);

		getContentPane().add(pctPanel);
	}

	void ok() {
//		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		TreePath path = m_tree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		System.out.println(node + "  " + path);

	}

	public void setVisible(boolean flag) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width / 2) - (getWidth() / 2), (dim.height / 2)
				- (getHeight() / 2));
		super.setVisible(flag);
	}

	public PayrollComponent getPayrollComponent() {
		return m_payComp;
	}

	public int getResponse() {
		return m_iResponse;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			if (m_payComp == null)
				return;

			if (m_payComp.isGroup()) {
				JOptionPane.showMessageDialog(this,
						"Please select non group account");
				return;
			}
			
			firePropertyChange("payroll", m_payComp,null );
			m_iResponse = JOptionPane.OK_OPTION;
			System.out.println("m_account = "+m_payComp);
			dispose();
		} else if (e.getSource() == cancelButton) {
			dispose();
		}

	}

	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if (path == null)
			return;

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		if (node != m_tree.getModel().getRoot()) {
			m_payComp = (PayrollComponent) node.getUserObject();
		}
	}

}
