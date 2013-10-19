/**
 * 
 */
package pohaci.gumunda.titis.appmanager.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;

/**
 * @author irwan
 *
 */
public class FunctionTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private DefaultTreeModel defaultTreeModel;
	private DefaultMutableTreeNode accountingNode;
	private DefaultMutableTreeNode hrmNode;
	private DefaultMutableTreeNode projectNode;
	private DefaultMutableTreeNode appManagerNode;
	
	
	public FunctionTree(Connection connection) {
		super();
		this.connection = connection;
		initialize();
	}

	private void initialize() {
		setModel(getTreeModel());
		ToolTipManager.sharedInstance().registerComponent(this);
		
		initializeData();
	}
	
	private void initializeData() {
		getSuperNodes(getAccountingNode());
		getSuperNodes(getHRMNode());
		getSuperNodes(getProjectNode());
		getSuperNodes(getAppManagerNode());
	}

	private void getSuperNodes(DefaultMutableTreeNode node) {
		try {
			AppManagerLogic logic = new AppManagerLogic(connection);
			ApplicationFunction[] functions = logic.getSuperFunctions((String) node.getUserObject());
			for(int i = 0; i < functions.length; i++){
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(functions[i]);
				getTreeModel().insertNodeInto(child, node, node.getChildCount());
				setSubAccount(child, getTreeModel());
			}
			expandPath(new TreePath(getTreeModel().getPathToRoot((TreeNode)getTreeModel().getRoot())));
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void setSubAccount(DefaultMutableTreeNode parent, DefaultTreeModel treeModel) throws Exception {
		AppManagerLogic logic = new AppManagerLogic(connection);
		ApplicationFunction parentFunction = (ApplicationFunction) parent.getUserObject();
		ApplicationFunction[] subFunctions = logic.getSubFunctions(parentFunction);
		for(int i = 0; i < subFunctions.length; i++){
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(subFunctions[i]);
			getTreeModel().insertNodeInto(child, parent, parent.getChildCount());
			setSubAccount(child, getTreeModel());
		}
	}

	private DefaultTreeModel getTreeModel() {
		if(defaultTreeModel==null){
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Application Function");
			defaultTreeModel = new DefaultTreeModel(root);
			defaultTreeModel.insertNodeInto(getAccountingNode(), root, 0);
			defaultTreeModel.insertNodeInto(getHRMNode(), root, 1);
			defaultTreeModel.insertNodeInto(getProjectNode(), root, 2);
			defaultTreeModel.insertNodeInto(getAppManagerNode(), root, 3);
		}
		return defaultTreeModel;
	}

	private DefaultMutableTreeNode getAppManagerNode() {
		if(appManagerNode==null){
			appManagerNode = new DefaultMutableTreeNode("APP MANAGER");
		}
		return appManagerNode;
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
}
