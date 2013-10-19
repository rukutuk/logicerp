package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.accounting.cgui.Journal;

/**
 * @author dark-knight
 *
 */
public class TrialBalanceJournalTypeSetting {
	private long index;
	private Journal journal;
	private String columnName;
	private boolean used;
	
	public static final String BEGINNING_BALANCE = "BEGINNING BALANCE";
	public static final String TRANSACTION = "TRANSACTION";
	public static final String UNADJUSTED = "UNADJUSTED BALANCE";
	public static final String ADJUSTMENT = "ADJUSTMENTS";
	public static final String ADJUSTED = "ADJUSTED BALANCE";
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Journal getJournal() {
		return journal;
	}
	public void setJournal(Journal journal) {
		this.journal = journal;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
}
