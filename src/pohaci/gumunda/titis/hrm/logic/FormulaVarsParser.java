/**
 * 
 */
package pohaci.gumunda.titis.hrm.logic;

import java.util.Hashtable;

import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaToken;
import pohaci.gumunda.titis.application.FormulaVariable;

/**
 * Recognizes formula variables in formula and convert them into tokens.
 * Formula variables to be recognized must be passed during construction.
 * Capable of decorating another TokenParser passed as construction parameter inner.
 * @author Yudhi Widyatama
 *
 */
public class FormulaVarsParser implements Formula.TokenParser {
	Hashtable map = new Hashtable();
	
	/**
	 * @see Decorator Pattern, in Design Patterns book
	 */
	Formula.TokenParser inner;
	public FormulaVarsParser(FormulaVariable[] formulaVariables, Formula.TokenParser inner)
	{
		int i;
		for (i=0;i<formulaVariables.length;i++)
			map.put(formulaVariables[i].formulaCode,formulaVariables[i]);
		this.inner = inner;
	}
	public FormulaToken parseToken(String str) {
		if (map.containsKey(str))
		{
			return FormulaToken.createVariable((FormulaVariable)map.get(str),true);
		}
		if (inner!=null)
			return inner.parseToken(str);
		return null;
	}
}