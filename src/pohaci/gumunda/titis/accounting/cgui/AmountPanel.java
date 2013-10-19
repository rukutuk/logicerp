package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import com.jgoodies.binding.formatter.EmptyNumberFormatter;

public class AmountPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JTextField currencyTextField = new JTextField(4);
	JFormattedTextField amountNumberField;
	Currency m_currency = null;
	protected AbstractFormatter m_numberFormatter;
	
	public AmountPanel() {
		initFormatter();
		amountNumberField=new JFormattedTextField(m_numberFormatter);
		currencyTextField.setEditable(false);
		setLayout(new BorderLayout(3, 3));
		add(currencyTextField, BorderLayout.WEST);
		add(amountNumberField, BorderLayout.CENTER);
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		amountNumberField.setEditable(enabled);
	}
	
	public void setCurrency(Currency currency) {
		m_currency = currency;
		currencyTextField.setText(currency.getSymbol());
	}
	
	public Currency getCurrency() {
		return m_currency;
	}
	
	public void setValue(double value) {
		amountNumberField.setValue(new Double(value));
	}
	
	public Object getValue() {
		return amountNumberField.getValue();
	}
	
	public void initFormatter(){
		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));		
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#,##0.00");
		//decformat.setMaximumIntegerDigits(20);
		decformat.setMaximumFractionDigits(2);
		formatter.setFormat(decformat);
		m_numberFormatter = formatter;
	}
}
