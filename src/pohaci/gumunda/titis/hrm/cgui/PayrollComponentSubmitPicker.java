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
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pohaci.gumunda.titis.application.*;

public class PayrollComponentSubmitPicker extends AttributePicker {
 
	private static final long serialVersionUID = 1L;
	private String m_type;
	private PayrollComponentTree m_tree;
	private Vector m_componentVector;
	private DefaultMutableTreeNode m_node;
	private boolean m_selected = false;
	
	public PayrollComponentSubmitPicker(Connection conn, long sessionid, String paymentType) {
		super(conn, sessionid);    
		m_type = paymentType;
		m_tree = new PayrollComponentSubmitTree(m_conn, m_sessionid, m_type);
		m_componentVector = new Vector();
	}

	public void done() {
		PayrollComponentSubmitTreeDlg dlg = new PayrollComponentSubmitTreeDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_tree);
		dlg.setVisible(true);
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			setObject(dlg.getPayrollComponent());
			m_node = dlg.getSelectedNode();
		}
	}
	
	public DefaultMutableTreeNode getSelectedNode() {
		return m_node;
	}

	public PayrollComponent[] getRootPayrollComponents() {
		m_componentVector = new Vector(); 
		PayrollComponent parent  = new PayrollComponent(-1, "", "Payroll Component", true, null,
				Short.parseShort("-1"), Short.parseShort("-1"), null, "");
		PayrollComponent payrollComponentChosen = parent;    
		
		if(payrollComponentChosen!=null){
			getAllChildNodes(payrollComponentChosen);
			m_selected = true;
		}
		PayrollComponent[] result = new PayrollComponent[m_componentVector.size()];    
		m_componentVector.copyInto(result);     
		return result;
	}
	public PayrollComponent[] getPayrollComponentsSelected() {
		m_componentVector = new Vector(); 
		PayrollComponent payrollComponentChosen = (PayrollComponent) getObject();    
		if(payrollComponentChosen!=null){
			getAllChildNodes(payrollComponentChosen);
			m_selected = true;
		}
		PayrollComponent[] result = new PayrollComponent[m_componentVector.size()];    
		m_componentVector.copyInto(result);     
		return result;
	}

	private void getAllChildNodes(PayrollComponent payrollComponentChosen) {
		DefaultMutableTreeNode parent = null;
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		//test
		parent = findNode(payrollComponentChosen, model);
		parent = null;
		
		if(payrollComponentChosen.getIndex()==-1){
			parent = (DefaultMutableTreeNode) model.getRoot();
		}else{
			parent = findNode(payrollComponentChosen, model);
		}
		if(parent!=null)
			getChildNodes(parent, model);
		//test
		parent = null;
		parent = findNode(payrollComponentChosen, model);
	}

	private void getChildNodes(DefaultMutableTreeNode parent, DefaultTreeModel model){
		DefaultMutableTreeNode node;
		if(model.getChildCount(parent)>0){
			for(int i=0; i<model.getChildCount(parent); i++){
				node = (DefaultMutableTreeNode) model.getChild(parent, i);
				getChildNodes(node, model);
			}
		}else{
			m_componentVector.addElement(parent.getUserObject());
		}
	}
  
	public boolean getSelected(){
		return m_selected;
	}

	private DefaultMutableTreeNode findNode(Object obj, DefaultTreeModel model){
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		DefaultMutableTreeNode node = null;
		
		if(root!=null){
			for(Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();){
				DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();       
				if(obj.equals(current.getUserObject())){
					node = current;
					//break;
				}
			}
		}
		return node;
	}
}
