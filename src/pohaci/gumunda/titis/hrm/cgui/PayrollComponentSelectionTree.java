package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class PayrollComponentSelectionTree extends PayrollComponentTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PayrollComponentSelectionTree(Connection conn, long sessionid,
			String paymentType, PayrollComponent[] componentsSelected) {
		super(conn, sessionid, paymentType, componentsSelected);
		
	}

	void setSubPayrollComponent(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, DefaultTreeModel model, PayrollComponent[] component) throws Exception {
		for(int i=0; i<component.length; i++){
			DefaultMutableTreeNode subchild = new DefaultMutableTreeNode(component[i]);
			
			if(component[i].isGroup()) {		    	 
		        PayrollComponent[] subcomponent = getPayrollComponent(component[i].getIndex());
		        setSubPayrollComponent(child, subchild, model, subcomponent);
		    }else{
		    	if(inSelection(component[i])){
		    		model.insertNodeInto(subchild, child, child.getChildCount());
		    	}
		    }	
		}
		if(child.getChildCount()>0){
			System.out.println("parent:" + parent.getUserObject() + " childcount:" + parent.getChildCount());
			model.insertNodeInto(child, parent, parent.getChildCount());
		}
	}
	
	private boolean inSelection(PayrollComponent component){
		boolean found = false;
		if(m_componentsSelected!=null){
			for(int i=0; i<m_componentsSelected.length; i++){
				if(component.getIndex()==m_componentsSelected[i].getIndex()){
					found = true;
					break;
				}
			}
		}
		return found;
	}
}
