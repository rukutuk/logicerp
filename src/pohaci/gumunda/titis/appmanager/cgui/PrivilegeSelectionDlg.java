package pohaci.gumunda.titis.appmanager.cgui;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;

public class PrivilegeSelectionDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane scrollPane = null;
	private JPanel buttonPanel = null;
	private JButton saveButton = null;
	private JButton cancelButton = null;
	private JTree tree = null;
	private Connection connection = null;
	private int response = JOptionPane.NO_OPTION;
	private ApplicationFunction function = null;
	private Frame owner = null;

	/**
	 * This is the default constructor
	 */
	public PrivilegeSelectionDlg(Frame owner, String title, Connection connection) {
		super(owner, title);
		this.owner = owner;
		this.connection = connection;
		setModal(true);
		initialize();
	}

	public void setVisible(boolean flag) {
		Rectangle rc = owner.getBounds();
	    Rectangle rcthis = getBounds();
	    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
	              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
	              (int)rcthis.getWidth(), (int)rcthis.getHeight());

	    super.setVisible(flag);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(430, 339);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
			jContentPane.add(getScrollPane(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTree());
		}
		return scrollPane;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout);
			buttonPanel.add(getSaveButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Add");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onAdd();
				}
			});
		}
		return saveButton;
	}

	protected void onAdd() {
		TreePath path = getTree().getSelectionPath();
		if(path==null){
			JOptionPane.showMessageDialog(this, "Privilege has not been selected.");
			return;
		}
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		if(node.getUserObject() instanceof ApplicationFunction){
			setPrivilege((ApplicationFunction)node.getUserObject());
			setResponse(JOptionPane.YES_OPTION);
			setVisible(false);
		}
	}

	private void setPrivilege(ApplicationFunction function) {
		this.function = function;
	}
	
	public ApplicationFunction getPrivilege(){
		return this.function;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onCancel();
				}
			});
		}
		return cancelButton;
	}

	protected void onCancel() {
		setResponse(JOptionPane.NO_OPTION);
		setVisible(false);
	}

	/**
	 * This method initializes tree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTree() {
		if (tree == null) {
			tree = new FunctionTree(connection);
		}
		return tree;
	}

	public int getResponse() {
		return response;
	}

	private void setResponse(int response) {
		this.response = response;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
