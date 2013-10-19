/**
 * 
 */
package pohaci.gumunda.titis.accounting.entity;

/**
 * @author dark-knight
 *
 */
public class TrialBalanceAccountTypeSetting {
	private long index;
	private long category;
	private String columnName;
	private boolean used;
	
	public static final String BEGINNING_BALANCE = "BEGINNING BALANCE";
	public static final String TRANSACTION = "TRANSACTION";
	public static final String UNADJUSTED = "UNADJUSTED BALANCE";
	public static final String ADJUSTMENT = "ADJUSTMENTS";
	public static final String ADJUSTED = "ADJUSTED BALANCE";
	
	public long getCategory() {
		return category;
	}
	public void setCategory(long category) {
		this.category = category;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
}
