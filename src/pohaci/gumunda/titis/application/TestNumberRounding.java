package pohaci.gumunda.titis.application;

import junit.framework.TestCase;

public class TestNumberRounding extends TestCase {

	/*
	 * Test method for 'pohaci.gumunda.titis.application.NumberRounding.round(double)'
	 */
	public void testRoundDouble() {
		double val = 123509.7864324d;
		short precision = 4;
		NumberRounding rounding = 
			new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, precision);
		val = rounding.round(val);
		System.out.println("New Value: " + val);
	}

}
