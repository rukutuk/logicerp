package pohaci.gumunda.titis.accounting.beginningbalance;

import java.util.Comparator;

import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.TableRowNumbererColumnClass;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.impl.sort.ComparableComparator;
/*
 * Bagaimana caranya sebuah child tahu account yg dimiliki bapaknya?
 * 
 */
public class BeginningBalanceChildFormats {
	private static String[] colNameEmp() {
		return new String[] { "No", "Name", "Job title", "Unit code",
				"Currency", "Debit", "Credit", "Exch Rate" };
	}

	public static abstract class TableFormatSuperType implements
			AdvancedTableFormat, WritableTableFormat {
		public Object getColumnValue(Object o, int col) {
			try {
				return getSafeColumnValue(o, col);
			} catch (Exception ex) {
				// ex.printStackTrace();
				return null;
			}
		}

		protected abstract Object getSafeColumnValue(Object o, int col);

		public int getColumnCount() {
			return colNames.length;
		}

		String[] colNames;

		public String getColumnName(int column) {
			if (column < colNames.length)
				return colNames[column];
			return "";
		}

		public static Object creditValue(Account account, double accValue) {
			return BeginningBalanceSheetEntryPanel
					.calcCredit(account, accValue);
		}

		public static Object debitValue(Account account, double accValue) {
			return BeginningBalanceSheetEntryPanel.calcDebit(account, accValue);
		}

		public double creditToValue(Number number, Account account) {
			if (number==null)
				return 0;
			
			if (account.getBalance() == 0) // DEBIT
				return -number.doubleValue();
			
			return number.doubleValue();
		}

		public double debitToValue(Number number, Account account) {
			if (number==null)
				return 0;
			
			if (account.getBalance() == 1) // CREDIT
				return -number.doubleValue();
			
			
			
			return number.doubleValue();
		}

		public Comparator getColumnComparator(int column) {
			return new ComparableComparator();
		}

		public Class getColumnClass(int column) {
			if (column==0)
				return TableRowNumbererColumnClass.class;
			return Object.class;
		}
	}

	public static class EmployeeReceivableFmt extends TableFormatSuperType {
		public EmployeeReceivableFmt() {
			this.colNames = colNameEmp();
		}

		public Object getSafeColumnValue(Object baseObject, int column) {
			BeginningEmpReceivable begin = (BeginningEmpReceivable) baseObject;
			switch (column) {
			case 0:
				return "";
			case 1:
				return begin.getEmployee();
			case 2:
				return begin.getEmployee().maxEmployment().getJobTitle();
			case 3:
			    return begin.getEmployee().maxEmployment().getUnit()
						.getCode();
			case 4:
				return begin.getCurrency();
			case 5:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 6:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 7:
				return new Double(begin.getExchangeRate());
			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			if (column == 1)
				return true;
			if (column == 4)
				return true;
			if (column == 5)
				return true;
			if (column == 6)
				return true;
			if (column == 7)
				return true;
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue,
				int column) {
			BeginningEmpReceivable begin = (BeginningEmpReceivable) baseObject;

			switch (column) {
			case 1:
				if (editedValue instanceof Employee)
					begin.setEmployee((Employee) editedValue);
				break;
			case 4:
				if (editedValue instanceof Currency)
					begin.setCurrency((Currency) editedValue);
				break;
			case 5:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 6:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 7:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;
			default:
				return null;
			}
			return begin;
		}

		public Class getColumnClass(int column) {
			switch (column) {
			case 1:
				return Employee.class;
			case 4:
				return Currency.class;
			case 5:
				return Double.class;
			case 6:
				return Double.class;
			case 7:
				return Double.class;
			}
			return super.getColumnClass(column);
		}

	}

	public static class AccountPayableFmt extends TableFormatSuperType {
		public AccountPayableFmt() {
			this.colNames = new String[] { "No", "Supplier", "Address",
					"Account",
					"Project Code", "Unit Code", "Currency", "Debit", "Credit",
					"Exch Rate" };
		}

		public Object getSafeColumnValue(Object baseObject, int column) {
			BeginningAccountPayable begin = (BeginningAccountPayable) baseObject;
			switch (column) {
			case 0:
				return "";
			case 1:
				return begin.getPartner();
			case 2:
				return begin.getPartner().getAddress();
			case 3:
				return begin.getAccount().getName();
			case 4:
				return begin.getProject();
			case 5:
				return begin.getProject().getUnit().getCode();
			case 6:
				return begin.getCurrency();
			case 7:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 8:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 9:
				return new Double(begin.getExchangeRate());
			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			switch (column) {
			case 1:
			case 4:
			case 6:
			case 7:
			case 8:
			case 9:
				return true;
			}
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue,
				int column) {
			BeginningAccountPayable begin = (BeginningAccountPayable) baseObject;
			switch (column) {
			case 1:
				if (editedValue instanceof Partner)
					begin.setPartner((Partner) editedValue);
				break;
			case 4:
				if (editedValue instanceof ProjectData)
					begin.setProject((ProjectData) editedValue);
				break;
			case 6:
				if (editedValue instanceof Currency)
					begin.setCurrency((Currency) editedValue);
				break;
			case 7:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 8:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 9:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;
			default:
				return null;
			}
			return begin;

		}
		public Class getColumnClass(int column) {
			switch (column) {
			case 1:
				return Partner.class;
			case 4:
				return ProjectData.class;
			case 6:
				return Currency.class;
			case 7:
				return Double.class;
			case 8:
				return Double.class;
			case 9:
				return Double.class;
			}
			return super.getColumnClass(column);
		}


	}

	public static class AccountReceivableFmt extends TableFormatSuperType {
		public AccountReceivableFmt() {
			this.colNames = new String[] { "No", "Customer", "Address",
					"Unit code", "Project code", "Currency", "Debit", "Credit",
					"Exch rate" };
		}

		public Object getSafeColumnValue(Object baseObject, int column) {
			BeginningAccountReceivable begin = (BeginningAccountReceivable) baseObject;
			switch (column) {
			case 0:
				return "";
			case 1:
				return begin.getCustomer();
			case 2:
				return begin.getCustomer().getAddress();
			case 3:
				return begin.getProject().getUnit();
			case 4:
				return begin.getProject().getCode();
            case 5:
                return begin.getCurrency();
			case 6:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 7:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 8:
				return new Double(begin.getExchangeRate());
			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			switch (column) {
			case 1: 
			case 4:
			case 5:
			case 6:
			case 7:
            case 8:
				return true;
			}
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue,
				int column) {
			BeginningAccountReceivable begin = (BeginningAccountReceivable) baseObject;
			switch (column) {
			case 1:
				if (editedValue instanceof Customer)
					begin.setCustomer((Customer) editedValue);
				break;
			case 4:
				if (editedValue instanceof ProjectData)
					begin.setProject((ProjectData) editedValue);
				break;
            case 5:
                if (editedValue instanceof Currency)
                    begin.setCurrency((Currency) editedValue);
                break;
			case 6:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 7:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 8:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;
			default:
				return null;
			}
			return begin;

		}

		public Class getColumnClass(int column) {
			switch (column)
			{
			case 1: return Customer.class; 
			case 4: return ProjectData.class;
            case 5: return Currency.class;
			case 6:
			case 7:
			case 8:
				return Double.class;
			}
			return super.getColumnClass(column);
		}
		
	}

	public static class CashAdvanceFmt extends TableFormatSuperType {
		public CashAdvanceFmt() {
			this.colNames = new String[] { "No", "Name", "Job title", "Unit code",
					"Project code",	"Currency", "Debit", "Credit", "Exch Rate" };
		}

		public Object getSafeColumnValue(Object baseObject, int column) {
			BeginningCashAdvance begin = (BeginningCashAdvance) baseObject;
			switch (column) {
			case 0:
				return "";
			case 1:
				return begin.getEmployee().getFirstName() + " "
						+ begin.getEmployee().getMidleName() + " "
						+ begin.getEmployee().getLastName();
			case 2:
				return begin.getEmployee().maxEmployment().getJobTitle();
			case 3:
				return begin.getEmployee().maxEmployment().getUnit();
			case 4:
				return begin.getProject().getCode();
			case 5:
				return begin.getCurrency();
			case 6:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 7:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 8:
				return new Double(begin.getExchangeRate());
			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			switch (column) {
			case 1: 
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				return true;
			}
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue,
				int column) {
			BeginningCashAdvance begin = (BeginningCashAdvance) baseObject;
			switch (column) {
			case 1: 
				if (editedValue instanceof Employee)
					begin.setEmployee((Employee) editedValue);
				break;
			case 4:
				if (editedValue instanceof ProjectData)
					begin.setProject((ProjectData) editedValue);
				break;
			case 5:
				if (editedValue instanceof Currency)
					begin.setCurrency((Currency) editedValue);
				break;
			case 6:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 7:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 8:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;
			
			default:
				return null;
			}
			return begin;
		}

		public Class getColumnClass(int column) {
			switch (column) {
			case 1:
				return Employee.class;
			case 4:
				return ProjectData.class;
			case 5:
				return Currency.class;
			case 6:
			case 7:
			case 8:
				return Double.class;
			}
			return super.getColumnClass(column);
		}

	}

	public static class CashAccountFmt extends TableFormatSuperType {
		public CashAccountFmt() {
			this.colNames = new String[] { "No", "Code", "Cash Name",
					"Currency", "Unit code", "Account", "Debit", "Credit",
					"Exch rate" };
		}

		public Object getSafeColumnValue(Object baseObject, int column) {
			BeginningCashDetail begin = (BeginningCashDetail) baseObject;
			switch (column) {
			case 0:
				return "";
			case 1:
				return begin.getCashAccount().getCode();
			case 2:
				return begin.getCashAccount().getName();
			case 3:
				return begin.getCashAccount().getCurrency();
			case 4:
				return begin.getCashAccount().getUnit().getCode();
			case 5:
				return begin.getAccount();
			case 6:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 7:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 8:
				return new Double(begin.getExchangeRate());
			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			switch (column) {
			case 1:
			case 6:
			case 7:
			case 8:
				return true;
			}
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue,
				int column) {
			BeginningCashDetail begin = (BeginningCashDetail) baseObject;
			switch (column) {
			case 1:
				if (editedValue instanceof CashAccount)
				{
					begin.setCashAccount((CashAccount) editedValue);
					begin.setAccount(begin.getCashAccount().getAccount());
					begin.setCurrency(begin.getCashAccount().getCurrency());
				}
				break;
			case 6:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 7:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 8:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;
			default:
				return null;
			}
			return begin;
		}

		public Class getColumnClass(int column) {
			switch (column) {
			case 1:
				return CashAccount.class;
			case 6:
				return Double.class;
			case 7:
				return Double.class;
			case 8:
				return Double.class;
			}
			return super.getColumnClass(column);
		}

	}

	public static class BankAccountFmt extends TableFormatSuperType {
		public BankAccountFmt() {
			this.colNames = new String[] { "No", "Code", "Bank Account No",
					"Currency", "Unit code", "Account", "Debit", "Credit",
					"Exch rate" };
		}

		public Object getSafeColumnValue(Object baseObject, int column) {
			BeginningBankDetail begin = (BeginningBankDetail) baseObject;
			switch (column) {
			case 0:
				return "";
			case 1:
				return begin.getBankAccount().getCode();
			case 2:
				return begin.getBankAccount().getAccountNo();
			case 3:
				return begin.getBankAccount().getCurrency();
			case 4:
				return begin.getBankAccount().getUnit();
			case 5:
				return begin.getAccount().toStringWithCode();
			case 6:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 7:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 8:
				return new Double(begin.getExchangeRate());

			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			switch (column) {
			case 1:
			case 6:
			case 7:
			case 8:
				return true;
			}
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue,
				int column) {
			BeginningBankDetail begin = (BeginningBankDetail) baseObject;
			switch (column) {
			case 1:
				if (editedValue instanceof BankAccount)
				{
				  begin.setBankAccount((BankAccount) editedValue);
				  begin.setAccount(begin.getBankAccount().getAccount());
				  begin.setCurrency(begin.getBankAccount().getCurrency());
				}
				break;
			case 6:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 7:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 8:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;
			default:
				return null;
			}
			return begin;
		}

		public Class getColumnClass(int column) {
			switch (column) {
			case 1:
				return BankAccount.class;
			case 6:
				return Double.class;
			case 7:
				return Double.class;
			case 8:
				return Double.class;
			}
			return super.getColumnClass(column);
		}

	}
	public static class LoanFmt extends TableFormatSuperType
	{

		public LoanFmt()
		{
			this.colNames=new String[] {
					"No","Creditor name","Loan code", "Currency","Account",
					"Unit code","Debit","Credit","Exch rate" 
			};
		}
		protected Object getSafeColumnValue(Object o, int col) {
			BeginningLoan begin = (BeginningLoan) o;
			switch (col)
			{
			case 0: return "";
			case 1: return begin.getCompanyLoan().getCreditorList().getName();
			case 2: return begin.getCompanyLoan().getCode();
			case 3: return begin.getCompanyLoan().getCurrency();
			case 4: return begin.getAccount();
			case 5: return begin.getCompanyLoan().getUnit().getCode();
			case 6:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 7:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 8:
				return new Double(begin.getExchangeRate()); 
			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			//BeginningLoan begin = (BeginningLoan) baseObject;
			switch (column)
			{
			case 1: 
			case 6:
			case 7:
			case 8:
				return true;
			}
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue, int column) {
			BeginningLoan begin = (BeginningLoan) baseObject;
			switch (column)
			{
			case 1:	if (editedValue instanceof CompanyLoan)
					{
					   begin.setCompanyLoan((CompanyLoan) editedValue);
					   begin.setCurrency(begin.getCompanyLoan().getCurrency());
					}
					break;
			case 6:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 7:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 8:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;	
			default:
				return null;
			}
			return begin;
		}
		public Class getColumnClass(int column) {
			switch (column)
			{
			case 1: return CompanyLoan.class;
			case 6:
			case 7:
			case 8:
				return Double.class;
			}
			return super.getColumnClass(column);
		}
		
	}
	
	public static class WorkInProgressFmt extends TableFormatSuperType
	{

		public WorkInProgressFmt()
		{
			this.colNames=new String[] {
					"No","Project Code","Unit Code", "Currency",
					"Debit","Credit","Exch rate" 
			};
		}
		protected Object getSafeColumnValue(Object o, int col) {
			BeginningWorkInProgress begin = (BeginningWorkInProgress) o;
			switch (col)
			{
			case 0: return "";
			case 1: return begin.getProject().getCode();
			case 2: return begin.getProject().getUnit();
			case 3: return begin.getCurrency();
			case 4:
				return debitValue(begin.getAccount(), begin.getAccValue());
			case 5:
				return creditValue(begin.getAccount(), begin.getAccValue());
			case 6:
				return new Double(begin.getExchangeRate()); 
			}
			return null;
		}

		public boolean isEditable(Object baseObject, int column) {
			//BeginningLoan begin = (BeginningLoan) baseObject;
			switch (column)
			{
			case 1: 
			case 3:
			case 4:
			case 5:
			case 6:
				return true;
			}
			return false;
		}

		public Object setColumnValue(Object baseObject, Object editedValue, int column) {
			BeginningWorkInProgress begin = (BeginningWorkInProgress) baseObject;
			switch (column)
			{
			case 1:	if (editedValue instanceof ProjectData)
					{
					   begin.setProject((ProjectData) editedValue);
					}
					break;
			case 3: 
				if (editedValue instanceof Currency)
					begin.setCurrency((Currency) editedValue);
			
				break;
			case 4:
				begin.setAccValue(debitToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 5:
				begin.setAccValue(creditToValue((Number) editedValue, begin
						.getAccount()));
				break;
			case 6:
				begin.setExchangeRate(((Number) editedValue).doubleValue());
				break;	
			default:
				return null;
			}
			return begin;
		}
		public Class getColumnClass(int column) {
			switch (column)
			{
			case 1: return ProjectData.class;
			case 3: return Currency.class;
			case 4:
			case 5:
			case 6:
				return Double.class;
			}
			return super.getColumnClass(column);
		}
		
	}
}