/**
 * 
 */
package pohaci.gumunda.titis.appmanager.logic;

import java.sql.Connection;
import java.sql.SQLException;

import pohaci.gumunda.titis.appmanager.dbapi.AppManagerSQL;
import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.entity.IDBConstants;
import pohaci.gumunda.titis.appmanager.entity.Role;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;

/**
 * @author dark-knight
 *
 */
public class AppManagerLogic {
	Connection connection = null;

	public AppManagerLogic(Connection connection) {
		this.connection = connection;
	}
	
	public UserProfile[] getUserProfiles() throws Exception {
	    AppManagerSQL sql = new AppManagerSQL();
	    
	    return sql.getUserProfiles(connection);
	}
	
	public Role addNewRole(Role role) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		sql.addNewRole(connection, role);
		
		long index = sql.getMaxIndex(connection, IDBConstants.TABLE_ROLE);
		
		role.setAutoindex(index);
		
		return role;
	}

	public Role[] getRoles() throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		return sql.getRoles(connection);
	}

	public void updateRole(Role role) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		sql.updateRole(connection, role);
	}

	public void deleteRole(Role role) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		sql.deleteRole(connection, role);
	}

	public ApplicationFunction[] getSuperFunctions(String application) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		return sql.getSuperFunctions(connection, application);
	}

	public ApplicationFunction[] getSubFunctions(ApplicationFunction parentFunction) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		return sql.getSubFunctions(connection, parentFunction);
	}

	public ApplicationFunction getFunctionByPath(String path, String application) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		return sql.getFunctionByPath(connection,  path, application);
	}

	public void deleteRoleMappingByRole(Role role) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		sql.deleteRoleMappingByRole(connection, role);
	}

	public void updateRoleMapping(Role role, ApplicationFunction[] privileges) throws Exception {
		deleteRoleMappingByRole(role);
		
		for(int i=0; i<privileges.length; i++){
			createNewRoleMapping(role, privileges[i]);
		}
	}

	private void createNewRoleMapping(Role role, ApplicationFunction privilege) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		sql.createNewRoleMapping(connection, role, privilege);
	}

	public ApplicationFunction[] getFunctionsByRole(Role role) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		return sql.getFunctionsByRole(connection, role);
	}

	public UserProfile createUserRoles(UserProfile userProfile, Role[] roles) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		int trans = Connection.TRANSACTION_READ_COMMITTED;

	    try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			sql.addNewUserAccount(connection, userProfile);
			sql.addNewUserDetail(connection, userProfile);
			sql.addNewUserType(connection, userProfile.getUsername());
			for(int i=0; i<roles.length; i++){
				sql.addNewUserRole(connection, userProfile, roles[i]);
			}
			
			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);

			return userProfile;
		}
	    catch (Exception ex) {
			connection.rollback();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);

			throw new Exception(ex.getMessage());
		}
	}

	public UserProfile updateUserRoles(UserProfile userProfile, Role[] roles) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		int trans = Connection.TRANSACTION_READ_COMMITTED;

	    try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			sql.updateUserAccount(connection, userProfile);
			sql.updateUserDetail(connection, userProfile);
			sql.deleteUserRoleByUser(connection, userProfile);
			for(int i=0; i<roles.length; i++){
				sql.addNewUserRole(connection, userProfile, roles[i]);
			}
			
			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);

			return userProfile;
		}
	    catch (Exception ex) {
			connection.rollback();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);

			throw new Exception(ex.getMessage(), ex);
		}
	}

	public Role[] getRolesByUser(UserProfile user) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		return sql.getRolesByUser(connection, user);
	}

	public void removeUserRoles(UserProfile userProfile) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();
		
		int trans = Connection.TRANSACTION_READ_COMMITTED;

	    try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			sql.deleteUserRoleByUser(connection, userProfile);
			sql.deleteUserDetail(connection, userProfile);
			sql.deleteUserAccount(connection, userProfile);
			
			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);

			return;
		}
	    catch (Exception ex) {
			connection.rollback();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);

			throw new Exception(ex.getMessage(), ex);
		}
	}

	public UserProfile getUserProfilByUsername(String username) throws Exception {
		AppManagerSQL sql = new AppManagerSQL();		
		UserProfile up =sql.getUserProfilByUsername(connection, username);		
		return up;
	}

	public ApplicationFunction[] getGrantedFunctionForUser(UserProfile profile, String application) throws SQLException {
		AppManagerSQL sql = new AppManagerSQL();
		
		return sql.getGrantedFunctionForUser(connection, profile, application);
	}
}
