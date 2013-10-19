package pohaci.gumunda.titis.hrm.cgui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import com.jgoodies.binding.formatter.EmptyNumberFormatter;

public class TaxArt21Tariff {
	protected long m_index = 0;
	protected double m_minimum = 0.0;
	protected double m_maximum = 0.0;
	protected float m_tariff = 0.0f;
	
	public TaxArt21Tariff(double minimum, double maximum, float tariff) {
		m_minimum = minimum;
		m_maximum = maximum;
		m_tariff = tariff;
	}
	
	public TaxArt21Tariff(long index, double minimum, double maximum, float tariff) {
		m_index = index;
		m_minimum = minimum;
		m_maximum = maximum;
		m_tariff = tariff;
	}
	
	public TaxArt21Tariff(long index, TaxArt21Tariff tarif) {
		m_index = index;
		m_minimum = tarif.getMinimum();
		m_maximum = tarif.getMaximum();
		m_tariff = tarif.getTariff();
	}
	
	public long getIndex() {
		return m_index;
	}
	
	public double getMinimum() {
		return m_minimum;
	}
	
	public double getMaximum() {
		return m_maximum;
	}
	
	public float getTariff() {
		return  m_tariff;
	}
	
	public String toString() {
		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#,##0.00");
		decformat.setMaximumFractionDigits(2);
		formatter.setFormat(decformat);

		try {
			return formatter.valueToString(new Double(m_minimum));
		} catch (ParseException e) {
			return "";
		}
		//CurrencyFormat.getString(m_minimum).trim();
	}
}