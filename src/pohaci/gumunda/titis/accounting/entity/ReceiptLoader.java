package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.SearchPurchaseReceiptDialog.SearchTable;

public interface ReceiptLoader {
	Object[] find(String criterion);
	String getCriterion(SearchTable table,String operator);
}
