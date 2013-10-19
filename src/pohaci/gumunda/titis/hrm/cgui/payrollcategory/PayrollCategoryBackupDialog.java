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
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JYearChooser;

/**
 * @author dark-knight
 *
 */
public class PayrollCategoryBackupDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel criteriaPanel = null;
	private JLabel yearPeriodLabel = null;
	private JLabel monthPeriodLabel = null;
	private JYearChooser yearChooser = null;
	private JMonthChooser monthChooser = null;
	private JLabel descriptionLabel = null;
	private JPanel buttonPanel = null;
	private JButton backupButton = null;
	private JButton cancelButton = null;
	private JScrollPane descScrollPane = null;
	private JTextArea descriptionTextArea = null;
	private Connection connection;
	private long sessionId;
	/**
	 * @param owner
	 */
	public PayrollCategoryBackupDialog(Frame owner, Connection connection, long sessionId) {
		super(owner);
		this.connection = connection;
		this.sessionId = sessionId;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("Payroll Category Backup Wizard");
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
			jContentPane.add(getCriteriaPanel(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes criteriaPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getCriteriaPanel() {
		if (criteriaPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.weighty = 1.0;
			gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints21.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.anchor = GridBagConstraints.NORTH;
			gridBagConstraints4.gridy = 2;
			descriptionLabel = new JLabel();
			descriptionLabel.setText("Description");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			monthPeriodLabel = new JLabel();
			monthPeriodLabel.setText("Month");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 0;
			yearPeriodLabel = new JLabel();
			yearPeriodLabel.setText("Year");
			criteriaPanel = new JPanel();
			criteriaPanel.setLayout(new GridBagLayout());
			criteriaPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
			criteriaPanel.add(yearPeriodLabel, gridBagConstraints);
			criteriaPanel.add(monthPeriodLabel, gridBagConstraints1);
			criteriaPanel.add(getYearChooser(), gridBagConstraints2);
			criteriaPanel.add(getMonthChooser(), gridBagConstraints3);
			criteriaPanel.add(descriptionLabel, gridBagConstraints4);
			criteriaPanel.add(getDescScrollPane(), gridBagConstraints21);
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
	 * This method initializes monthChooser
	 *
	 * @return javax.swing.JTextField
	 */
	private JMonthChooser getMonthChooser() {
		if (monthChooser == null) {
			monthChooser = new JMonthChooser(JMonthChooser.NO_SPINNER);
			monthChooser.setLocale(Locale.ENGLISH);
		}
		return monthChooser;
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
			buttonPanel.add(getBackupButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes backupButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBackupButton() {
		if (backupButton == null) {
			backupButton = new JButton();
			backupButton.setText("Backup");
			backupButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onBackup();
				}
			});
		}
		return backupButton;
	}

	protected void onBackup() {
		//backup
		PayrollCategoryBackupSQLUtil util = new PayrollCategoryBackupSQLUtil(connection, sessionId);
		int month = getMonthChooser().getMonth();
		int year = getYearChooser().getYear();
		String description = getDescriptionTextArea().getText();

		try {
			util.backup(month, year, description);

			JOptionPane.showMessageDialog(this, "Backup process has been done.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		dispose();
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
					onCancel();
				}
			});
		}
		return cancelButton;
	}

	protected void onCancel() {
		dispose();
	}

	/**
	 * This method initializes descScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getDescScrollPane() {
		if (descScrollPane == null) {
			descScrollPane = new JScrollPane();
			descScrollPane.setViewportView(getDescriptionTextArea());
		}
		return descScrollPane;
	}

	/**
	 * This method initializes descriptionTextArea
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getDescriptionTextArea() {
		if (descriptionTextArea == null) {
			descriptionTextArea = new JTextArea();
		}
		return descriptionTextArea;
	}

}
