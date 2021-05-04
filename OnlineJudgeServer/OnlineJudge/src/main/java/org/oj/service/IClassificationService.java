package org.oj.service;

import java.sql.SQLException;
import java.util.List;

import org.oj.entity.Classification;

public interface IClassificationService {

	public boolean alterClassificationName(int id, String name) throws SQLException;
	
	public boolean deleteClassification(int id) throws SQLException;
	
	public boolean addClassification(int id, String name) throws SQLException;
	
	public Classification getClassification(String name) throws SQLException;
	
	public List<Classification> getClassifications() throws SQLException;
}
