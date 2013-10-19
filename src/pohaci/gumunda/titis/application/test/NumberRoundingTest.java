/**
 *
 */
package pohaci.gumunda.titis.application.test;

import pohaci.gumunda.titis.application.NumberRounding;
import junit.framework.TestCase;

/**
 * @author dark-knight
 *
 */
public class NumberRoundingTest extends TestCase {

	/**
	 * Test method for {@link pohaci.gumunda.titis.application.NumberRounding#round(double)}.
	 */
	public void testRoundDouble() {
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		double nbr = -0.50999999999999d;
		double actual = nr.round(nbr);
		double expected = -0.51d;

		System.err.println(actual);
		assertEquals(expected, actual, 0d);
	}

}
