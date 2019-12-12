/* @author Raul Rivero Rubio
 * This class validates all inputs of any the forms for things like
 * 	- empty fields
 * 	- the username does not exists or username has been taken
 * 	- password incorrectly format
 * 	- confirm password does not match
 *  Also it 
 *   - grant access if correctly authenticated
 *   - create account if it meets the requirements
 * */
package validation;
import database.Databases;
import enumeration.TextFields;

import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Validation {
		private final String USER_EMPTY_FIELD = "Please enter your username.";
		private final String PWD_EMPTY_FIELD = "Please enter your password.";
		private final String CONFIRM_PWD_EMPTY_FIELD = "Please confirm the password.";
		private final String PWD_INVALID=  "Incorrect password!";
		private final String USER_DONT_EXIST = "Username does not exists!";
		private final String USERNAME_TAKEN = "Username have been taken!";
		private final String PWD_DONT_MATCH = "Password does not match!";
		
		private ResultSet resultSet = null;
		private Map formWarnings = new HashMap();	// Associative array to store key-value pair form the display textfield error messages
		
	public Validation() {
		formWarnings.put("userErrorMsg", "");
		formWarnings.put("pwdErrorMsg", "");
		formWarnings.put("confirm_pwdErrorMsg", "");
	}
	
	public boolean isEmptyTextField(String text, TextFields textfield) {
		// once the pass have been trim(both end) check if it is empty
		if(text.trim().isEmpty()) {
			// Set warning message for the login screen
			switch(textfield) {
				case USERNAME:
					formWarnings.replace("userErrorMsg", USER_EMPTY_FIELD); // warning message
					break;
				case PASSWORD:
					formWarnings.replace("pwdErrorMsg", PWD_EMPTY_FIELD);
					break;
				case CONFIRM_PASSWORD:
					formWarnings.replace("confirm_pwdErrorMsg", CONFIRM_PWD_EMPTY_FIELD);
					break;
				}
			// User name is empty
	 		return true;
	 	} else {
	 		return false;
	 	}
	}
	
	public boolean doUserExists(String user) throws SQLException, Exception {
		final String query = "SELECT user_name FROM accounts.users WHERE user_name = ?";
		String[] elem_container = {user};
		resultSet= null;
		boolean found = false;
		
		try {
			resultSet = Databases.fetchQueryResults(query, elem_container, elem_container.length);
			
			while(resultSet.next()) {
				if(resultSet.getString("user_name").equals(user)) {
					found = true;
				}
			}
			
			if(!found) {
				//if(isLoginPage) 
				formWarnings.replace("userErrorMsg", USER_DONT_EXIST);  // Username does not exists!
				return false;
			} else {
				formWarnings.replace("userErrorMsg", USERNAME_TAKEN);
				return true;
			}
		} catch(Exception e) {
			throw e; 
		}
	}
	
	public boolean doPasswordMatches(String pwd, String confirm_pwd) {
		if(!pwd.equals(confirm_pwd)) {
			formWarnings.replace("confirm_pwdErrorMsg", PWD_DONT_MATCH);
			return false;
		} else { 
			return true;
		}
	}
	
	public void createAccount(String user, String pwd, byte[] salt) throws SQLException, Exception {
		final String query = "INSERT INTO accounts.users(user_name, hashed_password, salt_seed)" + 
								"VALUES(?, ?, ?)";
		Object[] elem_container = {user, pwd, salt};
		resultSet = null;
				
		try {
			int[] result = Databases.InsertQuery(query, elem_container, elem_container.length);
			for(int i = 0; i < result.length; i++) {
				System.out.println("Rows Affected: " + result[i]);   
			}
		} catch(Exception e) {
			System.out.print("Exception - Account Not Created ");
			//System.out.println("Databases.doUserExists(String) : Connection failed! \"" + e.getMessage() + "\"");
			//return false;
			throw e; 
		}
	}
	
	// Get the seed for was use the hashed the user password
	public byte[] getDBSaltSeed(String user) throws Exception {
		byte[] salt = null;
		final String query = "SELECT user_name, salt_seed FROM accounts.users WHERE user_name = ?";
		String[] elem_container = {user};
		resultSet = null;
		
		try {
			resultSet = Databases.fetchQueryResults(query, elem_container, elem_container.length);
			
			while(resultSet.next()) {
				if(resultSet.getString("user_name").equals(user)) {
					salt = resultSet.getBytes("salt_seed");
				}
			}
			return salt;
		} catch(Exception e) {
			throw e; 
		}
	}
	
	public boolean accessGranted(String user, String hashed_pwd) throws SQLException, Exception {
		final String query = "SELECT user_name, hashed_password FROM accounts.users WHERE user_name = ?";
		
		String[] elem_container = {user};
		resultSet = null;
		boolean accessGrandted = false;
		
		try {
			resultSet = Databases.fetchQueryResults(query, elem_container, elem_container.length);
			
			while(resultSet.next()) {
				if(resultSet.getString("user_name").equals(user) && resultSet.getString("hashed_password").equals(hashed_pwd)) {
					accessGrandted = true;
				}
			}
			if(!accessGrandted) {
				formWarnings.replace("pwdErrorMsg", PWD_INVALID); // warning message
				return false;
			} else {
				return true;
			}
		} catch(Exception e) {
			System.out.print("Exception - *** ");
			throw e; 
		}
	}
	
	// to Get the text field error message when necessary
	public String userFormWarning(String warning) {
		return formWarnings.get(warning).toString();
	}
}