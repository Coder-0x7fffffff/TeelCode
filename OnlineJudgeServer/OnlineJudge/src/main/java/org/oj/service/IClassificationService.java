package org.oj.service;

import java.sql.SQLException;

public interface IClassificationService {

	public boolean alterClassificationName(int id, String name) throws SQLException;
	
}
