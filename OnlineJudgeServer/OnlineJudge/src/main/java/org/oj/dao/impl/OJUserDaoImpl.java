package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IOJUserDao;
import org.oj.entity.OJUser;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class OJUserDaoImpl implements IOJUserDao {
	
	private static class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<OJUser> ojuserList = new ArrayList<>();
			while (resultSet.next()) {
				String uid = resultSet.getString(1);
				String uname = resultSet.getString(2);
				int usex = resultSet.getInt(3);
				String udscp = resultSet.getString(4);
				ojuserList.add(new OJUser(uid, uname, usex, udscp));	
			}
			return ojuserList;
		}
	}

	private static final ResultHandler SELECT_RESULT_HANDLER = new SelectResultHandler();
	
	@Override
	public OJUser findOJUserById(String id) throws SQLException {
		String sql = "SELECT * FROM OJUser WHERE u_id=?";
		Object[] params = { id };
		@SuppressWarnings("unchecked")
		List<OJUser> ojuserList = (List<OJUser>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
		return ojuserList.isEmpty() ? null : ojuserList.get(0);
	}

	@Override
	public int insertOJUser(String id, String name) throws SQLException {
		String sql = "INSERT INTO OJUser(u_id,u_name) VALUES(?,?)";
		Object[] params = { id, name };
		return DBUtil.update(sql, params);
	}

	@Override
	public int deleteOJUser(String id) throws SQLException {
		String sql = "DELETE FROM OJUser WHERE u_id=?";
		Object[] params = { id };
		return DBUtil.update(sql, params);
	}

	@Override
	public int updateOJUserName(String id, String name) throws SQLException {
		String sql = "UPDATE OJUser SET u_name=? WHERE u_id=?";
		Object[] params = { name, id };
		return DBUtil.update(sql, params);
	}

	@Override
	public int updateOJUserSex(String id, int sex) throws SQLException {
		String sql = "UPDATE OJUser SET u_sex=? WHERE u_id=?";
		Object[] params = { sex, id };
		return DBUtil.update(sql, params);
	}

	@Override
	public int updateOJUserDscp(String id, String dscp) throws SQLException {
		String sql = "UPDATE OJUser SET u_dscp=? WHERE u_id=?";
		Object[] params = { dscp, id };
		return DBUtil.update(sql, params);
	}

	
	
}
