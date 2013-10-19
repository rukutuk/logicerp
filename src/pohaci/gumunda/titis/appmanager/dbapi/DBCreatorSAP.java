/**
 * 
 */
package pohaci.gumunda.titis.appmanager.dbapi;

import java.sql.Statement;

import pohaci.gumunda.titis.appmanager.entity.IDBConstants;

/**
 * @author dark-knight
 *
 */
public class DBCreatorSAP {

	/**
	 * @param args
	 */
	public static void createUserDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_USER_DETAIL + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_USERNAME + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_FULLNAME + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_USER_DETAIL
				+ " has created");
	}

	public static void deleteUserDetailTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_USER_DETAIL);

		System.out.println("Table " + IDBConstants.TABLE_USER_DETAIL
				+ " has dropped");
	}

	public static void createRoleTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ROLE + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_ROLENAME + " varchar(255) UNIQUE, "
				+ IDBConstants.ATTR_DESCRIPTION + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_ROLE
				+ " has created");
	}

	public static void deleteRoleTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ROLE);

		System.out.println("Table " + IDBConstants.TABLE_ROLE
				+ " has dropped");
	}
	
	public static void createFunctionTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_FUNCTION + "("
				+ IDBConstants.ATTR_AUTOINDEX
				+ " INT(38) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
				+ IDBConstants.ATTR_APPLICATION + " varchar(255), " 
				+ IDBConstants.ATTR_FUNCTIONNAME + " varchar(255), "
				+ IDBConstants.ATTR_TREE_PATH + " varchar(255))");

		System.out.println("Table " + IDBConstants.TABLE_FUNCTION
				+ " has created");
	}

	public static void deleteFunctionTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_FUNCTION);

		System.out.println("Table " + IDBConstants.TABLE_FUNCTION
				+ " has dropped");
	}
	
	public static void createFunctionStructureTable(Statement stm) throws Exception {
		String sql = "CREATE TABLE " + IDBConstants.TABLE_FUNCTION_STRUCTURE + "(" + 
				IDBConstants.ATTR_SUPER_FUNCTION + " INT(11), " +
				IDBConstants.ATTR_SUB_FUNCTION + " INT(11), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUPER_FUNCTION + ") REFERENCES " +
				IDBConstants.TABLE_FUNCTION + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_SUB_FUNCTION + ") REFERENCES " +
				IDBConstants.TABLE_FUNCTION + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )";
		System.out.println(sql);
		stm.executeUpdate(sql);

		System.out.println("Table " + IDBConstants.TABLE_FUNCTION_STRUCTURE
				+ " has created");
	}

	public static void deleteFunctionStructureTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_FUNCTION_STRUCTURE);

		System.out.println("Table " + IDBConstants.TABLE_FUNCTION_STRUCTURE
				+ " has dropped");
	}
	
	public static void createRoleMappingTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_ROLE_MAPPING + "(" + 
				IDBConstants.ATTR_ROLE + " INT(11), " +
				IDBConstants.ATTR_FUNCTION + " INT(11), " +
				IDBConstants.ATTR_APPLICATION + " varchar(255), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ROLE + ") REFERENCES " +
				IDBConstants.TABLE_ROLE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE, " +
				"FOREIGN KEY (" + IDBConstants.ATTR_FUNCTION + ") REFERENCES " +
				IDBConstants.TABLE_FUNCTION + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE )");

		System.out.println("Table " + IDBConstants.TABLE_ROLE_MAPPING
				+ " has created");
	}

	public static void deleteRoleMappingTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_ROLE_MAPPING);

		System.out.println("Table " + IDBConstants.TABLE_ROLE_MAPPING
				+ " has dropped");
	}
	
	public static void createUserRoleTable(Statement stm) throws Exception {
		stm.executeUpdate("CREATE TABLE " + IDBConstants.TABLE_USER_ROLE + "(" + 
				IDBConstants.ATTR_USERNAME + " varchar(255), " +
				IDBConstants.ATTR_ROLE + " INT(11), " +
				"FOREIGN KEY (" + IDBConstants.ATTR_ROLE + ") REFERENCES " +
				IDBConstants.TABLE_ROLE + "(" + IDBConstants.ATTR_AUTOINDEX + ") ON DELETE CASCADE)");

		System.out.println("Table " + IDBConstants.TABLE_USER_ROLE
				+ " has created");
	}

	public static void deleteUserRoleTable(Statement stm) throws Exception {
		stm.executeUpdate("DROP TABLE " + IDBConstants.TABLE_USER_ROLE);

		System.out.println("Table " + IDBConstants.TABLE_USER_ROLE
				+ " has dropped");
	}
}
