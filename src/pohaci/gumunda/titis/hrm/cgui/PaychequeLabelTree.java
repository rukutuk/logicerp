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

public class PaychequeLabelTree extends JTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn;
  long m_sessionid = -1;

  public PaychequeLabelTree(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent(){
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Paycheque Label"));
    setModel(model);
    ToolTipManager.sharedInstance().registerComponent(this);
  }

  void initData(){
    DefaultTreeModel model = (DefaultTreeModel)getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      PaychequeLabel[] label = logic.getSuperPaychequeLabel(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < label.length; i++){
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(label[i]);
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
    PaychequeLabel label = (PaychequeLabel)parent.getUserObject();

    PaychequeLabel[] sublabel = logic.getSubPaychequeLabel(m_sessionid, IDBConstants.MODUL_MASTER_DATA, label.getIndex());
    for(int i = 0; i < sublabel.length; i++){
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(sublabel[i]);
      model.insertNodeInto(child, parent, parent.getChildCount());

      setSubAccount(child, model);
    }
  }
}