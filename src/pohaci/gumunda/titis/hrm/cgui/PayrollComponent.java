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

public class PayrollComponent {
  public static final String NONE = "";
  public static final String PAYCHEQUE = "Paycheque";
  public static final String NON_PAYCHEQUE = "Non Paycheque";
  public static final String NON_PAYMENT = "Non Payment";
  public static String m_payments[] = new String[]{
    PAYCHEQUE, NON_PAYCHEQUE, NON_PAYMENT
  };

  public static final String FIELD_ALLOWANCE = "Field Allowance";
  public static final String MEAL_ALLOWANCE = "Meal Allowance";
  public static final String EMPLOYEE_RECEIVABLES = "Employee Receivables";
  public static final String TRANSPORTION_ALLOWANCE = "Transportation Allowance";
  public static final String OVERTIME = "Overtime";
  public static final String INSURANCE_ALLOWANCE = "Insurance Allowance";
  public static final String OTHER_ALLOWANCE = "Other Allowance";
  public static String m_submits[] = new String[]{
    PAYCHEQUE, FIELD_ALLOWANCE, EMPLOYEE_RECEIVABLES, MEAL_ALLOWANCE,
    TRANSPORTION_ALLOWANCE, OVERTIME, INSURANCE_ALLOWANCE, OTHER_ALLOWANCE
  };

  public static final short CODE = 0;
  public static final short DESCRIPTION = 1;
  public static final short ACCOUNT = 2;

  protected long m_index = 0;
  protected String m_code = "";
  protected String m_description = "";
  protected boolean m_isgroup = false;
  protected Account m_account = null;
  protected short m_payment = -1;
  protected short m_submit = -1;
  protected PaychequeLabel m_label = null;
  protected String m_reportLabel = "";

  protected PayrollComponent m_parent = null;
  protected short m_status = -1;

  public PayrollComponent(String code, String description, boolean isgroup, Account account,
                          short payment, short submit, PaychequeLabel label, String reportLabel) {
    m_code = code;
    m_description = description;
    m_isgroup = isgroup;
    m_account = account;
    m_payment = payment;
    m_submit = submit;
    m_label = label;
    m_reportLabel = reportLabel;
  }

  public PayrollComponent(long index, String code, String description, boolean isgroup, Account account,
                          short payment, short submit, PaychequeLabel label, String reportLabel) {
    m_index = index;
    m_code = code;
    m_description = description;
    m_isgroup = isgroup;
    m_account = account;
    m_payment = payment;
    m_submit = submit;
    m_label = label;
    m_reportLabel = reportLabel;
  }

  public PayrollComponent(long index, PayrollComponent component) {
    m_index = index;
    m_code = component.getCode();
    m_description = component.getDescription();
    m_isgroup = component.isGroup();
    m_account = component.getAccount();
    m_payment = component.getPayment();
    m_submit = component.getSubmit();
    m_label = component.getPaychequeLabel();
    m_reportLabel = component.getReportLabel();
  }

  public String getReportLabel() {
	return m_reportLabel;
}

public PayrollComponent(PayrollComponent component, short status) {
    this(component.getIndex(), component);
    m_status = status;
  }

  public long getIndex() {
    return m_index;
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
    return m_account;
  }

  public short getPayment() {
    return m_payment;
  }

  public String getPaymentAsString(){
    if(m_payment < 0 || m_payment >= m_payments.length)
      return "";
    else
      return m_payments[m_payment];
  }

  public static short paymentFromStringToID(String payment){
    short len = (short)m_payments.length;
    for(short i = 0; i < len; i++){
      if(m_payments[i].equals(payment))
        return i;
    }

    return -1;
  }

  public short getSubmit() {
    return m_submit;
  }

  public String getSubmitAsString(){
    if(m_submit < 0 || m_submit >= m_submits.length)
      return "";
    else
      return m_submits[m_submit];
  }

  public static short submitFromStringToID(String submit){
    short len = (short)m_submits.length;
    for(short i = 0; i < len; i++){
      if(m_submits[i].equals(submit))
        return i;
    }

    return -1;
  }

  public PaychequeLabel getPaychequeLabel() {
    return m_label;
  }

  public void setParent(PayrollComponent component) {
    m_parent = component;
  }

  public PayrollComponent getParent() {
    return m_parent;
  }

  public String toString() {
    if(m_status == CODE)
      return m_code;
    else if(m_status == DESCRIPTION)
      return m_description;
    else {
      if(getAccount() != null)
        return m_code + " " + m_description + " [" + getAccount().getCode() + "]";
      return m_code + " " + m_description;
    }
  }
}
