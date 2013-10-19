/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui.periodchooser;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import pohaci.gumunda.titis.application.JYearChooser;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;

/**
 * @author dark-knight
 *
 */
public class AnnuallyPeriodChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JYearChooser yearChooser = null;

	/**
	 * This is the default constructor
	 */
	public AnnuallyPeriodChooser() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		//this.setSize(300, 200);
		this.add(getYearChooser(), gridBagConstraints);
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

	public int getYear(){
		return yearChooser.getYear();
	}

	public void setEnabled(boolean enabled) {
		yearChooser.setEnabled(enabled);
	}
}
