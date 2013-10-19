package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;

import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.Misc;

public class TaxArt21ComponentPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	TaxArt21ComponentTree m_tree;
	JPopupMenu m_popupMenu;
	JMenuItem mi_add, mi_edit, mi_delete;

	public TaxArt21ComponentPanel(Connection conn, long sessionid) {
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
		mi_delete = new JMenuItem("Remove");
		mi_delete.addActionListener(this);
		m_popupMenu.addSeparator();
		m_popupMenu.add(mi_delete);

		m_tree = new TaxArt21ComponentTree(m_conn, m_sessionid);
		m_tree.addMouseListener(new TreeMouseAdapter());
		//

		add(new JScrollPane(m_tree), BorderLayout.CENTER);
	}

	void onAdd() {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		TreePath path = m_tree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		TaxArt21ComponentEditorDlg dialog = null;

		if (node == model.getRoot())
			dialog = new TaxArt21ComponentEditorDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid, null);
		else
			dialog = new TaxArt21ComponentEditorDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid, node);
		dialog.setVisible(true);

		if (dialog.getResponse() == JOptionPane.OK_OPTION) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(dialog
					.getTaxArt21Component());
			System.out.println("child = " + child + " node = " + node);
			model.insertNodeInto(child, node, node.getChildCount());
			m_tree
					.scrollPathToVisible(new TreePath(model
							.getPathToRoot(child)));
		}
	}

	void onEdit() {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		TreePath path = m_tree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		TaxArt21Component component = (TaxArt21Component) node.getUserObject();
		TaxArt21ComponentEditorDlg dlg = null;
		System.out.println(component + " , " + component.getIndex());

		if (parent == model.getRoot())
			dlg = new TaxArt21ComponentEditorDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid, null, component);
		else
			dlg = new TaxArt21ComponentEditorDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid, (DefaultMutableTreeNode) node
							.getParent(), component);
		dlg.setVisible(true);

		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			node.setUserObject(dlg.getTaxArt21Component());
			model.nodeChanged(node);
		}
	}

	void onDelete() {
		TreePath path = m_tree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();

			if (!Misc.getConfirmation())
				return;

			try {
				deleteNodeParent(node);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	void deleteNodeParent(DefaultMutableTreeNode parent) throws Exception {
		while (parent.getChildCount() != 0) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent
					.getFirstChild();
			deleteNodeParent(node);
		}
		deleteNode(parent);
	}

	void deleteNode(DefaultMutableTreeNode node) throws Exception {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		logic.deleteTaxArt21Component(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, ((TaxArt21Component) node
						.getUserObject()).getIndex());
		model.removeNodeFromParent(node);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mi_add) {
			onAdd();
		} else if (e.getSource() == mi_edit) {
			onEdit();
		} else if (e.getSource() == mi_delete) {
			onDelete();
		}
	}

	/**
	 * 
	 */
	class TreeMouseAdapter extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
				TreePath path = m_tree.getSelectionPath();
				if (path != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
							.getLastPathComponent();

					if (node == model.getRoot()) {
						mi_add.setEnabled(true);
						mi_edit.setEnabled(false);
						mi_delete.setEnabled(false);
					} else {
						if (((TaxArt21Component) node.getUserObject())
								.isGroup()) {
							mi_add.setEnabled(true);
							mi_edit.setEnabled(true);
							mi_delete.setEnabled(true);
						} else {
							mi_add.setEnabled(false);
							mi_edit.setEnabled(true);
							mi_delete.setEnabled(true);
						}
					}

					Rectangle rectangle = m_tree.getPathBounds(path);
					if (rectangle.contains(e.getPoint()))
						m_popupMenu.show(m_tree, e.getX(), e.getY());
				}
			}
		}
	}
}