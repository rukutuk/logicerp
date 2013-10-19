package pohaci.gumunda.titis.accounting.entity;


import pohaci.gumunda.titis.accounting.cgui.SearchIOweYouDialog.SearchTable;

public interface IOweULoader {

	Object[] find(String criterion);

	String getCriterion(SearchTable table,String operator);

}