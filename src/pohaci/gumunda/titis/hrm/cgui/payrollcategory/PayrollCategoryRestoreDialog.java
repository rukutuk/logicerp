/**
 *
 */
package pohaci.gumunda.titis.hrm.cgui.payrollcategory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JYearChooser;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

/**
 * @author dark-knight
 *
 */
public class PayrollCategoryRestoreDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel criteriaSelectionPanel = null;
	private JPanel criteriaPanel = null;
	private JLabel yearPeriodLabel = null;
	private JYearChooser yearChooser = null;
	private JLabel monthPeriodLabel = null;
	private JPanel buttonPanel = null;
	private JButton searchButton = null;
	private JButton restoreButton = null;
	private JButton cancelButton = null;
	private JPanel viewPanel = null;
	private JScrollPane viewScrollPane = null;
	private JTable viewTable = null;
	private JMonthChooser monthChooser = null;
	private JTextField filterTextField = null;
	private Connection connection;
	private long sessionId;
	private boolean restore = false;

	/**
	 * @param owner
	 */
	public PayrollCategoryRestoreDialog(Frame owner, Connection connection,
			long sessionId) {
		super(owner);
		this.connection = connection;
		this.sessionId = sessionId;
		initialize();
		doSearch();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("Payroll Category Restore");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
			jContentPane.add(getCriteriaSelectionPanel(), BorderLayout.NORTH);
			jContentPane.add(getViewPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes criteriaSelectionPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getCriteriaSelectionPanel() {
		if (criteriaSelectionPanel == null) {
			criteriaSelectionPanel = new JPanel();
			criteriaSelectionPanel.setLayout(new BorderLayout());
			criteriaSelectionPanel.add(getCriteriaPanel(), BorderLayout.CENTER);
			criteriaSelectionPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return criteriaSelectionPanel;
	}

	/**
	 * This method initializes criteriaPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getCriteriaPanel() {
		if (criteriaPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.NONE;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.anchor = GridBagConstraints.NORTH;
			gridBagConstraints7.gridy = 1;
			monthPeriodLabel = new JLabel();
			monthPeriodLabel.setText("Month");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.gridy = 0;
			yearPeriodLabel = new JLabel();
			yearPeriodLabel.setText("Year");
			criteriaPanel = new JPanel();
			criteriaPanel.setLayout(new GridBagLayout());
			criteriaPanel.add(yearPeriodLabel, gridBagConstraints);
			criteriaPanel.add(getYearChooser(), gridBagConstraints6);
			criteriaPanel.add(monthPeriodLabel, gridBagConstraints7);
			criteriaPanel.add(getMonthChooser(), gridBagConstraints11);
		}
		return criteriaPanel;
	}

	/**
	 * This method initializes yearChooser
	 *
	 * @return javax.swing.JTextField
	 */
	private JYearChooser getYearChooser() {
		if (yearChooser == null) {
			yearChooser = new JYearChooser();
		}
		return yearChooser;
	}

	/**
	 * This method initializes buttonPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout);
			buttonPanel.add(getSearchButton(), null);
			buttonPanel.add(getRestoreButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes searchButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setText("Search");
			searchButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doSearch();
				}
			});
		}
		return searchButton;
	}

	private void doSearch() {
		PayrollCategoryRestoreSQLUtil util = new PayrollCategoryRestoreSQLUtil(
				connection, sessionId);

		try {
			List list = util.getAllHistoryMaster(getMonthChooser().getMonth(),
					getYearChooser().getYear());

			EventList eventList = GlazedLists.eventList(list);

			TextFilterator filterator = new TextFilterator() {

				public void getFilterStrings(List list, Object obj) {
					PayrollCategoryBackupMaster master = (PayrollCategoryBackupMaster) obj;

					list.add(master.getDescription());
				}

			};

			FilterList filterList = new FilterList(eventList,
					new TextComponentMatcherEditor(getFilterTextField(),
							filterator));
			EventTableModel tableModel = new EventTableModel(filterList,
					new PayrollCategoryRestoreTableFormat());

			EventSelectionModel selectionModel = new EventSelectionModel(
					filterList);
			getViewTable().setModel(tableModel);
			getViewTable().setSelectionModel(selectionModel);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to query data");
		}
	}

	/**
	 * This method initializes restoreButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getRestoreButton() {
		if (restoreButton == null) {
			restoreButton = new JButton();
			restoreButton.setText("Restore");
			restoreButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							doRestore();
						}
					});
		}
		return restoreButton;
	}

	private void doRestore() {
		if (getViewTable().getSelectedRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Please select a row first");
			return;
		}

		EventSelectionModel model = (EventSelectionModel) getViewTable()
				.getSelectionModel();
		EventList eventList = model.getSelected();

		PayrollCategoryBackupMaster master = (PayrollCategoryBackupMaster) eventList
				.get(0);

		int response = JOptionPane.showConfirmDialog(this,
				"Are you sure to replace current payroll master with this one: \r\nperiod: "
						+ master.getPeriod() + "\r\ndescription: "
						+ master.getDescription(), "Confirmation",
				JOptionPane.YES_NO_OPTION);

		if (response == JOptionPane.YES_OPTION) {
			PayrollCategoryRestoreSQLUtil util = new PayrollCategoryRestoreSQLUtil(
					connection, sessionId);
			try {
				util.restore(master);
				JOptionPane
						.showMessageDialog(
								this,
								"Successfully restoring payroll master.\r\nPlease reopen Payroll Category to view the results.");

				setRestore(true);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Failed while restoring payroll master");

				setRestore(false);
			}
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

	protected void doCancel() {
		dispose();
	}

	/**
	 * This method initializes viewPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getViewPanel() {
		if (viewPanel == null) {
			viewPanel = new JPanel();
			viewPanel.setLayout(new BorderLayout());
			viewPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			viewPanel.add(getFilterTextField(), BorderLayout.NORTH);
			viewPanel.add(getViewScrollPane(), BorderLayout.CENTER);
		}
		return viewPanel;
	}

	/**
	 * This method initializes viewScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getViewScrollPane() {
		if (viewScrollPane == null) {
			viewScrollPane = new JScrollPane();
			viewScrollPane.setViewportView(getViewTable());
		}
		return viewScrollPane;
	}

	/**
	 * This method initializes viewTable
	 *
	 * @return javax.swing.JTable
	 */
	private JTable getViewTable() {
		if (viewTable == null) {
			viewTable = new JTable();
			viewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return viewTable;
	}

	/**
	 * This method initializes monthChooser
	 *
	 * @return javax.swing.JTextField
	 */
	private JMonthChooser getMonthChooser() {
		if (monthChooser == null) {
			monthChooser = new JMonthChooser(JMonthChooser.NO_SPINNER);
		}
		return monthChooser;
	}

	/**
	 * This method initializes filterTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getFilterTextField() {
		if (filterTextField == null) {
			filterTextField = new JTextField();
		}
		return filterTextField;
	}

	public boolean isRestore() {
		return restore;
	}

	public void setRestore(boolean restore) {
		this.restore = restore;
	}

}
