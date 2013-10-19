package pohaci.gumunda.titis.project.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.*;

public class DBCreatorSAP {

	public static void createCustomerCompanyGroupTable(Statement stm) throws Exception{
		String query = "CREATE TABLE " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + "( " +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_NAME + " varchar(255) UNIQUE, " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(255) )";
		System.out.println(query);
		stm.executeUpdate(query);

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + " has created");
	}

	public static void deleteCustomerCompanyGroupTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP );

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + " has destroyed");
	}

	public static void createPartnerCompanyGroupTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_NAME + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255) )" );

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP + " has created");
	}

	public static void deletePartnerCompanyGroupTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP );

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP + " has destroyed");
	}

	public static void createCustomerTable(Statement stm) throws Exception{
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_CUSTOMER + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_NAME + " varchar(255), " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CITY + " varchar(255), " +
				IDBConstants.ATTR_POSTALCODE + " integer, " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_PHONE1 + " varchar(255), " +
				IDBConstants.ATTR_PHONE2 + " varchar(255), " +
				IDBConstants.ATTR_FAX1 + " varchar(255), " +
				IDBConstants.ATTR_FAX2 + " varchar(255), " +
				IDBConstants.ATTR_EMAIL + " varchar(255), " +
				IDBConstants.ATTR_WEBSITE + " varchar(255))" );

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER + " has created");
	}

	public static void deleteCustomerTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CUSTOMER);

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER + " has destroyed");
	}

	public static void createCustomerGroupTable(Statement stm) throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_CUSTOMER_GROUP + "(" +
				IDBConstants.ATTR_CUSTOMER_INDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_GROUP_INDEX + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CUSTOMER_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_CUSTOMER + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_GROUP_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_GROUP + " has created");
	}

	public static void deleteCustomerGroupTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CUSTOMER_GROUP);

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_GROUP + " has destroyed");
	}

	public static void createCustomerAddressTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_CUSTOMER_ADDRESS + "( " +
				IDBConstants.ATTR_CUSTOMER_INDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_ADDRESS_NAME + " varchar(255), " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CITY + " varchar(255), " +
				IDBConstants.ATTR_POSTALCODE + " integer, " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_PHONE1 + " varchar(255), " +
				IDBConstants.ATTR_PHONE2 + " varchar(255), " +
				IDBConstants.ATTR_FAX1 + " varchar(255), " +
				IDBConstants.ATTR_FAX2 + " varchar(255), " +
				IDBConstants.ATTR_EMAIL + " varchar(255), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CUSTOMER_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_CUSTOMER +"(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )" );

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_ADDRESS + " has created");
	}

	public static void deleteCustomerAddressTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CUSTOMER_ADDRESS );

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_ADDRESS + " has destroyed");
	}

	public static void createCustomerContactTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_CUSTOMER_CONTACT + "(" +
				IDBConstants.ATTR_CUSTOMER_INDEX + " INT(38), " +
				IDBConstants.ATTR_PERSONAL_INDEX + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CUSTOMER_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_CUSTOMER + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PERSONAL_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PERSONAL + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_CONTACT + " has created");
	}

	public static void deleteCustomerContactTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CUSTOMER_CONTACT );

		System.out.println("Table " + IDBConstants.TABLE_CUSTOMER_CONTACT + " has destroyed");

	}

	public static void createPartnerTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PARTNER + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_NAME + " varchar(255), " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CITY + " varchar(255), " +
				IDBConstants.ATTR_POSTALCODE + " integer, " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_PHONE1 + " varchar(255), " +
				IDBConstants.ATTR_PHONE2 + " varchar(255), " +
				IDBConstants.ATTR_FAX1 + " varchar(255), " +
				IDBConstants.ATTR_FAX2 + " varchar(255), " +
				IDBConstants.ATTR_EMAIL + " varchar(255), " +
				IDBConstants.ATTR_WEBSITE + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_PARTNER + " has created");
	}

	public static void deletePartnerTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PARTNER);

		System.out.println("Table " + IDBConstants.TABLE_PARTNER + " has destroyed");
	}

	public static void createPartnerGroupTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PARTNER_GROUP + "(" +
				IDBConstants.ATTR_PARTNER_INDEX + " INT(38), " +
				IDBConstants.ATTR_GROUP_INDEX + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PARTNER_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PARTNER + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_GROUP_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PARTNER_COMPANY_GROUP + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_GROUP + " has created");
	}

	public static void deletePartnerGroupTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PARTNER_GROUP);

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_GROUP + " has destroyed");
	}

	public static void createPartnerAddressTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PARTNER_ADDRESS + "( " +
				IDBConstants.ATTR_PARTNER_INDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_ADDRESS_NAME + " varchar(255), " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CITY + " varchar(255), " +
				IDBConstants.ATTR_POSTALCODE + " integer, " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_PHONE1 + " varchar(255), " +
				IDBConstants.ATTR_PHONE2 + " varchar(255), " +
				IDBConstants.ATTR_FAX1 + " varchar(255), " +
				IDBConstants.ATTR_FAX2 + " varchar(255), " +
				IDBConstants.ATTR_EMAIL + " varchar(255), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PARTNER_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PARTNER +"(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )" );

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_ADDRESS + " has created");
	}

	public static void deletePartnerAddressTable(Statement stm) throws Exception{
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PARTNER_ADDRESS );

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_ADDRESS + " has destroyed");
	}

	public static void createPartnerContactTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PARTNER_CONTACT + "(" +
				IDBConstants.ATTR_PARTNER_INDEX + " INT(38), " +
				IDBConstants.ATTR_PERSONAL_INDEX + " INT(38), " +
				/*IDBConstants.ATTR_CODE + " varchar(255), " +
                      IDBConstants.ATTR_NAME + " varchar(255), " +
                      IDBConstants.ATTR_JOB_TITLE + " varchar(255), " +
                      IDBConstants.ATTR_MOBILE_PHONE + " varchar(255), " +
                      IDBConstants.ATTR_OFFICE_PHONE + " varchar(255), " +
                      IDBConstants.ATTR_FAX + " varchar(255), " +
                      IDBConstants.ATTR_EMAIL + " varchar(255), " +
                      IDBConstants.ATTR_ADDRESS + " varchar(255), " +
                      IDBConstants.ATTR_CITY + " varchar(255), " +
                      IDBConstants.ATTR_COUNTRY + " varchar(255), " +*/
				"FOREIGN KEY (" + IDBConstants.ATTR_PARTNER_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PARTNER + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PERSONAL_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PERSONAL + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_CONTACT + " has created");
	}

	public static void deletePartnerContactTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PARTNER_CONTACT );

		System.out.println("Table " + IDBConstants.TABLE_PARTNER_CONTACT + " has destroyed");

	}

	public static void createPersonalTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PERSONAL + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_TITLE + " varchar(255), " +
				IDBConstants.ATTR_FIRST_NAME + " varchar(255), " +
				IDBConstants.ATTR_LAST_NAME + " varchar(255), " +
				IDBConstants.ATTR_NICK_NAME + " varchar(255), " +
				IDBConstants.ATTR_BIRTH_PLACE + " varchar(255), " +
				IDBConstants.ATTR_BIRTH_DATE + " date, " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CITY + " varchar(255), " +
				IDBConstants.ATTR_POSTALCODE + " integer, " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_PHONE1 + " varchar(255), " +
				IDBConstants.ATTR_PHONE2 + " varchar(255), " +
				IDBConstants.ATTR_FAX1 + " varchar(255), " +
				IDBConstants.ATTR_FAX2 + " varchar(255), " +
				IDBConstants.ATTR_EMAIL + " varchar(255), " +
				IDBConstants.ATTR_WEBSITE + " varchar(255))" );

		System.out.println("Table " + IDBConstants.TABLE_PERSONAL + " has created");
	}

	public static void deletePersonalTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PERSONAL );

		System.out.println("Table " + IDBConstants.TABLE_PERSONAL + " has destroyed");
	}

	public static void createPersonalHomeTable(Statement stm) throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_PERSONAL_HOME + "(" +
				IDBConstants.ATTR_PERSONAL_INDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CITY + " varchar(255), " +
				IDBConstants.ATTR_POSTALCODE + " integer, " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_WEBSITE + " varchar(255), " +
				IDBConstants.ATTR_PHONE1 + " varchar(255), " +
				IDBConstants.ATTR_PHONE2 + " varchar(255), " +
				IDBConstants.ATTR_MOBILE1 + " varchar(255), " +
				IDBConstants.ATTR_MOBILE2 + " varchar(255), " +
				IDBConstants.ATTR_FAX + " varchar(255), " +
				"FOREIGN KEY(" + IDBConstants.ATTR_PERSONAL_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PERSONAL + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_PERSONAL_HOME + " has created");
	}

	public static void deletePersonalHomeTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PERSONAL_HOME );

		System.out.println("Table " + IDBConstants.TABLE_PERSONAL_HOME + " has destroyed");
	}

	public static void createPersonalBusinessTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PERSONAL_BUSINESS + "(" +
				IDBConstants.ATTR_PERSONAL_INDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_COMPANY + " varchar(255), " +
				IDBConstants.ATTR_ADDRESS + " varchar(255), " +
				IDBConstants.ATTR_CITY + " varchar(255), " +
				IDBConstants.ATTR_POSTALCODE + " integer, " +
				IDBConstants.ATTR_PROVINCE + " varchar(255), " +
				IDBConstants.ATTR_COUNTRY + " varchar(255), " +
				IDBConstants.ATTR_WEBSITE + " varchar(255), " +
				IDBConstants.ATTR_PHONE1 + " varchar(255), " +
				IDBConstants.ATTR_PHONE2 + " varchar(255), " +
				IDBConstants.ATTR_FAX + " varchar(255), " +
				IDBConstants.ATTR_JOB_TITLE + " varchar(255), " +
				IDBConstants.ATTR_DEPARTEMENT + " varchar(255), " +
				IDBConstants.ATTR_OFFICE + " varchar(255), " +
				"FOREIGN KEY(" + IDBConstants.ATTR_PERSONAL_INDEX + ") REFERENCES " +
				IDBConstants.TABLE_PERSONAL + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table " + IDBConstants.TABLE_PERSONAL_BUSINESS + " has created");
	}

	public static void deletePersonalBusinessTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PERSONAL_BUSINESS );

		System.out.println("Table " + IDBConstants.TABLE_PERSONAL_BUSINESS + " has destroyed");
	}

	public static void createProjectDataTable(Statement stm) throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_PROJECT_DATA + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, " +
				IDBConstants.ATTR_CUSTOMER + " INT(38), " +
				IDBConstants.ATTR_UNIT + " INT(38), " +
				IDBConstants.ATTR_ACTIVITY + " INT(38), " +
				IDBConstants.ATTR_DEPARTMENT + " INT(38), " +
				IDBConstants.ATTR_WORK_DESCRIPTION + " varchar(1024), " +
				IDBConstants.ATTR_REGDATE + " date, " +
				IDBConstants.ATTR_ORNO + " varchar(255), " +
				IDBConstants.ATTR_PONO + " varchar(255), " +
				IDBConstants.ATTR_IPCNO + " varchar(255), " +
				IDBConstants.ATTR_ORDATE + " date, " +
				IDBConstants.ATTR_PODATE + " date, " +
				IDBConstants.ATTR_IPCDATE + " date, " +
				IDBConstants.ATTR_FILE + " varchar(255), " +
				IDBConstants.ATTR_SHEET + " long byte, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CUSTOMER + ") REFERENCES " +
				IDBConstants.TABLE_CUSTOMER + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY ("+ IDBConstants.ATTR_UNIT + ") REFERENCES " +
				pohaci.gumunda.titis.accounting.dbapi.IDBConstants.TABLE_UNIT + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ACTIVITY + ") REFERENCES " +
				pohaci.gumunda.titis.accounting.dbapi.IDBConstants.TABLE_ACTIVITY + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_DEPARTMENT + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_DATA + " has created");
	}

	public static void deleteProjectDataTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_DATA );

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_DATA + " has destroyed");
	}

	public static void createProjectOrganizationAttachTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_ORGANIZATION_ATTACH + "(" +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_FILE + " varchar(255), " +
				IDBConstants.ATTR_SHEET + " long byte, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_ORGANIZATION_ATTACH + " has created");
	}

	public static void deleteProjectOrganizationAttachTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_ORGANIZATION_ATTACH );

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_ORGANIZATION_ATTACH + " has destroyed");
	}

	public static void createProjectPersonalTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_PERSONAL + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_EMPLOYEE + " INT(38), " +
				IDBConstants.ATTR_POSITION + " varchar(255), " +
				IDBConstants.ATTR_TASK + " varchar(255), " +
				IDBConstants.ATTR_WORK_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_STATUS + " varchar(255), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PERSONAL + " has created");
	}

	public static void deleteProjectPersonalTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_PERSONAL);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PERSONAL + " has dropped");
	}

	public static void createProjectContractTable(Statement stm) throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_PROJECT_CONTRACT + "( " +
		IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
		IDBConstants.ATTR_PROJECT + " INT(38), " +
		IDBConstants.ATTR_ESTIMATE_START_DATE + " date, " +
		IDBConstants.ATTR_ESTIMATE_END_DATE + " date, " +
		IDBConstants.ATTR_ACTUAL_START_DATE + " date, " +
		IDBConstants.ATTR_ACTUAL_END_DATE + " date, " +
		IDBConstants.ATTR_VALUE + " FIXED(38, 2), " +
		IDBConstants.ATTR_CURRENCY + " INT(38), " +
		IDBConstants.ATTR_PPN + " boolean, " +
		IDBConstants.ATTR_VALIDATION + " date, " +
		IDBConstants.ATTR_DESCRIPTION + " varchar(1024), " +
		IDBConstants.ATTR_FILE + " varchar(255), " +
		IDBConstants.ATTR_SHEET + " long byte, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
		IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_CURRENCY + ") REFERENCES " +
		pohaci.gumunda.titis.accounting.dbapi.IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_CONTRACT + " has created");
	}

	public static void deleteProjectContractTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_CONTRACT);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_CONTRACT + " has dropped");
	}

	public static void createContractPaymentTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_CONTRACT_PAYMENT + "( " +
				IDBConstants.ATTR_CONTRACT + " INT(38), " +
				IDBConstants.ATTR_DESCRIPTION + " Varchar(255), " +
				IDBConstants.ATTR_VALUE + " FIXED(38, 2), " +
				IDBConstants.ATTR_COMPLETION + " float,  " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CONTRACT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_CONTRACT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )");

		System.out.println("Table " + IDBConstants.TABLE_CONTRACT_PAYMENT + " has created");
	}

	public static void deleteContractPaymentTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_CONTRACT_PAYMENT);

		System.out.println("Table " + IDBConstants.TABLE_CONTRACT_PAYMENT + " has dropped");
	}

	public static void createProjectPartnerTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_PARTNER + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_PARTNER + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PARTNER + ") REFERENCES " +
				IDBConstants.TABLE_PARTNER+ "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PARTNER + " has created");
	}

	public static void deleteProjectPartnerTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_PARTNER);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PARTNER + " has dropped");
	}

	public static void createProjectPartnerContactTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_PARTNER_CONTACT + "(" +
				IDBConstants.ATTR_PARTNER + " INT(38), " +
				IDBConstants.ATTR_PERSONAL + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PARTNER + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_PARTNER + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PERSONAL + ") REFERENCES " +
				IDBConstants.TABLE_PERSONAL + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PARTNER_CONTACT + " has created");
	}

	public static void deleteProjectPartnerContactTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_PARTNER_CONTACT);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PARTNER_CONTACT + " has dropped");
	}


	public static void createProjectClientContactTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_CLIENT_CONTACT + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_PERSONAL + " INT(38), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PERSONAL + ") REFERENCES " +
				IDBConstants.TABLE_PERSONAL + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_CLIENT_CONTACT + " has created");
	}

	public static void deleteProjectClientContactTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_CLIENT_CONTACT);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_CLIENT_CONTACT + " has dropped");
	}

	public static void createProjectLocationTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_LOCATION + "( " +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_LOCATION + " varchar(255), " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255),  " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_LOCATION + " has created");
	}

	public static void deleteProjectLocationTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_LOCATION);

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_LOCATION + " has dropped");
	}


	// timesheet
	public static void createTimeSheetTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_TIME_SHEET + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_ENTRY_DATE + " date, " +
				IDBConstants.ATTR_PREPARED_BY + " INT(38), " +
				IDBConstants.ATTR_PREPARED_DATE + " date, " +
				IDBConstants.ATTR_CHEKED_BY + " INT(38), " +
				IDBConstants.ATTR_CHEKED_DATE + " date, " +
				IDBConstants.ATTR_APPROVAL + " varchar(255), " +
				IDBConstants.ATTR_APPROVAL_DATE + " date, " +
				IDBConstants.ATTR_WORK_DESCRIPTION + " varchar(1024), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PREPARED_BY + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_CHEKED_BY + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_TIME_SHEET + " has created");
	}

	public static void deleteTimeSheetTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TIME_SHEET );

		System.out.println("Table " + IDBConstants.TABLE_TIME_SHEET + " has destroyed");
	}

	public static void createTimeSheetDetailTable(Statement stm) throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_TIME_SHEET_DETAIL + "(" +
		IDBConstants.ATTR_TIME_SHEET + " INT(38), " +
		IDBConstants.ATTR_PERSONAL + " INT(38), " +
		IDBConstants.ATTR_AREA_CODE + " char(2), " +
		IDBConstants.ATTR_QUALIFICATION + " INT(38), " +
		IDBConstants.ATTR_START_DATE + " date, " +
		IDBConstants.ATTR_FINISH_DATE + " date, " +
		IDBConstants.ATTR_DAYS + " integer, " +
		IDBConstants.ATTR_REGULER + " integer, " +
		IDBConstants.ATTR_HOLIDAY + " integer, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_TIME_SHEET + ") REFERENCES " +
		IDBConstants.TABLE_TIME_SHEET + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
		"FOREIGN KEY (" + IDBConstants.ATTR_PERSONAL + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
		"FOREIGN KEY (" + IDBConstants.ATTR_QUALIFICATION + ") REFERENCES " +
		pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_QUALIFICATION + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_TIME_SHEET_DETAIL + " has created");
	}

	public static void deleteTimeSheetDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TIME_SHEET_DETAIL );

		System.out.println("Table " + IDBConstants.TABLE_TIME_SHEET_DETAIL + " has destroyed");
	}

	// monitoring
	public static void createProjectProgressTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_PROGRESS + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_PROGRESS_DATE + " date, " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_COMPLETION + " float, " +
				IDBConstants.ATTR_PREPARED_BY + " INT(38), " +
				IDBConstants.ATTR_APPROVER + " INT(38), " +
				IDBConstants.ATTR_REMARK + " varchar(255), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PREPARED_BY + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_APPROVER + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PROGRESS + " has created");
	}

	public static void deleteProjectProgressTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_PROGRESS );

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_PROGRESS + " has destroyed");
	}

	public static void createProjectNotesTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PROJECT_NOTES + "(" +
				IDBConstants.ATTR_AUTOINDEX + " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
				IDBConstants.ATTR_PROJECT + " INT(38), " +
				IDBConstants.ATTR_NOTES_DATE + " date, " +
				IDBConstants.ATTR_DESCRIPTION + " varchar(255), " +
				IDBConstants.ATTR_ACTION + " varchar(255), " +
				IDBConstants.ATTR_RESPONSIBILITY + " INT(38), " +
				IDBConstants.ATTR_PREPARED_BY + " INT(38), " +
				IDBConstants.ATTR_APPROVER + " INT(38), " +
				IDBConstants.ATTR_REMARK + " varchar(255), " +
				IDBConstants.ATTR_FILE + " varchar(255), " +
				IDBConstants.ATTR_SHEET + " long byte, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PROJECT + ") REFERENCES " +
				IDBConstants.TABLE_PROJECT_DATA + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_RESPONSIBILITY + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_PREPARED_BY + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_APPROVER + ") REFERENCES " +
				pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_NOTES + " has created");
	}

	public static void deleteProjectNotesTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PROJECT_NOTES );

		System.out.println("Table " + IDBConstants.TABLE_PROJECT_NOTES + " has destroyed");
	}
}