/* @author Raul Rivero Rubio
 * This is the login validation servlet that checks whether the form inputs for conditions such as
 * 	- empty fields
 * 	- the username does not exits
 * 	- password incorrect
 *  It also redirect if there any error with the database connection
 * */

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//packages
import enumeration.TextFields;
import database.Databases;
import hash.Encrypt;
import validation.Validation;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String username = null;
	private String password = null;
	private byte[] salt = null;		// seed for hasing
	
	// do Post for Security reasons
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException { // Can throw an input/output exception
		String username = req.getParameter("username");
		String password = req.getParameter("password"); 
		
		Validation login_validation = new Validation(); // create a Validation object to validate the user request
		
		// Get the current requested session
		HttpSession session = req.getSession();
		
		try {
			if(!Databases.connectionEstablished())
				Databases.mySQLConnection();
			
			// Check if both fields are empty
			if(login_validation.isEmptyTextField(username, TextFields.USERNAME) && login_validation.isEmptyTextField(password, TextFields.PASSWORD)) {
				// Set the Session user name to nulls
				session.setAttribute("username", "");
		 		// Check if this attribute exists in the request
		 		res.sendRedirect("login.jsp?userErrorMsg=" + login_validation.userFormWarning("userErrorMsg") + 
		 						"&pwdErrorMsg=" + login_validation.userFormWarning("pwdErrorMsg")); // redirect to the login.jsp with a userErrorMsg & pwdErrorMsg
			} else if(login_validation.isEmptyTextField(username, TextFields.USERNAME)){
				// Set the Session user name to nulls
				session.setAttribute("username", "");
				res.sendRedirect("login.jsp?userErrorMsg=" + login_validation.userFormWarning("userErrorMsg")); // redirect to the login.jsp with a userErrorMsg
			} else if(login_validation.isEmptyTextField(password, TextFields.PASSWORD)) {
				// // Set the Session user name to requested username
				session.setAttribute("username", username);
				res.sendRedirect("login.jsp?pwdErrorMsg=" + login_validation.userFormWarning("pwdErrorMsg")); // redirect to the login.jsp with a pwdErrorMsg
			} else if(login_validation.doUserExists(username) == false) {
				session.setAttribute("username", username);
		 		res.sendRedirect("login.jsp?userErrorMsg=" + login_validation.userFormWarning("userErrorMsg")); // redirect to the login.jsp with a userErrorMsg

			} else {
				session.setAttribute("username", username);
		
				// Get the seed register to that user to hashed properly the input password
				salt = login_validation.getDBSaltSeed(username);
				
				// Check if the seed is not null;
				if(salt != null) {
					// hashed the combine user & pass
					String hashed = Encrypt.hashPassword(username + password, salt);
					
					// If authenticated redirect to the controller (index) and grant access to that user welcome page
					if(login_validation.accessGranted(username, hashed)) {		
						session.setAttribute("granted", true);
						res.sendRedirect("index.jsp");
					} else {
						res.sendRedirect("login.jsp?pwdErrorMsg=" + login_validation.userFormWarning("pwdErrorMsg")); // redirect to the login.jsp with a pwdErrorMsg
					}
				}
			}
		} catch(Exception e)	{
			if(Databases.hasAError()) {
				session.setAttribute("exception", Databases.getErrorMsg());
				res.sendRedirect("error.jsp");
			}
		}
	}
}
			
		

	

