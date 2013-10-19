package pohaci.gumunda.titis.hrm.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.*;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.hrm.cgui.report.BankRpt;
import pohaci.gumunda.titis.hrm.cgui.report.CertificationRpt;
import pohaci.gumunda.titis.hrm.cgui.report.CurrentEmployementRpt;
import pohaci.gumunda.titis.hrm.cgui.report.EducationRpt;
import pohaci.gumunda.titis.hrm.cgui.report.EmploymentHistoryRpt;
import pohaci.gumunda.titis.hrm.cgui.report.FamilyRpt;
import pohaci.gumunda.titis.hrm.cgui.report.PaychequesRpt;
import pohaci.gumunda.titis.hrm.logic.PaychequesValueRpt;


public interface IHRMSQL {
  long getMaxIndex(String table, Connection conn) throws SQLException;

  void createOrganization(Organization org, Connection conn) throws SQLException;
  void createOrganizationStructure(long parentorg, long suborg, Connection conn) throws SQLException;
  Organization getOrganizationByIndex(long index, Connection conn) throws SQLException;
  Organization[] getSuperOrganization(Connection conn) throws SQLException;
  Organization[] getSubOrganization(long parentindex, Connection conn) throws SQLException;
  void updateOrganization(long index, Organization org, Connection conn) throws SQLException;
  void deleteOrganization(long index, Connection conn) throws SQLException;

  void createQualification(Qualification qua, Connection conn) throws SQLException;
  Qualification[] getAllQualification(Connection conn) throws SQLException;
  Qualification getQualification(long index, Connection conn) throws SQLException;
  void updateQualification(long index, Qualification qua, Connection conn) throws SQLException;
  void deleteQualification(long index, Connection conn) throws SQLException;

  void createJobTitle(JobTitle job, Connection conn) throws SQLException;
  JobTitle[] getAllJobTitle(Connection conn) throws SQLException;
  JobTitle getJobTitle(long index, Connection conn) throws SQLException;
  void updateJobTitle(long index, JobTitle job, Connection conn) throws SQLException;
  void deleteJobTitle(long index, Connection conn) throws SQLException;

  void createWorkAgreement(WorkAgreement work, Connection conn) throws SQLException;
  WorkAgreement[] getAllWorkAgreement(Connection conn) throws SQLException;
  WorkAgreement getWorkAgreement(long index, Connection conn) throws SQLException;
  void updateWorkAgreement(long index, WorkAgreement work, Connection conn) throws SQLException;
  void deleteWorkAgreement(long index, Connection conn) throws SQLException;

  void createEducation(Education edu, Connection conn) throws SQLException;
  Education[] getAllEducation(Connection conn) throws SQLException;
  Education getEducation(long index, Connection conn) throws SQLException;
  void updateEducation(long index, Education edu, Connection conn) throws SQLException;
  void deleteEducation(long index, Connection conn) throws SQLException;

  void createReligion(SimpleEmployeeAttribute religion, Connection conn) throws SQLException;
  SimpleEmployeeAttribute[] getAllReligion(Connection conn) throws SQLException;
  SimpleEmployeeAttribute getReligion(long index, Connection conn) throws SQLException;
  void updateReligion(long index, SimpleEmployeeAttribute religion, Connection conn) throws SQLException;
  void deleteReligion(long index, Connection conn) throws SQLException;

  void createSexType(SexType type, Connection conn) throws SQLException;
  SexType[] getAllSexType(Connection conn) throws SQLException;
  SexType getSexType(long index, Connection conn) throws SQLException;
  void updateSexType(long index, SexType type, Connection conn) throws SQLException;
  void deleteSexType(long index, Connection conn) throws SQLException;

  void createFamilyRelation(SimpleEmployeeAttribute religion, Connection conn) throws SQLException;
  SimpleEmployeeAttribute[] getAllFamilyRelation(Connection conn) throws SQLException;
  SimpleEmployeeAttribute getFamilyRelation(long index, Connection conn) throws SQLException;
  void updateFamilyRelation(long index, SimpleEmployeeAttribute religion, Connection conn) throws SQLException;
  void deleteFamilyRelation(long index, Connection conn) throws SQLException;

  void createMaritalStatus(SimpleEmployeeAttribute status, Connection conn) throws SQLException;
  SimpleEmployeeAttribute[] getAllMaritalStatus(Connection conn) throws SQLException;
  SimpleEmployeeAttribute getMaritalStatus(long index, Connection conn) throws SQLException;
  void updateMaritalStatus(long index, SimpleEmployeeAttribute status, Connection conn) throws SQLException;
  void deleteMaritalStatus(long index, Connection conn) throws SQLException;

  void createAllowenceMultiplier(AllowenceMultiplier multiplier, Connection conn) throws SQLException;
  AllowenceMultiplier[] getAllAllowenceMultiplier(Connection conn) throws SQLException;
  AllowenceMultiplier getAllowenceMultiplier(long index, Connection conn) throws SQLException;
  void updateAllowenceMultiplier(long index, AllowenceMultiplier multiplier, Connection conn) throws SQLException;
  void deleteAllowenceMultiplier(long index, Connection conn) throws SQLException;

  void createPTKP(PTKP ptkp, Connection conn) throws SQLException;
  PTKP[] getAllPTKP(Connection conn) throws SQLException;
  PTKP getPTKP(long index, Connection conn) throws SQLException;
  void updatePTKP(long index, PTKP ptkp, Connection conn) throws SQLException;
  void deletePTKP(long index, Connection conn) throws SQLException;

  void createTaxArt21Tariff(TaxArt21Tariff tariff, Connection conn) throws SQLException;
  TaxArt21Tariff[] getAllTaxArt21Tariff(Connection conn) throws SQLException;
  TaxArt21Tariff getTaxArt21Tariff(long index, Connection conn) throws SQLException;
  void updateTaxArt21Tariff(long index, TaxArt21Tariff tariff, Connection conn) throws SQLException;
  void deleteTaxArt21Tariff(long index, Connection conn) throws SQLException;

  void createLeaveType(LeaveType type, Connection conn) throws SQLException;
  LeaveType[] getAllLeaveType(Connection conn) throws SQLException;
  LeaveType getLeaveType(long index, Connection conn) throws SQLException;
  void updateLeaveType(long index, LeaveType type, Connection conn) throws SQLException;
  void deleteLeaveType(long index, Connection conn) throws SQLException;

  void createPermitionType(PermitionType type, Connection conn) throws SQLException;
  PermitionType[] getAllPermitionType(Connection conn) throws SQLException;
  PermitionType getPermitionType(long index, Connection conn) throws SQLException;
  void updatePermitionType(long index, PermitionType type, Connection conn) throws SQLException;
  void deletePermitionType(long index, Connection conn) throws SQLException;

  void createOfficeHourPermition(OfficeHourPermition hour, Connection conn) throws SQLException;
  OfficeHourPermition[] getAllOfficeHourPermition(Connection conn) throws SQLException;
  OfficeHourPermition getOfficeHourPermition(long index, Connection conn) throws SQLException;
  void updateOfficeHourPermition(long index, OfficeHourPermition hour, Connection conn) throws SQLException;
  void deleteOfficeHourPermition(long index, Connection conn) throws SQLException;

  void createEmployee(Employee employee, Connection conn) throws SQLException;
  Employee[] getAllEmployee(Connection conn) throws SQLException;
  Employee getEmployeeByIndex(long index, Connection conn) throws SQLException;
  void updateEmployee(long index, Employee employee, Connection conn) throws SQLException;
  void deleteEmployee(long index, Connection conn) throws SQLException;
  Employee[] getEmployeeByCriteria(String query, Connection conn) throws SQLException;
  EmployeeSubOrdinat[] getEmployeeByUnit(Connection conn,long unitNumber)throws SQLException ;


  void createEmployeeQualification(long employeeindex, long quaindex, Connection conn) throws SQLException;
  Qualification[] getEmployeeQualification(long employeeindex, Connection conn) throws SQLException;
  void deleteEmployeeQualification(long employeeindex, Connection conn) throws SQLException;

  void createEmployeeEmployment(long employeindex, Employment employ, Connection conn) throws SQLException;
  Employment[] getEmployeeEmployment(long employeeindex, Connection conn) throws SQLException;
  void deleteEmployeeEmployment(long employeeindex, Connection conn) throws SQLException;

  void createEmployeeEducation(long employeindex, EmployeeEducation edu, Connection conn) throws SQLException;
  EmployeeEducation[] getEmployeeEducation(long employeeindex, Connection conn) throws SQLException;
  void deleteEmployeeEducation(long index, Connection conn) throws SQLException;

  void createEmployeeCertification(long employeindex, Certification certificate, Connection conn) throws SQLException;
  Certification[] getEmployeeCertification(long employeeindex, Connection conn) throws SQLException;
  void deleteEmployeeCertification(long employeeindex, Connection conn) throws SQLException;

  void createEmployeeFamily(long employeeindex, EmployeeFamily family, Connection conn) throws SQLException;
  EmployeeFamily[] getEmployeeFamily(long employeeindex, Connection conn) throws SQLException;
  void deleteEmployeeFamily(long employeeindex, Connection conn) throws SQLException;

  void createEmployeeAccount(long employeindex, EmployeeAccount account, Connection conn) throws SQLException;
  EmployeeAccount[] getEmployeeAccount(long employeeindex, Connection conn) throws SQLException;
  void deleteEmployeeAccount(long employeeindex, Connection conn) throws SQLException;

  void createEmployeeRetirement(long employeindex, Retirement retirement, Connection conn) throws SQLException;
  Retirement getEmployeeRetirement(long employeeindex, Connection conn) throws SQLException;
  void deleteEmployeeRetirement(long employeeindex, Connection conn) throws SQLException;

  void createOvertimeMultiplier(OvertimeMultiplier multiplier, Connection conn) throws SQLException;
  OvertimeMultiplier[] getAllOvertimeMultiplier(Connection conn) throws SQLException;
  OvertimeMultiplier getOvertimeMultiplier(long index, Connection conn) throws SQLException;
  OvertimeMultiplier[] getAllOvertimeMultiplierOrdered(Connection m_conn) throws SQLException;
  void updateOvertimeMultiplier(long index, OvertimeMultiplier multiplier, Connection conn) throws SQLException;
  void deleteOvertimeMultiplier(long index, Connection conn) throws SQLException;

  void createDefaultWorkingDay(DefaultWorkingDay day, Connection conn) throws SQLException;
  DefaultWorkingDay getDefaultWorkingDay(Connection conn) throws SQLException;
  void deleteDefaultWorkingDay(Connection conn) throws SQLException;

  void createDefaultWorkingTime(DefaultWorkingTime time, Connection conn) throws SQLException;
  DefaultWorkingTime getDefaultWorkingTime(Connection conn) throws SQLException;
  void deleteDefaultWorkingTime(Connection conn) throws SQLException;

  void createPaychequeLabel(PaychequeLabel label, Connection conn) throws SQLException;
  void createPaychequeLabelStructure(long superlabel, long sublabel, Connection conn) throws SQLException;
  PaychequeLabel getPaychequeLabelByIndex(long index, Connection conn) throws SQLException;
  PaychequeLabel[] getSuperPaychequeLabel(Connection conn) throws SQLException;
  PaychequeLabel[] getSubPaychequeLabel(long superlabel, Connection conn) throws SQLException;
  void updatePaychequeLabel(long index, PaychequeLabel label, Connection conn) throws SQLException;
  void deletePaychequeLabel(long index, Connection conn) throws SQLException;

  void createEmployeeOfficeHourPermition(long employeeindex, EmployeeOfficeHourPermition reason, Connection conn) throws SQLException;
  EmployeeOfficeHourPermition[] getAllEmployeeOfficeHourPermition(long employeeindex, Connection conn) throws SQLException;
  EmployeeOfficeHourPermition[] getAllEmployeeOfficeHourPermition(String query, Connection conn) throws SQLException;
  void updateEmployeeOfficeHourPermition(long index, EmployeeOfficeHourPermition reason, Connection conn) throws SQLException;
  void deleteEmployeeOfficeHourPermition(long index, Connection conn) throws SQLException;

  void createEmployeeLeave(long employeeindex, EmployeeLeavePermition reason, Connection conn) throws SQLException;
  EmployeeLeavePermition[] getAllEmployeeLeave(long employeeindex, Connection conn) throws SQLException;
  void updateEmployeeLeave(long index, EmployeeLeavePermition reason, Connection conn) throws SQLException;
  void deleteEmployeeLeave(long index, Connection conn) throws SQLException;

  void createEmployeePermition(long employeeindex, EmployeeLeavePermition reason, Connection conn) throws SQLException;
  EmployeeLeavePermition[] getAllEmployeePermition(long employeeindex, Connection conn) throws SQLException;
  void updateEmployeePermition(long index, EmployeeLeavePermition reason, Connection conn) throws SQLException;
  void deleteEmployeePermition(long index, Connection conn) throws SQLException;
  EmployeeLeavePermition[] getAllEmployeeLeavePermition(String query, short type, Connection conn) throws SQLException;

  void createNonPaychequePeriod(NonPaychequePeriod period, Connection conn) throws SQLException;
  NonPaychequePeriod getNonPaychequePeriod(Connection conn) throws SQLException;
  void deleteNonPaychequePeriod(Connection conn) throws SQLException;

  void createAnnualLeaveRight(AnnualLeaveRight leave, Connection conn) throws SQLException;
  AnnualLeaveRight[] getAllAnnualLeaveRight(Connection conn) throws SQLException;
  AnnualLeaveRight getAnnualLeaveRight(long index, Connection conn) throws SQLException;
  void updateAnnualLeaveRight(long index, AnnualLeaveRight leave, Connection conn) throws SQLException;
  void deleteAnnualLeaveRight(long index, Connection conn) throws SQLException;

  void createHoliday(Holiday holiday, Connection conn) throws SQLException;
  Holiday[] getAllHoliday(Connection conn) throws SQLException;
  Holiday[] getHoliday(java.util.Date beginDate, java.util.Date endDate, Connection conn) throws SQLException;
  void deleteHoliday(java.util.Date date, Connection conn) throws SQLException;
  void deleteAllHoliday(Connection m_conn)throws SQLException;
  void updateHoliday(Holiday holiday, Connection conn) throws SQLException;

  java.util.Date getMinTMTEmployement(long employeeindex, Connection conn) throws SQLException;
  AnnualLeaveRight getAnnualLeaveRight(Connection conn) throws SQLException;
  int getEmployeeLeavePermitionUsed(long employeeindex, short year, Connection conn) throws SQLException;

  void createEmployeeOfficeWorkingTime(  Connection conn,Employee emp,WorkingTimeState wts) throws SQLException;
  WorkingTimeState[] getEmployeeOfficeWorkingTime(Connection conn, long empIndex)throws SQLException;
  void deleteEmployeeOfficeWorkingTime(Connection conn, Employee emp,WorkingTimeState wts) throws SQLException;
  void updateEmployeeOfficeWorkingTime(Connection conn, Employee emp,WorkingTimeState wts) throws SQLException;

  void createPayrollComponent(PayrollComponent component, Connection conn) throws SQLException;
  void createPayrollComponentStructure(long superpayroll, long subpayroll, Connection conn) throws SQLException;
  PayrollComponent[] getAllPayrollComponent(Connection conn) throws SQLException;
  PayrollComponent getPayrollComponent(long index, Connection conn) throws SQLException;
  PayrollComponent[] getSubPayrollComponent(long superindex, Connection m_conn) throws SQLException;
  PayrollComponent[] getSuperPayrollComponent(Connection conn) throws SQLException;
  void updatePayrollComponent(long index, PayrollComponent component, Connection conn) throws SQLException;
  void deletePayrollComponent(long index, Connection conn) throws SQLException;




  void createTaxArt21Component(TaxArt21Component component, Connection conn) throws SQLException;
  void createTaxArt21ComponentStructure(long superpayroll, long subpayroll, Connection conn) throws SQLException;
  TaxArt21Component[] getAllTaxArt21Component(Connection conn,IFormulaParser parser) throws SQLException;
  TaxArt21Component getTaxArt21Component(long index, Connection conn,IFormulaParser parser) throws SQLException;
  TaxArt21Component[] getSuperTaxArt21Component(Connection conn,IFormulaParser parser) throws SQLException;
  void updateTaxArt21Component(long index, TaxArt21Component component, Connection conn) throws SQLException;
  void deleteTaxArt21Component(long index, Connection conn) throws SQLException;
  TaxArt21Component[] getSubTaxArt21Component(long superindex, Connection conn,IFormulaParser parser) throws SQLException;


  void createTaxArt21Payroll(TaxArt21Component ta21Component,PayrollComponent pcComponent,Connection conn) throws SQLException ;
  void updateTaxArt21Payroll(long index, TaxArt21Component component, Connection conn) throws SQLException;
  void deleteTaxArt21Payroll(long index,PayrollComponent paycomp, Connection conn)throws SQLException ;

   PayrollComponent[] getTaxArt21Payroll(TaxArt21Component ta21Comp,Connection conn) throws SQLException;

     void createPayrollCategory(PayrollCategory category, Connection conn) throws SQLException;
	 void updatePayrollCategory(long index, PayrollCategory category, Connection conn) throws SQLException;
	 void deletePayrollCategory(long index, Connection conn) throws SQLException;
	 PayrollCategory[] getAllPayrollCategory(Connection conn) throws SQLException;

     void createPayrollCategoryComponent(long categoryindex, PayrollCategoryComponent component, Connection conn) throws SQLException;
	 void updatePayrollCategoryComponent(long index, PayrollCategoryComponent component, Connection conn) throws SQLException;
	 void deletePayrollCategoryComponent(long index, Connection conn) throws SQLException;
	 PayrollCategoryComponent[] getAllPayrollCategoryComponent(long categoryindex, Connection conn, IFormulaParser parser, String table) throws SQLException;
	 PayrollComponent[] getNonGroupPayrollComponent(Connection conn) throws SQLException;

	 void createPayrollCategoryEmployee(long categoryindex, long employeeindex, Connection conn) throws SQLException;
	 void deletePayrollCategoryEmployee(long categoryindex, long employeeindex, Connection conn) throws SQLException;
	 Employee[] getPayrollCategoryEmployee(long categoryindex, Connection conn) throws SQLException;
	 public static interface IFormulaParser {
		 FormulaEntity parseToFormula(String formulaStr);
		 FormulaEntity parseToFormula(String formulaStr, short whichMonth, NumberRounding numberRounding);
	 }
	 Employee_n[] getEmployeeBy_Unit(Connection m_conn, long unit, String tgl) throws SQLException;
	 public Employee_n[] getEmployeeBy_Criteria(Connection conn,String query)throws SQLException;
	 PayrollCategoryComponent[] getPayrollCategoryComponent(Connection conn,long indexEmployee, long payment,long submit,IFormulaParser parser) throws SQLException;
	 PayrollCategoryComponent[] getSelectedPayrollCategoryComponent(Connection m_conn, long employeeId, String componentIds, IFormulaParser parser) throws SQLException;
	 TimeSheetSummary[] getTimeSheetSummary(Connection m_conn, long employeeId, int month, int year) throws SQLException;
	 //int getEmployeeLeavePermitionUsedByRange(long employeeindex, String startDate, String endDate, Connection m_conn) throws SQLException;

	 void createEmployeePayroll(Connection conn, EmployeePayrollSubmit payrollMealAllowanceData) throws SQLException ;
	 void createEmployeeMealAllowance(Connection conn, EmployeeMealAllowanceSubmit payrollMealAllowanceData) throws SQLException ;
	  void createEmployeePayrollDetail(Connection m_conn, EmployeePayrollSubmit payrollMealAllowanceData) throws SQLException;
	  void updateEmployeePayroll( EmployeePayrollSubmit edu, Connection conn)
		throws SQLException ;

	 void createEmployeePayrollComponent(long employeeIndex, PayrollCategoryComponent component, Connection conn) throws SQLException;
	 void updateEmployeePayrollComponent(long index, PayrollCategoryComponent component, Connection conn) throws SQLException;
	 void deleteEmployeePayrollComponent(long index, Connection conn) throws SQLException;

	void deleteEmployeePayrollComponentByEmployee(long employeeIndex, Connection m_conn) throws SQLException;
	int countPayrollCategoryEmployeeByEmployee(long sessionid, Connection conn,
			String modul, long employeeIndex) throws SQLException;

	EmployeeLeavePermissionHistory[] getEmployeeLeavePermissionHistory(
			Connection conn, long employeeIndex) throws SQLException;

	EmployeeLeavePermissionHistory[] getEmployeeLeavePermissionHistoryByQuery(Connection m_conn, String query) throws SQLException;

	EmployeeLeave[] getEmployeeLeave(Connection m_conn, String query) throws SQLException;
	EmployeeAbsence[] getEmployeeAbsence(Connection m_conn, String query) throws SQLException;
	EmployeePermition[] getEmployeePermition(Connection m_conn, String query) throws SQLException;
	EmployeeWorkTime[] getEmployeeWorkTime(Connection m_conn, String query) throws SQLException;
	EmployeeOvertime[] getEmployeeOvertime(Connection m_conn, String query) throws SQLException;

	LeavePermissionType[] getAllLeavePermissionType(Connection conn, short type) throws SQLException;

	// yang ini bingung naruhnya
	public void updateValueEmployeePayrollDetail(EmployeePayrollSubmit emp,
			Connection conn) throws SQLException;

	CurrentEmployementRpt getCurrentEmployementRpt(Connection m_conn, long projectindex) throws SQLException;

	EmploymentHistoryRpt[] getEmploymentHistoryRpt(Connection m_conn, long index) throws SQLException;

	EducationRpt[] getEducationRpt(Connection m_conn, long index) throws SQLException;


	CertificationRpt[] getCertificationRpt(Connection m_conn, long index) throws SQLException;

	BankRpt[] getBankRpt(Connection m_conn, long index) throws SQLException;

	FamilyRpt[] getFamilyRpt(Connection m_conn, long index) throws SQLException;

	PaychequesRpt[] getPaychequesRpt(Connection m_conn, long index) throws SQLException;

	PaychequesValueRpt[] getPaychequesValueRpt(Connection m_conn, long superlabel, long paycequelabel, long paycequelabel2, String bln_thn) throws SQLException;

	long getPaychequeSuperlabelAtIndex(Connection m_conn, int indexPosition) throws SQLException;

	SimpleEmployee getEmpReceivableReportByUnit(Connection conn, String query) throws SQLException;

	Account[] getDistinctTaxArt21Account(Connection m_conn) throws SQLException;

	TaxArt21Component[] getTaxArt21ComponentsByAccount(Connection m_conn, long index, IFormulaParser parser) throws SQLException;

	void createTaxArt21Submit(Connection conn, TaxArt21Submit submit) throws SQLException;

	void createTaxArt21SubmitEmployeeDetail(Connection conn, long index, TaxArt21SubmitEmployeeDetail details) throws SQLException;

	void createTaxArt21SubmitComponentDetail(Connection conn, long index, TaxArt21SubmitComponentDetail componentDetail) throws SQLException;

	TaxArt21Submit getTaxArt21Submit(Connection m_conn, int month, int year, Unit unitSelected, Account accountSelected) throws SQLException;

	TaxArt21SubmitComponentDetail[] getTaxArt21SubmitComponentDetail(Connection m_conn, long autoindex, IFormulaParser parser) throws SQLException;

	TaxArt21SubmitEmployeeDetail[] getTaxArt21SubmitEmployeeDetail(Connection m_conn, long autoindex) throws SQLException;

	Employee_n[] getEmployeeAndPTKPByUnit(Connection m_conn, long unit, String date) throws SQLException;

	Employee_n[] getEmployeeAndPTKPByCriteria(Connection m_conn, String query) throws SQLException;

	PayrollCategoryComponent[] getSelectedEmployeePayrollComponent(Connection m_conn, long employeeIndex, long componentIndex) throws SQLException;

	SimpleEmployee[] getEmployeeForPayroll(Connection m_conn, String query) throws SQLException;

	PayrollCategoryComponent getSelectedAPayrollCategoryComponent(Connection m_conn, long employeeId, long componentId, IFormulaParser parser) throws SQLException;
}
