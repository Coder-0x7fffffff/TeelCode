package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.ILoginDao;
import org.oj.entity.Login;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class LoginDaoImpl implements ILoginDao {
	
	private class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<Login> loginList = new ArrayList<Login>();
			while (resultSet.next()) {
				String uid = resultSet.getString(1);
				String upwd = resultSet.getString(2);
				String uproblem = resultSet.getString(3);
				String uanswer = resultSet.getString(4);
				loginList.add(new Login(uid, upwd, uproblem, uanswer));
			}
			return loginList;
		}
	}

	@Override
	public Login findLoginById(String id) throws SQLException {
		String sql = "SELECT * FROM Login WHERE u_id=?";
		Object[] params = { id };
		@SuppressWarnings("unchecked")
		List<Login> loginList = (List<Login>) DBUtil.query(sql, params, new SelectResultHandler()); 
		return loginList.isEmpty() ? null : loginList.get(0);
	}
	
	@Override
	public int insertLogin(String id, String pwd, String problem, String answer) throws SQLException {
		String sql = "INSERT INTO Login VALUES(?,?,?,?)";
		Object[] params = { id, pwd, problem, answer };
		return DBUtil.update(sql, params);
	}

	@Override
	public int deleteLogin(String id) throws SQLException {
		String sql = "DELETE FROM Login WHERE u_id=?";
		Object[] params = { id };
		return DBUtil.update(sql, params);
	}

}
