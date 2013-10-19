package pohaci.gumunda.titis.application;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import com.jgoodies.binding.formatter.EmptyNumberFormatter;

/*
 * class ini pada awalnya dibuat untuk angka dalam cell dalam suatu table,
 * sedemikian sehingga ia mempunyai format seperti 1,030,123.00 tanpa ada Exponent
 */
public class DoubleFormatted {

	private EmptyNumberFormatter m_numberFormatter;

	private JFormattedTextField nilaiJF;

	public DoubleFormatted(double nilai) {
		initNumberFormats();
		nilaiJF = new JFormattedTextField(m_numberFormatter);
		nilaiJF.setValue(new Double(nilai));

	}

	private void initNumberFormats() {
		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#,##0.00");
		decformat.setMaximumFractionDigits(2);
		formatter.setFormat(decformat);
		m_numberFormatter = formatter;
	}

	public double doubleValue() {
		return ((Number) nilaiJF.getValue()).doubleValue();
	}

	public String toString() {
		String nilai = nilaiJF.getText();
		if (nilai.equals(""))
			return "0.00";
		else
			return nilaiJF.getText();
	}

	/**
	 * @param args
	 *            ngetESt
	 */
	public static void main(String[] args) {
		DoubleFormatted hasil = new DoubleFormatted(1000000000);
		DoubleFormatted hasil2 = new DoubleFormatted(1234567890);
		System.out.println("hasil_1 = " + hasil);
		System.out.println("hasil_2 = " + hasil2);

	}

}
