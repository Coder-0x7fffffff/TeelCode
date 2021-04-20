package org.oj.service;

import java.sql.SQLException;

public interface IUpdateUserService {

	public boolean updateUser(String id, String name, int sex, String dscp) throws SQLException;
	
}
