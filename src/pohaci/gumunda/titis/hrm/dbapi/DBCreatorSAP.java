package pohaci.gumunda.titis.hrm.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.SQLException;
import java.sql.Statement;

import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Overtime;

public class DBCreatorSAP {
	public static void createOrganizationTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ORGANIZATION
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_NAME + " varchar(255), "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_ORGANIZATION
				+ " has created");
	}

	public static void createEmployeePayrollSubmit(Statement stm)
			throws Exception {
		String query = "CREATE TABLE " + IDBConstants.TABLE_EMPLOYEE_PAYROLL
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_STATUS + " smallint , "
				+ IDBConstants.ATTR_SUBMITTED_DATE + " date, "
				+ IDBConstants.ATTR_UNIT + " INT(38), "
				+ IDBConstants.ATTR_MONTH + " smallint, "
				+ IDBConstants.ATTR_YEAR + " smallint, "
				+ IDBConstants.ATTR_PAYMENT_PERIODE + " smallint, "
				+ IDBConstants.ATTR_PAYROLL_TYPE + " smallint, "
				+ IDBConstants.ATTR_TRANSACTION + " INT(38), "
				+ IDBConstants.ATTR_PAYCHEQUE_TYPE + " smallint "
				/*
				+ "FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES "
				+ IDBConstants.TABLE_UNIT + ", "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_TRANSACTION + ") REFERENCES "
				+ pohaci.gumunda.titis.accounting.dbapi.IDBConstants.TABLE_TRANSACTION */
				+ " )";
		stm.executeUpdate(query);
		//System.err.println(query);

		System.out.println("Table  " + IDBConstants.TABLE_EMPLOYEE_PAYROLL
				+ " has created");
	}

	public static void createEmployeeMealAllowanceAttribute(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_MEAL_ALLOWANCE + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + " INT(38) , "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_PRESENCE + " INT(38)," + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EMPLOYEE + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE_PAYROLL
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE_PAYROLL
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table "
				+ IDBConstants.TABLE_EMPLOYEE_MEAL_ALLOWANCE + " has created");
	}

	public static void createOvertimeAttribute(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_OVERTIMEATTRIBUTE + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + " INT(38) , "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_OVERTIMEMULTIPLIER + " INT(38),"
				+ IDBConstants.ATTR_OVERTIMEVALUE + " float," + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EMPLOYEE + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_OVERTIMEMULTIPLIER
				+ ") REFERENCES " + IDBConstants.TABLE_OVERTIME_MULTIPLIER
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table " + IDBConstants.TABLE_OVERTIMEATTRIBUTE
				+ " has created");
	}

	public static void deleteOvertimeAttribute(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_OVERTIMEATTRIBUTE);

		System.out.println("Table " + IDBConstants.TABLE_OVERTIMEATTRIBUTE
				+ " has destroyed");
	}

	public static void createTransportationAllowanceAttribute(Statement stm)
			throws Exception {
		String query = "CREATE TABLE "
				+ IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + " INT(38) , "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_PRESENCE + " INT(38),"
				+ IDBConstants.ATTR_PRESENCELATE + " INT(38),"
				+ IDBConstants.ATTR_PRESENCENOTLATE + " INT(38),"
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)";
		System.out.println(query);
		stm.executeUpdate(query);

		System.out.println("Table "
				+ IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE + " has created");
	}

	public static void createTaxart21AllowanceAttribute(Statement stm)
			throws Exception {
		String query = "CREATE TABLE " + IDBConstants.TABLE_TAXART21_ALLOWANCE
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL_DETAIL + " INT(38) , "
				+ IDBConstants.ATTR_TAXART21_COMPONENT + " INT(38) , "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE_PAYROLL_DETAIL
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL
				+ " ON DELETE CASCADE," + " FOREIGN KEY ("
				+ IDBConstants.ATTR_TAXART21_COMPONENT + ") REFERENCES "
				+ IDBConstants.TABLE_TAXART21_COMPONENT + " ON DELETE CASCADE)";

		//System.out.println(query);
		stm.executeUpdate(query);

		System.out.println("Table " + IDBConstants.TABLE_TAXART21_ALLOWANCE
				+ " has created");
	}

	public static void deleteTaxart21AllowanceAttribute(Statement stm)
			throws Exception {
		stm
				.executeUpdate("DROP TABLE "
						+ IDBConstants.TABLE_TAXART21_ALLOWANCE);

		System.out.println("Table " + IDBConstants.TABLE_TAXART21_ALLOWANCE
				+ " has destroyed");
	}

	public static void deleteTransportationAllowanceAttribute(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE);

		System.out.println("Table "
				+ IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE
				+ " has destroyed");
	}

	/*
	public static void createOvertime(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE
				+ " INT(38) PRIMARY KEY, "
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + " INT(38) , "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_OVERTIMEMULTIPLIER + " float,"
				+ IDBConstants.ATTR_OVERTIMEVALUE + " float,"
				+"FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE + ") REFERENCES "
				+IDBConstants.TABLE_EMPLOYEE + " ON DELETE CASCADE, "
				+"FOREIGN KEY (" + IDBConstants.ATTR_OVERTIMEMULTIPLIER + ") REFERENCES "
				+IDBConstants.TABLE_OVERTIME_MULTIPLIER+ " ON DELETE CASCADE, "
				+"FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE_PAYROLL + ") REFERENCES " +
				IDBConstants.TABLE_EMPLOYEE_PAYROLL +  " ON DELETE CASCADE)");

		System.out.println("Table " + IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE
				+ " has created");
	}
	 */
	public static void deleteEmployeeMealAllowanceAttribute(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_MEAL_ALLOWANCE);

		System.out
				.println("Table " + IDBConstants.TABLE_EMPLOYEE_MEAL_ALLOWANCE
						+ " has destroyed");
	}

	public static void deleteEmployeePayroll(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EMPLOYEE_PAYROLL);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_PAYROLL
				+ " has destroyed");
	}

	public static void createEmployeePayrollDetail(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE "
			+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL + "("
			+ IDBConstants.ATTR_AUTOINDEX
			+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + " INT(38) , "
			+ IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL + " FIXED(38,2) , "
			+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
			+ IDBConstants.ATTR_JOB_TITLE + " varchar(255),"
			+ IDBConstants.ATTR_PAYROLL_COMPONENT + " INT(38),"
			+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE_PAYROLL
			+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE_PAYROLL
			+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_EMPLOYEE + ") REFERENCES "
			+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, "
			+ "FOREIGN KEY (" + IDBConstants.ATTR_PAYROLL_COMPONENT
			+ ") REFERENCES " + IDBConstants.TABLE_PAYROLL_COMPONENT
			+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL + " has created");
	}

	public static void deleteEmployeePayrollDetail(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL);

		System.out
				.println("Table " + IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL
						+ " has destroyed");
	}

	public static void deleteOrganizationTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ORGANIZATION);

		System.out.println("Table " + IDBConstants.TABLE_ORGANIZATION
				+ " has destroyed");
	}

	public static void createEmpAbsOffWorkTimeTable(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_EMP_ABS_OFFWORKTIME
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEEID + " INT(38), "
				+ IDBConstants.ATTR_DATE + " date, "
				+ IDBConstants.ATTR_ABSSTATUS + " smallint, "
				+ IDBConstants.ATTR_OVERTIME + " float," + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EMPLOYEEID + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + ")) ";
		//                        String sql = "CREATE TABLE "
		//				+ IDBConstants.TABLE_EMP_ABS_OFFWORKTIME + "("
		//				+ IDBConstants.ATTR_AUTOINDEX
		//				+ " INT(38) PRIMARY KEY, "
		//				+ IDBConstants.ATTR_EMPLOYEEID + " INT(38) UNIQUE, "
		//				+ IDBConstants.ATTR_DATE + " date, "
		//				+ IDBConstants.ATTR_ABSSTATUS + " smallint, "
		//				+ IDBConstants.ATTR_OVERTIME + " float," + "FOREIGN KEY ("
		//				+ IDBConstants.ATTR_EMPLOYEEID + ") REFERENCES "
		//				+ IDBConstants.TABLE_EMPLOYEE + ") ";
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_EMP_ABS_OFFWORKTIME
				+ " has created");
	}

	public static void deleteEmpAbsOffWorkTimeTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMP_ABS_OFFWORKTIME);

		System.out.println("Table " + IDBConstants.TABLE_EMP_ABS_OFFWORKTIME
				+ " has destroyed");
	}

	public static void createOrganizationStructureTable(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE "
			+ IDBConstants.TABLE_ORGANIZATION_STRUCTURE + " ("
			+ IDBConstants.ATTR_PARENT_ORG + " INT(38), "
			+ IDBConstants.ATTR_SUB_ORG + " INT(38), " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_PARENT_ORG + ") REFERENCES "
			+ IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, "
			+ "FOREIGN KEY (" + IDBConstants.ATTR_SUB_ORG + ") REFERENCES "
			+ IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_ORGANIZATION_STRUCTURE
				+ " has created");
	}

	public static void deleteOrganizationStructureTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_ORGANIZATION_STRUCTURE);

		System.out.println("Table " + IDBConstants.TABLE_ORGANIZATION_STRUCTURE
				+ " has destroyed");
	}

	public static void createQualificationTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_QUALIFICATION
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_NAME + " varchar(255), "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_QUALIFICATION
				+ " has created");
	}

	public static void deleteQualificationTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_QUALIFICATION);

		System.out.println("Table " + IDBConstants.TABLE_QUALIFICATION
				+ " has destroyed");
	}

	public static void createJobTitleTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_JOB_TITLE + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(40) UNIQUE, "
				+ IDBConstants.ATTR_NAME + " varchar(60), "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(100))");

		System.out.println("Table " + IDBConstants.TABLE_JOB_TITLE
				+ " has created");
	}

	public static void deleteJobTitleTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_JOB_TITLE);

		System.out.println("Table " + IDBConstants.TABLE_JOB_TITLE
				+ " has destroyed");
	}

	public static void createWorkAgreementTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_WORK_AGREEMENT
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_WORK_AGREEMENT
				+ " has created");
	}

	public static void deleteWorkAgreementTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_WORK_AGREEMENT);

		System.out.println("Table " + IDBConstants.TABLE_WORK_AGREEMENT
				+ " has destroyed");
	}

	public static void createEducationTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_EDUCATION + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_EDUCATION
				+ " has created");
	}

	public static void deleteEducationTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EDUCATION);

		System.out.println("Table " + IDBConstants.TABLE_EDUCATION
				+ " has destroyed");
	}

	public static void createReligionTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_RELIGION + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255) UNIQUE)");

		System.out.println("Table " + IDBConstants.TABLE_RELIGION
				+ " has created");
	}

	public static void deleteReligionTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_RELIGION);

		System.out.println("Table " + IDBConstants.TABLE_RELIGION
				+ " has destroyed");
	}

	public static void createSexTypeTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_SEX_TYPE + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_SEX_TYPE
				+ " has created");
	}

	public static void deleteSexTypeTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_SEX_TYPE);

		System.out.println("Table " + IDBConstants.TABLE_SEX_TYPE
				+ " has destroyed");
	}

	public static void createFamilyRelationTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_FAMILY_RELATION
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255) UNIQUE)");

		System.out.println("Table " + IDBConstants.TABLE_FAMILY_RELATION
				+ " has created");
	}

	public static void deleteFamilyRelationTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_FAMILY_RELATION);

		System.out.println("Table " + IDBConstants.TABLE_FAMILY_RELATION
				+ " has destroyed");
	}

	public static void createMaritalStatusTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_MARITAL_STATUS
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255) UNIQUE)");

		System.out.println("Table " + IDBConstants.TABLE_MARITAL_STATUS
				+ " has created");
	}

	public static void deleteMaritalStatusTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_MARITAL_STATUS);

		System.out.println("Table " + IDBConstants.TABLE_MARITAL_STATUS
				+ " has destroyed");
	}

	public static void createAllowenceMultiplierTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_ALLOWENCE_MULTIPLIER + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255), "
				+ IDBConstants.ATTR_MULTIPLIER + " float)");

		System.out.println("Table " + IDBConstants.TABLE_ALLOWENCE_MULTIPLIER
				+ " has created");
	}

	public static void deleteAllowenceMultiplierTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_ALLOWENCE_MULTIPLIER);

		System.out.println("Table " + IDBConstants.TABLE_ALLOWENCE_MULTIPLIER
				+ " has destroyed");
	}

	public static void createPTKPTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PTKP + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_NAME + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255), "
				+ IDBConstants.ATTR_VALUE + " fixed(38,2))");

		System.out.println("Table " + IDBConstants.TABLE_PTKP + " has created");
	}

	public static void deletePTKPTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PTKP);

		System.out.println("Table " + IDBConstants.TABLE_PTKP
				+ " has destroyed");
	}

	public static void createTaxArt21TariffTable(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE "
			+ IDBConstants.TABLE_TAX_ART_21_TARIFF + "("
			+ IDBConstants.ATTR_AUTOINDEX
			+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ IDBConstants.ATTR_MAXIMUM + " fixed(38,2), "
			+ IDBConstants.ATTR_MINIMUM + " fixed(38,2), "
			+ IDBConstants.ATTR_TARIFF + " float)";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_TAX_ART_21_TARIFF
				+ " has created");
	}

	public static void deleteTaxArt21TariffTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TAX_ART_21_TARIFF);

		System.out.println("Table " + IDBConstants.TABLE_TAX_ART_21_TARIFF
				+ " has destroyed");
	}

	public static void createLeaveTypeTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_LEAVE_TYPE + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255), "
				+ IDBConstants.ATTR_DEDUCTION + " boolean)");

		System.out.println("Table " + IDBConstants.TABLE_LEAVE_TYPE
				+ " has created");
	}

	public static void deleteLeaveTypeTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_LEAVE_TYPE);

		System.out.println("Table " + IDBConstants.TABLE_LEAVE_TYPE
				+ " has destroyed");
	}

	public static void createPermitionTypeTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PERMITION_TYPE
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255), "
				+ IDBConstants.ATTR_DAYS + " integer, "
				+ IDBConstants.ATTR_DEDUCTION + " boolean)");

		System.out.println("Table " + IDBConstants.TABLE_PERMITION_TYPE
				+ " has created");
	}

	public static void deletePermitionTypeTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PERMITION_TYPE);

		System.out.println("Table " + IDBConstants.TABLE_PERMITION_TYPE
				+ " has destroyed");
	}

	public static void createOfficeHourPermitionTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_CODE + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_OFFICE_HOUR_PERMITION
				+ " has created");
	}

	public static void deleteOfficeHourPermitionTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION);

		System.out.println("Table " + IDBConstants.TABLE_OFFICE_HOUR_PERMITION
				+ " has destroyed");
	}

	public static void createEmployeeTable(Statement stm) throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_EMPLOYEE + "("
		+ IDBConstants.ATTR_AUTOINDEX
		+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
		+ IDBConstants.ATTR_EMPLOYEE_NO + " varchar(255), "
		+ IDBConstants.ATTR_FIRST_NAME + " varchar(255), "
		+ IDBConstants.ATTR_MIDLE_NAME + " varchar(255), "
		+ IDBConstants.ATTR_LAST_NAME + " varchar(255), "
		+ IDBConstants.ATTR_NICK_NAME + " varchar(255), "
		+ IDBConstants.ATTR_BIRTH_PLACE + " varchar(255), "
		+ IDBConstants.ATTR_BIRTH_DATE + " date, "
		+ IDBConstants.ATTR_SEX + " INT(38), "
		+ IDBConstants.ATTR_RELIGION + " INT(38), "
		+ IDBConstants.ATTR_NATIONALITY + " varchar(255), "
		+ IDBConstants.ATTR_MARITAL + " INT(38), "
		+ IDBConstants.ATTR_ART_21 + " INT(38), "
		+ IDBConstants.ATTR_ADDRESS + " varchar(255), "
		+ IDBConstants.ATTR_CITY + " varchar(255), "
		+ IDBConstants.ATTR_POST_CODE + " integer, "
		+ IDBConstants.ATTR_PROVINCE + " varchar(255), "
		+ IDBConstants.ATTR_COUNTRY + " varchar(255), "
		+ IDBConstants.ATTR_PHONE + " varchar(255), "
		+ IDBConstants.ATTR_MOBILE_PHONE1 + " varchar(255), "
		+ IDBConstants.ATTR_MOBILE_PHONE2 + " varchar(255), "
		+ IDBConstants.ATTR_FAX + " varchar(255), "
		+ IDBConstants.ATTR_EMAIL + " varchar(255), " + "FOREIGN KEY ("
		+ IDBConstants.ATTR_SEX + ") REFERENCES "
		+ IDBConstants.TABLE_SEX_TYPE + "(" + IDBConstants.ATTR_AUTOINDEX + ") , " + "FOREIGN KEY ("
		+ IDBConstants.ATTR_RELIGION + ") REFERENCES "
		+ IDBConstants.TABLE_RELIGION + "(" + IDBConstants.ATTR_AUTOINDEX + ") , " + "FOREIGN KEY ("
		+ IDBConstants.ATTR_MARITAL + ") REFERENCES "
		+ IDBConstants.TABLE_MARITAL_STATUS + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
		+ IDBConstants.ATTR_ART_21 + ") REFERENCES "
		+ IDBConstants.TABLE_PTKP + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE
				+ " has created");
	}

	public static void deleteEmployeeTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EMPLOYEE);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE
				+ " has destroyed");
	}

	public static void createEmployeeQualificationTable(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE "
			+ IDBConstants.TABLE_EMPLOYEE_QUALIFICATION + "("
			+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
			+ IDBConstants.ATTR_QUALIFICATION + " INT(38), "
			+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
			+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
			+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_QUALIFICATION + ") REFERENCES "
			+ IDBConstants.TABLE_QUALIFICATION + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_QUALIFICATION
				+ " has created");
	}

	public static void deleteEmployeeQualificationTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_QUALIFICATION);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_QUALIFICATION
				+ " has destroyed");
	}

	public static void createEmployeeEmploymentTable(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE "
			+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + "("
			+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
			+ IDBConstants.ATTR_JOB_TITLE + " INT(38), "
			+ IDBConstants.ATTR_DEPARTMENT + " INT(38), "
			+ IDBConstants.ATTR_UNIT + " INT(38), "
			+ IDBConstants.ATTR_REFERENCE_NO + " varchar(255), "
			+ IDBConstants.ATTR_REFERENCE_DATE + " date, "
			+ IDBConstants.ATTR_TMT + " date, "
			+ IDBConstants.ATTR_END_DATE + " date, "
			+ IDBConstants.ATTR_WORK_AGREEMENT + " INT(38), "
			+ IDBConstants.ATTR_DESCRIPTION + " varchar(255), "
			+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
			+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
			+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_JOB_TITLE + ") REFERENCES "
			+ IDBConstants.TABLE_JOB_TITLE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_DEPARTMENT + ") REFERENCES "
			+ IDBConstants.TABLE_ORGANIZATION + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_UNIT + ") REFERENCES "
			+ pohaci.gumunda.titis.accounting.dbapi.IDBConstants.TABLE_UNIT
			+ "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY (" + IDBConstants.ATTR_WORK_AGREEMENT
			+ ") REFERENCES " + IDBConstants.TABLE_WORK_AGREEMENT + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);		

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT
				+ " has created");
	}

	public static void deleteEmployeeEmploymentTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT
				+ " has destroyed");
	}

	public static void createEmployeeEducationTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_EDUCATION + "("
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_GRADE + " INT(38), "
				+ IDBConstants.ATTR_MAJOR_STUDY + " varchar(255), "
				+ IDBConstants.ATTR_INSTITUE + " varchar(255), "
				+ IDBConstants.ATTR_FROM + " date, " + IDBConstants.ATTR_TO
				+ " date, " + IDBConstants.ATTR_GPA + " float, "
				+ IDBConstants.ATTR_MAX_GPA + " float, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255), "
				+ IDBConstants.ATTR_FILE + " varchar(255), "
				+ IDBConstants.ATTR_CERTIFICATE + " long byte, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY("
				+ IDBConstants.ATTR_GRADE + ") REFERENCES "
				+ IDBConstants.TABLE_EDUCATION + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_EDUCATION
				+ " has created");
	}

	public static void deleteEmployeeEducationTable(Statement stm)
			throws Exception {
		stm
				.executeUpdate("DROP TABLE "
						+ IDBConstants.TABLE_EMPLOYEE_EDUCATION);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_EDUCATION
				+ " has destroyed");
	}

	public static void createEmployeeCertificationTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_CERTIFICATION + "("
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_CERTIFICATE_NO + " varchar(255), "
				+ IDBConstants.ATTR_CERTIFICATE_DATE + " date, "
				+ IDBConstants.ATTR_INSTITUE + " varchar(255), "
				+ IDBConstants.ATTR_QUALIFICATION + " INT(38), "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255), "
				+ IDBConstants.ATTR_START_DATE + " date, "
				+ IDBConstants.ATTR_END_DATE + " date, "
				+ IDBConstants.ATTR_EXPIRE_DATE + " date, "
				+ IDBConstants.ATTR_RESULT + " varchar(255), "
				+ IDBConstants.ATTR_FILE + " varchar(255), "
				+ IDBConstants.ATTR_CERTIFICATE + " long byte, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_QUALIFICATION + ") REFERENCES "
				+ IDBConstants.TABLE_QUALIFICATION + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_CERTIFICATION
				+ " has created");
	}

	public static void deleteEmployeeCertificationTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_CERTIFICATION);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_CERTIFICATION
				+ " has destroyed");
	}

	public static void createEmployeeFamilyTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_EMPLOYEE_FAMILY
				+ "(" + IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_RELATION + " INT(38), "
				+ IDBConstants.ATTR_NAME + " varchar(255), "
				+ IDBConstants.ATTR_BIRTH_PLACE + " varchar(255), "
				+ IDBConstants.ATTR_BIRTH_DATE + " date, "
				+ IDBConstants.ATTR_EDUCATION + " INT(38), "
				+ IDBConstants.ATTR_JOB_TITLE + " varchar(255), "
				+ IDBConstants.ATTR_COMPANY + " varchar(255), "
				+ IDBConstants.ATTR_REMARK + " varchar(255), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_RELATION + ") REFERENCES "
				+ IDBConstants.TABLE_FAMILY_RELATION + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EDUCATION + ") REFERENCES "
				+ IDBConstants.TABLE_EDUCATION + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_FAMILY
				+ " has created");
	}

	public static void deleteEmployeeFamilyTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EMPLOYEE_FAMILY);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_FAMILY
				+ " has destroyed");
	}

	public static void createEmployeeAccountTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_EMPLOYEE_ACCOUNT
				+ "(" + IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_ACCOUNT_NAME + " varchar(255), "
				+ IDBConstants.ATTR_ACCOUNT_NO + " varchar(255), "
				+ IDBConstants.ATTR_BANK_NAME + " varchar(255), "
				+ IDBConstants.ATTR_BANK_ADDRESS + " varchar(255), "
				+ IDBConstants.ATTR_CURRENCY + " INT(38), "
				+ IDBConstants.ATTR_REMARK + " varchar(255), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_CURRENCY + ") REFERENCES "
				+ pohaci.gumunda.cross.dbapi.IDBConstants.TABLE_CURRENCY + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_ACCOUNT
				+ " has created");
	}

	public static void deleteEmployeeAccountTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EMPLOYEE_ACCOUNT);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_ACCOUNT
				+ " has destroyed");
	}

	public static void createEmployeeRetirementTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_RETIREMENT + "("
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_RETIREMENT_REFERENCE + " varchar(255), "
				+ IDBConstants.ATTR_RETIREMENT_DATE + " date, "
				+ IDBConstants.ATTR_REASON + " varchar(255), "
				+ IDBConstants.ATTR_REMARK + " varchar(255), "
				+ IDBConstants.ATTR_TMT + " date, "
				+ IDBConstants.ATTR_RETIREMENT_STATUS + " smallint, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_RETIREMENT
				+ " has created");
	}

	public static void deleteEmployeeRetirementTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_RETIREMENT);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_RETIREMENT
				+ " has destroyed");
	}

	public static void createDefaultWorkingDayTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_DEFAULT_WORKING_DAY + "("
				+ IDBConstants.ATTR_TYPE + " smallint, "
				+ IDBConstants.ATTR_MONDAY + " boolean, "
				+ IDBConstants.ATTR_TUESDAY + " boolean, "
				+ IDBConstants.ATTR_WEDNESDAY + " boolean, "
				+ IDBConstants.ATTR_THURSDAY + " boolean, "
				+ IDBConstants.ATTR_FRIDAY + " boolean, "
				+ IDBConstants.ATTR_SATURDAY + " boolean, "
				+ IDBConstants.ATTR_SUNDAY + " boolean)");

		System.out.println("Table " + IDBConstants.TABLE_DEFAULT_WORKING_DAY
				+ " has created");
	}

	public static void deleteDefaultWorkingDayTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_DEFAULT_WORKING_DAY);

		System.out.println("Table " + IDBConstants.TABLE_DEFAULT_WORKING_DAY
				+ " has destroyed");
	}

	public static void createDefaultWorkingTimeTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_DEFAULT_WORKING_TIME + "("
				+ IDBConstants.ATTR_TYPE + " smallint, "
				+ IDBConstants.ATTR_FROM + " time, " + IDBConstants.ATTR_TO
				+ " time)");

		System.out.println("Table " + IDBConstants.TABLE_DEFAULT_WORKING_TIME
				+ " has created");
	}

	public static void deleteDefaultWorkingTimeTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_DEFAULT_WORKING_TIME);

		System.out.println("Table " + IDBConstants.TABLE_DEFAULT_WORKING_TIME
				+ " has destroyed");
	}

	public static void createHolidayTable(Statement stm) throws SQLException {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_HOLIDAY + "("
				+ IDBConstants.ATTR_HOLIDAY_DATE + " date, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_HOLIDAY
				+ " has created");
	}

	public static void deleteHolidayTable(Statement stm) throws SQLException {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_HOLIDAY);

		System.out.println("Table " + IDBConstants.TABLE_HOLIDAY
				+ " has destroyed");
	}

	public static void createAnnualLeaveRightTable(Statement stm)
			throws SQLException {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_VALID_FROM + " smallint, "
				+ IDBConstants.ATTR_VALID_THRU + " smallint, "
				+ IDBConstants.ATTR_MIN_YEAR + " smallint, "
				+ IDBConstants.ATTR_MAX_YEAR + " smallint, "
				+ IDBConstants.ATTR_MIN_RIGHT + " smallint, "
				+ IDBConstants.ATTR_MAX_RIGHT + " smallint)");

		System.out.println("Table " + IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT
				+ " has created");
	}

	public static void deleteAnnualLeaveRightTable(Statement stm)
			throws SQLException {
		stm
				.executeUpdate("DROP TABLE "
						+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT);

		System.out.println("Table " + IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT
				+ " has destroyed");
	}

	public static void createOvertimeMultiplierTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_OVERTIME_MULTIPLIER + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_TYPE + " smallint, "
				+ IDBConstants.ATTR_HOUR_MIN + " integer, "
				+ IDBConstants.ATTR_HOUR_MAX + " integer, "
				+ IDBConstants.ATTR_MULTIPLIER + " float)");

		System.out.println("Table " + IDBConstants.TABLE_OVERTIME_MULTIPLIER
				+ " has created");
	}

	public static void deleteOvertimeMultiplierTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_OVERTIME_MULTIPLIER);

		System.out.println("Table " + IDBConstants.TABLE_OVERTIME_MULTIPLIER
				+ " has destroyed");
	}

	public static void createPaychequeLabelTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYCHEQUE_LABEL
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_LABEL + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_TYPE + " smallint, "
				+ IDBConstants.ATTR_SHOW + " boolean)");

		System.out.println("Table " + IDBConstants.TABLE_PAYCHEQUE_LABEL
				+ " has created");
	}

	public static void deletePaychequeLabelTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYCHEQUE_LABEL);

		System.out.println("Table " + IDBConstants.TABLE_PAYCHEQUE_LABEL
				+ " has destroyed");
	}

	public static void createPaychequeLabelStructureTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE + "("
				+ IDBConstants.ATTR_SUPER_LABEL + " INT(38), "
				+ IDBConstants.ATTR_SUB_LABEL + " INT(38))");
		System.out
				.println("Table "
						+ IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE
						+ " has created");
	}

	public static void deletePaychequeLabelStructureTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE
				+ " has destroyed");
	}

	public static void createNonPaychequePeriodeTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_NON_PAYCHEQUE_PERIOD + "("
				+ IDBConstants.ATTR_PERIOD_FROM1 + " smallint, "
				+ IDBConstants.ATTR_PERIOD_FROM2 + " smallint, "
				+ IDBConstants.ATTR_PERIOD_TO1 + " smallint, "
				+ IDBConstants.ATTR_PERIOD_TO2 + " smallint)");

		System.out.println("Table " + IDBConstants.TABLE_NON_PAYCHEQUE_PERIOD
				+ " has created");
	}

	public static void deleteNonPaychequePeriodeTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_NON_PAYCHEQUE_PERIOD);

		System.out.println("Table " + IDBConstants.TABLE_NON_PAYCHEQUE_PERIOD
				+ " has destroyed");
	}

	public static void createEmployeeOfficePermitionTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38),"
				+ IDBConstants.ATTR_PROPOSE_DATE + " date, "
				+ IDBConstants.ATTR_PERMISSION_DATE + " date, "
				+ IDBConstants.ATTR_FROM + " time, " + IDBConstants.ATTR_TO
				+ " time, " + IDBConstants.ATTR_REASON + " INT(38), "
				+ IDBConstants.ATTR_CHECKED + " INT(38), "
				+ IDBConstants.ATTR_CHECKED_DATE + " date, "
				+ IDBConstants.ATTR_APPROVED + " INT(38), "
				+ IDBConstants.ATTR_APPROVED_DATE + " date, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(1024), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_REASON + ") REFERENCES "
				+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_CHECKED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_CHECKED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out
				.println("Table "
						+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION
						+ " has created");
	}

	public static void deleteEmployeeOfficePermitionTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION);

		System.out.println("Table "
				+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION
				+ " has destroyed");
	}

	public static void createEmployeeLeaveTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_EMPLOYEE_LEAVE
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38),"
				+ IDBConstants.ATTR_PROPOSE_DATE + " date, "
				+ IDBConstants.ATTR_FROM + " date, " + IDBConstants.ATTR_TO
				+ " date, " + IDBConstants.ATTR_DAYS + " integer, "
				+ IDBConstants.ATTR_REASON + " INT(38), "
				+ IDBConstants.ATTR_ADDRESS + " varchar(255), "
				+ IDBConstants.ATTR_PHONE + " varchar(255), "
				+ IDBConstants.ATTR_REPLACED + " INT(38), "
				+ IDBConstants.ATTR_CHECKED + " INT(38), "
				+ IDBConstants.ATTR_CHECKED_DATE + " date, "
				+ IDBConstants.ATTR_APPROVED + " INT(38), "
				+ IDBConstants.ATTR_APPROVED_DATE + " date, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(1024), "
				+ IDBConstants.ATTR_IS_REFERENCE + " boolean, "
				+ IDBConstants.ATTR_FILE + " varchar(255), "
				+ IDBConstants.ATTR_REFERENCE + " long byte, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_REASON + ") REFERENCES "
				+ IDBConstants.TABLE_LEAVE_TYPE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_REPLACED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_CHECKED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_APPROVED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_LEAVE
				+ " has created");
	}

	public static void deleteEmployeeLeaveTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_EMPLOYEE_LEAVE);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_LEAVE
				+ " has destroyed");
	}

	public static void createEmployeePermitionTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_PERMITION + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38),"
				+ IDBConstants.ATTR_PROPOSE_DATE + " date, "
				+ IDBConstants.ATTR_FROM + " date, " + IDBConstants.ATTR_TO
				+ " date, " + IDBConstants.ATTR_DAYS + " integer, "
				+ IDBConstants.ATTR_REASON + " INT(38), "
				+ IDBConstants.ATTR_ADDRESS + " varchar(255), "
				+ IDBConstants.ATTR_PHONE + " varchar(255), "
				+ IDBConstants.ATTR_REPLACED + " INT(38), "
				+ IDBConstants.ATTR_CHECKED + " INT(38), "
				+ IDBConstants.ATTR_CHECKED_DATE + " date, "
				+ IDBConstants.ATTR_APPROVED + " INT(38), "
				+ IDBConstants.ATTR_APPROVED_DATE + " date, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(1024), "
				+ IDBConstants.ATTR_IS_REFERENCE + " boolean, "
				+ IDBConstants.ATTR_FILE + " varchar(255), "
				+ IDBConstants.ATTR_REFERENCE + " long byte, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_REASON + ") REFERENCES "
				+ IDBConstants.TABLE_PERMITION_TYPE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_REPLACED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_CHECKED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_APPROVED + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_PERMITION
				+ " has created");
	}

	public static void deleteEmployeePermitionTable(Statement stm)
			throws Exception {
		stm
				.executeUpdate("DROP TABLE "
						+ IDBConstants.TABLE_EMPLOYEE_PERMITION);

		System.out.println("Table " + IDBConstants.TABLE_EMPLOYEE_PERMITION
				+ " has destroyed");
	}

	public static void createPayrollComponentTable(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT
			+ "("
			+ IDBConstants.ATTR_AUTOINDEX
			+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ IDBConstants.ATTR_CODE
			+ " varchar(255), "
			+ IDBConstants.ATTR_DESCRIPTION
			+ " varchar(255), "
			+ IDBConstants.ATTR_ISGROUP
			+ " boolean, "
			+ IDBConstants.ATTR_ACCOUNT
			+ " INT(38), "
			+ IDBConstants.ATTR_PAYMENT
			+ " smallint, "
			+ IDBConstants.ATTR_SUBMIT
			+ " smallint, "
			+ IDBConstants.ATTR_PAYCHEQUE_LABEL
			+ " INT(38), "
			+ IDBConstants.ATTR_REPORT_LABEL
			+ " varchar(255), "
			+ "FOREIGN KEY ("
			+ IDBConstants.ATTR_ACCOUNT
			+ ") REFERENCES "
			+ pohaci.gumunda.titis.accounting.dbapi.IDBConstants.TABLE_ACCOUNT
			+ "(" + IDBConstants.ATTR_AUTOINDEX + "), " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_PAYCHEQUE_LABEL + ") REFERENCES "
			+ IDBConstants.TABLE_PAYCHEQUE_LABEL + "(" + IDBConstants.ATTR_AUTOINDEX + "))";
		System.out.println(sql);
		stm.executeUpdate(sql);
		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_COMPONENT
				+ " has created");
	}

	public static void createTaxArt21ComponentTable(Statement stm)
			throws Exception {
		stm
				.executeUpdate("CREATE TABLE "
						+ IDBConstants.TABLE_TAXART21_COMPONENT
						+ " ("
						+ IDBConstants.ATTR_AUTOINDEX
						+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
						+ IDBConstants.ATTR_CODE
						+ " varchar(255), "
						+ IDBConstants.ATTR_DESCRIPTION
						+ " varchar(255), "
						+ IDBConstants.ATTR_ISGROUP
						+ " boolean, "
						+ IDBConstants.ATTR_TAXACCOUNT
						+ " INT(38), "
						+ IDBConstants.ATTR_FORMULA
						+ " varchar(255), "
						+ IDBConstants.ATTR_NOTE
						+ " varchar(255), "
						+ IDBConstants.ATTR_ISROUNDED
						+ " boolean, "
						+ IDBConstants.ATTR_ROUND_VALUE
						+ " smallint, "
						+ IDBConstants.ATTR_PRECISION
						+ " smallint, "
						+ IDBConstants.ATTR_ISNEGATIVE
						+ " boolean, "
						+ IDBConstants.ATTR_ISCOMPARABLE
						+ " boolean, "
						+ IDBConstants.ATTR_COMPARATION_MODE
						+ " varchar(255), "
						+ IDBConstants.ATTR_COMPARATOR
						+ " FIXED(38, 2), "
						+ "FOREIGN KEY ("
						+ IDBConstants.ATTR_TAXACCOUNT
						+ ") REFERENCES "
						+ pohaci.gumunda.titis.accounting.dbapi.IDBConstants.TABLE_ACCOUNT
						+ "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table " + IDBConstants.TABLE_TAXART21_COMPONENT
				+ " has created");
	}

	public static void createTaxArt21Payroll(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_TAXART21_PAYROLL
				+ " (" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_TAXART21_COMPONENT + " INT(38), "
				+ IDBConstants.ATTR_PAYROLL_COMPONENT + " INT(38), " +

				"FOREIGN KEY (" + IDBConstants.ATTR_TAXART21_COMPONENT
				+ ") REFERENCES " + IDBConstants.TABLE_TAXART21_COMPONENT
				+ " ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_PAYROLL_COMPONENT + ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT + ")");

		System.out.println("Table " + IDBConstants.TABLE_TAXART21_COMPONENT
				+ " has created");
	}

	public static void deletePayrollComponentTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_COMPONENT);

		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_COMPONENT
				+ " has destroyed");
	}

	public static void deleteTaxArt21ComponentTable(Statement stm)
			throws Exception {
		stm
				.executeUpdate("DROP TABLE "
						+ IDBConstants.TABLE_TAXART21_COMPONENT);

		System.out.println("Table " + IDBConstants.TABLE_TAXART21_COMPONENT
				+ " has destroyed");
	}

	public static void deleteTaxArt21Payroll(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TAXART21_PAYROLL);

		System.out.println("Table " + IDBConstants.TABLE_TAXART21_COMPONENT
				+ " has destroyed");
	}

	public static void createTaxArt21ComponentStructureTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE " + "taxart21componentstructure"
						+ "(" +
						IDBConstants.ATTR_SUPER_TAXART21COMPONENT
						+ " INT(38), "
						+ IDBConstants.ATTR_SUB_TAXART21COMPONENT
						+ " INT(38), " + "FOREIGN KEY ("
						+ IDBConstants.ATTR_SUPER_TAXART21COMPONENT
						+ ") REFERENCES "
						+ IDBConstants.TABLE_TAXART21_COMPONENT
						+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
						+ IDBConstants.ATTR_SUB_TAXART21COMPONENT
						+ ") REFERENCES "
						+ IDBConstants.TABLE_TAXART21_COMPONENT
						+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE
				+ " has created");
	}

	public static void createPayrollComponentStructureTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE + "("
				+ IDBConstants.ATTR_SUPER_PAYROLL + " INT(38), "
				+ IDBConstants.ATTR_SUB_PAYROLL + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_SUPER_PAYROLL
				+ ") REFERENCES " + IDBConstants.TABLE_PAYROLL_COMPONENT
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_SUB_PAYROLL + ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE
				+ " has created");
	}

	public static void deletePayrollComponentStructureTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE
				+ " has destroyed");
	}

	public static void deleteTaxart21ComponentStructureTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TAXART21_COMPONENT_STRUCTURE);

		System.out.println("Table "
				+ IDBConstants.TABLE_TAXART21_COMPONENT_STRUCTURE
				+ " has destroyed");
	}

	public static void createPayrollCategoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_PAYROLL_CATEGORY
				+ "(" + IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_NAME + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(1024))");

		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_CATEGORY
				+ " has created");
	}

	public static void deletePayrollCategoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_PAYROLL_CATEGORY);

		System.out.println("Table " + IDBConstants.TABLE_PAYROLL_CATEGORY
				+ " has destroyed");
	}

	public static void createPayrollCategoryComponentTable(Statement stm)
			throws Exception {
		String sql = "CREATE TABLE "
			+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT + "("
			+ IDBConstants.ATTR_AUTOINDEX
			+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
			+ IDBConstants.ATTR_CATEGORY + " INT(38), "
			+ IDBConstants.ATTR_COMPONENT + " INT(38), "
			+ IDBConstants.ATTR_FORMULA + " varchar(255), "
			+ IDBConstants.ATTR_FORMULA_MONTH + " smallint, "
			+ IDBConstants.ATTR_ROUND_VALUE + " smallint, "
			+ IDBConstants.ATTR_PRECISION + " INT(38), "
			+ "FOREIGN KEY (" + IDBConstants.ATTR_CATEGORY
			+ ") REFERENCES " + IDBConstants.TABLE_PAYROLL_CATEGORY
			+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
			+ IDBConstants.ATTR_COMPONENT + ") REFERENCES "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)";
		System.out.println(sql);
		stm.executeUpdate(sql);
		// i add field month
		System.out.println("Table "+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT + " has created");
	}

	public static void deletePayrollCategoryComponentTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT
				+ " has destroyed");
	}

	public static void createPayrollCategoryEmployeeTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE + "("
				+ IDBConstants.ATTR_CATEGORY + " INT(38), "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_CATEGORY + ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out
				.println("Table "
						+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE
						+ " has created");
	}

	public static void deletePayrollCategoryEmployeeTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE
				+ " has destroyed");
	}

	public static void createEmployeePayrollComponentTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_COMPONENT + " INT(38), "
				+ IDBConstants.ATTR_FORMULA + " varchar(255), "
				+ IDBConstants.ATTR_FORMULA_MONTH + " smallint, "
				+ IDBConstants.ATTR_ROUND_VALUE + " smallint, "
				+ IDBConstants.ATTR_PRECISION + " fixed(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_COMPONENT + ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT
				+ " has created");
	}

	public static void deleteEmployeePayrollComponentTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT);

		System.out.println("Table "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT
				+ " has destroyed");
	}

	public static void createTaxArt21Submit(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_STATUS + " smallint, "
				+ IDBConstants.ATTR_TRANSACTION + " INT(38), "
				+ IDBConstants.ATTR_SUBMITTED_DATE + " date, "
				+ IDBConstants.ATTR_MONTH_SUBMITTED + " smallint, "
				+ IDBConstants.ATTR_YEAR_SUBMITTED + " smallint, "
				+ IDBConstants.ATTR_UNIT + " INT(38), "
				+ IDBConstants.ATTR_TAXACCOUNT + " INT(38) " +
				/*"FOREIGN KEY (" + IDBConstants.ATTR_UNIT + ") REFERENCES " +
				IDBConstants.TABLE_UNIT + ", " +
				"FOREIGN KEY (" + IDBConstants.ATTR_TAXACCOUNT + ") REFERENCES " +
				pohaci.gumunda.titis.accounting.dbapi.IDBConstants.ATTR_ACCOUNT + */
				")");

		System.out.println("Table " + IDBConstants.TABLE_TAX_ART_21_SUBMIT
				+ " has created");
	}

	public static void deleteTaxArt21Submit(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_TAX_ART_21_SUBMIT);

		System.out.println("Table " + IDBConstants.TABLE_TAX_ART_21_SUBMIT
				+ " has destroyed");
	}

	public static void createTaxArt21SubmitEmployeeDetail(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_TAX_ART_21_SUBMIT + " INT(38), "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_JOB_TITLE + " varchar(255), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_TAX_ART_21_SUBMIT
				+ ") REFERENCES " + IDBConstants.TABLE_TAX_ART_21_SUBMIT
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EMPLOYEE + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL
				+ " has created");
	}

	public static void deleteTaxArt21SubmitEmployeeDetail(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL);

		System.out.println("Table "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL
				+ " has destroyed");
	}

	public static void createTaxArt21SubmitComponentDetail(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_COMP_DETAIL + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_TAX_ART_21_SUBMIT_EMP_DETAIL
				+ " INT(38), " + IDBConstants.ATTR_TAXART21_COMPONENT
				+ " INT(38), " + IDBConstants.ATTR_VALUE + " FIXED(38,2), "
				+ "FOREIGN KEY ("
				+ IDBConstants.ATTR_TAX_ART_21_SUBMIT_EMP_DETAIL
				+ ") REFERENCES "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL
				+ "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_TAXART21_COMPONENT + ") REFERENCES "
				+ IDBConstants.TABLE_TAXART21_COMPONENT + "(" + IDBConstants.ATTR_AUTOINDEX + "))");

		System.out.println("Table "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_COMP_DETAIL
				+ " has created");
	}

	public static void deleteTaxArt21SubmitComponentDetail(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_COMP_DETAIL);

		System.out.println("Table "
				+ IDBConstants.TABLE_TAX_ART_21_SUBMIT_COMP_DETAIL
				+ " has destroyed");
	}

	public static void createOvertime(Statement stm) throws Exception {
		GenericMapper mapper = MasterMap.obtainMapperFor(Overtime.class);
		mapper.setActiveConn(stm.getConnection());
		mapper.ddlCreate();
	}

	public static void deleteOvertime(Statement stm) throws Exception {
		GenericMapper mapper = MasterMap.obtainMapperFor(Overtime.class);
		mapper.setActiveConn(stm.getConnection());
		mapper.ddlDrop();
	}

	public static void createPayrollCategoryBackup(Statement stm)
			throws Exception {
		createPayrollCategoryHistoryMasterTable(stm);
		createPayrollCategoryHistoryTable(stm);
		createPayrollCategoryComponentHistoryTable(stm);
		createPayrollCategoryEmployeeHistoryTable(stm);
		createEmployeePayrollComponentHistoryTable(stm);
	}

	public static void deletePayrollCategoryBackup(Statement stm)
			throws Exception {
		deletePayrollCategoryHistoryMasterTable(stm);
		deletePayrollCategoryHistoryTable(stm);
		deletePayrollCategoryComponentHistoryTable(stm);
		deletePayrollCategoryEmployeeHistoryTable(stm);
		deleteEmployeePayrollComponentHistoryTable(stm);
	}

	public static void createPayrollCategoryHistoryMasterTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER + " ("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_MONTH + " smallint, "
				+ IDBConstants.ATTR_YEAR + " smallint, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(1024), "
				+ IDBConstants.ATTR_DATE + " date)");

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER
				+ " has created");
	}

	public static void deletePayrollCategoryHistoryMasterTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER
				+ " has destroyed");
	}

	public static void createPayrollCategoryHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_HISTORY_MASTER + " INT(38), "
				+ IDBConstants.ATTR_NAME + " varchar(255), "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(1024), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_HISTORY_MASTER
				+ ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER
				+ " ON DELETE CASCADE)");

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY + " has created");
	}

	public static void deletePayrollCategoryHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY
				+ " has destroyed");
	}

	public static void createPayrollCategoryComponentHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT_HISTORY + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_HISTORY_MASTER + " INT(38), "
				+ IDBConstants.ATTR_CATEGORY + " INT(38), "
				+ IDBConstants.ATTR_COMPONENT + " INT(38), "
				+ IDBConstants.ATTR_FORMULA + " varchar(255), "
				+ IDBConstants.ATTR_FORMULA_MONTH + " smallint, "
				+ IDBConstants.ATTR_ROUND_VALUE + " smallint, "
				+ IDBConstants.ATTR_PRECISION + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_HISTORY_MASTER
				+ ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER
				+ " ON DELETE CASCADE, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_CATEGORY
				+ ") REFERENCES " + IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY
				+ " ON DELETE CASCADE, "
				+ "FOREIGN KEY ("
				+ IDBConstants.ATTR_COMPONENT + ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT + " ON DELETE CASCADE)");

		// i add field month

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT_HISTORY
				+ " has created");
	}

	public static void deletePayrollCategoryComponentHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT_HISTORY);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT_HISTORY
				+ " has destroyed");
	}

	public static void createPayrollCategoryEmployeeHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE_HISTORY + "("
				+ IDBConstants.ATTR_HISTORY_MASTER + " INT(38), "
				+ IDBConstants.ATTR_CATEGORY + " INT(38), "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_HISTORY_MASTER
				+ ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER
				+ " ON DELETE CASCADE, "
				+ "FOREIGN KEY ("
				+ IDBConstants.ATTR_CATEGORY + ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY
				+ " ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_EMPLOYEE + ") REFERENCES "
				+ IDBConstants.TABLE_EMPLOYEE + " ON DELETE CASCADE)");

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE_HISTORY
				+ " has created");
	}

	public static void deletePayrollCategoryEmployeeHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE_HISTORY);

		System.out.println("Table "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE_HISTORY
				+ " has destroyed");
	}

	public static void createEmployeePayrollComponentHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("CREATE TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT_HISTORY + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_HISTORY_MASTER + " INT(38), "
				+ IDBConstants.ATTR_EMPLOYEE + " INT(38), "
				+ IDBConstants.ATTR_COMPONENT + " FIXED(38), "
				+ IDBConstants.ATTR_FORMULA + " varchar(255), "
				+ IDBConstants.ATTR_FORMULA_MONTH + " smallint, "
				+ IDBConstants.ATTR_ROUND_VALUE + " smallint, "
				+ IDBConstants.ATTR_PRECISION + " FIXED(38), "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_HISTORY_MASTER
				+ ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER
				+ " ON DELETE CASCADE, "
				+ "FOREIGN KEY (" + IDBConstants.ATTR_EMPLOYEE
				+ ") REFERENCES " + IDBConstants.TABLE_EMPLOYEE
				+ " ON DELETE CASCADE, " + "FOREIGN KEY ("
				+ IDBConstants.ATTR_COMPONENT + ") REFERENCES "
				+ IDBConstants.TABLE_PAYROLL_COMPONENT + " ON DELETE CASCADE)");

		System.out.println("Table "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT_HISTORY
				+ " has created");
	}

	public static void deleteEmployeePayrollComponentHistoryTable(Statement stm)
			throws Exception {
		stm.executeUpdate("DROP TABLE "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT_HISTORY);

		System.out.println("Table "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT_HISTORY
				+ " has destroyed");
	}
}