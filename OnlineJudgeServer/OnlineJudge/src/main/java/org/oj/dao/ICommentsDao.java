package org.oj.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.oj.entity.Comments;

public interface ICommentsDao {

	public List<Comments> getCommentsByPid(int pid, int page, int pageSize) throws SQLException;
	
	public List<Comments> getChildComments(int cfaid) throws SQLException;
	
	public int insertComment(String uid, int pid, int cfaid, String ruid, String detail, String time) throws SQLException, ParseException;
	
}
