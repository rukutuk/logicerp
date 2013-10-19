package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;

import pohaci.gumunda.aas.logic.AuthorizationBusinessLogic;
import pohaci.gumunda.aas.logic.AuthorizationException;
import pohaci.gumunda.titis.hrm.cgui.JobTitle;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;

public class JobTitleLogic {

	public JobTitle createJobTitle(HRMBusinessLogic logic, long sessionid, String modul, JobTitle job)
			throws Exception {
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
			isql.createJobTitle(job, logic.m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_JOB_TITLE, logic.m_conn);
	
			logic.m_conn.commit();
			logic.m_conn.setAutoCommit(true);
			logic.m_conn.setTransactionIsolation(trans);
	
			return new JobTitle(index, job);
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

	public JobTitle updateJobTitle(HRMBusinessLogic logic, long sessionid, String modul, long index, JobTitle job) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
				throw new AuthorizationException(
						"Authorization update of module " + modul + " denied");
			}
	
			logic.m_conn.setAutoCommit(false);
			IHRMSQL isql = new HRMSQLSAP();
			isql.updateJobTitle(index, job, logic.m_conn);
	
			logic.m_conn.commit();
			logic.m_conn.setAutoCommit(true);
	
			return new JobTitle(index, job);
		} catch (Exception ex) {
			try {
				logic.m_conn.rollback();
				logic.m_conn.setAutoCommit(true);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		}
	}

	public JobTitle[] getAllJobTitle(HRMBusinessLogic logic, long sessionid, String modul)
			throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
				throw new AuthorizationException(
						"Authorization read of module " + modul + " denied");
			}
	
			IHRMSQL isql = new HRMSQLSAP();
			return isql.getAllJobTitle(logic.m_conn);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteJobTitle(HRMBusinessLogic logic, long sessionid, String modul, long index)
			throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
				throw new AuthorizationException(
						"Authorization update of module " + modul + " denied");
			}
	
			IHRMSQL isql = new HRMSQLSAP();
			isql.deleteJobTitle(index, logic.m_conn);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

}
