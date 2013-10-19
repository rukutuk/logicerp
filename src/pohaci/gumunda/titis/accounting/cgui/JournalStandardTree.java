package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */


import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class JournalStandardTree extends JTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  public JournalStandardTree(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Journal Standard"));
    setModel(model);
    setCellRenderer(new JournalStandardTreeCellRenderer());
    ToolTipManager.sharedInstance().registerComponent(this);
  }

  void initData(){
    DefaultTreeModel model = (DefaultTreeModel)getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      JournalStandard[] journal = logic.getSuperJournalStandard(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < journal.length; i++){
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(journal[i]);
        model.insertNodeInto(child, root, root.getChildCount());

        setSubJournalStandard(child, model);
      }

      expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setSubJournalStandard(DefaultMutableTreeNode parent, DefaultTreeModel model) throws Exception {
    AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
    JournalStandard activity = (JournalStandard)parent.getUserObject();

    JournalStandard[] subjournal = logic.getSubJournalStandard(m_sessionid, IDBConstants.MODUL_MASTER_DATA, activity.getIndex());
    for(int i = 0; i < subjournal.length; i++){
      subjournal[i].setParent((JournalStandard)parent.getUserObject());
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(subjournal[i]);
      model.insertNodeInto(child, parent, parent.getChildCount());

      setSubJournalStandard(child, model);
    }
  }
}