package org.oj.util;

import java.io.UnsupportedEncodingException;

public class WebUtil {

	private WebUtil() {}
	
	public static String decode(String s) throws UnsupportedEncodingException {
		return new String(s.getBytes("ISO-8859-1"), "UTF-8");
	}
	
}
