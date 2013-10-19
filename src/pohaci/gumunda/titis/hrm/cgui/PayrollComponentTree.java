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

public class PayrollComponentTree extends JTree {
  /**
   * 
   */
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid = -1;
	String m_type = "";
	PayrollComponent[] m_componentsSelected;
	
	public static final String NONE = "";
	public static final String PAYCHEQUE = "Paycheque";
	public static final String MEAL_ALLOWANCE = "Meal Allowance";
	public static final String TRANSPORTATION_ALLOWANCE = "Trasportaton Allowance";
	public static final String OVERTIME = "Overtime";
	public static final String OTHER_ALLOWANCE = "Other";
	public static final String INSURANCE_ALLOWANCE = "Insurance";
	public static final String FIELD_ALLOWANCE = "Field Allowance";
	public static final String PAYCHEQUE_RECEIVABLES = "Paycheque & Receivables";

  public PayrollComponentTree(Connection conn, long sessionid, String paymentType) {
    m_conn = conn;
    m_sessionid = sessionid;
    m_type = paymentType;

    constructComponent();
    initData();
  }
  
  public PayrollComponentTree(Connection conn, long sessionid, String paymentType, 
		  PayrollComponent[] componentsSelected){
	m_conn = conn;
    m_sessionid = sessionid;
    m_type = paymentType;
    m_componentsSelected = componentsSelected;

    constructComponent();
    initData();
  }

  void constructComponent(){
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Payroll Component"));
    setModel(model);
    setCellRenderer(new PayrollComponentTreeCellRenderer());
    ToolTipManager.sharedInstance().registerComponent(this);
  }

  void initData(){
	  DefaultTreeModel model = (DefaultTreeModel)getModel();
	  DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
	  
	  try {
		  HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		  PayrollComponent[] component = logic.getSuperPayrollComponent(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
		  
		  for(int i = 0; i < component.length; i++){    
			  DefaultMutableTreeNode child = new DefaultMutableTreeNode(component[i]);
			  
			  if(component[i].isGroup()) {
				  PayrollComponent[] subcomponent = getPayrollComponent(component[i].getIndex());          
				  setSubPayrollComponent(root, child, model, subcomponent);
			  }
			  else
				  model.insertNodeInto(child, root, root.getChildCount());
		  }
		  expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot())));
	  }catch(Exception ex){
		  JOptionPane.showMessageDialog(this, ex.getMessage(),
				  "Warning", JOptionPane.WARNING_MESSAGE);
	  }	
  }

  void setSubPayrollComponent(DefaultMutableTreeNode parent, DefaultMutableTreeNode child,
		  DefaultTreeModel model, PayrollComponent[] component) throws Exception {
	  
	  model.insertNodeInto(child, parent, parent.getChildCount());
	  for(int i = 0; i < component.length; i ++) {
		  DefaultMutableTreeNode subchild = new DefaultMutableTreeNode(component[i]);
		  if(component[i].isGroup()) {
			  PayrollComponent[] subcomponent = getPayrollComponent(component[i].getIndex());
			  setSubPayrollComponent(child, subchild, model, subcomponent);
		  } else
			  model.insertNodeInto(subchild, child, child.getChildCount());
	  }
  }

  PayrollComponent[] getPayrollComponent(long index) throws Exception {
    HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
    return logic.getSubPayrollComponent(m_sessionid, IDBConstants.MODUL_MASTER_DATA, index);
  }
}