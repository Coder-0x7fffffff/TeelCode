package org.oj.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {
	
	private EncryptionUtil() {}
	
	public static final String SALT = "/password";
	
	private static final String HEX_DIGITS = "0123456789ABCDEF";
	
	public static String md5(String text) {
        try {
        	final int length = 32;
        	byte[] bytes = MessageDigest.getInstance("md5").digest(text.getBytes());
        	char[] result = new char[length];
        	for (int i = 0; i < (length / 2); ++i) {
        		int curByte = bytes[i] & 0xff;
        		result[i * 2] = HEX_DIGITS.charAt(curByte >>> 4);
        		result[i * 2 + 1] = HEX_DIGITS.charAt(curByte & 0xf);
        	}
        	return new String(result);
        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	/* md5(text + salt) */
	public static String md5(String text, String salt) {
		return md5(text + salt);
	}

	public static String sha256(String text) {
		try {
			final int length = 32;
	        byte bytes[] = MessageDigest.getInstance("sha256").digest(text.getBytes());
	        char[] result = new char[length];
        	for (int i = 0; i < (length / 2); ++i) {
        		int curByte = bytes[i] & 0xff;
        		result[i * 2] = HEX_DIGITS.charAt(curByte >>> 4);
        		result[i * 2 + 1] = HEX_DIGITS.charAt(curByte & 0xf);
        	}
        	return new String(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* sha256(text + salt) */
	public static String sha256(String text, String salt) {
		return sha256(text + salt);
	}
	
}
