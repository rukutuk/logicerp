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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class PayrollComponentSubmitTree extends PayrollComponentTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PayrollComponentSubmitTree(Connection conn, long sessionid, String paymentType) {
    super(conn, sessionid, paymentType);
  
  }

  void setSubPayrollComponent(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, DefaultTreeModel model, PayrollComponent[] component) throws Exception {
    for(int i = 0; i < component.length; i++){      
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode(component[i]);      

      if(component[i].isGroup()) {
    	 
        PayrollComponent[] subcomponent = getPayrollComponent(component[i].getIndex());
        setSubPayrollComponent(child, subchild, model, subcomponent);
      }
      else {
    	  if(m_type.equals(PayrollComponentTree.FIELD_ALLOWANCE)) {
              if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
                  component[i].getPaymentAsString().equals(PayrollComponent.PAYCHEQUE)) &&
                  (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
                  component[i].getSubmitAsString().equals(PayrollComponent.FIELD_ALLOWANCE)))
            	  model.insertNodeInto(subchild, child, child.getChildCount());
          }
        
    	  else if(m_type.equals(PayrollComponentTree.PAYCHEQUE)) {
    		  if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
	              component[i].getPaymentAsString().equals(PayrollComponent.PAYCHEQUE)) &&
	              (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.PAYCHEQUE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.FIELD_ALLOWANCE)))
    			  model.insertNodeInto(subchild, child, child.getChildCount());
    	  }
    	  
    	  else if(m_type.equals(PayrollComponentTree.PAYCHEQUE_RECEIVABLES)) {
    		  if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
	              component[i].getPaymentAsString().equals(PayrollComponent.PAYCHEQUE)) &&
	              (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.PAYCHEQUE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.FIELD_ALLOWANCE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.EMPLOYEE_RECEIVABLES)))
    			  model.insertNodeInto(subchild, child, child.getChildCount());
    	  }

    	  else if(m_type.equals(PayrollComponentTree.MEAL_ALLOWANCE)) {
    		  if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
	              component[i].getPaymentAsString().equals(PayrollComponent.NON_PAYCHEQUE)) &&
	              (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.MEAL_ALLOWANCE)))
    			  model.insertNodeInto(subchild, child, child.getChildCount());
    	  }

    	  else if(m_type.equals(PayrollComponentTree.TRANSPORTATION_ALLOWANCE)) {
    		  if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
	              component[i].getPaymentAsString().equals(PayrollComponent.NON_PAYCHEQUE)) &&
	              (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.TRANSPORTION_ALLOWANCE)))
    			  model.insertNodeInto(subchild, child, child.getChildCount());
    	  }

    	  else if(m_type.equals(PayrollComponentTree.OVERTIME)) {
    		  if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
	              component[i].getPaymentAsString().equals(PayrollComponent.NON_PAYCHEQUE)) &&
	              (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.OVERTIME)))
    			  model.insertNodeInto(subchild, child, child.getChildCount());
    	  }

    	  else if(m_type.equals(PayrollComponentTree.OTHER_ALLOWANCE)) {
    		  if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
	              component[i].getPaymentAsString().equals(PayrollComponent.NON_PAYMENT)) &&
	              (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.OTHER_ALLOWANCE)))
    			  model.insertNodeInto(subchild, child, child.getChildCount());
    	  }

    	  else if(m_type.equals(PayrollComponentTree.INSURANCE_ALLOWANCE)) {
    		  if((component[i].getPaymentAsString().equals(PayrollComponent.NONE) ||
	              component[i].getPaymentAsString().equals(PayrollComponent.NON_PAYMENT)) &&
	              (component[i].getSubmitAsString().equals(PayrollComponent.NONE) ||
	              component[i].getSubmitAsString().equals(PayrollComponent.INSURANCE_ALLOWANCE)))
    			  model.insertNodeInto(subchild, child, child.getChildCount());
    	  }
      }
    }

    if(child.getChildCount() > 0) {
      model.insertNodeInto(child, parent, parent.getChildCount());
    }
  }
}