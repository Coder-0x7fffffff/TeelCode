package org.oj.service;

import java.sql.SQLException;
import java.util.List;

import org.oj.entity.Classification;
import org.oj.model.ProblemWithClassification;

public interface IProblemService {

	public List<ProblemWithClassification> all(int page, int pageSize) throws SQLException;

	public ProblemWithClassification getProblem(int id) throws SQLException;

	public boolean addProblem(int id, String name, int difficulty, int pass, int submit, String dscp, String inputs,
			String outputs, List<Classification> classificationList) throws SQLException;
	
	public boolean addClassification(int id, String name) throws SQLException;
	
	public Classification getClassification(String name) throws SQLException;
	
}
