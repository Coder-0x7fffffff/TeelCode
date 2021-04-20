package org.oj.service;

import java.sql.SQLException;

public interface INewUserService {
	
	public boolean newUser(String id, String name, int sex, String dscp) throws SQLException;
	
}
