package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;

import pohaci.gumunda.aas.logic.AuthorizationBusinessLogic;
import pohaci.gumunda.aas.logic.AuthorizationException;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;

public class OrganizationLogic {

	public Organization createOrganization(HRMBusinessLogic logic, long sessionid, String modul, Organization org) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				logic.m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}
	
			logic.m_conn.setAutoCommit(false);
			trans = logic.m_conn.getTransactionIsolation();
			logic.m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	
			IHRMSQL isql = new HRMSQLSAP();
			isql.createOrganization(org, logic.m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_ORGANIZATION,
					logic.m_conn);
	
			if (org.getParent() != null) {
				isql.createOrganizationStructure(org.getParent().getIndex(),
						index, logic.m_conn);
			}
	
			logic.m_conn.commit();
			logic.m_conn.setAutoCommit(true);
			logic.m_conn.setTransactionIsolation(trans);
	
			return new Organization(index, org);
		} catch (Exception ex) {
			try {
				logic.m_conn.rollback();
				logic.m_conn.setAutoCommit(true);
				logic.m_conn.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		}
	}

	public Organization getOrganizationByIndex(long index,Connection m_conn) throws Exception {	  
		IHRMSQL isql = new HRMSQLSAP();
		return isql.getOrganizationByIndex(index, m_conn);	  
	}
	
	public Organization updateOrganization(HRMBusinessLogic logic, long sessionid, String modul, long index, Organization org) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
				throw new AuthorizationException(
						"Authorization update of module " + modul + " denied");
			}
	
			IHRMSQL isql = new HRMSQLSAP();
			isql.updateOrganization(index, org, logic.m_conn);
			return new Organization(index, org);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteOrganization(HRMBusinessLogic logic, long sessionid, String modul, Organization org) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
				throw new AuthorizationException(
						"Authorization update of module " + modul + " denied");
			}
	
			IHRMSQL isql = new HRMSQLSAP();
			isql.deleteOrganization(org.getIndex(), logic.m_conn);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

}
