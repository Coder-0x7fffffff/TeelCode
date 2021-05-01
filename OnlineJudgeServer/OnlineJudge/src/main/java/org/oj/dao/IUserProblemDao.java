package org.oj.dao;

import java.sql.SQLException;

public interface IUserProblemDao {

	public int findStateByUidAndPid(String uid, int pid) throws SQLException;
	
}
