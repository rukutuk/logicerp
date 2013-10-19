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
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import pohaci.gumunda.cgui.IntegerCellEditor;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.payrollcategory.PayrollCategoryBackupDialog;
import pohaci.gumunda.titis.hrm.cgui.payrollcategory.PayrollCategoryRestoreDialog;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class PayrollCategoryPanel extends JPanel implements ActionListener,
		TreeSelectionListener {
	private static final long serialVersionUID = 8578082282842501843L;
	Connection m_conn;
	public long m_sessionid = -1;

	PayrollCategoryTree m_tree;
	PayrollCategoryTable m_table;
	JButton m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt, m_backupBt,
			m_restoreBt;

	JPopupMenu m_popupMenu = new JPopupMenu();
	JMenuItem mi_addcategory = new JMenuItem("Add Category");
	JMenuItem mi_editcategory = new JMenuItem("Edit Category");
	JMenuItem mi_removecategory = new JMenuItem("Remove Category");

	JMenuItem mi_addemployee = new JMenuItem("Add Employee");
	JMenuItem mi_removeemployee = new JMenuItem("Remove Employee");

	boolean m_new = false, m_edit = false;
	int m_editedIndex = -1;
	PayrollCategoryComponent m_component = null;
	PayrollCategory m_category = null;
	public HRMBusinessLogic logic;
	Employee m_employee = null;
	int mode = 0; // 0 if null, 1 if category, 2 if employee

	public PayrollCategoryPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		logic = new HRMBusinessLogic(m_conn);
		constructComponent();
	}

	void constructComponent() {
		m_tree = new PayrollCategoryTree(m_conn, m_sessionid);
		m_tree.addMouseListener(new TreeMouseAdapter());
		m_tree.addTreeSelectionListener(this);
		m_table = new PayrollCategoryTable();

		m_popupMenu.add(mi_addcategory);
		mi_addcategory.addActionListener(this);
		m_popupMenu.add(mi_editcategory);
		mi_editcategory.addActionListener(this);
		m_popupMenu.add(mi_removecategory);
		mi_removecategory.addActionListener(this);
		m_popupMenu.addSeparator();
		m_popupMenu.add(mi_addemployee);
		mi_addemployee.addActionListener(this);
		m_popupMenu.add(mi_removeemployee);
		mi_removeemployee.addActionListener(this);

		m_addBt = new JButton("Add");
		m_addBt.addActionListener(this);
		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		m_deleteBt = new JButton("Delete");
		m_deleteBt.addActionListener(this);
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		m_backupBt = new JButton("Backup"); // tambahan
		m_backupBt.addActionListener(this);
		m_restoreBt = new JButton("Restore"); // tambahan
		m_restoreBt.addActionListener(this);

		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_deleteBt.setEnabled(false);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_backupBt.setEnabled(true);
		m_restoreBt.setEnabled(true);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(m_addBt);
		buttonPanel.add(m_editBt);
		buttonPanel.add(m_deleteBt);
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);
		buttonPanel.add(m_backupBt);
		buttonPanel.add(m_restoreBt);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);

		splitPane.setLeftComponent(new JScrollPane(m_tree));
		splitPane.setRightComponent(centerPanel);
		splitPane.setDividerLocation(200);

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		add(splitPane, BorderLayout.CENTER);
	}

	void reloadPayrollCategory(long index) {
		try {
			//HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	void addCategory() {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		//TreePath path = m_tree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();

		PayrollCategoryEditorDlg dlg = new PayrollCategoryEditorDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn,
				m_sessionid);
		dlg.setVisible(true);

		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(dlg
					.getPayrollCategory());
			model.insertNodeInto(child, node, node.getChildCount());
			m_tree
					.scrollPathToVisible(new TreePath(model
							.getPathToRoot(child)));
		}
	}

	void editCategory() {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		TreePath path = m_tree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		PayrollCategory category = (PayrollCategory) node.getUserObject();
		PayrollCategoryEditorDlg dlg = new PayrollCategoryEditorDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn,
				m_sessionid, category);
		dlg.setVisible(true);

		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			node.setUserObject(dlg.getPayrollCategory());
			model.nodeChanged(node);
		}
	}

	void removeCategory() {
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

	void addEmployee() {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		TreePath path = m_tree.getSelectionPath();
		if (path == null)
			return;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		SearchEmployeeDetailDlg dlg = new SearchEmployeeDetailDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn,
				m_sessionid);
		dlg.setVisible(true);

		// create relation to payroll category
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			Employee[] employee = dlg.getEmployee();
			if (employee.length > 0) {

				try {
					HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

					// check employee whether he/she has been assign
					// to selected payroll category or not
					boolean notAssigned = (logic
							.countPayrollCategoryEmployeeByEmployee(
									m_sessionid,
									IDBConstants.MODUL_MASTER_DATA, employee[0]
											.getIndex()) == 0);

					if (notAssigned) {
						// assign employee for selected payroll category
						logic.createPayrollCategoryEmployee(m_sessionid,
								IDBConstants.MODUL_MASTER_DATA,
								((PayrollCategory) node.getUserObject())
										.getIndex(), employee[0].getIndex());

						// copy category's components into employee
						PayrollCategoryComponent[] components = logic
								.getAllPayrollCategoryComponent(
										m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										((PayrollCategory) node.getUserObject())
												.getIndex(),
										IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT);

						// save components into created employee
						if (components != null) {
							for (int i = 0; i < components.length; i++) {
								// save
								logic.createEmployeePayrollComponent(
										m_sessionid,
										IDBConstants.MODUL_MASTER_DATA,
										employee[0].getIndex(), components[i]);
								// set
								//m_table.setPayrollCategoryComponent(components);
							}
						}

						// add into tree
						DefaultMutableTreeNode child = new DefaultMutableTreeNode(
								employee[0]);
						model.insertNodeInto(child, node, node.getChildCount());
						m_tree.scrollPathToVisible(new TreePath(model
								.getPathToRoot(child)));
					} else {
						JOptionPane.showMessageDialog(this,
								"Employee has already added into one category",
								"Warning", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(),
							"Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

			}
		}
	}

	void removeEmployee() {
		TreePath path = m_tree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (!Misc.getConfirmation())
				return;

			try {
				deleteNodeEmployee(node, (DefaultMutableTreeNode) node
						.getParent());
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
			deleteNodeEmployee(node, parent);
		}
		deleteNodeCategory(parent);
	}

	void deleteNodeCategory(DefaultMutableTreeNode node) throws Exception {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		logic.deletePayrollCategory(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, ((PayrollCategory) node
						.getUserObject()).getIndex());
		model.removeNodeFromParent(node);
	}

	void deleteNodeEmployee(DefaultMutableTreeNode node,
			DefaultMutableTreeNode parent) throws Exception {
		DefaultTreeModel model = (DefaultTreeModel) m_tree.getModel();
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		logic.deletePayrollCategoryEmployee(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, ((PayrollCategory) parent
						.getUserObject()).getIndex(), ((Employee) node
						.getUserObject()).getIndex());

		logic.deleteEmployeePayrollComponentByEmployee(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, ((Employee) node
						.getUserObject()).getIndex());

		model.removeNodeFromParent(node);
	}

	void addComponent() {
		m_new = true;

		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		model.addRow(new Object[] {});
		m_editedIndex = model.getRowCount() - 1;
		m_table.getSelectionModel().setSelectionInterval(m_editedIndex,
				m_editedIndex);

		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_deleteBt.setEnabled(false);
	}

	void editComponent() {
		m_edit = true;

		m_editedIndex = m_table.getSelectedRow();
		m_component = (PayrollCategoryComponent) m_table.getValueAt(
				m_editedIndex, 0);

		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_deleteBt.setEnabled(false);
	}

	void deleteComponent() {
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		m_editedIndex = m_table.getSelectedRow();
		PayrollCategoryComponent component = (PayrollCategoryComponent) model
				.getValueAt(m_editedIndex, 0);
		if (!Misc.getConfirmation())
			return;

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			long index = component.getIndex();
			if (mode == 1) {
				logic.deletePayrollCategoryComponent(m_sessionid,
						IDBConstants.MODUL_MASTER_DATA, index);
			} else if (mode == 2) {
				logic.deleteEmployeePayrollComponent(m_sessionid,
						IDBConstants.MODUL_MASTER_DATA, index);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		model.removeRow(m_editedIndex);
	}

	void saveComponent() {
		java.util.ArrayList list = new java.util.ArrayList();
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		PayrollComponent component = null;
		FormulaEntity formulaEntity = null;

		Object obj = model.getValueAt(m_editedIndex, 0);
		if (obj instanceof PayrollCategoryComponent)
			component = ((PayrollCategoryComponent) obj).getPayrollComponent();
		else if (obj instanceof PayrollComponent)
			component = (PayrollComponent) obj;
		else
			list.add("Payroll Component");

		obj = model.getValueAt(m_editedIndex, 3);
		if (obj instanceof FormulaEntity)
			formulaEntity = (FormulaEntity) obj;
		else
			list.add("Formula");

		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if (exception.length > 0) {
			for (int i = 0; i < exception.length; i++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc, "Infomation",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			if (m_new) {
				if (mode == 1) {
					PayrollCategoryComponent comp = logic
							.createPayrollCategoryComponent(m_sessionid,
									IDBConstants.MODUL_MASTER_DATA, m_category
											.getIndex(),
									new PayrollCategoryComponent(component,
											formulaEntity));
					m_table.updatePayrollCategoryComponent(comp, m_editedIndex);
				} else if (mode == 2) {
					PayrollCategoryComponent comp = logic
							.createEmployeePayrollComponent(m_sessionid,
									IDBConstants.MODUL_MASTER_DATA, m_employee
											.getIndex(),
									new PayrollCategoryComponent(component,
											formulaEntity));
					m_table.updatePayrollCategoryComponent(comp, m_editedIndex);
				}
			} else {
				if (mode == 1) {
					PayrollCategoryComponent comp = logic
							.updatePayrollCategoryComponent(m_sessionid,
									IDBConstants.MODUL_MASTER_DATA, m_component
											.getIndex(),
									new PayrollCategoryComponent(component,
											formulaEntity));
					m_table.updatePayrollCategoryComponent(comp, m_editedIndex);
				} else if (mode == 2) {
					PayrollCategoryComponent comp = logic
							.updateEmployeePayrollComponent(m_sessionid,
									IDBConstants.MODUL_MASTER_DATA, m_component
											.getIndex(),
									new PayrollCategoryComponent(component,
											formulaEntity));
					m_table.updatePayrollCategoryComponent(comp, m_editedIndex);
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		m_new = false;
		m_edit = false;
		m_addBt.setEnabled(true);
		m_editBt.setEnabled(true);
		m_deleteBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
	}

	void cancelComponent() {
		m_table.stopCellEditing();

		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		if (m_new) {
			m_new = false;
			model.removeRow(m_editedIndex);
		} else if (m_edit) {
			m_edit = false;
			m_table.updatePayrollCategoryComponent(m_component, m_editedIndex);
			m_table.getSelectionModel().setSelectionInterval(m_editedIndex,
					m_editedIndex);
			m_addBt.setEnabled(true);
			m_editBt.setEnabled(true);
			m_saveBt.setEnabled(false);
			m_cancelBt.setEnabled(false);
			m_deleteBt.setEnabled(true);
		}
	}

	void setSelectedObject(Object obj) {
		if (obj instanceof PayrollCategory) {
			m_category = (PayrollCategory) obj;
			mode = 1;
			setPayrollCategoryComponent(m_category.getIndex(),
					IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT);

			m_new = false;
			m_edit = false;

			m_addBt.setEnabled(true);
			m_editBt.setEnabled(false);
			m_deleteBt.setEnabled(false);
			m_saveBt.setEnabled(false);
			m_cancelBt.setEnabled(false);
		} else if (obj instanceof Employee) {
			m_employee = (Employee) obj;
			mode = 2;
			setPayrollCategoryComponent(m_employee.getIndex(),
					IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT);

			m_new = false;
			m_edit = false;

			m_addBt.setEnabled(true);
			m_editBt.setEnabled(false);
			m_deleteBt.setEnabled(false);
			m_saveBt.setEnabled(false);
			m_cancelBt.setEnabled(false);
		} else
			objectViewNoSelection();

	}

	void objectViewNoSelection() {
		m_table.clear();
		m_new = false;
		m_edit = false;

		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_deleteBt.setEnabled(false);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
	}

	void setPayrollCategoryComponent(long index, String table) {
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			PayrollCategoryComponent[] component = logic
					.getAllPayrollCategoryComponent(m_sessionid,
							IDBConstants.MODUL_MASTER_DATA, index, table);
			/*parsing logic moved into HRMBusinessLogic
			  for(int i = 0; i < component.length; i ++) {
			  component[i] = new PayrollCategoryComponent(component[i].getIndex(), component[i].getPayrollComponent(),
			  parseInput(component[i].getFormula().getFormulaCode()));
			}*/

			m_table.setPayrollCategoryComponent(component);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			ex.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mi_addcategory) {
			addCategory();
		} else if (e.getSource() == mi_editcategory) {
			editCategory();
		} else if (e.getSource() == mi_removecategory) {
			removeCategory();
		} else if (e.getSource() == mi_addemployee) {
			addEmployee();
		} else if (e.getSource() == mi_removeemployee) {
			removeEmployee();
		} else if (e.getSource() == m_addBt) {
			addComponent();
		} else if (e.getSource() == m_editBt) {
			editComponent();
		} else if (e.getSource() == m_deleteBt) {
			deleteComponent();
		} else if (e.getSource() == m_saveBt) {
			saveComponent();
		} else if (e.getSource() == m_cancelBt) {
			cancelComponent();
		} else if (e.getSource() == m_backupBt) {
			backup();
		} else if (e.getSource() == m_restoreBt) {
			restore();
		}
	}

	private void restore() {
		PayrollCategoryRestoreDialog dlg = new PayrollCategoryRestoreDialog(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn,
				m_sessionid);
		dlg.setVisible(true);
	}

	private void backup() {
		PayrollCategoryBackupDialog dlg = new PayrollCategoryBackupDialog(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), m_conn,
				m_sessionid);
		dlg.setVisible(true);

	}

	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if (path != null) {
			setSelectedObject(((DefaultMutableTreeNode) path
					.getLastPathComponent()).getUserObject());
		} else
			objectViewNoSelection();
	}

	/**
	 *
	 */
	class PayrollCategoryTable extends JTable {
		private static final long serialVersionUID = -2893540607033744953L;
		PayrollCategoryTableModel m_model;

		public PayrollCategoryTable() {
			PayrollCategoryTableModel model = new PayrollCategoryTableModel();
			setModel(model);
			m_model = model;
			getSelectionModel().addListSelectionListener(model);
		}

		public TableCellEditor getCellEditor(int row, int col) {
			if (col == 0)
				return new PayrollComponentCellEditor(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						m_conn, m_sessionid);
			else if (col == 3)
				return new PayrollCategoryFormulaCellEditor(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						m_conn, m_sessionid);
			else if (col == 6)
				return new IntegerCellEditor();
			return super.getCellEditor(row, col);
		}

		void clear() {
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
		}

		void updatePayrollCategoryComponent(PayrollCategoryComponent component,
				int row) {
			PayrollCategoryTableModel model = m_model;
			// (PayrollCategoryTableModel)m_table.getModel();
			model.removeRow(row);
			model.insertRow(row, model.toRow(component));

			this.getSelectionModel().setSelectionInterval(row, row);
		}

		void setPayrollCategoryComponent(PayrollCategoryComponent[] component) {
			PayrollCategoryTableModel model = m_model;
			// (PayrollCategoryTableModel)getModel();
			model.setRowCount(0);
			for (int i = 0; i < component.length; i++) {
				model.addRow(model.toRow(component[i]));
			}
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if ((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
	}

	/**
	 *
	 */
	class PayrollCategoryTableModel extends DefaultTableModel implements
			ListSelectionListener {
		private static final long serialVersionUID = -7762197291353857063L;

		PayrollCategoryTableModel() {
			super();
			addColumn("Payroll Component");
			addColumn("Type");
			addColumn("Account");
			addColumn("Formula");
			addColumn("Month");
			addColumn("Rounding Mode");
			addColumn("Precision");
		}

		public boolean isCellEditable(int row, int col) {
			if (col == 1 || col == 2 || col == 4 || col == 5 || col == 6)
				return false;
			if ((m_new || m_edit) && row == m_editedIndex)
				return true;
			return false;
		}

		public void setValueAt(Object obj, int row, int col) {
			if (obj instanceof PayrollComponent)
				setPayrollComponent(obj, row);
			else if (obj instanceof FormulaEntity) {
				// super.setValueAt(obj, row, col);
				setFormula((FormulaEntity) obj, row);
				// super.setValueAt(((Formula)obj).getEveryWhichMonth(), row,
				// col++);
			} else
				super.setValueAt(obj, row, col);
		}

		private void setFormula(FormulaEntity formulEnt, int row) {
			super.setValueAt(formulEnt, row, 3);
			short month = formulEnt.getEveryWhichMonth();
			super.setValueAt(getWhichMonth(month), row, 4);
			super.setValueAt(formulEnt.getNumberRounding()
					.getRoundingModeAsString(), row, 5);
			super.setValueAt(new Integer(formulEnt.getNumberRounding()
					.getPrecision()), row, 6);
		}

		private String getWhichMonth(short month) {
			switch (month) {
			case 0:
				return "All";
			case 1:
				return "January";
			case 2:
				return "February";
			case 3:
				return "March";
			case 4:
				return "April";
			case 5:
				return "May";
			case 6:
				return "June";
			case 7:
				return "July";
			case 8:
				return "August";
			case 9:
				return "September";
			case 10:
				return "October";
			case 11:
				return "November";
			case 12:
				return "December";
			default:
				return "?";
			}

		}

		Object[] toRow(PayrollCategoryComponent component) {
			Object[] r = new Object[] { component,
					component.getPayrollComponent().getPaymentAsString(),
					component.getPayrollComponent().getAccount(),
					component.getFormulaEntity(),
					getWhichMonth(component.getEveryWhichMonth()),
					component.getNumberRounding().getRoundingModeAsString(),
					new Integer(component.getNumberRounding().getPrecision()) };
			return r;
		}

		void setPayrollComponent(Object object, int rowindex) {
			boolean bduplicate = false;
			int i;// , lastrow;

			PayrollComponent pcomponent = (PayrollComponent) object;
			for (i = 0; i < getRowCount() - 1; i++) {
				if (rowindex != i) {
					PayrollComponent comp = null;
					Object obj = getValueAt(i, 0);
					if (obj instanceof PayrollCategoryComponent)
						comp = ((PayrollCategoryComponent) obj)
								.getPayrollComponent();
					else
						comp = (PayrollComponent) obj;

					if (pcomponent.getCode().equals(comp.getCode())) {
						bduplicate = true;
						break;
					}
				}
			}

			i++;
			if (bduplicate) {
				JOptionPane.showMessageDialog(PayrollCategoryPanel.this,
						pcomponent + " has defined at line " + i);
				return;
			}
			// Object[] newRow = toRow(pcomponent);
			super.setValueAt(new PayrollComponent(pcomponent,
					PayrollComponent.DESCRIPTION), rowindex, 0);
			super.setValueAt(pcomponent.getPaymentAsString(), rowindex, 1);
			super.setValueAt(pcomponent.getAccount(), rowindex, 2);
		}

		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				int iRowIndex = ((ListSelectionModel) e.getSource())
						.getMinSelectionIndex();

				if (m_new || m_edit) {
					if (iRowIndex != m_editedIndex)
						m_table.getSelectionModel().setSelectionInterval(
								m_editedIndex, m_editedIndex);
				} else {
					if (iRowIndex != -1) {
						m_addBt.setEnabled(true);
						m_editBt.setEnabled(true);
						m_saveBt.setEnabled(false);
						m_cancelBt.setEnabled(false);
						m_deleteBt.setEnabled(true);
					} else {
						m_addBt.setEnabled(true);
						m_editBt.setEnabled(false);
						m_saveBt.setEnabled(false);
						m_cancelBt.setEnabled(false);
						m_deleteBt.setEnabled(false);
					}
				}
			}
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
						mi_addcategory.setEnabled(true);
						mi_editcategory.setEnabled(false);
						mi_removecategory.setEnabled(false);

						mi_addemployee.setEnabled(false);
						mi_removeemployee.setEnabled(false);
					} else if (node.getUserObject() instanceof PayrollCategory) {
						mi_addcategory.setEnabled(false);
						mi_editcategory.setEnabled(true);
						mi_removecategory.setEnabled(true);

						mi_addemployee.setEnabled(true);
						mi_removeemployee.setEnabled(false);
					} else {
						mi_addcategory.setEnabled(false);
						mi_editcategory.setEnabled(false);
						mi_removecategory.setEnabled(false);

						mi_addemployee.setEnabled(false);
						mi_removeemployee.setEnabled(true);
					}

					Rectangle rectangle = m_tree.getPathBounds(path);
					if (rectangle.contains(e.getPoint()))
						m_popupMenu.show(m_tree, e.getX(), e.getY());
				}
			}
		}
	}
}