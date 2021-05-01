package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.ICommentsDao;
import org.oj.entity.Comments;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class CommentsDaoImpl implements ICommentsDao {

	private static class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<Comments> commentsList = new ArrayList<Comments>();
			while (resultSet.next()) {
				int cid = resultSet.getInt(1);
				String uid = resultSet.getString(2);
				int pid = resultSet.getInt(3);
				int cfa = resultSet.getInt(4);
				String cdetails = resultSet.getString(5);
				java.sql.Date ctime = resultSet.getDate(6);
				commentsList.add(new Comments(cid, uid, pid, cfa, cdetails, ctime));
			}
			return commentsList;
		}
	}
	
	private static final ResultHandler SELECT_RESULT_HANDLER = new SelectResultHandler();
	
	@SuppressWarnings("unchecked")
	public List<Comments> getCommentsByPid(int pid, int page, int pageSize) throws SQLException {
		String sql = "SELECT * FROM Comments WHERE p_id=? LIMIT page, pageSize";
		Object[] params = { pid, (page - 1) * pageSize, pageSize };
		return (List<Comments>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}
	
	@SuppressWarnings("unchecked")
	public List<Comments> getCommentsByPidAndCid(int pid, int cid) throws SQLException {
		String sql = "SELECT * FROM Comments WHERE p_id=? AND c_fa=?";
		Object[] params = { pid, cid };
		return (List<Comments>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}
	
}
