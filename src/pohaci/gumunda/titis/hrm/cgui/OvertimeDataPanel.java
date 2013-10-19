package pohaci.gumunda.titis.hrm.cgui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JYearChooser;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class OvertimeDataPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel periodeEmployeePanel = null;
	private JLabel periodLabel = null;
	private JMonthChooser monthChooser = null;
	private JYearChooser yearChooser = null;
	private JLabel employeeLabel = null;
	private EmployeePicker employeePicker = null;
	private JPanel northPanel = null;
	private JPanel buttonPanel = null;
	private JButton editButton = null;
	private JButton saveButton = null;
	private JButton cancelButton = null;
	private JPanel centerPanel = null;
	private JScrollPane overtimeScrollPane = null;
	private JTable overtimeTable = null;
	private Connection conn;
	private long sessionId;
	/*private String[] month = new String[] { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };*/
	private OvertimeTableModel overtimeTableModel;
	private boolean isEditing;
	private boolean isNew = false;


	/**
	 * This is the default constructor
	 */
	public OvertimeDataPanel() {
		super();
		initialize();
	}

	public OvertimeDataPanel(Connection conn, long sessionId) {
		super();
		this.conn = conn;
		this.sessionId = sessionId;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(5);
		this.setLayout(borderLayout);
		// this.setSize(532, 200);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);

		setEdting(false);
	}

	/**
	 * This method initializes periodeEmployeePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPeriodeEmployeePanel() {
		if (periodeEmployeePanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(5, 0, 0, 5);
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 1;
			employeeLabel = new JLabel();
			employeeLabel.setText("Employee");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints2.gridx = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(5, 0, 0, 5);
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.gridy = 0;
			periodLabel = new JLabel();
			periodLabel.setText("Period");
			periodeEmployeePanel = new JPanel();
			periodeEmployeePanel.setLayout(new GridBagLayout());
			periodeEmployeePanel.setPreferredSize(new Dimension(300, 75));
			periodeEmployeePanel.add(periodLabel, gridBagConstraints);
			periodeEmployeePanel.add(getMonthChooser(), gridBagConstraints1);
			periodeEmployeePanel.add(getYearChooser(), gridBagConstraints2);
			periodeEmployeePanel.add(employeeLabel, gridBagConstraints3);
			periodeEmployeePanel.add(getEmployeePicker(), gridBagConstraints4);
		}
		return periodeEmployeePanel;
	}

	/**
	 * This method initializes monthComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JMonthChooser getMonthChooser() {
		if (monthChooser == null) {
			monthChooser = new JMonthChooser(JMonthChooser.NO_SPINNER);
			monthChooser.setLocale(Locale.ENGLISH); // dipaksa english
			monthChooser.addPropertyChangeListener("month",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(
								java.beans.PropertyChangeEvent e) {
							if (e.getPropertyName().equals("month"))
								onDataChanged();
						}
					});
		}
		return monthChooser;
	}

	private void onDataChanged() {
		if (getEmployeePicker().getEmployee() != null) {
			// logicnya aku taruh disini :(

			int month = getMonthChooser().getMonth();
			int year = getYearChooser().getYear();
			Employee employee = getEmployeePicker().getEmployee();
			long employeeId = employee.getIndex();

			GenericMapper map = MasterMap.obtainMapperFor(Overtime.class);
			map.setActiveConn(this.conn);
			String whereClause = "periodMonth=" + month + " and periodYear="
					+ year + " and employee=" + employeeId;
			List overtimes = map.doSelectWhere(whereClause);


			if (overtimes.size() > 0) {
				isNew = false;

				Collections.sort(overtimes, new Comparator() {

					public int compare(Object arg0, Object arg1) {
						if ((arg0 instanceof Overtime)
								&& (arg1 instanceof Overtime)) {
							Overtime ovt0 = (Overtime) arg0;
							Overtime ovt1 = (Overtime) arg1;

							int firstCriteria = (ovt0.getMultiplier().getType() - ovt1
									.getMultiplier().getType());
							if (firstCriteria == 0) {
								return (ovt0.getMultiplier().getHourMin() - ovt1
										.getMultiplier().getHourMin());
							} else
								return firstCriteria;
						}
						return -1;
					}

				});

				Iterator iterator = overtimes.iterator();
				getOvertimeTableModel().clear();
				while (iterator.hasNext()) {
					Overtime overtime = (Overtime) iterator.next();

					getOvertimeTableModel().addRow(overtime);
				}

				getOvertimeTable().setModel(getOvertimeTableModel());
			} else {
				isNew = true;

				map = MasterMap.obtainMapperFor(OvertimeMultiplier.class);
				map.setActiveConn(this.conn);
				List mutlipliers = map
						.doSelectWhere("1=1 order by type, hourmin");

				Iterator iterator = mutlipliers.iterator();
				getOvertimeTableModel().clear();
				while (iterator.hasNext()) {
					OvertimeMultiplier multiplier = (OvertimeMultiplier) iterator
							.next();

					Overtime overtime = new Overtime();
					overtime.setMonth(month);
					overtime.setYear(year);
					overtime.setMultiplier(multiplier);
					overtime.setEmployee(employee);
					overtime.setOvertime(0);

					getOvertimeTableModel().addRow(overtime);
				}

				getOvertimeTable().setModel(getOvertimeTableModel());
			}
		} else {
			getOvertimeTableModel().clear();
			getOvertimeTable().setModel(getOvertimeTableModel());
		}
	}

	/**
	 * This method initializes yearTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JYearChooser getYearChooser() {
		if (yearChooser == null) {
			yearChooser = new JYearChooser();
			yearChooser.addPropertyChangeListener("year",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(
								java.beans.PropertyChangeEvent e) {
							if (e.getPropertyName().equals("year"))
								onDataChanged();
						}
					});
		}
		return yearChooser;
	}

	/**
	 * This method initializes employeeTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private EmployeePicker getEmployeePicker() {
		if (employeePicker == null) {
			employeePicker = new EmployeePicker(this.conn, this.sessionId);
			employeePicker.addPropertyChangeListener("employee",
					new java.beans.PropertyChangeListener() {

						public void propertyChange(PropertyChangeEvent evt) {
							if (evt.getPropertyName().equals("employee"))
								onDataChanged();
						}

					});
		}
		return employeePicker;
	}

	/**
	 * This method initializes northPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(5);
			northPanel = new JPanel();
			northPanel.setLayout(borderLayout1);
			northPanel.add(getPeriodeEmployeePanel(), BorderLayout.WEST);
			northPanel.add(getButtonPanel(), BorderLayout.EAST);
		}
		return northPanel;
	}

	/**
	 * This method initializes buttonPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.RIGHT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout1);
			buttonPanel.add(getEditButton(), null);
			buttonPanel.add(getSaveButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes editButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getEditButton() {
		if (editButton == null) {
			editButton = new JButton();
			editButton.setText("Edit");
			editButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onEditSelected();
				}
			});
		}
		return editButton;
	}

	private void setEdting(boolean b) {
		getEditButton().setEnabled(!b);
		getSaveButton().setEnabled(b);
		getCancelButton().setEnabled(b);

		getMonthChooser().setEnabled(!b);
		getYearChooser().setEnabled(!b);
		getEmployeePicker().setEditable(!b);

		isEditing = b;


	}

	/**
	 * This method initializes saveButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doSave();
				}
			});
		}
		return saveButton;
	}

	private void doSave() {
		stopCellEditing();

		Vector vector = getOvertimeTableModel().getVector();
		List overtimes = new ArrayList(vector);

		GenericMapper map = MasterMap.obtainMapperFor(Overtime.class);
		map.setActiveConn(this.conn);

		Iterator iterator = overtimes.iterator();
		while (iterator.hasNext()) {
			Overtime overtime = (Overtime) iterator.next();

			if (isNew) {
				map.doInsert(overtime);
			} else {
				map.doUpdate(overtime);
			}
		}

		onDataChanged();

		if (isNew)
			isNew = false;
		setEdting(false);
	}

	private void stopCellEditing() {
		TableCellEditor editor = getOvertimeTable().getCellEditor();
		if (editor != null) {
			boolean val = editor.stopCellEditing();
			System.err.println("stopCellEditing: " + val);
		}
	}

	/**
	 * This method initializes cancelButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doCancel();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes centerPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(getOvertimeScrollPane(), BorderLayout.CENTER);
		}
		return centerPanel;
	}

	/**
	 * This method initializes overtimeScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getOvertimeScrollPane() {
		if (overtimeScrollPane == null) {
			overtimeScrollPane = new JScrollPane();
			overtimeScrollPane.setViewportView(getOvertimeTable());
		}
		return overtimeScrollPane;
	}

	/**
	 * This method initializes overtimeTable
	 *
	 * @return javax.swing.JTable
	 */
	private JTable getOvertimeTable() {
		if (overtimeTable == null) {
			overtimeTable = new JTable();
			overtimeTable.setModel(getOvertimeTableModel());
		}
		return overtimeTable;
	}

	private OvertimeTableModel getOvertimeTableModel() {
		if (overtimeTableModel == null) {
			overtimeTableModel = new OvertimeTableModel();
		}
		return overtimeTableModel;
	}

	/**
	 *
	 */
	private void onEditSelected() {
		if (getEmployeePicker().getEmployee() == null) {
			JOptionPane
					.showMessageDialog(this, "Please select employee first.");
		} else {
			setEdting(true);
		}
	}

	/**
	 *
	 */
	private void doCancel() {
		stopCellEditing();

		onDataChanged();
		setEdting(false);
	}

	private class OvertimeTableModel extends AbstractTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 7862139514927167654L;
		private Vector values;

		/**
		 *
		 */
		public OvertimeTableModel() {
			super();
			values = new Vector();
		}

		public void clear() {
			int old = getRowCount();

			values = new Vector();

			if (old == getRowCount())
				return;
			fireTableRowsDeleted(0, old - 1);

		}

		public Class getColumnClass(int columnIndex) {
			if (columnIndex == 0)
				return String.class;
			else if (columnIndex == 1)
				return Integer.class;
			else if (columnIndex == 2)
				return Integer.class;
			else if (columnIndex == 3)
				return Float.class;

			return Object.class;
		}

		public int getColumnCount() {
			return 4;
		}

		public String getColumnName(int columnIndex) {
			if (columnIndex == 0)
				return "Day";
			else if (columnIndex == 1)
				return "Hours Min";
			else if (columnIndex == 2)
				return "Hours Max";
			else if (columnIndex == 3)
				return "Overtime";

			return "";
		}

		public int getRowCount() {
			return values.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Overtime overtime = (Overtime) values.elementAt(rowIndex);

			if (columnIndex == 0)
				return overtime.getMultiplier().getTypeAsString();
			else if (columnIndex == 1)
				return new Integer(overtime.getMultiplier().getHourMin());
			else if (columnIndex == 2)
				return new Integer(overtime.getMultiplier().getHourMax());
			else if (columnIndex == 3)
				return new Float(overtime.getOvertime());

			return null;
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (isEditing) {
				if (columnIndex == 3)
					return true;
			}
			return false;
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 3) {
				if (rowIndex < values.size()) {
					Overtime overtime = (Overtime) values.elementAt(rowIndex);

					if (value instanceof Float) {
						Float floatValue = (Float) value;
						overtime.setOvertime(floatValue.floatValue());
					}
				}
			}
		}

		public void addRow(Overtime value) {
			insertRow(getRowCount(), value);
		}

		private void insertRow(int rowIndex, Overtime value) {
			values.insertElementAt(value, rowIndex);
			justifyRow(rowIndex, rowIndex + 1);
			fireTableRowsInserted(rowIndex, rowIndex);
		}

		private void justifyRow(int from, int to) {
			values.setSize(getRowCount());

			for (int i = from; i < to; i++) {
				if (values.elementAt(i) == null) {
					values.setElementAt(new Vector(), i);
				}
			}
		}

		public Vector getVector() {
			return values;
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
