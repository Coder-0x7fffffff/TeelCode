package org.oj.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IOJUserDao;
import org.oj.dao.IProblemDao;
import org.oj.dao.IUserProblemDao;
import org.oj.dao.impl.OJUserDaoImpl;
import org.oj.dao.impl.ProblemDaoImpl;
import org.oj.dao.impl.UserProblemDaoImpl;
import org.oj.entity.OJUser;
import org.oj.entity.Problem;
import org.oj.entity.UserProblem;
import org.oj.model.UserRecordModel;
import org.oj.service.IUserService;

public class UserServiceImpl implements IUserService {

	IUserProblemDao userRecordDao = new UserProblemDaoImpl();
	IOJUserDao ojuserDao = new OJUserDaoImpl();
	IProblemDao problemDao = new ProblemDaoImpl();
	
	@Override
	public List<UserRecordModel> getRecord(String id) throws SQLException {
		List<UserProblem> userProblemList = userRecordDao.findUserProblem(id);
		List<UserRecordModel> userRecordModelList = new ArrayList<UserRecordModel>();
		for (UserProblem userProblem : userProblemList) {
			int pid = userProblem.getPid();
			String uid = userProblem.getUid();
			Problem problem = problemDao.findProblemById(pid);
			OJUser ojuser = ojuserDao.findOJUserById(uid);
			userRecordModelList.add(new UserRecordModel(uid, ojuser.getUname(), pid, problem.getPname(), userProblem.getPstate()));
		}
		return userRecordModelList;
	}

}
