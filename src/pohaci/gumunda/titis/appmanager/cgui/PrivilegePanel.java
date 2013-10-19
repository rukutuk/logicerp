package pohaci.gumunda.titis.appmanager.cgui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.entity.Role;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;

import javax.swing.JTree;

public class PrivilegePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel buttonPanel = null;
	private JButton addButton = null;
	private JButton removeButton = null;
	private JPanel centerPanel = null;
	private Connection connection = null;
	private JScrollPane scrollPane = null;
	private JTree tree = null;
	private DefaultTreeModel defaultTreeModel;
	private DefaultMutableTreeNode projectNode;
	private DefaultMutableTreeNode hrmNode;
	private DefaultMutableTreeNode accountingNode;
	private DefaultMutableTreeNode appManagerNode;
	private Role role;
	private JButton updateButton = null;
	
	/**
	 * This is the default constructor
	 */
	public PrivilegePanel(Connection connection) {
		super();
		this.connection = connection;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(754, 385);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
		this.add(getButtonPanel(), java.awt.BorderLayout.NORTH);
		this.add(getCenterPanel(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout);
			buttonPanel.add(getAddButton(), null);
			buttonPanel.add(getRemoveButton(), null);
			buttonPanel.add(getUpdateButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes editButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add");
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onAdd();
				}
			});
		}
		return addButton;
	}

	protected void onAdd() {
		if(getRole()==null){
			JOptionPane.showMessageDialog(this, "Please choose the role first");
			return;
		}
		
		PrivilegeSelectionDlg dlg = new PrivilegeSelectionDlg(GumundaMainFrame.getMainFrame(), "Privilege Selection", connection);
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.YES_OPTION){
			ApplicationFunction privilege = dlg.getPrivilege();
			
			try{
				DefaultMutableTreeNode node = setPathToRoot(privilege);				
				setSubAccount(node, getTreeModel());
			}catch(Exception e){
				JOptionPane.showMessageDialog(this, "Failed while grant privileges");
			}
			
			getTree().setModel(getTreeModel());
		}
	}
	
	private void setSubAccount(DefaultMutableTreeNode parent, DefaultTreeModel treeModel) throws Exception {
		AppManagerLogic logic = new AppManagerLogic(connection);
		ApplicationFunction parentFunction = (ApplicationFunction) parent.getUserObject();
		ApplicationFunction[] subFunctions = logic.getSubFunctions(parentFunction);
		for(int i = 0; i < subFunctions.length; i++){
			DefaultMutableTreeNode findNode = treeContains(subFunctions[i]);
			if(findNode==null){
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(subFunctions[i]);
				getTreeModel().insertNodeInto(child, parent, parent.getChildCount());
				setSubAccount(child, getTreeModel());
			}else{
				setSubAccount(findNode, getTreeModel());
			}
		}
	}

	private DefaultMutableTreeNode setPathToRoot(ApplicationFunction privilege) throws Exception {
		DefaultMutableTreeNode appNode = getAppNode(privilege.getApplication());
		DefaultMutableTreeNode node = null;
		int i = 0;
		while(privilege.getPath().indexOf("\\", i)>0){
			i = privilege.getPath().indexOf("\\", i) + 1;
			String path = privilege.getPath().substring(0, i) + "\\";
			path = privilege.getPath().replace("\\", "\\\\");			
			AppManagerLogic logic = new AppManagerLogic(connection);
			ApplicationFunction function = logic.getFunctionByPath(path, privilege.getApplication());
			
			DefaultMutableTreeNode findNode = treeContains(function);
			if(findNode==null){
				if (node == null) {
					node = new DefaultMutableTreeNode(function);
					getTreeModel().insertNodeInto(node, appNode, appNode.getChildCount());
					//appNode.add(node);
				} else {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(
							function);
					getTreeModel().insertNodeInto(child, node, node.getChildCount());
					//node.add(child);
					node = child;
				}
			}else{
				node = findNode;
			}
		}
		getTree().expandPath(new TreePath(getTreeModel().getPathToRoot((TreeNode)getTreeModel().getRoot())));
		return node;
	}

	private DefaultMutableTreeNode treeContains(ApplicationFunction function) {
		DefaultMutableTreeNode appNode = getAppNode(function.getApplication());
		
		Enumeration enumeration = appNode.breadthFirstEnumeration();
		while(enumeration.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
			
			if(node.getUserObject() instanceof ApplicationFunction){
				ApplicationFunction funct = (ApplicationFunction) node.getUserObject();
				if(funct.getAutoindex()==function.getAutoindex())
					return node;
			}
		}
		return null;
	}

	private DefaultMutableTreeNode getAppNode(String application) {
		if(application.equals("ACCOUNTING"))
			return getAccountingNode();
		else if(application.equals("HRM"))
			return getHRMNode();
		else if(application.equals("PROJECT"))
			return getProjectNode();
		else if(application.equals("APP MANAGER"))
			return getAppManagerNode();
		return null;
	}

	private DefaultMutableTreeNode getAppManagerNode() {
		if(appManagerNode==null){
			appManagerNode = new DefaultMutableTreeNode("APP MANAGER");
		}
		return appManagerNode;
	}

	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveButton() {
		if (removeButton == null) {
			removeButton = new JButton();
			removeButton.setText("Remove");
			removeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onRemove();
				}
			});
		}
		return removeButton;
	}

	protected void onRemove() {
		if(getRole()==null){
			JOptionPane.showMessageDialog(this, "Please choose the role first");
			return;
		}
		
		TreePath path = getTree().getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		
		if(node.getUserObject() instanceof ApplicationFunction){
			removeNode(node);
		}
	}

	private void removeNode(DefaultMutableTreeNode node) {
		while(node.getChildCount() != 0){
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getFirstChild();
			removeNode(child);
		}
		getTreeModel().removeNodeFromParent(node);
	}

	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(getScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return centerPanel;
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
	 * This method initializes tree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTree() {
		if (tree == null) {
			tree = new JTree();
			tree.setModel(getTreeModel());
		}
		return tree;
	}

	private DefaultTreeModel getTreeModel() {
		if(defaultTreeModel==null){
			defaultTreeModel = createNewTreeModel();
		}
		return defaultTreeModel;
	}

	private DefaultTreeModel createNewTreeModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Privileges");
		root.insert(getAccountingNode(), root.getChildCount());
		root.insert(getHRMNode(), root.getChildCount());
		root.insert(getProjectNode(), root.getChildCount());
		root.insert(getAppManagerNode(), root.getChildCount());
		return new DefaultTreeModel(root);
	}

	private DefaultMutableTreeNode getProjectNode() {
		if(projectNode==null){
			projectNode = new DefaultMutableTreeNode("PROJECT");
		}
		return projectNode;
	}

	private DefaultMutableTreeNode getHRMNode() {
		if(hrmNode==null){
			hrmNode = new DefaultMutableTreeNode("HRM");
		}
		return hrmNode;
	}

	private DefaultMutableTreeNode getAccountingNode() {
		if(accountingNode==null){
			accountingNode = new DefaultMutableTreeNode("ACCOUNTING");
		}
		return accountingNode;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	private Role getRole() {
		return role;
	}
	
	public void loadData(){
		if(getRole()==null)
			return;
		
		AppManagerLogic logic = new AppManagerLogic(connection);
		
		clearAll();
		getTree().setModel(getTreeModel());
		
		try {
			ApplicationFunction[] privileges = logic.getFunctionsByRole(role);
			getSuperNodes(getAccountingNode(), privileges);
			getSuperNodes(getHRMNode(), privileges);
			getSuperNodes(getProjectNode(), privileges);
			getSuperNodes(getAppManagerNode(), privileges);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//getTree().setModel(getTreeModel());
	}

	private void clearAll() {
		defaultTreeModel = null;
		accountingNode = null;
		hrmNode = null;
		projectNode = null;
		appManagerNode = null;
	}

	private void getSuperNodes(DefaultMutableTreeNode node, ApplicationFunction[] privileges) throws Exception {
		AppManagerLogic logic = new AppManagerLogic(connection);
		ApplicationFunction[] functions = logic.getSuperFunctions((String) node.getUserObject());
		for(int i = 0; i < functions.length; i++){
			if(isContains(functions[i], privileges)){
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(functions[i]);
				getTreeModel().insertNodeInto(child, node, node.getChildCount());
				getSubNodes(child, privileges);
			}
		}
		getTree().expandPath(new TreePath(getTreeModel().getPathToRoot((TreeNode)getTreeModel().getRoot())));
	}

	private boolean isContains(ApplicationFunction function, ApplicationFunction[] privileges) {
		for(int i=0; i<privileges.length; i++){
			if(function.getAutoindex()==privileges[i].getAutoindex())
				return true;
		}
		return false;
	}

	private void getSubNodes(DefaultMutableTreeNode parent, ApplicationFunction[] privileges) throws Exception {
		AppManagerLogic logic = new AppManagerLogic(connection);
		ApplicationFunction parentFunction = (ApplicationFunction) parent.getUserObject();
		ApplicationFunction[] subFunctions = logic.getSubFunctions(parentFunction);
		for(int i = 0; i < subFunctions.length; i++){
			if(isContains(subFunctions[i], privileges)){
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(subFunctions[i]);
				getTreeModel().insertNodeInto(child, parent, parent.getChildCount());
				getSubNodes(child, privileges);
			}
		}
	}

	/**
	 * This method initializes updateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUpdateButton() {
		if (updateButton == null) {
			updateButton = new JButton();
			updateButton.setText("Update");
			updateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onUpdate();
				}
			});
		}
		return updateButton;
	}

	protected void onUpdate() {
		if(getRole()==null){
			JOptionPane.showMessageDialog(this, "Please choose the role first");
			return;
		}
		
		ApplicationFunction[] privileges = getSelectedPrivileges();
		
		AppManagerLogic logic = new AppManagerLogic(connection);
		try {
			logic.updateRoleMapping(getRole(), privileges);
			JOptionPane.showMessageDialog(this, "Update successfully");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to update role mapping");
		}

	}

	private ApplicationFunction[] getSelectedPrivileges() {
		ApplicationFunction[] privileges = null;
		Vector vector = new Vector();
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTreeModel().getRoot();
		Enumeration enumeration = root.breadthFirstEnumeration();
		while(enumeration.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
			
			if(node.getUserObject() instanceof ApplicationFunction){
				vector.addElement((ApplicationFunction)node.getUserObject());
			}
		}
		
		privileges = (ApplicationFunction[]) vector.toArray(new ApplicationFunction[vector.size()]);
		
		return privileges;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
