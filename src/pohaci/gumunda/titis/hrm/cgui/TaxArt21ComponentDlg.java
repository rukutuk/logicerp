package pohaci.gumunda.titis.hrm.cgui;

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
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TaxArt21ComponentDlg extends JDialog implements ActionListener,TreeSelectionListener{
	
	private static final long serialVersionUID = 1L;
	private JFrame m_mainframe;
	private Connection m_conn;
	private long m_sessionid;
	private JButton m_okBt, m_cancelBt;
	private int m_iResponse = JOptionPane.NO_OPTION;
	private TaxArt21ComponentTree m_tree;
	private TaxArt21Component taxArt21Component;
	DefaultMutableTreeNode m_node;
	
	public TaxArt21ComponentDlg(JFrame owner, Connection conn, long sessionid) {
		super(owner, "Department", true);
		setSize(350, 350);
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		
		constructComponent();
	}

	void constructComponent() {
		m_tree = new TaxArt21ComponentTree(m_conn, m_sessionid);
		m_tree.addTreeSelectionListener(this);
		m_tree.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					//onOK();
					valueChanged(null);
					if(taxArt21Component == null)
						return;
					m_iResponse = JOptionPane.OK_OPTION;
					dispose();
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

	public JTree getTree(){
		return m_tree;
	}
	
	public TreeModel getTreeModel(){
		return m_tree.getModel();
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
		if(node.getChildCount() > 0)
			return;
		
		if(node.getUserObject() instanceof TaxArt21Component) {
			taxArt21Component = (TaxArt21Component)node.getUserObject();
			m_iResponse = JOptionPane.OK_OPTION;
			dispose();
		}
	}

	public TaxArt21Component getTaxArt21Component() {
		return taxArt21Component;
	}
	public DefaultMutableTreeNode getSelectedNode() {
		return m_node;
	}
	public int getResponse() {
		return m_iResponse;
	}
	
	public void actionPerformed(ActionEvent e) {
		/*if(e.getSource() == m_okBt) {
			onOK();
		}*/
		 if(e.getSource() == m_cancelBt) {
			dispose();
		}
		
		if(e.getSource() == m_okBt){
			if(taxArt21Component == null)
				return;
			m_iResponse = JOptionPane.OK_OPTION;
			dispose();
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		//TreePath path = e.getNewLeadSelectionPath();
		TreePath path = m_tree.getSelectionPath();
		if(path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			if(node != m_tree.getModel().getRoot()) {
				taxArt21Component = (TaxArt21Component)node.getUserObject();
				m_node = node;
			}else { 
				taxArt21Component = new TaxArt21Component(-1, "", "TaxArt21 Component", true, null,
						null, null, false, -1, -1, false, false, "", -1);
				node.setUserObject(taxArt21Component);
				m_node = node;
			}
		}
	}
}
