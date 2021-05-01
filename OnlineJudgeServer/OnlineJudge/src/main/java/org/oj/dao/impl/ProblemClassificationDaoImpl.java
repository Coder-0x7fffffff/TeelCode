package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IProblemClassificationDao;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class ProblemClassificationDaoImpl implements IProblemClassificationDao {

	private static class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<Integer> lists = new ArrayList<Integer>();
			while (resultSet.next()) {
				lists.add(resultSet.getInt(1));	
			}
			return lists;
		}
	}
	
	private static final ResultHandler SELECT_RESULT_HANDLER = new SelectResultHandler();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findCidsByPid(int pid) throws SQLException {
		String sql = "SELECT c_id FROM ProblemClassification WHERE p_id=?";
		Object[] params = { pid };
		return (List<Integer>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findPidsByCid(int cid) throws SQLException {
		String sql = "SELECT p_id FROM ProblemClassification WHERE c_id=?";
		Object[] params = { cid };
		return (List<Integer>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}

	@Override
	public boolean insertProblemClassification(int pid, int cid) throws SQLException {
		String sql = "INSERT INTO ProblemClassification VALUES(?,?)";
		Object[] params = { pid, cid };
		return 1 == DBUtil.update(sql, params);
	}
	
}
