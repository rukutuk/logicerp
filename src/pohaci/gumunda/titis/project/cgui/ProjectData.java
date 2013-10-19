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
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class ProjectData {
  protected long m_index = -1;
  protected String m_code = null;
  protected Customer m_customer = null;
  protected Unit m_unit = null;
  protected Activity m_activity = null;
  protected Organization m_department = null;
  protected String m_work = "";
  protected Date m_regdate = null;
  protected String m_orno = "";
  protected String m_pono = "";
  protected String m_ipcno = "";
  protected Date m_ordate = null;
  protected Date m_podate = null;
  protected Date m_ipcdate = null;
  protected String m_file = "";
  protected byte[] m_sheet = null;
  
  // tambahan variable
  protected String m_custname,m_act;
  protected long m_indexcust,m_indexunit,m_indexact,m_indexdept;  
  protected Date m_startdate=null;

  protected java.util.Date m_enddate = null, m_validation=null;
  
  protected ProjectContract m_contract = null;

  public ProjectData(String code, Customer customer, Unit unit, Activity activity,
                     Organization department, String work, Date regdate,
                     String orno, String pono, String ipcno,
                     Date ordate, Date podate, Date ipcdate,
                     String file, byte[] sheet) {	 
    m_code = code;
    m_customer = customer;
    m_unit = unit;
    m_activity = activity;
    m_department = department;
    m_work = work;
    m_regdate = regdate;
    m_orno = orno;
    m_pono = pono;
    m_ipcno = ipcno;
    m_ordate = ordate;
    m_podate = podate;
    m_ipcdate = ipcdate;
    m_file = file;
    m_sheet = sheet;
  }

  public ProjectData(long index, String code, Customer customer, Unit unit, Activity activity,
                     Organization department, String work, Date regdate,
                     String orno, String pono, String ipcno,
                     Date ordate, Date podate, Date ipcdate,
                     String file, byte[] sheet) {	
    m_index = index;
    m_code = code;
    m_customer = customer;
    m_unit = unit;
    m_activity = activity;
    m_department = department;
    m_work = work;
    m_regdate = regdate;
    m_orno = orno;
    m_pono = pono;
    m_ipcno = ipcno;
    m_ordate = ordate;
    m_podate = podate;
    m_ipcdate = ipcdate;
    m_file = file;
    m_sheet = sheet;
  }  
  // project yang nunung tambahkan
  public ProjectData(long index, String code, String custname, String workdesc,String act, 
                     String orno, Date ordate, String pono, Date podate,String ipcno,
                     Date ipcdate, Date startdate, Date enddate,Date validation,Date regdate,
                     String attfile,byte[] sheet, long indexcust,long indexunit,long indexact, long indexdept){	  
	  
	  // yang ini;
	 
      m_index = index;
      m_code = code;
      m_custname = custname;
      m_work = workdesc;
      m_act = act;
      m_orno = orno;
      m_ordate = ordate;
      m_pono = pono;
      m_podate=podate;
      m_ipcno = ipcno;
      m_ipcdate = ipcdate;
      m_startdate = startdate;
      m_enddate = enddate;
      m_validation = validation;   
      m_regdate = regdate;
      m_file = attfile;   
      m_sheet = sheet;
      m_indexcust = indexcust;
      m_indexunit = indexunit;
      m_indexact = indexact;
      m_indexdept = indexdept;
  }
  
  public ProjectData(long index, ProjectData project){   	  
      m_index = index;
      m_code = project.getCode();
      m_custname = project.getCustname();
      m_work = project.getWorkDescription();
      m_act = project.getAct();
      m_orno = project.getORNo();
      m_ordate = project.getORDate();
      m_pono = project.getPONo();
      m_podate=project.getPODate();
      m_ipcno = project.getIPCNo();
      m_ipcdate = project.getIPCDate();
      m_startdate = project.getStartdate();
      m_enddate = project.getEnddate();
      m_validation = project.getValidation();   
      m_regdate = project.getRegDate();
      m_file = project.getFile();      
      m_indexcust = project.getIndexcust();
      m_indexunit = project.getIndexunit();
      m_indexact = project.getIndexact();
      m_indexdept = project.getIndexdept();
  }
//
  public void setIndex(long a){
	  m_index=a;
  }
 // get yang nunung tambahkan    
 public String getCustname(){
	  if(m_customer!=null)
		  return m_customer.getName();
      return m_custname;
  }  
  public String getAct(){
      return m_act;
  }
  public Date getStartdate(){
      return m_startdate;
      // masuk sini
  }
  public Date getEnddate(){
      return m_enddate;
  }
  public Date getValidation(){
      return m_validation;
  }  
  public long getIndexcust(){
      return m_indexcust;
  }
  public long getIndexunit(){
      return m_indexunit;
  }
  public long getIndexact(){
      return m_indexact;
  }
  public long getIndexdept(){
      return m_indexdept;
  }
  
  public long getIndex() {
    return m_index;
  }

  public String getCode() {
    return m_code;
  }

  public Customer getCustomer() {
    return m_customer;
  }
  
  public void setCustomer(Customer cust){
	  m_customer = cust;
  }

  public Unit getUnit() {
    return m_unit;
  }

  public Activity getActivity() {
    return m_activity;
  }

  public Organization getDepartment() {
    return m_department;
  }

  public String getWorkDescription() {
    return m_work;
  }

  public Date getRegDate() {
    return m_regdate;
  }

  public String getORNo() {
    return m_orno;
  }

  public String getPONo() {
    return m_pono;
  }

  public String getIPCNo() {
    return m_ipcno;
  }

  public Date getORDate() {
    return m_ordate;
  }

  public Date getPODate() {
    return m_podate;
  }

  public Date getIPCDate() {
    return m_ipcdate;
  }

  public String getFile() {
    return m_file;
  }

  public byte[] getSheet() {
    return m_sheet;
  }

  public void setProjectContract(ProjectContract contract) {
    m_contract = contract;
  }
  
  public void setUnit(Unit unit){
      m_unit = unit;
  }

  public ProjectContract getProjectContract() {
    return m_contract;
  }

  public String toString() {
    return m_code;
  }
  //Tambahan dari cok gung 22 Mei 2007
  public void setActivity(Activity activity){ 
	  m_activity=activity;
  }
  public void setDepartment(Organization organization){
	  m_department=organization;
  }
  /*
  // tambahan lagi dari i
  public ProjectData(long index, String code, Customer customer,
		  Unit unit, Activity activity, Organization department,
		  String workDescription, Date regDate, String orNo,
		  String poNo, String ipcNo, Date orDate, Date poDate,
		  Date ipcDate){
	  
	  this.m_index = index;
	  this.m_code = code;
	  this.m_customer = customer;
	  this.m_unit = unit;
	  this.m_activity = activity;
	  this.m_department = department;
	  this.m_work = workDescription;
	  this.m_regdate = regDate;
	  this.m_orno = orNo;
	  this.m_pono = poNo;
	  this.m_ipcno = ipcNo;
	  this.m_ordate = orDate;
	  this.m_podate = poDate;
	  this.m_ipcdate = ipcDate;
  }
  */

  private static ProjectData m_nullObject;
  
public static ProjectData nullObject() {
	if (m_nullObject == null) {
		m_nullObject = new ProjectData("",null,Unit.nullObject()
				,null,null,"",null,"","", "", null, null, null, "", null
				);
	}
	

	return m_nullObject;
}
}
