package org.oj.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.ICommentsDao;
import org.oj.dao.impl.CommentsDaoImpl;
import org.oj.entity.Comments;
import org.oj.model.CommentsModel;
import org.oj.service.ICommentsService;

public class CommentsServiceImpl implements ICommentsService {

	ICommentsDao commentsDao = new CommentsDaoImpl();
	
	public List<CommentsModel> getComments(int pid, int page, int pageSize) throws SQLException {
		List<CommentsModel> commentsModelList = new ArrayList<CommentsModel>();
		List<Comments> commentsList = commentsDao.getCommentsByPid(pid, page, pageSize);
		for (Comments comments : commentsList) {
			List<Comments> childCommentsList = commentsDao.getChildComments(comments.getCid());
			commentsModelList.add(new CommentsModel(comments, childCommentsList));
		}
		return commentsModelList;
	}
	
	public int addComment(String uid, int pid, int cfaid, String ruid, String detail, String time) throws SQLException, ParseException {
		return commentsDao.insertComment(uid, pid, cfaid, ruid, detail, time);
	}
	
}
