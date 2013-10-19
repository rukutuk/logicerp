package pohaci.gumunda.titis.accounting.cgui;


import java.sql.Connection;

import pohaci.gumunda.titis.accounting.cgui.panelloader.ITabbedTransactionPanel;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;

public class ExpenseSheetPanel extends javax.swing.JPanel implements ITabbedTransactionPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8577560244592607123L;
	Connection m_conn = null;
	long m_sessionid = -1;  
	public ExpenseSheetPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initComponents();
	}

	private void initComponents() {	
		expenseOthersPanel = new ExpenseOthersPanel(m_conn, m_sessionid,this);
		expenseProjectPanel = new ExpenseProjectPanel(m_conn, m_sessionid,this);
		jSplitPane1 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		setLayout(new java.awt.BorderLayout());
		setPreferredSize(new java.awt.Dimension(950, 675));
		jSplitPane1.setPreferredSize(new java.awt.Dimension(950, 675));
		jSplitPane1.setLayout(new java.awt.BorderLayout());
		jPanel1.setLayout(new java.awt.BorderLayout());
		jTabbedPane1.setPreferredSize(new java.awt.Dimension(100, 675));
		jTabbedPane1.addTab("Project", expenseProjectPanel);
		jTabbedPane1.addTab("Others", expenseOthersPanel);
		jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);
		jSplitPane1.add(jPanel1,java.awt.BorderLayout.CENTER);
		add(jSplitPane1, java.awt.BorderLayout.CENTER);
	}
	
	private ExpenseOthersPanel expenseOthersPanel;
	private ExpenseProjectPanel expenseProjectPanel;
	private javax.swing.JPanel jPanel1;
	//private ExpenseSheetListPanel ESListPanel;
	//private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JPanel jSplitPane1;
	private javax.swing.JTabbedPane jTabbedPane1;
	
	public void openAndLoadObject(StateTemplateEntity obj) {
		ExpenseSheet exp = (ExpenseSheet) obj;
		// project kah?
		if(exp.getProject()!=null){
			// project dong...
			jTabbedPane1.setSelectedIndex(0);
			expenseProjectPanel.doLoad(obj);
		}else {
			jTabbedPane1.setSelectedIndex(1);
			expenseOthersPanel.doLoad(obj);
		}
	}

 /*public void stateChanged(ChangeEvent e){
	 ESListPanel.setObjnya(jTabbedPane1.getSelectedIndex()+1);
	 ESListPanel.refreshData();
 }
  
 public void setEditableList(boolean bol){
	 ESListPanel.enabledList(bol);
 }
 public ExpenseSheetListPanel getListComp(){
  return ESListPanel;
 }*/
}
