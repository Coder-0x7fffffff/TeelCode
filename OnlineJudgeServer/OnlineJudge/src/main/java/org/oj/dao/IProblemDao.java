package org.oj.dao;

import java.sql.SQLException;
import java.util.List;

import org.oj.entity.Problem;

public interface IProblemDao {

	public Problem findProblemById(int id) throws SQLException;
	
	public List<Problem> findProblems(int page, int pageSize) throws SQLException;
	
	public boolean insertProblem(int id, String name, int difficulty, int pass, int submit, String dscp, String inputs,
			String outputs) throws SQLException;
	
	public boolean deleteProblem(int id) throws SQLException;
	
	public boolean updateProblem(int id, String name, int difficulty, int pass, int submit, String dscp, String inputs,
			String outputs) throws SQLException;
	
}