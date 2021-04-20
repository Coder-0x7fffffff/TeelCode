package org.oj.dao;

import java.sql.SQLException;

import org.oj.entity.Login;

public interface ILoginDao {

	public Login findLoginById(String id) throws SQLException;
	
	public int insertLogin(String id, String pwd, String question, String answer) throws SQLException;
	
	public int deleteLogin(String id) throws SQLException;

	// update
	
}
