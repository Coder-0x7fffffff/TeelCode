package org.oj.dao;

import java.sql.SQLException;

public interface IUserProblemDao {

	public int findStateByUidAndPid(String uid, int pid) throws SQLException;

	public boolean insertUserProblem(String uid, int pid, int pstate) throws SQLException;

	public boolean updateUserProblem(String uid, int pid, int pstate) throws SQLException;
	
}
