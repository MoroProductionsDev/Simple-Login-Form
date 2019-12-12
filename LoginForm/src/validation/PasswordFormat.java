/* @author Raul Rivero Rubio
 * This class validates if the registration password has the right format
 */
package validation;

public final class PasswordFormat {
	private static final String symbols = "~!@#$^&*()-_=+[]{};:.<>?";
	private static final int symbol_count = symbols.length();
	private static final int minLength = 8;
	private static final int maxLength = 25;
	private static String formattingStatement = "";

	private static final String FORMAT_PWD_TOO_SHORT = "Too short! must be 8+ (or more) characters)";
	private static final String FORMAT_PWD_TOO_LONG = "Too long!  must be 25- (or less) characters)";
	private static final String FORMAT_NO_UPPERCASE = "Must contain at least one UpperCase character";
	private static final String FORMAT_NO_DIGITS = "Must contain at least one digit (1 -> [to] 9)";
	private static final String FORMTAT_NO_SYMBOl = "Must contain at least one of thes symbols\n" + symbols;

	public static boolean validatePWDFormat(String pwd) {

		int pwd_length = pwd.length();
		if (pwd_length < 8) {
			formattingStatement = FORMAT_PWD_TOO_SHORT;
			return false;
		} else if (pwd_length > 25) {
			formattingStatement = FORMAT_PWD_TOO_LONG;
			return false;
		} else if (!hasUpperCaseChar(pwd)) {
			formattingStatement = FORMAT_NO_UPPERCASE;
			return false;
		} else if (!doesContainsDigits(pwd)) {
			formattingStatement = FORMAT_NO_DIGITS;
			return false;
		} else if (!doesContainsSymbol(pwd)) {
			formattingStatement = FORMTAT_NO_SYMBOl;
			return false;
		} else {
			return true;
		}
		
	}

	private static boolean hasUpperCaseChar(String pwd) {
		boolean hasUpperCase = false;
		for (short i = 0; i < pwd.length(); i++) {
			if (Character.toString((pwd.charAt(i))).equals(Character.toString(pwd.charAt(i)).toUpperCase())) {
				hasUpperCase = true;
				break; // or a boolean flag could also be use as an exit condition
			}
		}

		if (!hasUpperCase)
			return false;
		else
			return true;
	}
	
	private static boolean doesContainsDigits(String pwd) {
		boolean hasDigits = false;
		for (short i = 0; i < pwd.length(); i++) { 
			if (Character.isDigit(pwd.charAt(i))) {
				hasDigits = true;
				break; // or a boolean flag could also be use as an exit condition
			}
		}

		if (!hasDigits)
			return false;
		else
			return true;
	}

	private static boolean doesContainsSymbol(String pwd) {
		boolean hasSymbol = false;
		for (short i = 0; i < symbol_count; i++) {
			if (pwd.contains(Character.toString(symbols.charAt(i)))) {
				hasSymbol = true;
				break; // or a boolean flag could also be use as an exit condition
			}
		}

		if (!hasSymbol)
			return false;
		else
			return true;
	}

	public static String getFormattingStatement() {
		return formattingStatement;
	}
}
