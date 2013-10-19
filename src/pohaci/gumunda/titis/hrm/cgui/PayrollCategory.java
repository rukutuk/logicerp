package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class PayrollCategory {
	protected long m_index = 0;
	protected String m_name = "";
	protected String m_description = "";
	private long newIndex = 0;

	public PayrollCategory(String name, String description) {
		m_name = name;
		m_description = description;
	}

	public PayrollCategory(long index, String name, String description) {
		m_index = index;
		m_name = name;
		m_description = description;
	}

	public PayrollCategory(long index, PayrollCategory category) {
		m_index = index;
		m_name = category.getName();
		m_description = category.getDescription();
	}

	public long getIndex() {
		return m_index;
	}

	public String getName() {
		return m_name;
	}

	public String getDescription() {
		return m_description;
	}

	public String toString() {
		return getName();
	}

	public void setIndex(long index) {
		this.m_index = index;
	}

	public long getNewIndex() {
		return newIndex;
	}

	public void setNewIndex(long newIndex) {
		this.newIndex = newIndex;
	}
}