/**
 * 
 */
package pohaci.gumunda.titis.accounting.dbapi.reportdesign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.util.OtherSQLException;

/**
 * @author dark-knight
 * 
 */
public class ReportDesignSQL {
	private Connection connection;

	//private long sessionId;

	public ReportDesignSQL(Connection connection, long sessionId) {
		super();
		this.connection = connection;
		//this.sessionId = sessionId;
	}

	public void insertIncomeStatementJournal(long designId, long journalId)
			throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO "
				+ IDBConstants.TABLE_INCOME_STATEMENT_JOURNAL + "("
				+ IDBConstants.ATTR_DESIGN + "," // 1
				+ IDBConstants.ATTR_JOURNAL // 2
				+ ")" + " values (?, ?)";

		try {
			stm = connection.prepareStatement(query);
			stm.setLong(1, designId);
			stm.setLong(2, journalId);

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public Journal[] getIncomeStatementJournals(long designId)
			throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			ResultSet rs = stm
					.executeQuery("select journal.* from journal, isjournal where isjournal.journal=journal.autoindex "
							+ "and isjournal.design=" + designId);

			while (rs.next()) {
				vector.addElement(new Journal(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_NAME)));
			}

			Journal[] result = new Journal[vector.size()];
			vector.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteIncomeStatementJournals(long designId)
			throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			stm.executeUpdate("DELETE "
					+ IDBConstants.TABLE_INCOME_STATEMENT_JOURNAL + " WHERE "
					+ IDBConstants.ATTR_DESIGN + "=" + designId);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void insertIncomeStatementRows(long designId, String value,
			int sequence) throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO "
				+ IDBConstants.TABLE_INCOME_STATEMENT_ROWS + "("
				+ IDBConstants.ATTR_DESIGN + "," // 1
				+ IDBConstants.ATTR_ROWXML + "," // 2
				+ IDBConstants.ATTR_SECTION // 3
				+ ")" + " values (?, ?, ?)";

		try {
			stm = connection.prepareStatement(query);
			stm.setLong(1, designId);
			stm.setString(2, value);
			stm.setLong(3, sequence);

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public String getIncomeStatementRows(long designId) throws SQLException {
		Statement stm = null;
		String xml = "";

		try {
			stm = connection.createStatement();
			ResultSet rs = stm
					.executeQuery("select rowxml from isrows where design="
							+ designId + " order by section");

			while (rs.next()) {
				String str = rs.getString("rowxml");
				str = str.substring(0, str.length() - 1);
				xml += str;
			}

			return xml;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteIncomeStatementRows(long designId) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			stm.executeUpdate("DELETE "
					+ IDBConstants.TABLE_INCOME_STATEMENT_ROWS + " WHERE "
					+ IDBConstants.ATTR_DESIGN + "=" + designId);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteBalanceSheetJournals(long designId) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			stm.executeUpdate("DELETE "
					+ IDBConstants.TABLE_BALANCE_SHEET_JOURNAL + " WHERE "
					+ IDBConstants.ATTR_DESIGN + "=" + designId);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void insertBalanceSheetJournal(long designId, long journalId)
			throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO "
				+ IDBConstants.TABLE_BALANCE_SHEET_JOURNAL + "("
				+ IDBConstants.ATTR_DESIGN + "," // 1
				+ IDBConstants.ATTR_JOURNAL // 2
				+ ")" + " values (?, ?)";

		try {
			stm = connection.prepareStatement(query);
			stm.setLong(1, designId);
			stm.setLong(2, journalId);

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteBalanceSheetRows(long designId) throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			stm.executeUpdate("DELETE " + IDBConstants.TABLE_BALANCE_SHEET_ROWS
					+ " WHERE " + IDBConstants.ATTR_DESIGN + "=" + designId);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void insertBalanceSheetRows(long designId, String value, int sequence)
			throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO " + IDBConstants.TABLE_BALANCE_SHEET_ROWS
				+ "(" + IDBConstants.ATTR_DESIGN + "," // 1
				+ IDBConstants.ATTR_ROWXML + "," // 2
				+ IDBConstants.ATTR_SECTION // 3
				+ ")" + " values (?, ?, ?)";

		try {
			stm = connection.prepareStatement(query);
			stm.setLong(1, designId);
			stm.setString(2, value);
			stm.setLong(3, sequence);

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public Journal[] getBalanceSheetJournals(long designId) throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			ResultSet rs = stm
					.executeQuery("select journal.* from journal, bsjournal where bsjournal.journal=journal.autoindex "
							+ "and bsjournal.design=" + designId);

			while (rs.next()) {
				vector.addElement(new Journal(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_NAME)));
			}

			Journal[] result = new Journal[vector.size()];
			vector.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public String getBalanceSheetRows(long designId) throws SQLException {
		Statement stm = null;
		String xml = "";

		try {
			stm = connection.createStatement();
			ResultSet rs = stm
					.executeQuery("select rowxml from bsrows where design="
							+ designId + " order by section");

			while (rs.next()) {
				String str = rs.getString("rowxml");
				str = str.substring(0, str.length() - 1);
				xml += str;
			}

			return xml;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public Journal[] getIndirectCashFlowStatementJournals(long designId)
			throws SQLException {
		Statement stm = null;
		Vector vector = new Vector();

		try {
			stm = connection.createStatement();
			ResultSet rs = stm
					.executeQuery("select journal.* from journal, cfjournal where cfjournal.journal=journal.autoindex "
							+ "and cfjournal.design=" + designId);

			while (rs.next()) {
				vector.addElement(new Journal(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), rs
						.getString(IDBConstants.ATTR_NAME)));
			}

			Journal[] result = new Journal[vector.size()];
			vector.copyInto(result);
			return result;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public String getIndirectCashFlowStatementRows(long designId)
			throws SQLException {
		Statement stm = null;
		String xml = "";

		try {
			stm = connection.createStatement();
			ResultSet rs = stm
					.executeQuery("select rowxml from cfrows where design="
							+ designId + " order by section");

			while (rs.next()) {
				String str = rs.getString("rowxml");
				str = str.substring(0, str.length() - 1);
				xml += str;
			}

			return xml;
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteIndirectCashFlowStatementJournals(long designId)
			throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			stm.executeUpdate("DELETE " + IDBConstants.TABLE_CASH_FLOW_JOURNAL
					+ " WHERE " + IDBConstants.ATTR_DESIGN + "=" + designId);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void insertIndirectCashFlowStatementJournal(long designId,
			long journalId) throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO " + IDBConstants.TABLE_CASH_FLOW_JOURNAL
				+ "(" + IDBConstants.ATTR_DESIGN + "," // 1
				+ IDBConstants.ATTR_JOURNAL // 2
				+ ")" + " values (?, ?)";

		try {
			stm = connection.prepareStatement(query);
			stm.setLong(1, designId);
			stm.setLong(2, journalId);

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void deleteIndirectCashFlowStatementRows(long designId)
			throws SQLException {
		Statement stm = null;

		try {
			stm = connection.createStatement();
			stm.executeUpdate("DELETE " + IDBConstants.TABLE_CASH_FLOW_ROWS
					+ " WHERE " + IDBConstants.ATTR_DESIGN + "=" + designId);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void insertIndirectCashFlowStatementRows(long designId,
			String value, int sequence) throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO " + IDBConstants.TABLE_CASH_FLOW_ROWS + "("
				+ IDBConstants.ATTR_DESIGN + "," // 1
				+ IDBConstants.ATTR_ROWXML + "," // 2
				+ IDBConstants.ATTR_SECTION // 3
				+ ")" + " values (?, ?, ?)";

		try {
			stm = connection.prepareStatement(query);
			stm.setLong(1, designId);
			stm.setString(2, value);
			stm.setLong(3, sequence);

			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}
}
