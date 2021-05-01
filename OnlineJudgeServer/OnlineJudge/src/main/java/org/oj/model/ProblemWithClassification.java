package org.oj.model;

import java.util.List;

import org.oj.entity.Classification;
import org.oj.entity.Problem;

public class ProblemWithClassification {

	private Problem problem;
	private List<Classification> classificationList;
	private int passed;
	
	public ProblemWithClassification(Problem problem, List<Classification> classificationList, int passed) {
		super();
		this.problem = problem;
		this.classificationList = classificationList;
		this.passed = passed;
	}
	
	@Override
	public String toString() {
		return "ProblemWithClassification [problem=" + problem + ", classificationList=" + classificationList
				+ ", passed=" + passed + "]";
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
	
	public int getPassed() {
		return passed;
	}
	
	public void setPassed(int passed) {
		this.passed = passed;
	}
	
	
}
