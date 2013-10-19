/**
 * 
 */
package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;

import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaToken;
import pohaci.gumunda.titis.application.FormulaVariable;
import pohaci.gumunda.titis.application.Formula.TokenParser;
/**
 * Recognizes formula variables plus payroll component items in the formula.
 * @author Yudhi Widyatama
 *
 */
public class PayrollTokenParser implements Formula.TokenParser {
	public static final FormulaVariable[] formulaVariables = new FormulaVariable[] {
			  /*
			  new FormulaVariable("Presence","context.presenceCount()"),
			  new FormulaVariable("Presence Not Late","context.presenceNotLateCount()"),
			  new FormulaVariable("Field visits","context.fieldVisitCount()")
		*/
			  new FormulaVariable("Presence","@Presence@"),
			  new FormulaVariable("Presence Late","@PresenceLate@"),
			  new FormulaVariable("Presence Not Late","@PresenceNotLate@"),
			  new FormulaVariable("Field Visit","@FieldVisit@"),
			  new FormulaVariable("Overtime","@Overtime@"),
			  new FormulaVariable("Time Sheet","@TimeSheet@"),
			  new FormulaVariable("Receivables", "@Receivables@")

	  };
	final TokenParser varsParser;
	final TokenParser itemParser; 
	Connection conn;
	public PayrollTokenParser(Connection conn)
	{
		this.conn=conn;
		varsParser =  new FormulaVarsParser(formulaVariables,null);
		itemParser = new PayrollItemParser(conn,varsParser);
	}
	public FormulaToken parseToken(String str) {
		return itemParser.parseToken(str);
	}
}
