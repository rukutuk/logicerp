/**
 *
 */
package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.JPanel;

import pohaci.gumunda.titis.accounting.cgui.panelloader.ITabbedTransactionPanel;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayroll;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentSubmitPicker;

public abstract class PayrollVerificationPanel extends JPanel implements ITabbedTransactionPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected UnitPicker m_unitPicker;
	public JMonthChooser m_monthComboBox;
	public JSpinField m_yearField;
	protected PayrollComponentSubmitPicker m_componentPicker;

	public void openAndLoadObject(StateTemplateEntity obj) {
		EmployeePayroll payroll = (EmployeePayroll) obj;
		m_monthComboBox.setMonth(payroll.getMonth()-1);
		m_yearField.setValue(payroll.getYear());
		m_unitPicker.setObject(payroll.getUnit());
		PayrollComponent rootComponent = new PayrollComponent(-1, "", "Payroll Component", true, null,
				Short.parseShort("-1"), Short.parseShort("-1"), null,"");
		m_componentPicker.setObject(rootComponent);
		presentingSubmittedData();
	}

	protected abstract void presentingSubmittedData();
}
