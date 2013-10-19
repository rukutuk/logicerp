/**
 * 
 */
package pohaci.gumunda.titis.application;

/**
 * Variable in a formula, consists of variable's description 
 * (varName) and its formula code (formulaCode)
 * @author Pohaci
 *
 */
public class FormulaVariable {
	  public String varName;
	  public String formulaCode;
	  public FormulaVariable(String name,String code)
	  {
		  varName = name; formulaCode = code;
		  if (formulaCode.indexOf(" ")>=0)
		  {
			  throw new RuntimeException("formula code must not contain spaces");
		  }
	  }
	  public String toString()
	  {
		  return varName;
	  }
  }