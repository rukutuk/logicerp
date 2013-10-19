package pohaci.gumunda.titis.accounting.dbapi;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dwx
 * @version 1.0
 */

public interface IDBConstants {
//tabel master data
  public static final String TABLE_ACCOUNT = "account";
  public static final String TABLE_ACCOUNT_STRUCTURE = "accountstructure";
  public static final String TABLE_CURRENCY = "currency";
  public static final String TABLE_BANK_ACCOUNT = "bankaccount";
  public static final String TABLE_CREDITOR_LIST = "creditorlist";
  public static final String TABLE_COMPANY_LOAN = "companyloan";
  public static final String TABLE_CASH_ACCOUNT = "cashaccount";
  public static final String TABLE_JOURNAL = "journal";
  public static final String TABLE_JOURNAL_STANDARD = "journalstandard";
  public static final String TABLE_JOURNAL_STANDARD_STRUCTURE = "journalstandardstructure";
  public static final String TABLE_JOURNAL_STANDARD_ACCOUNT = "journalstandardaccount";
  public static final String TABLE_EXCHANGE_RATE = "exchangerate";
  public static final String TABLE_ACTIVITY = "activity";
  public static final String TABLE_ACTIVITY_STRUCTURE = "activitystructure";
  public static final String TABLE_UNIT = "unit";

//tabel user update
  public static final String TABLE_TRANSACTION_UPDATER = "transupdater";

//tabel setting
  public static final String TABLE_BEGINNING_BALANCE = "beginningbalance";
  public static final String TABLE_BEGINNING_BALANCE_SHEET_ENTRY = "beginningbalancesheetentry";
  public static final String TABLE_BEGINNING_BANK_DETAIL = "beginningbankdetail";
  public static final String TABLE_BEGINNING_CASH_DETAIL = "beginningcashdetail";
  public static final String TABLE_BEGINNING_WORK_IN_PROGRESS = "beginningworkinprogress";

  public static final String TABLE_JOURNAL_STANDARD_SETTING = "journalstandardsetting";
  public static final String TABLE_SIGNATURE = "signature";

  public static final String TABLE_BASE_CURRENCY = "basecurrency";
  public static final String TABLE_ACCOUNT_RECEIVABLE = "accountreceivable";
  public static final String TABLE_ACCOUNT_RECEIVABLE_DEFAULT = "accountreceivabledef";
  public static final String TABLE_ACCOUNT_PAYABLE = "accountpayable";
  public static final String TABLE_ACCOUNT_PAYABLE_DEFAULT = "accountpayabledef";

  public static final String TABLE_WORKSHEET = "worksheet";
  public static final String TABLE_WORKSHEET_COLUMN = "worksheetcolumn";
  public static final String TABLE_WORKSHEET_JOURNAL = "workshetjournal";
  public static final String TABLE_BALANCE_SHEET_REPORT = "balancesheetreport";


//tabel jurnal transaksi
  public static final String TABLE_TRANSACTION ="acctransaction";
  public static final String TABLE_TRANSACTION_VALUE ="transactionvalue";
  public static final String TABLE_TRANSACTION_POSTED ="transactionposted";
  public static final String TABLE_TRANSACTION_VALUE_POSTED ="transvalueposted";

//tabel transaksi
  public static final String TABLE_RCV_ES_DIFF = "rcvesdiff";
  public static final String TABLE_RCV_ES_DIFF_DETAIL = "rcvesdiffdetail";
  public static final String TABLE_RCV_UNIT_BANKCASH_TRNS = "rcvunitbankcashtrns";
  public static final String TABLE_RCV_UNIT_BANKCASH_TRNS_DETAIL = "rcvunitbankcashtrnsdet";
  public static final String TABLE_RCV_EMP_RECEIVABLE = "rcvempreceivable";
  public static final String TABLE_RCV_EMP_RECEIVABLE_DETAIL = "rcvempreceivabledet";
  public static final String TABLE_RCV_LOAN = "rcvloan";
  public static final String TABLE_RCV_LOAN_DETAIL = "rcvloandetail";
  public static final String TABLE_RCV_OTHERS = "rcvothers";
  public static final String TABLE_RCV_OTHERS_DETAIL = "rcvothersdetail";

  public static final String TABLE_PMT_PROJECT_COST = "pmtprojectcost";
  public static final String TABLE_PMT_PROJECT_COST_DETAIL = "pmtprojectcostdetail";
  public static final String TABLE_PMT_OPERATIONAL_COST = "pmtoperationalcost";
  public static final String TABLE_PMT_OPERATIONAL_COST_DETAIL = "pmtoperationalcostdetail";
  public static final String TABLE_PMT_CA_IOU_PROJECT = "pmtcaiouproject";
  public static final String TABLE_PMT_CA_IOU_PROJECT_INSTALLMENTS = "pmtcaiouprojectinstall";
  public static final String TABLE_PMT_CA_IOU_PROJECT_SETTLED = "pmtcaiouprojectsettled";
  public static final String TABLE_PMT_CA_IOU_PROJECT_RECEIVE = "pmtcaiouprojectreceive";
  public static final String TABLE_PMT_CA_IOU_OTHERS = "pmtcaiouothers";
  public static final String TABLE_PMT_CA_IOU_OTHERS_INSTALLMENTS = "pmtcaiouothersInstall";
  public static final String TABLE_PMT_CA_IOU_OTHERS_SETTLED = "pmtcaiouotherssettled";
  public static final String TABLE_PMT_CA_IOU_OTHERS_RECEIVE = "pmtcaiouothersreceive";
  public static final String TABLE_PMT_CA_PROJECT = "pmtcaproject";
  public static final String TABLE_PMT_CA_OTHERS = "pmtcaothers";
  public static final String TABLE_PMT_ES_DIFF = "pmtesdiff";
  public static final String TABLE_PMT_ES_DIFF_DETAIL = "pmtesdiffdetail";
  public static final String TABLE_PMT_UNIT_BANKCASH_TRNS = "pmtunitbankcashtrns";
  public static final String TABLE_PMT_UNIT_BANKCASH_TRNS_DETAIL = "pmtunitbankcashtrnsdet";
  public static final String TABLE_PMT_EMP_RECEIVABLE = "pmtempreceivable";
  public static final String TABLE_PMT_EMP_RECEIVABLE_DETAIL = "pmtempreceivabledet";
  public static final String TABLE_PMT_LOAN = "pmtloan";
  public static final String TABLE_PMT_LOAN_DETAIL = "pmtloandetail";
  public static final String TABLE_PMT_OTHERS = "pmtothers";
  public static final String TABLE_PMT_OTHERS_DETAIL = "pmtothersdetail";

  public static final String TABLE_SALES_ADVANCE = "salesadvance";
  public static final String TABLE_SALES_ADVANCE_DETAIL = "salesadvancedetail";
  public static final String TABLE_SALES_INVOICE = "salesinvoice";
  public static final String TABLE_SALES_INVOICE_DETAIL = "salesinvoicedetail";
  public static final String TABLE_SALES_ITEM = "salesitem";
  public static final String TABLE_SALES_ITEM_DETAIL = "salesitemdetail";
  public static final String TABLE_SALES_AR_RECEIVED = "salesarreceived";
  public static final String TABLE_SALES_AR_RECEIVED_DETAIL = "salesarrcvdetail";

  public static final String TABLE_PURCHASE_RECEIPT = "purchasereceipt";
  public static final String TABLE_PURCHASE_RECEIPT_DETAIL = "purchasereceiptdetail";
  public static final String TABLE_PURCHASE_RECEIPT_ITEM = "purchasereceiptitem";
  public static final String TABLE_PURCHASE_AP_PMT = "purchaseappmt";
  public static final String TABLE_PURCHASE_AP_PMT_DETAIL = "purchaseappmtdet";

  public static final String TABLE_PAYROLL_PMT_SALARY_HO = "payrollpmtslryho";
  public static final String TABLE_PAYROLL_PMT_SALARY_HO_DETAIL = "payrollpmtslryhodet";
  public static final String TABLE_PAYROLL_PMT_SALARY_HO_PAYABLE = "payrollpmtslryhopay";
  public static final String TABLE_PAYROLL_PMT_SALARY_UNIT = "payrollpmtslryunit";
  public static final String TABLE_PAYROLL_PMT_SALARY_UNIT_DETAIL = "payrollpmtslryunitdet";
  public static final String TABLE_PAYROLL_PMT_TAX21_HO = "payrollpmttax21ho";
  public static final String TABLE_PAYROLL_PMT_TAX21_HO_DETAIL = "payrollpmttax21hodet";
  public static final String TABLE_PAYROLL_PMT_TAX21_HO_PAYABLE = "payrollpmttax21hopay";
  public static final String TABLE_PAYROLL_PMT_TAX21_UNIT = "payrollpmttax21unit";
  public static final String TABLE_PAYROLL_PMT_TAX21_UNIT_DETAIL = "payrollpmttax21unitdet";
  public static final String TABLE_PAYROLL_PMT_EMP_INSURANCE = "payrollpmtempinsurance";
  public static final String TABLE_PAYROLL_PMT_EMP_INSURANCE_DETAIL = "payrollpmtempinsdet";
  public static final String TABLE_PAYROLL_PMT_EMP_INSURANCE_PAYABLE = "payrollpmtempinspay";

  public static final String TABLE_EXPENSE_SHEET = "expensesheet";
  public static final String TABLE_EXPENSE_SHEET_DETAIL = "expensesheetDetail";

  public static final String TABLE_MEMORIAL_JOURNAL_STANDARD = "memjournalstrd";
  public static final String TABLE_MEMORIAL_JOURNAL_STANDARD_DETAIL = "memjournalstrddet";
  public static final String TABLE_MEMORIAL_JOURNAL_NONSTANDARD = "memjournalnonstrd";
  public static final String TABLE_MEMORIAL_JOURNAL_NONSTANDARD_DETAIL = "memjournalnonstrddet";

  public static final String TABLE_SUBSIDIARY_ACCOUNT_SETTING = "subsidiaryaccountsetting";

  // report designer
  public static final String TABLE_INCOME_STATEMENT_DESIGN = "incomestatementdesign";
  public static final String TABLE_INCOME_STATEMENT_ACCOUNT = "incomestatementaccount";
  public static final String TABLE_INCOME_STATEMENT_ACCOUNT_STRUCTURE = "incomestatementaccountstructure";
  public static final String TABLE_BALANCE_SHEET_DESIGN = "balancesheetdesign";
  public static final String TABLE_CASH_FLOW_DESIGN = "cashflowdesign";
  public static final String TABLE_INCOME_STATEMENT_ROWS = "isrows";
  public static final String TABLE_BALANCE_SHEET_ROWS = "bsrows";
  public static final String TABLE_CASH_FLOW_ROWS = "cfrows";

  // Ditambahin sama yulan
  public static final String TABLE_DEFAULT_UNIT = "defaultunit";
  public static final String TABLE_TRANSACTION_PERIOD = "transactionperiod";

  // attr report design
  public static final String ATTR_REPORT_NAME = "name";
  public static final String ATTR_REPORT_TITLE = "title";
  public static final String ATTR_REPORT_LANG = "language";
  public static final String ATTR_REPORT_POSITIVE_BALANCE = "positivebalance";
  public static final String ATTR_ROWXML = "rowxml";
  public static final String ATTR_SECTION = "section";

  public static final String TABLE_INCOME_STATEMENT_JOURNAL = "isjournal";
  public static final String ATTR_DESIGN = "design";

  public static final String TABLE_BALANCE_SHEET_JOURNAL = "bsjournal";
  public static final String TABLE_CASH_FLOW_JOURNAL = "cfjournal";

  public static final String ATTR_ACCOUNT_LABEL = "accountlabel";
  public static final String ATTR_ISGROUP = "isgroup";
  public static final String ATTR_ISSHOWVALUE = "isshowvalue";
  public static final String ATTR_ISBOLD = "isbold";
  public static final String ATTR_ISITALIC = "isitalic";
  public static final String ATTR_ISALSOUSEDINVALUE = "isalsousedinvalue";
  public static final String ATTR_ALIGNMENT = "alignment";
  public static final String ATTR_INDENT = "indent";

  //attr table field Receive
  public static final String ATTR_RECEIVE_TO = "receiveto";
  public static final String ATTR_ES_NO = "esno";
  public static final String ATTR_RCV_ES_DIFF = "rcvesdiff";
  public static final String ATTR_EMP_RECEIVABLE = "empreceivable";
  public static final String ATTR_RCV_UNIT_CODE = "rcvunitcode";
  public static final String ATTR_TRANSFER_FROM = "transferfrom";
  public static final String ATTR_RCV_UNIT_BANKCASH_TRNS = "rcvunitbankcashtrns";
  public static final String ATTR_RCV_EMP_RECEIVABLE = "rcvempreceivable";
  public static final String ATTR_RCV_LOAN = "rcvloan";
  public static final String ATTR_COMPANY_LOAN = "companyloan";
  public static final String ATTR_RECEIVE_FROM = "receivefrom";
  public static final String ATTR_RECEIVE_OTHERS = "receiveothers";

//attr table field Payment
  public static final String ATTR_PAYMENT_SOURCE = "paymentsource";
  public static final String ATTR_PMT_PROJECT_COST = "pmtprojectcost";
  public static final String ATTR_DEPARTMENT = "department";
  public static final String ATTR_DEPARTMENT_GROUP = "departmentgroup";
  public static final String ATTR_PMT_OPERASIONAL_COST = "pmtoperationalcost";
  public static final String ATTR_PMT_CA_IOU_PROJECT = "pmtcaiouproject";
  public static final String ATTR_PMT_CA_IOU_OTHERS = "pmtcaiouothers";
  public static final String ATTR_PMT_CA_IOU_PROJECT_SETTLED = "pmtcaiouprojectsettled";
  public static final String ATTR_PMT_CA_IOU_OTHERS_SETTLED = "pmtcaiouotherssettled";
  public static final String ATTR_REQUESTED_AMOUNT = "requestedamount";
  public static final String ATTR_RCV_CASH_ACCOUNT = "rcvcashaccount";
  public static final String ATTR_RCV_BANK_ACCOUNT = "rcvbankaccount";
  public static final String ATTR_LOAN_RECEIPT = "loanreceipt";
  public static final String ATTR_PAYMENT_OTHERS = "pmtothers";
  public static final String ATTR_PMT_ES_DIFF = "pmtesdiff";
  public static final String ATTR_PMT_UNIT_BANKCASH_TRNS = "pmtunitbankcashtrns";
  public static final String ATTR_PMT_EMP_RECEIVABLE = "pmtempreceivable";
  public static final String ATTR_PMT_LOAN = "pmtloan";
  public static final String ATTR_PMT_OTHERS = "pmtothers";

//attr table field Sales
  public static final String ATTR_CUSTOMER_STATUS = "customerstatus";
  public static final String ATTR_SALES_ADV_PERCENT = "salesadvpercent";
  public static final String ATTR_SALES_ADV_CURR = "salesadvcurr";
  public static final String ATTR_SALES_ADV_EXCHRATE = "salesadvexchrate";
  public static final String ATTR_SALES_ADV_AMOUNT = "salesadvamount";
  public static final String ATTR_SALES_ADVANCE = "salesadvance";
  public static final String ATTR_SALES_AR_RECEIVED = "salesarreceived";
  public static final String ATTR_SALES_CURR = "salescurr";
  public static final String ATTR_SALES_EXCHRATE = "salesexchrate";
  public static final String ATTR_SALES_AMOUNT = "salesamount";
  public static final String ATTR_DOWNPAYMENT_CURR = "downpaymentcurr";
  public static final String ATTR_DOWNPAYMENT_AMOUNT = "downpaymentamount";
  public static final String ATTR_VAT_PERCENT = "vatpercent";
  public static final String ATTR_VAT_CURR = "vatcurr";
  public static final String ATTR_VAT_EXCHRATE = "vatexchrate";
  public static final String ATTR_VAT_AMOUNT= "vatamount";
  public static final String ATTR_BANKCHARGES_CURR = "bankchargescurr";
  public static final String ATTR_BANKCHARGES_EXCHRATE = "bankchargesexchrate";
  public static final String ATTR_BANKCHARGES_AMOUNT= "bankchargesamount";
  public static final String ATTR_RETENTION_CURR = "retentioncurr";
  public static final String ATTR_RETENTION_AMOUNT = "retentionamount";
  public static final String ATTR_TRANSLATION_CURR = "translationcurr";
  public static final String ATTR_TRANSLATION_AMOUNT = "translationamount";
  public static final String ATTR_TAX23_PERCENT = "tax23percent";
  public static final String ATTR_TAX23_CURR = "tax23curr";
  public static final String ATTR_TAX23_EXCHRATE = "tax23exchrate";
  public static final String ATTR_TAX23_AMOUNT= "tax23amount";
  public static final String ATTR_BRIEF_DESC= "briefdesc";
  public static final String ATTR_SALES_INVOICE = "salesinvoice";
  public static final String ATTR_SPECIFICATION= "specification";
  public static final String ATTR_QTY= "qty";
  public static final String ATTR_UNIT_PRICE= "unitprice";
  public static final String ATTR_PERSON_AMOUNT= "personamount";
  public static final String ATTR_PERSON_DESC= "persondesc";
  public static final String ATTR_SALES_ITEM= "salesitem";
  public static final String ATTR_INVOICE= "invoice";
  public static final String ATTR_NUMBER_INVOICE= "number";
  public static final String ATTR_SALES_AR_CURR = "salesarcurr";
  public static final String ATTR_SALES_AR_EXCHRATE = "salesarexchrate";
  public static final String ATTR_SALES_AR_AMOUNT = "salesaramount";
  public static final String ATTR_ATTENTION = "attention";

//attr table field Purchase
  public static final String ATTR_INVOICE_DATE = "invoicedate";
  public static final String ATTR_SUPPLIER = "supplier";
  public static final String ATTR_SUPPLIER_TYPE = "suppliertype";
  public static final String ATTR_AP_CURR = "apcurr";
  public static final String ATTR_AP_EXCHRATE = "apexchrate";
  public static final String ATTR_CONTRACT_NO = "contractno";
  public static final String ATTR_PURCHASE_RECEIPT = "purchasereceipt";
  public static final String ATTR_CONTRACT_DATE = "contractdate";
  public static final String ATTR_AP_PMT_CURR = "appmtcurr";
  public static final String ATTR_AP_PMT_EXCHRATE = "appmtexchrate";
  public static final String ATTR_AP_PMT_AMOUNT = "appmtamount";
  public static final String ATTR_PURCHASE_AP_PMT = "purchaseappmt";
  public static final String ATTR_PROJECT_TYPE = "projecttype";

//attr table field Payroll
  public static final String ATTR_PAYROLL_PMT_SALARY_HO = "payrollpmtslryho";
  public static final String ATTR_PAYROLL_COMP_PAYABLE = "payrollcomppay";
  public static final String ATTR_PAYROLL_PMT_SALARY_UNIT = "payrollpmtslryunit";
  public static final String ATTR_PAYROLL_PMT_TAX21_HO = "payrollpmttax21ho";
  public static final String ATTR_TAX21_PAYABLE = "tax21payable";
  public static final String ATTR_PAYROLL_PMT_TAX21_UNIT = "payrollpmttax21unit";
  public static final String ATTR_PAYROLL_PMT_EMP_INSURANCE = "payrollpmtempins";
  public static final String ATTR_INSURANCE_PAYABLE = "tax21payable";

//attr table field Expense Sheet
  public static final String ATTR_ES_PROJECT_TYPE = "esprojecttype";
  //public static final String ATTR_PMT_CA_IOU_PROJECT = "pmtcaiouproj";
  public static final String ATTR_ES_OWNER = "esowner";
  public static final String ATTR_PMT_CA_PROJECT = "pmtcaproject";
  public static final String ATTR_PMT_CA_OTHERS = "pmtcaothers";
  public static final String ATTR_EXPENSE_SHEET = "expensesheet";

//attr table field Memorial Journal
  public static final String ATTR_MEMORIAL_JOURNAL_STANDARD = "memjournalstrd";
  public static final String ATTR_MEMORIAL_JOURNAL_NONSTANDARD = "memjournalnonstrd";
  public static final String ATTR_SUBSIDIARY = "subsidiary";

  public static final String ATTR_CHEQUE_NO = "chequeno";
  public static final String ATTR_CHEQUE_DUE_DATE = "chequeduedate";
  public static final String ATTR_PAY_TO = "payto";

  public static final String ATTR_EMP_ORIGINATOR = "emporiginator";
  public static final String ATTR_JOB_TITLE_ORIGINATOR = "jobtitleoriginator";
  public static final String ATTR_DATE_ORIGINATOR = "dateoriginator";

  public static final String ATTR_EMP_APPROVED = "empapproved";
  public static final String ATTR_JOB_TITLE_APPROVED = "jobtitleapproved";
  public static final String ATTR_DATE_APPROVED = "dateapproved";

  public static final String ATTR_EMP_RECEIVED = "empreceived";
  public static final String ATTR_JOB_TITLE_RECEIVED = "jobtitlereceived";
  public static final String ATTR_DATE_RECEIVED = "datereceived";

  public static final String ATTR_EMP_CREATED = "empcreated";
  public static final String ATTR_JOB_TITLE_CREATED = "jobtitlecreated";
  public static final String ATTR_DATE_CREATED = "datecreated";

  public static final String ATTR_EMP_APPROVED1 = "empapproved1";
  public static final String ATTR_JOB_TITLE_APPROVED1 = "jobtitleapproved1";
  public static final String ATTR_DATE_APPROVED1 = "dateapproved1";

  public static final String ATTR_EMP_APPROVED2 = "empapproved2";
  public static final String ATTR_JOB_TITLE_APPROVED2 = "jobtitleapproved2";
  public static final String ATTR_DATE_APPROVED2 = "dateapproved2";

  public static final String ATTR_EMP_AUTHORIZE = "empauthorize";
  public static final String ATTR_JOB_TITLE_AUTHORIZE = "jobtitleauthorize";
  public static final String ATTR_DATE_AUTHORIZE = "dateauthorize";

  public static final String ATTR_AUTOINDEX = "autoindex";
  public static final String ATTR_ACCOUNT_CODE = "accountcode";
  public static final String ATTR_ACCOUNT_NAME = "accountname";
  public static final String ATTR_CATEGORY = "category";
  public static final String ATTR_IS_GROUP = "isgroup";
  public static final String ATTR_BALANCE_CODE = "balancecode";
  public static final String ATTR_NOTE = "note";
  public static final String ATTR_SUPER_ACCOUNT = "superaccount";
  public static final String ATTR_SUB_ACCOUNT = "subaccount";
  public static final String ATTR_CODE ="code";
  public static final String ATTR_DESCRIPTION ="description";
  public static final String ATTR_JOURNAL ="journal";
  public static final String ATTR_JOURNAL_STANDARD_ACCOUNT ="journalstandardaccount";
  public static final String ATTR_SUPER_JOURNAL = "superjournal";
  public static final String ATTR_SUB_JOURNAL = "subjournal";
  public static final String ATTR_CALCULATE = "calculate";
  public static final String ATTR_HIDDEN ="hidden";
  public static final String ATTR_STANDARD_JOURNAL = "standardjournal";

  public static final String ATTR_ACCOUNT ="account";
  public static final String ATTR_ACCOUNT1 ="account1";

 public static final String ATTR_CASH_ACCOUNT ="cashaccount";
  public static final String ATTR_CURRENCY ="currency";

  public static final String ATTR_NAME ="name";
  public static final String ATTR_SUPER_ACTIVITY = "superactivity";
  public static final String ATTR_SUB_ACTIVITY = "subactivity";
  public static final String ATTR_SYMBOL = "symbol";
  public static final String ATTR_ACCOUNT_NO = "accountno";
  public static final String ATTR_ADDRESS = "address";
  public static final String ATTR_CONTACT = "contact";
  public static final String ATTR_PHONE = "phone";
  public static final String ATTR_UNIT = "unit";
  public static final String ATTR_BANK = "bank";
  public static final String ATTR_INITIAL = "initial";
  public static final String ATTR_REMARKS = "remarks";

  public static final String ATTR_CURRENCY_FROM ="currencyfrom";
  public static final String ATTR_CURRENCY_TO ="currencyto";
  public static final String ATTR_STARTDATE = "startdate";
  public static final String ATTR_ENDDATE = "enddate";
  public static final String ATTR_EXCHANGE_VALUE = "exchangevalue";
  public static final String ATTR_REFERENCE_CURRENCY = "referencecurrency";
  public static final String ATTR_BASE_CURRENCY = "basecurrency";
  public static final String ATTR_EXCHANGE_RATE = "exchangerate";
  public static final String ATTR_VALID_FROM = "validfrom";
  public static final String ATTR_VALID_TO = "validto";
  public static final String ATTR_PROVINCE = "province";
  public static final String ATTR_COUNTRY = "country";

  public static final String ATTR_BANK_ACCOUNT = "bankaccount";
  public static final String ATTR_BANK_NAME = "bankname";
  public static final String ATTR_BANK_ADDRESS = "bankaddress";
  public static final String ATTR_CREDITOR = "creditor";
  public static final String ATTR_CREDITOR_TYPE = "creditortype";
  //public static final String ATTR_PROJECT = "project";

  public static final String ATTR_DUE_DATE = "duedate";

  public static final String ATTR_APPLICATION = "application";
  public static final String ATTR_NUMBER = "attrnumber";
  public static final String ATTR_INDEX = "attrindex";
  public static final String ATTR_WORKSHEET = "worksheet";
  public static final String ATTR_COLUMN = "attrcolumn";

  public static final String ATTR_TRANSACTION_DATE ="transactiondate";
  public static final String ATTR_TRANSACTION_CODE ="transactioncode";
  public static final String ATTR_VERIFICATION_DATE = "verificationdate";
  public static final String ATTR_POSTED_DATE = "posteddate";
  public static final String ATTR_TRANSACTION = "trans";
  public static final String ATTR_TRANSACTION_POSTED = "transactionposted";
  public static final String ATTR_VALUE = "accvalue";
  public static final String ATTR_REFERENCE_NO = "referenceno";

  public static final String ATTR_AMOUNT = "amount";
  public static final String ATTR_SUBMIT_DATE = "submitdate";
  public static final String ATTR_ENDING_BALANCE = "endingbalance";

  public static final String ATTR_STATUS = "status";
  public static final String ATTR_TYPE = "type";
  public static final String ATTR_SIGNATURE = "signature";
  public static final String ATTR_BEGINNING_BALANCE = "beginningbalance";

  // from others
  public static final String TABLE_EMPLOYEE = "employee";
  public static final String TABLE_CUSTOMER = "customer";
  public static final String TABLE_PARTNER = "partner";

  public static final String ATTR_EMPLOYEE = "employee";
  public static final String ATTR_FIRST_NAME = "firstname";
  public static final String ATTR_MIDLE_NAME = "midlename";
  public static final String ATTR_LAST_NAME = "lastname";
  public static final String ATTR_PROJECT = "project";
  public static final String ATTR_CUSTOMER = "customer";
  public static final String ATTR_PARTNER = "partner";

  public static final String ATTR_ATTRIBUTE = "attribute";

  public static final String ATTR_SUBSIDIARY_ACCOUNT = "subsidiaryaccount";

  // modul
  public static final String MODUL_ACCOUNTING = "Akun";
  public static final String MODUL_MASTER_DATA = "Master Data";

  // TAMBAHAN UNTUK VARIABLEACCOUNTSETTING
  public static final String TABLE_VARIABLE_ACCOUNT_SETTING = "variableaccountsetting";
  public static final String ATTR_VARIABLE = "variable";

  public static final String TABLE_BEGINNING_EMPRECV = "beginningempreceivable";
  public static final String TABLE_BEGINNING_CASHADVANCE = "beginningcashadvance";
  public static final String TABLE_BEGINNING_ESDIFF = "beginningesdiff";
  public static final String TABLE_BEGINNING_ACCOUNTPAYABLE = "beginningaccountpayable";
  public static final String TABLE_BEGINNING_ACCOUNT_RECEIVABLE = "beginningaccountreceivable";
  public static final String TABLE_BEGINNING_LOAN = "beginningloan";
  public static final String ATTR_PATH = "treepath";


  public static final String TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING = "trialbalancejournaltypesetting";
  public static final String ATTR_COLUMN_NAME = "columnname";
  public static final String ATTR_USED = "used";
  public static final String TABLE_TRIAL_BALANCE_ACCOUNT_TYPE_SETTING = "trialbalanceaccounttypesetting";

  public static final String TABLE_CLOSING_TRANSACTION = "closingtransaction";
  public static final String ATTR_PERIOD_FROM = "periodfrom";
  public static final String ATTR_PERIOD_TO = "periodto";
  public static final String TABLE_CLOSING_TRANSACTION_DETAIL = "closingtransactiondet";
  public static final String ATTR_CLOSING_TRANSACTION = "closingtransaction";

  // Tambahan untuk Variabel SearchVoucherDlg
  public static final String ATTR_EMP_NO = "employeeno";
  public static final String ATTR_ISVOID = "void";
  public static final String TABLE_PAYROLL_DEPARTMENT_VALUE = "payrolldeptvalue";
  public static final String TABLE_TAXART21_DEPARTMENT_VALUE = "taxart21deptvalue";

  public static final String ATTR_TOTAL = "total";
public static final String ATTR_SAY = "say";
public static final String ATTR_CENT_WORDS = "cent";
public static final String ATTR_LANGUAGE = "language";




}
