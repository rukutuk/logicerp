/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui.periodchooser;

import javax.swing.DefaultComboBoxModel;

/**
 * @author dark-knight
 *
 */
public class QuarterlyPeriodChooser extends PeriodChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public QuarterlyPeriodChooser() {
		super();
		model = new DefaultComboBoxModel(new String[]{"Q1", "Q2", "Q3", "Q4"});
		oldValue = "Q1";
		initialize();
	}

}
