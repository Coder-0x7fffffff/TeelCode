package org.oj.dao;

import java.sql.SQLException;

import org.oj.entity.Classification;

public interface IClassificationDao {

	public Classification findClassificationById(int id) throws SQLException;
	
	public Classification findClassificationByName(String name) throws SQLException;
	
	public boolean insertClassification(int id, String name) throws SQLException;
	
	public boolean deleteClassification(int id) throws SQLException;
	
	public boolean deleteClassification(String name) throws SQLException;
	
	public boolean updateClassification(int id, String name) throws SQLException;
	
}
