package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IProblemDao;
import org.oj.entity.Problem;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class ProblemDaoImpl implements IProblemDao {

	private static class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<Problem> problemList = new ArrayList<Problem>();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				int difficulty = resultSet.getInt(3);
				int pass = resultSet.getInt(4);
				int submit = resultSet.getInt(5);
				String dscp = resultSet.getString(6);
				String inputs = resultSet.getString(7);
				String outputs = resultSet.getString(8);
				problemList.add(new Problem(id, name, difficulty, pass, submit, dscp, inputs, outputs));
			}
			return problemList;
		}
	};
	
	private static final ResultHandler SELECT_RESULT_HANDLER = new SelectResultHandler();
	
	@Override
	public Problem findProblemById(int id) throws SQLException {
		String sql = "SELECT * FROM Problem WHERE p_id=?";
		Object[] params = { id };
		@SuppressWarnings("unchecked")
		List<Problem> problemList = (List<Problem>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
		return problemList.isEmpty() ? null : problemList.get(0);
	}

//	difficulty	# 0代表查询所有题目
//	class		# 0代表查询所有题目
//	status		# 0代表未完成，1代表完成，-1代表所有题目
	@SuppressWarnings("unchecked")
	@Override
	public List<Problem> findProblems(
			String uid, int page, int pageSize, int difficulty, int classification, int status) throws SQLException {
		/* Unfinished */
//		"SELECT * FROM Classification WHERE c_id=?"; // classification
//		"SELECT * FROM UserProblem WHERE u_id=? AND p_state=?"; // uid, status
//		"SELECT * FROM Problem WHERE p_difficulty=? LIMIT ?, ?"; // difficulty, (page-1)*pageSize, pageSize
		StringBuffer sqlStringBuffer = new StringBuffer();
		List<Object> paramsList = new ArrayList<Object>();
		sqlStringBuffer.append("SELECT * FROM Problem");
		boolean flag = false;
		if (0 != difficulty) {
			if (!flag) {
				sqlStringBuffer.append(" WHERE");
			}
			else {
				sqlStringBuffer.append(" AND");
			}
			sqlStringBuffer.append(" p_difficulty=?");
			paramsList.add(difficulty);
		}
		if (0 != classification) {
			if (!flag) {
				sqlStringBuffer.append(" WHERE");
			}
			else {
				sqlStringBuffer.append(" AND");
			}
			sqlStringBuffer.append(" p_id IN(SELECT p_id FROM Classification WHERE c_id=?)");
			paramsList.add(classification);
		}
		if (0 != status) {
			if (!flag) {
				sqlStringBuffer.append(" WHERE");
			}
			else {
				sqlStringBuffer.append(" AND");
			}
			sqlStringBuffer.append(" p_id IN(SELECT p_id FROM UserProblem WHERE u_id=? AND p_state=?)");
			paramsList.add(uid);
			paramsList.add(status);
		}
		sqlStringBuffer.append(" LIMIT ?, ?");
		paramsList.add((page - 1) * pageSize);
		paramsList.add(pageSize);
		return (List<Problem>) DBUtil.query(sqlStringBuffer.toString(), paramsList.toArray(), SELECT_RESULT_HANDLER);
	}
	
	@Override
	public boolean insertProblem(int id, String name, int difficulty, int pass, int submit, String dscp, String inputs,
			String outputs) throws SQLException {
		Problem problem = findProblemById(id);
		if (null == problem) {
			String sql = "INSERT INTO Problem VALUES(?,?,?,?,?,?,?,?)";
			Object[] params = { id, name, difficulty, pass, submit, dscp, inputs, outputs };
			return 1 == DBUtil.update(sql, params);
		}
		return false;
	}

	@Override
	public boolean deleteProblem(int id) throws SQLException {
		return false;
	}

	@Override
	public boolean updateProblem(int id, String name, int difficulty, int pass, int submit, String dscp, String inputs,
			String outputs) throws SQLException {
		return false;
	}

}
