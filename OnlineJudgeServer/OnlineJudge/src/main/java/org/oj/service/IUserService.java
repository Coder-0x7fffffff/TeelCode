package org.oj.service;

import java.sql.SQLException;
import java.util.List;

import org.oj.entity.UserProblem;

public interface IUserService {

	public List<UserProblem> getRecord(String id) throws SQLException;
	
}
