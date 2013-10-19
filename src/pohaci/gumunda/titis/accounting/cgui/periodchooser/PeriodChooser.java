/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui.periodchooser;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;

import pohaci.gumunda.titis.application.JYearChooser;

/**
 * @author dark-knight
 *
 */
public abstract class PeriodChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox periodComboBox = null;
	private JYearChooser yearChooser = null;
	protected DefaultComboBoxModel model = new DefaultComboBoxModel();
	protected Object oldValue = "";

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	protected void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new java.awt.Insets(0,3,0,0);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0,0,0,0);
		this.setLayout(new GridBagLayout());
		this.setSize(300, 200);
		this.add(getPeriodComboBox(), gridBagConstraints);
		this.add(getYearChooser(), gridBagConstraints1);
	}

	/**
	 * This method initializes periodComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getPeriodComboBox() {
		if (periodComboBox == null) {
			periodComboBox = new JComboBox(model);
			periodComboBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onPeriodChanged();
				}
			});
			
		}
		return periodComboBox;
	}

	private void onPeriodChanged() {
		Object newValue = periodComboBox.getSelectedItem();
		Object old = oldValue;
		oldValue = newValue;
		firePropertyChange("period", old, newValue);
	}

	/**
	 * This method initializes yearChooser	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JYearChooser getYearChooser() {
		if (yearChooser == null) {
			yearChooser = new JYearChooser();
			yearChooser.addPropertyChangeListener("year",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onYearChanged(e);
						}
					});
		}
		return yearChooser;
	}

	private void onYearChanged(PropertyChangeEvent e) {
		firePropertyChange("year", e.getOldValue(), e.getNewValue());
	}

	public int getPeriodIndex(){
		return periodComboBox.getSelectedIndex();
	}
	
	public String getPeriod(){
		return (String) periodComboBox.getSelectedItem();
	}
	
	public int getYear(){
		return yearChooser.getYear();
	}

	public void setEnabled(boolean enabled) {
		periodComboBox.setEnabled(enabled);
		yearChooser.setEnabled(enabled);
	}
}
