package org.oj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
	private DateUtil() {}
	
	public static java.util.Date string2UtilDate(String time) throws ParseException {
		return SIMPLE_DATE_FORMAT.parse(time);
	}
	
	public static java.sql.Timestamp string2SQLDate(String time) throws ParseException {
		return toSQLDate(SIMPLE_DATE_FORMAT.parse(time));
	}
	
	public static java.sql.Timestamp toSQLDate(java.util.Date utilDate) {
		return new java.sql.Timestamp(utilDate.getTime());
	}
	
	public static java.util.Date fromSQLDate(java.sql.Date sqlDate) {
		return new java.util.Date(sqlDate.getTime());
	}
	
}
