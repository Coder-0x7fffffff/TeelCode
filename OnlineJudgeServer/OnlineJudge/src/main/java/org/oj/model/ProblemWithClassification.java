package org.oj.model;

import java.util.List;

import org.oj.entity.Classification;
import org.oj.entity.Problem;

public class ProblemWithClassification {

	private Problem problem;
	private List<Classification> classificationList;
	
	public ProblemWithClassification(Problem problem, List<Classification> classificationList) {
		super();
		this.problem = problem;
		this.classificationList = classificationList;
	}
	
	@Override
	public String toString() {
		return "ProblemWithClassification [problem=" + problem + ", classificationList=" + classificationList + "]";
	}
	
	public Problem getProblem() {
		return problem;
	}
	
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	public List<Classification> getClassificationList() {
		return classificationList;
	}
	
	public void setClassificationList(List<Classification> classificationList) {
		this.classificationList = classificationList;
	}
	
}
