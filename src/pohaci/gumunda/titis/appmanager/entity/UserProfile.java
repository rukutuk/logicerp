/**
 * 
 */
package pohaci.gumunda.titis.appmanager.entity;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import pohaci.gumunda.util.Crypto;

/**
 * @author dark-knight
 *
 */
public class UserProfile {
	private String username;
	private byte[] password;
	private String note;
	private String fullname;
	
	public UserProfile(String username, byte[] password, String note, String fullname) {
		this.username = username;
		this.password = password;
		this.note = note;
		this.fullname = fullname;
	}

	public String getFullname() {
		return fullname;
	}

	public String getNote() {
		return note;
	}

	public byte[] getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String toString() {
		return username;
	}
	
	public String getPasswordAsString(){
		if(password==null){
			return "";
		}
		if(password.length==0){
			return "";
		}
		String pass = "";
		for(int i=0; i<password.length; i++){
			char passChar = (char) password[i];
			pass += passChar;
		}
		return pass;
	}
	
	public String getDecryptedPassword(){
		if(password==null){
			return "";
		}

		byte[] decrypted = null;
		try {
			decrypted = Crypto.decrypt(password);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		if(decrypted==null)
			return "";
		
		String pass = "";
		for(int i=0; i<decrypted.length; i++){
			char passChar = (char) decrypted[i];
			pass += passChar;
		}
		return pass;
	}
}
