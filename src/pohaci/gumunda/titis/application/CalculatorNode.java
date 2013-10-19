package pohaci.gumunda.titis.application;

/**
 * Node for calculating expressions
 * @author dewax4
 * @version 1.0
 */

public class CalculatorNode {
  protected String m_node = "";
  protected CalculatorNode m_left = null;
  protected CalculatorNode m_right = null;

  public static final String ADDITION = "+";
  public static final String SUBTRACTION = "-";
  public static final String MULTIPLICATION = "*";
  public static final String DIVISION = "/";
  public static final String NUMBER = "number";
  public static final String OPENBRACKET = "(";
  public static final String CLOSEBRACKET = ")";

  public CalculatorNode(String node) {
    m_node = node;
  }

  public void setLeftNode(CalculatorNode node){
    m_left = node;
  }

  public void setRightNode(CalculatorNode node){
    m_right = node;
  }

  public String getNodeValue(){
    return m_node;
  }

  public CalculatorNode getLeftNode(){
    return m_left;
  }

  public CalculatorNode getRightNode(){
    return m_right;
  }

  public static boolean isOperator(String token){
    if(token.equals(SUBTRACTION))
      return true;

    if(token.equals(ADDITION))
      return true;

    if(token.equals(MULTIPLICATION))
      return true;

    if(token.equals(DIVISION))
      return true;

    if(token.equals(OPENBRACKET))
      return true;

    if(token.equals(CLOSEBRACKET))
      return true;

    return false;
  }
}