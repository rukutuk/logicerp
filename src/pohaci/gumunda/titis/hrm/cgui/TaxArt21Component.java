package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.FormulaEntity;

public class TaxArt21Component {
  

  public long m_index = 0;
  protected String m_code = "";
  protected String m_description = "";
  protected boolean m_isgroup = false;
  protected Account m_taxAccount = null;
  //protected short m_payment = -1;
  //protected short m_submit = -1;
  //protected PaychequeLabel m_label = null;
  protected String m_note;

  protected TaxArt21Component m_parent = null;
  protected boolean rounded = false;
  protected int roundedValue;
  protected int precision;
  protected boolean negative = false;
  protected boolean comparable = false;
  protected String comparationMode = "";
  protected double comparatorValue;
  
  
  private FormulaEntity m_formulaEntity;

  public TaxArt21Component(String code, String description, boolean isgroup, Account taxaccount,
		  FormulaEntity formula, String note, boolean rounded, int roundedValue, int precision,
		  boolean negative, boolean comparable, String comparationMode, double comparatorValue) {
    m_code = code;
    m_description = description;
    m_isgroup = isgroup;
    m_taxAccount = taxaccount;
    m_formulaEntity = formula;
    m_note = note;
    
    this.rounded = rounded;
    this.roundedValue = roundedValue;
    this.precision = precision;
    this.negative = negative;
    this.comparable = comparable;
    this.comparationMode = comparationMode;
    this.comparatorValue = comparatorValue;
  }

  public TaxArt21Component(long index, String code, String description, boolean isgroup, Account taxaccount,
		                    FormulaEntity formula, String note, boolean rounded, int roundedValue, int precision,
		          		  boolean negative, boolean comparable, String comparationMode, double comparatorValue) {
    m_index = index;
    m_code = code;
    m_description = description;
    m_isgroup = isgroup;
    m_taxAccount = taxaccount;
    m_formulaEntity = formula;
    m_note = note;
    
    this.rounded = rounded;
    this.roundedValue = roundedValue;
    this.precision = precision;
    this.negative = negative;
    this.comparable = comparable;
    this.comparationMode = comparationMode;
    this.comparatorValue = comparatorValue;
  }

  public TaxArt21Component(long index, TaxArt21Component component) {
    m_index = index;
    m_code = component.getCode();
    m_description = component.getDescription();
    m_isgroup = component.isGroup();
    m_taxAccount = component.getAccount();
    m_formulaEntity = component.getFormulaEntity();
    m_note = component.getNote();
  }

  public long getIndex() {
    return m_index;
  }
  public void setIndex(long index){
	  m_index=index;
  }
  public String getNote() {
	    return m_note;
  }
  public FormulaEntity getFormulaEntity() {
	    return m_formulaEntity;
  }

  public String getCode() {
    return m_code;
  }

  public String getDescription() {
    return m_description;
  }

  public boolean isGroup() {
    return m_isgroup;
  }

  public Account getAccount() {
    return m_taxAccount;
  }
  
  public void setParent(TaxArt21Component component) {
    m_parent = component;
  }

  public TaxArt21Component getParent() {
    return m_parent;
  }

  public String toString() {
    if(getAccount() != null)
      return m_code + " " + m_description + " [" + getAccount().getCode() + "]";
    return m_code + " " + m_description;
  }

public boolean isComparable() {
	return comparable;
}

public void setComparable(boolean comparable) {
	this.comparable = comparable;
}

public String getComparationMode() {
	return comparationMode;
}

public void setComparationMode(String comparationMode) {
	this.comparationMode = comparationMode;
}

public double getComparatorValue() {
	return comparatorValue;
}

public void setComparatorValue(double comparatorValue) {
	this.comparatorValue = comparatorValue;
}

public boolean isNegative() {
	return negative;
}

public void setNegative(boolean negative) {
	this.negative = negative;
}

public int getPrecision() {
	return precision;
}

public void setPrecision(int precision) {
	this.precision = precision;
}

public boolean isRounded() {
	return rounded;
}

public void setRounded(boolean rounded) {
	this.rounded = rounded;
}

public int getRoundedValue() {
	return roundedValue;
}

public void setRoundedValue(int roundedValue) {
	this.roundedValue = roundedValue;
}
}