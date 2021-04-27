package org.oj.service;

import java.sql.SQLException;

public interface IAlterPasswordService {

	public boolean verify(String id, String answer) throws SQLException;
	
	public boolean alter(String id, String pwd) throws SQLException;
	
}
