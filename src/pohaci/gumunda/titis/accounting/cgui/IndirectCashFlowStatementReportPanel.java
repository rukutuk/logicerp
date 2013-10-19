/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.cgui.periodchooser.AnnuallyPeriodChooser;
import pohaci.gumunda.titis.accounting.cgui.periodchooser.MonthlyPeriodChooser;
import pohaci.gumunda.titis.accounting.cgui.periodchooser.QuarterlyPeriodChooser;
import pohaci.gumunda.titis.accounting.cgui.periodchooser.SemesterPeriodChooser;
import pohaci.gumunda.titis.accounting.cgui.report.Income_statement;
import pohaci.gumunda.titis.accounting.cgui.report.IndirectCashFlowReport;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IndirectCashFlowStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IndirectCashFlowStatementReportContext;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportContext;
import pohaci.gumunda.titis.accounting.logic.reportdesign.IndirectCashFlowStatementLogic;
import pohaci.gumunda.titis.application.DatePicker;

/**
 * @author dark-knight
 *
 */
public class IndirectCashFlowStatementReportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JSplitPane splitPane = null;
	private JPanel leftPanel = null;
	private JPanel rightPanel = null;
	private JPanel northLeftPanel = null;
	private JPanel reportDesignPanel = null;
	private JLabel reportDesignLabel = null;
	private JComboBox reportDesignComboBox = null;
	private JPanel reportOptionsPanel = null;
	private JCheckBox showNullCheckBox = null;
	private JPanel reportPeriodPanel = null;
	private JLabel startLabel = null;
	private DatePicker startDatePicker = null;
	private JLabel endDateLabel = null;
	private DatePicker endDatePicker = null;
	private JLabel labelLabel = null;
	private JTextField labelTextField = null;
	private JPanel comparativePanel = null;
	private JLabel compStartDateLabel = null;
	private DatePicker compStartDatePicker = null;
	private JLabel compEndDateLabel = null;
	private DatePicker compEndDatePicker = null;
	private JLabel compLabelLabel = null;
	private JTextField compLabelTextField = null;
	private JCheckBox comparativeCheckBox = null;
	private JRViewer report = null;
	private JPanel buttonPanel = null;
	private JButton previewButton = null;
	private Connection connection;
	private long sessionId;
	private JLabel periodTypeLabel = null;
	private JComboBox periodTypeComboBox = null;
	private JLabel periodLabel = null;
	private JPanel periodPanel = null;
	private JLabel compPeriodLabel = null;
	private JPanel compPeriodPanel = null;
	
	/**
	 * This is the default constructor
	 */
	public IndirectCashFlowStatementReportPanel(Connection connection, long sessionId) {
		super();
		this.connection = connection;
		this.sessionId = sessionId;
		initialize();
		setDefault();
		periodTypeSelected();
		initDesignList();
	}

	private void initDesignList() {
		IndirectCashFlowStatementLogic logic = new IndirectCashFlowStatementLogic(connection,
				sessionId);
		List list = logic.getDesignList();
		IndirectCashFlowStatementLogic[] designs = (IndirectCashFlowStatementLogic[]) list
				.toArray(new IndirectCashFlowStatementLogic[list.size()]);

		DefaultComboBoxModel model = new DefaultComboBoxModel(designs);
		getReportDesignComboBox().setModel(model);
	}

	private void setDefault() {
		getShowNullCheckBox().setSelected(true);
		getComparativeCheckBox().setSelected(false);
		setComparativeEnable(false);
	}

	private void setComparativeEnable(boolean enable) {
		getCompPeriodPanel().setEnabled(enable);
		if(periodTypeComboBox.getSelectedItem().equals("Custom"))
			compPeriodEnabling(enable);
		else
			compPeriodEnabling(false&enable);
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(873, 440);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(getSplitPane(), java.awt.BorderLayout.CENTER);
	}

	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(300);
			splitPane.setContinuousLayout(true);
			splitPane.setRightComponent(getRightPanel());
			splitPane.setLeftComponent(getLeftPanel());
		}
		return splitPane;
	}

	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(getNorthLeftPanel(), java.awt.BorderLayout.NORTH);
		}
		return leftPanel;
	}

	private JPanel getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JPanel();
			rightPanel.setLayout(new BorderLayout());
			rightPanel.add(getReport(), java.awt.BorderLayout.CENTER);
		}
		return rightPanel;
	}

	private JPanel getNorthLeftPanel() {
		if (northLeftPanel == null) {
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new java.awt.Insets(3, 0, 0, 0);
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new java.awt.Insets(3, 0, 0, 0);
			gridBagConstraints3.gridy = 3;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.insets = new java.awt.Insets(3, 0, 0, 0);
			gridBagConstraints12.gridy = 2;
			gridBagConstraints12.ipadx = 0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridx = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.insets = new java.awt.Insets(3, 0, 0, 0);
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.ipadx = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.insets = new java.awt.Insets(0, 0, 0, 0);
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.ipadx = 0;
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.gridx = 0;
			northLeftPanel = new JPanel();
			northLeftPanel.setBorder(javax.swing.BorderFactory
					.createEmptyBorder(5, 5, 5, 5));
			northLeftPanel.setLayout(new GridBagLayout());
			northLeftPanel.add(getReportDesignPanel(), gridBagConstraints10);
			northLeftPanel.add(getReportOptionsPanel(), gridBagConstraints11);
			northLeftPanel.add(getReportPeriodPanel(), gridBagConstraints12);
			northLeftPanel.add(getComparativePanel(), gridBagConstraints3);
			northLeftPanel.add(getButtonPanel(), gridBagConstraints20);
		}
		return northLeftPanel;
	}

	private JPanel getReportDesignPanel() {
		if (reportDesignPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 0;
			reportDesignLabel = new JLabel();
			reportDesignLabel.setText("Report Design");
			reportDesignPanel = new JPanel();
			reportDesignPanel.setLayout(new GridBagLayout());
			reportDesignPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Report Design",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12), null));
			reportDesignPanel.add(reportDesignLabel, gridBagConstraints);
			reportDesignPanel.add(getReportDesignComboBox(),
					gridBagConstraints1);
		}
		return reportDesignPanel;
	}

	private JComboBox getReportDesignComboBox() {
		if (reportDesignComboBox == null) {
			reportDesignComboBox = new JComboBox();
		}
		return reportDesignComboBox;
	}

	private JPanel getReportOptionsPanel() {
		if (reportOptionsPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridy = 0;
			reportOptionsPanel = new JPanel();
			reportOptionsPanel.setLayout(new GridBagLayout());
			reportOptionsPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Report Options",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12), null));
			reportOptionsPanel.add(getShowNullCheckBox(), gridBagConstraints2);
		}
		return reportOptionsPanel;
	}

	private JCheckBox getShowNullCheckBox() {
		if (showNullCheckBox == null) {
			showNullCheckBox = new JCheckBox();
			showNullCheckBox.setText("Show Label with Zero Values");
		}
		return showNullCheckBox;
	}

	private JPanel getReportPeriodPanel() {
		if (reportPeriodPanel == null) {
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 2;
			gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints24.insets = new java.awt.Insets(0,5,5,5);
			gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints24.gridy = 1;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints25.gridy = 1;
			gridBagConstraints25.weightx = 0.0;
			gridBagConstraints25.insets = new java.awt.Insets(0, 1, 5, 5);
			gridBagConstraints25.gridx = 2;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints23.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints23.gridy = 1;
			periodLabel = new JLabel();
			periodLabel.setText("Period");
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridy = 0;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints22.gridwidth = 2;
			gridBagConstraints22.gridx = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints21.gridy = 0;
			periodTypeLabel = new JLabel();
			periodTypeLabel.setText("Period Type");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridwidth = 2;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints8.gridy = 4;
			labelLabel = new JLabel();
			labelLabel.setText("Label");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints6.gridy = 3;
			endDateLabel = new JLabel();
			endDateLabel.setText("End Date");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints4.gridy = 2;
			startLabel = new JLabel();
			startLabel.setText("Start Date");
			reportPeriodPanel = new JPanel();
			reportPeriodPanel.setLayout(new GridBagLayout());
			reportPeriodPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Report Date",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12), null));
			reportPeriodPanel.add(startLabel, gridBagConstraints4);
			reportPeriodPanel.add(getStartDatePicker(), gridBagConstraints5);
			reportPeriodPanel.add(endDateLabel, gridBagConstraints6);
			reportPeriodPanel.add(getEndDatePicker(), gridBagConstraints7);
			reportPeriodPanel.add(labelLabel, gridBagConstraints8);
			reportPeriodPanel.add(getLabelTextField(), gridBagConstraints9);
			reportPeriodPanel.add(periodTypeLabel, gridBagConstraints21);
			reportPeriodPanel
					.add(getPeriodTypeComboBox(), gridBagConstraints22);
			reportPeriodPanel.add(periodLabel, gridBagConstraints23);
			reportPeriodPanel.add(getPeriodPanel(), gridBagConstraints24);
		}
		return reportPeriodPanel;
	}

	private DatePicker getStartDatePicker() {
		if (startDatePicker == null) {
			startDatePicker = new DatePicker();
			startDatePicker.addPropertyChangeListener("date",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onDateChanged(e);
						}
					});
		}
		return startDatePicker;
	}

	private void onDateChanged(PropertyChangeEvent e) {
		if(periodTypeComboBox.getSelectedItem().equals("Custom")){
			setLabel();
			setCompLabel();
		}
	}

	private DatePicker getEndDatePicker() {
		if (endDatePicker == null) {
			endDatePicker = new DatePicker();
			endDatePicker.addPropertyChangeListener("date",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onDateChanged(e);
						}
					});
		}
		return endDatePicker;
	}

	private JTextField getLabelTextField() {
		if (labelTextField == null) {
			labelTextField = new JTextField();
		}
		return labelTextField;
	}

	private JPanel getComparativePanel() {
		if (comparativePanel == null) {
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 1;
			gridBagConstraints27.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints27.insets = new java.awt.Insets(1,5,5,5);
			gridBagConstraints27.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints27.gridy = 1;
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints26.insets = new java.awt.Insets(1,5,5,5);
			gridBagConstraints26.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints26.gridy = 1;
			compPeriodLabel = new JLabel();
			compPeriodLabel.setText("Period");
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridwidth = 2;
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints19.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints19.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.gridy = 0;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.gridy = 4;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints18.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints18.gridx = 1;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints17.gridy = 4;
			compLabelLabel = new JLabel();
			compLabelLabel.setText("Label");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 3;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints15.gridy = 3;
			compEndDateLabel = new JLabel();
			compEndDateLabel.setText("End Date");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 2;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(0,5,5,5);
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 2;
			compStartDateLabel = new JLabel();
			compStartDateLabel.setText("Start Date");
			comparativePanel = new JPanel();
			comparativePanel.setLayout(new GridBagLayout());
			comparativePanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Comparative Date",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12), null));
			comparativePanel.add(compStartDateLabel, gridBagConstraints13);
			comparativePanel.add(getCompStartDatePicker(), gridBagConstraints14);
			comparativePanel.add(compEndDateLabel, gridBagConstraints15);
			comparativePanel.add(getCompEndDatePicker(), gridBagConstraints16);
			comparativePanel.add(compLabelLabel, gridBagConstraints17);
			comparativePanel.add(getCompLabelTextField(), gridBagConstraints18);
			comparativePanel
					.add(getComparativeCheckBox(), gridBagConstraints19);
			comparativePanel.add(compPeriodLabel, gridBagConstraints26);
			comparativePanel.add(getCompPeriodPanel(), gridBagConstraints27);
		}
		return comparativePanel;
	}

	/**
	 * This method initializes compStartDateTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private DatePicker getCompStartDatePicker() {
		if (compStartDatePicker == null) {
			compStartDatePicker = new DatePicker();
			compStartDatePicker.addPropertyChangeListener("date",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onDateChanged(e);
						}
					});
		}
		return compStartDatePicker;
	}

	/**
	 * This method initializes compEndDateTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private DatePicker getCompEndDatePicker() {
		if (compEndDatePicker == null) {
			compEndDatePicker = new DatePicker();
			compEndDatePicker.addPropertyChangeListener("date",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onDateChanged(e);
						}
					});
		}
		return compEndDatePicker;
	}

	/**
	 * This method initializes compLabelTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCompLabelTextField() {
		if (compLabelTextField == null) {
			compLabelTextField = new JTextField();
		}
		return compLabelTextField;
	}

	/**
	 * This method initializes comparativeCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getComparativeCheckBox() {
		if (comparativeCheckBox == null) {
			comparativeCheckBox = new JCheckBox();
			comparativeCheckBox.setText("View Comparative Period");
			comparativeCheckBox
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							comparative();
						}
					});
		}
		return comparativeCheckBox;
	}

	private void comparative() {
		setComparativeEnable(comparativeCheckBox.isSelected());
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JRViewer getReport() {
		if (report == null) {
			Income_statement stmt = new Income_statement();
			report = stmt.getViewer();
		}
		return report;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout);
			buttonPanel.add(getPreviewButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes previewButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPreviewButton() {
		if (previewButton == null) {
			previewButton = new JButton();
			previewButton.setText("Preview");
			previewButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							preview();
						}
					});
		}
		return previewButton;
	}

	private void preview() {
		IndirectCashFlowStatementDesign design = (IndirectCashFlowStatementDesign) getReportDesignComboBox()
				.getSelectedItem();
		
		if(design==null){
			JOptionPane.showMessageDialog(this, "No selected design");
			return;
		}
		
		try {
			IndirectCashFlowStatementLogic logic = new IndirectCashFlowStatementLogic(connection, sessionId);
			design = logic.getIndirectCashFlowStatementDesign(design);
			
			IndirectCashFlowStatementReportContext context = new IndirectCashFlowStatementReportContext();
			context.setReportTitle(design.getTitle());
			context.setLanguage(design.getLanguagePack());
			context.setPeriodType((String) periodTypeComboBox.getSelectedItem());
			context.setReportStartDate(startDatePicker.getDate());
			context.setReportEndDate(endDatePicker.getDate());
			context.setComparativeStartDate(compStartDatePicker.getDate());
			context.setComparativeEndDate(compEndDatePicker.getDate());
			context.setViewComparative(comparativeCheckBox.isSelected());
			context.setShowZeroValue(showNullCheckBox.isSelected());
			
			context.setReportPeriodLabel(labelTextField.getText());
			context.setComparativePeriodLabel(compLabelTextField.getText());
			
			context.setJournals(createJournals(design));
			
			revalidateReport(context, design);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "There is an error while compiling report. \n " +
					"Pleas check your design!");
			e.printStackTrace();
		}
		
	}

	private String createJournals(IndirectCashFlowStatementDesign design) {
		Journal[] journals = design.getJournals();
		String js = "";
		List list = Arrays.asList(journals);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			Journal j = (Journal) iterator.next();
			js += j.getIndex();
			if(iterator.hasNext())
				js += ", ";
		}
		return js;
	}

	private void revalidateReport(IndirectCashFlowStatementReportContext context, IndirectCashFlowStatementDesign design) {
		if(report==null)
			report = getReport();
		
		
		rightPanel.remove(report);
		
		IndirectCashFlowReport stmt = new IndirectCashFlowReport(context, design, connection, false);
		report = stmt.getViewer();
		
		rightPanel.add(report, java.awt.BorderLayout.CENTER);
		rightPanel.validate();
	}

	/**
	 * This method initializes periodComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getPeriodTypeComboBox() {
		if (periodTypeComboBox == null) {
			periodTypeComboBox = new JComboBox();
			DefaultComboBoxModel model = new DefaultComboBoxModel(ReportContext.PERIODS);
			periodTypeComboBox.setModel(model);
			periodTypeComboBox
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							periodTypeSelected();
						}
					});
		}
		return periodTypeComboBox;
	}

	private void periodTypeSelected() {
		String sel = (String) getPeriodTypeComboBox().getSelectedItem();
		if (sel == "Custom") {
			revalidatePeriodPanel(new JPanel());
			revalidateCompPeriodPanel(new JPanel());
			
			periodEnabling(true);
			compPeriodEnabling(true&getComparativeCheckBox().isSelected());
		} else {
			periodEnabling(false);
			compPeriodEnabling(false&getComparativeCheckBox().isSelected());
			if (sel == "Annually") {
				revalidatePeriodPanel(new AnnuallyPeriodChooser());
				revalidateCompPeriodPanel(new AnnuallyPeriodChooser());
			} else if (sel == "Semester") {
				revalidatePeriodPanel(new SemesterPeriodChooser());
				revalidateCompPeriodPanel(new SemesterPeriodChooser());
			} else if (sel == "Quarterly") {
				revalidatePeriodPanel(new QuarterlyPeriodChooser());
				revalidateCompPeriodPanel(new QuarterlyPeriodChooser());
			} else if (sel == "Monthly") {
				revalidatePeriodPanel(new MonthlyPeriodChooser());
				revalidateCompPeriodPanel(new MonthlyPeriodChooser());
			}
		}
	}

	private void revalidateCompPeriodPanel(JPanel panel) {
		comparativePanel.remove(compPeriodPanel);
		comparativePanel.revalidate();
		comparativePanel.repaint();
		
		compPeriodPanel = panel;
		compPeriodPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(java.beans.PropertyChangeEvent e) {
						onCompPeriodChanged(e);
					}
				});
		
		GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
		gridBagConstraints27.gridx = 1;
		gridBagConstraints27.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints27.insets = new java.awt.Insets(1,5,5,5);
		gridBagConstraints27.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints27.gridy = 1;
		
		comparativePanel.add(compPeriodPanel, gridBagConstraints27);
		comparativePanel.revalidate(); 
		
		setComparativeEnable(getComparativeCheckBox().isSelected());
	}

	private void compPeriodEnabling(boolean enable) {
		compStartDatePicker.setEditable(enable);
		compEndDatePicker.setEditable(enable);
		compLabelTextField.setEditable(enable);
	}

	private void periodEnabling(boolean enable) {
		startDatePicker.setEditable(enable);
		endDatePicker.setEditable(enable);
		labelTextField.setEditable(enable);
	}

	private void revalidatePeriodPanel(JPanel panel) {
		reportPeriodPanel.remove(periodPanel);
		reportPeriodPanel.revalidate();
		reportPeriodPanel.repaint();
		
		periodPanel = panel;
		periodPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(java.beans.PropertyChangeEvent e) {
						onPeriodChanged(e);
					}
				});
		
		GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
		gridBagConstraints24.gridx = 2;
		gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints24.insets = new java.awt.Insets(0,5,5,5);
		gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints24.gridy = 1;
		
		reportPeriodPanel.add(periodPanel, gridBagConstraints24);
		reportPeriodPanel.revalidate(); 
	}

	/**
	 * This method initializes periodPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPeriodPanel() {
		if (periodPanel == null) {
			periodPanel = new JPanel();
			
			periodPanel.addPropertyChangeListener("value",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onPeriodChanged(e);
						}
					});
		}
		return periodPanel;
	}

	protected void onPeriodChanged(PropertyChangeEvent e) {
		if(periodPanel instanceof AnnuallyPeriodChooser){
			AnnuallyPeriodChooser chooser = (AnnuallyPeriodChooser) periodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			cal.set(chooser.getYear(), 0, 1);
			getStartDatePicker().setDate(cal.getTime());
			cal.set(chooser.getYear(), 11, 31);
			getEndDatePicker().setDate(cal.getTime());
		}else if(periodPanel instanceof MonthlyPeriodChooser){
			MonthlyPeriodChooser chooser = (MonthlyPeriodChooser) periodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			cal.set(chooser.getYear(), chooser.getMonth(), 1);
			getStartDatePicker().setDate(cal.getTime());
			cal.set(chooser.getYear(), chooser.getMonth() + 1, 1);
			cal.add(Calendar.DATE, -1);
			getEndDatePicker().setDate(cal.getTime());
		}else if(periodPanel instanceof SemesterPeriodChooser){
			SemesterPeriodChooser chooser = (SemesterPeriodChooser) periodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			String period = chooser.getPeriod();
			if(period.equalsIgnoreCase("H1")){
				cal.set(chooser.getYear(), 0, 1);
				getStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 5, 30);
				getEndDatePicker().setDate(cal.getTime());
			}else{
				cal.set(chooser.getYear(), 6, 1);
				getStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 11, 31);
				getEndDatePicker().setDate(cal.getTime());
			}
		}else if(periodPanel instanceof QuarterlyPeriodChooser){
			QuarterlyPeriodChooser chooser = (QuarterlyPeriodChooser) periodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			String period = chooser.getPeriod();
			if(period.equalsIgnoreCase("Q1")){
				cal.set(chooser.getYear(), 0, 1);
				getStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 3, 1);
				cal.add(Calendar.DATE, -1);
				getEndDatePicker().setDate(cal.getTime());
			}else if(period.equalsIgnoreCase("Q2")){
				cal.set(chooser.getYear(), 3, 1);
				getStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 6, 1);
				cal.add(Calendar.DATE, -1);
				getEndDatePicker().setDate(cal.getTime());
			}else if(period.equalsIgnoreCase("Q3")){
				cal.set(chooser.getYear(), 6, 1);
				getStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 9, 1);
				cal.add(Calendar.DATE, -1);
				getEndDatePicker().setDate(cal.getTime());
			}else if(period.equalsIgnoreCase("Q4")){
				cal.set(chooser.getYear(), 9, 1);
				getStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 11, 31);
				getEndDatePicker().setDate(cal.getTime());
			}
		}
		setLabel();
	}

	private void setLabel() {
		if(periodPanel instanceof AnnuallyPeriodChooser){
			AnnuallyPeriodChooser chooser = (AnnuallyPeriodChooser) periodPanel;
			labelTextField.setText(String.valueOf(chooser.getYear()));
		}else if(periodPanel instanceof MonthlyPeriodChooser){
			MonthlyPeriodChooser chooser = (MonthlyPeriodChooser) periodPanel;
			labelTextField.setText(chooser.getMonthInString() + " " + chooser.getYear());
		}else if(periodPanel instanceof SemesterPeriodChooser){
			SemesterPeriodChooser chooser = (SemesterPeriodChooser) periodPanel;
			labelTextField.setText(chooser.getPeriod() + " " + chooser.getYear());
		}else if(periodPanel instanceof QuarterlyPeriodChooser){
			QuarterlyPeriodChooser chooser = (QuarterlyPeriodChooser) periodPanel;
			labelTextField.setText(chooser.getPeriod() + " " + chooser.getYear());
		}else{
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = startDatePicker.getDate();
			String start = dateformat.format(date);
			date = endDatePicker.getDate();
			String end = dateformat.format(date);
			String lbl = start + " to " + end;
			labelTextField.setText(lbl);
		}
	}

	/**
	 * This method initializes compPeriodPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCompPeriodPanel() {
		if (compPeriodPanel == null) {
			compPeriodPanel = new JPanel();
			compPeriodPanel.addPropertyChangeListener("value",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onCompPeriodChanged(e);
						}
					});
		}
		return compPeriodPanel;
	}

	private void onCompPeriodChanged(PropertyChangeEvent e) {
		if(compPeriodPanel instanceof AnnuallyPeriodChooser){
			AnnuallyPeriodChooser chooser = (AnnuallyPeriodChooser) compPeriodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			cal.set(chooser.getYear(), 0, 1);
			getCompStartDatePicker().setDate(cal.getTime());
			cal.set(chooser.getYear(), 11, 31);
			getCompEndDatePicker().setDate(cal.getTime());
		}else if(compPeriodPanel instanceof MonthlyPeriodChooser){
			MonthlyPeriodChooser chooser = (MonthlyPeriodChooser) compPeriodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			cal.set(chooser.getYear(), chooser.getMonth(), 1);
			getCompStartDatePicker().setDate(cal.getTime());
			cal.set(chooser.getYear(), chooser.getMonth() + 1, 1);
			cal.add(Calendar.DATE, -1);
			getCompEndDatePicker().setDate(cal.getTime());
		}else if(compPeriodPanel instanceof SemesterPeriodChooser){
			SemesterPeriodChooser chooser = (SemesterPeriodChooser) compPeriodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			String period = chooser.getPeriod();
			if(period.equalsIgnoreCase("H1")){
				cal.set(chooser.getYear(), 0, 1);
				getCompStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 5, 30);
				getCompEndDatePicker().setDate(cal.getTime());
			}else{
				cal.set(chooser.getYear(), 6, 1);
				getCompStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 11, 31);
				getCompEndDatePicker().setDate(cal.getTime());
			}
		}else if(compPeriodPanel instanceof QuarterlyPeriodChooser){
			QuarterlyPeriodChooser chooser = (QuarterlyPeriodChooser) compPeriodPanel;
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			String period = chooser.getPeriod();
			if(period.equalsIgnoreCase("Q1")){
				cal.set(chooser.getYear(), 0, 1);
				getCompStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 3, 1);
				cal.add(Calendar.DATE, -1);
				getCompEndDatePicker().setDate(cal.getTime());
			}else if(period.equalsIgnoreCase("Q2")){
				cal.set(chooser.getYear(), 3, 1);
				getCompStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 6, 1);
				cal.add(Calendar.DATE, -1);
				getCompEndDatePicker().setDate(cal.getTime());
			}else if(period.equalsIgnoreCase("Q3")){
				cal.set(chooser.getYear(), 6, 1);
				getCompStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 9, 1);
				cal.add(Calendar.DATE, -1);
				getCompEndDatePicker().setDate(cal.getTime());
			}else if(period.equalsIgnoreCase("Q4")){
				cal.set(chooser.getYear(), 9, 1);
				getCompStartDatePicker().setDate(cal.getTime());
				cal.set(chooser.getYear(), 11, 31);
				getCompEndDatePicker().setDate(cal.getTime());
			}
		}
		setCompLabel();
	}

	private void setCompLabel() {
		if(compPeriodPanel instanceof AnnuallyPeriodChooser){
			AnnuallyPeriodChooser chooser = (AnnuallyPeriodChooser) compPeriodPanel;
			compLabelTextField.setText(String.valueOf(chooser.getYear()));
		}else if(compPeriodPanel instanceof MonthlyPeriodChooser){
			MonthlyPeriodChooser chooser = (MonthlyPeriodChooser) compPeriodPanel;
			compLabelTextField.setText(chooser.getMonthInString() + " " + chooser.getYear());
		}else if(compPeriodPanel instanceof SemesterPeriodChooser){
			SemesterPeriodChooser chooser = (SemesterPeriodChooser) compPeriodPanel;
			compLabelTextField.setText(chooser.getPeriod() + " " + chooser.getYear());
		}else if(compPeriodPanel instanceof QuarterlyPeriodChooser){
			QuarterlyPeriodChooser chooser = (QuarterlyPeriodChooser) compPeriodPanel;
			compLabelTextField.setText(chooser.getPeriod() + " " + chooser.getYear());
		}else{
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = compStartDatePicker.getDate();
			String start = dateformat.format(date);
			date = compEndDatePicker.getDate();
			String end = dateformat.format(date);
			String lbl = start + " to " + end;
			compLabelTextField.setText(lbl);
		}
	}

}
