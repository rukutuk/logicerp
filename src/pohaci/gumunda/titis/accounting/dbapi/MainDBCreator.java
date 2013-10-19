package pohaci.gumunda.titis.accounting.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */


import java.sql.*;

public class MainDBCreator {

  public static void main(String[] args) {
    Connection conn = null;
    try{
      Class.forName("com.sap.dbtech.jdbc.DriverSapDB");
     // conn = DriverManager.getConnection("jdbc:sapdb://172.18.9.1/sampurna", "sampurnauser", "calamari");
      conn = DriverManager.getConnection("jdbc:sapdb://localhost/cok2", "sampurnauser", "calamari");
      
      conn.setAutoCommit(false);
      Statement stm = conn.createStatement();
      stm.close();
      conn.commit();
      conn.close();
      System.out.println("Ok");
    }
    catch(Exception ex){
      ex.printStackTrace();
      System.err.println(ex);
      if(conn != null)
        try{
        conn.rollback();
        }catch(Exception exe){}
    }
  }
}
