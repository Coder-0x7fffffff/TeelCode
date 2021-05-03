package org.oj.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface ILoginService {

	public boolean login(String id, String pwd) throws SQLException, NoSuchAlgorithmException;
	
}
