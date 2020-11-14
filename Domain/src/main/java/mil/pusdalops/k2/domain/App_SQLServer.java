package mil.pusdalops.k2.domain;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App_SQLServer {

	public static void main(String[] args) throws SQLException {
		System.out.println("Hello World!!!!");
		
		 Connection conn = null;
		 ResultSet rs = null;
		 String url = "jdbc:jtds:sqlserver://localhost;instance=SQLEXPRESS;DatabaseName=pusdalops";
		 String driver = "net.sourceforge.jtds.jdbc.Driver";
	     String userName = "sa";
	     String password = "p@ssw0rdpusdalops";
	        try {
	            Class.forName(driver);
	            conn = DriverManager.getConnection(url, userName, password);
	            System.out.println("Connected to the database!!! Getting table list...");
	            DatabaseMetaData dbm = conn.getMetaData();
	            rs = dbm.getTables(null, null, "%", new String[] { "TABLE" });
	            while (rs.next()) { System.out.println(rs.getString("TABLE_NAME")); }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            conn.close();
	            rs.close();
	        }	     
	}
	
}
