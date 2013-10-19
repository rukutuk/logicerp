package pohaci.gumunda.titis.accounting.entity;

//import pohaci.gumunda.titis.accounting.cgui.SearchVoucherFrame.SearchTable;

import pohaci.gumunda.titis.accounting.cgui.SearchVoucherDialog.SearchTable;

public interface VoucherLoader {

	Object[] find(String criterion);

	String getCriterion(SearchTable table,String operator);

	//String getCriterion(pohaci.gumunda.titis.accounting.cgui.SearchVoucherFrame.SearchTable table,String operator);

}
