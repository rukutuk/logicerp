package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.SearchExpenseSheetDialog.SearchTable;

public interface SheetLoader {
	Object[] find(String criterion);
	
	String getCriterion(SearchTable table,String operator);
}
