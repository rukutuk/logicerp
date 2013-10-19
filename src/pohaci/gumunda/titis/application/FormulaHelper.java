package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.*;

public class FormulaHelper {
 /* public static String NOTOP_PLUS = "@NOTOP_PLUS@";
  public static String NOTOP_MIN = "@NOTOP_MINUS@";
  public static String NOTOP_MUL = "@NOTOP_MUL@";
  public static String NOTOP_DIV = "@NOTOP_DIV@";
  public static String NOTOP_OPENBRACKET = "@NOTOP_OPENBRACKET@";
  public static String NOTOP_CLOSEBRACKET = "@NOTOP_CLOSEBRACKET@";*/
  public static String ITEM = "@I@";

  public static String[] getFormulaItems(String formula){
	  StringTokenizer tokenizer = new StringTokenizer(formula, "+-/*()");
	  Vector vresult = new Vector();
	  while(tokenizer.hasMoreElements()){
		  vresult.addElement(tokenizer.nextToken().trim());
	  }
	  
	  String tokens[] = new String[vresult.size()];
	  vresult.copyInto(tokens);
	  return tokens;
  }
}