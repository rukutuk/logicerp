package pohaci.gumunda.titis.project.cgui.report;

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

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.AccountTreeCellRenderer;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;

public class AccountbebanTree extends JTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn;
  long m_sessionid = -1;

  public AccountbebanTree(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent(){
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Account"));
    setModel(model);
    setCellRenderer(new AccountTreeCellRenderer());
    ToolTipManager.sharedInstance().registerComponent(this);
  }
  
  void initData(){
	  DefaultTreeModel model = (DefaultTreeModel)getModel();
	  DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
	  
	  try {
		  VariableAccountSetting vars = VariableAccountSetting.createVariableAccountSetting(m_conn,m_sessionid,IConstants.ATTR_VARS_COST_AND_EXPENSE);
		  
		  if (vars==null){
			  JOptionPane.showMessageDialog(this, "No variable account setting for Cost and Expense has been defined yet");
			  return;
		  }
			  
		  if (vars.getAccount()==null){
			  JOptionPane.showMessageDialog(this, "No variable account setting for Cost and Expense has been defined yet");
			  return;
		  }
			  
		  DefaultMutableTreeNode child = new DefaultMutableTreeNode(vars.getAccount());
		  model.insertNodeInto(child, root, root.getChildCount());
		  setSubAccount(child, model);
		  expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
	  }
	  catch(Exception ex){
		  JOptionPane.showMessageDialog(this, ex.getMessage(),"Warning", JOptionPane.WARNING_MESSAGE);
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