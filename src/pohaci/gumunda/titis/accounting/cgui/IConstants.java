package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public interface IConstants {
  public static final String APP_ACCOUNTING = "Accounting";

  public static final String RECEIVE_EXPENSE_SHEET_DIFFERENCE = "Receive Expense Sheet Difference";
  public static final String RECEIVE_UNIT_BANK_CASH_TRANSFER = "Receive Unit Bank/Cash Transfer";
  public static final String RECEIVE_EMPLOYEE_RECEIVABLE = "Receive Employee Receivable";
  public static final String RECEIVE_LOAN = "Receive Loan";
  public static final String RECEIVE_OTHERS = "Receive Others";

  public static final String PAYMENT_PROJECT_COST = "Project Cost Payment";
  public static final String PAYMENT_OPERASIONAL_COST = "Operational Cost Payment";
  public static final String PAYMENT_CASHADVANCE_IOU_PROJECT = "Cash Advance I Owe You Project";
  public static final String PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT = "Cash Advance I Owe You Project Settlement";
  public static final String PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT = "Cash Advance I Owe You Project Installment";
  public static final String PAYMENT_CASHADVANCE_IOU_PROJECT_RECEIVE = "Cash Advance I Owe You Project Receive";
  public static final String PAYMENT_CASHADVANCE_IOU_OTHERS = "Cash Advance I Owe You Others";
  public static final String PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT = "Cash Advance I Owe You Others Settlement";
  public static final String PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT = "Cash Advance I Owe You Others Installment";
  public static final String PAYMENT_CASHADVANCE_IOU_OTHERS_RECEIVE = "Cash Advance I Owe You Others Receive";
  public static final String PAYMENT_CASHADVANCE_PROJECT = "Cash Advance Project Payment";
  public static final String PAYMENT_CASHADVANCE_OTHERS = "Cash Advance Others Payment";
  public static final String PAYMENT_EXPENSESHEET_DIFFERENCE = "Expense Sheet Difference Payment";
  public static final String PAYMENT_UNIT_BANKCASH_TRANSFER = "Unit Bank/Cash Transfer Payment";
  public static final String PAYMENT_EMPLOYEE_RECEIVABLE = "Employee Receivable Payment";
  public static final String PAYMENT_LOAN = "Loan Payment";
  public static final String PAYMENT_OTHERS = "Others Payment";

  public static final String SALES_ADVANCE = "Sales Advance Receive";
  public static final String SALES_INVOICE = "Invoice";
  public static final String SALES_AR_RECEIVE = "Account Receivable Receive";

  public static final String PURCHASE_RECEIPT = "Purchase Receipt";
  public static final String PURCHASE_AP_PAYMENT = "Account Payable Payment";

  public static final String PAYROLL_VERIFICATION_PAYCHEQUES = "Paycheque Verification";
  public static final String PAYROLL_VERIFICATION_MEAL_ALLOWANCES = "Meal Allowance Verification";
  public static final String PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES = "Transportation Allowances Verification";
  public static final String PAYROLL_VERIFICATION_OVERTIME = "Overtime Verification";
  public static final String PAYROLL_VERIFICATION_OTHER_ALLOWANCE = "Other Allowance Verification";
  public static final String PAYROLL_VERIFICATION_INSURANCE_ALLOWANCE = "Insurance Allowance Verification";
  public static final String PAYROLL_VERIFICATION_TAX21 = "Tax21 Verification";

  public static final String PAYROLL_PAYMENT_SALARY_HO = "Head Office Payroll Payment";
  public static final String PAYROLL_PAYMENT_SALARY_UNIT = "Unit Payroll Payment";
  public static final String PAYROLL_PAYMENT_TAX21_HO = "Head Office Tax Art 21 Payment";
  public static final String PAYROLL_PAYMENT_TAX21_UNIT = "Unit Tax Art 21 Payment";
  public static final String PAYROLL_PAYMENT_EMPLOYEE_INSURANCE = "Employee Insurance Payment";

  public static final String EXPENSE_SHEET_PROJECT = "Project Expense Sheet";
  public static final String EXPENSE_SHEET_OTHERS = "Others Expense Sheet";
  public static final String MJ_STANDARD = "Memorial Journal Standard";
  public static final String MJ_NONSTANDARD_PROJECT  = "Memorial Journal Non Standard Project";
  public static final String MJ_NONSTANDARD_OTHERS  = "Memorial Journal Non Standard Others";


  // atribute
  public static final String ATTR_BANK_SAME_UNIT = "Paid from Bank in Same Unit";
  public static final String ATTR_BANK_OTHER_UNIT = "Paid from Bank to Other Unit";
  public static final String ATTR_CASH_SAME_UNIT = "Paid from Cash in Same Unit";
  public static final String ATTR_CASH_OTHER_UNIT = "Paid from Cash to Other Unit";
  public static final String[] ATTR_BANKCASH_TRANSFER =
	  new String[]{ATTR_BANK_SAME_UNIT, ATTR_BANK_OTHER_UNIT, ATTR_CASH_SAME_UNIT, ATTR_CASH_OTHER_UNIT};

  public static final String ATTR_RCV_BANK = "Receive in Bank";
  public static final String ATTR_RCV_CASH = "Receive in Cash";
  public static final String[] ATTR_RCV =
	  new String[]{ATTR_RCV_BANK, ATTR_RCV_CASH};

  public static final String ATTR_PMT_BANK = "Paid from Bank";
  public static final String ATTR_PMT_CASH = "Paid from Cash";
  public static final String[] ATTR_PMT =
	  new String[]{ATTR_PMT_BANK, ATTR_PMT_CASH};
  //Tambahan dari cok gung buat i owe u Install
  public static final String[] ATTR_INSTALL=
	  new String[]{ ATTR_PMT_CASH};

  public static final String ATTR_CA_SETTLED = "CA Settlement";
  public static final String ATTR_CA_SETTLED_REC = "CA Settlement Received";
  public static final String[] ATTR_CA =
	  new String[]{ATTR_CA_SETTLED, ATTR_CA_SETTLED_REC};

  public static final String ATTR_CS_WAPU = "WAPU";
  public static final String ATTR_CS_NONWAPU = "NON WAPU";
  public static final String[] ATTR_CUSTOMER_STATUS =
	  new String[]{ATTR_CS_WAPU, ATTR_CS_NONWAPU};

  public static final String ATTR_AR_RCV_BANK_WAPU = "AR Received in Bank from WAPU";
  public static final String ATTR_AR_RCV_BANK_NONWAPU = "AR Received in Bank from NONWAPU";
  public static final String ATTR_AR_RCV_CASH_WAPU = "AR Received in Cash from WAPU";
  public static final String ATTR_AR_RCV_CASH_NONWAPU = "AR Received in Cash from NONWAPU";
  public static final String[] ATTR_AR_RCV =
	  new String[]{ATTR_AR_RCV_CASH_WAPU, ATTR_AR_RCV_CASH_NONWAPU,
	  	ATTR_AR_RCV_BANK_WAPU, ATTR_AR_RCV_BANK_NONWAPU};

  public static final String[] ATTR_PMT_LOAN =
	  new String[]{ATTR_PMT_BANK,ATTR_PMT_CASH};

  public static final String ATTR_PURCHASE_RECEIPT_PKP = "Purchase From PKP";
  public static final String ATTR_PURCHASE_RECEIPT_NONPKP = "Purchase From Non PKP";
  public static final String[] ATTR_PURCHASE_RECEIPT =
	  new String[]{ATTR_PURCHASE_RECEIPT_PKP, ATTR_PURCHASE_RECEIPT_NONPKP};

  // special buat setting
  public static final String ATTR_VARS_ES_DIFF = "Expense Sheet Difference";
  public static final String ATTR_VARS_RC_REL = "RC Relations";
  public static final String ATTR_VARS_EMP_REC = "Employee Receivables";
  public static final String ATTR_VARS_CA_IOU_PROJECT = "Cash Advance I Owe You Project";
  public static final String ATTR_VARS_CA_PROJECT = "Cash Advance Project";
  public static final String ATTR_VARS_CA_IOU_OTHER = "Cash Advance I Owe You Other";
  public static final String ATTR_VARS_CA_OTHER = "Cash Advance Other";
  public static final String ATTR_VARS_SALES_ADVANCE = "Sales Advance";
  public static final String ATTR_VARS_TAX23 = "Tax Art 23";
  public static final String ATTR_VARS_TAX23_PAYABLE = "Tax Art 23 Payable";
  public static final String ATTR_VARS_OUT_VAT = "Value Added Tax (Keluaran)";
  public static final String ATTR_VARS_IN_VAT = "Value Added Tax (Masukan)";
  public static final String ATTR_VARS_MTRL_TOOLS_CONS = "Material, Tools, & Consumables";
  public static final String ATTR_VARS_AP = "Account Payable";
  public static final String ATTR_VARS_AR = "Account Receivables";
  public static final String ATTR_VARS_SALES = "Sales";
  public static final String ATTR_VARS_CALC_VAT = "Calculated Value Added Tax";
  public static final String ATTR_VARS_EXPENSE_PROJECT = "Project Expense";
  public static final String ATTR_VARS_CASH_AND_CASH_EQUIVALENTS = "Cash and Cash Equivalents";
  public static final String ATTR_VARS_MATERIALS_TOOLS_CONSUMABLES = "Materials, Tools & Consumables";
  public static final String ATTR_VARS_PROJECT_OPERATIONAL_COST = "Project Operational Cost";
  public static final String ATTR_VARS_THIRD_PARTY_COST = "Third Party Cost";
  public static final String ATTR_VARS_DEPRECIATION_AND_AMORTISATION = "Depreciation and Amortisation";
  public static final String ATTR_VARS_REPAIRMENT_COST = "Repairment Cost";
  public static final String ATTR_VARS_OTHERS_COGS = "Others Cost of Good Sold";
  public static final String ATTR_VARS_RETAINED_EARNINGS = "Retained Earnings";
  public static final String ATTR_VARS_INCOME_SUMMARY = "Income Summary";
  public static final String ATTR_VARS_COST_AND_EXPENSE = "Cost and Expense";
  public static final String ATTR_VARS_FIELD_ALLOWANCE = "Field Allowance";
  public static final String ATTR_VARS_WORK_IN_PROGRESS = "Work in Progress";
  public static final String ATTR_VARS_BANK_CHARGES = "Bank Charges";
  public static final String ATTR_VARS_TRANSLATION_GAIN = "Translation Gain";
  public static final String ATTR_VARS_TRANSLATION_LOSS = "Translation Loss";
  public static final String ATTR_VARS_PERSONNEL_COST = "Direct Remuneration Cost";
  public static final String ATTR_VARS_OTHER_PERSONNEL_COST = "Other Personnel Cost";

  public static final String[] ATTR_VARIABLES =
	  new String[]{ATTR_VARS_ES_DIFF,
	  				ATTR_VARS_RC_REL,
	  				ATTR_VARS_EMP_REC,
	  				ATTR_VARS_CA_IOU_PROJECT,
	  				ATTR_VARS_CA_PROJECT,
	  				ATTR_VARS_CA_IOU_OTHER,
	  				ATTR_VARS_CA_OTHER,
	  				ATTR_VARS_SALES_ADVANCE,
	  				ATTR_VARS_TAX23,
	  				ATTR_VARS_TAX23_PAYABLE,
	  				ATTR_VARS_OUT_VAT,
	  				ATTR_VARS_IN_VAT,
	  				ATTR_VARS_MTRL_TOOLS_CONS,
	  				ATTR_VARS_AP,
	  				ATTR_VARS_AR,
	  				ATTR_VARS_SALES,
	  				ATTR_VARS_CALC_VAT,
	  				ATTR_VARS_EXPENSE_PROJECT,
	  				ATTR_VARS_CASH_AND_CASH_EQUIVALENTS,

	  				ATTR_VARS_PERSONNEL_COST,
	  				ATTR_VARS_OTHER_PERSONNEL_COST,
	  				ATTR_VARS_MATERIALS_TOOLS_CONSUMABLES,
	  				ATTR_VARS_PROJECT_OPERATIONAL_COST,
	  				ATTR_VARS_THIRD_PARTY_COST,
	  				ATTR_VARS_DEPRECIATION_AND_AMORTISATION,
	  				ATTR_VARS_REPAIRMENT_COST,
	  				ATTR_VARS_OTHERS_COGS,

	  				ATTR_VARS_RETAINED_EARNINGS,
	  				ATTR_VARS_INCOME_SUMMARY,
	  				ATTR_VARS_COST_AND_EXPENSE,

	  				ATTR_VARS_FIELD_ALLOWANCE,
	  				ATTR_VARS_WORK_IN_PROGRESS,
	  				ATTR_VARS_BANK_CHARGES,
	  				ATTR_VARS_TRANSLATION_GAIN,
	  				ATTR_VARS_TRANSLATION_LOSS
	  				};

  public static final String CLOSING_TRANSACTION = "Closing Transaction";

  public static final String EMPLOYEENO = "employno";

  public static final String VOID_TRANSACTION = "Void Transaction";
}
