package org.oj.util;

public class DateUtil {

	private DateUtil() {}
	
	public static java.sql.Date toSQLDate(java.util.Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}
	
	public static java.util.Date fromSQLDate(java.sql.Date sqlDate) {
		return new java.util.Date(sqlDate.getTime());
	}
	
}
