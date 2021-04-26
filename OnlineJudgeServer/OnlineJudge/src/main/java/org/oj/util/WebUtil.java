package org.oj.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebUtil {

	private WebUtil() {}
	
	/**
	 * Used to decode the string to avoid chaos code
	 * 
	 * @param s from request.getParameter()
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String s) throws UnsupportedEncodingException {
		return new String(s.getBytes("ISO-8859-1"), "UTF-8");
	}
	
	public static String getToken(HttpServletRequest request) throws UnsupportedEncodingException {
		String token = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length != 0) {
		    for(int i = 0; i < cookies.length; ++i) {
		        Cookie cookie = cookies[i];
		        if (cookie.getName().equals("token")) {
		        	/* From cookie */
		        	token = cookie.getValue();
		        }
		    }
		}
		if (null == token) {
			/* From Header */
		    token = request.getHeader("token");
		}
		if (null == token) {
			/* From parameter */
	        token = request.getParameter("token");
	    }
		return null == token ? null : WebUtil.decode(token);
	}
	
}
