/**
 *
 */
package pohaci.gumunda.titis.application;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;

/**
 * A formula receive calls from the GUI when the user pressed one of the
 * calculator's buttons. The formula constructs a FormulaEntity based on these
 * calls. The formula also capable to build itself from its representative
 * string stored in the database (see parseFormula)
 *
 * @author Pohaci & Yudhi Widyatama
 *
 */

public class Formula extends Observable {
	public static interface TokenParser {
		FormulaToken parseToken(String str);
	}

	FormulaEntity formulaEntity = new FormulaEntity("");

	public Vector m_vformula = new Vector();

	public String m_formulacode = "", m_formulaText = "";

	public Vector tokens = new Vector();

	private void addFormulaToken(FormulaToken t) {
		this.m_formulacode = this.m_formulacode + t.code;
		this.m_vformula.addElement(new Integer(t.kind));
		setFormulaText(m_formulaText + t.view);
		tokens.addElement(t);
	}

	private void setFormulacode(String m_formulacode) {
		this.m_formulacode = m_formulacode;
	}

	public String getFormulacode() {
		return this.m_formulacode;
	}

	private Vector getVformula() {
		return this.m_vformula;
	}

	public String getFormulaText() {
		return m_formulaText;
	}

	private void setFormulaText(String string) {
		m_formulaText = string;
		this.setChanged();
		this.notifyObservers();
	}

	public void addOperator(String stroperator) {
		if (this.m_vformula.size() == 0)
			return;

		int ilast = getLastElementOfFormula();
		if (ilast == FormulaEntity.NUMBER || ilast == FormulaEntity.CLOSE
				|| ilast == FormulaEntity.PROCENT
				|| ilast == FormulaEntity.ITEM
				|| ilast == FormulaEntity.VARIABLE) {
			addFormulaToken(FormulaToken.createOperator(stroperator));
		}
	}

	public void addProcent() {
		if (this.m_vformula.size() == 0)
			return;

		int ilast = getLastElementOfFormula();
		if (ilast == FormulaEntity.NUMBER || ilast == FormulaEntity.CLOSE) {
			addFormulaToken(FormulaToken.createProcent());
		}
	}

	public int getLastElementOfFormula() {
		if(m_vformula.size()==0)
			return -1;
		Integer last = (Integer) this.m_vformula.lastElement();

		int ilast = last.intValue();
		return ilast;
	}

	public void addNumber(String strnumber) {
		Integer last = null;
		if (this.m_vformula.size() > 0)
			last = (Integer) this.m_vformula.lastElement();

		int ilast;
		if (last == null)
			ilast = FormulaEntity.NUMBER;
		else
			ilast = last.intValue();

		if (ilast == FormulaEntity.NUMBER || ilast == FormulaEntity.COMMA
				|| ilast == FormulaEntity.OPERATOR
				|| ilast == FormulaEntity.OPEN) {
			if (ilast == FormulaEntity.NUMBER || ilast == FormulaEntity.COMMA) {
				addFormulaToken(FormulaToken.createNumberNoPrefix(strnumber));
			} else {
				addFormulaToken(FormulaToken.createNumber(strnumber));
			}
		}
	}

	public void addComma() {
		if (this.m_vformula.size() == 0)
			return;

		Integer last = (Integer) getVformula().lastElement();

		int ilast = last.intValue();
		if (ilast == FormulaEntity.NUMBER) {
			addFormulaToken(FormulaToken.createComma());
		}
		return;
	}

	public void parseFormula(FormulaEntity m_formula,
			Formula.TokenParser additionalParser) {
		parseFormula(m_formula.getFormulaCode(),additionalParser);
	}

	public void reset() {
		setFormulacode("");
		setFormulaText("");
		getVformula().clear();
		tokens.clear();
	}

	public void addOpeningParentheses() {
		Integer last = null;
		if (this.m_vformula.size() > 0)
			last = (Integer) this.m_vformula.lastElement();

		int ilast = -1;

		if (last == null)
			addFormulaToken(FormulaToken.createOpenParent(false));
		else {
			ilast = last.intValue();
			if (ilast == FormulaEntity.OPERATOR)
				addFormulaToken(FormulaToken.createOpenParent(true));
		}
		return;
	}

	public void addClosingParentheses() {
		if (getVformula().size() == 0)
			return;

		Integer last = (Integer) getVformula().lastElement();

		int ilast = last.intValue();

		if (ilast == FormulaEntity.NUMBER || ilast == FormulaEntity.ITEM
				|| ilast == FormulaEntity.VARIABLE) {
			addFormulaToken(FormulaToken.createCloseParent(true));
		}
	}

	public void addItem(int idx, String label) {
		Integer last = null;
		if (getVformula().size() > 0)
			last = (Integer) getVformula().lastElement();

		int ilast = -1;
		if (last != null)
			ilast = last.intValue();

		if (last == null || ilast == FormulaEntity.OPERATOR
				|| ilast == FormulaEntity.OPEN) {
			addFormulaToken(FormulaToken.createItem(idx, label, last != null));
		}
	}

	public void addFormulaVariable(FormulaVariable f) {
		Integer last = null;
		if (getVformula().size() > 0)
			last = (Integer) getVformula().lastElement();

		int ilast = -1;
		if (last != null)
			ilast = last.intValue();

		if (last == null || ilast == FormulaEntity.OPERATOR
				|| ilast == FormulaEntity.OPEN) {
			addFormulaToken(FormulaToken.createVariable(f, last != null));
		}
	}

	/*
	 * variasi B public void deleteElement(JList theList) {
	 * getVformula().removeElementAt(getVformula().size() - 1);
	 * tokens.removeElementAt(tokens.size()-1); // remove last element
	 * rebuildFromTokens(); }
	 */
	public void deleteElement() { /* variasi C */
		getVformula().removeElementAt(getVformula().size() - 1);
		FormulaToken delToken = (FormulaToken) tokens.get(tokens.size() - 1); // remove
																				// last
																				// element
		tokens.removeElementAt(tokens.size() - 1);
		int delViewLen = delToken.view.length();
		int viewLen = m_formulaText.length();
		int delCodeLen = delToken.code.length();
		int codeLen = m_formulacode.length();
		setFormulaText(m_formulaText.substring(0, viewLen - delViewLen));
		setFormulacode(m_formulacode.substring(0, codeLen - delCodeLen));
	}

	/*
	 * private void rebuildFromTokens() { StringBuffer newFormula=new
	 * StringBuffer(); StringBuffer newCode = new StringBuffer();
	 * this.m_vformula.clear(); for (int i=0; i<tokens.size(); i++) { Token t =
	 * (Token)tokens.get(i); newFormula.append(t.view); newCode.append(t.code);
	 * m_vformula.addElement(new Integer(t.kind)); }
	 * setFormulaText(newFormula.toString());
	 * setFormulacode(newCode.toString()); }
	 */
	/*
	 * variasi A public void deleteElement(JList theList) { FormulaBuilder
	 * builder = this; builder.setFormulacode(builder.getFormulacode().trim());
	 *
	 * if(builder.getFormulacode().length() > 0){ int len = 1; String label =
	 * ""; Integer last = (Integer)builder.getVformula().lastElement();
	 *
	 * if(last.intValue() == PayrollCategoryFormulaDlg.ITEM) { String item = "";
	 * DefaultListModel model = (DefaultListModel)theList.getModel(); for(int i =
	 * 0; i < model.getSize(); i ++) { PayrollComponent component =
	 * (PayrollComponent)model.elementAt(i); item = FormulaHelper.ITEM +
	 * String.valueOf(component.getIndex()) + FormulaHelper.ITEM;
	 * if(builder.getFormulacode().endsWith(item)){ label =
	 * component.toString(); break; } } len = item.length(); }
	 *
	 * builder.setFormulacode(builder.getFormulacode().substring(0,
	 * builder.getFormulacode().length() - len));
	 *
	 * String view = builder.getFormulaText(); int itemindex =
	 * view.indexOf(label); if(itemindex != -1 && !label.equals("")) view =
	 * view.substring(0, itemindex); else view = view.substring(0, view.length() -
	 * len);
	 *
	 * builder.setFormulaText(view.trim());
	 * builder.getVformula().removeElementAt(builder.getVformula().size() - 1); } }
	 */
	public boolean isNumber(String token) {
		char ctoken[] = token.toCharArray();
		for (int i = 0; i < ctoken.length; i++) {
			if (!Character.isDigit(ctoken[i]) && ctoken[i] != '.')
				return false;
		}
		return true;
	}

	public FormulaEntity createFormulaEntity(short whichMonth,
			NumberRounding numberRounding) {
		FormulaEntity formulaEntity = new FormulaEntity(this.m_formulacode
				.replaceAll(",", "."), m_formulaText.trim(), whichMonth,
				numberRounding);
		formulaEntity.setFormulaStatus(this.m_vformula);
		return formulaEntity;
	}

	// extrack formula from FormulaEntity
	public void parseFormula(String input, Formula.TokenParser additionalParser) {
		reset();
		java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(
				input);
		int tokencount = tokenizer.countTokens();
		if (tokencount > 0) {
			for (int i = 0; i < tokencount; i++) {
				String strtoken = tokenizer.nextToken();
				FormulaToken t;
				t = additionalParser.parseToken(strtoken);
				if (t != null)
					addFormulaToken(t);
				else {
					if (isNumber(strtoken.trim()))
						strtoken = strtoken.replace('.', ',');
					if (strtoken.trim().length() > 0) {
						char[] chars = strtoken.trim().toCharArray();
						// identification of each character from token
						for (int ii = 0; ii < chars.length; ii++) {
							char c = chars[ii];
							if (c == ',')
								addComma();
							else if (c == '(')
								addOpeningParentheses();
							else if (c == ')')
								addClosingParentheses();
							else if ((c == '*') || (c == '+') || (c == '-')
									|| (c == '/'))
								addOperator(String.valueOf(c));
							else if (c == '%')
								addProcent();
							else
								addNumber(String.valueOf(c));
						}
					}
				}
			}
		}
	}

	// extrack formula from FormulaEntity for time sheet rate
	public void parseFormulaForTimeSheetRate(String input, Formula.TokenParser additionalParser) {
		reset();
		java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(
				input);
		int tokencount = tokenizer.countTokens();
		if (tokencount > 0) {
			for (int i = 0; i < tokencount; i++) {
				String strtoken = tokenizer.nextToken();
				FormulaToken t;
				if (strtoken.equalsIgnoreCase("@TimeSheet@")){
					strtoken = "1";
				}
				t = additionalParser.parseToken(strtoken);
				if (t != null)
					addFormulaToken(t);
				else {
					if (isNumber(strtoken.trim()))
						strtoken = strtoken.replace('.', ',');
					if (strtoken.trim().length() > 0) {
						char[] chars = strtoken.trim().toCharArray();
						// identification of each character from token
						for (int ii = 0; ii < chars.length; ii++) {
							char c = chars[ii];
							if (c == ',')
								addComma();
							else if (c == '(')
								addOpeningParentheses();
							else if (c == ')')
								addClosingParentheses();
							else if ((c == '*') || (c == '+') || (c == '-')
									|| (c == '/'))
								addOperator(String.valueOf(c));
							else if (c == '%')
								addProcent();
							else
								addNumber(String.valueOf(c));
						}
					}
				}
			}
		}
	}

	/**
	 * Reads which variables referenced from this formula
	 *
	 * @return
	 */
	public String[] readVars() {
		int i;
		ArrayList a = new ArrayList();
		for (i = 0; i < tokens.size(); i++) {
			FormulaToken t = (FormulaToken) tokens.get(i);
			if (t.kind == FormulaEntity.VARIABLE)
				a.add(t.code);
		}
		return (String[]) a.toArray(new String[0]);
	}

	/**
	 * Reads which special item's indexes referenced from this formula
	 *
	 * @return
	 */

	public Integer[] readItems() {
		int i;
		ArrayList a = new ArrayList();
		for (i = 0; i < tokens.size(); i++) {
			FormulaToken t = (FormulaToken) tokens.get(i);
			if (t.kind == FormulaEntity.ITEM)
				a.add(new Integer(t.itemIdx));
		}
		return (Integer[]) a.toArray(new Integer[0]);
	}

	/**
	 * evaluates the formula with the variable and special items substituted.
	 *
	 * @param variables
	 *            A map (hashtable) with variable name (ex: "@Presence@") as key
	 *            and variable's value as value. Both are strings.
	 * @param items
	 *            A map (hashtable) with Integer key and String value
	 * @return double-precision floating point value.
	 */
	public double evaluate(Hashtable variables, Hashtable items) {
		int i;
		StringBuffer expr = new StringBuffer();
		for (i = 0; i < tokens.size(); i++) {
			FormulaToken t = (FormulaToken) tokens.get(i);
			if (t.kind == FormulaEntity.COMMA) {
				expr.append(t.code.replace(',', '.'));
			} else if (t.kind == FormulaEntity.ITEM) {
				Integer key = new Integer(t.itemIdx);
				//String key = t.code.trim();
				String subt;
				if (!items.containsKey(key)) {
					System.err.println("FormulaBuilder: Special item index "+ key + " not found in context, returning 0");
					subt = "0";
					//throw new RuntimeException("Special item index " + key +" not found in context");
				}else{
					subt = (String) items.get(key);
				}
				if (t.withSepPrefix){
					expr.append(' ');
				}
				expr.append(subt);
			} else if (t.kind == FormulaEntity.VARIABLE) {
				String keyStr = t.code.trim();
				if (!variables.containsKey(keyStr)) {
					throw new RuntimeException("Variable " + keyStr
							+ " not found in context");
				}
				String subt = (String) variables.get(keyStr);
				if (t.withSepPrefix)
					expr.append(' ');
				expr.append(subt);
			} else if (t.kind == FormulaEntity.PROCENT) {
				expr.append(" * 0.01");
			} else
				expr.append(t.code);
		}
		try {
			return new SimpleCalculator(expr.toString()).execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}