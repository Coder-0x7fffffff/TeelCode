package org.oj.util;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobUtil {

	private BlobUtil() {}
	
	public static String blob2String(Blob blob) throws UnsupportedEncodingException, SQLException {
		if (null == blob) {
			return null;
		}
		return new String(blob.getBytes(1, (int) blob.length()),"GBK");
	}
	
	public static Blob string2Blob(String string) {
		return null;
	}
	
}
