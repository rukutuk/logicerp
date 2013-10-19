/**
 *
 */
package pohaci.gumunda.titis.accounting.helperutil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.titis.application.DatePicker;

/**
 * @author dark-knight
 *
 */
public class BeginningBalanceTransferHelperFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 8127705746417219608L;

	private JPanel jContentPane = null;

	private JSplitPane jSplitPane = null;

	private JPanel leftPanel = null;

	private JPanel rightPanel = null;

	private JPanel northLeftPanel = null;

	private Connection defaultConnection;

	private ConnectionManager connectionManager;

	private JPanel optionPanel = null;

	private JLabel sourceLabel = null;

	private JTextField sourceTextField = null;

	private JLabel targetLabel = null;

	private JTextField targetTextField = null;

	private JLabel dateLabel = null;

	private DatePicker datePicker = null;

	private JPanel buttonPanel = null;

	private JButton previewButton = null;

	private JPanel reportPanel = null;

	private JRViewer reportViewer = null;

	/**
	 * This method initializes jSplitPane
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(300);
			jSplitPane.setContinuousLayout(true);
			jSplitPane.setEnabled(false);
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setLeftComponent(getLeftPanel());
			jSplitPane.setRightComponent(getRightPanel());
		}
		return jSplitPane;
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
			leftPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
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
			rightPanel.add(getReportPanel(), java.awt.BorderLayout.CENTER);
		}
		return rightPanel;
	}

	/**
	 * This method initializes northLeftjPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getNorthLeftPanel() {
		if (northLeftPanel == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setVgap(5);
			northLeftPanel = new JPanel();
			northLeftPanel.setLayout(borderLayout);
			northLeftPanel.add(getOptionPanel(), java.awt.BorderLayout.CENTER);
			northLeftPanel.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return northLeftPanel;
	}

	/**
	 * This method initializes optionPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getOptionPanel() {
		if (optionPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.insets = new java.awt.Insets(5,5,0,0);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(5,5,0,0);
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 2;
			dateLabel = new JLabel();
			dateLabel.setText("Date");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(5,5,0,0);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(5,5,0,0);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 1;
			targetLabel = new JLabel();
			targetLabel.setText("Target properties");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,0,0);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(5,5,0,0);
			sourceLabel = new JLabel();
			sourceLabel.setText("Source/default properties");
			optionPanel = new JPanel();
			optionPanel.setLayout(new GridBagLayout());
			optionPanel.add(sourceLabel, gridBagConstraints);
			optionPanel.add(getSourceTextField(), gridBagConstraints1);
			optionPanel.add(targetLabel, gridBagConstraints2);
			optionPanel.add(getTargetTextField(), gridBagConstraints3);
			optionPanel.add(dateLabel, gridBagConstraints4);
			optionPanel.add(getDatePicker(), gridBagConstraints5);
		}
		return optionPanel;
	}

	/**
	 * This method initializes sourceTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getSourceTextField() {
		if (sourceTextField == null) {
			sourceTextField = new JTextField();
			sourceTextField.setText("sampurna");
		}
		return sourceTextField;
	}

	/**
	 * This method initializes targetTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTargetTextField() {
		if (targetTextField == null) {
			targetTextField = new JTextField();
		}
		return targetTextField;
	}

	/**
	 * This method initializes dateTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private DatePicker getDatePicker() {
		if (datePicker == null) {
			datePicker = new DatePicker();
		}
		return datePicker;
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
			previewButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					printPreview();
				}
			});
		}
		return previewButton;
	}

	/**
	 *
	 */
	private void printPreview() {
		if (getDatePicker().getDate() != null) {
			initializeDB();
			revalidateReport();
		}
	}

	/**
	 *
	 */
	private void setViewer() {
		Connection connection = defaultConnection;
		BeginningBalanceTransferHelperViewerCreator creator =
			new BeginningBalanceTransferHelperViewerCreator(connection, getDatePicker().getDate());
		reportViewer = creator.getViewer();
	}

	/**
	 * This method initializes reportPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getReportPanel() {
		if (reportPanel == null) {
			reportPanel = new JPanel();
			reportPanel.setLayout(new BorderLayout());
			reportPanel.add(getReportViewer(), java.awt.BorderLayout.CENTER);
		}
		return reportPanel;
	}

	/**
	 * This method initializes reportViewer
	 *
	 * @return javax.swing.JPanel
	 */
	private JRViewer getReportViewer() {
		if (reportViewer == null) {
			setViewer();
		}
		return reportViewer;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BeginningBalanceTransferHelperFrame thisClass = new BeginningBalanceTransferHelperFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public BeginningBalanceTransferHelperFrame() {
		super();
		initializeDB();
		initialize();
	}

	/**
	 *
	 */
	private void initializeDB() {
		try {
			String props = getSourceTextField().getText();
			connectionManager = new ConnectionManager(props);
			defaultConnection = connectionManager.getConnection();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(679, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Begining Balance Transfer Helper");
		this.setExtendedState(MAXIMIZED_BOTH);
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
			jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private void revalidateReport() {
		if(reportViewer==null)
			reportViewer = getReportViewer();


		getReportPanel().remove(reportViewer);

		setViewer();

		getReportPanel().add(reportViewer, java.awt.BorderLayout.CENTER);
		getReportPanel().validate();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
