package org.oj.service.impl;

import org.oj.dao.ICommentsDao;
import org.oj.dao.impl.CommentsDaoImpl;
import org.oj.service.ICommentsService;

public class CommentsServiceImpl implements ICommentsService {

	ICommentsDao commentsDao = new CommentsDaoImpl();
	
	
	
}
