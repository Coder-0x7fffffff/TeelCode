package org.oj.dao;

import java.sql.SQLException;
import java.util.List;

public interface IProblemClassificationDao {

	public List<Integer> findCidsByPid(int pid) throws SQLException;
	
	public List<Integer> findPidsByCid(int cid) throws SQLException;
	
	public boolean insertProblemClassification(int pid, int cid) throws SQLException;
	
}
