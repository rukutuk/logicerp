/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.util.ArrayList;

import pohaci.gumunda.titis.accounting.cgui.CashAdvanceGeneralPanel;
import pohaci.gumunda.titis.accounting.cgui.ExpenseSheetPanel;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.MJNonStandardPanel;
import pohaci.gumunda.titis.accounting.cgui.MJStandardPanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentCAIOUPanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentEmployeeReceivablePanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentExpenseSheetDifferencePanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentLoanPanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentOperationalCostPanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentOthersPanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentProjectCostPanel;
import pohaci.gumunda.titis.accounting.cgui.PaymentUnitBankCashTransferPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollInsuranceAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollMealAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollOtherAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollOvertimeVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollPaychequeVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollSalaryHeadOfficePanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollSalaryUnitPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollTransportationAllowanceVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PurchaseAccountPayablePaymentPanel;
import pohaci.gumunda.titis.accounting.cgui.PurchaseReceiptPanel;
import pohaci.gumunda.titis.accounting.cgui.ReceiveEmployeeReceivablePanel;
import pohaci.gumunda.titis.accounting.cgui.ReceiveExpenseSheetDifferencePanel;
import pohaci.gumunda.titis.accounting.cgui.ReceiveLoanPanel;
import pohaci.gumunda.titis.accounting.cgui.ReceiveOthersPanel;
import pohaci.gumunda.titis.accounting.cgui.ReceiveUnitBankCashTransferPanel;
import pohaci.gumunda.titis.accounting.cgui.SalesAccountReceivableReceivePanel;
import pohaci.gumunda.titis.accounting.cgui.SalesAdvancedPanel;
import pohaci.gumunda.titis.accounting.cgui.SalesInvoicePanel;
import pohaci.gumunda.titis.accounting.entity.TransactionPanelMap;

public class TransactionPanelMapper {
	static ArrayList mapList;
	
	static {
		TransactionPanelMapper.mapList = new ArrayList();
		
		// list the map of application vs panel class and its attributes
		// semua harus didaftarkan disini kalo mau panelnya bisa muncul ketika diklik di posting panel
		
		add(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES, PayrollPaychequeVerificationPanel.class,
				"Paycheques Verification", 800, 600);
		add(IConstants.PAYROLL_VERIFICATION_MEAL_ALLOWANCES, PayrollMealAllowanceVerificationPanel.class,
				"Meal Allowance Verification", 800, 600);
		add(IConstants.PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES, PayrollTransportationAllowanceVerificationPanel.class,
				"Transportation Allowance Verification", 800, 600);
		add(IConstants.PAYROLL_VERIFICATION_OVERTIME, PayrollOvertimeVerificationPanel.class,
				"Overtime Verification", 800, 600);
		add(IConstants.PAYROLL_VERIFICATION_INSURANCE_ALLOWANCE, PayrollInsuranceAllowanceVerificationPanel.class,
				"Insurance Allowance Verification", 800, 600);
		add(IConstants.PAYROLL_VERIFICATION_OTHER_ALLOWANCE, PayrollOtherAllowanceVerificationPanel.class,
				"Other Allowance Verification", 800, 600);
		
		add(IConstants.PAYROLL_PAYMENT_SALARY_HO, PayrollSalaryHeadOfficePanel.class,
				"Salary Payment - Head Office", 900, 685);
		add(IConstants.PAYROLL_PAYMENT_SALARY_UNIT, PayrollSalaryUnitPanel.class,
				"Salary Payment - Unit", 900, 720);
		
		add(IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE, ReceiveExpenseSheetDifferencePanel.class, 
				"Expense Sheet Differences Receive", 1130, 500);
		add(IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER, ReceiveUnitBankCashTransferPanel.class, 
				"Unit Bank/Cash Transfer Receive", 855, 450);
		add(IConstants.RECEIVE_EMPLOYEE_RECEIVABLE, ReceiveEmployeeReceivablePanel.class, 
				"Employee Receivables Receive", 850, 450);
		add(IConstants.RECEIVE_LOAN, ReceiveLoanPanel.class,
				"Loan Receive", 855, 450);
		add(IConstants.RECEIVE_OTHERS, ReceiveOthersPanel.class, 
				"Others Receive", 880, 550);
		
		add(IConstants.PAYMENT_PROJECT_COST, PaymentProjectCostPanel.class, 
				"Project Cost Payment", 850, 700);
		add(IConstants.PAYMENT_OPERASIONAL_COST, PaymentOperationalCostPanel.class,
				"Operational Cost Payment", 850, 500);
		
		add(IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT, PaymentCAIOUPanel.class,
				"Cash Advance I Owe You Payment", 850, 510);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT, PaymentCAIOUPanel.class,
				"Cash Advance I Owe You Payment", 850, 510);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT, PaymentCAIOUPanel.class,
				"Cash Advance I Owe You Payment", 850, 510);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS, PaymentCAIOUPanel.class,
				"Cash Advance I Owe You Payment", 850, 510);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT, PaymentCAIOUPanel.class,
				"Cash Advance I Owe You Payment", 850, 510);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT, PaymentCAIOUPanel.class,
				"Cash Advance I Owe You Payment", 850, 510);
		add(IConstants.PAYMENT_CASHADVANCE_PROJECT,	CashAdvanceGeneralPanel.class,
				"Cash Advance General Payment", 850, 570);
		add(IConstants.PAYMENT_CASHADVANCE_OTHERS, CashAdvanceGeneralPanel.class,
				"Cash Advance General Payment", 850, 570);
		
		add(IConstants.PAYMENT_EXPENSESHEET_DIFFERENCE, PaymentExpenseSheetDifferencePanel.class, 
				"Expense Sheet Differences Payment", 1130, 550);
		add(IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER, PaymentUnitBankCashTransferPanel.class,
				"Unit Bank/Cash Transfer Payment", 855, 475);
		add(IConstants.PAYMENT_EMPLOYEE_RECEIVABLE, PaymentEmployeeReceivablePanel.class,
				"Employee Receivables Payment", 855, 450);
		add(IConstants.PAYMENT_LOAN, PaymentLoanPanel.class,
				"Loan Payment", 855, 500);
		add(IConstants.PAYMENT_OTHERS, PaymentOthersPanel.class,
				"Others Payment", 850, 550);
		
		add(IConstants.SALES_ADVANCE, SalesAdvancedPanel.class, 
				"Sales Advance Receive", 955, 500);
		add(IConstants.SALES_INVOICE, SalesInvoicePanel.class,
				"Invoice", 880, 675);
		add(IConstants.SALES_AR_RECEIVE, SalesAccountReceivableReceivePanel.class,
				"Account Receivable Received", 955, 600);
		
		add(IConstants.PURCHASE_RECEIPT, PurchaseReceiptPanel.class,
				"Purchase Receipt", 950, 685);
		add(IConstants.PURCHASE_AP_PAYMENT, PurchaseAccountPayablePaymentPanel.class, 
				"Account Payable Payment", 950, 710);
		
		add(IConstants.EXPENSE_SHEET_PROJECT, ExpenseSheetPanel.class,
				"Expense Sheet", 1130, 725);
		add(IConstants.EXPENSE_SHEET_OTHERS, ExpenseSheetPanel.class,
				"Expense Sheet", 1130, 725);
		
		add(IConstants.MJ_STANDARD, MJStandardPanel.class,
				"Memorial Journal Standard", 850, 725);
		add(IConstants.MJ_NONSTANDARD_PROJECT, MJNonStandardPanel.class,
				"Memorial Journal Non Standard", 850,725);
		add(IConstants.MJ_NONSTANDARD_OTHERS, MJNonStandardPanel.class,
				"Memorial Journal Non Standard", 850,725);
	}
	
	private static void add(String application, Class panelClass, String title, int width, int height) {
		TransactionPanelMap map = new TransactionPanelMap(application, panelClass, title, width, height);
		mapList.add(map);
	}
	
	public static ArrayList getList(){
		return mapList;
	}
}
