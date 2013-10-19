package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Activity;

public class ActivityTreePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	ActivityTree m_tree;
	JPopupMenu m_popupMenu;
	JMenuItem mi_add, mi_edit, mi_delete;
	
	public ActivityTreePanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
	}
	
	void constructComponent() {
		setLayout(new BorderLayout());
		
		m_popupMenu = new JPopupMenu();
		mi_add = new JMenuItem("Add...");
		mi_add.addActionListener(this);
		m_popupMenu.add(mi_add);
		mi_edit = new JMenuItem("Edit...");
		mi_edit.addActionListener(this);
		m_popupMenu.add(mi_edit);
		mi_delete = new JMenuItem("Delete");
		mi_delete.addActionListener(this);
		m_popupMenu.addSeparator();
		m_popupMenu.add(mi_delete);
		m_tree = new ActivityTree(m_conn, m_sessionid);
		m_tree.addMouseListener(new TreeMouseAdapter());
		
		add(new JScrollPane(m_tree), BorderLayout.CENTER);
	}
	
	void onAdd() {
		DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
		TreePath path = m_tree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		ActivityEditorDlg dlg = null;
		if(node == model.getRoot())
			dlg = new ActivityEditorDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn, m_sessionid, null);
		else
			dlg = new ActivityEditorDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),  m_conn, m_sessionid, node);
		dlg.setVisible(true);
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(dlg.getActivity());
			model.insertNodeInto(child, node, node.getChildCount());
			m_tree.scrollPathToVisible( new TreePath(model.getPathToRoot(child)));
		}
	}
	
	void onEdit() {
		DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
		TreePath path = m_tree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		Activity activity = (Activity)node.getUserObject();
		ActivityEditorDlg dlg = null;
		if(parent == model.getRoot())
			dlg = new ActivityEditorDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn, m_sessionid,
					null, activity);
		else
			dlg = new ActivityEditorDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn, m_sessionid,
					(DefaultMutableTreeNode)node.getParent(), activity);
		dlg.setVisible(true);
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			node.setUserObject(dlg.getActivity());
			model.nodeChanged(node);
		}
	}
	
	void onDelete() {
		TreePath path = m_tree.getSelectionPath();
		if(path != null){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			Object[] options = {"Yes", "No"};
			int choise = JOptionPane.showOptionDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					"Are you sure deleting this " + node + " ?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(choise == JOptionPane.YES_OPTION){
				try {
					deleteNodeParent(node);
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(),
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	
	void deleteNodeParent(DefaultMutableTreeNode parent) throws Exception{
		while(parent.getChildCount() != 0){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)parent.getFirstChild();
			deleteNodeParent(node);
		}
		deleteNode(parent);
	}
	
	void deleteNode(DefaultMutableTreeNode node) throws Exception {
		DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		logic.deleteActivity(m_sessionid, IDBConstants.MODUL_MASTER_DATA, (Activity)node.getUserObject());
		model.removeNodeFromParent(node);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mi_add) {
			onAdd();
		}
		else if(e.getSource() == mi_edit) {
			onEdit();
		}
		else if(e.getSource() == mi_delete) {
			onDelete();
		}
	}
	
	class TreeMouseAdapter extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger()){
				DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
				TreePath path = m_tree.getSelectionPath();
				if(path != null){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
					
					if(node == model.getRoot()) {
						mi_add.setEnabled(true);
						mi_edit.setEnabled(false);
						mi_delete.setEnabled(false);
					}
					else {
						mi_add.setEnabled(true);
						mi_edit.setEnabled(true);
						mi_delete.setEnabled(true);
					}
					
					Rectangle rectangle = m_tree.getPathBounds(path);
					if( rectangle.contains(e.getPoint()))
						m_popupMenu.show(m_tree, e.getX(), e.getY());
				}
			}
		}
	}
}