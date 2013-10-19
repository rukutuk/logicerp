package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.SearchVoucherDialog.SearchTable;

public class NullVoucherLoader implements VoucherLoader {

	public Object[] find(String criterion) {
		return new Object[0];
	}
	
	public String getCriterion(SearchTable table,String operator) {
		return "";
	}

}
