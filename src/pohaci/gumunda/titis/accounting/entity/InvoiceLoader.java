package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.SearchInvoiceDialog.SearchTable;

public interface InvoiceLoader {
	Object[] find(String criterion);

	String getCriterion(SearchTable table,String operator);

}
