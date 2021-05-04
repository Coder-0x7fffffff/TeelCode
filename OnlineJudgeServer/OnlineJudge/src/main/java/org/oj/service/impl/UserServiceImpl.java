package org.oj.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.oj.dao.IUserProblemDao;
import org.oj.dao.impl.UserProblemDaoImpl;
import org.oj.entity.UserProblem;
import org.oj.service.IUserService;

public class UserServiceImpl implements IUserService {

	IUserProblemDao userRecordDao = new UserProblemDaoImpl();
	
	@Override
	public List<UserProblem> getRecord(String id) throws SQLException {
		return userRecordDao.findUserProblem(id);
	}

}
