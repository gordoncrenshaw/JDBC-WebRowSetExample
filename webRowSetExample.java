/*
 *	webRowSetExample.java
 *	Created by: Gordon Crenshaw at Progress DataDirect
 *
 *	This program is based on the JDBC Example application but displays the resultset
 *	via a webRowSet.  This prints the output as XML.  The application displays the XML
 *	to the screen and prints it to a file
 *
 */

import java.sql.*;
import java.io.*;

// Needed for WebRowSet support - com.sun.rowset.jar
import javax.sql.rowset.WebRowSet;
import com.sun.rowset.WebRowSetImpl;

public class webRowSetExample {
	
	public static void main (String args[]) {

		// Defining variables and objects
		Connection con;
		String url;
		String UserId = null;
		String Password = null;
		
		
		// Which JDBC driver is being used?
		char driver = 'O';	// Oracle connection
		//char driver = 'S';	// SQL Server connection
		
		try {

			// Load the JDBC driver and connect
			switch (driver) {
				case ('O'):
					Class.forName("com.ddtek.jdbc.oracle.OracleDriver");
					url = "jdbc:datadirect:oracle://localhost:1521;SID=XE";
					UserId = "test";
					Password = "test";
					break;
				case ('S'):
					Class.forName("com.ddtek.jdbc.sqlserver.SQLServerDriver");
					url = "jdbc:datadirect:sqlserver://localhost:1433;DatabaseName=test";
					UserId = "test";
					Password = "test";
					break;
				default:
					url = "jdbc:datadirect:oracle//localhost:1521;SID=XE";
					Class.forName("com.ddtek.jdbc.oracle.OracleDriver");
					UserId = "test";
					Password = "test";
					break;
			
			}
			
			System.out.println("Welcome to Example for Java!");
			System.out.println("Connecting to...");
			System.out.println("URL: " + url);
			
			con = DriverManager.getConnection(url, UserId, Password);
			checkForWarning (con.getWarnings ());

			// Initialize WebRowSet object
			WebRowSet webRS = new WebRowSetImpl();

			System.out.println("\nEnter SELECT statements (Press ENTER to QUIT)");			

			// Loop to continue running program til finished
			while (true) {				
				try {

					System.out.print("SQL> ");
			
					// Reads input from console window
					InputStreamReader isr = new InputStreamReader(System.in);
					BufferedReader console = new BufferedReader(isr);
			
					String query = console.readLine();
					
					if (query.length() == 0){
						System.out.println ("Exiting from Example program\n");
						break;
					}
			
					System.out.println (query);

					webRS.setCommand(query);
					webRS.execute(con);
					
					displayWebRowSetResults(webRS);
					writeToFileWebRowSetResults(webRS);
					
				} catch (SQLException ex) {
					System.out.println ("\n *** Statement Execute Failed!! **\n");
					while (ex != null){
						System.out.println ("SQLState: " + ex.getSQLState());
						System.out.println ("Message: " + ex.getMessage());
						System.out.println ("Vendor: " + ex.getErrorCode());
						ex = ex.getNextException();
						System.out.println ("");
					}
					continue;
				} catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}		
	
			}

			webRS.close();
			con.close();
					
		} catch (SQLException ex) {
			System.out.println ("\n *** SQLException caught **\n");
			while (ex != null){
				System.out.println ("SQLState: " + ex.getSQLState());
				System.out.println ("Message: " + ex.getMessage());
				System.out.println ("Vendor: " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println ("");
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
			
	}	

//-------------------------------------------------------------------
// checkForWarning
// Checks for and displays warnings.  Returns true if a warning
// existed
//-------------------------------------------------------------------

	private static boolean checkForWarning (SQLWarning warn) 	
		throws SQLException  {

		boolean rc = false;

		// If a SQLWarning object was given, display the
		// warning messages.  Note that there could be
		// multiple warnings chained together

		if (warn != null) {
			System.out.println ("\n *** Warning ***\n");
			rc = true;
			while (warn != null) {
				System.out.println ("SQLState: " +
						warn.getSQLState ());
				System.out.println ("Message:  " +
						warn.getMessage ());
				System.out.println ("Vendor:   " +
						warn.getErrorCode ());
				System.out.println ("");
				warn = warn.getNextWarning ();
			}
		}
		return rc;
	}

//-------------------------------------------------------------------
// dispResultSet
// Displays all columns and rows in the given result set
//-------------------------------------------------------------------

	private static void displayWebRowSetResults (WebRowSet wrs)
		throws SQLException {

		try {

			// Writes WebRowSet XML to screen
			StringWriter sw = new StringWriter();
			
			wrs.writeXml(sw);
			System.out.println(sw.toString());
			
			sw.close();
			
		} catch (SQLException ex) {
			System.out.println ("\n *** SQLException caught **\n");
			while (ex != null){
				System.out.println ("SQLState: " + ex.getSQLState());
				System.out.println ("Message: " + ex.getMessage());
				System.out.println ("Vendor: " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println ("");
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}

	}
	
	private static void writeToFileWebRowSetResults (WebRowSet wrs)
			throws SQLException {

			try {

				// Writes WebRowSet XML to file
				File file = new File("out.xml");
				FileWriter fw = new FileWriter(file);

				System.out.println("Writing XML data to file: " + file.getAbsolutePath());
				wrs.writeXml(fw);
				
				fw.flush();
				fw.close();

			} catch (SQLException ex) {
				System.out.println ("\n *** SQLException caught **\n");
				while (ex != null){
					System.out.println ("SQLState: " + ex.getSQLState());
					System.out.println ("Message: " + ex.getMessage());
					System.out.println ("Vendor: " + ex.getErrorCode());
					ex = ex.getNextException();
					System.out.println ("");
				}
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}

		}

}

