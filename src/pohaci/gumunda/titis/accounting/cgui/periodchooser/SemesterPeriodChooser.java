/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui.periodchooser;

import javax.swing.DefaultComboBoxModel;

/**
 * @author dark-knight
 *
 */
public class SemesterPeriodChooser extends PeriodChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SemesterPeriodChooser(){
		super();
		model = new DefaultComboBoxModel(new String[]{"H1", "H2"});
		oldValue = "H1";
		initialize();
	}
}
