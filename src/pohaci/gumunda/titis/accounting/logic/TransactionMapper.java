package pohaci.gumunda.titis.accounting.logic;

import java.util.ArrayList;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.MemJournalNonStrd;
import pohaci.gumunda.titis.accounting.entity.MemJournalStrd;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtEmpInsurance;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryHo;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryUnit;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21Ho;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21Unit;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.PmtESDiff;
import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.PmtLoan;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCost;
import pohaci.gumunda.titis.accounting.entity.PmtOthers;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCost;
import pohaci.gumunda.titis.accounting.entity.PmtUnitBankCashTrans;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.RcvESDiff;
import pohaci.gumunda.titis.accounting.entity.RcvEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.accounting.entity.RcvOthers;
import pohaci.gumunda.titis.accounting.entity.RcvUnitBankCashTrns;
import pohaci.gumunda.titis.accounting.entity.SalesAdvance;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesReceived;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayroll;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;

public class TransactionMapper {

	static ArrayList mapList;

	static {
		TransactionMapper.mapList = new ArrayList();
		// list the map of application vs class
		// semua harus didaftarkan disini kalo mau statusnya berubah saat posting
		
		add(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES,
				EmployeePayroll.class);
		add(IConstants.PAYROLL_VERIFICATION_MEAL_ALLOWANCES,
				EmployeePayroll.class);
		add(IConstants.PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES,
				EmployeePayroll.class);
		add(IConstants.PAYROLL_VERIFICATION_OVERTIME,
				EmployeePayroll.class);
		add(IConstants.PAYROLL_VERIFICATION_INSURANCE_ALLOWANCE,
				EmployeePayroll.class);
		add(IConstants.PAYROLL_VERIFICATION_OTHER_ALLOWANCE,
				EmployeePayroll.class);
		add(IConstants.PAYROLL_VERIFICATION_TAX21,
				TaxArt21Submit.class);
		
		add(IConstants.PAYROLL_PAYMENT_SALARY_HO,
				PayrollPmtSlryHo.class);
		add(IConstants.PAYROLL_PAYMENT_SALARY_UNIT,
				PayrollPmtSlryUnit.class);
		add(IConstants.PAYROLL_PAYMENT_TAX21_HO,
				PayrollPmtTax21Ho.class);
		add(IConstants.PAYROLL_PAYMENT_TAX21_UNIT,
				PayrollPmtTax21Unit.class);
		add(IConstants.PAYROLL_PAYMENT_EMPLOYEE_INSURANCE,
				PayrollPmtEmpInsurance.class);
		
		add(IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE,
				RcvESDiff.class);
		add(IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER,
				RcvUnitBankCashTrns.class);
		add(IConstants.RECEIVE_EMPLOYEE_RECEIVABLE,
				RcvEmpReceivable.class);
		add(IConstants.RECEIVE_LOAN,
				RcvLoan.class);
		add(IConstants.RECEIVE_OTHERS,
				RcvOthers.class);
		
		add(IConstants.PAYMENT_PROJECT_COST,
				PmtProjectCost.class);
		add(IConstants.PAYMENT_OPERASIONAL_COST,
				PmtOperationalCost.class);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT,
				PmtCAIOUProjectInstall.class);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT,
				PmtCAIOUProjectSettled.class);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT,
				PmtCAIOUProjectReceive.class);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT,
				PmtCAIOUOthersInstall.class);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT,
				PmtCAIOUOthersSettled.class);
		add(IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT,
				PmtCAIOUOthersReceive.class);
		add(IConstants.PAYMENT_CASHADVANCE_PROJECT,
				PmtCAProject.class);
		add(IConstants.PAYMENT_CASHADVANCE_OTHERS,
				PmtCAOthers.class);
		add(IConstants.PAYMENT_EXPENSESHEET_DIFFERENCE,
				PmtESDiff.class);
		add(IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
				PmtUnitBankCashTrans.class);
		add(IConstants.PAYMENT_EMPLOYEE_RECEIVABLE,
				PmtEmpReceivable.class);
		add(IConstants.PAYMENT_LOAN,
				PmtLoan.class);
		add(IConstants.PAYMENT_OTHERS,
				PmtOthers.class);
		
		add(IConstants.SALES_ADVANCE,
				SalesAdvance.class);
		add(IConstants.SALES_INVOICE,
				SalesInvoice.class);
		add(IConstants.SALES_AR_RECEIVE,
				SalesReceived.class);
		
		add(IConstants.PURCHASE_RECEIPT,
				PurchaseReceipt.class);
		add(IConstants.PURCHASE_AP_PAYMENT,
				PurchaseApPmt.class);
		
		add(IConstants.EXPENSE_SHEET_PROJECT,
				ExpenseSheet.class);
		add(IConstants.EXPENSE_SHEET_OTHERS,
				ExpenseSheet.class);
		
		add(IConstants.MJ_STANDARD,
				MemJournalStrd.class);
		add(IConstants.MJ_NONSTANDARD_PROJECT,
				MemJournalNonStrd.class);
		add(IConstants.MJ_NONSTANDARD_OTHERS,
				MemJournalNonStrd.class);
	}

	private static void add(String application, Class clazz){
		List list = new ArrayList(2);
		list.add(application);
		list.add(clazz);
		mapList.add(list);
	}
	
	public static ArrayList getList(){
		return mapList;
	}

}
