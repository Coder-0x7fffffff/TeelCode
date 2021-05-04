package org.oj.service.impl;

import java.sql.SQLException;

import org.oj.dao.IRecordDao;
import org.oj.dao.IUserProblemDao;
import org.oj.dao.impl.RecordDaoImpl;
import org.oj.dao.impl.UserProblemDaoImpl;
import org.oj.service.ISubmitService;

public class SubmitServiceImpl implements ISubmitService {

	IRecordDao recordDao = new RecordDaoImpl();
	IUserProblemDao userProblemDao = new UserProblemDaoImpl();
	
	public boolean submit(int pid, String uid, int pstate, java.sql.Date time,
			int timeUsage, int memUsage, String code, int codeType, String resultInfo) throws SQLException {
		int curState = userProblemDao.findStateByUidAndPid(uid, pid);
		/* first commit */
		if (-1 == curState) {
			userProblemDao.insertUserProblem(uid, pid, pstate);
		} else {
			userProblemDao.updateUserProblem(uid, pid, Math.max(curState, pstate));
		}
		return recordDao.insertRecord(pid, uid, pstate, time, timeUsage, memUsage, code, codeType, resultInfo);
	}
	
}
