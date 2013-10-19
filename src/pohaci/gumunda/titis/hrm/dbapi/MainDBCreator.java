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

public class MainDBCreator {

  public static void main(String[] args) {
    Connection conn = null;
    try{
      Class.forName("com.sap.dbtech.jdbc.DriverSapDB");
      conn = DriverManager.getConnection("jdbc:sapdb://172.18.9.1/sampurna", "sampurnauser", "calamari");
      conn.setAutoCommit(false);
      Statement stm = conn.createStatement();
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createOrganizationTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createOrganizationStructureTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createQualificationTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createJobTitleTable(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createWorkAgreementTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEducationTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createReligionTable(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createSexTypeTable(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createFamilyRelationTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createMaritalStatusTable(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createLeaveTypeTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createPermitionTypeTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createOfficeHourPermitionTable(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createPTKPTable(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeeTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeeEducationTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeeCertificationTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeeFamilyTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeeAccountTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmpAbsOffWorkTimeTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeePayrollSubmit(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeePayrollDetail(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeeMealAllowanceAttribute(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createTransportationAllowanceAttribute(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createOvertimeAttribute(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createEmployeePayrollComponentTable(stm);
      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createTaxart21AllowanceAttribute(stm);

      pohaci.gumunda.titis.hrm.dbapi.DBCreatorSAP.createOvertime(stm);

      stm.close();
      conn.commit();
      conn.close();
      System.out.println("ok");
    }
    catch(Exception ex){
      System.err.println(ex);
      if(conn != null)
        try{
        conn.rollback();
        }catch(Exception exe){}
    }
  }
}