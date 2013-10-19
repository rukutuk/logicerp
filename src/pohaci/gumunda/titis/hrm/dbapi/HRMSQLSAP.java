package pohaci.gumunda.titis.hrm.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import pohaci.gumunda.util.OtherSQLException;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.hrm.cgui.report.*;
import pohaci.gumunda.titis.hrm.logic.PaychequesValueRpt;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.PayrollSubmitStatus;

public class HRMSQLSAP implements IHRMSQL {

	public long getMaxIndex(String table, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT MAX("
					+ IDBConstants.ATTR_AUTOINDEX + ") as maxindex FROM "
					+ table);
			rs.next();
			return rs.getLong("maxindex");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createOrganization(Organization org, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_ORGANIZATION + "("
					+ IDBConstants.ATTR_CODE + "," + IDBConstants.ATTR_NAME
					+ "," + IDBConstants.ATTR_DESCRIPTION + ")"
					+ " values (?, ?, ?)");
			stm.setString(1, org.getCode());
			stm.setString(2, org.getName());
			stm.setString(3, org.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createOrganizationStructure(long parentorg, long suborg,
			Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO "
					+ IDBConstants.TABLE_ORGANIZATION_STRUCTURE + " values("
					+ parentorg + "," + suborg + ")");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}


	public Organization getOrganizationByIndex(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_ORGANIZATION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new Organization(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_NAME), rs
						.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}

	public Organization[] getSuperOrganization(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM "
			+ IDBConstants.TABLE_ORGANIZATION + " WHERE "
			+ IDBConstants.ATTR_AUTOINDEX + " NOT IN (SELECT "
			+ IDBConstants.ATTR_SUB_ORG + " FROM "
			+ IDBConstants.TABLE_ORGANIZATION_STRUCTURE + ")";
		stm = conn.createStatement();
		ResultSet rs = stm.executeQuery(query);
		while (rs.next()) {
			vresult.addElement(new Organization(rs
					.getLong(IDBConstants.ATTR_AUTOINDEX), rs
					.getString(IDBConstants.ATTR_CODE), rs
					.getString(IDBConstants.ATTR_NAME), rs
					.getString(IDBConstants.ATTR_DESCRIPTION)));
		}
		Organization[] org = new Organization[vresult.size()];
		vresult.copyInto(org);
		return org;
	}


	public Organization[] getSubOrganization(long parentindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT "
			+ IDBConstants.TABLE_ORGANIZATION + ".*  FROM "
			+ IDBConstants.TABLE_ORGANIZATION + ", "
			+ IDBConstants.TABLE_ORGANIZATION_STRUCTURE + " WHERE "
			+ IDBConstants.ATTR_PARENT_ORG + "=" + parentindex
			+ " AND " + IDBConstants.ATTR_SUB_ORG + "="
			+ IDBConstants.ATTR_AUTOINDEX;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(new Organization(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_NAME), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			Organization[] org = new Organization[vresult.size()];
			vresult.copyInto(org);
			return org;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateOrganization(long index, Organization org, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_ORGANIZATION + " SET "
					+ IDBConstants.ATTR_CODE + "=?, " + IDBConstants.ATTR_NAME
					+ "=?, " + IDBConstants.ATTR_DESCRIPTION + "=? "
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, org.getCode());
			stm.setString(2, org.getName());
			stm.setString(3, org.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteOrganization(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ORGANIZATION
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createQualification(Qualification qua, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_QUALIFICATION + "("
					+ IDBConstants.ATTR_CODE + "," + IDBConstants.ATTR_NAME
					+ "," + IDBConstants.ATTR_DESCRIPTION + ")"
					+ " values (?, ?, ?)");
			stm.setString(1, qua.getCode());
			stm.setString(2, qua.getName());
			stm.setString(3, qua.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Qualification[] getAllQualification(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_QUALIFICATION);
			while (rs.next()) {
				vresult.addElement(new Qualification(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_NAME), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			Qualification[] result = new Qualification[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Qualification getQualification(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_QUALIFICATION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new Qualification(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_NAME), rs
						.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateQualification(long index, Qualification qua,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_QUALIFICATION + " SET "
					+ IDBConstants.ATTR_CODE + "=?, " + IDBConstants.ATTR_NAME
					+ "=?, " + IDBConstants.ATTR_DESCRIPTION + "=? "
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, qua.getCode());
			stm.setString(2, qua.getName());
			stm.setString(3, qua.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteQualification(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_QUALIFICATION
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createJobTitle(JobTitle job, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_JOB_TITLE + "("
					+ IDBConstants.ATTR_CODE + "," + IDBConstants.ATTR_NAME
					+ "," + IDBConstants.ATTR_DESCRIPTION + ")"
					+ " values (?, ?, ?)");
			stm.setString(1, job.getCode());
			stm.setString(2, job.getName());
			stm.setString(3, job.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public JobTitle[] getAllJobTitle(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_JOB_TITLE);
			while (rs.next()) {
				vresult.addElement(
						new JobTitle(
								rs.getLong(IDBConstants.ATTR_AUTOINDEX),
								rs.getString(IDBConstants.ATTR_CODE),
								rs.getString(IDBConstants.ATTR_NAME),
								rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			JobTitle[] result = new JobTitle[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public JobTitle getJobTitle(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		String query = "SELECT * FROM "
			+ IDBConstants.TABLE_JOB_TITLE + " WHERE "
			+ IDBConstants.ATTR_AUTOINDEX + "=" + index;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return new JobTitle(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateJobTitle(long index, JobTitle job, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_JOB_TITLE + " SET "
					+ IDBConstants.ATTR_CODE + "=?, " + IDBConstants.ATTR_NAME
					+ "=?, " + IDBConstants.ATTR_DESCRIPTION + "=? "
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, job.getCode());
			stm.setString(2, job.getName());
			stm.setString(3, job.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteJobTitle(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_JOB_TITLE
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createWorkAgreement(WorkAgreement work, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_WORK_AGREEMENT + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?, ?)");

			stm.setString(1, work.getCode());
			stm.setString(2, work.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public WorkAgreement[] getAllWorkAgreement(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_WORK_AGREEMENT);
			while (rs.next()) {
				vresult.addElement(new WorkAgreement(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			WorkAgreement[] result = new WorkAgreement[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public WorkAgreement getWorkAgreement(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
                        String query = "SELECT * FROM "
					+ IDBConstants.TABLE_WORK_AGREEMENT + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index;
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return new WorkAgreement(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateWorkAgreement(long index, WorkAgreement work,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_WORK_AGREEMENT + " SET "
					+ IDBConstants.ATTR_CODE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, work.getCode());
			stm.setString(2, work.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteWorkAgreement(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_WORK_AGREEMENT
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEducation(Education edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EDUCATION + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?, ?)");
			stm.setString(1, edu.getCode());
			stm.setString(2, edu.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Education[] getAllEducation(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EDUCATION);
			while (rs.next()) {
				vresult.addElement(
						new Education(
								rs.getLong(IDBConstants.ATTR_AUTOINDEX),
								rs.getString(IDBConstants.ATTR_CODE),
								rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			Education[] result = new Education[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Education getEducation(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EDUCATION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new Education(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateEducation(long index, Education edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EDUCATION + " SET "
					+ IDBConstants.ATTR_CODE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, edu.getCode());
			stm.setString(2, edu.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEducation(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EDUCATION
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createReligion(SimpleEmployeeAttribute religion, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_RELIGION + "("
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?)");
			stm.setString(1, religion.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployeeAttribute[] getAllReligion(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_RELIGION);
			while (rs.next()) {
				vresult.addElement(new SimpleEmployeeAttribute(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			SimpleEmployeeAttribute[] result = new SimpleEmployeeAttribute[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployeeAttribute getReligion(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_RELIGION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new SimpleEmployeeAttribute(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateReligion(long index, SimpleEmployeeAttribute religion,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_RELIGION
					+ " SET " + IDBConstants.ATTR_DESCRIPTION + "=? "
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, religion.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteReligion(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_RELIGION
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createSexType(SexType type, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_SEX_TYPE + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?, ?)");

			stm.setString(1, type.getCode());
			stm.setString(2, type.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SexType[] getAllSexType(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_SEX_TYPE);
			while (rs.next()) {
				vresult.addElement(new SexType(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			SexType[] result = new SexType[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SexType getSexType(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_SEX_TYPE + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new SexType(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateSexType(long index, SexType type, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_SEX_TYPE
					+ " SET " + IDBConstants.ATTR_CODE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, type.getCode());
			stm.setString(2, type.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteSexType(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_SEX_TYPE
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createFamilyRelation(SimpleEmployeeAttribute relation,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_FAMILY_RELATION + "("
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?)");
			stm.setString(1, relation.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployeeAttribute[] getAllFamilyRelation(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_FAMILY_RELATION);
			while (rs.next()) {
				vresult.addElement(
						new SimpleEmployeeAttribute(
								rs.getLong(IDBConstants.ATTR_AUTOINDEX),
								rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			SimpleEmployeeAttribute[] result = new SimpleEmployeeAttribute[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployeeAttribute getFamilyRelation(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_FAMILY_RELATION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new SimpleEmployeeAttribute(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateFamilyRelation(long index,
			SimpleEmployeeAttribute relation, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_FAMILY_RELATION + " SET "
					+ IDBConstants.ATTR_DESCRIPTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, relation.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteFamilyRelation(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_FAMILY_RELATION
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createMaritalStatus(SimpleEmployeeAttribute status,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_MARITAL_STATUS + "("
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?)");
			stm.setString(1, status.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployeeAttribute[] getAllMaritalStatus(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_MARITAL_STATUS);
			while (rs.next()) {
				vresult.addElement(
						new SimpleEmployeeAttribute(
								rs.getLong(IDBConstants.ATTR_AUTOINDEX),
								rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			SimpleEmployeeAttribute[] result = new SimpleEmployeeAttribute[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployeeAttribute getMaritalStatus(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_MARITAL_STATUS + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new SimpleEmployeeAttribute(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateMaritalStatus(long index, SimpleEmployeeAttribute status,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_MARITAL_STATUS + " SET "
					+ IDBConstants.ATTR_DESCRIPTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, status.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteMaritalStatus(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_MARITAL_STATUS
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeOfficeWorkingTime(Connection conn, Employee emp,
			WorkingTimeState wts) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMP_ABS_OFFWORKTIME + "("
					+ IDBConstants.ATTR_EMPLOYEEID + ","
					+ IDBConstants.ATTR_DATE + ","
					+ IDBConstants.ATTR_ABSSTATUS + ","
					+ IDBConstants.ATTR_OVERTIME + ")" + " values (?, ?, ?,?)");
			stm.setLong(1, emp.getIndex());
			stm.setDate(2, new java.sql.Date(wts.getDate().getTime()));
			stm.setInt(3, wts.getState());
			float f = wts.getOverTime();
			stm.setFloat(4, f);
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateEmployeeOfficeWorkingTime(Connection conn, Employee emp,
			WorkingTimeState wts) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMP_ABS_OFFWORKTIME + " SET "
					+ IDBConstants.ATTR_ABSSTATUS + "=? " + ","
					+ IDBConstants.ATTR_OVERTIME + "= ?" + " WHERE "
					+ IDBConstants.ATTR_DATE + "= ?" + "AND "
					+ IDBConstants.ATTR_EMPLOYEEID + "=?");
			stm.setInt(1, wts.getState());
			stm.setFloat(2, wts.getOverTime());
			stm.setDate(3, new java.sql.Date(wts.getDate().getTime()));
			stm.setLong(4, emp.getIndex());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeOfficeWorkingTime(Connection conn, Employee emp,
			WorkingTimeState wts) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMP_ABS_OFFWORKTIME + " WHERE "
					+ IDBConstants.ATTR_DATE + "='"
					+ new java.sql.Date(wts.getDate().getTime()) + "' AND "
					+ IDBConstants.ATTR_EMPLOYEEID + "='" + emp.getIndex()
					+ "'");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public WorkingTimeState[] getEmployeeOfficeWorkingTime(Connection conn,
			long empIndex) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMP_ABS_OFFWORKTIME + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEEID + "=" + empIndex);
			while (rs.next()) {
				vresult.addElement(new WorkingTimeState(rs
						.getDate(IDBConstants.ATTR_DATE), rs
						.getInt(IDBConstants.ATTR_ABSSTATUS), rs
						.getFloat(IDBConstants.ATTR_OVERTIME)));
			}
			WorkingTimeState[] result = new WorkingTimeState[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createAllowenceMultiplier(AllowenceMultiplier multiplier,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_ALLOWENCE_MULTIPLIER + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ","
					+ IDBConstants.ATTR_MULTIPLIER + ")" + " values (?, ?, ?)");

			stm.setString(1, multiplier.getAreaCode());
			stm.setString(2, multiplier.getDescription());
			stm.setFloat(3, multiplier.getMuliplier());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public AllowenceMultiplier[] getAllAllowenceMultiplier(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_ALLOWENCE_MULTIPLIER;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(new AllowenceMultiplier(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getFloat(IDBConstants.ATTR_MULTIPLIER)));
			}
			AllowenceMultiplier[] result = new AllowenceMultiplier[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public AllowenceMultiplier getAllowenceMultiplier(long index,
			Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_ALLOWENCE_MULTIPLIER + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new AllowenceMultiplier(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getFloat(IDBConstants.ATTR_MULTIPLIER));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateAllowenceMultiplier(long index,
			AllowenceMultiplier multiplier, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_ALLOWENCE_MULTIPLIER + " SET "
					+ IDBConstants.ATTR_CODE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=?, "
					+ IDBConstants.ATTR_MULTIPLIER + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, multiplier.getAreaCode());
			stm.setString(2, multiplier.getDescription());
			stm.setFloat(3, multiplier.getMuliplier());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteAllowenceMultiplier(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_ALLOWENCE_MULTIPLIER + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPTKP(PTKP ptkp, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_PTKP + "(" + IDBConstants.ATTR_NAME
					+ "," + IDBConstants.ATTR_DESCRIPTION + ","
					+ IDBConstants.ATTR_VALUE + ")" + " values (?, ?, ?)");
			stm.setString(1, ptkp.getName());
			stm.setString(2, ptkp.getDescription());
			stm.setDouble(3, ptkp.getValue());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PTKP[] getAllPTKP(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PTKP);
			while (rs.next()) {
				vresult.addElement(new PTKP(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getDouble(IDBConstants.ATTR_VALUE)));
			}
			PTKP[] result = new PTKP[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PTKP getPTKP(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PTKP + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new PTKP(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getDouble(IDBConstants.ATTR_VALUE));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updatePTKP(long index, PTKP ptkp, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PTKP
					+ " SET " + IDBConstants.ATTR_NAME + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=?, "
					+ IDBConstants.ATTR_VALUE + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, ptkp.getName());
			stm.setString(2, ptkp.getDescription());
			stm.setDouble(3, ptkp.getValue());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deletePTKP(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PTKP + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTaxArt21Tariff(TaxArt21Tariff tariff, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_TAX_ART_21_TARIFF + "("
					+ IDBConstants.ATTR_MINIMUM + ","
					+ IDBConstants.ATTR_MAXIMUM + ","
					+ IDBConstants.ATTR_TARIFF + ")" + " values (?, ?, ?)");
			stm.setDouble(1, tariff.getMinimum());
			stm.setDouble(2, tariff.getMaximum());
			stm.setFloat(3, tariff.getTariff());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21Tariff[] getAllTaxArt21Tariff(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_TAX_ART_21_TARIFF);
			while (rs.next()) {
				vresult.addElement(
						new TaxArt21Tariff(
								rs.getLong(IDBConstants.ATTR_AUTOINDEX),
								rs.getDouble(IDBConstants.ATTR_MINIMUM),
								rs.getDouble(IDBConstants.ATTR_MAXIMUM),
								rs.getFloat(IDBConstants.ATTR_TARIFF)));
			}
			TaxArt21Tariff[] result = new TaxArt21Tariff[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21Tariff getTaxArt21Tariff(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_TAX_ART_21_TARIFF + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new TaxArt21Tariff(
						rs.getDouble(IDBConstants.ATTR_MINIMUM),
						rs.getDouble(IDBConstants.ATTR_MAXIMUM),
						rs.getFloat(IDBConstants.ATTR_TARIFF));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateTaxArt21Tariff(long index, TaxArt21Tariff tariff,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_TAX_ART_21_TARIFF + " SET "
					+ IDBConstants.ATTR_MINIMUM + "=?, "
					+ IDBConstants.ATTR_MAXIMUM + "=?, "
					+ IDBConstants.ATTR_TARIFF + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setDouble(1, tariff.getMinimum());
			stm.setDouble(2, tariff.getMaximum());
			stm.setFloat(3, tariff.getTariff());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteTaxArt21Tariff(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_TAX_ART_21_TARIFF
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createLeaveType(LeaveType type, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_LEAVE_TYPE + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ","
					+ IDBConstants.ATTR_DEDUCTION + ")" + " values (?, ?, ?)");
			stm.setString(1, type.getCode());
			stm.setString(2, type.getDescription());
			stm.setBoolean(3, type.getDeduction());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public LeaveType[] getAllLeaveType(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_LEAVE_TYPE);
			while (rs.next()) {
				vresult.addElement(new LeaveType(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getBoolean(IDBConstants.ATTR_DEDUCTION)));
			}
			LeaveType[] result = new LeaveType[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public LeaveType getLeaveType(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_LEAVE_TYPE + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new LeaveType(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getBoolean(IDBConstants.ATTR_DEDUCTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateLeaveType(long index, LeaveType type, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_LEAVE_TYPE + " SET "
					+ IDBConstants.ATTR_CODE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=?, "
					+ IDBConstants.ATTR_DEDUCTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, type.getCode());
			stm.setString(2, type.getDescription());
			stm.setBoolean(3, type.getDeduction());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteLeaveType(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_LEAVE_TYPE
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPermitionType(PermitionType type, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_PERMITION_TYPE + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ","
					+ IDBConstants.ATTR_DAYS + ", "
					+ IDBConstants.ATTR_DEDUCTION + ")"
					+ " values (?, ?, ?, ?)");
			stm.setString(1, type.getCode());
			stm.setString(2, type.getDescription());
			stm.setInt(3, type.getDays());
			stm.setBoolean(4, type.getDeduction());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PermitionType[] getAllPermitionType(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PERMITION_TYPE);
			while (rs.next()) {
				vresult.addElement(new PermitionType(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getInt(IDBConstants.ATTR_DAYS),
						rs.getBoolean(IDBConstants.ATTR_DEDUCTION)));
			}
			PermitionType[] result = new PermitionType[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PermitionType getPermitionType(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PERMITION_TYPE + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new PermitionType(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getInt(IDBConstants.ATTR_DAYS),
						rs.getBoolean(IDBConstants.ATTR_DEDUCTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updatePermitionType(long index, PermitionType type,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_PERMITION_TYPE + " SET "
					+ IDBConstants.ATTR_CODE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=?, "
					+ IDBConstants.ATTR_DAYS + "=?, "
					+ IDBConstants.ATTR_DEDUCTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, type.getCode());
			stm.setString(2, type.getDescription());
			stm.setInt(3, type.getDays());
			stm.setBoolean(4, type.getDeduction());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deletePermitionType(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PERMITION_TYPE
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createOfficeHourPermition(OfficeHourPermition hour,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?, ?)");
			stm.setString(1, hour.getCode());
			stm.setString(2, hour.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public OfficeHourPermition[] getAllOfficeHourPermition(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION);
			while (rs.next()) {
				vresult.addElement(new OfficeHourPermition(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			OfficeHourPermition[] result = new OfficeHourPermition[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public OfficeHourPermition getOfficeHourPermition(long index,
			Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new OfficeHourPermition(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateOfficeHourPermition(long index, OfficeHourPermition hour,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION + " SET "
					+ IDBConstants.ATTR_CODE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, hour.getCode());
			stm.setString(2, hour.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteOfficeHourPermition(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_OFFICE_HOUR_PERMITION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployee(Employee employee, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "+ IDBConstants.TABLE_EMPLOYEE+ "("
					+ IDBConstants.ATTR_EMPLOYEE_NO + ", " + IDBConstants.ATTR_FIRST_NAME + ", "
					+ IDBConstants.ATTR_MIDLE_NAME + ", " + IDBConstants.ATTR_LAST_NAME + ", "
					+ IDBConstants.ATTR_NICK_NAME + ", " + IDBConstants.ATTR_BIRTH_PLACE + ", "
					+ IDBConstants.ATTR_BIRTH_DATE + ", " + IDBConstants.ATTR_SEX + ", "
					+ IDBConstants.ATTR_RELIGION + ", " + IDBConstants.ATTR_NATIONALITY + ", "
					+ IDBConstants.ATTR_MARITAL + ", " + IDBConstants.ATTR_ART_21 + ", "
					+ IDBConstants.ATTR_ADDRESS + ", " + IDBConstants.ATTR_CITY + ", "
					+ IDBConstants.ATTR_POST_CODE + "," + IDBConstants.ATTR_PROVINCE + ", "
					+ IDBConstants.ATTR_COUNTRY + ", " + IDBConstants.ATTR_PHONE + ", "
					+ IDBConstants.ATTR_MOBILE_PHONE1 + ", " + IDBConstants.ATTR_MOBILE_PHONE2 + ", "
					+ IDBConstants.ATTR_FAX + ", " + IDBConstants.ATTR_EMAIL + ")"
					+ " values (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setString(col++, employee.getEmployeeNo());
			stm.setString(col++, employee.getFirstName());
			stm.setString(col++, employee.getMidleName());
			stm.setString(col++, employee.getLastName());
			stm.setString(col++, employee.getNickName());
			stm.setString(col++, employee.getBirthPlace());
			if (employee.getBirthDate() != null)
				stm.setDate(col++, new java.sql.Date(employee.getBirthDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setLong(col++, employee.getNSex());
			if (employee.getNReligion() != 0)
				stm.setLong(col++, employee.getNReligion());
			else
				stm.setNull(col++, Types.NULL);
			stm.setString(col++, employee.getNationality());
			if (employee.getNMarital() != 0)
				stm.setLong(col++, employee.getNMarital());
			else
				stm.setNull(col++, Types.NULL);
			stm.setLong(col++, employee.getNArt21());
			stm.setString(col++, employee.getAddress());
			stm.setString(col++, employee.getCity());
			stm.setInt(col++, employee.getPostCode());
			stm.setString(col++, employee.getProvince());
			stm.setString(col++, employee.getCountry());
			stm.setString(col++, employee.getPhone());
			stm.setString(col++, employee.getMobilePhone1());
			stm.setString(col++, employee.getMobilePhone2());
			stm.setString(col++, employee.getFax());
			stm.setString(col++, employee.getEmail());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeSubOrdinat[] getEmployeeByUnit(Connection conn,
			long unitNumber) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT e." + IDBConstants.ATTR_AUTOINDEX + ", e."
		+ IDBConstants.ATTR_EMPLOYEE_NO + ", e."
		+ IDBConstants.ATTR_FIRST_NAME + ", e."
		+ IDBConstants.ATTR_MIDLE_NAME + ", e."
		+ IDBConstants.ATTR_LAST_NAME + ", e."
		+ IDBConstants.ATTR_NICK_NAME + ", e."
		+ IDBConstants.ATTR_BIRTH_PLACE + ", e."
		+ IDBConstants.ATTR_BIRTH_DATE + ", e."
		+ IDBConstants.ATTR_NATIONALITY + ", e."
		+ IDBConstants.ATTR_ADDRESS + ", e." + IDBConstants.ATTR_CITY
		+ ", e." + IDBConstants.ATTR_POST_CODE + ", e."
		+ IDBConstants.ATTR_PROVINCE + ", e."
		+ IDBConstants.ATTR_COUNTRY + ", e." + IDBConstants.ATTR_PHONE
		+ ", e." + IDBConstants.ATTR_MOBILE_PHONE1 + ", e."
		+ IDBConstants.ATTR_MOBILE_PHONE2 + ", e."
		+ IDBConstants.ATTR_FAX + ", e." + IDBConstants.ATTR_EMAIL
		+ ", e." + IDBConstants.ATTR_SEX + ", e."
		+ IDBConstants.ATTR_MARITAL + ", e."
		+ IDBConstants.ATTR_RELIGION + ", e."
		+ IDBConstants.ATTR_ART_21 + " FROM "
		+ IDBConstants.TABLE_EMPLOYEE + " e " + " WHERE " + "e."
		+ IDBConstants.ATTR_AUTOINDEX + " IN (SELECT "
		+ IDBConstants.TABLE_EMPLOYEE + " FROM "
		+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " WHERE UNIT ="
		+ unitNumber + ")";
		System.err.println(query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				Employee emp = new Employee(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_EMPLOYEE_NO),
						rs.getString(IDBConstants.ATTR_FIRST_NAME),
						rs.getString(IDBConstants.ATTR_MIDLE_NAME),
						rs.getString(IDBConstants.ATTR_LAST_NAME),
						rs.getString(IDBConstants.ATTR_NICK_NAME),
						rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
						rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
						rs.getLong(IDBConstants.ATTR_SEX),
						rs.getLong(IDBConstants.ATTR_RELIGION),
						rs.getString(IDBConstants.ATTR_NATIONALITY),
						rs.getLong(IDBConstants.ATTR_MARITAL),
						rs.getLong(IDBConstants.ATTR_ART_21),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POST_CODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE),
						rs.getString(IDBConstants.ATTR_MOBILE_PHONE1),
						rs.getString(IDBConstants.ATTR_MOBILE_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX),
						rs.getString(IDBConstants.ATTR_EMAIL));
				Employment[] employeeEmployment = getEmployeeEmployment(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),conn);
				vresult.addElement(new EmployeeSubOrdinat(emp,employeeEmployment));
			}
			EmployeeSubOrdinat[] result = new EmployeeSubOrdinat[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Date[] getJobTMTByUnitAndEmployee(Connection conn, long index,
			long unitIndex) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT " + IDBConstants.ATTR_TMT + " FROM "
				+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " WHERE "
				+ IDBConstants.ATTR_EMPLOYEE + "=" + index + " AND "
				+ IDBConstants.ATTR_UNIT + "=" + unitIndex;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(rs.getDate(IDBConstants.ATTR_TMT));
			}
			Date[] date = new Date[vresult.size()];
			vresult.copyInto(date);
			return date;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public JobTitle getJobTitleByUnitAndEmployee(Connection conn, long index,
			long unitIndex) throws SQLException {
		Statement stm = null;
		String query = "SELECT " + IDBConstants.ATTR_JOB_TITLE + " FROM "
		+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " WHERE "
		+ IDBConstants.ATTR_EMPLOYEE + "=" + index + " AND "
		+ IDBConstants.ATTR_UNIT + "=" + unitIndex;
		System.err.println(query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.next();
			return null;//rs.getLong(IDBConstants.ATTR_JOB_TITLE);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee[] getAllEmployee(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT " + IDBConstants.ATTR_AUTOINDEX + ", "
		+ IDBConstants.ATTR_EMPLOYEE_NO + ", "
		+ IDBConstants.ATTR_FIRST_NAME + ", "
		+ IDBConstants.ATTR_MIDLE_NAME + ", "
		+ IDBConstants.ATTR_LAST_NAME + ", "
		+ IDBConstants.ATTR_NICK_NAME + ", "
		+ IDBConstants.ATTR_BIRTH_PLACE + ", "
		+ IDBConstants.ATTR_BIRTH_DATE + ", "
		+ IDBConstants.ATTR_NATIONALITY + ", "
		+ IDBConstants.ATTR_ADDRESS + ", " + IDBConstants.ATTR_CITY
		+ ", " + IDBConstants.ATTR_POST_CODE + ", "
		+ IDBConstants.ATTR_PROVINCE + ", " + IDBConstants.ATTR_COUNTRY
		+ ", " + IDBConstants.ATTR_PHONE + ", "
		+ IDBConstants.ATTR_MOBILE_PHONE1 + ", "
		+ IDBConstants.ATTR_MOBILE_PHONE2 + ", "
		+ IDBConstants.ATTR_FAX + ", " + IDBConstants.ATTR_EMAIL + ", "
		+ IDBConstants.ATTR_SEX + ", " + IDBConstants.ATTR_MARITAL
		+ ", " + IDBConstants.ATTR_RELIGION + ", "
		+ IDBConstants.ATTR_ART_21 + " FROM "
		+ IDBConstants.TABLE_EMPLOYEE + " emp WHERE "
		+ "NOT EXISTS (SELECT " + IDBConstants.ATTR_EMPLOYEE + " FROM "
		+ IDBConstants.TABLE_EMPLOYEE_RETIREMENT + " ret WHERE "
		+ IDBConstants.ATTR_TMT + "<=DATE(NOW()) AND "
		+ "emp." + IDBConstants.ATTR_AUTOINDEX
		+ "=ret." + IDBConstants.ATTR_EMPLOYEE
		+ " ) " + " ORDER BY " + IDBConstants.ATTR_EMPLOYEE_NO;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(new Employee(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_EMPLOYEE_NO), rs
						.getString(IDBConstants.ATTR_FIRST_NAME), rs
						.getString(IDBConstants.ATTR_MIDLE_NAME), rs
						.getString(IDBConstants.ATTR_LAST_NAME), rs
						.getString(IDBConstants.ATTR_NICK_NAME), rs
						.getString(IDBConstants.ATTR_BIRTH_PLACE), rs
						.getDate(IDBConstants.ATTR_BIRTH_DATE), rs
						.getLong(IDBConstants.ATTR_SEX), rs
						.getLong(IDBConstants.ATTR_RELIGION), rs
						.getString(IDBConstants.ATTR_NATIONALITY), rs
						.getLong(IDBConstants.ATTR_MARITAL), rs
						.getLong(IDBConstants.ATTR_ART_21), rs
						.getString(IDBConstants.ATTR_ADDRESS), rs
						.getString(IDBConstants.ATTR_CITY), rs
						.getInt(IDBConstants.ATTR_POST_CODE), rs
						.getString(IDBConstants.ATTR_PROVINCE), rs
						.getString(IDBConstants.ATTR_COUNTRY), rs
						.getString(IDBConstants.ATTR_PHONE), rs
						.getString(IDBConstants.ATTR_MOBILE_PHONE1), rs
						.getString(IDBConstants.ATTR_MOBILE_PHONE2), rs
						.getString(IDBConstants.ATTR_FAX), rs
						.getString(IDBConstants.ATTR_EMAIL)));
			}
			Employee[] result = new Employee[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee getEmployeeByIndex(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new Employee(rs.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_EMPLOYEE_NO), rs
						.getString(IDBConstants.ATTR_FIRST_NAME), rs
						.getString(IDBConstants.ATTR_MIDLE_NAME), rs
						.getString(IDBConstants.ATTR_LAST_NAME), rs
						.getString(IDBConstants.ATTR_NICK_NAME), rs
						.getString(IDBConstants.ATTR_BIRTH_PLACE), rs
						.getDate(IDBConstants.ATTR_BIRTH_DATE), this
						.getSexType(rs.getLong(IDBConstants.ATTR_SEX), conn),
						this.getReligion(
								rs.getLong(IDBConstants.ATTR_RELIGION), conn),
								rs.getString(IDBConstants.ATTR_NATIONALITY), this
								.getMaritalStatus(rs
										.getLong(IDBConstants.ATTR_MARITAL),
										conn), this.getPTKP(rs
												.getLong(IDBConstants.ATTR_ART_21), conn), rs
												.getString(IDBConstants.ATTR_ADDRESS), rs
												.getString(IDBConstants.ATTR_CITY), rs
												.getInt(IDBConstants.ATTR_POST_CODE), rs
												.getString(IDBConstants.ATTR_PROVINCE), rs
												.getString(IDBConstants.ATTR_COUNTRY), rs
												.getString(IDBConstants.ATTR_PHONE), rs
												.getString(IDBConstants.ATTR_MOBILE_PHONE1), rs
												.getString(IDBConstants.ATTR_MOBILE_PHONE2), rs
												.getString(IDBConstants.ATTR_FAX), rs
												.getString(IDBConstants.ATTR_EMAIL));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee_n getEmployee_nByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			Employee_n emp = null;
			if (rs.next()) {
				emp = new Employee_n(rs.getLong("autoindex"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						null);
			}
			return emp;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateEmployee(long index, Employee employee, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_EMPLOYEE
					+ " SET " + IDBConstants.ATTR_EMPLOYEE_NO + "=?, "
					+ IDBConstants.ATTR_FIRST_NAME + "=?, "
					+ IDBConstants.ATTR_MIDLE_NAME + "=?, "
					+ IDBConstants.ATTR_LAST_NAME + "=?, "
					+ IDBConstants.ATTR_NICK_NAME + "=?, "
					+ IDBConstants.ATTR_BIRTH_PLACE + "=?, "
					+ IDBConstants.ATTR_BIRTH_DATE + "=?, "
					+ IDBConstants.ATTR_SEX + "=?, "
					+ IDBConstants.ATTR_RELIGION + "=?, "
					+ IDBConstants.ATTR_NATIONALITY + "=?, "
					+ IDBConstants.ATTR_MARITAL + "=?, "
					+ IDBConstants.ATTR_ART_21 + "=?, "
					+ IDBConstants.ATTR_ADDRESS + "=?, "
					+ IDBConstants.ATTR_CITY + "=?, "
					+ IDBConstants.ATTR_POST_CODE + "=?,"
					+ IDBConstants.ATTR_PROVINCE + "=?, "
					+ IDBConstants.ATTR_COUNTRY + "=?, "
					+ IDBConstants.ATTR_PHONE + "=?, "
					+ IDBConstants.ATTR_MOBILE_PHONE1 + "=?, "
					+ IDBConstants.ATTR_MOBILE_PHONE2 + "=?, "
					+ IDBConstants.ATTR_FAX + "=?, " + IDBConstants.ATTR_EMAIL
					+ "=? " + " WHERE " + IDBConstants.ATTR_AUTOINDEX + "="
					+ index);
			int col = 1;
			stm.setString(col++, employee.getEmployeeNo());
			stm.setString(col++, employee.getFirstName());
			stm.setString(col++, employee.getMidleName());
			stm.setString(col++, employee.getLastName());
			stm.setString(col++, employee.getNickName());
			stm.setString(col++, employee.getBirthPlace());
			if (employee.getBirthDate() != null)
				stm.setDate(col++, new java.sql.Date(employee.getBirthDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setLong(col++, employee.getNSex());
			if (employee.getNReligion() != 0)
				stm.setLong(col++, employee.getNReligion());
			else
				stm.setNull(col++, Types.NULL);
			stm.setString(col++, employee.getNationality());
			if (employee.getNMarital() != 0)
				stm.setLong(col++, employee.getNMarital());
			else
				stm.setNull(col++, Types.NULL);
			stm.setLong(col++, employee.getNArt21());
			stm.setString(col++, employee.getAddress());
			stm.setString(col++, employee.getCity());
			stm.setInt(col++, employee.getPostCode());
			stm.setString(col++, employee.getProvince());
			stm.setString(col++, employee.getCountry());
			stm.setString(col++, employee.getPhone());
			stm.setString(col++, employee.getMobilePhone1());
			stm.setString(col++, employee.getMobilePhone2());
			stm.setString(col++, employee.getFax());
			stm.setString(col++, employee.getEmail());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployee(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EMPLOYEE
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee[] getEmployeeByCriteria(String query, Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(
						new Employee(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
								rs.getString(IDBConstants.ATTR_EMPLOYEE_NO),
								rs.getString(IDBConstants.ATTR_FIRST_NAME),
								rs.getString(IDBConstants.ATTR_MIDLE_NAME),
								rs.getString(IDBConstants.ATTR_LAST_NAME),
								rs.getString(IDBConstants.ATTR_NICK_NAME),
								rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
								rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
								rs.getLong(IDBConstants.ATTR_SEX),
								rs.getLong(IDBConstants.ATTR_RELIGION),
								rs.getString(IDBConstants.ATTR_NATIONALITY),
								rs.getLong(IDBConstants.ATTR_MARITAL),
								rs.getLong(IDBConstants.ATTR_ART_21),
								rs.getString(IDBConstants.ATTR_ADDRESS),
								rs.getString(IDBConstants.ATTR_CITY),
								rs.getInt(IDBConstants.ATTR_POST_CODE),
								rs.getString(IDBConstants.ATTR_PROVINCE),
								rs.getString(IDBConstants.ATTR_COUNTRY),
								rs.getString(IDBConstants.ATTR_PHONE),
								rs.getString(IDBConstants.ATTR_MOBILE_PHONE1),
								rs.getString(IDBConstants.ATTR_MOBILE_PHONE2),
								rs.getString(IDBConstants.ATTR_FAX),
								rs.getString(IDBConstants.ATTR_EMAIL)));
			}
			Employee[] result = new Employee[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeQualification(long employeeindex, long quaindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_QUALIFICATION + " values ("
					+ employeeindex + "," + quaindex + ")");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Qualification[] getEmployeeQualification(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT "
					+ IDBConstants.TABLE_QUALIFICATION + ".* FROM "
					+ IDBConstants.TABLE_QUALIFICATION + ","
					+ IDBConstants.TABLE_EMPLOYEE_QUALIFICATION + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex
					+ " AND " + IDBConstants.ATTR_QUALIFICATION + "="
					+ IDBConstants.TABLE_QUALIFICATION + "."
					+ IDBConstants.ATTR_AUTOINDEX);
			while (rs.next()) {
				vresult.addElement(new Qualification(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_NAME), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			Qualification[] result = new Qualification[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeQualification(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMPLOYEE_QUALIFICATION + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeEmployment(long employeindex, Employment employ,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + "("
					+ IDBConstants.ATTR_EMPLOYEE + ", "
					+ IDBConstants.ATTR_JOB_TITLE + ", "
					+ IDBConstants.ATTR_DEPARTMENT + ", "
					+ IDBConstants.ATTR_UNIT + ", "
					+ IDBConstants.ATTR_REFERENCE_NO + ", "
					+ IDBConstants.ATTR_REFERENCE_DATE + ", "
					+ IDBConstants.ATTR_TMT + ", " + IDBConstants.ATTR_END_DATE
					+ ", " + IDBConstants.ATTR_WORK_AGREEMENT + ", "
					+ IDBConstants.ATTR_DESCRIPTION + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeindex);
			stm.setLong(col++, employ.getJobTitle().getIndex());
			stm.setLong(col++, employ.getDepartment().getIndex());
			stm.setLong(col++, employ.getUnit().getIndex());
			stm.setString(col++, employ.getReferenceNo());
			if (employ.getReferenceDate() != null)
				stm.setDate(col++, new java.sql.Date(employ.getReferenceDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (employ.getTMT() != null)
				stm.setDate(col++, new java.sql.Date(employ.getTMT().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (employ.getEndDate() != null)
				stm.setDate(col++, new java.sql.Date(employ.getEndDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);

			stm.setLong(col++, employ.getWorkAgreement().getIndex());
			stm.setString(col++, employ.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employment[] getEmployeeEmployment(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			String query = "SELECT * FROM "
				+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " WHERE "
				+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex;
                        System.out.println(query);
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(
						new Employment(
								this.getJobTitle(rs.getLong(IDBConstants.ATTR_JOB_TITLE), conn),
								this.getOrganizationByIndex(rs.getLong(IDBConstants.ATTR_DEPARTMENT), conn),
								new AccountingSQLSAP().getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
								rs.getString(IDBConstants.ATTR_REFERENCE_NO),
								rs.getDate(IDBConstants.ATTR_REFERENCE_DATE),
								rs.getDate(IDBConstants.ATTR_TMT),
								rs.getDate(IDBConstants.ATTR_END_DATE),
								this.getWorkAgreement(rs.getLong(IDBConstants.ATTR_WORK_AGREEMENT),conn),
								rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			Employment[] result = new Employment[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployee getEmpReceivableReportByUnit(Connection conn,String query) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {

				return new SimpleEmployee(rs.getLong("autoindex"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getString("jobtitle"),
						rs.getString("department"));
			}
			return null;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}


	public void deleteEmployeeEmployment(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeEducation(long employeindex,
			EmployeeEducation edu, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_EDUCATION + "("
					+ IDBConstants.ATTR_EMPLOYEE + ", "
					+ IDBConstants.ATTR_GRADE + ", "
					+ IDBConstants.ATTR_MAJOR_STUDY + ", "
					+ IDBConstants.ATTR_INSTITUE + ", "
					+ IDBConstants.ATTR_FROM + ", " + IDBConstants.ATTR_TO
					+ ", " + IDBConstants.ATTR_GPA + ", "
					+ IDBConstants.ATTR_MAX_GPA + ", "
					+ IDBConstants.ATTR_DESCRIPTION + ", "
					+ IDBConstants.ATTR_FILE + ", "
					+ IDBConstants.ATTR_CERTIFICATE + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeindex);
			stm.setLong(col++, edu.getGrade().getIndex());
			stm.setString(col++, edu.getMajorStudy());
			stm.setString(col++, edu.getInstitute());
			if (edu.getFrom() != null)
				stm.setDate(col++, new java.sql.Date(edu.getFrom().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (edu.getTo() != null)
				stm.setDate(col++, new java.sql.Date(edu.getTo().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setFloat(col++, edu.getGPA());
			stm.setFloat(col++, edu.getMaxGPA());
			stm.setString(col++, edu.getDescription());
			stm.setString(col++, edu.getFile());
			if (edu.getCertificate() == null
					|| edu.getCertificate().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, edu.getCertificate());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeEducation[] getEmployeeEducation(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_EDUCATION + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
			while (rs.next()) {
				vresult.addElement(new EmployeeEducation(this.getEducation(rs
						.getLong(IDBConstants.ATTR_GRADE), conn), rs
						.getString(IDBConstants.ATTR_MAJOR_STUDY), rs
						.getString(IDBConstants.ATTR_INSTITUE), rs
						.getDate(IDBConstants.ATTR_FROM), rs
						.getDate(IDBConstants.ATTR_TO), rs
						.getFloat(IDBConstants.ATTR_GPA), rs
						.getFloat(IDBConstants.ATTR_MAX_GPA), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getString(IDBConstants.ATTR_FILE), rs
						.getBytes(IDBConstants.ATTR_CERTIFICATE)));
			}
			EmployeeEducation[] result = new EmployeeEducation[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeEducation(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EMPLOYEE_EDUCATION
					+ " WHERE " + IDBConstants.ATTR_EMPLOYEE + "="
					+ employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeCertification(long employeindex,
			Certification certificate, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_CERTIFICATION + "("
					+ IDBConstants.ATTR_EMPLOYEE + ", "
					+ IDBConstants.ATTR_CERTIFICATE_NO + ", "
					+ IDBConstants.ATTR_CERTIFICATE_DATE + ", "
					+ IDBConstants.ATTR_INSTITUE + ", "
					+ IDBConstants.ATTR_QUALIFICATION + ", "
					+ IDBConstants.ATTR_DESCRIPTION + ", "
					+ IDBConstants.ATTR_START_DATE + ", "
					+ IDBConstants.ATTR_END_DATE + ", "
					+ IDBConstants.ATTR_EXPIRE_DATE + ", "
					+ IDBConstants.ATTR_RESULT + ", " + IDBConstants.ATTR_FILE
					+ ", " + IDBConstants.ATTR_CERTIFICATE + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeindex);
			stm.setString(col++, certificate.getNo());
			if (certificate.getDate() != null)
				stm.setDate(col++, new java.sql.Date(certificate.getDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, certificate.getInstitute());
			stm.setLong(col++, certificate.getQualification().getIndex());
			stm.setString(col++, certificate.getDescription());
			if (certificate.getStartDate() != null)
				stm.setDate(col++, new java.sql.Date(certificate.getStartDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (certificate.getEndDate() != null)
				stm.setDate(col++, new java.sql.Date(certificate.getEndDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (certificate.getExpireDate() != null)
				stm.setDate(col++, new java.sql.Date(certificate.getExpireDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, certificate.getResult());
			stm.setString(col++, certificate.getFile());
			if (certificate.getCertificate() == null || certificate.getCertificate().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, certificate.getCertificate());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Certification[] getEmployeeCertification(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_CERTIFICATION + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
			while (rs.next()) {
				vresult.addElement(new Certification(
						rs.getString(IDBConstants.ATTR_CERTIFICATE_NO),
						rs.getDate(IDBConstants.ATTR_CERTIFICATE_DATE),
						rs.getString(IDBConstants.ATTR_INSTITUE),
						this.getQualification(
								rs.getLong(IDBConstants.ATTR_QUALIFICATION),conn),
								rs.getString(IDBConstants.ATTR_DESCRIPTION),
								rs.getDate(IDBConstants.ATTR_START_DATE),
								rs.getDate(IDBConstants.ATTR_END_DATE),
								rs.getDate(IDBConstants.ATTR_EXPIRE_DATE),
								rs.getString(IDBConstants.ATTR_RESULT),
								rs.getString(IDBConstants.ATTR_FILE),
								rs.getBytes(IDBConstants.ATTR_CERTIFICATE)));
			}
			Certification[] result = new Certification[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeCertification(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMPLOYEE_CERTIFICATION + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeFamily(long employeeindex, EmployeeFamily family,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_FAMILY + "("
					+ IDBConstants.ATTR_EMPLOYEE + ","
					+ IDBConstants.ATTR_RELATION + ","
					+ IDBConstants.ATTR_NAME+ ","
					+ IDBConstants.ATTR_BIRTH_PLACE + ","
					+ IDBConstants.ATTR_BIRTH_DATE + ","
					+ IDBConstants.ATTR_EDUCATION + ","
					+ IDBConstants.ATTR_JOB_TITLE + ","
					+ IDBConstants.ATTR_COMPANY + ","
					+ IDBConstants.ATTR_REMARK + ")"
					+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeeindex);
			stm.setLong(col++, family.getRelation().getIndex());
			stm.setString(col++, family.getName());
			stm.setString(col++, family.getBirthPlace());
			if (family.getBirthDate() != null)
				stm.setDate(col++, new java.sql.Date(family.getBirthDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setLong(col++, family.getEducation().getIndex());
			stm.setString(col++, family.getJobTitle());
			stm.setString(col++, family.getCompany());
			stm.setString(col++, family.getRemark());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeFamily[] getEmployeeFamily(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_FAMILY + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
			while (rs.next()) {
				vresult.addElement(new EmployeeFamily(this.getFamilyRelation(rs
						.getLong(IDBConstants.ATTR_RELATION), conn), rs
						.getString(IDBConstants.ATTR_NAME), rs
						.getString(IDBConstants.ATTR_BIRTH_PLACE), rs
						.getDate(IDBConstants.ATTR_BIRTH_DATE), this
						.getEducation(rs.getLong(IDBConstants.ATTR_EDUCATION),
								conn), rs
								.getString(IDBConstants.ATTR_JOB_TITLE), rs
								.getString(IDBConstants.ATTR_COMPANY), rs
								.getString(IDBConstants.ATTR_REMARK)));
			}
			EmployeeFamily[] result = new EmployeeFamily[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeFamily(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EMPLOYEE_FAMILY
					+ " WHERE " + IDBConstants.ATTR_EMPLOYEE + "="
					+ employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeAccount(long employeindex,
			EmployeeAccount account, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_ACCOUNT + "("
					+ IDBConstants.ATTR_EMPLOYEE + ", "
					+ IDBConstants.ATTR_ACCOUNT_NAME + ", "
					+ IDBConstants.ATTR_ACCOUNT_NO + ", "
					+ IDBConstants.ATTR_BANK_NAME + ", "
					+ IDBConstants.ATTR_BANK_ADDRESS + ", "
					+ IDBConstants.ATTR_CURRENCY + ", "
					+ IDBConstants.ATTR_REMARK + ") "
					+ " values (?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeindex);
			stm.setString(col++, account.getAccountName());
			stm.setString(col++, account.getAccountNo());
			stm.setString(col++, account.getBankName());
			stm.setString(col++, account.getBankAddress());
			stm.setLong(col++, account.getCurrency().getIndex());
			stm.setString(col++, account.getRemark());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeAccount[] getEmployeeAccount(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			String query = "SELECT * FROM "
				+ IDBConstants.TABLE_EMPLOYEE_ACCOUNT + " WHERE "
				+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex;
			System.err.println(query);
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(new EmployeeAccount(rs
						.getString(IDBConstants.ATTR_ACCOUNT_NAME), rs
						.getString(IDBConstants.ATTR_ACCOUNT_NO), rs
						.getString(IDBConstants.ATTR_BANK_NAME), rs
						.getString(IDBConstants.ATTR_BANK_ADDRESS),
						new AccountingSQLSAP().getCurrency(rs
								.getLong(IDBConstants.ATTR_CURRENCY), conn), rs
								.getString(IDBConstants.ATTR_REMARK)));
			}
			EmployeeAccount[] result = new EmployeeAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeAccount(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EMPLOYEE_ACCOUNT
					+ " WHERE " + IDBConstants.ATTR_EMPLOYEE + "="
					+ employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeRetirement(long employeindex,
			Retirement retirement, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
//			stm = conn.prepareStatement("INSERT INTO "
//			+ IDBConstants.TABLE_EMPLOYEE_RETIREMENT + "("
//			+ IDBConstants.ATTR_EMPLOYEE + ", "
//			+ IDBConstants.ATTR_RETIREMENT_REFERENCE + ", "
//			+ IDBConstants.ATTR_RETIREMENT_DATE + ", "
//			+ IDBConstants.ATTR_REASON + ", "
//			+ IDBConstants.ATTR_REMARK + ", " + IDBConstants.ATTR_TMT
//			+ ", " + IDBConstants.ATTR_RETIREMENT_STATUS + ") "
//			+ " values (?, ?, ?, ?, ?, ?, ?)");
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_RETIREMENT + "("
					+ IDBConstants.ATTR_EMPLOYEE + ", "
					+ IDBConstants.ATTR_RETIREMENT_REFERENCE + ", "
					+ IDBConstants.ATTR_RETIREMENT_DATE + ", "
					+ IDBConstants.ATTR_REASON + ", "
					+ IDBConstants.ATTR_REMARK + ", " + IDBConstants.ATTR_TMT
					+ ") "
					+ " values (?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeindex);
			stm.setString(col++, retirement.getRetirementReference());
			if (retirement.getRetirementDate() != null)
				stm.setDate(col++, new java.sql.Date(retirement
						.getRetirementDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, retirement.getReason());
			stm.setString(col++, retirement.getRemarks());
			if (retirement.getTMT() != null)
				stm.setDate(col++, new java.sql.Date(retirement.getTMT()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			//stm.setShort(col++, retirement.getStatus());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Retirement getEmployeeRetirement(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_RETIREMENT + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
			if (rs.next()) {
				return new Retirement(rs
						.getString(IDBConstants.ATTR_RETIREMENT_REFERENCE), rs
						.getDate(IDBConstants.ATTR_RETIREMENT_DATE), rs
						.getString(IDBConstants.ATTR_REASON), rs
						.getString(IDBConstants.ATTR_REMARK), rs
						.getDate(IDBConstants.ATTR_TMT), rs
						.getShort(IDBConstants.ATTR_RETIREMENT_STATUS));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeRetirement(long employeeindex, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMPLOYEE_RETIREMENT + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createDefaultWorkingDay(DefaultWorkingDay day, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_DEFAULT_WORKING_DAY + "("
					+ IDBConstants.ATTR_TYPE + " ,"
					+ IDBConstants.ATTR_MONDAY+ ", "
					+ IDBConstants.ATTR_TUESDAY + ", "
					+ IDBConstants.ATTR_WEDNESDAY + ", "
					+ IDBConstants.ATTR_THURSDAY + ", "
					+ IDBConstants.ATTR_FRIDAY + ", "
					+ IDBConstants.ATTR_SATURDAY + ", "
					+ IDBConstants.ATTR_SUNDAY + ") "
					+ " values (?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setShort(col++, day.getType());
			stm.setBoolean(col++, day.getMonday());
			stm.setBoolean(col++, day.getTuesday());
			stm.setBoolean(col++, day.getWednesday());
			stm.setBoolean(col++, day.getThursday());
			stm.setBoolean(col++, day.getFriday());
			stm.setBoolean(col++, day.getSaturday());
			stm.setBoolean(col++, day.getSunday());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public DefaultWorkingDay getDefaultWorkingDay(Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_DEFAULT_WORKING_DAY);
			if (rs.next())
				return new DefaultWorkingDay(rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getBoolean(IDBConstants.ATTR_MONDAY), rs
						.getBoolean(IDBConstants.ATTR_TUESDAY), rs
						.getBoolean(IDBConstants.ATTR_WEDNESDAY), rs
						.getBoolean(IDBConstants.ATTR_THURSDAY), rs
						.getBoolean(IDBConstants.ATTR_FRIDAY), rs
						.getBoolean(IDBConstants.ATTR_SATURDAY), rs
						.getBoolean(IDBConstants.ATTR_SUNDAY));
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteDefaultWorkingDay(Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_DEFAULT_WORKING_DAY);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createDefaultWorkingTime(DefaultWorkingTime time,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_DEFAULT_WORKING_TIME + "("
					+ IDBConstants.ATTR_TYPE + " ," + IDBConstants.ATTR_FROM
					+ ", " + IDBConstants.ATTR_TO + ") " + " values (?, ?, ?)");
			int col = 1;
			stm.setShort(col++, time.getType());
			stm.setTime(col++, time.getFrom());
			stm.setTime(col++, time.getTo());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public DefaultWorkingTime getDefaultWorkingTime(Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_DEFAULT_WORKING_TIME);
			if (rs.next())
				return new DefaultWorkingTime(rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getTime(IDBConstants.ATTR_FROM), rs
						.getTime(IDBConstants.ATTR_TO));
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteDefaultWorkingTime(Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_DEFAULT_WORKING_TIME);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createOvertimeMultiplier(OvertimeMultiplier multiplier,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_OVERTIME_MULTIPLIER + "("
					+ IDBConstants.ATTR_TYPE + "," + IDBConstants.ATTR_HOUR_MIN
					+ "," + IDBConstants.ATTR_HOUR_MAX + ","
					+ IDBConstants.ATTR_MULTIPLIER + ")"
					+ " values (?, ?, ?, ?)");
			stm.setShort(1, multiplier.getType());
			stm.setInt(2, multiplier.getHourMin());
			stm.setInt(3, multiplier.getHourMax());
			stm.setFloat(4, multiplier.getMultiplier());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public OvertimeMultiplier[] getAllOvertimeMultiplier(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_OVERTIME_MULTIPLIER);
			while (rs.next()) {
				vresult.addElement(new OvertimeMultiplier(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getInt(IDBConstants.ATTR_HOUR_MIN), rs
						.getInt(IDBConstants.ATTR_HOUR_MAX), rs
						.getFloat(IDBConstants.ATTR_MULTIPLIER)));
			}
			OvertimeMultiplier[] result = new OvertimeMultiplier[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public OvertimeMultiplier[] getAllOvertimeMultiplierOrdered(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_OVERTIME_MULTIPLIER
					+ " ORDER BY "
					+ IDBConstants.ATTR_TYPE + ", "
					+ IDBConstants.ATTR_HOUR_MIN);
			while (rs.next()) {
				vresult.addElement(new OvertimeMultiplier(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getInt(IDBConstants.ATTR_HOUR_MIN), rs
						.getInt(IDBConstants.ATTR_HOUR_MAX), rs
						.getFloat(IDBConstants.ATTR_MULTIPLIER)));
			}
			OvertimeMultiplier[] result = new OvertimeMultiplier[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public OvertimeMultiplier getOvertimeMultiplier(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_OVERTIME_MULTIPLIER + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new OvertimeMultiplier(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getInt(IDBConstants.ATTR_HOUR_MIN), rs
						.getInt(IDBConstants.ATTR_HOUR_MAX), rs
						.getFloat(IDBConstants.ATTR_MULTIPLIER));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateOvertimeMultiplier(long index,
			OvertimeMultiplier multiplier, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_OVERTIME_MULTIPLIER + " SET "
					+ IDBConstants.ATTR_TYPE + "=?, "
					+ IDBConstants.ATTR_HOUR_MIN + "=?, "
					+ IDBConstants.ATTR_HOUR_MAX + "=?, "
					+ IDBConstants.ATTR_MULTIPLIER + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setShort(1, multiplier.getType());
			stm.setInt(2, multiplier.getHourMin());
			stm.setInt(3, multiplier.getHourMax());
			stm.setFloat(4, multiplier.getMultiplier());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteOvertimeMultiplier(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_OVERTIME_MULTIPLIER + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPaychequeLabel(PaychequeLabel label, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL + "("
					+ IDBConstants.ATTR_LABEL + "," + IDBConstants.ATTR_TYPE
					+ "," + IDBConstants.ATTR_SHOW + ")" + " values (?, ?, ?)");
			stm.setString(1, label.getLabel());
			stm.setShort(2, label.getType());
			stm.setBoolean(3, label.isShow());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPaychequeLabelStructure(long superlabel, long sublabel,
			Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE + " values("
					+ superlabel + "," + sublabel + ")");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PaychequeLabel getPaychequeLabelByIndex(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if (rs.next()) {
				return new PaychequeLabel(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_LABEL), rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getBoolean(IDBConstants.ATTR_SHOW));
			}
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}

	public PaychequeLabel[] getSuperPaychequeLabel(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + " NOT IN (SELECT "
					+ IDBConstants.ATTR_SUB_LABEL + " FROM "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE + ")");
			while (rs.next()) {
				vresult.addElement(new PaychequeLabel(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_LABEL), rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getBoolean(IDBConstants.ATTR_SHOW)));
			}
			PaychequeLabel[] result = new PaychequeLabel[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PaychequeLabel[] getSubPaychequeLabel(long superlabel,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL + ".*  FROM "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL + ", "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE + " WHERE "
					+ IDBConstants.ATTR_SUPER_LABEL + "=" + superlabel
					+ " AND " + IDBConstants.ATTR_SUB_LABEL + "="
					+ IDBConstants.ATTR_AUTOINDEX);
			while (rs.next()) {
				vresult.addElement(new PaychequeLabel(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_LABEL), rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getBoolean(IDBConstants.ATTR_SHOW)));
			}
			PaychequeLabel[] result = new PaychequeLabel[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updatePaychequeLabel(long index, PaychequeLabel label,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_PAYCHEQUE_LABEL + " SET "
					+ IDBConstants.ATTR_LABEL + "=?, " + IDBConstants.ATTR_TYPE
					+ "=?, " + IDBConstants.ATTR_SHOW + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			stm.setString(1, label.getLabel());
			stm.setShort(2, label.getType());
			stm.setBoolean(3, label.isShow());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deletePaychequeLabel(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PAYCHEQUE_LABEL
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeOfficeHourPermition(long employeeindex,
			EmployeeOfficeHourPermition reason, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION + "("
					+ IDBConstants.ATTR_EMPLOYEE + ", "
					+ IDBConstants.ATTR_PROPOSE_DATE + ", "
					+ IDBConstants.ATTR_PERMISSION_DATE + ", "
					+ IDBConstants.ATTR_FROM + ", "
					+ IDBConstants.ATTR_TO+ ", "
					+ IDBConstants.ATTR_REASON + ", "
					+ IDBConstants.ATTR_CHECKED + ", "
					+ IDBConstants.ATTR_CHECKED_DATE + ", "
					+ IDBConstants.ATTR_APPROVED + ", "
					+ IDBConstants.ATTR_APPROVED_DATE + ", "
					+ IDBConstants.ATTR_DESCRIPTION + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeeindex);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getPermissionDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPermissionDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setTime(col++, reason.getFrom());
			stm.setTime(col++, reason.getTo());
			stm.setLong(col++, reason.getReason().getIndex());
			if (reason.getChecked() != null)
				stm.setLong(col++, reason.getChecked().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getCheckedDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getCheckedDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getApproved() != null)
				stm.setLong(col++, reason.getApproved().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getApprovedDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getApprovedDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, reason.getDesciption());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeOfficeHourPermition[] getAllEmployeeOfficeHourPermition(
			long employeeindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
			while (rs.next()) {
				vresult.addElement(new EmployeeOfficeHourPermition(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_PROPOSE_DATE),
						rs.getDate(IDBConstants.ATTR_PERMISSION_DATE),
						rs.getTime(IDBConstants.ATTR_FROM),
						rs.getTime(IDBConstants.ATTR_TO),
						this.getOfficeHourPermition(rs.getLong(IDBConstants.ATTR_REASON), conn),
						this.getEmployeeByIndex(
								rs.getLong(IDBConstants.ATTR_CHECKED), conn),
								rs.getDate(IDBConstants.ATTR_CHECKED_DATE),
						this.getEmployeeByIndex(
								rs.getLong(IDBConstants.ATTR_APPROVED), conn),
								rs.getDate(IDBConstants.ATTR_APPROVED_DATE),
								rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			EmployeeOfficeHourPermition[] result = new EmployeeOfficeHourPermition[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeOfficeHourPermition[] getAllEmployeeOfficeHourPermition(
			String query, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(
						new EmployeeOfficeHourPermition(
								rs.getLong(IDBConstants.ATTR_AUTOINDEX),
								rs.getDate(IDBConstants.ATTR_PROPOSE_DATE),
								rs.getDate(IDBConstants.ATTR_PERMISSION_DATE),
								rs.getTime(IDBConstants.ATTR_FROM),
								rs.getTime(IDBConstants.ATTR_TO),
								this.getOfficeHourPermition(
										rs.getLong(IDBConstants.ATTR_REASON), conn),
										this.getEmployeeByIndex(
												rs.getLong(IDBConstants.ATTR_CHECKED), conn),
												rs.getDate(IDBConstants.ATTR_CHECKED_DATE),
												this.getEmployeeByIndex(
														rs.getLong(IDBConstants.ATTR_APPROVED), conn),
														rs.getDate(IDBConstants.ATTR_APPROVED_DATE),
														rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}
			EmployeeOfficeHourPermition[] result = new EmployeeOfficeHourPermition[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateEmployeeOfficeHourPermition(long index,
			EmployeeOfficeHourPermition reason, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION + " SET "
					+ IDBConstants.ATTR_PROPOSE_DATE + "=?, "
					+ IDBConstants.ATTR_PERMISSION_DATE + "=?, "
					+ IDBConstants.ATTR_FROM + "=?, "
					+ IDBConstants.ATTR_TO+ "=?, "
					+ IDBConstants.ATTR_REASON + "=?, "
					+ IDBConstants.ATTR_CHECKED + "=?, "
					+ IDBConstants.ATTR_CHECKED_DATE + "=?, "
					+ IDBConstants.ATTR_APPROVED + "=?, "
					+ IDBConstants.ATTR_APPROVED_DATE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
			int col = 1;
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getPermissionDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPermissionDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setTime(col++, reason.getFrom());
			stm.setTime(col++, reason.getTo());
			stm.setLong(col++, reason.getReason().getIndex());
			if (reason.getChecked() != null)
				stm.setLong(col++, reason.getChecked().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getCheckedDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getCheckedDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getApproved() != null)
				stm.setLong(col++, reason.getApproved().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, reason.getDesciption());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeOfficeHourPermition(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeeLeave(long employeeindex,
			EmployeeLeavePermition reason, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_EMPLOYEE_LEAVE + "("
					+ IDBConstants.ATTR_EMPLOYEE + ", " + IDBConstants.ATTR_PROPOSE_DATE + ", "
					+ IDBConstants.ATTR_FROM + ", " + IDBConstants.ATTR_TO + ", "
					+ IDBConstants.ATTR_DAYS + ", " + IDBConstants.ATTR_REASON + ", "
					+ IDBConstants.ATTR_ADDRESS + ", " + IDBConstants.ATTR_PHONE + ", "
					+ IDBConstants.ATTR_REPLACED + ", " + IDBConstants.ATTR_CHECKED + ", "
					+ IDBConstants.ATTR_CHECKED_DATE + ", " + IDBConstants.ATTR_APPROVED + ", "
					+ IDBConstants.ATTR_APPROVED_DATE + ", " + IDBConstants.ATTR_DESCRIPTION + ", "
					+ IDBConstants.ATTR_IS_REFERENCE + ", " + IDBConstants.ATTR_FILE + ", "
					+ IDBConstants.ATTR_REFERENCE + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setLong(col++, employeeindex);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getFrom() != null)
				stm.setDate(col++,
						new java.sql.Date(reason.getFrom().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getTo() != null)
				stm.setDate(col++, new java.sql.Date(reason.getTo().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setInt(col++, reason.getDays());
			stm.setLong(col++, reason.getReason());
			stm.setString(col++, reason.getAddress());
			stm.setString(col++, reason.getPhone());
			if (reason.getReplaced() != 0)
				stm.setLong(col++, reason.getReplaced());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getChecked() != 0)
				stm.setLong(col++, reason.getChecked());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getCheckedDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getCheckedDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getApproved() != 0)
				stm.setLong(col++, reason.getApproved());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, reason.getDescription());
			stm.setBoolean(col++, reason.isReference());
			stm.setString(col++, reason.getFile());
			if (reason.getReference() == null || reason.getReference().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, reason.getReference());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	//public getSampedid
	public EmployeeLeavePermition[] getAllEmployeeLeave(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_LEAVE + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
			while (rs.next()) {
				vresult.addElement(new EmployeeLeavePermition(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX),
						(short) 0, rs
						.getDate(IDBConstants.ATTR_PROPOSE_DATE), rs
						.getDate(IDBConstants.ATTR_FROM), rs
						.getDate(IDBConstants.ATTR_TO), rs
						.getInt(IDBConstants.ATTR_DAYS), rs
						.getLong(IDBConstants.ATTR_REASON), rs
						.getString(IDBConstants.ATTR_ADDRESS), rs
						.getString(IDBConstants.ATTR_PHONE), rs
						.getLong(IDBConstants.ATTR_REPLACED), rs
						.getLong(IDBConstants.ATTR_CHECKED), rs
						.getDate(IDBConstants.ATTR_CHECKED_DATE), rs
						.getLong(IDBConstants.ATTR_APPROVED), rs
						.getDate(IDBConstants.ATTR_APPROVED_DATE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_IS_REFERENCE), rs
						.getString(IDBConstants.ATTR_FILE), rs
						.getBytes(IDBConstants.ATTR_REFERENCE)));

			}

			EmployeeLeavePermition[] result = new EmployeeLeavePermition[vresult
			                                                             .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateEmployeeLeave(long index, EmployeeLeavePermition reason,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_LEAVE + " SET "
					+ IDBConstants.ATTR_PROPOSE_DATE + "=?, "
					+ IDBConstants.ATTR_FROM + "=?, " + IDBConstants.ATTR_TO
					+ "=?, " + IDBConstants.ATTR_DAYS + "=?, "
					+ IDBConstants.ATTR_REASON + "=?, "
					+ IDBConstants.ATTR_ADDRESS + "=?, "
					+ IDBConstants.ATTR_PHONE + "=?, "
					+ IDBConstants.ATTR_REPLACED + "=?, "
					+ IDBConstants.ATTR_CHECKED + "=?, "
					+ IDBConstants.ATTR_CHECKED_DATE + "=?, "
					+ IDBConstants.ATTR_APPROVED + "=?, "
					+ IDBConstants.ATTR_APPROVED_DATE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=?, "
					+ IDBConstants.ATTR_IS_REFERENCE + "=?, "
					+ IDBConstants.ATTR_FILE + "=?, "
					+ IDBConstants.ATTR_REFERENCE + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			int col = 1;
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getFrom() != null)
				stm.setDate(col++,
						new java.sql.Date(reason.getFrom().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getTo() != null)
				stm.setDate(col++, new java.sql.Date(reason.getTo().getTime()));
			else
				stm.setNull(col++, Types.DATE);

			stm.setInt(col++, reason.getDays());
			stm.setLong(col++, reason.getReason());
			stm.setString(col++, reason.getAddress());
			stm.setString(col++, reason.getPhone());

			if (reason.getReplaced() != 0)
				stm.setLong(col++, reason.getReplaced());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getChecked() != 0)
				stm.setLong(col++, reason.getChecked());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getCheckedDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getCheckedDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getApproved() != 0)
				stm.setLong(col++, reason.getApproved());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, reason.getDescription());
			stm.setBoolean(col++, reason.isReference());
			stm.setString(col++, reason.getFile());

			if (reason.getReference() == null
					|| reason.getReference().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, reason.getReference());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeeLeave(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EMPLOYEE_LEAVE
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeePermition(long employeeindex,
			EmployeeLeavePermition reason, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn
			.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_PERMITION
					+ "("
					+ IDBConstants.ATTR_EMPLOYEE
					+ ", "
					+ IDBConstants.ATTR_PROPOSE_DATE
					+ ", "
					+ IDBConstants.ATTR_FROM
					+ ", "
					+ IDBConstants.ATTR_TO
					+ ", "
					+ IDBConstants.ATTR_DAYS
					+ ", "
					+ IDBConstants.ATTR_REASON
					+ ", "
					+ IDBConstants.ATTR_ADDRESS
					+ ", "
					+ IDBConstants.ATTR_PHONE
					+ ", "
					+ IDBConstants.ATTR_REPLACED
					+ ", "
					+ IDBConstants.ATTR_CHECKED
					+ ", "
					+ IDBConstants.ATTR_CHECKED_DATE
					+ ", "
					+ IDBConstants.ATTR_APPROVED
					+ ", "
					+ IDBConstants.ATTR_APPROVED_DATE
					+ ", "
					+ IDBConstants.ATTR_DESCRIPTION
					+ ", "
					+ IDBConstants.ATTR_IS_REFERENCE
					+ ", "
					+ IDBConstants.ATTR_FILE
					+ ", "
					+ IDBConstants.ATTR_REFERENCE
					+ ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			int col = 1;
			stm.setLong(col++, employeeindex);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getFrom() != null)
				stm.setDate(col++,
						new java.sql.Date(reason.getFrom().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getTo() != null)
				stm.setDate(col++, new java.sql.Date(reason.getTo().getTime()));
			else
				stm.setNull(col++, Types.DATE);

			stm.setInt(col++, reason.getDays());
			stm.setLong(col++, reason.getReason());
			stm.setString(col++, reason.getAddress());
			stm.setString(col++, reason.getPhone());

			if (reason.getReplaced() != 0)
				stm.setLong(col++, reason.getReplaced());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getChecked() != 0)
				stm.setLong(col++, reason.getChecked());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getCheckedDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getCheckedDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getApproved() != 0)
				stm.setLong(col++, reason.getApproved());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, reason.getDescription());
			stm.setBoolean(col++, reason.isReference());
			stm.setString(col++, reason.getFile());
			if (reason.getReference() == null
					|| reason.getReference().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, reason.getReference());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeLeavePermition[] getAllEmployeePermition(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_PERMITION + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);

			while (rs.next()) {
				vresult.addElement(new EmployeeLeavePermition(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX),
						(short) 1, rs
						.getDate(IDBConstants.ATTR_PROPOSE_DATE), rs
						.getDate(IDBConstants.ATTR_FROM), rs
						.getDate(IDBConstants.ATTR_TO), rs
						.getInt(IDBConstants.ATTR_DAYS), rs
						.getLong(IDBConstants.ATTR_REASON), rs
						.getString(IDBConstants.ATTR_ADDRESS), rs
						.getString(IDBConstants.ATTR_PHONE), rs
						.getLong(IDBConstants.ATTR_REPLACED), rs
						.getLong(IDBConstants.ATTR_CHECKED), rs
						.getDate(IDBConstants.ATTR_CHECKED_DATE), rs
						.getLong(IDBConstants.ATTR_APPROVED), rs
						.getDate(IDBConstants.ATTR_APPROVED_DATE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_IS_REFERENCE), rs
						.getString(IDBConstants.ATTR_FILE), rs
						.getBytes(IDBConstants.ATTR_REFERENCE)));
			}

			EmployeeLeavePermition[] result = new EmployeeLeavePermition[vresult
			                                                             .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateEmployeePermition(long index,
			EmployeeLeavePermition reason, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_PERMITION + " SET "
					+ IDBConstants.ATTR_PROPOSE_DATE + "=?, "
					+ IDBConstants.ATTR_FROM + "=?, " + IDBConstants.ATTR_TO
					+ "=?, " + IDBConstants.ATTR_DAYS + "=?, "
					+ IDBConstants.ATTR_REASON + "=?, "
					+ IDBConstants.ATTR_ADDRESS + "=?, "
					+ IDBConstants.ATTR_PHONE + "=?, "
					+ IDBConstants.ATTR_REPLACED + "=?, "
					+ IDBConstants.ATTR_CHECKED + "=?, "
					+ IDBConstants.ATTR_CHECKED_DATE + "=?, "
					+ IDBConstants.ATTR_APPROVED + "=?, "
					+ IDBConstants.ATTR_APPROVED_DATE + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=?, "
					+ IDBConstants.ATTR_IS_REFERENCE + "=?, "
					+ IDBConstants.ATTR_FILE + "=?, "
					+ IDBConstants.ATTR_REFERENCE + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			int col = 1;
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getFrom() != null)
				stm.setDate(col++,
						new java.sql.Date(reason.getFrom().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getTo() != null)
				stm.setDate(col++, new java.sql.Date(reason.getTo().getTime()));
			else
				stm.setNull(col++, Types.DATE);

			stm.setInt(col++, reason.getDays());
			stm.setLong(col++, reason.getReason());
			stm.setString(col++, reason.getAddress());
			stm.setString(col++, reason.getPhone());

			if (reason.getReplaced() != 0)
				stm.setLong(col++, reason.getReplaced());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getChecked() != 0)
				stm.setLong(col++, reason.getChecked());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getCheckedDate() != null)
				stm.setDate(col++, new java.sql.Date(reason.getCheckedDate()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if (reason.getApproved() != 0)
				stm.setLong(col++, reason.getApproved());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			if (reason.getPropose() != null)
				stm.setDate(col++, new java.sql.Date(reason.getPropose()
						.getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, reason.getDescription());
			stm.setBoolean(col++, reason.isReference());
			stm.setString(col++, reason.getFile());
			if (reason.getReference() == null
					|| reason.getReference().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, reason.getReference());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeePermition(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EMPLOYEE_PERMITION
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeLeavePermition[] getAllEmployeeLeavePermition(String query,
			short type, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {

				vresult.addElement(new EmployeeLeavePermition(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX),
						type, rs
						.getDate(IDBConstants.ATTR_PROPOSE_DATE), rs
						.getDate(IDBConstants.ATTR_FROM), rs
						.getDate(IDBConstants.ATTR_TO), rs
						.getInt(IDBConstants.ATTR_DAYS), rs
						.getLong(IDBConstants.ATTR_REASON), rs
						.getString(IDBConstants.ATTR_ADDRESS), rs
						.getString(IDBConstants.ATTR_PHONE), rs
						.getLong(IDBConstants.ATTR_REPLACED), rs
						.getLong(IDBConstants.ATTR_CHECKED), rs
						.getDate(IDBConstants.ATTR_CHECKED_DATE), rs
						.getLong(IDBConstants.ATTR_APPROVED), rs
						.getDate(IDBConstants.ATTR_APPROVED_DATE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_IS_REFERENCE), rs
						.getString(IDBConstants.ATTR_FILE), rs
						.getBytes(IDBConstants.ATTR_REFERENCE)));
			}

			EmployeeLeavePermition[] result = new EmployeeLeavePermition[vresult
			                                                             .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createNonPaychequePeriod(NonPaychequePeriod period,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_NON_PAYCHEQUE_PERIOD + "("
					+ IDBConstants.ATTR_PERIOD_FROM1 + " ,"
					+ IDBConstants.ATTR_PERIOD_TO1 + " ,"
					+ IDBConstants.ATTR_PERIOD_FROM2 + " ,"
					+ IDBConstants.ATTR_PERIOD_TO2 + ") "
					+ " values (?, ?, ?, ?)");

			int col = 1;
			stm.setShort(col++, period.getFrom1());
			stm.setShort(col++, period.getTo1());
			stm.setShort(col++, period.getFrom2());
			stm.setShort(col++, period.getTo2());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public NonPaychequePeriod getNonPaychequePeriod(Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_NON_PAYCHEQUE_PERIOD);

			if (rs.next())
				return new NonPaychequePeriod(rs
						.getShort(IDBConstants.ATTR_PERIOD_FROM1), rs
						.getShort(IDBConstants.ATTR_PERIOD_TO1), rs
						.getShort(IDBConstants.ATTR_PERIOD_FROM2), rs
						.getShort(IDBConstants.ATTR_PERIOD_TO2));
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteNonPaychequePeriod(Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_NON_PAYCHEQUE_PERIOD);

		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createAnnualLeaveRight(AnnualLeaveRight leave, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT + "("
					+ IDBConstants.ATTR_VALID_FROM + ","
					+ IDBConstants.ATTR_VALID_THRU + ","
					+ IDBConstants.ATTR_MIN_YEAR + ","
					+ IDBConstants.ATTR_MAX_YEAR + ","
					+ IDBConstants.ATTR_MIN_RIGHT + ","
					+ IDBConstants.ATTR_MAX_RIGHT + ")"
					+ " values(?, ?, ?, ?, ?, ?)");

			stm.setShort(1, leave.getFrom());
			stm.setShort(2, leave.getThru());
			stm.setShort(3, leave.getMinYear());
			stm.setShort(4, leave.getMaxYear());
			stm.setShort(5, leave.getMinRight());
			stm.setShort(6, leave.getMaxRight());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public AnnualLeaveRight[] getAllAnnualLeaveRight(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT);

			while (rs.next()) {
				vresult.addElement(new AnnualLeaveRight(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getShort(IDBConstants.ATTR_VALID_FROM), rs
						.getShort(IDBConstants.ATTR_VALID_THRU), rs
						.getShort(IDBConstants.ATTR_MIN_YEAR), rs
						.getShort(IDBConstants.ATTR_MAX_YEAR), rs
						.getShort(IDBConstants.ATTR_MIN_RIGHT), rs
						.getShort(IDBConstants.ATTR_MAX_RIGHT)));
			}

			AnnualLeaveRight[] result = new AnnualLeaveRight[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public AnnualLeaveRight getAnnualLeaveRight(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if (rs.next()) {
				return new AnnualLeaveRight(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getShort(IDBConstants.ATTR_VALID_FROM), rs
						.getShort(IDBConstants.ATTR_VALID_THRU), rs
						.getShort(IDBConstants.ATTR_MIN_YEAR), rs
						.getShort(IDBConstants.ATTR_MIN_YEAR), rs
						.getShort(IDBConstants.ATTR_MIN_RIGHT), rs
						.getShort(IDBConstants.ATTR_MIN_RIGHT));
			}

			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateAnnualLeaveRight(long index, AnnualLeaveRight leave,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT + " SET "
					+ IDBConstants.ATTR_VALID_FROM + "=?, "
					+ IDBConstants.ATTR_VALID_THRU + "=?, "
					+ IDBConstants.ATTR_MIN_YEAR + "=?, "
					+ IDBConstants.ATTR_MAX_YEAR + "=?, "
					+ IDBConstants.ATTR_MIN_RIGHT + "=?, "
					+ IDBConstants.ATTR_MAX_RIGHT + "=? " + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setShort(1, leave.getFrom());
			stm.setShort(2, leave.getThru());
			stm.setShort(3, leave.getMinYear());
			stm.setShort(4, leave.getMaxYear());
			stm.setShort(5, leave.getMinRight());
			stm.setShort(6, leave.getMaxRight());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteAnnualLeaveRight(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateHoliday(Holiday holiday, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {

			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_HOLIDAY
					+ " SET " + IDBConstants.ATTR_DESCRIPTION + "=? "
					+ " WHERE " + IDBConstants.ATTR_HOLIDAY_DATE + "= ?");

			stm.setString(1, holiday.getDescription());
			stm.setDate(2, new java.sql.Date(holiday.getDate().getTime()));
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createHoliday(Holiday holiday, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_HOLIDAY + "("
					+ IDBConstants.ATTR_HOLIDAY_DATE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?, ?)");

			stm.setDate(1, new java.sql.Date(holiday.getDate().getTime()));
			stm.setString(2, holiday.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Holiday[] getAllHoliday(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_HOLIDAY);

			while (rs.next()) {
				vresult.addElement(new Holiday(rs
						.getDate(IDBConstants.ATTR_HOLIDAY_DATE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}

			Holiday[] result = new Holiday[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Holiday[] getHoliday(java.util.Date beginDate,
			java.util.Date endDate, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_HOLIDAY + " WHERE "
					+ IDBConstants.ATTR_HOLIDAY_DATE + " BETWEEN '"
					+ new java.sql.Date(beginDate.getTime()) + "' AND '"
					+ new java.sql.Date(endDate.getTime()) + "'");

			while (rs.next()) {
				vresult.addElement(new Holiday(rs
						.getDate(IDBConstants.ATTR_HOLIDAY_DATE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}

			Holiday[] result = new Holiday[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteHoliday(java.util.Date date, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_HOLIDAY
					+ " WHERE " + IDBConstants.ATTR_HOLIDAY_DATE + "='"
					+ new java.sql.Date(date.getTime()) + "'");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public java.util.Date getMinTMTEmployement(long employeeindex,
			Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT MIN("
					+ IDBConstants.ATTR_TMT + ") as mindate FROM "
					+ IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex);
			if (rs.next())
				return rs.getDate("mindate");
			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public AnnualLeaveRight getAnnualLeaveRight(Connection conn)
	throws SQLException {
		Statement stm = null;
		short validthru = 0;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT + " WHERE "
					+ IDBConstants.ATTR_VALID_THRU + "=" + validthru + " AND "
					+ IDBConstants.ATTR_VALID_FROM + "= (SELECT MAX("
					+ IDBConstants.ATTR_VALID_FROM + ") FROM "
					+ IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT + ")");

			if (rs.next()) {
				return new AnnualLeaveRight(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getShort(IDBConstants.ATTR_VALID_FROM), rs
						.getShort(IDBConstants.ATTR_VALID_THRU), rs
						.getShort(IDBConstants.ATTR_MIN_YEAR), rs
						.getShort(IDBConstants.ATTR_MAX_YEAR), rs
						.getShort(IDBConstants.ATTR_MIN_RIGHT), rs
						.getShort(IDBConstants.ATTR_MAX_RIGHT));
			}

			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public int getEmployeeLeavePermitionUsed(long employeeindex, short year,
			Connection conn) throws SQLException {
		Statement stm = null;
		int n = 0;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT SUM("
					+ IDBConstants.ATTR_DAYS + ") as total FROM "
					+ IDBConstants.TABLE_EMPLOYEE_LEAVE + ","
					+ IDBConstants.TABLE_LEAVE_TYPE + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex
					+ " AND " + IDBConstants.ATTR_APPROVED_DATE
					+ " BETWEEN {d '" + year + "-01-01'}" + " AND {d '" + year
					+ "-12-31'} AND " + IDBConstants.ATTR_REASON + "="
					+ IDBConstants.TABLE_LEAVE_TYPE + "."
					+ IDBConstants.ATTR_AUTOINDEX + " AND "
					+ IDBConstants.ATTR_DEDUCTION + "= true");

			rs.next();
			n += rs.getInt("total");
			rs.close();
			rs = stm.executeQuery("SELECT SUM("
					+ IDBConstants.TABLE_EMPLOYEE_PERMITION + "."
					+ IDBConstants.ATTR_DAYS + ") as total FROM "
					+ IDBConstants.TABLE_EMPLOYEE_PERMITION + ","
					+ IDBConstants.TABLE_PERMITION_TYPE + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeindex
					+ " AND " + IDBConstants.ATTR_APPROVED_DATE
					+ " BETWEEN {d '" + year + "-01-01'}" + " AND {d '" + year
					+ "-12-31'} AND " + IDBConstants.ATTR_REASON + "="
					+ IDBConstants.TABLE_PERMITION_TYPE + "."
					+ IDBConstants.ATTR_AUTOINDEX + " AND "
					+ IDBConstants.ATTR_DEDUCTION + "= true");
			rs.next();
			n += rs.getInt("total");
			return n;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteAllHoliday(Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_HOLIDAY);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPayrollComponent(PayrollComponent component,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_PAYROLL_COMPONENT + "("
					+ IDBConstants.ATTR_CODE + ","
					+ IDBConstants.ATTR_DESCRIPTION + ","
					+ IDBConstants.ATTR_ISGROUP + ","
					+ IDBConstants.ATTR_ACCOUNT + ","
					+ IDBConstants.ATTR_PAYMENT + ","
					+ IDBConstants.ATTR_SUBMIT + ","
					+ IDBConstants.ATTR_PAYCHEQUE_LABEL + ","
					+ IDBConstants.ATTR_REPORT_LABEL + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?)");

			int col = 1;
			stm.setString(col++, component.getCode());
			stm.setString(col++, component.getDescription());
			stm.setBoolean(col++, component.isGroup());

			if (component.getAccount() != null)
				stm.setLong(col++, component.getAccount().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);

			stm.setShort(col++, component.getPayment());
			stm.setShort(col++, component.getSubmit());

			if (component.getPaychequeLabel() != null)
				stm.setLong(col++, component.getPaychequeLabel().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);

			stm.setString(col++, component.getReportLabel());

			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTaxArt21Component(TaxArt21Component component,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_TAXART21_COMPONENT + "("
					+ IDBConstants.ATTR_CODE + "," // 1
					+ IDBConstants.ATTR_DESCRIPTION + "," //2
					+ IDBConstants.ATTR_ISGROUP + "," // 3
					+ IDBConstants.ATTR_TAXACCOUNT + "," // 4
					+ IDBConstants.ATTR_FORMULA + "," // 5
					+ IDBConstants.ATTR_NOTE + "," // 6
					+ IDBConstants.ATTR_ISROUNDED + "," // 7
                    + IDBConstants.ATTR_ROUND_VALUE + "," // 8
                    + IDBConstants.ATTR_PRECISION + ","  // 9
                    + IDBConstants.ATTR_ISNEGATIVE + "," // 10
                    + IDBConstants.ATTR_ISCOMPARABLE + "," // 11
                    + IDBConstants.ATTR_COMPARATION_MODE + "," // 12
                    + IDBConstants.ATTR_COMPARATOR + ")" // 13
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");


			stm.setString(1, component.getCode());
			stm.setString(2, component.getDescription());
			stm.setBoolean(3, component.isGroup());
			if (component.getAccount() != null)
				stm.setLong(4, component.getAccount().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);
			if(component.getFormulaEntity() !=null)
				stm.setString(5, component.getFormulaEntity().getFormulaCode());
			else
				stm.setNull(5, Types.VARCHAR);
			stm.setString(6, component.getNote());
			stm.setBoolean(7, component.isRounded());
			if(component.isRounded()){
				stm.setInt(8, component.getRoundedValue());
				stm.setInt(9, component.getPrecision());
			}else{
				stm.setNull(8, Types.INTEGER);
				stm.setNull(9, Types.INTEGER);
			}
			stm.setBoolean(10, component.isNegative());
			stm.setBoolean(11, component.isComparable());
			if(component.isComparable()){
				stm.setString(12, component.getComparationMode());
				stm.setDouble(13, component.getComparatorValue());
			}else{
				stm.setNull(12, Types.VARCHAR);
				stm.setNull(13, Types.DOUBLE);
			}

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPayrollComponentStructure(long superpayroll,
			long subpayroll, Connection conn) throws SQLException {
		Statement stm = null;

		try {

			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO "
					+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE
					+ " values(" + superpayroll + "," + subpayroll + ")");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTaxArt21ComponentStructure(long superpayroll,
			long subpayroll, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_TAXART21_COMPONENT_STRUCTURE + "("
					+ IDBConstants.ATTR_SUPER_TAXART21COMPONENT + ","
					+ IDBConstants.ATTR_SUB_TAXART21COMPONENT + ")"
					+ " values (?, ?)");

			stm.setLong(1, superpayroll);
			stm.setLong(2, subpayroll);

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollComponent[] getAllPayrollComponent(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PAYROLL_COMPONENT);

			while (rs.next()) {
				vresult.addElement(new PayrollComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_ACCOUNT), conn), rs
								.getShort(IDBConstants.ATTR_PAYMENT), rs
								.getShort(IDBConstants.ATTR_SUBMIT),
								this.getPaychequeLabelByIndex(rs
										.getLong(IDBConstants.ATTR_PAYCHEQUE_LABEL),
										conn), rs.getString(IDBConstants.ATTR_REPORT_LABEL)));
			}
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}

		PayrollComponent[] result = new PayrollComponent[vresult.size()];
		vresult.copyInto(result);
		return result;
	}

	public TaxArt21Component[] getAllTaxArt21Component(Connection conn, IFormulaParser parser)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_TAXART21_COMPONENT);

			while (rs.next()) {
				TaxArt21Component component = new TaxArt21Component(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_TAXACCOUNT), conn),
						parser.parseToFormula(rs
								.getString(IDBConstants.ATTR_FORMULA)), rs
						.getString(IDBConstants.ATTR_NOTE), rs
						.getBoolean(IDBConstants.ATTR_ISROUNDED), rs
						.getInt(IDBConstants.ATTR_ROUND_VALUE), rs
						.getInt(IDBConstants.ATTR_PRECISION), rs
						.getBoolean(IDBConstants.ATTR_ISNEGATIVE), rs
						.getBoolean(IDBConstants.ATTR_ISCOMPARABLE), rs
						.getString(IDBConstants.ATTR_COMPARATION_MODE), rs
						.getDouble(IDBConstants.ATTR_COMPARATOR));
				vresult.add(component);
			}
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}

		TaxArt21Component[] result = new TaxArt21Component[vresult.size()];
		vresult.copyInto(result);
		return result;
	}

	public PayrollComponent getPayrollComponent(long index, Connection conn)
	throws SQLException {
		Statement stm = null;
		String query = "SELECT * FROM "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT + " WHERE "
			+ IDBConstants.ATTR_AUTOINDEX + "=" + index;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			if (rs.next())
				return new PayrollComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_ACCOUNT), conn), rs
								.getShort(IDBConstants.ATTR_PAYMENT), rs
								.getShort(IDBConstants.ATTR_SUBMIT),
						this.getPaychequeLabelByIndex(rs
								.getLong(IDBConstants.ATTR_PAYCHEQUE_LABEL),
								conn), rs
								.getString(IDBConstants.ATTR_REPORT_LABEL));
			return null;

		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21Component getTaxArt21Component(long index, Connection conn,IFormulaParser parser)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_TAXART21_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if (rs.next())
				return new TaxArt21Component(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_TAXACCOUNT), conn),
						parser.parseToFormula(rs
								.getString(IDBConstants.ATTR_FORMULA)), rs
						.getString(IDBConstants.ATTR_NOTE), rs
						.getBoolean(IDBConstants.ATTR_ISROUNDED), rs
						.getInt(IDBConstants.ATTR_ROUND_VALUE), rs
						.getInt(IDBConstants.ATTR_PRECISION), rs
						.getBoolean(IDBConstants.ATTR_ISNEGATIVE), rs
						.getBoolean(IDBConstants.ATTR_ISCOMPARABLE), rs
						.getString(IDBConstants.ATTR_COMPARATION_MODE), rs
						.getDouble(IDBConstants.ATTR_COMPARATOR));;
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public TaxArt21Component getTaxArt21Component(Account account, Connection conn,IFormulaParser parser)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_TAXART21_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_TAXACCOUNT + "=" + account.getIndex());

			if (rs.next())
				return new TaxArt21Component(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_TAXACCOUNT), conn),
						parser.parseToFormula(rs
								.getString(IDBConstants.ATTR_FORMULA)), rs
						.getString(IDBConstants.ATTR_NOTE), rs
						.getBoolean(IDBConstants.ATTR_ISROUNDED), rs
						.getInt(IDBConstants.ATTR_ROUND_VALUE), rs
						.getInt(IDBConstants.ATTR_PRECISION), rs
						.getBoolean(IDBConstants.ATTR_ISNEGATIVE), rs
						.getBoolean(IDBConstants.ATTR_ISCOMPARABLE), rs
						.getString(IDBConstants.ATTR_COMPARATION_MODE), rs
						.getDouble(IDBConstants.ATTR_COMPARATOR));;
			return null;
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollComponent[] getTaxArt21Payroll(TaxArt21Component ta21Comp,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PAYROLL_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "  IN (SELECT "
					+ IDBConstants.ATTR_PAYROLL_COMPONENT + " FROM "
					+ IDBConstants.TABLE_TAXART21_PAYROLL + " WHERE "
					+ IDBConstants.ATTR_TAXART21_COMPONENT + " = "
					+ ta21Comp.getIndex() + ")");

			while (rs.next()) {
				vresult.addElement(new PayrollComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_ACCOUNT), conn), rs
								.getShort(IDBConstants.ATTR_PAYMENT), rs
								.getShort(IDBConstants.ATTR_SUBMIT),
						this.getPaychequeLabelByIndex(rs
								.getLong(IDBConstants.ATTR_PAYCHEQUE_LABEL),
								conn), rs
								.getString(IDBConstants.ATTR_REPORT_LABEL)));

			}

			PayrollComponent[] result = new PayrollComponent[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollComponent[] getSuperPayrollComponent(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT + " WHERE "
			+ IDBConstants.ATTR_AUTOINDEX + " NOT IN (SELECT "
			+ IDBConstants.ATTR_SUB_PAYROLL + " FROM "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE + ")";

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new PayrollComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_ACCOUNT), conn), rs
								.getShort(IDBConstants.ATTR_PAYMENT), rs
								.getShort(IDBConstants.ATTR_SUBMIT),
						this.getPaychequeLabelByIndex(rs
								.getLong(IDBConstants.ATTR_PAYCHEQUE_LABEL),
								conn), rs
								.getString(IDBConstants.ATTR_REPORT_LABEL)));

			}

			PayrollComponent[] result = new PayrollComponent[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21Component[] getSuperTaxArt21Component(Connection conn,IFormulaParser iFormulaParser)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_TAXART21_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + " NOT IN (SELECT "
					+ IDBConstants.ATTR_SUB_TAXART21COMPONENT + " FROM "
					+ IDBConstants.TABLE_TAXART21_COMPONENT_STRUCTURE + ")");			//
			while (rs.next()) {

				vresult.addElement(new TaxArt21Component(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_TAXACCOUNT), conn),
						iFormulaParser.parseToFormula(rs
								.getString(IDBConstants.ATTR_FORMULA)), rs
						.getString(IDBConstants.ATTR_NOTE), rs
						.getBoolean(IDBConstants.ATTR_ISROUNDED), rs
						.getInt(IDBConstants.ATTR_ROUND_VALUE), rs
						.getInt(IDBConstants.ATTR_PRECISION), rs
						.getBoolean(IDBConstants.ATTR_ISNEGATIVE), rs
						.getBoolean(IDBConstants.ATTR_ISCOMPARABLE), rs
						.getString(IDBConstants.ATTR_COMPARATION_MODE), rs
						.getDouble(IDBConstants.ATTR_COMPARATOR)));

			}

			TaxArt21Component[] result = new TaxArt21Component[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollComponent[] getSubPayrollComponent(long superindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT + ".*  FROM "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT + ", "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT_STRUCTURE
			+ " WHERE " + IDBConstants.ATTR_SUPER_PAYROLL + "="
			+ superindex + " AND " + IDBConstants.ATTR_SUB_PAYROLL
			+ "=" + IDBConstants.ATTR_AUTOINDEX + " ORDER BY "
			+ IDBConstants.TABLE_PAYROLL_COMPONENT + "."
			+ IDBConstants.ATTR_CODE;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new PayrollComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_ACCOUNT), conn), rs
								.getShort(IDBConstants.ATTR_PAYMENT), rs
								.getShort(IDBConstants.ATTR_SUBMIT),
						this.getPaychequeLabelByIndex(rs
								.getLong(IDBConstants.ATTR_PAYCHEQUE_LABEL),
								conn), rs
								.getString(IDBConstants.ATTR_REPORT_LABEL)));
			}

			PayrollComponent[] result = new PayrollComponent[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21Component[] getSubTaxArt21Component(long superindex,
			Connection conn,IFormulaParser parser) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			String strQuery = "SELECT "
				+ IDBConstants.TABLE_TAXART21_COMPONENT + ".*  FROM "
				+ IDBConstants.TABLE_TAXART21_COMPONENT + ", "
				+ IDBConstants.TABLE_TAXART21_COMPONENT_STRUCTURE
				+ " WHERE " + IDBConstants.ATTR_SUPER_TAXART21COMPONENT
				+ "=" + superindex + " AND "
				+ IDBConstants.ATTR_SUB_TAXART21COMPONENT + "= "
				+ IDBConstants.ATTR_AUTOINDEX + " ORDER BY "
				+ IDBConstants.TABLE_TAXART21_COMPONENT + "."
				+ IDBConstants.ATTR_CODE;
			System.err.println(strQuery);
			ResultSet rs = stm.executeQuery(strQuery);

			while (rs.next()) {
				vresult.addElement(new TaxArt21Component(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_TAXACCOUNT), conn),
						parser.parseToFormula(rs
								.getString(IDBConstants.ATTR_FORMULA)), rs
						.getString(IDBConstants.ATTR_NOTE), rs
						.getBoolean(IDBConstants.ATTR_ISROUNDED), rs
						.getInt(IDBConstants.ATTR_ROUND_VALUE), rs
						.getInt(IDBConstants.ATTR_PRECISION), rs
						.getBoolean(IDBConstants.ATTR_ISNEGATIVE), rs
						.getBoolean(IDBConstants.ATTR_ISCOMPARABLE), rs
						.getString(IDBConstants.ATTR_COMPARATION_MODE), rs
						.getDouble(IDBConstants.ATTR_COMPARATOR)));
			}

			TaxArt21Component[] result = new TaxArt21Component[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}

	}

	public void updatePayrollComponent(long index, PayrollComponent component,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_PAYROLL_COMPONENT + " SET "
					+ IDBConstants.ATTR_CODE + "=?,"
					+ IDBConstants.ATTR_DESCRIPTION + "=?,"
					+ IDBConstants.ATTR_ISGROUP + "=?,"
					+ IDBConstants.ATTR_ACCOUNT + "=?,"
					+ IDBConstants.ATTR_PAYMENT + "=?,"
					+ IDBConstants.ATTR_SUBMIT + "=?,"
					+ IDBConstants.ATTR_PAYCHEQUE_LABEL + "=?,"
					+ IDBConstants.ATTR_REPORT_LABEL + "=? "
					+ " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			int col = 1;
			stm.setString(col++, component.getCode());
			stm.setString(col++, component.getDescription());
			stm.setBoolean(col++, component.isGroup());

			if (component.getAccount() != null)
				stm.setLong(col++, component.getAccount().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);

			stm.setShort(col++, component.getPayment());
			stm.setShort(col++, component.getSubmit());

			if (component.getPaychequeLabel() != null)
				stm.setLong(col++, component.getPaychequeLabel().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);

			stm.setString(col++, component.getReportLabel());

			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateTaxArt21Component(long index,
			TaxArt21Component component, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			String query = "UPDATE " + IDBConstants.TABLE_TAXART21_COMPONENT
			+ " SET " + IDBConstants.ATTR_CODE + "=?," // 1
			+ IDBConstants.ATTR_DESCRIPTION + "=?," // 2
			+ IDBConstants.ATTR_ISGROUP + "=?," // 3
			+ IDBConstants.ATTR_TAXACCOUNT + "=?," // 4
			+ IDBConstants.ATTR_FORMULA + "=?," // 5
			+ IDBConstants.ATTR_NOTE + "=?," // 6
			+ IDBConstants.ATTR_ISROUNDED + "=?," // 7
            + IDBConstants.ATTR_ROUND_VALUE + "=?," // 8
            + IDBConstants.ATTR_PRECISION + "=?,"  // 9
            + IDBConstants.ATTR_ISNEGATIVE + "=?," // 10
            + IDBConstants.ATTR_ISCOMPARABLE + "=?," // 11
            + IDBConstants.ATTR_COMPARATION_MODE + "=?," // 12
            + IDBConstants.ATTR_COMPARATOR + "=?" // 13
			+ " WHERE "
			+ IDBConstants.ATTR_AUTOINDEX + "=" + component.getIndex();

			stm = conn.prepareStatement(query);

			//int col = 1;
			stm.setString(1, component.getCode());
			stm.setString(2, component.getDescription());
			stm.setBoolean(3, component.isGroup());

			if (component.getAccount() != null && !component.isGroup()) {

				stm.setLong(4, component.getAccount().getIndex());
			} else {
				stm.setNull(4, Types.LONGVARCHAR);
			}
			if(component.getFormulaEntity()!=null)
				stm.setString(5, component.getFormulaEntity().getFormulaCode());
			else
				stm.setNull(5, Types.VARCHAR);

			stm.setString(6, component.getNote());

			stm.setBoolean(7, component.isRounded());
			if(component.isRounded()){
				stm.setInt(8, component.getRoundedValue());
				stm.setInt(9, component.getPrecision());
			}else{
				stm.setNull(8, Types.INTEGER);
				stm.setNull(9, Types.INTEGER);
			}
			stm.setBoolean(10, component.isNegative());
			stm.setBoolean(11, component.isComparable());
			if(component.isComparable()){
				stm.setString(12, component.getComparationMode());
				stm.setDouble(13, component.getComparatorValue());
			}else{
				stm.setNull(12, Types.VARCHAR);
				stm.setNull(13, Types.DOUBLE);
			}

			stm.executeUpdate();
		} catch (Exception ex) {
			// ex.printStackTrace();
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deletePayrollComponent(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PAYROLL_COMPONENT
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteTaxArt21Component(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_TAXART21_COMPONENT
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}

	}

	public void createTaxArt21Payroll(TaxArt21Component ta21Component,
			PayrollComponent pcComponent, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_TAXART21_PAYROLL + "("
					+ IDBConstants.ATTR_TAXART21_COMPONENT + ","
					+ IDBConstants.ATTR_PAYROLL_COMPONENT + ")"
					+ " values (?, ?)");

			stm.setLong(1, ta21Component.getIndex());
			stm.setLong(2, pcComponent.getIndex());
			stm.executeUpdate();
		} catch (Exception ex) {
			// ex.printStackTrace();
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}

	}

	public void updateTaxArt21Payroll(long index, TaxArt21Component component,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			String query = "UPDATE " + IDBConstants.TABLE_TAXART21_PAYROLL
			+ " SET " + IDBConstants.ATTR_TAXART21_COMPONENT + "=?,"
			+ IDBConstants.ATTR_PAYROLL_COMPONENT + "=?" + " WHERE "
			+ IDBConstants.ATTR_AUTOINDEX + "=" + index;
			stm = conn.prepareStatement(query);

			int col = 1;
			stm.setString(col++, component.getCode());
			stm.setString(col++, component.getDescription());

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}

	}

	public void deleteTaxArt21Payroll(long index, PayrollComponent paycomp,
			Connection conn) throws SQLException {
		Statement stm = null;
		try {

			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_TAXART21_PAYROLL
					+ " WHERE " + IDBConstants.ATTR_TAXART21_COMPONENT + "="
					+ index + " AND " + IDBConstants.ATTR_PAYROLL_COMPONENT
					+ "=" + paycomp.getIndex());

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPayrollCategory(PayrollCategory category, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY + "("
					+ IDBConstants.ATTR_NAME + ","
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?, ?)");
			stm.setString(1, category.getName());
			stm.setString(2, category.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updatePayrollCategory(long index, PayrollCategory category,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY + " SET "
					+ IDBConstants.ATTR_NAME + "=?, "
					+ IDBConstants.ATTR_DESCRIPTION + "=?" + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, category.getName());
			stm.setString(2, category.getDescription());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deletePayrollCategory(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PAYROLL_CATEGORY
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollCategory[] getAllPayrollCategory(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY);

			while (rs.next()) {
				vresult.addElement(new PayrollCategory(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_NAME), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}

			PayrollCategory[] result = new PayrollCategory[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPayrollCategoryComponent(long categoryindex,
			PayrollCategoryComponent component, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT + "("
					+ IDBConstants.ATTR_CATEGORY + ","
					+ IDBConstants.ATTR_COMPONENT + ","
					+ IDBConstants.ATTR_FORMULA+ ","
					+ IDBConstants.ATTR_FORMULA_MONTH + ","
					+ IDBConstants.ATTR_ROUND_VALUE + ","
					+ IDBConstants.ATTR_PRECISION + ")" + " values (?, ?, ?, ?, ?, ?)");

			// i add month

			stm.setLong(1, categoryindex);
			stm.setLong(2, component.getPayrollComponent().getIndex());
			stm.setString(3, component.getFormulaEntity().getFormulaCode());
			stm.setInt(4, (int)component.getEveryWhichMonth());
			stm.setInt(5, (int)component.getFormulaEntity().getNumberRounding().getRoundingMode());
			stm.setInt(6, component.getFormulaEntity().getNumberRounding().getPrecision());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updatePayrollCategoryComponent(long index,
			PayrollCategoryComponent component, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT + " SET "
					+ IDBConstants.ATTR_COMPONENT + "=?, "
					+ IDBConstants.ATTR_FORMULA + "=?, "
					+ IDBConstants.ATTR_FORMULA_MONTH + "=?, "
					+ IDBConstants.ATTR_ROUND_VALUE + "=?, "
					+ IDBConstants.ATTR_PRECISION + "=? "
					+ " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setLong(1, component.getPayrollComponent().getIndex());
			stm.setString(2, component.getFormulaEntity().getFormulaCode());
			stm.setInt(3, (int)component.getEveryWhichMonth());
			stm.setInt(4, (int)component.getFormulaEntity().getNumberRounding().getRoundingMode());
			stm.setInt(5, component.getFormulaEntity().getNumberRounding().getPrecision());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deletePayrollCategoryComponent(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollCategoryComponent[] getAllPayrollCategoryComponent(
			long index, Connection conn, IFormulaParser parser, String table)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			String query = "";
			if(table==IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT){
				query = "SELECT * FROM "
					+ table + " WHERE "
					+ IDBConstants.ATTR_CATEGORY + "=" + index;
			}else if(table==IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT){
				query = "SELECT * FROM "
					+ table + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + index;
			}
			ResultSet rs = stm.executeQuery(query);

			/* the real one
			 while (rs.next()) {
			 vresult.addElement(new PayrollCategoryComponent(rs
			 .getLong(IDBConstants.ATTR_AUTOINDEX),
			 getPayrollComponent(rs
			 .getLong(IDBConstants.ATTR_COMPONENT), conn),
			 parser.parseToFormula((rs
			 .getString(IDBConstants.ATTR_FORMULA)))));
			 }
			 */

			while (rs.next()) {
				vresult.addElement(new PayrollCategoryComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX),
						getPayrollComponent(rs
								.getLong(IDBConstants.ATTR_COMPONENT), conn),
								parser.parseToFormula(rs.getString(IDBConstants.ATTR_FORMULA),
										rs.getShort(IDBConstants.ATTR_FORMULA_MONTH),
										new NumberRounding(rs.getShort(IDBConstants.ATTR_ROUND_VALUE),
												rs.getInt(IDBConstants.ATTR_PRECISION)))));

			}

			PayrollCategoryComponent[] result = new PayrollCategoryComponent[
			                                                                 vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createPayrollCategoryEmployee(long categoryindex,
			long employeeindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE + "("
					+ IDBConstants.ATTR_CATEGORY + ","
					+ IDBConstants.ATTR_EMPLOYEE + ")" + " values ("
					+ categoryindex + "," + employeeindex + ")");
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deletePayrollCategoryEmployee(long categoryindex,
			long employeeindex, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE + " WHERE "
					+ IDBConstants.ATTR_CATEGORY + "= " + categoryindex
					+ " AND " + IDBConstants.ATTR_EMPLOYEE + "= "
					+ employeeindex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee[] getPayrollCategoryEmployee(long categoryindex,
			Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + " IN (" + " SELECT "
					+ IDBConstants.ATTR_EMPLOYEE + " FROM "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE + " WHERE "
					+ IDBConstants.ATTR_CATEGORY + "=" + categoryindex + ")");
			while (rs.next()) {
				vresult.addElement(new Employee(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_EMPLOYEE_NO), rs
						.getString(IDBConstants.ATTR_FIRST_NAME), rs
						.getString(IDBConstants.ATTR_MIDLE_NAME), rs
						.getString(IDBConstants.ATTR_LAST_NAME), rs
						.getString(IDBConstants.ATTR_NICK_NAME), rs
						.getString(IDBConstants.ATTR_BIRTH_PLACE), rs
						.getDate(IDBConstants.ATTR_BIRTH_DATE), this
						.getSexType(rs.getLong(IDBConstants.ATTR_SEX), conn),
						this.getReligion(
								rs.getLong(IDBConstants.ATTR_RELIGION), conn),
								rs.getString(IDBConstants.ATTR_NATIONALITY), this
								.getMaritalStatus(rs
										.getLong(IDBConstants.ATTR_MARITAL),
										conn), this.getPTKP(rs
												.getLong(IDBConstants.ATTR_ART_21), conn), rs
												.getString(IDBConstants.ATTR_ADDRESS), rs
												.getString(IDBConstants.ATTR_CITY), rs
												.getInt(IDBConstants.ATTR_POST_CODE), rs
												.getString(IDBConstants.ATTR_PROVINCE), rs
												.getString(IDBConstants.ATTR_COUNTRY), rs
												.getString(IDBConstants.ATTR_PHONE), rs
												.getString(IDBConstants.ATTR_MOBILE_PHONE1), rs
												.getString(IDBConstants.ATTR_MOBILE_PHONE2), rs
												.getString(IDBConstants.ATTR_FAX), rs
												.getString(IDBConstants.ATTR_EMAIL)));
			}

			Employee[] result = new Employee[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollComponent[] getNonGroupPayrollComponent(Connection conn)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT *  FROM "
					+ IDBConstants.TABLE_PAYROLL_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_ISGROUP + "= false" + " ORDER BY "
					+ IDBConstants.TABLE_PAYROLL_COMPONENT + "."
					+ IDBConstants.ATTR_CODE);

			while (rs.next()) {
				vresult.addElement(new PayrollComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_ACCOUNT), conn), rs
								.getShort(IDBConstants.ATTR_PAYMENT), rs
								.getShort(IDBConstants.ATTR_SUBMIT),
						this.getPaychequeLabelByIndex(rs
								.getLong(IDBConstants.ATTR_PAYCHEQUE_LABEL),
								conn), rs
								.getString(IDBConstants.ATTR_REPORT_LABEL)));
			}

			PayrollComponent[] result = new PayrollComponent[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee_n[] getEmployeeBy_Unit(Connection conn,long unitNumber,String tgl)throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,job.name as jobtitle from employee emp inner join " +
		"(select e.* from employeeemployment e, (select employee, max(tmt) tmt from " +
		"(select * from employeeemployment where tmt<'" + tgl + "') group by employee " +
		") lastemp where e.employee=lastemp.employee and e.tmt=lastemp.tmt " +
		"and unit=" + unitNumber + ") employment " +
		"on emp.autoindex=employment.employee " +
		"inner join jobtitle job on employment.jobtitle=job.autoindex " +
		"where not exists (SELECT employee FROM employeeretirement ret WHERE ret.tmt<='" + tgl + "' and " +
		"emp.autoindex=ret.employee) " +
		"order by emp.employeeno";
		System.err.println(query);

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				vresult.addElement(new Employee_n(rs.getLong("autoindex"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getString("jobtitle")));
			}
			Employee_n[] result = new Employee_n[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	/*public Employee_n[] getEmployeeBy_Unit(Connection conn,long unitNumber,String tgl)throws SQLException {
	 Statement stm = null;
	 Vector vresult = new Vector();
	 String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,job.name as jobtitle from employee emp inner join " +
	 "(select e.* from employeeemployment e, (select employee, max(tmt) tmt from " +
	 "(select * from employeeemployment where tmt<'" + tgl + "') group by employee " +
	 ") lastemp where e.employee=lastemp.employee and e.tmt=lastemp.tmt " +
	 "and unit=" + unitNumber + ") employment " +
	 "on emp.autoindex=employment.employee " +
	 "inner join jobtitle job on employment.jobtitle=job.autoindex " +
	 "where not exists (SELECT employee FROM employeeretirement ret WHERE ret.tmt<='" + tgl + "' and " +
	 "emp.autoindex=ret.employee) " +
	 "order by emp.employeeno";
	 //System.out.println(query);
	 try {
	 stm = conn.createStatement();
	 ResultSet rs = stm.executeQuery(query);
	 while (rs.next()) {

	 vresult.addElement(new Employee_n(rs.getLong("autoindex"),
	 rs.getString("employeeno"),
	 rs.getString("firstname"),
	 rs.getString("midlename"),
	 rs.getString("lastname"),
	 rs.getString("jobtitle")));
	 }
	 Employee_n[] result = new Employee_n[vresult.size()];
	 vresult.copyInto(result);
	 return result;
	 }catch(Exception ex){
	 throw new SQLException(OtherSQLException.getMessage(ex));
	 }
	 finally {
	 if (stm != null)
	 stm.close();
	 }
	 }*/

	/*public Employee_n[] getEmployeeBy_Index(Connection conn,long Index,String tgl)throws SQLException {
	 Statement stm = null;
	 Vector vresult = new Vector();
	 String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,job.name as jobtitle from employee emp inner join " +
	 "(select e.* from employeeemployment e, (select employee, max(tmt) tmt from " +
	 "(select * from employeeemployment where tmt<'" + tgl + "') group by employee " +
	 ") lastemp where e.employee=lastemp.employee and e.tmt=lastemp.tmt " +
	 "and unit=" + unitNumber + ") employment " +
	 "on emp.autoindex=employment.employee " +
	 "inner join jobtitle job on employment.jobtitle=job.autoindex " +
	 "where not exists (SELECT employee FROM employeeretirement ret WHERE ret.tmt<'" + tgl + "' and " +
	 "emp.autoindex=ret.employee) " +
	 "order by emp.employeeno";
	 try {
	 stm = conn.createStatement();
	 ResultSet rs = stm.executeQuery(query);
	 while (rs.next()) {

	 vresult.addElement(new Employee_n(rs.getLong("autoindex"),
	 rs.getString("employeeno"),
	 rs.getString("firstname"),
	 rs.getString("midlename"),
	 rs.getString("lastname"),
	 rs.getString("jobtitle")));
	 }
	 Employee_n[] result = new Employee_n[vresult.size()];
	 vresult.copyInto(result);
	 return result;
	 }catch(Exception ex){
	 throw new SQLException(OtherSQLException.getMessage(ex));
	 }
	 finally {
	 if (stm != null)
	 stm.close();
	 }
	 }*/

	public Employee_n[] getEmployeeBy_Criteria(Connection conn,String query)throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				vresult.addElement(new Employee_n(rs.getLong("autoindex"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getString("jobtitle")));
			}
			Employee_n[] result = new Employee_n[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public SimpleEmployee[] getEmployeeForPayroll(Connection conn,String query)throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				vresult.addElement(new SimpleEmployee(rs.getLong("autoindex"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getLong("department")));
			}
			SimpleEmployee[] result = new SimpleEmployee[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollCategoryComponent[] getPayrollCategoryComponent(Connection conn,long indexEmployee, long payment,long submit , IFormulaParser parser) throws SQLException{
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "select a.component,a.formula from payrollcategorycomponent " +
		"a,payrollcomponent b where category in (select category from payrollcategoryemployee where employee='" + indexEmployee + "') and a.component=b.autoindex";
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(new PayrollCategoryComponent(
						getPayrollComponent(rs.getLong("component"),conn),
						parser.parseToFormula((rs.getString(IDBConstants.ATTR_FORMULA)))));
			}
			PayrollCategoryComponent[] result = new PayrollCategoryComponent[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	/*
	 i comment this
	 do not erase this part
	 public PayrollCategoryComponent[] getSelectedPayrollCategoryComponent
	 (Connection m_conn, long employeeId, String componentIds,
	 IFormulaParser parser) throws SQLException {

	 Statement stm = null;
	 Vector vresult = new Vector();
	 String query = "select catemp.category, catemp.employee, " +
	 "catcomp.autoindex, catcomp.component, catcomp.formula " +
	 "from payrollcategoryemployee catemp, " +
	 "payrollcategorycomponent catcomp  " +
	 "where catemp.category=catcomp.category "+
	 "and catemp.employee=" + String.valueOf(employeeId) + " " ;
	 //	"and catcomp.component in (" + componentIds + ")";
	  ////System.out.println(query);
	  try {
	  stm = m_conn.createStatement();
	  ResultSet rs = stm.executeQuery(query);
	  while (rs.next()) {
	  vresult.addElement(new PayrollCategoryComponent(
	  getPayrollComponent(rs.getLong("component"), m_conn),
	  parser.parseToFormula((rs.getString(IDBConstants.ATTR_FORMULA)))));
	  }
	  PayrollCategoryComponent[] results = new PayrollCategoryComponent[vresult.size()];
	  vresult.copyInto(results);
	  return results;
	  }catch(Exception ex){
	  throw new SQLException(OtherSQLException.getMessage(ex));
	  }
	  finally {
	  if (stm != null)
	  stm.close();
	  }
	  }
	  */

	public PayrollCategoryComponent[] getSelectedPayrollCategoryComponent
	(Connection m_conn, long employeeId, String componentIds,
			IFormulaParser parser) throws SQLException {

		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM "
			+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT
			+ " WHERE "
			+ IDBConstants.ATTR_EMPLOYEE
			+ "=" + String.valueOf(employeeId);
		//////System.out.println(query);
		try {
			stm = m_conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(new PayrollCategoryComponent(
						getPayrollComponent(rs.getLong(IDBConstants.ATTR_COMPONENT), m_conn),
						parser.parseToFormula(
								rs.getString(IDBConstants.ATTR_FORMULA),
								rs.getShort(IDBConstants.ATTR_FORMULA_MONTH),
								new NumberRounding(
										rs.getShort(IDBConstants.ATTR_ROUND_VALUE),
										rs.getInt(IDBConstants.ATTR_PRECISION))
										           )
										                )
						           );
			}
			PayrollCategoryComponent[] results = new PayrollCategoryComponent[vresult.size()];
			vresult.copyInto(results);
			return results;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PayrollCategoryComponent getSelectedAPayrollCategoryComponent
	(Connection m_conn, long employeeId, long componentId,
			IFormulaParser parser) throws SQLException {

		Statement stm = null;
		String query = "SELECT * FROM "
			+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT
			+ " WHERE "
			+ IDBConstants.ATTR_EMPLOYEE
			+ "=" + String.valueOf(employeeId)
			+ " AND "
			+ IDBConstants.ATTR_COMPONENT
			+ "=" + String.valueOf(componentId);
		//////System.out.println(query);
		try {
			stm = m_conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return new PayrollCategoryComponent(
						getPayrollComponent(rs.getLong(IDBConstants.ATTR_COMPONENT), m_conn),
						parser.parseToFormula(
								rs.getString(IDBConstants.ATTR_FORMULA),
								rs.getShort(IDBConstants.ATTR_FORMULA_MONTH),
								new NumberRounding(
										rs.getShort(IDBConstants.ATTR_ROUND_VALUE),
										rs.getInt(IDBConstants.ATTR_PRECISION))
										           )
										                );
			}
			return null;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public TimeSheetSummary[] getTimeSheetSummary(Connection m_conn,
			long employeeId, int month, int year) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		String to = year + "-" + month + "-20";
		if(month==1){
			month = 12;
			year--;
		}else
			month--;

		String from = year + "-" + month + "-21";

		String query = "select personal, areacode, sum(days) days from " +
		"(select ts.entrydate, tsd.*  from timesheetdetail tsd " +
		"inner join timesheet ts " +
		"on tsd.timesheet=ts.autoindex " +
		"where ts.entrydate " +
		"between '" + from + "' and '" + to + "' " +
		"and tsd.personal=" + employeeId + ") " +
		"group by personal, areacode";


		try {
			stm = m_conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				vresult.addElement(new TimeSheetSummary(
						employeeId, rs.getString("areacode"),
						month, year, rs.getInt("days")));
			}
			TimeSheetSummary[] results = new TimeSheetSummary[vresult.size()];
			vresult.copyInto(results);
			return results;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public void updateEmployeePayrollVerified(long index,EmployeePayrollSubmit edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		String query="UPDATE "
			+ IDBConstants.TABLE_EMPLOYEE_PAYROLL+ " SET "
			+ IDBConstants.ATTR_TRANSACTION+ "=? ,"
			+ IDBConstants.ATTR_STATUS+ "=? "+ " WHERE "
			+ IDBConstants.ATTR_UNIT + "=" + edu.getUnit().getIndex()
			+" and "+  IDBConstants.ATTR_MONTH + " = " + edu.getMonth()
			+" and "+  IDBConstants.ATTR_YEAR + " = " + edu.getYear()
			+" and "+  IDBConstants.ATTR_PAYROLL_TYPE + " = " + edu.getPayrollType()
			+" and "+  IDBConstants.ATTR_PAYCHEQUE_TYPE + " = " + edu.getPaychequeType();

		if (edu instanceof EmployeeTransportationAllowance) {
			EmployeeTransportationAllowance eta = (EmployeeTransportationAllowance) edu;
			query += (" and " + IDBConstants.ATTR_PAYMENT_PERIODE + "=" + eta.getPaymentPeriode());
		}
		try {

			stm = conn.prepareStatement(query);

			stm.setLong(1,index);
			stm.setInt(2, edu.getStatus() );

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public void updateEmployeePayroll(EmployeePayrollSubmit edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		/**
		 * update status,payment_periode, submitted_date
		 */
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL+ " SET "
					+ IDBConstants.ATTR_STATUS+ "=?, "
					+ IDBConstants.ATTR_PAYMENT_PERIODE + "=?, "
					+ IDBConstants.ATTR_SUBMITTED_DATE + "=? " + " WHERE "
					+ IDBConstants.ATTR_UNIT + "=" + edu.getUnit().getIndex()
					+" and "+  IDBConstants.ATTR_MONTH + " = " + edu.getMonth()
					+" and "+  IDBConstants.ATTR_YEAR + " = " + edu.getYear()
					+" and "+  IDBConstants.ATTR_PAYROLL_TYPE + " = " + edu.getPayrollType()
			);

			stm.setInt(1, edu.getStatus() );
			stm.setInt(2, edu.getPaymentPeriode() );
			stm.setDate(3, new java.sql.Date(edu.getSubmittedDate().getTime() ) );
			stm.executeUpdate();
			updateEmployeePayrollDetail(edu,conn);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public void updateEmployeePayrollDetail(EmployeePayrollSubmit edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL+ " SET "
					+ IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL + "=? " + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + edu.getEmployeeIndex()
			);

			stm.setDouble(1, edu.getValue() );
			stm.executeUpdate();
			if (edu instanceof EmployeeMealAllowanceSubmit) {
				updateEmployeeMealAllowance(edu,conn);
			}if (edu instanceof EmployeeTransportationAllowance) {
				updateTransportationAllowance(edu,conn);
			}if (edu instanceof EmployeeOvertimeSubmit) {
				updateOvertimeAttribute(edu,conn);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateOvertimeAttribute(EmployeePayrollSubmit edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		EmployeeOvertimeSubmit new_name=null;
		if (edu instanceof EmployeeOvertimeSubmit) {
			new_name = (EmployeeOvertimeSubmit) edu;
		}

		//update overTimeValue
		try {
			for(int i=0;i<5;i++){
				String query="UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_MEAL_ALLOWANCE+ " SET "
					+ IDBConstants.ATTR_OVERTIMEVALUE + "=? " + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + edu.getEmployeeIndex()
					+ " and "+ IDBConstants.ATTR_OVERTIMEMULTIPLIER+ " = "+ i ;

				stm = conn.prepareStatement(query);

				if(i==1)stm.setFloat(1, new_name.getOverTimeLESSThanOneHour() );
				if(i==2)stm.setFloat(1, new_name.getOverTimeZeroToSevenHour() );
				if(i==3)stm.setFloat(1, new_name.getOverTimeMOREThanOneHour() );
				if(i==4)stm.setFloat(1, new_name.getOverTimeEightHour() );
				if(i==5)stm.setFloat(1, new_name.getOverTimeMoreThanNineHour() );

			}

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public void updateEmployeeMealAllowance(EmployeePayrollSubmit edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		EmployeeMealAllowanceSubmit new_name=null;
		if (edu instanceof EmployeeMealAllowanceSubmit) {
			new_name = (EmployeeMealAllowanceSubmit) edu;
		}
		// update presence
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_MEAL_ALLOWANCE+ " SET "
					+ IDBConstants.ATTR_PRESENCE + "=? " + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + edu.getEmployeeIndex()
			);

			stm.setInt(1, new_name.getPresence() );
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public void updateTransportationAllowance(EmployeePayrollSubmit edu, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;
		EmployeeTransportationAllowance new_name=null;
		if (edu instanceof EmployeeTransportationAllowance) {
			new_name = (EmployeeTransportationAllowance) edu;
		}

		//ipdate presence,presenceLate,presenceNotLate
		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE+ " SET "
					+ IDBConstants.ATTR_PRESENCELATE+ "=?, "
					+ IDBConstants.ATTR_PRESENCENOTLATE+ "=?, "
					+ IDBConstants.ATTR_PRESENCE + "=? " + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + edu.getEmployeeIndex()
			);

			stm.setInt(3, new_name.getPresence() );
			stm.setInt(1, new_name.getPresenceLate() );
			stm.setInt(2, new_name.getPresenceNotLate() );
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeePayroll(Connection conn,
			EmployeePayrollSubmit empPayroll) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL + "("
					+ IDBConstants.ATTR_STATUS + ","
					+ IDBConstants.ATTR_SUBMITTED_DATE + ","
					+ IDBConstants.ATTR_UNIT+ ","
					+ IDBConstants.ATTR_YEAR + ","
					+ IDBConstants.ATTR_MONTH + ","
					+ IDBConstants.ATTR_PAYMENT_PERIODE + ","
					+ IDBConstants.ATTR_PAYROLL_TYPE + ","
					+ IDBConstants.ATTR_TRANSACTION + ","
					+ IDBConstants.ATTR_PAYCHEQUE_TYPE + ")" + " values (?, ?, ?, ?, ?, ?, ?, ?,?)");

			stm.setInt(1, empPayroll.getStatus());
			stm.setDate(2, new java.sql.Date(empPayroll.getSubmittedDate().getTime()));
			stm.setLong(3, empPayroll.getUnit().getIndex());
			stm.setInt(4, empPayroll.getYear());
			stm.setInt(5, empPayroll.getMonth());
			stm.setInt(6, empPayroll.getPaymentPeriode());
			stm.setFloat(7, empPayroll.getPayrollType());
			stm.setLong(8, empPayroll.getTransaction());
			stm.setLong(9, empPayroll.getPaychequeType());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeePayrollSubmit[] getEmployeePayrollSubmit(Connection conn,EmployeePayrollSubmit employeePayrollSubmit)throws SQLException {
		if (employeePayrollSubmit instanceof EmployeeMealAllowanceSubmit) {
			return getEmployeeMealAllowanceSubmit(conn,(EmployeeMealAllowanceSubmit) employeePayrollSubmit);
		}else if (employeePayrollSubmit instanceof EmployeeTransportationAllowance) {
			return getEmployeeTransportationAll(conn, (EmployeeTransportationAllowance) employeePayrollSubmit);
		}else if (employeePayrollSubmit instanceof EmployeeOvertimeSubmit) {
			return getEmployeeOvertimePayrollSubmit(conn, (EmployeeOvertimeSubmit) employeePayrollSubmit);
		}else if (employeePayrollSubmit instanceof EmployeePayrollTaxArt21) {
			return getEmployeeTaxArt21AllowanceAttr(conn, (EmployeePayrollTaxArt21) employeePayrollSubmit);
		}else{
			return getEmployeePayrollSubmitDetail(conn, employeePayrollSubmit);
		}

	}
	public EmployeePayrollSubmit[] getAllPostedEmployeePayrollSubmit(Connection conn,
			int month, int year, Unit unit, Employee_n employee)throws SQLException {

		String query=
		"select distinct " +//SELECT
		"a.*,b.*,b.trans,b.submitteddate,d.employeeno,d.firstname,d.midlename,d.lastname " +
		"from " +//FROM
		"employeepayrolldetail a,employeepayroll b, " +
		"employee d " +
		"where " +//WHERE
		"a.employeepayroll=b.autoindex  " +
		"and a.employee=d.autoindex " +
		"and b.yearpayrollsubmit=" + year +
		" and b.monthpayrollsubmit=" + month +
		" and b.unit=" + unit.getIndex() +
		" and b.status=" + PayrollSubmitStatus.POSTED+
		" and a.employee=" + employee.getAutoindex() +
		" and a.valuesubmit>-1 " + // semoga ini bisa ngesolve
		" order by employee" ;

		System.out.println(query);
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeePayrollSubmit  empPayroll=null;
			Vector vector=new Vector();
			while (rs.next()) {
				empPayroll=new EmployeePayrollSubmit();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empPayroll.setName(name);
				empPayroll.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empPayroll.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empPayroll.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empPayroll.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empPayroll.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empPayroll.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empPayroll.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				empPayroll.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE));
				empPayroll.setTransaction(rs.getLong(IDBConstants.ATTR_TRANSACTION));
				vector.add(empPayroll);
			}
			EmployeePayrollSubmit[]  employeePayrollSub=new EmployeePayrollSubmit[vector.size()];
			vector.copyInto(employeePayrollSub);
			return employeePayrollSub;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}


	public EmployeePayrollSubmit[] getEmployeePayrollSubmitByCriteria(Connection conn,
			EmployeePayrollSubmit employeePayrollSubmit,String query)throws SQLException {
		if (employeePayrollSubmit instanceof EmployeeMealAllowanceSubmit) {
			return getEmployeeMealAllowanceSubmitByCriteria(conn,(EmployeeMealAllowanceSubmit) employeePayrollSubmit,query);
		}else if (employeePayrollSubmit instanceof EmployeeTransportationAllowance) {
			return getEmployeeTransportationByCritria(conn, (EmployeeTransportationAllowance) employeePayrollSubmit,query);
		}else if (employeePayrollSubmit instanceof EmployeeOvertimeSubmit) {
			return getEmployeeOvertimePayrollSubmitByCriteria(conn, (EmployeeOvertimeSubmit) employeePayrollSubmit,query);
		}else{
			return getEmployeePayrollSubmitDetailbyCriteria(conn, employeePayrollSubmit,query);
		}
	}

	public EmployeePayrollSubmit[] getEmployeeMealAllowanceSubmitByCriteria(Connection conn,EmployeeMealAllowanceSubmit employeeMeal,String query)throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeeMealAllowanceSubmit  empMealAllowance=null;
			Vector vector=new Vector();
			while (rs.next()) {
				empMealAllowance=new EmployeeMealAllowanceSubmit();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empMealAllowance.setName(name);
				empMealAllowance.setPresence(rs.getInt(IDBConstants.ATTR_PRESENCE )) ;
				empMealAllowance.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empMealAllowance.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empMealAllowance.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empMealAllowance.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empMealAllowance.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empMealAllowance.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empMealAllowance.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE) );
				empMealAllowance.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				vector.add(empMealAllowance);
			}
			EmployeeMealAllowanceSubmit[]  mealAllowanceSubmit=new EmployeeMealAllowanceSubmit[vector.size()];
			vector.copyInto(mealAllowanceSubmit);
			return mealAllowanceSubmit;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}
	public EmployeeTransportationAllowance[] getEmployeeTransportationByCritria(Connection conn,EmployeeTransportationAllowance employeePayrollSubmit,String query)throws SQLException {

		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeeTransportationAllowance  empTransportationAllow=null;
			Vector vector=new Vector();
			while (rs.next()) {
				empTransportationAllow=new EmployeeTransportationAllowance();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empTransportationAllow.setName(name);
				empTransportationAllow.setPresence(rs.getInt(IDBConstants.ATTR_PRESENCE )) ;
				empTransportationAllow.setPresenceLate(rs.getInt(IDBConstants.ATTR_PRESENCELATE )) ;
				empTransportationAllow.setPresenceNotLate(rs.getInt(IDBConstants.ATTR_PRESENCENOTLATE )) ;
				empTransportationAllow.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empTransportationAllow.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empTransportationAllow.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empTransportationAllow.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empTransportationAllow.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empTransportationAllow.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empTransportationAllow.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE) );
				empTransportationAllow.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				vector.add(empTransportationAllow);
			}
			EmployeeTransportationAllowance[]  Transportation=new EmployeeTransportationAllowance[vector.size()];
			vector.copyInto(Transportation);
			return Transportation;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}

	public EmployeeOvertimeSubmit[] getEmployeeOvertimePayrollSubmitByCriteria(Connection conn,EmployeeOvertimeSubmit employeePayrollSubmit,String query)throws SQLException {

		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeeOvertimeSubmit  employeeOvertimeSub=null;
			Vector vector=new Vector();
			while (rs.next()) {
				employeeOvertimeSub= new EmployeeOvertimeSubmit();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				employeeOvertimeSub.setName(name);
				employeeOvertimeSub.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				employeeOvertimeSub.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				employeeOvertimeSub.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				employeeOvertimeSub.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				employeeOvertimeSub.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				employeeOvertimeSub.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				employeeOvertimeSub.setMultiplierIndex(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER));
				employeeOvertimeSub.setOvertimeValue(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE));
				employeeOvertimeSub.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE) );
				employeeOvertimeSub.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 4){
					employeeOvertimeSub.setOverTimeEightHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE )) ;
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 1){
					employeeOvertimeSub.setOverTimeLESSThanOneHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE )) ;
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 5){
					employeeOvertimeSub.setOverTimeMoreThanNineHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE )) ;
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 3){
					employeeOvertimeSub.setOverTimeMOREThanOneHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE ));
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )==2){
					employeeOvertimeSub.setOverTimeMoreThanNineHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE ));
				}
				vector.add(employeeOvertimeSub);
			}
			EmployeeOvertimeSubmit[]  overtimeSubmit=new EmployeeOvertimeSubmit[vector.size()];
			vector.copyInto(overtimeSubmit);
			return overtimeSubmit;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}

	public EmployeePayrollSubmit[] getEmployeePayrollSubmitDetailbyCriteria(Connection conn,EmployeePayrollSubmit employeeMeal,String query)throws SQLException {

		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeePayrollSubmit  empPayrollSubmit=null;
			Vector vector=new Vector();
			while (rs.next()) {
				empPayrollSubmit=new EmployeePayrollSubmit();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empPayrollSubmit.setName(name);
				empPayrollSubmit.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empPayrollSubmit.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empPayrollSubmit.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empPayrollSubmit.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empPayrollSubmit.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empPayrollSubmit.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empPayrollSubmit.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				empPayrollSubmit.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE));
				vector.add(empPayrollSubmit);
			}
			EmployeePayrollSubmit[]  EmployeePayrollSub=new EmployeePayrollSubmit[vector.size()];
			vector.copyInto(EmployeePayrollSub);
			return EmployeePayrollSub;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeePayrollSubmit[] getEmployeePayrollSubmitDetail(Connection conn,EmployeePayrollSubmit employeeSubmitExample)throws SQLException {
		String query="select distinct a.*,b.*,b.trans,b.submitteddate,d.employeeno,d.firstname,d.midlename,d.lastname " +
		"from employeepayrolldetail a,employeepayroll b, " +
		"employee d where a.employeepayroll=b.autoindex  " +
		"and a.employee=d.autoindex "+
		" and  b.yearpayrollsubmit="+employeeSubmitExample.getYear() +
		" and b.monthpayrollsubmit="+employeeSubmitExample.getMonth() +
		" and b.unit="+ employeeSubmitExample.getUnit().getIndex()+
		" and b.payrolltype="+employeeSubmitExample.getPayrollType() +
		" and b.paychequetype="+employeeSubmitExample.getPaychequeType() + " order by employee" ;

		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeePayrollSubmit  empPayrollSubmit=null;
			Vector vector=new Vector();
			while (rs.next()) {
				empPayrollSubmit=new EmployeePayrollSubmit();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empPayrollSubmit.setName(name);
				empPayrollSubmit.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empPayrollSubmit.setEmployeePayroll (rs.getLong(IDBConstants.ATTR_EMPLOYEE_PAYROLL ));
				empPayrollSubmit.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empPayrollSubmit.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empPayrollSubmit.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empPayrollSubmit.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empPayrollSubmit.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empPayrollSubmit.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				empPayrollSubmit.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE));
				empPayrollSubmit.setTransaction(rs.getLong(IDBConstants.ATTR_TRANSACTION));
				empPayrollSubmit.setPaychequeType(rs.getInt(IDBConstants.ATTR_PAYCHEQUE_TYPE));
				vector.add(empPayrollSubmit);
			}
			EmployeePayrollSubmit[]  employeePayrollSub=new EmployeePayrollSubmit[vector.size()];
			vector.copyInto(employeePayrollSub);
			return employeePayrollSub;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeePayrollSubmit[] getEmployeeMealAllowanceSubmit(Connection conn,EmployeeMealAllowanceSubmit employeeMeal)throws SQLException {
		String query =
			"select distinct  " +
		"a.*,b.*,b.trans,b.submitteddate,d.employeeno,c.presence,d.firstname,d.midlename," +
		"d.lastname from employeepayrolldetail a,employeepayroll b," +
		" mealallowanceattribute c, " +
		"employee d " +
			"where " +
		"a.employeepayroll = b.autoindex and " +
		"b.autoindex = c.employeepayroll and a.employee=d.autoindex " +
		"and c.employee=d.autoindex"+
		" and  b.yearpayrollsubmit="+employeeMeal.getYear() +
		" and b.monthpayrollsubmit="+employeeMeal.getMonth() +
		" and b.unit="+ employeeMeal.getUnit().getIndex()+
		" and b.payrolltype="+employeeMeal.getPayrollType()+" order by employee" ;
		Statement stm = null;

		System.out.println(query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeeMealAllowanceSubmit  empMealAllowSubmit=null;
			Vector vector=new Vector();
			while (rs.next()) {//
				empMealAllowSubmit=new EmployeeMealAllowanceSubmit();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empMealAllowSubmit.setName(name);
				empMealAllowSubmit.setPresence(rs.getInt(IDBConstants.ATTR_PRESENCE )) ;
				empMealAllowSubmit.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empMealAllowSubmit.setEmployeePayroll (rs.getLong(IDBConstants.ATTR_EMPLOYEE_PAYROLL));
				empMealAllowSubmit.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empMealAllowSubmit.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empMealAllowSubmit.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empMealAllowSubmit.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empMealAllowSubmit.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empMealAllowSubmit.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE) );
				empMealAllowSubmit.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				empMealAllowSubmit.setTransaction(rs.getLong(IDBConstants.ATTR_TRANSACTION));
				vector.add(empMealAllowSubmit);
			}
			EmployeeMealAllowanceSubmit[]  mealAllowanceSubmit=new EmployeeMealAllowanceSubmit[vector.size()];
			vector.copyInto(mealAllowanceSubmit);
			return mealAllowanceSubmit;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}
	public EmployeePayrollSubmit[] getEmployeeTaxArt21AllowanceAttr(Connection conn,EmployeePayrollTaxArt21 employeeMeal)throws SQLException {
		String query =
			"select distinct  " +
			"a.*,b.*,b.trans,b.submitteddate,d.employeeno,c.TAXART21COMP,d.firstname,d.midlename," +
			"d.lastname from employeepayrolldetail a,employeepayroll b," +
			" TAXART21ALLOWANCEATTR c, " +
			"employee d " +
			"where " +
			"a.employeepayroll = b.autoindex and " +
			"a.autoindex = c.employeepayrolldetail and a.employee=d.autoindex " +
			" and  b.yearpayrollsubmit="+employeeMeal.getYear() +
			" and b.monthpayrollsubmit="+employeeMeal.getMonth() +
			" and b.unit="+ employeeMeal.getUnit().getIndex()+
			" and b.payrolltype="+employeeMeal.getPayrollType()+" order by employee" ;
		Statement stm = null;

		System.out.println(query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeePayrollTaxArt21  empTaxArt21=null;
			Vector vector=new Vector();
			while (rs.next()) {//
				empTaxArt21=new EmployeePayrollTaxArt21();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empTaxArt21.setName(name);
				TaxArt21Component x = getTaxArt21Component(rs.getLong(IDBConstants.ATTR_TAXART21_COMPONENT),conn,null);
				empTaxArt21.setTaxArt21Component(x) ;
				empTaxArt21.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empTaxArt21.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empTaxArt21.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empTaxArt21.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empTaxArt21.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empTaxArt21.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empTaxArt21.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE) );
				empTaxArt21.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				empTaxArt21.setTransaction(rs.getLong(IDBConstants.ATTR_TRANSACTION));
				vector.add(empTaxArt21);
			}
			EmployeePayrollTaxArt21[]  mealAllowanceSubmit=new EmployeePayrollTaxArt21[vector.size()];
			vector.copyInto(mealAllowanceSubmit);
			return mealAllowanceSubmit;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}
	public EmployeeTransportationAllowance[] getEmployeeTransportationAll(Connection conn,EmployeeTransportationAllowance employeePayrollSubmit)throws SQLException {
		String query="select distinct a.*,b.*,b.trans,b.submitteddate,d.employeeno,c.presence,c.presencelate,c.presencenotlate,d.firstname," +
		"d.midlename,d.lastname from employeepayrolldetail a,employeepayroll b, " +
		"transportationallowattr c,employee d where a.employeepayroll=b.autoindex  " +
		"and b.autoindex=c.employeepayroll and a.employee=d.autoindex "+
		" and c.employee=d.autoindex "+
		" and  b.yearpayrollsubmit="+employeePayrollSubmit.getYear() +
		" and b.monthpayrollsubmit="+employeePayrollSubmit.getMonth() +
		" and b.unit="+ employeePayrollSubmit.getUnit().getIndex()+
		" and b.paymentperiode="+ employeePayrollSubmit.getPaymentPeriode()+//<----
		" and b.payrolltype="+employeePayrollSubmit.getPayrollType() +" order by employee" ;
		Statement stm = null;

		System.out.println(query);

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeeTransportationAllowance  empTransAllowSubmit=null;
			Vector vector=new Vector();
			while (rs.next()) {
				empTransAllowSubmit=new EmployeeTransportationAllowance();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empTransAllowSubmit.setName(name);
				empTransAllowSubmit.setPresence(rs.getInt(IDBConstants.ATTR_PRESENCE )) ;
				empTransAllowSubmit.setPresenceLate(rs.getInt(IDBConstants.ATTR_PRESENCELATE )) ;
				empTransAllowSubmit.setPresenceNotLate(rs.getInt(IDBConstants.ATTR_PRESENCENOTLATE )) ;
				empTransAllowSubmit.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empTransAllowSubmit.setEmployeePayroll (rs.getLong(IDBConstants.ATTR_EMPLOYEE_PAYROLL ));
				empTransAllowSubmit.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empTransAllowSubmit.setValue(rs.getFloat(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empTransAllowSubmit.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empTransAllowSubmit.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empTransAllowSubmit.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empTransAllowSubmit.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE) );
				empTransAllowSubmit.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				empTransAllowSubmit.setTransaction(rs.getLong(IDBConstants.ATTR_TRANSACTION));
				vector.add(empTransAllowSubmit);
			}
			EmployeeTransportationAllowance[]  Transportation=new EmployeeTransportationAllowance[vector.size()];
			vector.copyInto(Transportation);
			return Transportation;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}
	public EmployeeOvertimeSubmit[] getEmployeeOvertimePayrollSubmit(Connection conn,EmployeeOvertimeSubmit employeePayrollSubmit)throws SQLException {
		/*String query="select distinct a.*,b.*,b.trans,b.submitteddate,d.employeeno,c.overtimemultiplier,c.overtimevalue,d.firstname," +
		"d.midlename,d.lastname from employeepayrolldetail a,employeepayroll b," +
		"overtimeattribute c,employee d where a.employeepayroll=b.autoindex  and " +
		"b.autoindex=c.employeepayroll and a.employee=d.autoindex"+
		" and c.employee=d.autoindex "+
		" and  b.yearpayrollsubmit="+employeePayrollSubmit.getYear() +
		" and b.monthpayrollsubmit="+employeePayrollSubmit.getMonth() +
		" and b.unit="+ employeePayrollSubmit.getUnit().getIndex()+
		" and b.payrolltype="+employeePayrollSubmit.getPayrollType() +" order by employee, payrollcomponent";*/
		String query = "select a.*,b.overtimemultiplier,b.overtimevalue from " +
				"(select a.*,d.employeeno,d.firstname,d.midlename,d.lastname,b.trans,b.status,b.submitteddate " +
				"from employeepayrolldetail a,employeepayroll b, employee d " +
				"where a.employee=d.autoindex and b.autoindex=a.employeepayroll and " +
				"a.employeepayroll=(select autoindex from employeepayroll b where " +
				"b.yearpayrollsubmit="+employeePayrollSubmit.getYear() +" and b.monthpayrollsubmit="+employeePayrollSubmit.getMonth() +" and b.unit="+ employeePayrollSubmit.getUnit().getIndex()+" and b.payrolltype="+employeePayrollSubmit.getPayrollType() +")) a, " +
				"(select distinct employeepayroll,employee,overtimemultiplier,overtimevalue from overtimeattribute where " +
				"employeepayroll=(select autoindex from employeepayroll b where " +
				"b.yearpayrollsubmit="+employeePayrollSubmit.getYear() +" and b.monthpayrollsubmit="+employeePayrollSubmit.getMonth() +" and b.unit="+ employeePayrollSubmit.getUnit().getIndex()+" and b.payrolltype="+employeePayrollSubmit.getPayrollType() +")) b " +
				"where a.employee=b.employee order by a.employee,a.payrollcomponent";

		System.out.println(query);

		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			EmployeeOvertimeSubmit  empOvertimeSubmit=null;
			Vector vector=new Vector();
			while (rs.next()) {
				empOvertimeSubmit= new EmployeeOvertimeSubmit();
				String firstname=rs.getString(IDBConstants.ATTR_FIRST_NAME );
				String middlename=rs.getString(IDBConstants.ATTR_MIDLE_NAME );
				String lastname= rs.getString(IDBConstants.ATTR_LAST_NAME );
				String spacer=" ";
				if(!middlename.equals("")){
					spacer=" "+middlename+" ";
				}
				String name=firstname+spacer+lastname;
				empOvertimeSubmit.setName(name);
				empOvertimeSubmit.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				empOvertimeSubmit.setEmployeePayroll(rs.getLong(IDBConstants.ATTR_EMPLOYEE_PAYROLL ));
				empOvertimeSubmit.setStatus(rs.getInt(IDBConstants.ATTR_STATUS));
				empOvertimeSubmit.setValue(rs.getDouble(IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL )) ;
				empOvertimeSubmit.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				empOvertimeSubmit.setEmployeeIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE));
				empOvertimeSubmit.setPayrollComponentIndex(rs.getLong(IDBConstants.ATTR_PAYROLL_COMPONENT));
				empOvertimeSubmit.setMultiplierIndex(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER));
				empOvertimeSubmit.setOvertimeValue(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE));
				empOvertimeSubmit.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE) );
				empOvertimeSubmit.setEmployeeNo(rs.getString(IDBConstants.ATTR_EMPLOYEE_NO));
				empOvertimeSubmit.setTransaction(rs.getLong(IDBConstants.ATTR_TRANSACTION));
				if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 4){
					empOvertimeSubmit.setOverTimeEightHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE )) ;
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 1){
					empOvertimeSubmit.setOverTimeLESSThanOneHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE )) ;
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 5){
					empOvertimeSubmit.setOverTimeMoreThanNineHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE )) ;
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )== 3){
					empOvertimeSubmit.setOverTimeMOREThanOneHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE ));
				}else if(rs.getLong(IDBConstants.ATTR_OVERTIMEMULTIPLIER )==2){
					empOvertimeSubmit.setOverTimeMoreThanNineHour(rs.getFloat(IDBConstants.ATTR_OVERTIMEVALUE ));
				}
				vector.add(empOvertimeSubmit);
			}
			EmployeeOvertimeSubmit[]  overtimeSubmit=new EmployeeOvertimeSubmit[vector.size()];
			vector.copyInto(overtimeSubmit);
			return overtimeSubmit;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}




	public void createEmployeeMealAllowance(Connection conn,
			EmployeeMealAllowanceSubmit payrollMealAllowanceData) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_MEAL_ALLOWANCE + "("
					+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + ","
					+ IDBConstants.ATTR_EMPLOYEE + ","
					+ IDBConstants.ATTR_PRESENCE + ")" + " values (?, ?, ?)");

			stm.setLong(1, payrollMealAllowanceData.getIndex() );
			stm.setLong(2, payrollMealAllowanceData.getEmployee_n().getAutoindex());
			stm.setLong(3, payrollMealAllowanceData.getPresence());

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public void createTransportationAllowance(Connection conn,
			EmployeeTransportationAllowance payrollMealAllowanceData) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_TRANSPORTATION_ALLOWANCE + "("
					+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + ","
					+ IDBConstants.ATTR_EMPLOYEE + ","
					+ IDBConstants.ATTR_PRESENCE+ ","
					+ IDBConstants.ATTR_PRESENCELATE + ","
					+ IDBConstants.ATTR_PRESENCENOTLATE + ")" + " values (?, ?, ?, ?,?)");

			stm.setLong(1, payrollMealAllowanceData.getIndex() );
			stm.setLong(2, payrollMealAllowanceData.getEmployee_n().getAutoindex());
			stm.setInt(3, payrollMealAllowanceData.getPresence());
			stm.setInt(4, payrollMealAllowanceData.getPresenceLate());
			stm.setInt(5, payrollMealAllowanceData.getPresenceNotLate());

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}
	public void createTaxArt21Allowance(Connection conn,
			EmployeePayrollTaxArt21 employeePayrollTaxArt21,long indexx) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_TAXART21_ALLOWANCE + "("
					+ IDBConstants.ATTR_EMPLOYEE_PAYROLL_DETAIL + ","
					+ IDBConstants.ATTR_TAXART21_COMPONENT+ ")" + " values (?, ?)");

			stm.setLong(1, employeePayrollTaxArt21.getIndex());
			stm.setLong(2, employeePayrollTaxArt21.getTaxArt21Component().getIndex());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}


	public void createOvertimePayrolSubmit(Connection conn,
			EmployeeOvertimeSubmit payrollMealAllowanceData) throws SQLException {
		PreparedStatement stm = null;

		try {
			toOvertimeAttribute(conn, payrollMealAllowanceData,payrollMealAllowanceData.getMultipOverTimeEightHour(), payrollMealAllowanceData.getOverTimeEightHour());
			toOvertimeAttribute(conn, payrollMealAllowanceData,payrollMealAllowanceData.getMultipOverTimeLESSThanOneHour(), payrollMealAllowanceData.getOverTimeLESSThanOneHour());
			toOvertimeAttribute(conn, payrollMealAllowanceData,payrollMealAllowanceData.getMultipOverTimeMoreThanNineHour(), payrollMealAllowanceData.getOverTimeMoreThanNineHour());
			toOvertimeAttribute(conn, payrollMealAllowanceData,payrollMealAllowanceData.getMultipOverTimeMOREThanOneHour(), payrollMealAllowanceData.getOverTimeMOREThanOneHour());
			toOvertimeAttribute(conn, payrollMealAllowanceData,payrollMealAllowanceData.getMultipOverTimeZeroToSevenHour(), payrollMealAllowanceData.getOverTimeZeroToSevenHour());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	private void toOvertimeAttribute(Connection conn, EmployeeOvertimeSubmit payrollMealAllowanceData,long index,float value) throws SQLException {
		PreparedStatement stm;
		stm = conn.prepareStatement("INSERT INTO "
				+ IDBConstants.TABLE_OVERTIMEATTRIBUTE + "("
				+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + ","
				+ IDBConstants.ATTR_EMPLOYEE + ","
				+ IDBConstants.ATTR_OVERTIMEMULTIPLIER + ","
				+ IDBConstants.ATTR_OVERTIMEVALUE + ")" + " values (?, ?, ?, ?)");


		stm.setLong(1, payrollMealAllowanceData.getIndex() );
		stm.setLong(2, payrollMealAllowanceData.getEmployee_n().getAutoindex());
		stm.setLong(3, index );
		stm.setFloat(4, value );
		stm.executeUpdate();
	}
	public void createEmployeePayrollDetail(Connection conn,
			EmployeePayrollSubmit employeePaySubDetail) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL + "("
					+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + ","
					+ IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL + ","
					+ IDBConstants.ATTR_EMPLOYEE+ ","
					+ IDBConstants.ATTR_JOB_TITLE+ ","
					+ IDBConstants.ATTR_PAYROLL_COMPONENT + ")" + " values (?, ?, ?, ?, ?)");

			stm.setLong(1, employeePaySubDetail.getIndex() );
			stm.setDouble(2, employeePaySubDetail.getValue() );
			stm.setLong(3, employeePaySubDetail.getEmployee_n().getAutoindex());
			stm.setString(4, employeePaySubDetail.getEmployee_n().getJobtitle());
			if(employeePaySubDetail.getPayrollComponent()!=null)
				stm.setLong(5, employeePaySubDetail.getPayrollComponent().getIndex());
			else
				stm.setString(5, null);
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createEmployeePayrollComponent(long employeeIndex,
			PayrollCategoryComponent component, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT + "("
					+ IDBConstants.ATTR_EMPLOYEE + ","
					+ IDBConstants.ATTR_COMPONENT + ","
					+ IDBConstants.ATTR_FORMULA + ","
					+ IDBConstants.ATTR_FORMULA_MONTH + ","
					+ IDBConstants.ATTR_ROUND_VALUE + ","
					+ IDBConstants.ATTR_PRECISION + ")" + " values (?, ?, ?, ?, ?, ?)");

			stm.setLong(1, employeeIndex);
			stm.setLong(2, component.getPayrollComponent().getIndex());
			stm.setString(3, component.getFormulaEntity().getFormulaCode());
			stm.setInt(4, (int)component.getEveryWhichMonth());
			stm.setInt(5, (int)component.getFormulaEntity().getNumberRounding().getRoundingMode());
			stm.setInt(6, component.getFormulaEntity().getNumberRounding().getPrecision());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
			//throw new SQLException(ex);
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateEmployeePayrollComponent(long index,
			PayrollCategoryComponent component, Connection conn)
	throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT + " SET "
					+ IDBConstants.ATTR_COMPONENT + "=?, "
					+ IDBConstants.ATTR_FORMULA + "=?, "
					+ IDBConstants.ATTR_FORMULA_MONTH + "=?, "
					+ IDBConstants.ATTR_ROUND_VALUE + "=?, "
					+ IDBConstants.ATTR_PRECISION + "=? "
					+ " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setLong(1, component.getPayrollComponent().getIndex());
			stm.setString(2, component.getFormulaEntity().getFormulaCode());
			stm.setInt(3, (int)component.getEveryWhichMonth());
			stm.setInt(4, (int)component.getFormulaEntity().getNumberRounding().getRoundingMode());
			stm.setInt(5, component.getFormulaEntity().getNumberRounding().getPrecision());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeePayrollComponent(long index, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_AUTOINDEX + "=" + index);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteEmployeePayrollComponentByEmployee(long employeeIndex, Connection conn)
	throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeIndex);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public int countPayrollCategoryEmployeeByEmployee(long sessionid, Connection conn,
			String modul, long employeeIndex) throws SQLException {

		Statement stm = null;
		String query = "SELECT COUNT(*) COUNTEMP FROM "
			+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE + " "
			+ "WHERE "
			+ IDBConstants.ATTR_EMPLOYEE + "=" + employeeIndex;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("COUNTEMP");
			}

			return count;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeLeavePermissionHistory[] getEmployeeLeavePermissionHistory(
			Connection conn, long employeeIndex) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			String query =
				"select autoindex, type, reason, "
				+ "proposedate, attrfrom, attrto, days, "
				+ "replaced, checked, checkeddate, "
				+ "approved, approveddate, deduction from ("
				+ "select l.autoindex, l.employee, 'Leave' type, lt.description reason, "
				+ "l.proposedate, l.attrfrom, l.attrto, l.days, "
				+ "ep.firstname & ' ' & ep.midlename & ' ' & ep.lastname replaced, "
				+ "ec.firstname & ' ' & ec.midlename & ' ' & ec.lastname checked, "
				+ "l.checkeddate, "
				+ "ea.firstname & ' ' & ea.midlename & ' ' & ea.lastname approved, "
				+ "l.approveddate, lt.deduction deduction from employeeleave l "
				+ "left join leavetype lt on l.reason=lt.autoindex "
				+ "left join employee ep on l.replaced=ep.autoindex "
				+ "left join employee ec on l.checked=ec.autoindex "
				+ "left join employee ea on l.approved=ea.autoindex "
				+ "union all "
				+ "select p.autoindex, p.employee, 'Permission' type, pt.description reason, "
				+ "p.proposedate, p.attrfrom, p.attrto, p.days, "
				+ "ep.firstname & ' ' & ep.midlename & ' ' & ep.lastname replaced, "
				+ "ec.firstname & ' ' & ec.midlename & ' ' & ec.lastname checked, "
				+ "p.checkeddate, "
				+ "ea.firstname & ' ' & ea.midlename & ' ' & ea.lastname approved, "
				+ "p.approveddate, pt.deduction deduction from employepermition p "
				+ "left join permitiontype pt on p.reason=pt.autoindex "
				+ "left join employee ep on p.replaced=ep.autoindex "
				+ "left join employee ec on p.checked=ec.autoindex "
				+ "left join employee ea on p.approved=ea.autoindex "
				+ ") where employee=" + employeeIndex;

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmployeeLeavePermissionHistory(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_TYPE),
						rs.getString(IDBConstants.ATTR_REASON),
						rs.getDate(IDBConstants.ATTR_PROPOSE_DATE),
						rs.getDate(IDBConstants.ATTR_FROM),
						rs.getDate(IDBConstants.ATTR_TO),
						rs.getInt(IDBConstants.ATTR_DAYS),
						rs.getString(IDBConstants.ATTR_REPLACED),
						rs.getString(IDBConstants.ATTR_CHECKED),
						rs.getDate(IDBConstants.ATTR_CHECKED_DATE),
						rs.getString(IDBConstants.ATTR_APPROVED),
						rs.getDate(IDBConstants.ATTR_APPROVED_DATE),
						rs.getBoolean(IDBConstants.ATTR_DEDUCTION)));
			}

			EmployeeLeavePermissionHistory[] result = new EmployeeLeavePermissionHistory[vresult
			                                                                             .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeLeavePermissionHistory[] getEmployeeLeavePermissionHistoryByQuery(
			Connection conn, String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmployeeLeavePermissionHistory(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_TYPE),
						rs.getString(IDBConstants.ATTR_REASON),
						rs.getDate(IDBConstants.ATTR_PROPOSE_DATE),
						rs.getDate(IDBConstants.ATTR_FROM),
						rs.getDate(IDBConstants.ATTR_TO),
						rs.getInt(IDBConstants.ATTR_DAYS),
						rs.getString(IDBConstants.ATTR_REPLACED),
						rs.getString(IDBConstants.ATTR_CHECKED),
						rs.getDate(IDBConstants.ATTR_CHECKED_DATE),
						rs.getString(IDBConstants.ATTR_APPROVED),
						rs.getDate(IDBConstants.ATTR_APPROVED_DATE),
						rs.getBoolean(IDBConstants.ATTR_DEDUCTION)));
			}

			EmployeeLeavePermissionHistory[] result = new EmployeeLeavePermissionHistory[vresult
			                                                                             .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public LeavePermissionType[] getAllLeavePermissionType(Connection conn,
			short type)
	throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			String query = "select * from "
				+ "(select 0 type, lt.autoindex, lt.code, lt.description "
				+ "from leavetype lt "
				+ "union all "
				+ "select 1 type, pt.autoindex, pt.code, pt.description "
				+ "from permitiontype pt)";

			if(type==0 || type==1)
				query += " where type=" + type;

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new LeavePermissionType(rs
						.getShort(IDBConstants.ATTR_TYPE), rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION)));
			}

			LeavePermissionType[] result = new LeavePermissionType[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	// tambahan nunung
	public EmployeeLeave[] getEmployeeLeave(
			Connection conn, String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmployeeLeave(
						rs.getLong("lv_1"),
						rs.getLong("lv_2"),
						rs.getLong("lv_3"),
						rs.getLong("lv_4")));
			}

			EmployeeLeave[] result = new EmployeeLeave[vresult
			                                           .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public CurrentEmployementRpt getCurrentEmployementRpt(
			Connection conn,long index) throws SQLException {
		Statement stm = null;
		String query = "select c.employee,d.name jobtitle,e.name org,f.code,f.description " +
		"from (select a.autoindex ,max(b.tmt) tmt from employeeemployment b, employee a where b.employee=a.autoindex  group by a.autoindex) b," +
		"employeeemployment c," +
		"jobtitle d," +
		"organization e," +
		"unit f " +
		"where b.autoindex=c.employee and b.tmt=c.tmt and c.jobtitle=d.autoindex and c.department=e.autoindex " +
		"and c.unit=f.autoindex and b.autoindex in("+ index +")";
		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			if (rs.next()) {
				return new CurrentEmployementRpt(
						rs.getString("jobtitle"),
						rs.getString("org"),
						rs.getString("code"),
						rs.getString("description"));
			}

			return null;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmploymentHistoryRpt[] getEmploymentHistoryRpt(
			Connection conn, long index) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query ="select b.employee,d.name jobtitle,e.name org,c.code,c.description,b.tmt" +
		" from  employeeemployment b, " +
		"unit c," +
		"jobtitle d," +
		"organization e " +
		"where b.unit=c.autoindex and b.jobtitle=d.autoindex and b.department=e.autoindex " +
		"and b.employee in(" + index + ") order by tmt ";

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmploymentHistoryRpt(
						rs.getString("jobtitle"),
						rs.getString("org"),
						rs.getString("code"),
						rs.getString("description"),
						rs.getDate("tmt")));
			}

			EmploymentHistoryRpt[] result = new EmploymentHistoryRpt[vresult
			                                                         .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EducationRpt[] getEducationRpt(
			Connection conn, long index) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query ="select a.employee,b.code grade,a.majorstudy,a.institute,a.attrto graduate " +
		"from employeeeducation a, education b where a.grade=b.autoindex and " +
		"a.employee in (" + index + ") order by a.attrfrom";

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EducationRpt(
						rs.getString("grade"),
						rs.getString("majorstudy"),
						rs.getString("institute"),
						rs.getDate("graduate")));
			}

			EducationRpt[] result = new EducationRpt[vresult
			                                         .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public FamilyRpt[] getFamilyRpt(
			Connection conn, long index) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query ="select a.employee,c.description relation,a.name,a.birthplace,a.birthdate," +
		"b.code education,a.jobtitle job " +
		"from employeefamily a, " +
		"education b," +
		"familyrelation c " +
		"where a.education=b.autoindex and a.relation=c.autoindex and a.employee in (" + index + ") order by birthdate";

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new FamilyRpt(
						rs.getString("relation"),
						rs.getString("name"),
						rs.getString("birthplace"),
						rs.getDate("birthdate"),
						rs.getString("education"),
						rs.getString("job")));
			}

			FamilyRpt[] result = new FamilyRpt[vresult
			                                   .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public CertificationRpt[] getCertificationRpt(
			Connection conn, long index) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query ="select b.employee,b.certificateno,b.certificatedate,b.institute,c.name qualification,b.description,b.expiredate " +
		"from employee a," +
		"employeecertification b," +
		"qualification c " +
		"where a.autoindex=b.employee and b.qualification=c.autoindex " +
		"and a.autoindex in(" + index + ") order by expiredate";

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new CertificationRpt(
						rs.getString("certificateno"),
						rs.getDate("certificatedate"),
						rs.getString("institute"),
						rs.getString("qualification"),
						rs.getString("description"),
						rs.getDate("expiredate")));
			}

			CertificationRpt[] result = new CertificationRpt[vresult
			                                                 .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public BankRpt[] getBankRpt(
			Connection conn, long index) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query ="select a.autoindex,b.accountname, b.accountno,b.bankname,c.code currency " +
		"from employee a," +
		"employeeaccount b," +
		"currency c " +
		"where a.autoindex=b.employee and b.currency=c.autoindex " +
		"and a.autoindex in (" + index + ") order by bankname";

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new BankRpt(
						rs.getString("accountname"),
						rs.getString("accountno"),
						rs.getString("bankname"),
						rs.getString("currency")
				));
			}

			BankRpt[] result = new BankRpt[vresult
			                               .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PaychequesRpt[] getPaychequesRpt(
			Connection conn,long superlabel) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query ="select a.label subname ,a.type,b.* " +
		"from paychequelabel a," +
		"(select a.superlabel,b.label supername,a.sublabel from paychequelabelstructure a, paychequelabel b where a.superlabel=b.autoindex) b " +
		"where a.autoindex=b.sublabel and b.superlabel=" + superlabel;
		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new PaychequesRpt(
						rs.getString("subname"),
						rs.getInt("type"),
						rs.getLong("superlabel"),
						rs.getString("supername"),
						rs.getLong("sublabel")
				));
			}

			PaychequesRpt[] result = new PaychequesRpt[vresult
			                                           .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public PaychequesValueRpt[] getPaychequesValueRpt(
			Connection conn,long indexemployee,long superlabel , long paychequelabel,
			String bln_thn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query ="select a.label,b.* from  " +
		"paychequelabel a, " +
		"(select a.monthpayrollSubmit,a.yearpayrollsubmit,b.*,c.description,c.paychequelabel from employeepayroll a,employeepayrolldetail b,payrollcomponent c " +
		"where a.status in (0,1,2,3) " +  // untuk yang posted aja ding
		"and  payrolltype = 5 and a.autoindex=b.employeepayroll  and b.payrollcomponent=c.autoindex) b," +
		"paychequelabelstructure c " +
		"where a.autoindex=b.paychequelabel and b.employee=" + indexemployee + "  and a.autoindex=c.sublabel and c.superlabel = " + superlabel + " and  b.paychequelabel = " + paychequelabel + " and " + bln_thn;
		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new PaychequesValueRpt(
						rs.getLong("valuesubmit")
				));
			}

			PaychequesValueRpt[] result = new PaychequesValueRpt[vresult
			                                                     .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeAbsence[] getEmployeeAbsence(
			Connection conn, String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmployeeAbsence(
						rs.getLong("present"),
						rs.getLong("present_not_late"),
						rs.getLong("present_late"),
						rs.getLong("absent"),
						rs.getLong("field_visit"),
						rs.getLong("other")));
			}

			EmployeeAbsence[] result = new EmployeeAbsence[vresult
			                                               .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeOvertime[] getEmployeeOvertime(
			Connection conn, String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmployeeOvertime(
						rs.getFloat("workingday"),
						rs.getFloat("nonworkingday")));
			}

			EmployeeOvertime[] result = new EmployeeOvertime[vresult
			                                               .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeePermition[] getEmployeePermition(
			Connection conn, String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmployeePermition(
						rs.getLong("pm_1"),
						rs.getLong("pm_2"),
						rs.getLong("pm_3"),
						rs.getLong("pm_4"),
						rs.getLong("pm_5"),
						rs.getLong("pm_6"),
						rs.getLong("pm_99")));
			}

			EmployeePermition[] result = new EmployeePermition[vresult
			                                                   .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public EmployeeWorkTime[] getEmployeeWorkTime(
			Connection conn, String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new EmployeeWorkTime(
						rs.getString("workingdate"),
						rs.getInt("overtime"),
						rs.getInt("today")
				));
			}

			EmployeeWorkTime[] result = new EmployeeWorkTime[vresult
			                                                 .size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}


	public void updateValueEmployeePayrollDetail(EmployeePayrollSubmit emp,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_DETAIL + " SET "
					+ IDBConstants.ATTR_VALUEEMPLOYEEPAYROLL + "=? "
					+ "WHERE "
					+ IDBConstants.ATTR_EMPLOYEE_PAYROLL + "=? "
					+ "AND " + IDBConstants.ATTR_EMPLOYEE + "=? "
					+ "AND " + IDBConstants.ATTR_PAYROLL_COMPONENT + "=?");

			stm.setDouble(1, emp.getValue());
			stm.setLong(2, emp.getIndex());
			stm.setLong(3, emp.getEmployeeIndex());
			stm.setLong(4, emp.getPayrollComponentIndex());

			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public long getPaychequeSuperlabelAtIndex(Connection conn, int indexPosition) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();

			String query = "select distinct " + IDBConstants.ATTR_SUPER_LABEL + " from " +
			IDBConstants.TABLE_PAYCHEQUE_LABEL_STRUCTURE + " order by " + IDBConstants.ATTR_SUPER_LABEL;
			ResultSet rs = stm.executeQuery(query);

			int i = 0;
			long res = -1;
			while (rs.next()) {
				if(i == indexPosition){
					res = rs.getLong(IDBConstants.ATTR_SUPER_LABEL);
					break;
				}
				i++;
			}

			return res;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Account[] getDistinctTaxArt21Account(Connection conn)
			throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			String query = "select distinct taxaccount from taxart21component where isgroup=false";
			ResultSet rs = stm.executeQuery(query);

			AccountingSQLSAP acct = new AccountingSQLSAP();
			while (rs.next()) {
				Account account = acct.getAccount(rs.getLong("TAXACCOUNT"),
						conn);
				vresult.addElement(account);
			}

			Account[] result = new Account[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21Component[] getTaxArt21ComponentsByAccount(Connection conn, long index, IFormulaParser parser) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();

			String query = "select * from taxart21component where isgroup=false and taxaccount=" + index;
			System.err.println(query);
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				TaxArt21Component component = new TaxArt21Component(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_CODE), rs
						.getString(IDBConstants.ATTR_DESCRIPTION), rs
						.getBoolean(IDBConstants.ATTR_ISGROUP),
						new AccountingSQLSAP().getAccount(rs
								.getLong(IDBConstants.ATTR_TAXACCOUNT), conn),
						parser.parseToFormula(rs
								.getString(IDBConstants.ATTR_FORMULA)), rs
						.getString(IDBConstants.ATTR_NOTE), rs
						.getBoolean(IDBConstants.ATTR_ISROUNDED), rs
						.getInt(IDBConstants.ATTR_ROUND_VALUE), rs
						.getInt(IDBConstants.ATTR_PRECISION), rs
						.getBoolean(IDBConstants.ATTR_ISNEGATIVE), rs
						.getBoolean(IDBConstants.ATTR_ISCOMPARABLE), rs
						.getString(IDBConstants.ATTR_COMPARATION_MODE), rs
						.getDouble(IDBConstants.ATTR_COMPARATOR));
				vresult.addElement(component);
			}

			TaxArt21Component[] result = new TaxArt21Component[vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public double getPTKPbyEmployeeIndex(Connection conn, long autoindex) throws SQLException {
		Statement stm = null;

		double value = 0;

		try {
			stm = conn.createStatement();

			String query = "select e.autoindex, p.attvalue from employee e, ptkp p where e.art21=p.autoindex and e.autoindex=" + autoindex;
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				value = rs.getDouble("ATTVALUE");
			}

			return value;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTaxArt21Submit(Connection conn, TaxArt21Submit submit) throws SQLException {
		PreparedStatement stm = null;
		try {
			String query = "INSERT INTO " + IDBConstants.TABLE_TAX_ART_21_SUBMIT + "("
					+ IDBConstants.ATTR_STATUS + ", "
					+ IDBConstants.ATTR_SUBMITTED_DATE + ", "
					+ IDBConstants.ATTR_MONTH_SUBMITTED + ", "
					+ IDBConstants.ATTR_YEAR_SUBMITTED + ", "
					+ IDBConstants.ATTR_UNIT  + ", "
					+ IDBConstants.ATTR_TAXACCOUNT + ") "
					+ " VALUES (?, ? ,? ,? ,?, ?)";
			stm = conn.prepareStatement(query);

			stm.setInt(1, submit.getStatus());
			stm.setDate(2, new java.sql.Date(submit.getSubmittedDate().getTime()));
			stm.setInt(3, submit.getMonthSubmitted());
			stm.setInt(4, submit.getYearSubmitted());
			stm.setLong(5, submit.getUnit().getIndex());
			stm.setLong(6, submit.getTaxAccount().getIndex());

			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTaxArt21SubmitEmployeeDetail(Connection conn, long index, TaxArt21SubmitEmployeeDetail detail) throws SQLException {
		PreparedStatement stm = null;
		try {
			String query = "INSERT INTO " + IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL + "("
					+ IDBConstants.ATTR_TAX_ART_21_SUBMIT + ", "
					+ IDBConstants.ATTR_EMPLOYEE + ", "
					+ IDBConstants.ATTR_JOB_TITLE + ") "
					+ " VALUES (?, ? ,?)";
			stm = conn.prepareStatement(query);

			stm.setLong(1, index);
			stm.setLong(2, detail.getEmployee().getAutoindex());
			stm.setString(3, detail.getJobTitle());
			/*stm.setLong(4, detail.getTaxArt21Component().getIndex());
			if(detail.getValue()!=null)
				stm.setDouble(5, detail.getValue().doubleValue());
			else
				stm.setNull(5, Types.DOUBLE);*/

			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTaxArt21SubmitComponentDetail(Connection conn, long index, TaxArt21SubmitComponentDetail componentDetail) throws SQLException {
		PreparedStatement stm = null;
		try {
			String query = "INSERT INTO " + IDBConstants.TABLE_TAX_ART_21_SUBMIT_COMP_DETAIL + "("
					+ IDBConstants.ATTR_TAX_ART_21_SUBMIT_EMP_DETAIL + ", "
					+ IDBConstants.ATTR_TAXART21_COMPONENT + ", "
					+ IDBConstants.ATTR_VALUE + ") "
					+ " VALUES (?, ? ,?)";
			stm = conn.prepareStatement(query);

			stm.setLong(1, index);
			stm.setLong(2, componentDetail.getComponent().getIndex());
			if(componentDetail.getValue()!=null)
				stm.setDouble(3, componentDetail.getValue().doubleValue());
			else
				stm.setNull(3, Types.DOUBLE);

			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21Submit getTaxArt21Submit(Connection conn, int month, int year, Unit unitSelected, Account accountSelected) throws SQLException {
		Statement stm = null;

		TaxArt21Submit result = null;

		try {
			stm = conn.createStatement();

			String query = "SELECT * FROM " + IDBConstants.TABLE_TAX_ART_21_SUBMIT + " WHERE " +
				IDBConstants.ATTR_MONTH_SUBMITTED + "=" + month + " AND " +
				IDBConstants.ATTR_YEAR_SUBMITTED + "=" + year + " AND " +
				IDBConstants.ATTR_UNIT + "=" + unitSelected.getIndex() + " AND " +
				IDBConstants.ATTR_TAXACCOUNT + "=" + accountSelected.getIndex();

			ResultSet rs = stm.executeQuery(query);

			AccountingSQLSAP sapAcct = new AccountingSQLSAP();

			while (rs.next()) {
				result = new TaxArt21Submit();
				result.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				result.setStatus(rs.getShort(IDBConstants.ATTR_STATUS));
				result.setSubmittedDate(rs.getDate(IDBConstants.ATTR_SUBMITTED_DATE));
				result.setMonthSubmitted(rs.getInt(IDBConstants.ATTR_MONTH_SUBMITTED));
				result.setYearSubmitted(rs.getInt(IDBConstants.ATTR_YEAR_SUBMITTED));
				result.setUnit(sapAcct.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn));
				result.setTaxAccount(sapAcct.getAccount(rs.getLong(IDBConstants.ATTR_TAXACCOUNT), conn));
				long obj = rs.getLong(IDBConstants.ATTR_TRANSACTION);
				if(obj>0)
					result.setTransaction(sapAcct.getTransaction(conn, rs.getLong(IDBConstants.ATTR_TRANSACTION)));
			}

			return result;
		} catch (Exception ex) {
			System.err.println(ex);
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21SubmitComponentDetail[] getTaxArt21SubmitComponentDetail(Connection conn, long taxArt21SubmitEmployeeIndex,
			IFormulaParser parser) throws SQLException {
		Statement stm = null;

		Vector vector = new Vector();

		try {
			stm = conn.createStatement();

			String query = "SELECT * FROM " + IDBConstants.TABLE_TAX_ART_21_SUBMIT_COMP_DETAIL + " WHERE " +
				IDBConstants.ATTR_TAX_ART_21_SUBMIT_EMP_DETAIL + "=" + taxArt21SubmitEmployeeIndex;

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				TaxArt21SubmitComponentDetail result = new TaxArt21SubmitComponentDetail();
				result.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				result.setComponent(getTaxArt21Component(rs.getLong(IDBConstants.ATTR_TAXART21_COMPONENT), conn, parser));
				result.setValue(new Double(rs.getDouble(IDBConstants.ATTR_VALUE)));

				vector.add(result);
			}

			TaxArt21SubmitComponentDetail[] details =
				(TaxArt21SubmitComponentDetail[]) vector.toArray(new TaxArt21SubmitComponentDetail[vector.size()]);

			return details;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public TaxArt21SubmitEmployeeDetail[] getTaxArt21SubmitEmployeeDetail(Connection conn, long taxArt21SubmitIndex) throws SQLException {
		Statement stm = null;

		Vector vector = new Vector();

		try {
			stm = conn.createStatement();

			String query = "SELECT * FROM " + IDBConstants.TABLE_TAX_ART_21_SUBMIT_EMP_DETAIL + " WHERE " +
				IDBConstants.ATTR_TAX_ART_21_SUBMIT + "=" + taxArt21SubmitIndex;

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				TaxArt21SubmitEmployeeDetail result = new TaxArt21SubmitEmployeeDetail();
				result.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				Employee_n emp = getEmployee_nByIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE), conn);
				result.setEmployee(emp);
				result.setJobTitle(rs.getString(IDBConstants.ATTR_JOB_TITLE));
				vector.add(result);
			}

			TaxArt21SubmitEmployeeDetail[] details =
				(TaxArt21SubmitEmployeeDetail[]) vector.toArray(new TaxArt21SubmitEmployeeDetail[vector.size()]);

			return details;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateTaxArt21Submit(Connection conn, Transaction transaction, TaxArt21Submit taxSubmitted) throws SQLException {
		PreparedStatement stm = null;

		String query="UPDATE "
			+ IDBConstants.TABLE_TAX_ART_21_SUBMIT + " SET "
			+ IDBConstants.ATTR_TRANSACTION+ "=? ,"
			+ IDBConstants.ATTR_STATUS+ "=? "+ " WHERE "
			+ IDBConstants.ATTR_AUTOINDEX + "=" + taxSubmitted.getIndex();
		try {

			stm = conn.prepareStatement(query);

			stm.setLong(1, transaction.getIndex());
			stm.setInt(2, taxSubmitted.getStatus());

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee_n[] getEmployeeAndPTKPByUnit(Connection conn, long unit, String date) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,job.name as jobtitle, ptkp.name as ptkp"
				+ " from employee emp inner join "
				+ "(select e.* from employeeemployment e, (select employee, max(tmt) tmt from "
				+ "(select * from employeeemployment where tmt<'"
				+ date
				+ "') group by employee "
				+ ") lastemp where e.employee=lastemp.employee and e.tmt=lastemp.tmt "
				+ "and unit="
				+ unit
				+ ") employment "
				+ "on emp.autoindex=employment.employee "
				+ "inner join jobtitle job on employment.jobtitle=job.autoindex "
				+ "inner join ptkp on emp.art21=ptkp.autoindex "
				+ "where not exists (SELECT employee FROM employeeretirement ret WHERE ret.tmt<='"
				+ date
				+ "' and "
				+ "emp.autoindex=ret.employee) "
				+ "order by emp.employeeno";
		System.err.println(query);

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				vresult.addElement(new Employee_n(rs.getLong("autoindex"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getString("jobtitle"),
						rs.getString("ptkp")));
			}
			Employee_n[] result = new Employee_n[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Employee_n[] getEmployeeAndPTKPByCriteria(Connection conn, String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				vresult.addElement(new Employee_n(rs.getLong("autoindex"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getString("jobtitle"),
						rs.getString("ptkp")));
			}
			Employee_n[] result = new Employee_n[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public java.sql.Date utilDatetoSqlDate(java.util.Date date) {
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

	public Hashtable getAllValueForTax(Connection conn,
			int month, int year, Unit unit, Employee_n employee)throws SQLException {

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		Date startDate = cal.getTime();
		java.sql.Date sqlStartDate = utilDatetoSqlDate(startDate);
		String strStartDate = sqlStartDate.toString();

		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endDate = cal.getTime();
		java.sql.Date sqlEndDate = utilDatetoSqlDate(endDate);
		String strEndDate = sqlEndDate.toString();

		String query=
			"select payrollcomponent pc, sum(valuesubmit) vs from (" +
			"select distinct epd.*, ep.unit " +
			"from payrollpmtslryho slry, " +
			"payrollpmtslryhodet slrydet, " +
			"employeepayroll ep, " +
			"employeepayrolldetail epd " +
			"where slry.autoindex=slrydet.payrollpmtslryho " +
			"and slry.status=3 " +
			"and ep.trans=slrydet.trans " +
			"and epd.employeepayroll=ep.autoindex " +
			"and epd.valuesubmit>-1 " +
			"and transactiondate between '" + strStartDate + "' and '" + strEndDate + "' " +
			"union " +
			"select distinct epd.*, ep.unit " +
			"from payrollpmtempinsurance slry, " +
			"payrollpmtempinsdet slrydet, " +
			"employeepayroll ep, " +
			"employeepayrolldetail epd " +
			"where slry.autoindex=slrydet.payrollpmtempins " +
			"and slry.status=3 " +
			"and ep.trans=slrydet.trans " +
			"and epd.employeepayroll=ep.autoindex " +
			"and epd.valuesubmit>-1 " +
			"and transactiondate between '" + strStartDate + "' and '" + strEndDate + "' " +
			") " +
			"where employee=" + employee.getAutoindex() + " " +
			"and unit=" + unit.getIndex() + " " +
			"group by payrollcomponent";


		System.out.println(query);
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			Hashtable hashtable = new Hashtable();
			//System.err.println(rs.getInt("pc") + " " + rs.getDouble("vs"));
			while (rs.next()) {
				hashtable.put(new Integer(rs.getInt("pc")), String.valueOf(new Double(rs.getDouble("vs"))));
			}

			return hashtable;
		}catch(Exception ex){
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}

	}

	public PayrollCategoryComponent[] getSelectedEmployeePayrollComponent(Connection conn, long employeeIndex, long componentIndex) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			String query = "SELECT * FROM "
					+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT + " WHERE "
					+ IDBConstants.ATTR_EMPLOYEE + " = " + employeeIndex
					+ " AND " + IDBConstants.ATTR_COMPONENT + " = "
					+ componentIndex;

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new PayrollCategoryComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX),
						getPayrollComponent(rs.getLong(IDBConstants.ATTR_COMPONENT), conn),
						null));

			}

			PayrollCategoryComponent[] result = new PayrollCategoryComponent[
			                                                                 vresult.size()];
			vresult.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}
}
