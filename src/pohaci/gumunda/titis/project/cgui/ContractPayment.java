package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public class ContractPayment {
  protected String m_description = "";
  protected double m_value = 0.0;
  protected float m_completion = 0.0f;

  public ContractPayment(String description, double value, float completion) {
    m_description = description;
    m_value = value;
    m_completion = completion;
  }

  public String getDescription() {
    return m_description;
  }

  public double getValue() {
    return m_value;
  }

  public float getCompletion() {
    return m_completion;
  }

  public String toString() {
    return m_description;
  }
}