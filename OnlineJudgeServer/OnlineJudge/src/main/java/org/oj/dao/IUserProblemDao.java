package org.oj.dao;

import java.sql.SQLException;
import java.util.List;

import org.oj.entity.UserProblem;

public interface IUserProblemDao {

	public int getUserProblemCount(String uid) throws SQLException;
	
	public int findStateByUidAndPid(String uid, int pid) throws SQLException;
	
	public List<UserProblem> findUserProblem(String uid, int page, int pageSize) throws SQLException;

	public boolean insertUserProblem(String uid, int pid, int pstate) throws SQLException;

	public boolean updateUserProblem(String uid, int pid, int pstate) throws SQLException;
	
}
