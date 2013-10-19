package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.Vector;


/**
 * FormulaEntity contains two separate String, one for viewing and the other for calculation purposes.
 * @author Pohaci
 *
 */

public class FormulaEntity {
  public static final int VARIABLE = 50;
  String m_code = "";
  String m_view = "";
  Vector m_vector = new Vector();
  double m_value = 0.0;
  short m_every;
  public static int OPERATOR = 0;
  public static int OPEN = 1;
  public static int CLOSE = 2;
  public static int COMMA = 3;
  public static int NUMBER = 4;
  public static int ITEM = 5;
  public static int PROCENT = 6;
    
  NumberRounding m_numberRounding = new NumberRounding(Short.parseShort("-1"), 0);
  
  public FormulaEntity(String code, String view, short whichMonth, NumberRounding numberRounding) {
    m_code = code;
    m_view = view;
    m_every = whichMonth;
    m_numberRounding = numberRounding;
  }
  
  public FormulaEntity(String code) {
    m_code = code;
  }

  public void setFormulaCode(String code) {
    m_code = code;
  }

  public void setFormulaView(String view) {
    m_view = view;
  }

  public String getFormulaCode() {
    return m_code;
  }

  public String getFormulaView() {
    return m_view;
  }

  public void setFormulaStatus(Vector vector) {
    m_vector = vector;
  }

  public Vector getFormulaStatus() {
    return m_vector;
  }

  public double getFormulaValue() {
    return m_value;
  }

  public String toString() {
    return m_view;
  }
  
  public short getEveryWhichMonth() {
	return m_every;
  }
  
  public NumberRounding getNumberRounding() {
	  return m_numberRounding;
  }
}