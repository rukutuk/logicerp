package pohaci.gumunda.titis.accounting.entity;
import pohaci.gumunda.titis.accounting.cgui.SearchMemorialJournalDialog.SearchTable;;
public interface JournalStandarLoader {
	Object[] find(String criterion);
	
	String getCriterion(SearchTable table,String operator);
	
}
