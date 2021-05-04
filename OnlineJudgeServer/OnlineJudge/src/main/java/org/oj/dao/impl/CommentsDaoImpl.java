package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.ICommentsDao;
import org.oj.entity.Comments;
import org.oj.util.DBUtil;
import org.oj.util.DateUtil;
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
				String cfauid = resultSet.getString(5);
				String cdetails = resultSet.getString(6);
				java.sql.Date ctime = resultSet.getDate(7);
				commentsList.add(new Comments(cid, uid, pid, cfa, cfauid, cdetails, ctime));
			}
			return commentsList;
		}
	}
	
	private static class InsertResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			while (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return -1;
		}
	}
	
	private static final ResultHandler SELECT_RESULT_HANDLER = new SelectResultHandler();
	private static final InsertResultHandler INSERT_RESULT_HANDLER = new InsertResultHandler();

	@Override
	@SuppressWarnings("unchecked")
	public List<Comments> getCommentsByPid(int pid, int page, int pageSize) throws SQLException {
		String sql = "SELECT * FROM Comments WHERE p_id=? LIMIT ?, ?";
		Object[] params = { pid, (page - 1) * pageSize, pageSize };
		return (List<Comments>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Comments> getChildComments(int cfaid) throws SQLException {
		String sql = "SELECT * FROM Comments WHERE c_fa=?";
		Object[] params = { cfaid };
		return (List<Comments>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}
	
	@Override
	public int insertComment(String uid, int pid, int cfaid, String ruid, String detail, String time) throws SQLException, ParseException {
		return (int) DBUtil.updateAndQuery(
				"INSERT INTO Comments(u_id,p_id,c_fa,c_fa_uid,c_details,c_time) VALUES(?,?,?,?,?,?)",
				new Object[] { uid, pid, cfaid, ruid, detail, DateUtil.string2SQLDate(time) }, 
				"SELECT LAST_INSERT_ID()", 
				null, 
				INSERT_RESULT_HANDLER);
	}
	
}
