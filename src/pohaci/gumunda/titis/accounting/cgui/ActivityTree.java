package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Activity;

public class ActivityTree extends JTree {
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid = -1;
	
	public ActivityTree(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		initData();
	}
	
	void constructComponent(){
		DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Activity"));
		setModel(model);
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	void initData(){
		DefaultTreeModel model = (DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			Activity[] activity = logic.getSuperActivity(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			for(int i = 0; i < activity.length; i++){
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(activity[i]);
				model.insertNodeInto(child, root, root.getChildCount());
				setSubAccount(child, model);
			}
			expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void setSubAccount(DefaultMutableTreeNode parent, DefaultTreeModel model) throws Exception {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		Activity activity = (Activity)parent.getUserObject();
		Activity[] subactivity = logic.getSubActivity(m_sessionid, IDBConstants.MODUL_MASTER_DATA, activity.getIndex());
		for(int i = 0; i < subactivity.length; i++){
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(subactivity[i]);
			model.insertNodeInto(child, parent, parent.getChildCount());
			setSubAccount(child, model);
		}
	}
}