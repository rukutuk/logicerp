package pohaci.gumunda.titis.accounting.dbapi;

import java.sql.*;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetEntry;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBankDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashDetail;
import pohaci.gumunda.titis.accounting.cgui.*;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.AccountPayable;
import pohaci.gumunda.titis.accounting.entity.AccountReceivable;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.ExchangeRate;
import pohaci.gumunda.titis.accounting.entity.PayrollDeptValue;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.TaxArt21DeptValue;
import pohaci.gumunda.titis.accounting.entity.Unit;

public interface IAccountingSQL {
	long getMaxIndex(String table, Connection conn) throws SQLException;
	void createAccount(Account account, Connection conn) throws SQLException;
	void updateAccount(long index, Account account, Connection conn) throws SQLException;
	void deleteAccount(long index, Connection conn) throws SQLException;
	Account[] getAllAccount(Connection conn) throws SQLException;
	Account getAccount(long index,Connection conn) throws SQLException;
	Account[] getSuperAccount(Connection conn) throws SQLException;
	Account[] getSubAccount(long superindex, Connection conn) throws SQLException;
	void createAccountStructure(long superindex, long subindex, Connection conn) throws SQLException;
	
	void createJournal(Journal journal, Connection conn) throws SQLException;
	Journal[] getAllJournal(Connection conn) throws SQLException;
	Journal getJournal(long index, Connection conn) throws SQLException;
	void updateJournal(long index, Journal journal, Connection conn) throws SQLException;
	void deleteJournal(long index, Connection conn) throws SQLException;
	
	void createJournalStandard(JournalStandard journal, Connection conn) throws SQLException;
	void createJournalStandardStructure(long superjournal, long subjournal, Connection conn) throws SQLException;
	//JournalStandard[] getAllJournalStandard(Connection conn) throws SQLException;
	JournalStandard getJournalStandardByIndex(long index, Connection conn) throws SQLException;
	JournalStandard[] getSuperJournalStandard(Connection conn) throws SQLException;
	JournalStandard[] getSubJournalStandard(long superindex, Connection conn) throws SQLException;
	void updateJournalStandard(long index, JournalStandard journal, Connection conn) throws SQLException;
	void deleteJournalStandard(long index, Connection conn) throws SQLException;
	
	void createJournalStandardAccount(long index, JournalStandardAccount account, Connection conn) throws SQLException;
	JournalStandardAccount[] getJournalStandardAccount(long index, Connection conn) throws SQLException;
	void deleteJournalStandardAccount(long index, Connection conn) throws SQLException;
	
	void createCurrency(Currency currency, Connection conn) throws SQLException;
	Currency[] getAllCurrency(Connection conn) throws SQLException;
	Currency getCurrency(long index, Connection conn) throws SQLException;
	void updateCurrency(long index, Currency currency, Connection conn) throws SQLException;
	void deleteCurrency(long index, Connection conn) throws SQLException;
	
	void createActivity(Activity activity, Connection conn) throws SQLException;
	void createActivityStructure(long superactivity, long subactivity, Connection conn) throws SQLException;
	Activity getActivityByIndex(long index, Connection conn) throws SQLException;
	Activity[] getSuperActivity(Connection conn) throws SQLException;
	Activity[] getSubActivity(long superindex, Connection conn) throws SQLException;
	void updateActivity(long index, Activity activity, Connection conn) throws SQLException;
	void deleteActivity(long index, Connection conn) throws SQLException;
	
	void createUnit(Unit unit, Connection conn) throws SQLException;
	Unit[] getAllUnit(Connection conn) throws SQLException;
	Unit getUnitByIndex(long index, Connection conn) throws SQLException;
	Unit getUnitByDescription(String description, Connection conn) throws SQLException;
	void updateUnit(long index, Unit unit, Connection conn) throws SQLException;
	void deleteUnit(long index, Connection conn) throws SQLException;
	
	void createBankAccount(BankAccount bank, Connection conn) throws SQLException;
	BankAccount[] getAllBankAccount(Connection conn) throws SQLException;
	BankAccount getBankAccountByIndex(long index, Connection conn) throws SQLException;
	void updateBankAccount(long index, BankAccount bank, Connection conn) throws SQLException;
	void deleteBankAccount(long index, Connection conn) throws SQLException;
	
	void createCreditorList(CreditorList bank, Connection conn) throws SQLException;
	CreditorList[] getAllCreditorList(Connection conn) throws SQLException;
	CreditorList getCreditorList(long index, Connection conn) throws SQLException;
	void updateCreditorList(long index, CreditorList bank, Connection conn) throws SQLException;
	void deleteCreditorList(long index, Connection conn) throws SQLException;
	
	void createCompanyLoan(CompanyLoan loan, Connection conn) throws SQLException;
	CompanyLoan[] getAllCompanyLoan(Connection conn) throws SQLException;
	CompanyLoan getCompanyLoan(long index, Connection conn) throws SQLException;
	void updateCompanyLoan(long index, CompanyLoan loan, Connection conn) throws SQLException;
	void deleteCompanyLoan(long index, Connection conn) throws SQLException;
	
	void createCashAccount(CashAccount cash, Connection conn) throws SQLException;
	CashAccount[] getAllCashAccount(Connection conn) throws SQLException;
	CashAccount getCashAccountByIndex(long index, Connection conn) throws SQLException;
	void updateCashAccount(long index, CashAccount cash, Connection conn) throws SQLException;
	void deleteCashAccount(long index, Connection conn) throws SQLException;
	
	void createExchangeRate(ExchangeRate exchange, Connection conn) throws SQLException;
	ExchangeRate[] getAllExchangeRate(Connection conn) throws SQLException;
	ExchangeRate getExchangeRate(long index, Connection conn) throws SQLException;
	void updateExchangeRate(long index, ExchangeRate exchange, Connection conn) throws SQLException;
	void deleteExchangeRate(long index, Connection conn) throws SQLException;
	
	// transaction
	void createTransaction(Connection conn, Transaction trans ) throws SQLException;
	void updateTransaction(long index, Transaction trans, Connection conn) throws SQLException;
	void deleteTransaction(long index, Connection conn) throws SQLException;
    void createTransactionDetail(long transindex, Connection conn, TransactionDetail detail) throws SQLException;
    void deleteTransactionDetail(long transindex, Connection conn) throws SQLException;
	TransactionDetail[] getTransactionDetail(long transindex, Connection conn) throws SQLException;
	void createTransactionPosted(Connection conn,Transaction transaction)throws SQLException;
	void createTransactionValuePosted(long index,Connection conn,TransactionDetail transactionDetail) throws SQLException;
	
	// setting
	void createBalanceSheetReport(Report worksheet, Connection conn) throws SQLException;
	Report[] getAllBalanceSheetReport(Connection conn) throws SQLException;
	void deleteBalanceSheetReport(long index, Connection conn) throws SQLException;
	Transaction[] getAllTransaction(Connection conn) throws SQLException;
	
	void createWorksheet(Worksheet worksheet, Connection conn) throws SQLException;
	Worksheet[] getAllWorksheet(Connection conn) throws SQLException;
	void deleteWorksheet(long index, Connection conn) throws SQLException;
	
	void createWorksheetColumn(long worksheetindex, WorksheetColumn column, Connection conn) throws SQLException;
	WorksheetColumn[] getAllWorksheetColumn(long worksheetindex, Connection conn) throws SQLException;
	void deleteWorksheetColumn(long index, Connection conn) throws SQLException;
	
	void createWorksheetJournal(long columnindex, long journalindex, Connection conn) throws SQLException;
	Journal[] getAllWorksheetJournal(long columnindex, Connection conn) throws SQLException;
	void deleteWorksheetJournal(long columnindex, long journalindex, Connection conn) throws SQLException;
	
	void createAccountReceivable(long customerindex, long accountindex, Connection conn) throws SQLException;
	AccountReceivable[] getAllAccountReceivable(Connection conn) throws SQLException;
	void updateAccountReceivable(long customerindex, long accountindex, Connection conn) throws SQLException;
	void deleteAccountReceivable(long customerindex, long accountindex, Connection conn) throws SQLException;
	
	void createAccountReceivableDefault(long accountindex, Connection conn) throws SQLException;
	void deleteAccountReceivableDefault(Connection conn) throws SQLException;
	Account getAccountReceivableDefault(Connection conn) throws SQLException;
	
	void createAccountPayable(AccountPayable payable, Connection conn) throws SQLException;
	AccountPayable[] getAllAccountPayable(Connection conn) throws SQLException;
	void updateAccountPayable(AccountPayable payable, Connection conn) throws SQLException;
	void deleteAccountPayable(long customerindex, Connection conn) throws SQLException;
	
	void createAccountPayableDefault(long accountindex, Connection conn) throws SQLException;
	void deleteAccountPayableDefault(Connection conn) throws SQLException;
	Account getAccountPayableDefault(Connection conn) throws SQLException;
	
	void createBaseCurrency(long index, Connection conn) throws SQLException;
	void deleteBaseCurrency(Connection conn) throws SQLException;
	Currency getBaseCurrency(Connection conn) throws SQLException;
	
	void createSignature(String app, Signature signature, Connection conn) throws SQLException;
	void deleteSignature(String app, Connection conn) throws SQLException;
	Signature[] getSignature(String app, Connection conn) throws SQLException;
	Signature getSignature(String app, short type, Connection conn) throws SQLException;
	
	void createJournalStandardSetting(String app, JournalStandardSetting journal, Connection conn) throws SQLException;
	void deleteJournalStandardSetting(String app, Connection conn) throws SQLException;
	//JournalStandardSetting[] getJournalStandardSetting(String app, Connection conn) throws SQLException;
	JournalStandardSetting getJournalStandardSetting(String app, short number, Connection conn) throws SQLException;
	
	// setting
	void createBeginningBalance(BeginningBalance balance, Connection conn) throws SQLException;
	void updateBeginningBalance(long index, BeginningBalance balance, Connection conn) throws SQLException;
	BeginningBalance getBeginningBalance(short type, Connection conn) throws SQLException;
	
	void createBeginningBalanceSheetDetail(long index, BeginningBalanceSheetEntry detail, Connection conn) throws SQLException;
	void deleteBeginningBalanceDetail(long index, Connection conn) throws SQLException;
	BeginningBalanceSheetEntry[] getBeginningBalanceSheetView(long index, short category, Connection conn) throws SQLException;
	TransactionDetail[] getBeginningBalanceSheet(long index, short category, Connection conn) throws SQLException;
	
	void createBeginningBankDetail(long index, BeginningBankDetail detail, Connection conn) throws SQLException;
	void deleteBeginningBankDetail(long index, Connection conn) throws SQLException;
	void updateBeginningBankDetail(long index, BeginningBankDetail detail, Connection conn) throws SQLException;
	boolean isBeginningBankDetail(long index, BeginningBankDetail detail, Connection conn) throws SQLException;
	BeginningBankDetail[] getBeginningBankView(long index, long unitindex, Connection conn) throws SQLException;
	
	void createBeginningCashDetail(long index, BeginningCashDetail detail, Connection conn) throws SQLException;
	void deleteBeginningCashDetail(long index, Connection conn) throws SQLException;
	void updateBeginningCashDetail(long index, BeginningCashDetail detail, Connection conn) throws SQLException;
	boolean isBeginningCashDetail(long index, BeginningCashDetail detail, Connection conn) throws SQLException;
	
	Transaction getTransaction(Connection conn, long index) throws SQLException;  
	String getLastReferenceNo(Connection conn, String likeClausa) throws SQLException;  
	String getLastReferenceNoByYear(Connection conn, int year, String likeClausa) throws SQLException;  
	PmtCAIOUProject[] getAllPmtCAIOUProject(Connection conn) throws SQLException;  
	PmtCAProject[] getAllPmtCAProject(Connection conn) throws SQLException;
	
	PurchaseReceipt[] getAllPurchaseReceipt(Connection conn) throws SQLException;
	PurchaseApPmt[] getAllPurchaseApPmtByReceipt(Connection conn, String query) throws SQLException;
	void createSubsidiaryAccountSetting(Connection conn, SubsidiaryAccountSetting accSetting) throws SQLException;
	void updateSubsidiaryAccountSetting(Connection conn, SubsidiaryAccountSetting accSetting) throws SQLException;
	void deleteSubsidiaryAccountSetting(long index, Connection conn) throws SQLException;	
	double getDebitValue(Connection conn, String query) throws SQLException;
	//TransactionPosted getTransactionPostedByIndex(long index, Connection conn) throws SQLException;
	
	BankAccount[] getListAllBankAccount(Connection conn, String query) throws SQLException;
	
	void createDefaultUnit(DefaultUnit dUnit, Connection conn) throws SQLException;
	//void deletDefaultUnit(DefaultUnit dUnit, Connection conn) throws SQLException;
	DefaultUnit[] getAllDefaultUnit(Connection conn) throws SQLException;
	void updateDefaultUnit(int index, DefaultUnit dUnit, Connection conn) throws SQLException;
	
	void createTransactionPeriod(TransactionPeriod tPeriod, Connection conn) throws SQLException;
	//void deleteTransactionPeriod(TransactionPeriod tPeriod, Connection conn) throws SQLException;
	//void updateTransactionPeriod(int index, TransactionPeriod tPeriod, Connection conn) throws SQLException;
	void updateStatusTransactionPeriod(int index, String status, Connection conn) throws SQLException;
	boolean findReferenceNo(Connection m_conn, String refNo) throws SQLException;
	DefaultUnit getDefaultUnit(Connection m_conn) throws SQLException;
	void createPayrollDeptValue(Connection m_conn, PayrollDeptValue payrollDeptValue) throws SQLException;
	void createTaxtArtDeptValue(Connection m_conn, TaxArt21DeptValue taxArt21DeptValue) throws SQLException;
	}
