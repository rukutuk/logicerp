/**
 *
 */
package pohaci.gumunda.titis.hrm.cgui.payrollcategory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategory;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryComponent;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

/**
 * @author dark-knight
 *
 */
public class PayrollCategoryBackupSQLUtil {
	private Connection connection;
	private long sessionId;

	public PayrollCategoryBackupSQLUtil(Connection connection, long sessionId) {
		super();
		this.connection = connection;
		this.sessionId = sessionId;
	}

	public void backup(int month, int year, String description)
			throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// content
			// 1: create master
			java.util.Date date = new java.util.Date();
			createMaster(month, year, description, date);
			long masterId = getMaxIndex(IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER);

			// payroll category
			IHRMSQL iSql = new HRMSQLSAP();
			PayrollCategory[] categories = iSql
					.getAllPayrollCategory(connection);
			saveCategory(masterId, categories);

			// payroll category employee
			saveCategoryEmployee(masterId, categories);

			// payroll category component
			saveCategoryComponent(masterId, categories);

			// employee component
			saveEmployeeComponent(masterId, categories);

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

	private void saveEmployeeComponent(long masterId, PayrollCategory[] categories) throws Exception {
		String sql = "INSERT INTO "
			+ IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT_HISTORY + "("
			+ IDBConstants.ATTR_HISTORY_MASTER + ","
			+ IDBConstants.ATTR_EMPLOYEE + ","
			+ IDBConstants.ATTR_COMPONENT + ","
			+ IDBConstants.ATTR_FORMULA + ","
			+ IDBConstants.ATTR_FORMULA_MONTH + ","
			+ IDBConstants.ATTR_ROUND_VALUE + ","
			+ IDBConstants.ATTR_PRECISION + ")" + " values (?, ?, ?, ?, ?, ?, ?)";



		for (int i = 0; i < categories.length; i++) {
			PayrollCategory payrollCategory = categories[i];

			IHRMSQL iSql = new HRMSQLSAP();
			Employee[] employees = iSql.getPayrollCategoryEmployee(
					payrollCategory.getIndex(), connection);

			for (int j=0; j < employees.length; j++) {
				Employee emp = employees[j];

				HRMBusinessLogic logic = new HRMBusinessLogic(connection);

				PayrollCategoryComponent[] components = logic
						.getAllPayrollCategoryComponent(sessionId,
								IDBConstants.MODUL_MASTER_DATA, emp
										.getIndex(),
								IDBConstants.TABLE_EMPLOYEE_PAYROLL_COMPONENT);

				for (int k = 0; k < components.length; k++) {
					PayrollCategoryComponent comp = components[k];

					PreparedStatement stm = connection.prepareStatement(sql);

					stm.setLong(1, masterId);
					stm.setLong(2, emp.getIndex());
					stm.setLong(3, comp.getPayrollComponent().getIndex());
					stm.setString(4, comp.getFormulaEntity().getFormulaCode());
					stm.setInt(5, (int) comp.getEveryWhichMonth());
					stm.setInt(6, (int) comp.getFormulaEntity().getNumberRounding()
							.getRoundingMode());
					stm.setInt(7, comp.getFormulaEntity().getNumberRounding()
							.getPrecision());

					stm.executeUpdate();
				}
			}
		}
	}

	private void saveCategoryComponent(long masterId,
			PayrollCategory[] categories) throws Exception {
		String sql = "INSERT INTO "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT_HISTORY + "("
				+ IDBConstants.ATTR_HISTORY_MASTER + ","
				+ IDBConstants.ATTR_CATEGORY + ","
				+ IDBConstants.ATTR_COMPONENT + "," + IDBConstants.ATTR_FORMULA
				+ "," + IDBConstants.ATTR_FORMULA_MONTH + ","
				+ IDBConstants.ATTR_ROUND_VALUE + ","
				+ IDBConstants.ATTR_PRECISION + ")"
				+ " values (?, ?, ?, ?, ?, ?, ?)";

		for (int i = 0; i < categories.length; i++) {
			PayrollCategory payrollCategory = categories[i];

			HRMBusinessLogic logic = new HRMBusinessLogic(connection);

			PayrollCategoryComponent[] components = logic
					.getAllPayrollCategoryComponent(sessionId,
							IDBConstants.MODUL_MASTER_DATA, payrollCategory
									.getIndex(),
							IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT);
			for (int j = 0; j < components.length; j++) {
				PayrollCategoryComponent comp = components[j];

				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setLong(1, masterId);
				stm.setLong(2, payrollCategory.getNewIndex());
				stm.setLong(3, comp.getPayrollComponent().getIndex());
				stm.setString(4, comp.getFormulaEntity().getFormulaCode());
				stm.setInt(5, (int) comp.getEveryWhichMonth());
				stm.setInt(6, (int) comp.getFormulaEntity().getNumberRounding()
						.getRoundingMode());
				stm.setInt(7, comp.getFormulaEntity().getNumberRounding()
						.getPrecision());

				stm.executeUpdate();
			}

		}
	}

	private void saveCategoryEmployee(long masterId,
			PayrollCategory[] categories) throws Exception {

		String sql = "INSERT INTO "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_EMPLOYEE_HISTORY + "("
				+ IDBConstants.ATTR_HISTORY_MASTER + ","
				+ IDBConstants.ATTR_CATEGORY + "," + IDBConstants.ATTR_EMPLOYEE
				+ ")" + " values (?, ?, ?)";

		for (int i = 0; i < categories.length; i++) {
			PayrollCategory payrollCategory = categories[i];

			IHRMSQL iSql = new HRMSQLSAP();
			Employee[] employees = iSql.getPayrollCategoryEmployee(
					payrollCategory.getIndex(), connection);

			for (int j = 0; j < employees.length; j++) {
				Employee emp = employees[j];

				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setLong(1, masterId);
				stm.setLong(2, payrollCategory.getNewIndex());
				stm.setLong(3, emp.getIndex());

				stm.executeUpdate();
			}

		}
	}

	private void saveCategory(long masterId, PayrollCategory[] categories)
			throws SQLException {
		for (int i = 0; i < categories.length; i++) {
			PayrollCategory payrollCategory = categories[i];

			String sql = "INSERT INTO "
					+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY + "("
					+ IDBConstants.ATTR_HISTORY_MASTER + ","
					+ IDBConstants.ATTR_NAME + ","
					+ IDBConstants.ATTR_DESCRIPTION + ")" + " values (?, ?, ?)";

			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setLong(1, masterId);
			stm.setString(2, payrollCategory.getName());
			stm.setString(3, payrollCategory.getDescription());

			stm.executeUpdate();

			long id = getMaxIndex(IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY);

			payrollCategory.setNewIndex(id);
		}
	}

	private void createMaster(int month, int year, String description,
			java.util.Date date) throws SQLException {
		String sql = "insert into "
				+ IDBConstants.TABLE_PAYROLL_CATEGORY_HISTORY_MASTER + " ("
				+ IDBConstants.ATTR_MONTH + ", " + IDBConstants.ATTR_YEAR
				+ ", " + IDBConstants.ATTR_DESCRIPTION + ", "
				+ IDBConstants.ATTR_DATE + ") VALUES (?, ?, ?, ?)";

		PreparedStatement stm = connection.prepareStatement(sql);
		stm.setInt(1, month);
		stm.setInt(2, year);
		stm.setString(3, description);
		stm.setDate(4, new Date(date.getTime()));

		stm.executeUpdate();
	}

	private long getMaxIndex(String table) throws SQLException {
		Statement stm = null;
		stm = connection.createStatement();
		ResultSet rs = stm.executeQuery("SELECT MAX("
				+ IDBConstants.ATTR_AUTOINDEX + ") as maxindex FROM " + table);
		rs.next();
		return rs.getLong("maxindex");
	}
}
