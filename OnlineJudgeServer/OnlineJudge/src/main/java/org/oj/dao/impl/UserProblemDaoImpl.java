package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IUserProblemDao;
import org.oj.entity.UserProblem;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class UserProblemDaoImpl implements IUserProblemDao {
	
	private static class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<UserProblem> lists = new ArrayList<UserProblem>();
			while (resultSet.next()) {
				String uid = resultSet.getString(1);
				int pid = resultSet.getInt(2);
				int pstate = resultSet.getInt(3);
				lists.add(new UserProblem(uid, pid, pstate));
			}
			return lists;
		}
	}
	
	private static final ResultHandler SELECT_RESULT_HANDLER = new SelectResultHandler();

	@SuppressWarnings("unchecked")
	public int findStateByUidAndPid(String uid, int pid) throws SQLException {
		String sql = "SELECT * FROM UserProblem WHERE u_id=? and p_id=?";
		Object[] params = { uid, pid };
		return ((List<UserProblem>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER)).get(0).getPstate();
	}
	
}
