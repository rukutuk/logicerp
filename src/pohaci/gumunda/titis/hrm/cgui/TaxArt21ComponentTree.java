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

public class TaxArt21ComponentTree extends JTree {
	
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid = -1;
	TaxArt21Component[] m_componentsSelected;
	
	public TaxArt21ComponentTree(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		initData();
	}
	
	public TaxArt21ComponentTree(Connection conn, long sessionid, TaxArt21Component[] componentsSelected) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_componentsSelected = componentsSelected;
		constructComponent();
		initData();
	}

	void constructComponent() {
		DefaultTreeModel model = new DefaultTreeModel(
				new DefaultMutableTreeNode("Tax Art 21 Component"));
		setModel(model);
		setCellRenderer(new TaxArt21ComponentTreeCellRenderer());
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	void initData(){
		DefaultTreeModel model = (DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			TaxArt21Component[] component = logic.getSuperTaxArt21Component(m_sessionid, 
					IDBConstants.MODUL_MASTER_DATA);
			
			for(int i = 0; i < component.length; i++){    
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(component[i]);
				
				if(component[i].isGroup()) {
					TaxArt21Component[] subcomponent = getTaxArt21Component(component[i].getIndex());          
					setSubTaxArt21Component(root, child, model, subcomponent);
				}
				else{
					model.insertNodeInto(child, root, root.getChildCount());
				}
			}
			expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	                                         //child                                  subChild
	 protected void setSubTaxArt21Component(DefaultMutableTreeNode parent, DefaultMutableTreeNode child,
			DefaultTreeModel model, TaxArt21Component[] taxArt21Component) throws Exception {
		
		model.insertNodeInto(child, parent, parent.getChildCount());
		for(int i = 0; i < taxArt21Component.length; i ++) {
			DefaultMutableTreeNode subchild = new DefaultMutableTreeNode(taxArt21Component[i]);
			if(taxArt21Component[i].isGroup()) {
				TaxArt21Component[] subcomponent = getTaxArt21Component(taxArt21Component[i].getIndex());
				setSubTaxArt21Component(child, subchild, model, subcomponent);
			} else
				model.insertNodeInto(subchild, child, child.getChildCount());
		}
	}
	
	TaxArt21Component[] getTaxArt21Component(long index) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		return logic.getSubTaxArt21Component(m_sessionid, IDBConstants.MODUL_MASTER_DATA, index);
	}
}