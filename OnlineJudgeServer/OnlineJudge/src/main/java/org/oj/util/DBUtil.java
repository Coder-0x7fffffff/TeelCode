package org.oj.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DBUtil {
	
	public abstract interface ResultHandler {
		public Object handle(ResultSet resultSet) throws SQLException;
	}
	
	private DBUtil() {}
	
	private static DataSource dataSource;

	/* load driver */
	static {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("druid.properties"));
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
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
		/* try-with-resources */
		try (Connection connection = getConnection(); 
				PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			if (null != params) {
				for (int i = 0; i < params.length; ++i) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return resultHandler.handle(resultSet);
			}
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
		/* try-with-resources */
		try (Connection connection = getConnection(); 
				PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			if (null != params) {
				for (int i = 0; i < params.length; ++i) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}
			return preparedStatement.executeUpdate();
		}
	}

}
