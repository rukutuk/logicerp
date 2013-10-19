package pohaci.gumunda.titis.hrm.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import junit.framework.TestCase;

public class EmployeePayrollTest/* extends TestCase */{

	/*
	 * Test method for 'pohaci.gumunda.titis.hrm.logic.EmployeePayroll.EmployeePayroll(Connection, int, int)'
	 */
	static class UserInfo {
		String host = "localhost";
		String dbname = "sampurna";
		String username = "yudhi";
		String password = "pwd";
	};
	UserInfo userInfo = new UserInfo();
	static {
		try {
			Class.forName("com.sap.dbtech.jdbc.DriverSapDB");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void testEmployeePayroll() throws SQLException {
		String url = "jdbc:sapdb://"
			+ userInfo.host + "/"
			+ userInfo.dbname;
		Connection conn = DriverManager.getConnection(url,userInfo.username,userInfo.password);
		EmployeePayroll p = new EmployeePayroll(conn,2007,2,0);
		System.out.println(p);
	}

	/*
	 * Test method for 'pohaci.gumunda.titis.hrm.logic.EmployeePayroll.calcPayrollComponent(PayrollCategoryComponent[], long)'
	 */
	public void testCalcPayrollComponent() throws SQLException {
		//String url = "jdbc:sapdb://"
		//	+ userInfo.host + "/"
		//	+ userInfo.dbname;
		//Connection conn = DriverManager.getConnection(url,userInfo.username,userInfo.password);
		//EmployeePayroll p = new EmployeePayroll(conn,2007,2,0);
		//HRMBusinessLogic logic = new HRMBusinessLogic(conn);
		//int employeeId = 1; // ganti sama yg ada di database
		//int categoryId = 1; // ganti sama yg ada di category
		//PayrollCategoryComponent[] comps;
//		Vector v = new Vector(Arrays.asList(comps)); array ke vektor
//		Object[] o = v.toArray(comps); vektor ke array
		try {
			/*comps = logic.getAllPayrollCategoryComponent(12345,"TEST",categoryId, pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_PAYROLL_CATEGORY_COMPONENT);
			PayrollCalcResult[] hasil = p.calcPayrollComponent(comps,employeeId);
			int i;
			System.out.println("Done");
			for (i=0; i<hasil.length; i++)				
				System.out.println("Component "+ hasil[i].component.toString() + " : " + hasil[i].value);*/
		} catch (Exception e) {			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}
