package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.tree.*;
import java.sql.Connection;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;

public class WorksheetColumnTree extends JTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  public WorksheetColumnTree(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent(){
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Column Name"));
    setModel(model);
    setCellRenderer(new WorksheetColumnTreeCellRenderer());
    ToolTipManager.sharedInstance().registerComponent(this);
  }

  void reloadWorksheetColumn(Worksheet worksheet) {
    DefaultTreeModel model = (DefaultTreeModel)getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

    root.removeAllChildren();
    model.reload();

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      WorksheetColumn[] column = logic.getAllWorksheetColumn(m_sessionid, IDBConstants.MODUL_MASTER_DATA, worksheet.getIndex());

      for(int i = 0; i < column.length; i ++) {
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(column[i]);
        model.insertNodeInto(child, root, root.getChildCount());
        reloadWorksheetJournal(child, model);
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }

    expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
  }

  void reloadWorksheetJournal(DefaultMutableTreeNode node, DefaultTreeModel model) throws Exception {
    AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
    Journal[] journal = logic.getAllWorksheetJournal(m_sessionid, IDBConstants.MODUL_MASTER_DATA, ((WorksheetColumn)node.getUserObject()).getIndex());

    for(int i = 0; i < journal.length; i ++) {
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(journal[i]);
      model.insertNodeInto(child, node, node.getChildCount());
    }
  }
}