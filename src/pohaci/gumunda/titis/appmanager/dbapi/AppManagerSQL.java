/**
 *
 */
package pohaci.gumunda.titis.appmanager.dbapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.entity.IDBConstants;
import pohaci.gumunda.titis.appmanager.entity.Role;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.util.OtherSQLException;

/**
 * @author dark-knight
 *
 */
public class AppManagerSQL {

	public long getMaxIndex(Connection connection, String table) throws SQLException{
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String query = "SELECT MAX(AUTOINDEX) AUTOINDEX FROM " + table;
			ResultSet rs = stm.executeQuery(query);

			if(rs.next())
				return rs.getLong("AUTOINDEX");

			return -1;
		}
		catch(Exception ex) {
			throw new SQLException();
		}
	}

	public UserProfile[] getUserProfiles(Connection connection) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			String query = "SELECT u.*, ud." + IDBConstants.ATTR_FULLNAME + " FROM " +
					"UACCOUNT u, " + IDBConstants.TABLE_USER_DETAIL + " ud WHERE u." +
					IDBConstants.ATTR_USERNAME + "=ud." + IDBConstants.ATTR_USERNAME;
			System.out.println(query);
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				UserProfile userProfile = new UserProfile(rs.getString(IDBConstants.ATTR_USERNAME),
						rs.getBytes("PASSWD"), rs.getString("NOTE"),
						rs.getString(IDBConstants.ATTR_FULLNAME));
				vector.addElement(userProfile);
			}
			UserProfile[] userProfiles = new UserProfile[vector.size()];
			vector.copyInto(userProfiles);
			return userProfiles;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void addNewRole(Connection connection, Role role) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_ROLE + " ("
					+ IDBConstants.ATTR_ROLENAME + ", " // 1
					+ IDBConstants.ATTR_DESCRIPTION + ") " // 2
					+ "VALUES (?, ?)");

			stm.setString(1, role.getRoleName());
			stm.setString(2, role.getDescription());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public Role[] getRoles(Connection connection) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			String query = "SELECT * FROM " + IDBConstants.TABLE_ROLE;
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				Role role = new Role(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ROLENAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
				vector.addElement(role);
			}
			Role[] roles = new Role[vector.size()];
			vector.copyInto(roles);
			return roles;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateRole(Connection connection, Role role) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_ROLE + " SET "
					+ IDBConstants.ATTR_ROLENAME + "=?, " // 1
					+ IDBConstants.ATTR_DESCRIPTION + "=? " // 2
					+ "WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=?"); // 3

			stm.setString(1, role.getRoleName());
			stm.setString(2, role.getDescription());
			stm.setLong(3, role.getAutoindex());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public void deleteRole(Connection connection, Role role) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ROLE +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + role.getAutoindex());
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public ApplicationFunction[] getSuperFunctions(Connection connection, String application) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			String query = "SELECT * FROM " + IDBConstants.TABLE_FUNCTION +
					" WHERE " + IDBConstants.ATTR_APPLICATION + "='" + application + "'" +
					" AND " + IDBConstants.ATTR_AUTOINDEX +
					" NOT IN (SELECT " + IDBConstants.ATTR_SUB_FUNCTION +
					" FROM " + IDBConstants.TABLE_FUNCTION_STRUCTURE +
					") ORDER BY " + IDBConstants.ATTR_AUTOINDEX;
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				ApplicationFunction function = new ApplicationFunction();
				function.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				function.setApplication(rs.getString(IDBConstants.ATTR_APPLICATION));
				function.setFunctionName(rs.getString(IDBConstants.ATTR_FUNCTIONNAME));
				function.setPath(rs.getString(IDBConstants.ATTR_TREE_PATH));
				vector.addElement(function);
			}
			ApplicationFunction[] functions = new ApplicationFunction[vector.size()];
			vector.copyInto(functions);
			return functions;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public ApplicationFunction[] getSubFunctions(Connection connection, ApplicationFunction parentFunction) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			String query = "SELECT DISTINCT " + IDBConstants.TABLE_FUNCTION + ".*  FROM " +
					IDBConstants.TABLE_FUNCTION + ", " + IDBConstants.TABLE_FUNCTION_STRUCTURE +
					" WHERE " + IDBConstants.ATTR_SUPER_FUNCTION + "=" + parentFunction.getAutoindex() +
					" AND " + IDBConstants.ATTR_SUB_FUNCTION + "=" + IDBConstants.TABLE_FUNCTION + "." + IDBConstants.ATTR_AUTOINDEX +
					" ORDER BY " + IDBConstants.TABLE_FUNCTION + "." + IDBConstants.ATTR_AUTOINDEX;
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				ApplicationFunction function = new ApplicationFunction();
				function.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				function.setApplication(rs.getString(IDBConstants.ATTR_APPLICATION));
				function.setFunctionName(rs.getString(IDBConstants.ATTR_FUNCTIONNAME));
				function.setPath(rs.getString(IDBConstants.ATTR_TREE_PATH));
				vector.addElement(function);
			}
			ApplicationFunction[] functions = new ApplicationFunction[vector.size()];
			vector.copyInto(functions);
			return functions;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public ApplicationFunction getFunctionByPath(Connection connection, String path, String application) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String query = "SELECT * FROM " + IDBConstants.TABLE_FUNCTION +
					" WHERE " + IDBConstants.ATTR_TREE_PATH + "= '" + path + "'" +
					" AND " + IDBConstants.ATTR_APPLICATION + "= '" + application + "'";			
			ResultSet rs = stm.executeQuery(query);

			if(rs.next()){
				ApplicationFunction function = new ApplicationFunction();
				function.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				function.setApplication(rs.getString(IDBConstants.ATTR_APPLICATION));
				function.setFunctionName(rs.getString(IDBConstants.ATTR_FUNCTIONNAME));
				function.setPath(rs.getString(IDBConstants.ATTR_TREE_PATH));

				return function;
			}
			return null;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteRoleMappingByRole(Connection connection, Role role) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String query = "DELETE FROM " + IDBConstants.TABLE_ROLE_MAPPING +
					" WHERE " + IDBConstants.ATTR_ROLE + "=" + role.getAutoindex();
			System.out.println(query);
			stm.executeUpdate(query);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createNewRoleMapping(Connection connection, Role role, ApplicationFunction privilege) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_ROLE_MAPPING + " ("
					+ IDBConstants.ATTR_ROLE + ", " // 1
					+ IDBConstants.ATTR_APPLICATION + ", " // 2
					+ IDBConstants.ATTR_FUNCTION + ") " // 3
					+ "VALUES (?, ?, ?)");

			stm.setLong(1, role.getAutoindex());
			stm.setString(2, privilege.getApplication());
			stm.setLong(3, privilege.getAutoindex());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public ApplicationFunction[] getFunctionsByRole(Connection connection, Role role) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			String query = "SELECT " + IDBConstants.TABLE_FUNCTION + ".* FROM "
				+ IDBConstants.TABLE_ROLE_MAPPING + ", "
				+ IDBConstants.TABLE_FUNCTION
				+ " WHERE "
				+ IDBConstants.TABLE_ROLE_MAPPING + "." + IDBConstants.ATTR_FUNCTION + "="
				+ IDBConstants.TABLE_FUNCTION + "." + IDBConstants.ATTR_AUTOINDEX
				+ " AND "
				+ IDBConstants.ATTR_ROLE + "=" + role.getAutoindex();

			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				ApplicationFunction function = new ApplicationFunction();
				function.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				function.setApplication(rs.getString(IDBConstants.ATTR_APPLICATION));
				function.setFunctionName(rs.getString(IDBConstants.ATTR_FUNCTIONNAME));
				function.setPath(rs.getString(IDBConstants.ATTR_TREE_PATH));
				vector.addElement(function);
			}
			ApplicationFunction[] functions = new ApplicationFunction[vector.size()];
			vector.copyInto(functions);
			return functions;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void addNewUserType(Connection connection, String username) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("INSERT INTO "
					+ "USERTYPE (USERNAME, UGROUP)"
					+ "VALUES (?, 'Administrator')");

			stm.setString(1, username);

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public void addNewUserAccount(Connection connection, UserProfile userProfile) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("INSERT INTO "
					+ "UACCOUNT (USERNAME, PASSWD, NOTE)"
					+ "VALUES (?, ?, ?)");

			stm.setString(1, userProfile.getUsername());
			stm.setBytes(2, userProfile.getPassword());
			stm.setString(3, userProfile.getNote());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public void addNewUserDetail(Connection connection, UserProfile userProfile) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_USER_DETAIL + " ("
					+ IDBConstants.ATTR_USERNAME + ", " // 1
					+ IDBConstants.ATTR_FULLNAME + ") " // 2
					+ "VALUES (?, ?)");

			stm.setString(1, userProfile.getUsername());
			stm.setString(2, userProfile.getFullname());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}

	}

	public void addNewUserRole(Connection connection, UserProfile userProfile, Role role) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_USER_ROLE + " ("
					+ IDBConstants.ATTR_USERNAME + ", " // 1
					+ IDBConstants.ATTR_ROLE + ") " // 2
					+ "VALUES (?, ?)");

			stm.setString(1, userProfile.getUsername());
			stm.setLong(2, role.getAutoindex());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public void updateUserAccount(Connection connection, UserProfile userProfile) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("UPDATE UACCOUNT SET "
					+ "USERNAME=?, "
					+ "PASSWD=?, "
					+ "NOTE=? "
					+ "WHERE USERNAME=?");

			stm.setString(1, userProfile.getUsername());
			stm.setBytes(2, userProfile.getPassword());
			stm.setString(3, userProfile.getNote());
			stm.setString(4, userProfile.getUsername());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public void updateUserDetail(Connection connection, UserProfile userProfile) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = connection.prepareStatement("UPDATE " + IDBConstants.TABLE_USER_DETAIL + " SET "
					+ IDBConstants.ATTR_USERNAME + "=?, "
					+ IDBConstants.ATTR_FULLNAME + "=? "
					+ "WHERE " + IDBConstants.ATTR_USERNAME + "=?");

			stm.setString(1, userProfile.getUsername());
			stm.setString(2, userProfile.getFullname());
			stm.setString(3, userProfile.getUsername());

			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(e));
		} finally {
			if(stm!=null)
				stm.close();
		}
	}

	public void deleteUserRoleByUser(Connection connection, UserProfile userProfile) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String query = "DELETE FROM " + IDBConstants.TABLE_USER_ROLE +
			" WHERE " + IDBConstants.ATTR_USERNAME + "='" + userProfile.getUsername() + "'";
			System.out.println(query);
			stm.executeUpdate(query);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Role[] getRolesByUser(Connection connection, UserProfile user) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			String query = "SELECT " + IDBConstants.TABLE_ROLE + ".* FROM "
				+ IDBConstants.TABLE_USER_ROLE + ", " + IDBConstants.TABLE_ROLE
				+ " WHERE "
				+ IDBConstants.TABLE_USER_ROLE + "." + IDBConstants.ATTR_ROLE + "="
				+ IDBConstants.TABLE_ROLE + "." + IDBConstants.ATTR_AUTOINDEX
				+ " AND "
				+ IDBConstants.ATTR_USERNAME + "='" + user.getUsername() +"'";

			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				Role role = new Role();
				role.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				role.setRoleName(rs.getString(IDBConstants.ATTR_ROLENAME));
				role.setDescription(rs.getString(IDBConstants.ATTR_DESCRIPTION));
				vector.addElement(role);
			}
			Role[] roles = new Role[vector.size()];
			vector.copyInto(roles);
			return roles;
		}
		catch(Exception ex) {
			throw new SQLException();
		}
	}

	public void deleteUserDetail(Connection connection, UserProfile userProfile) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String query = "DELETE FROM " + IDBConstants.TABLE_USER_DETAIL +
			" WHERE " + IDBConstants.ATTR_USERNAME + "='" + userProfile.getUsername() + "'";
			System.out.println(query);
			stm.executeUpdate(query);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteUserAccount(Connection connection, UserProfile userProfile) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String query = "DELETE FROM UACCOUNT " +
			" WHERE " + IDBConstants.ATTR_USERNAME + "='" + userProfile.getUsername() + "'";
			stm.executeUpdate(query);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public UserProfile getUserProfilByUsername(Connection connection, String username) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String query = "SELECT u.*, ud." + IDBConstants.ATTR_FULLNAME + " FROM " +
					"UACCOUNT u, " + IDBConstants.TABLE_USER_DETAIL + " ud WHERE u." +
					IDBConstants.ATTR_USERNAME + "=ud." + IDBConstants.ATTR_USERNAME +
					" AND u." + IDBConstants.ATTR_USERNAME + "='" + username + "'";
			System.out.println(query);
			ResultSet rs = stm.executeQuery(query);

			UserProfile userProfile = null;
			if(rs.next()){
				userProfile = new UserProfile(rs.getString(IDBConstants.ATTR_USERNAME),
						rs.getBytes("PASSWD"), rs.getString("NOTE"),
						rs.getString(IDBConstants.ATTR_FULLNAME));
			}

			return userProfile;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public ApplicationFunction[] getGrantedFunctionForUser(Connection connection, UserProfile profile, String application) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			String query = "SELECT " + IDBConstants.TABLE_FUNCTION + ".* FROM " +
				IDBConstants.TABLE_USER_ROLE + ", " +
				IDBConstants.TABLE_ROLE_MAPPING + ", " +
				IDBConstants.TABLE_FUNCTION + " WHERE " +
				IDBConstants.TABLE_USER_ROLE + "." + IDBConstants.ATTR_ROLE + "=" +
				IDBConstants.TABLE_ROLE_MAPPING + "." + IDBConstants.ATTR_ROLE + " AND " +
				IDBConstants.TABLE_ROLE_MAPPING + "." + IDBConstants.ATTR_FUNCTION + "=" +
				IDBConstants.TABLE_FUNCTION + "." + IDBConstants.ATTR_AUTOINDEX + " AND " +
				IDBConstants.TABLE_USER_ROLE + "." + IDBConstants.ATTR_USERNAME + "='" + profile.getUsername() + "' AND " +
				IDBConstants.TABLE_FUNCTION + "." + IDBConstants.ATTR_APPLICATION + "='" + application + "'";

			System.out.println(query);
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				ApplicationFunction function = new ApplicationFunction();
				function.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				function.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				function.setApplication(rs.getString(IDBConstants.ATTR_APPLICATION));
				function.setFunctionName(rs.getString(IDBConstants.ATTR_FUNCTIONNAME));
				function.setPath(rs.getString(IDBConstants.ATTR_TREE_PATH));
				vector.addElement(function);
			}

			ApplicationFunction[] functions = new ApplicationFunction[vector.size()];
			vector.copyInto(functions);
			return functions;
		}
		catch(Exception ex) {
			throw new SQLException();
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}
}
