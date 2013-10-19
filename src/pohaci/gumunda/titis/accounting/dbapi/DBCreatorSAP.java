package pohaci.gumunda.titis.accounting.dbapi;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dwx
 * @version 1.0
 */

import java.sql.*;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class DBCreatorSAP {
	// master data
	public static void createAccountTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACCOUNT + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_ACCOUNT_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_ACCOUNT_NAME + " varchar(255), " +
				IDBConstants.ATTR_CATEGORY + " smallint, " +
				IDBConstants.ATTR_IS_GROUP + " boolean, " +
				IDBConstants.ATTR_BALANCE_CODE + " smallint, " +
				IDBConstants.ATTR_NOTE + " varchar(255), " +
				IDBConstants.ATTR_PATH + " varchar(255)" +
		")");
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT + " has created");
	}
	
	public static void deleteAccountTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACCOUNT);
		
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT + " has dropped");
	}
	
	public static void createAccountStructureTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACCOUNT_STRUCTURE + "(" +
				IDBConstants.ATTR_SUPER_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_SUB_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUPER_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUB_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_STRUCTURE + " has created");
	}
	
	public static void deleteAccountStructureTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACCOUNT_STRUCTURE);
		
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_STRUCTURE + " has dropped");
	}
	
	public static void createCurrencyTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_CURRENCY + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_SYMBOL + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_CODE + " varchar(255), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255)," +
				IDBConstants.ATTR_SAY + " varchar(255)," +
				IDBConstants.ATTR_CENT_WORDS + " varchar(255)," +
				IDBConstants.ATTR_LANGUAGE + " varchar(255))" );
		
		System.out.println("Table " + IDBConstants.TABLE_CURRENCY + " has created");
	}
	
	public static void deleteCurrencyTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CURRENCY );
		
		System.out.println("Table " + IDBConstants.TABLE_CURRENCY + " has destroyed");
	}
	
	
	public static void createActivityTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACTIVITY + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE," +
				IDBConstants.ATTR_NAME + " varchar(255), " +
				IDBConstants.ATTR_DESCRIPTION + " Varchar(255))");
		
		System.out.println("Table " + IDBConstants.TABLE_ACTIVITY + " has created");
	}
	
	public static void deleteActivityTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACTIVITY);
		
		System.out.println("Table " + IDBConstants.TABLE_ACTIVITY + " has dropped");
	}
	
	public static void createActivityStructureTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACTIVITY_STRUCTURE + "(" +
				IDBConstants.ATTR_SUPER_ACTIVITY + " INT(38), " +
				IDBConstants.ATTR_SUB_ACTIVITY + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUPER_ACTIVITY + ") REFERENCES " +
				IDBConstants.TABLE_ACTIVITY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUB_ACTIVITY + ") REFERENCES " +
				IDBConstants.TABLE_ACTIVITY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )");
		
		System.out.println("Table " + IDBConstants.TABLE_ACTIVITY_STRUCTURE + " has created");
	}
	
	public static void deleteActivityStructureTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACTIVITY_STRUCTURE);
		
		System.out.println("Table " + IDBConstants.TABLE_ACTIVITY_STRUCTURE + " has dropped");
	}
	
	public static void createUnitTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_UNIT + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255) )" );
		
		System.out.println("Table " + IDBConstants.TABLE_UNIT + " has created");
	}
	
	public static void deleteUnitTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_UNIT );
		
		System.out.println("Table " + IDBConstants.TABLE_UNIT + " has destroyed");
	}
	
	public static void createBankAccountTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_BANK_ACCOUNT + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_NAME + " varchar(255), " +
				IDBConstants.ATTR_ACCOUNT_NO + " varchar(255), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CONTACT + " varchar(255), " +
				IDBConstants.ATTR_PHONE + " varchar(255), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_BANK_ACCOUNT + " has created");
	}
	
	public static void deleteBankAccountTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BANK_ACCOUNT );
		
		System.out.println("Table " + IDBConstants.TABLE_BANK_ACCOUNT + " has destroyed");
	}
	
	public static void createCreditorListTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_CREDITOR_LIST + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_NAME + " varchar(255), " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_CONTACT + " varchar(255), " +
				IDBConstants.ATTR_PHONE + " varchar(255), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " varchar(255), " +
				IDBConstants.ATTR_BANK_NAME + " varchar(255), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_BANK_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CREDITOR_TYPE + " varchar(10), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_CREDITOR_LIST + " has created");
	}
	
	public static void deleteCreditorListTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CREDITOR_LIST );
		
		System.out.println("Table " + IDBConstants.TABLE_CREDITOR_LIST + " has destroyed");
	}
	
	public static void createCompanyLoanTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_COMPANY_LOAN + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CREDITOR + " INT(38), " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_NAME + " varchar(255), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_INITIAL + " FIXED(38,2), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CREDITOR + ") REFERENCES " +
				IDBConstants.TABLE_CREDITOR_LIST + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_COMPANY_LOAN + " has created");
	}
	
	public static void deleteCompanyLoanTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_COMPANY_LOAN );
		
		System.out.println("Table " + IDBConstants.TABLE_COMPANY_LOAN + " has destroyed");
	}
	
	public static void createCashAccountTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_CASH_ACCOUNT + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_NAME + " varchar(255), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_CASH_ACCOUNT + " has created");
	}
	
	public static void deleteCashAccountTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CASH_ACCOUNT );
		
		System.out.println("Table " + IDBConstants.TABLE_CASH_ACCOUNT + " has destroyed");
	}
	
	public static void createJournalTable(Statement stm ) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_JOURNAL + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_NAME + " varchar(255))");
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL + " has created");
	}
	
	public static void deleteJournalTable(Statement stm ) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_JOURNAL );
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL + " has destroyed");
	}
	
	public static void createJournalStandardTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_IS_GROUP + " boolean, " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL_STANDARD + " has created");
	}
	
	public static void deleteJournalStandardTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD);
		
		System.out.println("Table " + IDBConstants.TABLE_UNIT + " has dropped");
	}
	
	public static void createJournalStandardStructureTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD_STRUCTURE + "(" +
				IDBConstants.ATTR_SUPER_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_SUB_JOURNAL + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUPER_JOURNAL + ") REFERENCES " +
				IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUPER_JOURNAL + ") REFERENCES " +
				IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL_STANDARD_STRUCTURE + " has created");
	}
	
	public static void deleteJournalStandardStructureTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD_STRUCTURE);
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL_STANDARD_STRUCTURE + " has dropped");
	}
	
	public static void createJournalStandardAccountTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT + "(" +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BALANCE_CODE  + " smallint, " +
				IDBConstants.ATTR_HIDDEN + " boolean, " +
				IDBConstants.ATTR_CALCULATE + " boolean, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES " +
				IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT + " has created");
	}
	
	public static void deleteJournalStandardAccountTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT);
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT + " has dropped");
	}
	
	public static void createExchangeRateTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_EXCHANGE_RATE + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_REFERENCE_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_BASE_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_VALID_FROM + " date, " +
				IDBConstants.ATTR_VALID_TO + " date, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_REFERENCE_CURRENCY + ") REFERENCES "+
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_BASE_CURRENCY + ") REFERENCES "+
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_EXCHANGE_RATE + " is created");
	}
	
	public static void deleteExchangeRateTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EXCHANGE_RATE);
		
		System.out.println("Table " + IDBConstants.TABLE_EXCHANGE_RATE + " is destroyed");
	}
	
	// setting
	public static void createBeginningBalanceTable(Statement stm)  throws SQLException {
		GenericMapper mapper = MasterMap.obtainMapperFor(BeginningBalance.class);
		mapper.setActiveConn(stm.getConnection());
		mapper.ddlCreate();
		/*stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_BEGINNING_BALANCE + "(" +
		 IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		 IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		 IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		 IDBConstants.ATTR_TYPE + " smallint, " +
		 IDBConstants.ATTR_STATUS + " smallint, " +
		 IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		 //IDBConstants.ATTR_UNIT + " INT(38), " +
		  //"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		   //IDBConstants.TABLE_UNIT + "," +
		    "FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		    IDBConstants.TABLE_TRANSACTION + " ON DELETE SET NULL)");
		    */
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_BALANCE + " is created");
	}
	
	public static void deleteBeginningBalanceTable(Statement stm)  throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BEGINNING_BALANCE);
		
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_BALANCE + " is destroyed");
	}
	
	public static void createBeginningBalanceSheetDetailTable(Statement stm) throws SQLException {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
		IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
		IDBConstants.ATTR_CALCULATE + " boolean, " +
		IDBConstants.ATTR_TYPE + " INT(38)," +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.TABLE_BEGINNING_BALANCE + ") REFERENCES "
		+ IDBConstants.TABLE_BEGINNING_BALANCE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
		+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + " is created");
	}
	
	public static void deleteBeginningBalanceSheetDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY);
		
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + " is destroyed");
	}
	
	public static void createBeginningBankDetailTable(Statement stm) throws SQLException {
		String subsidiaryFieldName = IDBConstants.ATTR_BANK_ACCOUNT;
		String subsidiaryTableName = IDBConstants.TABLE_BANK_ACCOUNT;
		String newTableName = IDBConstants.TABLE_BEGINNING_BANK_DETAIL;
		tmplCreateBeginningDetailTable(stm, subsidiaryFieldName, subsidiaryTableName, newTableName);
	}
	
	public static void createBeginningWorkInProgressDetailTable(Statement stm) throws SQLException {
		String subsidiaryFieldName = IDBConstants.ATTR_PROJECT;
		String subsidiaryTableName = pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_DATA;
		String newTableName = IDBConstants.TABLE_BEGINNING_WORK_IN_PROGRESS;
		tmplCreateBeginningDetailTable(stm, subsidiaryFieldName, subsidiaryTableName, newTableName);
	}
	
	private static void tmplCreateBeginningDetailTable(Statement stm, String subsidiaryFieldName, String subsidiaryTableName, String newTableName) throws SQLException {
		String sql = "CREATE TABLE " + newTableName + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + " INT(38), " +
		subsidiaryFieldName + " INT(38), " +
		IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + ") REFERENCES "
		+ IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + subsidiaryFieldName + ") REFERENCES "
		+ subsidiaryTableName + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
		+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		
		System.out.println("Table " + newTableName + " is created");
	}
	
	private static void tmplCreateBeginningDetailTable(Statement stm, String subsidiaryFieldName, String subsidiaryTableName, String subsidiaryFieldName2, String subsidiaryTableName2,String newTableName) throws SQLException {
		String sql = "CREATE TABLE " + newTableName + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + " INT(38), " +
		subsidiaryFieldName + " INT(38), " +
		subsidiaryFieldName2 + " INT(38), " +
		IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + ") REFERENCES "
		+ IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + subsidiaryFieldName + ") REFERENCES "
		+ subsidiaryTableName + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + subsidiaryFieldName2 + ") REFERENCES "
		+ subsidiaryTableName2 + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
		+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		
		System.out.println("Table " + newTableName + " is created");
	}
	
	public static void deleteBeginningBankDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BEGINNING_BANK_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_BANK_DETAIL + " is destroyed");
	}
	
	public static void deleteBeginningWorkInProgressDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BEGINNING_WORK_IN_PROGRESS);
		
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_WORK_IN_PROGRESS + " is destroyed");
	}
	
	public static void createBeginningCashDetailTable(Statement stm) throws SQLException {
		tmplCreateBeginningDetailTable(stm,IDBConstants.ATTR_CASH_ACCOUNT,IDBConstants.TABLE_CASH_ACCOUNT,IDBConstants.TABLE_BEGINNING_CASH_DETAIL);
		/*   stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL + "(" +
		 IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		 IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
		 IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		 IDBConstants.ATTR_VALUE + " INT(38,2), " +
		 IDBConstants.ATTR_CURRENCY + " INT(38), " +
		 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
		 IDBConstants.ATTR_STATUS + " smallint, " +
		 "FOREIGN KEY (" + IDBConstants.ATTR_BEGINNING_BALANCE + ") REFERENCES "
		 + IDBConstants.TABLE_BEGINNING_BALANCE + " ON DELETE CASCADE, " +
		 "FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES "
		 + IDBConstants.TABLE_CASH_ACCOUNT + ", " +
		 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
		 + IDBConstants.TABLE_ACCOUNT + ", " +
		 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		 + IDBConstants.TABLE_CURRENCY + ")");
		 */
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL + " is created");
	}
	
	public static void deleteBeginningCashDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL + " is destroyed");
	}
	
	public static void createBeginningAccountReceivableTable(Statement stm) throws Exception {
		tmplCreateBeginningDetailTable(stm,IDBConstants.ATTR_CUSTOMER,IDBConstants.TABLE_CUSTOMER,
				IDBConstants.ATTR_PROJECT, pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_DATA,
				IDBConstants.TABLE_BEGINNING_ACCOUNT_RECEIVABLE);
//		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE + "(" +
//		IDBConstants.ATTR_CUSTOMER + " INT(38), " +
//		IDBConstants.ATTR_ACCOUNT + " INT(38), " +
//		"FOREIGN KEY (" + IDBConstants.ATTR_CUSTOMER + ") REFERENCES " +
//		IDBConstants.TABLE_CUSTOMER + " ON DELETE CASCADE, " +
//		"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
//		IDBConstants.TABLE_ACCOUNT + " ON DELETE CASCADE)");
		
//		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE + " is created");
	}
	
	public static void deleteBeginningAccountReceivableTable(Statement stm) throws Exception {
		tmplDeleteTable(stm,IDBConstants.TABLE_BEGINNING_ACCOUNT_RECEIVABLE);
//		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE);
		
//		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE + " is destroyed");
	}
	
	public static void createAccountReceivableDefaultTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE_DEFAULT + "(" +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE_DEFAULT + " is created");
	}
	
	public static void deleteAccountReceivableDefaultTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE_DEFAULT);
		
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE_DEFAULT + " is destroyed");
	}
	
	public static void createBeginningAccountPayableTable(Statement stm) throws Exception {
		/*stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACCOUNT_PAYABLE + "(" +
		 IDBConstants.ATTR_PARTNER + " INT(38), " +
		 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		 "FOREIGN KEY (" + IDBConstants.ATTR_PARTNER + ") REFERENCES " +
		 IDBConstants.TABLE_PARTNER + " ON DELETE CASCADE, " +
		 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
		 IDBConstants.TABLE_ACCOUNT + " ON DELETE CASCADE)");
		 
		 System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE + " is created");*/
		tmplCreateBeginningDetailTable(stm,IDBConstants.ATTR_PARTNER,IDBConstants.TABLE_PARTNER,
				IDBConstants.ATTR_PROJECT, pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_DATA,
				IDBConstants.TABLE_BEGINNING_ACCOUNTPAYABLE);
	}
	
	public static void deleteBeginningAccountPayableTable(Statement stm) throws Exception {
		tmplDeleteTable(stm,IDBConstants.TABLE_BEGINNING_ACCOUNTPAYABLE);
//		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE);
		
//		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE + " is destroyed");
	}
	
	
	public static void createAccountPayableDefaultTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ACCOUNT_PAYABLE_DEFAULT + "(" +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_PAYABLE_DEFAULT + " is created");
	}
	
	public static void deleteAccountPayableDefaultTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ACCOUNT_PAYABLE_DEFAULT);
		
		System.out.println("Table " + IDBConstants.TABLE_ACCOUNT_PAYABLE_DEFAULT + " is destroyed");
	}
	
	public static void createBaseCurrencyTable(Statement stm) throws Exception {
		String query = "CREATE TABLE " + IDBConstants.TABLE_BASE_CURRENCY + "(" +
				IDBConstants.ATTR_CURRENCY + " INT(38) AUTO_INCREMENT, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)";
		System.out.println(query);
		stm.executeUpdate(query);
		
		System.out.println("Table " + IDBConstants.TABLE_BASE_CURRENCY + " is created");
	}
	
	public static void deleteBaseCurrencyTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BASE_CURRENCY);
		
		System.out.println("Table " + IDBConstants.TABLE_BASE_CURRENCY + " is destroyed");
	}
	
	public static void createWorksheetTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_WORKSHEET + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_NAME + " varchar(255))");
		
		System.out.println("Table " + IDBConstants.TABLE_WORKSHEET + " is created");
	}
	
	public static void deleteWorksheetTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_WORKSHEET);
		
		System.out.println("Table " + IDBConstants.TABLE_WORKSHEET + " is destroyed");
	}
	
	public static void createWorksheetColumnTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_WORKSHEET_COLUMN + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_WORKSHEET + " INT(38), " +
				IDBConstants.ATTR_COLUMN + " varchar(255), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_WORKSHEET + ") REFERENCES "
				+ IDBConstants.TABLE_WORKSHEET + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_WORKSHEET_COLUMN + " is created");
	}
	
	public static void deleteWorksheetColumnTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_WORKSHEET_COLUMN);
		
		System.out.println("Table " + IDBConstants.TABLE_WORKSHEET_COLUMN + " is destroyed");
	}
	
	public static void createWorksheetJournalTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_WORKSHEET_JOURNAL + "(" +
				IDBConstants.ATTR_COLUMN + " INT(38), " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_COLUMN + ") REFERENCES "
				+ IDBConstants.TABLE_WORKSHEET_COLUMN + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_WORKSHEET_JOURNAL + " is created");
	}
	
	public static void deleteWorksheetJournalTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_WORKSHEET_JOURNAL);
		
		System.out.println("Table " + IDBConstants.TABLE_WORKSHEET_JOURNAL + " is destroyed");
	}
	
	public static void createBalanceSheetReportTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_BALANCE_SHEET_REPORT + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_NAME + " varchar(255))");
		
		System.out.println("Table " + IDBConstants.TABLE_BALANCE_SHEET_REPORT + " is created");
	}
	
	public static void deleteBalanceSheetReportTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BALANCE_SHEET_REPORT);
		
		System.out.println("Table " + IDBConstants.TABLE_BALANCE_SHEET_REPORT + " is destroyed");
	}
	
	public static void createSignatureTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SIGNATURE + "(" +
				IDBConstants.ATTR_APPLICATION + " varchar(255), " +
				IDBConstants.ATTR_SIGNATURE + " smallint, " +
				IDBConstants.ATTR_EMPLOYEE + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE + ") REFERENCES " +
				IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_SIGNATURE + " is created");
	}
	
	public static void deleteSignatureTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SIGNATURE);
		
		System.out.println("Table " + IDBConstants.TABLE_SIGNATURE + " is destroyed");
	}
	
	public static void createJournalStandardSettingTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING + "(" +
				IDBConstants.ATTR_APPLICATION + " varchar(255), " +
				IDBConstants.ATTR_NUMBER + " smallint, " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_ATTRIBUTE + " varchar(255), "+
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING + " is created");
	}
	
	public static void deleteJournalStandardSettingTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING);
		
		System.out.println("Table " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING + " is destroyed");
	}
	
//	transaction Journal
	public static void createTransactionTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_TRANSACTION + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION_CODE + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_VERIFICATION_DATE + " date, " +
				IDBConstants.ATTR_POSTED_DATE + " date, " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_ISVOID + " boolean, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION_CODE + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL  + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES "
				+ IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION + " is created");
	}
	
	public static void deleteTransactionTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TRANSACTION);
		
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION + " is destroyed");
	}
	
	public static void createTransactionValueTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_TRANSACTION_VALUE + "(" +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_SUBSIDIARY_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES "
				+ IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION_VALUE + " is created");
	}
	
	public static void deleteTransactionValueTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TRANSACTION_VALUE);
		
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION_VALUE + " is destroyed");
	}
	
	public static void createTransactionPostedTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_TRANSACTION_POSTED + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_CODE + " INT(38), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_VERIFICATION_DATE + " date, " +
				IDBConstants.ATTR_POSTED_DATE + " date, " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION_CODE + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES "
				+ IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION_POSTED + " is created");
	}
	
	public static void deleteTransactionPostedTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TRANSACTION_POSTED);
		
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION_POSTED + " is destroyed");
	}
	
	public static void createTransactionValuePostedTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_TRANSACTION_VALUE_POSTED + "(" +
				IDBConstants.ATTR_TRANSACTION_POSTED + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_SUBSIDIARY_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION_POSTED + ") REFERENCES "
				+ IDBConstants.TABLE_TRANSACTION_POSTED + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION_VALUE_POSTED + " is created");
	}
	
	public static void deleteTransactionValuePostedTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TRANSACTION_VALUE_POSTED);
		
		System.out.println("Table " + IDBConstants.TABLE_TRANSACTION_VALUE_POSTED + " is destroyed");
	}
	
	public static void createReceiveESDiffTable(Statement stm) throws SQLException {
		String text = "CREATE TABLE " + IDBConstants.TABLE_RCV_ES_DIFF + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_RECEIVE_TO + " varchar(32), " +
		IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_ES_NO + " INT(38), " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		IDBConstants.ATTR_UNIT + " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_ES_NO + ") REFERENCES " +
		IDBConstants.TABLE_EXPENSE_SHEET + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(text);
		stm.executeUpdate(text );
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_ES_DIFF + " is created");
	}
	
	public static void deleteReceiveESDiffTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_ES_DIFF);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_ES_DIFF + " is destroyed");
	}
	
	public static void createReceiveESDiffDetailTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_ES_DIFF_DETAIL + "(" +
				IDBConstants.ATTR_RCV_ES_DIFF + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				// IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_RCV_ES_DIFF + ") REFERENCES "
				+ IDBConstants.TABLE_RCV_ES_DIFF + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_ES_DIFF_DETAIL + " is created");
	}
	
	public static void deleteReceiveESDiffDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_ES_DIFF_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_ES_DIFF_DETAIL + " is destroyed");
	}
	
	public static void createReceiveUnitCashBankTrnsTable(Statement stm) throws SQLException {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_RCV_UNIT_CODE + " INT(38), " +
		IDBConstants.ATTR_RECEIVE_TO + " varchar(32), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_TRANSFER_FROM + " INT(38), " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		//Tidak perlu ada attr unit lagi karena utk yg di search, unit code = RCV_UNIT_CODE
		//IDBConstants.ATTR_UNIT + " INT(38), " +
		//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		//IDBConstants.TABLE_UNIT + "," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_RCV_UNIT_CODE + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		//TRANSFER FROM diambil dari tabel PMT_UNIT_BANKCASH_TRNS yang statusnya= 3 atau sdh terposted
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSFER_FROM + ") REFERENCES " +
		IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS + " is created");
	}
	
	public static void deleteReceiveUnitCashBankTrnsTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS + " is destroyed");
	}
	
	public static void createReceiveUnitCashBankTrnsDetailTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS_DETAIL + "(" +
				IDBConstants.ATTR_RCV_UNIT_BANKCASH_TRNS + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_RCV_UNIT_BANKCASH_TRNS + ") REFERENCES "
				+ IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS_DETAIL + " is created");
	}
	
	public static void deleteReceiveUnitCashBankTrnsDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS_DETAIL + " is destroyed");
	}
	
	
	public static void createReceiveEmpReceivableTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_RECEIVE_TO + " varchar(32), " +
				IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_EMP_RECEIVABLE + " INT (38), " +
				IDBConstants.ATTR_CURRENCY + " INT (38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVABLE + ") REFERENCES " +
				IDBConstants.TABLE_PMT_EMP_RECEIVABLE + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE + " is created");
	}
	
	public static void deleteReceiveEmpReceivableTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE + " is destroyed");
	}
	
	public static void createReceiveEmpReceivableDetailTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE_DETAIL + "(" +
				IDBConstants.ATTR_RCV_EMP_RECEIVABLE + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_RCV_EMP_RECEIVABLE + ") REFERENCES "
				+ IDBConstants.TABLE_RCV_EMP_RECEIVABLE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE_DETAIL + " is created");
	}
	
	public static void deleteReceiveEmpReceivableDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_EMP_RECEIVABLE_DETAIL + " is destroyed");
	}
	
	public static void createReceiveLoanTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_LOAN + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_RECEIVE_TO + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_COMPANY_LOAN + " INT(38), " +
				IDBConstants.ATTR_CURRENCY + " INT (38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_COMPANY_LOAN + ") REFERENCES " +
				IDBConstants.TABLE_COMPANY_LOAN + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_LOAN + " is created");
	}
	
	public static void deleteReceiveLoanTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_LOAN);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_LOAN + " is destroyed");
	}
	
	public static void createReceiveLoanDetailTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_LOAN_DETAIL + "(" +
				IDBConstants.ATTR_RCV_LOAN + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_RCV_LOAN + ") REFERENCES "
				+ IDBConstants.TABLE_RCV_LOAN + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_LOAN_DETAIL + " is created");
	}
	
	public static void deleteReceiveLoanDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_LOAN_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_LOAN_DETAIL + " is destroyed");
	}
	
	public static void createReceiveOthersTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_OTHERS + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_RECEIVE_TO + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_RECEIVE_FROM + " varchar(100), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES " +
				IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_OTHERS + " is created");
	}
	
	public static void deleteReceiveOthersTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_OTHERS);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_OTHERS + " is destroyed");
	}
	
	public static void createReceiveOthersDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RCV_OTHERS_DETAIL + "(" +
				IDBConstants.ATTR_RECEIVE_OTHERS + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_RECEIVE_OTHERS + ") REFERENCES "
				+ IDBConstants.TABLE_RCV_OTHERS + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_OTHERS_DETAIL + " is created");
	}
	
	public static void deleteReceiveOthersDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RCV_OTHERS_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_RCV_OTHERS_DETAIL + " is destroyed");
	}
	
	public static void createPaymentProjectCostTable(Statement stm) throws SQLException {
		String query="CREATE TABLE " + IDBConstants.TABLE_PMT_PROJECT_COST + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
		IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
		IDBConstants.ATTR_PAY_TO + " varchar(255), " +
		IDBConstants.ATTR_PROJECT + " INT(38), " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		IDBConstants.ATTR_UNIT + " INT(38), " +
		IDBConstants.ATTR_TOTAL + " FIXED(38,2), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
		"PROJECTDATA" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" ;
		stm.executeUpdate(query);
		System.out.println("Table " + IDBConstants.TABLE_PMT_PROJECT_COST + " is created");
		System.out.println(query);
	}
	
	public static void deletePaymentProjectCostTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_PROJECT_COST);
		
		System.out.println("Table " + IDBConstants.TABLE_PMT_PROJECT_COST + " is destroyed");
	}
	
	public static void createPaymentProjectCostDetailTable(Statement stm) throws SQLException {
		String query="CREATE TABLE " + IDBConstants.TABLE_PMT_PROJECT_COST_DETAIL + "(" +
		IDBConstants.ATTR_PMT_PROJECT_COST + " INT(38), " +
		IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
		IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_PMT_PROJECT_COST + ") REFERENCES " +
		IDBConstants.TABLE_PMT_PROJECT_COST + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		stm.executeUpdate(query);
		System.out.println("Table " + IDBConstants.TABLE_PMT_PROJECT_COST_DETAIL + " is created");
		System.out.println(	query);
	}
	
	public static void deletePaymentProjectCostDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_PROJECT_COST_DETAIL);
		System.out.println("Table " + IDBConstants.TABLE_PMT_PROJECT_COST_DETAIL + " is destroyed");
	}
	
	public static void createPaymentOperationalCostTable(Statement stm) throws SQLException {
		String query="CREATE TABLE " + IDBConstants.TABLE_PMT_OPERATIONAL_COST + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
		IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
		//IDBConstants.ATTR_PAY_TO + " INT(38), " +
		IDBConstants.ATTR_PAY_TO + " varchar(255), " +
		IDBConstants.ATTR_UNIT + " INT(38), " +
		IDBConstants.ATTR_DEPARTMENT_GROUP + " smallint, " +
		IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		/*"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
		 "EMPLOYEE" + "," +*/
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
		"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" ;
		stm.executeUpdate(query);
		System.out.println("Table " + IDBConstants.TABLE_PMT_OPERATIONAL_COST + " is created");
		System.out.println(query);
	}
	
	public static void deletePaymentOperasionalCostTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_OPERATIONAL_COST);
		System.out.println("Table " + IDBConstants.TABLE_PMT_OPERATIONAL_COST + " is destroyed");
	}
	
	public static void createPaymentOperationalCostDetailTable(Statement stm) throws SQLException {
		String query="CREATE TABLE " + IDBConstants.TABLE_PMT_OPERATIONAL_COST_DETAIL + "(" +
		IDBConstants.ATTR_PMT_OPERASIONAL_COST + " INT(38), " +
		IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
		IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_PMT_OPERASIONAL_COST + ") REFERENCES " +
		IDBConstants.TABLE_PMT_OPERATIONAL_COST + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		stm.executeUpdate(query);
		System.out.println("Table " + IDBConstants.TABLE_PMT_OPERATIONAL_COST_DETAIL + " is created");
	}
	
	public static void deletePaymentOperasionalCostDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_OPERATIONAL_COST_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_PMT_OPERATIONAL_COST_DETAIL + " is destroyed");
	}
	
	public static void createCashAdvanceIOUProjectTable(Statement stm) throws SQLException {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_PAY_TO + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_PROJECT + " INT(38), " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		
		//Dilakukan perubahan agar sama dengan table yang lain => bisa pake master map donk
		// Juga bisa TransactionEntitiy => belum dilakukan
//		IDBConstants.ATTR_EMP_APPROVED1 + " INT(38), " +=>Jadi Approved
//		IDBConstants.ATTR_JOB_TITLE_APPROVED1 + " varchar(60), " +
//		IDBConstants.ATTR_DATE_APPROVED1 + " date, " +
//		IDBConstants.ATTR_EMP_APPROVED2 + " INT(38), " + => Jadi Received
//		IDBConstants.ATTR_JOB_TITLE_APPROVED2 + " varchar(60), " +
//		IDBConstants.ATTR_DATE_APPROVED2 + " date, " +
		//Penambahan Status dilakukan untuk mempermudah pengaturan behavior tombol
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		
		
		IDBConstants.ATTR_UNIT + " INT(38), " +
		IDBConstants.ATTR_DEPARTMENT + " INT(38), " +		
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +			
		"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
		pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT + " is created");
	}
	
	public static void deleteCashAdvanceIOUProjectTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT + " is destroyed");
	}
	
	public static void createCashAdvanceIOUProjectIstallmentsTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_INSTALLMENTS+ "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), "  +
				IDBConstants.ATTR_PMT_CA_IOU_PROJECT + " INT(38), "  +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255)," +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_IOU_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PMT_CA_IOU_PROJECT +"(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_INSTALLMENTS + " is created");
	}
	
	public static void deleteCashAdvanceIOUProjectIstallmentsTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_INSTALLMENTS);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_INSTALLMENTS + " is destroyed");
	}
	
	public static void createCashAdvanceIOUProjectSettledTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_SETTLED + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), "  +
				IDBConstants.ATTR_PMT_CA_IOU_PROJECT + " INT(38), "  +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255)," +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_IOU_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PMT_CA_IOU_PROJECT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_SETTLED + " is created");
	}
	
	public static void deleteCashAdvanceIOUProjectSettledTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_SETTLED);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_SETTLED + " is destroyed");
	}
	
	public static void createCashAdvanceIOUProjectReceiveTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_RECEIVE + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), "  +
				IDBConstants.ATTR_PMT_CA_IOU_PROJECT + " INT(38), "  +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255)," +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_RECEIVE_TO+ " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CURRENCY + " INT (38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_IOU_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PMT_CA_IOU_PROJECT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				//"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				//IDBConstants.TABLE_BANK_ACCOUNT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_RECEIVE + " is created");
	}
	
	public static void deleteCashAdvanceIOUProjectReceiveTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_RECEIVE);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT_RECEIVE + " is destroyed");
	}
	
	public static void createCashAdvanceIOUOthersTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PAY_TO + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				
				//Dilakukan perubahan agar sama dengan table yang lain => bisa pake master map donk
				// Juga bisa TransactionEntitiy => belum dilakukan
//				IDBConstants.ATTR_EMP_APPROVED1 + " INT(38), " +=>Jadi Approved
//				IDBConstants.ATTR_JOB_TITLE_APPROVED1 + " varchar(60), " +
//				IDBConstants.ATTR_DATE_APPROVED1 + " date, " +
//				IDBConstants.ATTR_EMP_APPROVED2 + " INT(38), " + => Jadi Received
//				IDBConstants.ATTR_JOB_TITLE_APPROVED2 + " varchar(60), " +
//				IDBConstants.ATTR_DATE_APPROVED2 + " date, " +
				//Penambahan Status dilakukan untuk mempermudah pengaturan behavior tombol
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS + " is created");
	}
	
	public static void deleteCashAdvanceIOUOthersTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS + " is destroyed");
	}
	
	public static void createCashAdvanceIOUOthersInstallmentsTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_INSTALLMENTS+ "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), "  +
				IDBConstants.ATTR_PMT_CA_IOU_OTHERS + " INT(38), "  +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255)," +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_IOU_OTHERS + ") REFERENCES " +
				IDBConstants.TABLE_PMT_CA_IOU_OTHERS + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_INSTALLMENTS + " is created");
	}
	
	public static void deleteCashAdvanceIOUOthersInstallmentsTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_INSTALLMENTS);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_INSTALLMENTS + " is destroyed");
	}
	
	public static void createCashAdvanceIOUOthersSettledTable(Statement stm) throws SQLException {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_SETTLED + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), "  +
		IDBConstants.ATTR_PMT_CA_IOU_OTHERS + " INT(38), "  +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255)," +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
		IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		//IDBConstants.ATTR_UNIT + " INT(38), " +
		//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		//IDBConstants.TABLE_UNIT + "," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_IOU_OTHERS + ") REFERENCES " +
		IDBConstants.TABLE_PMT_CA_IOU_OTHERS + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		stm.executeUpdate(sql);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_SETTLED + " is created");
	}
	
	public static void deleteCashAdvanceIOUOthersSettledTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_SETTLED);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_SETTLED + " is destroyed");
	}
	
	public static void createCashAdvanceIOUOthersReceiveTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_RECEIVE + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), "  +
				IDBConstants.ATTR_PMT_CA_IOU_OTHERS + " INT(38), "  +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255)," +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_RECEIVE_TO+ " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CURRENCY + " INT (38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_IOU_OTHERS + ") REFERENCES " +
				IDBConstants.TABLE_PMT_CA_IOU_OTHERS + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))")
				;
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_RECEIVE + " is created");
	}
	
	public static void deleteCashAdvanceIOUOthersReceiveTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_RECEIVE);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS_RECEIVE + " is destroyed");
	}
	
	public static void createCashAdvanceProjectTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_CA_PROJECT + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " INT(38), " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_REQUESTED_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				"PROJECTDATA" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_PROJECT+ " is created");
	}
	
	public static void deleteCashAdvanceProjectTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_PROJECT);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_PROJECT + " is destroyed");
	}
	
	public static void createCashAdvanceOthersTable(Statement stm) throws SQLException {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_PMT_CA_OTHERS + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
		IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
		IDBConstants.ATTR_PAY_TO + " INT(38), " +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
		IDBConstants.ATTR_UNIT + " INT(38), " +
		IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
		"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_OTHERS + " is created");
	}
	
	public static void deleteCashAdvanceOthersTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_CA_OTHERS);
		System.out.println("Table " + IDBConstants.TABLE_PMT_CA_OTHERS + " is destroyed");
	}
	
	public static void createPaymentESDifferenceTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_ES_DIFF + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_ES_NO + " INT(38), " +
				IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
				IDBConstants.ATTR_PAY_TO + " INT(38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT (38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ES_NO + ") REFERENCES " +
				IDBConstants.TABLE_EXPENSE_SHEET + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		System.out.println("Table " + IDBConstants.TABLE_PMT_ES_DIFF + " is created");
	}
	
	public static void deletePaymentESDifferenceTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_ES_DIFF);
		
		System.out.println("Table " + IDBConstants.TABLE_PMT_ES_DIFF + " is destroyed");
	}
	
	/*public static void createPaymentESDifferenceDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_ES_DIFF_DETAIL + "(" +
	 IDBConstants.ATTR_PMT_ES_DIFF + " INT(38), " +
	 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
	 IDBConstants.ATTR_VALUE + " INT(38,2), " +
	 IDBConstants.ATTR_CURRENCY + " INT(38), " +
	 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PMT_ES_DIFF + ") REFERENCES " +
	 IDBConstants.TABLE_PMT_ES_DIFF + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
	 IDBConstants.TABLE_ACCOUNT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
	 + IDBConstants.TABLE_CURRENCY + ")");
	 System.out.println("Table " + IDBConstants.TABLE_PMT_ES_DIFF_DETAIL + " is created");
	 }
	 
	 public static void deletePaymentESDifferenceDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_ES_DIFF_DETAIL);
	 System.out.println("Table " + IDBConstants.TABLE_PMT_ES_DIFF_DETAIL + " is destroyed");
	 }*/
	
	public static void createPaymentUnitCashBankTransferTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " varchar(32), " +
				IDBConstants.ATTR_RCV_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_RCV_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_RCV_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_RCV_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		System.out.println("Table " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS + " is created");
	}
	
	public static void deletePaymentUnitCashBankTransferTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS);
		System.out.println("Table " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS + " is destroyed");
	}
	
	/*public static void createPaymentUnitCashBankTransferDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS_DETAIL + "(" +
	 IDBConstants.ATTR_PMT_UNIT_BANKCASH_TRNS + " INT(38), " +
	 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
	 IDBConstants.ATTR_VALUE + " INT(38,2), " +
	 IDBConstants.ATTR_CURRENCY + " INT(38), " +
	 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PMT_UNIT_BANKCASH_TRNS + ") REFERENCES " +
	 IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
	 IDBConstants.TABLE_ACCOUNT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
	 + IDBConstants.TABLE_CURRENCY + ")");
	 System.out.println("Table " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS_DETAIL + " is created");
	 }
	 
	 public static void deletePaymentUnitCashBankTransferDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS_DETAIL);
	 System.out.println("Table " + IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS_DETAIL + " is destroyed");
	 }*/
	
	public static void createPaymentEmployeeReceivableTable(Statement stm) throws SQLException {
		String query="CREATE TABLE " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
		IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
		IDBConstants.ATTR_PAY_TO + " INT(38), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		IDBConstants.ATTR_UNIT + " INT(38), " +
		IDBConstants.ATTR_ENDING_BALANCE + " FIXED(38,2), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		stm.executeUpdate( query);
		System.out.println("Table " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE + " is created");
		System.out.println(query);
	}
	
	public static void deletePaymentEmployeeReceivableTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE);
		System.out.println("Table " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE + " is destroyed");
	}
	
	/*public static void createPaymentEmployeeReceivableDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE_DETAIL + "(" +
	 IDBConstants.ATTR_PMT_EMP_RECEIVABLE + " INT(38), " +
	 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
	 IDBConstants.ATTR_VALUE + " INT(38,2), " +
	 IDBConstants.ATTR_CURRENCY + " INT(38), " +
	 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PMT_EMP_RECEIVABLE + ") REFERENCES " +
	 IDBConstants.TABLE_PMT_EMP_RECEIVABLE + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
	 IDBConstants.TABLE_ACCOUNT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
	 + IDBConstants.TABLE_CURRENCY + ")");
	 System.out.println("Table " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE_DETAIL + " is created");
	 }
	 
	 public static void deletePaymentEmployeeReceivableDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE_DETAIL);
	 System.out.println("Table " + IDBConstants.TABLE_PMT_EMP_RECEIVABLE_DETAIL + " is destroyed");
	 }*/
	
	public static void createPaymentLoanTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_LOAN + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " INT(38), " +
				IDBConstants.ATTR_LOAN_RECEIPT + " INT(38), " +
				IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
				IDBConstants.ATTR_CURRENCY + " INT (38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAY_TO + ") REFERENCES " +
				IDBConstants.TABLE_COMPANY_LOAN + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				//ini diambil dari tabel RCVLOAN yang berstatus posted=3
				"FOREIGN KEY (" + IDBConstants.ATTR_LOAN_RECEIPT + ") REFERENCES " +
				IDBConstants.TABLE_RCV_LOAN + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		System.out.println("Table " + IDBConstants.TABLE_PMT_LOAN + " is created");
	}
	
	public static void deletePaymentLoanTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_LOAN);
		System.out.println("Table " + IDBConstants.TABLE_PMT_LOAN + " is destroyed");
	}
	
	/*public static void createPaymentLoanDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_LOAN_DETAIL + "(" +
	 IDBConstants.ATTR_PMT_LOAN + " INT(38), " +
	 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
	 IDBConstants.ATTR_VALUE + " INT(38,2), " +
	 IDBConstants.ATTR_CURRENCY + " INT(38), " +
	 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PMT_LOAN + ") REFERENCES " +
	 IDBConstants.TABLE_PMT_LOAN + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
	 IDBConstants.TABLE_ACCOUNT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
	 + IDBConstants.TABLE_CURRENCY + ")");
	 System.out.println("Table " + IDBConstants.TABLE_PMT_LOAN_DETAIL + " is created");
	 }
	 
	 public static void deletePaymentLoanDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_LOAN_DETAIL);
	 System.out.println("Table " + IDBConstants.TABLE_PMT_LOAN_DETAIL + " is destroyed");
	 }*/
	
	public static void createPaymentOthersTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_OTHERS + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_PAY_TO + " varchar(100), " +
				IDBConstants.ATTR_CURRENCY + " INT (38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT_GROUP + " smallint, " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES " +
				IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		System.out.println("Table " + IDBConstants.TABLE_PMT_OTHERS + " is created");
	}
	
	public static void deletePaymentOthersTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_OTHERS);
		System.out.println("Table " + IDBConstants.TABLE_PMT_OTHERS + " is destroyed");
	}
	
	public static void createPaymentOthersDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PMT_OTHERS_DETAIL + "(" +
				IDBConstants.ATTR_PAYMENT_OTHERS + " INT(38), " +
				IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAYMENT_OTHERS + ") REFERENCES "
				+ IDBConstants.TABLE_PMT_OTHERS + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_PMT_OTHERS_DETAIL + " is created");
	}
	
	public static void deletePaymentOthersDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PMT_OTHERS_DETAIL);
		System.out.println("Table " + IDBConstants.TABLE_PMT_OTHERS_DETAIL + " is destroyed");
	}
	
	public static void createSalesAdvanceTable(Statement stm) throws SQLException {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_SALES_ADVANCE + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_RECEIVE_TO + " varchar(32), " +
		IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_CUSTOMER_STATUS + " varchar(40), " +
		IDBConstants.ATTR_PROJECT + " INT(38), " +
		IDBConstants.ATTR_SALES_ADV_PERCENT + " FIXED(10,2), " +
		IDBConstants.ATTR_SALES_ADV_CURR + " INT(38), " +
		IDBConstants.ATTR_SALES_ADV_EXCHRATE + " FIXED(38,2), " +
		IDBConstants.ATTR_SALES_ADV_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_VAT_PERCENT + " FIXED(10,2), " +
		IDBConstants.ATTR_VAT_CURR + " INT (38), " +
		IDBConstants.ATTR_VAT_EXCHRATE + " FIXED(38,2), " +
		IDBConstants.ATTR_VAT_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_TAX23_PERCENT + " FIXED(10,2), " +
		IDBConstants.ATTR_TAX23_CURR + " INT (38), " +
		IDBConstants.ATTR_TAX23_EXCHRATE + " FIXED(38,2), " +
		IDBConstants.ATTR_TAX23_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		IDBConstants.ATTR_UNIT + " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
		"PROJECTDATA" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_SALES_ADV_CURR + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_VAT_CURR + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TAX23_CURR + ") REFERENCES " +
		IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_ADVANCE + " is created");
	}
	
	public static void deleteSalesAdvanceTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_ADVANCE);
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_ADVANCE + " is destroyed");
	}
	
	public static void createSalesAdvanceDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SALES_ADVANCE_DETAIL + "(" +
				IDBConstants.ATTR_SALES_ADVANCE + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_SALES_ADVANCE + ") REFERENCES " +
				IDBConstants.TABLE_SALES_ADVANCE + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_ADVANCE_DETAIL + " is created");
	}
	
	public static void deleteSalesAdvanceDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_ADVANCE_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_ADVANCE_DETAIL + " is destroyed");
	}
	
	public static void createSalesInvoiceTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SALES_INVOICE + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_SALES_CURR + " INT (38), " +
				IDBConstants.ATTR_SALES_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_SALES_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_RECEIVE_FROM + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_DOWNPAYMENT_CURR + " INT(38), " +
				IDBConstants.ATTR_DOWNPAYMENT_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_SALES_ADVANCE + " INT(38), " +
				IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " + // tambahan
				IDBConstants.ATTR_VAT_PERCENT + " FIXED(10,2), " +
				IDBConstants.ATTR_VAT_CURR + " INT(38), " +
				IDBConstants.ATTR_VAT_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_VAT_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_BRIEF_DESC + " varchar(255), " +
				IDBConstants.ATTR_ATTENTION + " varchar(255), " +
				IDBConstants.ATTR_EMP_AUTHORIZE + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_AUTHORIZE + " varchar(60), " +
				IDBConstants.ATTR_DATE_AUTHORIZE + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_CUSTOMER + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_ACTIVITY + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CUSTOMER+ ") REFERENCES " +
				IDBConstants.TABLE_CUSTOMER + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				
				"FOREIGN KEY (" + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_ACTIVITY  + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ACTIVITY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				"PROJECTDATA" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_SALES_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_VAT_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_AUTHORIZE + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_SALES_ADVANCE + ") REFERENCES " +
				"SALESADVANCE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_INVOICE + " is created");
	}
	
	public static void deleteSalesInvoiceTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_INVOICE);
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_INVOICE + " is destroyed");
	}
	
	/*public static void createSalesInvoiceDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SALES_INVOICE_DETAIL + "(" +
	 IDBConstants.ATTR_SALES_INVOICE + " INT(38), " +
	 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
	 IDBConstants.ATTR_VALUE + " INT(38,2), " +
	 IDBConstants.ATTR_CURRENCY + " INT(38), " +
	 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_SALES_INVOICE + ") REFERENCES " +
	 IDBConstants.TABLE_SALES_INVOICE + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
	 IDBConstants.TABLE_ACCOUNT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
	 + IDBConstants.TABLE_CURRENCY + ")");
	 
	 System.out.println("Table " + IDBConstants.TABLE_SALES_INVOICE_DETAIL + " is created");
	 }
	 
	 public static void deleteSalesInvoiceDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_INVOICE_DETAIL);
	 
	 System.out.println("Table " + IDBConstants.TABLE_SALES_INVOICE_DETAIL + " is destroyed");
	 }*/
	
	public static void createSalesItemTable(Statement stm) throws SQLException {
		String query = "CREATE TABLE " + IDBConstants.TABLE_SALES_ITEM + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT , " +
		IDBConstants.ATTR_NUMBER_INVOICE + " varchar(255), " +
		IDBConstants.ATTR_SALES_INVOICE + " INT (38), " +
		IDBConstants.ATTR_SPECIFICATION + " varchar(255), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_QTY + " FIXED(38,4), " +
		IDBConstants.ATTR_UNIT_PRICE + " FIXED(38,2), " +
		IDBConstants.ATTR_PERSON_AMOUNT + " FIXED(38,2), " +
		IDBConstants.ATTR_PERSON_DESC + " varchar(255), " +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_SALES_INVOICE + ") REFERENCES " +
		IDBConstants.TABLE_SALES_INVOICE + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		stm.executeUpdate(query);
		System.out.println("Table " + IDBConstants.TABLE_SALES_ITEM + " is created");
	}
	
	public static void deleteSalesItemTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_ITEM);
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_ITEM + " is destroyed");
	}
	
	public static void createSalesItemDetailTable(Statement stm) throws SQLException {
		String query = "CREATE TABLE " + IDBConstants.TABLE_SALES_ITEM_DETAIL + "(" +
		IDBConstants.ATTR_SALES_ITEM + " INT (38)," +
		IDBConstants.ATTR_NUMBER_INVOICE  + " varchar(255), " +
		IDBConstants.ATTR_SPECIFICATION + " varchar(255), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_QTY + " FIXED(20,2), " +
		IDBConstants.ATTR_UNIT_PRICE + " FIXED(38,2), " +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_SALES_ITEM + ") REFERENCES " +
		IDBConstants.TABLE_SALES_ITEM + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )";
		stm.executeUpdate(query);
		System.out.println("Table " + IDBConstants.TABLE_SALES_INVOICE + " is created");
	}
	
	public static void deleteSalesItemDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_ITEM_DETAIL );
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_ITEM_DETAIL + " is destroyed");
	}
	
	public static void createSalesARReceivedTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SALES_AR_RECEIVED + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_INVOICE + " INT(38), " +
				IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
				IDBConstants.ATTR_CUSTOMER_STATUS + " varchar(40), " +
				IDBConstants.ATTR_RECEIVE_TO + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_SALES_AR_CURR + " INT(38), " +
				IDBConstants.ATTR_SALES_AR_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_SALES_AR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_VAT_CURR + " INT (38), " +
				IDBConstants.ATTR_VAT_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_VAT_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_TAX23_PERCENT + " FIXED(10,2), " +
				IDBConstants.ATTR_TAX23_CURR + " int(38), " +
				IDBConstants.ATTR_TAX23_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_TAX23_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_BANKCHARGES_CURR + " INT(38), " +
				IDBConstants.ATTR_BANKCHARGES_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_BANKCHARGES_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_RETENTION_CURR + " INT(38), " + // RETENTION !!!!!
				IDBConstants.ATTR_RETENTION_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_TRANSLATION_CURR + " INT(38), " +
				IDBConstants.ATTR_TRANSLATION_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_SALES_AR_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TAX23_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_AR_RECEIVED + " is created");
	}
	
	public static void deleteSalesARReceivedTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_AR_RECEIVED);
		
		System.out.println("Table " + IDBConstants.TABLE_SALES_AR_RECEIVED + " is destroyed");
	}
	
	/*public static void createSalesARReceivedDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SALES_AR_RECEIVED_DETAIL + "(" +
	 IDBConstants.ATTR_SALES_AR_RECEIVED + " INT(38), " +
	 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
	 IDBConstants.ATTR_VALUE + " INT(38,2), " +
	 IDBConstants.ATTR_CURRENCY + " INT(38), " +
	 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_SALES_AR_RECEIVED + ") REFERENCES " +
	 IDBConstants.TABLE_SALES_AR_RECEIVED + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
	 IDBConstants.TABLE_ACCOUNT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
	 + IDBConstants.TABLE_CURRENCY + ")");
	 
	 System.out.println("Table " + IDBConstants.TABLE_SALES_AR_RECEIVED_DETAIL + " is created");
	 }
	 
	 public static void deleteSalesARReceivedDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SALES_AR_RECEIVED_DETAIL);
	 
	 System.out.println("Table " + IDBConstants.TABLE_SALES_AR_RECEIVED_DETAIL + " is destroyed");
	 }
	 */
	public static void createPurchaseReceiptTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PURCHASE_RECEIPT + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_INVOICE + " varchar(100), " +
				IDBConstants.ATTR_INVOICE_DATE + " date, " +
				IDBConstants.ATTR_SUPPLIER + " INT(38), " +
				IDBConstants.ATTR_SUPPLIER_TYPE + " varchar(50), " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_PROJECT_TYPE + " varchar(255), " +
				IDBConstants.ATTR_AP_CURR + " INT(38), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_AP_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_VAT_PERCENT + " FIXED(10,2), " +
				IDBConstants.ATTR_VAT_CURR + " INT (38), " +
				IDBConstants.ATTR_VAT_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " varchar(255), " +
				IDBConstants.ATTR_DUE_DATE + " date, " +
				IDBConstants.ATTR_CONTRACT_NO + " varchar(100), " +
				IDBConstants.ATTR_CONTRACT_DATE + " date, " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT+" INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_PROJECT_DATA  + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUPPLIER + ") REFERENCES " +
				"PARTNER" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_AP_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_VAT_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_RECEIPT + " is created");
	}
	
	public static void deletePurchaseReceiptTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PURCHASE_RECEIPT);
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_RECEIPT + " is destroyed");
	}
	
	/*public static void createPurchaseReceiptDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PURCHASE_RECEIPT_DETAIL + "(" +
	 IDBConstants.ATTR_PURCHASE_RECEIPT + " INT(38), " +
	 IDBConstants.ATTR_ACCOUNT + " INT(38), " +
	 IDBConstants.ATTR_VALUE + " INT(38,2), " +
	 IDBConstants.ATTR_CURRENCY + " INT(38), " +
	 IDBConstants.ATTR_EXCHANGE_RATE + " INT(38,2), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PURCHASE_RECEIPT + ") REFERENCES " +
	 IDBConstants.TABLE_PURCHASE_RECEIPT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
	 IDBConstants.TABLE_ACCOUNT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
	 + IDBConstants.TABLE_CURRENCY + ")");
	 
	 System.out.println("Table " + IDBConstants.TABLE_PURCHASE_RECEIPT_DETAIL + " is created");
	 }
	 
	 public static void deletePurchaseReceiptDetailTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PURCHASE_RECEIPT);
	 
	 System.out.println("Table " + IDBConstants.TABLE_PURCHASE_RECEIPT + " is destroyed");
	 }*/
	
	public static void createPurchaseReceiptItemTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PURCHASE_RECEIPT_ITEM + "(" +
				IDBConstants.ATTR_PURCHASE_RECEIPT + " INT (38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_SPECIFICATION + " varchar(255), " +
				IDBConstants.ATTR_QTY + " FIXED(20,2), " +
				IDBConstants.ATTR_UNIT_PRICE + " FIXED(38,2), " +
				IDBConstants.ATTR_AMOUNT + " FIXED(38,2), " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PURCHASE_RECEIPT + ") REFERENCES " +
				IDBConstants.TABLE_PURCHASE_RECEIPT + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_RECEIPT_ITEM + " is created");
	}
	
	public static void deletePurchaseReceiptItemTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PURCHASE_RECEIPT_ITEM);
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_RECEIPT_ITEM + " is destroyed");
	}
	
	public static void createPurchaseAPPaymentTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PURCHASE_AP_PMT+ "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO	 + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PURCHASE_RECEIPT + " INT(38), " +
				IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
				IDBConstants.ATTR_AP_PMT_CURR + " INT(38), " +
				IDBConstants.ATTR_AP_PMT_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_AP_PMT_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_TAX23_PERCENT + " FIXED(10,2), " +
				IDBConstants.ATTR_TAX23_CURR + " INT(38), " +
				IDBConstants.ATTR_TAX23_EXCHRATE + " FIXED(38,2), " +
				IDBConstants.ATTR_TAX23_AMOUNT + " FIXED(38,2), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PURCHASE_RECEIPT + ") REFERENCES " +
				IDBConstants.TABLE_PURCHASE_RECEIPT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_AP_PMT_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TAX23_CURR + ") REFERENCES " +
				IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_AP_PMT + " is created");
	}
	
	public static void deletePurchaseAPPaymentTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PURCHASE_AP_PMT);
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_AP_PMT + " is destroyed");
	}
	
	public static void createPurchaseAPPaymentDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PURCHASE_AP_PMT_DETAIL + "(" +
				IDBConstants.ATTR_PURCHASE_AP_PMT + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PURCHASE_AP_PMT + ") REFERENCES " +
				IDBConstants.TABLE_PURCHASE_AP_PMT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_AP_PMT_DETAIL + " is created");
	}
	
	public static void deletePurchaseAPPaymentDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PURCHASE_AP_PMT_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_PURCHASE_AP_PMT_DETAIL + " is destroyed");
	}
	
	public static void createPayrollPaymentSalaryHOTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " varchar(255), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO + " is created");
	}
	
	public static void deletePayrollPaymentSalaryHOTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO + " is destroyed");
	}
	
	public static void createPayrollPaymentSalaryHODetailTable(Statement stm) throws SQLException {
		String query="CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO_DETAIL + "(" +
		IDBConstants.ATTR_PAYROLL_PMT_SALARY_HO + " INT(38), " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_ACCOUNT + " INT(38), " +
		IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
		IDBConstants.ATTR_POSTED_DATE + " date, " +
		IDBConstants.ATTR_DESCRIPTION +  " Varchar(255),"+
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_SALARY_HO + ") REFERENCES " +
		IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE," +
		"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
		IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(query);
		stm.executeUpdate(query);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO_DETAIL + " is created");
	}
	
	public static void deletePayrollPaymentSalaryHODetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO + " is destroyed");
	}
	
	/*	public static void createPayrollPaymentSalaryHOPayableTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO_PAYABLE + "(" +
	 IDBConstants.ATTR_PAYROLL_PMT_SALARY_HO + " INT(38), " +
	 IDBConstants.ATTR_PAYROLL_COMP_PAYABLE + " INT(38), " +
	 IDBConstants.ATTR_UNIT + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
	 IDBConstants.TABLE_UNIT + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_SALARY_HO + ") REFERENCES " +
	 IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO + "," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_COMP_PAYABLE + ") REFERENCES "
	 + IDBConstants.TABLE_TRANSACTION_POSTED + ")");
	 
	 System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO_PAYABLE + " is created");
	 }
	 
	 public static void deletePayrollPaymentSalaryHOPayableTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO_PAYABLE);
	 
	 System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_HO_PAYABLE + " is destroyed");
	 }*/
	
	public static void createPayrollPaymentSalaryUnitTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " varchar(255), " +
				IDBConstants.ATTR_DEPARTMENT_GROUP + " smallint, " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT + " is created");
	}
	
	public static void deletePayrollPaymentSalaryUnitTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT + " is destroyed");
	}
	
	public static void createPayrollPaymentSalaryUnitDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT_DETAIL + "(" +
				IDBConstants.ATTR_PAYROLL_PMT_SALARY_UNIT + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_BALANCE_CODE + " smallint, " +
				IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_SALARY_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT_DETAIL + " is created");
	}
	
	public static void deletePayrollPaymentSalaryUnitDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT_DETAIL + " is destroyed");
	}
	
	public static void createPayrollPaymentTax21HOTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " varchar(255), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO + " is created");
	}
	
	public static void deletePayrollPaymentTax21HOTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO + " is destroyed");
	}
	
	public static void createPayrollPaymentTax21HODetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_DETAIL + "(" +
				IDBConstants.ATTR_PAYROLL_PMT_TAX21_HO + " INT(38), " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_POSTED_DATE + " date, " +
				IDBConstants.ATTR_DESCRIPTION +  " Varchar(255),"+
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_TAX21_HO + ") REFERENCES " +
				IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_DETAIL + " is created");
	}
	
	public static void deletePayrollPaymentTax21HODetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_DETAIL + " is destroyed");
	}
	
	/*public static void createPayrollPaymentTax21HOPayableTable(Statement stm) throws SQLException {
	 stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_PAYABLE + "(" +
	 IDBConstants.ATTR_PAYROLL_PMT_TAX21_HO + " INT(38), " +
	 IDBConstants.ATTR_TAX21_PAYABLE + " INT(38), " +
	 "FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_TAX21_HO + ") REFERENCES " +
	 IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO + " ON DELETE CASCADE," +
	 "FOREIGN KEY (" + IDBConstants.ATTR_TAX21_PAYABLE + ") REFERENCES "
	 + IDBConstants.TABLE_TRANSACTION_POSTED + ")");
	 
	 System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_PAYABLE + " is created");
	 }
	 
	 public static void deletePayrollPaymentTax21HOPayableTable(Statement stm) throws SQLException {
	 stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_PAYABLE);
	 
	 System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO_PAYABLE + " is destroyed");
	 }*/
	
	public static void createPayrollPaymentTax21UnitTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " varchar(255), " +
				IDBConstants.ATTR_DEPARTMENT_GROUP + " smallint, " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT + " is created");
	}
	
	public static void deletePayrollPaymentTax21UnitTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT + " is destroyed");
	}
	
	public static void createPayrollPaymentTax21UnitDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT_DETAIL + "(" +
				IDBConstants.ATTR_PAYROLL_PMT_TAX21_UNIT + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_BALANCE_CODE + " smallint, " +
				IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_TAX21_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT_DETAIL + " is created");
	}
	
	public static void deletePayrollPaymentTax21UnitDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT_DETAIL + " is destroyed");
	}
	
	public static void createPayrollPaymentEmployeeInsuranceTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PAYMENT_SOURCE + " varchar(32), " +
				IDBConstants.ATTR_CASH_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_BANK_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_CHEQUE_NO + " varchar(40), " +
				IDBConstants.ATTR_CHEQUE_DUE_DATE + " date, " +
				IDBConstants.ATTR_PAY_TO + " varchar(255), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_RECEIVED + " date, " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CASH_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_CASH_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_BANK_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_BANK_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE + " is created");
	}
	
	public static void deletePayrollPaymentEmployeeInsuranceTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE + " is destroyed");
	}
	
	public static void createPayrollPaymentEmployeeInsuranceDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_DETAIL + "(" +
				IDBConstants.ATTR_PAYROLL_PMT_EMP_INSURANCE + " INT(38), " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_POSTED_DATE + " date, " +
				IDBConstants.ATTR_DESCRIPTION +  " Varchar(255),"+
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_EMP_INSURANCE + ") REFERENCES " +
				IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_DETAIL + " is created");
	}
	
	public static void deletePayrollPaymentEmployeeInsuranceDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_DETAIL + " is destroyed");
	}
	
	public static void createPayrollPaymentEmployeeInsurancePayableTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_PAYABLE + "(" +
				IDBConstants.ATTR_PAYROLL_PMT_EMP_INSURANCE + " INT(38), " +
				IDBConstants.ATTR_TAX21_PAYABLE + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_PMT_EMP_INSURANCE + ") REFERENCES " +
				IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TAX21_PAYABLE + ") REFERENCES "
				+ IDBConstants.TABLE_TRANSACTION_POSTED + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_PAYABLE + " is created");
	}
	
	public static void deletePayrollPaymentEmployeeInsurancePayableTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_PAYABLE);
		
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_PMT_EMP_INSURANCE_PAYABLE + " is destroyed");
	}
	
	public static void createExpenseSheetTable(Statement stm) throws SQLException {
		String query = "CREATE TABLE " + IDBConstants.TABLE_EXPENSE_SHEET + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_ES_PROJECT_TYPE + " varchar(60), " +
		IDBConstants.ATTR_PMT_CA_IOU_PROJECT_SETTLED + " INT(38), " +
		IDBConstants.ATTR_PMT_CA_PROJECT + " INT(38), " +
		IDBConstants.ATTR_PMT_CA_IOU_OTHERS_SETTLED + " INT(38), " +
		IDBConstants.ATTR_PMT_CA_OTHERS + " INT(38), " +
		IDBConstants.ATTR_BEGINNING_BALANCE + " INT(38), " +
		IDBConstants.ATTR_ES_OWNER + " INT(38), " +
		IDBConstants.ATTR_CURRENCY + " INT (38), " +
		IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2)," +
		IDBConstants.ATTR_AMOUNT + " FIXED(38,2), "+
		IDBConstants.ATTR_DEPARTMENT_GROUP + " smallint, " +
		IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		//ini diubah dengan EmpReceive Biar lebih Mudah
		//   IDBConstants.ATTR_EMP_CREATED + " INT(38), " +
		//   IDBConstants.ATTR_JOB_TITLE_CREATED + " varchar(60), " +
		//   IDBConstants.ATTR_DATE_CREATED + " date, " +
		IDBConstants.ATTR_EMP_RECEIVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_RECEIVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_RECEIVED + " date, " +
		
		IDBConstants.ATTR_PROJECT + " INT(38), " +
		//pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_ACTIVITY + " INT(38), " +
		//IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
		//IDBConstants.ATTR_PROJECT + " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" +  IDBConstants.ATTR_PMT_CA_IOU_PROJECT_SETTLED+ ") REFERENCES " +
		IDBConstants.TABLE_PMT_CA_IOU_PROJECT_SETTLED + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_PROJECT + ") REFERENCES " +
		IDBConstants.TABLE_PMT_CA_PROJECT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_IOU_OTHERS_SETTLED + ") REFERENCES " +
		IDBConstants.TABLE_PMT_CA_IOU_OTHERS_SETTLED + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PMT_CA_OTHERS + ") REFERENCES " +
		IDBConstants.TABLE_PMT_CA_OTHERS + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		// dapat dilihat ketika search project code ketika akan mensearch
		//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
		//IDBConstants.TABLE_UNIT + "," +
		//"FOREIGN KEY (" + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_ACTIVITY  + ") REFERENCES " +
		//pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ACTIVITY + "," +
		//"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT  + ") REFERENCES " +
		//pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_ES_OWNER + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
		"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
		+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		//"FOREIGN KEY (" + IDBConstants.ATTR_EMP_CREATED + ") REFERENCES " +
		//"EMPLOYEE" + ")" ;
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_RECEIVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" ;
		System.out.println(query);
		stm.executeUpdate(query);
		System.out.println("Table " + IDBConstants.TABLE_EXPENSE_SHEET + " is created");
	}
	
	public static void deleteExpenseSheetTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EXPENSE_SHEET);
		System.out.println("Table " + IDBConstants.TABLE_EXPENSE_SHEET + " is destroyed");
	}
	
	public static void createExpenseSheetDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_EXPENSE_SHEET_DETAIL + "(" +
				IDBConstants.ATTR_EXPENSE_SHEET + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				//IDBConstants.ATTR_UNIT + " INT(38), " +
				//"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				//IDBConstants.TABLE_UNIT + "," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EXPENSE_SHEET + ") REFERENCES " +
				IDBConstants.TABLE_EXPENSE_SHEET + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		System.out.println("Table " + IDBConstants.TABLE_EXPENSE_SHEET_DETAIL + " is created");
	}
	
	public static void deleteExpenseSheetDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EXPENSE_SHEET_DETAIL);
		System.out.println("Table " + IDBConstants.TABLE_EXPENSE_SHEET_DETAIL + " is destroyed");
	}
	
	public static void createMemorialJournalStandardTable(Statement stm) throws SQLException {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + "(" +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_TRANSACTION + " INT(38), " +
		IDBConstants.ATTR_TRANSACTION_CODE + " INT(38), " +
		IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
		IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
		IDBConstants.ATTR_STATUS + " smallint, " +
		IDBConstants.ATTR_SUBMIT_DATE + " date, " +
		IDBConstants.ATTR_UNIT + " INT(38), " +
		IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
		IDBConstants.ATTR_DEPARTMENT_GROUP + " smallint, " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
		IDBConstants.ATTR_REMARKS + " varchar(255), " +
		IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
		IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
		IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
		IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
		IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
		IDBConstants.ATTR_DATE_APPROVED + " date, " +
		IDBConstants.ATTR_PROJECT+ " INT(38), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION_CODE+ ") REFERENCES " +
		IDBConstants.TABLE_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT+ ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
		IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES "
		+ IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
		"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
		"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
		"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + " is created");
	}
	
	public static void deleteMemorialJournalStandardTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD);
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + " is destroyed");
	}
	
	public static void createMemorialJournalStandardDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD_DETAIL + "(" +
				IDBConstants.ATTR_MEMORIAL_JOURNAL_STANDARD + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_BALANCE_CODE + " varchar(10), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_MEMORIAL_JOURNAL_STANDARD + ") REFERENCES " +
				IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD_DETAIL + " is created");
	}
	
	public static void deleteMemorialJournalStandardDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD_DETAIL + " is destroyed");
	}
	public static void createMemorialJournalNonStandardTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_TRANSACTION + " INT(38), " +
				IDBConstants.ATTR_JOURNAL + " INT(38), " +
				IDBConstants.ATTR_REFERENCE_NO + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_DATE + " date, " +
				IDBConstants.ATTR_STATUS + " smallint, " +
				IDBConstants.ATTR_SUBMIT_DATE + " date, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT_GROUP + " smallint, " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_REMARKS + " varchar(255), " +
				IDBConstants.ATTR_EMP_ORIGINATOR + " INT(38)," +
				IDBConstants.ATTR_JOB_TITLE_ORIGINATOR + " varchar(60), " +
				IDBConstants.ATTR_DATE_ORIGINATOR + " date, " +
				IDBConstants.ATTR_EMP_APPROVED + " INT(38), " +
				IDBConstants.ATTR_JOB_TITLE_APPROVED + " varchar(60), " +
				IDBConstants.ATTR_DATE_APPROVED + " date, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES " +
				IDBConstants.TABLE_JOURNAL + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES " +
				IDBConstants.TABLE_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES "
				+ "PROJECTDATA" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES "
				+ IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				"ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_ORIGINATOR + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMP_APPROVED + ") REFERENCES " +
				"EMPLOYEE" + "(" + IDBConstants.ATTR_AUTOINDEX + "))" );
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD + " is created");
	}
	
	public static void deleteMemorialJournalNonStandardTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD);
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD + " is destroyed");
	}
	
	public static void createMemorialJournalNonStandardDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD_DETAIL + "(" +
				IDBConstants.ATTR_MEMORIAL_JOURNAL_NONSTANDARD + " INT(38), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_SUBSIDIARY + " INT(38), " +
				IDBConstants.ATTR_VALUE + " FIXED(38,2), " +
				IDBConstants.ATTR_BALANCE_CODE + " varchar(10), " +
				IDBConstants.ATTR_CURRENCY + " INT(38), " +
				IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_MEMORIAL_JOURNAL_NONSTANDARD + ") REFERENCES " +
				IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ")," +
				"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD_DETAIL + " is created");
	}
	
	public static void deleteMemorialJournalNonStandardDetailTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD_DETAIL);
		
		System.out.println("Table " + IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD_DETAIL + " is destroyed");
	}
	
	public static void createVariableAccountSetting(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_VARIABLE_ACCOUNT_SETTING + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_VARIABLE + " varchar(255), " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_VARIABLE_ACCOUNT_SETTING + " is created");
	}
	
	public static void deleteVariableAccountSetting(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_VARIABLE_ACCOUNT_SETTING);
		
		System.out.println("Table " + IDBConstants.TABLE_VARIABLE_ACCOUNT_SETTING + " is destroyed");
	}
	
	public static void createSubsidiaryAccountSetting(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_ACCOUNT + " INT(38), " +
				IDBConstants.ATTR_SUBSIDIARY_ACCOUNT + " varchar(255), " +
				IDBConstants.ATTR_TRANSACTION_CODE + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT + ") REFERENCES " +
				IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING + " is created");
	}
	
	public static void deleteSubsidiaryAccountSetting(Statement stm) throws SQLException {
		String tableName = IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING;
		tmplDeleteTable(stm, tableName);
		System.out.println("Table " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING + " is destroyed");
	}
	
	private static void tmplDeleteTable(Statement stm, String tableName) throws SQLException {
		stm.executeUpdate("DROP TABLE " + tableName);
		System.out.println("Table " + tableName + " is destroyed");
	}
	
	public static void createBeginningEmployeeReceivableTable(Statement stm) throws SQLException {
		tmplCreateBeginningDetailTable(stm,IDBConstants.ATTR_EMPLOYEE,IDBConstants.TABLE_EMPLOYEE,
				IDBConstants.TABLE_BEGINNING_EMPRECV);
	}
	
	public static void deleteBeginningEmployeeReceivableTable(Statement stm) throws SQLException {
		tmplDeleteTable(stm,IDBConstants.TABLE_BEGINNING_EMPRECV);
	}
	
	public static void createBeginningCashInAdvanceTable(Statement stm) throws SQLException {
		tmplCreateBeginningDetailTable(stm,IDBConstants.ATTR_EMPLOYEE,IDBConstants.TABLE_EMPLOYEE,
				IDBConstants.ATTR_PROJECT, pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_DATA,
				IDBConstants.TABLE_BEGINNING_CASHADVANCE);
	}
	public static void deleteBeginningCashInAdvanceTable(Statement stm) throws SQLException {
		tmplDeleteTable(stm,IDBConstants.TABLE_BEGINNING_CASHADVANCE);
	}
	public static void createBeginningESDiffTable(Statement stm) throws SQLException {
		tmplCreateBeginningDetailTable(stm,IDBConstants.ATTR_EMPLOYEE,IDBConstants.TABLE_EMPLOYEE,
				IDBConstants.ATTR_PROJECT, pohaci.gumunda.titis.project.dbapi.IDBConstants.TABLE_PROJECT_DATA,
				IDBConstants.TABLE_BEGINNING_ESDIFF);
	}
	public static void deleteBeginningESDiffTable(Statement stm) throws SQLException {
		tmplDeleteTable(stm,IDBConstants.TABLE_BEGINNING_ESDIFF);
	}
	public static void createBeginningLoanTable(Statement stm) throws SQLException {
		tmplCreateBeginningDetailTable(stm,IDBConstants.ATTR_COMPANY_LOAN,IDBConstants.TABLE_COMPANY_LOAN,
				IDBConstants.TABLE_BEGINNING_LOAN);
	}
	public static void deleteBeginningLoanTable(Statement stm) throws SQLException {
		tmplDeleteTable(stm, IDBConstants.TABLE_BEGINNING_LOAN);
	}
	
	public static void createIncomeStatementDesignTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_DESIGN + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_REPORT_NAME + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_TITLE + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_LANG + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_POSITIVE_BALANCE + " smallint "
				+ ")");
		
		System.out.println("Table " + IDBConstants.TABLE_INCOME_STATEMENT_DESIGN
				+ " is created");
	}
	
	public static void deleteIncomeStatementDesignTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_INCOME_STATEMENT_DESIGN);
		
		System.out.println("Table " + IDBConstants.TABLE_INCOME_STATEMENT_DESIGN
				+ " is destroyed");
	}
	
	public static void createIncomeStatementJournalTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_JOURNAL + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESIGN + " INT(38), "
				+ IDBConstants.ATTR_JOURNAL + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_DESIGN + ") REFERENCES "
				+ IDBConstants.TABLE_INCOME_STATEMENT_DESIGN  + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL
				+ "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_INCOME_STATEMENT_JOURNAL
				+ " is created");
	}
	
	public static void deleteIncomeStatementJournalTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_INCOME_STATEMENT_JOURNAL);
		
		System.out.println("Table " + IDBConstants.TABLE_INCOME_STATEMENT_JOURNAL
				+ " is destroyed");
	}
	
	public static void createIncomeStatementAccountTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_ACCOUNT_LABEL + " varchar(255), "
				+ IDBConstants.ATTR_ISGROUP + " boolean, "
				+ IDBConstants.ATTR_ISSHOWVALUE + " boolean, "
				+ IDBConstants.ATTR_ISBOLD + " boolean, "
				+ IDBConstants.ATTR_ISITALIC + " boolean, "
				+ IDBConstants.ATTR_ISALSOUSEDINVALUE + " boolean, "
				+ IDBConstants.ATTR_ALIGNMENT + " varchar(50), "
				+ IDBConstants.ATTR_INDENT + " INT(38))");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT + " is created");
	}
	
	public static void deleteIncomeStatementAccountTable(Statement stm)
	throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT + " is destroyed");
	}
	
	public static void createIncomeStatementAccountStructureTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT_STRUCTURE + "("
				+ IDBConstants.ATTR_SUPER_ACCOUNT + " INT(38), "
				+ IDBConstants.ATTR_SUB_ACCOUNT + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_SUPER_ACCOUNT
				+ ") REFERENCES " + IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_SUB_ACCOUNT + ") REFERENCES "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT_STRUCTURE
				+ " has created");
	}
	
	public static void deleteIncomeStatementAccountStructureTable(Statement stm)
	throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT_STRUCTURE);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ACCOUNT_STRUCTURE
				+ " has dropped");
	}
	
	public static void createTrialBalanceJournalTypeSettingTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_JOURNAL + " INT(38), "
				+ IDBConstants.ATTR_COLUMN_NAME + " varchar(255), "
				+ IDBConstants.ATTR_USED + " boolean, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL
				+ ") REFERENCES " + IDBConstants.TABLE_JOURNAL
				+ " (" + IDBConstants.ATTR_AUTOINDEX + ")ON DELETE CASCADE)");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING
				+ " has created");
	}
	
	public static void deleteTrialBalanceJournalTypeSettingTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING
				+ " has dropped");
	}
	
	public static void createTrialBalanceAccountTypeSettingTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_TRIAL_BALANCE_ACCOUNT_TYPE_SETTING + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CATEGORY + " INT(38), "
				+ IDBConstants.ATTR_COLUMN_NAME + " varchar(255), "
				+ IDBConstants.ATTR_USED + " boolean)");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TRIAL_BALANCE_JOURNAL_TYPE_SETTING
				+ " has created");
	}
	
	public static void deleteTrialBalanceAccountTypeSettingTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TRIAL_BALANCE_ACCOUNT_TYPE_SETTING);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TRIAL_BALANCE_ACCOUNT_TYPE_SETTING
				+ " has dropped");
	}
	
	public static void createClosingTransactionTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_TRANSACTION + " INT(38), "
				+ IDBConstants.ATTR_TRANSACTION_DATE + " date, "
				+ IDBConstants.ATTR_REFERENCE_NO + " varchar(255), "
				+ IDBConstants.ATTR_PERIOD_FROM + " date, "
				+ IDBConstants.ATTR_PERIOD_TO + " date,"
				+ IDBConstants.ATTR_UNIT + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_UNIT
				+ ") REFERENCES " + IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION
				+ " has created");
	}
	
	public static void deleteClosingTransactionTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION
				+ " has dropped");
	}
	
	public static void createClosingTransactionDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION_DETAIL + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CLOSING_TRANSACTION + " INT(38), "
				+ IDBConstants.ATTR_ACCOUNT + " INT(38), "
				+ IDBConstants.ATTR_VALUE + " FIXED(38,2), "
				+ IDBConstants.ATTR_CURRENCY + " INT(38), "
				+ IDBConstants.ATTR_EXCHANGE_RATE + " FIXED(38,2), "
				+ IDBConstants.ATTR_BALANCE_CODE + " smallint, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_CLOSING_TRANSACTION
				+ ") REFERENCES " + IDBConstants.TABLE_CLOSING_TRANSACTION + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_ACCOUNT
				+ ") REFERENCES " + IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY
				+ ") REFERENCES " + IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION_DETAIL
				+ " has created");
	}
	
	public static void deleteClosingTransactionDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION_DETAIL);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_CLOSING_TRANSACTION_DETAIL
				+ " has dropped");
	}
	
	public static void createIncomeStatementRowsTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ROWS + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESIGN + " INT(38), "
				+ IDBConstants.ATTR_ROWXML + " varchar(255), "
				+ IDBConstants.ATTR_SECTION + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_DESIGN
				+ ") REFERENCES " + IDBConstants.TABLE_INCOME_STATEMENT_DESIGN + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ROWS
				+ " has created");
	}
	
	public static void deleteIncomeStatementRowsTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ROWS);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ROWS
				+ " has dropped");
	}
	
	public static void createBalanceSheetDesignTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_BALANCE_SHEET_DESIGN + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_REPORT_NAME + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_TITLE + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_LANG + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_POSITIVE_BALANCE + " smallint "
				+ ")");
		
		System.out.println("Table " + IDBConstants.TABLE_BALANCE_SHEET_DESIGN
				+ " is created");
	}
	
	public static void deleteBalanceSheetDesignTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BALANCE_SHEET_DESIGN);
		
		System.out.println("Table " + IDBConstants.TABLE_BALANCE_SHEET_DESIGN
				+ " is destroyed");
	}
	
	public static void createBalanceSheetJournalTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_BALANCE_SHEET_JOURNAL + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESIGN + " INT(38), "
				+ IDBConstants.ATTR_JOURNAL + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_DESIGN + ") REFERENCES "
				+ IDBConstants.TABLE_BALANCE_SHEET_DESIGN  + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL
				+ "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_BALANCE_SHEET_JOURNAL
				+ " is created");
	}
	
	public static void deleteBalanceSheetJournalTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_BALANCE_SHEET_JOURNAL);
		
		System.out.println("Table " + IDBConstants.TABLE_BALANCE_SHEET_JOURNAL
				+ " is destroyed");
	}
	
	public static void createBalanceSheetRowsTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_BALANCE_SHEET_ROWS + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESIGN + " INT(38), "
				+ IDBConstants.ATTR_ROWXML + " varchar(255), "
				+ IDBConstants.ATTR_SECTION + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_DESIGN
				+ ") REFERENCES " + IDBConstants.TABLE_BALANCE_SHEET_DESIGN + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ROWS
				+ " has created");
	}
	
	public static void deleteBalanceSheetRowsTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_BALANCE_SHEET_ROWS);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_BALANCE_SHEET_ROWS
				+ " has dropped");
	}
	
	// CF
	
	public static void createIndirectCashFlowStatementDesignTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_CASH_FLOW_DESIGN + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_REPORT_NAME + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_TITLE + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_LANG + " varchar(255), "
				+ IDBConstants.ATTR_REPORT_POSITIVE_BALANCE + " smallint "
				+ ")");
		
		System.out.println("Table " + IDBConstants.TABLE_CASH_FLOW_DESIGN
				+ " is created");
	}
	
	public static void deleteIndirectCashFlowStatementDesignTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CASH_FLOW_DESIGN);
		
		System.out.println("Table " + IDBConstants.TABLE_CASH_FLOW_DESIGN
				+ " is destroyed");
	}
	
	public static void createIndirectCashFlowStatementJournalTable(Statement stm)
	throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_CASH_FLOW_JOURNAL + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESIGN + " INT(38), "
				+ IDBConstants.ATTR_JOURNAL + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_DESIGN + ") REFERENCES "
				+ IDBConstants.TABLE_CASH_FLOW_DESIGN  + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_JOURNAL + ") REFERENCES "
				+ IDBConstants.TABLE_JOURNAL + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table " + IDBConstants.TABLE_CASH_FLOW_JOURNAL
				+ " is created");
	}
	
	public static void createDefaultUnitTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_DEFAULT_UNIT + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_UNIT + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES "
				+ IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		
		System.out.println("Table "
				+ IDBConstants.TABLE_DEFAULT_UNIT
				+ " has created");
	}
	
	public static void deleteDefaultUnitTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_DEFAULT_UNIT);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_DEFAULT_UNIT
				+ " has dropped");
	}
	
	public static void createTransactionPeriodTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_TRANSACTION_PERIOD + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_STARTDATE + " date, "
				+ IDBConstants.ATTR_ENDDATE + " date, "
				+ IDBConstants.ATTR_STATUS + " varchar(255) )");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TRANSACTION_PERIOD
				+ " has created");
	}
	
	public static void deleteTransactionPeriodTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TRANSACTION_PERIOD);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TRANSACTION_PERIOD
				+ " has dropped");
	}
	
	public static void deleteIndirectCashFlowStatementJournalTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CASH_FLOW_JOURNAL);
		
		System.out.println("Table " + IDBConstants.TABLE_CASH_FLOW_JOURNAL
				+ " is destroyed");
	}
	
	public static void createIndirectCashFlowStatementRowsTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_CASH_FLOW_ROWS + "("
				+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESIGN + " INT(38), "
				+ IDBConstants.ATTR_ROWXML + " varchar(255), "
				+ IDBConstants.ATTR_SECTION + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_DESIGN
				+ ") REFERENCES " + IDBConstants.TABLE_CASH_FLOW_DESIGN + "(" + IDBConstants.ATTR_AUTOINDEX + "))");
		
		System.out.println("Table "
				+ IDBConstants.TABLE_CASH_FLOW_ROWS
				+ " has created");
	}
	
	public static void deleteIndirectCashFlowStatementRowsTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_CASH_FLOW_ROWS);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_CASH_FLOW_ROWS
				+ " has dropped");
	}
	
	public static void createPayrollDeptValueTable(Statement stm) throws Exception {
		String query = "CREATE TABLE "
			+ IDBConstants.TABLE_PAYROLL_DEPARTMENT_VALUE + "("
			+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE_PAYROLL+ " INT(38), "
			+ IDBConstants.ATTR_DEPARTMENT + " INT(38), "
			+ IDBConstants.ATTR_ACCOUNT + " INT(38), "
			+ IDBConstants.ATTR_VALUE + " FIXED(38,2), "
			+ "FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES "
			+ "ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + "),"
			+ "FOREIGN KEY (" + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE_PAYROLL + ") REFERENCES "
			+ pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE_PAYROLL + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE,"
			+ "FOREIGN KEY (" +IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
			+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		stm.executeUpdate(query);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_DEPARTMENT_VALUE
				+ " has created");
	}
	
	public static void deletePayrollDeptValueTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_DEPARTMENT_VALUE);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_DEPARTMENT_VALUE
				+ " has dropped");
	}
	
	public static void createTaxArt21DeptValueTable(Statement stm) throws Exception {
		String query = "CREATE TABLE "
			+ IDBConstants.TABLE_TAXART21_DEPARTMENT_VALUE + "("
			+ IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_TAX_ART_21_SUBMIT+ " INT(38), "
			+ IDBConstants.ATTR_DEPARTMENT + " INT(38), "
			+ IDBConstants.ATTR_ACCOUNT + " INT(38), "
			+ IDBConstants.ATTR_VALUE + " FIXED(38,2), "
			+ "FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES "
			+ "ORGANIZATION" + "(" + IDBConstants.ATTR_AUTOINDEX + "),"
			+ "FOREIGN KEY (" + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_TAX_ART_21_SUBMIT + ") REFERENCES "
			+ pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_TAX_ART_21_SUBMIT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE,"
			+ "FOREIGN KEY (" +IDBConstants.ATTR_ACCOUNT + ") REFERENCES "
			+ IDBConstants.TABLE_ACCOUNT + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		stm.executeUpdate(query);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TAXART21_DEPARTMENT_VALUE
				+ " has created");
	}
	
	public static void deleteTaxArt21DeptValueTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TAXART21_DEPARTMENT_VALUE);
		
		System.out.println("Table "
				+ IDBConstants.TABLE_TAXART21_DEPARTMENT_VALUE
				+ " has dropped");
	}
}
