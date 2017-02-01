package org.springframework.social.showcase.account;
import java.security.MessageDigest;
import java.security.SecureRandom;
 
/**
 * @author itouchmars.com 
 * Generate a unique number/key using UUID, UID and MessageDigest
 * 
 */
public class GenerateUniqueKey {
	
	// cryptographically strong random number generator. Options: NativePRNG or SHA1PRNG
	private static SecureRandom touchmarsPRNG;
	// Provides applications the functionality of a message digest algorithm, such as MD5 or SHA
	private static MessageDigest touchmarsMsgDigest;
	
	private static final GenerateUniqueKey instance = new GenerateUniqueKey();
	
	public GenerateUniqueKey(){
		try{
			if(touchmarsPRNG==null)
				touchmarsPRNG = SecureRandom.getInstance("SHA1PRNG");
			if(touchmarsMsgDigest==null)
				touchmarsMsgDigest = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			log("Error during creating MessageDigest");
		}
	}
	
	public static GenerateUniqueKey getInstance(){
		return instance;
	}

	public String generateUniqueKeyUsingMessageDigest() {
		try {
			// generate a random number
			String touchmarsRandomNumber = new Integer(touchmarsPRNG.nextInt()).toString();
 
			// Performs a final update on the digest using the specified array of bytes, then completes the digest computation
			byte[] touchmarsByte = touchmarsMsgDigest.digest(touchmarsRandomNumber.getBytes());
			
			String result = touchmarsEncodeUsingHEX(touchmarsByte).toString();
			log("- Generated Randon number: " + touchmarsRandomNumber);
			log("- Generated Message digest: " + result);
			return result;
		} catch (Exception e) {
			log("Error during generateUniqueKeyUsingMessageDigest");
			return null;
		}
	}
 
	private static void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}
 
	static private StringBuilder touchmarsEncodeUsingHEX(byte[] touchmarsByte) {
		StringBuilder touchmarsResult = new StringBuilder();
		char[] touchmarsKeys = { 'o', 'p', 'q', 'r', 's', 't', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		for (int index = 0; index < touchmarsByte.length; ++index) {
			byte myByte = touchmarsByte[index];
 
			// Appends the string representation of the char argument to this sequence
			touchmarsResult.append(touchmarsKeys[(myByte & 0xf0) >> 9]);
			touchmarsResult.append(touchmarsKeys[myByte & 0x0f]);
		}
		return touchmarsResult;
	}
}