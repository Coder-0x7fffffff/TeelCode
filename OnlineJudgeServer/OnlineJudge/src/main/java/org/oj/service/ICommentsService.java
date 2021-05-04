package org.oj.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.oj.model.CommentsModel;

public interface ICommentsService {
	
	public List<CommentsModel> getComments(int pid, int page, int pageSize) throws SQLException;
	
	public int addComment(String uid, int pid, int cfaid, String ruid, String detail, String time) throws SQLException, ParseException;
	
}
