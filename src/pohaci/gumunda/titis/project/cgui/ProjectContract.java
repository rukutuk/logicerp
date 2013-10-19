package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.Date;
import pohaci.gumunda.titis.accounting.cgui.Currency;

public class ProjectContract {
    protected long m_index = -1;
  protected Date m_eststart = null;
  protected Date m_estend = null;
  protected Date m_actstart = null;
  protected Date m_actend = null;
  protected double m_value = 0.0;
  protected Currency m_currency = null;
  protected boolean  m_ppn = false;
  protected Date m_validation = null;
  protected String m_description = "";
  protected String m_file = "";
  protected byte[] m_sheet = null;
  protected long m_curr;

  protected ContractPayment[] m_payment = new ContractPayment[0];

  public ProjectContract(Date eststart, Date estend, Date actstart, Date actend,
                         double value, Currency currency, boolean ppn, Date Validation,
    String description, String file, byte[] sheet) {
    m_eststart = eststart;
    m_estend = estend;
    m_actstart = actstart;
    m_actend = actend;
    m_value = value;
    m_currency = currency;
    m_ppn = ppn;
    m_validation = Validation;
    m_description = description;
    m_file = file;
    m_sheet = sheet;
  }

  // konstruktor yang dipanggil pada project
  public ProjectContract(long index, Date start, Date end, Date actstart, Date actend,
                         double value, long curr, boolean ppn, Date Validation,
    String description, String file, byte[] sheet) {
    m_index = index;
    m_eststart = start;
    m_estend = end;
    m_actstart = actstart;
    m_actend = actend;
    m_value = value;
    //m_currency = currency;
    m_curr = curr;
    m_ppn = ppn;
    m_validation = Validation;
    m_description = description;
    m_file = file;
    m_sheet = sheet;
  }
  
  // digunain di accounting, by i
  public ProjectContract(Date eststart, Date estend, Date actstart, Date actend,
          double value, Currency currency, Date validation,
          String description) {
	  	m_eststart = eststart;
	    m_estend = estend;
	    m_actstart = actstart;
	    m_actend = actend;
	    m_value = value;
	    m_currency = currency;
	    m_validation = validation;
	    m_description = description;
  }

  public ProjectContract(long index, ProjectContract project) {
    m_index = index;
    m_eststart = project.getEstimateStartDate();
    m_estend = project.getEstimateEndDate();
    m_actstart = project.getActualStartDate();
    m_actend = project.getActualEndDate();
    m_value = project.getValue();
    m_currency = project.getCurrency();
    m_ppn = project.getPPN();
    m_validation = project.getValidation();
    m_description = project.getDescription();
    m_file = project.getFile();
    m_sheet = project.getSheet();
    m_payment = project.getContractPayment();
  }

  // method tambahan nunung
  public long getCurr(){
      return m_curr;
  }
  
  public long getIndex() {
    return m_index;
  }

  public Date getEstimateStartDate() {
    return m_eststart;
  }

  public Date getEstimateEndDate() {
    return m_estend;
  }

  public Date getActualStartDate() {
    return m_actstart;
  }

  public Date getActualEndDate() {
    return m_actend;
  }


  public double getValue() {
    return m_value;
  }

  public Currency getCurrency() {
    return m_currency;
  }

  public boolean getPPN() {
    return m_ppn;
  }

  public Date getValidation() {
    return m_validation;
  }

  public String getDescription() {
    return m_description;
  }

  public String getFile() {
    return m_file;
  }

  public byte[] getSheet() {
    return m_sheet;
  }

  public void setContractPayment(ContractPayment[] payment) {
    m_payment = payment;
  }

  public ContractPayment[] getContractPayment() {
    return m_payment;
  }
}