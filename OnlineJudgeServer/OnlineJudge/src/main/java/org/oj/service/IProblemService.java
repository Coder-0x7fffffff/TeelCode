package org.oj.service;

import java.sql.SQLException;
import java.util.List;

import org.oj.entity.Classification;
import org.oj.model.ProblemWithClassification;

public interface IProblemService {

	public List<ProblemWithClassification> all(
			String uid, int page, int pageSize, int difficulty, int classification, int status) throws SQLException;

	public ProblemWithClassification getProblemWithClassification(String uid, int pid) throws SQLException;

	public boolean addProblem(int id, String name, int difficulty, int pass, int submit, String dscp, String inputs,
			String outputs, List<Classification> classificationList) throws SQLException;
	
	public boolean addClassification(int id, String name) throws SQLException;
	
	public Classification getClassification(String name) throws SQLException;
	
	public List<Classification> getClassifications() throws SQLException;
	
	public int getProblemCount() throws SQLException;
	
	public int getNextProblemId() throws SQLException;
	
}
