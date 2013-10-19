package pohaci.gumunda.titis.project.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.*;

import pohaci.gumunda.titis.project.cgui.*;

public interface IProjectSQL {
  long getMaxIndex(String table, Connection conn) throws SQLException;

  void createCustomerCompanyGroup(CompanyGroup group, Connection conn) throws SQLException;
  CompanyGroup[] getAllCustomerCompanyGroup(Connection conn) throws SQLException;
  CompanyGroup getCustomerCompanyGroup(long index, Connection conn) throws SQLException;
  void updateCustomerCompanyGroup(long index, CompanyGroup group, Connection conn) throws SQLException;
  void deleteCustomerCompanyGroup(long index, Connection conn) throws SQLException;

  void createPartnerCompanyGroup(CompanyGroup group, Connection conn) throws SQLException;
  CompanyGroup[] getAllPartnerCompanyGroup(Connection conn) throws SQLException;
  CompanyGroup getPartnerCompanyGroup(long index, Connection conn) throws SQLException;
  void updatePartnerCompanyGroup(long index, CompanyGroup group, Connection conn) throws SQLException;
  void deletePartnerCompanyGroup(long index, Connection conn) throws SQLException;

  void createCustomer(Customer customer, Connection conn) throws SQLException;
  Customer[] getAllCustomer(Connection conn) throws SQLException;
  Customer getCustomerByIndex(long index, Connection conn) throws SQLException;
  void updateCustomer(long index, Customer customer, Connection conn) throws SQLException;
  void deleteCustomer(long index, Connection conn) throws SQLException;

  void createCustomerGroup(long customerindex, long groupindex, Connection conn) throws SQLException;
  CompanyGroup[] getCustomerGroup(long customerindex, Connection conn) throws SQLException;
  void deleteCustomerGroup(long customerindex, Connection conn) throws SQLException;

  void createCustomerAddress(long customerindex, SpecificAddress address, Connection conn) throws SQLException;
  SpecificAddress[] getCustomerAddress(long customerindex, Connection conn) throws SQLException;
  void deleteCustomerAddress(long customerindex, Connection conn) throws SQLException;

  void createCustomerContact(long customerindex, Personal contact, Connection conn) throws SQLException;
  Personal[] getCustomerContact(long customerindex, Connection conn) throws SQLException;
  void deleteCustomerContact(long customerindex, Connection conn) throws SQLException;

  Customer[] getCustomerByCriteria(String query, Connection conn) throws SQLException;

  void createPartner(Partner partner, Connection conn) throws SQLException;
  Partner[] getAllPartner(Connection conn) throws SQLException;
  Partner getPartnerByIndex(long index, Connection conn) throws SQLException;
  void updatePartner(long index, Partner partner, Connection conn) throws SQLException;
  void deletePartner(long index, Connection conn) throws SQLException;

  void createPartnerGroup(long partnerindex, long groupindex, Connection conn) throws SQLException;
  CompanyGroup[] getPartnerGroup(long partnerindex, Connection conn) throws SQLException;
  void deletePartnerGroup(long partnerindex, Connection conn) throws SQLException;

  void createPartnerAddress(long partnerindex, SpecificAddress address, Connection conn) throws SQLException;
  SpecificAddress[] getPartnerAddress(long partnerindex, Connection conn) throws SQLException;
  void deletePartnerAddress(long customerindex, Connection conn) throws SQLException;

  void createPartnerContact(long customerindex, Personal contact, Connection conn) throws SQLException;
  Personal[] getPartnerContact(long customerindex, Connection conn) throws SQLException;
  void deletePartnerContact(long customerindex, Connection conn) throws SQLException;

  Partner[] getPartnerByCriteria(String query, Connection conn) throws SQLException;

  void createPersonal(Personal personal, Connection conn) throws SQLException;
  Personal[] getAllPersonal(Connection conn) throws SQLException;
  Personal getPersonalByIndex(long index, Connection conn) throws SQLException;
  void updatePersonal(long index, Personal personal, Connection conn) throws SQLException;
  void deletePersonal(long index, Connection conn) throws SQLException;

  void createPersonalHome(long personalindex, PersonalHome home, Connection conn) throws SQLException;
  PersonalHome[] getPersonalHome(long personalindex, Connection conn) throws SQLException;
  void deletePersonalHome(long personalindex, Connection conn) throws SQLException;

  void createPersonalBusiness(long personalindex, PersonalBusiness business, Connection conn) throws SQLException;
  PersonalBusiness[] getPersonalBusiness(long personalindex, Connection conn) throws SQLException;
  void deletePersonalBusiness(long personalindex, Connection conn) throws SQLException;

  Personal[] getPersonalByCriteria(String query, Connection conn) throws SQLException;

  void createProjectData(ProjectData project, Connection conn) throws SQLException;
  ProjectData[] getAllProjectData(Connection conn) throws SQLException;
  ProjectData getProjectDataByIndex(long index, Connection conn) throws SQLException;
  void updateProjectData(long index, ProjectData project, Connection conn) throws SQLException;
  void deleteProjectData(long index, Connection conn) throws SQLException;
  ProjectData[] getProjectDataByCriteria(String query, Connection conn) throws SQLException;

  void createProjectPersonal(long projectindex, ProjectPersonal person, Connection conn) throws SQLException;
  ProjectPersonal[] getProjectPersonal(long projectindex, Connection conn) throws SQLException;
  void updateProjectPersonal(long index, ProjectPersonal person, Connection conn) throws SQLException;
  void deleteProjectPersonal(long index, Connection conn) throws SQLException;

  void createProjectOrganizationAttachment(long index, FileAttachment file, Connection conn) throws SQLException;
  FileAttachment getProjectOrganizationAttachment(long index, Connection conn) throws SQLException;
  void deleteProjectOrganizationAttachment(long index, Connection conn) throws SQLException;

  void createProjectContract(long projectindex, ProjectContract contract, Connection conn) throws SQLException;
  void updateProjectContract(long index, ProjectContract contract, Connection conn) throws SQLException;
  ProjectContract getProjectContract(long projectindex, Connection conn) throws SQLException;

  void createContractPayment(long contractindex, ContractPayment payment, Connection conn) throws SQLException;
  ContractPayment[] getContractPayment(long contractindex, Connection conn) throws SQLException;
  void deleteContractPayment(long contractindex, Connection conn) throws SQLException;

  void createProjectPartner(long projectindex, Partner partner, Connection conn) throws SQLException;
  ProjectPartner[] getProjectPartner(long projectindex, Connection conn) throws SQLException;
  void deleteProjectPartner(long index, Connection conn) throws SQLException;

  void createProjectPartnerContact(long index, Personal personal, Connection conn) throws SQLException;
  void updateProjectPartner(long index, Partner partner, Connection conn) throws SQLException;
  Personal[] getProjectPartnerContact(long index, Connection conn) throws SQLException;
  void deleteProjectPartnerContact(long index, Connection conn) throws SQLException;

  void createProjectClientContact(long projectindex, Personal personal, Connection conn) throws SQLException;
  ProjectClientContact[] getProjectClientContact(long projectindex, Connection conn) throws SQLException;
  void deleteProjectClientContact(long index, Connection conn) throws SQLException;

  void createProjectLocation(long index, ProjectLocation location, Connection conn) throws SQLException;
  void updateProjectLocation(long index, ProjectLocation location, Connection conn) throws SQLException;
  ProjectLocation[] getProjectLocation(long projectindex, Connection conn) throws SQLException;
  void deleteProjectLocation(long projectindex, Connection conn) throws SQLException;

  void createProjectNotes(long projectindex, ProjectNotes notes, Connection conn) throws SQLException;
  ProjectNotes[] getProjectNotes(long projectindex, Connection conn) throws SQLException;
  void updateProjectNotes(long index, ProjectNotes notes, Connection conn) throws SQLException;
  void deleteProjectNotes(long index, Connection conn) throws SQLException;
  ProjectNotes[] getProjectNotesByCriteria(String query, Connection conn) throws SQLException;

  void createProjectProgress(long projectindex, ProjectProgress progress, Connection conn) throws SQLException;
  ProjectProgress[] getProjectProgress(long projectindex, Connection conn) throws SQLException;
  void updateProjectProgress(long index, ProjectProgress progress, Connection conn) throws SQLException;
  void deleteProjectProgress(long index, Connection conn) throws SQLException;
  ProjectProgress[] getProjectProgressByCriteria(String qry, Connection conn) throws SQLException;

  void createTimeSheet(long projectindex, TimeSheet sheet, Connection conn) throws SQLException;
  TimeSheet[] getTimeSheet(long projectindex, Connection conn) throws SQLException;
  void updateTimeSheet(long index, TimeSheet sheet, Connection conn) throws SQLException;
  void deleteTimeSheet(long index, Connection conn) throws SQLException;
  TimeSheet[] getTimeSheetByCriteria(String query, Connection conn) throws SQLException;

  void createTimeSheetDetail(long sheetindex, TimeSheetDetail detail, Connection conn) throws SQLException;
  TimeSheetDetail[] getTimeSheetDetail(long sheetindex, Connection conn) throws SQLException;
  void deleteTimeSheetDetail(long sheetindex, Connection conn) throws SQLException;

ProjectData[] getProjectDataByAccount(Connection m_conn, long account) throws SQLException;
}