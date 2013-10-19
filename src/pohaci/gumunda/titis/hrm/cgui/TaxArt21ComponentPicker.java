package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pohaci.gumunda.titis.application.AttributePicker;

public class TaxArt21ComponentPicker extends AttributePicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector m_componentVector;
	private boolean m_selected = false;
	DefaultMutableTreeNode m_node;
	private TaxArt21ComponentDlg dlg;

	public TaxArt21ComponentPicker(Connection conn, long sessionid) {
		super(conn, sessionid);
		m_componentVector = new Vector();
	}
	
	public void done() {
		dlg = new TaxArt21ComponentDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn,
				m_sessionid);
		dlg.setVisible(true);
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			setObject(dlg.getTaxArt21Component());
			m_node = dlg.getSelectedNode();
		}
	}
	
	public TaxArt21Component[] getRootTaxArt21Components() {
		m_componentVector = new Vector(); 
		TaxArt21Component parent  = new TaxArt21Component(-1, "", "TaxArt21 Component", true, null,
				null, null, false, -1, -1, false, false, "", -1);
		TaxArt21Component payrollComponentChosen = parent;    
		
		if(payrollComponentChosen!=null){
			getAllChildNodes(payrollComponentChosen);
			m_selected = true;
		}
		TaxArt21Component[] result = new TaxArt21Component[m_componentVector.size()];    
		m_componentVector.copyInto(result);     
		return result;
	}
	
	public TaxArt21Component[] getTaxArt21ComponentsSelected() {
		m_componentVector = new Vector(); 
		TaxArt21Component payrollComponentChosen = (TaxArt21Component) getObject();    
		if(payrollComponentChosen!=null){
			getAllChildNodes(payrollComponentChosen);
			m_selected = true;
		}
		TaxArt21Component[] result = new TaxArt21Component[m_componentVector.size()];    
		m_componentVector.copyInto(result);     
		return result;
	}

	private void getAllChildNodes(TaxArt21Component payrollComponentChosen) {
		DefaultMutableTreeNode parent = null;
		DefaultTreeModel model = (DefaultTreeModel) dlg.getTreeModel();
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
		//parent = null;
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

	private DefaultMutableTreeNode findNode(TaxArt21Component obj, DefaultTreeModel model){
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		DefaultMutableTreeNode node = null;
		
		if(root!=null){
			for(Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();){
				DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();       
				if(current.getUserObject().equals(obj)){
					node = current;
					//break;
					return node;
				}
			}
		}
		return node;
	}

}
