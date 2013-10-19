/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;

/**
 * @author dark-knight
 *
 */
public class ReportRowPicker extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField rowTextField = null;
	private JButton rowButton = null;
	private Design design = null;
	private ReportRow row = null;

	/**
	 * This is the default constructor
	 */
	public ReportRowPicker(Design design) {
		initialize();
		this.design = design;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new java.awt.Insets(0,2,0,0);
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0,0,0,2);
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(300, 25);
		this.add(getRowTextField(), gridBagConstraints);
		this.add(getRowButton(), gridBagConstraints1);
	}

	/**
	 * This method initializes rowTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getRowTextField() {
		if (rowTextField == null) {
			rowTextField = new JTextField() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void addMouseListener(MouseListener l) {
				}

				public boolean isFocusTraversable() {
					return false;
				}

			};
			rowTextField.setPreferredSize(new java.awt.Dimension(54,18));
		}
		return rowTextField;
	}

	/**
	 * This method initializes rowButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRowButton() {
		if (rowButton == null) {
			rowButton = new JButton();
			rowButton.setText("...");
			rowButton.setPreferredSize(new java.awt.Dimension(22,18));
			rowButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onBrowse();
				}
			});
		}
		return rowButton;
	}

	private void onBrowse() {
		if(design.toString().equals(""))
			return;
		
		IncomeStatementRowPickerDlg dlg = new IncomeStatementRowPickerDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), "I/S Rows", true, design);
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.OK_OPTION){
			ReportRow row = dlg.getRow();
			setRow(row);
		}
	}

	public Design getDesign() {
		return design;
	}

	public void setDesign(Design design) {
		this.design = design;
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		rowButton.setEnabled(enabled);
		rowTextField.setEditable(enabled);
	}

	public ReportRow getRow() {
		return row;
	}

	public void setRow(ReportRow row) {
		ReportRow old = this.row;
		this.row = row;
		if(row != null)
			rowTextField.setText(this.row.toString());
		else
			rowTextField.setText("");
		firePropertyChange("row", row, old);
	}

}
