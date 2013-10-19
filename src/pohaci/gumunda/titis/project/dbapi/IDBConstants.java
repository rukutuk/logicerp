package pohaci.gumunda.titis.project.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public interface IDBConstants {
   public static final String TABLE_ACTIVITY                = "activity";
   
  public static final String TABLE_CUSTOMER_COMPANY_GROUP   = "customercompanygroup";
  public static final String TABLE_PARTNER_COMPANY_GROUP    = "partnercompanygroup";

  public static final String TABLE_CUSTOMER                 = "customer";
  public static final String TABLE_CUSTOMER_ADDRESS         = "customeraddress";
  public static final String TABLE_CUSTOMER_CONTACT         = "customercontact";
  public static final String TABLE_CUSTOMER_GROUP           = "customergroup";
  
  public static final String TABLE_EMPLOYEE                 = "employee";
  public static final String TABLE_EMPLOYEE_QUALIFICATION   = "employeequalification";
  public static final String TABLE_QUALIFICATION            = "qualification";

  public static final String TABLE_PARTNER                  = "partner";
  public static final String TABLE_PARTNER_ADDRESS          = "partneraddress";
  public static final String TABLE_PARTNER_CONTACT          = "partnercontact";
  public static final String TABLE_PARTNER_GROUP            = "partnergroup";

  public static final String TABLE_PERSONAL                 = "personal";
  public static final String TABLE_PERSONAL_HOME            = "personalhome";
  public static final String TABLE_PERSONAL_BUSINESS        = "personalbusiness";

  public static final String TABLE_PROJECT_DATA             = "projectdata";
  public static final String TABLE_PROJECT_PERSONAL         = "projectpersonal";

  public static final String TABLE_ORGANIZATION             = "organization";
  public static final String TABLE_PROJECT_ORGANIZATION_ATTACH = "projectorgattach";

  public static final String TABLE_PROJECT_CONTRACT         = "projectcontract";
  public static final String TABLE_CONTRACT_PAYMENT         = "contractpayment";
  public static final String TABLE_PROJECT_LOCATION         = "projectlocation";
  public static final String TABLE_PROJECT_PARTNER          = "projectpartner";
  public static final String TABLE_PROJECT_PARTNER_CONTACT  = "projectpartnercontact";
  public static final String TABLE_PROJECT_CLIENT_CONTACT   = "projectclientcontact";

  public static final String TABLE_UNIT                     = "unit";
  public static final String TABLE_TIME_SHEET               = "timesheet";
  public static final String TABLE_TIME_SHEET_DETAIL        = "timesheetdetail";

  public static final String TABLE_PROJECT_PROGRESS         = "projectprogress";
  public static final String TABLE_PROJECT_NOTES            = "projectnotes";

  public static final String ATTR_AUTOINDEX                 = "autoindex";
  public static final String ATTR_NAME                      = "name";
  public static final String ATTR_DESCRIPTION               = "description";

  public static final String ATTR_CODE                      = "code";
  public static final String ATTR_ADDRESS                   = "street";
  public static final String ATTR_CITY                      = "city";
  public static final String ATTR_POSTALCODE                = "postalcode";
  public static final String ATTR_PROVINCE                  = "province";
  public static final String ATTR_COUNTRY                   = "country";
  public static final String ATTR_PHONE1                    = "phone1";
  public static final String ATTR_PHONE2                    = "phone2";
  public static final String ATTR_FAX1                      = "fax1";
  public static final String ATTR_FAX2                      = "fax2";
  public static final String ATTR_EMAIL                     = "email";
  public static final String ATTR_WEBSITE                   = "website";
  public static final String ATTR_GROUP_INDEX               = "groupindex";
  public static final String ATTR_CUSTOMER_INDEX            = "customerindex";
  public static final String ATTR_ADDRESS_NAME              = "addressname";
  public static final String ATTR_PHONE                     = "phone";
  public static final String ATTR_FAX                       = "fax";
  public static final String ATTR_PARTNER_INDEX             = "partnerindex";
  public static final String ATTR_PARTNER                   = "partner";
  public static final String ATTR_TITLE                     = "title";
  public static final String ATTR_FIRST_NAME                = "firstname";
  public static final String ATTR_MIDLE_NAME                = "midlename";
  public static final String ATTR_LAST_NAME                 = "lastname";
  public static final String ATTR_NICK_NAME                 = "nickname";
  public static final String ATTR_BIRTH_PLACE               = "birthplace";
  public static final String ATTR_BIRTH_DATE                = "birthdate";
  public static final String ATTR_PERSONAL_INDEX            = "personalindex";
  public static final String ATTR_MOBILE1                   = "mobile1";
  public static final String ATTR_MOBILE2                   = "mobile2";
  public static final String ATTR_COMPANY                   = "company";
  public static final String ATTR_JOB_TITLE                 = "jobtitle";
  public static final String ATTR_OFFICE                    = "office";
  public static final String ATTR_DEPARTEMENT               = "department";
  public static final String ATTR_MOBILE_PHONE              = "mobilephone";
  public static final String ATTR_OFFICE_PHONE              = "officephone";

  public static final String ATTR_PROJECT                   = "project";
  public static final String ATTR_CUSTOMER                  = "customer";
  public static final String ATTR_UNIT                      = "unit";
  public static final String ATTR_ACTIVITY                  = "activity";
  public static final String ATTR_DEPARTMENT                = "department";
  public static final String ATTR_WORK_DESCRIPTION          = "workdescription";
  public static final String ATTR_REGDATE                   = "regdate";
  public static final String ATTR_ORNO                      = "orno";
  public static final String ATTR_PONO                      = "pono";
  public static final String ATTR_IPCNO                     = "ipcno";
  public static final String ATTR_ORDATE                    = "ordate";
  public static final String ATTR_PODATE                    = "podate";
  public static final String ATTR_IPCDATE                   = "ipcdate";
  public static final String ATTR_FILE                      = "attfile";
  public static final String ATTR_SHEET                     = "sheet";
  public static final String ATTR_EMPLOYEE                  = "employee";
  public static final String ATTR_POSITION                  = "position";
  public static final String ATTR_QUALIFICATION             = "qualification";
  public static final String ATTR_TASK                      = "task";
  public static final String ATTR_STATUS                    = "status";
  
  public static final String ATTR_EMPLOYEE_NO                  = "employeeno";

  public static final String ATTR_ESTIMATE_START_DATE       = "estimatestartdate";
  public static final String ATTR_ESTIMATE_END_DATE         = "estimateenddate";
  public static final String ATTR_ACTUAL_START_DATE         = "actualstartdate";
  public static final String ATTR_ACTUAL_END_DATE           = "actualenddate";
  public static final String ATTR_VALUE                     = "attvalue";
  public static final String ATTR_CURRENCY                  = "currency";
  public static final String ATTR_PPN                       = "ppn";
  public static final String ATTR_VALIDATION                = "validation";
  public static final String ATTR_CONTRACT                  = "contract";
  public static final String ATTR_COMPLETION                = "completion";
  public static final String ATTR_LOCATION                  = "location";

  public static final String ATTR_PREPARED_BY               = "preparedby";
  public static final String ATTR_PREPARED_DATE             = "prepareddate";
  public static final String ATTR_CHEKED_BY                 = "checkedby";
  public static final String ATTR_CHEKED_DATE               = "checkeddate";
  public static final String ATTR_APPROVAL                  = "approval";
  public static final String ATTR_APPROVAL_DATE             = "approvaldate";
  public static final String ATTR_ENTRY_DATE                = "entrydate";

  public static final String ATTR_TIME_SHEET                = "timesheet";
  public static final String ATTR_PERSONAL                  = "personal";
  public static final String ATTR_START_DATE                = "startdate";
  public static final String ATTR_FINISH_DATE               = "finishdate";
  public static final String ATTR_AREA_CODE                 = "areacode";
  public static final String ATTR_REGULER                   = "reguler";
  public static final String ATTR_HOLIDAY                   = "holiday";
  public static final String ATTR_REMARK                    = "remark";
  public static final String ATTR_DAYS                      = "days";

  public static final String ATTR_PROGRESS_DATE             = "progressdate";
  public static final String ATTR_NOTES_DATE                = "notesdate";
  public static final String ATTR_ACTION                    = "action";
  public static final String ATTR_RESPONSIBILITY            = "responsibility";
  public static final String ATTR_APPROVER                  = "approver";

  public static final String MODUL_MASTER_DATA              = "Master Data";
  public static final String MODUL_PROJECT_MANAGEMENT       = "Project Management";

}