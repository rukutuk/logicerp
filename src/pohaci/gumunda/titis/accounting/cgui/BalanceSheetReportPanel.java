package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;

import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.cgui.report.BalanceSheetReport;
import pohaci.gumunda.titis.accounting.entity.reportdesign.BalanceSheetDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.BalanceSheetReportContext;
import pohaci.gumunda.titis.accounting.entity.reportdesign.MySimpleDateFormat;
import pohaci.gumunda.titis.accounting.logic.reportdesign.BalanceSheetLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.LanguagePack;

/**
 * @author dark-knight
 *
 */
public class BalanceSheetReportPanel extends JPanel {

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
	private JPanel comparativePanel = null;
	private JLabel compStartDateLabel = null;
	private DatePicker compStartDatePicker = null;
	private JCheckBox comparativeCheckBox = null;
	private JRViewer report = null;
	private JPanel buttonPanel = null;
	private JButton previewButton = null;
	private JToggleButton excelButton = null;
	private Connection connection;
	private long sessionId;	
	private boolean m_excel = false;
	
	/**
	 * This is the default constructor
	 */
	public BalanceSheetReportPanel(Connection connection, long sessionId) {
		super();
		this.connection = connection;
		this.sessionId = sessionId;
		initialize();
		setDefault();
		initDesignList();
	}

	private void initDesignList() {
		BalanceSheetLogic logic = new BalanceSheetLogic(connection,
				sessionId);
		List list = logic.getDesignList();
		BalanceSheetDesign[] designs = (BalanceSheetDesign[]) list
				.toArray(new BalanceSheetDesign[list.size()]);

		DefaultComboBoxModel model = new DefaultComboBoxModel(designs);
		getReportDesignComboBox().setModel(model);
	}

	private void setDefault() {
		getShowNullCheckBox().setSelected(true);
		getComparativeCheckBox().setSelected(false);
		setComparativeEnable(false);
		
		getStartDatePicker().setDate(new Date());
		getCompStartDatePicker().setDate(new Date());
	}

	private void setComparativeEnable(boolean enable) {
		compPeriodEnabling(enable);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(873, 440);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(getSplitPane(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * This method initializes splitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
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

	/**
	 * This method initializes leftPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(getNorthLeftPanel(), java.awt.BorderLayout.NORTH);
		}
		return leftPanel;
	}

	/**
	 * This method initializes rightPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JPanel();
			rightPanel.setLayout(new BorderLayout());
			rightPanel.add(getReport(), java.awt.BorderLayout.CENTER);
		}
		return rightPanel;
	}

	/**
	 * This method initializes northLeftPanel
	 * 
	 * @return javax.swing.JPanel
	 */
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

	/**
	 * This method initializes reportDesignPanel
	 * 
	 * @return javax.swing.JPanel
	 */
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

	/**
	 * This method initializes reportDesignComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getReportDesignComboBox() {
		if (reportDesignComboBox == null) {
			reportDesignComboBox = new JComboBox();
		}
		return reportDesignComboBox;
	}

	/**
	 * This method initializes reportOptionsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
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

	/**
	 * This method initializes showNullCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getShowNullCheckBox() {
		if (showNullCheckBox == null) {
			showNullCheckBox = new JCheckBox();
			showNullCheckBox.setText("Show Label with Zero Values");
		}
		return showNullCheckBox;
	}

	/**
	 * This method initializes reportPeriodPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getReportPeriodPanel() {
		if (reportPeriodPanel == null) {
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints25.gridy = 1;
			gridBagConstraints25.weightx = 0.0;
			gridBagConstraints25.insets = new java.awt.Insets(0, 1, 5, 5);
			gridBagConstraints25.gridx = 2;
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
			startLabel.setText("Date");
			reportPeriodPanel = new JPanel();
			reportPeriodPanel.setLayout(new GridBagLayout());
			reportPeriodPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Report Period",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12), null));
			reportPeriodPanel.add(startLabel, gridBagConstraints4);
			reportPeriodPanel.add(getStartDatePicker(), gridBagConstraints5);
		}
		return reportPeriodPanel;
	}

	/**
	 * This method initializes startDateTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private DatePicker getStartDatePicker() {
		if (startDatePicker == null) {
			startDatePicker = new DatePicker();
		}
		return startDatePicker;
	}


	/**
	 * This method initializes comparativePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getComparativePanel() {
		if (comparativePanel == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridwidth = 2;
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints19.insets = new java.awt.Insets(1, 5, 5, 5);
			gridBagConstraints19.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.gridy = 0;
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
			compStartDateLabel.setText("Date");
			comparativePanel = new JPanel();
			comparativePanel.setLayout(new GridBagLayout());
			comparativePanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Comparative Period",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12), null));
			comparativePanel.add(compStartDateLabel, gridBagConstraints13);
			comparativePanel.add(getCompStartDatePicker(), gridBagConstraints14);
			comparativePanel
					.add(getComparativeCheckBox(), gridBagConstraints19);
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
		}
		return compStartDatePicker;
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
			BalanceSheetReport stmt = new BalanceSheetReport();
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
			buttonPanel.add(getExcelButton(), null);
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
							m_excel = false;
							preview();
						}
					});
		}
		return previewButton;
	}

	/**
	 * This method initializes excelButton
	 * 
	 * @return javax.swing.JToggleButton
	 */
	private JToggleButton getExcelButton() {
		if (excelButton == null) {
			excelButton = new JToggleButton(new ImageIcon("../images/excell.gif"));
			excelButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							m_excel = true;
							preview();
						}
					});
		}
		return excelButton;
	}
		
	private void preview() {
		BalanceSheetDesign design = (BalanceSheetDesign) getReportDesignComboBox()
				.getSelectedItem();
		
		if(design==null){
			JOptionPane.showMessageDialog(this, "No selected design");
			return;
		}
		
		BalanceSheetLogic logic = new BalanceSheetLogic(connection, sessionId);
		design = logic.getBalanceSheetDesign(design);
		
		BalanceSheetReportContext context = new BalanceSheetReportContext();
		context.setReportTitle(design.getTitle());
		context.setLanguage(design.getLanguagePack());
		context.setReportStartDate(startDatePicker.getDate());
		context.setComparativeStartDate(compStartDatePicker.getDate());
		context.setViewComparative(comparativeCheckBox.isSelected());
		context.setShowZeroValue(showNullCheckBox.isSelected());
		
		MySimpleDateFormat dateFormat = MySimpleDateFormat.ENGLISH;
		if(design.getLanguagePack()==LanguagePack.INDONESIAN)
			dateFormat = MySimpleDateFormat.INDONESIAN;
		
		String formatStd = dateFormat.format(new Date());
		
		context.setReportPeriodLabel((startDatePicker.getDate()!=null) ? dateFormat.format(startDatePicker.getDate()) : formatStd);
		context.setComparativePeriodLabel((compStartDatePicker.getDate()!=null) ? dateFormat.format(compStartDatePicker.getDate()) : formatStd);
		
		context.setJournals(createJournals(design));
		
		revalidateReport(context, design);
		
	}

	private String createJournals(BalanceSheetDesign design) {
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

	private void revalidateReport(BalanceSheetReportContext context, BalanceSheetDesign design) {
		if(report==null)
			report = getReport();
		
		
		rightPanel.remove(report);
		
		BalanceSheetReport stmt = new BalanceSheetReport(context, design, connection, m_excel);
		
		if(!m_excel)
			report = stmt.getViewer();
		
		rightPanel.add(report, java.awt.BorderLayout.CENTER);
		rightPanel.validate();
	}

	

	private void compPeriodEnabling(boolean enable) {
		compStartDatePicker.setEditable(enable);
	}
}
