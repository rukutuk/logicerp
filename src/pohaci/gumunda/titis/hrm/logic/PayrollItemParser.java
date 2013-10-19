/**
 * 
 */
package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;
import java.sql.SQLException;

import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaHelper;
import pohaci.gumunda.titis.application.FormulaToken;
//import pohaci.gumunda.titis.application.FormulaBuilder.TokenParser;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;

/**
 * Parses a string, replaces it with a PayrollComponent token if it
 * matches the special format given in FormulaHelper.ITEM such as:
 * <BR>
 * [ITEM]10[ITEM]<BR>
 * where [ITEM] = FormulaHelper.ITEM<BR>
 * If there is no match, delegates the task to the inner TokenParser given in the constructor.
 * If there is none, returns null
 * @author Yudhi Widyatama
 *
 */
public class PayrollItemParser implements Formula.TokenParser {
	IHRMSQL isql = new HRMSQLSAP();

	Formula.TokenParser inner;

	Connection conn;

	public PayrollItemParser(Connection conn, Formula.TokenParser inner) {
		this.inner = inner;
		this.conn = conn;
	}

	//pembersihan "@ITEM@" and extrack formulaToken
	public FormulaToken parseToken(String str) {
		if (str.trim().startsWith(FormulaHelper.ITEM)) {
			String strtoken = str.substring(FormulaHelper.ITEM.length(), str
					.length()- FormulaHelper.ITEM.length());

			PayrollComponent compnent;
			long itemIdx = 0;
			try {
				itemIdx = Long.parseLong(strtoken);
				compnent = isql.getPayrollComponent(itemIdx, conn);
				return FormulaToken.createItem((int) itemIdx, compnent.toString(),
						true);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (itemIdx == 0)
				return FormulaToken.createItem(0, "?", true);
			return FormulaToken.createItem((int) itemIdx, "?" + itemIdx + "?", true);
		}
		if (inner != null)
			return inner.parseToken(str);
		else
			return null;
	}
}