package pohaci.gumunda.titis.accounting.cgui.periodchooser;

import javax.swing.JPanel;

import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JYearChooser;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.util.Locale;

public class MonthlyPeriodChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMonthChooser monthChooser = null;
	private JYearChooser yearChooser = null;
	private String[] month = new String[]{"January", "February", "March", "April", "May", "June", 
							"July", "August", "September", "October", "November", "December"};

	/**
	 * This is the default constructor
	 */
	public MonthlyPeriodChooser() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
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
		//this.setSize(300, 200);
		this.add(getMonthChooser(), gridBagConstraints);
		this.add(getYearChooser(), gridBagConstraints1);
	}

	/**
	 * This method initializes monthChooser	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JMonthChooser getMonthChooser() {
		if (monthChooser == null) {
			monthChooser = new JMonthChooser(JMonthChooser.NO_SPINNER);
			monthChooser.setLocale(Locale.ENGLISH); // dipaksa english
			monthChooser.addPropertyChangeListener("month",
					new java.beans.PropertyChangeListener() {
						public void propertyChange(java.beans.PropertyChangeEvent e) {
							onMonthChanged(e);
						}
					});
		}
		return monthChooser;
	}

	private void onMonthChanged(PropertyChangeEvent e) {
		firePropertyChange("month", e.getOldValue(), e.getNewValue());
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

	public int getMonth(){
		return monthChooser.getMonth();
	}
	
	public String getMonthInString(){
		///Calendar cal = Calendar.getInstance(Locale.getDefault());
		int mm = monthChooser.getMonth();
		//cal.set(Calendar.MONTH, mm);
		//String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		String month = getDisplayName(mm);
		return month;
	}


	private String getDisplayName(int mm) {
		return month[mm];
	}

	public int getYear(){
		return yearChooser.getYear();
	}

	public void setEnabled(boolean enabled) {
		monthChooser.setEnabled(enabled);
		yearChooser.setEnabled(enabled);
	}
}
