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

public class PayrollComponentSubmitTreeDlg extends JDialog implements ActionListener, TreeSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame m_mainframe;
	PayrollComponentTree m_tree;
	JButton m_okBt, m_cancelBt;
	
	PayrollComponent m_component = null;
	int m_iResponse = JOptionPane.NO_OPTION;
	DefaultMutableTreeNode m_node;

	public PayrollComponentSubmitTreeDlg(JFrame owner, PayrollComponentTree tree) {
		super(owner, "Payroll Component", true);
		setSize(350, 350);
		m_mainframe = owner;
		m_tree = tree;
		
		constructComponents();
	}

	void constructComponents(){
		m_tree.addTreeSelectionListener(this);
		
		
		m_okBt = new JButton("Save");
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

	public void setVisible(boolean flag){	  
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		
		super.setVisible(flag);
	}

	public PayrollComponent getPayrollComponent(){
		return m_component;
	}
	
	public DefaultMutableTreeNode getSelectedNode() {
		return m_node;
	}
	
	public int getResponse() {
		return m_iResponse;
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_okBt){
			if(m_component == null)
				return;
			m_iResponse = JOptionPane.OK_OPTION;
			dispose();
		}else if(e.getSource() == m_cancelBt){
			dispose();
		}
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if(path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			if(node != m_tree.getModel().getRoot()) {
				m_component = (PayrollComponent)node.getUserObject();
				m_node = node;
			}else { 
				m_component = new PayrollComponent(-1, "", "Payroll Component", true, null,
						Short.parseShort("-1"), Short.parseShort("-1"), null, "");
				node.setUserObject(m_component);
				m_node = node;
			}
		}
	}
}