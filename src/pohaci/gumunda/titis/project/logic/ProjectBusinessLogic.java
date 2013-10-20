package pohaci.gumunda.titis.project.logic;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;

import pohaci.gumunda.aas.logic.AuthorizationBusinessLogic;
import pohaci.gumunda.aas.logic.AuthorizationException;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.CompanyGroup;
import pohaci.gumunda.titis.project.cgui.ContractPayment;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.EmployeeQualification;
import pohaci.gumunda.titis.project.cgui.EmployeeTimesheet;
import pohaci.gumunda.titis.project.cgui.FileAttachment;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.Personal;
import pohaci.gumunda.titis.project.cgui.PersonalBusiness;
import pohaci.gumunda.titis.project.cgui.PersonalHome;
import pohaci.gumunda.titis.project.cgui.ProjectClientContact;
import pohaci.gumunda.titis.project.cgui.ProjectContract;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectLocation;
import pohaci.gumunda.titis.project.cgui.ProjectNotes;
import pohaci.gumunda.titis.project.cgui.ProjectPartner;
import pohaci.gumunda.titis.project.cgui.ProjectPersonal;
import pohaci.gumunda.titis.project.cgui.ProjectProgress;
import pohaci.gumunda.titis.project.cgui.SpecificAddress;
import pohaci.gumunda.titis.project.cgui.TimeSheet;
import pohaci.gumunda.titis.project.cgui.TimeSheetDetail;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.dbapi.IProjectSQL;
import pohaci.gumunda.titis.project.dbapi.ProjectSQLSAP;

public class ProjectBusinessLogic {
  protected Connection m_conn = null;
    private static ProjectBusinessLogic classVariable = null;

    public static ProjectBusinessLogic getInstance(Connection conn) {
        if (classVariable == null) {
            classVariable = new ProjectBusinessLogic(conn);
        }
        return classVariable;
    }
  public ProjectBusinessLogic(Connection conn) {
    m_conn = conn;
  }

  public CompanyGroup createCustomerCompanyGroup(long sessionid, String modul, CompanyGroup group) throws Exception {
    /*AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);*/
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      /*if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }*/

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createCustomerCompanyGroup(group, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new CompanyGroup(index, group.getName(), group.getDescription());
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public CompanyGroup updateCustomerCompanyGroup(long sessionid, String modul, long index, CompanyGroup group) throws Exception {
    //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      /*if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }*/

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateCustomerCompanyGroup(index, group, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new CompanyGroup(index, group.getName(), group.getDescription());
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public CompanyGroup[] getAllCustomerCompanyGroup(long sessionid, String modul) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
//        throw new AuthorizationException("Authorization read of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getAllCustomerCompanyGroup(m_conn);
    }
    catch (Exception ex) {
        ex.printStackTrace();
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteCustomerCompanyGroup(long sessionid, String modul, long index) throws Exception {
    //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      /*if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }*/

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteCustomerCompanyGroup(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public CompanyGroup createPartnerCompanyGroup(long sessionid, String modul, CompanyGroup group) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
//        throw new AuthorizationException("Authorization write of module " + modul + " denied");
//      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createPartnerCompanyGroup(group, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PARTNER_COMPANY_GROUP, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new CompanyGroup(index, group.getName(), group.getDescription());
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public CompanyGroup updatePartnerCompanyGroup(long sessionid, String modul, long index, CompanyGroup group) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
//        throw new AuthorizationException("Authorization update of module " + modul + " denied");
//      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updatePartnerCompanyGroup(index, group, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new CompanyGroup(index, group.getName(), group.getDescription());
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public CompanyGroup[] getAllPartnerCompanyGroup(long sessionid, String modul) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
//        throw new AuthorizationException("Authorization read of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getAllPartnerCompanyGroup(m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deletePartnerCompanyGroup(long sessionid, String modul, long index) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
//        throw new AuthorizationException("Authorization update of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deletePartnerCompanyGroup(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Customer createCustomer(long sessionid, String modul, Customer customer) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      //IProjectSQL isql = new ProjectSQLSAP();
	  ProjectSQLSAP isql = new ProjectSQLSAP();
      isql.createCustomer(customer, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_CUSTOMER, m_conn);

      CompanyGroup[] group = customer.getCompanyGroup();
      for(int i = 0; i < group.length; i ++)
        isql.createCustomerGroup(index, group[i].getIndex(), m_conn);

      SpecificAddress[] address = customer.getSpecificAddress();
      for(int i = 0; i < address.length; i ++)
        isql.createCustomerAddress(index, address[i], m_conn);

      Personal[] personal = customer.getPersonal();
      for(int i = 0; i < personal.length; i ++)
        isql.createCustomerContact(index, personal[i], m_conn);


      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new Customer(index, customer);
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public Customer updateCustomer(long sessionid, String modul, long index, Customer customer) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateCustomer(index, customer, m_conn);

      CompanyGroup[] group = customer.getCompanyGroup();
      isql.deleteCustomerGroup(index, m_conn);
      for(int i = 0; i < group.length; i ++)
        isql.createCustomerGroup(index, group[i].getIndex(), m_conn);

      SpecificAddress[] address = customer.getSpecificAddress();
      isql.deleteCustomerAddress(index, m_conn);
      for(int i = 0; i < address.length; i ++)
        isql.createCustomerAddress(index, address[i], m_conn);

      Personal[] personal = customer.getPersonal();
      isql.deleteCustomerContact(index, m_conn);
      for(int i = 0; i < personal.length; i ++)
        isql.createCustomerContact(index, personal[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new Customer(index, customer);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public Customer[] getAllCustomer(long sessionid, String modul) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
//        throw new AuthorizationException("Authorization read of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getAllCustomer(m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteCustomer(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteCustomer(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Customer[] getCustomerByCriteria(long sessionid, String modul, String query) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getCustomerByCriteria(query, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Partner createPartner(long sessionid, String modul, Partner partner) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createPartner(partner, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PARTNER, m_conn);

      CompanyGroup[] group = partner.getCompanyGroup();
      for(int i = 0; i < group.length; i ++)
        isql.createPartnerGroup(index, group[i].getIndex(), m_conn);

      SpecificAddress[] address = partner.getSpecificAddress();
      for(int i = 0; i < address.length; i ++)
        isql.createPartnerAddress(index, address[i], m_conn);

      Personal[] personal = partner.getPersonal();
      for(int i = 0; i < personal.length; i ++)
        isql.createPartnerContact(index, personal[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new Partner(index, partner);
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public Partner updatePartner(long sessionid, String modul, long index, Partner partner) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updatePartner(index, partner, m_conn);

      CompanyGroup[] group = partner.getCompanyGroup();
      isql.deletePartnerGroup(index, m_conn);
      for(int i = 0; i < group.length; i ++)
        isql.createPartnerGroup(index, group[i].getIndex(), m_conn);

      SpecificAddress[] address = partner.getSpecificAddress();
      isql.deletePartnerAddress(index, m_conn);
      for(int i = 0; i < address.length; i ++)
        isql.createPartnerAddress(index, address[i], m_conn);

      Personal[] personal = partner.getPersonal();
      isql.deletePartnerContact(index, m_conn);
      for(int i = 0; i < personal.length; i ++)
        isql.createPartnerContact(index, personal[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new Partner(index, partner);
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public Partner[] getAllPartner(long sessionid, String modul) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
//        throw new AuthorizationException("Authorization read of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getAllPartner(m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deletePartner(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deletePartner(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public CompanyGroup[] getCustomerGroup(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getCustomerGroup(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public SpecificAddress[] getCustomerAddress(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getCustomerAddress(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Personal[] getCustomerContact(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getCustomerContact(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public CompanyGroup[] getPartnerGroup(long sessionid, String modul, long index) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
//        throw new AuthorizationException("Authorization read of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getPartnerGroup(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public SpecificAddress[] getPartnerAddress(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getPartnerAddress(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Personal[] getPartnerContact(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getPartnerContact(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Partner[] getPartnerByCriteria(long sessionid, String modul, String query) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getPartnerByCriteria(query, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  
  public Partner getPartnerByIndex(long index) throws Exception {	  
	  ProjectSQLSAP isql = new ProjectSQLSAP();
	  return isql.getPartnerByIndex(index, m_conn);	  
  }

  public Personal createPersonal(long sessionid, String modul, Personal personal) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
//        throw new AuthorizationException("Authorization write of module " + modul + " denied");
//      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createPersonal(personal, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PERSONAL, m_conn);

      PersonalHome[] home = personal.getPersonalHome();
      for(int i = 0; i < home.length; i ++)
        isql.createPersonalHome(index, home[i], m_conn);

      PersonalBusiness[] business = personal.getPersonalBusiness();
      for(int i = 0; i < business.length; i ++)
        isql.createPersonalBusiness(index, business[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new Personal(index, personal);
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public Personal updatePersonal(long sessionid, String modul, long index, Personal personal) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
//        throw new AuthorizationException("Authorization update of module " + modul + " denied");
//      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updatePersonal(index, personal, m_conn);

      PersonalHome[] home = personal.getPersonalHome();
      isql.deletePersonalHome(index, m_conn);
      for(int i = 0; i < home.length; i ++)
        isql.createPersonalHome(index, home[i], m_conn);

      PersonalBusiness[] business = personal.getPersonalBusiness();
      isql.deletePersonalBusiness(index, m_conn);
      for(int i = 0; i < business.length; i ++)
        isql.createPersonalBusiness(index, business[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new Personal(index, personal);
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public Personal[] getAllPersonal(long sessionid, String modul) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
//        throw new AuthorizationException("Authorization read of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getAllPersonal(m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deletePersonal(long sessionid, String modul, long index) throws Exception {
//    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
//        throw new AuthorizationException("Authorization update of module " + modul + " denied");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deletePersonal(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public PersonalHome[] getPersonalHome(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getPersonalHome(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public PersonalBusiness[] getPersonalBusiness(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getPersonalBusiness(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Personal[] getPersonalByCriteria(long sessionid, String modul, String query) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getPersonalByCriteria(query, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public ProjectData createProjectData(long sessionid, String modul, ProjectData project) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " ditolak");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectData(project, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_DATA, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectData(index, project);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectData[] getAllProjectData(long sessionid, String modul) throws Exception {
    //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    try{
//      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
//        throw new AuthorizationException("Authorization read of module " + modul + " ditolak");
//      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getAllProjectData(m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }
  
  public ProjectData[] getProjectDataByAccount(long sessionid, String modul, long account) throws Exception {
	    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
	    try{
	      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
	        throw new AuthorizationException("Authorization read of module " + modul + " ditolak");
	      }

	      IProjectSQL isql = new ProjectSQLSAP();
	      return isql.getProjectDataByAccount(m_conn, account);
	    }
	    catch(Exception exc) {
	      throw new Exception(exc.getMessage());
	    }
	  }

  public ProjectData updateProjectData(long sessionid, String modul, long index, ProjectData project) throws Exception {    
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      //IProjectSQL isql = new ProjectSQLSAP();
      ProjectSQLSAP isql = new ProjectSQLSAP();
      isql.updateProjectData(index, project, m_conn);

      return new ProjectData(index, project);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public void deleteProjectData(long sessionid, String modul, ProjectData project) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectData(project.getIndex(), m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public ProjectData[] getProjectDataByCriteria(long sessionid, String modul, String query) throws Exception {      
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try{
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization read of module " + modul + " ditolak");
      }

      ProjectSQLSAP isql = new ProjectSQLSAP();
      return isql.getProjectDataByCriteria(query, m_conn);
    }
    catch(Exception exc) {
      exc.printStackTrace();
      throw new Exception(exc.getMessage());
    }
  }
  
  public ProjectData getProjectDataByIndex(long index) throws Exception {	  
	  ProjectSQLSAP isql = new ProjectSQLSAP();
	  return isql.getProjectDataByIndex(index, m_conn);	  
  }
  
  public Customer getCustomerByCriteria1(long index,Connection m_conn)throws Exception{
      try{
          ProjectSQLSAP isql = new ProjectSQLSAP();
          return isql.getCustomerByIndex(index,m_conn);
      }
      catch(Exception exc) {
      exc.printStackTrace();
      throw new Exception(exc.getMessage());
    }
  }

  public ProjectPersonal createProjectPersonal(long sessionid, String modul, long projectindex, ProjectPersonal person) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectPersonal(projectindex, person, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_PERSONAL, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectPersonal(index, person);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectPersonal updateProjectPersonal(long sessionid, String modul, long index, ProjectPersonal person) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateProjectPersonal(index, person, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectPersonal(index, person);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectPersonal[] getProjectPersonal(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectPersonal(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteProjectPersonal(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectPersonal(index, m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public ProjectContract createProjectContract(long sessionid, String modul, long projectindex, ProjectContract contract) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
  }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectContract(projectindex, contract, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_CONTRACT, m_conn);

      ContractPayment[] payment = contract.getContractPayment();
      
      for(int i = 0; i < payment.length; i ++)
        isql.createContractPayment(index, payment[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectContract(index, contract);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectContract updateProjectContract(long sessionid, String modul, long index, ProjectContract contract) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateProjectContract(index, contract, m_conn);

      ContractPayment[] payment = contract.getContractPayment();
      isql.deleteContractPayment(index, m_conn);
      for(int i = 0; i < payment.length; i ++)
        isql.createContractPayment(index, payment[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectContract(index, contract);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectContract getProjectContract(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectContract(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public ContractPayment[] getProjectContractPayment(long sessionid, String modul,long index) throws Exception{
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    try {
          if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
            throw new AuthorizationException("Authorization read of module " + modul + " denied");
          }
          ProjectSQLSAP isql = new ProjectSQLSAP();
          return isql.getContractPayment(index,m_conn);
    }catch (Exception ex){      
        throw new Exception(ex.getMessage());
    }     
  } 
          
  public ProjectLocation createProjectLocation(long sessionid, String modul, long projectindex, ProjectLocation location) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectLocation(projectindex, location, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_LOCATION, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectLocation(index, location);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectLocation updateProjectLocation(long sessionid, String modul, long index, ProjectLocation location) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateProjectLocation(index, location, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectLocation(index, location);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectLocation[] getProjectLocation(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectLocation(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteProjectLocation(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectLocation(index, m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public ProjectClientContact createProjectClientContact(long sessionid, String modul, long projectindex, Personal personal) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectClientContact(projectindex, personal, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_CLIENT_CONTACT, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectClientContact(index, personal);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectClientContact[] getProjectClientContact(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectClientContact(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteProjectClientContact(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectClientContact(index, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
    }
    catch (Exception ex) {
      try{
        m_conn.rollback();
        m_conn.setAutoCommit(true);
      }
      catch(Exception e){}
      throw new Exception(ex.getMessage());
    }
  }

  public ProjectPartner createProjectPartner(long sessionid, String modul, long projectindex, Partner partner) throws Exception {      
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectPartner(projectindex, partner, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_PARTNER, m_conn);

      Personal[] personal = partner.getPersonal();
      for(int i = 0; i < personal.length; i ++)
        isql.createProjectPartnerContact(index, personal[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectPartner(index, partner);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectPartner updateProjectPartner(long sessionid, String modul, long index, Partner partner) throws Exception {
   AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
   int trans = Connection.TRANSACTION_READ_COMMITTED;

   try {
     if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
       throw new AuthorizationException("Authorization write of module " + modul + " denied");
     }

     m_conn.setAutoCommit(false);
     trans = m_conn.getTransactionIsolation();
     m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

     IProjectSQL isql = new ProjectSQLSAP();
     isql.updateProjectPartner(index, partner, m_conn);

     Personal[] personal = partner.getPersonal();
     isql.deleteProjectPartnerContact(index, m_conn);
     for(int i = 0; i < personal.length; i ++)
       isql.createProjectPartnerContact(index, personal[i], m_conn);

     m_conn.commit();
     m_conn.setAutoCommit(true);
     m_conn.setTransactionIsolation(trans);

     return new ProjectPartner(index, partner);
   }
   catch(Exception exc) {
     exc.printStackTrace();
     try {
       m_conn.rollback();
       m_conn.setAutoCommit(true);
       m_conn.setTransactionIsolation(trans);
       }catch(Exception ex1){}
       throw new Exception(exc.getMessage());
   }
  }

  public ProjectPartner[] getProjectPartner(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectPartner(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public Personal[] getProjectPartnerContact(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectPartnerContact(index, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteProjectPartner(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectPartner(index, m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public ProjectNotes createProjectNotes(long sessionid, String modul, long projectindex, ProjectNotes notes) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectNotes(projectindex, notes, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_NOTES, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectNotes(index, notes);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectNotes[] getProjectNotes(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectNotes(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public ProjectNotes[] getProjectNotesByCriteria(long sessionid, String modul, String query) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectNotesByCriteria(query, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public ProjectNotes updateProjectNotes(long sessionid, String modul, long index, ProjectNotes notes) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateProjectNotes(index, notes, m_conn);
      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectNotes(index, notes);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public void deleteProjectNotes(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectNotes(index, m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public ProjectProgress createProjectProgress(long sessionid, String modul, long projectindex, ProjectProgress progress) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createProjectProgress(projectindex, progress, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_PROJECT_PROGRESS, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectProgress(index, progress);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public ProjectProgress[] getProjectProgress(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectProgress(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  
  public ProjectProgress[] getProjectProgressByCriteria(long sessionid, String modul, String query) throws Exception {
	    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

	    try {
	      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
	        throw new AuthorizationException("Authorization read of module " + modul + " denied");
	      }

	      IProjectSQL isql = new ProjectSQLSAP();
	      return isql.getProjectProgressByCriteria(query, m_conn);
	    }
	    catch (Exception ex) {
	      throw new Exception(ex.getMessage());
	    }
  }

  public ProjectProgress updateProjectProgress(long sessionid, String modul, long index, ProjectProgress progress) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateProjectProgress(index, progress, m_conn);
      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new ProjectProgress(index, progress);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public void deleteProjectProgress(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectProgress(index, m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public void createProjectProjectOrganizationAttachment(long sessionid, String modul, long projectindex, FileAttachment file) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectOrganizationAttachment(projectindex, m_conn);
      isql.createProjectOrganizationAttachment(projectindex, file, m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
      }
      catch(Exception ex){}
      throw new Exception(exc.getMessage());
    }
  }

  public FileAttachment getProjectOrganizationAttachment(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getProjectOrganizationAttachment(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteProjectOrganizationAttachment(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteProjectOrganizationAttachment(index, m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public TimeSheet createTimeSheet(long sessionid, String modul, long projectindex, TimeSheet sheet) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
        throw new AuthorizationException("Authorization write of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.createTimeSheet(projectindex, sheet, m_conn);
      long index = isql.getMaxIndex(IDBConstants.TABLE_TIME_SHEET, m_conn);

      TimeSheetDetail[] detail = sheet.getTimeSheetDetail();
      for(int i = 0; i < detail.length; i ++)
        isql.createTimeSheetDetail(index, detail[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new TimeSheet(index, sheet);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public TimeSheet[] getTimeSheet(long sessionid, String modul, long projectindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getTimeSheet(projectindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public TimeSheet[] getTimeSheetByCriteria(long sessionid, String modul, String query) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getTimeSheetByCriteria(query, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public TimeSheet updateTimeSheet(long sessionid, String modul, long index, TimeSheet sheet) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
    int trans = Connection.TRANSACTION_READ_COMMITTED;

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      m_conn.setAutoCommit(false);
      trans = m_conn.getTransactionIsolation();
      m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      IProjectSQL isql = new ProjectSQLSAP();
      isql.updateTimeSheet(index, sheet, m_conn);

      TimeSheetDetail[] detail = sheet.getTimeSheetDetail();
      isql.deleteTimeSheetDetail(index, m_conn);
      for(int i = 0; i < detail.length; i ++)
        isql.createTimeSheetDetail(index, detail[i], m_conn);

      m_conn.commit();
      m_conn.setAutoCommit(true);
      m_conn.setTransactionIsolation(trans);

      return new TimeSheet(index, sheet);
    }
    catch(Exception exc) {
      try {
        m_conn.rollback();
        m_conn.setAutoCommit(true);
        m_conn.setTransactionIsolation(trans);
        }catch(Exception ex1){}
        throw new Exception(exc.getMessage());
    }
  }

  public void deleteTimeSheet(long sessionid, String modul, long index) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
        throw new AuthorizationException("Authorization update of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      isql.deleteTimeSheet(index, m_conn);
    }
    catch(Exception exc) {
      throw new Exception(exc.getMessage());
    }
  }

  public TimeSheetDetail[] getTimeSheetDetail(long sessionid, String modul, long sheetindex) throws Exception {
    AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      IProjectSQL isql = new ProjectSQLSAP();
      return isql.getTimeSheetDetail(sheetindex, m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  
   public EmployeeQualification[] getQualification(long sessionid, String modul, long index) throws  Exception{
         AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

    try {
      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
        throw new AuthorizationException("Authorization read of module " + modul + " denied");
      }

      ProjectSQLSAP isql = new ProjectSQLSAP();
      return isql.getEmployeeQualification(index,m_conn);
    }
    catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
   }
   
   public EmployeeQualification[] getEmployeeQualificationUsingName(long sessionid, String modul, long index) throws  Exception{
       AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

  try {
    if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
      throw new AuthorizationException("Authorization read of module " + modul + " denied");
    }

    ProjectSQLSAP isql = new ProjectSQLSAP();
    return isql.getEmployeeQualificationUsingName(index,m_conn);
  }
  catch (Exception ex) {
    throw new Exception(ex.getMessage());
  }
 }
   
   public EmployeeTimesheet[] getAllEmployeeTimesheet(String query,long sessionid, String modul) throws Exception{
	   AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
	    try {
	      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
	        throw new AuthorizationException("Authorization read of module " + modul + " denied");
	      }

	      ProjectSQLSAP isql = new ProjectSQLSAP();
	      return isql.getAllEmployeeTimesheet(query,m_conn);
	    }
	    catch (Exception ex) {
	      throw new Exception(ex.getMessage());
	    }	
   }
   
   public EmployeeTimesheet[] getAllEmployeeTimesheetUtilization(String query,long sessionid, String modul) throws Exception{
	   AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
	    try {
	      if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
	        throw new AuthorizationException("Authorization read of module " + modul + " denied");
	      }

	      ProjectSQLSAP isql = new ProjectSQLSAP();
	      return isql.getAllEmployeeTimesheetUtilization(query,m_conn);
	    }
	    catch (Exception ex) {
	      throw new Exception(ex.getMessage());
	    }	
   }
   
   
   public Customer getCustomerByIndex(long index){      
    Customer customer=null;   
    try{                           
         customer = getCustomerByCriteria1(index,m_conn);         
         return customer; 
    }
    catch(Exception ex) {      
      return null;
    }    
  }
      
  
  public Unit getUnit(long index){
      Unit unit=null;            
      try{
          AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
          unit = logic.getUnitByIndex(index);        
      }
      catch(Exception ex) {                                    
      }
      return unit;
  }  
  
  public Activity getActivity(long index){
      Activity activity = null;
      try{
          AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
          activity = logic.getActivityByIndex(index);            
      }
      catch(Exception ex) {                                    
      }
      return activity;
  }
  
  public Organization getOrganization(long index){
      Organization organization=null;
      try{          
          HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
          organization  = logic.getOrganizationByIndex(index);
      }
       catch(Exception ex) {                                    
      }
      return organization;      
  } 
  
  public Currency getCurrecy(long sessionid,long index){
      Currency currency = null;
      try{          
          AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
          currency = logic.getCurrencyByIndex(sessionid,"currency byindex", index);
      }catch(Exception ex){          
      }
      return currency;
  }
  
  
  
}