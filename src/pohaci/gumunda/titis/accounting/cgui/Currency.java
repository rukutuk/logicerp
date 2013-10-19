package pohaci.gumunda.titis.accounting.cgui;

import pohaci.gumunda.titis.accounting.logic.MoneyTalk;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class Currency implements Comparable {
	public static final short SYMBOL = 0;
	public static final short CODE = 1;
	public static final short DESCRIPTION = 2;

	protected long m_index = 0;
	protected String m_symbol = "";
	protected String m_code = "";
	protected String m_description = "";
	protected short m_status = -1;
	//Tambahan dari cok gung 22 Mei 2007
	protected boolean isBase = false; // 1 berarti sama dengan base currency, 0 berart ndak sama
	protected String m_say = "";
	protected String m_cent = "";
	protected String m_language = "";

	public Currency(String symbol, String code, String description, String say,
			String cent, String language) {
		m_symbol = symbol;
		m_code = code;
		m_description = description;
		m_say = say;
		m_cent = cent;
		m_language = language;
	}

	public Currency(long index, String symbol, String code, String description,
			String say, String cent, String language) {
		m_index = index;
		m_symbol = symbol;
		m_code = code;
		m_description = description;
		m_say = say;
		m_cent = cent;
		m_language = language;
	}

	public Currency(long index, Currency currency) {
		m_index = index;
		m_symbol = currency.getSymbol();
		m_code = currency.getCode();
		m_description = currency.getDescription();
		isBase = currency.isBase;
		m_say = currency.getSay();
		m_cent = currency.getCent();
		m_language = currency.getLanguage();
	}

	public String getLanguage() {
		return m_language;
	}

	public String getCent() {
		// TODO Auto-generated method stub
		return m_cent;
	}

	public Currency(Currency currency, short status) {
		this(currency.getIndex(), currency);
		m_status = status;
	}

	public long getIndex() {
		return m_index;
	}

	public String getSymbol() {
		return m_symbol;
	}

	public String getCode() {
		return m_code;
	}

	public String getDescription() {
		return m_description;
	}

	public String getSay() {
		return m_say;
	}

	public String toString() {
		if (m_status == CODE)
			return m_code;
		else if (m_status == DESCRIPTION)
			return m_symbol + " (" + m_description + ")";
		return m_symbol;
	}

	public int compareTo(Object o) {
		if (o instanceof Currency) {
			Currency oc = (Currency) o;
			return m_code.compareTo(oc.m_code);
		}
		return 0;
	}

	//Tambahan dari cok gung 22 Mei 2007
	public void setIsBase(boolean stat) {
		isBase = stat;
	}

	public boolean getIsBase() {
		return isBase;
	}

	private static Currency m_nullObject;

	public static Currency nullObject() {
		if (m_nullObject == null) {
			m_nullObject = new Currency("??", "[-]", "-", "-", "-", MoneyTalk.LANGUAGE_INDONESIAN);
		}
		return m_nullObject;
	}
}