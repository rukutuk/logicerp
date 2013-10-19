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

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class PayrollCategoryTree extends JTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn;
  long m_sessionid = -1;

  public PayrollCategoryTree(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent(){
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Payroll Category"));
    setModel(model);    
    setCellRenderer(new NoIconTreeCellRenderer());
    ToolTipManager.sharedInstance().registerComponent(this);
  }

  void initData(){
    DefaultTreeModel model = (DefaultTreeModel)getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      PayrollCategory[] category = logic.getAllPayrollCategory(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < category.length; i++){
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(category[i]);
        model.insertNodeInto(child, root, root.getChildCount());

        setPayrollCategoryEmployee(child, model);
      }

      expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
    }
    catch(Exception ex){
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setPayrollCategoryEmployee(DefaultMutableTreeNode parent, DefaultTreeModel model) throws Exception {
    HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
    PayrollCategory category = (PayrollCategory)parent.getUserObject();

    Employee[] employee = logic.getPayrollCategoryEmployee(m_sessionid, IDBConstants.MODUL_MASTER_DATA, category.getIndex());
    for(int i = 0; i < employee.length; i++){
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(employee[i]);
      model.insertNodeInto(child, parent, parent.getChildCount());
    }
  }
}