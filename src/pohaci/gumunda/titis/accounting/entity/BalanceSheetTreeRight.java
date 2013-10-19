package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.BalanceSheetDesignRightCellRenderer;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;

public class BalanceSheetTreeRight extends JTree {

	private static final long serialVersionUID = 1L;
Connection m_conn;
  long m_sessionid = -1;

  public BalanceSheetTreeRight(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent(){
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode(""));
    setModel(model);
    setCellRenderer(new BalanceSheetDesignRightCellRenderer());
    ToolTipManager.sharedInstance().registerComponent(this);
  }

  void initData(){
    DefaultTreeModel model = (DefaultTreeModel)getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Account[] account = logic.getSuperAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING);

      for(int i = 0; i < account.length; i++){
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(account[i]);
        model.insertNodeInto(child, root, root.getChildCount());

        if(account[i].isGroup())
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
    Account account = (Account)parent.getUserObject();

    Account[] subaccount = logic.getSubAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING, account.getIndex());
    for(int i = 0; i < subaccount.length; i++){
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(subaccount[i]);
      model.insertNodeInto(child, parent, parent.getChildCount());

      if(subaccount[i].isGroup())
          setSubAccount(child, model);	
    }
  }
}