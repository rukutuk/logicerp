package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;

import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class OrganizationTree extends JTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid = -1;

	public OrganizationTree(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		
		constructComponent();
		initData();
	}

	void constructComponent(){
		// definisikan root
		DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Department"));
		setModel(model);
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	void initData(){
		DefaultTreeModel model = (DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			Organization[] org = logic.getSuperOrganization(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			
			for(int i = 0; i < org.length; i++){    	 
				DefaultMutableTreeNode child = 
					new DefaultMutableTreeNode(
							new Organization(org[i], 
									EmployeeAttribute.CODE_DESCRIPTION));
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
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		Organization org = (Organization)parent.getUserObject();
		
		Organization[] suborg = logic.getSubOrganization(m_sessionid, IDBConstants.MODUL_MASTER_DATA, org.getIndex());
		for(int i = 0; i < suborg.length; i++){    	
			DefaultMutableTreeNode child =
				new DefaultMutableTreeNode(
						new Organization(suborg[i],
								EmployeeAttribute.CODE_DESCRIPTION));
			model.insertNodeInto(child, parent, parent.getChildCount());
			
			setSubAccount(child, model);
		}
	}
}