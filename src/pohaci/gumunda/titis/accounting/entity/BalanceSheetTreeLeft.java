package pohaci.gumunda.titis.accounting.entity;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dwx
 * @version 1.0
 */

import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;

import pohaci.gumunda.titis.accounting.cgui.BalanceSheetDesignLeftCellRenderer;

public class BalanceSheetTreeLeft extends JTree {
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid = -1;
	String[] m_data = {"Laporan versi lengkap [Neraca]","Laporan versi PSAK [Neraca]","Laporan Internal [Neraca]"};
	
	public BalanceSheetTreeLeft(Connection conn, long sessionid) {
		
		m_conn = conn;
		m_sessionid = sessionid;		
		constructComponent();
		initData();
	}
	
	void constructComponent(){
		DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Balance Sheet Design"));
		setCellRenderer(new BalanceSheetDesignLeftCellRenderer());
		setModel(model);
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	void initData(){
		DefaultTreeModel model = (DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		
		try {    
			for(int i = 0; i < m_data.length; i++){    	
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(m_data[i]);
				model.insertNodeInto(child, root, root.getChildCount());
			}			
			expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
		
	void setSubAccount(DefaultMutableTreeNode parent, DefaultTreeModel model) throws Exception {		
		for(int i = 0; i < m_data.length; i++){			
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(m_data[i]);
			model.insertNodeInto(child, parent, parent.getChildCount());
		}
	}
}