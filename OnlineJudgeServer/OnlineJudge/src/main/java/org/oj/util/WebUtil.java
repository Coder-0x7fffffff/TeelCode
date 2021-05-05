package org.oj.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

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
	
	public static Map<String, String> parseRequest(HttpServletRequest request) throws IOException {
		InputStream inputStream = request.getInputStream();
		int len = -1;
		byte[] bytes = new byte[4096];
		StringBuffer stringBuffer = new StringBuffer();
		while (-1 != (len = inputStream.read(bytes))) {
			stringBuffer.append(new String(bytes, 0, len));
		}
		Map<String, String> result = new HashMap<String, String>();
		String url = stringBuffer.toString();
		String[] params = url.split("&");
		for (String param : params) {
			String[] kv = param.split("=");
			String key = URLDecoder.decode(kv[0], "UTF-8");
			String value = null;
			if (kv.length > 1) {
				value = URLDecoder.decode(kv[1], "UTF-8");
			}
			result.put(key, value);
		}
		return result;
	}
	
}
