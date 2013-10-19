package pohaci.gumunda.titis.application;

import java.math.BigDecimal;
import java.math.BigInteger;

//import java.math.MathContext;
//import java.math.RoundingMode;
public class NumberRounding {

	public static final short NUMBERROUNDING_NONE = -1;
	public static final short NUMBERROUNDING_ROUND = 0;
	public static final short NUMBERROUNDING_ROUND_UP = 1;
	public static final short NUMBERROUNDING_ROUND_DOWN = 2;
	public static final String[] ROUNDING_MODE = new String[] { "Round",
			"Round Up", "Round Down" };

	private short m_roundingMode = -1;
	private int m_precision = 0;

	public NumberRounding(short roundingMode, int precision) {
		m_roundingMode = roundingMode;
		m_precision = precision;
	}

	public short getRoundingMode() {
		return m_roundingMode;
	}

	public String getRoundingModeAsString() {
		if (m_roundingMode > -1) {
			String[] s = ROUNDING_MODE;
			return s[m_roundingMode];
		}
		return "N/A";
	}

	public int getPrecision() {
		return m_precision;
	}

	public double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bd1 = roundAlogrithm(bd);
		return bd1.doubleValue();
	}

	public float round(float value) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bd1 = roundAlogrithm(bd);
		return bd1.floatValue();
	}

	public BigDecimal round(BigDecimal value) {
		BigDecimal bd = value;
		BigDecimal bd1 = roundAlogrithm(bd);
		return bd1;
	}

	private BigDecimal roundAlogrithm(BigDecimal value) {

		/*
		 * long val = value.longValue(); String str = String.valueOf(val); int
		 * len = str.length();
		 */

		double val = value.doubleValue();
		double res = Math.floor(val);
		long longVal = new BigDecimal(res).longValue();
		int len = String.valueOf(longVal).length();

		RoundAlgorithm ra = new RoundAlgorithm(value.doubleValue(),
				m_roundingMode, len + m_precision);
		BigDecimal bd1 = new BigDecimal(ra.round());
		return bd1;
	}

	class RoundAlgorithm {

		private double m_value;
		private short m_mode = -1; // user
		private int m_roundPrecision = 0;
		private int m_precision = 0;

		protected RoundAlgorithm(double value, short mode, int precision) {
			m_value = value;
			m_mode = mode;
			m_roundPrecision = precision;
			m_precision = precision();
		}

		protected double round() {
			if (m_mode == NUMBERROUNDING_NONE) {
				return m_value;
			}
			return this.doRound();
		}

		private double doRound() {
			if (m_roundPrecision == 0)
				return m_value;

			int drop = m_precision - m_roundPrecision;
			if (drop < 0)
				return m_value;

			double rounded = dropDigits(drop);
			return rounded;
		}

		private double dropDigits(int drop) {
			int lenInt = lenInt();
			int digits = drop - (m_precision - lenInt);

			BigDecimal temp = null;
			BigDecimal res = new BigDecimal(m_value);
			BigDecimal m_bigDecimal = res;
			long round = 0;
			BigInteger tenPower = tenPower(digits);
			if (digits < 0) { // n digit di belakang koma
				temp = m_bigDecimal.multiply(new BigDecimal(tenPower));
				if (m_mode == NUMBERROUNDING_ROUND)
					round = Math.round(temp.doubleValue());
				else if (m_mode == NUMBERROUNDING_ROUND_DOWN)
					round = (long) Math.floor(temp.doubleValue());
				else if (m_mode == NUMBERROUNDING_ROUND_UP)
					round = (long) Math.ceil(temp.doubleValue());

				BigDecimal roundBig = BigDecimal.valueOf(round);
				BigDecimal tenPowerBig = new BigDecimal(tenPower);
				res = roundBig.divide(tenPowerBig, Math.abs(digits),
						BigDecimal.ROUND_HALF_UP);
			} else { // n digit di depan koma
				temp = m_bigDecimal.divide(new BigDecimal(tenPower),
						BigDecimal.ROUND_HALF_UP);
				if (m_mode == NUMBERROUNDING_ROUND)
					round = Math.round(temp.doubleValue());
				else if (m_mode == NUMBERROUNDING_ROUND_DOWN)
					round = (long) Math.floor(temp.doubleValue());
				else if (m_mode == NUMBERROUNDING_ROUND_UP)
					round = (long) Math.ceil(temp.doubleValue());
				res = BigDecimal.valueOf(round).multiply(
						new BigDecimal(tenPower));
			}

			return res.doubleValue();
		}

		private BigInteger tenPower(int digits) {
			if (digits == 0)
				return BigInteger.ONE;
			BigInteger value = new BigInteger("1");
			digits = Math.abs(digits);
			for (int i = 1; i <= digits; i++) {
				value = value.multiply(BigInteger.valueOf(10));
			}
			return value;
		}

		private int precision() {
			String str = String.valueOf(m_value);
			int len = str.length();
			if (str.indexOf(".") > -1) {
				len = len - 1;
			}
			return len;
		}

		private int lenInt() {
			double val = m_value;
			double res = Math.floor(val);
			long longVal = new BigDecimal(res).longValue();
			int len = String.valueOf(longVal).length();
			return len;
		}
	}

}
