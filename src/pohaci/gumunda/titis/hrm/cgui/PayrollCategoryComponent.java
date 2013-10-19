package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;

public class PayrollCategoryComponent {
  protected long m_index = 0;
  protected PayrollComponent m_component = null;
  protected FormulaEntity m_formulaEntity = null;

  public PayrollCategoryComponent(PayrollComponent component, FormulaEntity formulaEntity) {
    m_component = component;
    m_formulaEntity = formulaEntity;
  }

  public PayrollCategoryComponent(long index, PayrollComponent component, FormulaEntity formulaEntity) {
    m_index = index;
    m_component = component;
    m_formulaEntity = formulaEntity;
  }
  
  public long getIndex() {
    return m_index;
  }

  public PayrollComponent getPayrollComponent() {
    return m_component;
  }

  public FormulaEntity getFormulaEntity() {
    return m_formulaEntity;
  }

  public short getEveryWhichMonth()
  {
  	return m_formulaEntity.getEveryWhichMonth();
  }

  public String toString() {
    return new PayrollComponent(m_component, PayrollComponent.DESCRIPTION).toString();
  }
  
  public NumberRounding getNumberRounding() {
	  return m_formulaEntity.getNumberRounding();
  }

/**
 * @return
 */
  public String getWhichMonth() {
	switch (getEveryWhichMonth())
	{
	case 0: return "All";
	case 1: return "Jan";
	case 2: return "Feb";
	case 3: return "Mar";
	case 4: return "Apr";
	case 5: return "May";
	case 6: return "Jun";
	case 7: return "Jul";
	case 8: return "Aug";
	case 9: return "Sep";
	case 10: return "Oct";
	case 11: return "Nov";
	case 12: return "Dec";	
	default: return "?";
	}
	
  }
}