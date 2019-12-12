/* @author: Raul Rivero Rubio
 * Class that hashed the user password with different hash algorithms
 */
package hash;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom; 

public final class Encrypt {
	private static final String MD5_HASH = "MD5";
	private static final String SHA1_HASH = "SHA-1";
	private static final String SHA256_HASH = "SHA-256";
	private static final String SHA384_HASH = "SHA-384";
	
	public static final byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException, Exception {
	    //Always use a SecureRandom generator
	    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
	    //Create array for salt
	    byte[] salt = new byte[16];
	    //Get a random salt
	    sr.nextBytes(salt);
	    //return salt
	    return salt;
	}
	
	public static String hashPassword(String plain_password, byte[] salt) {
		String hashed_password = null;
		
		try {
			// Create MessageDigest instance of MD5
			MessageDigest md = MessageDigest.getInstance(MD5_HASH);
			//Add password bytes to digest
			md.update(salt);
			//Get the hash's bytes
			byte[] bytes = md.digest(plain_password.getBytes());
			//This bytes[] has bytes in decimal format;
	        //Convert it to hexadecimal format
			StringBuilder strBld = new StringBuilder();
			
			for(int i=0; i < bytes.length; i++) {
				strBld.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));		
			}
			// Get complete hashed password in hex format
			hashed_password = strBld.toString();
		} catch(NoSuchAlgorithmException noSucchAlg_ex) { 
			noSucchAlg_ex.printStackTrace();
		}  catch(Exception e) {
			e.printStackTrace();
		}
		return hashed_password;
	}
}
