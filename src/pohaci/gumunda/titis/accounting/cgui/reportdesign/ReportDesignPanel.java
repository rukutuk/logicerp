/**
 *
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.logic.reportdesign.ReportLogic;

/**
 * @author dark-knight
 *
 */
public abstract class ReportDesignPanel extends JPanel implements
		ActionListener, TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	protected JTree designTree;
	protected JTree designRowsTree;
	protected Connection connection = null;
	protected long sessionid = -1;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	protected JButton saveChangesButton;
	protected JMenuItem addMenuItem;
	protected JMenuItem editMenuItem;
	protected JMenuItem deleteMenuItem;
	protected JMenuItem upMenuItem;
	protected JMenuItem downMenuItem;
	protected JPopupMenu popupMenu;
	protected Design selectedDesign = null;
	protected ReportRow selectedRow = null;
	protected Design root = null;
	protected ReportLogic logic = null;
	protected ReportGroup rootReportRow = null;
	protected String title = "";

	protected void init() {
		List list = logic.getDesignList();

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Design design = (Design) iterator.next();
			root.add(design);
		}

		designTree.setModel(new DefaultTreeModel(root));
	}

	protected abstract void setDefaultEntity();

	protected void constructComponent() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		addButton = new JButton("Add");
		addButton.addActionListener(this);
		editButton = new JButton("Edit");
		editButton.addActionListener(this);
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);

		JPanel listPanel = new JPanel();
		JPanel listPanel2 = new JPanel();

		JPanel buttonPanel = new JPanel();

		saveChangesButton = new JButton("Save Changes");
		saveChangesButton.addActionListener(this);
		saveChangesButton.setEnabled(false);

		JPanel buttonPanel2 = new JPanel();

		designTree.addTreeSelectionListener(this);

		popupMenu = new JPopupMenu();
		addMenuItem = new JMenuItem("Add");
		addMenuItem.addActionListener(this);
		popupMenu.add(addMenuItem);
		editMenuItem = new JMenuItem("Edit");
		editMenuItem.addActionListener(this);
		popupMenu.add(editMenuItem);
		deleteMenuItem = new JMenuItem("Delete");
		deleteMenuItem.addActionListener(this);
		popupMenu.add(deleteMenuItem);

		popupMenu.add(new JSeparator());

		upMenuItem = new JMenuItem("Up");
		upMenuItem.addActionListener(this);
		popupMenu.add(upMenuItem);
		downMenuItem = new JMenuItem("Down");
		downMenuItem.addActionListener(this);
		popupMenu.add(downMenuItem);

		designRowsTree.addMouseListener(new TreeMouseAdapter());
		designRowsTree.setCellRenderer(new TreeableReportRowTreeCellRenderer());

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		buttonPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));

		buttonPanel2.add(saveChangesButton);

		listPanel.setLayout(new BorderLayout());
		listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		listPanel.add(buttonPanel, BorderLayout.NORTH);
		listPanel.add(new JScrollPane(designTree), BorderLayout.CENTER);

		listPanel2.setLayout(new BorderLayout());
		listPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		listPanel2.add(buttonPanel2, BorderLayout.NORTH);
		listPanel2.add(new JScrollPane(designRowsTree), BorderLayout.CENTER);

		splitPane.setLeftComponent(listPanel);
		splitPane.setRightComponent(listPanel2);
		splitPane.setDividerLocation(250);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);

		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			doAdd();
		} else if (e.getSource() == editButton) {
			doEdit();
		} else if (e.getSource() == deleteButton) {
			doDelete();
		} else if (e.getSource() == addMenuItem) {
			doAddRows();
		} else if (e.getSource() == editMenuItem) {
			doEditRows();
		} else if (e.getSource() == deleteMenuItem) {
			doDeleteRows();
		} else if (e.getSource() == saveChangesButton) {
			doSaveRows();
		} else if (e.getSource() == upMenuItem) {
			doMoveRows(true);
		} else if (e.getSource() == downMenuItem) {
			doMoveRows(false);
		}
	}

	private void doMoveRows(boolean up) {
		ReportRow row = getSelectedRow();
		ReportRow parent = (ReportRow) row.getParent();

		int idx = parent.getIndex(row);
		parent.remove(row);

		if (up) {
			if (idx > 0)
				parent.insert(row, idx - 1);
		} else {
			if (idx < parent.getChildCount() - 1)
				parent.insert(row, idx + 1);
		}

		setDesignRowsTreeModel(row);
	}

	protected abstract void doAdd();

	protected abstract void doEdit();

	protected abstract void doDelete();

	private void setSelectedRow(ReportRow selectedRow) {
		this.selectedRow = selectedRow;
	}

	protected Design getSelectedDesign() {
		return selectedDesign;
	}

	protected void setSelectedDesign(Design selectedDesign) {
		this.selectedDesign = selectedDesign;
	}

	protected ReportRow getSelectedRow() {
		return selectedRow;
	}

	protected void doDesignTreeSelected(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if (path != null) {
			Design design = (Design) path.getLastPathComponent();

			setSelectedDesign(design);

			if (this.selectedDesign != null) {

				DefaultTreeModel model = (DefaultTreeModel) designTree
						.getModel();
				Design root = (Design) model.getRoot();

				ReportRow rootRow = null;
				if (this.selectedDesign != root) {
					saveChangesButton.setEnabled(true);
					rootRow = design.getRootRow();
				} else {
					saveChangesButton.setEnabled(false);
					rootRow = rootReportRow;
				}

				DefaultTreeModel modelRow = new DefaultTreeModel(rootRow);
				designRowsTree.setModel(modelRow);

			}
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (e.getSource() == designTree) {
			doDesignTreeSelected(e);
		}
	}

	protected void updatePositiveBalance(Design design) {
		ReportRow root = design.getRootRow();
		ArrayList list = new ArrayList();
		list.add(root);

		ArrayList allReportRowList = ReportRow.createStandardReportList(list);
		Iterator iterator = allReportRowList.iterator();
		while (iterator.hasNext()) {
			ReportRow row = (ReportRow) iterator.next();
			row.setNormalDebit(design.getPositiveBalance() == 0);
		}
	}

	protected void updateEntity(Design design) {
		try {
			design = logic.updateDesign(design);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void insertEntity(Design design) {
		try {
			design = logic.insertDesign(design);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void deleteDesign(Design design) {
		try {
			logic.deleteDesign(design);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setDesignRowsTreeModel(ReportRow row) {
		DefaultTreeModel model = (DefaultTreeModel) designRowsTree.getModel();
		ReportRow root = (ReportRow) model.getRoot();

		designRowsTree.setModel(new DefaultTreeModel(root));

		model = (DefaultTreeModel) designRowsTree.getModel();
		TreeNode[] nodes = model.getPathToRoot(row);
		TreePath path = new TreePath(nodes);
		designRowsTree.setExpandsSelectedPaths(true);
		designRowsTree.expandPath(path);
	}

	protected void doAddRows() {
		ReportRow row = getSelectedRow();
		RowDesignDlg dlg = new RowDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, row, title, getSelectedDesign()
						.getPositiveBalance() == 0);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			ReportRow node = (ReportRow) dlg.getReportRow();

			row.add(node);

			setDesignRowsTreeModel(row);
		}
	}

	protected void doEditRows() {
		ReportRow row = getSelectedRow();
		RowDesignDlg dlg = new RowDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, (ReportRow) row.getParent(), title,
				getSelectedDesign().getPositiveBalance() == 0);
		dlg.setReportRow(row);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			ReportRow node = (ReportRow) dlg.getReportRow();

			setDesignRowsTreeModel((ReportRow) node.getParent());
		}
	}

	protected void doDeleteRows() {
		if (getSelectedRow() == null) {
			JOptionPane.showMessageDialog(this, "Please select the row");
			return;
		}
		ReportRow row = getSelectedRow();
		ReportRow root = (ReportRow) designRowsTree.getModel().getRoot();
		if (row == root)
			return;

		int response = JOptionPane.showConfirmDialog(this,
				"Are you sure to delete this row", "Delete Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {

			ReportRow parent = (ReportRow) row.getParent();
			parent.remove(row);

			if ((parent.isLeaf()) && (parent != root))
				parent = (ReportRow) parent.getParent();

			setDesignRowsTreeModel(parent);
		}
	}

	protected void doSaveRows() {
		Design design = getSelectedDesign();

		ReportRow root = (ReportRow) designRowsTree.getModel().getRoot();

		design.setRootRow(root);

		try {
			logic.updateRows(design);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(this, "Successfully updated");
	}

	class TreeMouseAdapter extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				DefaultTreeModel model = (DefaultTreeModel) designRowsTree
						.getModel();
				TreePath path = designRowsTree.getSelectionPath();
				if (path != null) {
					ReportRow node = (ReportRow) path.getLastPathComponent();
					setSelectedRow(node);

					DefaultTreeModel modelDesign = (DefaultTreeModel) designTree
							.getModel();
					Design rootDesign = (Design) modelDesign.getRoot();

					if (selectedDesign == null) {
						addMenuItem.setEnabled(false);
						editMenuItem.setEnabled(false);
						deleteMenuItem.setEnabled(false);
						upMenuItem.setEnabled(false);
						downMenuItem.setEnabled(false);
					} else {
						if (selectedDesign.equals(rootDesign)) {
							addMenuItem.setEnabled(false);
							editMenuItem.setEnabled(false);
							deleteMenuItem.setEnabled(false);
							upMenuItem.setEnabled(false);
							downMenuItem.setEnabled(false);
						} else {
							if (node == model.getRoot()) {
								addMenuItem.setEnabled(true);
								editMenuItem.setEnabled(false);
								deleteMenuItem.setEnabled(false);
								upMenuItem.setEnabled(false);
								downMenuItem.setEnabled(false);
							} else {
								if (node.getAllowsChildren()) {
									addMenuItem.setEnabled(true);
									editMenuItem.setEnabled(true);
									deleteMenuItem.setEnabled(true);
								} else {
									addMenuItem.setEnabled(false);
									editMenuItem.setEnabled(true);
									deleteMenuItem.setEnabled(true);
								}

								ReportRow parent = (ReportRow) node.getParent();
								int idx = parent.getIndex(node);
								if (idx > 0) {
									upMenuItem.setEnabled(true);
								} else {
									upMenuItem.setEnabled(false);
								}
								if (idx < parent.getChildCount() - 1) {
									downMenuItem.setEnabled(true);
								} else {
									downMenuItem.setEnabled(false);
								}
							}
						}
					}
					Rectangle rectangle = designRowsTree.getPathBounds(path);
					if (rectangle.contains(e.getPoint()))
						popupMenu.show(designRowsTree, e.getX(), e.getY());
				}
			}
		}
	}

}
