package com.pohaci.titis.testconnection;
import java.sql.*;
import javax.sql.*;

public class MyConnection{
	Connection con=null;
	public MyConnection(){

		String dbtime;
		String dbUrl = "jdbc:mysql://localhost:3306/tisas_db";
		String dbClass = "com.mysql.jdbc.Driver";
		String query = "Select * FROM users";

		try {

			Class.forName(dbClass);
			con =DriverManager.getConnection(
					dbUrl,"root", "123");
			/*Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				dbtime = rs.getString(2);
				System.out.println(dbtime);
			} //end while

			con.close();
			 */		} //end try

		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		catch(SQLException e) {
			e.printStackTrace();
		}

	}  //end main
	public Connection getConnection(){
		return this.con;
	}

}  //end class

