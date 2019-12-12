package database;

import java.sql.*;

public class Databases {
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // com.mysql.jdbc.Driver is deprecated
	private static final String URL = "jdbc:mysql://localhost:3306";
	private static final String USER = "root";
	private static final String PASS = "";
	private static Connection connection = null;
	private static ResultSet resultSet = null;
	private static String errorMsg = null;		// this is use to display error exceptions in the error.jsp page

	// Establish the connection with MySQL (MariaDB)
	public static void mySQLConnection() throws SQLException, ClassNotFoundException, Exception {
		errorMsg = null;
		try {
			// load and register driver
			Class.forName(DRIVER);
			// create connection
			connection = DriverManager.getConnection(URL, USER, PASS);
			System.out.println("Databases.mySQLConnection() : Connection establish.");
		} catch (SQLException sqlEx) {
			errorMsg = "Exception - Databases.mySQLConnection() : Connection failed! \"" + sqlEx.getMessage() + "\"";
			throw sqlEx;
		} catch (ClassNotFoundException clsNotFnd_Ex) {
			// Handle errors for Class.forName
			errorMsg = "Exception - Databases.mySQLConnection() : Connection failed! \"" + clsNotFnd_Ex.getMessage()
					+ "\"";
			throw clsNotFnd_Ex;
		} catch (Exception e) {
			errorMsg = "Exception - Databases.mySQLConnection() : Connection failed! \"" + e.getMessage() + "\"";
			throw e;
		}
	}

	// Select query return result set
	public static ResultSet fetchQueryResults(String query, Object[] arr, int elemCount)
			throws SQLException, Exception {
		errorMsg = null;

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			for (int i = 0; i < elemCount; i++) {
				statement.setObject(i + 1, arr[i]);
			}

			resultSet = statement.executeQuery();

			statement.clearParameters();
			return resultSet;
		} catch (SQLException sqlExc) {
			errorMsg = "Exception - Databases.fetchQueryResults(String, String) : \"" + sqlExc.getMessage() + "\"";
			throw sqlExc;
		} catch (Exception e) {
			errorMsg = "Exception - Databases.fetchQueryResults() : \"" + e.getMessage() + "\"";
			throw e;
		}
	}

	// Insert query , return number of rows affected
	public static int[] InsertQuery(String query, Object[] arr, int elemCount) throws SQLException, Exception {
		errorMsg = null;

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			for (int i = 0; i < elemCount; i++) {
				statement.setObject(i + 1, arr[i]);
			}
			// Add row to the batch.
			statement.addBatch();

			// Batch is ready, execute it to insert the data
			int[] result = statement.executeBatch();

			statement.clearParameters();
			return result;
		} catch (SQLException sqlExc) {
			errorMsg = "Exception - Databases.InsertQuery(String, String) : \"" + sqlExc.getMessage() + "\"";
			throw sqlExc;
		} catch (Exception e) {
			errorMsg = "Exception - Databases.InsertQuery() : \"" + e.getMessage() + "\"";
			throw e;
		}
	}

	// Close the database connection
	public static void close() throws SQLException, Exception {
		try {
			if (resultSet != null && connection != null) {
				resultSet.close();
				connection.close();
				connection = null;
			}
		} catch (SQLException sqlExc) {
			errorMsg = "Exception - Databases.close() : \"" + sqlExc.getMessage() + "\"";
		} catch (Exception e) {
			errorMsg = "Exception - Databases.mySQLConnection() : Connection failed! \"" + e.getMessage() + "\"";
			throw e;
		}
	}
	
	// Check if the connection have been established
	public static boolean connectionEstablished() {
		if (connection != null)
			return true;
		else
			return false;
	}

	// Check if errorMsg string have an exception message for displaying purposes
	public static boolean hasAError() {
		if (errorMsg != null)
			return true;
		else
			return false;
	}

	// Gets the error message if not null
	public static final String getErrorMsg() {
		if (errorMsg != null)
			return errorMsg;
		else
			return null;
	}
}
