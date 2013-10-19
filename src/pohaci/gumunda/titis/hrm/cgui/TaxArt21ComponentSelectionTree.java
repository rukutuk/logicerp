package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class TaxArt21ComponentSelectionTree extends TaxArt21ComponentTree {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TaxArt21ComponentSelectionTree(Connection conn, long sessionid,
			TaxArt21Component[] componentsSelected) {
		super(conn, sessionid,componentsSelected);
		
	}
	
	void initData(){
		DefaultTreeModel model = (DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			TaxArt21Component[] component = logic.getSuperTaxArt21Component(m_sessionid, 
					IDBConstants.MODUL_MASTER_DATA);
			//scanning
			for(int i = 0; i < component.length; i++){    
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(component[i]);
				
				if(component[i].isGroup()) {
					TaxArt21Component[] subcomponent = getTaxArt21Component(component[i].getIndex());          
					setSubTaxArt21Component(root, child, model, subcomponent);
				}else{
					model.insertNodeInto(child, root, root.getChildCount());
				}
				if(child.getParent()== root){
					additionHeaderColumn(model, root);
				}
			}
			expandPath(new TreePath(model.getPathToRoot((TreeNode)model.getRoot()))); 
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	
	protected void setSubTaxArt21Component(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, 
			DefaultTreeModel model, TaxArt21Component[] component) throws Exception {
		
		for(int i=0; i<component.length; i++){
			DefaultMutableTreeNode subchild = new DefaultMutableTreeNode(component[i]);
			if(component[i].isGroup()) {		    	 
				TaxArt21Component[] subcomponent = getTaxArt21Component(component[i].getIndex());
				setSubTaxArt21Component(child, subchild, model, subcomponent);
			}else{
				if(inSelection(component[i])){
					model.insertNodeInto(subchild, child, child.getChildCount());
				}
			}	
		}
		if(child.getChildCount()>0){
			model.insertNodeInto(child, parent, parent.getChildCount());
		}
	}
	
	private boolean inSelection(TaxArt21Component component){
		boolean found = false;
		if(m_componentsSelected!=null){
			for(int i=0; i<m_componentsSelected.length; i++){
				if(component.getIndex()== m_componentsSelected[i].getIndex()){
					/*StringTokenizer token = new StringTokenizer(component.getAccount().getCode(),".");
					if(token.countTokens()>0){
						token.nextToken();
						String akunValue = token.nextToken();
						new Double(akunValue).doubleValue();
						if(new Double(akunValue).doubleValue()==1){*/
							found = true;
							break;
					/*	}
					}*/
				}
			}
		}
		return found;
	}
	
	private void additionHeaderColumn(DefaultTreeModel model, DefaultMutableTreeNode root) {
		DefaultMutableTreeNode netIncome = new DefaultMutableTreeNode("Net Income");
		model.insertNodeInto(netIncome, root, root.getChildCount());
		DefaultMutableTreeNode MonthlyNet = new DefaultMutableTreeNode("Monthly");
		DefaultMutableTreeNode YearlyNet = new DefaultMutableTreeNode("Yearly");
		model.insertNodeInto(MonthlyNet, netIncome, netIncome.getChildCount());
		model.insertNodeInto(YearlyNet, netIncome, netIncome.getChildCount());
		
		DefaultMutableTreeNode PTKP = new DefaultMutableTreeNode("Yearly PTKP");
		model.insertNodeInto(PTKP, root, root.getChildCount());
		
		DefaultMutableTreeNode PKP = new DefaultMutableTreeNode("PKP");
		model.insertNodeInto(PKP, root, root.getChildCount());
		DefaultMutableTreeNode Amount = new DefaultMutableTreeNode("Amount");
		DefaultMutableTreeNode Rounded = new DefaultMutableTreeNode("Rounded");
		model.insertNodeInto(Amount, PKP, PKP.getChildCount());
		model.insertNodeInto(Rounded, PKP, PKP.getChildCount());
		
		DefaultMutableTreeNode TaxArt21 = new DefaultMutableTreeNode("Tax Art 21");
		model.insertNodeInto(TaxArt21, root, root.getChildCount());
		DefaultMutableTreeNode MonthlyTax = new DefaultMutableTreeNode("Monthly");
		DefaultMutableTreeNode YearlyTax = new DefaultMutableTreeNode("Yearly");
		model.insertNodeInto(YearlyTax, TaxArt21, TaxArt21.getChildCount());
		model.insertNodeInto(MonthlyTax, TaxArt21, TaxArt21.getChildCount());
	}
}
