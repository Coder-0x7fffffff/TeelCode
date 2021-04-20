package org.oj.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	
	public abstract interface ResultHandler {
		public Object handle(ResultSet resultSet) throws SQLException;
	}
	
	private DBUtil() {}
	
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";   
	private static final String URL = "jdbc:mysql://localhost:3306/OJ?useUnicode=true&characterEncoding=UTF-8";
	private static final String USERNAME = "coderz";
	private static final String PASSWORD = "mysql4567654";
	
	/* load driver */
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME,PASSWORD);
	}
	
	/**
	 * Execute an query sql, and you need to give a result handler to handle the result set
	 * 
	 * @param sql the sql you want to execute
	 * @param params parameters in this sql
	 * @param resultHandler a ResultHandler object, used to handle the result set, cannot be null
	 * @return an object based on your resultHandler.hadle()
	 * @throws SQLException
	 */
	public static Object query(String sql, Object[] params, ResultHandler resultHandler) throws SQLException {
		final Connection connection = getConnection();
		final PreparedStatement preparedStatement = connection.prepareStatement(sql);
		if (null != params) {
			for (int i = 0; i < params.length; ++i) {
				preparedStatement.setObject(i + 1, params[i]);
			}
		}
		/* try-with-resources */
		try (connection; preparedStatement; ResultSet resultSet = preparedStatement.executeQuery()) {
			return resultHandler.handle(resultSet);
		}
	}
	
	/**
	 * Execute an update sql
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int update(String sql, Object[] params) throws SQLException {
		Connection connection = getConnection();
		final PreparedStatement preparedStatement = connection.prepareStatement(sql);
		if (null != params) {
			for (int i = 0; i < params.length; ++i) {
				preparedStatement.setObject(i + 1, params[i]);
			}
		}
		/* try-with-resources */
		try (connection; preparedStatement) {
			return preparedStatement.executeUpdate();
		}
	}

}
