package org.oj.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface IRegisterService {

	public boolean register(String id, String pwd, String question, String answer) throws SQLException, NoSuchAlgorithmException;
	
}
