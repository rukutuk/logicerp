package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;

import pohaci.gumunda.aas.logic.AuthorizationBusinessLogic;
import pohaci.gumunda.aas.logic.AuthorizationException;
import pohaci.gumunda.titis.hrm.cgui.Qualification;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;

public class QualificationLogic {
	private HRMBusinessLogic m_logic;

	QualificationLogic(HRMBusinessLogic logic)
	{
		this.m_logic = logic;
	}
	public Qualification createQualification(long sessionid, String modul,
			Qualification qua) throws Exception {
		Connection m_conn = m_logic.m_conn;
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IHRMSQL isql = new HRMSQLSAP();
			isql.createQualification(qua, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_QUALIFICATION,
					m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Qualification(index, qua);
		} catch (Exception ex) {
			try {
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		}
	}

	public Qualification updateQualification(long sessionid, String modul, long index, Qualification qua) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
				throw new AuthorizationException(
						"Authorization update of module " + modul + " denied");
			}
	
			m_logic.m_conn.setAutoCommit(false);
			IHRMSQL isql = new HRMSQLSAP();
			isql.updateQualification(index, qua, m_logic.m_conn);
	
			m_logic.m_conn.commit();
			m_logic.m_conn.setAutoCommit(true);
	
			return new Qualification(index, qua);
		} catch (Exception ex) {
			try {
				m_logic.m_conn.rollback();
				m_logic.m_conn.setAutoCommit(true);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		}
	}

	public Qualification[] getAllQualification(long sessionid, String modul)
			throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)) {
				throw new AuthorizationException(
						"Authorization read of module " + modul + " denied");
			}
	
			IHRMSQL isql = new HRMSQLSAP();
			return isql.getAllQualification(m_logic.m_conn);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteQualification(long sessionid, String modul, long index)
			throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_logic.m_conn);
	
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)) {
				throw new AuthorizationException(
						"Authorization update of module " + modul + " denied");
			}
	
			IHRMSQL isql = new HRMSQLSAP();
			isql.deleteQualification(index, m_logic.m_conn);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

}
