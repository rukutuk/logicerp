package pohaci.gumunda.titis.hrm.logic;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 *
 * @author dewax4
 * @version 1.0
 */

/* ini tambahan nunung*/
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import pohaci.gumunda.aas.logic.AuthorizationBusinessLogic;
import pohaci.gumunda.aas.logic.AuthorizationException;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.Formula.TokenParser;
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.AnnualLeaveRight;
import pohaci.gumunda.titis.hrm.cgui.Certification;
import pohaci.gumunda.titis.hrm.cgui.DefaultWorkingDay;
import pohaci.gumunda.titis.hrm.cgui.DefaultWorkingTime;
import pohaci.gumunda.titis.hrm.cgui.Education;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeAbsence;
import pohaci.gumunda.titis.hrm.cgui.EmployeeAccount;
import pohaci.gumunda.titis.hrm.cgui.EmployeeEducation;
import pohaci.gumunda.titis.hrm.cgui.EmployeeFamily;
import pohaci.gumunda.titis.hrm.cgui.EmployeeLeave;
import pohaci.gumunda.titis.hrm.cgui.EmployeeLeavePermissionHistory;
import pohaci.gumunda.titis.hrm.cgui.EmployeeLeavePermition;
import pohaci.gumunda.titis.hrm.cgui.EmployeeMealAllowanceSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeeOfficeHourPermition;
import pohaci.gumunda.titis.hrm.cgui.EmployeeOvertime;
import pohaci.gumunda.titis.hrm.cgui.EmployeeOvertimeSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollTaxArt21;
import pohaci.gumunda.titis.hrm.cgui.EmployeePermition;
import pohaci.gumunda.titis.hrm.cgui.EmployeeSubOrdinat;
import pohaci.gumunda.titis.hrm.cgui.EmployeeTransportationAllowance;
import pohaci.gumunda.titis.hrm.cgui.EmployeeWorkTime;
import pohaci.gumunda.titis.hrm.cgui.Employee_n;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.cgui.Holiday;
import pohaci.gumunda.titis.hrm.cgui.JobTitle;
import pohaci.gumunda.titis.hrm.cgui.LeavePermissionType;
import pohaci.gumunda.titis.hrm.cgui.LeaveType;
import pohaci.gumunda.titis.hrm.cgui.NonPaychequePeriod;
import pohaci.gumunda.titis.hrm.cgui.OfficeHourPermition;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.OvertimeMultiplier;
import pohaci.gumunda.titis.hrm.cgui.PTKP;
import pohaci.gumunda.titis.hrm.cgui.PaychequeLabel;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategory;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponent;
import pohaci.gumunda.titis.hrm.cgui.PermitionType;
import pohaci.gumunda.titis.hrm.cgui.Qualification;
import pohaci.gumunda.titis.hrm.cgui.Retirement;
import pohaci.gumunda.titis.hrm.cgui.SexType;
import pohaci.gumunda.titis.hrm.cgui.SimpleEmployee;
import pohaci.gumunda.titis.hrm.cgui.SimpleEmployeeAttribute;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Component;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21SubmitComponentDetail;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21SubmitEmployeeDetail;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Tariff;
import pohaci.gumunda.titis.hrm.cgui.TimeSheetSummary;
import pohaci.gumunda.titis.hrm.cgui.WorkAgreement;
import pohaci.gumunda.titis.hrm.cgui.WorkingTimeState;
import pohaci.gumunda.titis.hrm.cgui.report.BankRpt;
import pohaci.gumunda.titis.hrm.cgui.report.CertificationRpt;
import pohaci.gumunda.titis.hrm.cgui.report.CurrentEmployementRpt;
import pohaci.gumunda.titis.hrm.cgui.report.EducationRpt;
import pohaci.gumunda.titis.hrm.cgui.report.EmploymentHistoryRpt;
import pohaci.gumunda.titis.hrm.cgui.report.FamilyRpt;
import pohaci.gumunda.titis.hrm.cgui.report.PaychequesRpt;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;

public class HRMBusinessLogic {

    protected Connection m_conn = null;
    public OrganizationLogic orgLogic = new OrganizationLogic();
    public JobTitleLogic jobTitleLogic = new JobTitleLogic();
    
    private static HRMBusinessLogic hrmbl = null;

    public static HRMBusinessLogic getInstance(Connection conn) {
        System.out.println("logic======================================");
        if (hrmbl == null) {
            hrmbl = new HRMBusinessLogic(conn);
        }
        return hrmbl;
    }

    public HRMBusinessLogic(Connection conn) {
        m_conn = conn;
    }

    public Organization[] getSuperOrganization(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSuperOrganization(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Organization[] getSubOrganization(long sessionid, String modul,
            long superindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSubOrganization(superindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    public QualificationLogic qualification = new QualificationLogic(this);

    public WorkAgreement createWorkAgreement(long sessionid, String modul,
            WorkAgreement work) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createWorkAgreement(work, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_WORK_AGREEMENT,
                    m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new WorkAgreement(index, work);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public WorkAgreement updateWorkAgreement(long sessionid, String modul,
            long index, WorkAgreement work) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateWorkAgreement(index, work, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new WorkAgreement(index, work);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public WorkAgreement[] getAllWorkAgreement(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllWorkAgreement(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteWorkAgreement(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteWorkAgreement(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Education createEducation(long sessionid, String modul, Education edu)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createEducation(edu, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_EDUCATION, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new Education(index, edu);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public Education updateEducation(long sessionid, String modul, long index,
            Education edu) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateEducation(index, edu, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new Education(index, edu);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public Education[] getAllEducation(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllEducation(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteEducation(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteEducation(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute createReligion(long sessionid, String modul,
            SimpleEmployeeAttribute attr) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createReligion(attr, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_RELIGION, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new SimpleEmployeeAttribute(index, attr.getDescription());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute updateReligion(long sessionid, String modul,
            long index, SimpleEmployeeAttribute attr) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateReligion(index, attr, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            return new SimpleEmployeeAttribute(index, attr.getDescription());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute[] getAllReligion(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllReligion(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteReligion(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteReligion(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SexType createSexType(long sessionid, String modul, SexType type)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createSexType(type, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_SEX_TYPE, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new SexType(index, type);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SexType updateSexType(long sessionid, String modul, long index,
            SexType type) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateSexType(index, type, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new SexType(index, type);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SexType[] getAllSexType(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllSexType(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteSexType(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteSexType(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute createFamilyRelation(long sessionid,
            String modul, SimpleEmployeeAttribute attr) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createFamilyRelation(attr, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_FAMILY_RELATION,
                    m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new SimpleEmployeeAttribute(index, attr.getDescription());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute updateFamilyRelation(long sessionid,
            String modul, long index, SimpleEmployeeAttribute attr)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateFamilyRelation(index, attr, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new SimpleEmployeeAttribute(index, attr.getDescription());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute[] getAllFamilyRelation(long sessionid,
            String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllFamilyRelation(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteFamilyRelation(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteFamilyRelation(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute createMaritalStatus(long sessionid,
            String modul, SimpleEmployeeAttribute attr) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createMaritalStatus(attr, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_MARITAL_STATUS,
                    m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new SimpleEmployeeAttribute(index, attr.getDescription());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute updateMaritalStatus(long sessionid,
            String modul, long index, SimpleEmployeeAttribute attr)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateMaritalStatus(index, attr, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new SimpleEmployeeAttribute(index, attr.getDescription());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute[] getAllMaritalStatus(long sessionid,
            String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllMaritalStatus(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteMaritalStatus(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteMaritalStatus(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PTKP createPTKP(long sessionid, String modul, PTKP ptkp)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
//                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createPTKP(ptkp, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_PTKP, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PTKP(index, ptkp);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PTKP updatePTKP(long sessionid, String modul, long index, PTKP ptkp)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
//                m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updatePTKP(index, ptkp, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new PTKP(index, ptkp);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PTKP[] getAllPTKP(long sessionid, String modul) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
//                m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllPTKP(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deletePTKP(long sessionid, String modul, long index)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
//                m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deletePTKP(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Tariff createTaxArt21Tariff(long sessionid, String modul,
            TaxArt21Tariff tariff) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createTaxArt21Tariff(tariff, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_TAX_ART_21_TARIFF,
                    m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new TaxArt21Tariff(index, tariff);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Tariff updateTaxArt21Tariff(long sessionid, String modul,
            long index, TaxArt21Tariff tariff) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateTaxArt21Tariff(index, tariff, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new TaxArt21Tariff(index, tariff);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Tariff[] getAllTaxArt21Tariff(long sessionid, String modul)
            throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllTaxArt21Tariff(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteTaxArt21Tariff(long sessionid, String modul, long index)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteTaxArt21Tariff(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public AllowenceMultiplier createAllowenceMultiplier(long sessionid,
            String modul, AllowenceMultiplier multiplier) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createAllowenceMultiplier(multiplier, m_conn);
            long index = isql.getMaxIndex(
                    IDBConstants.TABLE_ALLOWENCE_MULTIPLIER, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new AllowenceMultiplier(index, multiplier);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent createTaxArt21Payroll(long sessionid, String modul,
            PayrollComponent payrollComponent) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            // isql.createAllowenceMultiplier(multiplier, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_TAXART21_PAYROLL,
                    m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PayrollComponent(index, payrollComponent);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public AllowenceMultiplier updateAllowenceMultiplier(long sessionid,
            String modul, long index, AllowenceMultiplier multiplier)
            throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateAllowenceMultiplier(index, multiplier, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new AllowenceMultiplier(index, multiplier);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent updateTaxart21Payroll(long sessionid, String modul,
            long index, PayrollComponent payrollComponent) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }
            m_conn.setAutoCommit(false);
            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new PayrollComponent(index, payrollComponent);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public AllowenceMultiplier[] getAllAllowenceMultiplier(long sessionid,
            String modul) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllAllowenceMultiplier(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteTaxart21Payroll(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteAllowenceMultiplier(long sessionid, String modul,
            long index) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteAllowenceMultiplier(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public OfficeHourPermition createOfficeHourPermition(long sessionid,
            String modul, OfficeHourPermition hour) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createOfficeHourPermition(hour, m_conn);
            long index = isql.getMaxIndex(
                    IDBConstants.TABLE_OFFICE_HOUR_PERMITION, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new OfficeHourPermition(index, hour);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public OfficeHourPermition updateOfficeHourPermition(long sessionid,
            String modul, long index, OfficeHourPermition hour)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateOfficeHourPermition(index, hour, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new OfficeHourPermition(index, hour);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public OfficeHourPermition[] getAllOfficeHourPermition(long sessionid,
            String modul) throws Exception {
       //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllOfficeHourPermition(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteOfficeHourPermition(long sessionid, String modul,
            long index) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteOfficeHourPermition(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public LeaveType createLeaveType(long sessionid, String modul,
            LeaveType type) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createLeaveType(type, m_conn);
            long index = isql
                    .getMaxIndex(IDBConstants.TABLE_LEAVE_TYPE, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new LeaveType(index, type);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public LeaveType updateLeaveType(long sessionid, String modul, long index,
            LeaveType type) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateLeaveType(index, type, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new LeaveType(index, type);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public LeaveType[] getAllLeaveType(long sessionid, String modul)
            throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }
            System.out.println("3 :======================================");
            IHRMSQL isql = new HRMSQLSAP();
            LeaveType[] rv = isql.getAllLeaveType(m_conn);
            System.out.println("4 :======================================");
            return rv;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public LeaveType getLeaveType(long sessionid, String modul, long index)
            throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getLeaveType(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteLeaveType(long sessionid, String modul, long index)
            throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteLeaveType(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PermitionType createPermitionType(long sessionid, String modul,
            PermitionType type) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createPermitionType(type, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_PERMITION_TYPE,
                    m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PermitionType(index, type);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PermitionType updatePermitionType(long sessionid, String modul,
            long index, PermitionType type) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updatePermitionType(index, type, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new PermitionType(index, type);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PermitionType[] getAllPermitionType(long sessionid, String modul)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllPermitionType(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PermitionType getPermissionType(long sessionid, String modul, long index)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPermitionType(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deletePermitionType(long sessionid, String modul, long index)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deletePermitionType(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employee createEmployee(long sessionid, String modul,
            Employee employee) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createEmployee(employee, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_EMPLOYEE, m_conn);

            Qualification[] qua = employee.getQualification();
            for (int i = 0; i < qua.length; i++) {
                isql.createEmployeeQualification(index, qua[i].getIndex(),
                        m_conn);
            }

            Employment[] employment = employee.getEmployment();
            for (int i = 0; i < employment.length; i++) {
                isql.createEmployeeEmployment(index, employment[i], m_conn);
            }

            EmployeeEducation[] education = employee.getEducation();
            for (int i = 0; i < education.length; i++) {
                isql.createEmployeeEducation(index, education[i], m_conn);
            }

            Certification[] certificate = employee.getCertification();
            for (int i = 0; i < certificate.length; i++) {
                isql.createEmployeeCertification(index, certificate[i], m_conn);
            }

            EmployeeFamily[] family = employee.getFamily();
            for (int i = 0; i < family.length; i++) {
                isql.createEmployeeFamily(index, family[i], m_conn);
            }

            EmployeeAccount[] account = employee.getAccount();
            for (int i = 0; i < account.length; i++) {
                isql.createEmployeeAccount(index, account[i], m_conn);
            }

            Retirement retirement = employee.getRetirement();
            if (retirement != null) {
                isql.deleteEmployeeRetirement(index, m_conn);
                isql.createEmployeeRetirement(index, retirement, m_conn);
            }

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new Employee(index, employee);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public Employee updateEmployee(long sessionid, String modul, long index,
            Employee employee) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateEmployee(index, employee, m_conn);

            Qualification[] qua = employee.getQualification();
            isql.deleteEmployeeQualification(index, m_conn);
            for (int i = 0; i < qua.length; i++) {
                isql.createEmployeeQualification(index, qua[i].getIndex(),
                        m_conn);
            }

            Employment[] employment = employee.getEmployment();
            //SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			/*String enddate= "";
             if (!employee.getEmployment()[0].getWorkAgreement().getDescription().equals("Permanent"))
             enddate = dateformat.format(employee.getEmployment()[0].getEndDate());*/
            isql.deleteEmployeeEmployment(index, m_conn);
            for (int i = 0; i < employment.length; i++) {
                isql.createEmployeeEmployment(index, employment[i], m_conn);
            }

            EmployeeEducation[] education = employee.getEducation();
            isql.deleteEmployeeEducation(index, m_conn);
            for (int i = 0; i < education.length; i++) {
                isql.createEmployeeEducation(index, education[i], m_conn);
            }

            Certification[] certificate = employee.getCertification();
            isql.deleteEmployeeCertification(index, m_conn);
            for (int i = 0; i < certificate.length; i++) {
                isql.createEmployeeCertification(index, certificate[i], m_conn);
            }

            EmployeeFamily[] family = employee.getFamily();
            isql.deleteEmployeeFamily(index, m_conn);
            for (int i = 0; i < family.length; i++) {
                isql.createEmployeeFamily(index, family[i], m_conn);
            }

            EmployeeAccount[] account = employee.getAccount();
            isql.deleteEmployeeAccount(index, m_conn);
            for (int i = 0; i < account.length; i++) {
                isql.createEmployeeAccount(index, account[i], m_conn);
            }

            Retirement retirement = employee.getRetirement();
            if (retirement != null) {
                isql.deleteEmployeeRetirement(index, m_conn);
                isql.createEmployeeRetirement(index, retirement, m_conn);
            }

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new Employee(index, employee);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public Employee[] getAllEmployee(long sessionid, String modul)
            throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllEmployee(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployee[] getEmployeeForPayroll(long sessionid, String modul, String query)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeForPayroll(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteEmployee(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteEmployee(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employee[] getEmployeeByCriteria(long sessionid, String modul,
            String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeByCriteria(query, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Qualification[] getEmployeeQualification(long sessionid,
            String modul, long employeeindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeQualification(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employment getMaxEmployment(long sessionid, String modul,
            long employeeindex) throws Exception {
        Employment[] employments = getEmployeeEmployment(sessionid, modul, employeeindex);
        java.util.Date befdate = null, afterdate = null;
        int index = -1;

        for (int i = 0; i < employments.length; i++) {
            if (befdate == null) {
                befdate = employments[i].getTMT();
                index = i;
            }

            if (i + 1 < employments.length) {
                afterdate = employments[i + 1].getTMT();
                if (befdate.compareTo(afterdate) == -1) {
                    befdate = afterdate;
                    index = i + 1;
                }
            }
        }
        if (index >= 0) {
            return employments[index];
        }
        return null;
    }

    public Employment[] getEmployeeEmployment(long sessionid, String modul,
            long employeeindex) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeEmployment(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /*public Employment getEmpReceivableReportByUnit(long sessionid, String modul,
     String query) throws Exception {
     AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
     m_conn);

     try {
     if (!autho.isAuthorized(sessionid, modul,
     pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
     throw new AuthorizationException(
     "Authorization read of module " + modul + " denied");
     }

     IHRMSQL isql = new HRMSQLSAP();
     return isql.getEmpReceivableReportByUnit(m_conn,query);
     } catch (Exception ex) {
     throw new Exception(ex.getMessage());
     }
     }*/
    public EmployeeSubOrdinat[] getEmployeeByUnit(long sessionid, String modul,
            long unit) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeByUnit(m_conn, unit);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployee getEmployeeReceivableReportByUnit(long sessionid, String modul,
            String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmpReceivableReportByUnit(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employee getEmployeeByIndex(long sessionid, String modul,
            long index) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeByIndex(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employee getEmployeeByIndex1(long index) throws SQLException {
        IHRMSQL isql = new HRMSQLSAP();
        return isql.getEmployeeByIndex(index, m_conn);
    }

    public JobTitle getJobTitle(long sessionid, String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getJobTitle(index, m_conn);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employee_n[] getEmployeeBy_unit(long sessionid, String modul,
            long unit, String tgl) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeBy_Unit(m_conn, unit, tgl);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public Employee_n[] getEmployeeBy_Criteria(long sessionid, String modul,
            String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeBy_Criteria(m_conn, query);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public PayrollCategoryComponent[] getPayrollCategoryComponent(long sessionid,
            String modul, long indexEmployee, long payment, long submit) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPayrollCategoryComponent(m_conn, indexEmployee, payment, submit, new PayrollFormulaParser());

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeEducation[] getEmployeeEducation(long sessionid,
            String modul, long employeeindex) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeEducation(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Certification[] getEmployeeCertification(long sessionid,
            String modul, long employeeindex) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeCertification(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeFamily[] getEmployeeFamily(long sessionid, String modul,
            long employeeindex) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeFamily(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeAccount[] getEmployeeAccount(long sessionid, String modul,
            long employeeindex) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeAccount(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Retirement getEmployeeRetirement(long sessionid, String modul,
            long employeeindex) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeRetirement(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void createDefaultWorkingDay(long sessionid, String modul,
            DefaultWorkingDay day, DefaultWorkingTime time) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteDefaultWorkingDay(m_conn);
            isql.createDefaultWorkingDay(day, m_conn);
            isql.deleteDefaultWorkingTime(m_conn);
            isql.createDefaultWorkingTime(time, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public DefaultWorkingDay getDefaultWorkingDay(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getDefaultWorkingDay(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void createDefaultWorkingTime(long sessionid, String modul,
            DefaultWorkingTime time) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteDefaultWorkingTime(m_conn);
            isql.createDefaultWorkingTime(time, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public DefaultWorkingTime getDefaultWorkingTime(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getDefaultWorkingTime(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public OvertimeMultiplier createOvertimeMultiplier(long sessionid,
            String modul, OvertimeMultiplier multiplier) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createOvertimeMultiplier(multiplier, m_conn);
            long index = isql.getMaxIndex(
                    IDBConstants.TABLE_OVERTIME_MULTIPLIER, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new OvertimeMultiplier(index, multiplier);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public OvertimeMultiplier updateOvertimeMultiplier(long sessionid,
            String modul, long index, OvertimeMultiplier multiplier)
            throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateOvertimeMultiplier(index, multiplier, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new OvertimeMultiplier(index, multiplier);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public OvertimeMultiplier[] getAllOvertimeMultiplier(long sessionid,
            String modul) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
//                throw new AuthorizationException(
//                        "Authorization read of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllOvertimeMultiplier(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public OvertimeMultiplier[] getAllOvertimeMultiplierOrdered(long sessionid,
            String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllOvertimeMultiplierOrdered(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteOvertimeMultiplier(long sessionid, String modul,
            long index) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
//                throw new AuthorizationException(
//                        "Authorization update of module " + modul + " denied");
//            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteOvertimeMultiplier(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PaychequeLabel createPaychequeLabel(long sessionid, String modul,
            PaychequeLabel label) throws Exception {
//        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
//            if (!autho.isAuthorized(sessionid, modul,
//                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
//                throw new AuthorizationException(
//                        "Authorization write of module " + modul + " denied");
//            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createPaychequeLabel(label, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_PAYCHEQUE_LABEL,
                    m_conn);

            if (label.getParent() != null) {
                isql.createPaychequeLabelStructure(
                        label.getParent().getIndex(), index, m_conn);
            }

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PaychequeLabel(index, label);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PaychequeLabel updatePaychequeLabel(long sessionid, String modul,
            long index, PaychequeLabel label) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.updatePaychequeLabel(index, label, m_conn);
            return new PaychequeLabel(index, label);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PaychequeLabel[] getSuperPaychequeLabel(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSuperPaychequeLabel(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PaychequeLabel[] getSubPaychequeLabel(long sessionid, String modul,
            long superlabel) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSubPaychequeLabel(superlabel, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deletePaychequeLabel(long sessionid, String modul,
            PaychequeLabel label) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deletePaychequeLabel(label.getIndex(), m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeOfficeHourPermition createEmployeeOfficeHourPermition(
            long sessionid, String modul, long employeeindex,
            EmployeeOfficeHourPermition reason) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createEmployeeOfficeHourPermition(employeeindex, reason,
                    m_conn);
            long index = isql.getMaxIndex(
                    IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new EmployeeOfficeHourPermition(index, reason);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeOfficeHourPermition updateEmployeeOfficeHourPermition(
            long sessionid, String modul, long index,
            EmployeeOfficeHourPermition reason) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateEmployeeOfficeHourPermition(index, reason, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new EmployeeOfficeHourPermition(index, reason);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeOfficeHourPermition[] getAllEmployeeOfficeHourPermition(
            long sessionid, String modul, long employeeindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql
                    .getAllEmployeeOfficeHourPermition(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeOfficeHourPermition[] getAllEmployeeOfficeHourPermition(
            long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllEmployeeOfficeHourPermition(query, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteEmployeeOfficeHourPermition(long sessionid, String modul,
            long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteEmployeeOfficeHourPermition(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermition createEmployeeLeave(long sessionid,
            String modul, long employeeindex, EmployeeLeavePermition reason)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createEmployeeLeave(employeeindex, reason, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_EMPLOYEE_LEAVE,
                    m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new EmployeeLeavePermition(index, reason);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermition updateEmployeeLeave(long sessionid,
            String modul, long index, EmployeeLeavePermition reason)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateEmployeeLeave(index, reason, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new EmployeeLeavePermition(index, reason);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermition[] getAllEmployeeLeave(long sessionid,
            String modul, long employeeindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllEmployeeLeave(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteEmployeeLeave(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteEmployeeLeave(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermition createEmployeePermition(long sessionid,
            String modul, long employeeindex, EmployeeLeavePermition reason)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createEmployeePermition(employeeindex, reason, m_conn);
            long index = isql.getMaxIndex(
                    IDBConstants.TABLE_EMPLOYEE_PERMITION, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new EmployeeLeavePermition(index, reason);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermition updateEmployeePermition(long sessionid,
            String modul, long index, EmployeeLeavePermition reason)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateEmployeePermition(index, reason, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new EmployeeLeavePermition(index, reason);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermition[] getAllEmployeePermition(long sessionid,
            String modul, long employeeindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllEmployeePermition(employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteEmployeePermition(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteEmployeePermition(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermition[] getAllEmployeeLeavePermition(
            long sessionid, String modul, String query, short type)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllEmployeeLeavePermition(query, type, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void createNonPaychequePeriod(long sessionid, String modul,
            NonPaychequePeriod period) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteNonPaychequePeriod(m_conn);
            isql.createNonPaychequePeriod(period, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public NonPaychequePeriod getNonPaychequePeriod(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getNonPaychequePeriod(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public AnnualLeaveRight createAnnualLeaveRight(long sessionid,
            String modul, AnnualLeaveRight leave) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createAnnualLeaveRight(leave, m_conn);
            long index = isql.getMaxIndex(
                    IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new AnnualLeaveRight(index, leave);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public AnnualLeaveRight updateAnnualLeaveRight(long sessionid,
            String modul, long index, AnnualLeaveRight leave) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateAnnualLeaveRight(index, leave, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new AnnualLeaveRight(index, leave);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public AnnualLeaveRight[] getAllAnnualLeaveRight(long sessionid,
            String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllAnnualLeaveRight(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteAnnualLeaveRight(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteAnnualLeaveRight(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void createEmployeeOfficeWorkingtime(long sessionid, String modul,
            Employee emp, WorkingTimeState[] wts) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }
            m_conn.setAutoCommit(false);

            IHRMSQL isql = new HRMSQLSAP();
            for (int i = 0; i < wts.length; i++) {
                if (wts[i].inserted) {
                    isql.createEmployeeOfficeWorkingTime(m_conn, emp, wts[i]);
                    System.out.println(" overtime = " + wts[i].getOverTime()
                            + " date = " + wts[i].getDate());
                } else if (wts[i].deleted) {
                    isql.deleteEmployeeOfficeWorkingTime(m_conn, emp, wts[i]);
                } else if (wts[i].updated) {
                    isql.updateEmployeeOfficeWorkingTime(m_conn, emp, wts[i]);
                }
            }
            m_conn.commit();
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }

            throw new Exception(ex.getMessage());
        }
    }

    public WorkingTimeState[] getEmployeeOfficeWorkingTime(long sessionid,
            String modul, long empIndex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeOfficeWorkingTime(m_conn, empIndex);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public void createHoliday(long sessionid, String modul, Holiday[] holiday)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();

            for (int i = 0; i < holiday.length; i++) {
                if (holiday[i].deleted == true) {
                    isql.deleteHoliday(holiday[i].getDate(), m_conn);
                }
                if (holiday[i].inserted == true) {
                    isql.createHoliday(holiday[i], m_conn);
                    holiday[i].inserted = false;
                }
                if (holiday[i].updated == true) {
                    isql.updateHoliday(holiday[i], m_conn);
                    holiday[i].updated = false;
                }
            }

            m_conn.commit();
            m_conn.setAutoCommit(true);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public Holiday[] getAllHoliday(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllHoliday(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Holiday[] getHoliday(long sessionid, String modul,
            java.util.Date beginDate, java.util.Date endDate) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getHoliday(beginDate, endDate, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public java.util.Date getMinTMTEmployement(long sessionid, String modul,
            long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getMinTMTEmployement(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public AnnualLeaveRight getAnnualLeaveRight(long sessionid, String modul)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAnnualLeaveRight(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public int getEmployeeLeavePermitionUsed(long sessionid, String modul,
            long employeeindex, short year) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeLeavePermitionUsed(employeeindex, year,
                    m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent createPayrollComponent(long sessionid,
            String modul, PayrollComponent component) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createPayrollComponent(component, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_PAYROLL_COMPONENT,
                    m_conn);
            System.out.println("index = " + index);

            if (component.getParent() != null) {
                isql.createPayrollComponentStructure(component.getParent()
                        .getIndex(), index, m_conn);
            }

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PayrollComponent(index, component);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Component createTaxArt21Component(long sessionid,
            String modul, TaxArt21Component component) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createTaxArt21Component(component, m_conn);
            long index = isql.getMaxIndex(
                    IDBConstants.TABLE_TAXART21_COMPONENT, m_conn);
            if (component.getParent() != null) {
                isql.createTaxArt21ComponentStructure(component.getParent()
                        .getIndex(), index, m_conn);
            }
            component.setIndex(index);
            /*for (int i = 0; i < payComp.length; i++) {
             isql.createTaxArt21Payroll(component, payComp[i], m_conn);
             }*/

            m_conn.commit();
            //System.out.println("ndex = " + index + " component.getParent() = "
            //		+ component.getParent());
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return component;
        } catch (Exception ex) {
            ex.printStackTrace();
            //ex.printStackTrace();

            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent updatePayrollComponent(long sessionid,
            String modul, long index, PayrollComponent component)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.updatePayrollComponent(index, component, m_conn);
            return new PayrollComponent(index, component);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Component updateTaxArt21Component(long sessionid,
            String modul, TaxArt21Component ta21component) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            // isql.deleteTaxArt21Payroll(ta21component.getIndex(), m_conn);

            // createTaxArt21Component(sessionid, modul, ta21component,
            // pccomponent);

            isql.updateTaxArt21Component(sessionid, ta21component, m_conn);
            return ta21component;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Component updateTaxArt21Payroll_DUMMY(long sessionid,
            String modul, TaxArt21Component ta21component,
            PayrollComponent[] pccomponent) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }
            return ta21component;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Component updateTaxArt21Payrol(long sessionid, String modul,
            TaxArt21Component ta21component, PayrollComponent[] payCompActual)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        System.out.println(ta21component.getIndex());

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }


            PayrollComponent[] payCompRepository = getTaxArt21Payroll(
                    sessionid, modul, ta21component);

            Vector repoVector = new Vector();
            Vector repoVector2 = new Vector();
            Vector repoStringVector = new Vector();

            // ArrayList al=new ArrayList();

            for (int i = 0; i < payCompRepository.length; i++) {
                repoVector.add(payCompRepository[i]);
                //repoVector2.add(payCompRepository[i]);
                repoStringVector.add(payCompRepository[i].getCode());
            }
            Vector actualVector = new Vector();
            Vector actualStringVector = new Vector();

            for (int i = 0; i < payCompActual.length; i++) {
                actualVector.add(payCompActual[i]);
                actualStringVector.add(payCompActual[i].getCode());
            }

            for (int i = 0; i < repoStringVector.size(); i++) {
                boolean flag = true;
                String code = (String) repoStringVector.get(i);
                if (actualStringVector.contains(code)) {
                    int in = actualStringVector.indexOf(code);
                    actualStringVector.remove(in);
                    actualVector.remove(in);
                    System.out.println(i);
                    flag = false;
                    //repoVector2.removeElementAt(i);
                }
                if (flag) {
                    repoVector2.add(repoVector.get(i));
                }

            }

            PayrollComponent[] newPayComp = new PayrollComponent[actualVector
                    .size()];
            actualVector.copyInto(newPayComp);


            IHRMSQL isql = new HRMSQLSAP();
            for (int i = 0; i < newPayComp.length; i++) {
                System.out.println(" ta21component.getIndex() = " + ta21component.getIndex()
                        + "newPayComp[i].getIndex() = " + newPayComp[i].getIndex());
                isql.createTaxArt21Payroll(ta21component, newPayComp[i],
                        m_conn);
            }

            PayrollComponent[] newPayComp2 = new PayrollComponent[repoVector2.size()];
            repoVector2.copyInto(newPayComp2);

            for (int i = 0; i < newPayComp2.length; i++) {
                System.out.println(" ta21component.getIndex() = " + ta21component.getIndex()
                        + "newPayComp[i].getIndex() = " + newPayComp2[i].getIndex());
                isql.deleteTaxArt21Payroll(ta21component.getIndex(),
                        newPayComp2[i], m_conn);
            }

            return ta21component;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent[] getTaxArt21Payroll(long sessionid, String modul,
            TaxArt21Component ta21Comp) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getTaxArt21Payroll(ta21Comp, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public PayrollComponent[] getSubPayrollComponent(long sessionid,
            String modul, long superindex) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSubPayrollComponent(superindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Component[] getSubTaxArt21Component(long sessionid,
            String modul, long superindex) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSubTaxArt21Component(superindex, m_conn, new PayrollFormulaParser());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent[] getSuperPayrollComponent(long sessionid,
            String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSuperPayrollComponent(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Component[] getSuperTaxArt21Component(long sessionid,
            String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSuperTaxArt21Component(m_conn, new PayrollFormulaParser());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deletePayrollComponent(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deletePayrollComponent(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteTaxArt21Component(long sessionid, String modul, long index)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteTaxArt21Component(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    // tambahan nunung
    public Organization getOrganizationByIndex(long index) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
        //		m_conn);
        try {
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getOrganizationByIndex(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SexType getSexType(long index) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
        //		m_conn);
        try {
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSexType(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute getReligion(long index) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
        //		m_conn);
        try {
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getReligion(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public SimpleEmployeeAttribute getMaritalStatus(long index)
            throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
        //		m_conn);
        try {
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getMaritalStatus(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PTKP getPTKP(long index) throws Exception {
        //AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
        //		m_conn);
        try {
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPTKP(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employee[] getPayrollCategoryEmployee(long sessionid,
            String modul, long categoryindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPayrollCategoryEmployee(categoryindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deletePayrollCategoryEmployee(long sessionid, String modul,
            long categoryindex, long employeeindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException("Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deletePayrollCategoryEmployee(categoryindex, employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategory[] getAllPayrollCategory(long sessionid,
            String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllPayrollCategory(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent getPayrollComponent(long sessionid, String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException("Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPayrollComponent(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategoryComponent createPayrollCategoryComponent(long sessionid,
            String modul, long categoryindex, PayrollCategoryComponent component) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createPayrollCategoryComponent(categoryindex, component, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PayrollCategoryComponent(index, component.getPayrollComponent(), component.getFormulaEntity());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategoryComponent updatePayrollCategoryComponent(long sessionid,
            String modul, long index, PayrollCategoryComponent component)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updatePayrollCategoryComponent(index, component, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new PayrollCategoryComponent(index, component.getPayrollComponent(), component.getFormulaEntity());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategoryComponent[] getAllPayrollCategoryComponent(long sessionid,
            String modul, long categoryindex, String table) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllPayrollCategoryComponent(categoryindex, m_conn,
                    new PayrollFormulaParser(), table);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public class PayrollFormulaParser implements IHRMSQL.IFormulaParser {

        TokenParser itemParser = new PayrollTokenParser(HRMBusinessLogic.this.m_conn);
        Formula mformula = new Formula();

        public FormulaEntity parseToFormula(String formulaStr) {
            try {
                short whichMonth = 0;
                short roundingMode = -1;
                int precision = 0;
                if (formulaStr == null) {
                    return null;
                }
                mformula.parseFormula(formulaStr, itemParser);
                return mformula.createFormulaEntity(whichMonth, new NumberRounding(roundingMode, precision));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public FormulaEntity parseToFormula(String formulaStr, short whichMonth, NumberRounding numberRounding) {
            try {
                mformula.parseFormula(formulaStr, itemParser);
                return mformula.createFormulaEntity(whichMonth, numberRounding);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void deletePayrollCategoryComponent(long sessionid, String modul,
            long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException("Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deletePayrollCategoryComponent(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategory createPayrollCategory(long sessionid,
            String modul, PayrollCategory category) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createPayrollCategory(category, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_PAYROLL_CATEGORY, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PayrollCategory(index, category);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategory updatePayrollCategory(long sessionid,
            String modul, long index, PayrollCategory category)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updatePayrollCategory(index, category, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new PayrollCategory(index, category);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public void deletePayrollCategory(long sessionid, String modul,
            long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException("Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deletePayrollCategory(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void createPayrollCategoryEmployee(long sessionid, String modul,
            long categoryindex, long employeeindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException("Authorization write of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.createPayrollCategoryEmployee(categoryindex, employeeindex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollComponent[] getNonGroupPayrollComponent(long sessionid, String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException("Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getNonGroupPayrollComponent(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /*
     public Formula parsePayrollComponentFormula(String input)  throws Exception {
     java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(input);
     String formulaview = "";
     java.util.Vector vresult = new java.util.Vector();
     IHRMSQL isql = new HRMSQLSAP();

     int tokencount = tokenizer.countTokens();
     if(tokencount > 0){
     for(int i = 0; i < tokencount; i++){
     String strtoken = tokenizer.nextToken();
     if(strtoken.trim().startsWith(FormulaHelper.ITEM)){
     strtoken = strtoken.substring(FormulaHelper.ITEM.length(), strtoken.length() - FormulaHelper.ITEM.length());

     PayrollComponent compnent = isql.getPayrollComponent(Long.parseLong(strtoken),this.m_conn);
     vresult.addElement(new Integer(Formula.ITEM));
     if(i > 0)
     formulaview += " ";
     formulaview += compnent.toString();
     }
     else {
     if(isNumber(strtoken.trim()))
     strtoken = strtoken.replace('.',',');
     if(i > 0)
     formulaview += " ";
     formulaview += strtoken.trim();

     if(strtoken.trim().length() > 0){
     char[] chars = strtoken.trim().toCharArray();
     for(int ii=0; ii  < chars.length;ii++){
     char c = chars[ii];
     if(c == ',')
     vresult.addElement(new Integer(Formula.COMMA));
     else if(c == '(')
     vresult.addElement(new Integer(Formula.OPEN));
     else if(c == ')')
     vresult.addElement(new Integer(Formula.CLOSE));
     else if((c == '*') || (c == '+') || (c == '-') || (c == '/'))
     vresult.addElement(new Integer(Formula.OPERATOR));
     else
     vresult.addElement(new Integer(Formula.NUMBER));
     }
     }
     }
     }
     }

     Formula formula = new Formula(input, formulaview, (short)0 //whichMonth
     );
     formula.setFormulaStatus(vresult);
     return formula;
     }
     */
    public PayrollCategoryComponent[] getSelectedPayrollCategoryComponent(long sessionid, String modul, long employeeId,
            PayrollComponent[] components) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            String componentIds = "";
            for (int i = 0; i < components.length; i++) {
                if (componentIds.equals("")) {
                    componentIds += String.valueOf(components[i].getIndex());
                } else {
                    componentIds += ", " + String.valueOf(components[i].getIndex());
                }
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSelectedPayrollCategoryComponent(m_conn, employeeId,
                    componentIds, new PayrollFormulaParser());

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategoryComponent getSelectedAPayrollCategoryComponent(
            long sessionid, String modul, long employeeId,
            long componentId) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }


            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSelectedAPayrollCategoryComponent(m_conn, employeeId,
                    componentId, new PayrollFormulaParser());

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public TimeSheetSummary[] getTimeSheetSummary(
            long sessionid, String modul, long employeeId,
            int month, int year) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }


            IHRMSQL isql = new HRMSQLSAP();
            return isql.getTimeSheetSummary(m_conn, employeeId,
                    month, year);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void updateEmployeePayroll(long sessionid, String modul,
            EmployeePayrollSubmit employeepayrollSubmit) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            HRMSQLSAP isql = new HRMSQLSAP();
            isql.updateEmployeePayroll(employeepayrollSubmit, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public void updateEmployeePayrollVerified(long index, long sessionid, String modul,
            EmployeePayrollSubmit employeepayrollSubmit) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            HRMSQLSAP isql = new HRMSQLSAP();
            isql.updateEmployeePayrollVerified(index, employeepayrollSubmit, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public void createEmployeePayroll(long sessionid, String modul,
            EmployeePayrollSubmit employeePayrollSubmit) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }
            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            HRMSQLSAP isql = new HRMSQLSAP();
            isql.createEmployeePayroll(m_conn, employeePayrollSubmit);
            long index = isql.getMaxIndex(IDBConstants.TABLE_EMPLOYEE_PAYROLL, m_conn);
            employeePayrollSubmit.setIndex(index);
            if (employeePayrollSubmit instanceof EmployeeTransportationAllowance) {
                EmployeeTransportationAllowance new_name = (EmployeeTransportationAllowance) employeePayrollSubmit;
                isql.createTransportationAllowance(m_conn, new_name);
            }
            if (employeePayrollSubmit instanceof EmployeeOvertimeSubmit) {
                EmployeeOvertimeSubmit new_name = (EmployeeOvertimeSubmit) employeePayrollSubmit;
                isql.createOvertimePayrolSubmit(m_conn, new_name);
            }
            if (employeePayrollSubmit instanceof EmployeeMealAllowanceSubmit) {
                EmployeeMealAllowanceSubmit new_name = (EmployeeMealAllowanceSubmit) employeePayrollSubmit;
                isql.createEmployeeMealAllowance(m_conn, new_name);
            } else {
            }
            isql.createEmployeePayrollDetail(m_conn, employeePayrollSubmit);
            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public void createEmployeePayrollSubmit(long sessionid, String modul,
            EmployeePayrollSubmit[] employeePayrollSubmit) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }
            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            HRMSQLSAP isql = new HRMSQLSAP();
            long index = -1;
            if (employeePayrollSubmit[0] != null) {
                isql.createEmployeePayroll(m_conn, employeePayrollSubmit[0]);
                index = isql.getMaxIndex(IDBConstants.TABLE_EMPLOYEE_PAYROLL, m_conn);
            }
            for (int i = 0; i < employeePayrollSubmit.length; i++) {
                employeePayrollSubmit[i].setIndex(index);
                isql.createEmployeePayrollDetail(m_conn, employeePayrollSubmit[i]);
                if (employeePayrollSubmit[i] instanceof EmployeeTransportationAllowance) {
                    EmployeeTransportationAllowance new_name = (EmployeeTransportationAllowance) employeePayrollSubmit[i];
                    isql.createTransportationAllowance(m_conn, new_name);
                }
                if (employeePayrollSubmit[i] instanceof EmployeeOvertimeSubmit) {
                    EmployeeOvertimeSubmit new_name = (EmployeeOvertimeSubmit) employeePayrollSubmit[i];
                    isql.createOvertimePayrolSubmit(m_conn, new_name);
                }
                if (employeePayrollSubmit[i] instanceof EmployeeMealAllowanceSubmit) {
                    EmployeeMealAllowanceSubmit new_name = (EmployeeMealAllowanceSubmit) employeePayrollSubmit[i];
                    isql.createEmployeeMealAllowance(m_conn, new_name);
                }
                if (employeePayrollSubmit[i] instanceof EmployeePayrollTaxArt21) {
                    EmployeePayrollTaxArt21 new_name = (EmployeePayrollTaxArt21) employeePayrollSubmit[i];
                    long indexx = isql.getMaxIndex(IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL, m_conn);
                    new_name.setIndex(indexx);
                    isql.createTaxArt21Allowance(m_conn, new_name, indexx);
                } else {
                }

            }
            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public void createEmployeeVerification(long sessionid, String modul,
            EmployeePayrollSubmit[] employeePayrollSubmit) throws Exception {

        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }
            m_conn.setAutoCommit(false);

            HRMSQLSAP isql = new HRMSQLSAP();
            long index = -1;
            if (employeePayrollSubmit[0] != null) {
                isql.createEmployeePayroll(m_conn, employeePayrollSubmit[0]);
                index = isql.getMaxIndex(IDBConstants.TABLE_EMPLOYEE_PAYROLL, m_conn);
            }
            for (int i = 0; i < employeePayrollSubmit.length; i++) {
                employeePayrollSubmit[i].setIndex(index);
                if (employeePayrollSubmit[i] instanceof EmployeeTransportationAllowance) {
                    EmployeeTransportationAllowance new_name = (EmployeeTransportationAllowance) employeePayrollSubmit[i];
                    isql.createTransportationAllowance(m_conn, new_name);
                }
                if (employeePayrollSubmit[i] instanceof EmployeeOvertimeSubmit) {
                    EmployeeOvertimeSubmit new_name = (EmployeeOvertimeSubmit) employeePayrollSubmit[i];
                    isql.createOvertimePayrolSubmit(m_conn, new_name);
                }
                if (employeePayrollSubmit[i] instanceof EmployeeMealAllowanceSubmit) {
                    EmployeeMealAllowanceSubmit new_name = (EmployeeMealAllowanceSubmit) employeePayrollSubmit[i];
                    isql.createEmployeeMealAllowance(m_conn, new_name);
                } else {
                }
                isql.createEmployeePayrollDetail(m_conn, employeePayrollSubmit[i]);
            }
            m_conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategoryComponent createEmployeePayrollComponent(long sessionid,
            String modul, long employeeIndex, PayrollCategoryComponent component) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createEmployeePayrollComponent(employeeIndex, component, m_conn);
            long index = isql.getMaxIndex(IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);

            return new PayrollCategoryComponent(index, component.getPayrollComponent(), component.getFormulaEntity());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategoryComponent updateEmployeePayrollComponent(long sessionid,
            String modul, long index, PayrollCategoryComponent component)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            IHRMSQL isql = new HRMSQLSAP();
            isql.updateEmployeePayrollComponent(index, component, m_conn);

            m_conn.commit();
            m_conn.setAutoCommit(true);

            return new PayrollCategoryComponent(index, component.getPayrollComponent(), component.getFormulaEntity());
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteEmployeePayrollComponent(long sessionid, String modul,
            long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException("Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteEmployeePayrollComponent(index, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void deleteEmployeePayrollComponentByEmployee(long sessionid, String modul,
            long employeeIndex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException("Authorization update of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.deleteEmployeePayrollComponentByEmployee(employeeIndex, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public int countPayrollCategoryEmployeeByEmployee(long sessionid, String modul,
            long employeeIndex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException("Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.countPayrollCategoryEmployeeByEmployee(sessionid, m_conn, modul,
                    employeeIndex);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeePayrollSubmit[] getEmployeePayrollSubmit(long sessionid,
            String modul, EmployeePayrollSubmit employeepayrollSubmit) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            HRMSQLSAP isql = new HRMSQLSAP();
            return isql.getEmployeePayrollSubmit(
                    m_conn, employeepayrollSubmit);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeePayrollSubmit[] getAllPostedEmployeePayrollSubmit(long sessionid,
            String modul, int month, int year, Unit unit, Employee_n employee) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }
            HRMSQLSAP isql = new HRMSQLSAP();
            return isql.getAllPostedEmployeePayrollSubmit(
                    m_conn, month, year, unit, employee);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeePayrollSubmit[] getEmployeePayrollSubmitByCriteria(long sessionid,
            String modul, EmployeePayrollSubmit employeepayrollSubmit, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }
            HRMSQLSAP isql = new HRMSQLSAP();
            return isql.getEmployeePayrollSubmitByCriteria(
                    m_conn, employeepayrollSubmit, query);
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermissionHistory[] getEmployeeLeavePermissionHistory(
            long sessionid, String modul, long employeeIndex)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeLeavePermissionHistory(m_conn, employeeIndex);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeLeavePermissionHistory[] getEmployeeLeavePermissionHistoryByQuery(
            long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql
                    .getEmployeeLeavePermissionHistoryByQuery(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public LeavePermissionType[] getAllLeavePermissionType(
            long sessionid, String modul, short type) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getAllLeavePermissionType(m_conn, type);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    // tambahan nunung
    public EmployeeLeave[] getEmployeeLeave(
            long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeLeave(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public CurrentEmployementRpt getCurrentEmployementRpt(long sessionid, String modul, long projectindex) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException("Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getCurrentEmployementRpt(m_conn, projectindex);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmploymentHistoryRpt[] getEmploymentHistoryRpt(
            long sessionid, String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmploymentHistoryRpt(m_conn, index);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EducationRpt[] getEducationRpt(
            long sessionid, String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEducationRpt(m_conn, index);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public FamilyRpt[] getFamilyRpt(
            long sessionid, String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getFamilyRpt(m_conn, index);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public CertificationRpt[] getCertificationRpt(
            long sessionid, String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getCertificationRpt(m_conn, index);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());

        }
    }

    public BankRpt[] getBankRpt(
            long sessionid, String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getBankRpt(m_conn, index);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());

        }
    }

    public PaychequesRpt[] getPaychequesRpt(
            long sessionid, String modul, long superlabel) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPaychequesRpt(m_conn, superlabel);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());

        }
    }

    public PaychequesValueRpt[] getPaychequesValueRpt(
            long sessionid, String modul, long indexemployee, long superlabel, long paycequelabel,
            String bln_thn) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPaychequesValueRpt(m_conn, indexemployee, superlabel, paycequelabel, bln_thn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());

        }
    }

    public EmployeeAbsence[] getEmployeeAbsence(
            long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeAbsence(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeOvertime[] getEmployeeOvertime(
            long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeOvertime(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeePermition[] getEmployeePermition(
            long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeePermition(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void updateValueEmployeePayrollDetail(long sessionid, String modul,
            EmployeePayrollSubmit emp) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            isql.updateValueEmployeePayrollDetail(emp, m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public EmployeeWorkTime[] getEmployeeWorkTime(
            long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeWorkTime(m_conn, query);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public long getPaychequeSuperlabelAtIndex(long sessionid, String modul, int indexPosition) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getPaychequeSuperlabelAtIndex(m_conn, indexPosition);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Account[] getDistinctTaxArt21Account(long sessionid, String modul) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getDistinctTaxArt21Account(m_conn);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Component[] getTaxArt21ComponentsByAccount(long sessionid,
            String modul, long index) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getTaxArt21ComponentsByAccount(m_conn, index, new PayrollFormulaParser());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public TaxArt21Submit createTaxArt21Submit(long sessionid,
            String modul, TaxArt21Submit submit) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        int trans = Connection.TRANSACTION_READ_COMMITTED;

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
                throw new AuthorizationException(
                        "Authorization write of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            trans = m_conn.getTransactionIsolation();
            m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            IHRMSQL isql = new HRMSQLSAP();
            isql.createTaxArt21Submit(m_conn, submit);
            long index = isql.getMaxIndex(IDBConstants.TABLE_TAX_ART_21_SUBMIT,
                    m_conn);

            submit.setIndex(index);
            TaxArt21SubmitEmployeeDetail[] details = submit.getEmployeeDetails();
            for (int i = 0; i < details.length; i++) {
                TaxArt21SubmitEmployeeDetail detail = details[i];
                isql.createTaxArt21SubmitEmployeeDetail(m_conn, index, detail);

                long idx = isql.getMaxIndex(IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL, m_conn);

                detail.setAutoindex(idx);

                TaxArt21SubmitComponentDetail[] componentDetails = detail.getComponentDetails();
                for (int j = 0; j < componentDetails.length; j++) {
                    TaxArt21SubmitComponentDetail componentDetail = componentDetails[j];
                    isql.createTaxArt21SubmitComponentDetail(m_conn, idx, componentDetail);
                }
            }


            m_conn.commit();
            m_conn.setAutoCommit(true);
            m_conn.setTransactionIsolation(trans);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
                m_conn.setTransactionIsolation(trans);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage());
        }
        return submit;
    }

    public TaxArt21Submit getFullTaxArt21Submit(long sessionid,
            String modul, int month, int year, Unit unitSelected,
            Account accountSelected) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            TaxArt21Submit submitted = isql.getTaxArt21Submit(m_conn, month, year, unitSelected, accountSelected);

            if (submitted == null) {
                return null;
            }

            TaxArt21SubmitEmployeeDetail[] employeeDetails =
                    isql.getTaxArt21SubmitEmployeeDetail(m_conn, submitted.getIndex());

            for (int i = 0; i < employeeDetails.length; i++) {
                TaxArt21SubmitEmployeeDetail employeeDetail = employeeDetails[i];

                TaxArt21SubmitComponentDetail[] componentDetails =
                        isql.getTaxArt21SubmitComponentDetail(m_conn, employeeDetail.getAutoindex(), new PayrollFormulaParser());

                employeeDetail.setComponentDetails(componentDetails);
            }

            submitted.setEmployeeDetails(employeeDetails);

            return submitted;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void updateTaxArt21Submit(long sessionid, String modul, Transaction transaction, TaxArt21Submit taxSubmitted) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }

            m_conn.setAutoCommit(false);
            HRMSQLSAP isql = new HRMSQLSAP();
            isql.updateTaxArt21Submit(m_conn, transaction, taxSubmitted);

            m_conn.commit();
            m_conn.setAutoCommit(true);
        } catch (Exception ex) {
            try {
                m_conn.rollback();
                m_conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            throw new Exception(ex.getMessage(), ex);
        }
    }

    public Employee_n[] getEmployeeAndPTKPByUnit(long sessionid, String modul, long unit, String date) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeAndPTKPByUnit(m_conn, unit, date);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Employee_n[] getEmployeeAndPTKPByCriteria(long sessionid, String modul, String query) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }
            IHRMSQL isql = new HRMSQLSAP();
            return isql.getEmployeeAndPTKPByCriteria(m_conn, query);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public Hashtable getAllValueForTax(long sessionid,
            String modul, int month, int year, Unit unit, Employee_n employee) throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);
        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
                throw new AuthorizationException(
                        "Authorization update of module " + modul + " denied");
            }
            HRMSQLSAP isql = new HRMSQLSAP();
            return isql.getAllValueForTax(
                    m_conn, month, year, unit, employee);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public PayrollCategoryComponent[] getSelectedEmployeePayrollComponent(
            long sessionid, String modul, long employeeIndex, long componentIndex)
            throws Exception {
        AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
                m_conn);

        try {
            if (!autho.isAuthorized(sessionid, modul,
                    pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
                throw new AuthorizationException(
                        "Authorization read of module " + modul + " denied");
            }

            IHRMSQL isql = new HRMSQLSAP();
            return isql.getSelectedEmployeePayrollComponent(m_conn,
                    employeeIndex, componentIndex);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
