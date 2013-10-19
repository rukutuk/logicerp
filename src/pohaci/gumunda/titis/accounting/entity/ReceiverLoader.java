package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.SearchReceiptDialog.SearchTable;
import pohaci.gumunda.titis.accounting.cgui.SearchSalesArReceiptDialog.SearchTableArReceive;

public interface ReceiverLoader {
	Object[] find(String criterion);
	
	String getCriterion(SearchTable table,String[] kolom,String operator);
	String getCriterion(SearchTableArReceive table,String[] kolom,String operator);
}
