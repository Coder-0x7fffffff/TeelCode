package org.oj.dao;

import java.sql.SQLException;
import java.util.List;

import org.oj.entity.Comments;

public interface ICommentsDao {

	public List<Comments> getCommentsByPid(int pid, int page, int pageSize) throws SQLException;
	
	public List<Comments> getCommentsByPidAndCid(int pid, int cid) throws SQLException;
	
}
