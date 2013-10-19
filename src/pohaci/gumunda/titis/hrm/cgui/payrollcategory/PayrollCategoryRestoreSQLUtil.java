package pohaci.gumunda.titis.hrm.cgui.payrollcategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.Formula.TokenParser;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategory;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL.IFormulaParser;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollTokenParser;
import pohaci.gumunda.util.OtherSQLException;

public class PayrollCategoryRestoreSQLUtil {
	private Connection connection;
	private long sessionId;

	public PayrollCategoryRestoreSQLUtil(Connection connection, long sessionId) {
		super();
		this.connection = connection;
		this.sessionId = sessionId;
	}

	public List getAllHistoryMaster(int month, int year) throws SQLException {
		List newList = new ArrayList();

		Statement stm = connection.createStatement();

		String sql = "SELECT * FROM "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER
				+ " WHERE " + IDBConstants.ATTR_MONTH + " = " + month + " AND "
				+ IDBConstants.ATTR_YEAR + " = " + year;

		ResultSet rs = stm.executeQuery(sql);
		while (rs.next()) {
			PayrollCategoryBackupMaster master = new PayrollCategoryBackupMaster(
					rs.getLong(IDBConstants.ATTR_AUTOINDEX), rs
							.getInt(IDBConstants.ATTR_MONTH), rs
							.getInt(IDBConstants.ATTR_YEAR), rs
							.getString(IDBConstants.ATTR_DESCRIPTION), rs
							.getDate(IDBConstants.ATTR_DATE));

			newList.add(master);
		}

		return newList;
	}

	public void restore(PayrollCategoryBackupMaster master) throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			removeOldOne(IDBConstants.TABLE_PAYROLL_CATEGORY);
			removeOldOne(IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT);
			removeOldOne(IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE);
			removeOldOne(IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT);

			List list = restoreCategory(master);
			restoreCategoryEmployee(master, list);
			restoreCategoryComponent(master, list);
			restoreEmployeeComponent(master, list);

			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
		} catch (SQLException e) {
			connection.rollback();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
			e.printStackTrace();
			throw new Exception("Failed to backup. ", e);
		}

	}

	private void restoreEmployeeComponent(PayrollCategoryBackupMaster master,
			List list) throws SQLException {
		String sql = "INSERT INTO "
				+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT + "("
				+ IDBConstants.ATTR_EMPLOYEE + ","
				+ IDBConstants.ATTR_COMPONENT + "," + IDBConstants.ATTR_FORMULA
				+ "," + IDBConstants.ATTR_FORMULA_MONTH + ","
				+ IDBConstants.ATTR_ROUND_VALUE + ","
				+ IDBConstants.ATTR_PRECISION + ")"
				+ " values (?, ?, ?, ?, ?, ?)";

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PayrollCategory cat = (PayrollCategory) iterator.next();

			Long[] ids = getPayrollCategoryEmployee(cat.getIndex(), master
					.getId());

			for (int i = 0; i < ids.length; i++) {
				long id = ids[i].longValue();

				// HRMBusinessLogic logic = new HRMBusinessLogic(connection);

				PayrollCategoryComponent[] components = getAllPayrollCategoryComponent(
						id, new PayrollFormulaParser(),
						IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT_HISTORY,
						master.getId());
				for (int j = 0; j < components.length; j++) {
					PayrollCategoryComponent comp = components[j];

					PreparedStatement stm = connection.prepareStatement(sql);

					stm.setLong(1, id);
					stm.setLong(2, comp.getPayrollComponent().getIndex());
					stm.setString(3, comp.getFormulaEntity().getFormulaCode());
					stm.setInt(4, (int) comp.getEveryWhichMonth());
					stm.setInt(5, (int) comp.getFormulaEntity()
							.getNumberRounding().getRoundingMode());
					stm.setInt(6, comp.getFormulaEntity().getNumberRounding()
							.getPrecision());

					stm.executeUpdate();
				}
			}
		}
	}

	private void restoreCategoryComponent(PayrollCategoryBackupMaster master,
			List list) throws Exception {
		String sql = "INSERT INTO "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT + "("
				+ IDBConstants.ATTR_CATEGORY + ","
				+ IDBConstants.ATTR_COMPONENT + "," + IDBConstants.ATTR_FORMULA
				+ "," + IDBConstants.ATTR_FORMULA_MONTH + ","
				+ IDBConstants.ATTR_ROUND_VALUE + ","
				+ IDBConstants.ATTR_PRECISION + ")"
				+ " values (?, ?, ?, ?, ?, ?)";

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			PayrollCategory payrollCategory = (PayrollCategory) iterator.next();

			// HRMBusinessLogic logic = new HRMBusinessLogic(connection);

			PayrollCategoryComponent[] components = getAllPayrollCategoryComponent(
					payrollCategory.getIndex(), new PayrollFormulaParser(),
					IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT_HISTORY,
					master.getId());
			for (int j = 0; j < components.length; j++) {
				PayrollCategoryComponent comp = components[j];

				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setLong(1, payrollCategory.getNewIndex());
				stm.setLong(2, comp.getPayrollComponent().getIndex());
				stm.setString(3, comp.getFormulaEntity().getFormulaCode());
				stm.setInt(4, (int) comp.getEveryWhichMonth());
				stm.setInt(5, (int) comp.getFormulaEntity().getNumberRounding()
						.getRoundingMode());
				stm.setInt(6, comp.getFormulaEntity().getNumberRounding()
						.getPrecision());

				stm.executeUpdate();
			}
		}
	}

	private void restoreCategoryEmployee(PayrollCategoryBackupMaster master,
			List list) throws SQLException {

		String sql = "INSERT INTO "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE + " ("
				+ IDBConstants.ATTR_CATEGORY + ", "
				+ IDBConstants.ATTR_EMPLOYEE + ") VALUES (?, ?)";

		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			PayrollCategory cat = (PayrollCategory) iterator.next();

			Long[] ids = getPayrollCategoryEmployee(cat.getIndex(), master
					.getId());

			for (int i = 0; i < ids.length; i++) {
				long id = ids[i].longValue();

				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setLong(1, cat.getNewIndex());
				stm.setLong(2, id);

				stm.executeUpdate();
			}
		}
	}

	private List restoreCategory(PayrollCategoryBackupMaster master)
			throws Exception {
		Statement stm = connection.createStatement();

		String sql = "SELECT * FROM "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY + " WHERE "
				+ IDBConstants.ATTR_HISTORY_MASTER + " = " + master.getId();

		ResultSet rs = stm.executeQuery(sql);

		HRMBusinessLogic logic = new HRMBusinessLogic(connection);
		List list = new ArrayList();

		while (rs.next()) {
			PayrollCategory cat = new PayrollCategory(rs
					.getLong(IDBConstants.ATTR_AUTOINDEX), rs
					.getString(IDBConstants.ATTR_NAME), rs
					.getString(IDBConstants.ATTR_DESCRIPTION));

			PayrollCategory newCat = logic.createPayrollCategory(sessionId,
					IDBConstants.MODUL_MASTER_DATA, cat);

			cat.setNewIndex(newCat.getIndex());

			list.add(cat);
		}

		return list;
	}

	private void removeOldOne(String table) throws SQLException {
		Statement stm = connection.createStatement();

		String sql = "DELETE FROM " + table;

		stm.executeUpdate(sql);
	}

	public Long[] getPayrollCategoryEmployee(long categoryindex, long masterId)
			throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = connection.createStatement();
			String sql = "SELECT " + IDBConstants.ATTR_EMPLOYEE + " FROM "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE_HISTORY
					+ " WHERE " + IDBConstants.ATTR_CATEGORY + "="
					+ categoryindex + " AND "
					+ IDBConstants.ATTR_HISTORY_MASTER + "=" + masterId;
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				vresult.addElement(new Long(rs
						.getLong(IDBConstants.ATTR_EMPLOYEE)));
			}

			Long[] result = new Long[vresult.size()];
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

	public PayrollCategoryComponent[] getAllPayrollCategoryComponent(
			long index, IFormulaParser parser, String table, long masterIdx)
			throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		HRMSQLSAP sap = new HRMSQLSAP();

		try {
			stm = connection.createStatement();
			String query = "";
			if (table == IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT_HISTORY) {
				query = "SELECT * FROM " + table + " WHERE "
						+ IDBConstants.ATTR_CATEGORY + "=" + index + " AND "
						+ IDBConstants.ATTR_HISTORY_MASTER + "=" + masterIdx;
			} else if (table == IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT_HISTORY) {
				query = "SELECT * FROM " + table + " WHERE "
						+ IDBConstants.ATTR_EMPLOYEE + "=" + index + " AND "
						+ IDBConstants.ATTR_HISTORY_MASTER + "=" + masterIdx;
			}
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				vresult.addElement(new PayrollCategoryComponent(rs
						.getLong(IDBConstants.ATTR_AUTOINDEX), sap
						.getPayrollComponent(rs
								.getLong(IDBConstants.ATTR_COMPONENT),
								connection), parser.parseToFormula(rs
						.getString(IDBConstants.ATTR_FORMULA), rs
						.getShort(IDBConstants.ATTR_FORMULA_MONTH),
						new NumberRounding(rs
								.getShort(IDBConstants.ATTR_ROUND_VALUE), rs
								.getInt(IDBConstants.ATTR_PRECISION)))));

			}

			PayrollCategoryComponent[] result = new PayrollCategoryComponent[vresult
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

	public class PayrollFormulaParser implements IHRMSQL.IFormulaParser {
		TokenParser itemParser = new PayrollTokenParser(connection);
		Formula mformula = new Formula();

		public FormulaEntity parseToFormula(String formulaStr) {
			try {
				short whichMonth = 0;
				short roundingMode = -1;
				int precision = 0;
				if (formulaStr == null)
					return null;
				mformula.parseFormula(formulaStr, itemParser);
				return mformula.createFormulaEntity(whichMonth,
						new NumberRounding(roundingMode, precision));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public FormulaEntity parseToFormula(String formulaStr,
				short whichMonth, NumberRounding numberRounding) {
			try {
				mformula.parseFormula(formulaStr, itemParser);
				return mformula.createFormulaEntity(whichMonth, numberRounding);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
