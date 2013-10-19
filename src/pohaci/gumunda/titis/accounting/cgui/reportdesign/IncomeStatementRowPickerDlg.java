/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;

/**
 * @author dark-knight
 *
 */
public class IncomeStatementRowPickerDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel buttonPanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JScrollPane scrollPane = null;
	private JTree rowTree = null;
	private JFrame mainframe = null;
	private Design design = null;
	private ReportRow row = null;
	private int response = JOptionPane.CANCEL_OPTION;
	
	/**
	 * This is the default constructor
	 */
	public IncomeStatementRowPickerDlg(JFrame owner, String title, boolean modal, Design design) {
		super(owner, title, modal);
		mainframe = owner;
		this.design = design;
		initialize();
		initTree();
	}

	private void initTree() {
		ReportRow root = design.getRootRow();
		DefaultTreeModel model = new DefaultTreeModel(root);
		getRowTree().setModel(model);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
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
			jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
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
			buttonPanel.add(getOkButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Add");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onOK();
				}
			});
		}
		return okButton;
	}

	protected void onOK() {
		setResponse(JOptionPane.OK_OPTION);	
		setVisible(false);
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
		setResponse(JOptionPane.CANCEL_OPTION);
		setVisible(false);
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getRowTree());
		}
		return scrollPane;
	}

	/**
	 * This method initializes rowTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getRowTree() {
		if (rowTree == null) {
			rowTree = new JTree();
			rowTree.setCellRenderer(new TreeableReportRowTreeCellRenderer());
			rowTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
					onTreeSelection(e);
				}
			});
		}
		return rowTree;
	}

	protected void onTreeSelection(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if(path==null)
			return;
		
		ReportRow node = (ReportRow) path.getLastPathComponent();
		if(node!=null)
			setRow(node);
	}

	public void setVisible(boolean flag) {
		Rectangle rc = mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int) (rc.getWidth() - rcthis.getWidth()) / 2 + rc.x,
				(int) (rc.getHeight() - rcthis.getHeight()) / 2 + rc.y,
				(int) rcthis.getWidth(), (int) rcthis.getHeight());

		super.setVisible(flag);
	}

	public ReportRow getRow() {
		return row;
	}

	public void setRow(ReportRow row) {
		this.row = row;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

}
