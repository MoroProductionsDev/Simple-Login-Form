/* @author Raul Rivero Rubio
 * This is the registration form validation servlet that checks whether the form inputs for conditions such as
 * 	- empty fields
 * 	- the username has been taken
 * 	- password incorrectly format
 * 	- confirm password does not match
 *  It also redirect if there any error with the database connection
 * */

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// packages
import enumeration.TextFields;
import database.Databases;
import hash.Encrypt;
import validation.PasswordFormat;
import validation.Validation;

/**
 * Servlet implementation class RegisterServlet
 */
// servlet annotation
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static byte[] salt = null; // stores the seed for hashing


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			String confirm_password = req.getParameter("confirm_password");
			
			Validation register_validation = new Validation();	// create a Validation object to validate the user request
			
			// Get the current requested session
			HttpSession session = req.getSession();
			
			try {
				if(!Databases.connectionEstablished())
					Databases.mySQLConnection();
				
				// Check if both fields are empty
				if(register_validation.isEmptyTextField(username, TextFields.USERNAME) && register_validation.isEmptyTextField(password, TextFields.PASSWORD)) {
					// Set the Session user name to nulls
					session.setAttribute("username", "");
			 		// Check if this attribute exists in the request
			 		res.sendRedirect("register.jsp?userErrorMsg=" + register_validation.userFormWarning("userErrorMsg") + 
			 						"&pwdErrorMsg=" + register_validation.userFormWarning("pwdErrorMsg")); // redirect to the login.jsp with a userErrorMsg & pwdErrorMsg
				} else if(register_validation.isEmptyTextField(username, TextFields.USERNAME)){
					// Set the Session user name to nulls
					session.setAttribute("username", "");
					res.sendRedirect("register.jsp?userErrorMsg=" + register_validation.userFormWarning("userErrorMsg")); // redirect to the login.jsp with a userErrorMsg
				} else if(register_validation.doUserExists(username) == true) {
					session.setAttribute("username", username);
			 		res.sendRedirect("register.jsp?userErrorMsg=" + register_validation.userFormWarning("userErrorMsg")); // redirect to the login.jsp with a userErrorMsg 
				} else if(register_validation.isEmptyTextField(password, TextFields.PASSWORD)) {
					// Set the Session user name to requested username
					session.setAttribute("username", username);
					res.sendRedirect("register.jsp?pwdErrorMsg=" + register_validation.userFormWarning("pwdErrorMsg")); // redirect to the login.jsp with a pwdErrorMsg
				} else if(!PasswordFormat.validatePWDFormat(password)) {
					session.setAttribute("username", username);
					res.sendRedirect("register.jsp?pwdErrorMsg=" + PasswordFormat.getFormattingStatement());
				} else if(register_validation.isEmptyTextField(confirm_password, TextFields.CONFIRM_PASSWORD)) {
					// // Set the Session user name to requested username
					session.setAttribute("username", username);
					res.sendRedirect("register.jsp?confirm_pwdErrorMsg=" + register_validation.userFormWarning("confirm_pwdErrorMsg")); // redirect to the login.jsp with a cofirm_pwdErrorMsg
				} else if(!register_validation.doPasswordMatches(password, confirm_password)) {
					session.setAttribute("username", username);
					res.sendRedirect("register.jsp?confirm_pwdErrorMsg=" + register_validation.userFormWarning("confirm_pwdErrorMsg")); // redirect to the login.jsp with a pwdErrorMsg
				} 
				else {
					// does salt seed have been created successfully
					if(generateSalt()) {
						// hashed the combine user & pass 
						String hashed = Encrypt.hashPassword(username + password, salt);
						// Create the account
						register_validation.createAccount(username, hashed, salt); // will throw of exception if not successfully creates it

						// store in the request that a new account have been successfully created
						req.setAttribute("new_registration", true);;
						RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
						rd.forward(req, res);
					} else {
						System.out.println("Encryption salt could not be generated");
						res.sendRedirect("login.jsp");
					}
				}
			} catch(Exception e)	{
				if(Databases.hasAError()) {
					session.setAttribute("exception", Databases.getErrorMsg());
					res.sendRedirect("error.jsp");
				}
			}
		} 
	// Call the function that create a seed for hashing and returns true if it succesfully created, 
	// false if not
	private boolean generateSalt() {
		try {
			salt = Encrypt.getSalt();
			return true;
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}	
	}
}
