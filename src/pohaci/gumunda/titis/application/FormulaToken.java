/**
 * 
 */
package pohaci.gumunda.titis.application;
/**
 * Refactored from pohaci's FormulaBuilder.
 * @author Yudhi Widyatama
 *
 */
public class FormulaToken {
    int kind; //jenis 
    int itemIdx;//index dari payroll
    protected String code;// yg di simpen d DB
    protected String view;//  yg d tampilin
    boolean withSepPrefix=false;
    public static FormulaToken createComma()
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.COMMA;
      r.code = ",";
      r.view = ",";
      return r;
    }
    public static FormulaToken createNumber(String num)
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.NUMBER;
      r.code = " " + num;
      r.view = " " + num;
      r.withSepPrefix = true;
      return r;
    }
    public static FormulaToken createProcent()
    {
    	FormulaToken r = new FormulaToken();
    	r.kind = FormulaEntity.PROCENT;
    	r.code = "%";
    	r.view = "%";
    	r.withSepPrefix = false;
    	return r;
    }
    public static FormulaToken createNumberNoPrefix(String num)
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.NUMBER;
      r.code = num;
      r.view = num;
      r.withSepPrefix = false;
      return r;
    }
    public static FormulaToken createOperator(String operator)
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.OPERATOR;
      r.code = " " + operator;
      r.view = " " + operator;
      r.withSepPrefix = true;
      return r;
    }
    public static FormulaToken createOpenParent(boolean withSeparatorPrefix)
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.OPEN;
      r.code = "(";
      r.view = "(";
      r.withSepPrefix = withSeparatorPrefix;
      r.addPrefixIfNeeded();
      return r;
    }
    public static FormulaToken createCloseParent(boolean withSeparatorPrefix)
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.CLOSE;
      r.code = ")";
      r.view = ")";
      r.withSepPrefix = withSeparatorPrefix;
      r.addPrefixIfNeeded();
      return r;
    }
    public static FormulaToken createVariable(FormulaVariable formulaVariable,boolean withSeparatorPrefix)
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.VARIABLE;
      r.code = formulaVariable.formulaCode;
      r.view = formulaVariable.varName;
      r.withSepPrefix = withSeparatorPrefix;
      r.addPrefixIfNeeded();
      return r;
    }
    public static FormulaToken createItem(int itemIdx, String itemName, boolean withSeparatorPrefix)
    {
      FormulaToken r = new FormulaToken();
      r.kind = FormulaEntity.ITEM;
      r.code = FormulaHelper.ITEM + itemIdx + FormulaHelper.ITEM;
      r.view = itemName;
      r.withSepPrefix = withSeparatorPrefix;
      r.itemIdx = itemIdx;
      r.addPrefixIfNeeded();
      return r;
    }
    private void addPrefixIfNeeded() {
      if (withSepPrefix)
      {
        code = " " + code;
        view = " " + view;
      }
    }
  }