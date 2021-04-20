package org.oj.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {
	
	private EncryptionUtil() {}
	
	public static final String SALT = "/password";
	
	private static final String HEX_DIGITS = "0123456789ABCDEF";
	
	public static String md5(String text) {
        try {
        	byte[] bytes = MessageDigest.getInstance("md5").digest(text.getBytes());
        	char[] result = new char[32];
        	for (int i = 0; i < 16; ++i) {
        		int curByte = bytes[i] & 0xff;
        		result[i * 2] = HEX_DIGITS.charAt(curByte >>> 4);
        		result[i * 2 + 1] = HEX_DIGITS.charAt(curByte & 0xf);
        	}
        	return new String(result).substring(7, 23);
        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	public static String md5(String text, String salt) {
		return md5(text + salt);
	}

	public static void main(String[] args) {
		System.out.println(md5("1234"));
	}

}
